package teste;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import localidade.Localidade;
import principal.Principal;


public class teste_Localidade {
	@Test
	public void tst_leLocalidades (){
		Principal.setLogAtivado(true);
		assertEquals (0, Localidade.leLocalidades("teste/01Localidades.txt").size() );
		Principal.setLogAtivado(false);
		assertEquals (0, Localidade.leLocalidades("teste/02Localidades.txt").size() );
		assertEquals (0, Localidade.leLocalidades("teste/03Localidades.txt").size() );
		assertEquals (0, Localidade.leLocalidades("teste/04Localidades.txt").size() );
		assertEquals (0, Localidade.leLocalidades("teste/05Localidades.txt").size() );
		assertEquals (1, Localidade.leLocalidades("teste/06Localidades.txt").size() );
		assertEquals (0, Localidade.leLocalidades("teste/07Localidades.txt").size() );
	}
}
