package dev.slasher.smartplugins;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slasher.smartplugins.booster.Booster;
import dev.slasher.smartplugins.cmd.Commands;
import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.hook.SCoreExpansion;
import dev.slasher.smartplugins.hook.protocollib.FakeAdapter;
import dev.slasher.smartplugins.hook.protocollib.HologramAdapter;
import dev.slasher.smartplugins.hook.protocollib.NPCAdapter;
import dev.slasher.smartplugins.nms.NMS;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.fake.FakeManager;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.plugin.HyPlugin;
import dev.slasher.smartplugins.plugin.config.HyConfig;
import dev.slasher.smartplugins.queue.Queue;
import dev.slasher.smartplugins.queue.QueuePlayer;
import dev.slasher.smartplugins.servers.ServerItem;
import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.deliveries.Delivery;
import dev.slasher.smartplugins.libraries.MinecraftVersion;
import dev.slasher.smartplugins.libraries.holograms.HologramLibrary;
import dev.slasher.smartplugins.libraries.npclib.NPCLibrary;
import dev.slasher.smartplugins.listeners.Listeners;
import dev.slasher.smartplugins.listeners.PluginMessageListener;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class Core extends HyPlugin {

  private static Core instance;
  public static boolean validInit;
  public static final List<String> warnings = new ArrayList<>();
  public static final List<String> minigames = Arrays.asList("Sky Wars", "The Bridge", "Murder", "Bed Wars", "Build Battle");
  public static String minigame = "";

  @Override
  public void start() {
    instance = this;
  }

  @Override
  public void load() {}

  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("Your version is not compatible with the plugin, use version 1_8_R3 (Current: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }

    saveDefaultConfig();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();

    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }

    // Plugins que causaram incompatibilidades
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("blacklist.txt"), StandardCharsets.UTF_8))) {
      String plugin;
      while ((plugin = reader.readLine()) != null) {
        if (Bukkit.getPluginManager().getPlugin(plugin.split(" ")[0]) != null) {
          warnings.add(" - " + plugin);
        }
      }
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, "Cannot load blacklist.txt: ", ex);
    }

    if (!warnings.isEmpty()) {
      CommandSender sender = Bukkit.getConsoleSender();
      StringBuilder sb = new StringBuilder(" \n §6§lIMPORTANT WARNING\n \n §7Apparently you use plugins that conflict with HyCore.\n §7You will not be able to start the server with the following plugins:");
      for (String warning : warnings) {
        sb.append("\n§f").append(warning);
      }
      sb.append("\n ");
      sender.sendMessage(sb.toString());
      System.exit(0);
      return;
    }


    // Remover /reload
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);

      //RELOAD
      knownCommands.remove("rl");
      knownCommands.remove("reload");
      knownCommands.remove("bukkit:rl");
      knownCommands.remove("bukkit:reload");

      //PL ETC
      knownCommands.remove("pl");
      knownCommands.remove("plugins");
      knownCommands.remove("ver");
      knownCommands.remove("version");
      knownCommands.remove("help");
      knownCommands.remove("?");
      knownCommands.remove("bukkit:pl");
      knownCommands.remove("bukkit:plugins");
      knownCommands.remove("bukkit:ver");
      knownCommands.remove("bukkit:version");
      knownCommands.remove("bukkit:help");
      knownCommands.remove("bukkit:?");
      knownCommands.remove("about");
      knownCommands.remove("bukkit:about");
    } catch (ReflectiveOperationException ex) {
      getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
    }

    if (!PlaceholderAPIPlugin.getInstance().getDescription().getVersion().equals("2.10.5")) {
      Bukkit.getConsoleSender().sendMessage(" \n §6§lIMPORTANT WARNING\n \n §7Use the PlaceHolderAPI version 2.10.5, you are using the v" + PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "\n ");
      System.exit(0);
      return;
    }

    PlaceholderAPI.registerExpansion(new SCoreExpansion());

    Database.setupDatabase(
      getConfig().getString("database.type"),
      getConfig().getString("database.mysql.host"),
      getConfig().getString("database.mysql.port"),
      getConfig().getString("database.mysql.database"),
      getConfig().getString("database.mysql.user"),
      getConfig().getString("database.mysql.password"),

      getConfig().getBoolean("database.mysql.hikari",false),
      getConfig().getBoolean("database.mysql.mariadb", false),
      getConfig().getString("database.mongodb.url", "")
    );

    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);

    setupRoles();
    Language.setupLanguage();
    FakeManager.setupFake();
    Title.setupTitles();
    Booster.setupBoosters();
    Delivery.setupDeliveries();
    ServerItem.setupServers();
    Achievement.setupAchievements();

    Commands.setupCommands();
    Listeners.setupListeners();

    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());

    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "HyCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "HyCore", new PluginMessageListener());

   // Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> new SlickUpdater(this, 2).run());

    validInit = true;
    this.getLogger().info("The plugin has been activated.");
  }

  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          profile.save();
          this.getLogger().info("The player " + profile.getName() + " has been saved!");
          profile.destroy();
        }
      });
      Database.getInstance().close();
    }

    File update = new File("plugins/HyCore/update", "HyCore.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/HyCore.jar"));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/HyCore.jar"));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update added.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("The plugin has been disabled.");
  }

  private void setupRoles() {
    HyConfig config = getConfig("roles");
    for (String key : config.getSection("roles").getKeys(false)) {
      String name = config.getString("roles." + key + ".name");
      String prefix = config.getString("roles." + key + ".prefix");
      String permission = config.getString("roles." + key + ".permission");
      boolean broadcast = config.getBoolean("roles." + key + ".broadcast", true);
      boolean broadcastplus = config.getBoolean("roles." + key + ".broadcastplus", true);
      boolean alwaysVisible = config.getBoolean("roles." + key + ".alwaysvisible", false);

      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast, broadcastplus));
    }

    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Default", "&7", "", false, false, false));
    }
  }

  private static Location lobby;

  public static void setLobby(Location location) {
    lobby = location;
  }

  public static Location getLobby() {
    return lobby;
  }

  public static Core getInstance() {
    return instance;
  }

  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }

    Player player = profile.getPlayer();
    if (Core.getInstance().getConfig("utils").getBoolean("queue")) {
      if (player != null) {
        player.closeInventory();
        Queue queue = player.hasPermission("core.queue") ? Queue.VIP : Queue.MEMBER;
        QueuePlayer qp = queue.getQueuePlayer(player);
        if (qp != null) {
          if (qp.server.equalsIgnoreCase(name)) {
            qp.player.sendMessage("§cYou are already in the connection queue!");
          } else {
            qp.server = name;
          }
          return;
        }

        queue.queue(player, profile, name);
      }
    } else {
      if (player != null) {
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          if (player.isOnline()) {
            player.closeInventory();
            NMS.sendActionBar(player, "");
            profile.saveSync();
            player.sendMessage("§aConnecting...");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(name);
            player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
          }
        });
      }
    }
  }
}
