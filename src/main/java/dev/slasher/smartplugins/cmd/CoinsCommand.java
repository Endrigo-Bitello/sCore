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
            player.sendMessage("\n§aSEUS COINS:");

            for (String table : new String[] {"kCoreBedWars", "kCoreBuildBattle", "kCoreMurder", "kCoreTheBridge", "kCoreSkyWars"}) {
                String name = table.replace("kCore", "");
                player.sendMessage("\n§6" + name + ": §e" + StringUtils.formatNumber(profile.getCoins(table.replace(" ", ""))));
            }

            player.sendMessage("\n ");
        } else {
            sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
        }
    }
}
