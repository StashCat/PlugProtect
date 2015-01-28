package me.stashcat.PlugProtect;

import org.bukkit.Bukkit;
import org.bukkit.Location;


public class Areas extends Main {
	Main pl;
	
	public Areas(Main Main){
		pl = Main;
	}
	
	public boolean exists(String area){
		if (area == null)
			return false;
		return getCConfig().getString(area) != null;
	}
	
	public String getWelcomeMessage(String area, String pname){
		String msg = getCConfig().getString(area + ".welcome");
		if (msg == null)
			msg = "Welcome to %area of %owner";
		return msg.replace("%area", "&a" + area + "&r").replace("%owner", "&a" + pname + "&r");
	}
	
	public String getFarewellMessage(String area, String pname){
		String msg = getCConfig().getString(area + ".farewell");
		if (msg == null)
			msg = "You have left %area of %owner";
		return msg.replace("%area", "&a" + area + "&r").replace("%owner", "&a" + pname + "&r");
	}
	
	public int getSize(String area){
		String[] pos1 = getCConfig().getString(area + ".pos1").split(",");
		String[] pos2 = getCConfig().getString(area + ".pos2").split(",");
		double x1 = 0, z1 = 0, x2 = 0, z2 = 0;
		try {
			x1 = Double.parseDouble(pos1[0]);
			z1 = Double.parseDouble(pos1[1]);
			x2 = Double.parseDouble(pos2[0]);
			z2 = Double.parseDouble(pos2[1]);
		} catch (NumberFormatException e){
			return -1;
		}
		double x = Math.max(x1, x2) - Math.min(x1, x2);
		double z = Math.max(z1, z2) - Math.min(z1, z2);
		int size = (int) Math.round(x * z);
		return size;
	}
	
	public double[] getMiddle(String area){
		double[] mid = {-1, -1};
		String[] pos1 = getCConfig().getString(area + ".pos1").split(",");
		String[] pos2 = getCConfig().getString(area + ".pos2").split(",");
		double x1 = 0, z1 = 0, x2 = 0, z2 = 0;
		try {
			x1 = Double.parseDouble(pos1[0]);
			z1 = Double.parseDouble(pos1[1]);
			x2 = Double.parseDouble(pos2[0]);
			z2 = Double.parseDouble(pos2[1]);
		} catch (NumberFormatException e){
			return mid;
		}
		double x = (Math.max(x1, x2) + Math.min(x1, x2)) / 2;
		double z = (Math.max(z1, z2) + Math.min(z1, z2)) / 2;
		mid[0] = x + .5;
		mid[1] = z + .5;
		return mid;
	}
	
	public double[] getWarp(String area){
		double[] warp = {-1, -1};
		if (getCConfig().getString(area + ".warp") != null){
			String[] warpget = getCConfig().getString(area + ".warp").split(",");
			try {
				warp[0] = Double.parseDouble(warpget[0]);
				warp[1] = Double.parseDouble(warpget[1]);
			} catch (NumberFormatException e){
				return warp;
			}
		} else {
			double[] mid = getMiddle(area);
			return mid;
		}
		return warp;
	}
	
	public int getNewSize(int x1, int z1, int x2, int z2){
		double x = Math.max(x1, x2) - Math.min(x1, x2);
		double z = Math.max(z1, z2) - Math.min(z1, z2);
		int size = (int) Math.round(x * z);
		return size;
	}
	
	public boolean isProtected(Location loc){
		for (String key : getCConfig().getConfigurationSection("").getKeys(false)){
			String[] pos1 = getCConfig().getString(key + ".pos1").split(",");
			String[] pos2 = getCConfig().getString(key + ".pos2").split(",");
			double x1 = Double.parseDouble(pos1[0]);
			double z1 = Double.parseDouble(pos1[1]);
			double x2 = Double.parseDouble(pos2[0]);
			double z2 = Double.parseDouble(pos2[1]);
			double xp = loc.getX();
			double zp = loc.getZ();
			if (xp >= Math.min(x1, x2) && xp <= (Math.max(x1, x2) + 1) && zp >= Math.min(z1, z2) && zp <= (Math.max(z1, z2) + 1)){
				return true;
			}
		}
		return false;
	}
	
	public String getArea(Location loc){
		for (String key : getCConfig().getConfigurationSection("").getKeys(false)){
			String[] pos1 = getCConfig().getString(key + ".pos1").split(",");
			String[] pos2 = getCConfig().getString(key + ".pos2").split(",");
			double x1 = Double.parseDouble(pos1[0]);
			double z1 = Double.parseDouble(pos1[1]);
			double x2 = Double.parseDouble(pos2[0]);
			double z2 = Double.parseDouble(pos2[1]);
			double xp = loc.getX();
			double zp = loc.getZ();
			if (xp >= Math.min(x1, x2) && xp <= (Math.max(x1, x2) + 1) && zp >= Math.min(z1, z2) && zp <= (Math.max(z1, z2) + 1)){
				return key;
			}
		}
		return null;
	}
	
	public boolean isOwner(String area, String player){
		if (getCConfig().getString(area + ".owner").equalsIgnoreCase(player))
			return true;
		return false;
	}
	
	public String getOwner(String area){
		return getCConfig().getString(area + ".owner");
	}
	
	public Location getPosLocs(String area, int pos){
		double[] posd = {-1, -1};
		String[] str = getCConfig().getString(area + ".pos" + pos).split(",");
		posd[0] = Double.parseDouble(str[0]);
		posd[1] = Double.parseDouble(str[1]);
		Location loc = new Location(Bukkit.getWorld(getCConfig().getString(area + ".world")), posd[0], 0, posd[1]);
		loc.setY(loc.getWorld().getHighestBlockYAt(loc));
		return loc;
	}
	
	public Location[] getPosLocs(String area){
		Location[] locs = {getPosLocs(area, 1), getPosLocs(area, 2)};
		return locs;
	}
	
	public boolean canBuild(String area, String pname){
		if (getCConfig().getString(area + ".owner").equals(pname) || getCConfig().getList(area + ".whitelist").contains(pname))
			return true;
		return false;
	}
	
	public boolean isRestricted(String area){
		if (getCConfig().getBoolean(area + ".restricted"))
			return true;
		return false;
	}
}
