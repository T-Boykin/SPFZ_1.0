package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Class contains the sound file locations
 */
public class SPFZMenuSoundPack
{
  //Music
  private static final Music mainmenu = Gdx.audio.newMusic(Gdx.files.internal("music/Heclecta main menu.mp3"));

  //Sounds
  private static final Sound deselect = Gdx.audio.newSound(Gdx.files.internal("sound/deselect.ogg"));
  private static final Sound portraitUIButton = Gdx.audio.newSound(Gdx.files.internal("sound/portconfirm.ogg"));
  private static final Sound back = Gdx.audio.newSound(Gdx.files.internal("sound/backbtn1.ogg"));
  private static final Sound confirm = Gdx.audio.newSound(Gdx.files.internal("sound/okconfirm.ogg"));

  public SPFZMenuSoundPack() {

  }

  public static Music mainMenuMusic()
  {
    return mainmenu;
  }

  public static Sound deselectSound()
  {
    return deselect;
  }

  public static Sound portraitUIButtonSound()
  {
    return portraitUIButton;
  }

  public static Sound backSound()
  {
    return back;
  }

  public static Sound confirmSound()
  {
    return confirm;
  }

}
