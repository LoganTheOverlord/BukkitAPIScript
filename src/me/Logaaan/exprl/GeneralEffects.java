package me.Logaaan.exprl;

import java.net.InetSocketAddress;

import org.bukkit.Bukkit;

public class GeneralEffects {
	
	Main p;
	
	public GeneralEffects(Main m) {
		p = m;
	}
	public void broadcast(String m) {
		p.getServer().broadcastMessage(m);
	}
	
	public void kick(String m) {
		Bukkit.getPlayer(m).kickPlayer("");
	}
	
	public String getIP(String m) {
		return Bukkit.getPlayer(m).getAddress().getHostName();
	}

}
