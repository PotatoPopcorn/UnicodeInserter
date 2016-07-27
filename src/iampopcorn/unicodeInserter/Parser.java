package iampopcorn.unicodeInserter;

public class Parser {
	//Change Unicode ID to a string
	public String convertToString(String code){
		int hexInt = Integer.parseInt(code.substring(2), 16);
		String character = new String(Character.toChars(hexInt));
		return character;
	}
}
