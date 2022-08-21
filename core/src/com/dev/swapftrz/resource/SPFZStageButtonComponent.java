package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class SPFZStageButtonComponent implements Component {
  public boolean isTouched = false;
  private static String stageName;

  private Array<SPFZStageButtonComponent.ButtonListener> listeners = new Array<>();

  public interface ButtonListener {
    public void touchUp();

    public void touchDown();

    public void clicked();
  }

  public void addListener(SPFZStageButtonComponent.ButtonListener listener, String stageName) {
    listeners.add(listener);
    this.stageName = stageName;
  }

  public void removeListener(SPFZStageButtonComponent.ButtonListener listener) {
    listeners.removeValue(listener, true);
  }

  public void clearListeners() {
    listeners.clear();
  }

  public void setTouchState(boolean isTouched) {
    if (!this.isTouched && isTouched) {
      for (int i = 0; i < listeners.size; i++) {
        listeners.get(i).touchDown();
      }
    }
    if (this.isTouched && !isTouched) {
      for (int i = 0; i < listeners.size; i++) {
        listeners.get(i).touchUp();
        listeners.get(i).clicked();
      }
    }
    this.isTouched = isTouched;
  }

  public static String getStageName() { return stageName; }
}
