package com.dev.swapftrz.stage;

import com.dev.swapftrz.resource.SPFZSceneLoader;

class SPFZStageButtonListeners
{
  private final SPFZStageAction stage_action;
  //private ItemWrapper stageWrapper;
  private final String[] stagebtntags = {"pausetag", "upbutton", "rightupbutton", "rightbutton", "downrightbutton", "downbutton",
    "downleftbutton", "leftbutton", "leftupbutton", "punch", "kick", "mmbutton", "resbutton", "button"};

  public SPFZStageButtonListeners(SPFZStageAction stage_action, SPFZSceneLoader stageSSL) {
    this.stage_action = stage_action;
    addStageComponents(stageSSL);
  }

  public void addStageComponents(SPFZSceneLoader stageSSL) {
    for (String buttontags : stagebtntags)
      stageSSL.addComponentsByTagName(buttontags, SPFZStageComponent.class);
  }

  private final SPFZStageComponent.ButtonListener pauseButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processPause();
    }
  };

  private final SPFZStageComponent.ButtonListener resumeButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processResume();
    }
  };

  private final SPFZStageComponent.ButtonListener characterSelectButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processCharacterSelect();
    }
  };

  private final SPFZStageComponent.ButtonListener mainMenuButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processMainMenu();
    }
  };

  private final SPFZStageComponent.ButtonListener yesConfirmButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processYes();
    }
  };


  private final SPFZStageComponent.ButtonListener noConfirmButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
    }

    @Override
    public void touchDown() {
    }

    @Override
    public void clicked() {
      stage_action.processNo();
    }
  };


  private final SPFZStageComponent.ButtonListener rightButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processRightButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processRightButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener upRightButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processUpRightButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processUpRightButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener downRightButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processDownRightButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processDownRightButton(true);
    }

    @Override
    public void clicked() {

    }
  };

  private final SPFZStageComponent.ButtonListener downButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processDownButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processDownButton(true);
    }

    @Override
    public void clicked() {

    }
  };

  private final SPFZStageComponent.ButtonListener downLeftButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processDownLeftButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processDownLeftButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener leftButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processLeftButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processLeftButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener upLeftButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processUpLeftButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processUpLeftButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener upButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processUpButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processUpButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener punchButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processPunchButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processPunchButton(true);
    }

    @Override
    public void clicked() {
    }
  };

  private final SPFZStageComponent.ButtonListener kickButtonListener = new SPFZStageComponent.ButtonListener()
  {
    @Override
    public void touchUp() {
      stage_action.processKickButton(false);
    }

    @Override
    public void touchDown() {
      stage_action.processKickButton(true);
    }

    @Override
    public void clicked() {
    }
  };
}
