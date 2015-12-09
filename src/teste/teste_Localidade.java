package teste;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import localidade.Localidade;
import principal.Principal;


public class teste_Localidade {
	@Test
	public void tst_leLocalidades (){
		String 	dir, arq;
		int		i, num_arq;
		int[]	esperado = { 0, 0, 0, 0, 1, 0 }; // qtd. de Localidades lidas
		
		dir = "/teste/";
		arq = "Localidades.txt";
		
		// o primeiro é especial
		Principal.setLogAtivado(true);
		assertEquals ( 0, Localidade.leLocalidades(dir+"01"+arq).size() );
		Principal.setLogAtivado(false);
		
		
		num_arq = 6;
		for(i=2; i<=num_arq; i++){
			assertEquals (esperado[i], Localidade.leLocalidades(dir+"0"+i+"Localidades.txt").size() );
		}
	}
}
