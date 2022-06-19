package com.dev.swapftrz.stage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.dev.swapftrz.SwapFyterzMain;
import com.dev.swapftrz.fyter.SPFZP1Movement;
import com.dev.swapftrz.fyter.SPFZP2Movement;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZStageImagePack;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPFZStage extends Stage
{

  boolean isMoving, switchp1, switchp2, close, neutral, faceleft1, faceleft2, faceright1, faceright2, eoround,
    finishedrd, pausetime, gameover, standby, damagedealt, training, pauseconfirm, show, showboxes, boxes, pauseset, strt1,
    strt2, initcheck, setneut, sigp1lock, runscript, shake, p1charzoom, p2charzoom;

  Camera stageCamera;
  SPFZResourceManager resManager;
  SPFZStageHUD stageHUD;
  ItemWrapper stageWrapper;
  double timeleftdbl;
  Entity p1c1, p1c2, p1c3, p2c1, p2c2, p2c3;

  float roundTime, xattr, p1xattr, p1yattr, p2xattr, p2yattr, diff, aspectratioh, aspectratiow, p1HPpercent, p2HPpercent,
    p1SPpercent, p2SPpercent, begpercent, begsuperpct, stagetempx, stagetempy, reflPCT;

  float[] cameraBoundaries;
  float[] stageBoundaries;

  // Keeps the characters at this height which will be the ground level for each
  // stage. STARTP1 and STARTP2 must level out to = 640(Viewport size which
  // maintains
  // center at the beginning of rounds
  final float GROUND = 120f, WALLJRES = 20f, STAGE_CENTER = 320f, CHAR_SPACE = 100f;

  final float HALF_WORLDH = 200, HALF_WORLDW = 320, WORLDH = 400, WORLDW = 640, SPECIAL_WINDOW = 15f, SWAP_WINDOW = 15f,
    SCALE_TEXT = .2f;

  long time, timeElapsed, pausedElapsed, showtime;

  static final int ANDROID = 0, DESKTOP = 1, MAX_SUPER = 600;

  short btnupdwn, btnlftrgt, camcon;
  int p1, p2, switchcount, picked, timeleft, p1health, p2health, p1spec, p2spec, rdcount, p1rdcount, p2rdcount,
    startp1, startp2, check;

  SwapFyterzMain access;

  //Sprite pausetex, health1, health2;

  List<List<ArrayList<Double>>> player1data = new ArrayList<List<ArrayList<Double>>>();
  List<HashMap<String, int[]>> player1anims = new ArrayList<HashMap<String, int[]>>();
  List<ArrayList<String>> player1moves = new ArrayList<ArrayList<String>>();

  List<List<ArrayList<Double>>> player2data = new ArrayList<List<ArrayList<Double>>>();
  List<HashMap<String, int[]>> player2anims = new ArrayList<HashMap<String, int[]>>();
  List<ArrayList<String>> player2moves = new ArrayList<ArrayList<String>>();

  List<String> characters = new ArrayList<String>();
  List<Object> arrscripts = new ArrayList<Object>();
  List<Integer> processed = new ArrayList<Integer>();

  // Scripts for each character

  SPFZP1Movement spfzp1move;
  SPFZP2Movement spfzp2move;

  Sprite texhel1, texhel2;
  String anim, stageimage, firstpause;

  String[] stagebtntags = {"pausetag", "upbutton", "rightupbutton", "rightbutton", "downrightbutton", "downbutton",
    "downleftbutton", "leftbutton", "leftupbutton", "punch", "kick", "mmbutton", "resbutton", "button"};
  //                  0      1      2      3      4      5      6      7      8     9       10     11    12      13

  //should be in Player class
  String[] normals = {};
  //                  14     15     16     17
  TextureRegion testregion, healthreg1, healthreg2, healthout1, healthout2;

  s
  Texture health1, health2, outline1, outline2, specmeter1, specmeter2, exdots1, healthtex1, healthtex2,
    exdots2, superout1, superout2;

  Sprite pausetex;

  public SPFZStage(List<String> characters, boolean training, SPFZResourceManager resManager) {
    this.resManager = resManager;
    stageCamera = resManager.getStageCam();
    cameraBoundaries = resManager.getCameraBoundaries();
    stageBoundaries = resManager.getStageBoundaries();
    stageWrapper = resManager.rootWrapper();
    this.training = training;
    // setup stage
    initStage(characters);

    // setup character scripts
    initScripts();
    charspicked(characters);
    setupcharacters(characters);
    totalhealth();
    setactors();

    //((OrthographicCamera) access.viewportland.getCamera()).zoom = .075f;
  }

  public void initStage(List<String> chars) {
    standby = true;
    Gdx.input.setInputProcessor(this);
    this.characters = chars;

    // look at options to figure out what time to set timeleft at
    timeleft = 10;
    //optime = trial.stageTime;
    roundTime = resManager.getRoundTimeSettings();

    // Lifesystem engine manages the health bars drawn onto the scene

    // initActions();
    stageHUD.createStageHUDTextures();
    stageHUD.resetHUDTimer(roundTime);
    stageHUD.setTimerTexture(training);
    stageHUD.setHUDCharacterNames(characters);
    stageHUD.preFightFade();
  }

  public void initScripts()
  {
    spfzp1move = new SPFZP1Movement(this);

    spfzp2move = new SPFZP2Movement(this);
  }

  public void charspicked(List<String> sprites)
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
        arrscripts.set(keep, spfzp1move);
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
    }
  }

  public void setupcharacters(List<String> chars) {
    // Entity p1c1;

    // load the characters from the library
    CompositeItemVO player1char1 = access.land.loadVoFromLibrary(chars.get(0));
    CompositeItemVO player2char1 = access.land.loadVoFromLibrary(chars.get(3));

    player1char1.zIndex = 4;
    player2char1.zIndex = 4;

    p1c1 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player1char1);
    p2c1 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player2char1);

    ItemWrapper playerone = new ItemWrapper(p1c1);
    ItemWrapper playertwo = new ItemWrapper(p2c1);

    access.land.entityFactory.initAllChildren(access.land.getEngine(), p1c1, player1char1.composite);
    access.land.entityFactory.initAllChildren(access.land.getEngine(), p2c1, player2char1.composite);

    p1c1.getComponent(MainItemComponent.class).itemIdentifier = chars.get(0);
    p2c1.getComponent(MainItemComponent.class).itemIdentifier = chars.get(3);
    access.land.getEngine().addEntity(p1c1);
    access.land.getEngine().addEntity(p2c1);

    p1 = 0;
    p2 = 3;

    playerone.addScript((IScript) arrscripts.get(p1));
    playertwo.addScript((IScript) arrscripts.get(p2));

   // access.stageimg = access.stageimg;
    p1c1.getComponent(ZIndexComponent.class).setZIndex(4);
    p2c1.getComponent(ZIndexComponent.class).setZIndex(4);
    // this for loop will set the booleans for each directional input for each
    // character
    // These boolean values will be needed to gather input as well as control
    // the CPU

    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7
    // for (int i = 0; i < chars.size() + 2; i++)
    for (String character : chars)
    {
      spfzp1move.p1movement.add(false);
      spfzp1move.lastp1movement.add(null);
      spfzp2move.p2movement.add(false);
      spfzp2move.lastp2movement.add(null);
    }
    spfzp1move.p1movement.add(false);
    spfzp1move.p1movement.add(false);
    spfzp1move.lastp1movement.add(null);
    spfzp1move.lastp1movement.add(null);
    spfzp2move.p2movement.add(false);
    spfzp2move.p2movement.add(false);
    spfzp2move.lastp2movement.add(null);
    spfzp2move.lastp2movement.add(null);

    time = System.currentTimeMillis();
  }

  public void setactors()
  {
    // for (int i = 0; i < stagebtntags.length; i++)
    for (String buttontags : stagebtntags)
    {
      access.update(access.view).addComponentsByTagName(buttontags, SPFZStageComponent.class);
    }

    pausebtn();
    updateclock();

    upcontrols();
    upleftcontrols();
    uprightcontrols();
    downcontrols();
    downleftcontrols();
    downrightcontrols();
    rightcontrols();
    leftcontrols();
    attackcontrols();

  }

  public void controls()
  {
    if (Gdx.input.isKeyPressed(Keys.UP))
    {
      spfzp1move.isUp = true;
    }

    else
    {
      spfzp1move.isUp = false;
    }
    if (Gdx.input.isKeyPressed(Keys.DOWN))
    {
      spfzp1move.isDown = true;
    }
    else
    {
      spfzp1move.isDown = false;
    }
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
    {
      spfzp1move.isRight = true;
    }
    else
    {
      spfzp1move.isRight = false;
      if (spfzp1move.ltstuck && spfzp1move.attributes().scaleX < 0)
      {
        spfzp1move.ltstuck = false;
      }
    }

    if (Gdx.input.isKeyPressed(Keys.LEFT))
    {
      spfzp1move.isLeft = true;
    }
    else
    {
      spfzp1move.isLeft = false;
      if (spfzp1move.ltstuck && spfzp1move.attributes().scaleX > 0)
      {
        spfzp1move.ltstuck = false;
      }
    }

    if (!spfzp1move.isPunch && Gdx.input.isKeyJustPressed(Keys.Q))
    {
      spfzp1move.isPunch = true;
    }
    else
    {
      spfzp1move.isPunch = false;
    }

    if (!spfzp1move.isPunch && !spfzp1move.isKick && Gdx.input.isKeyJustPressed(Keys.W))
    {
      spfzp1move.isKick = true;
    }
    else
    {
      spfzp1move.isKick = false;
    }
    if (Gdx.input.isKeyJustPressed(Keys.B))
    {
      if(!boxes)
      {
        boxes = true;
      }
      else
      {
        boxes = false;
      }
    }
    if (Gdx.input.isKeyJustPressed(Keys.S) && spfzp1move.swap)
    {
      switchp1 = true;
      switchcount = 0;
    }
    if (Gdx.input.isKeyJustPressed(Keys.P))
    {
      switchp2 = true;
      switchcount = 0;
      p2spec -= 100f;
    }
    if (Gdx.input.isKeyJustPressed(Keys.Z))
    {
      if (!shake)
      {
        shake = true;
      }
      else
      {
        shake = false;
      }
    }
    if (Gdx.input.isKeyJustPressed(Keys.A))
    {
      if (!p1charzoom && !p2charzoom)
      {
        p1charzoom = true;
      }
      else
      {
        p1charzoom = false;
      }
    }

   /*// if (Gdx.input.isKeyJustPressed(Keys.Q))
   // {
   //   if (!p1charzoom && !p2charzoom)
      {
        p2charzoom = true;
      }
      else
      {
        p2charzoom = false;
      }
    }*/

  }

  public void pausebtn()
  {
    Entity pausebtn = stageWrapper.getChild("ctrlandhud").getChild("pausebutton").getEntity();

    pausebtn.getComponent(SPFZStageComponent.class).addListener(new SPFZStageComponent.ButtonListener()
    {

      @Override
      public void touchUp()
      {

      }

      @Override
      public void touchDown()
      {
        // return true;
      }

      @Override
      public void clicked()
      {
        if (access.draggedfrmbtn("pausebutton", true, "ctrlandhud"))
        {
          // DO NOT PROCESS BUTTON
        }
        else
        {
          access.paused = true;
          if (!standby)
          {
            access.pause();
          }
        }
      }
    });

  }

  public void upleftcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("diagupleftbtn").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          if (btnupdwn == 0)
          {
            spfzp1move.isUp = false;
          }
          if (btnlftrgt == 0)
          {
            spfzp1move.isLeft = false;
          }
          isMoving = false;
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isUp = true;
          spfzp1move.isLeft = true;
          // checkScripts();

        }

        @Override
        public void clicked()
        {
          /*
           * if (access.draggedfrmbtn("pausebutton", true, "ctrlandhud")) { //
           * DO NOT PROCESS BUTTON } else { access.pause(); }
           */
        }
      });

  }

  public void leftcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("leftbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          spfzp1move.isLeft = false;
          isMoving = false;
          btnlftrgt = 0;
          spfzp1move.ltstuck = false;
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isRight = false;
          spfzp1move.isLeft = true;
          btnlftrgt = 1;
          // checkScripts();
        }

        @Override
        public void clicked()
        {
          /*
           * if (access.draggedfrmbtn("pausebutton", true, "ctrlandhud")) { //
           * DO NOT PROCESS BUTTON } else { access.pause(); }
           */
        }
      });

  }

  public void downleftcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("diagdownleftbtn").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          if (btnupdwn == 0)
          {
            spfzp1move.isDown = false;
          }
          if (btnlftrgt == 0)
          {
            spfzp1move.isLeft = false;
          }
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isDown = true;
          spfzp1move.isLeft = true;
          // checkScripts();

        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void downcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("downbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          spfzp1move.isDown = false;
          btnupdwn = 0;
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isDown = true;
          btnupdwn = 1;
        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void downrightcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("diagdownrightbtn").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {

          if (btnupdwn == 0)
          {
            spfzp1move.isDown = false;
          }
          if (btnlftrgt == 0)
          {
            spfzp1move.isRight = false;
          }
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isDown = true;
          spfzp1move.isRight = true;
        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void rightcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("rightbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {

          spfzp1move.isRight = false;
          isMoving = false;
          btnlftrgt = 0;
          spfzp1move.ltstuck = false;
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isLeft = false;

          spfzp1move.isRight = true;
          btnlftrgt = 1;
          // checkScripts();
        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void uprightcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("diaguprightbtn").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {

          // checkScripts();
          if (btnlftrgt == 0)
          {
            spfzp1move.isRight = false;
          }
          if (btnupdwn == 0)
          {
            spfzp1move.isUp = false;
          }
          isMoving = false;
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isUp = true;
          spfzp1move.isRight = true;
          isMoving = true;

        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void upcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("upbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {

          spfzp1move.isUp = false;
          btnupdwn = 0;
          // checkScripts();
        }

        @Override
        public void touchDown()
        {
          spfzp1move.isUp = true;
          btnupdwn = 1;

        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void attackcontrols()
  {
    stageWrapper.getChild("ctrlandhud").getChild("punchbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          spfzp1move.isPunch = false;
          show = false;
        }

        @Override
        public void touchDown()
        {

          spfzp1move.isPunch = true;
          show = true;
          showtime = System.currentTimeMillis();

        }

        @Override
        public void clicked()
        {

        }
      });

    stageWrapper.getChild("ctrlandhud").getChild("kickbutton").getEntity().getComponent(SPFZStageComponent.class)
      .addListener(new SPFZStageComponent.ButtonListener()
      {

        @Override
        public void touchUp()
        {
          spfzp1move.isKick = false;

        }

        @Override
        public void touchDown()
        {
          spfzp1move.isKick = true;

        }

        @Override
        public void clicked()
        {

        }
      });

  }

  public void updateclock()
  {
    Entity fader, p1round1, p1round2, p2round1, p2round2, roundtext;

    p1round1 = stageWrapper.getChild("ctrlandhud").getChild("roundonep1").getEntity();
    p1round1.getComponent(TransformComponent.class).originX -= p1round1.getComponent(TransformComponent.class).originX
      / 2;
    p1round1.getComponent(TransformComponent.class).originY -= p1round1.getComponent(TransformComponent.class).originY
      / 2;
    p1round1.getComponent(TransformComponent.class).x += p1round1.getComponent(TransformComponent.class).originX / 2;
    p1round1.getComponent(TransformComponent.class).y += p1round1.getComponent(TransformComponent.class).originY / 2;

    p1round2 = stageWrapper.getChild("ctrlandhud").getChild("roundtwop1").getEntity();
    p1round2.getComponent(TransformComponent.class).originX -= p1round2.getComponent(TransformComponent.class).originX
      / 2;
    p1round2.getComponent(TransformComponent.class).originY -= p1round2.getComponent(TransformComponent.class).originY
      / 2;
    p1round2.getComponent(TransformComponent.class).x += p1round2.getComponent(TransformComponent.class).originX / 2;
    p1round2.getComponent(TransformComponent.class).y += p1round2.getComponent(TransformComponent.class).originY / 2;

    p2round1 = stageWrapper.getChild("ctrlandhud").getChild("roundonep2").getEntity();
    p2round1.getComponent(TransformComponent.class).originX -= p2round1.getComponent(TransformComponent.class).originX
      / 2;
    p2round1.getComponent(TransformComponent.class).originY -= p2round1.getComponent(TransformComponent.class).originY
      / 2;
    p2round1.getComponent(TransformComponent.class).x += (p2round1.getComponent(TransformComponent.class).originX
      + p2round1.getComponent(TransformComponent.class).originX / 2);
    p2round1.getComponent(TransformComponent.class).y += p2round1.getComponent(TransformComponent.class).originY / 2;

    p2round2 = stageWrapper.getChild("ctrlandhud").getChild("roundtwop2").getEntity();
    p2round2.getComponent(TransformComponent.class).originX -= p2round2.getComponent(TransformComponent.class).originX
      / 2;
    p2round2.getComponent(TransformComponent.class).originY -= p2round2.getComponent(TransformComponent.class).originY
      / 2;
    p2round2.getComponent(TransformComponent.class).x += (p2round2.getComponent(TransformComponent.class).originX
      + p2round2.getComponent(TransformComponent.class).originX / 2);
    p2round2.getComponent(TransformComponent.class).y += p2round2.getComponent(TransformComponent.class).originY / 2;

    roundtext = stageWrapper.getChild("ctrlandhud").getChild("roundtext").getEntity();

    Actions.addAction(roundtext, Actions.fadeOut(.01f));
  }

  public void timer() {
    Entity tenths = stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("tenths").getEntity();
    Entity ones = stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("ones").getEntity();

    int tempten = (timeleft % 100) / 10;
    int tempone = timeleft % 10;
    double tedbl;

    if (timeleft > 0 && !pausetime && !standby)
    {
      setneut = false;
      timeElapsed = (System.currentTimeMillis() - time) / 1000;
      timeleft = (int) (roundTime - timeElapsed);

      // Double time is correct. Need to figure out way to translate it into the
      // timer
      tedbl = ((System.currentTimeMillis() - (long) time) / 1000.0);
      timeleftdbl = (double) roundTime - tedbl;

      tenths.getComponent(SpriteAnimationStateComponent.class).paused = false;
      ones.getComponent(SpriteAnimationStateComponent.class).paused = false;

      tenths.getComponent(SpriteAnimationStateComponent.class)
        .set(new FrameRange("tens", (timeleft % 100) / 10, (timeleft % 100) / 10), 1, Animation.PlayMode.NORMAL);
      ones.getComponent(SpriteAnimationStateComponent.class)
        .set(new FrameRange("ones" + timeleft + "", timeleft % 10, timeleft % 10), 1, Animation.PlayMode.NORMAL);

      ones.getComponent(SpriteAnimationStateComponent.class).paused = true;
      tenths.getComponent(SpriteAnimationStateComponent.class).paused = true;

      if (((timeleft % 100) / 10) != tempten)
      {
        animatenum(tenths);
      }
      if ((timeleft % 10) != tempone)
      {
        animatenum(ones);
      }
    }
    if (timeleft == 0)
    {
      if (!standby)
      {
        eoround = true;
      }
    }
  }

  public void animatenum(Entity num)
  {

    Actions.addAction(num, Actions.sequence(Actions.scaleTo(0, 0, 0f),
      Actions.parallel(Actions.rotateBy(720f, .360f), Actions.scaleBy(1f, 1f, .6f, Interpolation.elastic))));

  }

  public void animatecount(Entity num)
  {

    Actions.addAction(num, Actions.scaleTo(1f, 1f, .3f, Interpolation.elastic));

  }

   public void totalhealth()
  {
    LifeSystem lifesystem = new LifeSystem(access.update(access.view).getBatch(), access.viewportland.getCamera(),
      this);
    SpecialSystem Specsystem = new SpecialSystem(access.update(access.view).getBatch(), access.viewportland.getCamera(),
      this);
    for (int i = 0; i < processed.size(); i++)
    {
      // CharAttributes healthgetter = new CharAttributes(null);

      if (i < 3)
      {
        // p1health += healthgetter.getHealth();
        p1health += 1000;
      }
      else
      {
        // p2health += healthgetter.getHealth();
        p2health += 1000;
      }
    }

    startp1 = p1health;
    startp2 = p2health;


    stageWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity()
      .add(new LifeTextureComponent(health1, outline1, startp1, stageWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("healthout1").getEntity(), true));

    stageWrapper.getChild("ctrlandhud").getChild("healthcheck2").getEntity()
      .add(new LifeTextureComponent(health2, outline2, startp2,
        stageWrapper.getChild("ctrlandhud").getChild("healthcheck2").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("healthout2").getEntity(), false));

    stageWrapper.getChild("ctrlandhud").getChild("supbarone").getEntity()
      .add(new SpecialTexComponent(specmeter1, exdots1, superout1, 0,
        stageWrapper.getChild("ctrlandhud").getChild("supbarone").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("fillone").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("sprmtrone").getEntity(), true));

    stageWrapper.getChild("ctrlandhud").getChild("supbartwo").getEntity()
      .add(new SpecialTexComponent(specmeter2, exdots2, superout2, 0,
        stageWrapper.getChild("ctrlandhud").getChild("supbartwo").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("filltwo").getEntity(),
        stageWrapper.getChild("ctrlandhud").getChild("sprmtrtwo").getEntity(), false));

    Entity healthreg = stageWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity();

   /* p1HPpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity()
      .getComponent(LifeTextureComponent.class).width;

    p2HPpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity()
      .getComponent(LifeTextureComponent.class).width;

    begpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck2").getEntity()
      .getComponent(LifeTextureComponent.class).width;*/
   /*p1HPpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity()
      .getComponent(DimensionsComponent.class).width;

    p2HPpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck1").getEntity()
            .getComponent(DimensionsComponent.class).width;

    begpercent = stageItemWrapper.getChild("ctrlandhud").getChild("healthcheck2").getEntity()
            .getComponent(DimensionsComponent.class).width;*/
   p1HPpercent = healthreg.getComponent(LifeTextureComponent.class).width;
   p2HPpercent = healthreg.getComponent(LifeTextureComponent.class).width;
   begpercent = healthreg.getComponent(LifeTextureComponent.class).width;

    begsuperpct = stageWrapper.getChild("ctrlandhud").getChild("supbarone").getEntity()
      .getComponent(SpecialTexComponent.class).width;

    access.update(access.view).engine.addSystem(lifesystem);
    lifesystem.priority = 50;
    access.update(access.view).engine.addSystem(Specsystem);
    Specsystem.priority = 51;
  }

  public void checkstun(int player)
  {
    float fd;
    if (((Attribs) arrscripts.get(player)).attacked())
    {
      anim = "stun";
    }
    else
    {
      if (((Attribs) arrscripts.get(player)).attributes().y == GROUND)
      {
        anim = "block";
        if (spfzp2move.isDown)
        {
          anim = "dblock";
        }
      }
      else
      {
        anim = "ablock";
      }
    }
      ((Attribs) arrscripts.get(player)).animationstate().set(
      ((Attribs) arrscripts.get(player)).animationcomponent().frameRangeMap.get(anim), 60, Animation.PlayMode.NORMAL);
  }

  public void collision(int i)
  {

    setcollisionboxes(i);

    if(boxes)
    {
      showcollisionboxes(i);
    }

    // Collision boxes that keep the players from crossing through each
    // other
    close = false;

    if (spfzp1move.setrect().overlaps(spfzp2move.setrect()))
    {
      // needs to be just a variable stating that the objects are within close
      // range so we
      // will need to alter the speeds
      controlcrossing();

    }

    // Collision boxes that will process the health as well as other
    // functionalities
    // when dealing with hitboxes
    if (stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
    {
      if (stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete())
      {
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).x = 0;
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).y = -20f;

        stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects
          .removeValue(stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
            .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);

      }
    }
    if (stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
    {
      if (stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete())
      {
        stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).x = 0;
        stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).y = -20f;
        stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects
          .removeValue(stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
            .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);

      }
    }
    if (spfzp1move.attacking() || spfzp1move.projact || spfzp2move.attacking())
    {
      hitboxprocessing();
    }

  }

  public void pausechar()
  {
    spfzp1move.tempfdur = spfzp1move.spfzanimationstate.currentAnimation.getFrameDuration();
    spfzp1move.spfzanimationstate.currentAnimation.setFrameDuration(5f);
  }

  public void hitboxprocessing()
  {
    Vector2 hitconfirm = new Vector2();

    // If player one attacked player 2
    if (spfzp1move.sethitbox().overlaps(spfzp2move.setcharbox()) && spfzp1move.attacking && !spfzp1move.getboxconfirm()
      || spfzp1move.projconfirm())
    {
      if (spfzp1move.hitboxsize.x > 0 || spfzp1move.projconfirm())
      {
        spfzp1move.hitboxconfirm(true);
        spfzp1move.wallb = false;
        if(spfzp1move.bouncer)
        {
          spfzp2move.bounced = true;
          spfzp1move.wallb = true;
          spfzp1move.bouncer = false;
        }
        spfzp1move.sethitbox().getCenter(hitconfirm);

        hitconfirm.set((spfzp2move.setcharbox().x + spfzp1move.sethitbox().x + spfzp1move.sethitbox().width) * .5f,
          hitconfirm.y);

        //somehow extra call to hit() method is allowing the
        //pushback to work. Need to correct logic elsewhere
        spfzp2move.hit();

        if (spfzp2move.hit())
        {
          spfzp2move.attacked = true;
          spfzp2move.blocking = false;
        }
        else
        {
          spfzp2move.attacked = false;
          spfzp2move.blocking = true;
        }
        if (!spfzp1move.projconfirm())
        {
          pausechar();
        }
        checkstun(p2);

        float tempflip;
        if (spfzp1move.center() < spfzp2move.center())
        {
          tempflip = 1f;
        }
        else
        {
          tempflip = -1f;
        }

        //Set particle effects to appropriate scaling based on hitboxsize that the opponent was attacked by
        stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
          .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleX = (spfzp1move.hitboxsize.y / 50f) * tempflip;
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleY =
          (spfzp1move.hitboxsize.y / 50f) * tempflip;


        stageWrapper.getChild("p1block").getChild("p1bconfirm").getEntity()
          .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
        stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleX = (spfzp1move.hitboxsize.y / 50f) * tempflip;
        stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleY = (spfzp1move.hitboxsize.y / 50f) * tempflip;

        if (spfzp1move.attributes().y > spfzp1move.charGROUND())
        {
          stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;
          stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;

        }
        else
        {
          stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = 0;
          stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = 0;
        }

        // Set the positioning of the particle effects and handle hit events
        if (spfzp2move.attacked)
        {
          if (spfzp1move.projconfirm())
          {
            if (spfzp1move.projectile.spfzattribute.scaleX > 0)
            {
              stageWrapper.getChild("p1hit").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                + spfzp1move.projectile.spfzdim.width;
            }
            else
            {
              stageWrapper.getChild("p1hit").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                - spfzp1move.projectile.spfzdim.width;
            }

            stageWrapper.getChild("p1hit").getEntity()
              .getComponent(TransformComponent.class).y = spfzp1move.projectile.spfzattribute.y
              + spfzp1move.projectile.spfzdim.height * .5f;
            spfzp1move.projectile.hit = false;
          }
          else
          {
            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).x = hitconfirm.x;

            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).y = hitconfirm.y;
          }

          stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity().getComponent(SPFZParticleComponent.class)
            .startEffect();
          // shake = true;

          if (!damagedealt)
          {
            Entity Hit = stageWrapper.getChild("ctrlandhud").getChild("p1himg").getEntity();
            Entity cc = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getEntity();
            spfzp2move.setcombonum(spfzp2move.combonum() + 1);

            // Hit.getComponent(TransformComponent.class).originY =
            // Hit.getComponent(DimensionsComponent.class).height * .5f;
            if (spfzp2move.combonum() >= 2)
            {
              if (spfzp2move.combonum() == 2 && Hit.getComponent(TransformComponent.class).scaleY == 0
                && cc.getComponent(TransformComponent.class).scaleY == 0)
              {

                Actions.addAction(Hit, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));
                Actions.addAction(cc, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));

              }

              Entity parent = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getEntity();
              Entity p2cntTEN = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getChild("tenths").getEntity();
              Entity p2cntONE = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getChild("ones").getEntity();
              LabelComponent combocount1;
              combocount1 = stageWrapper.getChild("ctrlandhud").getChild("combocount1").getEntity()
                .getComponent(LabelComponent.class);

              // combocount1.setText(Integer.toString(spfzp2move.combonum()) + "
              // HITS");

              combocounter(parent, p2cntTEN, p2cntONE, spfzp2move.combonum());
            }
            else if(spfzp2move.combonum() == 1 && Hit.getComponent(TransformComponent.class).scaleY >= 0 &&
              cc.getComponent(TransformComponent.class).scaleY >= 0)
            {
              Actions.removeActions(Hit);
              Actions.removeActions(cc);
              Actions.addAction(Hit, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
              Actions.addAction(cc, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
            }
            //else if(spfzp2move.combonum() == 1)

            if (spfzp1move.input == -1)
            {
              p2health -= 200f;
              p1spec += 120f;
              p2spec += 120f;

            }
            else
            {
              p2health -= player1data.get(p1).get(spfzp1move.HITDMG).get(spfzp1move.move).intValue();
              p1spec += player1data.get(p1).get(spfzp1move.HITMTR).get(spfzp1move.move).intValue();
              p2spec += player1data.get(p1).get(spfzp1move.HITMTR).get(spfzp1move.move).intValue() / 2;
            }
            damagedealt = true;
          }
        }
        else
        {
          if (spfzp1move.projconfirm())
          {
            if (spfzp1move.projectile.spfzattribute.scaleX > 0)
            {
              stageWrapper.getChild("p1block").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                + spfzp1move.projectile.spfzdim.width;
            }
            else
            {
              stageWrapper.getChild("p1block").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                - spfzp1move.projectile.spfzdim.width;
            }

            stageWrapper.getChild("p1block").getEntity()
              .getComponent(TransformComponent.class).y = spfzp1move.projectile.spfzattribute.y
              + spfzp1move.projectile.spfzdim.height * .5f;
            spfzp1move.projectile.hit = false;
          }
          else
          {

            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).x = hitconfirm.x;
            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).y = hitconfirm.y;
          }
          stageWrapper.getChild("p1block").getChild("p1bconfirm").getEntity().getComponent(SPFZParticleComponent.class)
            .startEffect();
          // shake = true;
        }
      }
    }

    // If player two attacked player 1
    else if ((spfzp2move.sethitbox().overlaps(spfzp1move.setcharbox()) && spfzp2move.attacking()
      && !spfzp2move.getboxconfirm()) || spfzp2move.projhit)
    {
      // spfzp2move.hit();
      if (spfzp2move.hitboxsize.x > 0 || spfzp2move.projhit)
      {
        spfzp2move.hitboxconfirm(true);
        spfzp2move.sethitbox().getCenter(hitconfirm);

        hitconfirm.set((spfzp1move.setcharbox().x + spfzp2move.sethitbox().x + spfzp2move.sethitbox().width) * .5f,
          hitconfirm.y);


        if (spfzp1move.hit())
        {
          spfzp1move.attacked = true;
          spfzp1move.blocking = false;
        }
        else
        {
          //spfzp1move.attacked = true;
          spfzp1move.blocking = true;

        }

        if (spfzp2move.projhit)
        {
          spfzp2move.projhit = false;

        }

        float tempflip;
        if (spfzp2move.center() < spfzp1move.center())
        {
          tempflip = 1f;
        }
        else
        {
          tempflip = -1f;
        }
        //Set particle effects to appropriate scaling based on hitboxsize that the opponent was attacked by
        stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
          .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
        stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).scaleX = (spfzp2move.hitboxsize.y / 100f) * tempflip;
        stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).scaleY =
          (spfzp2move.hitboxsize.y / 100f) * tempflip;


        stageWrapper.getChild("p2block").getChild("p2bconfirm").getEntity()
          .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
        stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).scaleX = (spfzp2move.hitboxsize.y / 100f) * tempflip;
        stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).scaleY =
          (spfzp2move.hitboxsize.y / 100f) * tempflip;

        if (spfzp2move.attributes().y > spfzp2move.charGROUND())
        {
          stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;
          stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;

        }
        else
        {
          stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).rotation = 0;
          stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).rotation = 0;
        }

        // Set the positioning of the particle effects and handle hit events
        if (spfzp1move.attacked)
        {
          if (spfzp1move.projconfirm())
          {
            if (spfzp1move.projectile.spfzattribute.scaleX > 0)
            {
              stageWrapper.getChild("p2hit").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                + spfzp1move.projectile.spfzdim.width;
            }
            else
            {
              stageWrapper.getChild("p2hit").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                - spfzp1move.projectile.spfzdim.width;
            }

            stageWrapper.getChild("p2hit").getEntity()
              .getComponent(TransformComponent.class).y = spfzp1move.projectile.spfzattribute.y
              + spfzp1move.projectile.spfzdim.height * .5f;
            spfzp1move.projectile.hit = false;
          }
          else
          {
            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).x = hitconfirm.x;

            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).y = hitconfirm.y;
          }

          stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity().getComponent(SPFZParticleComponent.class)
            .startEffect();
          // shake = true;

          if (!damagedealt)
          {
            Entity Hit = stageWrapper.getChild("ctrlandhud").getChild("p2himg").getEntity();
            Entity cc = stageWrapper.getChild("ctrlandhud").getChild("p2cc").getEntity();
            spfzp2move.setcombonum(spfzp2move.combonum() + 1);

            // Hit.getComponent(TransformComponent.class).originY =
            // Hit.getComponent(DimensionsComponent.class).height * .5f;
            if (spfzp2move.combonum() >= 2)
            {
              if (spfzp2move.combonum() == 2 && Hit.getComponent(TransformComponent.class).scaleY == 0
                && cc.getComponent(TransformComponent.class).scaleY == 0)
              {

                Actions.addAction(Hit, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));
                Actions.addAction(cc, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));

              }

              //handle combo counter
              Entity parent = stageWrapper.getChild("ctrlandhud").getChild("p2cc").getEntity();
              Entity p2cntTEN = stageWrapper.getChild("ctrlandhud").getChild("p2cc").getChild("tenths").getEntity();
              Entity p2cntONE = stageWrapper.getChild("ctrlandhud").getChild("p2cc").getChild("ones").getEntity();
              LabelComponent combocount2;
              combocount2 = stageWrapper.getChild("ctrlandhud").getChild("combocount2").getEntity()
                .getComponent(LabelComponent.class);


              combocounter(parent, p2cntTEN, p2cntONE, spfzp2move.combonum());
            }


            if (spfzp2move.move == -1)
            {
              p1health -= 200f;
              p2spec += 120f;
              p2spec += 120f;

            }
            else
            {
              //p1health -= player2data.get(p2).get(spfzp2move.HITDMG).get(spfzp2move.move).intValue();
              //p2spec += player2data.get(p2).get(spfzp2move.HITMTR).get(spfzp2move.move).intValue();
              //p1spec += player2data.get(p2).get(spfzp2move.HITMTR).get(spfzp2move.move).intValue() / 2;
            }
            damagedealt = true;
          }
        }
        else
        {
          if (spfzp1move.projconfirm())
          {
            if (spfzp1move.projectile.spfzattribute.scaleX > 0)
            {
              stageWrapper.getChild("p2block").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                + spfzp1move.projectile.spfzdim.width;
            }
            else
            {
              stageWrapper.getChild("p2block").getEntity()
                .getComponent(TransformComponent.class).x = spfzp1move.projectile.spfzattribute.x
                - spfzp1move.projectile.spfzdim.width;
            }

            stageWrapper.getChild("p2block").getEntity()
              .getComponent(TransformComponent.class).y = spfzp1move.projectile.spfzattribute.y
              + spfzp1move.projectile.spfzdim.height * .5f;
            spfzp1move.projectile.hit = false;
          }
          else
          {

            stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).x = hitconfirm.x;
            stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).y = hitconfirm.y;
          }
          stageWrapper.getChild("p2block").getChild("p2bconfirm").getEntity().getComponent(SPFZParticleComponent.class)
            .startEffect();
        }
      }
    }

  }

  public void newcrossing()
  {
    if (spfzp1move.center() < spfzp2move.center())
    {
      if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp1move.attributes().x -= spfzp1move.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();

        spfzp2move.attributes().x += spfzp1move.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();
      }
      else
      {
        spfzp1move.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
      }
    }
    else
    {

    }
  }

  public void combocounter(Entity parent, Entity tens, Entity ones, int cc)
  {
    float holder, holder1;

    // holder1 = parent.getComponent(TransformComponent.class).y;
    // holder = parent.getComponent(TransformComponent.class).y -
    // parent.getComponent(DimensionsComponent.class).height * .5f;
    parent.getComponent(TransformComponent.class).originY = parent.getComponent(DimensionsComponent.class).height * .5f;

    // parent.getComponent(TransformComponent.class).y = holder;

    tens.getComponent(SpriteAnimationStateComponent.class).paused = false;
    ones.getComponent(SpriteAnimationStateComponent.class).paused = false;

    tens.getComponent(SpriteAnimationStateComponent.class).set(new FrameRange("tens", (cc % 100) / 10, (cc % 100) / 10),
      1, Animation.PlayMode.NORMAL);
    ones.getComponent(SpriteAnimationStateComponent.class).set(new FrameRange("ones" + cc + "", cc % 10, cc % 10), 1,
      Animation.PlayMode.NORMAL);

    ones.getComponent(SpriteAnimationStateComponent.class).paused = true;
    tens.getComponent(SpriteAnimationStateComponent.class).paused = true;

    // if(((timeleft % 100) / 10) != tempten)
    // {
    // animatenum(tens);
    // }
    animatecount(ones);
    Entity Hit = stageWrapper.getChild("ctrlandhud").getChild("p2himg").getEntity();
    parent.getComponent(TransformComponent.class).y = Hit.getComponent(TransformComponent.class).y;
    // parent.getComponent(TransformComponent.class).y = holder1;

  }

  public void controlcrossing()
  {
    close = true;

    // /////////////////////////////////////////PLAYER ONE ON THE RIGHT
    // SIDE //////////////////////////////////////////////////////
    // /////////////////////////////////////////PLAYER TWO ON THE LEFT
    // SIDE //////////////////////////////////////////////////////

    neutral = true;
    // check to see if the players have both stopped moving that way they can be
    // readjusted
    for (int j = 0; j < 2; j++)
    {
      if (spfzp1move.p1movement.get(j) || spfzp2move.p2movement.get(j))
      {
        neutral = false;
      }
    }
    // leave as is for now. was neutral
    if (neutral || (spfzp1move.attributes().y > spfzp1move.charGROUND() || spfzp2move.attributes().y > spfzp2move.charGROUND()))
    {
      checkneutral();
    }

    if (!spfzp1move.dash)
    {
      if (p1xattr > p2xattr)
      {

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // LEFT //////////////////////////////////////////////////////

        if (spfzp1move.p1movement.get(0))
        {
          P1SRightMLeft();
        }

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // RIGHT //////////////////////////////////////////////////////

        else if (spfzp1move.p1movement.get(1))
        {
          P1SRightMRight();
        }
      }

      // /////////////////////////////////////////PLAYER ONE ON THE LEFT
      // SIDE //////////////////////////////////////////////////////
      // /////////////////////////////////////////PLAYER TWO ON THE RIGHT
      // SIDE /////////////////////////////////////////////////////

      else
      {

        // ///////////////////////////////// PLAYER ONE MOVING TO THE RIGHT
        // //////////////////////////////////////////////////////
        if (spfzp1move.p1movement.get(1))
        {
          P1SLeftMRight();
        }

        // ///////////////////////////////// PLAYER ONE MOVING TO THE LEFT
        // //////////////////////////////////////////////////////

        else if (spfzp1move.p1movement.get(0))
        {
          P1SLeftMLeft();
        }
      }
    }
    else
    {
      if (spfzp1move.dash)
      {
        if (spfzp1move.dashdir == 0)
        {
          if (stageCamera.position.x <= camboundary[0] + 1 && spfzp2move.attributes().x <= stageboundary[0])
          {
            spfzp1move.attributes().x += 0f;
            spfzp2move.attributes().x += 0f;
          }
          else
          {
            spfzp2move.attributes().x -= (spfzp1move.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzp1move.attributes().x -= (spfzp1move.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
        else
        {
          if (stageCamera.position.x + 1 >= camboundary[1] && spfzp2move.attributes().x + 1 >= stageboundary[1])
          {
            spfzp1move.attributes().x += 0f;
            spfzp2move.attributes().x += 0f;
          }
          else
          {
            spfzp2move.attributes().x += (spfzp1move.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzp1move.attributes().x += (spfzp1move.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
      }
    }

  }

  public void setface()
  {

    if (spfzp1move.center() < spfzp2move.center())
    {
      if (spfzp1move.attributes().scaleX < 0 && !faceright1)
      {
        faceright1 = true;
      }
      if (spfzp2move.attributes().scaleX > 0 && !faceleft2)
      {
        faceleft2 = true;
      }
    }

    if (spfzp1move.center() > spfzp2move.center())
    {
      if (spfzp1move.attributes().scaleX > 0 && !faceleft1 && spfzp1move.setrect().y <= GROUND)
      {
        faceleft1 = true;
      }
      if (spfzp2move.attributes().scaleX < 0 && !faceright2 && spfzp2move.setrect().y <= GROUND)
      {
        faceright2 = true;
      }
    }

    setfacingp1();
    setfacingp2();

  }

  public void setfacingp1()
  {


    if (faceright1)
    {
      faceleft1 = false;
      if (spfzp1move.attributes().y <= spfzp1move.charGROUND() && spfzp1move.attributes().scaleX < 0)
      {

          spfzp1move.attributes().scaleX *= -1f;
          spfzp1move.attributes().x -= spfzp1move.dimrect.width - (spfzp1move.adjustX + (spfzp1move.setrect().width * .5f));
          faceright1 = false;

      }
    }
    if (faceleft1)
    {
      faceright1 = false;
      if (spfzp1move.attributes().y <= spfzp1move.charGROUND() && spfzp1move.attributes().scaleX > 0)
      {
          spfzp1move.attributes().scaleX *= -1f;

          spfzp1move.attributes().x += spfzp1move.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
          faceleft1 = false;
      }
    }
  }

  public void setfacingp2()
  {
    if (faceright2)
    {
      if (spfzp2move.attributes().y <= spfzp2move.charGROUND() && spfzp2move.attributes().scaleX < 0)
      {
        spfzp2move.attributes().scaleX *= -1f;
        spfzp2move.attributes().x -= spfzp2move.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
        faceright2 = false;
      }
    }
    if (faceleft2)
    {
      if (spfzp2move.attributes().y <= spfzp2move.charGROUND() && spfzp2move.attributes().scaleX > 0)
      {
        spfzp2move.attributes().scaleX *= -1f;
        spfzp2move.attributes().x += spfzp2move.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
        faceleft2 = false;
      }
    }
  }

  public void P1SRightMLeft()
  {
    // player 2 is moving left
    if (spfzp2move.p2movement.get(0))
    {

      if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

      else if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
      {

        spfzp2move.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

    }
    // player 2 is moving right
    else if (spfzp2move.p2movement.get(1))
    {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzp2move.moveandjump().x - spfzp1move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x += (spfzp2move.moveandjump().x - spfzp1move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      else
      {
        spfzp2move.attributes().x += 0f;
        spfzp1move.attributes().x += 0f;
      }
    }
    // if player 2 is neutral
    else
    {

      if (stageCamera.position.x <= camboundary[0] + 1 && spfzp2move.attributes().x <= stageboundary[0])
      {
        spfzp1move.attributes().x += 0f;
      }
      else
      {

        spfzp2move.attributes().x -= (spfzp1move.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x -= (spfzp1move.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void P1SRightMRight()
  {
    // if player 2 is moving left
    if (spfzp2move.p2movement.get(0))
    {
      spfzp1move.attributes().x += spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();
      spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
    // if player 2 is moving right
    else if (spfzp2move.p2movement.get(1))
    {

      if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp1move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

        if (spfzp2move.attributes().x <= stageboundary[1] - (spfzp2move.setrect().width * .33f))
        {

        }
      }
      else if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        if (spfzp2move.attributes().x <= stageboundary[1] - (spfzp2move.setrect().width * .33f))
        {

        }
        else
        {
          spfzp1move.attributes().x += spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();
          spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        }

      }
    }
    else
    {
      spfzp1move.attributes().x += spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
  }

  public void P1SLeftMLeft()
  {
    // if player 2 is moving to the left
    if (spfzp2move.p2movement.get(0))
    {
      if (spfzp1move.attributes().x <= stageboundary[0] + (spfzp1move.setrect().width * .33f))
      {
        spfzp2move.attributes().x += 0f;
      }
      else
      {
        if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
        {
          spfzp2move.attributes().x -= spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();
          spfzp1move.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        }
        else if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
        {
          spfzp1move.attributes().x += -(spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime());
          spfzp2move.attributes().x += -(spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime());
        }

      }
    }

    // if player 2 is moving to right
    else if (spfzp2move.p2movement.get(1))
    {

      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x -= (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      else
      {
        spfzp2move.attributes().x -= 0f;
        spfzp2move.attributes().x -= 0f;
      }
    }

    else
    {
      spfzp1move.attributes().x -= spfzp1move.moveandjump().x * Gdx.graphics.getDeltaTime();
    }
  }

  public void P1SLeftMRight()
  {
    // if player 2 is moving to the left
    if (spfzp2move.p2movement.get(0))
    {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzp1move.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x += (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzp1move.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += (spfzp1move.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }

    }

    // if player 2 is neutral
    else if (!spfzp2move.p2movement.get(0) && !spfzp2move.p2movement.get(1))
    {

      if (stageCamera.position.x + 1 >= camboundary[1] && spfzp2move.attributes().x + 1 >= stageboundary[1])
      {
        spfzp1move.attributes().x += 0f;
      }
      else
      {

        spfzp2move.attributes().x += (spfzp1move.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzp1move.attributes().x += (spfzp1move.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
    // if player 2 is moving to right
    else if (spfzp2move.p2movement.get(1))
    {
      if (spfzp2move.moveandjump().x < spfzp1move.moveandjump().x)
      {

        spfzp2move.attributes().x += (spfzp1move.moveandjump().x * .25f) * Gdx.graphics.getDeltaTime();

      }
      else if (spfzp2move.moveandjump().x > spfzp1move.moveandjump().x)
      {
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void setcollisionboxes(int i)
  {
    float reversebox;
    // temp reach will be the box length incoming
    float tempreach = 10;

    // used to determine how to position hitbox when facing left or right
    if (((Attribs) arrscripts.get(i)).attributes().scaleX > 0)
    {
      reversebox = 0;
      tempreach *= 1;
    }
    else
    {
      reversebox = ((Attribs) arrscripts.get(i)).hitboxsize().x;
      tempreach *= -1;
    }

    setstrkbox(i);

    setcrossbox(i);

    sethitbox(i, reversebox, tempreach);

    if (spfzp2move.reflect)
    {
      spfzp2move.setreflect();
    }

  }

  public void setstrkbox(int i)
  {
    // set character full strike box

    if(!((Attribs) arrscripts.get(i)).invul())
    {
      /*((Attribs) arrscripts.get(i)).setcharbox().set(
        ((Attribs) arrscripts.get(i)).center() - ((Attribs) arrscripts.get(i)).setrect().width,

        ((Attribs) arrscripts.get(i)).attributes().y, ((Attribs) arrscripts.get(i)).setrect().width,
        ((Attribs) arrscripts.get(i)).setrect().height);*/
      ((Attribs) arrscripts.get(i)).setcharbox();
    }
    else
    {
      ((Attribs) arrscripts.get(i)).setcharbox().set(0,0,0,0);
    }
  }

  public void shwstrkbox(int i)
  {
    // Show the character hit box

    ((Attribs) arrscripts.get(i)).drawcharbox().setProjectionMatrix(access.viewportland.getCamera().combined);
    ((Attribs) arrscripts.get(i)).drawcharbox().begin(ShapeType.Line);

    if (i == p1)
    {
      ((Attribs) arrscripts.get(i)).drawcharbox().setColor(Color.RED);
    }

    else
    {
      ((Attribs) arrscripts.get(i)).drawcharbox().setColor(Color.ROYAL);
    }

    ((Attribs) arrscripts.get(i)).drawcharbox().rect(((Attribs) arrscripts.get(i)).setcharbox().x,
      ((Attribs) arrscripts.get(i)).setcharbox().y, ((Attribs) arrscripts.get(i)).setcharbox().width,
      ((Attribs) arrscripts.get(i)).setcharbox().height);

    ((Attribs) arrscripts.get(i)).drawcharbox().end();
  }

  public void setcrossbox(int i)
  {
    // set character cross box
    ((Attribs) arrscripts.get(i)).setcross();
    spfzp1move.dimrectangle();
    spfzp2move.dimrectangle();

  }

  public void shwcrossbox(int i)
  {
    // Shows the cross box that keeps characters from crossing each other

    ((Attribs) arrscripts.get(i)).drawrect().setProjectionMatrix(access.viewportland.getCamera().combined);
    ((Attribs) arrscripts.get(i)).drawrect().begin(ShapeType.Line);

    if (i == p1)
    {
      ((Attribs) arrscripts.get(i)).drawrect().setColor(Color.WHITE);

    }

    else
    {
      ((Attribs) arrscripts.get(i)).drawrect().setColor(Color.WHITE);
    }

    ((Attribs) arrscripts.get(i)).drawrect().rect(((Attribs) arrscripts.get(i)).setcross().x,
      ((Attribs) arrscripts.get(i)).setcross().y, ((Attribs) arrscripts.get(i)).setcross().width,
      ((Attribs) arrscripts.get(i)).setcross().height);

    ((Attribs) arrscripts.get(i)).drawrect().end();

    spfzp1move.spfzdimbox.setProjectionMatrix(access.viewportland.getCamera().combined);
    spfzp1move.spfzdimbox.begin(ShapeType.Line);
    spfzp1move.spfzdimbox.setColor(Color.LIME);
    spfzp2move.spfzdimbox.setProjectionMatrix(access.viewportland.getCamera().combined);
    spfzp2move.spfzdimbox.begin(ShapeType.Line);
    spfzp2move.spfzdimbox.setColor(Color.LIME);

    /*spfzp1move.dimrectangle();
    spfzp2move.dimrectangle();*/
    spfzp1move.spfzdimbox.rect(spfzp1move.dimrect.x, spfzp1move.dimrect.y, spfzp1move.dimrect.width, spfzp1move.dimrect.height);
    spfzp2move.spfzdimbox.rect(spfzp2move.dimrect.x, spfzp2move.dimrect.y, spfzp2move.dimrect.width, spfzp2move.dimrect.height);

    spfzp1move.spfzdimbox.end();
    spfzp2move.spfzdimbox.end();
  }

  public void sethitbox(int i, float reversebox, float tempreach)
  {
    // When setting boxes(position x, position y, width, height)

    // Sets the hitbox for attacks.
    // if the character is attacking, and the current frame of animation is
    // the active frames based on the
    // activeframe array, draw the hitbox
    if (((Attribs) arrscripts.get(i)).attacking())
    {
      if (damagedealt)
      {
        damagedealt = false;
      }

      //reset the hitbox and call sethitbox method to call function that created the hitbox values coming in from database
      ((Attribs) arrscripts.get(i)).sethitbox().set(0, 0, 0, 0);

      if (((Attribs) arrscripts.get(i)).currentframe() >= ((Attribs) arrscripts.get(i)).activeframes()[0]
        && ((Attribs) arrscripts.get(i)).currentframe() <= ((Attribs) arrscripts.get(i)).activeframes()[1])
      {
        if (((Attribs) arrscripts.get(i)).attributes().y > GROUND)
        {
          spfzp1move.inair = true;
        }

        ((Attribs) arrscripts.get(i)).sethitbox();
      }
      else if (((Attribs) arrscripts.get(i)).currentframe() > ((Attribs) arrscripts.get(i)).activeframes()[1])
      {
        if (i == p1)
        {
          spfzp1move.inair = false;
        }
        ((Attribs) arrscripts.get(i)).hitboxpos().setZero();
        ((Attribs) arrscripts.get(i)).hitboxsize().setZero();
        ((Attribs) arrscripts.get(i)).hitboxconfirm(false);
      }
    }

  }

  public void shwhitbox(int i)
  {
    // Projectile Hitboxes
    if (spfzp1move.projectile != null)
    {
      spfzp1move.projectile.hitbox();
    }
    // Shows the hitbox for attacks.
    // if the character is attacking, and the current frame of animation is
    // the active frames based on the
    // activeframe array, draw the hitbox

    if (((Attribs) arrscripts.get(i)).attacking())
    {
      if (((Attribs) arrscripts.get(i)).currentframe() >= ((Attribs) arrscripts.get(i)).activeframes()[0]
        && ((Attribs) arrscripts.get(i)).currentframe() <= ((Attribs) arrscripts.get(i)).activeframes()[1])
      {
        // update hitbox positioning based on character
        // This will eventually need to be a method that will update hitbox
        // positions due to the variation of characters

        ((Attribs) arrscripts.get(i)).drawhitbox().setProjectionMatrix(access.viewportland.getCamera().combined);
        ((Attribs) arrscripts.get(i)).drawhitbox().begin(ShapeType.Filled);

        ((Attribs) arrscripts.get(i)).drawhitbox().setColor(Color.ORANGE);

        ((Attribs) arrscripts.get(i)).drawhitbox().rect(((Attribs) arrscripts.get(i)).sethitbox().x,
          ((Attribs) arrscripts.get(i)).sethitbox().y, ((Attribs) arrscripts.get(i)).sethitbox().width,
          ((Attribs) arrscripts.get(i)).sethitbox().height);

        ((Attribs) arrscripts.get(i)).drawhitbox().end();
      }
    }
  }

  public void showcollisionboxes(int i)
  {

    shwstrkbox(i);
    shwcrossbox(i);
    shwhitbox(i);

    if (spfzp2move.reflect)
    {
      spfzp2move.shwreflect();
    }

  }

  public void checkneutral()
  {

    if (spfzp1move.center() > spfzp2move.center()
      || spfzp1move.setrect().x + spfzp1move.setrect().width > spfzp2move.center())
    {
      if (spfzp1move.setrect().x + (spfzp1move.setrect().width * .5f) > spfzp2move.setrect().x
        + (spfzp2move.setrect().width * .5f))
      {
        spfzp1move.attributes().x += 1.2f;
        spfzp2move.attributes().x -= 1.2f;
      }
      else
      {
        spfzp1move.attributes().x -= 1.2f;
        spfzp2move.attributes().x += 1.2f;
      }
    }

    if (spfzp2move.center() > spfzp1move.center()
      || spfzp2move.setrect().x + spfzp2move.setrect().width > spfzp1move.center())
    {
      if (spfzp2move.setrect().x + (spfzp2move.setrect().width * .5f) > spfzp1move.setrect().x
        + (spfzp1move.setrect().width * .5f))
      {
        spfzp2move.attributes().x += 1.2f;
        spfzp1move.attributes().x -= 1.2f;
      }
      else
      {
        spfzp2move.attributes().x -= 1.2f;
        spfzp1move.attributes().x += 1.2f;
      }
    }

  }

  public void newcam()
  {
    Vector3 movecamera = new Vector3();
    float[] bounds;
    float camX, camY;
    // "camX" will be placed between player one and player two. It will stop
    // whenever it has reached either the left
    // or right bound.

    //p1locX = spfzp1move.center();
   // p2locX = spfzp2move.center();
    //p1locY = spfzp1move.setrect().y + spfzp1move.setrect().height * .5f;
    //p2locY = spfzp2move.setrect().y + spfzp2move.setrect().height * .5f;

    camX = (spfzp1move.center() + spfzp2move.center()) * .5f;
    camY = ((spfzp1move.setrect().y + spfzp1move.setrect().height * .5f) + (spfzp2move.setrect().y + spfzp2move.setrect().height * .5f)) * .5f;

    if (camX < bounds[0])
    {
      camX = bounds[0];
    }
    else if (camX > bounds[1])
    {
      camX = bounds[1];
    }

    if (camY < HALF_WORLDH)
    {
      movecamera.set(camX, HALF_WORLDH, 0);
    }
    else
    {
      movecamera.set(camX, camY, 0);
    }

    if (!p1charzoom && !p2charzoom)
    {
      if (((OrthographicCamera) access.viewportland.getCamera()).zoom != 1f)
      {
        access.Zoom(1f, .3f, movecamera.x, movecamera.y);

        if (((OrthographicCamera) access.viewportland.getCamera()).zoom > .998f)
        {
          ((OrthographicCamera) access.viewportland.getCamera()).zoom = 1f;
        }
      }
      stageCamera.position.lerp(movecamera, .3f);
      if (camcon == 3)
      {
        camcon = 0;
      }
    }
    else
    {
      if (p1charzoom)
      {

        if (p2charzoom)
        {
          p2charzoom = false;
        }

        if (spfzp1move.attributes().scaleX > 0)
        {
          if (camcon == 0 || camcon == 2)
          {
            access.Zoom(.25f, .2f, spfzp1move.attributes().x + (spfzp1move.spfzrect.width),
              spfzp1move.attributes().y + (spfzp1move.spfzrect.height * .5f));

            if (camcon != 2)
            {
              if (((OrthographicCamera) access.viewportland.getCamera()).zoom <= .26f)
              {
                camcon = 1;
              }
            }
            else
            {
              if (((OrthographicCamera) access.viewportland.getCamera()).zoom <= .26f)
              {
                camcon = 3;
              }
            }
          }
          else
          {
            if (camcon != 3)
            {
              access.Zoom(.75f, .1f, movecamera.x, movecamera.y);
              if (((OrthographicCamera) access.viewportland.getCamera()).zoom > .749f)
              {
                camcon = 2;
              }
            }
          }
        }
        else
        {
          access.Zoom(.25f, .5f, spfzp1move.attributes().x - (spfzp1move.spfzrect.width),
            spfzp1move.attributes().y + (spfzp1move.spfzrect.height * .5f));
        }

      }
      else if (p2charzoom)
      {
        p1charzoom = false;
        if (spfzp2move.attributes().scaleX > 0)
        {
          access.Zoom(.25f, .5f, spfzp2move.attributes().x + (spfzp2move.spfzrect.width),
            spfzp2move.attributes().y + (spfzp2move.spfzrect.height * .5f));
        }
        else
        {
          access.Zoom(.25f, .5f, spfzp2move.attributes().x - (spfzp2move.spfzrect.width),
            spfzp2move.attributes().y + (spfzp2move.spfzrect.height * .5f));
        }

      }
    }

    if (shake)
    {
      int rand = MathUtils.random(1);
      float incrementx = MathUtils.random(1);
      float incrementy = MathUtils.random(5);
      float newx, newy = HALF_WORLDH;
      if (rand == 0)
      {
        newx = stageCamera.position.x + incrementx;
        newy = stageCamera.position.y + incrementy;
      }
      else
      {
        newx = stageCamera.position.x - incrementx;
        newy = stageCamera.position.y - incrementy;
      }
      if (newy < HALF_WORLDH)
      {
        movecamera.set(newx, HALF_WORLDH, 0);
      }
      else
      {
        movecamera.set(newx, newy, 0);

      }
      // movecamera.set(newx, camY, 0);
      if(!spfzp1move.bouncer)
      {
        stageCamera.position.lerp(movecamera, 1f);
      }
    }
  }

  public void switchp1()
  {
    float facing;
    p1spec -= 100f;
    sigp1lock = true;
    strt1 = true;
    spfzp1move.attacking = false;
    if (spfzp1move.attributes().scaleX > 0)
    {
      facing = spfzp1move.attributes().scaleX;
    }
    else
    {
      facing = spfzp1move.attributes().scaleX * -1;
    }
    p1xattr = spfzp1move.attributes().x;

    p1yattr = spfzp1move.attributes().y;

    CompositeItemVO player1char1;
    CompositeItemVO player1char2;
    CompositeItemVO player1char3;
    ItemWrapper playerone;
    switch (p1)
    {
      case 0:

        p1++;
        access.land.getEngine().removeEntity(p1c1);
        player1char2 = access.land.loadVoFromLibrary(characters.get(p1));
        p1c2 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player1char2);
        p1c2.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c2.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c2.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        access.land.entityFactory.initAllChildren(access.land.getEngine(), p1c2, player1char2.composite);
        access.land.getEngine().addEntity(p1c2);
        playerone = new ItemWrapper(p1c2);
        break;

      case 1:
        p1++;
        access.land.getEngine().removeEntity(p1c2);
        player1char3 = access.land.loadVoFromLibrary(characters.get(p1));
        p1c3 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player1char3);
        p1c3.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c3.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c3.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        access.land.entityFactory.initAllChildren(access.land.getEngine(), p1c3, player1char3.composite);
        access.land.getEngine().addEntity(p1c3);
        playerone = new ItemWrapper(p1c3);

        break;

      case 2:
        p1 = 0;
        access.land.getEngine().removeEntity(p1c3);
        player1char1 = access.land.loadVoFromLibrary(characters.get(p1));
        p1c1 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player1char1);
        p1c1.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c1.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c1.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        access.land.entityFactory.initAllChildren(access.land.getEngine(), p1c1, player1char1.composite);
        access.land.getEngine().addEntity(p1c1);
        playerone = new ItemWrapper(p1c1);
        break;
      default:
        playerone = new ItemWrapper(null);
        break;
    }

    playerone.addScript((IScript) arrscripts.get(p1));

    // Ensure collision box is setup correctly

    if (facing > 0)
    {

      spfzp1move.setrect().set(spfzp1move.setrect().x + spfzp1move.adjustX,
        spfzp1move.attributes().y + spfzp1move.adjustY, spfzp1move.setrect().width,
        spfzp1move.setrect().height);
    }
    else
    {
      spfzp1move.setrect().set(spfzp1move.setrect().x - spfzp1move.adjustX,
              spfzp1move.attributes().y + spfzp1move.adjustY, spfzp1move.setrect().width, spfzp1move.setrect().height);

    }
    spfzp1move.attributes().scaleX = facing;

    // Start swap particle effect

    if (stageWrapper.getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
    {
      stageWrapper.getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects
        .removeValue(stageWrapper.getChild("p1swap").getChild("swapp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
    }
    stageWrapper.getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = spfzp1move.center();
    stageWrapper.getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = spfzp1move.attributes().y
      + spfzp1move.setrect().height * .5f;
    stageWrapper.getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
    stageWrapper.getChild("p1swap").getChild("swapp1").getEntity().getComponent(TransformComponent.class).scaleX = 1f;
    stageWrapper.getChild("p1swap").getChild("swapp1").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
    switchcount++;
    switchp1 = false;

  }

  public void switchp2()
  {

    if (switchp2 & switchcount == 0)
    {
      if (spfzp2move.attributes().scaleX > 0)
      {
        p2xattr = spfzp2move.attributes().x;
      }
      else
      {
        p2xattr = spfzp2move.attributes().x - spfzp2move.setrect().width;
      }
      p2yattr = spfzp2move.attributes().y;

      CompositeItemVO player2char1;
      CompositeItemVO player2char2;
      CompositeItemVO player2char3;
      ItemWrapper playertwo;
      switch (p2)
      {
        case 3:
          p2++;
          access.land.getEngine().removeEntity(p2c1);
          player2char2 = access.land.loadVoFromLibrary(characters.get(p2));
          p2c2 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player2char2);
          p2c2.getComponent(ZIndexComponent.class).layerIndex = 2;
          p2c2.getComponent(ZIndexComponent.class).setZIndex(4);
          access.land.entityFactory.initAllChildren(access.land.getEngine(), p2c2, player2char2.composite);
          access.land.getEngine().addEntity(p2c2);

          playertwo = new ItemWrapper(p2c2);
          playertwo.addScript((IScript) arrscripts.get(p2));
          spfzp2move.setrect().set(
            ((Attribs) arrscripts.get(3)).attributes().x + ((Attribs) arrscripts.get(3)).setrect().width * .33f,
            ((Attribs) arrscripts.get(3)).attributes().y, ((Attribs) arrscripts.get(3)).setrect().width,
            ((Attribs) arrscripts.get(3)).setrect().height);
          switchp2 = false;
          break;

        case 4:
          p2++;
          access.land.getEngine().removeEntity(p2c2);
          player2char3 = access.land.loadVoFromLibrary(characters.get(p2));
          p2c3 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player2char3);
          p2c3.getComponent(ZIndexComponent.class).layerIndex = 2;
          p2c3.getComponent(ZIndexComponent.class).setZIndex(4);
          access.land.entityFactory.initAllChildren(access.land.getEngine(), p2c3, player2char3.composite);
          access.land.getEngine().addEntity(p2c3);
          playertwo = null;
          playertwo = new ItemWrapper(p2c3);
          playertwo.addScript((IScript) arrscripts.get(p2));
          spfzp1move.setrect().set(
            ((Attribs) arrscripts.get(4)).attributes().x + ((Attribs) arrscripts.get(4)).setrect().width * .33f,
            ((Attribs) arrscripts.get(4)).attributes().y, ((Attribs) arrscripts.get(4)).setrect().width,
            ((Attribs) arrscripts.get(4)).setrect().height);
          switchp2 = false;
          break;

        case 5:
          p2 = 3;
          access.land.getEngine().removeEntity(p2c3);
          player2char1 = access.land.loadVoFromLibrary(characters.get(p2));
          p2c1 = access.land.entityFactory.createEntity(stageWrapper.getEntity(), player2char1);
          p2c1.getComponent(ZIndexComponent.class).layerIndex = 2;
          p2c1.getComponent(ZIndexComponent.class).setZIndex(4);
          access.land.entityFactory.initAllChildren(access.land.getEngine(), p2c1, player2char1.composite);
          access.land.getEngine().addEntity(p2c1);
          playertwo = null;
          playertwo = new ItemWrapper(p2c1);
          playertwo.addScript((IScript) arrscripts.get(p2));
          spfzp1move.setrect().set(
            ((Attribs) arrscripts.get(5)).attributes().x + ((Attribs) arrscripts.get(5)).setrect().width * .33f,
            ((Attribs) arrscripts.get(5)).attributes().y, ((Attribs) arrscripts.get(5)).setrect().width,
            ((Attribs) arrscripts.get(5)).setrect().height);
          switchp2 = false;
          break;
      }
      switchcount++;
    }

  }

  public void roundover()
  {

    // standby = true;

    if (!standby)
    {
      rdcount++;
      animateround(rdcount);
      standby = true;
    }
    // set characters back to neutral animation when round is over
    if (!setneut)
    {
      anim = "IDLE";
      setneut = true;
    }

    if (finishedrd && !gameover)
    {
      Vector3 CENTER = new Vector3(320, 200, 0);
      Entity fader;
      // LabelComponent timer;
      // fader =
      // stageItemWrapper.getChild("ctrlandhud").getChild("transition").getEntity();
      fader = stageWrapper.getChild("fader").getEntity();
      // timer =
      // stageItemWrapper.getChild("ctrlandhud").getChild("time").getEntity().getComponent(LabelComponent.class);

      eoround = false;
      resettimer();
      standby = true;
      finishedrd = false;

      // reset clock back to the round time then trigger the timer for the
      // next round
      timeleft = roundTime;
      time = System.currentTimeMillis();
      // timer.setText(Integer.toString(timeleft));
      Actions.addAction(fader, Actions.run(new Runnable()
      {

        @Override
        public void run()
        {

          if (!gameover)
          {
            if (p1health != startp1)
            {
              p1health = startp1;
              // p1spec = 0;

              p1HPpercent = health1.getWidth();// healthp1.getTexture().getWidth();
            }

            if (p2health != startp2)
            {
              p2health = startp2;
              // p2spec = 0;

              p2HPpercent = health2.getWidth();// healthp2.getTexture().getWidth();
            }

            timeElapsed = 0;
            timeleft = 99;
            time = System.currentTimeMillis();

            finishedrd = true;

          }

        }
      }));
      stageWrapper.getChild("ctrlandhud").getChild("pausebutton").getEntity()
        .getComponent(TransformComponent.class).scaleX = 1f;
      stageWrapper.getChild("ctrlandhud").getChild("pausebutton").getEntity()
        .getComponent(TransformComponent.class).scaleY = 0f;

      roundtextset();
      prefighttimer();

      // center stage and characters

      access.viewportland.getCamera().position.set(CENTER);
      access.viewportland.getCamera().update();

      if (access.viewportland.getCamera().position.idt(CENTER))
      {
        spfzp1move.setPos();
        spfzp2move.setPos();
      }

    }
    else if (finishedrd && gameover)
    {
      roundtextset();
    }

  }

  public void animateround(int roundcount)
  {
    Entity roundtext;
    roundtext = stageWrapper.getChild("ctrlandhud").getChild("roundtext").getEntity();
    switch (roundcount)
    {
      case 1:
        if (p1HPpercent > p2HPpercent)
        {
          p1round1();
          p1rdcount++;

        }

        else if (p2HPpercent > p1HPpercent)
        {
          p2round1();
          p2rdcount++;
        }

        else
        {
          p1round1();
          p2round1();
          p1rdcount++;
          p2rdcount++;
          roundcount++;
        }
        roundcount++;
        if (roundcount == 2)
        {
          roundtext.getComponent(LabelComponent.class).setText("ROUND 2");
          // roundtext.setText("ROUND 2");
        }
        else
        {
          roundtext.getComponent(LabelComponent.class).setText("FINAL ROUND");
        }
        break;
      case 2:
        if (p1HPpercent > p2HPpercent)
        {
          if (p1rdcount == 0)
          {
            p1round1();
          }
          else
          {
            p1round2();
            gameover = true;
          }
          p1rdcount++;
        }
        else if (p2HPpercent > p1HPpercent)
        {
          if (p2rdcount == 0)
          {
            p2round1();
          }
          else
          {
            p2round2();
            gameover = true;
          }
          p2rdcount++;
        }
        else
        {
          if (p1rdcount == 0)
          {
            p1round1();
          }
          else
          {
            p1round2();
            gameover = true;
          }

          if (p2rdcount == 0)
          {
            p2round1();
          }
          else
          {
            p2round2();
            gameover = true;
          }

          p1rdcount++;
          p2rdcount++;
        }

        roundtext.getComponent(LabelComponent.class).setText("FINAL ROUND");
        break;

      case 3:
        if (p1HPpercent > p2HPpercent)
        {
          p1round2();
          p1rdcount++;
        }
        else if (p2HPpercent > p1HPpercent)
        {
          p2round2();
          p2rdcount++;
        }
        else
        {
          p1round2();
          p2round2();
          p1rdcount++;
          p2rdcount++;
        }

        gameover = true;

        break;
      default:

        break;
    }

  }


  public void p1round1()
  {
    Entity p1round1;

    p1round1 = stageWrapper.getChild("ctrlandhud").getChild("roundonep1").getEntity();

    // if the player one percentage is greater than player 2's. Increment
    // Player 1's round count. else, opposite.

    Actions.addAction(p1round1,
      Actions.sequence(Actions.parallel(Actions.rotateBy(720, 1f),
        Actions.sequence(Actions.scaleBy(-.6f, -.6f, .5f), Actions.scaleBy(.6f, .6f, .5f)),
        Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
      {

        @Override
        public void run() {
          Entity fader;
          // fader =
          // stageItemWrapper.getChild("ctrlandhud").getChild("fader").getEntity();
          fader = stageWrapper.getChild("fader").getEntity();
          if (p2rdcount != 2)
          {
            Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
            {

              @Override
              public void run() {
                Entity fader;
                fader = stageWrapper.getChild("fader").getEntity();

                finishedrd = true;
                Actions.addAction(fader, Actions.fadeOut(1f));
              }
            })));
          }

        }
      })));

  }

  public void p2round1()
  {
    Entity p2round1;

    p2round1 = stageWrapper.getChild("ctrlandhud").getChild("roundonep2").getEntity();

    Actions.addAction(p2round1,
      Actions.sequence(Actions.parallel(Actions.rotateBy(-720, 1f),
        Actions.sequence(Actions.scaleBy(.6f, -.6f, .5f), Actions.scaleBy(-.6f, .6f, .5f)),
        Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
      {

        @Override
        public void run()
        {
          Entity fader;
          fader = stageWrapper.getChild("fader").getEntity();
          ;
          if (p1rdcount != 2)
          {
            Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
            {

              @Override
              public void run()
              {
                Entity fader;
                fader = stageWrapper.getChild("fader").getEntity();

                finishedrd = true;
                Actions.addAction(fader, Actions.fadeOut(1f));
              }
            })));
          }
        }
      })));

  }

  public void p1round2()
  {
    Entity p1round2;

    p1round2 = stageWrapper.getChild("ctrlandhud").getChild("roundtwop1").getEntity();

    Actions.addAction(p1round2,
      Actions.sequence(Actions.parallel(Actions.rotateBy(720, 1f),
        Actions.sequence(Actions.scaleBy(-.6f, -.6f, .5f), Actions.scaleBy(.6f, .6f, .5f)),
        Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
      {

        @Override
        public void run()
        {
          finishedrd = true;
          gameover = true;
        }
      })));
  }

  public void p2round2()
  {
    Entity p2round2;

    p2round2 = stageWrapper.getChild("ctrlandhud").getChild("roundtwop2").getEntity();

    Actions.addAction(p2round2,
      Actions.sequence(Actions.parallel(Actions.rotateBy(-720, 1f),
        Actions.sequence(Actions.scaleBy(.6f, -.6f, .5f), Actions.scaleBy(-.6f, .6f, .5f)),
        Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
      {

        @Override
        public void run()
        {
          finishedrd = true;
          gameover = true;
        }
      })));

  }

  public void lifeandround()
  {
    if (initcheck)
    {
      initcheck = false;
    }

    // showboxes
    if (show)
    {
      if ((System.currentTimeMillis() - showtime) * .001f >= 1f)
      {
        if (showboxes)
        {
          showboxes = false;
        }
        else
        {
          showboxes = true;
        }
        showtime = System.currentTimeMillis();
      }
    }

    // p1HPpercent and p2HPpercent reflect actual health that is kept based on
    // p1 and p2
    // health
    // reflPCT is needed to be the "in between" variable to maintain the proper
    // visual of the percentage through the lifebar.
    // begpercent contains the lifebar's width which is necessary to reflect the
    // damage based on how much is
    // stored within p1 and p2health.
    if (p1spec >= MAX_SUPER)
    {
      p1spec = MAX_SUPER;
    }
    if (p2spec >= MAX_SUPER)
    {
      p2spec = MAX_SUPER;
    }
    reflPCT = begpercent * ((float) p1health / (float) startp1);
    p1HPpercent = reflPCT;

    reflPCT = begpercent * ((float) p2health / (float) startp2);
    p2HPpercent = reflPCT;

    reflPCT = begsuperpct * ((float) p1spec / (float) MAX_SUPER);
    p1SPpercent = reflPCT;

    reflPCT = begsuperpct * ((float) p2spec / (float) MAX_SUPER);
    p2SPpercent = reflPCT;

    if (p1HPpercent <= 0 || p2HPpercent <= 0)
    {
      if (!standby)
      {
        eoround = true;
      }
    }

    if (!gameover)
    {
      if (!eoround && !training)
      {
        timer();
      }

      if (eoround && !training)
      {
        roundover();
      }
    }
  }


  public float p1percent()
  {
    return p1HPpercent;
  }

  public float p2percent()
  {
    return p2HPpercent;
  }

  @Override
  public void dispose()
  {

    arrscripts.clear();
    access.stageback.dispose();
    // access.trc = null;
    access.testregion = null;
    health1.dispose();
    health2.dispose();
    specmeter1.dispose();
    specmeter2.dispose();
    exdots1.dispose();
    superout1.dispose();
    superout2.dispose();
    testregion = null;
    healthout1 = null;
    healthout2 = null;
    access.land.engine.removeSystem(access.land.engine.getSystem(LifeSystem.class));
    access.land.engine.removeSystem(access.land.engine.getSystem(SpecialSystem.class));

    // spfzp1move.dispose();
    // spfzp2move.dispose();

    super.dispose();
  }
}
