package dev.slasher.smartplugins.database.tables;

import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.database.MySQLDatabase;
import dev.slasher.smartplugins.database.data.DataContainer;
import dev.slasher.smartplugins.database.data.DataTable;
import dev.slasher.smartplugins.database.data.interfaces.DataTableInfo;
import dev.slasher.smartplugins.database.HikariDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "sCoreTheBridge",
    create = "CREATE TABLE IF NOT EXISTS `sCoreTheBridge` (`name` VARCHAR(32), `1v1kills` LONG, `1v1deaths` LONG, `1v1games` LONG, `1v1points` LONG, `1v1wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2games` LONG, `2v2points` LONG, `2v2wins` LONG, `coins` DOUBLE, `winstreak` LONG, `laststreak` LONG, `lastmap` LONG, `hotbar` TEXT, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `sCoreTheBridge` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `sCoreTheBridge` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
    update = "UPDATE `sCoreTheBridge` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1games` = ?, `1v1points` = ?, `1v1wins` = ?, `2v2kills` = ?, `2v2deaths` = ?, `2v2games` = ?, `2v2points` = ?, `2v2wins` = ?, `coins` = ?, `winstreak` = ?, `laststreak` = ?, `lastmap` = ?, `hotbar` = ?, `cosmetics` = ?, `selected` = ? WHERE LOWER(`name`) = ?")
public class TheBridgeTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `sCoreTheBridge` LIKE 'hotbar'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `sCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `sCoreTheBridge` LIKE 'hotbar'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `sCoreTheBridge` ADD `hotbar` TEXT AFTER `lastmap`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("1v1kills", new DataContainer(0L));
    defaultValues.put("1v1deaths", new DataContainer(0L));
    defaultValues.put("1v1games", new DataContainer(0L));
    defaultValues.put("1v1points", new DataContainer(0L));
    defaultValues.put("1v1wins", new DataContainer(0L));
    defaultValues.put("2v2kills", new DataContainer(0L));
    defaultValues.put("2v2deaths", new DataContainer(0L));
    defaultValues.put("2v2games", new DataContainer(0L));
    defaultValues.put("2v2points", new DataContainer(0L));
    defaultValues.put("2v2wins", new DataContainer(0L));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("winstreak", new DataContainer(0L));
    defaultValues.put("laststreak", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("kitconfig", new DataContainer("{}"));
    return defaultValues;
  }
}
