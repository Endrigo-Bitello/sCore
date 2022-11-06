package dev.slasher.smartplugins.libraries;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import static java.lang.Integer.parseInt;

public class MinecraftVersion {

  private int major;
  private int minor;
  private int build;
  private int compareId;

  /**
   * Create a version from the current server<br/>
   * that can be picked up through {@link Bukkit#getServer()}
   *
   * @param server A bukkit server {@link Server}
   */
  public MinecraftVersion(Server server) {
    this(extractVersion(server));
  }

  /**
   * Create a version through the server build.<br/>
   * Build example: 1.8.R3
   *
   * @param version The server build.
   */
  public MinecraftVersion(String version) {
    int[] numbers = parseVersion(version);
    this.major = numbers[0];
    this.minor = numbers[1];
    this.build = numbers[2];
    this.compareId = parseInt(this.major + "" + this.minor + "" + this.build);
  }

  /**
   * Create a version using build numbers. <br/>
   * Ex: 1.8.R3<br/>
   * major: 1
   * minor: 8
   * build: 3
   *
   * @param major A major da versão (1)
   * @param minor A minor da versão (1.X)
   * @param build A build da versão (1.8.R"X")
   */
  public MinecraftVersion(int major, int minor, int build) {
    this.major = major;
    this.minor = minor;
    this.build = build;
    this.compareId = parseInt(major + "" + minor + "" + build);
  }

  /**
   * Check if this version is less recent or the same as requested.
   *
   * @param version The version to compare.
   * @return TRUE if less recent or equal, FALSE if more recent.
   */
  public boolean lowerThan(MinecraftVersion version) {
    return this.compareId <= version.getCompareId();
  }

  /**
   * Check if this version is newer or the same as requested.
   *
   * @param version The version to compare.
   * @return TRUE if newer or equal, FALSE if less recent.
   */
  public boolean newerThan(MinecraftVersion version) {
    return this.compareId >= version.getCompareId();
  }

  /**
   * Check if this version is less recent or equal to @param latest e<br/>
   * if newer or equal to @param olded.
   *
   * @param latest The version that needs to be less recent or equal to current.
   * @param olded The version that needs to be newer or equal to current.
   * @return TRUE if both conditions are met, FALSE otherwise.
   */
  public boolean inRange(MinecraftVersion latest, MinecraftVersion olded) {
    return (this.compareId <= latest.getCompareId()) && (this.compareId >= olded.getCompareId());
  }

  /**
   * Get the Major value of the version.
   *
   * @return Numeric Major
   */
  public int getMajor() {
    return this.major;
  }

  /**
   * Pega o valor Minor da versão.
   *
   * @return Minor númerico
   */
  public int getMinor() {
    return this.minor;
  }

  /**
   * Gets the Build value of the version.
   *
   * @return Numerical Build
   */
  public int getBuild() {
    return this.build;
  }

  /**
   * Get the version comparison value.<br/>
   * Example: 1.8.R3
   * Comparison ID: 183
   *
   * @return Comparison value
   */
  public int getCompareId() {
    return this.compareId;
  }

  private int[] parseVersion(String version) {
    String[] elements = version.split("\\.");
    int[] numbers = new int[3];

    if (elements.length <= 1 || version.split("R").length < 1) {
      throw new IllegalStateException("Corrupt MC Server version: " + version);
    }

    for (int i = 0; i < 2; i++) {
      numbers[i] = parseInt(elements[i]);
    }

    numbers[2] = parseInt(version.split("R")[1]);
    return numbers;
  }

  /**
   * Returns the version in its initial state (package build).<br/>
   * Example: 1_8_R3
   *
   * @return Original version
   */
  public String getVersion() {
    return String.format("v%s_%s_R%s", this.major, this.minor, this.build);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MinecraftVersion)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    MinecraftVersion other = (MinecraftVersion) obj;
    return this.getMajor() == other.getMajor() && this.getMinor() == other.getMinor() && this.getBuild() == other.getBuild();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getMajor(), this.getMinor(), this.getBuild());
  }

  @Override
  public String toString() {
    return String.format("%s", this.getVersion());
  }

  private static String extractVersion(Server server) {
    return extractVersion(server.getClass().getPackage().getName().split("\\.")[3]);
  }

  private static String extractVersion(String version) {
    return version.replace('_', '.').replace("v", "");
  }

  /**
   * Current version
   */
  private static MinecraftVersion currentVersion;

  /**
   * Get the version of the server that is currently running.
   *
   * @return The server version represented by a {@link MinecraftVersion}.
   */
  public static MinecraftVersion getCurrentVersion() {
    if (currentVersion == null) {
      currentVersion = new MinecraftVersion(Bukkit.getServer());
    }

    return currentVersion;
  }
}
