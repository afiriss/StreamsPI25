package controllers;

import models.Usuario;
import play.mvc.Controller;

public class Logins extends Controller{
	
	public static void form() {
		render();
	}
	
	public static void logar(String email, String senha) {
		Usuario usuario = Usuario.find("email = ?1 and senha = ?2", email, senha).first();
		if (usuario == null) {
			flash.error("Email ou senha inválidos");
			form();
		}else {
			session.put("usuarioLogado", usuario.email);
			session.put("usuarioPerfil", usuario.perfil.name());
			flash.success("Logado com sucesso!");
			Filmes.listar();
			Usuarios.listar(null);
		}
	}
	
	public static void logout() {
		session.clear();
		flash.success("Você saiu do sistema!");
		form();
	}
}
