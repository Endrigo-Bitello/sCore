package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.Manager;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.player.fake.FakeManager;
import dev.slasher.smartplugins.player.role.Role;
import dev.slasher.smartplugins.utils.enums.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static dev.slasher.smartplugins.player.fake.FakeManager.ALEX;
import static dev.slasher.smartplugins.player.fake.FakeManager.STEVE;

public class FakeCommand extends Commands {
  
  public FakeCommand() {
    super("fake", "faker", "fakel");
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cOnly players can use this command.");
      return;
    }
    
    Player player = (Player) sender;
    if (!player.hasPermission("hyplay.cmd.fake") || (label.equalsIgnoreCase("fakel") && !player.hasPermission("hyplay.cmd.fakelist"))) {
      player.sendMessage("§cYou do not have permission to use this command.");
      return;
    }
    
    Profile profile = Profile.getProfile(player.getName());
    if (label.equalsIgnoreCase("fake")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cYou cannot use this command at this time.");
        return;
      }
      
      if (FakeManager.getRandomNicks().stream().noneMatch(FakeManager::isUsable)) {
        player.sendMessage(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n ");
        return;
      }
      
      if (args.length == 0) {
        FakeManager.sendRole(player);
        return;
      }
      
      String roleName = args[0];
      if (!FakeManager.isFakeRole(roleName)) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendRole(player);
        return;
      }
      
      if (Role.getRoleByName(roleName) == null) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendRole(player);
        return;
      }
      
      if (args.length == 1) {
        EnumSound.ORB_PICKUP.play(player, 1.0F, 2.0F);
        FakeManager.sendSkin(player, roleName);
        return;
      }
      
      String skin = args[1];
      if (!skin.equalsIgnoreCase("alex") && !skin.equalsIgnoreCase("steve") && !skin.equalsIgnoreCase("you")) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendSkin(player, roleName);
        return;
      }
      
      List<String> enabled = FakeManager.getRandomNicks().stream().filter(FakeManager::isUsable).collect(Collectors.toList());
      String fakeName = enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
      if (fakeName == null) {
        player.sendMessage(" \n §c§lCHANGE NICKNAME\n \n §cNo nickname is available for use at the moment.\n ");
        return;
      }

      enabled.clear();
      FakeManager.applyFake(player, fakeName, roleName, skin.equalsIgnoreCase("steve") ? STEVE : skin.equalsIgnoreCase("you") ? Manager.getSkin(player.getName(), true) : ALEX);
    } else if (label.equalsIgnoreCase("faker")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cYou cannot use this command at the moment.");
        return;
      }
      
      if (!FakeManager.isFake(player.getName())) {
        player.sendMessage("§cYou are not using a fake nickname.");
        return;
      }
      
      FakeManager.removeFake(player);
    } else {
      List<String> nicked = FakeManager.listNicked();
      StringBuilder sb = new StringBuilder();
      for (int index = 0; index < nicked.size(); index++) {
        sb.append("§c").append(nicked.get(index)).append(" §fis actually ").append("§ahyplayfakereal:").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
      }
      
      nicked.clear();
      if (sb.length() == 0) {
        sb.append("§cThere is no user using a fake nickname.");
      }
      
      player.sendMessage(" \n§eList of fake nicknames:\n \n" + sb + "\n ");
    }
  }
}
