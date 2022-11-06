package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand extends Commands {
  
  public CoinsCommand() {
    super("coins");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      Profile profile = Profile.getProfile(player.getName());
      player.sendMessage("\n§eYour Coins:\n ");
      
      for (String name : new String[]{"Bed Wars", "Murder", "The Bridge", "Sky Wars", "Build Battle"}) {
        player.sendMessage(" §8▪ §e" + name + ": §6" + StringUtils
            .formatNumber(profile.getCoins("sCore" + name.replace(" ", ""))));
      }
      
      player.sendMessage("\n");
    } else {
      sender.sendMessage("§cOnly players.");
    }
  }
}