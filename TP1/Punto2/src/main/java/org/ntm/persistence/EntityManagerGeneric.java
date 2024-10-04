package org.ntm.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerGeneric {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Punto2Pu");
    private final EntityManager em = emf.createEntityManager();
    private static EntityManagerGeneric instancia;

    private EntityManagerGeneric () {

    }
    public static EntityManagerGeneric getInstancia () {
        if (instancia == null) {
            instancia = new EntityManagerGeneric();
        }
        return instancia;
    }
    public <T> void guardar(T obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public <T> void actualizar(T obj) {
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public <T> T buscar(Class<T> clase, String id) {
        return em.find(clase, id);
    }

}
