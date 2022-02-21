package com.dev.swapftrz.menu;

/**
 * Class contains list of all Overlap2d objects to be referenced for processing
 */
class SPFZMenuO2DMenuObjects
{
  public static final String

    //Shared menu objects

    //Portrait view Menu Objects
    PODS = "pods",
    MENUFADER = "transition",
    PAUSEFADER = "fader",
    P_ARCBUTTON = "arcbutton",
    P_VSBUTTON = "vsbutton",
    P_TRNBUTTON = "trnbutton",
    P_OPTBUTTON = "optbutton",
    P_HLPBUTTON = "hlpbutton",
    EXITDIALOG = "exitdialog",
    OPTIONSCREEN = "optionscreen",
    CTRLBOARD = "controlboard",
    MENU_SCREEN_HELPBUTTON = "mnuscnbutton",
    IN_GAME_HELPBUTTON = "ingamebutton",
    HELP_BACKBUTTON = "hlpbackbutton",
    CHARSELHELP = "charselhelp",
    PAUSEHELP = "pausehelp",

  //Landscape view Menu Objects
  L_ARCBUTTON = "larcbutton",
    L_VSBUTTON = "lvsbutton",
    L_TRNBUTTON = "ltrnbutton",
    L_OPTBUTTON = "loptbutton",
    L_HLPBUTTON = "lhlpbutton",
    L_SOUNDBTN = "soundbutton",
    L_BRIGHTBTN = "brightbutton",
    L_EXITBTN = "exitbutton",
    OPTDIALOG = "optdialog",
    TTCIMAGE = "ttcimage",
    SWYPEFRMBTM = "swypefrmbtm",
    SWYPEFRMTOP = "swypefrmtop";

  private static final String[]
    continuePortComponents = {"animcircle", "introcircle", "ttcimage"},
    landMainMenuButtons = {},

  //Portrait view Menu Objects
  portMainMenuButtons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton", "brightnessbtn",
    "soundbutton", "exitbutton", "yes", "no", "thirtytime", "sixtytime", "ninetytime", "slidebright",
    "slidesound", "revert"},
    portMain5Buttons = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton"};


  public SPFZMenuO2DMenuObjects() {

  }

  public String[] continueLandComponents() {
    return new String[]{TTCIMAGE, SWYPEFRMBTM, SWYPEFRMTOP};
  }

  public String[] continuePortComponents() {
    return continuePortComponents;
  }

  public String[] helpOptions() {
    return new String[]{MENU_SCREEN_HELPBUTTON, IN_GAME_HELPBUTTON, HELP_BACKBUTTON};
  }

  public String[] landMain3Buttons() {
    return new String[]{L_SOUNDBTN, L_BRIGHTBTN, L_EXITBTN};
  }

  public String[] landMain5Buttons() {
    return new String[]{L_ARCBUTTON, L_VSBUTTON, L_TRNBUTTON, L_OPTBUTTON, L_HLPBUTTON};
  }

  public String[] landMainMenuButtons()
  {
    return landMainMenuButtons;
  }

  public String[] portMain5Buttons()
  {
    return portMain5Buttons;
  }

  public String[] portMainMenuButtons() {
    return portMainMenuButtons;
  }

  public String[] portMenuScreenImages() {
    return new String[]{CHARSELHELP, PAUSEHELP};
  }
}
