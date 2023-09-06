package com.tienda.model;

import java.util.Date;
import java.util.List;

public class Venta {
	private Date fecha;
	private List<DetalleVenta> listaVenta;
	
	
	public Venta () {}
	
	public Venta(Date fecha, List<DetalleVenta> listaVenta) {
		super();
		this.fecha = fecha;
		this.listaVenta = listaVenta;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<DetalleVenta> getListaVenta() {
		return listaVenta;
	}

	public void setListaVenta(List<DetalleVenta> listaVenta) {
		this.listaVenta = listaVenta;
	}
	
	

}
