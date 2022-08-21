package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZButtonSystem;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.resource.SPFZStageButtonComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Class performs the button functionality for the Menu UI
 */
public class SPFZMenuAction {
  private static final String MENU_BUTTON_TAG = "button";
  private final SPFZMenuButtonListeners menu_listeners;
  private final SPFZResourceManager resManager;
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;
  private final SPFZMenu spfzMenu;

  //1 Clear button hold 2 restart button hold
  private float[][] buttonHoldTimers = {{0, 1f}, {0, 1f}};

  public SPFZMenuAction(SPFZMenu spfzMenu, SPFZResourceManager resManager, SPFZMenuO2DMenuObjects menu_o2d,
                        SPFZMenuAnimation menu_animation, SPFZMenuSound menu_sound) {

    menu_listeners = new SPFZMenuButtonListeners(this);
    this.spfzMenu = spfzMenu;
    this.resManager = resManager;
    this.menu_o2d = menu_o2d;
    this.menu_animation = menu_animation;
    this.menu_sound = menu_sound;
  }
  //UI component BUTTON FUNCTIONALITY

  //handles for multiple menus, main, pause, etc
  public void setMenuButtonComponents() {
    resManager.getCurrentSSL().addComponentsByTagName(MENU_BUTTON_TAG, SPFZButtonComponent.class);
  }

  //TODO Make sure to change Stage select button tags to stageButton/stagebutton within Overlap2D
  public void setStageSelectButtonComponents() {
    resManager.getCurrentSSL().addComponentsByTagName(MENU_BUTTON_TAG, SPFZStageButtonComponent.class);
  }

  public void setMainMenuButtonListeners() {
    SPFZButtonComponent.ButtonListener[] altButtonListeners = menu_listeners.altButtonListeners();
    SPFZButtonComponent.ButtonListener[] main3ButtonListeners = menu_listeners.main3ButtonListeners();
    SPFZButtonComponent.ButtonListener[] main5ButtonListeners = menu_listeners.main5ButtonListeners();
    String[] altButtons = menu_o2d.altButtons();
    String[] main3Buttons = menu_o2d.main3Buttons();
    String[] main5Buttons = resManager.getCurrentOrientation().equals(resManager.PORTRAIT)
      ? menu_o2d.portMain5Buttons() : menu_o2d.landMain5Buttons();

    if (resManager.getCurrentOrientation().equals(resManager.PORTRAIT))
      for (int i = 0; i < main3Buttons.length; i++)
        resManager.rootWrapper().getChild(menu_o2d.CTRLBOARD).getChild(menu_o2d.main3Buttons()[i]).getEntity()
          .getComponent(SPFZButtonComponent.class).addListener(main3ButtonListeners[i]);
    else
      for (int i = 0; i < main3Buttons.length; i++)
        resManager.rootWrapper().getChild(main3Buttons[i]).getEntity()
          .getComponent(SPFZButtonComponent.class).addListener(main3ButtonListeners[i]);

    //process for adding Listeners for constellations and support links
    //TODO Find a way to use key values in a set up such as: You: {fb, twit} process creates link

    for (int i = 0; i < main5Buttons.length; i++)
      resManager.rootWrapper().getChild(main5Buttons[i]).getEntity()
        .getComponent(SPFZButtonComponent.class).addListener(main5ButtonListeners[i]);

    for (int i = 0; i < altButtonListeners.length; i++)
      resManager.rootWrapper().getChild(altButtons[i]).getEntity()
        .getComponent(SPFZButtonComponent.class).addListener(altButtonListeners[i]);
  }

  public void setCharacterSelectButtonListeners() {
    //character buttons
    //ok, back, and clear buttons
  }

  //consider making an SPFZStageButtonComponent
  public void setStageSelectButtonListeners() {
    String[] stages = menu_o2d.stages(), stageSelectButtons = menu_o2d.stageSelectButtons();

    for (int i = 0; i < stages.length - 1; i++)
      resManager.rootWrapper().getChild(stageSelectButtons[i]).getComponent(SPFZStageButtonComponent.class)
        .addListener(menu_listeners.stageSelectButtonListener(), stages[i]);
  }

  public void setClearButtonListener() {
    resManager.rootWrapper().getChild(menu_o2d.CLEARBUTTON).getComponent(SPFZButtonComponent.class)
      .addListener(menu_listeners.clearButtonListener());
  }

