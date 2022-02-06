package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZResourceManager;

public class SPFZMenu
{
  private boolean isTraining;
  private static final SPFZMenuAction menu_action = new SPFZMenuAction();
  private static final SPFZMenuSound menu_sound = new SPFZMenuSound();
  private static final SPFZMenuO2DMenuObjects menu_o2d = new SPFZMenuO2DMenuObjects();
  private SPFZResourceManager resManager;
  private SPFZMenuAnimation menu_animation = new SPFZMenuAnimation(resManager.getPortraitSL(), resManager.getLandscapeSL(), menu_o2d);

  public SPFZMenu(SPFZResourceManager resManager) {
    this.resManager = resManager;
  }

  public SPFZMenuAction menuAct() {
    return menu_action;
  }

  public void arcadeCharacterSelect() {

  }

  public void vsCharacterSelect() {

  }

  public void stageSelect() {

  }

  public void enterCredits() {

  }

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
    resManager.getCurrentOrientation();
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
