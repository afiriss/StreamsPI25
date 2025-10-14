package controllers;

import java.util.List;
import models.Filme;
import models.Status;
import models.Usuario;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.With;
import security.Administrador;
import play.data.validation.Valid; // Importar @Valid

public class Usuarios extends Controller {

    public static void form() {
        render();
    }

    // A anotação @Administrador foi removida temporariamente para permitir o cadastro por novos usuários
    // Se quiser que apenas administradores possam cadastrar, adicione @Administrador novamente
    public static void salvar(@Valid Usuario u, String confirmacaoSenha) {
        // Validação da confirmação de senha (continua necessária)
        if (u.senha != null && !u.senha.isEmpty()) {
            validation.equals("confirmacaoSenha", u.senha, "Confirmação de Senha", confirmacaoSenha).message("As senhas não conferem.");
        }

        if (validation.hasErrors()) {
            // Mantém os dados no formulário em caso de erro
            params.flash(); 
            // Mantém os erros de validação para a próxima página
            validation.keep(); 
            form();
        }

        // Converte para maiúsculas após a validação
        u.nome = u.nome.toUpperCase();
        u.email = u.email.toUpperCase();

        u.save();

        flash.success("Cadastro realizado com sucesso! Faça o login.");
        Logins.form(); // Redireciona para a tela de login
    }

    @Administrador
    public static void listar(String termo) {
        List<Usuario> usuarios;
        if (termo == null || termo.isEmpty()) {
            usuarios = Usuario.find("status <> ?1", Status.INATIVO).fetch();
        } else {
            usuarios = Usuario.find("(lower(nome) like ?1 or lower(email) like ?1) and status <> ?2",
                    "%" + termo.toLowerCase() + "%",
                    Status.INATIVO).fetch();
        }
        render(usuarios, termo);
    }
    
    @Administrador
    public static void editar(Long id) {
        Usuario u = Usuario.findById(id);
        renderTemplate("Usuarios/form.html", u);
    }

    @Administrador
    public static void remover(long id) {
        Usuario usuario = Usuario.findById(id);
        if (usuario != null) {
            usuario.status = Status.INATIVO;
            usuario.save();
        }
        listar(null);
    }
}