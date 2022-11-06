package dev.slasher.smartplugins.menus.profile;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.database.data.container.PreferencesContainer;
import dev.slasher.smartplugins.libraries.menu.PlayerMenu;
import dev.slasher.smartplugins.menus.MenuProfile;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.enums.BloodAndGore;
import dev.slasher.smartplugins.player.enums.PlayerVisibility;
import dev.slasher.smartplugins.player.enums.PrivateMessages;
import dev.slasher.smartplugins.player.enums.ProtectionLobby;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPreferences extends PlayerMenu {
  
  public MenuPreferences(Profile profile) {
    super(profile.getPlayer(), "Preferences", 5);
    
    PreferencesContainer pc = profile.getPreferencesContainer();
    
    PlayerVisibility pv = pc.getPlayerVisibility();
    this.setItem(11, BukkitUtils.deserializeItemStack("347 : 1 : name>&aPlayers : desc>&7Enable or disable\n&7players in the lobby."));
    this.setItem(20, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + pv.getInkSack() + " : 1 : name>" + pv.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pv.getName()) + "\n \n&eClick to modify!"));
    
    PrivateMessages pm = pc.getPrivateMessages();
    this.setItem(12, BukkitUtils.deserializeItemStack("PAPER : 1 : name>&aPrivate Messages : desc>&7Enable or disable\n&7your private messages."));
    this.setItem(21, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + pm.getInkSack() + " : 1 : name>" + pm.getName() + " : desc>&fState: &7" + StringUtils.stripColors(pm.getName()) + "\n \n&eClick to modify!"));
    
    BloodAndGore bg = pc.getBloodAndGore();
    this.setItem(14, BukkitUtils.deserializeItemStack("REDSTONE : 1 : name>&aViolence : desc>&7Ative ou desative as partículas\n&7de sangue no PvP."));
    this.setItem(23, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + bg.getInkSack() + " : 1 : name>" + bg.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(bg.getName()) + "\n \n&eClick to modify!"));
    
    ProtectionLobby pl = pc.getProtectionLobby();
    this.setItem(15, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : name>&aConfirm in /lobby : desc>&7Activate or deactivate the\n&7confirmation request when using /lobby."));
    this.setItem(24, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + pl.getInkSack() + " : 1 : name>" + pl.getName() + " : desc>&fState: &7" + StringUtils.stripColors(pl.getName()) + "\n \n&eClick to modify!"));
    
    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : name>&cBack"));
    
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
            if (evt.getSlot() == 10 || evt.getSlot() == 11 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 15 || evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 20) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePlayerVisibility();
              if (!profile.playingGame()) {
                profile.refreshPlayers();
              }
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 21) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePrivateMessages();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 23) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeBloodAndGore();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 24) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeProtectionLobby();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 40) {
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
