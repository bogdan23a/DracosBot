package dracos.dracos;

import net.dv8tion.jda.core.entities.ChannelType;

public enum CommandScope {
	   BOTH,
	   PUBLIC,
	   PRIVATE;

	   public static boolean matchChannelType(ChannelType ct, CommandScope cs) {
	      if (cs.equals(CommandScope.BOTH) || (ct.equals(ChannelType.PRIVATE) && cs.equals(CommandScope.PRIVATE))
	            || (ct.equals(ChannelType.TEXT) && cs.equals(CommandScope.PUBLIC))) {
	         return true;
	      }
	      return false;
	   }
	}