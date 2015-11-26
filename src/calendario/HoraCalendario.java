package calendario;

import java.time.LocalTime;

import palestra.Palestra;
import palestrante.Palestrante;

/**
 * Módulo que representa objeto do tipo Hora.
 * Sua abragência é como se fosse uma lista com arquivos entitulados
 * de acordo com o dia, ano e o mês do evento.
 * Ex: 10:00-11:00, 11:01-17:50, .... da pasta '15 de Novembro/2015'
 * @author 	Marcos
 * @since 	0.1
 *
 */
public class HoraCalendario {

	private LocalTime dataHoraInicio;
	private LocalTime dataHoraFim;
	private Palestra palestra;
	private Palestrante palestrante;

	public LocalTime getDataHoraInicio() {
		return dataHoraInicio;
	}
	public void setDataHoraInicio(LocalTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}
	public LocalTime getDataHoraFim() {
		return dataHoraFim;
	}
	public void setDataHoraFim(LocalTime dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}
	public Palestra getPalestra() {
		return palestra;
	}
	public void setPalestra(Palestra palestra) {
		this.palestra = palestra;
	}
	public Palestrante getPalestrante() {
		return palestrante;
	}
	public void setPalestrante(Palestrante palestrante) {
		this.palestrante = palestrante;
	}
	
	public LocalTime[] obterPeriodo (){
		LocalTime[] res = new LocalTime[2];
		
		res[0] = dataHoraInicio;
		res[1] = dataHoraFim;
		
		return res;
	}
}
