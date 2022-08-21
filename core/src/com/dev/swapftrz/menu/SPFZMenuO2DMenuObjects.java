package com.dev.swapftrz.menu;

/**
 * Class contains list of all Overlap2d objects to be referenced for processing
 */
class SPFZMenuO2DMenuObjects
{
  public final String

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
    OPTDIALOG = "optdialog",
    TTCIMAGE = "ttcimage",
    ANIMCIRCLE = "animcircle",
    INTROCIRCLE = "introcircle",
    SWYPEFRMBTM = "swypefrmbtm",
    SWYPEFRMTOP = "swypefrmtop",
    STGONEBUTTON = "stageonebutton",
    STGTWOBUTTON = "stagetwobutton",
    STGTHREEBUTTON = "stagethreebutton",
    STGFOURBUTTON = "stagefourbutton",
    STGFIVEBUTTON = "stagefivebutton",
    STGSIXBUTTON = "stagesixbutton",
    STGSEVENBUTTON = "stagesevenbutton",
    STGEIGHTBUTTON = "stageeightbutton",
    STGNINEBUTTON = "stageninebutton",
    CLEARBUTTON = "clearbutton",
    OKBUTTON = "okaybutton",

  //Shared Menu objects
  SOUNDBTN = "soundbutton",
    BRIGHTBTN = "brightbutton",
    EXITBTN = "exitbutton",
    THIRTY = "thirtytime",
    SIXTY = "sixtytime",
    NINETY = "ninetytime",
    YESBTN = "yes",
    NOBTN = "no",
    B_SLIDER = "brightslider",
    S_SLIDER = "soundslider",
    OPTBACK = "optbackbutton";

  //Portrait view Menu Objects

  public SPFZMenuO2DMenuObjects() {
  }

  public String animCircle() {
    return ANIMCIRCLE;
  }

  public String ttcImage() {
    return TTCIMAGE;
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

  public String[] main3Buttons() {
    return new String[]{SOUNDBTN, BRIGHTBTN, EXITBTN};
  }

  public String[] portMain5Buttons() {
    return new String[]{P_ARCBUTTON, P_VSBUTTON, P_TRNBUTTON, P_OPTBUTTON, P_HLPBUTTON};
  }

  public String[] landMain5Buttons() {
    return new String[]{L_ARCBUTTON, L_VSBUTTON, L_TRNBUTTON, L_OPTBUTTON, L_HLPBUTTON};
  }

  public String[] altButtons() {
    return new String[]{HELP_BACKBUTTON, THIRTY, SIXTY, NINETY, B_SLIDER, S_SLIDER, YESBTN,
      NOBTN, OPTBACK};
  }

  public String[] landMainMenuButtons() {
    return new String[] {L_ARCBUTTON, L_VSBUTTON, L_TRNBUTTON, L_OPTBUTTON, L_HLPBUTTON,
      SOUNDBTN, BRIGHTBTN, EXITBTN, YESBTN, NOBTN, THIRTY, NINETY, B_SLIDER, S_SLIDER};
  }

  public String[] portMainMenuButtons() {
    return new String[] {P_ARCBUTTON, P_VSBUTTON, P_TRNBUTTON, P_HLPBUTTON, P_OPTBUTTON, BRIGHTBTN,
      SOUNDBTN, EXITBTN, YESBTN, NOBTN, THIRTY, SIXTY, NINETY, B_SLIDER,
      S_SLIDER, "revert"};
  }

  public String[] stageSelectButtons() {
    return new String[] {STGONEBUTTON, STGTWOBUTTON, STGTHREEBUTTON, STGFOURBUTTON, STGFIVEBUTTON, STGSIXBUTTON,
      STGSEVENBUTTON, STGEIGHTBUTTON, STGNINEBUTTON};
  }

  public String[] portMenuScreenImages() {
    return new String[] {CHARSELHELP, PAUSEHELP};
  }

  public String[] portPods() {
    return new String[] {POD_TOPRIGHT, POD_BOTTOMRIGHT, POD_TOPLEFT, POD_BOTTOM};
  }

  public String[] platformLabels() {
    return new String[] {"charonelbl", "chartwolbl", "charthreelbl", "charfourlbl",
      "charfivelbl", "charsixlbl"};
  }

  public String[] platformSelectParticles() {
    return new String[] {"firstcharpart", "secondcharpart", "thirdcharpart",
      "fourthcharpart",
      "fifthcharpart", "sixthcharpart"};
  }

  public String[] platformNullParticles() {
    return new String[] {"firstnullpart", "secondnullpart", "thirdnullpart",
      "fourthnullpart", "fifthnullpart", "sixthnullpart"};
  }

  public String[] stages() {
    return new String[] {"halloweenstage", "cathedralstage", "clubstage", "egyptstage", "futurestage",
      "gargoyle", "junglestage", "skullstage", "undergrounstage"};
  }
}
