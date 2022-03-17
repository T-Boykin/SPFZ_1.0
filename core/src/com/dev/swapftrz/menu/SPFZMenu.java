package com.dev.swapftrz.menu;

import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.resource.SPFZResourceManager;

public class SPFZMenu
{
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZResourceManager resManager;
  private final SPFZMenuAction menu_action;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;
  private final AndroidInterfaceLIBGDX android;
  private static final int PORT_SCENE_MAX = 4;
  private int portScene = 1;

  private boolean isTraining;

  public SPFZMenu(SPFZResourceManager resManager) {

    this.resManager = resManager;
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(resManager, menu_o2d, menu_animation, menu_sound);
    android = null;
  }

  public SPFZMenu(SPFZResourceManager resManager, AndroidInterfaceLIBGDX android) {
    this.resManager = resManager;
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(resManager, menu_o2d, menu_animation, menu_sound);
    this.android = android;
  }

  /**
   * Method directs the UI to the Arcade Select screen
   */
  public void arcadeCharacterSelect() {

  }

  /**
   * Method directs the UI to the Character Select screen
   */
  public void vsCharacterSelect() {
  }

  /**
   * Method directs the UI to the Stage Select screen
   */
  public void stageSelect() {

  }

  /**
   * Method directs the UI to the Credits Section
   */
  public void enterCredits() {

  }

  /**
   * Method directs the UI back to the Main Menu screen
   */
  public void exitCredits() {

  }

  public void zoomToContributor() {

  }

  public void zoomToTechnologies() {

  }

  public void quitGame() {

  }

  public void cancelQuit() {

  }

  public void changeBrightness() {

  }

  public void back() {

  }

  public boolean isPortrait() {
    return resManager.getCurrentOrientation().equals("portrait");
  }

  public boolean isLandscape() {
    return resManager.getCurrentOrientation().equals("landscape");
  }

  public boolean isTraining() {
    return isTraining;
  }

  public void setIsTraining(boolean isTraining) {
    this.isTraining = isTraining;
  }

  public void setPortScene() {
    if (portScene == PORT_SCENE_MAX)
      portScene = 1;
    else
      portScene++;
  }

  public SPFZMenuSound sound() {
    return menu_sound;
  }

  public int portScene() {
    return portScene;
  }
}
