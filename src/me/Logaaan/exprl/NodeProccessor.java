package me.Logaaan.exprl;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.DocumentationTool.Location;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mysql.jdbc.StringUtils;

import org.bukkit.entity.LivingEntity;

public class NodeProccessor {
	
	private boolean ifs = false;
	private boolean ifs_t = true;
	
	private Main m;
	
	public NodeProccessor(Main m) {
		this.m = m;
	}
	
	public void proccessTaskWithArgs(String task, Object[] argz) {
		String[] l = task.split(" ");
		String node = "";
		String args = "";
		boolean to = false;
		String arg_to = "";
		int amount = 0;
		boolean started = false;
		String obj = "";
		for (String o : l) {
			o = o.replaceAll("\\s+","");
			if (node == "") {
				if (o.equals("damage")) {
					node = "send";
				}
				if (o.equals("heal")) {
					node = "kick";
				}
				if (o.equals("kill")) {
					node = "give";
				}
			} else {
				if (node == "kill") {
					if (o.equals("all")) {
						obj = "all";
					} else {
						obj = o;
					}
				}
				if (node == "heal") {
					if (o.equals("by")) {
						to = true;
					}
					if (!to) {
						if (o.equals("victim") || o.equals("attacker") || o.equals("event-entity")) {
							obj = o;
						}
					} else {
						if (!o.equals("by")) {
							amount = Integer.parseInt(o);
						}
					}
				}
				if (node == "damage") {
					if (o.equals("by")) {
						to = true;
					}
					if (!to) {
						if (o.equals("victim") || o.equals("attacker") || o.equals("event-entity")) {
							obj = o;
						}
					} else {
						if (!o.equals("by")) {
							amount = Integer.parseInt(o);
						}
					}
				}
			}
		}
		if (node.equals("kill")){
			if (obj != "") {
				if (obj == "attacker") {
					Entity a = (Entity) argz[0];
					a.remove();
				}
				if (obj == "victim") {
					Entity a1 = (Entity) argz[1];
					a1.remove();
				}
				if (obj == "event-entity") {
					Entity a1 = (Entity) argz[2];
					a1.remove();
				}

			}
		}

	}
	
