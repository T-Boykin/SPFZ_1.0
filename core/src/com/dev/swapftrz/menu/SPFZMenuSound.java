package com.dev.swapftrz.menu;

import com.dev.swapftrz.resource.SPFZResourceManager;

/**
 * Class plays the Swap Fyterz Menu sounds
 */
public class SPFZMenuSound
{
  private final SPFZResourceManager resourceManager;
  private float gameVolume;

  public SPFZMenuSound(SPFZResourceManager resourceManager) {
    this.resourceManager = resourceManager;
  }

  public void playMainMenuMusic() {
    SPFZMenuSoundPack.mainMenuMusic().play();
  }

  public void stopMainMenuMusic() {
    if (SPFZMenuSoundPack.mainMenuMusic().isPlaying())
      SPFZMenuSoundPack.mainMenuMusic().stop();
  }

  public void playDeselectSound() {
    SPFZMenuSoundPack.deselectSound().play();
  }

  public void playPortraitUIButtonSound() {
    SPFZMenuSoundPack.portraitUIButtonSound().play();
  }

  public void playBackSound() {
    SPFZMenuSoundPack.backSound().play();
  }

  public void playConfirmSound() {
    SPFZMenuSoundPack.confirmSound().play();
  }

  public void playZoomInSound() {

  }

  public void playZoomOutSound() {

  }

  public void setGameVolume(float gameVolume)
  {
    this.gameVolume = gameVolume;
  }

  public float currentGameVolume()
  {
    return gameVolume;
  }
}
