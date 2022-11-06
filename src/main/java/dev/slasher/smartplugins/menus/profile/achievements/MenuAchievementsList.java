package dev.slasher.smartplugins.menus.profile.achievements;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.libraries.menu.PagedPlayerMenu;
import dev.slasher.smartplugins.menus.profile.MenuAchievements;
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

import java.util.ArrayList;
import java.util.List;

public class MenuAchievementsList<T extends Achievement> extends PagedPlayerMenu {
  
  public MenuAchievementsList(Profile profile, String name, Class<T> achievementCLass) {
    super(profile.getPlayer(), "Quests - " + name, 6);
    this.previousPage = 18;
    this.nextPage = 26;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cBack"), 48);
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("BARRIER : 1 : name>&cClose"), 49);
    
    List<ItemStack> items = new ArrayList<>();
    List<T> achievements = Achievement.listAchievements(achievementCLass);
    for (T achievement : achievements) {
      items.add(achievement.getIcon(profile));
    }
    
    this.setItems(items);
    achievements.clear();
    items.clear();
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == this.previousPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openPrevious();
            } else if (evt.getSlot() == this.nextPage) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              this.openNext();
            } else if (evt.getSlot() == 49) {
              player.closeInventory();
            } else if (evt.getSlot() == 48) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievements(profile);
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
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
