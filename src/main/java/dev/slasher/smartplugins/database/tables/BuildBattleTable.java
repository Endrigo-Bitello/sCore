package dev.slasher.smartplugins.database.tables;


import dev.slasher.smartplugins.database.Database;
import dev.slasher.smartplugins.database.HikariDatabase;
import dev.slasher.smartplugins.database.MySQLDatabase;
import dev.slasher.smartplugins.database.data.DataContainer;
import dev.slasher.smartplugins.database.data.DataTable;
import dev.slasher.smartplugins.database.data.interfaces.DataTableInfo;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "kCoreBuildBattle",
        create = "CREATE TABLE IF NOT EXISTS `kCoreBuildBattle` (`name` VARCHAR(32), `1v1wins` LONG, `2v2wins` LONG, `1v1games` LONG, `2v2games` LONG, `points` LONG, " +
                "`monthlywins` LONG, `monthlygames` LONG, `monthlypoints` LONG, `month` TEXT, `coins` DOUBLE, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `kCoreBuildBattle` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `kCoreBuildBattle` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `kCoreBuildBattle` SET " +
                "`1v1wins` = ?, " +
                "`2v2wins` = ?, " +
                "`1v1games` = ?, " +
                "`2v2games` = ?, " +
                "`points` = ?, " +
                "`monthlywins` = ?, " +
                "`monthlygames` = ?, " +
                "`monthlypoints` = ?, " +
                "`month` = ?, " +
                "`coins` = ?, " +
                "`lastmap` = ?, " +
                "`cosmetics` = ?, " +
                "`selected` = ? " +
                "WHERE LOWER(`name`) = ?"
)
public class BuildBattleTable extends DataTable {

    @Override
    public void init(Database database) {
        if (database instanceof MySQLDatabase) {
            if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreBuildBattle` LIKE 'lastmap'") == null) {
                ((MySQLDatabase) database).execute(
                        "ALTER TABLE `kCoreBuildBattle` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`");
            }
        } else if (database instanceof HikariDatabase) {
            if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreBuildBattle` LIKE 'lastmap'") == null) {
                ((HikariDatabase) database).execute(
                        "ALTER TABLE `kCoreBuildBattle` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`");
            }
        }
    }

    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
        defaultValues.put("1v1wins", new DataContainer(0L));
        defaultValues.put("2v2wins", new DataContainer(0L));
        defaultValues.put("1v1games", new DataContainer(0L));
        defaultValues.put("2v2games", new DataContainer(0L));
        defaultValues.put("points", new DataContainer(0L));
        for (String key : new String[]{"wins", "games", "points"}) {
            defaultValues.put("monthly" + key, new DataContainer(0L));
        }
        defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" +
                Calendar.getInstance().get(Calendar.YEAR)));
        defaultValues.put("coins", new DataContainer(0.0D));
        defaultValues.put("lastmap", new DataContainer(0L));
        defaultValues.put("cosmetics", new DataContainer("{}"));
        defaultValues.put("selected", new DataContainer("{}"));
        return defaultValues;
    }
}