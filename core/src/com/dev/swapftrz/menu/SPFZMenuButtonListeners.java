package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZCharButtonComponent;
import com.dev.swapftrz.resource.SPFZStageButtonComponent;

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
    versusButtonListener = new SPFZButtonComponent.ButtonListener() {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processVsTrainingButton(false);
    }
  };

  private final SPFZButtonComponent.ButtonListener
    trainingButtonListener = new SPFZButtonComponent.ButtonListener() {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processVsTrainingButton(true);
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
      public void clicked() { menu_action.processSprite(SPFZCharButtonComponent.getCharacter()); }
    };

  private final SPFZButtonComponent.ButtonListener okButtonListener = new SPFZButtonComponent.ButtonListener() {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      menu_action.processOkButton("");
    }
  };

  private final SPFZButtonComponent.ButtonListener backButtonListener = new SPFZButtonComponent.ButtonListener() {
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

  private final SPFZButtonComponent.ButtonListener clearButtonListener = new SPFZButtonComponent.ButtonListener() {
    @Override
    public void touchUp() { menu_action.processClearButton(false); }

    @Override
    public void touchDown() { menu_action.processClearButton(true); }

    @Override
    public void clicked() {
      menu_action.processClearButton();
    }
  };
  //TODO 9 stages
  private final SPFZStageButtonComponent.ButtonListener stageSelectButtonListener = new SPFZStageButtonComponent.ButtonListener() {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() { menu_action.processStageSelect(SPFZStageButtonComponent.getStageName()); }
  };

  public SPFZButtonComponent.ButtonListener[] main3ButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[] {brightButtonListener, soundButtonListener, exitButtonListener};
  }

  public SPFZButtonComponent.ButtonListener[] main5ButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[] {arcadeButtonListener, versusButtonListener,
      trainingButtonListener, optionsButtonListener, brightButtonListener};
  }

  public SPFZButtonComponent.ButtonListener[] altButtonListeners() {
    return new SPFZButtonComponent.ButtonListener[] {hlpBackButtonListener, thirtyButtonListener,
      sixtyButtonListener, ninetyButtonListener, brightSliderListener, soundSliderListener, yesButtonListener,
      noButtonListener, optBackButtonListener};
  }

  public SPFZButtonComponent.ButtonListener okButtonListener() { return okButtonListener; }

  public SPFZButtonComponent.ButtonListener clearButtonListener() { return clearButtonListener; }

  public SPFZStageButtonComponent.ButtonListener stageSelectButtonListener() { return stageSelectButtonListener; }
}
