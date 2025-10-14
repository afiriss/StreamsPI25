package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Usuario extends Model{
	
	public String nome;
	public String email;
	public String telefone;
	public String senha;
	
	@Enumerated(EnumType.STRING)
	public Status status;

	@Enumerated(EnumType.STRING)
	public Perfil perfil;
	
	//	Um usuário TEM MUITOS itens no carrinho (OneToMany).
    //    'cascade = CascadeType.ALL' significa: se eu salvar, alterar ou deletar um usuário,
    //    faça o mesmo com os itens de carrinho associados a ele. Facilita muito a vida.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    public List<CarrinhoItem> carrinho; // O carrinho de COMPRAS do usuário
	
	public Usuario() {
		this.status = Status.ATIVO;
		this.perfil = Perfil.ADMINISTRADOR;
		//a lista para evitar erros (NullPointerException).
        //    Assim, todo novo usuário já começa com uma lista de filmes vazia.
		this.filmes = new ArrayList<>();
		this.carrinho = new ArrayList<>();
	}
	
	@ManyToMany
	//O atributo 'filme' (singular) virou 'filmes' (plural) para guardar uma lista.
    //    Em vez de um único objeto Filme, teremos uma coleção.
	public List<Filme> filmes;
}

