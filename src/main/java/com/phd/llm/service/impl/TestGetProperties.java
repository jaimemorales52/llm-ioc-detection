package com.phd.llm.service.impl;

import com.phd.llm.properties.ConfigLoader;

public class TestGetProperties {

	public static void main(String[] args) {
		// Leer propiedades
		String appName = ConfigLoader.getProperty("gemini.password");

		// Imprimir las variables
		System.out.println("Nombre de la aplicación: " + appName);
	}
}
