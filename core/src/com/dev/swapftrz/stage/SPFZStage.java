package com.dev.swapftrz.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dev.swapftrz.fyter.SPFZPlayer;
import com.dev.swapftrz.resource.LifeSystem;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.resource.SpecialSystem;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.List;

public class SPFZStage extends Stage
{
  private SPFZResourceManager resManager;
  private SPFZSceneLoader stageSSL;
  private final SPFZStageCamera stageCamera;
  private final SPFZStageAnimation stageAnimation;
  private SPFZStageHUD stageHUD;
  private SPFZStageStatus stageStatus;
  private ItemWrapper stageWrapper;
  private float[] cameraBoundaries = {}, stageBoundaries = {};
  private final float GROUND, WALLJUMPBOUNDARY, CHARACTER_SPACING;
  private final SPFZPlayer spfzPlayer1, spfzPlayer2;

  public SPFZStage(List<String> characters, SPFZResourceManager resManager) {
    this.resManager = resManager;
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
    spfzPlayer1 = new SPFZPlayer(this);
    spfzPlayer2 = new SPFZPlayer(this);
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

  @Override
  public void dispose() {
    stageSSL.engine.removeSystem(stageSSL.engine.getSystem(LifeSystem.class));
    stageSSL.engine.removeSystem(stageSSL.engine.getSystem(SpecialSystem.class));
    super.dispose();
  }

  public SPFZSceneLoader stageSSL() {
    return stageSSL;
  }

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
}
