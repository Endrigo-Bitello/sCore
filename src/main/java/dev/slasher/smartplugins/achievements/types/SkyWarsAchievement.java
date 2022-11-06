package dev.slasher.smartplugins.achievements.types;

import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class SkyWarsAchievement extends Achievement {
  
  protected SkyWarsReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public SkyWarsAchievement(SkyWarsReward reward, String id, String name, String desc, int reach, String... stats) {
    super("sw-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "1k1", "Assassin (Solo)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins", 50, "1v1kills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "1k2", "Master Assassin (Solo)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins", 250,
            "1v1kills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "1w1", "Victorious Solo)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 50, "1v1wins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "1w2", "Master Victorious (Solo)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 200,
            "1v1wins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "1p1", "Assistent (Solo)", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6100 Coins", 50,
            "1v1assists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "1p2", "Master Assistent (Solo)", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6500 Coins", 250,
            "1v1assists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "1g1", "Persistent (Solo)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "1v1games"));
    
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "2k1", "Assassin (Doubles)", "&7Kill a total of %reach%\n&7players to receive :\n \n &8• &6100 Coins", 50, "2v2kills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "2k2", "Master Assassin (Doubles)", "&7Kill a total of %reach%\n&7players to receive :\n \n &8• &6500 Coins", 250,
            "2v2kills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "2w1", "Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 50, "2v2wins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(1000), "2w2", "Master Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 200,
            "2v2wins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(100), "2p1", "Assistent (Doubles)", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6100 Coins", 50,
            "2v2assists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(500), "2p2", "Master Assistent (Doubles)", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &6500 Coins", 250,
            "2v2assists"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new CoinsReward(250), "2g1", "Persistent (Doubles)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "2v2games"));
    
    Achievement.addAchievement(
        new SkyWarsAchievement(new TitleReward("swk"), "tk", "Celestial Traitor ", "&7Kill a total of %reach%\n&7players to receive :\n \n &8• &fIcon: &4Angel of Death", 500,
            "1v1kills", "2v2kills"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new TitleReward("sww"), "tw", "Heavenly Fate", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fIcon: &bHeavenly King", 400,
            "1v1wins", "2v2wins"));
    Achievement.addAchievement(
        new SkyWarsAchievement(new TitleReward("swa"), "tp", "Guardian Angel", "&7Get a total of %reach%\n&7assists to receive:\n \n &8• &fIcon: &6Wings Companion",
            500, "1v1assists", "2v2assists"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("HySkyWars", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("HySkyWars", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "PAPER" : "COAL_BLOCK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface SkyWarsReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements SkyWarsReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("HySkyWars", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements SkyWarsReward {
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
