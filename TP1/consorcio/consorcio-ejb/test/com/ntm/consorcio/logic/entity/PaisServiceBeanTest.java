
package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOPaisBean;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Test para PaisServiceBean
 * @author Tomas Rando
 * @version 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaisServiceBeanTest {
    @InjectMocks
    private PaisServiceBean service;
    @Mock
    private DAOPaisBean dao;
    private String idPais;
    private Pais pais;
    public PaisServiceBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        idPais = "idFalsa";
        service = new PaisServiceBean();
        MockitoAnnotations.initMocks(this);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test para crearPais()
     */
    @Test
    public void testACrearPais() throws Exception {
        when(dao.buscarPaisPorNombre(anyString())).thenThrow(new NoResultDAOException("No se encontró"));
        try {
            service.crearPais("Argentina");
            verify(dao).guardarPais(any(Pais.class));
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            fail("No se debería haber lanzado una excepción");
        }
    }
    
    /**
     * Test para buscarPais()
     * @throws Exception 
     */
    @Test
    public void testBBuscarPais() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        when(dao.buscarPais(anyString())).thenReturn(pais);
        try {
            assertEquals(service.buscarPais(idPais).getNombre(), "Argentina");            
        } catch (Exception ex) {
            fail("No se debería haber lanzado una excepción");
        }
    }
    
    /**
     * Test para modificarPais()
     */
    @Test
    public void testCModificarPais() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId("idFalso");
        when(dao.buscarPaisPorNombre(anyString())).thenThrow(new NoResultDAOException("No se encontró"));
        when(dao.buscarPais(anyString())).thenReturn(pais);
        try {
            service.modificarPais(idPais, "Chile");
            verify(dao).actualizarPais(any(Pais.class));
        } catch (Exception ex) {
            fail("No se debería haber lanzado una excepción");
        } 
    }
    
        /**
     * Test para crearPais() con un nombre igual a uno ya existente
     */
    @Test
    public void testDCrearPaisExistente() throws Exception {
        pais = new Pais();
        pais.setNombre("Chile");
        pais.setEliminado(false);
        pais.setId("idFalso");
        when(dao.buscarPaisPorNombre(anyString())).thenReturn(pais);
        try {
            service.crearPais("Chile");
            fail("Debería haber habido una excepción");
        } catch (ErrorServiceException ex) {
            //La excepción es correcta, la prueba pasa
        } catch (Exception ex) {
            fail("La excepción no era la que se esperaba.");
        }
    }
    
        
    /**
     * Test para modificarPais() con un nombre igual a uno existente
     */
    @Test
    public void testEModificarPaisExistente() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId(idPais);
        Pais pais2 = new Pais();
        pais2.setNombre("Chile");
        pais2.setEliminado(false);
        pais2.setId("idFalso2");
        when(dao.buscarPaisPorNombre("Chile")).thenReturn(pais2);
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.modificarPais(idPais, "Chile");
            fail("Se esperaba una excepción");
        } catch (ErrorServiceException ex) {
            //La excepción es la esperada, la prueba pasa
        } catch (Exception ex) {
            fail("No se debería haber lanzado otra excepción");
        } 
    }
    
    /**
     * Test para EliminarPais()
     * @throws Exception 
     */
    @Test
    public void testFEliminarPais() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId(idPais);
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.eliminarPais(idPais);
            verify(dao).actualizarPais(any(Pais.class));
        } catch (Exception ex) {
            fail("No se debería haber lanzado otra excepción");
        }
    }
    
    /**
     * Test para EliminarPais() con Pais ya eliminado
     * @throws Exception 
     */
    @Test
    public void testGEliminarPaisEliminado() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(true);
        pais.setId(idPais);
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.eliminarPais(idPais);
            fail("Debería haber lanzado excepción");
        } catch (ErrorServiceException ex) {
            // Excepción esperada, el test pasa
        } catch (Exception ex) {
            fail("No se debería haber lanzado otra excepción");
        }
    }
    
    /**
     * Test para EliminarPais() con id nulo
     * @throws Exception 
     */
    @Test
    public void testHEliminarPaisNulo() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId(idPais);
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.eliminarPais(null);
            fail("Debería haber lanzado excepción");
        } catch (ErrorServiceException ex) {
            // Excepción esperada, el test pasa
        } catch (Exception ex) {
            fail("No se debería haber lanzado otra excepción");
        }
    }
    
    /**
     * Test para ListarPaisActivo()
     * @throws Exception 
     */
    @Test
    public void testIListarPaisActivo() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId(idPais);
        Collection<Pais> paises = new ArrayList<>();
        paises.add(pais);
        when(dao.listarPaisActivo()).thenReturn(paises);
        try {
            service.listarPaisActivo();
        } catch (Exception ex) {
            fail("No se debería haber lanzado otra excepción");
        }
    }
    
    /**
    * Test para modificarPais() con nombre nulo
    */
    @Test
    public void testJModificarPaisNombreNulo() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId("idFalso");
        when(dao.buscarPaisPorNombre(anyString())).thenThrow(new NoResultDAOException("No se encontró"));
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.modificarPais(idPais, null);
        } catch (ErrorServiceException ex) {
            System.out.println(ex.getMessage());
            //Excepción esperada  
        } catch (Exception ex) {
            fail("No se debería haber lanzado una excepción");
        } 
    }
    
    
    /**
    * Test para modificarPais() con id nulo
    */
    @Test
    public void testKModificarPaisIdNulo() throws Exception {
        pais = new Pais();
        pais.setNombre("Argentina");
        pais.setEliminado(false);
        pais.setId("idFalso");
        when(dao.buscarPaisPorNombre(anyString())).thenThrow(new NoResultDAOException("No se encontró"));
        when(dao.buscarPais(idPais)).thenReturn(pais);
        try {
            service.modificarPais(null, "Chile");
        } catch (ErrorServiceException ex) {
            System.out.println(ex.getMessage());
            //Excepción esperada  
        } catch (Exception ex) {
            fail("No se debería haber lanzado una excepción");
        } 
    }
    

}
