package jobs;

import models.Filme;
import models.Usuario;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Inicializador extends Job {
	


		@Override
		public void doJob() throws Exception {
			if (Filme.count() == 0) {
				Filme romance = new Filme("A culpa é das Estrelas", "romance");
				Filme romance2 = new Filme("A Pequena Sereia", "romance");
				Filme romance3 = new Filme("Crepúsculo", "romance");
				romance.save();
				romance2.save();
				romance3.save();

				Filme comedia = new Filme("Lilo & Sticth", "comedia");
				Filme comedia2 = new Filme("Rio 2", "comedia");
				Filme comedia3 = new Filme("Homem-formiga e a Vespa: Quantomania", "comedia");
				comedia.save();
				comedia2.save();
				comedia3.save();

				Filme terror = new Filme("Pânico 6", "terror");
				Filme terror2 = new Filme("O Exorcista do Papa", "terror");
				Filme terror3 = new Filme("João e Maria: Caçadores de Bruxas", "terror");
				terror.save();
				terror2.save();
				terror3.save();
			
		}
			Usuario joao = new Usuario();
			joao.nome = "João da silva";
			joao.email = "joao12new@gmail.com";
			joao.senha = "1234";
			joao.telefone = "84 991678900";
			// joao.filme = romance3 - não funciona
			// está com alguns errros, tirar duvida com professor
			joao.save();			
		}
	}