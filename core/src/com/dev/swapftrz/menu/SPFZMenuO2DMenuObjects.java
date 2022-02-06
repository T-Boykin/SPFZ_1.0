package com.dev.swapftrz.menu;

/**
 * Class contains list of all Overlap2d objects to be referenced for processing
 */
class SPFZMenuO2DMenuObjects
{
  private static final String pods = "pods", menuFader = "transition", pauseFader = "fader",
    exitDialog = "exitdialog", optDialog = "optdialog",

  //Portrait view Menu Objects
    ctrlboard = "controlboard";
  private static final String[] continueLandComponents = {"ttcimage", "swypefrmbtm", "swypefrmtop"},
    continuePortComponents = {"animcircle", "introcircle", "ttcimage"},
    landMain5Buttons = {"larcbutton", "lvsbutton", "ltrnbutton", "loptbutton", "lhlpbutton"},
    landMain3Buttons = {"soundbutton", "brightbutton", "exitbutton"},
    landMainMenuButtons = {},
    portMain5Buttons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton"},

  //Portrait view Menu Objects
    portMainMenuButtons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton", "brightnessbtn",
      "soundbutton", "exitbutton", "yes", "no", "thirtytime", "sixtytime", "ninetytime", "slidebright",
      "slidesound", "revert"};

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

  public String menuFader()
  {
    return menuFader;
  }

  public String pauseFader()
  {
    return pauseFader;
  }

  public String[] landMain3Buttons()
  {
    return landMain3Buttons;
  }

  public String[] landMain5Buttons()
  {
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
