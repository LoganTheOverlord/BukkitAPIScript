package me.Logaaan.exprl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	String[][] cev_lines;
	List<Listener> handlers = new ArrayList<Listener>();
	public GeneralEffects effects = new GeneralEffects(this);
	public NodeProccessor nodep = new NodeProccessor(this);
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		readScripts();
	}
	
	public void onDisable() {
		
		
	}
	
	void readScripts(){
		File f = new File(this.getDataFolder() + "/default.script");
		List<String> lines = new ArrayList<String>();
		try {
			Scanner s = new Scanner(f);
			int index = 0;
			while (s.hasNext()) {
				lines.add(s.nextLine());
				index++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getServer().getLogger().info("Loaded "+lines.size()+" lines");
		String cev = "";
		Listener l = null;
		for (String s : lines) {
			this.getServer().getLogger().info(s);
			if (s.equalsIgnoreCase("onPlayerChat") || s.equalsIgnoreCase("onPlayerLevelChange")) {
				if (cev != s) {
					if (s.equalsIgnoreCase("onPlayerChat")) {
						cev = s;
						l = (Listener) new PlayerChatEventHandler(this);
						handlers.add(l);
					}
					if (s.equalsIgnoreCase("onPlayerLevelChange")) {
						cev = s;
						l = (Listener) new PlayerChatEventHandler(this);
						handlers.add(l);
					}
				} else {
					this.getServer().getLogger().info("Found duplicit event! Ignoring...");
				}
			}
			if (cev != "") {
				for (Method m : l.getClass().getDeclaredMethods()) {
					if (m.getName().equalsIgnoreCase("addSet")) {
						m.setAccessible(true);
						try {
							m.invoke(l,s);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		this.getServer().getLogger().info("Loaded "+handlers.size() + " handlers!");
		for (Listener ls : handlers) {
			this.getServer().getPluginManager().registerEvents(ls, this);
		}
	}

}
