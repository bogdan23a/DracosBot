package dracos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class JsonUrlParse {

	
	public static String readUrl(String urlString) throws Exception {
	      BufferedReader reader = null;

	      try {
	         URLConnection url = new URL(urlString).openConnection();
	         url.setRequestProperty("User-Agent",
	               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
	         url.connect();

	         try {
	        	
	            reader = new BufferedReader(new InputStreamReader(url.getInputStream(), Charset.forName("UTF-8")));
	         } catch (FileNotFoundException e) {
	            System.out.println("Invalid url? - JsonUrlParse.readUrl - Null pointer!");
	            return "{ \"error\": { } }";
	         } catch (IOException e) {
	            System.out.println("Request HTTP code 400 - Bad code? Format of GET request. Check that m8!");
	            return "{ \"error\": { } }";
	         }

	         StringBuffer buffer = new StringBuffer();
	         int read;
	         char[] chars = new char[1024];

	         while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read);

	         return buffer.toString();
	      } finally {
	         if (reader != null)
	            reader.close();
	      }
	   }

	   public static String readUrlAuthentication(String urlString, String userpass) throws Exception {
	      BufferedReader reader = null;

	      try {
	         URLConnection url = new URL(urlString).openConnection();
	         url.setRequestProperty("User-Agent",
	               "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

	         String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	         url.setRequestProperty("Authorization", basicAuth);
	         url.connect();

	         try {
	            reader = new BufferedReader(new InputStreamReader(url.getInputStream(), Charset.forName("UTF-8")));
	         } catch (FileNotFoundException e) {
	            System.out.println("Invalid url? - JsonUrlParse.readUrl - Null pointer!");
	            return "{ \"error\": { } }";
	         }

	         StringBuffer buffer = new StringBuffer();
	         int read;
	         char[] chars = new char[1024];

	         while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read);

	         return buffer.toString();
	      } finally {
	         if (reader != null)
	            reader.close();
	      }
	   }
	   // parser here
}
