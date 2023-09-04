package com.tienda.ProyectoTiendaMaven;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.tienda.model.*;
import com.tienda.util.*;

public class App {

	private static Tienda tienda;

	// Ingresa desde un archivo csv, productos al stock.
	// Si un producto excede al stock, no es ingresado y se corta la carga.
	private static void cargarProductos(TipoProducto tipo, String rutaArchivo) {

		String csvFilePath = rutaArchivo; // Reemplaza con la ruta de tu archivo CSV
		Producto producto = null;
		Map<String, Integer> encabezadoMap = new HashMap<>();
		int contador = 0;
		int productosIngresados = 0;
		int stockInicial = tienda.getStockTotal();
		int stockMaximo = tienda.getNroMaxProdStock();

		String cadenaIdentificador = "";
		Boolean primeraLinea = true;
		Boolean stockCompleto = false;
		List<Producto> listaProductos = new ArrayList<>();

		try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {

			List<String[]> productos = csvReader.readAll();
			String[] encabezados = productos.get(0);
			for (int cont = 0; cont < encabezados.length; cont++) {
				encabezadoMap.put(encabezados[cont], cont);
			}

			for (String[] prod : productos) {

				if (primeraLinea) {
					primeraLinea = false;
					continue;
				}

				switch (tipo) {
				case Bebida:
					producto = new Bebida();
					cadenaIdentificador = Bebida.getPrefijo();
					((Bebida) producto).setEsAlcoholica(prod[encabezadoMap.get("esAlcoholica")].equals("1"));
					((Bebida) producto).setEsImportado(prod[encabezadoMap.get("esImportado")] == "1");
					((Bebida) producto).setGraduacion(Short.parseShort(prod[encabezadoMap.get("graduacion")]));
					((Bebida) producto).setCalorias(Short.parseShort(prod[encabezadoMap.get("calorias")]));
					((Bebida) producto).setFechaVencimineto(new Date(prod[encabezadoMap.get("fechaVencimiento")]));

					break;
				case Envasado:
					producto = new Envasado();
					cadenaIdentificador = Envasado.prefijo;
					((Envasado) producto).setTipoEnvase(TipoEnvase.valueOf(prod[encabezadoMap.get("tipoEnvase")]));
					((Envasado) producto).setEsImportado(prod[encabezadoMap.get("esImportado")] == "1");
					((Envasado) producto).setCalorias(Short.parseShort(prod[encabezadoMap.get("calorias")]));
					((Envasado) producto).setFechaVencimineto(new Date(prod[encabezadoMap.get("fechaVencimiento")]));

					break;
				case Limpieza:
					producto = new Limpieza();
					cadenaIdentificador = Limpieza.prefijo;
					((Limpieza) producto)
							.setTipoAplicacion(TipoAplicacion.valueOf(prod[encabezadoMap.get("tipoAplicacion")]));
					break;

				default:
					break;
				}

				producto.setTipo(tipo);
				producto.setDisponible(true);
				producto.setDescripcion(prod[encabezadoMap.get("descripcion")]);
				producto.setCantidadStock(Short.parseShort(prod[encabezadoMap.get("cantidad")]));

				String precioStr = prod[encabezadoMap.get("precioUnidad")];
				precioStr = precioStr.replace(",", ".");
				producto.setPrecioUnidad(Float.parseFloat(precioStr));

				precioStr = prod[encabezadoMap.get("costoUnidad")];
				precioStr = precioStr.replace(",", ".");
				producto.setCostoUnidad(Float.parseFloat(precioStr));

				cadenaIdentificador = cadenaIdentificador + contador;
				cadenaIdentificador = Identificador.generarIdentificador(cadenaIdentificador);
				producto.setIdentificador(new Identificador(cadenaIdentificador));

				contador++;

				// Verificar stock para agregar el producto.
				productosIngresados += Integer.parseInt(prod[encabezadoMap.get("cantidad")]);

				if (stockInicial + productosIngresados > stockMaximo) {
					stockCompleto = true;
					break;
				} else {
					listaProductos.add(producto);
				}

			}
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}

		Map<TipoProducto, List<Producto>> stockProductos = tienda.getProductos();
		stockProductos.put(tipo, listaProductos);
		tienda.setProductos(stockProductos);
		System.out.println("Stock completo: " + stockCompleto);
		if (stockCompleto) {
			System.out.println("Stock completo, no se agregaron todos los productos");
		} else {
			System.out.println("Se agregaron todos los productos del listado");
		}
	}

