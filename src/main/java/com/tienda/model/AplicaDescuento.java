package com.tienda.model;

import java.util.Date;

import com.tienda.model.Descuento;

public class AplicaDescuento implements Descuento {

	private Float descuento;
	private Date fechaInicio;
	private Date fechaFin;

	public AplicaDescuento(Float descuento, Date fechaInicio) {
		super();
		this.descuento = descuento;
		this.fechaInicio = fechaInicio;
		this.fechaFin = null;
	}

	@Override
	public Float getDescuento() {
		return descuento;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Override
	public void setDescuento(float descuento) {
		this.descuento = descuento;

	}

}
