package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.Filme;


public class Application extends Controller {

	    public static void index() {
	        listarLivros();
	    }
	    
	    public static void listarLivros() {
	        List<Filme> filmes = Filme.findAll();
	        render(filmes);
	    }

}