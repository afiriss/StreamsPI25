package controllers;

import java.util.List;

import models.Filme;
import models.Usuario;
import models.Usuario;
import play.mvc.Controller;

public class Usuarios extends Controller{
	
	public static void form() {
		render();
	}

	public static void listar(String termo) {
		List<Usuario> usuarios = null;
		if (termo == null) {
			usuarios = Usuario.findAll();	
		} else {
			usuarios = Usuario.find("lower(nome) like ?1 "
					+ "or lower(email) like ?1",
					"%" + termo.toLowerCase() + "%").fetch();
		}
		render(usuarios, termo);
	}
	
	public static void detalhar(Usuario usuario) {
		render(usuario);
		detalhar(usuario);
	}
	
	public static void editar(Long id) {
		Usuario u = Usuario.findById(id);
		List<Filme> filmes = Filme.findAll();
		
		renderTemplate("Usuario/form.html", filmes);
	}
	
	public static void salvar(Usuario u) {
		if (u.nome != null) {
			u.nome = u.nome.toUpperCase();
		}
		if (u.email != null) {
			u.email = u.email.toUpperCase();
		}
		if(u.telefone != null) {
			u.telefone = u.telefone;
		}
		u.save();
		detalhar(u);
	}
	
	public static void remover(long id) {
		Usuario usuario = Usuario.findById(id);
		usuario.delete();
		listar(null);
	}
	
	
}
