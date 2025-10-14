package controllers;

import models.Perfil;
import models.Usuario;
import play.mvc.Before;
import play.mvc.Controller;
import security.Administrador;

public class Seguranca extends Controller{

	@Before
	static void verificarAutenticacao() {
		if (!session.contains("usuarioLogado")) {
			flash.error("Você deve logar no sistema.");
			Logins.form();
		}
	}
	
	@Before
	static void carregarUsuario() {
		if (session.contains("usuarioLogado")) {
			String email = session.get("usuarioLogado");
			Usuario usuario = Usuario.find("byEmail", email).first();
			if (usuario != null) {
				// Disponibiliza o objeto 'usuario' para todas as views
                renderArgs.put("usuario", usuario);
			}
		}
	}
	
	@Before
	static void verificarAdministrador() {
		String perfil = session.get("usuarioPerfil");
		Administrador adminAnnotation = getActionAnnotation(Administrador.class);
		if (adminAnnotation != null && !Perfil.ADMINISTRADOR.name().equals(perfil)) {
			forbidden("Acesso restrito aos administradores do sistema");
		}
	}
}
