package palestrante;

public class TratamentoDados {
	
	public static String nomePalestrante(String linha) {
		return linha.replace("Nome: ","").replace(".","");
	}

}
