package controllers;

import java.util.List;

import models.Filme;
import models.Status;
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
			usuarios = Usuario.find("status <> ?1", Status.INATIVO).fetch();
		} else {
			usuarios = Usuario.find("(lower(nome) like ?1 "
					+ "or lower(email) like ?1) and status <> ?2",
					"%" + termo.toLowerCase() + "%",
					Status.INATIVO).fetch();
		}
		render(usuarios, termo);
	}
	
	public static void editar(Long id) {
		Usuario u = Usuario.findById(id);
		
		renderTemplate("Usuarios/form.html", u);
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
		listar(null);
	}
	
	public static void remover(long id) {
		Usuario usuario = Usuario.findById(id);
		usuario.status = Status.INATIVO;
		usuario.save();
		listar(null);
	}
	
	
}