  public void setOkButtonListener() {
    resManager.rootWrapper().getChild(menu_o2d.OKBUTTON).getComponent(SPFZButtonComponent.class)
      .addListener(menu_listeners.okButtonListener());
  }

  public SPFZSceneLoader currentSSL() {
    return resManager.getCurrentSSL();
  }

  //maybe set this within the system itself
  public void setProcessing() {
    resManager.getCurrentSSL().engine.getSystem(SPFZButtonSystem.class).setProcessing(true);
  }

  //ACTION RUNNABLES
  //Don't forget to lock orientation when changing to next steps of scene

  public void processArcadeButton() {
    spfzMenu.setIsArcade(true);
    setProcessing();
    menu_sound.playConfirmSound();
    menu_animation.fadeInAndOutPlusAction(resManager.rootWrapper(), () -> resManager.setLandscapeSSL("arcadeselscn"));
  }

  public void processVsTrainingButton(boolean isTraining) {
    spfzMenu.setIsTraining(isTraining);
    setProcessing();
    menu_sound.playConfirmSound();
    menu_animation.fadeInAndOutPlusAction(resManager.rootWrapper(), () -> {
      resManager.setLandscapeSSL("charselscene");
      menu_animation.setCorrectCharacterSelectTitle(isTraining);
    });
  }

  public void processHelpButton() {
    setProcessing();
    menu_sound.playConfirmSound();
    menu_animation.openHelpOptions();
  }

  public void processOptionsButton() {
    //oritentation lock while options menu is open
    setProcessing();
    menu_sound.playConfirmSound();
    setsliders();
    menu_animation.openOptions();
  }

