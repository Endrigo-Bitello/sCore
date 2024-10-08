package dev.slasher.smartplugins.database.data.interfaces;

import dev.slasher.smartplugins.database.data.DataContainer;

public abstract class AbstractContainer {

  protected DataContainer dataContainer;

  public AbstractContainer(DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  public void gc() {
    this.dataContainer = null;
  }
}
