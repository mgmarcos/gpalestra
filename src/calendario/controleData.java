package calendario;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * Módulo que contém uma livraria de funções para conversão e verificação de datas.
 * As datas, em quaisquer formatos (String, LocalDateTime), precisam conversar.
 * Esta livraria irá não só permitir essa comunicação (conversão de um tipo de dado para o outro)
 * como também confirmar se estas datas estão no formato correto, a fim de não prejudicar
 * o funcionamento do programa.
 * @author 	Cristiano
 * @since 	0.2
 *
 */
public class ControleData {
	
	private static HashMap<String, Integer> diasSemana;
	static {
		diasSemana = new HashMap<String,Integer>();
        
		diasSemana.put("Seg",1);
		diasSemana.put("Ter",2);
		diasSemana.put("Qua",3);
		diasSemana.put("Qui",4);
		diasSemana.put("Sex",5);
		diasSemana.put("Sab",6);
		diasSemana.put("Dom",7);
	}
	
	
	/**
	 * Recebe uma string S que precisa estar no formato: XX/XX/XXXX HH:mm - HH:mm; ... (n);
	 * Retorna uma lista do tipo LocalDateTime de tamanho m, sendo este o número de datas no formato correto.
	 * @param linha
	 * @return LinkedList<LocalTime> t
	 * @throws IllegalArgumentException
	 */
	public static LinkedList<Disponibilidade> ajustaDisponibilidade(String linha) {
		LinkedList<Disponibilidade> disponibilidade = new LinkedList<Disponibilidade>();
		
		String[] disponibilidades = linha.replace("Disponibilidade: ","").replace(".","").split(";");
		
        for(String d:disponibilidades) {
        	String[] camposDisp = d.split(",");
        	
        	String[] data = camposDisp[1].split("/");
        	int dia = Integer.valueOf(data[0].trim());
        	int mes = Integer.valueOf(data[1].trim());
        	int ano = Integer.valueOf(data[2].trim());
        	
        	if ( ControleData.isValidDay(dia) && ControleData.isValidMonth(mes) && ControleData.isValidYear(ano) ){
        		
        		String[] duração = camposDisp[2].split("-");
        		
        		if ( duração.length == 2 ){
        			LocalTime[] instante = new LocalTime[2];
        			
        			instante[0] = ControleData.string_to_localTime(duração[0]);
        			instante[1] = ControleData.string_to_localTime(duração[1]);
        			
        			if ( instante[0] != null && instante[1] != null ){
        				
        				if ( ControleData.getDurationBetween(instante[1], instante[0]) > 0 ){
        					
        					Disponibilidade disp = Disponibilidade.of(ano, mes, dia);
        					
        					disp.setPeriodo(instante);

        					disponibilidade.add(disp);
        				}
        			}
        		}
        	}
        }
        
        if ( disponibilidade.size() == 0 )
    		throw new IllegalArgumentException();
        
        
        return disponibilidade;
	}
	
	/**
	 * Faz uma comparação entre dois objetos do tipo disponibilidade,
	 * no âmbito AnoMesDia. Retorna verdadeiro se as datas forem
	 * iguais. Exemplo: 08/12/15 e 08/12/15 => true.
	 * @param 	d1 disponibilidade 1
	 * @param 	d2 disponibilidade 2
	 * @return 	verdadeiro se ambas forem iguais
	 */
	public static boolean comparaAnoMesDia (Disponibilidade d1, Disponibilidade d2){
		
		if ( d1.getAno() == d2.getAno() ){
			
			if ( d1.getMes() == d2.getMes() ){
				
				if ( !d1.special_mesTodo() && !d2.special_mesTodo() ){ 
					if ( d1.getDia() != d2.getDia() )
						return false;
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Converte uma string para objeto do tipo LocalTime
	 * @param 	str Uma string no formato HH:MM
	 * @return	LocalTime se o formato estiver correto
	 */
	public static LocalTime string_to_localTime(String str){
		String[] instant;
		
		instant = str.split(":");
		
		if (instant.length != 2)
			return null;
		
		int houer = Integer.valueOf(instant[0].trim());
		int minute = Integer.valueOf(instant[1].trim());
		
		if ( !isValid24_time(houer, minute) )
			return null;
		
		return LocalTime.of(houer, minute);
	}
	
	public static String localDateTime_to_weekString(LocalDateTime loc){
		return getDayFromInt(loc.getDayOfWeek().getValue()) + ", " + loc.toLocalTime().toString();
	}
	
	/**
	 * Verifica se um horário no formato 24 horas está no formato:
	 * HH[0,23]:MM[0,59]
	 * @param houer		Hora
	 * @param minute	Minitos
	 * @return	verdadeiro se for válido
	 */
	public static boolean isValid24_time(int houer, int minute){
		if (0 <= houer && houer <= 24)
			if (0 <= minute && minute <= 59)
				return true;
	
		return false;	
	}
	
	public static boolean isValidYear (int y){
		return (y>0);
	}
	
	public static boolean isValidMonth (int m){
		return (1 <= m && m <= 12);
	}
	
	public static boolean isValidDay (int d){
		return (0 <= d && d <= 31); // 0 => todo mes
	}
	
	/**
	 * Retorna um inteiro [1,7] equivalendo ao dia da semana
	 * @param 	day	String no formato: Seg || Ter || Qua ...
	 * @return	o inteiro caso a string esteja no formato especificado. -1 caso contrário
	 */
	public static int getDayFromString(String day)
    {
		if ( diasSemana.get(day) != null )
			return diasSemana.get(day).intValue();
		
		return -1;
    }
	
	/**
	 * Retorna uma String no formato (Seg || Ter || Qua ...) a partir de um inteiro
	 * @param 	day	Inteiro no intervalo [1,7]
	 * @return	A string caso esteja no formato especificado. Null caso contrário
	 */
	public static String getDayFromInt(Integer day){
		for ( Entry<String, Integer> entry : diasSemana.entrySet()) {
		    String key = entry.getKey();
		    int id = entry.getValue();
		   
		    if (id == day)
		    	return key;
		}
		
		return null;
	}
	
	/**
	 * Retorna a duração em minutos entre dois objetos do tipo LocalTime
	 * @param a	LocalTime1
	 * @param b	LocalTime2
	 * @return	A duração em minutos entre estes dois intervalos de tempo
	 */
	public static int getDurationBetween(LocalTime a, LocalTime b){
		int minute_a, minute_b;
		
		minute_a = a.getHour()*60 + a.getMinute();
		minute_b = b.getHour()*60 + b.getMinute();
		
		return minute_a - minute_b;
	}
	
	/**
	 * Obtém o LocalTime mais perto de 23:59h
	 * @param 	a	LocalTime1
	 * @param 	b	LocalTime2
	 * @return 	O maior entre eles
	 */
	public static LocalTime maxof_localTime(LocalTime a, LocalTime b){
		
		return ( getDurationBetween(a, b) > 0 ) ? a : b;
	}
	
	/**
	 * Obtém o LocalTime mais perto de 00:00h
	 * @param 	a	LocalTime1
	 * @param 	b	LocalTime2
	 * @return	O menor entre eles
	 */
	public static LocalTime minof_localTime(LocalTime a, LocalTime b){
		
		return ( getDurationBetween(a, b) < 0 ) ? a : b;
	}

}
