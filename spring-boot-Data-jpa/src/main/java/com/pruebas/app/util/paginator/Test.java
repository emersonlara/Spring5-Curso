package com.pruebas.app.util.paginator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.pruebas.app.models.entity.Factura;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Factura");
		EntityManager em = emf.createEntityManager();
		TypedQuery<Factura> consulta = em.createQuery("select e from Experto e", Factura.class);

		System.out.println(consulta.getSingleResult().getId());
	}

}
