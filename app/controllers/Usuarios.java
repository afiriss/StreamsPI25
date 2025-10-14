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

    public static void form() {
        // --- INÍCIO DA MODIFICAÇÃO ---
        // A linha que busca todos os filmes foi removida, pois não é mais necessária aqui.
        // List<Filme> filmes = Filme.findAll();
        render(); // Agora o render é chamado sem passar a lista de filmes.
        // --- FIM DA MODIFICAÇÃO ---
    }

    // O método 'salvar' não precisa de alterações. Se o campo não vem do formulário,
    // o Play simplesmente não tentará associar nenhum filme, o que é o comportamento desejado.
    @Administrador
    public static void salvar(Usuario u, String confirmacaoSenha) {
        // ... (código de validação da senha continua o mesmo)
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
            // --- MODIFICAÇÃO AQUI TAMBÉM ---
            // Se a validação falhar, precisamos renderizar o formulário novamente.
            // Como o formulário não usa mais a variável 'filmes', não precisamos buscá-la.
            renderTemplate("Usuarios/form.html", u);
            // --- FIM DA MODIFICAÇÃO ---
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
    public static void detalhar(Long id) {
        Usuario u = Usuario.findById(id);
        render(u);
    }

 
    @Administrador
    public static void editar(Long id) {
        Usuario u = Usuario.findById(id);
        // --- INÍCIO DA MODIFICAÇÃO ---
        // A linha que busca todos os filmes foi removida aqui também.
        // List<Filme> filmes = Filme.findAll();
        renderTemplate("Usuarios/form.html", u);
        // --- FIM DA MODIFICAÇÃO ---
    }

    @Administrador
    public static void remover(long id) {
        Usuario usuario = Usuario.findById(id);
        usuario.status = Status.INATIVO;
        usuario.save();
        listar(null);
    }
}