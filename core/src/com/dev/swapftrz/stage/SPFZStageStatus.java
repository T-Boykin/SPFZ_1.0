package com.dev.swapftrz.stage;

class SPFZStageStatus
{
  private boolean gameOver,
    endOfRound,
    roundStart,
    gamePause;

  public SPFZStageStatus() {

  }

  public void checkStatus() {
  }

  public void resetGameOver() {
    gameOver = false;
  }

  public void resetEndOfRound() {
    endOfRound = false;
  }

  public void resetRoundStart() {
    roundStart = false;
  }

  public void resetGamePause() {
    gamePause = false;
  }

  public void setGameOver() {
    gameOver = true;
  }

  public void setEndOfRound() {
    endOfRound = true;
  }

  public void setRoundStart() {
    roundStart = true;
  }

  public void setGamePause() {
    gamePause = true;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isEndOfRound() {
    return endOfRound;
  }

  public boolean isRoundStart() {
    return roundStart;
  }

  public boolean isGamePause() {
    return gamePause;
  }
}
