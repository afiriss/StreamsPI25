package controllers;

import java.util.ArrayList;
import java.util.List;

import models.CarrinhoItem;
import models.Filme;
import play.mvc.Controller;

public class CarrinhoItens extends Controller{
	

	public static void adicionar(Long id) {
        Filme filme = Filme.findById(id);
        if (filme != null) {
            List<CarrinhoItem> itens = session.get("carrinho") != null ? 
                (List<CarrinhoItem>) play.cache.Cache.get(session.getId()) : new ArrayList<>();
            
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
        session.put("carrinho", "true");
        }
        
     // Redireciona para a página do carrinho
        ver();
	}
	
	// pagina que mostra o carrinho
	 public static void ver() {
	        List<CarrinhoItem> itens = session.get("carrinho") != null ? 
	            (List<CarrinhoItem>) play.cache.Cache.get(session.getId()) : new ArrayList<>();
	        
	        double total = 0;
	        for (CarrinhoItem item : itens) {
	            total += item.getSubtotal();
	        }
	        
	        render(itens, total);
	    }
	 
	 //remove item do carrinho
	 public static void remover(Long id) {
	        List<CarrinhoItem> itens = session.get("carrinho") != null ? 
	            (List<CarrinhoItem>) play.cache.Cache.get(session.getId()) : new ArrayList<>();
	            
	        for (int i = 0; i < itens.size(); i++) {
	            if (itens.get(i).filme.id.equals(id)) {
	                itens.remove(i);
	                break;
	            }
	        }
	        
	        play.cache.Cache.set(session.getId(), itens, "30mn");
	        ver();
	    }

        //remove tudo do carrinho	 
 	    public static void limpar() {
	        play.cache.Cache.delete(session.getId());
	        session.remove("carrinho");
	        ver();
	    }
}


