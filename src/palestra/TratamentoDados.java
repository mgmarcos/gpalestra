package palestra;

import java.util.HashMap;
import java.util.LinkedList;

import palestrante.Palestrante;
import localidade.Localidade;

import calendario.ControleData;

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
		int hora, min;
		
		String[] horaMin = linha.replace("Duracao: ","").replace("h.","").split(":");
		
		if ( horaMin.length == 2 ){
			
			try{
				hora = Integer.parseInt(horaMin[0].trim());
				min = Integer.parseInt(horaMin[1].trim());
			}
			catch ( NumberFormatException e ){
				return -1;
			}
			
			if ( ControleData.isValid24_time(hora, min) )
				return (hora*60) + min;
		}
		
		return -1;
	}
}
