package com.tienda.model;

public interface Descuento {
	
	void setDescuento(float descuento);
	Float getDescuento();
	
	default Float getValorDescuento(float precio, float descuento) {
		return precio * descuento;
		
	};
	
	default Float getPrecioConDescuento(float precio, float descuento) {
		return precio - getValorDescuento(precio, descuento);
	};
}
