package calendario;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Iterator;

/**
 * Recebe objeto do tipo calendário e escreve um arquivo em formato de Linha do Tempo com palestras marcadas
 * @author Marcos
 *
 */
public class Persistencia {

	/**
	 * 
	 * @param calendario
	 * @param arquivo para escrita
	 */
	public static void geraArquivoCalendario(Calendario calendario, String arq) {
		Iterator<String> itMeses = calendario.getMeses().keySet().iterator();
		if (itMeses.hasNext()) {
			AnoMesCalendario anoMesCal = calendario.getMeses().get(itMeses.next());
			String primMes = String.format("%02d",anoMesCal.getMes()) + "/" + String.format("%04d",anoMesCal.getAno());
			String ultMes = new String();
			if(itMeses.hasNext()) {
				while(itMeses.hasNext())
					anoMesCal = calendario.getMeses().get(itMeses.next());
				ultMes = " - " + String.format("%02d",anoMesCal.getMes()) + "/" + String.format("%04d",anoMesCal.getAno());
			}
			
			itMeses = calendario.getMeses().keySet().iterator();
			try {
				PrintWriter writer = new PrintWriter(arq);
				writer.println("Calendario de Palestras (" + primMes + ultMes + ")");
				while(itMeses.hasNext()) {
					anoMesCal = calendario.getMeses().get(itMeses.next());
					writer.println("");
					writer.println("Mes " + String.format("%02d",anoMesCal.getMes()) + "/" + String.format("%04d",anoMesCal.getAno()));
					Iterator<String> itDias = anoMesCal.getDias().keySet().iterator();
					while(itDias.hasNext()) {
						DiaCalendario diaCal = anoMesCal.getDias().get(itDias.next());
						writer.println("");
						writer.println("Dia " + String.format("%02d",diaCal.getDia()));
						Iterator<LocalTime> itHoras = diaCal.getHoras().keySet().iterator();
						while(itHoras.hasNext()) {
							HoraCalendario horaCal = diaCal.getHoras().get(itHoras.next());
							writer.println(
									horaCal.getPalestra().getNome() + 
									" (" + horaCal.getPalestrante().getNome() + 
									") (" + horaCal.getPalestra().getLocal().getNome() + " ~ " + horaCal.getPalestra().getLocal().getEndereço() + "): " + 
									
									String.format("%02d",horaCal.getDataHoraInicio().getHour()) + ":" +
									String.format("%02d",horaCal.getDataHoraInicio().getMinute()) + "-" +
									String.format("%02d",horaCal.getDataHoraFim().getHour()) + ":" +
									String.format("%02d",horaCal.getDataHoraFim().getMinute()) + ".");
						}
					}
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
