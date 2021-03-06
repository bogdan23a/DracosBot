package dracos.dracos;

import java.awt.Color;
import java.util.Random;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

public class DiscordMessage {

	private static String[] warnings = {
										"https://i.pinimg.com/736x/4a/36/b5/4a36b51e3b561f77497dcd8a732e9671--ways-to-say-no-soliciting-signs.jpg",							
										"https://i.pinimg.com/564x/d6/15/fc/d615fc041771a121a655891f0cc796d3.jpg",
										"https://img.memecdn.com/making-mistakes_o_2181867.jpg",
										"https://i.pinimg.com/564x/f3/59/7f/f3597fe5ccf7f283cad91d2a10e89c2e.jpg",
										"https://img.memecdn.com/mistake_o_992678.jpg"
	};
	
	/**
	 * Creates a simple Discord Message
	 * @param text
	 * @return
	 */
	public static Message toSimple(String text)
	{
		MessageBuilder mb = new MessageBuilder();
		mb.append(checkLength(text));
		return mb.build();
	}


	/**
	 * Creates and Toggles TTS of a Discord message and appends text
	 * @param text
	 * @return
	 */
	public static Message toTTS(String text)
	{
		MessageBuilder mb = new MessageBuilder();
		mb.setTTS(true);
		mb.append(checkLength(text));
		return mb.build();
	}
	
	
	   /**
	    * Creates a PROBLEM type Discord Message. Displaying a red icon and text notifying the users a problem has happened.
	    * @param u
	    * @param text
	    * @return
	    */	
	
	public static MessageEmbed embedProblem(User u, String text)
	{
		Random randomNum = new Random();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(),u.getAvatarUrl(),u.getAvatarUrl());
		eb.setColor(Color.red);
		eb.setTitle("Oops! Problem Occured");
		eb.setDescription(text);
		eb.setThumbnail(warnings[randomNum.nextInt(4)]);
		return eb.build();
	}
	
	
	
	   /**
	    * Creates a Notification embed message. The standard for any warnining message.
	    * @param u
	    * @param text
	    * @return
	    */	
	
	public static MessageEmbed embedNotification(User u, String text)
	{
		Random randomNum = new Random();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(),u.getAvatarUrl(),u.getAvatarUrl());
		eb.setColor(Color.yellow);
		eb.setTitle("Note!");
		eb.setDescription(checkEmbedLength(text));
		eb.setThumbnail(warnings[randomNum.nextInt(4)]);
		return eb.build();
	}
	
	
	
	   /**
	    * Embed message used by the music player.
	    * @param u
	    * @param title
	    * @param text
	    * @return
	    */	
	public static MessageEmbed embedMusicMessage(User u, String title, String text)
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(),u.getAvatarUrl(),u.getAvatarUrl());
		eb.setColor(Color.blue);
		eb.setTitle("Music Player -" + title);
		eb.setDescription(checkEmbedLength(text));
		eb.setThumbnail("https://cdn.shopify.com/s/files/1/1906/9041/products/24_1_73b25961-de08-4641-b1af-a677b3483039_grande.jpg");
		return eb.build();
	}
	
	
	
	/**
	    * Create an Embed Message. Wrap method.
	    * @param u
	    * @param title
	    * @param text
	    * @param color
	    * @param imageurl
	    * @return
	    */	
	public static MessageEmbed embedMessage(User u, String text, Color color, String title, String imgurl)
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(u.getName(),u.getAvatarUrl(),u.getAvatarUrl());
		eb.setColor(color);
		eb.setTitle(title);
		eb.setDescription(checkEmbedLength(text));
		eb.setThumbnail(imgurl);
		return eb.build();
	}
	
	
	
	
	/**
	    * Wrap method for a simple embed message. Cut the bullshit.
	    * @param u
	    * @param title
	    * @param text
	    * @return
	    */
	   public static MessageEmbed embedSimple(User u, String title, String text) 
	   {
	      EmbedBuilder eb = new EmbedBuilder();
	      eb.setAuthor(u.getName(), u.getAvatarUrl(), u.getAvatarUrl());
	      eb.setColor(Color.BLACK);
	      eb.setDescription(checkEmbedLength(text));
	      eb.setTitle(title);
	      return eb.build();
	   }
	   
	   
	private static String checkLength(String msg) {
	      if (msg.length() <= 4000)
	         return msg;
	      return msg.substring(0, 4000 - 5) + "(...)";
	   }

	   private static String checkEmbedLength(String msg) {
	      if (msg.length() <= 2048)
	         return msg;
	      return msg.substring(0, 2048 - 5) + "(...)";
	   }
}
