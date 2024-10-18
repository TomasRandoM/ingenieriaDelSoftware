package com.ingenieriaSoftware.videojuegos.controller;

import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;
import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import com.ingenieriaSoftware.videojuegos.business.logic.CategoriaService;
import com.ingenieriaSoftware.videojuegos.business.logic.EstudioService;
import com.ingenieriaSoftware.videojuegos.business.logic.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class TemplateInicioController {

    @Autowired
    private VideojuegoService videojuegoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EstudioService estudioService;

    @GetMapping("/home")
    public String inicio(Model model) {
        try {
            Collection<Videojuego> colVideo = videojuegoService.listarVideojuegos();
            Collection<Categoria> categorias = categoriaService.listarCategorias().stream().limit(4).collect(Collectors.toList());
            Collection<Estudio> estudios = estudioService.listarEstudios();
            model.addAttribute("videojuegos", colVideo);
            model.addAttribute("categorias", categorias);
            model.addAttribute("estudios", estudios);
            return "view/index";
        } catch (Exception ex) {
            return "error";
        }
    }

}
