package com.dev.swapftrz.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dev.swapftrz.fyter.SPFZPlayer;
import com.dev.swapftrz.menu.SPFZMenu;
import com.dev.swapftrz.resource.LifeSystem;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.resource.SpecialSystem;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.List;

public class SPFZStage extends Stage
{
  private SPFZResourceManager resManager;
  private final SPFZSceneLoader stageSSL, pauseSSL;
  private final SPFZStageCamera stageCamera;
  private final SPFZStageAnimation stageAnimation;
  private final SPFZMenu spfzMenu;
  private SPFZStageHUD stageHUD;
  private SPFZStageStatus stageStatus;
  private ItemWrapper stageWrapper;
  private float[] cameraBoundaries = { }, stageBoundaries = { };
  private final float GROUND, WALLJUMPBOUNDARY, CHARACTER_SPACING;
  private final SPFZPlayer spfzPlayer1, spfzPlayer2;

  public SPFZStage(SPFZMenu spfzMenu, List<String> characters, SPFZResourceManager resManager) {
    this.spfzMenu = spfzMenu;
    this.resManager = resManager;
    resManager.loadStageResource();
    GROUND = resManager.getStageGround();
    WALLJUMPBOUNDARY = resManager.getWallJumpBoundary();
    CHARACTER_SPACING = resManager.getStageStartSpacing();
    cameraBoundaries = resManager.getCameraBoundaries();
    stageBoundaries = resManager.getStageBoundaries();
    stageWrapper = resManager.rootWrapper();
    stageSSL = resManager.getCurrentSSL();
    stageCamera = new SPFZStageCamera(resManager);
    stageHUD = new SPFZStageHUD(this, resManager);
    stageAnimation = new SPFZStageAnimation(this);
    stageStatus = new SPFZStageStatus();
    pauseSSL = resManager.getPauseSSL();
    spfzPlayer1 = new SPFZPlayer(this, resManager, characters, true);
    spfzPlayer2 = new SPFZPlayer(this, resManager, characters, false);
    spfzPlayer1.setOpponent(spfzPlayer2);
    spfzPlayer2.setOpponent(spfzPlayer1);
    initStage(characters);
    stageCamera.setPlayers(spfzPlayer1, spfzPlayer2);
  }

  public void initStage(List<String> characters) {
    Gdx.input.setInputProcessor(this);
    stageHUD.createStageHUDTextures();
    stageHUD.setTimerTexture(false);
    stageHUD.setHUDCharacterNames(characters);
    stageHUD.preFightFade();
  }

  public void runProcesses() {
    if (stageStatus.isGameOver())
      stageStatus = stageStatus;

    if (stageStatus.isEndOfRound())
      stageStatus = stageStatus;

    if (stageStatus.isGamePause())
      stageStatus = stageStatus;

    if (stageStatus.isRoundStart())
      stageStatus = stageStatus;

  }

  @Override
  public void dispose() {
    stageSSL.engine.removeSystem(stageSSL.engine.getSystem(LifeSystem.class));
    stageSSL.engine.removeSystem(stageSSL.engine.getSystem(SpecialSystem.class));
    super.dispose();
  }

  public SPFZSceneLoader stageSSL() {
    return stageSSL;
  }

  public SPFZStageCamera camera() { return stageCamera; }

  public ItemWrapper stageWrapper() {
    return stageWrapper;
  }

  public float centerOfStage() {
    return stageBoundaries[1] - stageBoundaries[0];
  }

  public float ground() {
    return GROUND;
  }

  public float wallJumpBoundary() {
    return WALLJUMPBOUNDARY;
  }

  public float[] cameraBoundaries() {
    return cameraBoundaries;
  }

  public float[] stageBoundaries() {
    return stageBoundaries;
  }

  public SPFZPlayer player1() {
    return spfzPlayer1;
  }

  public SPFZPlayer player2() {
    return spfzPlayer2;
  }

  public void setStageStatus(SPFZStageStatus stageStatus) {
    this.stageStatus = stageStatus;
  }

  public boolean gameOver() { return stageStatus.isGameOver(); }

  public boolean gamePaused() { return stageStatus.isGamePause(); }

  public boolean roundStarted() { return stageStatus.isRoundStart(); }

  public boolean endOfRound() { return stageStatus.isEndOfRound(); }
}
