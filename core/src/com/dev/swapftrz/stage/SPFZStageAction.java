package com.dev.swapftrz.stage;

import com.dev.swapftrz.fyter.SPFZPlayer;

public class SPFZStageAction
{
  private final SPFZStageStatus stage_status;
  private final SPFZStageButtonListeners stage_listeners;
  //player controlled by the stage controller
  private final SPFZPlayer player;

  public SPFZStageAction(SPFZStage stage, SPFZPlayer player) {
    stage_status = new SPFZStageStatus();
    stage_listeners = new SPFZStageButtonListeners(this, stage.stageSSL());
    this.player = player;
  }

  public void processPause() {
    stage_status.setGamePause();
  }

  public void processRightButton(boolean pressed) {
    player.isRight = pressed;
  }

  public void processDownRightButton(boolean pressed) {
    player.isLeft = pressed;
    player.isUp = pressed;
  }

  public void processDownButton(boolean pressed) {
    player.isDown = pressed;
  }

  public void processDownLeftButton(boolean pressed) {
    player.isLeft = pressed;
    player.isUp = pressed;
  }

  public void processLeftButton(boolean pressed) {
    player.isLeft = pressed;
  }

  public void processUpLeftButton(boolean pressed) {
    player.isLeft = pressed;
    player.isUp = pressed;
  }

  public void processUpButton(boolean pressed) {
    player.isUp = pressed;
  }

  public void processUpRightButton(boolean pressed) {
    player.isLeft = pressed;
    player.isUp = pressed;
  }

  public void processPunchButton(boolean pressed) {
    player.isPunch = pressed;
  }

  public void processKickButton(boolean pressed) {
    player.isKick = pressed;
  }
}