package dev.slasher.smartplugins.menus.profile;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.libraries.menu.PlayerMenu;
import dev.slasher.smartplugins.menus.MenuProfile;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuStatistics extends PlayerMenu {
  
  public MenuStatistics(Profile profile) {
    super(profile.getPlayer(), "Statistics", 5);
    
    this.setItem(10, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "EYE_OF_ENDER : 1 : name>&aSky Wars : desc>&eSolo:\n &8▪ &fKills: &7%sCore_SkyWars_1v1kills%\n &8▪ &fDeaths: &7%sCore_SkyWars_1v1deaths%\n &8▪ &fWins: &7%sCore_SkyWars_1v1wins%\n &8▪ &fGames: &7%sCore_SkyWars_1v1games%\n &8▪ &fAssistências: &7%sCore_SkyWars_1v1assists%\n " + /*"\n&eDupla:\n &8▪ &fKills: &7%sCore_SkyWars_2v2kills%\n &8▪ &fDeaths: &7%sCore_SkyWars_2v2deaths%\n &8▪ &fWins: &7%sCore_SkyWars_2v2wins%\n &8▪ &fGames: &7%sCore_SkyWars_2v2games%\n &8▪ &fAssistências: &7%sCore_SkyWars_2v2assists%\n*/ "\n&eRanked:\n &8▪ &fKills: &7%sCore_SkyWars_rankedkills%\n &8▪ &fDeaths: &7%sCore_SkyWars_rankeddeaths%\n &8▪ &fWins: &7%sCore_SkyWars_rankedwins%\n &8▪ &fGames: &7%sCore_SkyWars_rankedgames%\n &8▪ &fPoints: &7%sCore_SkyWars_rankedpoints%\n \n&fCoins: &6%sCore_SkyWars_coins%")));
    
    this.setItem(12, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "BED : 1 : name>&aBed Wars : desc>&eGeneral:\n &8▪ &fGames: &7%sCore_BedWars_games%\n &8▪ &fKills: &7%sCore_BedWars_kills%\n &8▪ &fDeaths: &7%sCore_BedWars_deaths%\n &8▪ &fKills Finais: &7%sCore_BedWars_finalkills%\n &8▪ &fDeaths Finais: &7%sCore_BedWars_finaldeaths%\n &8▪ &fWins: &7%sCore_BedWars_wins%\n &8▪ &fCamas destruídas: &7%sCore_BedWars_bedsdestroyeds%\n &8▪ &fCamas perdidas: &7%sCore_BedWars_bedslosteds%\n \n&fCoins: &6%sCore_BedWars_coins%")));
    
    this.setItem(14, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "STAINED_CLAY:11 : 1 : name>&aThe Bridge : desc>&e1v1:\n &8▪ &fKills: &7%sCore_TheBridge_1v1kills%\n &8▪ &fDeaths: &7%sCore_TheBridge_1v1deaths%\n &8▪ &fPoints: &7%sCore_TheBridge_1v1points%\n &8▪ &fWins: &7%sCore_TheBridge_1v1wins%\n &8▪ &fGames: &7%sCore_TheBridge_1v1games%\n " + "\n&e2v2:\n &8▪ &fKills: &7%sCore_TheBridge_2v2kills%\n &8▪ &fDeaths: &7%sCore_TheBridge_2v2deaths%\n &8▪ &fPoints: &7%sCore_TheBridge_2v2points%\n &8▪ &fWins: &7%sCore_TheBridge_2v2wins%\n &8▪ &fGames: &7%sCore_TheBridge_2v2games%\n \n&eWinstreak:\n &8▪ &fDiário: &7%sCore_TheBridge_winstreak%\n \n&fCoins: &6%sCore_TheBridge_coins%")));
    
    this.setItem(16, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "BOW : 1 : name>&aMurder : desc>&eClassic: \n &8▪ &fKills: &7%sCore_Murder_classic_kills%\n &8▪ &fWins: &7%sCore_Murder_classic_wins%\n \n&eAssassins: \n &8▪ &fKills: &7%sCore_Murder_assassins_kills%\n &8▪ &fWins: &7%sCore_Murder_assassins_wins%\n \n&fCoins: &6%sCore_Murder_coins%")));
  
    this.setItem(22, BukkitUtils.deserializeItemStack(PlaceholderAPI.setPlaceholders(this.player,
        "58 : 1 : name>&aBuild Battle : desc>&eGeneral: \n &8▪ &fWins: &7%sCore_BuildBattle_wins%\n &8▪ &fGames: &7%sCore_BuildBattle_games%\n &8▪ &fPoints: &7%sCore_BuildBattle_points%\n \n&fCoins: &6%sCore_BuildBattle_coins%")));
    
    this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : name>&cBack"));
    
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
            if (evt.getSlot() == 10 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 16 || evt.getSlot() == 22) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
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
