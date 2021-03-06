package dracos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.StringBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import dracos.dracos.DiscordMessage;
import dracos.dracos.Logger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShortcutFileHandling {

	public static void LoadShortcuts(HashMap<String, String> shortcuts) {

		File shcuts = new File("shortcuts.txt");
		FileReader fr = null;
		BufferedReader bf = null;

		if (shcuts.exists()) {

			try {

				fr = new FileReader(shcuts);
				bf = new BufferedReader(fr);
				String line;
				while ((line = bf.readLine()) != null) {

					String key = line.split(",")[0];
					String link = line.split(",")[1];
					shortcuts.put(key, link);
				}
				
			} catch (Exception exception) {

			}
			finally
			{
				try {
				fr.close();
				bf.close();
				}
				catch(IOException exception)
				{
					
				}
			}
		}
		else
		{
			try
			{
				Files.createFile(Paths.get("shortcuts.txt"));
			}
			catch(Exception exception)
			{
				
			}
		}

	}

	public static void WriteShortcut(HashMap<String, String> shortcuts, MessageReceivedEvent event, String key,
			String url) 
	{
		File shcuts = new File("shortcuts.txt");
		BufferedWriter bw = null;
		if (shcuts.exists()) {
			try {

				bw = new BufferedWriter(new FileWriter(new File("shortcuts.txt"), true));

				bw.write(key + "," + url);
				bw.newLine();
			} catch (Exception exception) {
				Logger.debug(exception.getStackTrace());
			} finally {

				try {

					bw.close();
					event.getChannel().sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "New Shortcut:",
							"Added **" + key + "** linked to *" + url + "* (Memorised)")).queue();
				} catch (IOException exception) {
					Logger.debug(exception.getStackTrace());
				}
			}
		} else
			event.getChannel().sendMessage(
					DiscordMessage.embedProblem(event.getAuthor(), "Err404!(no file found <<Someone deleted a file>> damage to source) Please be careful! "));

		
	}

	public static void RemoveShortcut(HashMap<String, String> shortcuts, MessageReceivedEvent event, String key) {
		File shcuts = new File("shortcuts.txt");
		FileReader fr = null;
		BufferedReader bf = null;

		if (shcuts.exists()) {

			try {

				fr = new FileReader(shcuts);
				bf = new BufferedReader(fr);
				String line;
				String newContent = null;
				while ((line = bf.readLine()) != null) {

					String localKey = line.split(",")[0];
					System.out.println(localKey + "@@@@@@" + key);
					if(localKey != key)
					{
						newContent += line + "\n";
					}
					
				}
				System.out.println(newContent);
				
			} catch (Exception exception) {

			}
			finally
			{
				try {
				fr.close();
				bf.close();
				}
				catch(IOException exception)
				{
					
				}
			}
		}
		else
		{
			try
			{
				Files.createFile(Paths.get("shortcuts.txt"));
			}
			catch(Exception exception)
			{
				
			}
		}
		
	}

}
