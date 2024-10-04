package org.ntm;

import org.ntm.domain.entity.*;
import org.ntm.logic.*;

public class Main {
    public static void main(String[] args) {

        DomicilioService domSer = new DomicilioService();
        CategoriaService catSer = new CategoriaService();
        ArticuloService artSer = new ArticuloService();
        FacturaService fSer = new FacturaService();
        ClienteService cliSer = new ClienteService();

        try {
            Domicilio dom = domSer.crearDomicilio("San Martin", 24);
            Categoria cat = catSer.crearCategoria("Tecnología");
            Articulo art = artSer.crearArticulo(1, "Celular A24", 34000);
            Cliente cli = cliSer.crearCliente("Leandro", "Spadaro", 25252525, dom.getId());
            Factura f = fSer.crearFactura("04/10/2024", 1, 34000, cli.getId());
            catSer.agregarArticulo(art.getId(), cat.getId());
            fSer.agregarDetalle(1, 34000, art.getId(), f.getId());

        } catch (Exception ex) {
            System.out.println("Error");
        }
    }
}