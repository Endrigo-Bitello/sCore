package dev.slasher.smartplugins.menus.profile;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.booster.Booster;
import dev.slasher.smartplugins.booster.NetworkBooster;
import dev.slasher.smartplugins.libraries.menu.PlayerMenu;
import dev.slasher.smartplugins.menus.MenuProfile;
import dev.slasher.smartplugins.menus.profile.boosters.MenuBoostersList;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.TimeUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuBoosters extends PlayerMenu {
  
  public MenuBoosters(Profile profile) {
    super(profile.getPlayer(), "Boosters", 4);
    
    this.setItem(12, BukkitUtils.deserializeItemStack(
        "POTION : 1 : name>&aPersonal Multipliers : desc>&7Grants &6Coin Multiplier &7just\n&7for &bYOU &7in all minigames\n&7on the server for a short period of time.\n \n&eClick to view!"));
    this.setItem(14, BukkitUtils.deserializeItemStack(
        "POTION:8232 : 1 : hide>all : name>&aNetwork Multipliers : desc>&7Grants &6Coin Multiplier &7to\n&bALL &7players in just one minigame\n&7for a short period.\n \n&eClick to view!"));
    
    String booster = profile.getBoostersContainer().getEnabled();
    StringBuilder result = new StringBuilder(), network = new StringBuilder();
    for (int index = 0; index < Core.minigames.size(); index++) {
      String minigame = Core.minigames.get(index);
      NetworkBooster nb = Booster.getNetworkBooster(minigame);
      network.append(" &8• &b").append(minigame).append(": ")
          .append(nb == null ? "&cDisabled" : "&6" + nb.getMultiplier() + "x &7by " + Role.getColored(nb.getBooster()) + " &8(" + TimeUtils.getTimeUntil(nb.getExpires()) + ")")
          .append(index + 1 == Core.minigames.size() ? "" : "\n");
    }
    result.append("&fActive Personal Multiplier:\n ");
    if (booster == null) {
      result.append("&cYou do not have any active multiplier.");
    } else {
      String[] splitted = booster.split(":");
      double all = 50.0 * Double.parseDouble(splitted[0]);
      result.append("&8• &6Multiplier ").append(splitted[0]).append("x &8(").append(TimeUtils.getTimeUntil(Long.parseLong(splitted[1])))
          .append(")\n \n&fCalculation:\n &7With the multiplier active when receiving &650 Coins &7the\n &7total received will be equivalent to &6").append((int) all).append(" Coins&7.");
    }
    this.setItem(30, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : name>&cBack"));
    this.setItem(31, BukkitUtils.deserializeItemStack(
        "PAPER : 1 : name>&aCredit Multiplier : desc>&7Multipliers are cumulative. The more active\n&7multipliers, the greater the bonus received.\n \n&fNetwork Multipliers:\n" + network + "\n \n" + result));
    
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
            if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, "Private", Booster.BoosterType.PRIVATE);
            } else if (evt.getSlot() == 14) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, "Network", Booster.BoosterType.NETWORK);
            } else if (evt.getSlot() == 30) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            } else if (evt.getSlot() == 31) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
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
