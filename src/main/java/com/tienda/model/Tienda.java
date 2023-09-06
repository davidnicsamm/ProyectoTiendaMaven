package com.tienda.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.tienda.model.Comestible;
import com.tienda.model.Identificador;
import com.tienda.model.TipoAplicacion;
import com.tienda.model.TipoProducto;
import com.tienda.util.Scripts;

public class Tienda {
	private String nombre;
	private Integer nroMaxProdStock;
	private Float saldoEnCaja;
	private Map<TipoProducto, List<Producto>> productos;
	private List<Venta> listaVentas;

	// Verifica si un objeto implementa una interfaz
	private Boolean implementaInterface(Class<?> clazz, Class<?> targetInterface) {
		Class<?>[] interfaces = clazz.getInterfaces();
		boolean encontrado = false;
		int contador = 0;

		while (!encontrado && contador < interfaces.length) {
			if (interfaces[contador] == targetInterface) {
				encontrado = true;
			}
			contador++;
		}

		if (!encontrado) {
			System.out
					.println("La clase " + clazz.getName() + " no implementa la interfaz " + targetInterface.getName());
		}

		return encontrado;

	}

	// Calcula el descuento para un producto.
	private Short calcularPorcentajeDescuento(Producto producto) {
		Short porcentajeMaximo = 0;

		switch (producto.getClass().getSimpleName()) {
		case "Bebida":
			porcentajeMaximo = 15;
			break;
		case "Envasado":
			porcentajeMaximo = 20;
			break;

		case "Limpieza":
			porcentajeMaximo = 25;
			break;

		default:
			break;
		}

		float diferenciaPrecio = producto.getPrecioUnidad() - producto.getCostoUnidad();
		float descuentoMaximo = (producto.getPrecioUnidad() * porcentajeMaximo) / 100;

		if (diferenciaPrecio < descuentoMaximo) {
			float descuentoMaximoAux = descuentoMaximo - (descuentoMaximo - diferenciaPrecio);
			porcentajeMaximo = (short) ((descuentoMaximoAux * porcentajeMaximo) / descuentoMaximo);
		}

		return porcentajeMaximo;

	}

	private Map<String, Short> calcularPorcentajesVenta(Producto producto) {

		Map<String, Short> porcentajes = new HashMap<>();
		Short porcentajeMinimo = 0;
		Short porcentajeMaximo = 0;

		// Verificar si producto implementa comestible.
		Class<?> clazz = producto.getClass();
		Class<?> targetInterface = Comestible.class;
		Boolean implementa = implementaInterface(clazz, targetInterface);

		// Si es comestible
		if (implementa) {
			porcentajeMaximo = 20;
		}

		if (producto instanceof Limpieza) {
			porcentajeMaximo = 25;
			if (((Limpieza) producto).getTipoAplicacion() == TipoAplicacion.Cocina
					|| ((Limpieza) producto).getTipoAplicacion() == TipoAplicacion.Piso) {

				porcentajeMinimo = 10;
			}
		}

		porcentajes.put("PorcentajeMinimo", porcentajeMinimo);
		porcentajes.put("PorcentajeMaximo", porcentajeMaximo);

		return porcentajes;

	}

	// Verifica si un producto existe en la tienda, si existe, devuelve su
	// identificador.
	public Identificador existeProducto(String descripcionProducto) {
		Boolean existe = false;
		Producto producto = null;

		for (TipoProducto tipo : this.productos.keySet()) {
			List<Producto> lista = this.productos.get(tipo);

			for (Producto prod : lista) {
				producto = descripcionProducto.equals(prod.getDescripcion()) ? prod : null;
			}
		}
		if (producto != null) {
			return producto.getIdentificador();
		} else {
			return null;
		}
	}

	// Busca un producto por su identificador
	public Producto getProducto(String codigoProducto, TipoProducto tipo) {
		List<Producto> lista = this.productos.get(tipo);
		Producto producto = null;
		Boolean encontrado = false;

		int index = 0;

		while (!encontrado && index < lista.size()) {

			if (codigoProducto.equals(lista.get(index).getIdentificador().getValor())) {
				producto = lista.get(index);
			}
			index++;
		}

		return producto;
	}

	public int getPosicionProducto(String codigoProducto, TipoProducto tipo) {

		int posicion = -1;
		Boolean exito = false;
		List<Producto> lista = this.productos.get(tipo);

		int index = 0;
		while (!exito && index < lista.size()) {
			if (codigoProducto.equals(lista.get(index).getIdentificador().getValor())) {
				exito = true;
				posicion = index;
			} else {
				index++;
			}
		}

		return posicion;
	}

