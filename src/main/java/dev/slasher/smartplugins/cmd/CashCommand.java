package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.cash.CashException;
import dev.slasher.smartplugins.cash.CashManager;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand extends Commands {
  
  public CashCommand() {
    super("cash");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (args.length == 0) {
      if (sender instanceof Player) {
        sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
        return;
      }
      
      sender.sendMessage(
              " \n§b/cash set <player> <amount> §f- §7Set someone's cash number.." +
                  "\n§b/cash add <player> <amount> §f- §7Give cash to a player." +
                  "\n§b/cash remove <player> <amount> §f- §7Remove a player's cash value.\n ");
      return;
    }
    
    if (!sender.hasPermission("hyplay.cmd.cash")) {
      sender.sendMessage("§7Cash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
      return;
    }
    
    String action = args[0];
    if (!action.equalsIgnoreCase("set") && !action.equalsIgnoreCase("add") && !action.equalsIgnoreCase("remove")) {
      sender.sendMessage(
              " \n§b/cash set <player> <amount> §f- §7Set someone's cash number.." +
                      "\n§b/cash add <player> <amount> §f- §7Give cash to a player." +
                      "\n§b/cash remove <player> <amount> §f- §7Remove a player's cash value.\n ");
      return;
    }
    
    if (args.length <= 2) {
      sender.sendMessage("§cUsage /cash " + action + " <player> <amount>");
      return;
    }
    
    long amount = 0L;
    try {
      if (args[2].startsWith("-")) {
        throw new NumberFormatException();
      }
      
      amount = Long.parseLong(args[2]);
    } catch (NumberFormatException ex) {
      sender.sendMessage("§cUse valid and positive numbers.");
      return;
    }
    
    try {
      switch (action.toLowerCase()) {
        case "set":
          CashManager.setCash(args[1], amount);
          sender.sendMessage("§aYou have set " + Role.getColored(args[1]) + " cash to " + StringUtils.formatNumber(amount) + "§a.");
          break;
        case "add":
          CashManager.addCash(args[1], amount);
          sender.sendMessage("§7You added §b" + StringUtils.formatNumber(amount) + " Cash §7to §f" + Role.getColored(args[1]) + "§7.");
          break;
        case "remove":
          CashManager.removeCash(args[1], amount);
          sender.sendMessage("§cYou removed §b" + StringUtils.formatNumber(amount) + " Cash §cfrom §e" + Role.getColored(args[1]) + "§c.");
      }
    } catch (CashException ex) {
      sender.sendMessage("§cUser must be logged in.");
    }
  }
}
