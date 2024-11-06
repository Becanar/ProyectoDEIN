package com.example.proyectodein.model;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Clase abstracta para gestionar las propiedades de configuración.
 * Carga las propiedades desde un archivo externo (db.properties)
 * y proporciona acceso a sus valores a través de un método estático.
 */
public abstract class Propiedades {
	private static Properties props = new Properties(); ///< Propiedades cargadas desde el archivo.

	static {
		try (FileInputStream input = new FileInputStream("db.properties")) {
			props.load(input); // Carga las propiedades desde el archivo
		} catch (Exception e) {
			e.printStackTrace(); // Manejo de excepciones durante la carga
		}
	}

	/**
	 * Obtiene el valor asociado a la clave proporcionada desde el archivo de propiedades.
	 *
	 * @param clave La clave para la cual se desea obtener el valor.
	 * @return El valor asociado a la clave, o lanza una excepción si no se encuentra.
	 * @throws RuntimeException si la clave no existe en las propiedades.
	 */
	public static String getValor(String clave) {
		String valor = props.getProperty(clave); // Recupera el valor correspondiente a la clave
		if (valor != null) {
			return valor; // Retorna el valor si se encuentra
		}
		throw new RuntimeException("El fichero de configuracion no existe"); // Lanza excepción si no se encuentra
	}
}
