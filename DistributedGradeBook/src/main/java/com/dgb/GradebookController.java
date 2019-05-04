package com.dgb;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GradebookController {
	
	@Autowired
	GradebookService gradebookService;

	@Autowired
	ApplicationContextProvider context;
	
	@RequestMapping(path = "/gradebook/{name}", method = RequestMethod.POST)
	public Integer createGradebook(@PathVariable String name) throws GradeBookExistsException,GradeBookTitleInvalidException, InvalidAccessException 
	{
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8090)
			throw new InvalidAccessException("Secondary Server cannot create grades");
		try{
			return gradebookService.createGradebook(name, true).getId();
		} catch (GradeBookExistsException e) {
			throw e;
		} catch (GradeBookTitleInvalidException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook", method = RequestMethod.GET,produces= {"application/xml", "text/xml;charset=utf-8" })
	public List<Gradebook> getGradebooks()
	{
		//get all gradebooks on this server, including primary and secondary copies
		return gradebookService.getGradebooks();
	}
	
	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.POST)
	public void createSecondary(@PathVariable Integer id) throws GradeBookExistsOnServer, InvalidAccessException, InvalidAccessExceptionForPrimaryServer
	{
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8080)
			throw new InvalidAccessExceptionForPrimaryServer("Primary Server cannot create grades on secondary");
		try {
			gradebookService.createSecondaryGradebook(id, DistributedGradeBookApplication.secondary_host);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.POST)
	public void createStudent(@PathVariable Integer id, @PathVariable String name,
			@PathVariable String grade) throws GradeInvalidException, StudentExistsException, InvalidAccessException
	{
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8090)
			throw new InvalidAccessException("Cannot create on secondary server");
		try {
			gradebookService.createStudent(id, name, grade);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (GradeInvalidException e) {
			throw e;
		} catch (StudentExistsException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}/grade/{grade}", method = RequestMethod.PUT)
	public void updateStudent(@PathVariable Integer id, @PathVariable String name,
		@PathVariable String grade) throws GradeInvalidException, StudentNotFoundException, InvalidAccessException
	{
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8090)
			throw new InvalidAccessException("Cannot create on secondary server");
		try {
			gradebookService.updateStudent(id, name, grade);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (GradeInvalidException e) {
			throw e;
		} catch (StudentNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public List<Student> getStudents(@PathVariable Integer id)
	{
		try {
			return gradebookService.getStudents(id);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.GET,
			produces={"text/xml;charset=utf-8"})
	public Student getStudent(@PathVariable Integer id, @PathVariable String name) throws StudentNotFoundException
	{
		//get student information, can be done on primary or secondary copy
		try {
			return gradebookService.getStudent(id, name);
		} catch (GradebookNotFoundException e) {
			throw e;
		} catch (StudentNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/gradebook/{id}", method = RequestMethod.DELETE)
	public void deleteGradebook(@PathVariable Integer id) throws InvalidAccessException
	{
		//delete gradebook, must be done on primary sever, deletion also deletes on secondary copies
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8090)
			throw new InvalidAccessException("Secondary Server cannot delete book");
		try{
			gradebookService.deleteGradebook(id);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}

	@RequestMapping(path = "/secondary/{id}", method = RequestMethod.DELETE)
	public void deleteSecondary(@PathVariable Integer id) throws InvalidAccessExceptionForPrimaryServer
	{
		//delete secondary, must be done on secondary server, does not affect primary copy
		// gradebook := gradebookService.getGradebook(id);
		// if gradebook.isPrimary():
		//		throw error
		// gradebookService.deleteGradebook(id);
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))==8080)
			throw new InvalidAccessExceptionForPrimaryServer("Primary Server cannot delete gradebook on secondary");
		this.gradebookService.deleteSecondary(id);
	}

	@RequestMapping(path = "/gradebook/{id}/student/{name}", method = RequestMethod.DELETE)
	public void deleteStudent(@PathVariable Integer id, @PathVariable String name)
	{
		try {
			gradebookService.deleteStudent(id, name);
		} catch (GradebookNotFoundException e) {
			throw e;
		}
	}
}