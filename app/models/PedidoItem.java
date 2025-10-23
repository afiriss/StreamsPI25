package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class PedidoItem extends Model {

    // MUITOS itens pertencem a UM pedido.
    @ManyToOne
    public Pedido pedido;

    // MUITOS itens de pedido podem se referir ao MESMO filme.
    @ManyToOne
    public Filme filme;

    public int quantidade;
    public Double precoUnitario; // Armazena o preço do filme no momento da compra

    // Construtor
    public PedidoItem(Pedido pedido, Filme filme, int quantidade) {
        this.pedido = pedido;
        this.filme = filme;
        this.quantidade = quantidade;
        // Guarda o preço atual do filme. Importante caso o preço mude no futuro.
        this.precoUnitario = filme.preco;
    }

    // Calcula o subtotal para este item
    public double getSubtotal() {
        // Verifica se precoUnitario não é null para evitar NullPointerException
        return (precoUnitario != null ? precoUnitario : 0.0) * quantidade;
    }
}