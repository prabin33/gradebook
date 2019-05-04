package com.dgb;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="student")
public class Student implements Serializable{

	private String name;
	private String grade;
	public Student() {
	}
	
	
	public Student(String name, String grade) {
		this.name = name;
		this.grade = grade;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + "]";
	}
	
}
