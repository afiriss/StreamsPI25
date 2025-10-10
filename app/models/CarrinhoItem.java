package models;

import play.db.jpa.Model;

public class CarrinhoItem extends Model {
	public Filme filme;
    public int quantidade;

    public CarrinhoItem(Filme filme, int quantidade) {
        this.filme = filme;
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return filme.preco * quantidade;
    }

}
