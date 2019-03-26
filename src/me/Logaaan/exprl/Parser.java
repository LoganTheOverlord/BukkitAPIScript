package me.Logaaan.exprl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class Parser {
	
	public boolean ifs;
	public Map<String,Object> vars = new HashMap<String,Object>();

	Main p;
	public Parser(Main m) {
		this.p = m;
	}
	
	public void parse(String c, Object[] args) {
		if (c.contains("event.")) {
			Class cs = (Class) Player.class;
			Object pl = args[0];
			Object event = args[1];
		}
	}
}
