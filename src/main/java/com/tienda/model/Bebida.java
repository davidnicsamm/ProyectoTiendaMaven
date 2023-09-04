package com.tienda.model;

import java.util.Date;

import com.tienda.model.Comestible;
import com.tienda.model.Identificador;
import com.tienda.model.TipoProducto;

public class Bebida extends Producto implements Comestible {

	private Boolean esAlcoholica;
	private Boolean esImportado;
	private Short graduacion;
	private Short calorias;
	private Date fechaVencimiento;

	public static final String prefijo = "AC";

	public Bebida() {
		super();
	}

	public static String getPrefijo() {
		return prefijo;
	}

	public Bebida(Boolean disponible, String descripcion, TipoProducto tipo, Boolean esAlcoholica, Boolean esImportado,
			Short graduacion, Short calorias, Date fechaVencimiento) {

		super(disponible, descripcion, tipo);
		this.esAlcoholica = esAlcoholica;
		this.esImportado = esImportado;
		this.graduacion = graduacion;
		this.calorias = calorias;
		this.fechaVencimiento = fechaVencimiento;

	}

	public Bebida(Identificador identificador, Boolean disponible, String descripcion, Short cantidadStock,
			Float precioUnidad, Float costoUnidad, AplicaDescuento[] descuentos, TipoProducto tipo,
			Boolean esAlcoholica, Boolean esImportado, Short graduacion, Short calorias, Date fechaVencimiento) {

		super(identificador, disponible, descripcion, cantidadStock, precioUnidad, costoUnidad, descuentos, tipo);
		this.esAlcoholica = esAlcoholica;
		this.esImportado = esImportado;
		this.graduacion = graduacion;
		this.calorias = calorias;
		this.fechaVencimiento = fechaVencimiento;
	}

	// Getters and Setters

	public Short getGraduacion() {
		return graduacion;
	}

	public void setGraduacion(Short graduacion) {
		this.graduacion = graduacion;
	}

	public Boolean getEsAlcoholica() {
		return esAlcoholica;
	}

	public void setEsAlcoholica(Boolean esAlcoholica) {
		this.esAlcoholica = esAlcoholica;
	}

	public Boolean getEsImportado() {
		return esImportado;
	}

	public void setEsImportado(Boolean esImportado) {
		this.esImportado = esImportado;
	}

	@Override
	public void setFechaVencimineto(Date fechaVencimiento) {
		// TODO Auto-generated method stub
		this.fechaVencimiento = fechaVencimiento;

	}

	@Override
	public Date getFechaVencimiento() {
		// TODO Auto-generated method stub
		return this.fechaVencimiento;
	}

	@Override
	public void setCalorias(short calorias) {
		// TODO Auto-generated method stub
		this.calorias = calorias;

	}

	@Override
	public Short getCalorias() {
		// TODO Auto-generated method stub
		return this.calorias;
	}

}
