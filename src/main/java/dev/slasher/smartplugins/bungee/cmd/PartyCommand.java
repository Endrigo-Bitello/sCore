package dev.slasher.smartplugins.bungee.cmd;

import dev.slasher.smartplugins.Manager;
import dev.slasher.smartplugins.bungee.party.BungeeParty;
import dev.slasher.smartplugins.bungee.party.BungeePartyManager;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

import static dev.slasher.smartplugins.party.PartyRole.LEADER;

public class PartyCommand extends Commands {
  
  public PartyCommand() {
    super("party");
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cOnly players can use this command."));
      return;
    }
    
    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(
                  "§e/p <message> §7- §bTalk to your party members." +
                  "\n§e/party open §7- §bMake your party public." +
                  "\n§e/party close §7- §bPrivate your party to invite only." +
                  "\n§e/party join <player> §7- §bJoin a player's party." +
                  "\n§e/party accept <player> §7- §bAccept a party invite." +
                  "\n§e/party help §7- §bOpen the party instructions list." +
                  "\n§e/party invite <player> §7- §bSend an invite to a player." +
                  "\n§e/party delete §7- §bDelete the party." +
                  "\n§e/party kick <player> §7- §bKick a member." +
                  "\n§e/party info §7- §bView your party information." +
                  "\n§e/party deny <player> §7- §bDecline a party request." +
                  "\n§e/party leave §7- §bLeave the party." +
                  "\n§e/party transfer <player> §7- §bTransfer party ownership to another player."));
      return;
    }
    
    String action = args[0];
    if (action.equalsIgnoreCase("open")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not in a party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the party leader."));
        return;
      }
      
      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour party is now public."));
        return;
      }
      
      party.setIsOpen(true);
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
      player.sendMessage(TextComponent.fromLegacyText("§6You opened your party to everyone!"));
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
    } else if (action.equalsIgnoreCase("open")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not in a party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the party leader."));
        return;
      }
      
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour party is already private."));
        return;
      }
      
      party.setIsOpen(false);
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
      player.sendMessage(TextComponent.fromLegacyText("§cYou have closed your invite-only party!"));
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
    } else if (action.equalsIgnoreCase("join")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsage: /party join <player>"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot join your own party."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not the party leader."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s party is private."));
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s party is full."));
        return;
      }
      
      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
      player.sendMessage(TextComponent.fromLegacyText("§eYou joined §6" + Role.getPrefixed(target) + "'s §eparty!"));
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
    } else if (action.equalsIgnoreCase("accept")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsage: /party accept <player>"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot accept invitations from yourself."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not a leader."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't have invitations from " + Manager.getCurrent(target)) + ".");
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + "'s is full."));
        return;
      }
      
      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
      player.sendMessage(TextComponent.fromLegacyText("§eYou joined §6" + Role.getPrefixed(target) + "'s §eparty!"));
      player.sendMessage(TextComponent.fromLegacyText("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-"));
    } else if (action.equalsIgnoreCase("help")) {
      player.sendMessage(TextComponent.fromLegacyText(
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
                      "\n§e/party transfer <jogador> §7- §bTransfer party ownership to another player."));
    } else if (action.equalsIgnoreCase("send")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the party leader."));
        return;
      }
      
      party.summonMembers(player.getServer().getInfo());
      player.sendMessage(TextComponent.fromLegacyText("§7You pulled all players from your party."));
    } else if (action.equalsIgnoreCase("delete")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a party."));
        return;
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not the party leader."));
        return;
      }
      
      party.broadcast("" + Role.getPrefixed(player.getName()) + " §cdeleted the party.", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§cYou deleted the party."));
    } else if (action.equalsIgnoreCase("kick")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsage: /party kick <player>"));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not a party leader."));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou can't kick yourself out."));
        return;
      }
      
      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cThis player does not belong to your party."));
        return;
      }
      
      target = party.getName(target);
      party.kick(target);
      party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
      party.broadcast("§b" + Role.getPrefixed(player.getName()) + " §ekicked §f" + Role.getPrefixed(target) + " §eout of the party!\n ");
      party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a party."));
        return;
      }
      
      List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
                 " \n§8▪ §7Leader: " + Role.getPrefixed(party.getLeader()) +
                  "\n§8▪ §7Status: " + (party.isOpen() ? "§fPública" : "§fParticular") +
                  "\n§8▪ §7Member Limit: §f" + party.listMembers().size() + "/" + party.getSlots() +
                  "\n§8▪ §7Members: §f" + StringUtils.join(members, "§7, ") + "\n "));
    } else if (action.equalsIgnoreCase("deny")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsage /party deny <player>"));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou can't deny yourself invitations."));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already belong to a party."));
        return;
      }
      
      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " is not a party leader."));
        return;
      }
      
      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " didn't invite you."));
        return;
      }
      
      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§cYou declined " + Role.getPrefixed(target) + "'s invitation."));
    } else if (action.equalsIgnoreCase("leave")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou don't belong to a party."));
        return;
      }
      
      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§cYou left the party. "));
    } else if (action.equalsIgnoreCase("transfer")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsage /party transfer <player>"));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou are not a party leader."));
        return;
      }
      
      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou already own this party."));
        return;
      }
      
      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cThat player does not belong to your party."));
        return;
      }
      
      target = party.getName(target);
      party.transfer(target);
      party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
      party.broadcast("§b" + Role.getPrefixed(player.getName()) + " §etransferred ownership of the party to §f" + Role.getPrefixed(target) + "§e.");
      party.broadcast("§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-§9§m-§b§m-");
    } else {
      if (action.equalsIgnoreCase("invite")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§cUsage /party invite <player>"));
          return;
        }
        
        action = args[1];
      }
      
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cPlayer not found."));
        return;
      }
      
      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cYou cannot send invitations to yourself"));
        return;
      }
      
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }
      
      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cOnly the party leader can send invites"));
        return;
      }
      
      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cYour party is full."));
        return;
      }
      
      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§cHave you already sent an invitation to " + Manager.getCurrent(action) + "."));
        return;
      }
      
      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(action) + " already belongs to a party."));
        return;
      }
      
      party.invite(target);
      player.sendMessage(
          TextComponent.fromLegacyText("§6----------------------------------\n" + Role.getPrefixed(action) + " §ehas invited to join their party! He has 60 seconds to accept or deny this request.\n§6----------------------------------"));
    }
  }
}
