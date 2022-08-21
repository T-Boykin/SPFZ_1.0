package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPFZPlayer implements IScript, BufferandInput {
  public boolean isRight, isUp, isDown, isLeft, isPunch, isKick;

  public boolean special, swap, hit, blk, dblk, dash, left, right, isJumping, jumpdir, attacking, attacked, confirm, walljump,
    blocking, punchstuck, kickstuck, runscript, cancel, pushed, createbox, projact, pausefrm, inair, stwlk, ltstuck, ownatk,
    projhit, bouncer, wallb, invul, playerOne;

  SPFZStage stage;
  Vector2 walkandjump, dashpoints;

  float ground, wallJumpBoundary, stun, tempfdur, gravity, jumpspeed, walkspeed, dashspeed, tempspeed, tempdist, tempdur,
    pauseTime, health, totalHealth, meter, stateTime = 0, intpush, spectime = 0, swaptime = 0, duration =
    .13f, distance = 1f,
    startpt,
    adjustX, adjustY;

  private final String[] characters = {"", "", ""};
  private SPFZPlayer opponent;
  private float[] cameraBoundaries = { }, stageBoundaries = { };
  private final CharacterAttributes characterAttributes;
  private final SPFZFyterAnimation spfzFyterAnimation;
  private final SPFZFyterCollision spfzFyterCollision;
  private SPFZBuffer buffer;
  private SPFZProjectile spfzProjectile;
  private SPFZResourceManager resManager;
  private List<List<ArrayList<Double>>> characterFrameData = new ArrayList<List<ArrayList<Double>>>();
  private HashMap<String, float[]> characterAttributeData = new HashMap<>();
  private List<HashMap<String, int[]>> animations = new ArrayList<HashMap<String, int[]>>();
  private List<ArrayList<String>> specials = new ArrayList<ArrayList<String>>();
  int combocount, currentframe, lastcount, move, input, lastfps, slot, slotIndex;

  int[] activeframes;

  final int BLKSTN = 0;
  final int HITSTN = 1;
  final int BLKDIST = 2;
  final int HITDIST = 3;
  final int BLKDMG = 10;
  final int HITDMG = 11;
  final int BLKMTR = 12;
  final int HITMTR = 13;
  final int FMOVE = 14;
  final int BMOVE = 15;
  final int JUGG = 16;
  final int BACK_START = 17;
  final int FWD_START = 18;
  final int BACK_ACTIVE = 19;
  final int FWD_ACTIVE = 20;
  final int BACK_RECOV = 21;
  final int FWD_RECOV = 22;

  ArrayList<int[]> inputs = new ArrayList<int[]>();
  Entity spfzentity;

  ActionComponent spfzaction;
  TransformComponent spfzattribute;
  DimensionsComponent spfzdim;

  float intpol;

  Rectangle spfzrect, spfzhitrect, spfzcharrect, crossrect, dimrect;

  ShapeRenderer spfzsr, spfzhitbox, spfzcharbox, spfzdimbox;
  short cancelled, speccount;

  int pauseframe;
  SpriteAnimationComponent spfzanimation;
  SpriteAnimationStateComponent spfzanimationstate;
  String lastanim, weight;
  String[] loopanims = {"FWLK", "BWLK", "IDLE", "CRCH"};

  Vector2 hitboxsize, posofhitbox;

  //SPFZProjScript projectile;

  private enum playerStatus {
    NEUTRAL, WALKING, DASHING, JUMPING, INAIR,
    ATTACKING, ATTACKED, BLOCKING
  }

  public SPFZPlayer(SPFZStage stage, SPFZResourceManager resManager, List<String> characters, boolean playerOne) {
    this.stage = stage;
    this.playerOne = playerOne;
    slot = playerOne ? 1 : 4;
    slotIndex = slot - 1;
    setCharacters(characters);
    ground = stage.ground();
    wallJumpBoundary = stage.wallJumpBoundary();
    cameraBoundaries = stage.cameraBoundaries();
    stageBoundaries = stage.stageBoundaries();
    this.resManager = resManager;
    buffer = new SPFZBuffer(this);
    characterAttributes = new CharacterAttributes(resManager, this.characters);
    spfzFyterAnimation = new SPFZFyterAnimation(this, stage, characterAttributes.retrieveFrameData().get(slot),
      animations, specials);
    spfzFyterCollision = new SPFZFyterCollision(this, stage.stageWrapper(),
      characterAttributes.retrieveFrameData().get(slot), animations, specials);
    spfzProjectile = new SPFZProjectile(this, "");
  }

  public void setCharacters(List<String> characters) {
    int charListStart = slot - 1;

    for (int i = 0; i < this.characters.length; i++)
      this.characters[i] = characters.get(charListStart + i);
  }

  public void setCharacter(List<String> chars) {
    CompositeItemVO playerCompositeItemVO;
    Entity playerEntity;
    ItemWrapper playerone;
    slotIndex = slot - 1;
    // load the characters from the library
    playerCompositeItemVO = stage.stageSSL().loadVoFromLibrary(chars.get(slotIndex));
    playerCompositeItemVO.zIndex = 4;
    playerEntity = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), playerCompositeItemVO);
    playerone = new ItemWrapper(playerEntity);
    stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), playerEntity, playerCompositeItemVO.composite);
    playerEntity.getComponent(MainItemComponent.class).itemIdentifier = chars.get(slotIndex);
    stage.stageSSL().getEngine().addEntity(playerEntity);
    playerone.addScript(this);
  }


  @Override
  public void act(float delta) {
    setInput();

    if (!stage.gameOver())
      processInputs(delta);

    spfzFyterAnimation.processAnimation();
    spfzFyterCollision.processCollisionBoxes(0);
    boundlogic();
    jumplogic(delta);
  }

  //TODO still need to perform a check of which device is being used
  public void setInput() {
    //TODO check the "stuck" logic. Seems to be timing based, simplify it.
    isUp = Gdx.input.isKeyPressed(Input.Keys.UP);
    isDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
    isRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    isLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
    isPunch = Gdx.input.isKeyPressed(Input.Keys.Q); // can put the int eqivalent
    isKick = Gdx.input.isKeyPressed(Input.Keys.W);
    /*if (Gdx.input.isKeyJustPressed(Keys.S) && spfzp1move.swap)
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
        p1charzoom = true;
      {
      }
      else
      {
        p1charzoom = false;
      }
    }*/

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

  public void inair() {

  }

  public void ground() {

  }


  //TODO make this logic better
  public void boundlogic() {
    float halfViewportWidth = stage.camera().viewportWidth / 2,
      cameraPositionX = stage.camera().position.x;

    // If the player has reached the left bound facing right
    if (spfzrect.x <= cameraPositionX - halfViewportWidth && spfzattribute.scaleX > 0) {
      spfzattribute.x = cameraPositionX - charX() - halfViewportWidth;
    }
    // If the player has reached the right bound facing right
    else if (spfzrect.x + spfzrect.width >= cameraPositionX + halfViewportWidth && spfzattribute.scaleX > 0) {
      spfzattribute.x = cameraPositionX + halfViewportWidth - (spfzrect.width);
    }

    // If the player has reached the right bound facing left
    if (spfzrect.x + spfzrect.width >= cameraPositionX + halfViewportWidth && spfzattribute.scaleX < 0) {
      spfzattribute.x = cameraPositionX + halfViewportWidth - (spfzrect.width + charX());
    }

    // If the player has reached the left bound facing left
    else if (spfzrect.x - spfzrect.width <= cameraPositionX - halfViewportWidth && spfzattribute.scaleX < 0) {
      spfzattribute.x = cameraPositionX - halfViewportWidth + spfzrect.width;
    }
  }

  public float center() {
    return setrect().x + spfzrect.width * .5f;
  }

  public int combonum() {
    return combocount;
  }

  @Override
  public void dispose() {
  }

  public boolean getboxconfirm() {
    return confirm;
  }

  public TransformComponent getspfzattribute() {
    return spfzattribute;
  }

  public float getWalkspeed() {
    return walkspeed;
  }

  public void hitboxconfirm(boolean confirm) {
    this.confirm = confirm;
  }

  @Override
  public void init(Entity entity) {
    buffer.setCommandInputs(characterAttributes.inputs.get(slotIndex));
    spfzsr = new ShapeRenderer();
    spfzhitbox = new ShapeRenderer();
    spfzcharbox = new ShapeRenderer();
    spfzdimbox = new ShapeRenderer();

    //spfzrect = new Rectangle();
    spfzrect = new Rectangle(characterAttributes.getCharDims());
    adjustX = spfzrect.x;
    adjustY = spfzrect.y;
    spfzhitrect = new Rectangle();
    spfzcharrect = new Rectangle();
    dimrect = new Rectangle();
    crossrect = new Rectangle();
    hitboxsize = new Vector2();
    posofhitbox = new Vector2();
    activeframes = new int[] {0, 0};
    hitboxsize = new Vector2(0, 0);
    posofhitbox = new Vector2(0, 0);
    spfzentity = entity;
    setanimations();

    // get the values of the character from the Character attributes class
    gravity = characterAttributes.getGravity();
    jumpspeed = characterAttributes.getJump();
    walkspeed = characterAttributes.getWalkspeed();
    buffer.setCommandInputs(characterAttributes.getmoveinputs());
    dashspeed = characterAttributes.getdashadv();
    //tempspeed = walkandjump.x;

    setPos();
  }

  public void jumplogic(float delta) {
    // Apply gravity for spfzattribute calculations
    if (setrect().y > ground) {
      if (!spfzanimationstate.paused) {
        walkandjump.y += gravity * delta;
      }
    }
    // assign the new jump value to the spfzattribute attribute to
    // apply gravity to the spfzattribute
    if (!spfzanimationstate.paused) {
      spfzattribute.y += walkandjump.y;
    }

    if (isJumping) {
      if (spfzrect.y > ground) {
        if (spfzrect.y > ground + wallJumpBoundary
          && (stage.camera().position.x <= cameraBoundaries[0] + 1 || stage.camera().position.x + 1 >= cameraBoundaries[1])
          && ((spfzattribute.x + adjustX <= stageBoundaries[0] && spfzattribute.scaleX > 0)
          || spfzattribute.x - adjustX + 1 >= stageBoundaries[1] && spfzattribute.scaleX < 0)) {
          if (isLeft && isUp && !isRight && spfzattribute.scaleX < 0
            || isRight && isUp && !isLeft && spfzattribute.scaleX > 0) {
            // Needs modification, character keeps riding up on wall
            walljump = true;
            spfzattribute.y += walkandjump.y;
          }
        }
        else if (spfzrect.y < ground + wallJumpBoundary) {
          walljump = false;
        }
        // if Jump direction is true(right) it will advance the player
        // to the
        // right
        // else Jump direction is false(left) it will advance the player
        // to the
        // left
        if (jumpdir) {
          if (walljump) {
            if (!spfzanimationstate.paused) {

              spfzattribute.x += walkandjump.x * .0150f;
            }
            // code for wall jump particle effect
          }
          else {
            if (!spfzanimationstate.paused) {

              spfzattribute.x += walkandjump.x * .0150f;

            }
          }
        }
        else {
          if (walljump) {
            // wall jump particle effect will be here

            if (!spfzanimationstate.paused) {
              spfzattribute.x -= walkandjump.x * .0150f;
            }
          }
          else {
            if (!spfzanimationstate.paused) {
              spfzattribute.x -= walkandjump.x * .0150f;
            }
          }
        }
      }
      else {
        if (!isUp && (!isRight || !isLeft) && isJumping) {
          isJumping = false;
        }
      }
    }

    // If spfzattribute has reached the boundary of the ground, set to the
    // boundary of the ground
    if (spfzrect.y < ground) {

      if (stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
        stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects
          .removeValue(stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity()
            .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
      }
      stage.stageWrapper().getChild("p1land").getEntity().getComponent(TransformComponent.class).x = center();
      stage.stageWrapper().getChild("p1land").getEntity().getComponent(TransformComponent.class).y = ground;
      stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity()
        .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
      stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity()
        .getComponent(TransformComponent.class).scaleX = 1f;
      stage.stageWrapper().getChild("p1land").getChild("landp1").getEntity().getComponent(SPFZParticleComponent.class)
        .startEffect();
      attacking = false;
      confirm = false;
      hitboxsize.setZero();
      walkandjump.y = 0;
      spfzattribute.y = charGROUND();
    }

  }

  public float charX() {
    return spfzrect.x - spfzattribute.x;
  }

  public float charY() {
    return spfzrect.y - spfzattribute.y;
  }

  public float charGROUND() {
    return ground - (spfzrect.y - spfzattribute.y);
  }

  public void kick() {
    spfzanimation.currentAnimation = null;

    if (swap && meter >= 100f) {
      // if height restriction or on ground allow,
      if (spfzrect.y == ground && projact || spfzrect.y >= ground + 30f) {
        swap = false;
        isPunch = false;
        spectime = 0;
        swaptime = 0;
        System.out.println("****************************** SWAP USED ******************************");
      }
      else {
        swap = false;
        System.out
          .println("****************************** SWAP LOST - HEIGHT RESTRICTION  ******************************");
      }
    }
    else {
      move = -1;

      if (spfzattribute.scaleX > 0) {
        if (!special || spfzrect.y > ground) {
          if (spfzrect.y == ground) {
            if (isLeft) {
              input = 9;
              if (isDown) {
                input = 3;
              }
              weight = "L";

            }
            else if (isRight) {
              input = 11;
              if (isDown) {
                input = 5;
              }
              weight = "H";


            }
            else if (!isLeft && !isRight) {
              input = 10;
              if (isDown) {
                input = 4;
              }
              weight = "M";

            }
          }
          else {
            // if isJumping is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping) {
              // if jumpdir is true, means we are jumping forwrds,
              // otherwise, we
              // are jumping backwards
              if (jumpdir) {
                input = 17;
              }
              else {
                input = 15;
              }
            }
            else {
              input = 16;
            }
            weight = "H";
          }
        }
      }
      else {
        if (!special || spfzrect.y > ground) {
          // Ground Kicks
          if (spfzrect.y == ground) {
            if (isLeft) {
              input = 11;
              if (isDown) {
                input = 5;
              }
              weight = "H";

            }
            else if (isRight) {
              input = 9;
              if (isDown) {
                input = 3;
              }
              weight = "L";
            }
            else if (!isLeft && !isRight) {
              input = 10;
              if (isDown) {
                input = 4;
              }
              weight = "M";
            }
          }
          // Air Kicks
          else {
            // if isJumping is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping) {
              // if jumpdir is true, means we are jumping
              // backwards, otherwise,
              // we are jumping forwards
              if (jumpdir) {
                input = 15;
              }
              else {
                input = 17;
              }
            }
            else {
              input = 16;
            }
            weight = "H";
          }
        }

      }

      attacking = true;
      // if the move is not null technically
      // move = characterAttributes.moveset.indexOf(stage.normals[input]);

      if (move != -1) {
        spfzanimation.currentAnimation = characterAttributes.moveset.get(slotIndex).get(move);
        spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation),
          characterAttributes.animFPS.get(slotIndex)
            .get(characterAttributes.anims.indexOf(spfzanimation.currentAnimation)), Animation.PlayMode.NORMAL);
        //attackMove();
      }

    }

    attacking = true;
    if (spfzanimation.currentAnimation != null) {
      spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation),
        characterAttributes.animFPS.get(slotIndex)
          .get(characterAttributes.anims.indexOf(spfzanimation.currentAnimation)), Animation.PlayMode.NORMAL);
    }
  }

  public boolean invul() {
    return invul;
  }

  public Vector2 moveandjump() {

    return walkandjump;
  }

  public void processInputs(float delta) {
    buffer.bufferUpdate();

    if (cancelled == 1) {
      isPunch = true;
      special = true;
      attacking = false;
      punchstuck = false;
      kickstuck = false;
      spfzanimationstate.paused = false;

    }
    if (isPunch && !attacked && !dash) {
      if (attacking) {
        punchstuck = true;
      }
      if (!punchstuck) {
        punch();
      }
    }
    else {
      if (!isPunch) {

        punchstuck = false;
      }
    }
    if (isKick && !attacked && !dash) {
      if (attacking) {
        if (!swap) {
          kickstuck = true;
        }
      }
      if (!kickstuck) {
        kick();
      }
    }
    else {
      if (!isKick) {
        kickstuck = false;
      }
    }


    if (intpol != 1 && !dash) {
      intpol = 1;
    }


    if ((spfzanimation.currentAnimation == "BDASH" || spfzanimation.currentAnimation == "FDASH") ||
      dash && spfzrect.y == ground) {
      float totalFrames = currTotalFrames();
      float totalFrmTime = rtnFrametime(totalFrames);

      float progress = stateTime / totalFrmTime;

      if (startpt == 0) {
        startpt = spfzattribute.x;
        dashpoints = new Vector2(startpt, 0);
      }

     /* if (dashdir == 0) {
        if (!stage.close) {
          dashpoints.interpolate(new Vector2(startpt - dashspeed, 0), progress, Interpolation.exp10In);
        }
      }
      else {
        if (!stage.close) {
          dashpoints.interpolate(new Vector2(startpt + dashspeed, 0), progress, Interpolation.exp10In);
        }
      }*/

      spfzattribute.x = dashpoints.x;

      if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime)) {
        walkandjump.x = tempspeed;
        startpt = 0;
      }

    }
    if (special) {
      spectime += Gdx.graphics.getDeltaTime();
      //if (spectime >= rtnFrametime(stage.SPECIAL_WINDOW))
      // {
      System.out.println("****************************** SPECIAL NOT AVAILABLE ******************************");
      special = false;
      spectime = 0;
      speccount = 0;
      //  }
    }
    if (swap) {
      swaptime += Gdx.graphics.getDeltaTime();
      // if (swaptime >= rtnFrametime(stage.SWAP_WINDOW))
      //{
      System.out.println("****************************** SWAP NOT AVAILABLE ******************************");
      swap = false;
      swaptime = 0;
      speccount = 0;
      // }
    }
  }

  public void punch() {
    // "null" move
    bouncer = false;
    move = -1;

    if (spfzattribute.scaleX > 0) {
      if (!special || spfzrect.y > ground) {
        if (spfzrect.y == ground) {
          if (isLeft) {

            input = 6;
            if (isDown) {
              input = 0;
            }
            weight = "L";
            ltstuck = true;
          }
          else if (isRight) {
            input = 8;
            if (isDown) {
              input = 2;
              bouncer = true;
            }
            weight = "H";
          }
          else if (!isLeft && !isRight) {
            input = 7;
            if (isDown) {
              input = 1;
            }
            weight = "M";
          }
        }
        else {
          // if isJumping is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping) {
            // if jumpdir is true, means we are jumping forwards,
            // otherwise, we
            // are jumping backwards
            if (jumpdir) {
              input = 14;
            }
            else {
              input = 12;
            }
          }
          else {
            input = 13;
          }

          if (spfzrect.y > ground) {
            weight = "H";
          }
          else {
            createbox = false;
          }
        }
      }
      else {
        if (special && spfzrect.y == ground) {
          spfzanimation.currentAnimation = "projectile";
          if (!projact && special) {
            spwnPrj();
            weight = "H";

            speccount = 0;
            createbox = false;
            if (cancelled == 1) {
              stateTime = 0;
              cancelled = 0;
              spfzanimationstate.paused = false;
            }
          }
        }
      }
    }
    else {
      if (!special || spfzrect.y > ground) {

        if (spfzrect.y == ground) {
          if (isLeft) {
            input = 8;
            weight = "H";
            if (isDown) {
              input = 2;
              bouncer = true;
            }
          }
          else if (isRight) {
            input = 6;
            if (isDown) {
              input = 0;
            }
            weight = "L";
            ltstuck = true;
          }
          else if (!isRight && !isLeft) {
            weight = "M";
            input = 7;
            if (isDown) {
              input = 1;
            }
          }
        }
        else {
          // if isJumping is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping) {
            // if jumpdir is true, means we are jumping backwards,
            // otherwise, we
            // are jumping forwards
            if (jumpdir) {
              input = 12;
            }
            else {
              input = 14;
            }
          }
          else {
            input = 13;
          }
          if (spfzrect.y > ground) {
            weight = "H";
            createbox = true;
          }
        }

      }
      else {
        if (special && spfzrect.y == ground) {
          move = -1;
          spfzanimation.currentAnimation = "projectile";
          if (!projact && special) {
            spwnPrj();
            weight = "H";
            createbox = false;
            if (cancelled == 1) {
              stateTime = 0;
              cancelled = 0;
            }
          }
        }
      }
    }

    attacking = true;
    // if the move is not null technically
    //move = characterAttributes.moveset.indexOf(stage.normals[input]);
    if (move != -1) {
      spfzanimation.currentAnimation = characterAttributes.moveset.get(slotIndex).get(move);
    }
    spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation),
      characterAttributes.animFPS.get(slotIndex).get(characterAttributes.anims.indexOf(spfzanimation.currentAnimation)),
      Animation.PlayMode.NORMAL);
    //attackMove();
  }

  public void setneutral() {

    stateTime = 0;
    attacking = false;
    attacked = false;
    spfzanimation.currentAnimation = "IDLE";
    if (isDown) {
      spfzanimation.currentAnimation = "CRCH";
    }

    spfzanimation.fps =
      characterAttributes.animFPS.get(slotIndex).get(characterAttributes.anims.indexOf(spfzanimation.currentAnimation));
    spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
      Animation.PlayMode.LOOP);
  }

  public void setanimations() {
    NodeComponent nc;

    nc = ComponentRetriever.get(spfzentity, NodeComponent.class);
    spfzaction = ComponentRetriever.get(spfzentity, ActionComponent.class);
    spfzattribute = ComponentRetriever.get(spfzentity, TransformComponent.class);
    spfzattribute.x = stage.centerOfStage() - 200f; // value may be initial start
    spfzattribute.y = charGROUND();

    spfzdim = ComponentRetriever.get(spfzentity, DimensionsComponent.class);
    spfzanimation = ComponentRetriever.get(nc.children.get(0), SpriteAnimationComponent.class);
    spfzanimationstate = ComponentRetriever.get(nc.children.get(0), SpriteAnimationStateComponent.class);
    List<String> keys;


    keys = new ArrayList<>(animations.get(slotIndex).keySet());

    // create frame ranges for all animations listed for each character

    if (characterFrameData.size() < 3) {
      for (int i = 0; i < characterAttributes.getAnimations().size(); i++)
        spfzanimation.frameRangeMap.put(keys.get(i), new FrameRange(keys.get(i),
          characterAttributes.animations.get(slotIndex).get(keys.get(i))[0],
          characterAttributes.getAnimations().get(keys.get(i))[1]));
    }
    else {
      for (int i = 0; i < animations.get(slotIndex).size(); i++) {

        spfzanimation.frameRangeMap.put(keys.get(i),
          new FrameRange(keys.get(i), animations.get(slotIndex).get(keys.get(i))[0],
            animations.get(slotIndex).get(keys.get(i))[1]));
      }
    }

    setneutral();

  }

  public void setcombonum(int comboint) {
    combocount = comboint;
  }

  public Rectangle sethitbox() {
    spfzhitrect.set(posofhitbox.x, posofhitbox.y, hitboxsize.x, hitboxsize.y);
    return spfzhitrect;
  }

  public void setPos() {

  }

  //TODO re-write stun logic

  public boolean projconfirm() {
    return false;
  }

  public Rectangle setrect() {
    if (spfzattribute.scaleX > 0) {
      spfzrect.x = spfzattribute.x + adjustX;
    }
    else {
      spfzrect.x = (spfzattribute.x - adjustX) - spfzrect.width;
    }
    spfzrect.y = spfzattribute.y + adjustY;

    return spfzrect;
  }

  public Rectangle setcross() {
    float box = spfzrect.width * .5f;
    crossrect.set(spfzrect.x + (box * .5f),
      spfzrect.y, spfzrect.width * .5f,
      spfzrect.height);

    return crossrect;
  }

  public void spwnPrj() {
    SPFZProjScript projecter = new SPFZProjScript(stage);
    // SPFZProjScript2 projecter = new SPFZProjScript2(stage);
    Entity project;
    CompositeItemVO projVO = stage.stageSSL().loadVoFromLibrary("projectile");
    project = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), projVO);
    ItemWrapper projwrapper = new ItemWrapper(project);
    project.getComponent(ZIndexComponent.class).setZIndex(4);
    project.getComponent(ZIndexComponent.class).needReOrder = false;
    project.getComponent(ZIndexComponent.class).layerName = "Default";
    project.getComponent(MainItemComponent.class).itemIdentifier = "projectile";
    stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), project, projVO.composite);
    stage.stageSSL().getEngine().addEntity(project);

    //projwrapper.addScript((IScript) projector);
    projact = true;
    special = false;

  }


