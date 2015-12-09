package teste;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import palestra.TratamentoDados;

public class teste_palestra {
	/*
	@Test
	public void carregaPalestra () throws FileNotFoundException{
		// arquivo com todos os erros possíveis
		LinkedList<Palestra> palestras;
		LinkedHashMap<String,Palestrante> palestrantes;
		LinkedList<Localidade> localidades;
		
		palestrantes = Palestrante.lePalestrantes("/teste/00palestrante.txt");
		localidades = Localidade.leLocalidades("/teste/00localidades.txt");
		
		palestras = Palestra.lePalestras("tst_palestras.txt", palestrantes, localidades);
	}*/

	// Caixa preta
	@Test
	public void teste_tratamentoDados(){
		// Casos de testes realizados utilizando método: Classe de Equivalência
		assertEquals(-1, TratamentoDados.duracaoMinutosPalestra("AA:00"));
		assertEquals(-1, TratamentoDados.duracaoMinutosPalestra("-1:58"));
		assertEquals(-1, TratamentoDados.duracaoMinutosPalestra("23:-1"));
		assertEquals(-1, TratamentoDados.duracaoMinutosPalestra("24:00"));
		assertEquals(-1, TratamentoDados.duracaoMinutosPalestra("23:99"));
		
		assertEquals(793, TratamentoDados.duracaoMinutosPalestra("13:13"));
	}
	
	
}
