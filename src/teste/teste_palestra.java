package teste;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import palestra.Palestra;
import palestrante.Palestrante;
import localidade.Localidade;

public class teste_palestra {
	@Test
	public void carregaPalestra () throws FileNotFoundException{
		// arquivo com todos os erros possíveis
		LinkedList<Palestra> palestras;
		LinkedHashMap<String,Palestrante> palestrantes;
		LinkedList<Localidade> localidades;
		
		palestrantes = Palestrante.lePalestrantes("tst_palestrante.txt");
		localidades = Localidade.leLocalidades("tst_localidades.txt");
		
		palestras = Palestra.lePalestras("tst_palestras.txt", palestrantes, localidades);
	}

}
