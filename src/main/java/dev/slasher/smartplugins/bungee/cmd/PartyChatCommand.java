package dev.slasher.smartplugins.bungee.cmd;

import dev.slasher.smartplugins.bungee.party.BungeeParty;
import dev.slasher.smartplugins.bungee.party.BungeePartyManager;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyChatCommand extends Commands {

  public PartyChatCommand() {
    super("p");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUsage: /pc <message>"));
      return;
    }

    BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
    if (party == null) {
      player.sendMessage(TextComponent.fromLegacyText("§cYou are not in a party."));
      return;
    }

    party.broadcast("§6[PARTY] " + Role.getPrefixed(player.getName()) + " §f➟ §7" + StringUtils.join(args, " "));
  }
}
