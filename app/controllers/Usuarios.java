package controllers;

import java.util.List;

import models.Departamento;
import models.Pessoa;
import models.Usuario;
import play.mvc.Controller;

public class Usuarios extends Controller{
	
	public static void form() {
		render();
	}

	public static void listar() {
		List<Usuario> usuarios = Usuario.findAll();
		render(usuarios);
	}
	
	public static void detalhar(Usuario usuario) {
		render(usuario);
		detalhar(usuario);
	}
	
	public static void editar(Long id) {
		Usuario usuario = usuario.findById(id);
		List<Departamento> departamentos = Departamento.findAll();
		
		renderTemplate("Pessoas/form.html", p, departamentos);
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
		listar();
	}
	
	public static void remover(long id) {
		Usuario usuario = Usuario.findById(id);
		usuario.delete();
		listar();
	}
	
	
}
