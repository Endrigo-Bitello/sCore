package dev.slasher.smartplugins.achievements.types;

import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class BedWarsAchievement extends Achievement {
  
  protected BedWarsReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public BedWarsAchievement(BedWarsReward reward, String id, String name, String desc, int reach, String... stats) {
    super("bw-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(100), "2k1", "Assassin (Dobules)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins",
            50, "2v2kills"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(500), "2k2", "Master Assassin (Doubles)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins",
            250, "2v2kills"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "2w1", "Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins",
            50, "2v2wins"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(1000), "2w2", "Mestre Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins",
            200, "2v2wins"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "2d1", "Destroyer (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "2v2bedsdestroyeds"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(1000), "2d2", "Master Destroyer (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 1000, "2v2bedsdestroyeds"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "2g1", "Persistent (Doubles)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 1000, "2v2games"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(100), "4k1", "Assassin (4v4v4v4)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins", 50, "4v4kills"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(500), "4k2", "Master Assassin (4v4v4v4)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins", 250, "4v4kills"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "4w1", "Victorious (4v4v4v4)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 50, "4v4wins"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(1000), "4w2", "Master Victorious (4v4v4v4)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 200, "4v4wins"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "4d1", "Destroyer (4v4v4v4)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "4v4bedsdestroyeds"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(1000), "4d2", "Master Destroyer (4v4v4v4)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 1000, "4v4bedsdestroyeds"));
    Achievement.addAchievement(
        new BedWarsAchievement(new CoinsReward(250), "4g1", "Persistent (4v4v4v4)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 1000, "4v4games"));
    
    Achievement.addAchievement(
        new BedWarsAchievement(new TitleReward("bwk"), "bwk", "Lurking Killer", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &fIcon: &cDestroyer of Dreams", 500,
            "2v2kills", "4v4kills"));
    Achievement.addAchievement(
        new BedWarsAchievement(new TitleReward("bww"), "bww", "Bed Protector", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fIcon: &6Sleepy Angel", 400,
            "2v2wins", "4v4wins"));
    Achievement.addAchievement(
        new BedWarsAchievement(new TitleReward("bwp"), "bwp", "Freddy Krueger", "&7Destroy a total of %reach%\n&7beds to receive:\n \n &8• &fIcon: &4Nightmare", 2000,
            "2v2bedsdestroyeds", "4v4bedsdestroyeds"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("HyBedWars", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("HyBedWars", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "PAPER" : "COAL_BLOCK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface BedWarsReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements BedWarsReward {
    
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("HyBedWars", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements BedWarsReward {
    private final String titleId;
    
    public TitleReward(String titleId) {
      this.titleId = titleId;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getTitlesContainer().add(Title.getById(this.titleId));
    }
  }
}