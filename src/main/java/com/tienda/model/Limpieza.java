package com.tienda.model;

import com.tienda.model.Identificador;
import com.tienda.model.TipoAplicacion;
import com.tienda.model.TipoProducto;

public class Limpieza extends Producto {

	public static final String prefijo = "AZ";

	private TipoAplicacion tipoAplicacion;

	public Limpieza() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Limpieza(Boolean disponible, String descripcion, TipoProducto tipo, TipoAplicacion tipoAplicacion) {

		super(disponible, descripcion, tipo);
		this.tipoAplicacion = tipoAplicacion;
	}

	public Limpieza(Identificador identificador, Boolean disponible, String descripcion, Short cantidadStock,
			Float precioUnidad, Float costoUnidad, AplicaDescuento[] descuentos, TipoProducto tipo,
			TipoAplicacion tipoAplicacion) {

		super(identificador, disponible, descripcion, cantidadStock, precioUnidad, costoUnidad, descuentos, tipo);
		this.tipoAplicacion = tipoAplicacion;
	}

	// Getters and Setters

	public TipoAplicacion getTipoAplicacion() {
		return tipoAplicacion;
	}

	public void setTipoAplicacion(TipoAplicacion tipoAplicacion) {
		this.tipoAplicacion = tipoAplicacion;
	}

}
