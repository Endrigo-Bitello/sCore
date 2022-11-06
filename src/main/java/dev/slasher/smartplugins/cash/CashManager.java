package dev.slasher.smartplugins.cash;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.plugin.config.HyConfig;
import org.bukkit.entity.Player;

public class CashManager {
  
  public static final boolean CASH;
  private static final HyConfig CONFIG;
  
  static {
    CONFIG = Core.getInstance().getConfig("utils");
    if (!CONFIG.contains("cash")) {
      CONFIG.set("cash", true);
    }
    
    CASH = CONFIG.getBoolean("cash");
  }
  
  public static void addCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("User needs to be logged in to change cash.");
    }
    
    profile.setStats("HyCoreProfile", profile.getStats("HyCoreProfile", "cash") + amount, "cash");
  }
  
  public static void addCash(Player player, long amount) throws CashException {
    addCash(player.getName(), amount);
  }
  
  public static void addCash(String name, long amount) throws CashException {
    addCash(Profile.getProfile(name), amount);
  }
  
  public static void removeCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("User needs to be logged in to change cash.");
    }
    
    profile.setStats("HyCoreProfile", profile.getStats("HyCoreProfile", "cash") - amount, "cash");
  }
  
  public static void removeCash(Player player, long amount) throws CashException {
    removeCash(player.getName(), amount);
  }
  
  public static void removeCash(String name, long amount) throws CashException {
    removeCash(Profile.getProfile(name), amount);
  }
  
  public static void setCash(Profile profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("User needs to be logged in to change cash.");
    }
    
    profile.setStats("HyCoreProfile", amount, "cash");
  }
  
  public static void setCash(Player player, long amount) throws CashException {
    setCash(player.getName(), amount);
  }
  
  public static void setCash(String name, long amount) throws CashException {
    setCash(Profile.getProfile(name), amount);
  }
  
  public static long getCash(Profile profile) {
    long cash = 0L;
    if (profile != null) {
      cash = profile.getStats("HyCoreProfile", "cash");
    }
    
    return cash;
  }
  
  public static long getCash(Player player) {
    return getCash(player.getName());
  }
  
  public static long getCash(String name) {
    return getCash(Profile.getProfile(name));
  }
}
