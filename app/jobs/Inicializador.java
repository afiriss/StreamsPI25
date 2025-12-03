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
		// Apaga os filmes existentes para garantir que os novos dados sejam carregados
		Filme.deleteAll(); 

		if (Filme.count() == 0) {
			
		
			new Filme("A culpa é das Estrelas", "romance", 22.0, "filmes/aculpa.jpg").save();
			new Filme("A Pequena Sereia", "romance", 22.0, "filmes/pequena.jpeg").save();
			new Filme("Crepúsculo", "romance", 22.0, "filmes/crepusculo.jpg").save();
			new Filme("O Espaço Entre Nós", "romance", 24.0, "filmes/o-espaco-entre-nos.jpg").save();

			
			new Filme("Lilo & Sticth", "comedia", 33.0, "filmes/lilo-e-stitch.jpg").save();
			new Filme("Rio 2", "comedia", 33.0, "filmes/rio2.jpg").save();
			new Filme("Homem-formiga e a Vespa: Quantomania", "comedia", 33.0, "filmes/homem-formiga.jpg").save();
			new Filme("Minha Mãe é Uma Peça", "comedia", 36.0, "filmes/minha-mae-e-uma-peca.jpg").save();

			
			new Filme("Pânico 6", "terror", 44.0, "filmes/panico6.jpg").save();
			new Filme("O Exorcista do Papa", "terror", 44.0, "filmes/o-exorcista-do-papa.png").save();
			new Filme("João e Maria: Caçadores de Bruxas", "terror", 44.0, "filmes/joaoemaria.jpg").save();
			new Filme("It a Coisa", "terror", 47.0, "filmes/it-a-coisa.jpg").save();

		}
		
		if (Usuario.count("byEmail", "ADMIN@EMAIL.COM") == 0) {
            Usuario admin = new Usuario();
            admin.nome = "ADMINISTRADOR";
            admin.email = "ADM@EMAIL.COM";
            // É recomendável usar um hash para a senha em produção
            // Exemplo simples (não ideal para produção):
            admin.senha = "123a123b"; // Troque por uma senha segura
            admin.perfil = Perfil.ADMINISTRADOR;
            admin.status = models.Status.ATIVO; // Garantir que está ativo
            admin.save();
        }
	}
}