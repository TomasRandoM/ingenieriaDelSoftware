package com.ntm.consorcio.persistence.entity;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.persistence.NoResultDAOException;
import java.util.Collection;
import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test para DAOPais
 * @version 1.0.0
 * @author Tomas Rando
 */
@RunWith(MockitoJUnitRunner.class)
public class DAOPaisBeanTest {
    
    @InjectMocks
    private DAOPaisBean dao;
    
    @Mock
    private EntityManager em;
    
    private Query query;
    private Pais pais;
    private String idPais;
    public DAOPaisBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        idPais = "falsoId";
        pais = new Pais();
        pais.setEliminado(false);
        pais.setId(idPais);
        pais.setNombre("Argentina");
        MockitoAnnotations.initMocks(this);

    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test para guardarPais()
     */
    @Test
    public void testGuardarPais() throws Exception {
        try {
            dao.guardarPais(pais);
            verify(em).persist(any(Pais.class));
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            fail("No se debería haber lanzado una excepción");
        }
    }

    /**
     * Test para actualizarPais()
     */
    @Test
    public void testActualizarPais() throws Exception {
        try {
            dao.actualizarPais(pais);
            verify(em).merge(any(Pais.class));
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            fail("No se debería haber lanzado una excepción");
        }
    }

    /**
     * Test of buscarPais method, of class DAOPaisBean.
     */
    @Test
    public void testBuscarPais() throws Exception {
        when(em.find(Pais.class, idPais)).thenReturn(pais);
        try {
            Pais aux = dao.buscarPais(idPais);
            assertEquals(aux, pais);
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            fail("No se debería haber lanzado una excepción");
        }
    }

    
    /**
     * Test of buscarPaisPorNombre method, of class DAOPaisBean.
     */
    /*
    @Test
    public void testBuscarPaisPorNombre() throws Exception {
        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(pais);
        try {
            dao.buscarPaisPorNombre("Argentina");
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            fail("No se debería haber lanzado una excepción");
        }
    }
    */
    /**
     * Test of listarPaisActivo method, of class DAOPaisBean.
     */
    /*
    @Test
    public void testListarPaisActivo() throws Exception {
        
    }
    */
    
}
