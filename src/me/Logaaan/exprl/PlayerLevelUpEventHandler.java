package me.Logaaan.exprl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class PlayerLevelUpEventHandler implements Listener {
	
	Main p;
	public List<String> instructions;
	
	public PlayerLevelUpEventHandler(Main m) {
		p = m;
		instructions = new ArrayList<String>();
		p.getServer().getLogger().info("Loaded PlayerLevelChangeEvent");
	}
	
	public void addSet(String s) {
		this.instructions.add(s);
	}
	
	@EventHandler
	public void onChat(PlayerLevelChangeEvent e) {
		for (String i : instructions) {
			String u = i;
			if (e.getOldLevel() < e.getNewLevel()) {
				p.nodep.proccessTask2(u, new Object[] { e.getPlayer(), e });
			}
		}
	}

}
