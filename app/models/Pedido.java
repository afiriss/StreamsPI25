// app/models/Pedido.java
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;

@Entity
public class Pedido extends Model {

    @ManyToOne // MUITOS pedidos podem pertencer a UM usuário
    public Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP) // Guarda data e hora
    public Date dataPedido;

    public Double valorTotal;

    // UM pedido tem MUITOS itens de pedido.
    // CascadeType.ALL significa que salvar/deletar um Pedido também afetará seus PedidoItens.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    public List<PedidoItem> itens;

    // Construtor
    public Pedido(Usuario usuario) {
        this.usuario = usuario;
        this.dataPedido = new Date(); // Define a data/hora atual no momento da criação
        this.itens = new ArrayList<>(); // Inicializa a lista de itens
        this.valorTotal = 0.0; // Inicializa o valor total
    }

    // Método auxiliar para recalcular o total (pode ser útil)
    public void calcularTotal() {
        this.valorTotal = 0.0;
        if (this.itens != null) {
            for (PedidoItem item : itens) {
                this.valorTotal += item.getSubtotal();
            }
        }
    }
}