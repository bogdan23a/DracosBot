package dracos.music;


import java.util.Timer;
import java.util.TimerTask;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import dracos.dracos.DiscordMessage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {

	
	 /**
	  * Delay constant for timer
	  */
	private static final int ACTIVITYDELAY = 20 * 1000;//20s
	
	 /**
	  * Delay constant for timer
	  */
	private static final int LEAVEDELAY = 10 * 1000;//10s
	
	/**
	 * The guild this manager represents
	 */
	public final Guild guild;
	
	/**
	 * Audio player for the guild.
	 */
	public final AudioPlayer player;
	
	
	/**
	 * Track scheduler for the audio player
	 */
	public final TrackScheduler scheduler;
	
	/**
	 * Checks activity of the player on the guild
	 */
	public final Timer checkActivity;
	
	
	
	
	
	
	/**
	 * Creates a player and a track scheduler
	 * @param manager
	 * @param guild
	 */
	public GuildMusicManager(AudioPlayerManager manager, Guild guild)
	{
		this.guild = guild;
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player);
		checkActivity = new Timer();
		checkActivity.schedule((new TimerTask() {
		
			
			public boolean isChecking = false;
			
			

			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				if(!scheduler.isPlaying() &&
				   !player.isPaused() &&
					guild.getAudioManager().isConnected() &&
				   !isChecking)//not playing and connected
				{
					isChecking = true;
					Message msg = guild.getPublicChannel()
							.sendMessage(DiscordMessage.embedMusicMessage(dracos.dracos.Main.bot.getInstance().getSelfUser(), "Auto Leave", 
									"Seems like nobody is using the bot ! Will leave music channel in " + LEAVEDELAY/1000 + " seconds!"))
							.complete();
					new Timer().schedule(new TimerTask() {
						@Override
						public void run()
						{
							if(scheduler.isPlaying() || player.isPaused()) 
								return;
							msg.editMessage(DiscordMessage.embedMusicMessage((User)dracos.dracos.Main.bot, "Auto Leave",
									"Leaving Music Channel! Clearing music data!"))
							.complete();
							player.destroy();
							scheduler.clearQueue();
							guild.getAudioManager().closeAudioConnection();
							isChecking = false;
						}
					}, LEAVEDELAY);
				}

				
			}
		}), ACTIVITYDELAY, ACTIVITYDELAY);//every 10 sec check for activity
		
		player.addListener(scheduler);
	}
	
	
	
	public AudioPlayerSendHandler getSendHandler()
	{
		return new AudioPlayerSendHandler(player);
	}
	
}
