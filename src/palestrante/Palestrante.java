package palestrante;


import java.io.File;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

import palestrante.TratamentoDados;
import calendario.ControleData;
import calendario.Disponibilidade;

public class Palestrante {

	private String nome;
	private LinkedList<Disponibilidade> disponibilidade;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LinkedList<Disponibilidade> getDisponibilidade() {
		return disponibilidade;
	}
	public void setDisponibilidade(LinkedList<Disponibilidade> disponibilidade) {
		this.disponibilidade = disponibilidade;
	}
	
	
	
	private static Scanner scan;
	
	public static LinkedHashMap<String,Palestrante> lePalestrantes(String arq) {
		
		LinkedHashMap<String,Palestrante> palestrantes = new LinkedHashMap<String,Palestrante>();
		try {
	    	scan = new Scanner(new File(arq));
	    	int numeroLinha = 0;
	    	int numeroPalestrantes = 0;
	    	
	        while(scan.hasNextLine()) {
	        	String linha = scan.nextLine();
	        	numeroLinha ++;
	        	if(linha.startsWith("Nome: ")) {
	        		Palestrante novoPalestrante = new Palestrante();
	        		novoPalestrante.setNome(TratamentoDados.nomePalestrante(linha));
	        		
		            if(scan.hasNextLine()) {
		            	linha = scan.nextLine();
		            	numeroLinha ++;
			            if(linha.startsWith("Disponibilidade: ")) {
			            	novoPalestrante.setDisponibilidade(ControleData.ajustaDisponibilidade(linha));
			            	palestrantes.put(novoPalestrante.getNome(), novoPalestrante);
			            	numeroPalestrantes++;
			            } else {
			            	throw new IllegalArgumentException("Esperado \"Disponibilidade: \" na linha " + numeroLinha + " do arquivo " + arq);
			            }
		            } else {
		            	throw new IllegalArgumentException("Último Palestrante não possui Disponibilidade");
		            }
	        	} else {
	            	throw new IllegalArgumentException("Esperado \"Nome: \" na linha " + numeroLinha + " do arquivo " + arq);
	            }
	        }
	        scan.close();
	        
	        System.out.println(numeroPalestrantes + " palestrantes lidos com sucesso.");
	    } 
	    catch (FileNotFoundException e) {
	        System.out.println("Não foi encontrado arquivo contendo palestrantes");
	        
	    }catch (IllegalArgumentException e) {
	        e.printStackTrace();
	    }
		
		return palestrantes;
	}
	
}