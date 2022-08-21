package com.dev.swapftrz.menu;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.dev.swapftrz.resource.SPFZO2DMethods;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.MoveByData;
import com.uwsoft.editor.renderer.systems.action.data.MoveToData;
import com.uwsoft.editor.renderer.systems.action.data.ScaleToData;
import com.uwsoft.editor.renderer.systems.action.logic.ScaleToAction;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.Arrays;

//TODO Find a way to take in information coming in from components and use them within defined actions

/**
 * Class handles the bulk of animations for the menu screens
 */
public class SPFZMenuAnimation extends SPFZO2DMethods
{
  private final SPFZSceneLoader portrait, landscape;
  private final SPFZMenuO2DMenuObjects menuo2d;
  private final SPFZMenu spfzmenu;
  private final ComponentMapper<ActionComponent> ac = ComponentMapper.getFor(ActionComponent.class);
  private final ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);
  private ItemWrapper portRoot, landRoot;
  private ActionComponent action;
  private TransformComponent transAction;

  public SPFZMenuAnimation(SPFZMenu spfzmenu, SPFZSceneLoader portrait, SPFZSceneLoader landscape,
                           SPFZMenuO2DMenuObjects menuo2d) {
    this.portrait = portrait;
    this.landscape = landscape;
    this.menuo2d = menuo2d;
    this.spfzmenu = spfzmenu;
  }

  public ActionComponent ActRoot() {
    return action;
  }

  public TransformComponent TransRoot() {
    return transAction;
  }

  public void PrepareUIComponents(ActionComponent act, TransformComponent tform, ItemWrapper root) {
    setActionRoot(root);
    setTransformRoot(root);
  }

  public void setActionRoot(ItemWrapper root) {
    action = ac.get(root.getEntity());
  }

  public void setTransformRoot(ItemWrapper root) {
    transAction = tc.get(root.getEntity());
  }

  //ANIMATION COMMAND METHODS --------------------------------------------------------------///////

  /**
   * Landscape Main Menu opening animation
   */
  public void landscapeMenuAnimation() {
    hideO2dObjects(landRoot, menuo2d.continueLandComponents());
    rmvO2dObjects(landscape, getLandRoot(), menuo2d.continueLandComponents());
    faderOutPlusAction(getLandRoot(), landAnimationRunnable());
  }

  public void portraitMenuAnimation() {
    hideO2dObjects(portRoot, menuo2d.continuePortComponents());
    rmvO2dObjects(portrait, getPortRoot(), menuo2d.continuePortComponents());
    portButtonsToMainCenter();
  }

  //ANIMATION COMMAND METHODS --------------------------------------------------------------\\\\\\\

  public void landRemove(String object) {
  }

  public ItemWrapper getPortRoot() {
    return portRoot;
  }

  public void setPortRoot(ItemWrapper portRoot) {
    this.portRoot = portRoot;
  }

  public ItemWrapper getLandRoot() {
    return landRoot;
  }

  public void setLandRoot(ItemWrapper landRoot) {
    this.landRoot = landRoot;
  }

  //UI component MOVEMENT ACTIONS

  public void openHelpOptions() {
    for (int i = 0; i < menuo2d.helpOptions().length; i++)
      Actions.addAction(portRoot.getChild(menuo2d.helpOptions()[i]).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(-SPFZ_MAct.PORT_HOPTS_BUTTONS_X[i],
          0, SPFZ_MAct.PORT_HOPTS_SCALE_X[i], 0, 0, SPFZ_MAct.OPTION_MOVE_TIME, null, null)));
  }

  public void closeHelpOptions() {
    for (int i = 0; i < menuo2d.helpOptions().length; i++)
      Actions.addAction(portRoot.getChild(menuo2d.helpOptions()[i]).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(SPFZ_MAct.PORT_HOPTS_BUTTONS_X[i],
          0, 0, 0, 0, SPFZ_MAct.OPTION_MOVE_TIME, null, null)));
  }

  public void openMenuScreenScreens() {
    for (int i = 0; i < menuo2d.portMenuScreenImages().length; i++)
      Actions.addAction(portRoot.getChild(menuo2d.portMenuScreenImages()[i]).getEntity(),
        scaleO2dObj(SPFZ_MAct.PMENU_MNUSCN_SCNSX, 0, 0, SPFZ_MAct.OPTION_MOVE_TIME * .33f, null));
  }

  public void closeMenuScreenScreens() {
    for (int i = 0; i < menuo2d.portMenuScreenImages().length; i++)
      Actions.addAction(portRoot.getChild(menuo2d.portMenuScreenImages()[i]).getEntity(),
        scaleO2dObj(-SPFZ_MAct.PMENU_MNUSCN_SCNSX, 0, 0, SPFZ_MAct.OPTION_MOVE_TIME * .33f, null));
  }


  public void showExitDialog() {
    float dialogY = 0;

    if (spfzmenu.isPortrait())
      dialogY = SPFZ_MAct.PMENU_EXIT_DIALOG_Y;
    else
      dialogY = SPFZ_MAct.LMENU_EXIT_DIALOG_Y;

    Actions.addAction(portRoot.getChild(menuo2d.EXITDIALOG).getEntity(), moveO2dObjBy(0, dialogY,
      SPFZ_MAct.TWO_DUR, Interpolation.swing));
  }

  public void lowerExitDialog() {
    float dialogY = 0;

    if (spfzmenu.isPortrait())
      dialogY = -SPFZ_MAct.PMENU_EXIT_DIALOG_Y;
    else
      dialogY = -SPFZ_MAct.LMENU_EXIT_DIALOG_Y;

    Actions.addAction(portRoot.getChild(menuo2d.EXITDIALOG).getEntity(), moveO2dObjBy(0, dialogY,
      SPFZ_MAct.TWO_DUR, Interpolation.swing));
  }

  public void podsTowardsScreen() {
    Actions.addAction((portRoot.getChild(menuo2d.PODS)).getEntity(), Actions.parallel(moveToAndScaleO2dObj(0,
      0, SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE, 0, SPFZ_MAct.NORM_DUR, null, null)));
  }

  public void landPodsFlyOut() {
    Actions.addAction((portRoot.getChild(menuo2d.PODS)).getEntity(),
      Actions.sequence(Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LMENU_PODS_FLY_XY, SPFZ_MAct.LMENU_PODS_FLY_XY,
          SPFZ_MAct.NORMAL_SCALE * SPFZ_MAct.LMENU_PODS_FLY, SPFZ_MAct.NORMAL_SCALE * SPFZ_MAct.LMENU_PODS_FLY,
          0, SPFZ_MAct.NORM_DUR, null, null)),
        Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LANDWIDTH_PORTHEIGHT * .5f, SPFZ_MAct.LANDHEIGHT_PORTWIDTH * .5f, 0,
          0, 0, 0, null, null))));
  }

  public void portFlyPods() {
    //get the sceneSelection integer
    int scene = spfzmenu.portScene();
    String[] pods = Arrays.copyOfRange(menuo2d.portPods(), scene - 1, menuo2d.portPods().length - 1);
    float[][] podXY = SPFZ_MAct.portPodsXY()[scene];
    float sequenceDelay = 0, addDelay = SPFZ_MAct.POD_SEQ, duration = SPFZ_MAct.POD_DUR;

    for (int i = 0; i < pods.length; i++)
    {
      Actions.addAction(portRoot.getChild(pods[i]).getEntity(), moveO2dObjTo(podXY[i][0], podXY[i][1],
        duration + sequenceDelay, Interpolation.linear));

      sequenceDelay += addDelay;
    }
  }

  /**
   * Push portrait UI buttons Below the Main Center
   */
  public void portButtonsBelowMain() {
    float sequenceDelay = 0, addDelay = SPFZ_MAct.PMAIN5_SEQ;
    float main5posY = SPFZ_MAct.PMENU_MAIN5_BY_POSY, scaleX = SPFZ_MAct.PMENU_MAIN5_SCALEX,
      scaleY = SPFZ_MAct.PMENU_MAIN5_SCALEY, duration = SPFZ_MAct.NORM_DUR;

    Actions.addAction(portRoot.getChild(menuo2d.CTRLBOARD).getEntity(), moveO2dObjBy(0,
      -SPFZ_MAct.PMENU_CTRL_POSY, SPFZ_MAct.NORM_DUR, null));

    for (int i = 0; i < menuo2d.portMain5Buttons().length; i++)
    {
      Actions.addAction(portRoot.getChild(menuo2d.portMain5Buttons()[i]).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(0, -main5posY, scaleX, scaleY, 0,
          duration + sequenceDelay, null, null)));

      sequenceDelay += addDelay;
    }
  }

  /**
   * Bring portrait UI buttons to the Main Center
   * <p>
   * ACTIONS:
   * 1. MoveBy on ControlBoard
   * 2. MoveBy and ScaleTo on Main portrait 5 Main Menu buttons in delay sequence
   */
  public void portButtonsToMainCenter() {
    float sequenceDelay = 0, addDelay = SPFZ_MAct.PMAIN5_SEQ;
    float main5posY = SPFZ_MAct.PMENU_MAIN5_BY_POSY, scaleX = SPFZ_MAct.PMENU_MAIN5_SCALEX,
      scaleY = SPFZ_MAct.PMENU_MAIN5_SCALEY, duration = SPFZ_MAct.NORM_DUR;

    Actions.addAction(portRoot.getChild(menuo2d.CTRLBOARD).getEntity(),
      moveO2dObjBy(0, SPFZ_MAct.PMENU_CTRL_POSY, SPFZ_MAct.NORM_DUR, null));

    for (int i = 0; i < menuo2d.portMain5Buttons().length; i++)
    {
      Actions.addAction(portRoot.getChild(menuo2d.portMain5Buttons()[i]).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(0, main5posY, scaleX, scaleY, 0,
          duration + sequenceDelay, null, null)));

      sequenceDelay += addDelay;
    }
  }

  /**
   * Bring portrait UI buttons in to the Main Center
   */
  public void portButtonsBringIn() {
  }

  /**
   * Scatter landscape UI buttons
   */
  public void landButtonsScatter() {
    for (int i = 0; i < menuo2d.landMain5Buttons().length; i++)
    {
      Actions.addAction(landRoot.getChild(menuo2d.landMain5Buttons()[i]).getEntity(),
        Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LAND_MAIN5_SCATTER_X[i],
          SPFZ_MAct.LAND_MAIN5_SCATTER_Y[i], SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE,
          0, SPFZ_MAct.OPTION_MOVE_TIME, null, null)));
    }

    shrinkLandMain3Buttons();
  }

  /**
   * Bring landscape UI buttons in to the Main Center
   */
  public void landButtonsBringIn() {
    for (int i = 0; i < menuo2d.landMain5Buttons().length; i++)
    {
      Actions.addAction(landRoot.getChild(menuo2d.landMain5Buttons()[i]).getEntity(),
        Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LAND_MAIN5_BUTTONS_X[i],
          SPFZ_MAct.LAND_MAIN5_BUTTONS_Y[i], SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE,
          0, SPFZ_MAct.OPTION_MOVE_TIME, null, null)));
    }

    expandLandMain3Buttons();
  }

  public void openOptions() {
    if (spfzmenu.isPortrait())
      Actions.addAction(portRoot.getChild(menuo2d.OPTIONSCREEN).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(-SPFZ_MAct.PMENU_OPTSCN_POSX,
          0, SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE, 0, SPFZ_MAct.OPTION_MOVE_TIME,
          Interpolation.linear, Interpolation.linear)));
    else
      Actions.addAction(landRoot.getChild(menuo2d.OPTDIALOG).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(-SPFZ_MAct.LMENU_OPTDLG_BY_POSX, -SPFZ_MAct.LMENU_OPTDLG_BY_POSY,
          0, 0, 0, SPFZ_MAct.NORM_DUR, Interpolation.linear,
          Interpolation.linear)));
  }

  public void closeOptions() {
    if (spfzmenu.isPortrait())
      Actions.addAction(portRoot.getChild(menuo2d.OPTIONSCREEN).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(SPFZ_MAct.PMENU_OPTSCN_POSX,
          0, 0, SPFZ_MAct.NORMAL_SCALE, 0, SPFZ_MAct.OPTION_MOVE_TIME,
          Interpolation.linear, Interpolation.linear)));
    else
      Actions.addAction(landRoot.getChild(menuo2d.OPTDIALOG).getEntity(),
        Actions.parallel(moveByAndScaleO2dObj(SPFZ_MAct.LMENU_OPTDLG_BY_POSX, SPFZ_MAct.LMENU_OPTDLG_BY_POSY,
          0, 0, 0, SPFZ_MAct.NORM_DUR, Interpolation.linear,
          Interpolation.linear)));
  }

  /**
   * Expand the brightness, sound, and exit buttons
   */
  private void expandLandMain3Buttons() {
    float sequenceDelay = 0, addDelay = SPFZ_MAct.MAIN3_SEQ;
    String[] arrObjs = menuo2d.main3Buttons();

    for (int i = 0; i < arrObjs.length; i++)
    {
      Actions.addAction(landRoot.getChild(arrObjs[i]).getEntity(),
        Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LAND_MAIN3_BUTTONS_X[i],
          SPFZ_MAct.LAND_MAIN3_BUTTONS_Y[i], SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE,
          SPFZ_MAct.LAND_MAIN3_BUTTONS_SCL[i], SPFZ_MAct.MAIN3_DUR + sequenceDelay, null, null)));

      sequenceDelay += addDelay;
    }
  }

  /**
   * Shrink the brightness, sound, and exit buttons
   */
  private void shrinkLandMain3Buttons() {
    float sequenceDelay = 0, addDelay = SPFZ_MAct.MAIN3_SEQ;
    String[] arrObjs = menuo2d.main3Buttons();

    for (int i = 0; i < arrObjs.length; i++)
    {
      Actions.addAction(landRoot.getChild(arrObjs[i]).getEntity(),
        Actions.parallel(moveToAndScaleO2dObj(SPFZ_MAct.LMENU_MAIN3_SHRINK_X[i], SPFZ_MAct.LMENU_MAIN3_SHRINKY,
          0, 0, 0, SPFZ_MAct.MAIN3_DUR + sequenceDelay, null, null)));

      sequenceDelay += addDelay;
    }
  }

  /**
   * Close the options dialog and disable buttons from dialog
   */
  public void closeOptionsDialog() {
    Actions.addAction(landRoot.getChild(menuo2d.OPTDIALOG).getEntity(),
      Actions.parallel(moveByAndScaleO2dObj(SPFZ_MAct.LMENU_OPTDLG_BY_POSX, SPFZ_MAct.LMENU_OPTDLG_BY_POSY,
        0, 0, 0, SPFZ_MAct.NORM_DUR, Interpolation.linear,
        Interpolation.linear)));
  }

  //TRANSPARENCY/REMOVAL ACTIONS


  //COMBINED/CUSTOM ACTIONS

  /**
   * MoveTo and ScaleTo Overlap2D Object
   * Interpolation is fade by default unless specified otherwise
   *
   * @param toXVal             - to X value given
   * @param toYVal             - to Y value given
   * @param scaleXVal          - scaleTo X Value
   * @param scaleYVal          - scaleTo Y Value
   * @param scaleResize        - resizing to add to both scaleX and scaleY
   * @param duration           - time of action
   * @param moveInterpolation  - interpolation to be set for movement
   * @param scaleInterpolation - interpolation to be set for scaling
   */
  private ActionData[] moveToAndScaleO2dObj(float toXVal, float toYVal, float scaleXVal,
                                            float scaleYVal, float scaleResize,
                                            float duration, Interpolation moveInterpolation,
                                            Interpolation scaleInterpolation) {
    if (moveInterpolation == null)
      moveInterpolation = Interpolation.fade;
    if (scaleInterpolation == null)
      moveInterpolation = Interpolation.fade;

    MoveToData moveActionData = new MoveToData(moveInterpolation, duration, toXVal, toYVal);
    ScaleToData scaleActionData = new ScaleToData(scaleInterpolation, duration, scaleXVal + scaleResize,
      scaleYVal + scaleResize);
    moveActionData.logicClassName = MoveToAction.class.getName();
    scaleActionData.logicClassName = ScaleToAction.class.getName();

    return new ActionData[]{moveActionData, scaleActionData };
  }

  /**
   * Move and Scale Overlap2D Objects By specified values in delayed sequence
   * less than 1 second should be ideal
   *
   * @param byXVal    - to X value given
   * @param byYVal    - to Y value given
   * @param scaleXVal - scaleTo X Value
   * @param scaleYVal - scaleTo Y Value
   * @param scaleResize - additional resizing to add to both scaleX and scaleY
   * @param duration  - time of action
   * @param moveInterpolation - interpolation to be set for movement
   * @param scaleInterpolation - interpolation to be set for scaling
   */
  private ActionData[] moveByAndScaleO2dObj(float byXVal, float byYVal, float scaleXVal,
                                            float scaleYVal, float scaleResize,
                                            float duration, Interpolation moveInterpolation,
                                            Interpolation scaleInterpolation) {
    if (moveInterpolation == null)
      moveInterpolation = Interpolation.fade;
    if (scaleInterpolation == null)
      moveInterpolation = Interpolation.fade;

    MoveByData moveActionData = new MoveByData(moveInterpolation, duration, byXVal, byYVal);
    ScaleToData scaleActionData = new ScaleToData(scaleInterpolation, duration, scaleXVal + scaleResize,
      scaleYVal + scaleResize);
    moveActionData.logicClassName = MoveByAction.class.getName();
    scaleActionData.logicClassName = ScaleToAction.class.getName();

    return new ActionData[]{moveActionData, scaleActionData};
  }

  public void faderInPlusAction(ItemWrapper rootIW, Runnable run) {
    Actions.addAction(rootIW.getChild(menuo2d.fader()).getEntity(), Actions.sequence(fadeInO2dObject(SPFZ_MAct.FADE_DUR, null),
      Actions.run(run)));
  }

  public void faderOutPlusAction(ItemWrapper rootIW, Runnable run) {
    Actions.addAction(rootIW.getChild(menuo2d.fader()).getEntity(), Actions.sequence(fadeOutO2dObject(SPFZ_MAct.FADE_DUR, null),
      Actions.run(run)));
  }

  public void fadeInAndOutPlusAction(ItemWrapper rootIW, Runnable run) {
    Actions.addAction(rootIW.getChild(menuo2d.fader()).getEntity(), Actions.sequence(fadeInO2dObject(SPFZ_MAct.FADE_DUR, null),
      Actions.run(run), fadeOutO2dObject(SPFZ_MAct.FADE_DUR, null)));
  }

  //CUSTOM ANIMATION RUNNABLES

  /**
   * Game introduction Animation
   * <p>
   * 1. Tap to continue is hidden
   * 2. Blackout is hidden to show 1st circle and Main Menu music starts at the same time
   * 3.
   */
  public Runnable spfzIntroduction(boolean isLandscape) {
    Runnable runnable, startRunnable, soundRunnable,
      animCircleRunnable, introCircleRunnable, flashTTCRunnable;
    //TODO this needs to be handled better
    float delay1 = 1.5f, fade = SPFZ_MAct.FADE_DUR, delay2 = delay1 + fade,
      delay3 = delay1 + delay2 + fade;

    if (isLandscape)
      setLandRoot(spfzmenu.rootEntityWrapper());
    else
      setPortRoot(spfzmenu.rootEntityWrapper());

    startRunnable = () -> {
      Actions.addAction(portRoot.getChild(menuo2d.ttcImage()).getEntity(),
        fadeOutO2dObject(0, null));
    };

    soundRunnable = () -> {
      Actions.addAction(portRoot.getChild(menuo2d.fader()).getEntity(),
        Actions.parallel(fadeOutO2dObject(0, null), Actions.run(spfzmenu.sound().playMainMenuLoopMusic())));

    };

    animCircleRunnable = () -> {
      ActionData[] actionData = moveByAndScaleO2dObj(0, 0, SPFZ_MAct.PMENU_ANIMCIRCLE_SCL, SPFZ_MAct.PMENU_ANIMCIRCLE_SCL, 0,
        SPFZ_MAct.ANIMCIRCLE_DUR, null, null);

      Actions.addAction(portRoot.getChild(menuo2d.animCircle()).getEntity(),
        Actions.sequence(delayO2dObject(delay1), Actions.parallel(actionData[0], actionData[1],
          fadeOutO2dObject(SPFZ_MAct.FADE_DUR, null))));
    };

    introCircleRunnable = () -> {
      ActionData[] actionData = moveByAndScaleO2dObj(0, 0, SPFZ_MAct.PMENU_INTROCIRCLE_SCL,
        SPFZ_MAct.PMENU_INTROCIRCLE_SCL, 0, SPFZ_MAct.INTROCIRCLE_DUR, null, null);

      Actions.addAction(portRoot.getChild(menuo2d.animCircle()).getEntity(),
        Actions.sequence(delayO2dObject(delay2), Actions.parallel(actionData[0], actionData[1])));
    };

    flashTTCRunnable = () -> {
      Actions.addAction(portRoot.getChild(menuo2d.ttcImage()).getEntity(),
        Actions.sequence(delayO2dObject(delay3),
          Actions.sequence(flashO2dObject(SPFZ_MAct.TTC_REPS, SPFZ_MAct.TTC_DUR, false, false))));
    };

    runnable = () -> {
      for (Runnable run : Arrays.asList(startRunnable, soundRunnable, animCircleRunnable,
        introCircleRunnable, flashTTCRunnable))
      {
        run.run();
      }
    };

    return runnable;
  }

  /**
   * Move all main menu landscape buttons into position, fadeout the black screen
   * create zoom in zoom out effect with buttons
   *
   * @return - RunnableAction for land animation
   */
  private Runnable landAnimationRunnable() {
    return () -> {
      //Assign action to each of the Main 5 main menu buttons
      for (int i = 0; i < menuo2d.landMain5Buttons().length; i++)
      {
        Actions.addAction(landRoot.getChild(menuo2d.landMain5Buttons()[i]).getEntity(),
          moveO2dObjTo(SPFZ_MAct.LAND_MAIN5_BUTTONS_X[i],
            SPFZ_MAct.LAND_MAIN5_BUTTONS_Y[i], SPFZ_MAct.OPTION_MOVE_TIME, null));
      }

      landButtonsBringIn();
      moveToAndScaleO2dObj(0, 0, SPFZ_MAct.NORMAL_SCALE, SPFZ_MAct.NORMAL_SCALE,
        0, SPFZ_MAct.OPTION_MOVE_TIME, null, null);
    };
  }

  /**
   * method runs credit hint process every 5 seconds
   * credit hint timer is canceled after user enters credits
   * or opens the option/help/exit screen
   */
  public void processCreditsHint() {
    creditsHintAnimation();
  }

  public void processLights() {
    Entity mainlight = landRoot.getChild("mainlight").getEntity();

    if (spfzmenu.isLandscape())
    {
      if (mainlight.getComponent(TransformComponent.class).x < spfzmenu.camera().viewportWidth * .5f)
        Actions.addAction(mainlight, moveO2dObjTo(spfzmenu.camera().viewportWidth * .75f,
          spfzmenu.camera().viewportHeight / .5f, 1f, null));
      else if (mainlight.getComponent(TransformComponent.class).x >= spfzmenu.camera().viewportWidth * .5f)
        Actions.addAction(mainlight, moveO2dObjTo(spfzmenu.camera().viewportWidth * .25f,
          spfzmenu.camera().viewportHeight / .5f, 1f, null));
    }
  }

  /**
   * method fades the credits and arrows in and out
   */
  private void creditsHintAnimation() {
    String[] cdtcomponents = {"swypefrmbtm", "swypefrmtop"};

    // move the credit text and arrows downwards
    if (spfzmenu.isLandscape())
    {
      // move the credit text and arrows downwards
      Actions.addAction(landRoot.getChild(cdtcomponents[1]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
        Actions.parallel(Actions.moveBy(0, -20f, 1f), Actions.fadeOut(1f)), Actions.moveBy(0, 20f, .01f)));

      // move the credit text and arrows upwards
      Actions.addAction(landRoot.getChild(cdtcomponents[0]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
        Actions.parallel(Actions.moveBy(0, 20f, 1f), Actions.fadeOut(1f)), Actions.moveBy(0, -20f, .01f)));

      /*for (int i = 1; i < cdtcomponents.length - 3; i++)
      {
        Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
          Actions.parallel(Actions.moveBy(0, -20f, .5f), Actions.fadeOut(.5f)), Actions.moveBy(0, 20f, .01f)));
      }

      // move the to menu text and arrows upwards
      for (int i = 2; i < cdtcomponents.length; i++)
      {
        Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
          Actions.parallel(Actions.moveBy(0, 20f, .5f), Actions.fadeOut(.5f)), Actions.moveBy(0, -20f, .01f)));
      }*/

      /*if (!credpress)
      {
        Actions.addAction(landRoot.getChild("meconst").getChild("medraw").getEntity(),
          Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
      }*/

    }
    else
    {
      Actions.addAction(portRoot.getChild("tocreditsone").getEntity(),
        Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
      Actions.addAction(portRoot.getChild("tocreditstwo").getEntity(),
        Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
    }
  }


  public void animsel(Entity entity) {
    float DURATION = .1f;
    float scaleY = .43f;
    TransformComponent transform = new TransformComponent();
    DimensionsComponent dimension = new DimensionsComponent();
    ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);
    ComponentMapper<DimensionsComponent> dc = ComponentMapper.getFor(DimensionsComponent.class);
    transform = tc.get(entity);
    dimension = dc.get(entity);


    float origX = transform.scaleX;
    float origY = transform.scaleY;

    float increase = origY + (origY * .5f);


    Actions.addAction(entity,
      Actions.sequence(Actions.parallel(Actions.scaleTo(origX, increase, DURATION, Interpolation.swing)),
        Actions.scaleTo(origX, origY, DURATION, Interpolation.swing)));
  }

  //Character select opening animation
  public void charselIntro() {
    String[] slides = {"translideone", "translidetwo", "translidethree", "translidefour", "translidefive",
      "translidesix", "translideseven", "translideeight", "translidenine", "translideten", "translideeleven",
      "translidetwelve"};
    // Character Select Transition
    //transform = tc.get(rootEntityWrapper().getEntity());
    //action = ac.get(rootEntityWrapper().getEntity());

    for (int i = 0; i < 4; i++) {
      Actions.addAction(getLandRoot().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(800f, -800f, 1f),
        Actions.sequence(Actions.color(Color.WHITE, .3f), Actions.color(Color.BLACK, .3f))));
    }
    for (int i = 4; i < 8; i++) {
      Actions.addAction(getLandRoot().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(-800f, -800f, 1f),
        Actions.sequence(Actions.color(Color.BLACK, .3f), Actions.color(Color.RED, .3f))));
    }

    for (int i = 8; i < 12; i++) {
      Actions.addAction(getLandRoot().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(0, 800f, 1f),
        Actions.sequence(Actions.color(Color.RED, .3f), Actions.color(Color.WHITE, .3f))));
    }


    Actions.addAction(getLandRoot().getChild("mainslide").getEntity(), Actions.fadeOut(1.5f));
    // Actions.addAction(rootEntityWrapper().getChild("mainslide").getEntity(), Actions.sequence(Actions.color(Color.WHITE, .3f)));
    //   Actions.fadeOut(1.5f)));
  }

  public void setCorrectCharacterSelectTitle(boolean isTraining) {
    //process will determine which title will be set between Training Mode and Character Select Screen
    if (isTraining) {
      getLandRoot().getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 351;
      getLandRoot().getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 420;
    }
    else {
      getLandRoot().getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 351;
      getLandRoot().getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 420;
    }

  }

}
