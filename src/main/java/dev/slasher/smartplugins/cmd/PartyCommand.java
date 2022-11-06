package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.Manager;
import dev.slasher.smartplugins.bukkit.BukkitParty;
import dev.slasher.smartplugins.bukkit.BukkitPartyManager;
import dev.slasher.smartplugins.party.PartyRole;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PartyCommand extends Commands {
  
  public PartyCommand() {
    super("party", "p");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cOnly players can use this command.");
      return;
    }
    
    Player player = (Player) sender;
    if (label.equalsIgnoreCase("p")) {
      if (args.length == 0) {
        player.sendMessage("§cUsage: /cp [message] to chat with your party.");
        return;
      }
      
      BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage("§cYou don't belong to a Party.");
        return;
      }
      
      party.broadcast("§6[PARTY] " + Role.getPrefixed(player.getName()) + "§f: " + StringUtils.join(args, " "));
    } else {
      if (args.length == 0) {
        player.sendMessage(
                        "§e/p <message> §7- §bTalk to your party members." +
                        "\n§e/party open §7- §bMake your party public." +
                        "\n§e/party close §7- §bPrivate your party to invite only." +
                        "\n§e/party join <player> §7- §bJoin a player's party." +
                        "\n§e/party accept <player> §7- §bAccept a party invite." +
                        "\n§e/party help §7- §bOpen the party instructions list." +
                        "\n§e/party invite <jogador> §7- §bSend an invite to a player." +
                        "\n§e/party delete §7- §bDelete the party." +
                        "\n§e/party kick <jogador> §7- §bKick a member." +
                        "\n§e/party info §7- §bView your party information." +
                        "\n§e/party deny <jogador> §7- §bDecline a party request." +
                        "\n§e/party leave §7- §bLeave the party." +
                        "\n§e/party transfer <jogador> §7- §bTransfer party ownership to another player.");
        return;
      }
      
      String action = args[0];
      if (action.equalsIgnoreCase("open")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou don't belong to a Party.");
          return;
        }
        
        if (!party.isLeader(player.getName())) {
          player.sendMessage("§cYou are not the Party Leader.");
          return;
        }
        
        if (party.isOpen()) {
          player.sendMessage("§cYour party is now public.");
          return;
        }
        
        party.setIsOpen(true);
        player.sendMessage("§aYou opened the party to any player.");
      } else if (action.equalsIgnoreCase("close")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou don't belong to a Party.");
          return;
        }
        
        if (!party.isLeader(player.getName())) {
          player.sendMessage("§cYou are not the Party Leader.");
          return;
        }
        
        if (!party.isOpen()) {
          player.sendMessage("§cYour party is already private.");
          return;
        }
        
        party.setIsOpen(false);
        player.sendMessage("§cYou closed the party to invite only.");
      } else if (action.equalsIgnoreCase("entrar")) {
        if (args.length == 1) {
          player.sendMessage("§cUsage /party join [player]");
          return;
        }
        
        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou cannot join the party yourself.");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage("§cYou already belong to a Party.");
          return;
        }
        
        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage("§c" + Manager.getCurrent(target) + " is not a party leader.");
          return;
        }
        
        target = party.getName(target);
        if (!party.isOpen()) {
          player.sendMessage("§c" + Manager.getCurrent(target) + "'s party is closed to invitees only.");
          return;
        }
        
        if (!party.canJoin()) {
          player.sendMessage("§c" + Manager.getCurrent(target) + "'s is full.");
          return;
        }
        
        party.join(player.getName());
        player.sendMessage("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
        player.sendMessage("§eYou joined §6" + Role.getPrefixed(target) + "'s §eparty!");
        player.sendMessage("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
      } else if (action.equalsIgnoreCase("aceitar")) {
        if (args.length == 1) {
          player.sendMessage("§cUsage /party accept [player]");
          return;
        }
        
        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou cannot accept invitations from yourself.");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage("§cYou already belong to a Party.");
          return;
        }
        
        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage("§c" + Manager.getCurrent(target) + " is not a party leader.");
          return;
        }
        
        target = party.getName(target);
        if (!party.isInvited(player.getName())) {
          player.sendMessage("§c" + Manager.getCurrent(target) + " didn't invite you to the party.");
          return;
        }
        
        if (!party.canJoin()) {
          player.sendMessage("§c" + Manager.getCurrent(target) + "'s is full.");
          return;
        }
        
        party.join(player.getName());
        player.sendMessage("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
        player.sendMessage("§eYou joined §6" + Role.getPrefixed(target) + "'s §eparty!");
        player.sendMessage("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
      } else if (action.equalsIgnoreCase("help")) {
                player.sendMessage(
                                "§e/p <message> §7- §bTalk to your party members." +
                                "\n§e/party open §7- §bMake your party public." +
                                "\n§e/party close §7- §bPrivate your party to invite only." +
                                "\n§e/party join <player> §7- §bJoin a player's party." +
                                "\n§e/party accept <player> §7- §bAccept a party invite." +
                                "\n§e/party help §7- §bOpen the party instructions list." +
                                "\n§e/party invite <jogador> §7- §bSend an invite to a player." +
                                "\n§e/party delete §7- §bDelete the party." +
                                "\n§e/party kick <jogador> §7- §bKick a member." +
                                "\n§e/party info §7- §bView your party information." +
                                "\n§e/party deny <jogador> §7- §bDecline a party request." +
                                "\n§e/party leave §7- §bLeave the party." +
                                "\n§e/party transfer <jogador> §7- §bTransfer party ownership to another player.");

      } else if (action.equalsIgnoreCase("delete")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou don't belong to a Party.");
          return;
        }
        
        if (!party.isLeader(player.getName())) {
          player.sendMessage("§cYou are not the Party Leader.");
          return;
        }
        
        party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §cdeleted the party!\n ", true);
        party.delete();
        player.sendMessage("§cYou deleted the party.");
      } else if (action.equalsIgnoreCase("kick")) {
        if (args.length == 1) {
          player.sendMessage("§cUsage /party kick <player>");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou are not a Party Leader.");
          return;
        }
        
        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou cannot expel yourself.");
          return;
        }
        
        if (!party.isMember(target)) {
          player.sendMessage("§cThis player does not belong to your Party.");
          return;
        }
        
        target = party.getName(target);
        party.kick(target);
        party.broadcast(" \n" + Role.getPrefixed(player.getName()) + " §ekicked " + Role.getPrefixed(target) + " §eout of the party!\n ");
      } else if (action.equalsIgnoreCase("info")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou don't belong to a Party.");
          return;
        }
        
        List<String> members =
            party.listMembers().stream().filter(pp -> pp.getRole() != PartyRole.LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
        player.sendMessage(
            " \n§6Leader: " + Role.getPrefixed(party.getLeader()) + "\n§6Type: " + (party.isOpen() ? "§ePúblic" : "§ePrivate") + "\n§6Members Limit: §f" + party.listMembers()
                .size() + "/" + party.getSlots() + "\n§6Members: " + StringUtils.join(members, "§7, ") + "\n ");
      } else if (action.equalsIgnoreCase("deny")) {
        if (args.length == 1) {
          player.sendMessage("§cUsage: /party deny <player>");
          return;
        }
        
        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou can't deny invitations from yourself.");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party != null) {
          player.sendMessage("§cYou already belong to a Party.");
          return;
        }
        
        party = BukkitPartyManager.getLeaderParty(target);
        if (party == null) {
          player.sendMessage("§c" + Manager.getCurrent(target) + " is not a Party Leader.");
          return;
        }
        
        target = party.getName(target);
        if (!party.isInvited(player.getName())) {
          player.sendMessage("§c" + Manager.getCurrent(target) + " did not invite you to party.");
          return;
        }
        
        party.reject(player.getName());
        player.sendMessage(" \n§aYou declined " + Role.getPrefixed(target) + "'s Party invite.\n ");
      } else if (action.equalsIgnoreCase("leave")) {
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou don't belong to a Party.");
          return;
        }
        
        party.leave(player.getName());
        player.sendMessage("§eYou left the Party!");
      } else if (action.equalsIgnoreCase("transfer")) {
        if (args.length == 1) {
          player.sendMessage("§cUsage /party transfer <player>");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getLeaderParty(player.getName());
        if (party == null) {
          player.sendMessage("§cYou are not a Party Leader.");
          return;
        }
        
        String target = args[1];
        if (target.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou cannot transfer the Party to yourself.");
          return;
        }
        
        if (!party.isMember(target)) {
          player.sendMessage("§cThis player does not belong to your party.");
          return;
        }
        
        target = party.getName(target);
        party.transfer(target);
        party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
        party.broadcast("§b" + Role.getPrefixed(player.getName()) + " §etransfered ownership of the party to §f" + Role.getPrefixed(target) + "§e.");
        party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
      } else {
        if (action.equalsIgnoreCase("invite")) {
          if (args.length == 1) {
            player.sendMessage("§cUsage /party invite [player]");
            return;
          }
          
          action = args[1];
        }
        
        Player target = Bukkit.getPlayerExact(action);
        if (target == null) {
          player.sendMessage("§cPlayer not found.");
          return;
        }
        
        action = target.getName();
        if (action.equalsIgnoreCase(player.getName())) {
          player.sendMessage("§cYou cannot send invitations to yourself.");
          return;
        }
        
        BukkitParty party = BukkitPartyManager.getMemberParty(player.getName());
        if (party == null) {
          party = BukkitPartyManager.createParty(player);
        }

        if (!party.isLeader(player.getName())) {
          player.sendMessage("§cOnly the Party Leader can send invites.");
          return;
        }
        
        if (!party.canJoin()) {
          player.sendMessage("§cYour party is full.");
          return;
        }
        
        if (party.isInvited(action)) {
          player.sendMessage("§cHave you already sent an invitation to " + Manager.getCurrent(action) + ".");
          return;
        }
        
        if (BukkitPartyManager.getMemberParty(action) != null) {
          player.sendMessage("§c" + Manager.getCurrent(action) + " already belong to a Party.");
          return;
        }
        
        party.invite(target);
        player.sendMessage("§6----------------------------------\n" + Role.getPrefixed(action) + " §ehas invited to join their party! He has 60 seconds to accept or deny this request.\n§6----------------------------------");
      }
    }
  }
}
