// app/controllers/Pedidos.java
package controllers;

import java.util.ArrayList; // Importa ArrayList
import java.util.List;      // Importa List
import models.CarrinhoItem;
import models.Pedido;       // Importa Pedido
import models.PedidoItem;   // Importa PedidoItem
import models.Usuario;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class) // Garante que o usuário está logado
public class Pedidos extends Controller {

     // Método auxiliar para buscar o usuário logado
     private static Usuario getUsuarioLogado() {
        String emailUsuario = session.get("usuarioLogado");
        if (emailUsuario == null) {
            // Se não encontrar o email na sessão, redireciona para login
            Logins.form();
            return null; // Retorna null para indicar que o usuário não foi encontrado
        }
        Usuario usuario = Usuario.find("byEmail", emailUsuario).first();
        if (usuario == null) {
             // Se o email da sessão não corresponder a um usuário válido, limpa a sessão e redireciona
             session.clear();
             Logins.form();
             return null;
        }
        return usuario;
    }

     public static void finalizar() {
        Usuario usuarioLogado = getUsuarioLogado();

        // Verifica se o usuário foi encontrado e se o carrinho não está vazio
        if (usuarioLogado == null) {
             // getUsuarioLogado já redirecionou, apenas interrompe
             return;
        }

        if (usuarioLogado.carrinho == null || usuarioLogado.carrinho.isEmpty()) {
            flash.error("Seu carrinho está vazio.");
            // Redireciona de volta para o carrinho
            Carrinho.ver();
            return; // Interrompe a execução
        }

        // --- Lógica de Persistência do Pedido ---

        // 1. Criar um novo Pedido associado ao usuário logado
        Pedido novoPedido = new Pedido(usuarioLogado);
        // A lista 'itens' já é inicializada no construtor de Pedido

        // 2. Criar uma cópia da lista de itens do carrinho para iterar com segurança
        //    Isso é importante porque vamos modificar a lista original (usuarioLogado.carrinho)
        List<CarrinhoItem> itensCarrinhoCopia = new ArrayList<>(usuarioLogado.carrinho);

        // 3. Transferir itens do Carrinho (da cópia) para PedidoItem
        for (CarrinhoItem itemCarrinho : itensCarrinhoCopia) {
            if (itemCarrinho.filme != null) { // Garante que o filme associado existe
                // Cria um novo item de pedido
                PedidoItem itemPedido = new PedidoItem(novoPedido, itemCarrinho.filme, itemCarrinho.quantidade);
                // Adiciona o item à lista de itens do novo pedido
                novoPedido.itens.add(itemPedido);
            } else {
                 // Log ou tratamento de erro caso um item de carrinho esteja sem filme (improvável, mas seguro verificar)
                 System.out.println("WARN: Item de carrinho com ID " + itemCarrinho.id + " sem filme associado foi ignorado.");
            }
        }

        // 4. Calcular o valor total do pedido
        novoPedido.calcularTotal(); // Usa o método auxiliar do modelo Pedido

        // 5. Salvar o Pedido no banco de dados
        //    Devido ao CascadeType.ALL na relação Pedido -> PedidoItem,
        //    os PedidoItens serão salvos automaticamente junto com o Pedido.
        try {
            novoPedido.save();
        } catch (Exception e) {
            flash.error("Erro ao salvar o pedido no banco de dados: " + e.getMessage());
            // Talvez redirecionar para uma página de erro ou de volta ao carrinho
            Carrinho.ver();
            return;
        }


        // 6. Limpar o carrinho do usuário após salvar o pedido com sucesso
        //    Itera sobre a cópia e remove/deleta os itens originais
        for (CarrinhoItem itemCarrinhoParaRemover : itensCarrinhoCopia) {
             // Remove o item da coleção gerenciada pelo JPA dentro do objeto usuarioLogado
             usuarioLogado.carrinho.remove(itemCarrinhoParaRemover);
             // Deleta o item do carrinho do banco de dados explicitamente
             try {
                itemCarrinhoParaRemover.delete();
             } catch (Exception e) {
                 // Log ou tratamento de erro caso a deleção falhe
                 System.out.println("WARN: Falha ao deletar CarrinhoItem com ID " + itemCarrinhoParaRemover.id + ": " + e.getMessage());
             }
        }
        // Salva o usuário para persistir a remoção dos itens da sua lista de carrinho
         try {
            usuarioLogado.save();
        } catch (Exception e) {
             flash.error("Erro ao atualizar o carrinho do usuário: " + e.getMessage());
             // Mesmo com erro aqui, o pedido foi salvo. Informar o usuário pode ser necessário.
             Filmes.listar(); // Redireciona mesmo assim
             return;
        }


        // 7. Mensagem de sucesso e redirecionamento
        flash.success("Compra finalizada com sucesso! Número do pedido: " + novoPedido.id + ". Obrigado por comprar conosco.");

        // Redireciona o usuário de volta para o catálogo de filmes (ou para uma página de "Meus Pedidos" se existir).
        Filmes.listar();
    }

}