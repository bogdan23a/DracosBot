package dracos.commands;

import java.awt.Color;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import dracos.dracos.DiscordMessage;
import dracos.util.JsonUrlParse;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Youtube implements Command {
   private int LIMIT = 3;
   private final static HashMap<String, String> subcommands;

   static {
      subcommands = new HashMap<String, String>();
      subcommands.put("limit", "Sets limit for the number of search queries displayed!");
      subcommands.put("<keywords>", "A set of keywords to be parsed for search.");
   }

   @Override
   public boolean called(String raw, String[] args, MessageReceivedEvent event) {
      if (args.length == 0) {
         event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Invalid arguments!")).queue();
         return false;
      }
      return true;
   }

   @Override
   public void action(String raw, String[] args, MessageReceivedEvent event) {
      if (args[0].equalsIgnoreCase("limit")) {
         try {
            LIMIT = Integer.parseInt(args[1]);
            event.getChannel()
                  .sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
                        "Youtube - Settings",
                        Color.RED,
                        "**Limit configuration:** Limit set!( Limit: " + LIMIT + " )",
                        
                        "http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"
                  ))
            .complete().getId();
         } catch (NumberFormatException e) {
            event.getChannel()
                  .sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Could not read number (>.<)")).queue();
         }
         ;
         return;
      }

      try {
         String query = String.join(" ", args);
         String msg_id = event.getChannel()
               .sendMessage(DiscordMessage.embedMessage(event.getAuthor(),
                     "Youtube - Processing",
                     Color.RED,
                     "**Searching through youtube...** ( Limit: " + LIMIT + " )",
                     
                     "http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"
               ))
         .complete().getId();

         query = query.replace(" ", "+");
         String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" 
               + LIMIT + "&type=video&q="
               + query + "&key=" + "AIzaSyCR_Yz92gj5lC-1TZm3lw5fsEYAKwDaj-Y";//YOUTUBE API KEY

         JSONObject jsonObject = new JSONObject(JsonUrlParse.readUrl(url));
         if (jsonObject.has("error")) {
            event.getChannel()
                  .sendMessage(
                        DiscordMessage.embedProblem(event.getAuthor(), "**API ERROR:** " + jsonObject.get("error")))
                  .queue();
            return;
         }

         JSONArray jsonArray = jsonObject.getJSONArray("items"); // .
         event.getChannel().editMessageById(msg_id,
               DiscordMessage.embedMessage(event.getAuthor(),
                     "Youtube - Results",
                     Color.RED,
                     "Searched " + query,
                     
                     "http://www.freeiconspng.com/uploads/youtube-subscribe-png-28.png"
                     ))
               .queue();

         for (int i = 0; i < jsonArray.length(); i++) {
            if (i > LIMIT) // doubt that will happen but you never know...
               break;
            event.getChannel().sendMessage("https://www.youtube.com/watch?v="
                  + jsonArray.getJSONObject(i).getJSONObject("id").getString("videoId")).queue();
         }

      } catch (Exception e) {
         event.getChannel()
               .sendMessage(DiscordMessage.embedProblem(event.getAuthor(), "Unable to get YouTube results.")).queue();
         e.printStackTrace();
      }
   }

   @Override
   public void Executed(boolean succ, MessageReceivedEvent event) {
      // TODO Auto-generated method stub

   }

   @Override
   public String getDescription() {
      return "Searches youtube for videos.";
   }

   @Override
   public HashMap<String, String> getSubCommands() {
      return subcommands;
   }

   @Override
   public CommandCategory getCategory() {
      return CommandCategory.UTILITY;
   }

   @Override
   public CommandScope getScope() {
      return CommandScope.BOTH;
   }

   @Override
   public boolean isListed() {
      return true;
   }
}