/* Jump logic that may be needed
    if (!attacking && !attacked && !dash && !stage.standby)
    {

      if (ground == spfzrect.y || walljump)
      {
        if (!walljump)
        {
          walkandjump.y = jumpspeed;
        }

        if (isRight)
        {
          jumpdir = true;
          isJumping = true;

          if (walljump && spfzattribute.scaleX > 0)
          {
            walkandjump.y = jumpspeed / 4;
            walljump = false;
          }
        }
        else if (isLeft)
        {
          jumpdir = false;
          isJumping = true;

          if (walljump && spfzattribute.scaleX < 0)
          {
            walkandjump.y = jumpspeed / 4;
            walljump = false;
          }
        }
      }
    }*/

  public float rtnFrametime(float frames) {
    //return frames / 60f;
    return frames / characterAttributes.animFPS.get(slotIndex)
      .get(characterAttributes.anims.indexOf(spfzanimation.currentAnimation));
  }

  @Override
  public void update() {
  }

  @Override
  public void returnmove() {
  }

  //TODO attack and attacked logic

  public float currTotalFrames() {
    return spfzanimationstate.currentAnimation.getKeyFrames().length;
  }

  public Rectangle setcharbox() {
    spfzcharrect = spfzrect;
    return spfzcharrect;
  }

  public Rectangle dimrectangle() {
    if (spfzattribute.scaleX > 0) {
      dimrect.set(spfzattribute.x, spfzattribute.y, spfzdim.width, spfzdim.height);
    }
    else {
      dimrect.set(spfzattribute.x - spfzdim.width, spfzattribute.y, spfzdim.width, spfzdim.height);
    }
    return dimrect;
  }

  public ShapeRenderer drawcharbox() { return spfzcharbox; }

  public Rectangle setrflbox() { return null; }

  public ShapeRenderer drawrflbox() { return null; }

  public boolean[] movementInput() {
    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7 NEUTRAL = 8
    return new boolean[] {isLeft, isRight, isUp, isDown,
      (isLeft && isUp), (isRight && isUp), (isRight && isDown), (isLeft && isDown),
      (!isLeft && !isDown && !isRight && !isUp)};
  }

  public void setOpponent(SPFZPlayer opponent) { this.opponent = opponent; }

  public SPFZPlayer opponent() { return opponent; }

  public List<List<ArrayList<Double>>> characterData() { return characterFrameData; }

  public List<HashMap<String, int[]>> animations() { return animations; }

  public List<ArrayList<String>> specials() { return specials; }

  public void setTotalHealth(float health) { this.health = health; }

  public float getHealth() { return health; }

  public float getMeter() { return meter; }

  public CharacterAttributes getCharacterAttributes() { return characterAttributes; }

  public int getSlotIndex() { return slotIndex; }
}