  public void setsliders() {
    String composite = "";
    TransformComponent transcomponent = ComponentRetriever
      .get(resManager.rootWrapper().getChild(composite).getChild("brightslider").getEntity(), TransformComponent.class);
    DimensionsComponent dimcomponent = ComponentRetriever
      .get(resManager.rootWrapper().getChild(composite).getChild("brightslider").getEntity(), DimensionsComponent.class);
    TransformComponent transcomp = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("brightbar").getEntity(),
      TransformComponent.class);
    DimensionsComponent dimcompon = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("brightbar").getEntity(),
      DimensionsComponent.class);
    Vector2 dimwhs = new Vector2(), dimwh = new Vector2();
    Vector3 transpar = new Vector3(0, 0, 0);
    final float MAX_VOL = resManager.getMaxSoundSettingsValues(), MAX_BRIGHT = resManager.getMaxBrightSettingsValues();
    float brightamount = resManager.getBrightSettingsValues(), soundamount = resManager.getSoundSettingsValues(), fullbarpercent;

    // set bright
    // slider---------------------------------------------------------------

    transpar.x = transcomponent.x;
    transpar.y = transcomponent.y;

    dimwh.x = dimcomponent.width * transcomponent.scaleX;
    dimwh.y = dimcomponent.height * transcomponent.scaleY;
    dimwhs.x = dimcompon.width * transcomp.scaleX;
    dimwhs.y = dimcompon.height * transcomp.scaleY;

    // bar full percentage
    fullbarpercent = dimwhs.x;

    // brightamount is received from the flavor in prefs
    brightamount = 100 * (brightamount / MAX_BRIGHT);

    transcomponent.x = (transcomp.x - (dimwh.x * .5f)) + (float) ((brightamount * .01) * fullbarpercent);

    brightamount = brightamount * (MAX_BRIGHT * .01f);
    // set sound
    // slider----------------------------------------------------------------
    transcomponent = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getEntity(), TransformComponent.class);

    transpar.x = transcomponent.x;
    transpar.y = transcomponent.y;

    transcomponent = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("soundslider").getEntity(),
      TransformComponent.class);
    dimcomponent = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("soundslider").getEntity(),
      DimensionsComponent.class);
    transcomp = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("soundbar").getEntity(),
      TransformComponent.class);
    dimcompon = ComponentRetriever.get(resManager.rootWrapper().getChild(composite).getChild("soundbar").getEntity(),
      DimensionsComponent.class);

    dimwh.x = dimcomponent.width * transcomponent.scaleX;
    dimwh.y = dimcomponent.height * transcomponent.scaleY;
    dimwhs.x = dimcompon.width * transcomp.scaleX;
    dimwhs.y = dimcompon.height * transcomp.scaleY;

    // bar full percentageU
    fullbarpercent = dimwhs.x;

    // soundamount is received from the file - see readFile(String File)
    soundamount = 100 * (soundamount / MAX_VOL);

    // transcomponent.x = (transcomp.x - (dimwh.x / 2)) + (float) ((soundamount
    // * .01) * fullbarpercent);
    //Center the Slider
    transcomponent.x = (transcomp.x - (dimwh.x * .5f)) + (float) ((soundamount * .01) * fullbarpercent);

    soundamount = soundamount * (MAX_VOL * .01f);
  }

  public void processSoundButton() {
    float maxSound = resManager.getMaxSoundSettingsValues(),
      currentSoundLevel = resManager.getSoundSettingsValues(),
      increments = resManager.getSoundIncrementsSettingsValues(),
      newSoundLevel = 0;

    for (float i = 1; i <= increments; i++)
    {
      if (currentSoundLevel > i / increments && newSoundLevel == 0)
        continue;

      if (currentSoundLevel == maxSound)
        newSoundLevel = 1 / increments;

      if (newSoundLevel == 0)
        newSoundLevel = i / increments;
    }

    resManager.saveSoundSettings(newSoundLevel);
    menu_sound.setGameVolume(newSoundLevel);
    menu_sound.playConfirmSound();
  }

  public void processBrightnessButton() {
    float minBrightness = resManager.getMinBrightSettingsValues(),
      maxBrightness = resManager.getMaxBrightSettingsValues(),
      currentBrightnessLevel = resManager.getBrightSettingsValues(),
      increments = resManager.getBrightIncrementSettingsValues(),
      newBrightnessLevel = 0;

    for (float i = 1; i <= increments; i++)
    {
      if (currentBrightnessLevel > (i / maxBrightness) && newBrightnessLevel == 0)
        continue;

      if (currentBrightnessLevel == maxBrightness)
        newBrightnessLevel = minBrightness;

      if (newBrightnessLevel == 0)
        newBrightnessLevel = i / increments;
    }

    resManager.saveBrightSettings(newBrightnessLevel);
    menu_sound.playConfirmSound();
  }

  public void processExitButton() {
    setProcessing();
    menu_animation.showExitDialog();
    menu_animation.portButtonsBelowMain();
  }

  public void processYesConfirmButton() {
    menu_animation.faderInPlusAction(resManager.rootWrapper(),
      () -> Gdx.app.exit());
  }

  public void processNoConfirmButton() {
    setProcessing();
    menu_animation.lowerExitDialog();
    menu_animation.portButtonsToMainCenter();
  }

  public void processMnuScnButton() {
  }

  public void processInGameButton() {
  }

  public void processHlpBackButton() {
  }

  public void processThirtyButton() {
    resManager.setRoundTimeSettings(30);
  }

  public void processSixtyButton() {
    resManager.setRoundTimeSettings(60);
  }

  public void processNinetyNineButton() {
    resManager.setRoundTimeSettings(99);
  }

  public void initializeSliders() {

  }

  public void processBrightSlider() {
  }

  public void processSoundSlider() {
  }

  public void processConstellationButton() {
  }

  public void processExternalSupportButton() {
  }
  //CHARACTER AND STAGE SELECT MENU ACTIONS

  public void processOkButton(String currentScene) {
    switch (currentScene)
    {
      case "stageselscn":
        menu_animation.fadeInAndOutPlusAction(resManager.rootWrapper(), () -> { spfzMenu.createStage(); });
        break;
    }
  }

  public void processBackButton() {
    menu_animation.fadeInAndOutPlusAction(resManager.rootWrapper(), () -> { spfzMenu.back(); });
  }

  public void processClearButton() {
    //animate the clear button, animate the character disappearance, particle effect, etc

  }

  public void processClearButton(boolean clearButtonHeld) {
    float currentHoldTime = buttonHoldTimers[0][0], endHoldTime = buttonHoldTimers[0][1];

    if (!clearButtonHeld)
      currentHoldTime = 0f;

    if (currentHoldTime < endHoldTime) {
      currentHoldTime += Gdx.graphics.getDeltaTime();
      buttonHoldTimers[0][0] = currentHoldTime;
    }
  }

  public void processYesButton() {
  }

  public void processNoButton() {
  }

  public void processSprite(String character) {
    //if Arcade
    //setcharsprite(arrayOfChar?)
    //else
  }

  //STAGE SELECT
  public void processStageSelect(String stage) {
  }

  public boolean clearAllTimerLimitReached() { return buttonHoldTimers[0][0] >= buttonHoldTimers[0][1]; }
}
