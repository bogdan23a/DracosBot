package dracos.commands;

import java.util.HashMap;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class IMDB implements Command{

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
		return null;
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandCategory getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandScope getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isListed() {
		// TODO Auto-generated method stub
		return false;
	}

}
