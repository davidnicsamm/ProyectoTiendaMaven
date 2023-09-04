package com.tienda.model;

import java.util.List;
import java.util.Objects;

import com.tienda.model.Identificador;
import com.tienda.model.TipoProducto;

public abstract class Producto {

	private Identificador identificador;
	private Boolean disponible;
	private String descripcion;
	private Short cantidadStock;
	private Float precioUnidad;
	private Float costoUnidad;
	private List<AplicaDescuento> descuentos;
	private TipoProducto tipo;

	public Producto() {
	};

	// Constructor para un producto nuevo
	public Producto(Boolean disponible, String descripcion, TipoProducto tipo) {

		super();
		this.disponible = disponible;
		this.descripcion = descripcion;
		this.descuentos = null;
		this.tipo = tipo;
		this.cantidadStock = 0;
		this.precioUnidad = 0f;
		this.costoUnidad = 0f;

	}

	public Producto(Identificador identificador, Boolean disponible, String descripcion, Short cantidadStock,
			Float precioUnidad, Float costoUnidad, AplicaDescuento[] descuentos, TipoProducto tipo) {
		super();
		this.identificador = identificador;
		this.disponible = disponible;
		this.descripcion = descripcion;
		this.cantidadStock = cantidadStock;
		this.precioUnidad = precioUnidad;
		this.costoUnidad = costoUnidad;
		this.descuentos = null;
		this.tipo = tipo;
	}

	// Getters and Setters
	public List<AplicaDescuento> getDescuentos() {
		return descuentos;
	}

	public AplicaDescuento getUltimoDescuento() {

		if (descuentos != null) {
			return (descuentos.get(descuentos.size() - 1));
		} else {
			return null;
		}

	}

	public float valorDescuento() {
		float valorDescuento = 0;

		if (descuentos != null) {
			AplicaDescuento descuento = descuentos.get(descuentos.size() - 1);
			if (descuento.getFechaFin() == null) {
				valorDescuento = descuento.getDescuento();
			}
		}

		return valorDescuento;

	}

	public void setDescuentos(List<AplicaDescuento> descuentos) {
		this.descuentos = descuentos;
	}

	public TipoProducto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProducto tipo) {
		this.tipo = tipo;
	}

	public void setIdentificador(Identificador identificador) {
		this.identificador = identificador;
	}

	public Identificador getIdentificador() {
		return identificador;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Short getCantidadStock() {
		return cantidadStock;
	}

	public void setCantidadStock(Short cantidadStock) {
		this.cantidadStock = cantidadStock;
		this.disponible = !(this.cantidadStock == 0);
	}

	public Float getPrecioUnidad() {
		return precioUnidad;
	}

	public void setPrecioUnidad(Float precioUnidad) {
		this.precioUnidad = precioUnidad;
	}

	public Float getCostoUnidad() {
		return costoUnidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, identificador);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return Objects.equals(descripcion, other.descripcion) && Objects.equals(identificador, other.identificador);
	}

	public void setCostoUnidad(Float costoUnidad) {
		this.costoUnidad = costoUnidad;
	}

}
