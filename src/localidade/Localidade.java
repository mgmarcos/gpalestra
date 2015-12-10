package localidade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

import calendario.ControleData;
import calendario.Disponibilidade;

import palestrante.Palestrante;
import principal.Principal;


/**
 * <h1> Localidade <h1>
 * * Gerencia leitura de arquivos contendo localidades
 * * Faz o tratamento de dados destes dados lidos
 * * Prepara os dados armazenados para persistência
 * @author	Cristiano
 * @version 0.1
 * @since	2015-10-30
 */
public class Localidade {
	private String 		nome;
	private String 		endereço;
	private LinkedList<Disponibilidade> disponibilidade;
	
	private LinkedHashMap<String,Palestrante> pilha;
	
	private static Scanner scan;
	
	public Localidade(){
		endereço = null;
		disponibilidade = null;
		nome = null;
		pilha = new LinkedHashMap<String,Palestrante>();
	}
	
	
	
	public String getNome (){
		return nome;
	}
	
	public void setNome (String nome){
		this.nome = nome;
	}
	
	public void setEndereço(String Endereço) {
		this.endereço = Endereço;
	}
	
	public String getEndereço(){
		return endereço;
	}
	
	public LinkedList<Disponibilidade> getDisponibilidade() {
		return disponibilidade;
	}
	public void setDisponibilidade(LinkedList<Disponibilidade> disponibilidade) {
		this.disponibilidade = disponibilidade;
	}
	
	public LinkedHashMap<String,Palestrante> getPilha (){
		return pilha;
	}
	
	public void setPilha (LinkedHashMap<String,Palestrante> pilha){
		this.pilha = pilha;
	}
	
	/**
	 * * Lê as localidades de um arquivo
	 * * Confere se cada localidade possui todas as informações obrigatórias
	 * * Confere se essas informações não possui incosistências
	 * @param String arq : localização do arquivo
	 * @return LinkedHashMap<String,Localidade> : lista contendo localidades lidas com sucesso
	 */
	public static LinkedList<Localidade> leLocalidades(String arq) {
		
		LinkedList<Localidade> localidades = new LinkedList<Localidade>();
		int numeroLinha = 0;
    	int numeroLocalidades = 0;
    	
    	Localidade novaLocalidade = new Localidade();
    	
    	PrintWriter log = null;
		
		
		try{
			scan = new Scanner(new File(arq));
			
			if ( Principal.logAtivado() )
				log = new PrintWriter(arq+"log");
			
		}catch (FileNotFoundException e){
			System.out.println("Houve um erro ao acessar arquivos de localidades.");
			return localidades;
		}

		
        while(scan.hasNextLine()) {
        	String linha = scan.nextLine();
        	numeroLinha++;
        	
        	try{
	        	if ( linha.startsWith("Nome: ") ){
	
	        		// Leia informação básica e crie nova localidade
	        		linha = TratamentoDados.ajustaNome(linha);
	        		
	        		novaLocalidade.setNome(linha);
	        	}
	    		
	    		else if ( linha.startsWith("Disponibilidade: ") ){
	        		LinkedList<Disponibilidade> disp = ControleData.ajustaDisponibilidade(linha);
	        		
	        		novaLocalidade.disponibilidade = disp;
	        	}
	        	
	    		else if ( linha.startsWith("Endereco: ") ){
	    			
	    			linha = TratamentoDados.ajustaEndereço(linha);
	    			
	    			novaLocalidade.setEndereço(linha);
	    		}
	    		
	    		else
	    			throw new IllegalArgumentException(numeroLinha + "> " + linha);
	        	
				// Se possui localidade com todos os dados corretos
	    		if ( novaLocalidade.possui_informações() ){
	    			
	    			localidades.add(novaLocalidade);
	    			novaLocalidade = new Localidade();
	    			numeroLocalidades++;
	    		}
	    		
        	} catch ( IllegalArgumentException e ){
        		if (log != null)
        			log.println(e.getMessage());
        	}
        }
        
        scan.close();
        
        if (log != null)
        	log.close();
        
        System.out.println(numeroLocalidades + " localidades lidas com sucesso.");
		
		
		return localidades;
	}
	
	private boolean possui_informações (){
		if (this.endereço != null && this.nome != null)
			if ( this.disponibilidade != null )
				if ( this.disponibilidade.size() > 0 )
					return true;
		
		return false;
	}

}