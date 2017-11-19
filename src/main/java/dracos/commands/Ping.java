package dracos.commands;

import java.awt.Color;
import java.util.HashMap;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import dracos.dracos.DiscordMessage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping implements Command {
	   private long t0;

	   public boolean called(String raw, String[] args, MessageReceivedEvent event) {
	      t0 = System.currentTimeMillis();
	      return true; // always runs, anyone is allowed to run this cmd!
	   }

	   public void action(String raw, String[] args, MessageReceivedEvent event) {
	      event.getChannel()
	            .sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
	                  "PONG!",
	                  Color.white,
	                  "I took **" + (System.currentTimeMillis() - t0) + "ms** to answer that!\n My latency: "
	                  + event.getJDA().getPing() + "ms",
	                  
	                  "http://www.yorozuyasoul.com/images/omake/omake_ep4-1.jpg"
	             ))
	     .queue();
	   }

	   public void Executed(boolean succ, MessageReceivedEvent event) {

	   }

	   public String getDescription() {
	      return "Ping, pong!";
	   }

	   public CommandScope getScope() {
	      return CommandScope.BOTH; // Can run in private channels
	   }

	   public CommandCategory getCategory() {
	      return CommandCategory.UTILITY;
	   }

	   public boolean isListed() {
	      return true;
	   }

	   @Override
	   public HashMap<String, String> getSubCommands() {
	      return new HashMap<String, String>();
	   }
}