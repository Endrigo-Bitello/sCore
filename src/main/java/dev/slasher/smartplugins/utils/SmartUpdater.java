package dev.slasher.smartplugins.utils;

import dev.slasher.smartplugins.nms.NMS;
import dev.slasher.smartplugins.plugin.SPlugin;
import dev.slasher.smartplugins.plugin.logger.SLogger;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SmartUpdater {

  private SPlugin plugin;
  private SLogger logger;
  private int resourceId;
  public boolean canDownload;

  public SmartUpdater(SPlugin plugin, int resourceId) {
    this.plugin = plugin;
    this.logger = ((SLogger) this.plugin.getLogger()).getModule("UPDATER");
    this.resourceId = resourceId;
  }





  public void downloadUpdate(Player player) {
    player.sendMessage("§aTrying to download update...");

    try {
      File file = new File("plugins/sCore/update", "sCore.jar");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.smartplugins.com/download/sCore.jar").openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      int max = connection.getContentLength();
      BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
      FileOutputStream fos = new FileOutputStream(file);
      BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

      int oneChar;
      int progress = 0;
      player.sendMessage("§aFile size: " + (max / 1024) + "kb");
      while ((oneChar = in.read()) != -1) {
        bout.write(oneChar);
        progress += 1;
        int percentage = (progress * 100) / max;
        if (max > 0 && progress % 1024 == 0) {
          NMS.sendActionBar(player, "§fDownloading " + file.getName() + " §7[§a" + StringUtils.repeat("█", percentage / 4) + "§8" + StringUtils.repeat("█", 25 - (percentage / 4)) + "§7]");
        }
      }

      NMS.sendActionBar(player, "§aUpdate downloaded, stop the server to progress.");
      in.close();
      bout.close();
    } catch (Exception ex) {
      NMS.sendActionBar(player, "§aCould not download update: " + ex.getMessage());
    }
  }

  public static SmartUpdater UPDATER;

  private static String getVersion(int resourceId) {
    try {
      HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.smartplugins.store/api/v1/plugin/" + resourceId).openConnection();
      connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
      JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
      return ((JSONObject) object.get("version")).get("id").toString();
    } catch (Exception ex) {
      return null;
    }
  }
}
