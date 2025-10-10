package controllers;

import java.util.List;
import models.Filme;
import models.Status;
import models.Usuario;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.With;
import security.Administrador;


public class Usuarios extends Controller {

    // Este método é PÚBLICO e renderiza o formulário de cadastro.
    public static void form() {
        List<Filme> filmes = Filme.findAll();
        render(filmes);
    }

    // Este método também é PÚBLICO para permitir que novos usuários se cadastrem.
    @Administrador
    public static void salvar(Usuario u, String confirmacaoSenha) {
        // Validação da senha
        if (u.id == null || (u.senha != null && !u.senha.isEmpty())) {
            validation.required("Senha", u.senha);
            validation.equals("confirmacaoSenha", u.senha, "Confirmação de Senha", confirmacaoSenha).message("As senhas não conferem.");
        } else {
            if (u.id != null) {
                Usuario usuarioDB = Usuario.findById(u.id);
                u.senha = usuarioDB.senha;
            }
        }

        if (validation.hasErrors()) {
            List<Filme> filmes = Filme.findAll();
            flash.error("Verifique os erros no formulário.");
            renderTemplate("Usuarios/form.html", u, filmes);
        }

        if (u.nome != null) {
            u.nome = u.nome.toUpperCase();
        }
        if (u.email != null) {
            u.email = u.email.toUpperCase();
        }

        u.save();

        if (u.id == null) {
             flash.success("Cadastro realizado com sucesso! Faça o login.");
             Index.form(); 
        } else {
            // Se for uma edição, redireciona para a lista de filmes.
            flash.success("Usuário salvo com sucesso!");
            Filmes.listar();
        }
    }


    @Administrador
    public static void listar(String termo) {
        List<Usuario> usuarios = null;
        if (termo == null) {
            usuarios = Usuario.find("status <> ?1", Status.INATIVO).fetch();
        } else {
            usuarios = Usuario.find("(lower(nome) like ?1 or lower(email) like ?1) and status <> ?2",
                    "%" + termo.toLowerCase() + "%",
                    Status.INATIVO).fetch();
        }
        render(usuarios, termo);
    }

    @Administrador
    public static void detalhar(Usuario u) {
        render(u);
    }

 
    @Administrador
    public static void editar(Long id) {
        Usuario u = Usuario.findById(id);
        List<Filme> filmes = Filme.findAll();
        renderTemplate("Usuarios/form.html", u, filmes);
    }

    @Administrador
    public static void remover(long id) {
        Usuario usuario = Usuario.findById(id);
        usuario.status = Status.INATIVO;
        usuario.save();
        listar(null);
    }
}