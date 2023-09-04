package com.tienda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.tienda.model.Identificador;
import com.tienda.model.TipoAplicacion;
import com.tienda.model.TipoEnvase;
import com.tienda.model.TipoProducto;
import com.tienda.model.Bebida;
import com.tienda.model.Envasado;
import com.tienda.model.Limpieza;

public class Scripts {

	// Seleccionar S/N
	public static char seleccionarConfirmacion(String mensaje) {

		Scanner scann = new Scanner(System.in);
		char opcion;

		do {
			System.out.println(mensaje);
			System.out.println("[S]-->Si");
			System.out.println("[N]-->No");

			opcion = scann.next().charAt(0);
			opcion = Character.toUpperCase(opcion);
		} while (opcion != 'S' && opcion != 'N');

		return opcion;
	}

	// Seleccionar una categoría de producto
	public static TipoProducto seleccionarTipoProducto() {
		TipoProducto tipo = null;
		Scanner scann = new Scanner(System.in);

		char opcion;
		do {

			System.out.println("Seleccione el tipo de producto: ");
			System.out.println("[B]-->Bebida");
			System.out.println("[E]-->Envasado");
			System.out.println("[L]-->Limpieza");

			opcion = scann.next().charAt(0);
			opcion = Character.toUpperCase(opcion);
		} while (opcion != 'B' && opcion != 'E' && opcion != 'L');

		switch (opcion) {
		case 'B':
			tipo = TipoProducto.Bebida;
			break;
		case 'E':
			tipo = TipoProducto.Envasado;
			break;
		case 'L':
			tipo = TipoProducto.Limpieza;
			break;
		}

		return tipo;
	}

	public static String leerCodigoProducto(TipoProducto tipo) {
		Scanner scann = new Scanner(System.in);
		String codigoProducto = null;
		String codigoNumerico = null;

		switch (tipo) {
		case Bebida:
			codigoProducto = Bebida.prefijo;
			break;
		case Envasado:
			codigoProducto = Envasado.prefijo;
			break;
		case Limpieza:
			codigoProducto = Limpieza.prefijo;
			break;

		default:
			break;
		}

		do {
			System.out.println("Ingrese el código numérico");
			System.out.println("de la forma 000: ");
			codigoNumerico = scann.next();

		} while (!Identificador.validarIdentificador(codigoProducto + codigoNumerico));

		return codigoProducto + codigoNumerico;

	}
	
	// Menú para seleccionar un tipo de envase
	public static TipoEnvase seleccionarTipoEnvase() {
			TipoEnvase tipo = null;
			Scanner scann = new Scanner(System.in);

			char opcion;
			do {

				System.out.println("Seleccione el tipo de envase: ");
				System.out.println("[L]-->Lata");
				System.out.println("[E]-->Plástico");
				System.out.println("[B]-->Vidrio");

				opcion = scann.next().charAt(0);
				opcion = Character.toUpperCase(opcion);
			} while (opcion != 'L' && opcion != 'P' && opcion != 'V');

			switch (opcion) {
			case 'B':
				tipo = TipoEnvase.Lata;
				break;
			case 'E':
				tipo = TipoEnvase.Plastico;
				break;
			case 'L':
				tipo = TipoEnvase.Vidrio;
				break;
			}

			return tipo;
		}

		// Menú para seleccionar un tipo de aplicación producto limpieza
		public static TipoAplicacion seleccionarTipoAplicacion() {
			TipoAplicacion tipo = null;
			Scanner scann = new Scanner(System.in);

			char opcion;
			do {
				System.out.println("Seleccione el tipo de aplicacion: ");
				System.out.println("[C]-->Cocina");
				System.out.println("[M]-->Multiuso");
				System.out.println("[P]-->Piso");
				System.out.println("[R]-->Ropa");

				opcion = scann.next().charAt(0);
				opcion = Character.toUpperCase(opcion);
			} while (opcion != 'C' && opcion != 'M' && opcion != 'P' && opcion != 'R');

			switch (opcion) {
			case 'C':
				tipo = TipoAplicacion.Cocina;
				break;
			case 'M':
				tipo = TipoAplicacion.Multiuso;
				break;
			case 'P':
				tipo = TipoAplicacion.Piso;
				break;
			case 'R':
				tipo = TipoAplicacion.Ropa;
				break;
			}

			return tipo;
		}

		public static Date ingresarFecha(String mensaje) throws ParseException {
			Scanner scann = new Scanner(System.in);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date fecha = null;

			System.out.println(mensaje);
			String stringFecha;
			stringFecha = scann.nextLine();
			fecha = dateFormat.parse(stringFecha);

			return fecha;

		}
}
