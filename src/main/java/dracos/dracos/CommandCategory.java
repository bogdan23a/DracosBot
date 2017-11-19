package dracos.dracos;

import java.util.ArrayList;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;
public enum CommandCategory {
	/**
	 * These permissions are at high level.
	 * Requirements that they need to operate!
	 * Individual command restrictions are done in the called() method!
	 */
	DEVELOPER( "developer", "Developing", "Category of commands reserved for developers.",  new ArrayList<Permission>() ), // no perms needed checked by id
	
	ADMINISTRATIVE( "administrative", "Administration", "Administrator only commands.", new ArrayList<Permission>() {{
		add(Permission.ADMINISTRATOR);
	}}),
	
	INFORMATIVE( "informative", "Information", "Informative commands.", new ArrayList<Permission>() {{
		add(Permission.MESSAGE_READ);
	}} ),
	
	FUN( "fun", "Fun", "Fun section of commands.", new ArrayList<Permission>() {{
		add(Permission.MESSAGE_READ);
	}} ),
	
	UTILITY( "utility", "Utility", "Uitlity based commands.", new ArrayList<Permission>() {{
		add(Permission.MESSAGE_READ);
	}} ),
	
	MUSIC( "utility", "Utility", "Commands for the MUSIC system", new ArrayList<Permission>() {{
		add(Permission.MESSAGE_READ);
		add(Permission.VOICE_CONNECT);
	}} ),
	
	UNKNOWN("unknown", "Unknown", "Other category of commands",new ArrayList<Permission>() {{
		add(Permission.MESSAGE_READ);
	}});
	
	private final String ID;
	private final String displayName;
	private final String description;
	private final ArrayList<Permission> neededPermissions;
	
	CommandCategory(String ID, String displayName, String description, ArrayList<Permission> neededPermissions) {
	    this.ID = ID;
	    this.displayName = displayName;
	    this.description = description;
	    this.neededPermissions = neededPermissions;
	}

	public String getDisplayName() {
	    return displayName;
	}
	
	public String getID() {
	    return ID;
	}

	public String getDesciption() {
	    return description;
	}

	public ArrayList<Permission> getNeededPermissions() {
		return neededPermissions;
	}
	
	public boolean hasPermissions( Member m ){
		for( Permission v : neededPermissions ){
			if ( !PermissionUtil.checkPermission( m, v ) )
				return false;
		}
		return true;
	}
}