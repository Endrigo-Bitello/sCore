package dev.slasher.smartplugins.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with utilities related to {@link String}.
 */
public class StringUtils {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

  /**
   * Formats a number "#,###" using the {@link DecimalFormat}
   *
   * @param number To format.
   * @return The formatted number.
   */
  public static String formatNumber(int number) {
    return DECIMAL_FORMAT.format(number);
  }

  /**
   * Formats a number "#,###" using {@link DecimalFormat}
   *
   * @param number To format.
   * @return The formatted number.
   */
  public static String formatNumber(long number) {
    return DECIMAL_FORMAT.format(number);
  }

  /**
   * Formats a number "#,###" using {@link DecimalFormat}
   *
   * @param number To format.
   * @return The formatted number.
   */
  public static String formatNumber(double number) {
    return DECIMAL_FORMAT.format(number);
  }

  private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");

  /**
   * Removes all colors from a String.<br/>
   * Color code: §
   *
   * @param input The string to remove colors.
   * @return The uncolored string.
   */
  public static String stripColors(final String input) {
    if (input == null) {
      return null;
    }

    return COLOR_PATTERN.matcher(input).replaceAll("");
  }

  /**
   * Format the {@code and} to the color code {@code §}.
   * *
   * * @param textToFormat The string to format the colors for.
   * * @return The string with the formatted colors.
   */
  public static String formatColors(String textToFormat) {
    return translateAlternateColorCodes('&', textToFormat);
  }

  /**
   * Unformats color code {@code §} to {@code &}.
   *
   * @param textToDeFormat The string to unformat the colors.
   * @return The string with the unformatted colors.
   */
  public static String deformatColors(String textToDeFormat) {
    Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
    while (matcher.find()) {
      String color = matcher.group();
      textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("&" + color.substring(1)));
    }

    return textToDeFormat;
  }

  /**
   * Format the {@link "altColorChar"} to the color code {@code §}.
   *
   * @param altColorChar The character that is defined as the color code.
   * @param textToTranslate The string to format the colors for.
   * @return The string with the formatted colors.
   */
  public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
    Pattern pattern = Pattern.compile("(?i)(" + String.valueOf(altColorChar) + ")[0-9A-FK-OR]");

    Matcher matcher = pattern.matcher(textToTranslate);
    while (matcher.find()) {
      String color = matcher.group();
      textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("§" + color.substring(1)));
    }

    return textToTranslate;
  }

  /**
   * Gets the first color of a {@code String}.
   *
   * @param input The string to get the color.
   * @return The first color or {@code ""(empty)} if none is found.
   */
  public static String getFirstColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String first = "";
    if (matcher.find()) {
      first = matcher.group();
    }

    return first;
  }

  /**
   * Get the last color of a {@code String}.
   *
   * @param input The string to get the color.
   * @return The last color or {@code ""(empty)} if none is found.
   */
  public static String getLastColor(String input) {
    Matcher matcher = COLOR_PATTERN.matcher(input);
    String last = "";
    while (matcher.find()) {
      last = matcher.group();
    }

    return last;
  }

  /**
   * Repeats a String several times.
   *
   * @param repeat The string to repeat.
   * @param amount The amount of times to repeat.
   * @return Result of repetition.
   */
  public static String repeat(String repeat, int amount) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      sb.append(repeat);
    }

    return sb.toString();
  }

  /**
   * Joins an Array into a {@code String} using a separator.
   *
   * @param array The array to join.
   * @param index The start of the array iteration (0 = entire array).
   * @param separator The junction separator.
   * @return Result of the join.
   */
  public static <T> String join(T[] array, int index, String separator) {
    StringBuilder joined = new StringBuilder();
    for (int slot = index; slot < array.length; slot++) {
      joined.append(array[slot].toString() + (slot + 1 == array.length ? "" : separator));
    }

    return joined.toString();
  }

  /**
   * Joins an Array into a {@code String} using a separator.
   *
   * @param array The array to join.
   * @param separator The junction separator.
   * @return Result of the join.
   */
  public static <T> String join(T[] array, String separator) {
    return join(array, 0, separator);
  }

  /**
   * Join a Collection into a {@code String} using a separator.
   *
   * @param collection The collection to join.
   * @param separator The junction separator.
   * @return Result of the join.
   */
  public static <T> String join(Collection<T> collection, String separator) {
    return join(collection.toArray(new Object[collection.size()]), separator);
  }

  /**
   * Breaks a {@code String} multiple times to create lines with the defined maximum length.<br/>
   * <b>Note:</b> This method is a variation of {@link StringUtils#split(String, int, boolean)}
   * with the {@code ignoreCompleteWords} parameter set to {@code false}.
   *
   * @param toSplit String to break.
   * @param length Maximum length of lines.
   * @return Result of separation.
   */
  public static String[] split(String toSplit, int length) {
    return split(toSplit, length, false);
  }

  /**
   * "Capitalizes" a String Example: NOTCH becomes Notch
   *
   * @param toCapitalise String to capitalize
   * @return Capitalized result.
   */
  public static String capitalise(String toCapitalise) {
    StringBuilder result = new StringBuilder();

    String[] splittedString = toCapitalise.split(" ");
    for (int index = 0; index < splittedString.length; index++) {
      String append = splittedString[index];
      result.append(append.substring(0, 1).toUpperCase() + append.substring(1).toLowerCase() + (index + 1 == splittedString.length ? "" : " "));
    }

    return result.toString();
  }


  /**
   * Breaks a {@code String} multiple times to create lines with the defined maximum length.
   *
   * @param toSplit String to break.
   * @param length Maximum length of lines.
   * @param ignoreIncompleteWords Whether to ignore word breaks or not (if disabled and
   * is to break a word, it will be removed from the current line and added to the next one).
   * @return Result of separation.
   */
  public static String[] split(String toSplit, int length, boolean ignoreIncompleteWords) {
    StringBuilder result = new StringBuilder(), current = new StringBuilder();

    char[] arr = toSplit.toCharArray();
    for (int charId = 0; charId < arr.length; charId++) {
      char character = arr[charId];
      if (current.length() == length) {
        if (!ignoreIncompleteWords) {
          List<Character> removedChars = new ArrayList<>();
          for (int l = current.length() - 1; l > 0; l--) {
            if (current.charAt(l) == ' ') {
              current.deleteCharAt(l);
              result.append(current.toString() + "\n");
              Collections.reverse(removedChars);
              current = new StringBuilder(join(removedChars, ""));
              break;
            }

            removedChars.add(current.charAt(l));
            current.deleteCharAt(l);
          }

          removedChars.clear();
          removedChars = null;
        } else {
          result.append(current.toString() + "\n");
          current = new StringBuilder();
        }
      }

      current.append(current.length() == 0 && character == ' ' ? "" : character);
      if (charId + 1 == arr.length) {
        result.append(current.toString() + "\n");
      }
    }

    return result.toString().split("\n");
  }
}
