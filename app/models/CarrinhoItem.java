package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class CarrinhoItem extends Model {

    // MUITOS itens de carrinho pertencem a UM usuário.
    @ManyToOne
    public Usuario usuario;
  
    // MUITOS itens de carrinho podem se referir ao MESMO filme.
	@ManyToOne
    // --- FIM DA NOVA LÓGICA ---
	public Filme filme;
	public int quantidade;
    
    public CarrinhoItem(Usuario usuario, Filme filme, int quantidade) {
    	this.usuario = usuario;
        this.filme = filme;
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return filme.preco * quantidade;
    }

}
