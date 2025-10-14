package controllers;

import java.util.List;
import models.Filme;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Filmes extends Controller {

    public static void listar() {
        // Busca os filmes de cada gênero separadamente
        
        // CORREÇÃO: Trocado '?' por '?1' em todas as consultas
        List<Filme> filmesRomance = Filme.find("genero = ?1 order by nome", "romance").fetch();
        List<Filme> filmesComedia = Filme.find("genero = ?1 order by nome", "comedia").fetch();
        List<Filme> filmesTerror = Filme.find("genero = ?1 order by nome", "terror").fetch();
        
        // Envia as três listas para a view
        render(filmesRomance, filmesComedia, filmesTerror);
    }
}