	public Boolean remplazarProducto(Producto producto, TipoProducto tipo) {

		Boolean exito = false;
		List<Producto> lista = this.productos.get(tipo);

		int index = 0;
		while (!exito && index < lista.size()) {
			if (producto.getIdentificador().getValor().equals(lista.get(index).getIdentificador().getValor())) {
				exito = true;
			} else {
				index++;
			}
		}
		lista.set(index, producto);
		this.productos.put(tipo, lista);

		return exito;
	}

	// Devuelve el stock total de la tienda
	public int getStockTotal() {
		int totalProductos = 0;
		for (TipoProducto tipo : this.productos.keySet()) {
			List<Producto> lista = this.productos.get(tipo);

			for (Producto prod : lista) {
				totalProductos += prod.getCantidadStock();
			}
		}
		return totalProductos;
	}

	// Devuelve el stock de productos disponibles de la tienda.
	public int getStockDisponibles() {
		int totalProductos = 0;
		for (TipoProducto tipo : this.productos.keySet()) {
			List<Producto> lista = this.productos.get(tipo);

			for (Producto prod : lista) {
				if (prod.getDisponible())
					totalProductos += prod.getCantidadStock();
			}
		}
		return totalProductos;
	}

	public void venderProducto() {
	
		Scanner scann = new Scanner(System.in);
		int cantidadTipoProducto = 0;
		int cantidadProductos = 0;
		char opcion;
		TipoProducto tipo;
		String codigoProducto;
		Producto producto;
		Boolean seguir = false;
		List<DetalleVenta> listadoVenta = new ArrayList<>();
		Date fechaVenta;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		// Crea un map con los tipos de productos y la cantidad que se va acumulando en
		// la venta
		Map<TipoProducto, Integer> tipos = new HashMap<>();
		
		for (TipoProducto t : TipoProducto.values()) {
			tipos.put(t, 0);
		}

		float totalVenta = 0;

		do {

			tipo = Scripts.seleccionarTipoProducto();
			codigoProducto = Scripts.leerCodigoProducto(tipo);
			producto = this.getProducto(codigoProducto, tipo);

			// Ver si el producto se encuentra disponible.
			if (producto == null) {
				System.out.println("El producto no se encuentra dado de alta en el stock");
			} else {
				if (!producto.getDisponible()) {
					System.out.println("El producto no se encuentra disponible por el momento");
				} else {
					// Si el producto existe y está disponible

					if (cantidadTipoProducto == 3 && tipos.get(tipo) == 0) {
						System.out.println("No puede agregar otro tipo de producto");
					} else {
						int cantDisponible = 10 - tipos.get(tipo);

						if (cantDisponible > producto.getCantidadStock())
							cantDisponible = producto.getCantidadStock();

						short cantAComprar;
						do {
							System.out.println("Tiene como máximo " + cantDisponible + " para llevar");
							System.out.println("Ingrese la cantidad a comprar, 0 para cancelar");
							cantAComprar = scann.nextShort();

						} while (cantAComprar != 0 && cantAComprar > cantDisponible);

						if (cantAComprar != 0) {
							float precio = producto.getPrecioUnidad();
							float descuento = producto.valorDescuento();
							float totalDescuento = (precio * descuento) * cantAComprar;
							float precioVenta = (precio * cantAComprar);

							// Verifica si son productos importados y apica recargo del 10%
							if (tipo == TipoProducto.Bebida) {
								if (((Bebida) producto).getEsImportado())
									precioVenta = (precio * cantAComprar) * (float) 1.10;
							}
							if (tipo == TipoProducto.Envasado) {
								if (((Envasado) producto).getEsImportado())
									precioVenta = (precio * cantAComprar) * (float) 1.10;
							}

							precioVenta = precioVenta - totalDescuento;

							System.out.println(producto.getIdentificador().getValor() + " " + producto.getDescripcion()
									+ " " + cantAComprar + " x " + precio);

							if (totalDescuento > 0) {
								System.out.print("(-" + totalDescuento + ")");
							}

							System.out.println();

							producto.setCantidadStock((short) (producto.getCantidadStock() - cantAComprar));
							totalVenta += precioVenta;

							DetalleVenta detalleVenta = new DetalleVenta(producto, precio, cantAComprar, totalDescuento);
							listadoVenta.add(detalleVenta);		
						

							if (tipos.get(tipo) == 0)
								cantidadTipoProducto += 1;
							tipos.put(tipo, tipos.get(tipo) + cantAComprar);

						}
					}
				}
			}

			opcion = Scripts.seleccionarConfirmacion("Ingresa más productos para vender ");
			seguir = (opcion == 'S') && (cantidadTipoProducto <= 3);

		} while (seguir);
		
		
		
		Venta venta = new Venta();
		fechaVenta = new Date();
		String fechaFormateada = formateador.format(fechaVenta);
		venta.setFecha(fechaVenta);	
		venta.setListaVenta(listadoVenta);
		this.agregarVentas(venta);
		
		

		System.out.println(
				"**********************************************************************************************");
		System.out.println(
				"******************************  Detalle de la venta ******************************************");
		System.out.println(
				"Cód.                  Descripción                           Cantidad         P.Unit.    Desc.");

		for (DetalleVenta detalleVenta : listadoVenta) {
			System.out.print(detalleVenta.getProducto().getIdentificador().getValor() + "        ");
			System.out.print(detalleVenta.getProducto().getDescripcion() + "                   ");
			System.out.print(detalleVenta.getCantidad() + " x            ");
			System.out.print(detalleVenta.getPrecioUnitario() + "  ");
			System.out.println(detalleVenta.getDescuento() + " ");
		}

		
		
		System.out.println(
				"**********************************************************************************************");
		System.out.println(
				"**********************************************************************************************");

		this.saldoEnCaja += totalVenta;

	}

	

