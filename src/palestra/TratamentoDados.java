package palestra;

import java.util.HashMap;
import java.util.LinkedList;

import palestrante.Palestrante;
import localidade.Localidade;

public class TratamentoDados {
	public static String nomePalestra(String linha) {
		return linha.replace("Nome: ","").replace(".","");
	}
	
	public static Palestrante palestrantePalestra(String linha, HashMap<String,Palestrante> palestrantes) {
		return palestrantes.get(linha.replace("Palestrante: ","").replace(".",""));
	}
	
	public static String temaPalestra(String linha) {
		return linha.replace("Tema: ","").replace(".","");
	}
	
	public static Localidade localPalestra(String linha, LinkedList<Localidade> localidades) {
		linha = linha.replace("Local: ","").replace(".","");
		
		for(Localidade loc:localidades){
			
			if (loc.getNome().equals(linha))
				return loc;
		}
		
		return null;
	}
	
	public static int duracaoMinutosPalestra(String linha) {
		String[] horaMin = linha.replace("Duracao: ","").replace("h.","").split(":");
		
		if ( horaMin.length == 2 ){
			int hora = Integer.valueOf(horaMin[0].trim());
			int min = Integer.valueOf(horaMin[1].trim());
			
			return (hora*60) + min;
		}
		
		return -1;
	}
}
