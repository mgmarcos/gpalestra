package teste;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import calendario.ControleData;

/**
 * 	Exemplos de Teste de Caixa Fechada usando os módulos: <br>
 *	* ControleData<br>
 *
 */
public class teste_CaixaPreta {
	
	// Exemplos de Classe de Equivalância (vide tabela de testes)
	@Test
	public void t_controleData00(){
		String[] Testes = new String[7];
		
		Testes[0] = "12:54";
		Testes[1] = "A2:B5";
		Testes[2] = "12:543";
		Testes[3] = "-1:55";
		Testes[4] = "25:54";
		Testes[5] = "23:-3";
		Testes[6] = "23:68";
		
		int i=0;
		for(String tst: Testes){
			if ( ControleData.string_to_localTime(tst) != null )
				i++;
		}
		
		assertEquals(i, 1);
	}
}
