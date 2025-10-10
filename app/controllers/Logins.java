package controllers;

import models.Usuario;
import play.mvc.Controller;

public class Logins extends Controller{
	
	 public static void form() {
	        render();
	    }

	    public static void logar(String email, String senha) {
	        // --- INÍCIO DA MODIFICAÇÃO ---

	        // 1. Converte o e-mail digitado para letras maiúsculas.
	        // Isso é crucial porque o e-mail no banco de dados também está em maiúsculas.
	        // Sem isso, "teste@email.com" seria diferente de "TESTE@EMAIL.COM".
	        String emailMaiusculo = email != null ? email.toUpperCase() : null;

	        // 2. Busca o usuário no banco de dados usando o e-mail em maiúsculas e a senha.
	        // A consulta JPQL ("email = ?1 and senha = ?2") é a forma como buscamos
	        // um registro na tabela de Usuários que corresponda a ambos os critérios.
	        Usuario usuario = Usuario.find("email = ?1 and senha = ?2", emailMaiusculo, senha).first();

	        // --- FIM DA MODIFICAÇÃO ---

	        if (usuario == null) {
	            // Se nenhum usuário for encontrado, exibe uma mensagem de erro.
	            flash.error("Email ou senha inválidos");
	            form();
	        } else {
	            // Se o usuário for encontrado, armazena suas informações na sessão.
	            session.put("usuarioLogado", usuario.email);
	            session.put("usuarioPerfil", usuario.perfil.name());
	            flash.success("Logado com sucesso!");
	            
	            // Redireciona para a lista de filmes.
	            Filmes.listar();
	        }
	    }

	
	    public static void logout() {
	        // Limpa todas as informações da sessão do usuário
	        session.clear();
	        
	        // Adiciona uma mensagem de sucesso para ser exibida na próxima página
	        flash.success("Você saiu do sistema!");
	        
	        // --- INÍCIO DA MODIFICAÇÃO ---
	        // Em vez de chamar o form() local, agora chamamos a ação form()
	        // do controlador Indexprincipal, que é a sua página inicial.
	        Index.form();
	     
	    }
}