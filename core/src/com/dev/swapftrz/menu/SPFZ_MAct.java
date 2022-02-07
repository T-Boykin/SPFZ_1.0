package com.dev.swapftrz.menu;

/**
 * Class contains all Menu action values necessary for the Swap Fyterz UI
 */
class SPFZ_MAct
{
  public static final float ZERO = 0.0f,
    OPTION_MOVE_TIME = .7f,
    LMENU_MAIN3_SPD = .6f,
    PAUSE_BUTTON_SPEED = .15f,
    STAGE_MULTIPLIER = 1.75f,
    NORMAL_SCALE = 1f,
    SCALE_DOWN = .00001f,

  /* CAMERA MENU ACTIONS */
  ZOOM_CAMERA_IN = .40f,
    ZOOM_CAMERA_OUT = 1f,
    ZOOM_CAMERA_DURATION_IN = 2f,
    ZOOM_CAMERA_DURATION_OUT = 1f,

  /* OBJECT ACTION DURATIONS */
    FADE_DUR = .5f,
    MAIN3_DUR = .5f,
    NORM_DUR = 1f,
    TWO_DUR = 2f,

  /* DELAY SEQUENCE TIMES */

  MAIN3_SEQ = .1f,
  PMAIN5_SEQ = .2f,
  /* SCALE RESIZING */
  PMENU_MAIN5_SCALEX = 1.8f,
    PMENU_MAIN5_SCALEY = 2f,
    LSO_EXIT_BTN_SCL = .1f,
    LBRIGHT_BTN_SCL = .2f,

  /* MAIN MENU NORMAL POSITIONING */

  /* PORTRAIT */
  PMENU_CTRL_POSY = 90f,
    PMENU_MAIN5_BY_POSY = 570f,
    PMENU_EXIT_DIALOG_Y = 570f,

  /* LANDSCAPE */
  LMENU_ARCADE_POSX = 240f,
    LMENU_ARCADE_POSY = 250f,
    LMENU_ARC_OPT_HLP_BY = 200f,
    LMENU_VS_TRN_BY_XY = 250f,
    LMENU_VERSUS_POSX = 48f,
    LMENU_VS_TRN_POSY = 177f,
    LMENU_TRAINING_POSX = 433f,
    LMENU_OPTIONS_POSX = 364f,
    LMENU_OPTS_HELP_POSY = 71f,
    LMENU_HELP_POSX = 121f,
    LMENU_SOUND_POSX = 3f,
    LMENU_BRT_SND_POSY = 340f,
    LMENU_BRIGHTNESS_POSX = 74f,
    LMENU_EXIT_POSX = 572f,
    LMENU_EXIT_POSY = 333f,
    LMENU_OPTDLG_BY_POSX = 268f,
    LMENU_OPTDLG_BY_POSY = 171f,

  /* MAIN 3 ALT POSITIONING*/
  LMENU_SOUND_SHRINKX = 33f,
    LMENU_BRIGHT_SHRINKX = 103f,
    LMENU_EXIT_SHRINKX = 606f,
    LMENU_MAIN3_SHRINKY = 366f;

  public static float[]
    LAND_MAIN5_BUTTONS_X = {LMENU_ARCADE_POSX, LMENU_VERSUS_POSX, LMENU_TRAINING_POSX, LMENU_OPTIONS_POSX, LMENU_HELP_POSX},
    LAND_MAIN5_BUTTONS_Y = {LMENU_ARCADE_POSY, LMENU_VS_TRN_POSY, LMENU_VS_TRN_POSY, LMENU_OPTS_HELP_POSY, LMENU_OPTS_HELP_POSY},
    LAND_MAIN3_BUTTONS_X = {LMENU_SOUND_POSX, LMENU_BRIGHTNESS_POSX, LMENU_EXIT_POSX},
    LAND_MAIN3_BUTTONS_Y = {LMENU_BRT_SND_POSY, LMENU_BRT_SND_POSY, LMENU_EXIT_POSY},
    LAND_MAIN5_SCATTER_X = {ZERO, LMENU_VS_TRN_BY_XY, -LMENU_VS_TRN_BY_XY, -LMENU_ARC_OPT_HLP_BY, LMENU_ARC_OPT_HLP_BY},
    LAND_MAIN5_SCATTER_Y = {-LMENU_ARC_OPT_HLP_BY, -LMENU_VS_TRN_BY_XY, -LMENU_VS_TRN_BY_XY,
      LMENU_ARC_OPT_HLP_BY, LMENU_ARC_OPT_HLP_BY},
    LMENU_MAIN3_SHRINK_X = {LMENU_SOUND_SHRINKX, LMENU_BRIGHT_SHRINKX, LMENU_EXIT_SHRINKX},

  /*SCALING*/
  LAND_MAIN3_BUTTONS_SCL = {LSO_EXIT_BTN_SCL, LBRIGHT_BTN_SCL, LSO_EXIT_BTN_SCL};

  public SPFZ_MAct() {
  }
}
