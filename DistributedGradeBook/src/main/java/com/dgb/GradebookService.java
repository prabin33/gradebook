package com.dgb;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GradebookService extends RestTemplate {

	public static final String PROTOCOL = "http";

	@Autowired
	public GradebookRepository gradebookRepository;

	public Gradebook createGradebook (String gradebookName, Boolean isPrimary) throws GradeBookExistsException,GradeBookTitleInvalidException {
		for (Gradebook gradebook : gradebookRepository.findAll()) {
			if (gradebook.getTitle().equals(gradebookName)) {
				throw new GradeBookExistsException("Gradebook Id-" + gradebook.getId());
			}
		}
		Gradebook gradebook = new Gradebook();
		gradebook.setTitle(gradebookName);
		gradebook.setPrimaryServer(isPrimary);
		gradebookRepository.save(gradebook);
		return gradebook;
	}

	public void createSecondaryGradebook (Integer gradebookId, String secondaryHost) throws GradebookNotFoundException, GradeBookExistsOnServer {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		if(gradebook.getSecondaryHost() != null)
		if(gradebook.getSecondaryHost().equals(DistributedGradeBookApplication.secondary_host))
			throw new GradeBookExistsOnServer("already a copy exists on server");
		gradebook.setSecondaryHost(secondaryHost);
		gradebook.setPrimaryServer(false);
		try{
			this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() + "/gradebook/" + gradebook.getId(), gradebook);
		} catch (RestClientException e) {
			logger.error(e);
		}

		gradebookRepository.save(gradebook);
	}
	
	public List<Gradebook> getGradebooks () {
		List<Gradebook> books = gradebookRepository.findAll();
		return books;
	}

	public List<Student> getStudents (Integer gradebookId) {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();
		return gradebook.getStudents();
	}


	// POST
	public void createStudent (Integer gradebookId, String studentName, String studentGrade) throws GradeInvalidException, StudentExistsException {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		if (checkGradeInput(studentGrade) == false) throw new GradeInvalidException("grade-" + studentGrade);
		Gradebook gradebook = opt.get();
		if(!gradebook.getStudents().isEmpty()) {
			for (Student temp : gradebook.getStudents())
			{
				if (temp.getName().equals(studentName)) throw new StudentExistsException("grade-" + studentGrade);
			}
		}
		Student student = new Student();
		student.setName(studentName);
		student.setGrade(studentGrade);
		gradebook.addStudent(student);
		gradebookRepository.save(gradebook);

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}

		this.postForLocation(PROTOCOL + "://" + gradebook.getSecondaryHost() + 
				"/gradebook/" + gradebook.getId() + 
				"/student/" + studentName + "/grade/" + studentGrade, gradebook);
	}


	//PUT
	public void updateStudent (Integer gradebookId, String studentName, String studentGrade) throws GradeInvalidException, StudentNotFoundException  {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		if (checkGradeInput(studentGrade) == false) {
			throw new GradeInvalidException("grade-" + studentGrade);
		}
		Gradebook gradebook = opt.get();

		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		}
		gradebook.getStudent(studentName).setGrade(studentGrade);
		gradebookRepository.save(gradebook);

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}

		// Save to secondary
		this.put(PROTOCOL + "://" + secondaryHost +
				"/gradebook/" + gradebook.getId() + 
				"/student/" + studentName + 
				"/grade/" + studentGrade,
				gradebook);
	}

	public Student getStudent(Integer gradebookId, String studentName) throws GradebookNotFoundException, StudentNotFoundException
	{
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		Student student = gradebook.getStudent(studentName);
		if (student == null) {
			throw new StudentNotFoundException("Name -" + studentName);
		} else {
			return student;
		}

	}

	public void deleteGradebook (Integer gradebookId) throws GradebookNotFoundException {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		gradebookRepository.deleteById(gradebookId);

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost + "/gradebook/" + gradebookId);
	}

	public void deleteSecondary(Integer id) 
	{
		Optional<Gradebook> opt = gradebookRepository.findById(id);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + id);
		}
		Gradebook gradebook = opt.get();
		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			throw new GradebookNotFoundException("Does not exists on secondary server");
		}
		gradebook.setSecondaryHost(null);
		gradebook.setPrimaryServer(true);

		this.gradebookRepository.save(gradebook);

	}
	// DELETE
	public void deleteStudent (Integer gradebookId, String studentName) throws GradebookNotFoundException {
		Optional<Gradebook> opt = gradebookRepository.findById(gradebookId);
		if (!opt.isPresent()) {
			throw new GradebookNotFoundException("Gradebook Id-" + gradebookId);
		}
		Gradebook gradebook = opt.get();

		gradebookRepository.deleteById(gradebookId);

		String secondaryHost = gradebook.getSecondaryHost();
		if (secondaryHost == null) {
			return;
		}
		this.delete(PROTOCOL + "://" + secondaryHost + 
				"/gradebook/" + gradebookId +
				"/student/" + studentName);
	}
	public boolean checkGradeInput(String grade)
	{
		if(!grade.matches("[a-dA-D][+-]?|[eE]|[fF]|[iI]|[wW]|[zZ]"))
			return false;
		return true;
	}
}