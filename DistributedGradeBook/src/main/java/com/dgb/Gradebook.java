package com.dgb;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement(name="gradebook")
public class Gradebook {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	private String title, secondaryHost;
	private boolean isPrimaryServer;
	@XmlElement
	private ArrayList<Student> students = new ArrayList<Student>();

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws GradeBookTitleInvalidException {
		if(title.startsWith(" "))
			throw new GradeBookTitleInvalidException("Title cannot start with whitespace");
		this.title = title;
	}
	@JsonIgnore
	public boolean isPrimaryServer() {
		return isPrimaryServer;
	}

	
	public void setPrimaryServer(boolean isPrimaryServer) {
		this.isPrimaryServer = isPrimaryServer;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}

	@JsonIgnore
	public List<Student> getStudents()
	{
		return this.students;
	}

	public void addStudent(Student student)
	{
		students.add(student);
	}
	public void removeStudent(String name) {
		for (int i = 0; i < this.students.size(); ++i) {
			if (this.students.get(i).getName().equals(name)) {
				this.students.remove(i);
				break;
			}
		}
	}
	public Student getStudent(String name) {
		for (Student s : this.students) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	@JsonIgnore
	public String getSecondaryHost() {
		return secondaryHost;
	}
	public void setSecondaryHost(String value) {
		secondaryHost = value;
	}

	@Override
	public String toString() {
		return "Gradebook [id=" + id + ", title=" + title + ", secondaryHost=" + secondaryHost + ", isPrimaryServer="
				+ isPrimaryServer + ", students=" + students + "]";
	}

}