	// Se crea un objeto producto, según el tipo especificado.
	private static Producto crearProducto(String descripcion, TipoProducto tipo) {
		Scanner scann = new Scanner(System.in);

		Producto producto = null;
		TipoEnvase tipoEnvase = null;
		TipoAplicacion tipoAplicacion = null;
		Boolean esImportado;
		Date fechaVencimiento;
		Short calorias;
		Short graduacion;
		Boolean esAlcoholica;

		switch (tipo) {
		case Bebida:

			esAlcoholica = Scripts.seleccionarConfirmacion("Es alcoholica S/N") == 'S';

			if (esAlcoholica) {
				System.out.println("Graduación: ");
				graduacion = scann.nextShort();
			} else {
				graduacion = 0;
			}

			System.out.println("Calorías: ");
			calorias = scann.nextShort();

			esImportado = Scripts.seleccionarConfirmacion("Es importado S/N") == 'S';
			fechaVencimiento = null;
			System.out.println("Fecha de vencimineto: ");
			do {
				try {
					fechaVencimiento = Scripts.ingresarFecha("Ingrese una fecha (dd/MM/yyyy): ");
				} catch (ParseException e) {
					System.out.println("Error al cargar la fecha");
					fechaVencimiento = null;
				}

			} while (fechaVencimiento == null);

			producto = new Bebida(true, descripcion, tipo, esAlcoholica, esImportado, graduacion, calorias,
					fechaVencimiento);
			tienda.agregarProducto(producto);
			break;
		case Envasado:

			System.out.println("Tipo de envase: ");
			tipoEnvase = Scripts.seleccionarTipoEnvase();
			

			esImportado = Scripts.seleccionarConfirmacion("Es importado S/N") == 'S';
			fechaVencimiento = null;
			System.out.println("Fecha de vencimineto: ");

			do {
				try {
					fechaVencimiento = Scripts.ingresarFecha("Ingrese una fecha (dd/MM/yyyy): ");
				} catch (ParseException e) {
					System.out.println("Error al cargar la fecha");
					fechaVencimiento = null;
				}

			} while (fechaVencimiento == null);

			System.out.println("Calorías: ");
			calorias = scann.nextShort();

			producto = new Envasado(true, descripcion, tipo, tipoEnvase, esImportado, calorias, fechaVencimiento);
			tienda.agregarProducto(producto);
			break;
		case Limpieza:
			System.out.println("Tipo de aplicación");
			tipoAplicacion = Scripts.seleccionarTipoAplicacion();

			producto = new Limpieza(true, descripcion, tipo, tipoAplicacion);
			tienda.agregarProducto(producto);
			break;

		default:
			break;
		}

		return producto;
	}

	private static Boolean consultarDisponibilidadTienda(float monto, short cantidad) {
		Boolean disponible = false;

		if (tienda.getStockTotal() > cantidad) {
			System.out.println("Hay lugar en depósito");
			disponible = true;
		} else {
			System.out.println("No hay lugar en depósito");
		}

		if (tienda.getSaldoEnCaja() > monto * cantidad) {
			System.out.println("Saldo disponible para la compra");
			disponible = true;
		} else {
			System.out.println("No hay saldo disponible para la compra");
		}

		return disponible;
	}

	// Ingresa un nuevo producto a la tienda
	private static void nuevoProducto() {

		Scanner scann = new Scanner(System.in);
		Producto producto = null;
		Identificador identificador;
		TipoProducto tipo;

		System.out.println("Ingrese la descripción del producto: ");
		String descripcion = scann.nextLine();

		identificador = tienda.existeProducto(descripcion);

		if (identificador != null) {
			System.out.println("El producto ya se encuentra dado de alta");
			System.out.println("Identificador: " + identificador.getValor());
		} else {
			System.out.println("Dar de alta el producto");

			// Pedir tipo producto
			System.out.println("Seleccione la categoría del producto");
			tipo = Scripts.seleccionarTipoProducto();
			producto = crearProducto(descripcion, tipo);

		}

	}

