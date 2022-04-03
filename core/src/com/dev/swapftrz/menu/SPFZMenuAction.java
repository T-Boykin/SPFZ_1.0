package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZButtonSystem;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;

/**
 * Class performs the button functionality for the Menu UI
 */
public class SPFZMenuAction
{
  private static final String MENU_BUTTON_TAG = "button";
  private final SPFZMenuButtonListeners menu_listeners;
  private final SPFZResourceManager resManager;
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;

  public SPFZMenuAction(SPFZResourceManager resManager, SPFZMenuO2DMenuObjects menu_o2d,
                        SPFZMenuAnimation menu_animation, SPFZMenuSound menu_sound) {

    menu_listeners = new SPFZMenuButtonListeners(this);
    this.resManager = resManager;
    this.menu_o2d = menu_o2d;
    this.menu_animation = menu_animation;
    this.menu_sound = menu_sound;
  }
  //UI component BUTTON FUNCTIONALITY

  public void setMainMenuButtonComponents() {
    resManager.getCurrentSSL().addComponentsByTagName(MENU_BUTTON_TAG, SPFZButtonComponent.class);
  }

  public void setMainMenuButtonListeners() {
    SPFZButtonComponent.ButtonListener[] main3ButtonListeners = menu_listeners.main3ButtonListeners();
    SPFZButtonComponent.ButtonListener[] main5ButtonListeners = menu_listeners.main5ButtonListeners();
    String[] main3Buttons = menu_o2d.main3Buttons();
    String[] main5Buttons = resManager.getCurrentOrientation().equals(resManager.PORTRAIT)
      ? menu_o2d.portMain5Buttons() : menu_o2d.landMain5Buttons();

    if (resManager.getCurrentOrientation().equals(resManager.PORTRAIT))
    {
      for (int i = 0; i < main3Buttons.length; i++)
        resManager.rootWrapper().getChild(menu_o2d.CTRLBOARD).getChild(menu_o2d.main3Buttons()[i]).getEntity()
          .getComponent(SPFZButtonComponent.class).addListener(main3ButtonListeners[i]);
    }
    else
    {
      for (int i = 0; i < main3Buttons.length; i++)
        resManager.rootWrapper().getChild(main3Buttons[i]).getEntity()
          .getComponent(SPFZButtonComponent.class).addListener(main3ButtonListeners[i]);
    }

    for (int i = 0; i < main5Buttons.length; i++)
      resManager.rootWrapper().getChild(main5Buttons[i]).getEntity()
        .getComponent(SPFZButtonComponent.class).addListener(main5ButtonListeners[i]);

  }

  public SPFZSceneLoader currentSSL() {
    return resManager.getCurrentSSL();
  }

  public void setProcessing() {
    resManager.getCurrentSSL().engine.getSystem(SPFZButtonSystem.class).setProcessing(true);
  }

  //ACTION RUNNABLES

  public void goToCharacterSelect() {
  }

  public void processArcadeButton() {
    setProcessing();
  }

  public void processVsTrainingButton() {
    setProcessing();
  }

  public void processHelpButton() {
    setProcessing();
    menu_sound.playConfirmSound();
  }

  public void processOptionsButton() {
    setProcessing();
  }

  public void processSoundButton() {
  }

  public void processBrightnessButton() {
    /*//get amount from properties file int brightamount = 0;
    brightamount += 51;
    if (brightamount >= 255)
    {
      brightamount = 51;
    }
    else if (brightamount < 51)
    {
      brightamount = 51;
    }*/
    //saveSettings in ResourceManager
    //adjustBrightness(brightamount);
  }

  public void processExitButton() {
    setProcessing();
  }

  public void processYesConfirmButton() {
  }

  public void processNoConfirmButton() {
    setProcessing();
  }

  public void processMnuScnButton() {
  }

  public void processInGameButton() {
  }

  public void processHlpBackButton() {
  }

  public void processThirtyButton() {
  }

  public void processSixtyButton() {
  }

  public void processNinetyButton() {
  }

  public void processBrightSlider() {
  }

  public void processSoundSlider() {
  }

  public void processConstellationButton() {
  }

  //CHARACTER AND STAGE SELECT MENU ACTIONS

  public void processOkButton() {
  }

  public void processBackButton() {
  }

  public void processClearButton() {
  }

  public void processSprite() {
    //if Arcade
    //setcharsprite(arrayOfChar?)
    //else
  }

  //STAGE SELECT

  public void processStageSelect() {
  }
}
