package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import play.db.jpa.Model;

@Entity
public class Usuario extends Model{
	
	public String nome;
	public String email;
	public String telefone;
	
	@Enumerated(EnumType.STRING)
	public Status status;

	public Pessoa() {
		this.status = Status.ATIVO;
	}
	
}

