package principal;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import calendario.Calendario;
import calendario.ControleTempo;
import calendario.Persistencia;
import palestra.Palestra;
import palestrante.Palestrante;
import localidade.Localidade;

public class Principal {
	
	private static boolean logAtivado = false;
	static boolean interativo = true;
	
	public static boolean logAtivado (){
		return logAtivado;
	}

	
	public static void main(String[] args) {
		
		for(String a:args){
			if ( a.compareTo("-l") == 0 ){
				logAtivado = true;
			}
			
			if ( a.compareTo("-a") == 0 ){
				interativo = false;
			}
		}
		
		if ( interativo )
			Interativo.iteractiveLoopStart();
		else
			auto_read();
		
	}
	
	
	public static void auto_read (){
		LinkedHashMap<String, Palestrante> palestrantes = Palestrante.lePalestrantes("Palestrantes.txt");
	
		LinkedList<Localidade> localidades = Localidade.leLocalidades("Localidades.txt");
	
		LinkedList<Palestra> palestras = Palestra.lePalestras("Palestras.txt", palestrantes, localidades);
	
		Calendario calendario = ControleTempo.organizaPalestras(palestras, localidades);
	
		Persistencia.geraArquivoCalendario(calendario,"Calendario.txt");
	}
}
