package controllers;

import java.util.List;

import models.CarrinhoItem;
import models.Filme;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.With;


@With(Seguranca.class)
public class Carrinho extends Controller {
	// Método para buscar o usuário que está logado na sessão atual
	private static Usuario getUsuarioLogado() {
		String emailUsuario = session.get("usuarioLogado");
		if (emailUsuario == null) {
			return null;
		}
		return Usuario.find("byEmail", emailUsuario).first();
	}

	public static void adicionar(long id) {
		Usuario usuarioLogado = getUsuarioLogado();
		Filme filmeParaAdicionar = Filme.findById(id);

		if (usuarioLogado != null && filmeParaAdicionar != null) {
			// verifica se o filme já está no carrinho do usuario
			CarrinhoItem itemExistente = null;
			for (CarrinhoItem item : usuarioLogado.carrinho) {
				if (item.filme.id.equals(id)) {
					itemExistente = item;
					break;
				}
			}

			if (itemExistente != null) {
				// se já existe, apenas incrementa a quant
				itemExistente.quantidade++;
				itemExistente.save(); // salva a alteração no item
			}else {
				// se não exite, cria um novo item no carrinho
				CarrinhoItem novoItem = new CarrinhoItem(usuarioLogado, filmeParaAdicionar, 1);
				// novoItem.usuario = usuarioLogado;
				 usuarioLogado.carrinho.add(novoItem); // Adiciona na lista do usuário
	                usuarioLogado.save(); // Salva o usuário, o que também salva o novo item por causa do 'cascade'
	            }
	        }
		
		if(request.isAjax()) {
			renderText("Filme adicionado ao carrinho com sucesso!");
		}
	        // Redireciona para a tela que mostra o carrinho
	        ver();
	    }

	    public static void ver() {
	        Usuario usuarioLogado = getUsuarioLogado();
	        List<CarrinhoItem> itens = usuarioLogado.carrinho;

	        double total = 0;
	        for (CarrinhoItem item : itens) {
	            total += item.getSubtotal();
	        }

	        render(itens, total);
	    }

	    public static void remover(Long idDoItem) {
	        Usuario usuarioLogado = getUsuarioLogado();
	        CarrinhoItem itemParaRemover = CarrinhoItem.findById(idDoItem);

	        // Verifica se o item existe e se realmente pertence ao usuário logado (segurança)
	        if (itemParaRemover != null && itemParaRemover.usuario.id.equals(usuarioLogado.id)) {
	            usuarioLogado.carrinho.remove(itemParaRemover); // Remove da lista
	            usuarioLogado.save(); // Salva a alteração no usuário
	            itemParaRemover.delete(); // Deleta o item do banco de dados
	        }
	        ver();
	    }

	    public static void limpar() {
	        Usuario usuarioLogado = getUsuarioLogado();
	        // Para cada item, deleta ele do banco
	        for (CarrinhoItem item : usuarioLogado.carrinho) {
	            item.delete();
	        }
	        usuarioLogado.carrinho.clear(); // Limpa a lista na memória
	        usuarioLogado.save(); // Salva o usuário com a lista de carrinho vazia
	        
	        if (request.isAjax()) {
	            renderText("Carrinho limpo com sucesso!");
	        } else {
	            // Comportamento padrão (sem JS): recarrega a página
	            ver();
	        }
	    }
	}

//@Valid lembrar pra que serve (se não, não vai rodar)
//Só caso precisemos 
