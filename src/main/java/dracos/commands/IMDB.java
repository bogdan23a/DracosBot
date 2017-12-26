package dracos.commands;

import java.util.HashMap;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class IMDB implements Command{

	private final static HashMap<String, String> subcommands;
	
	
	static {
		subcommands = new HashMap<String, String>();
		subcommands.put("comedy", "Searches for comedy movies");
		subcommands.put("horror", "Searches for horror movies");
		subcommands.put("action", "Searches for action movies");
		subcommands.put("drama", "Searches for drama movies");
		subcommands.put("western", "Searches for western movies");
		subcommands.put("animation", "Searches for animation movies");
		subcommands.put("romance", "Searches for romance movies");
		subcommands.put("fiction", "Searches for fiction movies");
		subcommands.put("thriller", "Searches for thriller movies");
		subcommands.put("adventure", "Searches for adventure movies");
		subcommands.put("scifi", "Searches for scifi movies");
		subcommands.put("documentary", "Searches for documentary movies");
		subcommands.put("musical", "Searches for music movies");
		subcommands.put("historical", "Searches for historical movies");
		subcommands.put("movie", "Searches for a movie");
		subcommands.put("actor", "Searches for movies in which this actor played");
		subcommands.put("blank", "Searches for ");
		
		
		
	}
	@Override
	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public void action(String raw, String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Executed(boolean succ, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "all IMDB commands start with this one";
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		// TODO Auto-generated method stub
		return subcommands;
	}

	@Override
	public CommandCategory getCategory() {
		// TODO Auto-generated method stub
		return CommandCategory.UTILITY;
	}

	@Override
	public CommandScope getScope() {
		// TODO Auto-generated method stub
		return CommandScope.PUBLIC;
	}

	@Override
	public boolean isListed() {
		// TODO Auto-generated method stub
		return true;
	}

}
