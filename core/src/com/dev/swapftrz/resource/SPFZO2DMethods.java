package com.dev.swapftrz.resource;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.data.AlphaData;
import com.uwsoft.editor.renderer.systems.action.data.DelayData;
import com.uwsoft.editor.renderer.systems.action.data.MoveByData;
import com.uwsoft.editor.renderer.systems.action.data.MoveToData;
import com.uwsoft.editor.renderer.systems.action.data.ScaleToData;
import com.uwsoft.editor.renderer.systems.action.logic.DelayAction;
import com.uwsoft.editor.renderer.systems.action.logic.ScaleToAction;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class SPFZO2DMethods
{
  public SPFZO2DMethods() {
  }

  public void rmvO2dObject(SPFZSceneLoader ssl, ItemWrapper rootIW, String stringObj) {
    ssl.getEngine().removeEntity(rootIW.getChild(stringObj).getEntity());
  }

  public void rmvO2dObjects(SPFZSceneLoader ssl, ItemWrapper rootIW, String[] stringObjs) {
    for (String stringObj : stringObjs)
      ssl.getEngine().removeEntity(rootIW.getChild(stringObj).getEntity());
  }

  public void hideO2dObject(ItemWrapper rootIW, String stringObj) {
    Actions.addAction(rootIW.getChild(stringObj).getEntity(), Actions.fadeOut(0f));
  }

  public void hideO2dObjects(ItemWrapper rootIW, String[] stringObjs) {
    for (String stringObj : stringObjs)
      Actions.addAction(rootIW.getChild(stringObj).getEntity(), Actions.fadeOut(0f));
  }

  public AlphaData fadeOutO2dObject(float duration, Interpolation interpolation) {

    if (interpolation == null)
      interpolation = Interpolation.linear;

    AlphaData alphaData = new AlphaData(interpolation, duration, 0);
    alphaData.logicClassName = AlphaAction.class.getName();

    return alphaData;
  }

  public AlphaData fadeInO2dObject(float duration, Interpolation interpolation) {

    if (interpolation == null)
      interpolation = Interpolation.linear;

    AlphaData alphaData = new AlphaData(interpolation, duration, 1);
    alphaData.logicClassName = AlphaAction.class.getName();

    return alphaData;
  }

  public DelayData delayO2dObject(float duration) {
    DelayData delayData = new DelayData(
      duration
    );
    delayData.logicClassName = DelayAction.class.getName();
    return delayData;
  }

  // ACTIONDATA CUSTOM ACTIONS ---------------------------------- \\\\\\\\\\\\\\\

  /**
   * Move Overlap2D Object to target destination
   *
   * @param toXVal        - move X to Value
   * @param toYVal        - move Y to Value
   * @param duration      - time of action
   * @param interpolation - tween operator
   */
  public ActionData moveO2dObjTo(float toXVal, float toYVal, float duration, Interpolation interpolation) {
    if (interpolation == null)
      interpolation = Interpolation.fade;

    MoveToData actionData = new MoveToData(interpolation, duration, toXVal, toYVal);

    return actionData;
  }

  /**
   * Move Overlap2D Object By specified values
   *
   * @param byXVal        - by X value given
   * @param byYVal        - by Y value given
   * @param duration      - time of action
   * @param interpolation - tween operator
   */
  public MoveByData moveO2dObjBy(float byXVal, float byYVal, float duration, Interpolation interpolation) {
    MoveByData actionData = new MoveByData(interpolation, duration, byXVal, byYVal);
    actionData.logicClassName = MoveByAction.class.getName();

    return actionData;
  }

  //TRANSFORM ACTIONS
  public ScaleToData scaleO2dObj(float scaleX,
                                 float scaleY, float scaleResize,
                                 float duration, Interpolation interpolation) {
    if (interpolation == null)
      interpolation = Interpolation.fade;

    ScaleToData actionData = new ScaleToData(
      interpolation,
      duration,
      scaleX + scaleResize,
      scaleY + scaleResize
    );
    actionData.logicClassName = ScaleToAction.class.getName();
    return actionData;
  }

  /**
   * Action flashes the object passed, essentially fading it in and out
   *
   * @param repititions - number of times object should fade in and out
   * @param duration    - time action should be performed
   * @param slowDown    - reptitions slow down overtime if set to true
   * @param speedUp     - repititions speed up overtime if set to true
   */
  public AlphaData[] flashO2dObject(float repititions, float duration,
                                    boolean slowDown, boolean speedUp) {
    float actionDuration = duration / repititions;
    boolean setFadeIn = false;
    AlphaData[] fadeActions = {};

    for (int i = 0; i < repititions; i++)
    {
      if (speedUp && !slowDown)
        actionDuration /= actionDuration;

      if (slowDown && !speedUp)
        actionDuration *= actionDuration;

      if (setFadeIn)
      {
        fadeActions[i] = fadeInO2dObject(actionDuration, null);
        setFadeIn = false;
      }
      else
      {
        fadeActions[i] = fadeOutO2dObject(actionDuration, null);
        setFadeIn = true;
      }
    }

    return fadeActions;
  }
}
