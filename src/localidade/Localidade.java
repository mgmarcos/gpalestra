package localidade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

import calendario.ControleData;
import calendario.Disponibilidade;

import java.time.LocalTime;

import palestrante.Palestrante;


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
	

	
	/*
	public Responsavel getResonsável (){
		return responsável;
	}
	
	public void setResponsável (Responsavel res){
		this.responsável = res;
	}
	*/
	
	public LinkedHashMap<String,Palestrante> getPilha (){
		return pilha;
	}
	
	public void setPilha (LinkedHashMap<String,Palestrante> pilha){
		this.pilha = pilha;
	}
	
	
	/**
	 * Recebe uma das disponibilidades da localidade e retorna uma string em formato mais legível
	 * @parm data - array de 2 elementos representando um intervalo de tempo inicio-fim
	 * @return String - texto legível
	 */
	public static String printDisponibilidade (Disponibilidade data){
		LocalTime[] p = data.getPeriodo();
		String hora = p[0].toString() + "-" + p[1].toString();
		
		
		if ( data.special_mesTodo() )
			return "(todo mês)" + ", " + hora;
		
		return data.getDia() + ", " + hora;
	}
	
	/**
	 * <h2> leLocalidades <h2>
	 * * Lê as localidades de um arquivo
	 * * Confere se cada localidade possui todas as informações obrigatórias
	 * * Confere se essas informações não possui incosistências
	 * @param String arq : localização do arquivo
	 * @return LinkedHashMap<String,Localidade> : lista contendo localidades lidas com sucesso
	 */
	public static LinkedList<Localidade> leLocalidades(String arq) {
		
		LinkedList<Localidade> localidades = new LinkedList<Localidade>();
		
		try {
	    	scan = new Scanner(new File(arq));
	    	
	    	int numeroLinha = 0;
	    	int numeroLocalidades = 0;
	    	
	    	Localidade novaLocalidade = null;

	    	
	        while(scan.hasNextLine()) {
	        	String linha = scan.nextLine();
	        	numeroLinha++;
	        	
	        	if ( linha.startsWith("Nome: ") ){

	        		// Leia informação básica e crie nova localidade
	        		linha = TratamentoDados.ajustaNome(linha);
	        		
	        		if ( linha == null ){
	        			throw new IllegalArgumentException("Formato inválido para localidade");
	        		}
	        		
	        		novaLocalidade = new Localidade();
	        		
	        		novaLocalidade.setNome(linha);
	        	}
	    		
	    		else if ( linha.startsWith("Disponibilidade: ") ){
	        		LinkedList<Disponibilidade> disp = ControleData.ajustaDisponibilidade(linha);
	        		if (disp == null){
	        			throw new IllegalArgumentException("Formato inválido para Hora. Linha: " + numeroLinha + ". Arquivo: " + arq);
	        		}
	        		
	        		novaLocalidade.disponibilidade = disp;
	        	}
	        	
	    		else if ( linha.startsWith("Endereco: ") ){
	    			
	    			linha = TratamentoDados.ajustaEndereço(linha);
	    			
	    			if ( linha == null ){
	    				throw new IllegalArgumentException("Formato inválido para Endereço. Linha: " + numeroLinha + ". Arquivo: " + arq);
	    			}
	    			
	    			novaLocalidade.setEndereço(linha);
	    		}
	    		
	    		else {
	        		throw new IllegalArgumentException("Informação inválida para Localidade. Linha: " + numeroLinha + ". Arquivo: " + arq);
	        	}
	        	
    			// Se possui localidade com todos os dados corretos
        		if ( (novaLocalidade != null) && novaLocalidade.possui_informações() ){
        			
        			localidades.add(novaLocalidade);
        			novaLocalidade = null;
        			numeroLocalidades++;
        		}
	        	
	        }
	        
	        scan.close();
	        System.out.println(numeroLocalidades + " localidades lidas com sucesso.");
		}

		
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}

		
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