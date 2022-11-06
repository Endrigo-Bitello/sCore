package dev.slasher.smartplugins.bungee.cmd;

import dev.slasher.smartplugins.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FakeResetCommand extends Commands {
  
  public FakeResetCommand() {
    super("fakeremove");
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cThis command is exclusive to players."));
      return;
    }
    
    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("hyplay.cmd.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou don't have permission."));
      return;
    }
    
    if (!Bungee.isFake(player.getName())) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou are not using a fake nickname."));
      return;
    }
    
    Bungee.removeFake(player);
  }
}
