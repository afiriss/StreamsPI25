package controllers;

import java.util.ArrayList;
import java.util.List;

import models.CarrinhoItem;
import models.Filme;
import play.mvc.Controller;

public class CarrinhoItens extends Controller{
	

	private static List<CarrinhoItem> getCarrinhoDaSessao() {
        Object cachedCarrinho = play.cache.Cache.get(session.getId());
        if (cachedCarrinho != null && cachedCarrinho instanceof List) {
            return (List<CarrinhoItem>) cachedCarrinho;
        }
        return new ArrayList<>();
    }

    public static void adicionar(Long id) {
        Filme filme = Filme.findById(id);
        if (filme != null) {
            List<CarrinhoItem> itens = getCarrinhoDaSessao();
            
            boolean found = false;
            for (CarrinhoItem item : itens) {
                if (item.filme.id.equals(id)) {
                    item.quantidade++;
                    found = true;
                    break;
                }
            }

            if (!found) {
                itens.add(new CarrinhoItem(filme, 1));
            }
            
            play.cache.Cache.set(session.getId(), itens, "30mn");
        }
        ver(); // Redireciona para a página do carrinho
    }

    public static void ver() {
        List<CarrinhoItem> itens = getCarrinhoDaSessao();
        
        double total = 0;
        for (CarrinhoItem item : itens) {
            total += item.getSubtotal();
        }
        
        render(itens, total);
    }
    
    public static void remover(Long id) {
        List<CarrinhoItem> itens = getCarrinhoDaSessao();
            
        // Usar um Iterator é mais seguro para remover itens de uma lista
        itens.removeIf(item -> item.filme.id.equals(id));
        
        play.cache.Cache.set(session.getId(), itens, "30mn");
        ver();
    }

    public static void limpar() {
        play.cache.Cache.delete(session.getId());
        ver();
    }
}