	// Inhabilita un producto
	private static void cambiarDisponibilidadProducto() {

		Producto producto = null;
		char opcion;
		TipoProducto tipo = Scripts.seleccionarTipoProducto();
		String codigoProducto = Scripts.leerCodigoProducto(tipo);
		producto = tienda.getProducto(codigoProducto, tipo);

		if (producto == null) {
			System.out.println("No existe el producto con código: " + codigoProducto);
		} else if (producto.getDisponible()) {
			System.out.println("*******************************************************************");
			System.out.println("Producto: ");
			System.out.println(("Código: " + producto.getIdentificador().getValor()));
			System.out.println("Descripción: " + producto.getDescripcion());
			System.out.println("Cantidad en stock: " + producto.getCantidadStock());
			System.out.println("*******************************************************************");

			if (producto.getDisponible()) {
				opcion = Scripts.seleccionarConfirmacion("Saca del stock a este producto: Si/No");

				if (opcion == 'S') {
					producto.setDisponible(false);
					System.out.println("El producto ya no se encuentra disponible");
				}
			} else {
				if (producto.getCantidadStock() == 0) {
					System.out.println("No hay stock suficiente realizar la acción requerida");
				} else {
					producto.setDisponible(true);
					System.out.println("El producto ahora se encuentra disponible");
				}
			}

			// Reincertar el producto en la lista
			tienda.remplazarProducto(producto, tipo);
		} else {
			System.out.println("El producto ya se encuentra dado de baja");
		}
	}

	// Agrega un nuevo descuento a un producto.
	private static void agregarDescuento() {

		Producto producto = null;
		TipoProducto tipo = Scripts.seleccionarTipoProducto();
		String codigoProducto = Scripts.leerCodigoProducto(tipo);
		producto = tienda.getProducto(codigoProducto, tipo);
		char opcion = 'N';

		if (producto == null) {
			System.out.println("No existe el producto con código: " + codigoProducto);
		} else {

			if (!producto.getDisponible()) {
				System.out.println("Este producto no se encuentra disponible por el momento");
			} else {

				float descuento = producto.valorDescuento();

				if (descuento > 0)
					opcion = Scripts.seleccionarConfirmacion("Este producto tiene un descuento activo del " + descuento
							+ "% " + "¿Cancela esta promoción?");

				if (opcion == 'S' || descuento == 0) {
					tienda.activarNuevoDescuento(producto);
				} else {
					System.out.println("Operación cancelada");
				}
			}
		}
	}

	private static void comprarProducto() {
		Scanner scann = new Scanner(System.in);
		char opcion;
		String codigoProducto;
		TipoProducto tipo;
		Producto producto = null;

		do {
			tipo = Scripts.seleccionarTipoProducto();
			codigoProducto = Scripts.leerCodigoProducto(tipo);
			producto = tienda.getProducto(codigoProducto, tipo);

			// Ver si el producto se encuentra disponible.
			if (producto == null) {
				System.out.println("El producto no se encuentra dado de alta en el stock");
			} else {
				tienda.comprarProducto(producto);
			}

			opcion = Scripts.seleccionarConfirmacion("Ingresa más productos al stock Si/No: ");
		} while (opcion == 'S');

	}

	private static void venderProducto() {

		if (tienda.getStockDisponibles() == 0) {
			System.out.println("No hay stock para la venta");
		} else {
			tienda.venderProducto();
		}
	}

	// Lista de productos comestibles NO importados cuyo descuento sea menor a un
	// determinado porcentaje
	private static void obtenerComestiblesConMenorDescuento() {
		Scanner scann = new Scanner(System.in);
		float topeDescuento = 0f;

		System.out.println("Ingrese el procentaje de descuento como tope máximo, 0 para salir");
		topeDescuento = scann.nextFloat();

		if (topeDescuento > 0) {
			List<Producto> lista = tienda.obtenerComestiblesConMenorDescuento(topeDescuento);

			if (lista == null) {
				System.out.println("No hay productos con ese porcentaje de descuento");
			} else {
				System.out.println("Productos con descuento menor al " + topeDescuento + "%");
				for (Producto prod : lista) {
					System.out.print("Código: " + prod.getIdentificador().getValor());
					System.out.print("    ");
					System.out.println("Descripción: " + prod.getDescripcion());
				}
			}
		}

	}

