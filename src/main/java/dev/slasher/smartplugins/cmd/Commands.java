package dev.slasher.smartplugins.cmd;

import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.cash.CashManager;
import dev.slasher.smartplugins.player.fake.FakeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.TeleportCommand;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class Commands extends Command {

    public Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));

        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "score", this);
        } catch(ReflectiveOperationException ex) {
            Core.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
        }
    }

    public static void setupCommands() {
        new CoreCommand();
        new CoinsCommand();
        if(CashManager.CASH) {
            new CashCommand();
        }
        if(!FakeManager.isBungeeSide()) {
            new FakeCommand();
            new PartyCommand();
        }
        new TeleportCommand();
    }

    public abstract void perform(CommandSender sender, String label, String[] args);

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        this.perform(sender, commandLabel, args);
        return true;
    }
}