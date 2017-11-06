package dracos.commands;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import dracos.dracos.Command;
import dracos.dracos.CommandCategory;
import dracos.dracos.CommandScope;
import dracos.dracos.DiscordMessage;
import dracos.dracos.LongMessage;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements Command{

	private final HashMap<String, String> subcommands = new HashMap<String, String>();

	@Override
	public boolean called(String raw, String[] args, MessageReceivedEvent event) {
		return true;
	}

	@Override
	public void action(String raw, String[] args, MessageReceivedEvent event) {
		if (args.length == 0) { // display general help information
			StringJoiner joiner = new StringJoiner("\n");
			Iterator<Entry<String, Command>> it = dracos.dracos.Main.bot.getCommands().entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<String, Command> pair = (Map.Entry<String, Command>) it.next();
				String key = pair.getKey();
				Command value = pair.getValue();

				if (!pair.getValue().isListed())
					continue;

				joiner.add("\u258C " + key + "[" + value.getCategory().getDisplayName() + "]" + "{"
						+ value.getScope().name() + "}(#" + value.getSubCommands().size() + "scmds): "
						+ value.getDescription());
			}

			MessageEmbed embed = DiscordMessage.embedMessage(event.getAuthor(),
					"Help Documentation - Sponsor by " + dracos.dracos.Main.bot.getInstance().getSelfUser().getName()
							+ "\n Type <prefix>help <command> | for detailed documentation.", Color.BLACK,
					"```xl\n" + joiner.toString() + "```\n", 
					"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/VisualEditor_-_Icon_-_Help.svg/2000px-VisualEditor_-_Icon_-_Help.svg.png");

			LongMessage.storeBigMessage(event.getChannel().sendMessage(embed).complete(), joiner.toString());

		} else if (args.length > 0) { // display specific information
			if (dracos.dracos.Main.bot.getCommands().containsKey(args[0].toLowerCase())) {
				HashMap<String, String> scmds = dracos.dracos.Main.bot.getCommands().get(args[0].toLowerCase()).getSubCommands();
				String help_message = null;

				if (scmds.size() == 0) {
					help_message = "```xl\n This command has 0 subcommands!```\n";
					return;
				}

				StringJoiner joiner = new StringJoiner("\n");
				Iterator<Entry<String, String>> it = scmds.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
					String key = pair.getKey();
					String value = pair.getValue();

					joiner.add("\u258C " + key + ": " + value);
				}

				help_message = "```xl\n" + joiner.toString() + "\n```";

				event.getChannel().sendMessage(DiscordMessage.embedMessage(event.getAuthor(),help_message,Color.BLACK,
						"Help for \"" + args[0].toLowerCase() + "\":",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/VisualEditor_-_Icon_-_Help.svg/2000px-VisualEditor_-_Icon_-_Help.svg.png"))
						.queue();
			} else {
				event.getChannel().sendMessage(
						DiscordMessage.embedProblem(event.getAuthor(), "There exists no " + args[0] + " command!"))
						.queue();
			}
		}
	}

	@Override
	public void Executed(boolean succ, MessageReceivedEvent event) {

	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public HashMap<String, String> getSubCommands() {
		return subcommands;
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.INFORMATIVE;
	}

	@Override
	public CommandScope getScope() {
		return CommandScope.BOTH;
	}

	@Override
	public boolean isListed() {
		return false;
	}
}
