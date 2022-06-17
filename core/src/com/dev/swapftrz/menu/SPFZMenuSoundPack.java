package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

//TODO consider using a properties file for sound location storage
/**
 * Class contains the sound file locations
 */
public class SPFZMenuSoundPack
{
  //Music
  private static final Music mainMenu = Gdx.audio.newMusic(Gdx.files.internal("music/Heclecta main menu.mp3")),
    mainMenuLoop = Gdx.audio.newMusic(Gdx.files.internal("music/Heclecta main menu.mp3"));

  //Sounds
  private static final Sound deselect = Gdx.audio.newSound(Gdx.files.internal("sound/deselect.ogg")),
    portraitUIButton = Gdx.audio.newSound(Gdx.files.internal("sound/portconfirm.ogg")),
    back = Gdx.audio.newSound(Gdx.files.internal("sound/backbtn1.ogg")),
    confirm = Gdx.audio.newSound(Gdx.files.internal("sound/okconfirm.ogg")),
    zoomIn = Gdx.audio.newSound(Gdx.files.internal("sound/zoomin.ogg")),
    zoomOut = Gdx.audio.newSound(Gdx.files.internal("sound/zoomout.ogg"));

  public SPFZMenuSoundPack() {
  }

  public final Music mainMenuMusic() {
    return mainMenu;
  }

  public final Music mainMenuLoopMusic() {
    return mainMenuLoop;
  }

  public final Sound deselectSound() {
    return deselect;
  }

  public final Sound portraitUIButtonSound() {
    return portraitUIButton;
  }

  public final Sound backSound() {
    return back;
  }

  public final Sound confirmSound() {
    return confirm;
  }

  public final Sound zoomIn() {
    return zoomIn;
  }

  public final Sound zoomOut() {
    return zoomOut;
  }

}
