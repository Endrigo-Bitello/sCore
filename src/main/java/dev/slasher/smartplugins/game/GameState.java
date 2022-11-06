package dev.slasher.smartplugins.game;

public enum GameState {

  NENHUM, AGUARDANDO, INICIANDO, EMJOGO, ENCERRADO;
  
  public boolean canJoin() {
    return this == AGUARDANDO;
  }
}
