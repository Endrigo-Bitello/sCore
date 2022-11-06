package dev.slasher.smartplugins.player.hotbar;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import dev.slasher.smartplugins.menus.MenuProfile;
import dev.slasher.smartplugins.menus.MenuServers;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.utils.StringUtils;
import dev.slasher.smartplugins.listeners.Listeners;
import org.bukkit.entity.Player;

public abstract class HotbarActionType {

  public abstract void execute(Profile profile, String action);

  private static Map<String, HotbarActionType> actionTypes = new HashMap<>();
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.#");

  static {
    actionTypes.put("command", new HotbarActionType() {

      @Override
      public void execute(Profile profile, String action) {
        profile.getPlayer().performCommand(action);
      }
    });
    actionTypes.put("message", new HotbarActionType() {

      @Override
      public void execute(Profile profile, String action) {
        profile.getPlayer().sendMessage(StringUtils.formatColors(action).replace("\\n", "\n"));
      }
    });
    actionTypes.put("core", new HotbarActionType() {
      
      @Override
      public void execute(Profile profile, String action) {
        if (action.equalsIgnoreCase("games")) {
          new MenuServers(profile);
        } else if (action.equalsIgnoreCase("profile")) {
          new MenuProfile(profile);
        } else if (action.equalsIgnoreCase("players")) {
          Player player = profile.getPlayer();
          long start = Listeners.DELAY_PLAYERS.containsKey(player.getName()) ? Listeners.DELAY_PLAYERS.get(player.getName()) : 0;
          if (start > System.currentTimeMillis()) {
            double time = (start - System.currentTimeMillis()) / 1000.0;
            if (time > 0.1) {
              String timeString = DECIMAL_FORMAT.format(time).replace(",", ".");
              if (timeString.endsWith("0")) {
                timeString = timeString.substring(0, timeString.lastIndexOf("."));
              }

              player.sendMessage("§cYou need to wait another §a" + timeString + "s §cto use it again.");
              return;
            }
          }

          Listeners.DELAY_PLAYERS.put(player.getName(), System.currentTimeMillis() + 3000);
          profile.getPreferencesContainer().changePlayerVisibility();
          switch (profile.getPreferencesContainer().getPlayerVisibility()) {
            case TODOS:
              player.sendMessage("§aPlayer visibility enabled.");
              break;
            case NENHUM:
              player.sendMessage("§cPlayer visibility off.");
              break;
          }
          profile.refreshPlayers();
        }
      }
    });
  }

  public static void addActionType(String name, HotbarActionType actionType) {
    actionTypes.put(name.toLowerCase(), actionType);
  }

  public static HotbarActionType fromName(String name) {
    return actionTypes.get(name.toLowerCase());
  }
}
