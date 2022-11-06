package dev.slasher.smartplugins.achievements.types;

import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

public class MurderAchievement extends Achievement {
  
  protected MurderReward reward;
  protected String icon;
  protected String[] stats;
  protected int reach;
  
  public MurderAchievement(MurderReward reward, String id, String name, String desc, int reach, String... stats) {
    super("mm-" + id, name);
    this.reward = reward;
    this.icon = "%material% : 1 : name>%name% : desc>" + desc + "\n \n&fProgress: %progress%";
    this.stats = stats;
    this.reach = reach;
  }
  
  public static void setupAchievements() {
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(500), "d1", "Investigator", "&7Win a total of %reach% games\n&7as a Detective to receive:\n \n &8• &6500 Coins", 100,
            "cldetectivewins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(500), "k2", "Trapper", "&7Win a total of %reach% partidas\n&7as a Assassin to receive:\n \n &8• &6500 Coins", 100,
            "clkillerwins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(1500), "d2", "Forensics Expert", "&7Win a total of %reach% games\n&7as a Detective to receive:\n \n &8• &61.500 Coins", 200,
            "cldetectivewins"));
    Achievement.addAchievement(
        new MurderAchievement(new CoinsReward(1500), "k2", "Betrayer", "&7Win a total of %reach% games\n&7as a Assassin to receive:\n \n &8• &61.500 Coins",
            200, "clkillerwins"));
    
    Achievement.addAchievement(
        new MurderAchievement(new TitleReward("mmd"), "td", "Detective", "&7Win a total of %reach% games\n&7as a Detective to receive:\n \n &8• &fIcon: &6Sherlock Holmes",
            400, "cldetectivewins"));
    Achievement.addAchievement(new MurderAchievement(new TitleReward("mmk"), "tk", "Serial Killer",
        "&7Win a total of %reach% games\n&7as Assassin to receive:\n \n &8• &fIcon: &4Jeff the Killer", 400, "clkillerwins"));
  }
  
  @Override
  protected void give(Profile profile) {
    this.reward.give(profile);
  }
  
  @Override
  protected boolean check(Profile profile) {
    return profile.getStats("sMurder", this.stats) >= this.reach;
  }
  
  public ItemStack getIcon(Profile profile) {
    long current = profile.getStats("sMurder", this.stats);
    if (current > this.reach) {
      current = this.reach;
    }
    
    return BukkitUtils.deserializeItemStack(
        this.icon.replace("%material%", current == this.reach ? "PAPER" : "COAL_BLOCK").replace("%name%", (current == this.reach ? "&a" : "&c") + this.getName())
            .replace("%current%", StringUtils.formatNumber(current)).replace("%reach%", StringUtils.formatNumber(this.reach))
            .replace("%progress%", (current == this.reach ? "&a" : current > this.reach / 2 ? "&7" : "&c") + current + "/" + this.reach));
  }
  
  interface MurderReward {
    void give(Profile profile);
  }
  
  static class CoinsReward implements MurderReward {
    private final double amount;
    
    public CoinsReward(double amount) {
      this.amount = amount;
    }
    
    @Override
    public void give(Profile profile) {
      profile.getDataContainer("sMurder", "coins").addDouble(this.amount);
    }
  }
  
  static class TitleReward implements MurderReward {
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
