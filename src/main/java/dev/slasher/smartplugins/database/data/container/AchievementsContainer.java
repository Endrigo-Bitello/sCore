package dev.slasher.smartplugins.database.data.container;

import dev.slasher.smartplugins.achievements.Achievement;
import dev.slasher.smartplugins.database.data.interfaces.AbstractContainer;
import org.json.simple.JSONArray;
import dev.slasher.smartplugins.database.data.DataContainer;

@SuppressWarnings("unchecked")
public class AchievementsContainer extends AbstractContainer {

  public AchievementsContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void complete(Achievement achievement) {
    JSONArray achievements = this.dataContainer.getAsJsonArray();
    achievements.add(achievement.getId());
    this.dataContainer.set(achievements.toString());
    achievements.clear();
  }

  public boolean isCompleted(Achievement achievement) {
    return this.dataContainer.getAsJsonArray().contains(achievement.getId());
  }
}
