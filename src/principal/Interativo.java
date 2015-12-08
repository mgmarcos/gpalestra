package principal;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import calendario.Calendario;
import calendario.ControleTempo;
import calendario.Disponibilidade;
import calendario.Persistencia;
import localidade.Localidade;
import palestra.Palestra;
import palestrante.Palestrante;

public class Interativo {
	
	static LinkedHashMap<String, Palestrante> palestrantes = null;
	static LinkedList<Localidade> localidades = null;
	static LinkedList<Palestra> palestras = null;
	static Calendario calendario = null;
	
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	private enum gCMD {
		leia, limpa, liste, compile, sair;
	}
	
	private enum OBJ {
		evt, pal, loc; // evento (palestra), palestrante, localidade
	}
	
	private enum ACTION {
		EXIT, CONTINUE, SYNTAX_ERR;
	}
	
	public static void iteractiveLoopStart (){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		System.out.println(ANSI_RED+"Modo Interativo ativado."+ANSI_RESET);
		
		
		boolean g_continue = true;
		try {
			while ( g_continue ){
				System.out.print("$ ");
				line = br.readLine();
				
				ACTION feedback = cmdPhaser(line);
				
				switch ( feedback ){
					case SYNTAX_ERR:
						System.out.println("Comando inválido."); 
						break;
						
					case EXIT:
						cleanAll();
						g_continue = false;
						
					default:
				}
			}
		}
		catch (IOException e){
			System.out.println("Interativo: Não foi possível ler entrada do usuário.");
		}
	}
	
	public static ACTION cmdPhaser(String line){
		String[] args = line.split(" ");
		
		
		if ( args.length == 0 )
			return ACTION.CONTINUE;
		
		try{
			gCMD cmd = gCMD.valueOf(args[0]);
			OBJ o;
			
			switch (cmd){
				case leia:
					if ( args.length != 3 )
						return ACTION.SYNTAX_ERR;
					
					o = OBJ.valueOf(args[1]);
					
					switch (o){
						case evt: doLoadEventos(args[2]); break;
						case pal: doLoadPalestrantes(args[2]); break;
						case loc: doLoadLocalidades(args[2]); break;
						default:
							return ACTION.SYNTAX_ERR;
					}
					break;
					
					
				case limpa:
					cleanAll();
					break;
					
					
				case liste:
					
					if ( args.length != 2 )
						return ACTION.SYNTAX_ERR;
					
					o = OBJ.valueOf(args[1]);
					
					switch (o){
					case evt: imprimePalestras(palestras); break;
					case pal: imprimePalestrantes(palestrantes); break;
					case loc: imprimeLocalidades(localidades); break;
					
					}
					break;
					
					
				case compile:
					
					if ( args.length != 2 )
						return ACTION.SYNTAX_ERR;
					
					doWriteCalendario(args[1]); 
					break;
					
					
				case sair:
					return ACTION.EXIT;
			}
		}
		catch ( IllegalArgumentException e ){
			return ACTION.SYNTAX_ERR;
		}
			
		
		return ACTION.CONTINUE;
	}
	
	
	
	public static void doLoadEventos ( String arq ){
		System.out.println("A carregar palestras do arquivo: " + arq);
		
		
		try{
			if ( palestrantes == null || localidades == null ){
				System.out.println("Não foram carregados todos as informações necessárias para ler as palestras");
				return;
			}
			
			palestras = Palestra.lePalestras(arq, palestrantes, localidades);
		}
		catch ( FileNotFoundException e ){
			System.out.println("Arquivo de palestras não encontrado.");
		}
	}
	
	public static void doLoadPalestrantes (String arq){
		System.out.println("A carregar palestrantes do arquivo: " + arq);

		palestrantes = Palestrante.lePalestrantes("Palestrantes.txt");
	}
	
	public static void doLoadLocalidades (String arq){
		System.out.println("A carregar localidades do arquivo: " + arq);
		
		localidades = Localidade.leLocalidades("Localidades.txt");
	}
	
	public static void doWriteCalendario (String arq){
		System.out.println("Cruzando informações para calendário..." );
		
		calendario = ControleTempo.organizaPalestras(palestras, localidades);
		
		System.out.println("Abrindo resultados...");
		
		Persistencia.geraArquivoCalendario(calendario, arq);
		
		
		try {
			Desktop.getDesktop().edit(new File(arq));
		} catch (IOException e) {
			System.out.println("Não foi possível abrir o arquivo contendo o calendário");
		}
		
	}
	
	
	
	public static void imprimePalestras(LinkedList<Palestra> lista){
		int i=1;
		
		for(Palestra pal: lista){
			
			System.out.println(	"Palestra " + i + ": " + pal.getNome() + "\nTema: " + pal.getTema() + 
								"\nLocal: " + pal.getLocal() + "\nPalestrante: " + pal.getPalestrante() +
								"\nDuração: " + pal.getDuracaoMinutos());
			
			i++;
		}
		System.out.println("");
	}
	
	public static void imprimePalestrantes(LinkedHashMap<String, Palestrante>lista){
		int i=1;
		
		for(Palestrante pal: lista.values()){
			
			System.out.print("Palestrante " + i + ": " + pal.getNome() + "\nDisponibilidade: ");
			
			printListaDisponiblidade(pal.getDisponibilidade());
			
			i++;
		}
		System.out.println("");
		
	}
	
	public static void imprimeLocalidades(LinkedList<Localidade>lista){
		int i=1;
		
		for(Localidade loc : lista){
			
			System.out.print("Localidade " + i + ": " + loc.getNome() + "\nEndereço: " + loc.getEndereço() + "\nDisponibilidade: ");
			
			printListaDisponiblidade(loc.getDisponibilidade());
			System.out.println("");
					
			i++;
		}
		
	}
	
	public static void printListaDisponiblidade (LinkedList<Disponibilidade> lista){
		Iterator<Disponibilidade> itDisp = lista.iterator();
		
		if ( itDisp.hasNext() ){
			Disponibilidade disp = itDisp.next();
			LocalTime[] t = disp.getPeriodo();
			
			System.out.print(disp.obterStringAnoMesDia() + " " + t[0].toString() + "-" + t[1].toString() + "; ");
		}
		
	}
	
	
	
	public static void cleanAll(){
		palestrantes = null;
		palestras = null;
		localidades = null;
	}
}