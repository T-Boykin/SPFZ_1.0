package com.dev.swapftrz.menu;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dev.swapftrz.SPFZState;
import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;

public class SPFZMenu {
  private static final int PORT_SCENE_MAX = 4;
  private final SPFZMenuO2DMenuObjects menu_o2d;
  private final SPFZResourceManager resManager;
  private final SPFZMenuAction menu_action;
  private final SPFZMenuAnimation menu_animation;
  private final SPFZMenuSound menu_sound;
  private final AndroidInterfaceLIBGDX android;
  private final SPFZMenuCamera camera;
  private float[][] arcSelectPosition = {{75f, 280f}, {190f, 170f}, {55f, 60f}},
    charSelectPosition = {{50f, 290f}, {85f, 180f}, {45, 65}, {595f, 290f}, {550f, 180f}, {585f, 65f}},
  //Timers managed within arrays, 1 CreditHints, 2 Ads, 3 MainMenu Light
  timersAndStops = {{0, 5f}, {0, 7f}, {0, 1.1f}};
  private String[] arcadeScript;
  private ArrayList<String> characters = new ArrayList<>();
  private SPFZState state;
  private int portScene = 1, numberOfCharactersSelected, level, paragraph;
  private boolean isArcade, isTraining;

  public SPFZMenu(SPFZResourceManager resManager, SPFZState state) {
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(this, resManager, menu_o2d, menu_animation, menu_sound);
    camera = resManager.getMenuCam();
    this.resManager = resManager;
    this.state = state;
    android = null;
  }

