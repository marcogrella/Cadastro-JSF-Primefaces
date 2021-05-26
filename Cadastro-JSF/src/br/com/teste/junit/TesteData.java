package br.com.teste.junit;


import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

public class TesteData {

	
	@Test
	public void testData() {
		
		try {
		/* executa o método */
		assertEquals("21052021", DateUtils.getDateAtualReportName());
		assertEquals("'2021-05-21'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));
		assertEquals("2021-05-21", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));
		
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
}
