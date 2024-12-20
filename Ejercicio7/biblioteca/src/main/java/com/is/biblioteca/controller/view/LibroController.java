package com.is.biblioteca.controller.view;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is.biblioteca.business.domain.entity.Autor;
import com.is.biblioteca.business.domain.entity.Editorial;
import com.is.biblioteca.business.domain.entity.Libro;
import com.is.biblioteca.business.logic.service.AutorService;
import com.is.biblioteca.business.logic.service.EditorialService;
import com.is.biblioteca.business.logic.service.LibroService;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroService libroService;
    
    @Autowired
    private AutorService autorService;
    
    @Autowired
    private EditorialService editorialService;
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////////// VIEW: CREAR LIBRO ////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String irEditAlta(ModelMap modelo) {
    	
       try {	
    	   
        List<Autor> autores = autorService.listarAutor();
        List<Editorial> editoriales = editorialService.listarEditorial();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
        
       }catch(Exception e) {
    	return null;   
       }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String aceptarEditAlta(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            			   	      @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
                                  @RequestParam String idEditorial, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo) {
        try {

        	libroService.crearLibro(archivo,isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El Libro fue cargado correctamente!");

            return "redirect:/regresoPage";
            
        } catch (Exception ex) {
        	
        	List<Autor> autores = new ArrayList();
        	List<Editorial> editoriales = new ArrayList();
        	try {
             autores = autorService.listarAutor();
             editoriales = editorialService.listarEditorial();
        	}catch(Exception w) {} 

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());

            return "libro_form.html";
        }
        
    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////////// VIEW: LISTA LIBROS ///////// 
    //////////////////////////////////////////
    //////////////////////////////////////////

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
    	
       try {
    	   
         List<Libro> libros = libroService.listarLibro();
         modelo.addAttribute("libros", libros);

         return "libro_list";
        
       }catch(Exception e) {
    	 return null;   
       }
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////// VIEW: MODIFICAR LIBRO /////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{isbn}")
    public String irEditModificar(@PathVariable Long isbn, ModelMap modelo) {
      
       try {
    	   
        modelo.put("libro", libroService.buscarLibroPorIsbn(isbn));
        
        List<Autor> autores = autorService.listarAutor();
        List<Editorial> editoriales = editorialService.listarEditorial();
        
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        
        return "libro_modificar.html";
        
       }catch(Exception e) {
      	 return null;   
       } 
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{isbn}")
    public String aceptarEditModificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap modelo, MultipartFile archivo) {
        try {
        	
            List<Autor> autores = autorService.listarAutor();
            List<Editorial> editoriales = editorialService.listarEditorial();
            
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            libroService.modificarLibro(archivo, "", isbn, titulo, ejemplares, idAutor, idEditorial);
            
                        
            return "redirect:/regresoPage";

        } catch (Exception ex) {
        	
        	List<Autor> autores = new ArrayList();
        	List<Editorial> editoriales = new ArrayList();
        	
        	try {
             autores = autorService.listarAutor();
             editoriales = editorialService.listarEditorial();
        	}catch(Exception e) {} 
            
            modelo.put("error", ex.getMessage());
            
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            
            return "libro_modificar.html";
        }

    }
}
