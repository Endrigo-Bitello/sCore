package dev.slasher.smartplugins.database.conversor;

import com.mongodb.client.model.Filters;
import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.database.MySQLDatabase;
import dev.slasher.smartplugins.nms.NMS;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import dev.slasher.smartplugins.Core;
import dev.slasher.smartplugins.database.MongoDBDatabase;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoDBConversor implements Listener {

  public static String[] CONVERT;
  public static BukkitTask task;
  private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("###.#");

  // 0 = host
  // 1 = port
  // 2 = banco de dados
  // 3 = user
  // 4 = password
  @EventHandler(priority = EventPriority.LOWEST)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    if (CONVERT != null) {
      evt.setCancelled(true);
      String message = evt.getMessage();
      if (message.equalsIgnoreCase("cancelar")) {
        CONVERT = null;
        if (task != null) {
          task.cancel();
          task = null;
        }
        player.sendMessage("§aConversão cancelada!");
        return;
      }

      if (CONVERT[0] == null) {
        CONVERT[0] = message;
        player.sendMessage("§aInsert MySQL Port");
      } else if (CONVERT[1] == null) {
        CONVERT[1] = message;
        player.sendMessage("§aInsert MySQL Database");
      } else if (CONVERT[2] == null) {
        CONVERT[2] = message;
        player.sendMessage("§aInsert MySQL User");
      } else if (CONVERT[3] == null) {
        CONVERT[3] = message;
        player.sendMessage("§aInsert MySQL Password");
      } else if (CONVERT[4] == null) {
        if (message.equals("!")) {
          message = "";
        }
        CONVERT[4] = message;
        startConvert(player);
      } else {
        player.sendMessage("§cOngoing process.");
      }
    }
  }

  private static void startConvert(Player player) {
    String host = CONVERT[0], port = CONVERT[1], database = CONVERT[2], user = CONVERT[3], password = CONVERT[4];
    player.sendMessage(
      " \n§6Info\n §7▪ §fHost: §7" + host + "\n §7▪ §fPort: §7" + port + "\n §7▪ §fDatabase: §7" + database + "\n §7▪ §fUser: §7" + user + "\n §7▪ §fPassword: §7\"" + password + "\"\n ");
    player.sendMessage(" \n §c§lWARNING \n §cIf the data is incorrect, the server will be closed.\n ");

    MongoDBDatabase mongoDB = (MongoDBDatabase) Database.getInstance();
    MySQLDatabase mysql = new MySQLDatabase(host, port, database, user, password, false, true);

    Map<String, Long> tables = new LinkedHashMap<>();
    for (String table : new String[] {"sCoreProfile", "sCoreTheBridge", "sCoreSkyWars", "sCosmetics", "sCoreMurder", "HyMysteryBox", "sCoreNetworkBooster", "HyMysteryBoxContent"}) {
      if (mysql.query("SELECT `table_name` FROM INFORMATION_SCHEMA.STATISTICS WHERE table_name = ?", table) != null) {
        try {
          tables.put(table, Long.parseLong(mysql.query("SELECT COUNT(*) FROM " + table).getObject(1).toString()));
        } catch (Exception ex) {
          player.sendMessage("§cUnable to verify the amount of table entries \"" + table + "\".");
        }
      }
    }

    final List<String> tableQueue = new ArrayList<>(tables.keySet());
    player.sendMessage("§6Tables queue:");
    for (String table : tableQueue) {
      player.sendMessage(" §7▪ §f" + table);
    }
    player.sendMessage("");
    task = new BukkitRunnable() {
      private boolean running;
      private String currentTable = tableQueue.get(0);
      private long currentRow = 0, maxRows = tables.get(currentTable);
      private CachedRowSet rs;
      private ExecutorService executor = Executors.newSingleThreadExecutor();

      @Override
      public void run() {
        if (rs == null) {
          rs = mysql.query("SELECT * FROM `" + this.currentTable + "` LIMIT " + currentRow + ", " + Math.min(currentRow + 1000, maxRows));
          if (rs == null) {
            player.sendMessage("§aTable Processing " + this.currentTable + " completed.");
            tableQueue.remove(0);
            if (tableQueue.isEmpty()) {
              mysql.close();
              player.sendMessage("§aDatabase Conversion Completea §8(MySQL -> MongoDB)");
              cancel();
              return;
            }

            this.currentTable = tableQueue.get(0);
            this.currentRow = 0;
            this.maxRows = tables.get(this.currentTable);
            return;
          }
        }

        if (!running) {
          this.running = true;
          executor.execute(() -> {
            String collection =
              this.currentTable.equalsIgnoreCase("scorenetworkbooster") || this.currentTable.equalsIgnoreCase("smysteryboxcontent") ? this.currentTable : "Profile";
            if (currentRow == 0) {
              if (collection.equalsIgnoreCase("Profile")) {
                if (this.currentTable.equalsIgnoreCase("sCoreProfile")) {
                  mongoDB.getDatabase().getCollection(collection).drop();
                }
              } else {
                mongoDB.getDatabase().getCollection(collection).drop();
              }
            }
            List<Document> documents = new ArrayList<>(rs.size());
            try {
              rs.beforeFirst();
              while (rs.next()) {
                documents.add(convertResultSetToDocument(this.currentTable, rs));
                this.currentRow++;
              }
            } catch (SQLException ex) {
              ex.printStackTrace();
            } finally {
              try {
                rs.close();
              } catch (SQLException ignore) {}
            }
            if (collection.equalsIgnoreCase("Profile") && !this.currentTable.equalsIgnoreCase("sCoreProfile")) {
              documents.forEach(document -> {
                String _id = document.getString("_id");
                document.remove("_id");
                mongoDB.getCollection().updateOne(Filters.eq("_id", _id), new Document("$set", new Document(this.currentTable, document)));
              });
            } else {
              mongoDB.getDatabase().getCollection(collection).insertMany(documents);
            }
            this.running = false;
            this.rs = null;
          });
        }

        if (player.isOnline()) {
          NMS.sendActionBar(player, "§aConverting §f" + this.currentTable + ": §7" + this.currentRow + "/" + this.maxRows + " §8(" + NUMBER_FORMAT
            .format(((this.currentRow * 100.0) / this.maxRows)) + "%)");
        }
      }
    }.runTaskTimerAsynchronously(Core.getInstance(), 0, 5L);
  }

  public static Document convertResultSetToDocument(String table, CachedRowSet rs) throws SQLException {
    Document document = new Document();
    if (table.contains("SkyWars") || table.contains("TheBridge")) {
      document.put("totalkills", rs.getLong("1v1kills") + rs.getLong("2v2kills"));
      document.put("totalwins", rs.getLong("1v1wins") + rs.getLong("2v2wins"));
      if (table.contains("TheBridge")) {
        document.put("totalpoints", rs.getLong("1v1points") + rs.getLong("2v2points"));
      }
    }

    for (int column = 1; column <= rs.getMetaData().getColumnCount(); column++) {
      String name = rs.getMetaData().getColumnName(column);
      if (name.equals("id") || name.equals("name")) {
        if (document.containsKey("_id")) {
          document.put(name, rs.getObject(name));
          continue;
        }

        document.put("_id", rs.getObject(name));
        continue;
      }

      try {
        document.put(name, rs.getLong(name));
        continue;
      } catch (SQLException ignore) {}

      document.put(name, rs.getObject(name));
    }

    return document;
  }
}
