package com.dev.swapftrz.menu;

/**
 * Class plays the Swap Fyterz Menu sounds
 */
public class SPFZMenuSound
{
  private float gameVolume;

  public SPFZMenuSound() {

  }

  public void playMainMenuMusic() {
    SPFZMenuSoundPack.mainMenuMusic().play();
  }

  public void stopMainMenuMusic() {
    if(SPFZMenuSoundPack.mainMenuMusic().isPlaying())
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
