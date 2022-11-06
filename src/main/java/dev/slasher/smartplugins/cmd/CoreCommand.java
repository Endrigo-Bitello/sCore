package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.utils.SmartUpdater;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommand extends Commands {
  
  public CoreCommand() {
    super("sCore");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!player.hasPermission("smart.admin")) {
        player.sendMessage("§cYou don't have permission.");
        return;
      }
      
      if (args.length == 0) {
        player.sendMessage("\n§6/smart update §f- §7Download the latest version of the plugin.\n"
        		           + "§6/smart convert §f- §7Convert the database information.\n ");
        return;
      }
      
      String action = args[0];
      if (action.equalsIgnoreCase("update")) {
        if (SmartUpdater.UPDATER != null) {
          if (!SmartUpdater.UPDATER.canDownload) {
            player.sendMessage("\n§6§lPlugins §6- New version already added!\n"
                + "\n§7The new version has already been added to the server files."
                + "§7To receive the updates, restart the server using §f/stop§7.\n");
            return;
          }
          SmartUpdater.UPDATER.canDownload = false;
          SmartUpdater.UPDATER.downloadUpdate(player);
        } else {
          player.sendMessage("§a[sCore] §fThe plugin is already in its latest version");
        }
      } else if (action.equalsIgnoreCase("convert")) {
        player.sendMessage("§fDatabase: §7" + Database.getInstance().getClass().getSimpleName().replace("Database", ""));
        Database.getInstance().convertDatabase(player);
      } else {
        player.sendMessage("\n§6/hc update §f- §7Download the latest version of the plugin.\n"
                + "§6/hc convert §f- §7Convert the database information.\n ");
      }
    } else {
      sender.sendMessage("§cPlayers only!");
    }
  }
}
