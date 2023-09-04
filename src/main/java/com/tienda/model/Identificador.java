package com.tienda.model;

import java.util.Objects;
import java.util.regex.*;

//Clase que crea y valida el identificador de un producto.
public class Identificador {
	private String valor;

	// Verifica si el valor ingresado cumple con la norma requerida para el
	// identificador.
	public static boolean validarIdentificador(String valor) {
		String patron = "(AB|AZ|AC)\\d{3}";
		Pattern pattern = Pattern.compile(patron);
		Matcher matcher = pattern.matcher(valor);

		return matcher.matches();
	}

	// Genera el identificador del producto ya existente
	public static String generarIdentificador(String cadenaIdentificador) {

		if (cadenaIdentificador.length() == 2) {
			return cadenaIdentificador + "001";
		} else {

			String letras = cadenaIdentificador.substring(0, 2);
			int numero = Integer.parseInt(cadenaIdentificador.substring(2));
			numero += 1;

			String stringNumero = Integer.toString(numero);
			int cantDigitos = stringNumero.length();

			if (cantDigitos == 1) {
				stringNumero = "00" + stringNumero;
			} else if (cantDigitos == 2) {
				stringNumero = "0" + stringNumero;
			}

			return letras + stringNumero;
		}
	}

	public Identificador(String valor) {
		if (validarIdentificador(valor)) {
			this.valor = valor;
		} else {
			throw new IllegalArgumentException("Identificador no válido: " + valor);
		}
	}

	public String getValor() {
		return valor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identificador other = (Identificador) obj;
		return Objects.equals(valor, other.valor);
	}

}
