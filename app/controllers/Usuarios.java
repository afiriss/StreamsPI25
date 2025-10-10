package controllers;

import java.util.List;

import models.Filme;
import models.Status;
import models.Usuario;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.With;
import security.Administrador;

@With(Seguranca.class)
public class Usuarios extends Controller{
	
	@Administrador
	public static void form() {
		List<Filme> filmes = Filme.findAll();
		render(filmes);
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
	
	public static void detalhar(Usuario u) {
		render(u);
	}
	
	public static void editar(Long id) {
		Usuario u = Usuario.findById(id);
		
		List <Filme> filmes = Filme.findAll();
		
		renderTemplate("Usuarios/form.html", u, filmes);
	}
	
	@Administrador
	public static void salvar(Usuario u, String confirmacaoSenha) {
	    // Validação para um novo usuário OU para quando a senha está sendo alterada em uma edição
	    if (u.id == null || (u.senha != null && !u.senha.isEmpty())) {
	        validation.required("Senha", u.senha);
	        validation.equals("confirmacaoSenha", u.senha, "Confirmação de Senha", confirmacaoSenha).message("As senhas não conferem.");
	    } else {
	        // Se for uma edição e o campo senha estiver vazio, mantém a senha antiga do banco de dados.
	        Usuario usuarioDB = Usuario.findById(u.id);
	        u.senha = usuarioDB.senha;
	    }

	    // Se houver qualquer erro de validação, renderiza o formulário novamente
	    if (validation.hasErrors()) {
	        List<Filme> filmes = Filme.findAll();
	        flash.error("Verifique os erros no formulário.");
	        renderTemplate("Usuarios/form.html", u, filmes);
	    }
	    
	    // Deixa os campos em maiúsculo
	    if (u.nome != null) {
	        u.nome = u.nome.toUpperCase();
	    }
	    if (u.email != null) {
	        u.email = u.email.toUpperCase();
	    }
	    
	    // Salva o usuário
	    u.save();
	    
	    // Exibe uma mensagem de sucesso e redireciona para a lista
	    flash.success("Usuário salvo com sucesso!");
	    listar(null);
	}
	
	public static void remover(long id) {
		Usuario usuario = Usuario.findById(id);
		usuario.status = Status.INATIVO;
		usuario.save();
		listar(null);
	}
	
	
}
