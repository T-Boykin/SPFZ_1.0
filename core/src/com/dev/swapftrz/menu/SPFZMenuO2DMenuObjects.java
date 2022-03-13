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
    P_ARCBUTTON = "arcbutton",
    P_VSBUTTON = "vsbutton",
    P_TRNBUTTON = "trnbutton",
    P_OPTBUTTON = "optbutton",
    P_HLPBUTTON = "hlpbutton",
    EXITDIALOG = "exitdialog",
    OPTIONSCREEN = "optionscreen",
    CTRLBOARD = "controlboard",
    POD_TOPRIGHT = "toprightspfz",
    POD_BOTTOMRIGHT = "bottomrightspfz",
    POD_TOPLEFT = "topleftspfz",
    POD_BOTTOM = "bottomspfz",
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
    ANIMCIRCLE = "animcircle",
    INTROCIRCLE = "introcircle",
    SWYPEFRMBTM = "swypefrmbtm",
    SWYPEFRMTOP = "swypefrmtop";

  //Portrait view Menu Objects

  public SPFZMenuO2DMenuObjects() {

  }

  public String animCircle() {
    return "animcircle";
  }

  public String ttcImage() {
    return "ttcimage";
  }

  public String fader() {
    return "blackout";
  }

  public String[] continueLandComponents() {
    return new String[]{TTCIMAGE, SWYPEFRMBTM, SWYPEFRMTOP};
  }

  public String[] continuePortComponents() {
    return new String[]{ANIMCIRCLE, INTROCIRCLE, TTCIMAGE};
  }

  public String[] helpOptions() {
    return new String[]{MENU_SCREEN_HELPBUTTON, IN_GAME_HELPBUTTON, HELP_BACKBUTTON};
  }

  public String[] landMain3Buttons() {
    return new String[]{L_SOUNDBTN, L_BRIGHTBTN, L_EXITBTN};
  }

  public String[] portMain5Buttons() {
    return new String[]{P_ARCBUTTON, P_VSBUTTON, P_TRNBUTTON, P_OPTBUTTON, P_HLPBUTTON};
  }

  public String[] landMain5Buttons() {
    return new String[]{L_ARCBUTTON, L_VSBUTTON, L_TRNBUTTON, L_OPTBUTTON, L_HLPBUTTON};
  }

  public String[] landMainMenuButtons() {
    return new String[]{};
  }

  public String[] portMainMenuButtons() {
    return new String[]{"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton", "brightnessbtn",
      "soundbutton", "exitbutton", "yes", "no", "thirtytime", "sixtytime", "ninetytime", "slidebright",
      "slidesound", "revert"};
  }

  public String[] portMenuScreenImages() {
    return new String[]{CHARSELHELP, PAUSEHELP};
  }

  public String[] portPods() {
    return new String[]{POD_TOPRIGHT, POD_BOTTOMRIGHT, POD_TOPLEFT, POD_BOTTOM};
  }

}
