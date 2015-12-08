package calendario;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import calendario.ControleData;
import palestra.Palestra;
import palestrante.Palestrante;
import localidade.Localidade;

/**
 * Este módulo tem por seu objetivo marcar os eventos no calendário
 * conforme as cirunstâncias. Se há disponibilidade de um palestrante
 * para uma palestra, o evento está alocado. Se há local para a palestra
 * neste horário, o evento está confirmado.
 * @version 0.1
 * @since	2015-09-19
 */
public class ControleTempo {
	
	// altera as cores do terminal (funciona perfeitamente em unix)
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";
	
	
	/**
	 * Recebe lista com localidades e palestrantes, tenta fazer o casamento dos dois de acordo com os critérios da palestra
	 * Ex: Palestra1 precisa do Palestrante1 na Localidade1
	 * @param palestras		Lista unicamente ligada contendo palestras lidas
	 * @param localidades	Lista unicamente ligada contendo localidades lidas
	 * @return Calendario	Objeto calendário com os eventos marcados com sucesso
	 */
	public static Calendario organizaPalestras(LinkedList<Palestra> palestras, LinkedList<Localidade> localidades) {
		Calendario calendario = new Calendario();
		calendario.setMeses(new LinkedHashMap<String,AnoMesCalendario>());
		
		
		for(Palestra palestra:palestras) {
			System.out.println("A encontrar disponibilidade de horário de palestrate e local para palestra " + palestra.getNome() + " com duração de " + palestra.getDuracaoMinutos() + " minutos(s)...");
			
			boolean marcado = false;
			Iterator<Disponibilidade> it = palestra.getPalestrante().getDisponibilidade().iterator();
			
			LocalTime[] evento = null;
			
			
			while (!marcado && it.hasNext()) {
				Disponibilidade dataHoraPal = it.next();
				
				if(Disponibilidade.duraçãoHora(dataHoraPal).toMinutes() >= palestra.getDuracaoMinutos()) {
					// std_war: disponibility of object:'Palestrante' can be used twice
					// std_fix: remove disponibility object from LinkedList<LocalDateTime[]> (auto_fix) @ 2015-10-02
					// std_war: disponibility of object:'Palestrante' is modfied
					LocalTime[] p = dataHoraPal.getPeriodo();
					System.out.println("Encontrado palestrante com disponibilidade no dia " + dataHoraPal.obterStringAnoMesDia() + ", " + p[0].toString() + "-" + p[1].toString());
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
							
							if ( ControleData.comparaAnoMesDia(dataHoraPal, dataHoraLoc) ){
								
								if ( Disponibilidade.duraçãoHora(dataHoraLoc).toMinutes() >= palestra.getDuracaoMinutos() ){
									evento = encaixaPalestraConformeDisponbilidade(dataHoraPal.getPeriodo(), dataHoraLoc.getPeriodo(), palestra.getDuracaoMinutos());
									
									if ( evento != null ){
										
										itDispLoc.remove();
										pilha_palestrante.put(nome_palestrante, palestrante);
								
										System.out.println(ANSI_RED+ "Marcada palestra " + palestra.getNome() + " em " + dataHoraPal.toString() +ANSI_RESET);
										marcado = true;
									}
								}
							}
							
							if (!marcado)
								System.out.println("Disponibilidade do local " + local.getEndereço() + " ("+dataHoraLoc.obterStringDia()+", "+dataHoraLoc.obterStringHora()+") não se encaixa com o dia da palestra.");
						}
						else
							System.out.println(nome_palestrante + " não pode dar uma palestra no local mais de uma vez (rodízio)");
					
					
					// se encontrou, adicione ao calendário
					if (marcado){
						String anoMes = String.format("%04d", dataHoraPal.getAno()) + String.format("%02d", dataHoraPal.getMes());
						AnoMesCalendario anoMesCal = calendario.getMeses().get(anoMes);
						if(anoMesCal == null) {
							anoMesCal = new AnoMesCalendario();
							anoMesCal.setAno(dataHoraPal.getAno());
							anoMesCal.setMes(dataHoraPal.getMes());
							anoMesCal.setDias(new LinkedHashMap<String,DiaCalendario>());
							calendario.getMeses().put(anoMes, anoMesCal);
						}
						
						String dia = String.format("%02d", dataHoraPal.getDia());
						DiaCalendario diaCal = anoMesCal.getDias().get(dia);
						if(diaCal == null) {
							diaCal = new DiaCalendario();
							diaCal.setDia(dataHoraPal.getDia());
							diaCal.setHoras(new LinkedHashMap<LocalTime,HoraCalendario>());
							anoMesCal.getDias().put(dia, diaCal);
						}
						
						HoraCalendario hora = diaCal.getHoras().get(evento[0]);
						if(hora == null) {
							
							hora = new HoraCalendario();
							hora.setDataHoraInicio(evento[0]);
							hora.setDataHoraFim(evento[1]);
							hora.setPalestrante(palestrante);
							hora.setPalestra(palestra);
							diaCal.getHoras().put(evento[0], hora);
							
							
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

	private static LocalTime[] encaixaPalestraConformeDisponbilidade (LocalTime[] pal, LocalTime[] loc, int duração){
		LocalTime[] intervalo = new LocalTime[2];

		// verifica se há interseção entre os dois intervalos de tempo
		intervalo[0] = ControleData.minof_localTime(pal[0], loc[0]);
		intervalo[1] = ControleData.maxof_localTime(pal[1], loc[1]);
		
		if ( ControleData.getDurationBetween(intervalo[1], intervalo[0]) >= duração ){
			
			intervalo[1] = LocalTime.from(intervalo[0]).plus(Duration.ofMinutes(duração));
			
			return intervalo;
		}
		
		
		return null;
	}
}
