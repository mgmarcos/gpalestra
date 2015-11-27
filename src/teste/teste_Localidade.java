package teste;

import java.util.LinkedList;
import java.util.Iterator;
import java.time.LocalTime;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

import localidade.Localidade;
import localidade.Responsavel;
import calendario.ControleData;
import calendario.Disponibilidade;
import localidade.TratamentoDados;


public class teste_Localidade {
	@Test
	public void carregaLocalidade(){
		String arq;
		LinkedList<Localidade> localidades;
		
		arq = "tst_localidade.txt";
		
		localidades = Localidade.leLocalidades(arq);
		
		imprimeLocalidades(localidades);
		
		System.out.println("Num: "+ localidades.size());
		
		assertEquals ( 2, localidades.size() );
		
	}
	
	@Ignore
	@Test
	public void dadosVariados(){
		String resp_incompleto = "Responsável: Katrina; (61) 9999-9999;";
		String resp_errado1 = "Responsável: Juliano; 61 999;";
		String resp_errado2 = "Responsável: Juliano; 61 999;;;;;;;;";
		
		String disp_errado1 = "Disponibilidade: Seg, 8:00-12:00; Ter, 12:00-11:00; Qua";
		LinkedList<Integer> dias = new LinkedList<Integer>();
		
		
		assertNull (TratamentoDados.ajustaResponsável(resp_incompleto));
		assertNull (TratamentoDados.ajustaResponsável(resp_errado1));
		assertNull (TratamentoDados.ajustaResponsável(resp_errado2));
		
		ControleData.ajustaDisponibilidade(disp_errado1);
		
		assertEquals ( 1, dias.size() );
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
	
	@Ignore
	public static void imprimeResponsável(Responsavel res){
		System.out.print("\nResponsável: " + res.getNome() + "; ");
		imprimeNumero(res.getNumero());
		System.out.println("; " + res.getEmail());
	}
	
	@Ignore
	public static void imprimeLocalidades(LinkedList<Localidade>lista){
		int i=1;
		
		for(Localidade loc : lista){
			
			System.out.print("Localidade " + i + ": " + loc.getNome() + "\nEndereço: " + loc.getEndereço() + "\nDisponibilidade: ");
			
			Iterator<Disponibilidade> itDisp = loc.getDisponibilidade().iterator();
			
			if ( itDisp.hasNext() ){
				Disponibilidade disp = itDisp.next();
				LocalTime[] t = disp.getPeriodo();
				
				System.out.print(disp.paraString() + " " + t[0].toString() + "-" + t[1].toString() + "; ");
			}
					
			i++;
		}
		System.out.println("");
		
	}
}
