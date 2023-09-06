package com.tienda.model;

public class DetalleVenta {

	private Producto producto;
	private Float precioUnitario;
	private Short cantidad;
	private Float descuento;

	public DetalleVenta() {
	};

	public DetalleVenta(Producto producto, Float precioUnitario, Short cantidad, Float descuento) {
		super();
		this.producto = producto;
		this.precioUnitario = precioUnitario;
		this.cantidad = cantidad;
		this.descuento = descuento;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Float getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Float precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Short getCantidad() {
		return cantidad;
	}

	public void setCantidad(Short cantidad) {
		this.cantidad = cantidad;
	}

	public Float getDescuento() {
		return descuento;
	}

	public void setDescuento(Float descuento) {
		this.descuento = descuento;
	}

}
