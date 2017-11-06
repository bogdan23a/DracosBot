package dracos.dracos;

import java.io.FileReader;
import java.util.Scanner;
import java.io.IOException;

public class Ref {
	public static String[] getTokenAndClientID() {
		
		/*Scanner tokenScanner = null;
		String TOKEN = null;
		String CLIENT_ID = null;
		String[] tokenAndID = new String[2];
		
		try 
		{
			tokenScanner = new Scanner(new FileReader("token.txt"));
			TOKEN = tokenScanner.nextLine();
			CLIENT_ID = tokenScanner.nextLine();
			tokenAndID[0] = TOKEN;
			tokenAndID[1] = CLIENT_ID;
		} 
		catch (IOException e) 
		{
			System.out.println("Error in token.txt : " + e.getMessage());			
		}
		finally 
		{
			tokenScanner.close();
		}*/
		String[] tokenAndID = {"Mzc2MDU4OTg0MDY4Njc3Njc0.DN5Qag.NBEFqeoRnIKf4J50gk2aAsrxTAs" , "376058984068677674"};
		return tokenAndID;
	}
}
