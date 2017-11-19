package dracos.listeners;

import java.util.ArrayList;

import dracos.dracos.DiscordMessage;
import dracos.dracos.Logger;
import dracos.dracos.LongMessage;
import dracos.dracos.Command;
import dracos.dracos.CommandParser;
import dracos.dracos.CommandScope;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class MessageListener extends ListenerAdapter{
	/*
	    * When we receive a message this pops! :D (non-Javadoc)
	    * 
	    * @see
	    * net.dv8tion.jda.core.hooks.ListenerAdapter#onMessageReceived(net.dv8tion.
	    * jda.core.events.message.MessageReceivedEvent)
	    */
	   @Override
	   public void onMessageReceived(MessageReceivedEvent event) {
	      User nissan = event.getJDA().getSelfUser();
	      User author = event.getMessage().getAuthor();
	      String message = event.getMessage().getContent();

	      if (event.getChannelType() == ChannelType.PRIVATE) {
	         Logger.print("[PRIVATE][" + author.getName() + "] " + message);
	      } else {
	         Logger.print("[" + event.getGuild().getName() + "][" + author.getName() + "] " + message);
	      }

	      for (String x : dracos.dracos.Main.bot.getPrefixes()) {
	    	  
	         if (message.startsWith(x) && (author.getId() != nissan.getId())) {
	            handleCommand(dracos.dracos.Main.bot.getCommandparser().parse(message, event));
	         }
	      }
	   }

	   /*
	    * Support for long Messages - Working decently - meh!
	    */
	   @Override
	   public void onMessageReactionAdd(MessageReactionAddEvent event) {
	      if (event.getUser().equals(dracos.dracos.Main.bot.getInstance().getSelfUser()))
	         return; // First reactions are automated on long messages made by the
	                 // bot!

	      String msg_id = event.getReaction().getMessageId();
	      ArrayList<LongMessage> storage = dracos.dracos.Main.bot.getStorageBigMessages();
	      LongMessage message = null;

	      for (LongMessage v : storage) { // may slow down if a lot of long
	                                      // messages! Manage that somewhere else
	                                      // pl0x
	         if (v.getMessageID().equals(msg_id)) {
	            message = v;
	            break;
	         }
	      }

	      if (message == null)
	         return; // Not a long message or smthg is wrong, we dont care!

	      Message jdamessage = event.getChannel().getMessageById(msg_id).complete();
	      String emote_name = event.getReactionEmote().getName();
	      int current_page = message.getPage();

	      if (emote_name.equals("\u2B05")) { // If it is the left arrow reaction
	         message.setPage(--current_page);
	         jdamessage.clearReactions();
	      } else if (emote_name.equals("\u274E")) { // Cross
	         event.getChannel().deleteMessageById(msg_id).complete();
	         LongMessage.removeFromStorage(msg_id); // Remove from storage! Save
	                                                // memory.
	         return;
	      } else if (emote_name.equals("\u27A1")) { // Right arrow
	         message.setPage(++current_page);
	      } else {
	         return; // we dont care about other emotes!
	      }

	      if (message.hasEmbeds()) {
	         MessageEmbed embed = message.getEmbed().get(0);
	         EmbedBuilder eb = new EmbedBuilder();
	         eb.setAuthor(embed.getAuthor().getName(), embed.getAuthor().getIconUrl(), embed.getAuthor().getIconUrl());
	         eb.setColor(embed.getColor());
	         eb.setDescription(message.getCurrentContent());
	         eb.setTitle(embed.getTitle());
	         eb.setThumbnail(embed.getThumbnail().getProxyUrl());

	         event.getChannel().editMessageById(message.getMessageID(), eb.build()).queue();
	      } else {
	         event.getChannel().editMessageById(message.getMessageID(), message.getCurrentContent()).queue();
	      }

	      // We do this so that we dont have to clear our reaction to react again!
	      event.getReaction().removeReaction(event.getUser()).complete();
	   }

	   /**
	    * Handles Commands - Post parse
	    * 
	    * @param cmd
	    */
	   private void handleCommand(CommandParser.CommandContainer cmd) {
	      if (dracos.dracos.Main.bot.getCommands().containsKey(cmd.invoke)) {
	         // BlackList system
	        /* if (dracos.dracos.Main.bot.getBlackList().contains(cmd.event.getAuthor())) {
	            cmd.event.getChannel().sendMessage(DiscordMessage.embedBlackListMessage(cmd.event.getAuthor(), "BLOCKED",
	                  "You are blocked from using commands!")).queue();
	            return;*/
	         }
	         // Check Permission
	         Command command = dracos.dracos.Main.bot.getCommands().get(cmd.invoke);/*
	         if (!command.getCategory().hasPermissions(cmd.event.getMember())) {
	            cmd.event.getChannel()
	                  .sendMessage(DiscordMessage.embedBlackListMessage(cmd.event.getAuthor(),
	                        "LACK OF PERMISSIONS",
	                        "You do not have the required permissions for this command type! Needed permissions: `"
	                              + command.getCategory().getNeededPermissions().toString() + "`"))
	                  .queue();
	            return;
	         }*/
	         // Check Channel type
	         if (CommandScope.matchChannelType(cmd.event.getChannelType(), command.getScope())) { // The
	                                                                                              // the
	                                                                                              // types
	                                                                                              // match!
	            boolean safe = dracos.dracos.Main.bot.getCommands().get(cmd.invoke).called(cmd.raw, cmd.args, cmd.event);
	            // Check safety
	            if (safe) {
	               command.action(cmd.raw, cmd.args, cmd.event);
	               command.Executed(safe, cmd.event);
	            } else {
	               command.Executed(safe, cmd.event);
	            }
	         } else { // Invalid channel-command match!
	            cmd.event.getChannel().sendMessage(DiscordMessage.embedProblem(cmd.event.getAuthor(),
	                  "This command cannot be operated under this channel type!"
	                  ))
	            .queue();
	         }
	   }
}

