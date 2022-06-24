package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dev.swapftrz.SPFZState;
import com.dev.swapftrz.SwapFyterzMain;
import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZCharButtonComponent;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

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

  public void stageSel() {
    fader = root.getChild("transition").getEntity();
    Actions.addAction(fader, Actions.sequence(Actions.fadeOut(.3f)));
    update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

    root.getChild("stageonebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "halloweenstage";

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagetwobutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "cathedralstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagethreebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "clubstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagefourbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "egyptstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagefivebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "futurestage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagesixbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "gargoyle";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagesevenbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "junglestage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stageeightbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "skullstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stageninebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "undergroundstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("okaybutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          if (selectedStage == null)
          {
            // enter custom toast message here
          }
          else
          {
            Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
            {

              @Override
              public void run() {
                ok.play(1.0f);
                createstage();
              }
            })));

          }
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("backbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          back.play(1.0f);
          Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              fromss = true;

              backprocessing();
            }
          })));

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
  }

  //Pause Menu buttonlisteners
  public void pauseoptions() {

    pauseroot = new ItemWrapper(pause.getRoot());
    //transform = tc.get(pauseroot.getEntity());
    //action = ac.get(pauseroot.getEntity());

    pause.addComponentsByTagName("button", SPFZButtonComponent.class);

    if (paused)
    {
      pauseroot.getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 0f;
      pauseroot.getChild("pausemenu").getEntity().getComponent(TintComponent.class).color.a = 1f;

      pauseroot.getChild("pausemenu").getChild("resumebutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("resumebutton", true, "pausemenu"))
              {

              }
              else
              {
                resumefrmpause();
              }
            }
          }

          @Override
          public void touchDown() {
            restart = true;
            restarttime = System.currentTimeMillis();
            restarttint = pauseroot.getChild("pausemenu").getChild("resumebutton").getChild("restart").getEntity()
              .getComponent(TintComponent.class);

          }

          @Override
          public void touchUp() {
            if (restarttint.color.a <= .9f)
            {
              restart = false;
              Actions.addAction(
                pauseroot.getChild("pausemenu").getChild("resumebutton").getChild("restart").getEntity(),
                Actions.fadeOut(.01f));
            }
          }
        });
      pauseroot.getChild("pausemenu").getChild("charselbutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            // Bring up a confirmation to ensure user wants to go to character
            // select
            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("charselbutton", true, "pausemenu"))
              {

              }
              else
              {
                pmenuopt = 1;
                bringupconfirm();
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });

/*      pauseroot.getChild("pausemenu").getChild("movesetbtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked()
          {
            if (!stage.pauseconfirm)
            {
              // Bring up the moveset for each character that has been
              // selected
              // for the fight
              if (draggedfrmbtn("movesetbtn", true, "pausemenu"))
              {

              }
              else
              {

              }
            }
          }

          @Override
          public void touchDown()
          {

          }

          @Override
          public void touchUp()
          {

          }
        });*/

      pauseroot.getChild("pausemenu").getChild("mainmenubutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("mainmenubutton", true, "pausemenu"))
              {

              }
              else
              {
                pmenuopt = 2;
                bringupconfirm();
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
      pauseroot.getChild("pausemenu").getChild("yesbtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            fader = pauseroot.getChild("fader").getEntity();
            if (stage.pauseconfirm)
            {
              if (draggedfrmbtn("yesbtn", true, "pausemenu"))
              {

              }
              else
              {
                switch (pmenuopt)
                {
                  case 0:

                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        //stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        restartmatch();
                      }
                    })));

                    break;
                  case 1:
                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        //stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        toCharSel();
                      }
                    })));
                    break;
                  case 2:
                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                      }
                    })));
                    break;
                  default:
                    break;
                }
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
      pauseroot.getChild("pausemenu").getChild("nobtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (stage.pauseconfirm)
            {
              if (draggedfrmbtn("nobtn", true, "pausemenu"))
              {

              }
              else
              {
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("resumebutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("charselbutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));
                //Actions.addAction(pauseroot.getChild("pausemenu").getChild("movesetbtn").getEntity(),
                //  Actions.fadeIn(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("mainmenubutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));

                Actions.addAction(pauseroot.getChild("pausemenu").getChild("yesbtn").getEntity(),
                  Actions.fadeOut(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("nobtn").getEntity(),
                  Actions.fadeOut(PAUSE_BTN_SPD));

                stage.pauseconfirm = false;
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });

    }
    else
    {
      pauseroot.getChild("pausemenu").getEntity().getComponent(TintComponent.class).color.a = 0f;
      pauseroot.getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 1f;
      seteofbtns();
    }
  }

  //More button listeners
  public void inMode() {

    // the view will have to be in landscape therefore we will be
    // setting the landscape view up for the back button.
    root.getEntity().removeAll();
    root = new ItemWrapper(update(view).getRoot());
    main = mc.get(root.getEntity());

    update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

    // When we are at the character select scene we want to process so
    // we can
    // move forward
    // to the Stage Select scene
    //if (resourceManager.currentScene() != "arcadeselscn" && resourceManager.currentScene() != "stageselscene")
    if (resourceManager.currentScene() != "arcadeselscn" && resourceManager.currentScene() != "newstagesel")
    {
      fader = root.getChild("transition").getEntity();

      //process will determine which title will be set between Training Mode and Character Select Screen
      if (selecttype == 0)
      {
        root.getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 351;
        root.getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 420;
      }
      else
      {
        root.getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 351;
        root.getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 420;
      }

      charselIntro();
      charSel(false);
      root.getChild("okaybutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (resourceManager.currentScene() == "charselscene")
            {
              p1char1 = charsselected.get(0);
              p1char2 = charsselected.get(1);
              p1char3 = charsselected.get(2);
              p2char1 = charsselected.get(3);
              p2char2 = charsselected.get(4);
              p2char3 = charsselected.get(5);

              if (p2char3 == null)
              {

              }
              else
              {
                ok.play(1.0f);
                Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
                {

                  @Override
                  public void run() {

                    root.getEntity().removeAll();
                    ////resourceManager.currentScene() = "stageselscene";
                    //resourceManager.currentScene() = "newstagesel";

                    land = new SPFZSceneLoader(resourceManager, SwapFyterzMain.this, "", "");
                    // land.engine.removeSystem(update(view).engine.getSystem(ScriptSystem.class));
                    // land.engine.removeSystem(update(view).engine.getSystem(PhysicsSystem.class));

                    update(view).loadScene(resourceManager.currentScene(), viewportland);
                    root = new ItemWrapper(update(view).getRoot());
                    stageSel();
                  }
                })));

              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
    }
    else if (resourceManager.currentScene() == "arcadeselscn")
    {

      charSel(true);
    }

    // If a stage has been selected and "OK" has been pressed, setup for
    // the
    // stage selected
    // and set the stage run variable to true to process the camera
    // within the
    // screen

    root.getChild("backbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          back.play(1.0f);
          Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {

              state = SPFZState.RESUME;
              backprocessing();
            }
          })));

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });

  }

  //End of Fight Menu buttonlisteners
  public void seteofbtns() {
    pauseroot.getChild("endoffightmenu").getChild("pabtn").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          restartmatch();
        }
      });
    pauseroot.getChild("endoffightmenu").getChild("csbtn").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          toCharSel();
        }
      });

    pauseroot.getChild("endoffightmenu").getChild("mmbtn").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          toMenu();
        }
      });

  }


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
