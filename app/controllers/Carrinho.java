package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.CarrinhoItem;
import models.Filme;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Carrinho extends Controller {

    private static Usuario getUsuarioLogado() {
        String emailUsuario = session.get("usuarioLogado");
        if (emailUsuario == null) return null;
        Usuario u = Usuario.find("byEmail", emailUsuario).first();
        if (u == null) {
            session.clear();
            Logins.form();
        }
        return u;
    }

    // Método auxiliar para contar itens totais do usuário
    private static int contarItensTotal(Usuario u) {
        int total = 0;
        if (u != null && u.carrinho != null) {
            for (CarrinhoItem item : u.carrinho) {
                total += item.quantidade;
            }
        }
        return total;
    }

    public static void adicionar(long id) {
        Usuario usuarioLogado = getUsuarioLogado();
        Filme filme = Filme.findById(id);

        if (usuarioLogado != null && filme != null) {
            CarrinhoItem itemExistente = null;
            for (CarrinhoItem item : usuarioLogado.carrinho) {
                if (item.filme.id.equals(id)) {
                    itemExistente = item;
                    break;
                }
            }

            if (itemExistente != null) {
                itemExistente.quantidade++;
                itemExistente.save();
            } else {
                CarrinhoItem novoItem = new CarrinhoItem(usuarioLogado, filme, 1);
                usuarioLogado.carrinho.add(novoItem);
                usuarioLogado.save();
            }
        }

        if (request.isAjax()) {
            // Retorna JSON com mensagem e o novo total para o contador
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("mensagem", "Filme adicionado com sucesso!");
            json.put("totalItens", contarItensTotal(usuarioLogado));
            renderJSON(json);
        }
        
        if (usuarioLogado == null) return;
        ver();
    }

    public static void remover(Long idDoItem) {
        Usuario usuarioLogado = getUsuarioLogado();
        CarrinhoItem item = CarrinhoItem.findById(idDoItem);

        if (usuarioLogado == null || item == null) return;

        boolean foiRemovidoTotalmente = false;

        // LÓGICA DE DECREMENTO
        if (item.quantidade > 1) {
            item.quantidade--;
            item.save();
        } else {
            usuarioLogado.carrinho.remove(item);
            usuarioLogado.save();
            item.delete();
            foiRemovidoTotalmente = true;
        }

        // Calcula novo valor total da compra (R$)
        double valorTotalCompra = 0;
        for (CarrinhoItem i : usuarioLogado.carrinho) {
            valorTotalCompra += i.getSubtotal();
        }

        if (request.isAjax()) {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("removido", foiRemovidoTotalmente);
            json.put("totalItens", contarItensTotal(usuarioLogado)); // Para atualizar o menu
            json.put("valorTotalCompra", valorTotalCompra); // Para atualizar o total R$
            
            if (!foiRemovidoTotalmente) {
                json.put("novaQtd", item.quantidade);
                json.put("novoSubtotal", item.getSubtotal());
            }
            renderJSON(json);
        } else {
            ver();
        }
    }

    public static void ver() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            session.clear();
            Logins.form();
            return;
        }
        List<CarrinhoItem> itens = usuarioLogado.carrinho;
        double total = 0;
        for (CarrinhoItem item : itens) {
            total += item.getSubtotal();
        }
        render(itens, total);
    }

    public static void limpar() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado != null) {
            for (CarrinhoItem item : usuarioLogado.carrinho) {
                item.delete();
            }
            usuarioLogado.carrinho.clear();
            usuarioLogado.save();
        }
        
        if (request.isAjax()) {
            renderText("Carrinho limpo");
        } else {
            ver();
        }
    }
}