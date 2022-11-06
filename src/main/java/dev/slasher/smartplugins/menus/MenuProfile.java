package dev.slasher.smartplugins.menus;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.libraries.menu.PlayerMenu;
import dev.slasher.smartplugins.menus.profile.*;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.role.Role;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MenuProfile extends PlayerMenu {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MMMM d'th', yyyy '-' HH:mm",
            Locale.forLanguageTag("en-US"));

    public MenuProfile(Profile profile) {
        super(profile.getPlayer(), "My profile", 3);

        this.setItem(10, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack(
                "SKULL_ITEM:3 : 1 : name>&aPersonal informations : desc>&fRank: " + Role.getRoleByName(profile.getDataContainer("sCoreProfile", "role").getAsString())
                        .getName() + "\n&fCash: &b" + StringUtils.formatNumber(profile.getStats("sCoreProfile", "cash")) + "\n \n&fFirst login: &7" + SDF.format(profile.getDataContainer("sCoreProfile", "created").getAsLong()) + "\n&fLast login: &7" + SDF
                        .format(profile.getDataContainer("sCoreProfile", "lastlogin").getAsLong()))));

        this.setItem(11, BukkitUtils.deserializeItemStack(
                "PAPER : 1 : name>&aStatistics : desc>&7View your stats for each\n&7minigame on the server.\n \n&eClick to view your stats!"));

        this.setItem(13, BukkitUtils.deserializeItemStack(
                "REDSTONE_COMPARATOR : 1 : name>&aPreferences : desc>&7On this server you can completely\n&7customize your gaming experience.\n&7Customize various unique options like you wish!\n \n&8Options include enabling or disabling\n&8private messages, players and others.\n \n&eClick to browse!"));

        this.setItem(14, BukkitUtils.deserializeItemStack(
                "MAP : 1 : hide>all : name>&aTitles : desc>&7Lavish style with a unique title\n&7that goes above your head to\n&7the other players.\n \n&8Remembering that you will not see the\n&8title, only the other players.\n \n&eClick to browse!"));

        this.setItem(15, BukkitUtils.deserializeItemStack(
                "POTION:8232 : 1 : hide>all : name>&aCoin Multiplier : desc>&7On our server there is a system of\n&7&6Coins Multipliers &7that increase the amount\n&7of &6Coins &7earned in matches.\n \n&8Multipliers can very from\n&8personal or general, and\n&8may benefit you and even the other players.\n \n&eClick to browse!"));

        this.setItem(16, BukkitUtils.deserializeItemStack(
                "GOLD_INGOT : 1 : name>&aChallanges : desc>&7On our server there is\n&7of &6challanges &7that consists of missions\n&7of unique accomplishment that guarantee\n&7you several lifetime prizes.\n \n&8Rewards vary between titles, coins,\n§8prestige icons and other cosmetics.\n \n&eClique to view!"));


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
                            EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
                        } else if (evt.getSlot() == 11) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuStatistics(profile);
                        } else if (evt.getSlot() == 13) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuPreferences(profile);
                        } else if (evt.getSlot() == 14) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuTitles(profile);
                        } else if (evt.getSlot() == 15) {
                            EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
                            new MenuBoosters(profile);
                        } else if (evt.getSlot() == 16) {
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
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }
}
