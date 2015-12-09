package palestra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import palestra.TratamentoDados;
import palestrante.Palestrante;
import principal.Principal;
import localidade.Localidade;

public class Palestra {
	private String nome;
	private Palestrante palestrante;
	private String tema;
	private Localidade local;
	private int duracaoMinutos;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Palestrante getPalestrante() {
		return palestrante;
	}
	public void setPalestrante(Palestrante palestrante) {
		this.palestrante = palestrante;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public Localidade getLocal() {
		return local;
	}
	public void setLocal(Localidade local) {
		this.local = local;
	}
	public int getDuracaoMinutos() {
		return duracaoMinutos;
	}
	public void setDuracaoMinutos(int duracaoMinutos) {
		this.duracaoMinutos = duracaoMinutos;
	}
	
	
	
	private static Scanner scan;

	public static enum diasDaSemana {
		Dom, Seg, Ter, Qua, Qui, Sex, Sab
	}
	
	/**
	 * <h2> lePalestras <h2>
	 * * Lê informações de palestra
	 * * Confere se cada palestra possui todas as informações obrigatórias
	 * * Confere se essas informações não possui incosistências
	 * @param String arq : localização do arquivo
	 * @return LinkedList<Palestra> : lista contendo palestras lidas com sucesso
	 * @throws FileNotFoundException
	 */
	public static LinkedList<Palestra> lePalestras (String arq, LinkedHashMap<String,Palestrante> palestrantes, LinkedList<Localidade>localidades) {
		LinkedList<Palestra> palestras = new LinkedList<Palestra>();
		
		PrintWriter log = null;

		try{
			scan = new Scanner(new File(arq));
			
			if ( Principal.logAtivado() )
				log = new PrintWriter("[Log]"+arq);
			
		}catch (FileNotFoundException e){
			System.out.println("Houve um erro ao acessar arquivos de localidades.");
			return null;
		}
    	
    	int numeroLinha = 0;
    	int numeroPalestras = 0;
    	
    	Palestra novaPalestra = null;
    	
        while(scan.hasNextLine()) {
        	
        	String linha = scan.nextLine();
        	numeroLinha++;
        	
        	try{
        		if(linha.startsWith("Nome: ")) {
        			linha = TratamentoDados.nomePalestra(linha);
        			
        			novaPalestra = new Palestra();
        			novaPalestra.setNome(linha);
        			
        			linha = scan.nextLine(); numeroLinha++;
        			if ( linha.startsWith("Palestrante: ") ){
        				novaPalestra.setPalestrante(TratamentoDados.palestrantePalestra(linha, palestrantes));
		            
        				if( novaPalestra.getPalestrante() == null )
        					throw new IllegalArgumentException("Palestrante não identificado");
        				
        				linha = scan.nextLine(); numeroLinha++;
        				if ( linha.startsWith("Tema: ") ){
        					novaPalestra.setTema(TratamentoDados.temaPalestra(linha));
        					
        					linha = scan.nextLine(); numeroLinha++;
        					if ( linha.startsWith("Local: ") ){
        						novaPalestra.setLocal(TratamentoDados.localPalestra(linha, localidades));
        	            		
        						if (novaPalestra.getLocal() == null)
        	            			throw new IllegalArgumentException("Localidade não identificada");
        						
        						
        						linha = scan.nextLine(); numeroLinha++;
        						if ( linha.startsWith("Duracao: ") ){
        							int dur = TratamentoDados.duracaoMinutosPalestra(linha);
        							
        							if ( dur == -1 )
        								throw new IllegalArgumentException("Duração inválida");
        							
        							novaPalestra.setDuracaoMinutos(dur);
        							
				            		palestras.add(novaPalestra);
				            		numeroPalestras++;
        						}	
        					}
        				}
        			}
        		}
        		
        	}
        	catch ( IllegalArgumentException e ){
        		if (log != null)
        			log.println(numeroLinha + "> " + e.getMessage());
        	}
        	
        	catch ( NoSuchElementException e ){
        		if (log != null)
        			log.println("Dados do arquivo estão incompletos para palestra: " + novaPalestra.getNome());
        	}
        }

        scan.close();
        
        if (log != null)
        	log.close();
        
        System.out.println(numeroPalestras + " palestras lidas com sucesso.");
		
		return palestras;
	}
}
