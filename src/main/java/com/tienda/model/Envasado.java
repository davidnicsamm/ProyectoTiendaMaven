package com.tienda.model;

import java.util.Date;
import com.tienda.model.Comestible;
import com.tienda.model.Identificador;
import com.tienda.model.TipoEnvase;
import com.tienda.model.TipoProducto;

public class Envasado extends Producto implements Comestible {

	private TipoEnvase tipoEnvase;
	private Boolean esImportado;
	private Short calorias;
	private Date fechaVencimiento;

	public static final String prefijo = "AB";

	public Envasado() {
		super();
	}

	public Envasado(Boolean disponible, String descripcion, TipoProducto tipo, TipoEnvase tipoEnvase,
			Boolean esImportado, Short calorias, Date fechaVencimiento) {

		super(disponible, descripcion, tipo);
		this.tipoEnvase = tipoEnvase;
		this.esImportado = esImportado;
		this.calorias = calorias;
		this.fechaVencimiento = fechaVencimiento;
	}

	public Envasado(Identificador identificador, Boolean disponible, String descripcion, Short cantidadStock,
			Float precioUnidad, Float costoUnidad, AplicaDescuento[] descuentos, TipoProducto tipo,
			TipoEnvase tipoEnvase, Boolean esImportado, Short calorias, Date fechaVencimiento) {

		super(identificador, disponible, descripcion, cantidadStock, precioUnidad, costoUnidad, descuentos, tipo);
		this.tipoEnvase = tipoEnvase;
		this.esImportado = esImportado;
		this.calorias = calorias;
		this.fechaVencimiento = fechaVencimiento;
	}

	// Getters and Setters

	public TipoEnvase getTipoEnvase() {
		return tipoEnvase;
	}

	public void setTipoEnvase(TipoEnvase tipoEnvase) {
		this.tipoEnvase = tipoEnvase;
	}

	public Boolean getEsImportado() {
		return esImportado;
	}

	public void setEsImportado(Boolean esImportado) {
		this.esImportado = esImportado;
	}

	@Override
	public void setFechaVencimineto(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;

	}

	@Override
	public Date getFechaVencimiento() {

		return this.fechaVencimiento;
	}

	@Override
	public void setCalorias(short calorias) {

		this.calorias = calorias;

	}

	@Override
	public Short getCalorias() {
		return this.calorias;
	}

}
