package localidade;

/**
 * Como as informações estão sendo retiradas de um arquivo externo, não podem
 * ser confiadas pelo programa, pois um único dado errado pode comprometer todo
 * o funcionamento do sistema.
 * @since 	2015-10-29
 */
public class TratamentoDados {
	
	public static String ajustaEndereço (String linha){
		linha = linha.replace("Endereco: ", "").replace(".", "");
		
		return linha;
	}
	
	public static String ajustaNome (String linha){
		linha = linha.replace("Nome: ", "");
		
		return linha;
	}

	
	/**
	 * Recebe uma string S que precisa estar no formato: %s;%d;%s;
	 * Sendo o número %d: (XX) XXXX-XXXX. Pode também ter 11 dígitos.
	 * 
	 * @param linha
	 * @return 
	 */
	public static Responsavel ajustaResponsável (String linha){
		String[] 	info;
		int[] 		numero;
		
		
		info = linha.replace("Responsável: ", "").split(";");
		if (info.length != 3)
			return null;
		
		
		numero = string_to_phoneNumber(info[1]);
		if (numero == null)
			return null;
		
		
		Responsavel resp = new Responsavel();
		
		resp.setNome( info[0] );
		resp.setNumero(numero);
		resp.setEmail( parm_textFormat(info[2]) );
		
		return resp;
	}

	/**
	 * Este recebe uma string e tenta transformar para número.
	 * Se esta estiver entre 10 a 11 digitos é valido.
	 * @param str String contento o número
	 * @return int [] Array de inteiro com resultado
	 */
	private static int[] string_to_phoneNumber(String str){
		int[] number = new int[11];
		int i = 0;
		
		for (char c: str.toCharArray()){
			
			if ( '0' <= c && c <= '9' ){
				number[i] = c-'0';
				i++;
			}
		}
		
		return (10 <= i && i <= 11) ? number : null;
	}

	/**
	 * * Cada informação no arquivo de Localidades pode conter n sub-informações.
	 * * Exemplo: Responsável: Luiz; 61 9090-9090; l@grr.la;
	 * * Neste caso, informação é Responsável e as sub-informações estão separadas por ;
	 * * Esta função irá retirar o primeiro espaço entre as informações.
	 * @param str
	 * @return
	 */
	private static String parm_textFormat (String str){
		if ( str.startsWith(" ") )
			str = str.replaceFirst(" ", "");
		
		return str;
	}
}
