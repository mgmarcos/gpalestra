package calendario;

import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDate;

public class Disponibilidade {
	private int ano;
	private int mes;
	private int dia;
	
	private LocalTime inicio;
	private LocalTime fim;
	
	public int getAno(){
		return ano;
	}
	
	public int getMes(){
		return mes;
	}
	
	public int getDia(){
		return dia;
	}
	
	public LocalTime[] getPeriodo(){
		LocalTime[] t = new LocalTime[2];
		
		t[0] = inicio;
		t[1] = fim;
		
		return t;
	}
	
	public void setPeriodo(LocalTime[] p){
		this.inicio = p[0];
		this.fim = p[1];
	}
	
	public static Disponibilidade of (int ano, int mes, int dia){
		Disponibilidade d = new Disponibilidade();
		
		d.ano = ano;
		d.mes = mes;
		d.dia = dia;
		
		return d;
	}
	
	public boolean special_mesTodo (){
		return ( this.dia == 0 );
	}
	
	public static Duration duraçãoHora (Disponibilidade d1){
		return java.time.Duration.between(d1.inicio, d1.fim);
		
	}
	
	public String obterStringAnoMesDia(){
		String		dia;
		
		
		if ( this.dia != 0 ){
			LocalDate tmp;
			
			tmp = LocalDate.of(this.ano, this.mes, this.dia);
			
			dia = ControleData.getDayFromInt(tmp.getDayOfWeek().getValue());
		}
		else
			dia = "(todo mes)";
		

		return dia + ", " + String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
	}
	
	public String obterStringDia(){
		
		if (this.dia == 0)
			return "(todo mes)";
		else
			return ""+this.dia;
	}
	
	public String obterStringHora(){
		String hora = inicio.toString() + "-" + fim.toString();
		
		return hora;
	}
}

