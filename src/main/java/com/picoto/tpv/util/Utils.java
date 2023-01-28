package com.picoto.tpv.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
	
	public static boolean esVacio(String str) {
		return str == null || str.trim().isEmpty();
	}
	

	public static void debug(String msg) {
		System.out.println(msg);
	}


	public static boolean opcionActivada(String op) {
		return !esVacio(op) && "S".equalsIgnoreCase(op);
	}


	public static int getDia() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date()); 
		return cal.get(Calendar.DAY_OF_YEAR);
	}
}
