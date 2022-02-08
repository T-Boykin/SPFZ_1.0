package com.dev.swapftrz.menu;

/**
 * Class contains list of all Overlap2d objects to be referenced for processing
 */
class SPFZMenuO2DMenuObjects
{
  private static final String
    //Portrait view Menu Objects
    pods = "pods",
    menuFader = "transition",
    pauseFader = "fader",
    exitDialog = "exitdialog",
    optDialog = "optdialog",
    ctrlboard = "controlboard",
    menu_screen_helpbutton = "mnuscnbutton",
    ingame_helpbutton = "ingamebutton",
    help_backbutton = "hlpbackbutton";

  private static final String[] continueLandComponents = {"ttcimage", "swypefrmbtm", "swypefrmtop"},
    continuePortComponents = {"animcircle", "introcircle", "ttcimage"},
    landMain5Buttons = {"larcbutton", "lvsbutton", "ltrnbutton", "loptbutton", "lhlpbutton"},
    landMain3Buttons = {"soundbutton", "brightbutton", "exitbutton"},
    helpOptions = {menu_screen_helpbutton, ingame_helpbutton, help_backbutton},
    landMainMenuButtons = {},

  //Portrait view Menu Objects
  portMainMenuButtons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton", "brightnessbtn",
    "soundbutton", "exitbutton", "yes", "no", "thirtytime", "sixtytime", "ninetytime", "slidebright",
    "slidesound", "revert"},
    portMain5Buttons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton"};


  public SPFZMenuO2DMenuObjects() {

  }

  public String[] continueLandComponents() {
    return continueLandComponents;
  }

  public String[] continuePortComponents() {
    return continuePortComponents;
  }

  public String pods()
  {
    return pods;
  }

  public String menuFader() {
    return menuFader;
  }

  public String pauseFader() {
    return pauseFader;
  }

  public String[] helpOptions() {
    return helpOptions;
  }

  public String[] landMain3Buttons() {
    return landMain3Buttons;
  }

  public String[] landMain5Buttons() {
    return landMain5Buttons;
  }

  public String[] landMainMenuButtons()
  {
    return landMainMenuButtons;
  }

  public String controlBoard() { return ctrlboard; }

  public String[] portMain5Buttons()
  {
    return portMain5Buttons;
  }

  public String[] portMainMenuButtons()
  {
    return portMainMenuButtons;
  }

  public String exitDialog() { return exitDialog; }

  public String optDialog() { return optDialog; }
}