	public void comprarProducto(Producto producto) {
		Scanner scann = new Scanner(System.in);

		System.out.println("*******************************************************************");
		System.out.println("Producto: ");
		System.out.println(("Código: " + producto.getIdentificador().getValor()));
		System.out.println("Descripción: " + producto.getDescripcion());
		System.out.println("Cantidad en stock: " + producto.getCantidadStock());
		System.out.println("*******************************************************************");

		System.out.println("Cantidad a ingresar: ");
		Short cantidad = scann.nextShort();
		System.out.println("Costo por unidad");
		float costoUnidad = scann.nextFloat();

		int stockActual = this.getStockTotal();
		int maximoStock = this.getNroMaxProdStock();
		int espacioEnStock = maximoStock - stockActual;

		if (espacioEnStock < cantidad) {
			System.out.println("Espacio insuficiente para agregar esta cantidad de productos");
			System.out.println("Espacio disponible en stock: " + espacioEnStock);
		} else {
			float costoOperacion = cantidad * costoUnidad;

			if ((this.getSaldoEnCaja() - costoOperacion) <= 0) {
				System.out.println("No hay saldo en caja suficiente para cubrir esta operación");
			} else {

				Map<String, Short> porcentajesVenta = calcularPorcentajesVenta(producto);
				Short porcentajeMinimo = porcentajesVenta.get("PorcentajeMinimo");
				Short porcentajeMaximo = porcentajesVenta.get("PorcentajeMaximo");
				Short porcentajeVenta = 0;

				do {
					System.out
							.println("El porcentaje máximo de de ganancia para este producto es: " + porcentajeMaximo);
					System.out.println("Ingrese el procentaje de ganancia o 0 para cancelar: ");
					porcentajeVenta = scann.nextShort();
				} while ((porcentajeVenta > porcentajeMaximo) || (porcentajeVenta < porcentajeMinimo));

				float valorVenta = costoUnidad + (costoUnidad * porcentajeVenta) / 100;

				this.setSaldoEnCaja(this.getSaldoEnCaja() - costoOperacion);

				Short stockNuevo = (short) (producto.getCantidadStock() + cantidad);
				producto.setCantidadStock(stockNuevo);

				this.remplazarProducto(producto, producto.getTipo());

			}
		}
	}

