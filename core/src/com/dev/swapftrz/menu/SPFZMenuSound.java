package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZResourceManager;

/**
 * Class plays the Swap Fyterz Menu sounds
 */
public class SPFZMenuSound
{
  private final SPFZResourceManager resourceManager;
  private final SPFZMenuSoundPack soundPack = new SPFZMenuSoundPack();
  private float gameVolume, gamePan, gamePitch;

  public SPFZMenuSound(SPFZResourceManager resourceManager) {
    this.resourceManager = resourceManager;
    //set based on settings/preferences file
    gameVolume = 1.0f;
  }

  //TODO need to create a Main Music file and a Main Music Looping file

  /**
   * Plays the main menu music
   */
  public void playMainMenuMusic() {
    if (soundPack.mainMenuMusic().getVolume() != gameVolume)
      soundPack.mainMenuMusic().setVolume(gameVolume);

    soundPack.mainMenuMusic().play();
  }

  public void playMainMenuLoopMusic() {
    if (soundPack.mainMenuMusic().getVolume() != gameVolume)
      soundPack.mainMenuMusic().setVolume(gameVolume);

    soundPack.mainMenuMusic().setLooping(true);
    soundPack.mainMenuMusic().play();
  }

  public void stopMainMenuMusic() {
    if (soundPack.mainMenuMusic().isPlaying())
      soundPack.mainMenuMusic().stop();
  }

  public void playDeselectSound() {
    soundPack.deselectSound().play(gameVolume);
  }

  public void playPortraitUIButtonSound() {
    soundPack.portraitUIButtonSound().play(gameVolume);
  }

  public void playBackSound() {
    soundPack.backSound().play(gameVolume);
  }

  public void playConfirmSound() {
    soundPack.confirmSound().play(gameVolume);
  }

  public void playZoomInSound() {
    soundPack.zoomIn().play(gameVolume);
  }

  public void playZoomOutSound() {
    soundPack.zoomOut().play(gameVolume);
  }

  public void setGameVolume(float gameVolume) {
    this.gameVolume = gameVolume;
  }

  public void setGamePan(float gamePan) {
    this.gamePan = gamePan;
  }

  public void setGamePitch(float gamePitch) {
    this.gamePitch = gamePitch;
  }

  public float gameVolume() {
    return gameVolume;
  }

  public float gamePan() {
    return gamePan;
  }

  public float gamePitch() {
    return gamePitch;
  }
}
