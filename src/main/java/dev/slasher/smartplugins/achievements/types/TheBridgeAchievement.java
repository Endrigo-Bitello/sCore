package dev.slasher.smartplugins.achievements.types;

import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class TheBridgeAchievement extends Achievement {
  
  protected TheBridgeReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public TheBridgeAchievement(TheBridgeReward reward, String id, String name, String desc, int reach, String... stats) {
    super("tb-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(100), "1k1", "Assassin (Solo)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins", 50, "1v1kills"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(500), "1k2", "Master Assassin (Solo)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins", 250,
            "1v1kills"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "1w1", "Victorious (Solo)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 50, "1v1wins"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(1000), "1w2", "Master Victorious (Solo)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 200,
            "1v1wins"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "1p1", "Scorer (Solo)", "&7Get a total of %reach%\n&7points to receive:\n \n &8• &6250 Coins", 250, "1v1points"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(1000), "1p2", "Master Scorer (Solo)", "&7Get a total of %reach%\n&7points to receive:\n \n &8• &61.000 Coins", 1000,
            "1v1points"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "1g1", "Persistent (Solo)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "1v1games"));
    
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(100), "2k1", "Assassin (Doubles)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6100 Coins", 50, "2v2kills"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(500), "2k2", "Master Assassin (Doubles)", "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &6500 Coins", 250,
            "2v2kills"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "2w1", "Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 50, "2v2wins"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(1000), "2w2", "Master Victorious (Doubles)", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &61.000 Coins", 200,
            "2v2wins"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "2p1", "Scorer (Doubles)", "&7Get a total of %reach%\n&7points to receive:\n \n &8• &6250 Coins", 250, "2v2points"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(1000), "2p2", "Master Scorer (Doubles)", "&7Get a total of %reach%\n&7points to receive:\n \n &8• &61.000 Coins", 1000,
            "2v2points"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new CoinsReward(250), "2g1", "Persistent (Doubles)", "&7Play a total of %reach%\n&7matches to receive:\n \n &8• &6250 Coins", 250, "2v2games"));
    
    Achievement.addAchievement(new TheBridgeAchievement(new TitleReward("tbk"), "tk", "Killer On The Bridge",
        "&7Kill a total of %reach%\n&7players to receive:\n \n &8• &fIcon: &cBridge Killer", 500, "1v1kills", "2v2kills"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new TitleReward("tbw"), "tw", "Glorious over Bridges", "&7Win a total of %reach%\n&7matches to receive:\n \n &8• &fIcon: &6Bridge Leader",
            400, "1v1wins", "2v2wins"));
    Achievement.addAchievement(
        new TheBridgeAchievement(new TitleReward("tbp"), "tp", "Score Mastery", "&7Get a total of %reach%\n&7points to receive:\n \n &8• &fIcon: &eMaster Scorer",
            2000, "1v1points", "2v2points"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("sTheBridge", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("sTheBridge", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "PAPER" : "COAL_BLOCK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface TheBridgeReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements TheBridgeReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("sTheBridge", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements TheBridgeReward {
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
