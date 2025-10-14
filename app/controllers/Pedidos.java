package controllers;

import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Pedidos extends Controller{

	 public static void finalizar() {
	        // Futuramente, aqui entraria a lógica de pagamento e gravação do pedido no banco.
	        
	        // Por enquanto, vamos limpar o carrinho do usuário.
	        Carrinho.limpar();
	        
	        flash.success("Compra finalizada com sucesso! Obrigado por comprar conosco.");
	        
	        // Redireciona o usuário de volta para o catálogo de filmes.
	        Filmes.listar();
	    }
	
}
