package dev.slasher.smartplugins.menus.profile;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.achievements.types.BedWarsAchievement;
import dev.slasher.smartplugins.achievements.types.MurderAchievement;
import dev.slasher.smartplugins.achievements.types.SkyWarsAchievement;
import dev.slasher.smartplugins.achievements.types.TheBridgeAchievement;
import dev.slasher.smartplugins.libraries.menu.PlayerMenu;
import dev.slasher.smartplugins.menus.MenuProfile;
import dev.slasher.smartplugins.menus.profile.achievements.MenuAchievementsList;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuAchievements extends PlayerMenu {
  
  public MenuAchievements(Profile profile) {
    super(profile.getPlayer(), "Quests", 4);
    
    List<SkyWarsAchievement> skywars = Achievement.listAchievements(SkyWarsAchievement.class);
    long max = skywars.size();
    long completed = skywars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    skywars.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack("EYE_OF_ENDER : 1 : name>&aSkyWars : desc>&fChallanges: " + color + completed + "/" + max + "\n \n&eClick to view!"));

    List<TheBridgeAchievement> thebridge = Achievement.listAchievements(TheBridgeAchievement.class);
    max = thebridge.size();
    completed = thebridge.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    thebridge.clear();
    this.setItem(12,
        BukkitUtils.deserializeItemStack("STAINED_CLAY:11 : 1 : name>&aTheBridge : desc>&fChallanges: " + color + completed + "/" + max + "\n \n&eClick to view!"));
    
    List<MurderAchievement> murder = Achievement.listAchievements(MurderAchievement.class);
    max = murder.size();
    completed = murder.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    murder.clear();
    this.setItem(14, BukkitUtils.deserializeItemStack("IRON_SWORD : 1 : name>&aMurder Mystery : desc>&fChallanges: " + color + completed + "/" + max + "\n \n&eClick to view!"));
    
    List<BedWarsAchievement> bedwars = Achievement.listAchievements(BedWarsAchievement.class);
    max = bedwars.size();
    completed = bedwars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    bedwars.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack("BED : 1 : name>&aBedWars : desc>&fChallanges: " + color + completed + "/" + max + "\n \n&eClick to view!"));
    
    
    this.setItem(31, BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cBack"));
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Sky Wars", SkyWarsAchievement.class);
            } else if (evt.getSlot() == 12) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "The Bridge", TheBridgeAchievement.class);
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Murder", MurderAchievement.class);
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Bed Wars", BedWarsAchievement.class);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