	public void agregarProducto(Producto producto) {
		Identificador identificador = null;

		int longitud;
		String cadenaIdentificador;

		System.out.println("Creando producto");

		if (producto instanceof Bebida) {
			System.out.println("Bebida");
			Bebida bebida = null;
			List<Producto> lista = this.productos.get(TipoProducto.Bebida);
			longitud = lista.size();

			if (longitud == 0) {
				cadenaIdentificador = Bebida.prefijo;

			} else {
				bebida = (Bebida) lista.get(longitud - 1);
				cadenaIdentificador = bebida.getIdentificador().getValor();
			}

			cadenaIdentificador = Identificador.generarIdentificador(cadenaIdentificador);
			identificador = new Identificador(cadenaIdentificador);
			producto.setIdentificador(identificador);
			lista.add(producto);
			this.productos.put(TipoProducto.Bebida, lista);

		} else if (producto instanceof Envasado) {

			System.out.println("Envasado");

			Envasado envasado = null;
			List<Producto> lista = this.productos.get(TipoProducto.Envasado);
			longitud = lista.size();

			if (longitud == 0) {
				cadenaIdentificador = Envasado.prefijo;
			} else {
				envasado = (Envasado) lista.get(longitud - 1);
				cadenaIdentificador = envasado.getIdentificador().getValor();
			}

			cadenaIdentificador = Identificador.generarIdentificador(cadenaIdentificador);
			identificador = new Identificador(cadenaIdentificador);
			producto.setIdentificador(identificador);
			lista.add(producto);
			this.productos.put(TipoProducto.Envasado, lista);

		} else if (producto instanceof Limpieza) {
			System.out.println("Limpieza");
			Limpieza limpieza = null;
			List<Producto> lista = this.productos.get(TipoProducto.Limpieza);
			longitud = lista.size();

			if (longitud == 0) {
				cadenaIdentificador = Limpieza.prefijo;
			} else {
				limpieza = (Limpieza) lista.get(longitud - 1);
				cadenaIdentificador = limpieza.getIdentificador().getValor();

			}

			cadenaIdentificador = Identificador.generarIdentificador(cadenaIdentificador);
			identificador = new Identificador(cadenaIdentificador);
			producto.setIdentificador(identificador);
			lista.add(producto);
			this.productos.put(TipoProducto.Limpieza, lista);

		} else {
			System.out.println("Tipo de producto desconocido");
		}

		System.out.println(producto.getDescripcion());
	}

	// Se crea un nuevo descuento para el producto.
	public void activarNuevoDescuento(Producto producto) {

		Scanner scann = new Scanner(System.in);
		Date fecha = new Date();
		Short porcDescuento;
		Short porcentajeMaximo = calcularPorcentajeDescuento(producto);

		do {
			System.out.println("El porcentaje máximo de descuento para este producto es: " + porcentajeMaximo);
			System.out.println("Ingrese el procentaje del descuento o 0 para cancelar: ");
			porcDescuento = scann.nextShort();
		} while (porcDescuento > porcentajeMaximo);

		if (porcDescuento != 0) {
			AplicaDescuento descuento;
			List<AplicaDescuento> descuentos = producto.getDescuentos();
			descuento = new AplicaDescuento((float) porcDescuento / 100, fecha);
			if (descuentos == null) {
				descuentos = new ArrayList<AplicaDescuento>();
				descuentos.add(descuento);
			} else {
				int ultimoElemnto = descuentos.size() - 1;
				AplicaDescuento auxDescuento = descuentos.get(ultimoElemnto);
				auxDescuento.setFechaFin(fecha);
				descuentos.set(ultimoElemnto, auxDescuento);
				descuentos.add(descuento);
			}
			producto.setDescuentos(descuentos);
			// reempazar el producto en la tienda
			remplazarProducto(producto, producto.getTipo());
		} else {
			System.out.println("No se aplico un nuevo descuento");
		}

	}

	private void listarProductoCategoria(TipoProducto tipo) {
		char opcion;
		int cont = 0;
		List<Producto> lista = this.productos.get(tipo);

		System.out.println("Listado de " + tipo + ": ");

		for (Producto prod : lista) {
			Producto p = prod;

			System.out.print("Identificador: " + p.getIdentificador().getValor());
			System.out.print("      ");
			System.out.println("Descripción: " + p.getDescripcion());
			System.out.print("Precio: " + "$" + p.getPrecioUnidad());
			System.out.print("      ");
			AplicaDescuento descuento = p.getUltimoDescuento();
			System.out.print("Descuento: ");
			System.out
					.print(descuento == null ? "Sin descuento" : "Descuento: " + descuento.getDescuento() * 100 + "%");
			System.out.print("      ");
			System.out.print("Stock: " + p.getCantidadStock() + " unidades");
			System.out.println("");

			if (p instanceof Bebida) {
				Bebida b = (Bebida) p;
				System.out.print("¿Es alcohólica?: ");
				System.out.print(b.getEsAlcoholica() ? "Si" : "No");
				System.out.print("      ");
				System.out.print(b.getEsAlcoholica() ? b.getGraduacion() + "%vol" : "");
				System.out.print("      ");
				System.out.println("Calorías: " + b.getCalorias());
				// Calorias
			} else if (p instanceof Envasado) {
				Envasado e = (Envasado) p;
				System.out.print("Tipo de envase: " + e.getTipoEnvase());
				System.out.print("      ");
				System.out.println("Calorías: " + e.getCalorias());

			} else if (p instanceof Limpieza) {
				Limpieza l = (Limpieza) p;
				System.out.println("Aplicación: " + l.getTipoAplicacion());
			}
			System.out.print("Disponible: ");
			System.out.println(prod.getDisponible() ? "Si" : "No");
			System.out.println("");

			cont += 1;

			if (cont == 4) {
				opcion = Scripts.seleccionarConfirmacion("Continúa");

				if (opcion == 'N')
					break;
				cont = 0;
				System.out.println("Listado de " + tipo + ": ");
			}
		}
	}

