package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.*; // Importar validações
import play.db.jpa.Model;

@Entity
public class Usuario extends Model{
	
	@Required(message="O nome é um campo obrigatório")
	public String nome;
	
	@Required(message="O e-mail é um campo obrigatório")
	@Email(message="Forneça um e-mail válido")
	public String email;
	
	public String telefone;
	
	@Required(message="A senha é um campo obrigatório")
	@MinSize(value=6, message="A senha deve ter no mínimo 6 caracteres")
	public String senha;
	
	@Enumerated(EnumType.STRING)
	public Status status;

	@Enumerated(EnumType.STRING)
	public Perfil perfil;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	public List<CarrinhoItem> carrinho;
	
	public Usuario() {
		this.status = Status.ATIVO;
		this.perfil = Perfil.ADMINISTRADOR;
		this.filmes = new ArrayList<>();
		this.carrinho = new ArrayList<>();
	}
	
	@ManyToMany
	public List<Filme> filmes;
}