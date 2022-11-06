package dev.slasher.smartplugins.database.tables;

import dev.slasher.smartplugins.database.data.interfaces.DataTableInfo;
import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.database.HikariDatabase;
import dev.slasher.smartplugins.database.MySQLDatabase;
import dev.slasher.smartplugins.database.data.DataContainer;
import dev.slasher.smartplugins.database.data.DataTable;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(name = "kCoreMurder",
    create = "CREATE TABLE IF NOT EXISTS `kCoreMurder` (`name` VARCHAR(32), `clkills` LONG, `clbowkills` LONG, `clknifekills` LONG, `clthrownknifekills` LONG, `clwins` LONG, `cldetectivewins` LONG, `clkillerwins` LONG, `clquickestdetective` LONG, `clquickestkiller` LONG, `clchancedetective` LONG, `clchancekiller` LONG, `askills` LONG, `asthrownknifekills` LONG, `aswins` LONG, `coins` DOUBLE, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCoreMurder` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCoreMurder` VALUES (" +
             "?," +
            " ?," +
            " ?," +
            " ?, " +
             "?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?," +
            " ?)",
    update = "UPDATE `kCoreMurder` SET " +
            "`clkills` = ?, " +
            "`clbowkills` = ?, " +
            "`clknifekills` = ?, " +
            "`clthrownknifekills` = ?, " +
            "`clwins` = ?, " +
            "`cldetectivewins` = ?, " +
            "`clkillerwins` = ?, " +
            "`clquickestdetective` = ?, " +
            "`clquickestkiller` = ?, " +
            "`clchancedetective` = ?, " +
            "`clchancekiller` = ?, " +
            "`askills` = ?, " +
            "`asthrownknifekills` = ?, " +
            "`aswins` = ?, " +
            "`coins` = ?, " +
            "`lastmap` = ?, " +
            "`cosmetics` = ?, " +
            "`selected` = ? " +
            "WHERE LOWER(`name`) = ?")
public class MurderTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreMurder` LIKE 'askills'") == null) {
        ((MySQLDatabase) database).execute(
          "ALTER TABLE `kCoreMurder` ADD `askills` LONG DEFAULT 0 AFTER `clchancekiller`, ADD `asthrownknifekills` LONG DEFAULT 0 AFTER `askills`, ADD `aswins` LONG DEFAULT 0 AFTER `asthrownknifekills`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreMurder` LIKE 'askills'") == null) {
        ((HikariDatabase) database).execute(
          "ALTER TABLE `kCoreMurder` ADD `askills` LONG DEFAULT 0 AFTER `clchancekiller`, ADD `asthrownknifekills` LONG DEFAULT 0 AFTER `askills`, ADD `aswins` LONG DEFAULT 0 AFTER `asthrownknifekills`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("clkills", new DataContainer(0L));
    defaultValues.put("clbowkills", new DataContainer(0L));
    defaultValues.put("clknifekills", new DataContainer(0L));
    defaultValues.put("clthrownknifekills", new DataContainer(0L));
    defaultValues.put("clwins", new DataContainer(0L));
    defaultValues.put("cldetectivewins", new DataContainer(0L));
    defaultValues.put("clkillerwins", new DataContainer(0L));
    defaultValues.put("clquickestdetective", new DataContainer(0L));
    defaultValues.put("clquickestkiller", new DataContainer(0L));
    defaultValues.put("clchancedetective", new DataContainer(1L));
    defaultValues.put("clchancekiller", new DataContainer(1L));
    defaultValues.put("askills", new DataContainer(0L));
    defaultValues.put("asthrownknifekills", new DataContainer(0L));
    defaultValues.put("aswins", new DataContainer(0L));
    defaultValues.put("coins", new DataContainer(0.0D));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    return defaultValues;
  }
}
