package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;


@Entity
public class Filme extends Model{
	public String nome;
	public String genero;
	public Double preco;
	
    //    O atributo "mappedBy" indica que a relação já foi configurada na classe Usuario,
    //    no atributo "filmes". Isso evita que o banco de dados crie tabelas duplicadas
    //    para gerenciar a relação.
	
	public String imagemUrl; // Guarda o caminho para a imagem do filme
	
    @ManyToMany(mappedBy = "filmes")
    // Criamos uma lista de Usuarios para que um filme possa "saber" quem o favoritou.
    public List<Usuario> usuarios;
	
	public Filme() {
		//Também inicializamos a lista aqui, pela mesma razão da classe Usuario.
        this.usuarios = new ArrayList<>();
	}
	
	public Filme(String nome, String genero, Double preco, String imagemUrl) {
		this(); // Chama o construtor padrão para inicializar a lista.
		this.nome = nome;
		this.genero = genero;
		this.preco = preco;
		this.imagemUrl = imagemUrl;
		}
	
	@Override
	public String toString() {
		return nome + " ("+ genero + ")";
	}
}
