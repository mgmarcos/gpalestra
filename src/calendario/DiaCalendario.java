package calendario;

import java.time.LocalTime;
import java.util.LinkedHashMap;

/**
 * Módulo que representa objeto do tipo Dia.
 * Sua abragência é como se fosse uma lista com pastas entituladas
 * de acordo com o dia do evento, sendo a gaveta onde elas estão
 * inseridas um objeto do tipo AnoMes
 * Ex: 13, 14, 15, ... da pasta:'Novembro/2015'
 * @author 	Marcos
 * @since 	0.1
 *
 */
public class DiaCalendario {

	private int dia;
	private LinkedHashMap<LocalTime,HoraCalendario> horas;

	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public LinkedHashMap<LocalTime, HoraCalendario> getHoras() {
		return horas;
	}
	public void setHoras(LinkedHashMap<LocalTime, HoraCalendario> horas) {
		this.horas = horas;
	}
}
