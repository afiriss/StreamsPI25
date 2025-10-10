package controllers;

import java.util.List;

import models.Filme;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Filmes extends Controller{

	public static void listar() {
		List<Filme> filmes = Filme.findAll();
		render(filmes);
	}
	
}
