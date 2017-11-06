package dracos.music;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.Game;


/**
 * This class schedules tracks for the audio player. 
 * It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter{

	
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private boolean repeat;
	private AudioTrack repeat_song;
	
	
	/**
	 * @param player
	 *           The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player)
	{
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		repeat = false;
		repeat_song = null;
	}
	

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track
	 *           The track to play or add to queue.
	 */
	public void queue(AudioTrack track)
	{
		// Calling startTrack with the noInterrupt set to true will start the
	    // track only if nothing is currently playing. If
	    // something is playing, it returns false and does nothing. In that case
	    // the player was already playing so this
	    // track goes to the queue instead.
		if(!player.startTrack(track, true))
			queue.offer(track);
		
	}
	
	
	
	/**
	    * Start the next track, stopping the current one if it is playing.
	    */
	public void nextTrack()
	{
		
	   // Start the next track, regardless of if something is already playing or
	   // not. In case queue was empty, we are
	   // giving null to startTrack, which is a valid argument and will simply
	   // stop the player.
		
		
		if(player.isPaused())
			resume();
		if(repeat)
			player.startTrack(repeat_song.makeClone(), false);
		else
			player.startTrack(queue.poll(), false);
			}
	
	
	
	/**
	 * Reset queue
	 */
	public void clearQueue()
	{
		queue.clear();
	}
	
	
	/**
	 * Sets the volume of the track
	 * @param volume
	 */				
	public void setVolume(int volume)
	{
		player.setVolume(volume);
	}
	
	
	/**
	 * Pauses the track
	 */
	public void stop()
	{
		player.setPaused(true);
	}
	
	
	
	/**
	 * Is the track paused
	 * @return
	 */
	public boolean isPlaying()
	{
		return player.isPaused();
	}
	
	public boolean hasNextTrack()
	{
		return !queue.isEmpty();
	}
	
	
	
	
	/**
	 * Returns the tracks in queue
	 * @return
	 */
	public BlockingQueue<AudioTrack> getQueue()
	{
		return queue;
	}
	
	
	public void resume()
	{
		player.setPaused(false);
	}
	
	
	/**
	 * Returns the current song
	 * @return
	 */
	public AudioTrack getPlaying()
	{
		return player.getPlayingTrack();
	}
	
	
	public boolean toggleRepeat()
	{
		repeat = !repeat;
		if(repeat)
			repeat_song = player.getPlayingTrack();
		return repeat;
	}
	
	/**
	 * Gets the song that was saved to repeat
	 * @return
	 */
	public AudioTrack getRepeatedSong()
	{
		return repeat_song;
	}
	
	/**
	 * Shuffles the queue
	 */
	public void shuffleTrack()
	{
		Collection<AudioTrack> shuffledQueue = new ArrayList<AudioTrack>();
		queue.drainTo(shuffledQueue);
		queue.clear();
		long seed = System.nanoTime();
		Collections.shuffle((List<AudioTrack>) shuffledQueue,new Random(seed));
		
		queue.addAll(shuffledQueue);
	}
	
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason)
	{
		
		// Only start the next track if the end reason is suitable for it
	    // (FINISHED or LOAD_FAILED)
		
		if(reason.mayStartNext)
			nextTrack();
	}
	
	
	
	
	
	 
	     @Override 
	     public void onTrackStart(AudioPlayer player, AudioTrack track) {
	    	 dracos.dracos.Main.bot.getInstance().getPresence().setGame(Game.of(track.getInfo().title));

	     }
	     
	     @Override 
	     public void onTrackStuck(AudioPlayer player, AudioTrack track,
	     long thresholdMs) {
	     
	     }
	    
}
