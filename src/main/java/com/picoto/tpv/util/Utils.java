package com.picoto.tpv.util;

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
}