	private static void listarProductosConUtilidadesInferiores() {
		Scanner scann = new Scanner(System.in);
		float topeGanancia = 0f;

		System.out.println("Ingrese el procentaje tope de ganancia, 0 para salir");
		topeGanancia = scann.nextFloat();

		if (topeGanancia > 0) {
			List<Producto> lista = tienda.listarProductosConUtilidadesInferiores(topeGanancia);

			if (lista == null) {
				System.out.println("No hay productos com porcentaje de utilidad menor a " + topeGanancia + "%");
			} else {
				System.out.println("Productos con descuento menor al " + topeGanancia + "%");
				for (Producto prod : lista) {
					System.out.print("Código: " + prod.getIdentificador().getValor());
					System.out.print("    ");
					System.out.print("Descripción: " + prod.getDescripcion());
					System.out.print("  ");
					System.out.println("Stock disponible: " + prod.getCantidadStock() + " unidades");
				}
			}
		}

		System.out.println("");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scann = new Scanner(System.in);
		char op;
		tienda = new Tienda("Al infinito", 100000, 100000f);

		cargarProductos(TipoProducto.Bebida, "src/main/java/com/resources/Bebidas.csv");
		cargarProductos(TipoProducto.Envasado, "src/main/java/com/resources/Envasados.csv");
		cargarProductos(TipoProducto.Limpieza, "src/main/java/com/resources/Limpieza.csv");

		do {
			System.out
					.println("**************************************************************************************");
			System.out
					.println("***************************  Sistema gestión de stock maven********************************");

			System.out.println("Tienda: " + tienda.getNombre());
			System.out.println("Saldo en caja: " + tienda.getSaldoEnCaja());
			System.out.println("Stock total: " + tienda.getStockTotal());
			System.out.println("Stock productos disponibles: " + tienda.getStockDisponibles());
			System.out.println("");
			System.out.println("");
			System.out.println("Opciones de menú: ");
			System.out.println("[1]-->Nuevo producto");
			System.out.println("[2]-->Cambiar disponibilidad");
			System.out.println("[4]-->Agregar descuento");
			System.out.println("[5]-->Comprar  producto");
			System.out.println("[6]-->Vender  producto");
			System.out.println("[7]-->Listar productos");
			System.out.println("[8]-->Productos comestibles no importados");
			System.out.println("[9]-->Listar productos con utilidades inferiores");
			System.out.println("[0]-->Salir");
			System.out.println("");
			System.out.println("");

			System.out.print("Seleccione una opción del menú: ");
			op = scann.next().charAt(0);
			System.out.println("");
			System.out.println("");

			switch (op) {
			case '1':
				System.out.println("Nuevo producto");
				nuevoProducto();

				break;
			case '2':
				System.out.println("Cambiar disponibilidad");
				cambiarDisponibilidadProducto();
				break;
			case '3':
				System.out.println("Modificar producto");
				break;
			case '4':
				System.out.println("Agregar descuento");
				agregarDescuento();
				break;
			case '5':
				System.out.println("Ingresar producto al stock");
				comprarProducto();
				break;

			case '6':
				System.out.println("Vender  producto");
				venderProducto();
				break;
			case '7':
				System.out.println("Listado de stock");
				tienda.listarProductos();

				break;
			case '8':
				System.out.println("Productos comestibles no importados con descuento");
				obtenerComestiblesConMenorDescuento();
				break;
			case '9':
				System.out.println("Listar productos con utilidades inferiores");
				listarProductosConUtilidadesInferiores();
				break;
			case '0':
				System.out.println("Saliendo del programa...");
				break;

			default:
				System.out.println("Opción inválida");
				break;
			}
		} while (op != '0');

		System.out.println("Programa finalizado");
		System.out.println();

		scann.close();

	}

}
