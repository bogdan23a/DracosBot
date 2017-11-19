package dracos.dracos;

import java.util.HashMap;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
	   // fields in interface are by default public static final methods are public and abstract
	   boolean called(String raw, String[] args, MessageReceivedEvent event);
	   void action(String raw, String[] args, MessageReceivedEvent event);
	   void Executed(boolean succ, MessageReceivedEvent event);
	   String getDescription();
	   HashMap<String, String> getSubCommands();
	   CommandCategory getCategory();
	   CommandScope getScope();
	   boolean isListed();
	}