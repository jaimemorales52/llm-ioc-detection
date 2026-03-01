package com.phd.llm.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
	private static Properties properties = new Properties();

	static {
		try (FileInputStream fis = new FileInputStream("src/main/resources/keys.properties")) {
			properties.load(fis);
		} catch (IOException e) {
			System.err.println("Error al cargar el archivo de propiedades: " + e.getMessage());
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