  public SPFZMenu(SPFZResourceManager resManager, AndroidInterfaceLIBGDX android, SPFZState state) {
    menu_o2d = new SPFZMenuO2DMenuObjects();
    menu_animation = new SPFZMenuAnimation(this, resManager.getPortraitSSL(), resManager.getLandscapeSSL(), menu_o2d);
    menu_sound = new SPFZMenuSound(resManager);
    menu_action = new SPFZMenuAction(this, resManager, menu_o2d, menu_animation, menu_sound);
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

  public void processTimerTasks() {
    runTimers();

    if (isTimeToReset(0))
      menu_animation.processCreditsHint();

    if (isTimeToReset(1))
      state = state;

    if (isTimeToReset(2))
      menu_animation.processLights();

    if (menu_action.clearAllTimerLimitReached())
      clearAll();
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

  public boolean isArcade() { return isArcade; }

  public boolean isTraining() {
    return isTraining;
  }

  public void setIsArcade(boolean isArcade) {
    this.isArcade = isArcade;
  }

  public void setIsTraining(boolean isTraining) {
    if (isArcade && isTraining)
      isArcade = false;

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

  public SPFZStage createStage() {
    resManager.loadStageResource(resManager.selectedStage());
    resManager.setLandscapeSSL("stagescene");
    //stagesystem.priority = 0;
    //update(view).engine.addSystem(stagesystem);

    return new SPFZStage(this, characters, resManager);
  }

  //Character Select processing

  //character select button method

  /**
   * Method sets up the sprites and loads them onto the platforms during the
   * Arcade mode
   */
  public void setCharacterToPlatform(String character) {
    String MAIN_LAYER = "Default";
    int characterLimit = isArcade ? 3 : 6, slot = characters.size();

    if (characters.size() < characterLimit) {
      characters.set(slot, character);
      preSlotPopulateProcess(slot, character, slot == characterLimit - 1 || slot == (characterLimit / 2) - 1);
      populateSlot(character, slot, isArcade);
    }
  }

  //always could improvise with creating animations with color changing for names
  public void preSlotPopulateProcess(int index, String character, boolean isLastSlot) {
    Entity label = rootEntityWrapper().getChild(menu_o2d.platformLabels()[index]).getEntity(),
      particle = rootEntityWrapper().getChild(menu_o2d.platformSelectParticles()[index]).getEntity();

    if (!isLastSlot)
      label.getComponent(TintComponent.class).color = Color.GRAY;
    else
      for (int i = index; i >= index - 2; i--)
        rootEntityWrapper().getChild(menu_o2d.platformLabels()[i]).getEntity()
          .getComponent(TintComponent.class).color = Color.WHITE;

    particle.getComponent(SPFZParticleComponent.class).worldMultiplyer = 1;
    particle.getComponent(SPFZParticleComponent.class).startEffect();
    label.getComponent(LabelComponent.class).setText(character);
  }

  public void populateSlot(String character, int slot, boolean isArcade) {
    Entity characterEntity = resManager.createObject(character, "Default");
    //If Arcade mode, scale will only be one direction for 3 characters(1.25f)
    //otherwise, use the slot to determine which way characters should be facing
    float scale = isArcade ? SPFZ_MAct.ARCADE_SCALE : slot < 3 ?
      SPFZ_MAct.NORMAL_SCALE : -SPFZ_MAct.NORMAL_SCALE;

    if (isArcade) {
      characterEntity.getComponent(TransformComponent.class).x =
        arcSelectPosition[slot][0];
      characterEntity.getComponent(TransformComponent.class).y =
        arcSelectPosition[slot][1];
    }
    else {
      characterEntity.getComponent(TransformComponent.class).x =
        charSelectPosition[slot][0];
      characterEntity.getComponent(TransformComponent.class).y =
        charSelectPosition[slot][1];
    }

    characterEntity.getComponent(TransformComponent.class).scaleX = scale;
    characterEntity.getComponent(TransformComponent.class).scaleY = scale;
  }

  //Back button processing
  private void backprocessing() {

  }

  //Clear button processing
  public void clearAll() {
    if (rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).getText().toString().equals("")) {
      for (int i = characters.size() - 1; i >= 0; i--) {
        startParticleEffect(menu_o2d.platformNullParticles()[i]);
        changeCharacterLabelText(menu_o2d.platformLabels()[i], null);
      }

     /* fader in and out

      Actions.addAction(fader, Actions.sequence(Actions.color(Color.WHITE, .0001f),
        Actions.alpha(1f, .0001f), Actions.alpha(0f, 1f), Actions.run(new Runnable()
        {
          @Override
          public void run() {
            fader.getComponent(TintComponent.class).color.set(0);
          }
        })));*/
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
    //method needs to set a random end time, and maintain 2nd array of counters
  }

  //Arcade Mode processing
  public void continuestory() {
    readnext(arcadeScript);

    /*if (displaytext != "")
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
        Actions.delay(.2f), Actions.run(() -> resManager.setStage(createstage()))));
    }*/
  }

  public void arcadeinit() {
    short WORLD_WIDTH = 640;
    short WORLD_HEIGHT = 400;
    String storypath;
    if (!resManager.getManager().isFinished()) {

      if (resManager.getManager().update()) {
        if (resManager.appDevice() == resManager.ANDROID) {
          //android.toast("Arcade textures loaded");
        }
        else {
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
        /*resManager.setLandscapeSSL(resManager.currentScene());
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
        continuestory();*/
      }
    }
  }

  public String[] getStoryText(String File) {
    FileHandle file = Gdx.files.internal(File + ".txt");
    String text = file.readString();

    return text.split("\r\n");
  }

  /**
   * display next paragraph from text file
   */
  public void readnext(String[] arcadeScript) {

    //redo logic of reading next paragraph
  }

  public void arcadeprocessing() {
    boolean contin = false;

    // Need to set a timer to advance the scene text
    if (Gdx.input.justTouched()) {
      contin = true;
    }
    else {
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

    paragraph = 0;
    for (int i = 1; i < 6; i++)
    {
      storypath = "arcade/" + story + "/" + i + ".png";
      resManager.getManager().load(storypath, Texture.class);
    }
    // resManager.getManager().finishLoading();

  }

  //character select processing
  public void reloadchars() {

    for (byte i = 0; i < characters.size(); i++) {
      // slot = i;

      if (characters.get(i) == null) {
        // charcomposites.set(i,
        // update(view).loadVoFromLibrary(characters.get(i)));
        // charcomposites.set(i,
        // update(view).loadVoFromLibrary(characters.get(i))).layerName =
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


        switch (i) {
          case 0:
          case 1:
            changeCharacterLabel(menu_o2d.platformLabels()[i], characters.get(i), Color.GRAY);
            startParticleEffect(menu_o2d.platformSelectParticles()[i]);
            break;
          case 2:
            changeCharacterLabel(menu_o2d.platformLabels()[i], characters.get(i), Color.GRAY);
            startParticleEffect(menu_o2d.platformSelectParticles()[i]);
            changeCharacterLabelTextColor(menu_o2d.platformLabels()[0], Color.WHITE);
            changeCharacterLabelTextColor(menu_o2d.platformLabels()[1], Color.WHITE);

            rootEntityWrapper().getChild("playerlbl").getEntity().getComponent(TintComponent.class).color = Color.RED;
            break;
          case 3:
            rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(characters.get(i));
            rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
            rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
            rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
            break;
          case 4:
            rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(characters.get(i));
            rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
            rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
            rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
            break;
          case 5:
            rootEntityWrapper().getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(characters.get(i));
            rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
            rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

            rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
            rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
            rootEntityWrapper().getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
            rootEntityWrapper().getChild("cpupng").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
            break;
          default:
            break;
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
      pixels[i - 1] = (byte) 255;


    Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(),
      Pixmap.Format.RGBA8888);
    BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
    // PixmapIO.writePNG(Gdx.files.external("mypixmap.png"), pixmap);
    pausescn = new Texture(pixmap);
    pixmap.dispose();

    if (pausetex.getWidth() != gWidth || pausetex.getHeight() != gHeight) {
      pausetex.setSize(gWidth, gHeight);
    }
    pausetex.setTexture(pausescn);

  }

  //Arcade selection position processing
  public void arcselposition(Entity characterEntity, int characterNumber) {
    float[] positionX = {75f, 190f, 55f}, positionY = {280f, 170f, 60f};
    float scale = 1.25f;

    characterEntity.getComponent(TransformComponent.class).scaleX =
      characterEntity.getComponent(TransformComponent.class).scaleY = scale;
    characterEntity.getComponent(TransformComponent.class).x = positionX[characterNumber];
    characterEntity.getComponent(TransformComponent.class).x = positionY[characterNumber];
  }

  /**
   * Method adds the sprites to the character select screen
   */
  public void setcharsprites(String string, String button) {
    String MAIN_LAYER = "Default";

    for (int i = 0; i < characters.size(); i++) {
      // Check allows the same character to be selected on each side.
      if (characters.get(i) == string && characters.get(2) == null || i >= 3 && characters.get(i) == string) {
        i = 6;
      }
      else {
        if (characters.get(i) == null) {


          characters.set(i, string);
          //charcomposites.set(i, update(view).loadVoFromLibrary(characters.get(i)));
          //charcomposites.set(i, update(view).loadVoFromLibrary(characters.get(i))).layerName = MAIN_LAYER;

          // second half of character processing is commented out here but
          // executes within the
          // loadslot() method. The method executes when the character slot
          // particle is finished

          // charentities.set(i,
          // update(view).entityFactory.createEntity(root.getEntity(),
          // charcomposites.get(slot)));
          // update(view).entityFactory.initAllChildren(update(view).getEngine(),
          // charentities.get(i),
          // charcomposites.get(i).composite);


          switch (i) {
            case 0:
              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(string);
              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
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
              rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              rootEntityWrapper().getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;

              rootEntityWrapper().getChild("playerlbl").getEntity().getComponent(TintComponent.class).color = Color.RED;
              break;
            case 3:
              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(string);
              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("fourthcharpart").getEntity()
                .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 4:
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(string);
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 5:
              rootEntityWrapper().getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(string);
              rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              rootEntityWrapper().getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              rootEntityWrapper().getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              rootEntityWrapper().getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
              break;
            default:
              break;

          }

          i = 6;

          menu_animation.animsel(rootEntityWrapper().getChild("charobject").getChild(button).getEntity());
        }

      }
    }
  }


  //Resume Button Logic?
  //public void resumeButton() { resManager.removePauseMenuResources(); }

  public void prePauseProcessing() {

    /*update(view).getEngine().getSystem(LifeSystem.class).update(Gdx.graphics.getDeltaTime());
    update(view).getEngine().getSystem(SpecialSystem.class).update(Gdx.graphics.getDeltaTime());*/

    //TODO make this method return a texture that will be turned into an item to add to the pause scene's engine
    getscreenshot();
    //load pause scene's root wrapper and set endooffightmenu module's TintComponent's alpha value to 0
    resManager.createPauseScene();
/*
    pauseroot.getChild("endoffightmenu").getChild("pabtn").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          restartmatch();
        }
      });
    pauseroot.getChild("endoffightmenu").getChild("csbtn").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          toCharSel();
        }
      });

    pauseroot.getChild("endoffightmenu").getChild("mmbtn").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void touchUp() {

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void clicked() {
          toMenu();
        }
      });
*/


    // Set the pause menus and screen transition to the correct positioning

    /*pauserootEntityWrapper().getChild("pausemenu").getEntity()
      .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
    pauserootEntityWrapper().getChild("pausemenu").getEntity()
      .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;

    pauserootEntityWrapper().getChild("endoffightmenu").getEntity()
      .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
    pauserootEntityWrapper().getChild("endoffightmenu").getEntity()
      .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;*/

    //ENSURE ENDOFFIGHTMENU SHOWS UP AT END OF FIGHT
    /*if (stage.gameover && isArcade)
    {

      pauserootEntityWrapper().getChild("endoffightmenu").getChild("eof").getEntity().getComponent(TintComponent.class).color.a = 0;
      pauserootEntityWrapper().getChild("endoffightmenu").getChild("pabtn").getEntity()
        .getComponent(TintComponent.class).color.a = 0;
      pauserootEntityWrapper().getChild("endoffightmenu").getChild("csbtn").getEntity()
        .getComponent(TintComponent.class).color.a = 0;
      pauserootEntityWrapper().getChild("endoffightmenu").getChild("mmbtn").getEntity()
        .getComponent(TintComponent.class).color.a = 0;
      // Add Arcade end of game functionality

    }*/
  }

      /*// Begin next arcade stage
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
        setupArcade(characters.get(0));
        genrand();
      }*/

  /*public void renderss() {
    stage.getBatch().begin();
    stage.getBatch().draw(stage.pausetex, 0, 0);
    stage.getBatch().end();
  }*/

  public boolean isTimeToReset(int timer) {
    return timersAndStops[timer][0] == 0;
  }

  public void runTimers() {
    for (int i = 0; i < timersAndStops.length; i++) {
      timersAndStops[i][0] += Gdx.graphics.getDeltaTime();

      if (timersAndStops[i][0] > timersAndStops[i][1])
        timersAndStops[i][0] = 0f;
    }
  }

  public void setStateOfGame(SPFZState state) { this.state = state; }

  public ArrayList<String> listOfCharacters() { return characters; }

  public void startParticleEffect(String particleEffect) { resManager.activateParticleEffect(particleEffect); }

  public void changeCharacterLabel(String characterObject, String newText, Color color) {
    resManager.changeLabel(characterObject, newText, color);
  }

  public void changeCharacterLabelText(String characterObject, String newText) {
    resManager.changeLabelText(characterObject, newText);
  }

  public void changeCharacterLabelTextColor(String characterObject, Color color) {
    resManager.changeLabelTextColor(characterObject, color);
  }
  //Back processing - the button and Android back button on device
}


