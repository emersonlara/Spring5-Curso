package com.pruebas.app.util.paginator;

public class PageItem {

	private Integer numero;
	private Boolean actual;

	public PageItem(Integer numero, Boolean actual) {
		super();
		this.numero = numero;
		this.actual = actual;
	}

	public Integer getNumero() {
		return numero;
	}

	public Boolean getActual() {
		return actual;
	}

}
