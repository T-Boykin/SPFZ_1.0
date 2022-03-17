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

  }

  public void processExitButton() {
    setProcessing();
  }

  public void processYesConfirmButton() {

  }

  public void processNoConfirmButton() {
    setProcessing();
  }

  public void moveBrightnessSlider() {

  }

  public void moveSoundSlider() {

  }

  public void setMainMenuButtonComponents() {
    resManager.getCurrentSSL().addComponentsByTagName(MENU_BUTTON_TAG, SPFZButtonComponent.class);
  }

  public void setMainMenuButtonListeners() {
    if (resManager.getCurrentOrientation().equals(resManager.PORTRAIT))
    {
      resManager.rootWrapper().getChild(menu_o2d.P_ARCBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.arcadeButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.P_VSBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.versusTrainingButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.P_TRNBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.versusTrainingButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.P_OPTBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.optionsButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.P_HLPBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.helpButtonListener());

    }
    else
    {
      resManager.rootWrapper().getChild(menu_o2d.L_ARCBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.arcadeButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.L_VSBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.versusTrainingButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.L_TRNBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.versusTrainingButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.L_OPTBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.optionsButtonListener());

      resManager.rootWrapper().getChild(menu_o2d.L_HLPBUTTON).getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(menu_listeners.helpButtonListener());
    }
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
}