	public void listarProductos() {
		TipoProducto tipoProducto = null;
		Scanner scann = new Scanner(System.in);

		char opcion, opcionListado;
		// ***********************************************************

		opcion = Scripts.seleccionarConfirmacion(
				"Seleccione Si para mostrar todo el stock y No para ver una categoría en particular");
		switch (opcion) {
		case 'S':
			for (TipoProducto tipo : this.productos.keySet()) {
				listarProductoCategoria(tipo);
			}
			break;
		case 'N':
			tipoProducto = Scripts.seleccionarTipoProducto();
			listarProductoCategoria(tipoProducto);
			break;

		default:
			break;
		}

	}

	public List<Producto> obtenerComestiblesConMenorDescuento(float topeDescuento) {
		List<Producto> listaProductos = new ArrayList<>();

		listaProductos = this.productos.values().stream().flatMap(List<Producto>::stream)
				.filter(producto -> (producto.valorDescuento() > 0) && producto.valorDescuento() * 100 < topeDescuento)
				.collect(Collectors.toList());

		return listaProductos;

	};

	public List<Producto> listarProductosConUtilidadesInferiores(float topeGanancia) {
		List<Producto> listaProductos = new ArrayList<>();

		listaProductos = this.productos.values().stream().flatMap(List<Producto>::stream).map(producto -> {
			float costoUnidad = producto.getCostoUnidad();
			float precioUnidad = producto.getPrecioUnidad();
			float difPrecioUnidad = precioUnidad - costoUnidad;
			float porcentajeGanancia = (difPrecioUnidad * 100) / costoUnidad;
			if (porcentajeGanancia < topeGanancia)
				return producto;
			return null;

		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (listaProductos.size() > 0)
			return listaProductos;
		else
			return null;
	}

	public Tienda(String nombre, Integer nroMaxProdStock, Float saldoEnCaja) {
		super();

		List<Producto> bebidas = new ArrayList<>();
		List<Producto> envasados = new ArrayList<>();
		List<Producto> limpieza = new ArrayList<>();

		this.nombre = nombre;
		this.nroMaxProdStock = nroMaxProdStock;
		this.saldoEnCaja = saldoEnCaja;
		this.productos = new HashMap<>();
		this.productos.put(TipoProducto.Bebida, bebidas);
		this.productos.put(TipoProducto.Envasado, envasados);
		this.productos.put(TipoProducto.Limpieza, limpieza);
		this.listaVentas = new ArrayList<>();
	}

	public Map<TipoProducto, List<Producto>> getProductos() {
		return productos;
	}

	public void setProductos(Map<TipoProducto, List<Producto>> productos) {
		this.productos = productos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getNroMaxProdStock() {
		return nroMaxProdStock;
	}

	public void setNroMaxProdStock(Integer nroMaxProdStock) {
		this.nroMaxProdStock = nroMaxProdStock;
	}

	public Float getSaldoEnCaja() {
		return saldoEnCaja;
	}

	public void setSaldoEnCaja(Float saldoEnCaja) {
		this.saldoEnCaja = saldoEnCaja;
	}

	// Reemplaza la lista del tipo producto.
	public void insertarListaProductos(TipoProducto tipo, List<Producto> lista) {
		productos.put(tipo, lista);
	}
	
	public List<Venta> getListaVentas() {
		return listaVentas;
	}

	public void setListaVentas(List<Venta> listaVentas) {
		this.listaVentas = listaVentas;
	}
	
	public void agregarVentas(Venta ventas) {
		this.listaVentas.add(ventas);
	}
	
	

}
