package dev.slasher.smartplugins.deliveries;

import dev.slasher.smartplugins.booster.Booster;
import dev.slasher.smartplugins.player.Profile;
import org.bukkit.Bukkit;

public class DeliveryReward {

  private RewardType type;
  private Object[] values;

  public DeliveryReward(String reward) {
    if (reward == null) {
      reward = "";
    }

    String[] splitter = reward.split(">");
    RewardType type = RewardType.from(splitter[0]);
    if (type == null || reward.replace(splitter[0] + ">", "").split(":").length < type.getParameters()) {
      this.type = RewardType.COMMAND;
      this.values = new Object[] {"tell {name} §cPrize \"" + reward + "\" invalid!"};
      return;
    }

    this.type = type;
    try {
      this.values = type.parseValues(reward.replace(splitter[0] + ">", ""));
    } catch (Exception ex) {
      ex.printStackTrace();
      this.type = RewardType.COMMAND;
      this.values = new Object[] {"tell {name} §cPrize \"" + reward + "\" invalid!"};
    }
  }

  public void dispatch(Profile profile) {
    if (this.type == RewardType.COMMAND) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ((String) this.values[0]).replace("{name}", profile.getName()));
    } else if (this.type == RewardType.CASH) {
      profile.setStats("sCoreProfile", profile.getStats("sCoreProfile", "cash") + (long) this.values[0], "cash");
    } else if (this.type.name().contains("_COINS")) {
      profile.addCoins("sCore" + this.type.name().replace("_COINS", ""), (double) this.values[0]);
    } else if (this.type.name().contains("_BOOSTER")) {
      for (int i = 0; i < (int) this.values[0]; i++) {
        profile.getBoostersContainer().addBooster(Booster.BoosterType.valueOf(this.type.name().replace("_BOOSTER", "")), (double) this.values[1], (long) this.values[2]);
      }
    }
  }

  private enum RewardType {
    COMMAND(1),
    CASH(1),
    SKYWARS_COINS(1),
    BEDWARS_Coins(1),
    THEBRIDGE_COINS(1),
    MURDER_COINS(1),
    PRIVATE_BOOSTER(3),
    NETWORK_BOOSTER(3);

    private int parameters;

    RewardType(int parameters) {
      this.parameters = parameters;
    }

    public int getParameters() {
      return this.parameters;
    }

    public Object[] parseValues(String value) throws Exception {
      if (this == COMMAND) {
        return new Object[] {value};
      } else if (this == CASH) {
        return new Object[] {Long.parseLong(value)};
      } else if (this.name().contains("_COINS")) {
        return new Object[] {Double.parseDouble(value)};
      } else if (this.name().contains("_BOOSTER")) {
        String[] values = value.split(":");
        return new Object[] {Integer.parseInt(values[0]), Double.parseDouble(values[1]), Long.parseLong(values[2])};
      }

      throw new Exception();
    }

    public static RewardType from(String name) {
      for (RewardType type : values()) {
        if (type.name().equalsIgnoreCase(name)) {
          return type;
        }
      }

      return null;
    }
  }
}