	public void proccessTask2(String task, Object[] program_args) {
		if (task.contains("endif")) {
			ifs_t = true;
		}
		if (task.contains("if") && !task.contains("endif")) {
			ifs_t = false;
			String[] split = task.split(" ");
			Object ifa = null;
			String mode = null;
			Object ifss = null;
			for (String s : split) {
				m.getServer().getLogger().info(s);
				if (s.contains("player")) {
					Object player = program_args[0];
					String classname = "player";
					Class c = player.getClass();
					Object me = null;
					if (isMethod(s)) {
						me = getIsMethod(player, s);
						m.getServer().getLogger().info(me.toString());
					}
					if (me != null) {
						ifa = me;
					}
				}	else {
					if (ifa == null) {
						ifa = s;
					}
				}
				if (ifa != null) {
					if (mode == null) {
					if (s.contains("==")) {
						mode = "equal";
					}
					if (s.contains(">")) {
						mode = "bigger";
						
					}
					if (s.contains("<")) {
						mode = "lesser";
					}
					} else {
						if (s.contains("player")) {
							Object player = program_args[0];
							String classname = "player";
							Class c = player.getClass();
							Object me = null;
							if (isMethod(s)) {
								me = getIsMethod(player, s);
							}
							if (me != null) {
								ifss = me;
							}
						}	else {
							ifss = s;
						}
	

						break;
					}
				}
			}
			if (mode != null) {
				if (mode.equals("equal")) {
					ifs = true;
					m.getServer().getLogger().info(ifa + " "  + ifss);
					if (ifa.toString().equals(ifss.toString())) {
						ifs_t = true;
						
					} else {
						ifs_t = false;
					}
				}
				if (mode.equals("bigger")) {
					ifs = true;
					m.getServer().getLogger().info("Res: "  + ifa + " "  + ifss);
					if (Double.parseDouble(ifa.toString()) > Double.parseDouble(ifss.toString())) {
						ifs_t = true;
						
					} else {
						ifs_t = false;
					}
				}
				
				if (mode.equals("lesser")) {
					ifs = true;
					m.getServer().getLogger().info(ifa + " "  + ifss);
					if (Double.parseDouble(ifa.toString()) < Double.parseDouble(ifss.toString())) {
						ifs_t = true;
						
					} else {
						ifs_t = false;
					}
				}


			}
		}
		if (ifs_t == true) {
		if (task.contains("player")) {
			Object player = program_args[0];
			task = task.replace("player.", "");
			String classname = "player";
			Class c = player.getClass();
			for (Method d : c.getMethods()) {
				if (task.contains(d.getName())) {
					Class[] params = d.getParameterTypes();
					int params_count = d.getParameterCount();
					task = task.replace(d.getName()+"(", "");
					String[] fields = task.split(";");
					if (fields.length == params.length) {
					List<String> newclass = new ArrayList<String>();
					Map<String,List<String>> arguments = new HashMap<String,List<String>>();
					List<Object> arguments_final = new ArrayList<Object>();
					for (String f : fields) {
						if (f.contains("new")) {
							f = f.replace("new ", "");
							f = f.replaceAll("\\s+","");
							f = f.replace("(", "~");
							String[] classtodeclare = f.split("~");
							m.getServer().getLogger().info(classtodeclare[0]);
							newclass.add(classtodeclare[0]);
							String nw = classtodeclare[0];
							classtodeclare[1] = classtodeclare[1].replace(")", "");
							m.getServer().getLogger().info(classtodeclare[1]);
							List<String> ags = new ArrayList<String>();
							String[] args = classtodeclare[1].split(",");
							if (args.length > 0) {
								for (String ar : args) {
									ags.add(ar);
								}
							} else {
								String plll = classtodeclare[1].replace(")", "");
								ags.add(plll);
							}
							arguments.put(nw, ags);
						} else {
							arguments.put(f, Arrays.asList(f.replace(")","")));
						}
					}
					m.getServer().getLogger().info(newclass.size()+"");
					int index = 0;
					if (newclass.size() > 0) {
						for (String ff : newclass) {
							if (arguments.containsKey(ff)) {
								m.getServer().getLogger().info(ff);
								try {
									m.getServer().getLogger().info("Getting parameters..");
									m.getServer().getLogger().info(params[index].getName().replace("[L", "").replace(";", ""));
									Constructor[] cont = null;
									try {
										cont = Class.forName(params[index].getName().replace("[L", "").replace(";", "")).getConstructors();
									} catch (SecurityException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (ClassNotFoundException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									for (Constructor co : cont) {
										Parameter[] coneeds = co.getParameters();
										if (coneeds.length == arguments.get(ff).size()) {
										m.getServer().getLogger().info("Types: "+coneeds.length);
										List<Object> papa = new ArrayList<Object>();
										int indp = 0;
										for (String parse : arguments.get(ff)) {
											try {	
													Class cll = (Class) coneeds[indp].getType().getClass().getGenericSuperclass();
													Object param = cll.cast(parse);
													papa.add(param);
												} catch(ClassCastException e) {
													e.printStackTrace();
												}
											indp++;
												
										}
										if (co.getParameterCount() == 1) {
											m.getServer().getLogger().info("Param count: 1");
											try {
												Object arg1 = papa.get(0);
												if (isMethod(papa.get(0).toString())) {
													arg1 = getIsMethod(player, papa.get(0).toString());
												}
												Object parsed = co.newInstance(papa.get(0));
												arguments_final.add(parsed);
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										if (co.getParameterCount() == 2) {
											try {
												Object parsed = co.newInstance(papa.get(0), papa.get(1));
												arguments_final.add(parsed);
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										if (co.getParameterCount() == 3) {
											try {
												Object parsed = co.newInstance(papa.get(0), papa.get(1), papa.get(2));
												arguments_final.add(parsed);
											} catch (IllegalArgumentException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										if (co.getParameterCount() == 4) {
											try {
												Object parsed = co.newInstance(papa.get(0), papa.get(1), papa.get(2), papa.get(3));
												arguments_final.add(parsed);
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
								} catch (InstantiationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						index++;
					} else {
						for (String key : arguments.keySet()) {
							List<String> cr = arguments.get(key);
							arguments_final.add(cr.get(0));
						}
					}
					m.getServer().getLogger().info(d.getName());
					m.getServer().getLogger().info(arguments_final.size()+"");
					if (params.length == 1) {
						if (arguments_final.size() > 0) {
							try {
								Class arg1 = parseClass(params[0].getName());
								Object finall = arguments_final.get(0);
								finall = argumentParser(arg1, player, finall);
								m.getServer().getLogger().info(finall.getClass().getName() + " " + arg1.getName());
								((Player) player).getClass().getDeclaredMethod(d.getName(), arg1).invoke(player, finall);
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					
					if (params.length == 2) {
						if (arguments_final.size() > 0) {
							try {
								Class arg1 = int.class;
								try {
									arg1 = Class.forName(params[0].getName().replace("[L", "").replace("[B", "").replace(";", ""));
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Class arg2 = int.class;
								try {
									arg1 = Class.forName(params[1].getName().replace("[L", "").replace("[B", "").replace(";", ""));
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								((Player) player).getClass().getDeclaredMethod(d.getName(), arg1, arg2).invoke(player, arguments_final.get(0), arguments_final.get(1));

							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					if (params.length == 3) {
						try {
							d.invoke(arguments_final.get(0), arguments_final.get(1), arguments_final.get(2));
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
		}
		}
	} 
	
	private Class parseClass(String name) {
		Class c = String.class;
		if (name.contains("String")) {
			c = String.class;
		}
		if (name.contains("float")) {
			c = float.class;
		}
		if (name.contains("int")) {
			c = int.class;
		}
		if (name.contains("double")) {
			c = double.class;
		}
		if (name.contains("long")) {
			c = long.class;
		}
		if (name.contains("short")) {
			c = short.class;
		}
		// TODO Auto-generated method stub
		return c;
	}

	public Object argumentParser(Class arg1, Object arg2, Object finall) {
		Object f = finall;
		if (f.toString().contains("+")) {
			if (arg1.equals(String.class)) {
				f = f.toString().replaceAll("\\+", " +");
				String[] split = f.toString().split(" +");
				String final_s = "";
				for (String s : split) {
					Object mr = s;
					if (isMethod(s)) {
						mr = getIsMethod(arg2, s);
					}
					final_s = final_s + " " + mr.toString();
				}
				f = final_s;
			} else {
				if (arg1.equals(double.class)) {
					f = f.toString().replaceAll("\\+", " +");
					String[] split = f.toString().split("\\+");
					double curr = 0;
					for (String b : split) {
						Object s = b;
						if (isMethod(b)) {
							s = getIsMethod(arg2, b);
						}
						b = s.toString();
						if (b.contains("-")) {
							String[] further = b.split("\\-");
							for (String ff : further) {
								Object ss = ff;
								if (isMethod(ff)) {
									ss = getIsMethod(arg2, ff);
								}
								ff = ss.toString();
								double fr = Double.parseDouble(ff);
								curr -= fr;
							}
						} else {
							double fr = Double.parseDouble(b);
							curr += fr;
						}
					}
					f = curr;
				}

				if (arg1.equals(int.class)) {
					f = f.toString().replaceAll("\\+", " +");
					String[] split = f.toString().split("\\+");
					int curr = 0;
					for (String b : split) {
						Object s = b;
						if (isMethod(b)) {
							s = getIsMethod(arg2, b);
						}
						b = s.toString();
						if (b.contains("-")) {
							
							String[] further = b.split("\\-");
							for (String ff : further) {
								Object ss = ff;
								if (isMethod(ff)) {
									ss = getIsMethod(arg2, ff);
								}
								ff = ss.toString();

								int fr = Integer.parseInt(ff);
								curr -= fr;
							}
						} else {
							int fr = Integer.parseInt(b);
							curr += fr;
						}
					}
					f = curr;
				}
				if (arg1.equals(float.class)) {
					f = f.toString().replaceAll("\\+", " +");
					String[] split = f.toString().split("\\+");
					Float curr = 0F;
					for (String b : split) {
						Object s = b;
						if (isMethod(b)) {
							s = getIsMethod(arg2, b);
						}
						b = s.toString();
						if (b.contains("-")) {
							String[] further = b.split("\\-");
							for (String ff : further) {
								Object ss = ff;
								if (isMethod(ff)) {
									ss = getIsMethod(arg2, ff);
								}
								ff = ss.toString();

								float fr = Float.parseFloat(ff);
								curr -= fr;
							}
						} else {
							float fr = Float.parseFloat(b);
							curr += fr;
						}
					}
					f = curr;
				}
			}
		} else {
			if (isMethod(f.toString())) {
				f = getIsMethod(arg2, f.toString());
				String ff = f.toString();
				ff = ff.replaceAll("\\s+", "");
				if (arg1.equals(String.class)) {
					f = ff.toString();
				}
				if (arg1.equals(int.class)) {
					f = Integer.parseInt(ff.toString());
				}
				if (arg1.equals(float.class)) {
					f = Float.parseFloat(ff.toString());
				}
				if (arg1.equals(double.class)) {
					f = Double.parseDouble(ff.toString());
				}
			} else {
				String ff = f.toString();
				ff = ff.replaceAll("\\s+", "");
				if (arg1.equals(String.class)) {
					f = ff.toString();
				}
				if (arg1.equals(int.class)) {
					f = Integer.parseInt(ff.toString());
				}
				if (arg1.equals(float.class)) {
					f = Float.parseFloat(ff.toString());
				}
				if (arg1.equals(double.class)) {
					f = Double.parseDouble(ff.toString());
				}
			}
		}
		return f;
	}
	
	public boolean isMethod(String c) {
		Class player = Player.class;
		if (c.contains("player.")) {
			c = c.replace("player.", "");
			for (Method d: player.getMethods()) {
				if (c.contains(d.getName())) {
					return true;
				}
			}
		}
		if (c.contains("Bukkit.")) {
			c = c.replace("Bukkit.", "");
			for (Method d: Bukkit.class.getMethods()) {
				if (c.contains(d.getName())) {
					return true;
				}
			}
		}

		return false;
	}
	
	public Object getIsMethod(Object original, String c) {
		Object toReturn = null;
		Class player = Player.class;
		if (c.contains("player.")) {
			c = c.replace("player.", "");
			for (Method d: player.getMethods()) {
				if (c.contains(d.getName())) {
					if (!c.contains(".")) {
						if (d.getTypeParameters().length < 1) {
							try {
								toReturn = player.getMethod(d.getName()).invoke((Player) original);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							if (c.contains("(")) {
								
							} else {
								return null;
							}
						}
					}
					
				} else {
					if (d.getTypeParameters().length < 1) {
						try {
							toReturn = player.getMethod(d.getName()).invoke((Player) original);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						if (c.contains("(")) {
							
						} else {
							return null;
						}
					}

				}
			}

		}
		if (c.contains("Bukkit.")) {
			c = c.replace("Bukkit.", "");
			for (Method d: Bukkit.class.getMethods()) {
				if (c.contains(d.getName())) {
						if (d.getTypeParameters().length < 1) {
							try {
								toReturn = Bukkit.class.getMethod(d.getName()).invoke((Player) original);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							if (c.contains("(")) {
								
							} else {
								return null;
							}
						}
					
				}
			}

		}

		return toReturn;
		
	}
	
	public void proccessTask(String task) {
		String[] l = task.split(" ");
		String node = "";
		String args = "";
		boolean to = false;
		String arg_to = "";
		boolean started = false;
		String obj = "";
		int amount= 0;
		for (String o : l) {
			o = o.replaceAll("\\s+","");
			if (node.equals("")) {
				if (o.equals("send")) {
					node = "send";
				}
				if (o.equals("kick")) {
					node = "kick";
				}
				if (o.equals("give")) {
					m.getServer().getLogger().info("give2");
					node = "give";
				}
				if (o.equals("kill")) {
					node = "kill";
				}
				if (o.equals("heal")) {
					node = "heal";
				}
				if (o.equals("damage")) {
					node = "damage";
				}
			} else {
				if (node == "heal") {
					if (o.equals("by")) {
						to = true;
					}
					if (!to) {
						if (o.equals("all")) {
							
							obj = "all";
						} else {
							obj = o;
						}
					} else {
						if (!o.equals("by")) {
							amount = Integer.parseInt(o);
						}
					}
				}
				if (node == "damage") {
					if (o.equals("by")) {
						to = true;
					}
					if (!to) {
						if (o.equals("all")) {
							
							obj = "all";
						} else {
							obj = o;
						}
					} else {
						if (!o.equals("by")) {
							amount = Integer.parseInt(o);
						}
					}
				}

				if (node == "kick") {
					if (o.equals("all")) {
						obj = "all";
					} else {
						obj = o;
					}
				}
				if (node == "kill") {
					if (o.equals("all")) {
						obj = "all";
					} else {
						obj = o;
					}
				}
				if (node.equals("give")) {
					if (o.equals("to")) {
						to = true;
					}
					if (!to) {
						if (o.equals("named")){
							args = args + " ~n";
						} else {
							args = args + " " + o;
						}
					} else {
						if (!o.equals("to")) {
							if (o.equals("all")) {
								obj = "all";
							} else {
								obj = o;
							}
						}
					}
				}
				if (node.equals("send")) {
					if (o.contains("message")) {
						started = !started;
					}
					if (started) {
						if (!o.equals("to") && !o.equals("message")) {
							args = args + " " + o;
						}
						if (o.contains("to")) {
							to = true;
							started = false;
						}

					} else {
						if (to) {
							if (!o.equals("to")) {
								if (o.equals("all")) {
									obj = "all";
								} else {
									obj = o;
								}
							}
						}
					}
				}
			}
		}
		if (node.equals("send")){
			if (!obj.equals("")) {
				if (obj == "all") {
					for (Player p : m.getServer().getOnlinePlayers()) {
							p.sendMessage(color(args));
						}
					
				} else {
					((Player) Bukkit.getPlayer(obj)).sendMessage(color(args));
				}
			}
		}
		if (node.equals("kill")){
			if (!obj.equals("")) {
				if (obj == "all") {
						for (Player p : m.getServer().getOnlinePlayers()) {
							((LivingEntity) p).damage(9999);
						}
					
				} else {
					((LivingEntity) Bukkit.getPlayer(obj)).damage(9999);
				}
			}
		}

		if (node.equals("give")) {
			m.getServer().getLogger().info("Give " + args);
			String[] ll = args.split(" ");
			Material _m = Material.AIR;
			int c = 0;
			String n = "";
			boolean name = false;
			int ind = 0;
			for (String ss : ll) {
				ss = ss.replaceAll("\\s+","");
				if (ind == 2) {
					_m = Material.valueOf(ss.toUpperCase());
				}
				if (ind == 1) {
					c = Integer.parseInt(ss);
				}
				if (ss.contains("~n")) {
					ss = ss.replace("~n", "");
					name = true;
				}
				if (name) {
					if (n == "") {
						n = ss;
					} else {
						n = n + " " +ss; 
					}
				}
				ind++;
			}
			n = color(n);
			if (obj == "all") {
				for (Player r : m.getServer().getOnlinePlayers()) {
					ItemStack item = new ItemStack(_m,c);
					ItemMeta me = item.getItemMeta();
					me.setDisplayName(n);
					item.setItemMeta(me);
					r.getInventory().addItem(item);
				}
			} else {
				Player r = Bukkit.getPlayer(obj);
				ItemStack item = new ItemStack(_m,c);
				ItemMeta me = item.getItemMeta();
				me.setDisplayName(n);
				item.setItemMeta(me);
				r.getInventory().addItem(item);
			}
		}
	}

	public String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}


}