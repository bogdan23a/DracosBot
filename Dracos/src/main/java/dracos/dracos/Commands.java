package dracos.dracos;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter{
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		String[] command = event.getMessage().getContent().split(" ");
		
		if(!command[0].startsWith("!"))
			return;
		if(command[0].equalsIgnoreCase("!ping"))
		{
			String msg = "Pong `" + event.getJDA().getPing() + "`";
			if(command.length == 1)	
				event.getChannel().sendMessage(msg).queue();
			else if(command.length == 2 && command[1].equalsIgnoreCase("-e"))
			{
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(Color.BLUE);
				eb.setDescription(msg);
				event.getChannel().sendMessage(eb.build()).queue();
			}
		}	
	}

}
