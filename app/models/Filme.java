package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Filme extends Model{
	public String nome;
	public String genero;
	
	public Filme() {
		
	}
	
	public Filme(String nome, String genero) {
		this.nome = nome;
		this.genero = genero;
	}
	
	@Override
	public String toString() {
		return nome + " ("+ genero + ")";
	}
}
