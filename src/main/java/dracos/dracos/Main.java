package dracos.dracos;

import java.util.ArrayList;
import java.util.HashMap;

import dracos.music.MusicHandler;
import dracos.commands.Help;
import dracos.commands.Music;
import dracos.commands.Ping;
import dracos.commands.Youtube;
import dracos.listeners.GeneralListener;
import dracos.listeners.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class Main extends ListenerAdapter {
	public static Client bot;
	public static MusicHandler musicHandler;
	public static String token;
	public static void main(String[] args) 
	{
		final HashMap<String, Command> commands = new HashMap<String, Command>();
		final CommandParser commandparser = new CommandParser();
		final String[] prefixes = new String[2];
		final MusicHandler musicHandler = new MusicHandler();
		final ArrayList<LongMessage> storageBigMessages = new ArrayList<LongMessage>();
		
		
		token = Ref.getTokenAndClientID()[0];
		prefixes[0] = "!";
		
		commands.put("ping", new Ping());
		commands.put("imdb", new IMDB());
		commands.put("help", new Help());
		commands.put("music", new Music());
		commands.put("youtube", new Youtube());
		try
		{
			bot = new Client(new JDABuilder(AccountType.BOT)
					.setToken(token)
					.setGame(Game.of("yourMom"))
					.setAutoReconnect(true)
					.setBulkDeleteSplittingEnabled(false)
					.setAudioEnabled(true)
					.addEventListener(new MessageListener())
					.addEventListener(new GeneralListener())
					.buildBlocking()
					,commands
					,prefixes
					,commandparser
					,musicHandler
					,storageBigMessages
					);
					}
		catch(Exception e)
		{
			System.out.println("Error in building the api in main class : " + e.getMessage());
		}
	}
	
	public void onGuildJoinEvent(GuildJoinEvent event)
	{
		System.out.printf("[+] %s (%s Members)",event.getGuild().getName(), event.getGuild().getMembers().size());
	}
}
