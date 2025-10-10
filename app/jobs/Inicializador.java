package jobs;

import models.Filme;
import models.Perfil;
import models.Usuario;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Inicializador extends Job {
	


		@Override
		public void doJob() throws Exception {
			if (Filme.count() == 0) {
				Filme romance = new Filme("A culpa é das Estrelas", "romance", 22.0);
				Filme romance2 = new Filme("A Pequena Sereia", "romance", 22.0);
				Filme romance3 = new Filme("Crepúsculo", "romance", 22.0);
				romance.save();
				romance2.save();
				romance3.save();

				Filme comedia = new Filme("Lilo & Sticth", "comedia", 33.0);
				Filme comedia2 = new Filme("Rio 2", "comedia", 33.0);
				Filme comedia3 = new Filme("Homem-formiga e a Vespa: Quantomania", "comedia", 33.0);
				comedia.save();
				comedia2.save();
				comedia3.save();

				Filme terror = new Filme("Pânico 6", "terror", 44.0);
				Filme terror2 = new Filme("O Exorcista do Papa", "terror", 44.0);
				Filme terror3 = new Filme("João e Maria: Caçadores de Bruxas", "terror", 44.0);
				terror.save();
				terror2.save();
				terror3.save();
			
		}
			Usuario joao = new Usuario();
			joao.nome = "João da silva";
			joao.email = "joao12new@gmail.com";
			joao.senha = "1234";
			joao.telefone = "84 991678900";
			joao.perfil = Perfil.ADMINISTRADOR;
			// joao.filme = romance3 - não funciona
			// está com alguns errros, tirar duvida com professor
			joao.save();			
		}
	}