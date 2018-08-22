package com.pruebas.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;

	private int totalPaginas;
	private int numElementosPorPaginas;
	private int paginaActual;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		this.numElementosPorPaginas = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (totalPaginas <= numElementosPorPaginas) { // el num elementos es pequeño, se muestran todas las páginas
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= numElementosPorPaginas / 2) { // rango inicial
				desde = 1;
				hasta = numElementosPorPaginas;
			} else if (paginaActual >= numElementosPorPaginas / 2) { // rango final
				desde = totalPaginas - numElementosPorPaginas;
				hasta = numElementosPorPaginas;
			} else { // rango medio
				desde = paginaActual - numElementosPorPaginas / 2;
				hasta = numElementosPorPaginas;
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
