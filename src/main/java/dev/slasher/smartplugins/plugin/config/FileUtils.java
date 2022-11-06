package dev.slasher.smartplugins.plugin.config;

import dev.slasher.smartplugins.plugin.HyPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class FileUtils {

  private HyPlugin plugin;

  public FileUtils(HyPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Deletes a file/folder.
   *
   * @param file The file to delete.
   */
  public void deleteFile(File file) {
    if (!file.exists()) {
      return;
    }

    if (file.isDirectory()) {
      Arrays.stream(file.listFiles()).forEach(this::deleteFile);
    }

    file.delete();
  }

  /**
   * Copies a file from one directory to another.
   *
   * @param in File to copy.
   * @param out Recipient to paste.
   * @param ignore Key files to ignore if a folder of files.
   */
  public void copyFiles(File in, File out, String... ignore) {
    List<String> list = Arrays.asList(ignore);
    if (in.isDirectory()) {
      if (!out.exists()) {
        out.mkdirs();
      }

      for (File file : in.listFiles()) {
        if (list.contains(file.getName())) {
          continue;
        }

        copyFiles(file, new File(out, file.getName()));
      }
    } else {
      try {
        copyFile(new FileInputStream(in), out);
      } catch (IOException ex) {
        this.plugin.getLogger().log(Level.WARNING, "An unexpected error occurred while copying the file \"" + out.getName() + "\": ", ex);
      }
    }
  }

  /**
   * Copies a file from a {@link InputStream}.
   *
   * @param input {@link InputStream} to copy.
   * @param out Recipient to paste.
   */
  public void copyFile(InputStream input, File out) {
    FileOutputStream ou = null;
    try {
      ou = new FileOutputStream(out);
      byte[] buff = new byte[1024];
      int len;
      while ((len = input.read(buff)) > 0) {
        ou.write(buff, 0, len);
      }
    } catch (IOException ex) {
      this.plugin.getLogger().log(Level.WARNING, "An unexpected error occurred while copying the file \"" + out.getName() + "\": ", ex);
    } finally {
      try {
        if (ou != null) {
          ou.close();
        }
        if (input != null) {
          input.close();
        }
      } catch (IOException ignored) {}
    }
  }
}
