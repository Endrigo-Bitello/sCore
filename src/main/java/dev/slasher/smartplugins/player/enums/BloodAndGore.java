package dev.slasher.smartplugins.player.enums;

public enum BloodAndGore {
  ATIVADO,
  DESATIVADO;

  public String getInkSack() {
    if (this == ATIVADO) {
      return "10";
    }

    return "8";
  }

  public String getName() {
    if (this == ATIVADO) {
      return "§aON";
    }

    return "§cOFF";
  }

  public BloodAndGore next() {
    if (this == DESATIVADO) {
      return ATIVADO;
    }

    return DESATIVADO;
  }

  private static final BloodAndGore[] VALUES = values();

  public static BloodAndGore getByOrdinal(long ordinal) {
    if (ordinal < 2 && ordinal > -1) {
      return VALUES[(int) ordinal];
    }

    return null;
  }
}
