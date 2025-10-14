package controllers;

import play.mvc.Controller;

public class Index extends Controller{

	public static void form() {
		//Criamos variáveis 'email' e 'senha' vazias.
		String email = "";
		String senha = "";
		//    Passamos essas variáveis vazias ao renderizar o formulário.
        //    Isso vai garantir que os campos na tela sempre comecem em branco.
		render("Index/form.html", email, senha);
	}
	
}
