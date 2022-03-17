package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZButtonComponent;

public class SPFZMenuButtonListeners
{
  private final SPFZMenuAction menu_action;

  public SPFZMenuButtonListeners(SPFZMenuAction menu_action) {
    this.menu_action = menu_action;
  }

  //Listeners
  private final SPFZButtonComponent.ButtonListener
    arcadeButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processArcadeButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener
    versusTrainingButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processVsTrainingButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener
    optionsButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processOptionsButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener
    helpButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processHelpButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener brightButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {

    }

    @Override
    public void touchDown() {

    }

    @Override
    public void clicked() {

    }
  };

  private final SPFZButtonComponent.ButtonListener soundButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {

    }

    @Override
    public void touchDown() {

    }

    @Override
    public void clicked() {

    }
  };

  private final SPFZButtonComponent.ButtonListener exitButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {

    }

    @Override
    public void touchDown() {

    }

    @Override
    public void clicked() {

    }
  };

  //Clicked calls within listeners
  public SPFZButtonComponent.ButtonListener arcadeButtonListener() {
    return arcadeButtonListener;
  }

  public SPFZButtonComponent.ButtonListener versusTrainingButtonListener() {
    return versusTrainingButtonListener;
  }

  public SPFZButtonComponent.ButtonListener optionsButtonListener() {
    return optionsButtonListener;
  }

  public SPFZButtonComponent.ButtonListener helpButtonListener() {
    return helpButtonListener;
  }

  public SPFZButtonComponent.ButtonListener brightButtonListener() {
    return brightButtonListener;
  }

  public SPFZButtonComponent.ButtonListener soundButtonListener() {
    return soundButtonListener;
  }

  public SPFZButtonComponent.ButtonListener exitButtonListener() {
    return exitButtonListener;
  }
}
