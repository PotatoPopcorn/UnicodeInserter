package iampopcorn.unicodeInserter;

import java.io.*;

public class FileHandler {
	byte[] importedFile;
	File charFile;
	FileInputStream charFIS;
	FileOutputStream charFOS;

	// Initiate file and load characters
	public FileHandler() {
		try {
			charFile = new File("charFile.dat");
			if(!charFile.exists()){
				charFile.createNewFile();
				System.out.println("Cannot find charFile.dat");
			}
			importedFile = new byte[(int) charFile.length()];
			charFIS = new FileInputStream(charFile);
			charFIS.read(importedFile);
			charFIS.close();
		} catch (Exception e) {
			handleError(e, "Initialisation");
			System.out.println("Crashing...");
			System.exit(-1);
		}
	}

	// Way of getting last characters used
	public String[] loadCharacters(int retCount) {
		try {
			String[] retArr = new String[retCount];
			if(importedFile[0] != 0x55 || importedFile[1] != 0x2B ){
				System.out.println("FileHandler: File not valid");
				return null;
			}
			//Ensure no overflow
			int stringCount = 0;
			retArr[stringCount]="U+";
			for(int i = 0; i<importedFile.length; i++){
				switch(importedFile[i]){
				case 0x30:
					retArr[stringCount] += "0";
					break;
				case 0x31:
					retArr[stringCount] += "1";
					break;
				case 0x32:
					retArr[stringCount] += "2";
					break;
				case 0x33:
					retArr[stringCount] += "3";
					break;
				case 0x34:
					retArr[stringCount] += "4";
					break;
				case 0x35:
					retArr[stringCount] += "5";
					break;
				case 0x36:
					retArr[stringCount] += "6";
					break;
				case 0x37:
					retArr[stringCount] += "7";
					break;
				case 0x38:
					retArr[stringCount] += "8";
					break;
				case 0x39:
					retArr[stringCount] += "9";
					break;
				case 0x41:
					retArr[stringCount] += "A";
					break;
				case 0x42:
					retArr[stringCount] += "B";
					break;
				case 0x43:
					retArr[stringCount] += "C";
					break;
				case 0x44:
					retArr[stringCount] += "D";
					break;
				case 0x45:
					retArr[stringCount] += "E";
					break;
				case 0x46:
					retArr[stringCount] += "F";
					break;
				case 0xA:
					try {
						if (importedFile[i + 1] != 0x55 || importedFile[i + 2] != 0x2B) {
							System.out.println("FileHandler: File not valid");
							return null;
						} else {
							stringCount++;
							if(stringCount >99){
								return retArr;
							}
							i += 2;
							retArr[stringCount] = "U+";
						} 
					} catch (Exception e) {
					}
					break;
				}
			}
			return retArr;
		} catch (Exception e) {
			handleError(e, "FileLoad");
		}
		return null;
	}

	// Add characters to memory file
	public void rememberCharacter(String input) {
		try{
			input += "\n";
			byte[] inputArr = input.getBytes();
			byte[] data = new byte[inputArr.length + importedFile.length];
			for (int i = 0; i < data.length; ++i)
			{
			    data[i] = i < inputArr.length ? inputArr[i] : importedFile[i - inputArr.length];
			}
			charFOS = new FileOutputStream(charFile);
			charFOS.write(data);
			charFOS.close();
			charFOS = null;
			importedFile = data;
			
		}catch(Exception e){
			handleError(e, "WriteChar");
		}
	}
	
	private void handleError(Exception e, String s){
		e.printStackTrace();
		System.out.println("FileHandler Error: " + s);
	}
	
	@SuppressWarnings("unused")
	private void printImported(){
		System.out.println("\n --Prining Imported File--");
		for(int i = 0; i<importedFile.length; i++){
			System.out.println(i + " - " + importedFile[i]);
		}
		System.out.println("\n --End Of Imported File--");
	}
}
