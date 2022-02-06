package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class SPFZCharButtonComponent implements Component
{
	public boolean isTouched = false;

  private Array<ButtonListener> listeners = new Array<ButtonListener>();

  public interface ButtonListener {
      public void touchUp();
      public void touchDown();
      public void clicked();
  }

  public void addListener(ButtonListener listener) {
      listeners.add(listener);
  }

  public void removeListener(ButtonListener listener) {
      listeners.removeValue(listener, true);
  }

  public void clearListeners() {
      listeners.clear();
  }

  public void setTouchState(boolean isTouched) {
     // if(!this.isTouched && isTouched) {
  	    if(isTouched) 
  	    {
          for(int i = 0; i < listeners.size; i++) 
          {
              listeners.get(i).touchDown();
              listeners.get(i).clicked();
          }
      }
      if(this.isTouched && !isTouched) {
          for(int i = 0; i < listeners.size; i++) {
              listeners.get(i).touchUp();
              //listeners.get(i).clicked();
          }
      }
      this.isTouched = isTouched;
  }
}
