package dev.slasher.smartplugins.titles;

import com.mongodb.client.MongoCursor;
import dev.slasher.smartplugins.player.Profile;
import dev.slasher.smartplugins.utils.BukkitUtils;
import dev.slasher.smartplugins.utils.StringUtils;
import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.database.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Title {

  private String id;
  private String icon;
  private String title;

  public Title(String id, String title, String desc) {
    this.id = id;
    this.icon = "%material%:%durability% : 1 : hide>all : name>%name% : desc>&fTitle: " + title + "\n \n" + desc + "\n \n%action%";
    this.title = title;
  }

  public String getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public void give(Profile profile) {
    if (!this.has(profile)) {
      profile.getTitlesContainer().add(this);
    }
  }

  public boolean has(Profile profile) {
    return profile.getTitlesContainer().has(this);
  }

  public ItemStack getIcon(Profile profile) {
    boolean has = this.has(profile);
    Title selected = profile.getSelectedContainer().getTitle();

    return BukkitUtils.deserializeItemStack(
      this.icon.replace("%material%", has ? (selected != null && selected.equals(this)) ? "MAP" : "EMPTY_MAP" : "STAINED_GLASS_PANE").replace("%durability%", has ? "0" : "14")
        .replace("%name%", (has ? "&a" : "&c") + StringUtils.stripColors(this.title))
        .replace("%action%", (has ? (selected != null && selected.equals(this)) ? "&eClick to unselect!" : "&eClick to select!" : "&cYou do not own this title.")));
  }

  private static final List<Title> TITLES = new ArrayList<>();

  public static void setupTitles() {
    TITLES.add(new Title("tbk", "§cBridge Sentinel", "&8Can be unlocked through the\n\"Assassino das Pontes\" &8challange."));
    TITLES.add(new Title("tbw", "§6Bridge Leader", "&8Can be unlocked through the\n\"Glorioso sobre Pontes\" &8challange."));
    TITLES.add(new Title("tbp", "§eMaster Scorer", "&8Can be unlocked through the\n\"Maestria em Pontuação\"&8challange."));

    TITLES.add(new Title("swk", "§cAngel of Death", "&8Can be unlocked through the\n\"Traidor Celestial\" &8challange."));
    TITLES.add(new Title("sww", "§bHeavenly King", "&8Can be unlocked through the\n\"Destrono Celestial\" &8challange."));
    TITLES.add(new Title("swa", "§6Wings Companion", "&8Can be unlocked through the\n\"Anjo Guardião\" &8challange."));

    TITLES.add(new Title("mmd", "§6Sherlock Holmes", "&8Can be unlocked through the\n\"Detetive\" &8challange."));
    TITLES.add(new Title("mmk", "§4Jef the Killer", "&8Can be unlocked through the\n\"Serial Killer\" &8challange."));

    TITLES.add(new Title("bwk", "§cDestroyer of Dreams", "&8Can be unlocked through the\n\"Assasino a espreita\" &8challange."));
    TITLES.add(new Title("bww", "§6Sleepy Angel", "&8Can be unlocked through the\n\"Protetor de Camas\" &8challange."));
    TITLES.add(new Title("bwp", "§4Nightmare", "&8Can be unlocked through the\n\"Freddy Krueger\" &8challange."));

    if (Database.getInstance() instanceof MongoDBDatabase) {
      MongoDBDatabase database = ((MongoDBDatabase) Database.getInstance());

      MongoCursor<Document> titles = database.getDatabase().getCollection("HyCoreTitles").find().cursor();
      while (titles.hasNext()) {
        Document title = titles.next();
        TITLES.add(new Title(title.getString("_id"), title.getString("name"), title.getString("description")));
      }

      titles.close();
    }
  }

  public static Title getById(String id) {
    return TITLES.stream().filter(title -> title.getId().equals(id)).findFirst().orElse(null);
  }

  public static Collection<Title> listTitles() {
    return TITLES;
  }
}
