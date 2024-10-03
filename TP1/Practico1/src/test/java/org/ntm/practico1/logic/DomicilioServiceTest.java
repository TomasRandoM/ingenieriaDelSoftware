package org.ntm.practico1.logic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DomicilioServiceTest {

    @Autowired
    private DomicilioService domicilio;

    @Test
    public void probarCrear() {
        try {
            domicilio.crearDomicilio("hola", 245);
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }
}