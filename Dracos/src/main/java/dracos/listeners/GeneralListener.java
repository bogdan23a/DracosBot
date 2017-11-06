package dracos.listeners;


import dracos.dracos.Logger;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GeneralListener extends ListenerAdapter {
	@Override
	public void onReady(ReadyEvent event) {
		Logger.info("JDA is ready!");
	}

	@Override
	public void onResume(ResumedEvent event) {
		Logger.info("JDA has resumed!");
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		Logger.info("JDA has reconnected! Response code:" + event.getResponseNumber());
	}

	@Override
	public void onDisconnect(DisconnectEvent event) {
		//Nissan.bot.saveData(); // save data

		String data = "";
		for (String v : event.getCloudflareRays())
			data = data + v + " ";

		Logger.info("JDA has disconnected. Closed by server: " + event.isClosedByServer() + " Close code: "
				+ event.getCloseCode() + "\n Cloudfare data: " + data);
	}

	@Override
	public void onShutdown(ShutdownEvent event) {
		//dracos.dracos.Main.bot.saveData(); // save data
		Logger.info("JDA has shutdown! Close code: " + event.getCode());
	}

	@Override
	public void onStatusChange(StatusChangeEvent event) {
		Logger.info("JDA status changed - Old: " + event.getOldStatus().toString() + " New: "
				+ event.getStatus().toString());
	}

	@Override
	public void onException(ExceptionEvent event) {
		Logger.critical(event.getCause());
	}
}
