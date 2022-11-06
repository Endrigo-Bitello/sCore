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

@DataTableInfo(name = "sBedWars",
        create = "CREATE TABLE IF NOT EXISTS `sBedWars` (`name` VARCHAR(32), `" +
                "1v1kills` LONG, `1v1deaths` LONG, `1v1games` LONG, `1v1bedsdestroyeds` LONG, `1v1bedslosteds` LONG, `1v1finalkills` LONG, `1v1finaldeaths` LONG, `1v1wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2games` LONG, `2v2bedsdestroyeds` LONG, `2v2bedslosteds` LONG, `2v2finalkills` LONG, `2v2finaldeaths` LONG, `2v2wins` LONG, `3v3kills` LONG, `3v3deaths` LONG, `3v3games` LONG, `3v3bedsdestroyeds` LONG, `3v3bedslosteds` LONG, `3v3finalkills` LONG, `3v3finaldeaths` LONG, `3v3wins` LONG, `4v4kills` LONG," +
                " `4v4deaths` LONG, `4v4games` LONG, `4v4bedsdestroyeds` LONG, `4v4bedslosteds` LONG, `4v4finalkills` LONG, `4v4finaldeaths` LONG, `4v4wins` LONG, `monthlykills` LONG, `monthlykills1v1` LONG, `monthlykills2v2` LONG, `monthlykills3v3` LONG, `monthlykills4v4` LONG, `monthlydeaths` LONG, `monthlydeaths1v1` LONG, `monthlydeaths2v2` LONG, `monthlydeaths3v3` LONG, `monthlydeaths4v4` LONG, `monthlyassists` LONG, `monthlyassists1v1` LONG, `monthlyassists2v2` LONG, `monthlyassists3v3` LONG, `monthlyassists4v4` LONG, `monthlybeds` LONG, `monthlybeds1v1` LONG, `monthlybeds2v2` LONG, `monthlybeds3v3` LONG, `monthlybeds4v4` LONG, `monthlywins` LONG, `monthlywins1v1` LONG, `monthlywins2v2` LONG, `monthlywins3v3` LONG, `monthlywins4v4` LONG, `monthlygames` LONG, `monthlygames1v1` LONG, `monthlygames2v2` LONG, `monthlygames3v3` LONG, `monthlygames4v4` LONG, `month` TEXT, `coins` DOUBLE, `xp` LONG," +
                " `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `kitconfig` TEXT, `favorites` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `sBedWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `sBedWars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `sBedWars` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1games` = ?, `1v1bedsdestroyeds` = ?, `1v1bedslosteds` = ?, `1v1finalkills` = ?, `1v1finaldeaths` = ?, `1v1wins` = ?`, `2v2kills` = ?, `2v2deaths` = ?, `2v2games` = ?, `2v2bedsdestroyeds` = ?, `2v2bedslosteds` = ?, `2v2finalkills` = ?, `2v2finaldeaths` = ?, `2v2wins` = ?`, `3v3kills` = ?, `3v3deaths` = ?, `3v3games` = ?, `3v3bedsdestroyeds` = ?, `3v3bedslosteds` = ?, `3v3finalkills` = ?, `3v3finaldeaths` = ?, `3v3wins` = ?`, `4v4kills` = ?, `4v4deaths` = ?, `4v4games` = ?, `4v4bedsdestroyeds` = ?, `4v4bedslosteds` = ?, `4v4finalkills` = ?, `4v4finaldeaths` = ?, `monthlykills` = ?, `monthlykills1v1` = ?, `monthlykills2v2` = ?, `monthlykills3v3` = ?, `monthlykills4v4` = ?, `montlhydeaths` = ?, `montlhydeaths1v1, `montlhydeaths2v2` = ?` = ?, `montlhydeaths3v3` = ?, `montlhydeaths4v4` = ?, `monthlyassists` = ?, `monthlyassists1v1` = ?, `monthlyassists2v2` = ?, `monthlyassists3v3` = ?, `monthlyassists4v4` = ?, `monthlybeds` = ?, `monthlybeds1v1` = ?, `monthlybeds2v2` = ?, `monthlybeds3v3` = ?, `monthlybeds4v4` = ?, `monthlywins` = ?, `monthlywins1v1` = ?, `monthlywins2v2` = ?, `monthlywins3v3` = ?, `monthlywins4v4` = ?, `monthlygames` = ?, `monthlygames1v1` = ?, `monthlygames2v2` = ?, `monthlygames3v3` = ?, `monthlygames4v4` = ?, `month` = ?, `4v4wins` = ?, `coins` = ?, `xp` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `kitconfig` = ? WHERE LOWER(`name`) = ?")
public class BedWarsTable extends DataTable {

    @Override
    public void init(Database database) {
        if (database instanceof MySQLDatabase) {
            if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `sBedWars` LIKE 'lastmap'") == null) {
                ((MySQLDatabase) database).execute(
                        "ALTER TABLE `sBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`, ADD `kitconfig` TEXT AFTER `selected`");
            }
        } else if (database instanceof HikariDatabase) {
            if (((HikariDatabase) database).query("SHOW COLUMNS FROM `sBedWars` LIKE 'lastmap'") == null) {
                ((HikariDatabase) database).execute(
                        "ALTER TABLE `sBedWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `favorites` TEXT AFTER `selected`, ADD `kitconfig` TEXT AFTER `selected`");
            }
        }
    }

    public Map<String, DataContainer> getDefaultValues() {
        Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
        // Poupar tempo.
        for (String stats : new String[] {"1v1", "2v2", "4v4", "3v3"}) {
            defaultValues.put(stats + "kills", new DataContainer(0L));
            defaultValues.put(stats + "deaths", new DataContainer(0L));
            defaultValues.put(stats + "games", new DataContainer(0L));
            defaultValues.put(stats + "bedsdestroyeds", new DataContainer(0L));
            defaultValues.put(stats + "bedslosteds", new DataContainer(0L));
            defaultValues.put(stats + "finalkills", new DataContainer(0L));
            defaultValues.put(stats + "finaldeaths", new DataContainer(0L));
            defaultValues.put(stats + "wins", new DataContainer(0L));
        }
        if (Database.getInstance() instanceof MySQLDatabase || Database.getInstance() instanceof HikariDatabase) {
            for (String stats : new String[] {"kills", "kills4v4", "kills3v3", "kills2v2", "kills1v1", "deaths1v1", "deaths2v2", "deaths3v3", "deaths4v4", "deaths", "assists4v4", "assists3v3", "assists2v2", "assists1v1", "assists", "beds4v4", "beds3v3", "beds2v2", "beds1v1", "beds", "wins4v4", "wins3v3", "wins2v2", "wins1v1", "wins", "games", "games1v1", "games2v2", "games3v3", "games4v4"}) {
                defaultValues.put("monthly" + stats, new DataContainer(0L));
            }
            defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)));
        }
        defaultValues.put("coins", new DataContainer(0.0D));
        defaultValues.put("xp", new DataContainer(0L));
        defaultValues.put("lastmap", new DataContainer(0L));
        defaultValues.put("cosmetics", new DataContainer("{}"));
        defaultValues.put("selected", new DataContainer("{}"));
        defaultValues.put("favorites", new DataContainer("{}"));
        defaultValues.put("kitconfig", new DataContainer("{}"));
        return defaultValues;
    }
}