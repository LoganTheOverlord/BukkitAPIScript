package me.Logaaan.exprl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChatEventHandler implements Listener {
	
	Main p;
	public List<String> instructions;
	
	public PlayerChatEventHandler(Main m) {
		p = m;
		instructions = new ArrayList<String>();
		p.getServer().getLogger().info("Loaded PlayerChatEvent");
	}
	
	public void addSet(String s) {
		this.instructions.add(s);
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		e.getPlayer().get
		for (String i : instructions) {
			String u = i;
			if (u.contains("give")) {
				p.getServer().getLogger().info("give");
			}
			if (u.contains("to player")) {
				u = u.replace("to player", "to "+e.getPlayer().getName());
			}
			if (u.contains("%player%")) {
				u = u.replaceAll("%player%", e.getPlayer().getName());
			}
			if (u.contains("%message%")) {
				u = u.replaceAll("%message%", e.getMessage());
			}
			if (u.equals("cancel event")) {
				e.setCancelled(true);
			}
			p.nodep.proccessTask2(u, new Object[] { e.getPlayer(), e });
		}
	}

}
