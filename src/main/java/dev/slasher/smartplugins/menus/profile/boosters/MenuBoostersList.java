package dev.slasher.smartplugins.menus.profile.boosters;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.booster.Booster;
import dev.slasher.smartplugins.libraries.menu.PagedPlayerMenu;
import dev.slasher.smartplugins.menus.profile.MenuBoosters;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import dev.slasher.smartplugins.utils.TimeUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MenuBoostersList<T extends Achievement> extends PagedPlayerMenu {
  
  private Booster.BoosterType type;
  private Map<ItemStack, Booster> boosters = new HashMap<>();
  public MenuBoostersList(Profile profile, String name, Booster.BoosterType type) {
    super(profile.getPlayer(), "Boosters " + name, 5);
    this.type = type;
    this.previousPage = 36;
    this.nextPage = 44;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25);
    
    this.removeSlotsWith(BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : name>&cBack"), 40);
    
    List<ItemStack> items = new ArrayList<>();
    List<Booster> boosters = profile.getBoostersContainer().getBoosters(type);
    for (Booster booster : boosters) {
      ItemStack icon = BukkitUtils.deserializeItemStack(
          "POTION" + (type == Booster.BoosterType.NETWORK ? ":8232" : "") + " : 1 : hide>all : name>&aBoosters " + (type == Booster.BoosterType.NETWORK ?
              "Network" :
              "Private") + " : desc>&fMultiplier: &6" + booster.getMultiplier() + "x\n&fDuration: &7" + TimeUtils
              .getTime(TimeUnit.HOURS.toMillis(booster.getHours())) + "\n \n&eClick to activate the booster!");
      items.add(icon);
      this.boosters.put(icon, booster);
    }
    
    if (items.size() == 0) {
      this.removeSlotsWith(BukkitUtils.deserializeItemStack("WEB : 1 : name>&cYou don't have any multiplier!"), 22);
    }
    this.setItems(items);
    boosters.clear();
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
            } else if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuBoosters(profile);
            } else {
              Booster booster = this.boosters.get(item);
              if (booster != null) {
                if (type == Booster.BoosterType.NETWORK) {
                  if (!Core.minigames.contains(Core.minigame)) {
                    this.player.sendMessage("§cYou must be on a Minigame server to activate this Multiplier.");
                    return;
                  }
                  
                  if (!Booster.setNetworkBooster(Core.minigame, profile, booster)) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    this.player.sendMessage("§cThere is already an active General Multiplier in " + Core.minigame + ".");
                    this.player.closeInventory();
                    return;
                  }
                  
                  EnumSound.LEVEL_UP.play(this.player, 0.5F, 1.0F);
                  Profile.listProfiles().forEach(pf -> pf.getPlayer().sendMessage(" \n " + StringUtils.getLastColor(Role.getPlayerRole(this.player).getPrefix()) + this.player
                      .getName() + " §7activated a §6General Booster§7.\n §bALL §7players received a §b" + booster
                      .getMultiplier() + "x §7bonus §7on §8" + Core.minigame + " §7matches.\n "));
                  this.player.closeInventory();
                } else {
                  if (!profile.getBoostersContainer().enable(booster)) {
                    EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
                    this.player.sendMessage("§cYou already have an active Personal Multiplier.");
                    this.player.closeInventory();
                    return;
                  }
                  
                  this.player.sendMessage(
                      "§aYou have activated §6Coin Multiplier " + booster.getMultiplier() + "x §8(" + TimeUtils.getTime(TimeUnit.HOURS.toMillis(booster.getHours())) + ")§a.");
                  new MenuBoosters(profile);
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    this.type = null;
    this.boosters.values().forEach(Booster::gc);
    this.boosters.clear();
    this.boosters = null;
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
