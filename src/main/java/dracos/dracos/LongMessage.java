package dracos.dracos;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class LongMessage {
	private final String messageID;
	private final ArrayList<String> messageParts;
	private final List<MessageEmbed> embed;
	private int page;

	public LongMessage(String messageID, ArrayList<String> messageParts, List<MessageEmbed> embed) {
		this.messageID = messageID;
		this.messageParts = messageParts;
		this.embed = embed;
		page = 0;
	}

	public String getMessageID() {
		return messageID;
	}

	public ArrayList<String> getMessageParts() {
		return messageParts;
	}

	public int getPage() {
		return page;
	}

	public String getCurrentContent() {
		return messageParts.get(page);
	}

	public List<MessageEmbed> getEmbed() {
		return embed;
	}

	public boolean hasEmbeds() {
		return !(embed == null);
	}

	public void setPage(int page) {
		if ((page >= messageParts.size()) || (page < 0))
			return;
		this.page = page;
	}

	public static boolean storageHasMessage(String id) {
		for (LongMessage v : dracos.dracos.Main.bot.getStorageBigMessages()) { // may slow
																	// down if a
																	// lot of long
																	// messages!
																	// Manage that
																	// somewhere
																	// else pl0x
			if (v.getMessageID().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public static void removeFromStorage(String id) {
		int i = 0;
		for (LongMessage v : dracos.dracos.Main.bot.getStorageBigMessages()) { // may slow
																	// down if a
																	// lot of long
																	// messages!
																	// Manage that
																	// somewhere
																	// else pl0x
			if (v.getMessageID().equals(id)) {
				dracos.dracos.Main.bot.getStorageBigMessages().remove(i);
				break;
			}
			i++;
		}
	}

	public static void storeBigMessage(Message msg, String content) { // string
																		// contains
																		// initial
																		// content
																		// since it
																		// gets
																		// trimmed!
		Logger.debug("LONG MESSAGE RAN");
		// Defaults - for normal messages
		int DIVISOR = 4000;
		List<MessageEmbed> embed = null;

		if (!msg.getEmbeds().isEmpty()) { // Currently only supports 1 embed per
											// message
			DIVISOR = 2048;
			embed = msg.getEmbeds();
		}

		if ((content.length() < DIVISOR) || (content.length() > 20000)) // dont
																		// store
																		// too big
																		// or too
																		// small
																		// messages
			return;

		// Store big message!
		ArrayList<String> message = new ArrayList<String>();
		while (content.length() > 0) {
			if (content.length() < DIVISOR) {
				message.add(content);
				break;
			}
			String nextChunk = content.substring(0, DIVISOR);
			// store the chunk.
			message.add(nextChunk);
			// make it a fraction of its prev
			content = content.substring(DIVISOR, content.length());
		}

		dracos.dracos.Main.bot.getStorageBigMessages().add(new LongMessage(msg.getId(), message, embed));
		if (dracos.dracos.Main.bot.getStorageBigMessages().size() > 100) { // Alert for memory
																// managing!
			Logger.critical("Storage Messages over 100! Current size: " + dracos.dracos.Main.bot.getStorageBigMessages());
		}

		// Add reactions
		msg.addReaction("\u2B05").queue();
		msg.addReaction("\u274E").queue();
		msg.addReaction("\u27A1").queue();

		Logger.debug("LONG REACTIONS ADDED!");
	}
}
