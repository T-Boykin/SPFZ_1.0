package com.dev.swapftrz;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.menu.SPFZMenu;
import com.dev.swapftrz.resource.LifeSystem;
import com.dev.swapftrz.resource.SPFZButtonComponent;
import com.dev.swapftrz.resource.SPFZCharButtonComponent;
import com.dev.swapftrz.resource.SPFZCharButtonSystem;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.resource.SPFZParticleDrawableLogic;
import com.dev.swapftrz.resource.SPFZParticleSystem;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.resource.SPFZStageSystem;
import com.dev.swapftrz.resource.SpecialSystem;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.systems.action.data.ParallelData;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class SwapFyterzMain extends ApplicationAdapter implements InputProcessor, GestureListener
{
  public enum State
  {
    RUNNING,
    PAUSED,
    STOPPED,
    ERROR
  }

  private final SPFZResourceManager resourceManager;
  private final AndroidInterfaceLIBGDX android;
  private final SPFZMenu spfzmenu;

  boolean init, isArcade, mode, uicomplete, dialog, charpicked, mute, stageconfirmed, transition, flingup, flingdown,
    inhelp, mnuscn, exit, paused, pmenuloaded, setupstage, optionsup, gamestart, deselect, longclear, partstartone,
    partstarttwo, partstartthree, partstartfour, partstartfive, partstartsix, adjustbright, adjustsound, frmresume,
    stageset, fromss, restart, istraining, moveright, credpress, isloading, strt1, strt2, charfound, inact, slowtime,
    pickedup;

  byte selecttype;
  ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);
  ComponentMapper<SPFZParticleComponent> pc = ComponentMapper.getFor(SPFZParticleComponent.class);
  ComponentMapper<ActionComponent> ac = ComponentMapper.getFor(ActionComponent.class);

  //ComponentMapper<BoundingBoxComponent> boundingBox = ComponentMapper.getFor(BoundingBoxComponent.class);
  ComponentMapper<MainItemComponent> mc = ComponentMapper.getFor(MainItemComponent.class);

  //CompositeItemVO player1char1, player1char2, player1char3, player2char1, player2char2, player2char3;

  //Driver driver;

  List<CompositeItemVO> charcomposites = new ArrayList<CompositeItemVO>();

  Entity p1c1, p1c2, p1c3, p2c1, p2c2, p2c3, fader;
  List<Entity> charentities = new ArrayList<Entity>();

  float soundamount, brightamount, tmpsound, tmpbright, camzoom, zoompoint, endzoom, targetduration, ctargetduration,
    startingduration;
  FPSLogger logger;

  int savebright, stageTime, savescene, pmenuopt, rcount, scenesel, level, paragraph, gWidth,
    gHeight, INTRO = 0, randsecs = 10;

  GestureDetector gd;
  InputMultiplexer im;

  // ItemWrapper grabs all of the entities created within the Overlap2d
  // Application
  ItemWrapper root, pauseroot;

  long credittime, cleartime, restarttime, adtime, sectime;
  Pixmap pixmap, pixmap2;
  SPFZSceneLoader port, land, pause;
  SPFZCharButtonSystem spfzbsystem;
  SPFZStageSystem stagesystem;
  SPFZParticleDrawableLogic logic;
  // Stage needed for Controls when within the fight interface
  SPFZStage stage;
  public SPFZState state;
  Sprite texhel1, texhel2;

  //ShaderProgram shaderProgram;

  String prevScene, view, selectedStage, p1char1, p1char2,
    p1char3, p2char1, p2char2, p2char3, character, storyline, displaytext;

  // String[] opponents = { "spriteball", "spriteballred", "spriteballblack",
  // "spriteblock", "redblotch", "spritepurplex",
  // "walksprite" };

  // String[] stages = { "halloweenstage", "cathedralstage", "clubstage",
  // "egyptstage", "futurestage", "gargoyle",
  // "junglestage", "skullstage", "undergroundstage" };

  String[] savedvals, storysplit;
  String[] names = {"", "", "", "", "", ""};

  List<String> characters = new ArrayList<String>();

  List<String> charsselected = new ArrayList<String>();

  Texture health1, healthout1, tex, stageback, storytex;

  TextureAtlas healthatlas;

  TextureRegion healthy, testregion;

  TextureRegionComponent trc = new TextureRegionComponent();

  SimpleImageVO stageimg = new SimpleImageVO();

  MainItemComponent main;

  TintComponent restarttint = new TintComponent();

  //TransformComponent transform;

  Viewport viewportland, viewportport;

  Vector2 arcplacer = new Vector2();
  Vector3 credits = new Vector3(320, 400 + (int) (400 * .5), 0), tomenu = new Vector3(320, 400 * .5f, 0),
    termov = new Vector3(160f, 700f, 0), treymov = new Vector3(480f, 700f, 0), migmov = new Vector3(160f, 500f, 0),
    mikmov = new Vector3(480f, 500f, 0), credmov = new Vector3(320f, 600f, 0), move = new Vector3(0, 0, 0);

  // Default Constructor - mainly for Desktop setup
  public SwapFyterzMain() {
    resourceManager = new SPFZResourceManager();
    spfzmenu = new SPFZMenu(resourceManager);
    android = null;
  }

  //Constructor set to receive the custom interfacing for Android System
  //Utilization
  public SwapFyterzMain(AndroidInterfaceLIBGDX tools, SPFZResourceManager resManager) {
    resourceManager = resManager;
    android = tools;
    spfzmenu = new SPFZMenu(resourceManager, android);
  }

  //revamped
  public void animateland() {
  }

  public void animatemainmenu(String view) {


    if (view == "portrait")
    {
      buttonsup();
      //flypods();
      //portFlyPods()
    }
    else
    {
      animateland();
    }
  }

  /**
   * Method determines the position for each character selected in Arcade mode
   */
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


  public void arcadeinit() {
    short WORLD_WIDTH = 640;
    short WORLD_HEIGHT = 400;
    String storypath;
    if (isloading)
    {

      if (resourceManager.getManager().update())
      {
        isloading = false;
        if (resourceManager.appDevice() == resourceManager.ANDROID)
        {
          //android.toast("Arcade textures loaded");
        }
        else
        {
          System.out.print("Arcade textures loaded" + "\n");
        }
        if (resourceManager.appDevice() == resourceManager.ANDROID)
        {
          //android.lockOrientation(mode, view);

        }

        //resourceManager.currentScene() = "storyscene";

        // in testing, re-initializing the SceneLoader made
        // this work when switching between landscape
        // scenes.
        land = new SPFZSceneLoader(resourceManager, SwapFyterzMain.this, "", "");

        update(view).loadScene(resourceManager.currentScene(), viewportland);
        root = new ItemWrapper(update(view).getRoot());
        //transform = tc.get(root.getEntity());
        //action = ac.get(root.getEntity());
        setSettings();
        level++;
        // load the 1st texture that will appear on the default layer
        storypath = "arcade/" + storyline + "/" + level + ".png";
        Pixmap pixmap = new Pixmap(Gdx.files.internal(storypath));
        // Pixmap pixmap = new Pixmap((FileHandle) resourceManager.getManager().get(storypath,
        // Texture.class));

        Pixmap pixmap2 = new Pixmap((int) WORLD_WIDTH * resourceManager.projectVO().pixelToWorld,
          (int) WORLD_HEIGHT * resourceManager.projectVO().pixelToWorld, pixmap.getFormat());

        pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, pixmap2.getWidth(),
          pixmap2.getHeight());
        storytex = new Texture(pixmap2);

        pixmap2.dispose();
        pixmap.dispose();

        trc.regionName = storypath;
        testregion = new TextureRegion(storytex, (int) WORLD_WIDTH * resourceManager.projectVO().pixelToWorld,
          (int) WORLD_HEIGHT * resourceManager.projectVO().pixelToWorld);

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

        update(view).entityFactory.createEntity(root.getEntity(), stageimg);
        Actions.addAction(root.getChild("fader").getEntity(), Actions.fadeOut(1f));
        Actions.addAction(root.getChild("textbackground").getEntity(), Actions.scaleTo(3.8f, 1.0f, 1.5f));
        inact = true;
        continuestory();

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

    if (contin && isArcade && root.getChild("arcadetext").getEntity().getComponent(TintComponent.class).color.a == 1.0)
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

      resourceManager.getManager().load(storypath, Texture.class);

    }
    // resourceManager.getManager().finishLoading();

  }

  private void backprocessing() {
    if (mode)
    {
      // prevScene = resourceManager.currentScene();
      if (resourceManager.currentScene() == "stagescene")
      {
        // resourceManager.unloadstage();
      }
      else if (resourceManager.currentScene() == "charselscene")
      {
        // resourceManager.unloadsix();
      }
      else if (resourceManager.currentScene() == "arcadeselscn")
      {
        // resourceManager.unloadarcade();
      }

      // stage select back processing
      //if (resourceManager.currentScene() == "stageselscene" && !stageconfirmed)
      if (resourceManager.currentScene() == "newstagesel" && !stageconfirmed)
      {

        root.getEntity().removeAll();

        // new sceneloader has to be created after removing entities
        // from
        // last screen
        // land = new SceneLoader(resourceManager);
        land = new SPFZSceneLoader(resourceManager, this, "", "");
        // land.engine.removeSystem(update(view).engine.getSystem(ScriptSystem.class));
        // land.engine.removeSystem(update(view).engine.getSystem(PhysicsSystem.class));

        update(view).loadScene(prevScene, viewportland);
        //resourceManager.currentScene() = prevScene;
        inMode();

      }
      else
      {
        if (selectedStage != null && stageconfirmed)
        {
          stage.dispose();
          stage = null;
        }

        selectedStage = null;
        stageconfirmed = false;
        mode = false;

        scenesel = 1;

        root.getEntity().removeAll();

        if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight())
        {


          view = "landscape";
          //resourceManager.currentScene() = "landscene";

          // This may be bad. However it is
          // causing the application to process as
          // expected. Need to figure out the
          // issue
          // as to why the landscape view cannot
          // move to the next scene properly
          // without an initialization.
          land = new SPFZSceneLoader(resourceManager, this, "", "");
          // land.engine.removeSystem(update(view).engine.getSystem(ScriptSystem.class));
          // land.engine.removeSystem(update(view).engine.getSystem(PhysicsSystem.class));

          update(view).loadScene(resourceManager.currentScene(), viewportland);
          root = new ItemWrapper(update(view).getRoot());
          setMainMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else
        {
          scenesel = 1;
          view = "portrait";

          //resourceManager.currentScene() = "sceneone";

          update(view).loadScene(resourceManager.currentScene(), viewportport);
          scenesel++;
          setMainMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
      }
    }
  }

  public void buttonsapt() {
  }

  public void buttonssqz() {
  }

  public void bringscreen() {
    Actions.addAction(root.getChild("ctrlandhud").getChild("faderscreen").getEntity(),
      Actions.sequence(Actions.fadeIn(2f), Actions.fadeOut(2f)));
  }

  public void bringupconfirm() {
    stage.pauseconfirm = true;
    Actions.addAction(pauseroot.getChild("pausemenu").getChild("resumebutton").getEntity(), Actions.fadeOut(.01f));
    Actions.addAction(pauseroot.getChild("pausemenu").getChild("charselbutton").getEntity(), Actions.fadeOut(PAUSE_BTN_SPD));
    Actions.addAction(pauseroot.getChild("pausemenu").getChild("mainmenubutton").getEntity(), Actions.fadeOut(PAUSE_BTN_SPD));

    Actions.addAction(pauseroot.getChild("pausemenu").getChild("yesbtn").getEntity(), Actions.fadeIn(PAUSE_BTN_SPD));
    Actions.addAction(pauseroot.getChild("pausemenu").getChild("nobtn").getEntity(), Actions.fadeIn(PAUSE_BTN_SPD));
  }

  public void buttonsdown() {
    float push;
    float SCALE_BTN = 1.8f;
    byte SCALE_DURATION = 1;
    float separator = .2f;
    float buttontime = .8f;
    if (view == "portrait")
    {
      String[] portbtntags = {"arcbutton", "vsbutton", "trnbutton", "helpbutton", "optbutton", "brightnessbtn",
        "soundbutton", "exitbutton", "yes", "no", "thirtytime", "sixtytime", "ninetytime", "slidebright",
        "slidesound", "revert"};

      if (!dialog || optionsup)
      {
        Actions.addAction(root.getChild("controlboard").getEntity(),
          Actions.sequence(Actions.moveBy(0, 90f, 1f, Interpolation.fade)));
      }

      // adjust the buttons to make them a bit more animated than what they
      // seem

      for (int i = 0; i < 5; i++)
      {

        Actions.addAction(root.getChild(portbtntags[i]).getEntity(),
          Actions.parallel(Actions.moveBy(0, -570f, buttontime, Interpolation.swing), Actions.sequence(
            Actions.parallel(Actions.scaleTo(SCALE_BTN, 2f, SCALE_DURATION), Actions.moveBy(0, 0, SCALE_DURATION)))));

        buttontime = buttontime + separator;
      }

      if (!optionsup)
      {
        Actions.addAction(root.getChild("exitdialog").getEntity(),
          Actions.sequence(Actions.moveBy(0, -570f, 1f, Interpolation.swing)));
      }
    }
    else
    {
      if (!dialog)
      {
        //open dialog
      }
      //shrink buttons

      // push the exit dialog box up for confirmation
      if (!optionsup && dialog)
      {
      }
    }
  }

  /**
   * method contains actions for pushing buttons up for both main menu screens
   */
  public void buttonsup() {
    float push;
    if (view == "portrait")
    {
      if (dialog || optionsup)
      {
      }
      else
      {
      }

      //if (resourceManager.currentScene() == "sceneone")
      {

				/*update(view).getEngine().removeEntity(root.getChild("animcircle").getEntity());
				update(view).getEngine().removeEntity(root.getChild("introcircle").getEntity());
				update(view).getEngine().removeEntity(root.getChild("ttcimage").getEntity());*/

      }

      // adjust the buttons to make them a bit more animated than what they
      // seem

      for (int i = 0; i < 5; i++)
      {
        if (dialog || optionsup)
        {
        }
        else
        {
        }
      }

      if (!optionsup)
      {
        //lowerExitDialog()
      }
    }
    else
    {
      if (!dialog)
      {
      }
      //expand buttons

      // push the exit dialog back down
      if (!optionsup && dialog)
      {
        //lowerExitDialog()
      }
    }

  }

  public void charSel(boolean arcade) {
    spfzbsystem.priority = 0;

    if (!arcade)
    {

      fader = root.getChild("mainslide").getEntity();
    }
    else
    {
      fader = root.getChild("transition").getEntity();
      Actions.addAction(fader, Actions.sequence(Actions.fadeOut(.3f)));
    }
    update(view).engine.addSystem(spfzbsystem);

    final boolean arc = arcade;

    for (int i = 0; i < 6; i++)
    {

      charsselected.set(i, null);
      charcomposites.set(i, null);
      charentities.set(i, null);
    }

    p1char1 = null;
    p1char2 = null;
    p1char3 = null;
    p2char1 = null;
    p2char2 = null;
    p2char3 = null;

    update(view).addComponentsByTagName("button", SPFZButtonComponent.class);
    update(view).addComponentsByTagName("charbtn", SPFZCharButtonComponent.class);

    root.getChild("charobject").getChild("spriteselbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("spriteselbutton", false, null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            setcharasprites("spriteball");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("spriteball", "spriteselbutton");
            }
            pickedup = true;
          }

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });

    root.getChild("charobject").getChild("spriteredbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("spriteredbutton", false, null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            //setcharasprites("spriteballred");
            setcharasprites("zaine");
          }
          else
          {
            if (!pickedup)
            {
              //setcharsprites("spriteballred", "spriteredbutton");
              setcharsprites("zaine", "spriteredbutton");
              pickedup = true;
            }
          }

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });
    root.getChild("charobject").getChild("blotchbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("blotchbutton", false, null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            setcharasprites("redblotch");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("redblotch", "blotchbutton");
            }
            pickedup = true;
          }

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });
    root.getChild("charobject").getChild("purplexbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("purplexbutton", false, null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            setcharasprites("spritepurplex");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("spritepurplex", "purplexbutton");
            }
            pickedup = true;

          }


        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });

    root.getChild("charobject").getChild("spriteblackbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("spriteblackbutton", false,
          // null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;


          if (arc)
          {
            setcharasprites("spriteballblack");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("spriteballblack", "spriteblackbutton");
            }
            pickedup = true;
          }


        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });
    root.getChild("charobject").getChild("blockbutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("blockbutton", false, null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            setcharasprites("hynryck");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("hynryck", "blockbutton");
            }
            pickedup = true;
          }


        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });

    root.getChild("charobject").getChild("walkspritebutton").getEntity().getComponent(SPFZCharButtonComponent.class)
      .addListener(new SPFZCharButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          // if(draggedfrmbtn("walkspritebutton", false,
          // null))
          // {
          // DO NOT PROCESS BUTTON
          // }
          // else
          // {
          charpicked = false;

          if (arc)
          {
            setcharasprites("walksprite");
          }
          else
          {
            if (!pickedup)
            {
              setcharsprites("walksprite", "walkspritebutton");
            }
            pickedup = true;
          }
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {
          pickedup = false;
        }
      });

    root.getChild("deselbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          if (!partstartone && !partstarttwo && !partstartthree && !partstartfour && !partstartfive && !partstartsix)
          {
            longclear = false;
            // if(draggedfrmbtn("walkspritebutton", false,
            // null))
            // {
            // DO NOT PROCESS BUTTON
            // }
            // else
            // {
            charpicked = false;
            deselect = true;
            for (int i = charsselected.size() - 1; i >= 0; i--)
            {
              if (charsselected.get(i) != null && charentities.get(i) != null && deselect)
              {
                //desel.play(1.0f);

                charsselected.set(i, null);
                charcomposites.set(i, null);

                update(view).getEngine().removeEntity(charentities.get(i));

                switch (i)
                {
                  case 0:
                    root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("firstnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("firstnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                    break;
                  case 1:
                    root.getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("secondnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("secondnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                    break;
                  case 2:
                    root.getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("thirdnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("thirdnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                    root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                    root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;

                    // root.getChild("playerlbl").getEntity().getComponent(TintComponent.class).color
                    // = Color.WHITE;
                    break;
                  case 3:
                    root.getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("fourthnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("fourthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                    break;
                  case 4:
                    root.getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("fifthnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("fifthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                    break;
                  case 5:
                    root.getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(null);
                    root.getChild("sixthnullpart").getEntity()
                      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                    root.getChild("sixthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                    root.getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                    root.getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                    root.getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                    break;
                  default:
                    break;
                }
                deselect = false;
              }
            }

          }
        }

        @Override
        public void touchDown() {
          longclear = true;
          cleartime = System.currentTimeMillis();

        }

        @Override
        public void touchUp() {
          longclear = false;

        }
      });

  }

  /**
   * Character Select intro animation
   */
  public void charselIntro() {
    String[] slides = {"translideone", "translidetwo", "translidethree", "translidefour", "translidefive",
      "translidesix", "translideseven", "translideeight", "translidenine", "translideten", "translideeleven",
      "translidetwelve"};
    // Character Select Transition

    if (INTRO == 2)
    {
      //transform = tc.get(root.getEntity());
      //action = ac.get(root.getEntity());

      for (int i = 0; i < 4; i++)
      {
        Actions.addAction(root.getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(800f, -800f, 1f),
          Actions.sequence(Actions.color(Color.WHITE, .3f), Actions.color(Color.BLACK, .3f))));
      }
      for (int i = 4; i < 8; i++)
      {
        Actions.addAction(root.getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(-800f, -800f, 1f),
          Actions.sequence(Actions.color(Color.BLACK, .3f), Actions.color(Color.RED, .3f))));
      }

      for (int i = 8; i < 12; i++)
      {
        Actions.addAction(root.getChild(slides[i]).getEntity(), Actions.parallel(Actions.moveBy(0, 800f, 1f),
          Actions.sequence(Actions.color(Color.RED, .3f), Actions.color(Color.WHITE, .3f))));
      }


      Actions.addAction(root.getChild("mainslide").getEntity(), Actions.fadeOut(1.5f));
      // Actions.addAction(root.getChild("mainslide").getEntity(), Actions.sequence(Actions.color(Color.WHITE, .3f)));
      //   Actions.fadeOut(1.5f)));


    }
  }

  public void chkrstrt() {
    if ((System.currentTimeMillis() - restarttime) * .001f >= 1f)
    {
      restart = false;
      bringupconfirm();
      pmenuopt = 0;
    }
    else
    {
      if (restarttint.color.a != 1f)
      {
        restarttint.color.a += .015f;
      }
    }
  }

  public void clearAll() {

    if ((System.currentTimeMillis() - cleartime) * .001f >= 1f && longclear
      && root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).getText().toString() != "")
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
              root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("firstnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("firstnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 1:
              root.getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("secondnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("secondnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 2:
              root.getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("thirdnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("thirdnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 3:
              root.getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("fourthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("fourthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              break;
            case 4:
              root.getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("fifthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("fifthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              break;
            case 5:
              root.getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(null);
              root.getChild("sixthnullpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("sixthnullpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
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

    if (resourceManager.appDevice() == resourceManager.ANDROID)
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

  /**
   * method runs credit process every 5 seconds
   */
  public void controlCredits() {

    if (resourceManager.currentScene() == "landscene" || view == "portrait")
    {
      //banner logic meshed with fade credits
      if ((System.currentTimeMillis() - credittime) * .001f >= 10)
      {
        fadeCredit();
        credittime = System.currentTimeMillis();
        System.out.println("fade in credits");
      }


    }
  }

  public void controlLights() {
    TransformComponent lighttranscomp = root.getChild("mainlight").getEntity().getComponent(TransformComponent.class);
    if (resourceManager.currentScene() == "landscene")
    {
      if (lighttranscomp.x >= 160f && moveright)
      {
        lighttranscomp.x += .5f;

        if (lighttranscomp.x >= 480f)
        {
          moveright = false;
        }
      }
      if (lighttranscomp.x <= 480f && !moveright)
      {
        lighttranscomp.x -= .5f;

        if (lighttranscomp.x <= 160f)
        {
          moveright = true;
        }
      }
    }

  }


  @Override
  public void create() {


  }

  public void animsel(Entity entity) {
    float DURATION = .1f;
    float scaleY = .43f;
    TransformComponent transform = new TransformComponent();
    DimensionsComponent dimension = new DimensionsComponent();
    ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);
    ComponentMapper<DimensionsComponent> dc = ComponentMapper.getFor(DimensionsComponent.class);
    transform = tc.get(entity);
    dimension = dc.get(entity);


    float origX = transform.scaleX;
    float origY = transform.scaleY;

    float increase = origY + (origY * .5f);


    Actions.addAction(entity,
      Actions.sequence(Actions.parallel(Actions.scaleTo(origX, increase, DURATION, Interpolation.swing)),
        Actions.scaleTo(origX, origY, DURATION, Interpolation.swing)));


  }

  public ParallelData createactions(Entity entity) {
    //SequenceData data = null;
    float DURATION = .2f;
    ParallelData data = null;
    TransformComponent transform = new TransformComponent();
    DimensionsComponent dimension = new DimensionsComponent();
    ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);
    ComponentMapper<DimensionsComponent> dc = ComponentMapper.getFor(DimensionsComponent.class);
    transform = tc.get(entity);
    dimension = dc.get(entity);

    transform.originX -= transform.originX * .5f;

    switch (resourceManager.currentScene())
    {
      case "landscene":
        data = Actions.parallel(Actions.scaleTo(transform.scaleX, 0f, DURATION, Interpolation.swing),
          Actions.moveBy(0f, ((dimension.height * transform.scaleY) * .5f), DURATION, Interpolation.swing));
        //Actions.rotateTo(360f, DURATION));
        // Actions.moveBy(0f, 0f, .175f, Interpolation.circle));
        //data = Actions.sequence(Actions.parallel(Actions.scaleTo(transform.scaleX, 0f, .175f, Interpolation.circle)));
        break;
      case "sceneone":
        data = Actions.parallel(Actions.scaleTo(transform.scaleX, 0f, DURATION, Interpolation.swing),
          Actions.moveBy(0f, ((dimension.height * transform.scaleY) * .5f), DURATION, Interpolation.swing));
        break;
      default:
        data = Actions.sequence(Actions.parallel(Actions.scaleBy(2f, 2f, .2f), Actions.color(Color.RED, .2f)));
        break;
    }

    return data;
  }

	/*void debugRender()
	{
		if (view == "portrait")
		{
			shapeRenderer.setProjectionMatrix(viewportport.getCamera().combined);
			camera = (OrthographicCamera) viewportport.getCamera();
		}
		else
		{
			shapeRenderer.setProjectionMatrix(viewportland.getCamera().combined);
			camera = (OrthographicCamera) viewportland.getCamera();
		}
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);

		@SuppressWarnings("unchecked")
		Family bounding = Family.all(BoundingBoxComponent.class).get();
		ImmutableArray<Entity> entities = update(view).engine.getEntitiesFor(bounding);

		for (Entity entity : entities)
		{
			BoundingBoxComponent boundingbox = boundingBox.get(entity);
			rect = boundingbox.getBoundingRect();
			shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
			for (int i = 0; i < 4; i++)
			{
				shapeRenderer.rect(boundingbox.points[i].x - 2, boundingbox.points[i].y - 2, 4, 4);
			}
		}
		shapeRenderer.setColor(Color.BLUE);

		shapeRenderer.setProjectionMatrix(viewportland.getCamera().combined);
		shapeRenderer.rect(camera.position.x - ((camera.viewportWidth * camera.zoom) / 4),
			camera.position.y - ((camera.viewportHeight * camera.zoom) / 4), (camera.viewportWidth * camera.zoom) / 2,
			(camera.viewportHeight * camera.zoom) / 2);

		shapeRenderer.end();

	}*/

  public boolean concheck(String scene) {
    fader = root.getChild("transition").getEntity();

    if (scene == "landscene" || scene == "sceneone")
    {
      if (fader.getComponent(TintComponent.class).color.a < .8f)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

  /**
   * Method returns a boolean stating that the user has dragged off of the
   * button so we do not process the button
   */
  public boolean draggedfrmbtn(String button, boolean haschildren, String parent) {
    Vector3 vec3 = new Vector3();
    Vector3 transpar = new Vector3(0, 0, 0);
    TransformComponent transcomponent;
    DimensionsComponent dimcomponent;
    Vector2 dimwh = new Vector2();
    if (haschildren)
    {
      if (root.getChild(parent).getEntity() != null)
      {

        transcomponent = ComponentRetriever.get(root.getChild(parent).getEntity(), TransformComponent.class);

        transpar.x = transcomponent.x;
        transpar.y = transcomponent.y;

        transcomponent = ComponentRetriever.get(root.getChild(parent).getChild(button).getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(parent).getChild(button).getEntity(),
          DimensionsComponent.class);
      }
      else
      {
        transcomponent = ComponentRetriever.get(pauseroot.getChild(parent).getEntity(), TransformComponent.class);

        transpar.x = transcomponent.x;
        transpar.y = transcomponent.y;

        transcomponent = ComponentRetriever.get(pauseroot.getChild(parent).getChild(button).getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(pauseroot.getChild(parent).getChild(button).getEntity(),
          DimensionsComponent.class);
      }
    }
    else
    {
      if (root.getChild(button).getEntity() != null)
      {
        transcomponent = ComponentRetriever.get(root.getChild(button).getEntity(), TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(button).getEntity(), DimensionsComponent.class);
        transpar.x = 0;
        transpar.y = 0;
      }
      else
      {
        transcomponent = new TransformComponent();
        dimcomponent = new DimensionsComponent();
      }

    }

    if (view.equals("portrait"))

    {
      viewportport.getCamera().update();
      vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      viewportport.unproject(vec3);
    }
    else

    {
      viewportland.getCamera().update();
      vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      viewportland.unproject(vec3);
    }


    dimwh.x = dimcomponent.width * transcomponent.scaleX;
    dimwh.y = dimcomponent.height * transcomponent.scaleY;


    if (vec3.x >= transcomponent.x + transpar.x && vec3.x <= (transcomponent.x + dimwh.x + transpar.x)
      && vec3.y >= transcomponent.y + transpar.y && vec3.y <= (transcomponent.y + dimwh.y + transpar.y))

    {
      transcomponent = null;
      dimcomponent = null;
      dimwh = null;
      vec3 = null;
      transpar = null;
      return false;
    }
    else

    {
      transcomponent = null;
      dimcomponent = null;
      dimwh = null;
      vec3 = null;
      transpar = null;
      return true;
    }

  }

  /**
   * method fades the credits and arrows in and out
   */
  public void fadeCredit() {
    String[] cdtcomponents = {"swypefrmbtm", "swypefrmtop"};

    // move the credit text and arrows downwards
    if (resourceManager.currentScene().equals("landscene"))
    {
      // move the credit text and arrows downwards
      Actions.addAction(root.getChild(cdtcomponents[1]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
        Actions.parallel(Actions.moveBy(0, -20f, 1f), Actions.fadeOut(1f)), Actions.moveBy(0, 20f, .01f)));

      // move the credit text and arrows upwards
      Actions.addAction(root.getChild(cdtcomponents[0]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
        Actions.parallel(Actions.moveBy(0, 20f, 1f), Actions.fadeOut(1f)), Actions.moveBy(0, -20f, .01f)));

      /*for (int i = 1; i < cdtcomponents.length - 3; i++)
      {
        Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
          Actions.parallel(Actions.moveBy(0, -20f, .5f), Actions.fadeOut(.5f)), Actions.moveBy(0, 20f, .01f)));
      }

      // move the to menu text and arrows upwards
      for (int i = 2; i < cdtcomponents.length; i++)
      {
        Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.sequence(Actions.fadeIn(.05f),
          Actions.parallel(Actions.moveBy(0, 20f, .5f), Actions.fadeOut(.5f)), Actions.moveBy(0, -20f, .01f)));
      }*/

      if (!credpress)
      {
        Actions.addAction(root.getChild("meconst").getChild("medraw").getEntity(),
          Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
      }

    }
    if (view == "portrait")
    {

      Actions.addAction(root.getChild("tocreditsone").getEntity(),
        Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
      Actions.addAction(root.getChild("tocreditstwo").getEntity(),
        Actions.sequence(Actions.fadeIn(.3f), Actions.fadeOut(.3f), Actions.fadeIn(.3f), Actions.fadeOut(.3f)));
    }
  }

  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    // The fling process controls when the user can go to the credits or back to
    // the main menu
    // Current process will allow you to fling up and down only when the user is
    // at the main menu,
    // and if the user has not pressed any of the constellations.

    if (!optionsup && view == "landscape" && ac != null && resourceManager.currentScene() == "landscene")
    {
      if (velocityY > 1000f && !credpress && !transition)
      {
        flingup = true;
        flingdown = false;
      }
      else if (velocityY < -1000f && !credpress && !transition)
      {
        flingdown = true;
        flingup = false;
      }
    }
    return true;
  }

  public void flypods() {
    if (view == "portrait")
    {
      if (!gamestart)
      {
        //portFlyPods
      }
      else
      {
        //fade with portFlyPods
      }
    }
  }

  public void genrand() {
    Random arcsel = new Random();
    byte randchar;
    byte randstg;

    randchar = 0;
    randstg = 0;
    String[] opponents = {"spriteball", "spriteballred", "spriteballblack", "spriteblock", "redblotch",
      "spritepurplex", "walksprite"};
    String[] stages = {"halloweenstage", "cathedralstage", "clubstage", "egyptstage", "futurestage", "gargoyle",
      "junglestage", "skullstage", "undergroundstage"};

    while (randchar == 0 || randchar == 7 && randstg == 0 || randstg == 9)
    {
      randchar = (byte) ((byte) arcsel.nextInt(6) + 1);
      randstg = (byte) ((byte) arcsel.nextInt(8) + 1);
    }

    p1char1 = charsselected.get(0);
    p1char2 = charsselected.get(1);
    p1char3 = charsselected.get(2);

    charsselected.set(3, opponents[randchar]);
    charsselected.set(4, opponents[randchar]);
    charsselected.set(5, opponents[randchar]);

    p2char1 = opponents[randchar];
    p2char2 = opponents[randchar];
    p2char3 = opponents[randchar];

    charcomposites.set(3, update(view).loadVoFromLibrary(charsselected.get(3)));
    // charcomposites.set(3,
    // update(view).loadVoFromLibrary(charsselected.get(3))).layerName =
    // MAIN_LAYER;

    charcomposites.set(4, update(view).loadVoFromLibrary(charsselected.get(4)));
    // charcomposites.set(4,
    // update(view).loadVoFromLibrary(charsselected.get(4))).layerName =
    // MAIN_LAYER;

    charcomposites.set(5, update(view).loadVoFromLibrary(charsselected.get(5)));
    // charcomposites.set(5,
    // update(view).loadVoFromLibrary(charsselected.get(5))).layerName =
    // MAIN_LAYER;

    charentities.set(3, update(view).entityFactory.createEntity(root.getEntity(), charcomposites.get(3)));
    charentities.set(4, update(view).entityFactory.createEntity(root.getEntity(), charcomposites.get(4)));
    charentities.set(5, update(view).entityFactory.createEntity(root.getEntity(), charcomposites.get(5)));

    selectedStage = stages[randstg];
    stageconfirmed = true;

    arcsel = null;
  }

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

  public void init() {

    //logger = new FPSLogger();
    //GLProfiler.enable();

    byte AFTER_INTRO = 8;
    //short WORLD_WIDTH = 640;
    //short WORLD_HEIGHT = 400;
    gWidth = Gdx.graphics.getWidth();
    gHeight = Gdx.graphics.getHeight();
    storyline = null;
    /*// During initialization, figure out how we are running this application
    switch (Gdx.app.getType())
    {
      case Android:
        resourceManager.appDevice() = ANDROID;
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        break;
      case Desktop:
        resourceManager.appDevice() = DESKTOP;
        break;
      default:
        break;
    }*/

    state = SPFZState.RUNNING;

    //if (resourceManager.appDevice() == resourceManager.ANDROID)
    //{
    // On initial install. Create the spfzfile by writing values.

    //}

    // Initializations
    init = true;

    if (//mainmenu.getVolume() > 0f)
    {
      mute = true;
    }

    // set screen for inputs and set the starting point for the main menu
    // screen
    // music and start music
    //mainmenu.setPosition(AFTER_INTRO);
    resourceManager.setManager(resourceManager.getManager());
    // initialize the resource manager

    // spfzbsystem = new SPFZButtonSystem(this);
    spfzbsystem = new SPFZCharButtonSystem();
    stagesystem = new SPFZStageSystem();

    //resourceManager = new SPFZResourceManager(this);
    // initialize viewports
    //viewportland = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
    //viewportport = new StretchViewport(WORLD_HEIGHT, WORLD_WIDTH);
    //resourceManager.currentScene() = "landscene";

    //port = new SPFZSceneLoader(resourceManager, this, "", "");
    //land = new SPFZSceneLoader(resourceManager, this, "", "");
    //pause = new SPFZSceneLoader(resourceManager, this, "", "");
    logic = (SPFZParticleDrawableLogic) land.renderer.drawableLogicMapper.getDrawable(7);


    // initialize character arrays for Character Select Processing
    for (int i = 0; i < 6; i++)
    {
      charsselected.add(null);
      charcomposites.add(null);
      charentities.add(null);
    }

    // initialize characters
    for (int i = 0; i < 6; i++)
    {
      characters.add(null);
    }


    //}

    // Set the GestureDetector in order to utilize the camera lerping function
    // for Credits
    im = new InputMultiplexer();
    gd = new GestureDetector(this);

    im.addProcessor(gd);
    im.addProcessor(this);

    // initialize all resources

    resourceManager.initAllResources();

    // Initialize the first scene

    prevScene = "landscene";

  }

  public void inMode() {

    // the view will have to be in landscape therefore we will be
    // setting the landscape view up for the back button.
    root.getEntity().removeAll();
    root = new ItemWrapper(update(view).getRoot());
    main = mc.get(root.getEntity());

    update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

    setSettings();
    // When we are at the character select scene we want to process so
    // we can
    // move forward
    // to the Stage Select scene
    //if (resourceManager.currentScene() != "arcadeselscn" && resourceManager.currentScene() != "stageselscene")
    if (resourceManager.currentScene() != "arcadeselscn" && resourceManager.currentScene() != "newstagesel")
    {
      fader = root.getChild("transition").getEntity();

      //process will determine which title will be set between Training Mode and Character Select Screen
      if (selecttype == 0)
      {
        root.getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 351;
        root.getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 420;
      }
      else
      {
        root.getChild("tmpng").getEntity().getComponent(TransformComponent.class).y = 351;
        root.getChild("csspng").getEntity().getComponent(TransformComponent.class).y = 420;
      }

      charselIntro();
      charSel(false);
      root.getChild("okaybutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (resourceManager.currentScene() == "charselscene")
            {
              p1char1 = charsselected.get(0);
              p1char2 = charsselected.get(1);
              p1char3 = charsselected.get(2);
              p2char1 = charsselected.get(3);
              p2char2 = charsselected.get(4);
              p2char3 = charsselected.get(5);

              if (p2char3 == null)
              {

              }
              else
              {
                ok.play(1.0f);
                Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
                {

                  @Override
                  public void run() {

                    root.getEntity().removeAll();
                    ////resourceManager.currentScene() = "stageselscene";
                    //resourceManager.currentScene() = "newstagesel";

                    land = new SPFZSceneLoader(resourceManager, SwapFyterzMain.this, "", "");
                    // land.engine.removeSystem(update(view).engine.getSystem(ScriptSystem.class));
                    // land.engine.removeSystem(update(view).engine.getSystem(PhysicsSystem.class));

                    update(view).loadScene(resourceManager.currentScene(), viewportland);
                    root = new ItemWrapper(update(view).getRoot());
                    stageSel();
                  }
                })));

              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
    }
    else if (resourceManager.currentScene() == "arcadeselscn")
    {

      charSel(true);
    }

    // If a stage has been selected and "OK" has been pressed, setup for
    // the
    // stage selected
    // and set the stage run variable to true to process the camera
    // within the
    // screen

    root.getChild("backbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          back.play(1.0f);
          Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {

              state = SPFZState.RESUME;
              backprocessing();
            }
          })));

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });

  }

  /**
   * method contains the intro animation
   */
  public void Intro() {
    // //mainmenu.play();
    //Animation process:
    /* 1. on intro,
     * 2. fader will
     */
    String[] cdtcomponents = {"ttcimage", "swypefrmbtm", "swypefrmtop"};
    if (resourceManager.currentScene().equals("sceneone") && init)
    {
     /* //Actions.addAction(root.getChild("ttcimage").getEntity(), Actions.fadeOut(0f));

      // screen transition
      Actions.addAction(fader,
        Actions.sequence(Actions.delay(3f), Actions.parallel(Actions.fadeOut(0f), Actions.run(new Runnable()
        {

          @Override
          public void run() {
            //mainmenu.play();
          }
        })), Actions.run(new Runnable()
        {

          @Override
          public void run() {
            Actions.addAction(root.getChild("animcircle").getEntity(), Actions.sequence(Actions.delay(1.5f),
              Actions.parallel(Actions.scaleBy(6f, 6f, .7f), Actions.moveBy(0, 0, .3f), Actions.fadeOut(2f))));
            Actions.addAction(root.getChild("introcircle").getEntity(), Actions.sequence(Actions.delay(3.5f), Actions
              .parallel(Actions.scaleBy(40f, 40f, 2.2f), Actions.moveBy(0, -2400f, 2.2f), Actions.run(new Runnable()
              {

                @Override
                public void run() {
                  uicomplete = true;
                }
              }))));

            Actions.addAction(root.getChild("ttcimage").getEntity(),
              Actions.sequence(Actions.delay(8f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
                Actions.fadeOut(.4f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
                Actions.fadeOut(.4f), Actions.fadeIn(1f)));
          }
        })));


      init = false;*/
    }

    if (resourceManager.currentScene() == "landscene" && init)
    {

      for (int i = 0; i < cdtcomponents.length; i++)
      {
        Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.fadeOut(0f));
      }

      // //mainmenu.play();

      // screen transition
      Actions.addAction(fader,
        Actions.sequence(Actions.delay(3f), Actions.parallel(Actions.fadeOut(0f), Actions.run(new Runnable()
        {

          @Override
          public void run() {
            //mainmenu.play();
          }
        })), Actions.run(new Runnable()
        {

          @Override
          public void run() {
            Actions.addAction(root.getChild("animscn").getEntity(), Actions.sequence(Actions.delay(1.5f),
              Actions.parallel(Actions.scaleBy(6f, 6f, .7f), Actions.moveBy(0f, 0f, .3f), Actions.fadeOut(1f))));
            Actions.addAction(root.getChild("lintro").getEntity(),
              Actions.sequence(Actions.delay(3.5f), Actions.parallel(Actions.scaleBy(100f, 100f, 2.2f),
                Actions.moveBy(-150f, -200f, 2.2f), Actions.run(new Runnable()
                {

                  @Override
                  public void run() {
                    uicomplete = true;
                  }
                }))));

            Actions.addAction(root.getChild("ttcimage").getEntity(),
              Actions.sequence(Actions.delay(8f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
                Actions.fadeOut(.4f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
                Actions.fadeOut(.4f), Actions.fadeIn(1f)));
          }
        })));


      init = false;
    }

    INTRO += 1;
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  public void loadslot(int position, boolean arcade) {

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
    charentities.set(position, update(view).entityFactory.createEntity(root.getEntity(), charcomposites.get(position)));
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

  @Override
  public boolean longPress(float x, float y) {

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {

    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }

  /**
   * method controls the elements within the intro
   */
  public void omitintro() {

    update(view).getEngine().removeEntity(root.getChild("animcircle").getEntity());
    update(view).getEngine().removeEntity(root.getChild("introcircle").getEntity());
    // rmvintro = true;

  }

  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {

    return false;
  }

  @Override

  public boolean panStop(float x, float y, int pointer, int button) {

    return false;
  }

  @Override
  public void pause() {
    if (!exit)
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
        stageset = false;
        state = SPFZState.PAUSE;
        pausing();
      }
    }
  }

  public void pauseoptions() {

    pauseroot = new ItemWrapper(pause.getRoot());
    //transform = tc.get(pauseroot.getEntity());
    //action = ac.get(pauseroot.getEntity());

    pause.addComponentsByTagName("button", SPFZButtonComponent.class);

    if (paused)
    {
      pauseroot.getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 0f;
      pauseroot.getChild("pausemenu").getEntity().getComponent(TintComponent.class).color.a = 1f;

      pauseroot.getChild("pausemenu").getChild("resumebutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("resumebutton", true, "pausemenu"))
              {

              }
              else
              {
                resumefrmpause();
              }
            }
          }

          @Override
          public void touchDown() {
            restart = true;
            restarttime = System.currentTimeMillis();
            restarttint = pauseroot.getChild("pausemenu").getChild("resumebutton").getChild("restart").getEntity()
              .getComponent(TintComponent.class);

          }

          @Override
          public void touchUp() {
            if (restarttint.color.a <= .9f)
            {
              restart = false;
              Actions.addAction(
                pauseroot.getChild("pausemenu").getChild("resumebutton").getChild("restart").getEntity(),
                Actions.fadeOut(.01f));
            }
          }
        });
      pauseroot.getChild("pausemenu").getChild("charselbutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            // Bring up a confirmation to ensure user wants to go to character
            // select
            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("charselbutton", true, "pausemenu"))
              {

              }
              else
              {
                pmenuopt = 1;
                bringupconfirm();
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });

/*      pauseroot.getChild("pausemenu").getChild("movesetbtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked()
          {
            if (!stage.pauseconfirm)
            {
              // Bring up the moveset for each character that has been
              // selected
              // for the fight
              if (draggedfrmbtn("movesetbtn", true, "pausemenu"))
              {

              }
              else
              {

              }
            }
          }

          @Override
          public void touchDown()
          {

          }

          @Override
          public void touchUp()
          {

          }
        });*/

      pauseroot.getChild("pausemenu").getChild("mainmenubutton").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (!stage.pauseconfirm)
            {
              if (draggedfrmbtn("mainmenubutton", true, "pausemenu"))
              {

              }
              else
              {
                pmenuopt = 2;
                bringupconfirm();
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
      pauseroot.getChild("pausemenu").getChild("yesbtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {
            fader = pauseroot.getChild("fader").getEntity();
            if (stage.pauseconfirm)
            {
              if (draggedfrmbtn("yesbtn", true, "pausemenu"))
              {

              }
              else
              {
                switch (pmenuopt)
                {
                  case 0:

                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        //stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        restartmatch();
                      }
                    })));

                    break;
                  case 1:
                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        //stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        toCharSel();
                      }
                    })));
                    break;
                  case 2:
                    Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                    {

                      @Override
                      public void run() {
                        stoprender = true;
                        Gdx.gl.glClearColor(0, 0, 0, 1);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        toMenu();
                      }
                    })));
                    break;
                  default:
                    break;
                }
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });
      pauseroot.getChild("pausemenu").getChild("nobtn").getEntity().getComponent(SPFZButtonComponent.class)
        .addListener(new SPFZButtonComponent.ButtonListener()
        {

          @Override
          public void clicked() {

            if (stage.pauseconfirm)
            {
              if (draggedfrmbtn("nobtn", true, "pausemenu"))
              {

              }
              else
              {
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("resumebutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("charselbutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));
                //Actions.addAction(pauseroot.getChild("pausemenu").getChild("movesetbtn").getEntity(),
                //  Actions.fadeIn(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("mainmenubutton").getEntity(),
                  Actions.fadeIn(PAUSE_BTN_SPD));

                Actions.addAction(pauseroot.getChild("pausemenu").getChild("yesbtn").getEntity(),
                  Actions.fadeOut(PAUSE_BTN_SPD));
                Actions.addAction(pauseroot.getChild("pausemenu").getChild("nobtn").getEntity(),
                  Actions.fadeOut(PAUSE_BTN_SPD));

                stage.pauseconfirm = false;
              }
            }
          }

          @Override
          public void touchDown() {

          }

          @Override
          public void touchUp() {

          }
        });

    }
    else
    {
      pauseroot.getChild("pausemenu").getEntity().getComponent(TintComponent.class).color.a = 0f;
      pauseroot.getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 1f;
      seteofbtns();
    }
  }

  public void pausing() {
    short HALF_WORLDW = 320;
    short HALF_WORLDH = 200;

    if (pauseroot != null && !stage.gameover)
    {
      pauseroot.getChild("endoffightmenu").getEntity().getComponent(TintComponent.class).color.a = 0;
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

      pause.loadScene("pausescene", viewportland);
      viewportland.getCamera().position.set(stage.stagetempx, stage.stagetempy, 0);
      pauseoptions();

      // Set the pause menus and screen transition to the correct positioning

      pauseroot.getChild("pausemenu").getEntity()
        .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
      pauseroot.getChild("pausemenu").getEntity()
        .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;

      pauseroot.getChild("endoffightmenu").getEntity()
        .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - HALF_WORLDW;
      pauseroot.getChild("endoffightmenu").getEntity()
        .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - HALF_WORLDH;

      if (stage.gameover && isArcade)
      {

        pauseroot.getChild("endoffightmenu").getChild("eof").getEntity().getComponent(TintComponent.class).color.a = 0;
        pauseroot.getChild("endoffightmenu").getChild("pabtn").getEntity()
          .getComponent(TintComponent.class).color.a = 0;
        pauseroot.getChild("endoffightmenu").getChild("csbtn").getEntity()
          .getComponent(TintComponent.class).color.a = 0;
        pauseroot.getChild("endoffightmenu").getChild("mmbtn").getEntity()
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
      pause.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    if (stage != null)
    {

      stage.pausedElapsed = System.currentTimeMillis();
      stage.firstpause = String.valueOf(stage.pausedElapsed).substring(0, 1);
      // Begin next arcade stage
      if (isArcade && stage.gameover && Gdx.input.isTouched())
      {
        paused = false;
        pause.engine.removeAllEntities();
        pause.entityFactory.clean();
        land.engine.removeAllEntities();
        land.entityFactory.clean();
        root.getEntity().removeAll();

        land = new SPFZSceneLoader(resourceManager, SwapFyterzMain.this, "", "");
        stage = null;

        state = SPFZState.RESUME;
        pmenuloaded = false;
        isloading = false;
        setupArcade(charsselected.get(0));
        genrand();
      }
    }
  }


  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

    return false;
  }

  public void pinchStop() {
  }

  public void processback() {
    if (credpress)
    {
      credpress = false;
    }

    if (!optionsup)
    {
      backprocessing();
    }

  }

  public void processcharselect() {
    if (partstartone)
    {

      if (root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(0, isArcade);
        partstartone = false;
      }
    }
    if (partstarttwo)
    {

      if (root.getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(1, isArcade);
        partstarttwo = false;
      }
    }
    if (partstartthree)
    {
      if (root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(2, isArcade);
        partstartthree = false;
      }
    }
    if (partstartfour)
    {
      if (root.getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(3, isArcade);
        partstartfour = false;
      }
    }
    if (partstartfive)
    {
      if (root.getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(4, isArcade);
        partstartfive = false;
      }
    }
    if (partstartsix)
    {
      if (root.getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).pooledeffects.get(0)
        .isComplete())
      {

        loadslot(5, isArcade);
        partstartsix = false;
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

  public void getAnimData() {
  }

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
          // update(view).entityFactory.createEntity(root.getEntity(),
          // charcomposites.get(slot)));
          // update(view).entityFactory.initAllChildren(update(view).getEngine(),
          // charentities.get(i),
          // charcomposites.get(i).composite);


          switch (i)
          {
            case 0:
              root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartone = true;
              break;
            case 1:
              root.getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              root.getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstarttwo = true;
              break;
            case 2:
              root.getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;

              root.getChild("playerlbl").getEntity().getComponent(TintComponent.class).color = Color.RED;
              partstartthree = true;
              break;
            case 3:
              root.getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              root.getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartfour = true;
              break;
            case 4:
              root.getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
              root.getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
              partstartfive = true;
              break;
            case 5:
              root.getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(charsselected.get(i));
              root.getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
              root.getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

              root.getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              root.getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
              root.getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
              root.getChild("cpupng").getEntity().getComponent(TintComponent.class).color = Color.BLUE;

              partstartsix = true;
              break;
            default:
              break;

          }
        }
      }
    }
  }

  public void render() {

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    if (Gdx.input.getInputProcessor() != im && resourceManager.currentScene() != "stageselscn")
    {
      Gdx.input.setInputProcessor(im);
    }

    if (paused)
    {
      state = SPFZState.PAUSE;
    }

    switch (state)
    {
      case RUNNING:

        running();
        break;

      case STOPPED:

        break;

      case PAUSE:
        if (stage == null)
        {
          state = SPFZState.RESUME;
        }
        else
        {
          pausing();
        }
        break;

      case RESUME:
        state = SPFZState.RUNNING;
        break;

    }
    if (stage == null)
    {
      UIProcessing();
    }
    else
    {
      if (Gdx.input.isKeyJustPressed(Keys.BACK))
      {
        paused = true;
      }
    }

    logger.log();


    // logger.log();
    // System.ou.println("Number of draw calls: " + GLProfiler.drawCalls);
    // System.out.println("Number of texture bindings: " +
    // GLProfiler.textureBindings);
    // System.out.println("Number of vertices rendered: " +
    // GLProfiler.vertexCount.total);
    // GLProfiler.reset();

  }

  public void renderss() {
    stage.getBatch().begin();
    stage.getBatch().draw(stage.pausetex, 0, 0);
    stage.getBatch().end();
  }

  public void continuestory() {
    readnext(storyline);

    if (displaytext != "")
    {
      if (paragraph == 1)
      {
        Actions.addAction(root.getChild("arcadetext").getEntity(),
          Actions.sequence(Actions.delay(1.5f), Actions.fadeOut(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              root.getChild("arcadetext").getEntity().getComponent(LabelComponent.class).setText(displaytext);

            }
          }), Actions.fadeIn(1f)));
      }
      else
      {
        Actions.addAction(root.getChild("arcadetext").getEntity(),
          Actions.sequence(Actions.fadeOut(.8f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              root.getChild("arcadetext").getEntity().getComponent(LabelComponent.class).setText(displaytext);

            }
          }), Actions.fadeIn(1f)));
      }
    }
    else
    {
      //close the storytext scene and create the new level for the main character(1st character selected)
      Actions.addAction(root.getChild("fader").getEntity(), Actions.sequence(Actions.fadeIn(1f),
        Actions.delay(.2f), Actions.run(new Runnable()
        {
          @Override
          public void run() {
            createstage();
          }
        })));

      // isArcade = false;
      // isArcade = true;
      inact = false;
    }
  }

  public void procname(int name, int pos) {
    if (pos <= 3)
    {

    }
    else
    {

    }
  }

  public void resize(int width, int height) {

    //first thing, check/update state on resize
    int gWidth;
    int gHeight;
    gWidth = width;
    gHeight = height;

    // needs to be commented out once testing is done
    uicomplete = true;
    if (state != SPFZState.PAUSE)
    {

      if (width > height)
      {
        view = "landscape";

        if (stage != null)
        {
          stage.getViewport().update(width, height);
        }

        // for now we are setting uicomplete to true to clear events for
        // the
        // portrait view
        // uicomplete = true;

        // If the user is within the main menu screens
        if (!frmresume)
        {
          if (!mode)
          {

            //resourceManager.currentScene() = "landscene";

            update(view).loadScene(resourceManager.currentScene(), viewportland);

            root = new ItemWrapper(update(view).getRoot());
            setMainMenu(gWidth, gHeight);

          }
          else
          {

            land.engine.getSystem(SPFZParticleSystem.class).setscene(resourceManager.currentScene());
            update(view).loadScene(resourceManager.currentScene(), viewportland);
            inMode();
          }
        }
        else
        {
          frmresume = false;
        }

      }
      else
      {

        prevScene = resourceManager.currentScene();
        view = "portrait";

        // Main Menu Portrait Screen processing to switch to the five
        // different
        // views
        // Setup for new SceneLoader is needed for whenever the game is
        // resetting
        // back to the portrait
        // version of the main menu
        if (!mode)
        {

          if (scenesel == 4)
          {
            scenesel = 0;

          }
          if (frmresume)
          {
            scenesel = savescene;
            frmresume = false;
          }
          else
          {
            //resourceManager.currentScene() = "sceneone";

            // port SceneLoader must be re-initialized in order for the
            // portrait
            // view to recover from the landscape lock

            port = new SPFZSceneLoader(resourceManager, this, "", "");
            // port.engine.removeSystem(update(view).engine.getSystem(ScriptSystem.class));
            // port.engine.removeSystem(update(view).engine.getSystem(PhysicsSystem.class));
            // port.engine.removeSystem(update(view).engine.getSystem(CompositeSystem.class));
            update(view).loadScene(resourceManager.currentScene(), viewportport);
            // resourceManager.unLoadScene(prevScene);
            // resourceManager.unLoadSceneVO(prevScene);

            // resourceManager.initScene("landscene");
            root = new ItemWrapper(update(view).getRoot());
            prevScene = resourceManager.currentScene();

            setMainMenu(width, height);

            // if (gamestart)
            // {
            // scenesel++;
            // }

          }
        }
      }

    }
    else
    {
      pause.loadScene("pausescene", viewportland);

    }

    logic.setscene(resourceManager.currentScene());
    /*
     * @SuppressWarnings("unchecked") ImmutableArray<Entity> dimensionEntities =
     * update(view).engine.getEntitiesFor(Family.all(DimensionsComponent.class).
     * get()); for (Entity entity : dimensionEntities) { entity.add(new
     * BoundingBoxComponent()); }
     */


  }

  public void restartmatch() {
    float[] bounds = {80, 560};
    // Match restart logic here

    stage.dispose();
    resumefrmpause();

    if (stage != null)
    {
      stage = null;
    }

    update(view).loadScene("stagescene", viewportland);

    root = new ItemWrapper(update(view).getRoot());
    //transform = tc.get(root.getEntity());
    //action = ac.get(root.getEntity());

    setupstage();

    stage = new SPFZStage(update(view).getRm(), viewportland.getCamera(), selectedStage, resourceManager.appDevice(), bounds, characters,
      SwapFyterzMain.this, istraining);
  }

  public void resume() {
    pmenuloaded = false;
    if (!paused)
    {
      state = SPFZState.RESUME;
      frmresume = true;
      if (stage != null)
      {
        // if (stage.pausecam != null)
        // {
        // viewportland.getCamera().position.set(stage.pausecam);
        // }
        // stage.time = System.currentTimeMillis();
        stage.time = System.currentTimeMillis() - (Long.parseLong(stage.firstpause) - stage.pausedElapsed);
      }
    }
  }

  // method handles what actions will take place when the back option is
  // selected

  // method handles the pause menu when in a match

  public void resumefrmpause() {
    paused = false;
    pause.engine.removeAllEntities();
    pause.entityFactory.clean();
    state = SPFZState.RESUME;
    pmenuloaded = false;
    stage.pausetime = false;

    // not correct exactly but is working for now set the time back to the
    // correct seconds for the unpausing
    stage.time = System.currentTimeMillis() - ((stage.optime - stage.timeleft) * 1000);

    // stage.time = System.currentTimeMillis() -
    // (Long.parseLong(stage.firstpause) - stage.pausedElapsed);

  }

  public void running() {
    if (isArcade)
    {
      arcadeprocessing();
    }

    // update the camera
    if (view == "landscape")
    {
      viewportland.apply();
    }
    else
    {
      viewportport.apply();
    }

    if (state != SPFZState.PAUSE)
    {

      //if(adtime < System.currentTimeMillis() + Math.random(20))
      //{

      displayAD(resourceManager.currentScene(), adtime, randsecs);
      if (resourceManager.appDevice() == resourceManager.ANDROID && Gdx.input.isTouched())
      {
        //android.hideAD();
      }

      //}
      // > ----------- RENDER AND UPDATE ENGINE LINE
      // ------------ <

      // update method returns an Overlap2d Scene and renders each entity

      // slow downs will need to be implemented by multiplying an amount * Delta
      if (slowtime)
      {
        update(view).getEngine().update(Gdx.graphics.getDeltaTime() * 0.8f);
      }
      else
      {

        update(view).getEngine().update(Gdx.graphics.getDeltaTime());
      }

      // > ----------------------------------- PROCESS STAGE
      // ---------------------------------- <

      // STAGE SHOULD BE RENDERED LAST AS THE STAGE NEEDS TO BE OVER THE STAGE
      // BACKGROUND

      // STAGE BACKGROUND IS AN ENTITY THAT IS ADDED TO THE SCENE

      if (stage != null)
      {
        stageprocessing();

        if (Gdx.input.isKeyJustPressed(Keys.T))
        {
          if (!slowtime)
          {
            slowtime = true;
            stage.spfzp1move.animationstate().paused = true;
          }
          else
          {
            slowtime = false;
            stage.spfzp1move.animationstate().paused = false;
          }
        }
      }

      // debugRender();
      // > ---------------------------- END OF STAGE PROCESSING
      // ------------------------------- <
    }

    // loop main menu music
    // if (//mainmenu.getPosition() > ENDOFMAIN)
    // {
    // //mainmenu.setPosition(LOOP_MUSIC);
    // }

    // > ------------------- HANDLE INITIALIZATION PROCESS
    // ---------------------------------- <

    if (Gdx.input.isTouched() && INTRO == 1 && uicomplete == true || Gdx.input.isTouched() && !gamestart && INTRO == 2)
    {
      gamestart = true;
      // recall setmainmenu method to pull up the buttons after intro
      // stuff
      // has
      // been handled
      setMainMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    // > --------------------- END OF INITIALIZATION PROCESS
    // -------------------------------- <

    // > ------------------------PROCESS CHARACTER SELECT SCREEN
    // ---------------------------- <
    if (resourceManager.currentScene() == "charselscene" || resourceManager.currentScene() == "arcadeselscn")
    {
      processcharselect();

      if (longclear)
      {
        clearAll();
      }
    }

    // > ----------------- END OF CHARACTER SELECT SCREEN PROCESSING
    // ------------------------ <

  }

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
             * update(view).entityFactory.createEntity(root.getEntity(),
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
                root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstartone = true;
                break;
              case 1:
                root.getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("secondcharpart").getEntity()
                  .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstarttwo = true;
                break;
              case 2:
                root.getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1;
                root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstartthree = true;
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

  /**
   * Method adds the sprites to the character select screen
   */
  public void setcharsprites(String string, String button) {
    String MAIN_LAYER = "Default";

    for (int i = 0; i < charsselected.size(); i++)
    {
      // Check allows the same character to be selected on each side.
      if (charsselected.get(i) == string && charsselected.get(2) == null || i >= 3 && charsselected.get(i) == string)
      {
        i = 6;
        charpicked = true;
      }
      else
      {

        character = string;
        if (charsselected.get(i) == null)
        {

          if (!charpicked)
          {
            charsselected.set(i, string);
            charcomposites.set(i, update(view).loadVoFromLibrary(charsselected.get(i)));
            charcomposites.set(i, update(view).loadVoFromLibrary(charsselected.get(i))).layerName = MAIN_LAYER;

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


            switch (i)
            {
              case 0:
                root.getChild("charonelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("firstcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstartone = true;
                break;
              case 1:
                root.getChild("chartwolbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("secondcharpart").getEntity()
                  .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("secondcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstarttwo = true;
                break;
              case 2:
                root.getChild("charthreelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("thirdcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                root.getChild("charonelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                root.getChild("chartwolbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;

                root.getChild("playerlbl").getEntity().getComponent(TintComponent.class).color = Color.RED;
                partstartthree = true;
                break;
              case 3:
                root.getChild("charfourlbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("fourthcharpart").getEntity()
                  .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("fourthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstartfour = true;
                break;
              case 4:
                root.getChild("charfivelbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.GRAY;
                root.getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("fifthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
                partstartfive = true;
                break;
              case 5:
                root.getChild("charsixlbl").getEntity().getComponent(LabelComponent.class).setText(string);
                root.getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
                root.getChild("sixthcharpart").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

                root.getChild("charfourlbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                root.getChild("charfivelbl").getEntity().getComponent(TintComponent.class).color = Color.WHITE;
                root.getChild("cpulbl").getEntity().getComponent(TintComponent.class).color = Color.BLUE;
                partstartsix = true;
                break;
              default:
                break;

            }

            i = 6;

            animsel(root.getChild("charobject").getChild(button).getEntity());

          }
        }
      }
    }
  }

  public void seteofbtns() {
    pauseroot.getChild("endoffightmenu").getChild("pabtn").getEntity().getComponent(SPFZButtonComponent.class)
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

  }

  public void setMainMenu(int w, int h) {
    istraining = false;
    optionsup = false;
    dialog = false;
    exit = false;
    transition = false;
    flingup = false;
    flingdown = false;
    credittime = System.currentTimeMillis();
    adtime = System.currentTimeMillis();
    sectime = System.currentTimeMillis();
    // When coming back to the main menu, unlock the orientation
    if (resourceManager.appDevice() == resourceManager.ANDROID)
    {
      //android.lockOrientation(false, view);
    }
    // setup the buttons for the portrait view
    if (h > w)
    {
      setupportrait();
    }
    // Setup the buttons for the landscape view
    else
    {
      setuplandscape();
      //im.removeProcessor(gd);
      //im.addProcessor(gd);
    }

  }

  public void setSettings() {
    readOut("spfzfile");
    //mainmenu.setVolume(soundamount);
    adjustBrightness(brightamount);
  }

  public void setsliders(String composite) {
    TransformComponent transcomponent = ComponentRetriever
      .get(root.getChild(composite).getChild("brightslider").getEntity(), TransformComponent.class);
    DimensionsComponent dimcomponent = ComponentRetriever
      .get(root.getChild(composite).getChild("brightslider").getEntity(), DimensionsComponent.class);
    TransformComponent transcomp = ComponentRetriever.get(root.getChild(composite).getChild("brightbar").getEntity(),
      TransformComponent.class);
    DimensionsComponent dimcompon = ComponentRetriever.get(root.getChild(composite).getChild("brightbar").getEntity(),
      DimensionsComponent.class);
    Vector2 dimwhs = new Vector2();
    Vector2 dimwh = new Vector2();
    Vector3 transpar = new Vector3(0, 0, 0);
    final short MAX_VOL = 1;
    final short MAX_BRIGHT = 255;
    float fullbarpercent;

    // set bright
    // slider---------------------------------------------------------------

    transpar.x = transcomponent.x;
    transpar.y = transcomponent.y;

    dimwh.x = dimcomponent.width * transcomponent.scaleX;
    dimwh.y = dimcomponent.height * transcomponent.scaleY;
    dimwhs.x = dimcompon.width * transcomp.scaleX;
    dimwhs.y = dimcompon.height * transcomp.scaleY;

    // bar full percentage
    fullbarpercent = dimwhs.x;


    // brightamount is received from the flavor in prefs
    brightamount = 100 * (brightamount / MAX_BRIGHT);

    transcomponent.x = (transcomp.x - (dimwh.x * .5f)) + (float) ((brightamount * .01) * fullbarpercent);

    brightamount = brightamount * (MAX_BRIGHT * .01f);
    // set sound
    // slider----------------------------------------------------------------
    transcomponent = ComponentRetriever.get(root.getChild(composite).getEntity(), TransformComponent.class);

    transpar.x = transcomponent.x;
    transpar.y = transcomponent.y;

    transcomponent = ComponentRetriever.get(root.getChild(composite).getChild("soundslider").getEntity(),
      TransformComponent.class);
    dimcomponent = ComponentRetriever.get(root.getChild(composite).getChild("soundslider").getEntity(),
      DimensionsComponent.class);
    transcomp = ComponentRetriever.get(root.getChild(composite).getChild("soundbar").getEntity(),
      TransformComponent.class);
    dimcompon = ComponentRetriever.get(root.getChild(composite).getChild("soundbar").getEntity(),
      DimensionsComponent.class);

    dimwh.x = dimcomponent.width * transcomponent.scaleX;
    dimwh.y = dimcomponent.height * transcomponent.scaleY;
    dimwhs.x = dimcompon.width * transcomp.scaleX;
    dimwhs.y = dimcompon.height * transcomp.scaleY;

    // bar full percentageU
    fullbarpercent = dimwhs.x;

    // soundamount is received from the file - see readFile(String File)
    soundamount = 100 * (soundamount / MAX_VOL);

    // transcomponent.x = (transcomp.x - (dimwh.x / 2)) + (float) ((soundamount
    // * .01) * fullbarpercent);
    //Center the Slider
    transcomponent.x = (transcomp.x - (dimwh.x * .5f)) + (float) ((soundamount * .01) * fullbarpercent);

    soundamount = soundamount * (MAX_VOL * .01f);

    transcomponent = null;
    dimcomponent = null;
    transcomp = null;
    dimcompon = null;
    dimwh = null;
    dimwhs = null;
    transpar = null;
  }

  // ------------- END OF CODE BEFORE GESTURE DETECTOR METHODS
  // ----------------------- //[

  /**
   * Method sets up landscape main menu buttons
   */
  public void setuplandscape() {
    final float RIFT = 14.5f;

    String[] cdtcomponents = {"ttcimage", "swypefrmbtm", "swypefrmtop"};
    // lighttranscomp =
    // root.getChild("mainlight").getEntity().getComponent(TransformComponent.class);
    fader = root.getChild("transition").getEntity();
    Preferences spfzpref = Gdx.app.getPreferences("spfzfile");
    if (!spfzpref.getBoolean("initialize"))
    {
      //saveSettings in ResourceManager
      setrdtime(99);
    }
    setSettings();
    if (!gamestart && INTRO >= 1)
    {
      Actions.addAction(fader, Actions.fadeOut(0f));
    }
    credittime = System.currentTimeMillis();

    view = "landscape";

    //transform = tc.get(root.getEntity());
    //action = ac.get(root.getEntity());

    // Intro Animation only starts up once
    if (INTRO == 0)
    {
      Intro();
    }
    else
    {
      INTRO = 2;

      // After intro animation, setup the menu as usual
      if (INTRO >= 2 && uicomplete == true)
      {

        if (!gamestart)
        {
          //if (//mainmenu.getPosition() < RIFT)
          //{
          //mainmenu.setPosition(RIFT);
          // }

          for (int i = 0; i < cdtcomponents.length; i++)
          {
            Actions.addAction(root.getChild(cdtcomponents[i]).getEntity(), Actions.fadeOut(0f));
          }

          update(view).getEngine().removeEntity(root.getChild("animscn").getEntity());
          update(view).getEngine().removeEntity(root.getChild("lintro").getEntity());

          Actions.addAction(root.getChild("ttcimage").getEntity(),
            Actions.sequence(Actions.delay(.5f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
              Actions.fadeOut(.4f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
              Actions.fadeOut(.4f), Actions.fadeIn(1f)));
        }
        // remove the touch to continue entity once the user has pressed
        // the
        // screen and
        // position the music to the correct position a.k.a the rift
        // position
        if (INTRO == 2 && Gdx.input.isTouched() || gamestart)
        {
          gamestart = true;

          Actions.addAction(root.getChild("ttcimage").getEntity(), Actions.fadeOut(0f));

          //if (//mainmenu.getPosition() < RIFT)
          //{
          //mainmenu.setPosition(RIFT);

          //  update(view).getEngine().removeEntity(root.getChild("ttcimage").getEntity());
          //}

          // If this boolean is set, setup the main menu.
          if (uicomplete == true)
          {
            if (!dialog)
            {

              //update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

              // animate the landscape scene
              //animateland();
              root.getChild("lhlpbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("lhlpbutton", false, null))
                    {
                      // WE DO NOT PROCESS THE BUTTON
                    }
                    else
                    {

                      if (!flingup && !flingdown && !transition)
                      {
                        if (resourceManager.appDevice() == resourceManager.ANDROID)
                        {

                        }
                      }

                      buttonsapt();
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("loptbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    String FILE_NAME = "spfzfile";
                    if (draggedfrmbtn("loptbutton", false, null))
                    {

                    }
                    else
                    {
                      if (!flingup && !flingdown && !transition)
                      {
                        // set buttons apart for exit menu
                        buttonsdown();

                        optionsup = true;

                        if (resourceManager.appDevice() == resourceManager.ANDROID)
                        {
                          //android.lockOrientation(true, view);
                        }

                        readOut(FILE_NAME);
                        tmpsound = soundamount;
                        tmpbright = brightamount;


                        setsliders("optdialog");
                      }
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("optdialog").getChild("optback").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    // if (draggedfrmbtn("optback", true, "optdialog"))
                    // {

                    // }
                    // else
                    // {

                    // needs a better way of checking to ensure back
                    // button is pressed once

                    if (root.getChild("optdialog").getEntity().getComponent(TransformComponent.class).y <= 50f)
                    {
                      // move buttons back in place
                      buttonsup();

                      optionsup = false;
                      if (resourceManager.appDevice() == resourceManager.ANDROID)
                      {
                        //android.lockOrientation(false, view);
                      }
                      if (tmpsound != soundamount || tmpbright != brightamount)
                      {
                        //saveSettings in ResourceManager
                      }
                      else
                      {
                        soundamount = tmpsound;
                        brightamount = tmpbright;
                      }

                    }

                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("brightbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("brightbutton", false, null))
                    {
                      // DO NOT PROCESS BUTTON
                    }
                    else
                    {
                      if (!flingup && !flingdown && !transition)
                      {
                        //if (resourceManager.appDevice() == resourceManager.ANDROID)
                        // {

                        brightamount += 51;
                        if (brightamount >= 255)
                        {
                          brightamount = 51;
                        }
                        else if (brightamount < 51)
                        {
                          brightamount = 51;
                        }
                        //saveSettings in ResourceManager
                        adjustBrightness(brightamount);

                      }
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("soundbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("soundbutton", false, null))
                    {
                      // DO NOT PROCESS BUTTON
                    }
                    else
                    {
                      if (!flingup && !flingdown && !transition)
                      {
                        if (mute)
                        {
                          ////mainmenu.setVolume(0f);
                          //if (resourceManager.appDevice() == resourceManager.ANDROID)
                          //
                          //saveSound in ResourceManager

                          // }
                          mute = false;
                        }
                        else
                        {
                          ////mainmenu.setVolume(1f);
                          //if (resourceManager.appDevice() == resourceManager.ANDROID)
                          //{
                          //saveSound in ResourceManager
                          //}
                          mute = true;
                        }
                      }
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("exitbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("exitbutton", false, null))
                    {
                      // DO NOT PROCESS BUTTON
                    }
                    else
                    {
                      if (!flingup && !flingdown && !transition)
                      {
                        // Reset the phone settings back to what they were
                        // before
                        // The Application was started
                        dialog = true;
                        if (!exit)
                        {
                          buttonsdown();
                          //pods fly out
                        }
                        exit = true;
                        // exit code
                      }
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("optdialog").getChild("thirtytime").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  public void clicked() {
                    // if (draggedfrmbtn("thirtytime ", true, "optdialog"))
                    // {
                    // DO NOT PROCESS BUTTON
                    // }
                    // else
                    // {

                    // Reset the phone settings back to what they were
                    // before
                    // The Application was started

                    //stageTime = 11;
                    setrdtime(11);
                    //}
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("optdialog").getChild("sixtytime").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  public void clicked() {
                    // if (draggedfrmbtn("sixtytime ", true, "optdialog"))
                    // {
                    // DO NOT PROCESS BUTTON
                    // }
                    // else
                    // {

                    // Reset the phone settings back to what they were
                    // before
                    // The Application was started

                    //stageTime = 60;
                    setrdtime(60);
                    // }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("optdialog").getChild("ninetytime").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  public void clicked() {
                    // if (draggedfrmbtn("ninetytime ", true, "optdialog"))
                    // {
                    // DO NOT PROCESS BUTTON
                    // else
                    // {

                    // Reset the phone settings back to what they were
                    // before
                    // The Application was started

                    //stageTime = 99;
                    setrdtime(99);
                    // }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("optdialog").getChild("brightslider").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  public void clicked() {

                  }

                  @Override
                  public void touchDown() {
                    // TransformComponent transcomponent =
                    // ComponentRetriever.get(root.getChild("optdialog").getChild("brightslider").getEntity(),
                    // TransformComponent.class);
                    // DimensionsComponent dimcomponent =
                    // ComponentRetriever.get(root.getChild("optdialog").getChild("brightslider").getEntity(),
                    // DimensionsComponent.class);
                    // TransformComponent transcomp =
                    // ComponentRetriever.get(root.getChild("optdialog").getChild("brightbar").getEntity(),
                    // TransformComponent.class);
                    // DimensionsComponent dimcompon =
                    // ComponentRetriever.get(root.getChild("optdialog").getChild("brightbar").getEntity(),DimensionsComponent.class);

                    adjustbright = true;


                  }

                  @Override
                  public void touchUp() {

                    // adjustbright = false;
                  }
                });
              root.getChild("optdialog").getChild("soundslider").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  public void clicked() {

                  }

                  @Override
                  public void touchDown() {

                    adjustsound = true;

                  }

                  @Override
                  public void touchUp() {

                    // adjustsound = false;
                  }
                });

              root.getChild("meconst").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("meconst", false, null))
                    {

                    }
                    else
                    {
                      // move = termov;
                      move = new Vector3(root.getChild("meconst").getEntity().getComponent(TransformComponent.class).x
                        + root.getChild("meconst").getEntity().getComponent(DimensionsComponent.class).width * .5f,
                        root.getChild("meconst").getEntity().getComponent(TransformComponent.class).y
                          + root.getChild("meconst").getEntity().getComponent(DimensionsComponent.class).height
                          * .4f,
                        0);
                      credpress = true;
                      Actions.addAction(root.getChild("meconst").getChild("medraw").getEntity(), Actions.fadeIn(.8f));
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("treyconstbtn").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("treyconstbtn", false, null))
                    {

                    }
                    else
                    {
                      move = treymov;
                      credpress = true;
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("miguelconstbtn").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("miguelconstbtn", false, null))
                    {

                    }
                    else
                    {
                      move = migmov;
                      credpress = true;
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("mikloconstbtn").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("mikloconstbtn", false, null))
                    {

                    }
                    else
                    {
                      move = mikmov;
                      credpress = true;
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });
              root.getChild("creditbtn").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("creditbtn", false, null))
                    {

                    }
                    else
                    {

                      move = credmov;
                      credpress = true;
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("exitdialog").getChild("yesbutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("yesbutton", true, "exitdialog"))
                    {

                    }
                    else
                    {
                      if (exit)
                      {
                        Gdx.app.exit();
                      }
                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

              root.getChild("exitdialog").getChild("nobutton").getEntity().getComponent(SPFZButtonComponent.class)
                .addListener(new SPFZButtonComponent.ButtonListener()
                {

                  @Override
                  public void clicked() {
                    if (draggedfrmbtn("nobutton", true, "exitdialog"))
                    {

                    }
                    else
                    {
                      buttonsup();
                      //pods bring toward screen
                      dialog = false;
                      exit = false;

                    }
                  }

                  @Override
                  public void touchDown() {

                  }

                  @Override
                  public void touchUp() {

                  }
                });

            }

          }
        }
      }
    }
  }

  public void unsetpress() {
    im.clear();
  }

  /**
   * Method sets up portrait main menu buttons
   */
  public void setupportrait() {
    final float RIFT = 14.5f;
    view = "portrait";

    // Retriever the components needed to trigger Actions
    //transform = tc.get(root.getEntity());
    //action = ac.get(root.getEntity());

    fader = root.getChild("transition").getEntity();

    Preferences spfzpref = Gdx.app.getPreferences("spfzfile");
    if (spfzpref.getBoolean("initialize") == false)
    {
      //writeOut("1.0\n255");save Settings in ResourceManager
      setrdtime(99);
    }
    setSettings();

    Actions.addAction(root.getChild("tocreditsone").getEntity(), Actions.fadeOut(0f));
    Actions.addAction(root.getChild("tocreditstwo").getEntity(), Actions.fadeOut(0f));
    if (!gamestart && INTRO >= 1)
    {
      Actions.addAction(fader, Actions.fadeOut(0f));
    }
    // Intro Animation only starts up once
    if (INTRO == 0)
    {
      Intro();
    }
    else
    {
      INTRO = 2;

      // After intro animation, setup the menu as usual
      if (INTRO >= 2 && uicomplete == true)
      {

        if (!gamestart)
        {
          //if (//mainmenu.getPosition() < RIFT)
          //{
          //mainmenu.setPosition(RIFT);
          //}

          Actions.addAction(root.getChild("ttcimage").getEntity(), Actions.fadeOut(0f));
          update(view).getEngine().removeEntity(root.getChild("animcircle").getEntity());
          update(view).getEngine().removeEntity(root.getChild("introcircle").getEntity());

          Actions.addAction(root.getChild("ttcimage").getEntity(),
            Actions.sequence(Actions.delay(.5f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
              Actions.fadeOut(.4f), Actions.fadeIn(.4f), Actions.fadeOut(.4f), Actions.fadeIn(.4f),
              Actions.fadeOut(.4f), Actions.fadeIn(1f)));
        }

        // remove the touch to continue entity once the user has pressed
        // the
        // screen and
        // position the music to the correct position a.k.a the rift
        // position.

        if (INTRO == 2 && Gdx.input.isTouched() || gamestart)
        {
          gamestart = true;

          if (scenesel == 1 || resourceManager.currentScene() == "landscene")
          {
            Actions.addAction(root.getChild("ttcimage").getEntity(), Actions.fadeOut(0f));

          }
          //if (//mainmenu.getPosition() < RIFT)
          //{
          //mainmenu.setPosition(RIFT);

          //  update(view).getEngine().removeEntity(root.getChild("ttcimage").getEntity());
          //}

          // If this boolean is set, setup the main menu.
          if (uicomplete == true)
          {
            if (!dialog)
            {
              animatemainmenu(view);

            }

            // buttons must be added by tag name before assigning
            // listener
            // actions

            update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

            root.getChild("arcbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("arcbutton", false, null))
                  {

                  }
                  else
                  {
                    if (concheck(resourceManager.currentScene()))
                    {
                      //resourceManager.currentScene() = "arcadeselscn";
                      prevScene = "arcadeselscn";
                      isArcade = true;
                      mode = true;
                      //portbtns.play(1.0f);
                      Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
                      {

                        @Override
                        public void run() {
                          view = "landscape";
                          selecttype = 0;

                          if (resourceManager.appDevice() == resourceManager.ANDROID)
                          {
                            //android.lockOrientation(true, view);
                          }

                        }
                      })));


                    }

                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("vsbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {

                  if (draggedfrmbtn("vsbutton", false, null))
                  {

                  }
                  else
                  {
                    if (concheck(resourceManager.currentScene()))
                    {
                      isArcade = false;
                      mode = true;
                      //portbtns.play(1.0f);
                      Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
                      {

                        @Override
                        public void run() {
                          view = "landscape";
                          //resourceManager.currentScene() = "charselscene";
                          prevScene = "charselscene";
                          selecttype = 0;
                          // resourceManager.unloadGame();
                          // resourceManager.initsix();
                          if (resourceManager.appDevice() == resourceManager.ANDROID)
                          {
                            //android.lockOrientation(true, view);
                          }

                        }
                      })));

                    }

                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("trnbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()

              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("trnbutton", false, null))
                  {

                  }
                  else
                  {
                    if (concheck(resourceManager.currentScene()))
                    {
                      isArcade = false;
                      mode = true;
                      //portbtns.play(1.0f);
                      Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
                      {

                        @Override
                        public void run() {
                          view = "landscape";
                          istraining = true;
                          //resourceManager.currentScene() = "charselscene";
                          prevScene = "charselscene";
                          selecttype = 1;
                          // resourceManager.unloadGame();
                          // resourceManager.initsix();

                          if (resourceManager.appDevice() == resourceManager.ANDROID)
                          {
                            //android.lockOrientation(true, view);
                          }

                        }
                      })));
                    }
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }

              });

            root.getChild("helpbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("helpbutton", false, null))
                  {

                  }
                  else
                  {
                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {

                    }

                    optionsup = true;
                    buttonsup();

                    //OPEN HELP OPTIONS
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("optbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  String FILE_NAME = "spfzfile";

                  if (draggedfrmbtn("optbutton", false, null))
                  {

                  }
                  else
                  {
                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {

                    }
                    optionsup = true;
                    //portbtns.play(1.0f);
                    buttonsup();

                    //openOptionsScreen

                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {
                      //android.lockOrientation(true, view);
                    }

                    readOut(FILE_NAME);

                    tmpsound = soundamount;
                    tmpbright = brightamount;

                    setsliders("optionscreen");
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });
            root.getChild("mnuscnbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("mnuscnbutton", false, null))
                  {

                  }
                  else
                  {
                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {

                    }
                    inhelp = true;
                    mnuscn = true;
                    //portbtns.play(1.0f);

                    //closeHelpOptions()
                    //openMenuScreenScreens()

                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("ingamebutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("ingamebutton", false, null))
                  {

                  }
                  else
                  {
                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {

                    }
                    inhelp = true;
                    mnuscn = false;
                    //portbtns.play(1.0f);

                    /*Actions.addAction(root.getChild("mnuscnbutton").getEntity(),
                      Actions.sequence(Actions.parallel(Actions.scaleBy(.5f, 2f, OPT_TIME / 8),
                        Actions.parallel(Actions.scaleBy(-3.0551f, -2f, OPT_TIME / 6),
                          Actions.moveBy(113f, 0f, OPT_TIME / 6, Interpolation.linear)))));

                    Actions.addAction(root.getChild("ingamebutton").getEntity(),
                      Actions.sequence(Actions.parallel(Actions.scaleBy(.5f, 2f, OPT_TIME / 8),
                        Actions.parallel(Actions.scaleBy(-3.0551f, -2f, OPT_TIME / 6),
                          Actions.moveBy(119f, 0f, OPT_TIME / 6, Interpolation.linear)))));
                    Actions.addAction(root.getChild("ingamehelp").getEntity(), Actions.sequence(Actions.delay(.5f),
                      Actions.parallel(Actions.scaleBy(1.8f, 0f, OPT_TIME / 3))));*/
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("hlpbackbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("hlpbackbutton", false, null))
                  {

                  }
                  else
                  {
                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {

                    }

                    if (inhelp)
                    {
                      /*Actions.addAction(root.getChild("mnuscnbutton").getEntity(),
                        Actions.sequence(Actions.delay(.5f),
                          Actions.parallel(Actions.scaleBy(2.5551f, 0, OPT_TIME / 8),
                            Actions.moveBy(-113f, 0f, OPT_TIME / 8, Interpolation.linear))));
                      Actions.addAction(root.getChild("ingamebutton").getEntity(),
                        Actions.sequence(Actions.delay(.5f),
                          Actions.parallel(Actions.scaleBy(2.5551f, 0, OPT_TIME / 8),
                            Actions.moveBy(-119f, 0f, OPT_TIME / 8, Interpolation.linear))));*/

                      if (mnuscn)
                      {
                        /*Actions.addAction(root.getChild("charselhelp").getEntity(),
                          Actions.sequence(Actions.parallel(Actions.scaleBy(-3.3f, 0f, OPT_TIME / 3))));
                        Actions.addAction(root.getChild("pausehelp").getEntity(),
                          Actions.sequence(Actions.parallel(Actions.scaleBy(-3.3f, 0f, OPT_TIME / 3))));*/
                      }
                      else
                      {
                        /*Actions.addAction(root.getChild("ingamehelp").getEntity(),
                          Actions.sequence(Actions.parallel(Actions.scaleBy(-1.8f, 0f, OPT_TIME / 3))));*/
                      }
                      inhelp = false;
                    }
                    else
                    {
                      Actions.addAction(root.getChild("mnuscnbutton").getEntity(),
                        Actions.sequence(Actions.parallel(Actions.scaleBy(.5f, 2f, OPT_TIME / 8),
                          Actions.parallel(Actions.scaleBy(-3.0551f, -2f, OPT_TIME / 6),
                            Actions.moveBy(113f, 0f, OPT_TIME / 6, Interpolation.linear)))));

                      Actions.addAction(root.getChild("ingamebutton").getEntity(),
                        Actions.sequence(Actions.parallel(Actions.scaleBy(.5f, 2f, OPT_TIME / 8),
                          Actions.parallel(Actions.scaleBy(-3.0551f, -2f, OPT_TIME / 6),
                            Actions.moveBy(119f, 0f, OPT_TIME / 6, Interpolation.linear)))));

                      Actions.addAction(root.getChild("hlpbackbutton").getEntity(),
                        Actions.sequence(Actions.parallel(Actions.scaleBy(-.5f, 0f, OPT_TIME / 8),
                          Actions.moveBy(29f, 0f, OPT_TIME / 8, Interpolation.linear))));

                      buttonsdown();
                      optionsup = false;
                    }

                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("optionscreen").getChild("thirtytime").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                public void clicked() {
                  // if (draggedfrmbtn("thirtytime ", true, "optdialog"))
                  // {
                  // DO NOT PROCESS BUTTON
                  // }
                  // else
                  // {

                  // Reset the phone settings back to what they were
                  // before
                  // The Application was started

                  //stageTime = 11;
                  setrdtime(11);
                  // }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("optionscreen").getChild("sixtytime").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                public void clicked() {
                  // if (draggedfrmbtn("sixtytime ", true, "optdialog"))
                  // {
                  // DO NOT PROCESS BUTTON
                  // }
                  // else
                  // {

                  // Reset the phone settings back to what they were
                  // before
                  // The Application was started

                  //stageTime = 60;
                  setrdtime(60);
                  // }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("optionscreen").getChild("ninetytime").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                public void clicked() {
                  // if (draggedfrmbtn("ninetytime ", true, "optdialog"))
                  // {
                  // DO NOT PROCESS BUTTON
                  // else
                  // {

                  // Reset the phone settings back to what they were
                  // before
                  // The Application was started

                  //stageTime = 99;
                  setrdtime(99);
                  // }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("optionscreen").getChild("brightslider").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                public void clicked() {

                }

                @Override
                public void touchDown() {
                  // TransformComponent transcomponent;

                  adjustbright = true;

                  // transcomponent =
                  // ComponentRetriever.get(root.getChild("optionscreen").getEntity(),
                  // TransformComponent.class);

                  // transpar.x = transcomponent.x;
                  // transpar.y = transcomponent.y;

                }

                @Override
                public void touchUp() {

                  // adjustbright = false;
                }
              });

            root.getChild("optionscreen").getChild("soundslider").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                public void clicked() {

                }

                @Override
                public void touchDown() {

                  adjustsound = true;


                }

                @Override
                public void touchUp() {

                  // adjustbright = false;
                }
              });

            root.getChild("optionscreen").getChild("optback").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  // if (draggedfrmbtn("optback", true, "optdialog"))
                  // {

                  // }
                  // else
                  // {

                  // needs a better way of checking to ensure back
                  // button is pressed once

                  if (root.getChild("optionscreen").getEntity().getComponent(TransformComponent.class).x <= 60f)
                  {

                    //closeOptionsScreen

                    buttonsdown();

                    optionsup = false;

                    if (resourceManager.appDevice() == resourceManager.ANDROID)
                    {
                      //android.lockOrientation(false, view);
                    }
                    if (tmpsound != soundamount || tmpbright != brightamount)
                    {
                      writeOut(Float.toString(soundamount) + "\n" + Float.toString(brightamount));
                    }
                    else
                    {
                      soundamount = tmpsound;
                      brightamount = tmpbright;
                    }


                  }

                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("controlboard").getChild("brightnessbtn").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("brightnessbtn", true, "controlboard"))
                  {

                  }
                  else
                  {

                    //if (resourceManager.appDevice() == resourceManager.ANDROID)
                    //{
                    brightamount += 51;
                    if (brightamount >= 255)
                    {
                      brightamount = 51;
                    }
                    else if (brightamount < 51)
                    {
                      brightamount = 51;
                    }


                    writeOut(Float.toString(soundamount) + "\n" + Float.toString(brightamount));
                    adjustBrightness(brightamount);
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("controlboard").getChild("soundbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {

                  if (mute)
                  {
                    //mainmenu.setVolume(0f);
                    mute = false;
                  }
                  else
                  {
                    //mainmenu.setVolume(1f);
                    mute = true;
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("controlboard").getChild("exitbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("exitbutton", true, "controlboard"))
                  {

                  }
                  else
                  {
                    dialog = true;
                    if (!exit)
                    {
                      buttonsup();
                    }
                    exit = true;
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("exitdialog").getChild("yesbutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("yesbutton", true, "exitdialog"))
                  {

                  }
                  else
                  {
                    if (exit)
                    {
                      Gdx.app.exit();
                    }
                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });

            root.getChild("exitdialog").getChild("nobutton").getEntity().getComponent(SPFZButtonComponent.class)
              .addListener(new SPFZButtonComponent.ButtonListener()
              {

                @Override
                public void clicked() {
                  if (draggedfrmbtn("nobutton", true, "exitdialog"))
                  {

                  }
                  else
                  {
                    dialog = false;
                    buttonsdown();
                    exit = false;

                  }
                }

                @Override
                public void touchDown() {

                }

                @Override
                public void touchUp() {

                }
              });
          }
        }
      }
    }
  }

  public void setupstage() {
    float texWidth;
    float texHeight;
    short WORLD_WIDTH = 640;
    short WORLD_HEIGHT = 400;
    String stagepath = "stages/" + selectedStage + ".png";
    //stoprender = false;
    texWidth = (WORLD_WIDTH * STAGE_MULT) * resourceManager.projectVO().pixelToWorld;
    texHeight = (WORLD_HEIGHT * 2) * resourceManager.projectVO().pixelToWorld;

    pixmap = new Pixmap(Gdx.files.internal(stagepath));

    pixmap2 = new Pixmap((int) texWidth, (int) texHeight, pixmap.getFormat());

    pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, pixmap2.getWidth(),
      pixmap2.getHeight());
    stageback = new Texture(pixmap2);

    pixmap2.dispose();
    pixmap.dispose();

    trc.regionName = selectedStage;
    testregion = new TextureRegion(stageback, (int) texWidth, (int) texHeight);

    setupstage = true;
    trc.region = testregion;

    stageimg.layerName = "Default";
    stageimg.zIndex = 0;

    stageimg.scaleX = 1f;
    stageimg.scaleY = 1f;

    // stageimg.x = 200f;
    // stageimg.y = -120f;
    stageimg.x = -240f;
    stageimg.y = 0f;

    update(view).entityFactory.createEntity(root.getEntity(), stageimg);

  }

  public String setworkingres() {

    String lResolution;

    if ((gWidth >= 100 && gWidth <= 800) && (gHeight >= 100 && gHeight <= 480))
    {
      lResolution = "smallland";
    }

    else if ((gWidth >= 801 && gWidth <= 1040) && (gHeight >= 481 && gHeight <= 600))
    {
      lResolution = "smalnormland";
    }

    else if ((gWidth >= 801 && gWidth <= 1280) && (gHeight >= 481 && gHeight <= 720))
    {
      lResolution = "normalland";
    }
    else if ((gWidth >= 1281 && gWidth <= 1600) && (gHeight >= 721 && gHeight <= 960))
    {
      lResolution = "largeland";
    }

    else
    {
      lResolution = "orig";
    }

    return lResolution;
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

  public void stageprocessing() {
    short HALF_WORLDH = 200;
    short HALF_WORLDW = 320;

    if (resourceManager.appDevice() == resourceManager.DESKTOP)
    {
      stage.controls();
    }

    if (root.getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects != null)
    {
      if (root.getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
      {
        if ((!root.getChild("p1swap").getChild("swapp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete()) && stage.strt1)
        {
          root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = stage.spfzp1move.center();
          root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = stage.spfzp1move.attributes().y
            + stage.spfzp1move.dimensions().height / 2;
        }
      }
    }

    stage.lifeandround();

    stage.newcam();

    root.getChild("ctrlandhud").getEntity()
      .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - (HALF_WORLDW);

    root.getChild("ctrlandhud").getEntity()
      .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - (HALF_WORLDH);

    viewportland.getCamera().update();

    if (stage.arrscripts != null)
    {
      for (int i = 0; i < stage.arrscripts.size(); i++)
      {
        if (i == stage.p1 || i == stage.p2)
        {
          stage.collision(i);
        }
      }
    }

    if (stage.switchp1)
    {
      stage.switchp1();
    }
    if (stage.switchp2)
    {
      stage.switchp2();
    }

  }

  public void readOut(final String file) {
    String values;
    String FILE_NAME = "spfzfile";

    Preferences spfzpref = Gdx.app.getPreferences(FILE_NAME);
    values = spfzpref.getString("settings");
    if (values == "")
    {
      values = "0.5\n50";
    }
    savedvals = values.split("\n");

    soundamount = Float.valueOf(savedvals[0]);
    brightamount = Float.valueOf(savedvals[1]);

  }

  public void setrdtime(final int values) {
    String FILE_NAME = "spfzfile";

    Preferences spfzpref = Gdx.app.getPreferences(FILE_NAME);
    spfzpref.putInteger("time", values);

    spfzpref.flush();

  }

  /*public void writeOut(final String values) {
    String FILE_NAME = "spfzfile";

    Preferences spfzpref = Gdx.app.getPreferences(FILE_NAME);
    spfzpref.putString("settings", values);

    //if (!spfzpref.getBoolean("initialize"))
    //  spfzpref.putBoolean("initialize", true);


    spfzpref.flush();

  }*/

  /**
   * Method contains the stages to select from
   */
  public void stageSel() {
    fader = root.getChild("transition").getEntity();
    setSettings();
    Actions.addAction(fader, Actions.sequence(Actions.fadeOut(.3f)));
    update(view).addComponentsByTagName("button", SPFZButtonComponent.class);

    root.getChild("stageonebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "halloweenstage";

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagetwobutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "cathedralstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagethreebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "clubstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagefourbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "egyptstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagefivebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "futurestage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagesixbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "gargoyle";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stagesevenbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "junglestage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stageeightbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "skullstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("stageninebutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {

          selectedStage = "undergroundstage";
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("okaybutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          if (selectedStage == null)
          {
            // enter custom toast message here
          }
          else
          {
            Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
            {

              @Override
              public void run() {
                ok.play(1.0f);
                createstage();
              }
            })));

          }
        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
    root.getChild("backbutton").getEntity().getComponent(SPFZButtonComponent.class)
      .addListener(new SPFZButtonComponent.ButtonListener()
      {

        @Override
        public void clicked() {
          back.play(1.0f);
          Actions.addAction(fader, Actions.sequence(Actions.fadeIn(.3f), Actions.run(new Runnable()
          {

            @Override
            public void run() {
              fromss = true;

              backprocessing();
            }
          })));

        }

        @Override
        public void touchDown() {

        }

        @Override
        public void touchUp() {

        }
      });
  }

  public void createstage() {
    float[] bounds = {80, 560};
    String stagep = "stages/" + selectedStage + ".png";
    stageconfirmed = true;

    resourceManager.getManager().load(stagep, Texture.class);
    // need to add "loading finished" logic
    resourceManager.getManager().finishLoading();
    // resourceManager.getManager().i
    root.getEntity().removeAll();

    land = new SPFZSceneLoader(resourceManager, SwapFyterzMain.this, "", "");

    update(view).loadScene("stagescene", viewportland);
    stagesystem.priority = 0;

    update(view).engine.addSystem(stagesystem);

    // if the stage has been selected and the OK button
    // has been
    // touched, begin process
    // for the controls creation for the fighting scene
    if (selectedStage != null && stageconfirmed)
    {
      characters.set(0, p1char1);
      characters.set(1, p1char2);
      characters.set(2, p1char3);
      characters.set(3, p2char1);
      characters.set(4, p2char2);
      characters.set(5, p2char3);

      // Call the stage to setup controls and pass the
      // camera;
      root = new ItemWrapper(update(view).getRoot());
      //transform = tc.get(root.getEntity());
      //action = ac.get(root.getEntity());

      setupstage();

      if (stageTime == 0)
      {
        //stageTime = 99;
        Preferences spfzpref = Gdx.app.getPreferences("spfzfile");
        stageTime = spfzpref.getInteger("time");
      }

      stage = new SPFZStage(update(view).getRm(), viewportland.getCamera(), selectedStage, resourceManager.appDevice(), bounds,
        characters, SwapFyterzMain.this, istraining);
    }
  }

  public void preload() {
    selectedStage = "halloweenstage";
    p1char1 = "spriteballred";
    p1char2 = "spriteballblack";
    p1char3 = "spritepurplex";
    p2char1 = "spriteballred";
    p2char2 = "spriteballblack";
    p2char3 = "spritepurplex";

    characters.set(0, p1char1);
    characters.set(1, p1char2);
    characters.set(2, p1char3);
    characters.set(3, p2char1);
    characters.set(4, p2char2);
    characters.set(5, p2char3);
    istraining = true;
  }

  /**
   * method performs action when the user swipes up or down
   */
  public void swipecheck() {
    if (!optionsup)
    {
      if (flingup)
      {
        if (viewportland.getCamera().position.y == credits.y)
        {
          flingup = false;
        }

        viewportland.getCamera().position.lerp(credits, 0.2f);

      }
      if (flingdown)
      {
        if (viewportland.getCamera().position.y == tomenu.y)
        {
          flingdown = false;
        }

        viewportland.getCamera().position.lerp(tomenu, 0.2f);
        if (((OrthographicCamera) viewportland.getCamera()).zoom != ZOOMCOUT)
        {
          ((OrthographicCamera) viewportland.getCamera()).zoom = ZOOMCOUT;
        }
      }
    }
  }

  @Override
  public boolean tap(float x, float y, int count, int button) {

    return false;
  }

  public void toCharSel() {
    // isArcade = false;
    mode = true;

    if (resourceManager.appDevice() == resourceManager.ANDROID)
    {
      //android.lockOrientation(true, view);
    }

    if (isArcade)
    {
      level = 0;
      // selecttype = "Arcade select Screen";
      //resourceManager.currentScene() = "arcadeselscn";
      prevScene = "arcadeselscn";
    }
    else
    {
      // selecttype = "CHARACTER SELECT SCREEN";
      //resourceManager.currentScene() = "charselscene";
      prevScene = "charselscene";

    }
    land = new SPFZSceneLoader(resourceManager, this, "", "");
    // resourceManager.unloadstage();
    // resourceManager.initsix();
    update(view).loadScene(resourceManager.currentScene(), viewportland);
    resumefrmpause();

    if (selectedStage != null && stageconfirmed)
    {
      stage.dispose();
      stage = null;
    }

    selectedStage = null;
    stageconfirmed = false;

    inMode();
  }

  public void toMenu() {
    resumefrmpause();
    backprocessing();
  }

  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (optionsup)
    {
      TransformComponent transcomp = new TransformComponent();
      TransformComponent transcomponent = new TransformComponent();
      DimensionsComponent dimcompon = new DimensionsComponent();
      DimensionsComponent dimcomponent = new DimensionsComponent();
      Vector2 dimwh = new Vector2();
      Vector2 dimwhs = new Vector2();
      Vector3 vec3 = new Vector3();
      Vector3 transpar = new Vector3(0, 0, 0);
      String option = new String();

      if (view == "portrait")
      {
        option = "optionscreen";
      }
      else
      {
        option = "optdialog";
      }

      if (adjustbright)
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          DimensionsComponent.class);
      }
      else
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          DimensionsComponent.class);
      }
      if (adjustsound)
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          DimensionsComponent.class);
      }
      else
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          DimensionsComponent.class);
      }

      transpar.x = ComponentRetriever.get(root.getChild(option).getEntity(), TransformComponent.class).x;
      transpar.y = ComponentRetriever.get(root.getChild(option).getEntity(), TransformComponent.class).y;

      int MAX_VOL = 1;
      int MAX_BRIGHT = 255;
      float fullbarpercent;


      if (view == "portrait")
      {
        viewportport.getCamera().update();
        vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewportport.unproject(vec3);
      }
      else
      {
        viewportland.getCamera().update();
        vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewportland.unproject(vec3);
      }

      if (adjustbright || adjustsound)
      {
        dimwh.x = dimcomponent.width * transcomponent.scaleX;
        dimwh.y = dimcomponent.height * transcomponent.scaleY;
        dimwhs.x = dimcompon.width * transcomp.scaleX;
        dimwhs.y = dimcompon.height * transcomp.scaleY;

        // bar full percentage
        fullbarpercent = dimwhs.x;

        if (transcomponent.x + transpar.x >= (transcomp.x + transpar.x - (dimwh.x * .5f))
          && (transpar.x + transcomponent.x + (dimwh.x * .5f)) <= (transpar.x + transcomp.x + dimwhs.x))
        {
          // transcomponent.x = (vec3.x - (dimwh.x / 2)) - transpar.x;
          transcomponent.x = (vec3.x - (dimwh.x * .5f)) - transpar.x;
        }

        // If the Slider(transcomponent.x + parent entity.x) value is less
        // than
        // the beginning of the bar(transcomp.x + parent entity.x),
        // set the slider to the beginning of the bar

        //if (transcomponent.x + transpar.x < (transcomp.x + transpar.x - (dimwh.x * .5f)))
        if (transcomponent.x + transpar.x < (transcomp.x + transpar.x - (dimwh.x * .5f)))
        {
          // transcomponent.x = transcomp.x - (dimwh.x / 2);
          transcomponent.x = transcomp.x - (dimwh.x * .5f);
        }

        // If the end of the Slider(transcomponent.x + parent entity.x +
        // slider
        // width) value is less than the end of the bar(transcomp.x + parent
        // entity.x + bar width),
        // set the slider to the end of the slider to the end of the bar

        if ((transpar.x + transcomponent.x + (dimwh.x * .5f)) > (transpar.x + transcomp.x + dimwhs.x))
        {
          transcomponent.x = transcomp.x + dimwhs.x - (dimwh.x * .5f) - .5f;
        }

        // Adjust brightness or sound with the new slider value

        if (adjustbright)
        {
          brightamount = 100 * (((dimwh.x * .5f) + transcomponent.x) - transcomp.x) / fullbarpercent;

          brightamount = (brightamount * .01f) * MAX_BRIGHT;


          if (brightamount >= 5f && brightamount <= MAX_BRIGHT)
          {

            adjustBrightness(brightamount);
          }
        }

        if (adjustsound)
        {
          soundamount = 100 * (((dimwh.x * .5f) + transcomponent.x) - transcomp.x) / fullbarpercent;

          soundamount = (soundamount * .01f) * MAX_VOL;


          if (soundamount >= 0f && soundamount <= MAX_VOL)
          {
            //mainmenu.setVolume(soundamount);
          }
        }

      }


      transcomponent = null;
      dimcomponent = null;
      transcomp = null;
      dimcompon = null;
      dimwh = null;
      dimwhs = null;
      vec3 = null;
      transpar = null;
    }
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return true;
  }


  /**
   * Function "brightens" or "dims" the in game screen range of (.2 - 1f)
   *
   * @param brightness
   */
  public void adjustBrightness(float brightness) {
    Entity bright = root.getChild("brightness").getEntity();
    float MAX = 255f;
    //float a_max = 1f;
    float a_max = .8f;
    float alter_bright;
    float remainder = MAX - brightness;

    alter_bright = (remainder * a_max) / MAX;

    //Do not allow the user to make the screen too dark(altering the opacity of the fader screen(1f == full fader screen
    //being implemented

    if (bright != null)
    {
      bright.getComponent(TintComponent.class).color.a = alter_bright;
    }
  }

  /**
   * method processes user input as well as performs different functionalities
   * instilled
   * within the game. etc. Back functionality, swipe, credits control.
   */
  public void UIProcessing() {
    if (optionsup)
    {
      if (!Gdx.input.isTouched())
      {
        adjustbright = false;
        adjustsound = false;
      }
    }

    if (resourceManager.currentScene() == "landscene") // && Gdx.input.isTouched())
    {
      swipecheck();
    }

    creditprocessing();

  }

  public void creditprocessing() {
    controlCredits();
    if (flingup && ((OrthographicCamera) viewportland.getCamera()).position.y >= credits.y - 1)
    {
      flingup = false;
      flingdown = false;
    }
    if (flingdown && ((OrthographicCamera) viewportland.getCamera()).position.y <= tomenu.y + 1)
    {
      flingdown = false;
      flingup = false;

    }
    if (credpress)
    {
      if (flingdown || flingup)
      {
        credpress = false;
      }
      else
      {
        // Zoom into credits, passing in the distance of zoom, time it should
        // take, and the positioning of the camera
        Zoom(ZOOMCIN, ZOOMCIDUR, move.x, move.y);

        if (Gdx.input.isKeyJustPressed(Keys.SPACE))
        {
          processback();
        }
      }
    }
    else
    {
      if (((OrthographicCamera) viewportland.getCamera()).zoom != ZOOMCOUT && !flingdown && !flingup
        && viewportland.getCamera().position.y >= tomenu.y)
      {
        // Zoom out, passing in the distance of zoom, time it should take, and
        // the positioning of the camera
        Zoom(ZOOMCOUT, ZOOMCODUR, credits.x, credits.y);
      }
    }

    if (Gdx.input.isKeyJustPressed(Keys.BACK))
    {
      if (stage == null)
      {
        processback();
      }
    }
  }

  public void Zoom(float targetzoom, float duration, float movex, float movey) {
    // set current vals to process interpolation smoothly
    zoompoint = ((OrthographicCamera) viewportland.getCamera()).zoom;
    endzoom = targetzoom;
    targetduration = startingduration = duration;

    if (((OrthographicCamera) viewportland.getCamera()).zoom >= targetzoom && credpress
      || ((OrthographicCamera) viewportland.getCamera()).zoom <= targetzoom && !credpress)
    {
      targetduration -= Gdx.graphics.getDeltaTime();
      float progress = targetduration < 0 ? 1 : 1f - targetduration / startingduration;

      ((OrthographicCamera) viewportland.getCamera()).zoom = Interpolation.pow3Out.apply(zoompoint, endzoom, progress);

      viewportland.getCamera().position.x = Interpolation.pow3Out.apply(viewportland.getCamera().position.x, movex,
        progress);
      viewportland.getCamera().position.y = Interpolation.pow3Out.apply(viewportland.getCamera().position.y, movey,
        progress);
    }
    else if (stage != null)
    {
      if (stage.p1charzoom || stage.p2charzoom)
      {
        targetduration -= Gdx.graphics.getDeltaTime();
        float progress = targetduration < 0 ? 1 : 1f - targetduration / startingduration;

        ((OrthographicCamera) viewportland.getCamera()).zoom = Interpolation.pow5Out.apply(zoompoint, endzoom,
          progress);

        viewportland.getCamera().position.x = Interpolation.pow5Out.apply(viewportland.getCamera().position.x, movex,
          progress);
        viewportland.getCamera().position.y = Interpolation.pow5Out.apply(viewportland.getCamera().position.y, movey,
          progress);
      }
    }

  }

  /**
   * Method returns SceneLoader based on Screen orientation
   */
  public SPFZSceneLoader update(String orientation) {
    if (orientation == ("portrait"))
    {
      return port;
    }
    else
    {
      return land;
    }
  }

  @Override
  public boolean zoom(float initialDistance, float distance) {

    return false;
  }

  public SPFZResourceManager resourceManager() {
    return resourceManager;
  }
}
