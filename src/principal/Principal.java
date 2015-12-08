package principal;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import calendario.Calendario;
import calendario.ControleTempo;
import calendario.Persistencia;
import palestra.Palestra;
import palestrante.Palestrante;
import localidade.Localidade;

public class Principal {
	
	public static boolean logAtivado = false;
	static boolean interativo = false;

	
	public static void main(String[] args) {
		
		for(String a:args){
			if ( a.compareTo("-l") == 0 ){
				logAtivado = true;
			}
			
			if ( a.compareTo("-i") == 0 ){
				interativo = true;
			}
		}
		
		if ( interativo )
			Interativo.iteractiveLoopStart();
		else
			auto_read();
		
	}
	
	
	public static void auto_read (){
		try{
			LinkedHashMap<String, Palestrante> palestrantes = Palestrante.lePalestrantes("Palestrantes.txt");
		
			LinkedList<Localidade> localidades = Localidade.leLocalidades("Localidades.txt");
		
			LinkedList<Palestra> palestras = Palestra.lePalestras("Palestras.txt", palestrantes, localidades);
		
			Calendario calendario = ControleTempo.organizaPalestras(palestras, localidades);
		
			Persistencia.geraArquivoCalendario(calendario,"Calendario.txt");
		}
		catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
	}
}
