package calendario;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import calendario.controleData;
import palestra.Palestra;
import palestrante.Palestrante;
import localidade.Localidade;

/**
 * Este módulo tem por seu objetivo marcar os eventos no calendário
 * conforme as cirunstâncias. Se há disponibilidade de um palestrante
 * para uma palestra, o evento está alocado. Se há local para a palestra
 * neste horário, o evento está confirmado.
 * @author 	Marcos, Cristiano
 * @version 0.1
 * @since	2015-09-19
 */
public class ControleTempo {
	// altera as cores do terminal (funciona perfeitamente em unix)
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	
	/**
	 * <h1> Organiza Palestras </h1>
	 * Recebe lista com localidades e palestrantes, tenta fazer o casamento dos dois de acordo com os critérios da palestra
	 * Ex: Palestra1 precisa do Palestrante1 na Localidade1
	 * Retorna um calendário com os eventos marcados com sucesso
	 * @param LinkedList<Palestra>
	 * @return Calendario
	 */
	public static Calendario organizaPalestras(LinkedList<Palestra> palestras, LinkedList<Localidade> localidades) {
		Calendario calendario = new Calendario();
		calendario.setMeses(new LinkedHashMap<String,AnoMesCalendario>());
		
		
		for(Palestra palestra:palestras) {
			System.out.println("A encontrar disponibilidade de horário de palestrate e local para palestra " + palestra.getNome() + " com duração de " + palestra.getDuracaoMinutos() + " minutos(s)...");
			
			boolean marcado = false;
			Iterator<Disponibilidade> it = palestra.getPalestrante().getDisponibilidade().iterator();
			
			LocalTime[] intervalo = null;
			
			
			while (!marcado && it.hasNext()) {
				Disponibilidade dataHora = it.next();
				
				if(Disponibilidade.duraçãoHora(dataHora).toMinutes() >= palestra.getDuracaoMinutos()) {
					// std_war: disponibility of object:'Palestrante' can be used twice
					// std_fix: remove disponibility object from LinkedList<LocalDateTime[]> (auto_fix) @ 2015-10-02
					LocalTime[] p = dataHora.getPeriodo();
					System.out.println("Encontrado palestrante com disponibilidade no dia " + dataHora.paraString() + ", " + p[0].toString() + "-" + p[1].toString());
					it.remove();
					
					
					// procure por um local
					Localidade local = palestra.getLocal();
					Palestrante palestrante = palestra.getPalestrante();
					String nome_palestrante = palestrante.getNome();
					
					Iterator<Disponibilidade> itDispLoc = local.getDisponibilidade().iterator();
					LinkedHashMap<String,Palestrante> pilha_palestrante = local.getPilha();
					
					while ( !marcado && itDispLoc.hasNext() ){
						Disponibilidade dataHoraLoc = itDispLoc.next();
						
						if ( !pilha_palestrante.containsKey(nome_palestrante) ){ // rodizio
							
							if ( controleData.comparaAnoMes(dataHora, dataHoraLoc) ){
								
								if ( Disponibilidade.duraçãoHora(dataHoraLoc).toMinutes() >= palestra.getDuracaoMinutos() ){
									
									intervalo = encaixaPalestraConformeDisponbilidade(dataHora.getPeriodo(), dataHoraLoc.getPeriodo(), palestra.getDuracaoMinutos());
									
									if ( intervalo != null ){
										
										itDispLoc.remove();
										pilha_palestrante.put(nome_palestrante, palestrante);
								
										System.out.println(ANSI_RED+ "Marcada palestra " + palestra.getNome() + " em " + dataHora.toString() +ANSI_RESET);
										marcado = true;
									}
								}
							}
							
							if (!marcado)
								System.out.println("Disponibilidade do local " + local.getEndereço() + " (" + Localidade.printDisponibilidade(dataHoraLoc) + ") não se encaixa com o dia da palestra.");
						}
						else
							System.out.println(nome_palestrante + " não pode dar uma palestra no local mais de uma vez (rodízio)");
					
					
					// se encontrou, adicione ao calendário
					if (marcado){
						String anoMes = String.format("%04d", dataHora.getAno()) + String.format("%02d", dataHora.getMes());
						AnoMesCalendario anoMesCal = calendario.getMeses().get(anoMes);
						if(anoMesCal == null) {
							anoMesCal = new AnoMesCalendario();
							anoMesCal.setAno(dataHora.getAno());
							anoMesCal.setMes(dataHora.getMes());
							anoMesCal.setDias(new LinkedHashMap<String,DiaCalendario>());
							calendario.getMeses().put(anoMes, anoMesCal);
						}
						
						String dia = String.format("%02d", dataHora.getDia());
						DiaCalendario diaCal = anoMesCal.getDias().get(dia);
						if(diaCal == null) {
							diaCal = new DiaCalendario();
							diaCal.setDia(dataHora.getDia());
							diaCal.setHoras(new LinkedHashMap<LocalTime,HoraCalendario>());
							anoMesCal.getDias().put(dia, diaCal);
						}
						
						HoraCalendario hora = diaCal.getHoras().get(intervalo[0]);
						if(hora == null) {
							
							hora = new HoraCalendario();
							hora.setDataHoraInicio(intervalo[0]);
							hora.setDataHoraFim(intervalo[1]);
							hora.setPalestrante(palestrante);
							hora.setPalestra(palestra);
							diaCal.getHoras().put(intervalo[0], hora);
							
							
							}
						}
					}
				}
			}
			
			if(!marcado){
				throw new IllegalArgumentException("Não foi possível marcar a palestra " + palestra.getNome() + " por indisponibilidade de horário");
			}
		}

		return calendario;
	}
	
	private static LocalTime[] encaixaPalestraConformeDisponbilidade (LocalTime[] pal, LocalTime[] loc, long duração){
		int diferença;
		LocalTime[] intervalo; // intervalo de permitido para alocação segura da palestra
		
		// eu sei que não dá para entender nada, mas não dá pra pensar claro em pouco tempo
		
		diferença = pal[1].compareTo(loc[1]);
		
		if ( diferença > 0 ){
			
			diferença = loc[1].compareTo(pal[0]);
			
			if ( diferença > 0 && diferença > duração ) {
				
				intervalo = new LocalTime[2];
				
				intervalo[0] = pal[0];
				intervalo[1] = loc[1];
				
				return intervalo;
			}
		}
		else if ( diferença < 0 ){
			
			diferença = pal[1].compareTo(loc[0]);
			
			if ( diferença > 0 && diferença > duração ){
				
				intervalo = new LocalTime[2];
				
				intervalo[0] = loc[0];
				intervalo[1] = pal[1];
				
				return intervalo;
			}
		}
		
		return null;
	}
}
