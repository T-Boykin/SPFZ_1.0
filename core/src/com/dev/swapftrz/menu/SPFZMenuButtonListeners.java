package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZCharButtonComponent;

public class SPFZMenuButtonListeners
{
  private final SPFZMenuAction menu_action;

  public SPFZMenuButtonListeners(SPFZMenuAction menu_action) {
    this.menu_action = menu_action;
  }

  //MAIN MENU LISTENERS
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
      menu_action.processBrightnessButton();
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
      menu_action.processSoundButton();
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
      menu_action.processExitButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener mnuScnButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processExitButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener inGameButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processInGameButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener hlpBackButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processHlpBackButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener thirtyButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processThirtyButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener sixtyButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processSixtyButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener ninetyButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processNinetyNineButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener brightSliderListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processBrightSlider();
    }
  };

  private final SPFZButtonComponent.ButtonListener soundSliderListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processSoundSlider();
    }
  };
  private final SPFZButtonComponent.ButtonListener yesButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processYesButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener noButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processNoButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener optBackButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processExitButton();
    }
  };
  //TODO Me, Trey, Ahmed, Michael, Sayid(if he is reachable), Overlap2D
  private final SPFZButtonComponent.ButtonListener constellationButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processConstellationButton();
    }
  };

  private final SPFZButtonComponent.ButtonListener externalSupportButtonListener = new SPFZButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processExternalSupportButton();
    }
  };

  //CHARACTER SELECT LISTENERS
//TODO Zaine, Hynryck, Rappa, four, five, six, seven
  private final SPFZCharButtonComponent.ButtonListener characterSelectButtonListener =
    new SPFZCharButtonComponent.ButtonListener()
    {
      @Override
      public void touchUp() {
      }

      @Override
      public void touchDown() {
      }

      @Override
      public void clicked() {
        menu_action.processSprite();
      }
    };

  private final SPFZCharButtonComponent.ButtonListener okButtonListener = new SPFZCharButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processOkButton();
    }
  };

  private final SPFZCharButtonComponent.ButtonListener backButtonListener = new SPFZCharButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processBackButton();
    }
  };

  private final SPFZCharButtonComponent.ButtonListener clearButtonListener = new SPFZCharButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processClearButton();
    }
  };
  //TODO 9 stages
  private final SPFZCharButtonComponent.ButtonListener stageSelectButtonListener = new SPFZCharButtonComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processSprite();
    }
  };

  public SPFZButtonComponent.ButtonListener[] main3ButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[]{brightButtonListener, soundButtonListener, exitButtonListener};
  }

  public SPFZButtonComponent.ButtonListener[] main5ButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[]{arcadeButtonListener, versusTrainingButtonListener,
      versusTrainingButtonListener, optionsButtonListener, brightButtonListener};
  }

  public SPFZButtonComponent.ButtonListener[] altButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[]{hlpBackButtonListener, thirtyButtonListener,
      sixtyButtonListener, ninetyButtonListener, brightSliderListener, soundSliderListener, yesButtonListener,
      noButtonListener, optBackButtonListener};
  }
}
