package teste;

import java.util.LinkedList;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import localidade.Localidade;
import principal.Interativo;


public class teste_Localidade {
	@Test
	public void carregaLocalidade() throws FileNotFoundException{
		String arq;
		LinkedList<Localidade> localidades;
		
		arq = "tst_localidade.txt";
		
		localidades = Localidade.leLocalidades(arq);
		
		Interativo.imprimeLocalidades(localidades);
		
		System.out.println("Num: "+ localidades.size());
		
		assertEquals ( 2, localidades.size() );
		
	}
	
	@Ignore
	public static void imprimeNumero(int[] num){
		int i, len;
		
		len = num.length;
		
		if (len != 10 && len != 11)
			return;
		
		System.out.print("(" + num[0] + num[1] + ") ");
		
		for(i=2; i<len; i++){
			System.out.print(num[i]);
		}
	}
}
