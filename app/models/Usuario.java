package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

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
	
	public Usuario() {
		this.status = Status.ATIVO;
		this.perfil = Perfil.ADMINISTRADOR;
	}
	
	@ManyToOne
	public Filme filme;
}

