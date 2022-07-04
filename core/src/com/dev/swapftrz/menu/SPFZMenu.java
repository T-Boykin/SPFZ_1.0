package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dev.swapftrz.SPFZState;
import com.dev.swapftrz.SwapFyterzMain;
import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.resource.LifeSystem;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.resource.SpecialSystem;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SPFZMenu
{
  private static final int PORT_SCENE_MAX = 4;
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZResourceManager resManager;
  private final SPFZMenuAction menu_action;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;
  private final AndroidInterfaceLIBGDX android;
  private final SPFZMenuCamera camera;
  private SPFZState state;
  private int portScene = 1;
  private ArrayList<String> characters = new ArrayList<>();

  private boolean isTraining;

  public SPFZMenu(SPFZResourceManager resManager, SPFZState state) {
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(resManager, menu_o2d, menu_animation, menu_sound);
    camera = resManager.getMenuCam();
    this.resManager = resManager;
    this.state = state;
    android = null;
  }

  public SPFZMenu(SPFZResourceManager resManager, AndroidInterfaceLIBGDX android, SPFZState state) {
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(resManager, menu_o2d, menu_animation, menu_sound);
    camera = resManager.getMenuCam();
    this.resManager = resManager;
    this.android = android;
    this.state = state;
  }

  /**
   * Method directs the UI to the Arcade Select screen
   */
  public void arcadeCharacterSelect() {
  }

  /**
   * Method directs the UI to the Character Select screen
   */
  public void vsCharacterSelect() {
  }

  /**
   * Method directs the UI to the Stage Select screen
   */
  public void stageSelect() {
  }

  /**
   * Method directs the UI to the Credits Section
   */
  public void enterCredits() {
  }

  /**
   * Method directs the UI back to the Main Menu screen
   */
  public void exitCredits() {
  }

  public void zoomToContributor() {
  }

  public void zoomToTechnologies() {
  }

  public void quitGame() {
  }

  public void cancelQuit() {
  }

  public void changeBrightness() {
  }

  /**
   * Sets the main menu up based on if device is in Landscape
   *
   * @param isLandscape
   */
  public void setupMainMenu(boolean isLandscape) {
    if (isLandscape)
      resManager.setScene(1);
    else
      resManager.setScene(0);

    menu_animation.spfzIntroduction(isLandscape).run();
  }

  /**
   * Method runs the main menu animation and sets game state to RUNNING
   */
  public void runMenuAnimation() {
    if (isLandscape())
      menu_animation.landscapeMenuAnimation();
    else
      menu_animation.portraitMenuAnimation();

    state = SPFZState.RUNNING;
  }

  public void processTimedBackgroundTasks() {
    resManager.runTimers();

    if (resManager.isTimeToReset(0))
      menu_animation.processCreditsHint();

    if (resManager.isTimeToReset(1))
      state = state;

    if (resManager.isTimeToReset(2))
      menu_animation.processLights();
  }

  public void back() {
    resManager.setBackToPrevousScene();
  }

  public Camera camera() {
    return camera;
  }

  public boolean isPortrait() {
    return resManager.getCurrentOrientation().equals("portrait");
  }

  public boolean isLandscape() {
    return resManager.getCurrentOrientation().equals("landscape");
  }

  public boolean isTraining() {
    return isTraining;
  }

  public void setIsTraining(boolean isTraining) {
    this.isTraining = isTraining;
  }

  public void setPortScene() {
    if (portScene == PORT_SCENE_MAX)
      portScene = 1;
    else
      portScene++;
  }

  public SPFZMenuSound sound() {
    return menu_sound;
  }

  public ItemWrapper rootEntityWrapper() {
    return resManager.rootWrapper();
  }

  public int portScene() {
    return portScene;
  }

  /*public void charspicked(List<String> sprites)
  {
    int keep = 0;
    // for (int i = 0; i < sprites.size(); i++)
    for (String sprite : sprites)
    {

      // Set ArrayLists to null for initialization
      // Add a boolean to see if we are coming back from Stage Select

      if (arrscripts.size() < 6)
      {
        arrscripts.add(null);
        processed.add(null);
      }

      if (sprite == "spriteball")
      {
        picked = 0;
      }
      //if (sprite == "spriteballred")
      if (sprite == "zaine")
      {
        picked = 1;
      }
      if (sprite == "spriteballblack")
      {
        picked = 2;
      }
      if (sprite == "spriteblock")
      {
        picked = 3;
      }
      if (sprite == "spritepurplex")
      {
        picked = 4;
      }
      if (sprite == "redblotch")
      {
        picked = 5;
      }
      if (sprite == "walksprite")
      {
        picked = 6;
      }
      // set the processed array based on the character picked in order to
      // evaluate
      // which character data is needed from CharAttributes class when an object
      // of it's
      // instance is initialized within SPFZ(P1/P2)Movement classes
      processed.set(keep, picked);

      // Assign the player one script to belong to the first 3 characters
      // selected
      if (keep <= 2)
      {
        arrscripts.set(keep, spfzPlayer1);
      }
      // Assign the player two script to belong to the last 3 characters
      // selected
      else
      {
        if (keep <= 5)
        {
          arrscripts.set(keep, spfzp2move);
        }
      }
      keep++;
    }*/

  public SPFZStage createstage() {
    resManager.loadStageResource("stage");
    resManager.setLandscapeSSL("stagescene");
    //stagesystem.priority = 0;
    //update(view).engine.addSystem(stagesystem);

    return new SPFZStage(characters, resManager);
  }

  //Character Select processing

  /**
   * Method sets up the sprites and loads them onto the platforms during the
   * Arcade mode
   */
  public void setcharasprites(String string) {
    String MAIN_LAYER = "Default";

    // Set and add the sprites to the character select screen
    for (int i = 0; i < charsselected.size() - 3; i++)
    {
      if (charsselected.get(i) == string)
      {
        i = 3;
        charpicked = true;
      }
      else
      {
        if (charsselected.get(i) == null)
        {

          if (!charpicked)
          {
            charsselected.set(i, string);
            charcomposites.set(i, update(view).loadVoFromLibrary(charsselected.get(i)));
            charcomposites.set(i, update(view).loadVoFromLibrary(charsselected.get(i))).layerName = MAIN_LAYER;
            /*
             * charentities.set(i,
             * update(view).entityFactory.createEntity(rootEntityWrapper().getEntity(),
             * charcomposites.get(i)));
             * update(view).entityFactory.initAllChildren(update(view).getEngine
             * (), charentities.get(i), charcomposites.get(i).composite);
             * update(view).getEngine().addEntity(charentities.get(i));
             *
             * arcselposition(i);
             */

            switch (i)
            {
              case 0:
                rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                //partstartone = true;
                break;
              case 1:
                rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(string);
                rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                rootEntityWrapper().getChild("secondcharpart").getEntity()
                  .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                rootEntityWrapper().getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                break;
              case 2:
                rootEntityWrapper().getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1;
                rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                break;
              default:
                break;
            }
            i = 3;
          }
        }
      }
    }

  }


  public void ssposition(int player) {

    switch (player)
    {
      case 0:

        charentities.get(player).getComponent(TransformComponent.class).x = 50f;
        charentities.get(player).getComponent(TransformComponent.class).y = 290f;
        break;

      case 1:

        charentities.get(player).getComponent(TransformComponent.class).x = 85f;
        charentities.get(player).getComponent(TransformComponent.class).y = 180f;
        break;

      case 2:

        charentities.get(player).getComponent(TransformComponent.class).x = 45f;
        charentities.get(player).getComponent(TransformComponent.class).y = 65f;

        break;

      case 3:

        charentities.get(player).getComponent(TransformComponent.class).x = 595f;
        charentities.get(player).getComponent(TransformComponent.class).scaleX *= -1f;
        charentities.get(player).getComponent(TransformComponent.class).y = 290f;
        break;

      case 4:

        charentities.get(player).getComponent(TransformComponent.class).x = 550f;
        charentities.get(player).getComponent(TransformComponent.class).scaleX *= -1f;
        charentities.get(player).getComponent(TransformComponent.class).y = 180f;

        break;

      case 5:
        charentities.get(player).getComponent(TransformComponent.class).x = 585f;
        charentities.get(player).getComponent(TransformComponent.class).scaleX *= -1f;
        charentities.get(player).getComponent(TransformComponent.class).y = 65f;
        break;
    }
  }

  //Back button processing
  private void backprocessing() {

  }

  //Clear button processing
  public void clearAll() {

    if ((System.currentTimeMillis() - cleartime) * .001f >= 1f && longclear
      && rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).getText().toString() != "")
    {
      for (int i = charsselected.size() - 1; i >= 0; i--)
      {
        if (charsselected.get(i) != null)
        {
          charsselected.set(i, null);
          charcomposites.set(i, null);

          update(view).getEngine().removeEntity(charentities.get(i));

          switch (i)
          {
            case 0:
              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("firstnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("firstnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 1:
              rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("secondnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("secondnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 2:
              rootEntityWrapper().getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("thirdnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("thirdnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 3:
              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("fourthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fourthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 4:
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("fifthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fifthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 5:
              rootEntityWrapper().getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(null);
              rootEntityWrapper().getChild("sixthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("sixthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            default:
              break;
          }
        }
      }

      Actions.addAction(fader, Actions.sequence(Actions.color(Color.WHITE, .0001f),
        Actions.alpha(1f, .0001f), Actions.alpha(0f, 1f), Actions.run(new Runnable()
        {
          @Override
          public void run() {
            fader.getComponent(TintComponent.class).color.set(0);
          }
        })));

      longclear = false;
    }

  }

  //Ad- processing

  /**
   * DisplayAD will process ads when necessary based on parameters received(process called while "running")
   * scene - current screen within SWAP FYTERZ
   * lastgen - last time ad generated in milliseconds
   * seconds - next time in seonds when the ad will display
   *
   * @param scene
   * @param lastgen
   * @param seconds
   */
  public void displayAD(String scene, long lastgen, int seconds) {
    Random randtime = new Random();

    if (resManager.appDevice() == resManager.ANDROID)
    {
      final int AD_GEN_MAX = 15;
      final int AD_GEN_MIX = 10;

      if ((System.currentTimeMillis() - lastgen) * .001f >= seconds)
      {
        switch (scene)
        {
          case "landscene":
            //if the last generated ad is now past the timer(seconds til the next AD generation), generate new AD

            //check if AD not generated, if not, generate AD

            //android.NEW_SPFZ_AD("banner");
            adtime = System.currentTimeMillis();
            randsecs = (randtime.nextInt(AD_GEN_MAX - AD_GEN_MIX) + 1) + AD_GEN_MAX;


            break;
          case "sceneone":
            //if the last generated ad is now past the timer(seconds til the next AD generation), generate new AD

            //check if AD not generated, if not, generate AD

            //android.NEW_SPFZ_AD("banner");
            adtime = System.currentTimeMillis();
            randsecs = (randtime.nextInt(AD_GEN_MAX - AD_GEN_MIX) + 1) + AD_GEN_MAX;

            break;
          case "training":
            break;
          case "storyscene":
            switch (level)
            {
              case 2:
                //android.NEW_SPFZ_AD("inter");
                break;
              case 4:
                //android.NEW_SPFZ_AD("inter");
                break;
              case 99:
                //android.NEW_SPFZ_AD("interv");
                break;
            }
            break;
          default:
            break;

        }
      }
    }
    else
    {

      final int AD_GEN_MAX = 15;
      final int AD_GEN_MIX = 10;

      String AdType = "";
      if ((System.currentTimeMillis() - lastgen) * .001f >= seconds)
      {
        switch (scene)
        {

          case "landscene":

            //if the last generated ad is now past the timer(seconds til the next AD generation), generate new AD


            AdType = "banner";
            adtime = System.currentTimeMillis();
            randsecs = (randtime.nextInt(10 - 5) + 1) + 10;


            break;
          case "sceneone":

            adtime = System.currentTimeMillis();
            randsecs = (randtime.nextInt(10 - 5) + 1) + 10;

            AdType = "banner";
            break;
          case "training":
            AdType = "banner";
            break;
          case "storyscene":

            switch (level)
            {
              case 2:
                AdType = "InterStit";
                break;
              case 4:
                AdType = "InterStit";
                break;
              case 99:
                AdType = "InterStitVid";
              default:
                break;
            }
            break;
          default:
            break;

        }
        if (AdType != "")
        {
          System.out.println("displaying " + AdType + " for " + scene + "");
        }
      }
    }
  }


  //character select processing
  public void loadslot(int position, boolean isTraining) {

    // first half of loading is handle within the setcharsprites() method
    // however the first half is commented out and kept here for reference

    // charsselected.set(slot, character);
    // charcomposites.set(position,
    // update(view).loadVoFromLibrary(charsselected.get(position)));
    // charcomposites.set(position,
    // update(view).loadVoFromLibrary(charsselected.get(position))).layerName =
    // MAIN_LAYER;

    // if (charentities.get(i) == null)
    // {
    charentities.set(position, update(view).entityFactory.createEntity(rootEntityWrapper().getEntity(), charcomposites.get(position)));
    update(view).entityFactory.initAllChildren(update(view).getEngine(), charentities.get(position),
      charcomposites.get(position).composite);
    update(view).getEngine().addEntity(charentities.get(position));

    if (arcade)
    {
      arcselposition(position);
    }
    else
    {
      ssposition(position);
    }

  }

  //Character select particle processing
  public void processcharselect() {
    if (partstartone)
    {

      if (rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(0, isArcade);
        partstartone = false;
      }
    }
    if (partstarttwo)
    {

      if (rootEntityWrapper().getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(1, isArcade);
        partstarttwo = false;
      }
    }
    if (partstartthree)
    {
      if (rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(2, isArcade);
        partstartthree = false;
      }
    }
    if (partstartfour)
    {
      if (rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(3, isArcade);
        partstartfour = false;
      }
    }
    if (partstartfive)
    {
      if (rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(4, isArcade);
        partstartfive = false;
      }
    }
    if (partstartsix)
    {
      if (rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(5, isArcade);
        partstartsix = false;
      }
    }
  }

  //Arcade Mode processing
  public void continuestory() {
    readnext(storyline);

    if (displaytext != "")
    {
      if (paragraph == 1)
      {
        Actions.addAction(rootEntityWrapper().getChild("arcadetext").getEntity(),
          Actions.sequence(Actions.delay(1.5f), Actions.fadeOut(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              rootEntityWrapper().getChild("arcadetext").getEntity().getComponent(LabelComponent.class).setText(displaytext);

            }
          }), Actions.fadeIn(1f)));
      }
      else
      {
        Actions.addAction(rootEntityWrapper().getChild("arcadetext").getEntity(),
          Actions.sequence(Actions.fadeOut(.8f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              rootEntityWrapper().getChild("arcadetext").getEntity().getComponent(LabelComponent.class).setText(displaytext);

            }
          }), Actions.fadeIn(1f)));
      }
    }
    else
    {
      //close the storytext scene and create the new level for the main character(1st character selected)
      Actions.addAction(rootEntityWrapper().getChild("fader").getEntity(), Actions.sequence(Actions.fadeIn(1f),
        Actions.delay(.2f), Actions.run(new Runnable()
        {
          @Override
          public void run() {
            resManager.setStage(createstage());
          }
        })));

      // isArcade = false;
      // isArcade = true;
      inact = false;
    }
  }

  public void arcadeinit() {
    short WORLD_WIDTH = 640;
    short WORLD_HEIGHT = 400;
    String storypath;
    if (isloading)
    {

      if (resManager.getManager().update())
      {
        isloading = false;
        if (resManager.appDevice() == resManager.ANDROID)
        {
          //android.toast("Arcade textures loaded");
        }
        else
        {
          System.out.print("Arcade textures loaded" + "\n");
        }
        if (resManager.appDevice() == resManager.ANDROID)
        {
          //android.lockOrientation(mode, view);

        }

        //resManager.currentScene() = "storyscene";

        // in testing, re-initializing the SceneLoader made
        // this work when switching between landscape
        // scenes.
        land = new SPFZSceneLoader(resManager, SwapFyterzMain.this, "", "");

        update(view).loadScene(resManager.currentScene(), viewportland);
        root = new ItemWrapper(update(view).getRoot());
        //transform = tc.get(rootEntityWrapper().getEntity());
        //action = ac.get(rootEntityWrapper().getEntity());
        level++;
        // load the 1st texture that will appear on the default layer
        storypath = "arcade/" + storyline + "/" + level + ".png";
        Pixmap pixmap = new Pixmap(Gdx.files.internal(storypath));
        // Pixmap pixmap = new Pixmap((FileHandle) resManager.getManager().get(storypath,
        // Texture.class));

        Pixmap pixmap2 = new Pixmap((int) WORLD_WIDTH * resManager.projectVO().pixelToWorld,
          (int) WORLD_HEIGHT * resManager.projectVO().pixelToWorld, pixmap.getFormat());

        pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, pixmap2.getWidth(),
          pixmap2.getHeight());
        storytex = new Texture(pixmap2);

        pixmap2.dispose();
        pixmap.dispose();

        trc.regionName = storypath;
        testregion = new TextureRegion(storytex, (int) WORLD_WIDTH * resManager.projectVO().pixelToWorld,
          (int) WORLD_HEIGHT * resManager.projectVO().pixelToWorld);

        setupstage = true;
        trc.region = testregion;

        stageimg.layerName = "Default";
        stageimg.zIndex = 0;

        stageimg.scaleX = 1f;
        stageimg.scaleY = 1f;

        stageimg.x = 0f;
        stageimg.y = 0f;
        if (storysplit == null)
        {
          getStoryText("arcade/story");
        }

        update(view).entityFactory.createEntity(rootEntityWrapper().getEntity(), stageimg);
        Actions.addAction(rootEntityWrapper().getChild("fader").getEntity(), Actions.fadeOut(1f));
        Actions.addAction(rootEntityWrapper().getChild("textbackground").getEntity(), Actions.scaleTo(3.8f, 1.0f, 1.5f));
        inact = true;
        continuestory();

      }

    }
  }

  public void getStoryText(String File) {
    FileHandle file = Gdx.files.internal(File + ".txt");
    String text = file.readString();

    storysplit = text.split("\r\n");

  }

  /**
   * display next paragraph from text file
   */
  public void readnext(String storychar) {
    charfound = false;
    displaytext = "";
    paragraph += 1;

    for (int i = 0; i < storysplit.length; i++)
    {
      if (storysplit[i].equals(storychar))
      {
        charfound = true;
      }

      if (charfound)
      {
        if (storysplit[i].contains(Integer.toString(level) + Integer.toString(paragraph)))
        {
          for (int j = i + 1; j < i + 5; j++)
          {
            displaytext += storysplit[j] + "\n";
          }
          i = storysplit.length;
        }
      }
    }
  }

  public void arcadeprocessing() {
    boolean contin = false;

    // Need to set a timer to advance the scene text
    if (Gdx.input.justTouched() && inact)
    {
      contin = true;
    }
    else
    {
      contin = false;
    }
    arcadeinit();

    if (contin && rootEntityWrapper().getChild("arcadetext").getEntity().getComponent(TintComponent.class).color.a == 1.0)
    {
      continuestory();
    }
  }

  public void setupArcade(String story) {
    String storypath;
    // fade in transition slide, fade character select music. load character's
    // story textures

    isloading = true;
    paragraph = 0;
    storyline = story;
    for (int i = 1; i < 6; i++)
    {
      storypath = "arcade/" + story + "/" + i + ".png";

      resManager.getManager().load(storypath, Texture.class);

    }
    // resManager.getManager().finishLoading();

  }

  //character select processing
  public void reloadchars() {

    for (byte i = 0; i < charsselected.size(); i++)
    {

      // slot = i;

      if (charsselected.get(i) == null)
      {

        if (!charpicked)
        {

          // charcomposites.set(i,
          // update(view).loadVoFromLibrary(charsselected.get(i)));
          // charcomposites.set(i,
          // update(view).loadVoFromLibrary(charsselected.get(i))).layerName =
          // MAIN_LAYER;

          // second half of character processing is commented out here but
          // executes within the
          // loadslot() method. The method executes when the character slot
          // particle is finished

          // charentities.set(i,
          // update(view).entityFactory.createEntity(rootEntityWrapper().getEntity(),
          // charcomposites.get(slot)));
          // update(view).entityFactory.initAllChildren(update(view).getEngine(),
          // charentities.get(i),
          // charcomposites.get(i).composite);


          switch (i)
          {
            case 0:
              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartone = true;
              break;
            case 1:
              rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstarttwo = true;
              break;
            case 2:
              rootEntityWrapper().getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;

              rootEntityWrapper().getChild("playerlbl").getEntity().getComponent(TintComponent.class).color = Color.RED;
              partstartthree = true;
              break;
            case 3:
              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartfour = true;
              break;
            case 4:
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartfive = true;
              break;
            case 5:
              rootEntityWrapper().getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
              rootEntityWrapper().getChild("cpupng").getEntity().getComponent(TintComponent.class).color = Color.BLUE;

              partstartsix = true;
              break;
            default:
              break;

          }
        }
      }
    }
  }

  //Pause screenshot logic
  public void getscreenshot() {
    int gWidth = Gdx.graphics.getWidth();
    int gHeight = Gdx.graphics.getHeight();
    Texture pausescn = new Texture(gWidth, gHeight, Pixmap.Format.RGB888);
    Sprite pausetex = new Sprite(pausescn);
    byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(),
      Gdx.graphics.getBackBufferHeight(), true);

    // this loop makes sure the whole screenshot is opaque and looks exactly
    // like what the user is seeing
    for (int i = 4; i < pixels.length; i += 4)
    {
      pixels[i - 1] = (byte) 255;
    }

    Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(),
      Pixmap.Format.RGBA8888);
    BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
    // PixmapIO.writePNG(Gdx.files.external("mypixmap.png"), pixmap);
    pausescn = new Texture(pixmap);
    pixmap.dispose();

    stage.pausetex = pausetex;

    if (pausetex.getWidth() != gWidth || pausetex.getHeight() != gHeight)
    {
      pausetex.setSize(gWidth, gHeight);
    }
    pausetex.setTexture(pausescn);

  }

  //Character select opening animation
  public void charselIntro() {
    String[] slides = {"translideone", "translidetwo", "translidethree", "translidefour", "translidefive",
      "translidesix", "translideseven", "translideeight", "translidenine", "translideten", "translideeleven",
      "translidetwelve"};
    // Character Select Transition

    if (INTRO == 2)
    {
      //transform = tc.get(rootEntityWrapper().getEntity());
      //action = ac.get(rootEntityWrapper().getEntity());

      for (int i = 0; i < 4; i++)
      {
        Actions.addAction(rootEntityWrapper().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(800f, -800f, 1f),
          Actions.sequence(Actions.color(Color.WHITE, .3f), Actions.color(Color.BLACK, .3f))));
      }
      for (int i = 4; i < 8; i++)
      {
        Actions.addAction(rootEntityWrapper().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(-800f, -800f, 1f),
          Actions.sequence(Actions.color(Color.BLACK, .3f), Actions.color(Color.RED, .3f))));
      }

      for (int i = 8; i < 12; i++)
      {
        Actions.addAction(rootEntityWrapper().getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(0, 800f, 1f),
          Actions.sequence(Actions.color(Color.RED, .3f), Actions.color(Color.WHITE, .3f))));
      }


      Actions.addAction(rootEntityWrapper().getChild("mainslide").getEntity(), Actions.fadeOut(1.5f));
      // Actions.addAction(rootEntityWrapper().getChild("mainslide").getEntity(), Actions.sequence(Actions.color(Color.WHITE, .3f)));
      //   Actions.fadeOut(1.5f)));


    }
  }

  //Arcade selection processing
  public void arcselposition(int player) {
    // Set the sprite positions for the character select
    switch (player)
    {
      case 0:

        charentities.get(player).getComponent(TransformComponent.class).scaleX = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).scaleY = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).x = 75f;
        charentities.get(player).getComponent(TransformComponent.class).y = 280f;

        break;

      case 1:

        charentities.get(player).getComponent(TransformComponent.class).scaleX = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).scaleY = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).x = 190f;
        charentities.get(player).getComponent(TransformComponent.class).y = 170f;

        break;

      case 2:

        charentities.get(player).getComponent(TransformComponent.class).scaleX = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).scaleY = 1.25f;
        charentities.get(player).getComponent(TransformComponent.class).x = 55f;
        charentities.get(player).getComponent(TransformComponent.class).y = 60f;

        break;

    }
  }

  //Resume Button Logic?
  //public void resumeButton() { resManager.removePauseMenuResources(); }

  //Pause processing
  if(!exit)

  {
    savescene = scenesel;
    if (stage != null)
    {
      if (!paused && !stage.gameover)
      {
        paused = true;
      }
      stage.pauseset = false;
      stage.pausetime = true;
      if (!stage.gameover)
      {
        // Call the Life and Special system update methods
        // prior to retrieving the screenshot in order to
        // have the special and life bars present during
        // the pause menu display
        update(view).getEngine().getSystem(LifeSystem.class).update(Gdx.graphics.getDeltaTime());
        update(view).getEngine().getSystem(SpecialSystem.class).update(Gdx.graphics.getDeltaTime());

        getscreenshot();
      }
      state = SPFZState.PAUSE;
      pausing();
    }
  }

  public void renderss() {
    stage.getBatch().begin();
    stage.getBatch().draw(stage.pausetex, 0, 0);
    stage.getBatch().end();
  }

  public void pausing() {
    short HALF_WORLDW = 320;
    short HALF_WORLDH = 200;

    if (pauseroot != null && !stage.gameover)
    {
      pauserootEntityWrapper().getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 0;
    }

    if (!pmenuloaded)
    {

      scenesel = 5;

      if (!stageset)
      {
        stage.stagetempx = viewportland.getCamera().position.x;
        stage.stagetempy = viewportland.getCamera().position.y;

        stageset = true;
      }

      pauseSSL.loadScene("pausescene", viewportland);
      viewportland.getCamera().position.set(stage.stagetempx, stage.stagetempy, 0);
      pauseoptions();

      // Set the pause menus and screen transition to the correct positioning

      pauserootEntityWrapper().getChild("pausemenu").getEntity()
        .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
      pauserootEntityWrapper().getChild("pausemenu").getEntity()
        .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;

      pauserootEntityWrapper().getChild("endoffightmenu").getEntity()
        .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
      pauserootEntityWrapper().getChild("endoffightmenu").getEntity()
        .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;

      if (stage.gameover && isArcade)
      {

        pauserootEntityWrapper().getChild("endoffightmenu").getChild("eof").getEntity().getComponent(TintComponent.class).color.a = 0;
        pauserootEntityWrapper().getChild("endoffightmenu").getChild("pabtn").getEntity()
          .getComponent(TintComponent.class).color.a = 0;
        pauserootEntityWrapper().getChild("endoffightmenu").getChild("csbtn").getEntity()
          .getComponent(TintComponent.class).color.a = 0;
        pauserootEntityWrapper().getChild("endoffightmenu").getChild("mmbtn").getEntity()
          .getComponent(TintComponent.class).color.a = 0;
        // Add Arcade end of game functionality

      }
      pmenuloaded = true;
    }

    else
    {

     /* if (!stoprender && !stage.gameover)
      {
        renderss();
      }*/

      if (restart && !isArcade)
      {
        chkrstrt();
      }
      pauseSSL.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    if (stage != null)
    {

      stage.pausedElapsed = System.currentTimeMillis();
      stage.firstpause = String.valueOf(stage.pausedElapsed).substring(0, 1);
      // Begin next arcade stage
      if (isArcade && stage.gameover && Gdx.input.isTouched())
      {
        paused = false;
        pauseSSL.engine.removeAllEntities();
        pauseSSL.entityFactory.clean();
        land.engine.removeAllEntities();
        land.entityFactory.clean();
        rootEntityWrapper().getEntity().removeAll();

        land = new SPFZSceneLoader(resManager, SwapFyterzMain.this, "", "");
        stage = null;

        state = SPFZState.RESUME;
        pmenuloaded = false;
        isloading = false;
        setupArcade(charsselected.get(0));
        genrand();
      }
    }
  }

  public void setStateOfGame(SPFZState state) {
    this.state = state;
  }
  //Back processing - the button and Android back button on device
}


