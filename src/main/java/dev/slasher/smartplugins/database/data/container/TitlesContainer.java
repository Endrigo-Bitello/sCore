package dev.slasher.smartplugins.database.data.container;

import dev.slasher.smartplugins.titles.Title;
import dev.slasher.smartplugins.database.data.interfaces.AbstractContainer;
import org.json.simple.JSONArray;
import dev.slasher.smartplugins.database.data.DataContainer;

@SuppressWarnings("unchecked")
public class TitlesContainer extends AbstractContainer {

  public TitlesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void add(Title title) {
    JSONArray titles = this.dataContainer.getAsJsonArray();
    titles.add(title.getId());
    this.dataContainer.set(titles.toString());
    titles.clear();
  }

  public boolean has(Title title) {
    return this.dataContainer.getAsJsonArray().contains(title.getId());
  }
}
