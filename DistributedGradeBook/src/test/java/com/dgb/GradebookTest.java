package com.dgb;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;


public class GradebookTest {

	@Test
	public void testSetTitle() throws GradeBookTitleInvalidException {
		Gradebook book = new Gradebook();
		book.setTitle("Comp Science");
		assertEquals("Comp Science", book.getTitle());
	}
	@Test(expected=GradeBookTitleInvalidException.class)
	public void testSetTitleThrowsException()  {
		Gradebook book = new Gradebook();
		boolean error = false;
		try {
			book.setTitle(" Comp Science");
			
		} catch (GradeBookTitleInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = true;
		}
		assertEquals(true, error);
	}

}
