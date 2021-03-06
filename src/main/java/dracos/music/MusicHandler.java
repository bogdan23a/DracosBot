package dracos.music;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dracos.dracos.DiscordMessage;
import dracos.dracos.LongMessage;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
public class MusicHandler {
	
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	
	public MusicHandler()
	{
		this.musicManagers = new HashMap<>();
		/**
		 * musicManager.scheduler.setVolume(10);
		 */
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}
	
	
	
	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild)
	{
		Long guildID = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildID);
		if(musicManager == null)
		{
			musicManager = new GuildMusicManager(playerManager, guild);
			musicManagers.put(guildID, musicManager); 
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		return musicManager;
	}
	
	/**
	 * Queues next song to play or plays if there is no queue
	 * @param servicer
	 * @param channel
	 * @param trackUrl
	 */
	public void loadAndPlay(final User servicer, final TextChannel channel, final String trackUrl)
	{
		if(!checkConnection(channel))
			if(!tryConnectVoiceChannel(channel, servicer))
				return;
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		String msg_id = channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Processing...", "Please wait"))
				.complete().getId();
		channel.sendTyping().queue();
		
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track)
			{
				channel.editMessageById(msg_id, DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Song Added!", "Adding to queue " + track.getInfo().title))
				.queue();
				queue(channel, track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist)
			{
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if(firstTrack == null)
					firstTrack = playlist.getTracks().get(0);
				
				int i = 0;
				for(AudioTrack at : playlist.getTracks())
				{
					i++;
					queue(channel, at);
					if((i % (playlist.getTracks().size() / 5) == 0))// only 5 requests 
					{
						channel.editMessageById(msg_id, DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Playlist Added", "Proccesing " + playlist.getName() + "(Added " + i + " songs to queue)"))
						.queue();
					}
				}
			}
			
			@Override
			public void noMatches()
			{
				channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Nothing found by : " + trackUrl )).queue();
			}
			
			@Override
			public void loadFailed(FriendlyException exception)
			{
				channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Could not play: " + exception.getMessage())).queue();
				
			}
		});
	}
	
	
	
	
	public void queue(TextChannel channel, AudioTrack track)
	{
		if(!checkConnection(channel))
		{
			channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Please make sure the bot is connected to the channel(Use !music play <shortcut/link/file>)")).queue();
			return;
		}
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.queue(track);
		
		
	}
	
	
	
	public void getQueue(TextChannel channel)
	{
		if(!checkConnection(channel))
			return;
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		
		if(queue.isEmpty())
		{
			channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Oops!There is nothing in the queue")).queue();
			return;
		}
		
		
		StringJoiner joiner = new StringJoiner("\n");
		int count = 0;
		for(AudioTrack t : queue)
		{
			joiner.add("**" + count + " - NEW TRACK** - ID: " +t.getInfo().identifier);
			joiner.add("\u258C Title   " + t.getInfo().title + "\n");
			joiner.add("\u258C Author  " + t.getInfo().author + "\n");
			joiner.add("\u258C Length  " + (double)(t.getInfo().length / 1000) / 60 + "\n");
			joiner.add("\u258C URI     " + t.getInfo().uri + "\n");
			joiner.add("_________________________________");
			count++;
		}
		
		
		MessageEmbed embed = DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Queue Display - **Size: " + count + "**", joiner.toString());
		
		LongMessage.storeBigMessage(channel.sendMessage(embed).complete(), joiner.toString());
		
	}
	
	
	
	
	
	public void skipTrack(TextChannel channel)
	{
		 if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      if (musicManager.scheduler.hasNextTrack()) {
	         musicManager.scheduler.nextTrack();
	         AudioTrack nextrack = musicManager.scheduler.getPlaying();
	         channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Song Skip",
	               " Skipped to next track: [" + nextrack.getInfo().title + "]")).queue();
	      } else {
	         channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Song Skip - End Track", "There is no next track! Ending song...")).queue();
	         musicManager.player.destroy();
	      }
	}
	
	
	public void toggleRepeat(TextChannel channel)
	{
		if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Repeat",
	            " Repeating current track set to: **" + musicManager.scheduler.toggleRepeat() + "**")).queue();
	}
	
	
	/**
	    * Clears current queue.
	    * 
	    * @param channel
	    */
	   public void clearQueue(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      musicManager.scheduler.clearQueue();
	      channel.sendMessage(
	            DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Reset!", "Queue has been reset!"))
	            .queue();
	   }

	   /**
	    * Sets the music player volume.
	    * 
	    * @param channel
	    * @param volume
	    */
	   public void setVolume(TextChannel channel, int volume) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      if (volume < 0 || volume > 100) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Invalid volume value! 0 to 100 please!")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      musicManager.scheduler.setVolume(volume);
	      channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Volume Control",
	            "Volume of player set to: **" + volume + "**")).queue();
	   }

	   /**
	    * Pauses current song.
	    * 
	    * @param channel
	    */
	   public void pause(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      if (!musicManager.scheduler.isPlaying()) {
	         AudioTrack track = musicManager.scheduler.getPlaying();
	         musicManager.scheduler.stop();
	         channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Paused!",
	               "Song [" + track.getInfo().title + "] was paused!")).queue();
	      } else {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Nothing is playing!"))
	               .queue();
	      }
	   }

	   /**
	    * Resumes current song.
	    * 
	    * @param channel
	    */
	   public void resume(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      if (musicManager.scheduler.isPlaying()) {
	         musicManager.scheduler.resume();
	         AudioTrack track = musicManager.scheduler.getPlaying();
	         channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Resumed!",
	               "Song [" + track.getInfo().title + "] was resumed!")).queue();
	      } else {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Nothing is paused!"))
	               .queue();
	      }
	   }

	   /**
	    * Leaves voice channel and turns audio off.
	    * 
	    * @param channel
	    */
	   public void leave(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      musicManager.player.destroy();
	      musicManager.scheduler.clearQueue();
	      JDA drac = dracos.dracos.Main.bot.getInstance();
	      channel.getGuild().getAudioManager().closeAudioConnection();
	      channel.sendMessage(
	            DiscordMessage.embedMusicMessage(drac.getSelfUser(), "Disconnected!", "Leaving Voice channel! Cya later!"))
	            .queue();
	   }

	   /**
	    * Displays information of the currently playing song, if any.
	    * 
	    * @param channel
	    */
	   public void nowPlaying(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <shortcut/link/file>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      JDA nis = dracos.dracos.Main.bot.getInstance();

	      if (!musicManager.scheduler.isPlaying()) {
	         StringJoiner joiner = new StringJoiner("\n");
	         AudioTrack t = musicManager.scheduler.getPlaying();

	         joiner.add("Now Playing - ID:" + t.getInfo().identifier);
	         joiner.add("\u258C Title     " + t.getInfo().title);
	         joiner.add("\u258C Author    " + t.getInfo().author);
	         joiner.add("\u258C Length    " + t.getInfo().length);
	         joiner.add("\u258C URI       " + t.getInfo().uri);

	         channel
	               .sendMessage(DiscordMessage.embedMusicMessage(nis.getSelfUser(), "Now Playing info:", joiner.toString()))
	               .queue();
	      } else {
	         channel.sendMessage(
	               DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Currently nothing is playing!)"))
	               .queue();
	      }
	   }

	   /**
	    * Toggle shuffle mode
	    * 
	    * @param channel
	    */
	   public void shuffle(TextChannel channel) {
	      if (!checkConnection(channel)) {
	         channel.sendMessage(DiscordMessage.embedProblem(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	               "Please make sure the bot is connected to a voice channel! (use !music play <x>)")).queue();
	         return;
	      }

	      GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
	      musicManager.scheduler.shuffleTrack();
	      channel.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Shuffle!",
	            "Queue has been shuffled! Check !music queue")).queue();
	   }

	   // privates utility
	   private boolean checkConnection(TextChannel channel) {
	      AudioManager audioManager = channel.getGuild().getAudioManager();
	      if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
	         return false;
	      }
	      return true;
	   }

	   // utility that makes bot join a voice channel and play music depending on
	   // some factors
	   private boolean tryConnectVoiceChannel(TextChannel channel, User servicer) {
	      AudioManager audioManager = channel.getGuild().getAudioManager();
	      if (audioManager.isConnected() || audioManager.isAttemptingToConnect()) // if already connected or connecting ignore!
	         return false;

	      List<VoiceChannel> channels = audioManager.getGuild().getVoiceChannels();
	      VoiceChannel connected_channel = null;
	      for (VoiceChannel voiceChannel : channels) { // get channel servicer is on - has to be a better way??
	         boolean found = false;
	         for (Member m : voiceChannel.getMembers()) {
	            if (m.getUser().equals(servicer)) {
	               found = true;
	               break;
	            }
	         }

	         if (found) {
	            audioManager.openAudioConnection((net.dv8tion.jda.core.entities.VoiceChannel) voiceChannel);
	            connected_channel = voiceChannel;
	            break;
	         }
	      }

	      if (connected_channel == null) { // if he was not in any...
	         channel
	               .sendMessage(DiscordMessage.embedProblem(servicer,
	                     "You are **not** in a voice channel! \n The servicer of this command must join a voice channel!"))
	               .queue();
	         return false;
	      }

	      channel
	            .sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(),
	                  "Joined Voice Channel!", "Joined channel **" + connected_channel.getName() + "** to play music!"))
	            .queue();
	      return true;
	   }
}
