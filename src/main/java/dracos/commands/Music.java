package dracos.commands;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import dracos.dracos.DiscordMessage;
import dracos.dracos.LongMessage;
import dracos.music.MusicHandler;
import dracos.util.ShortcutFileHandling;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Music implements Command {

	private final static HashMap<String, String> subcommands;
	private final static HashMap<String, String> shortcuts; // universal, not by
															// guild.

	static {
		subcommands = new HashMap<String, String>();
		subcommands.put("play", "Plays the specified file/url/shortcut.\n Current shortcut:");
		subcommands.put("queue", "Gets the current music player queue.");
		subcommands.put("leave", "Disconnects bot from voice channel.");
		subcommands.put("purge", "Resets the music queue.");
		subcommands.put("volume", "Sets the volume(0-100) using the next parameter.");
		subcommands.put("pause", "Pauses the current song.");
		subcommands.put("resume", "Resumes the current song.");
		subcommands.put("skip", "Skips to the next song in queue, if any.");
		subcommands.put("playing", "Displays data of the current song playing.");
		subcommands.put("shuffle", "Toggles shuffle.");
		subcommands.put("repeat", "Toggles repeat.");
		subcommands.put("types", "Displays the file/url types permited by the player.");
		subcommands.put("shortcuts", "Displays all current Universal shortcuts");
		subcommands.put("shortadd", "<shortcut> <url> adds a shortcut refering to the url");
		subcommands.put("shortremove", "<shortcut> <url> removes a shortcut refering to the url");

		// Defaults, maybe in the future make this depend on guild.
		shortcuts = new HashMap<String, String>();
		/*
		 * shortcuts.put("gamutape", "https://www.youtube.com/watch?v=x3O9rASQCuo");
		 * shortcuts.put("gasgasgas", "https://www.youtube.com/watch?v=atuFSv2bLa8");
		 * shortcuts.put("dejavu", "https://www.youtube.com/watch?v=dv13gl0a-FA");
		 * shortcuts.put("chanuto",
		 * "https://www.youtube.com/playlist?list=PLnVyz6ZocZudKIZ7S_49sISwqbU9VaXMV");
		 * shortcuts.put("chanimu",
		 * "https://www.youtube.com/playlist?list=PLnVyz6ZocZucT007Y6iWY9WUWAQOitW_M");
		 */
		shortcuts.put("beyond", "https://www.youtube.com/watch?v=HvR9-LniaPM");

		
		ShortcutFileHandling.LoadShortcuts(shortcuts);
		

	};

	@Override
	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length < 1) {
			event.getTextChannel()
					.sendMessage(
							DiscordMessage.embedProblem(event.getAuthor(), "Invalid **arguments**! Try !help music"))
					.queue();
			;
			return false;
		}
		return true;
	}

	@Override
	public void action(String raw, String[] args, MessageReceivedEvent event) {
		TextChannel channel = event.getTextChannel();
		MusicHandler mh = dracos.dracos.Main.bot.getMusicHandler();
		if (args[0].equalsIgnoreCase("play")) {
			if (args.length < 2)
				event.getTextChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid **arguments**! Try !help music"))
						.queue();
			if (shortcuts.containsKey(args[1]))
				args[1] = shortcuts.get(args[1]);
			{
				//System.out.println("_________");
				//System.out.println(args[1]);
				//System.out.println("_________");
				mh.loadAndPlay(event.getAuthor(), channel, args[1]);

			}
		} else if (args[0].equalsIgnoreCase("queue")) {
			mh.getQueue(channel);
		} else if (args[0].equalsIgnoreCase("skip")) {
			mh.skipTrack(channel);
		} else if (args[0].equalsIgnoreCase("leave")) {
			mh.leave(channel);
		} else if (args[0].equalsIgnoreCase("purge")) {
			mh.clearQueue(channel);
		} else if (args[0].equalsIgnoreCase("volume")) {
			int volume = 10;
			try {
				volume = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				event.getChannel()
						.sendMessage(
								DiscordMessage.embedProblem(event.getAuthor(), "The volume number is not a value!"))
						.queue();
				return;
			}
			mh.setVolume(channel, volume);
		} else if (args[0].equalsIgnoreCase("pause")) {
			mh.pause(channel);
		} else if (args[0].equalsIgnoreCase("resume")) {
			mh.resume(channel);
		} else if (args[0].equalsIgnoreCase("playing")) {
			mh.nowPlaying(channel);
		} else if (args[0].equalsIgnoreCase("shuffle")) {
			mh.shuffle(channel);
		} else if (args[0].equalsIgnoreCase("repeat")) {
			mh.toggleRepeat(channel);
		} else if (args[0].equalsIgnoreCase("types")) {
			event.getChannel()
					.sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "Supported Types",
							"**File Types:**\n" + "MP3\r\n" + "FLAC\r\n" + "WAV\r\n"
									+ "Matroska/WebM (AAC, Opus or Vorbis codecs)\r\n" + "MP4/M4A (AAC codec)\r\n"
									+ "OGG streams (Opus, Vorbis and FLAC codecs)\r\n" + "AAC streams\r\n"
									+ "Stream playlists (M3U and PLS)\n\n" + "**Websites/Applications Types:**\n"
									+ "YouTube\r\n" + "SoundCloud\r\n" + "Bandcamp\r\n" + "Vimeo\r\n"
									+ "Twitch streams\r\n" + "Local files\r\n" + "HTTP URLs"))
					.queue();
		} else if (args[0].equalsIgnoreCase("shortcuts")) {
			String ret = "";
			Iterator<Entry<String, String>> it = shortcuts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
				ret += "[" + pair.getKey() + ":" + pair.getValue() + "]\n";
			}
			ret = "```xl\n" + ret + "```";
			MessageEmbed embed = DiscordMessage.embedMusicMessage(event.getAuthor(), "Shortcut list:", ret);

			LongMessage.storeBigMessage(event.getChannel().sendMessage(embed).complete(), ret);

		} else if (args[0].equalsIgnoreCase("shortadd")) {
			if (args.length < 3) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments for this subcommand!"));
				return;
			}

			String key = args[1];
			String url = args[2];

			shortcuts.put(key, url);
			
			/**
			 * Save new shortcut to memory in file
			 */
			
			ShortcutFileHandling.WriteShortcut(shortcuts, event, key, url);
			
			
		} else if (args[0].equalsIgnoreCase("shortremove")) {
			if (args.length < 2) {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "Invalid #arguments for this subcommand!"));
				return;
			}

			String key = args[1];
			if (!shortcuts.containsKey(key)) {
				event.getChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
						"Could not find that shortcut! Check !music shortcuts."));
				return;
			}

			shortcuts.remove(key);
			event.getChannel().sendMessage(DiscordMessage.embedMusicMessage(event.getAuthor(), "Removed Shortcut:",
					"Removed **" + key + "** out of shortcuts!")).queue();
			ShortcutFileHandling.RemoveShortcut(shortcuts,event, key);
			System.out.println(key);
		} else {
			event.getTextChannel().sendMessage(DiscordMessage.embedProblem(event.getAuthor(),
					"Invalid **sub command**! Please check the list using !help music")).queue();
		}
	}

	@Override
	public void Executed(boolean succ, MessageReceivedEvent event) {

	}

	@Override
	public String getDescription() {
		return "All the music player commands start with this one.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.UTILITY;
	}

	@Override
	public CommandScope getScope() {
		return CommandScope.PUBLIC;
	}

	@Override
	public boolean isListed() {
		return true;
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}
}
