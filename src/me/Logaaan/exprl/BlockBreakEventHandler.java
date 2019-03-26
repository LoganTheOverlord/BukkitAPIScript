package me.Logaaan.exprl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
public class BlockBreakEventHandler implements Listener {
	
	Main p;
	public List<String> instructions;
	
	public BlockBreakEventHandler(Main m) {
		p = m;
		instructions = new ArrayList<String>();
		p.getServer().getLogger().info("Loaded BlockBreakEvent");
	}
	
	public void addSet(String s) {
		this.instructions.add(s);
	}
	
	@EventHandler
	public void onChat(BlockBreakEvent e) {
		for (String i : instructions) {
			String u = i;
			p.nodep.proccessTask2(u, new Object[] { e.getPlayer(), e });
		}
	}

}
