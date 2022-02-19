package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZResourceManager;

public class SPFZMenu
{
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZResourceManager resManager;
  private final SPFZMenuAction menu_action;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;

  private boolean isTraining;

  public SPFZMenu(SPFZResourceManager resManager) {

    this.resManager = resManager;
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_action = new SPFZMenuAction();
    menu_animation = new SPFZMenuAnimation(resManager.getPortraitSL(), resManager.getLandscapeSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
  }

  public SPFZMenuAction menuAct() {
    return menu_action;
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

  public String getMenuOrientation() {
    return resManager.getCurrentOrientation();
  }

  public boolean isTraining()
  {
    return isTraining;
  }

  public void setIsTraining(boolean isTraining)
  {
    this.isTraining = isTraining;
  }
}
