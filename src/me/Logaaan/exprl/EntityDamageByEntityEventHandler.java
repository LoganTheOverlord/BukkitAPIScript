package me.Logaaan.exprl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class EntityDamageByEntityEventHandler implements Listener {
	
	Main p;
	public List<String> instructions;
	
	public EntityDamageByEntityEventHandler(Main m) {
		p = m;
		instructions = new ArrayList<String>();
		p.getServer().getLogger().info("Loaded EntityDamageByEntityEvent");
	}
	
	public void addSet(String s) {
		this.instructions.add(s);
	}
	
	@EventHandler
	public void onChat(EntityDamageByEntityEvent e) {
		for (String i : instructions) {
			String u = i;
			p.nodep.proccessTask2(u, new Object[] { null, e });
		}
	}

}
