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

  public boolean special, swap, hit, dash, left, right, jumpdir, confirm, walljump,
    punchstuck, kickstuck, runscript, cancel, pushed, createbox, projact, pausefrm, stwlk, ltstuck, ownatk,
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
  private final SPFZResourceManager resManager;
  private SPFZBuffer buffer;
  private SPFZProjectile spfzProjectile;
  private List<ArrayList<Double>> currentCharacterData = new ArrayList<>();
  private List<List<ArrayList<Double>>> all3CharactersFrameData = new ArrayList<List<ArrayList<Double>>>();
  private HashMap<String, float[]> characterAttributeData = new HashMap<>();
  private List<HashMap<String, int[]>> animations = new ArrayList<HashMap<String, int[]>>();
  private List<ArrayList<String>> specials = new ArrayList<ArrayList<String>>();
  private int combocount, currentframe, lastcount, move, input, lastfps, slot, slotIndex;

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
  private TransformComponent spfzAttribute;
  DimensionsComponent spfzDim;

  float intpol;

  private enum PlayerStatus {
    NEUTRAL, WALKING, DASHING, JUMPING, INAIR,
    ATTACKING, ATTACKED, BLOCKING
  }

  Rectangle spfzRect, spfzhitrect, spfzCharRect, crossrect, dimRect;

  ShapeRenderer spfzsr, spfzHitbox, spfzCharBox, spfzdimbox;
  short cancelled, speccount;

  int pauseframe;
  SpriteAnimationComponent spfzAnimation;
  SpriteAnimationStateComponent spfzAnimationState;
  String lastanim, weight;
  String[] loopanims = {"FWLK", "BWLK", "IDLE", "CRCH"};

  Vector2 hitboxsize, posofhitbox;

  PlayerStatus playerStatus;
  //SPFZProjScript projectile;



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
    if (spfzRect.x <= cameraPositionX - halfViewportWidth && spfzAttribute.scaleX > 0) {
      spfzAttribute.x = cameraPositionX - charX() - halfViewportWidth;
    }
    // If the player has reached the right bound facing right
    else if (spfzRect.x + spfzRect.width >= cameraPositionX + halfViewportWidth && spfzAttribute.scaleX > 0) {
      spfzAttribute.x = cameraPositionX + halfViewportWidth - (spfzRect.width);
    }

    // If the player has reached the right bound facing left
    if (spfzRect.x + spfzRect.width >= cameraPositionX + halfViewportWidth && spfzAttribute.scaleX < 0) {
      spfzAttribute.x = cameraPositionX + halfViewportWidth - (spfzRect.width + charX());
    }

    // If the player has reached the left bound facing left
    else if (spfzRect.x - spfzRect.width <= cameraPositionX - halfViewportWidth && spfzAttribute.scaleX < 0) {
      spfzAttribute.x = cameraPositionX - halfViewportWidth + spfzRect.width;
    }
  }

  public float center() {
    if (isFacingRight())
      return setrect().x + spfzRect.width * .5f;
    else
      return setrect().x - spfzRect.width * .5f;
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
    spfzHitbox = new ShapeRenderer();
    spfzCharBox = new ShapeRenderer();
    spfzdimbox = new ShapeRenderer();

    //spfzrect = new Rectangle();
    spfzRect = new Rectangle(characterAttributes.getCharDims());
    adjustX = spfzRect.x;
    adjustY = spfzRect.y;
    spfzhitrect = new Rectangle();
    spfzCharRect = new Rectangle();
    dimRect = new Rectangle();
    crossrect = new Rectangle();
    hitboxsize = new Vector2();
    posofhitbox = new Vector2();
    hitboxsize = new Vector2(0, 0);
    posofhitbox = new Vector2(0, 0);
    spfzentity = entity;
    setanimations();

    // get the values of the character from the Character attributes class
    gravity = characterAttributes.getGravity();
    jumpspeed = characterAttributes.getJump();
    walkspeed = characterAttributes.getWalkspeed();
    buffer.setCommandInputs(characterAttributes.getAllMoveInputs().get(slotIndex));
    dashspeed = characterAttributes.getdashadv();
    //tempspeed = walkandjump.x;

    setPos();
  }

  public void jumplogic(float delta) {
    // Apply gravity for spfzattribute calculations
    if (setrect().y > ground)
      if (!spfzAnimationState.paused)
        walkandjump.y += gravity * delta;

    // assign the new jump value to the spfzattribute attribute to
    // apply gravity to the spfzattribute
    if (!spfzAnimationState.paused)
      spfzAttribute.y += walkandjump.y;

    if (isJumping()) {
      if (spfzRect.y > ground) {
        if (spfzRect.y > ground + wallJumpBoundary
          && (stage.camera().position.x <= cameraBoundaries[0] + 1 || stage.camera().position.x + 1 >= cameraBoundaries[1])
          && ((spfzAttribute.x + adjustX <= stageBoundaries[0] && spfzAttribute.scaleX > 0)
          || spfzAttribute.x - adjustX + 1 >= stageBoundaries[1] && spfzAttribute.scaleX < 0)) {
          if (isLeft && isUp && !isRight && spfzAttribute.scaleX < 0
            || isRight && isUp && !isLeft && spfzAttribute.scaleX > 0) {
            // Needs modification, character keeps riding up on wall
            walljump = true;
            spfzAttribute.y += walkandjump.y;
          }
        }
        else if (spfzRect.y < ground + wallJumpBoundary) {
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
            if (!spfzAnimationState.paused) {

              spfzAttribute.x += walkandjump.x * .0150f;
            }
            // code for wall jump particle effect
          }
          else {
            if (!spfzAnimationState.paused) {

              spfzAttribute.x += walkandjump.x * .0150f;

            }
          }
        }
        else {
          if (walljump) {
            // wall jump particle effect will be here

            if (!spfzAnimationState.paused) {
              spfzAttribute.x -= walkandjump.x * .0150f;
            }
          }
          else {
            if (!spfzAnimationState.paused) {
              spfzAttribute.x -= walkandjump.x * .0150f;
            }
          }
        }
      }
      else {
        if (!isUp && (!isRight || !isLeft) && isJumping()) {
          setNeutralStatus();
        }
      }
    }

    // If spfzattribute has reached the boundary of the ground, set to the
    // boundary of the ground
    if (isOnGround()) {

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
      setNeutralStatus();
      confirm = false;
      hitboxsize.setZero();
      walkandjump.y = 0;
      spfzAttribute.y = charGROUND();
    }

  }

  public boolean isOnGround() { return spfzRect.y <= ground; }

  public float charX() { return spfzRect.x - spfzAttribute.x; }

  public float charY() { return spfzRect.y - spfzAttribute.y; }

  public float charGROUND() { return ground - (spfzRect.y - spfzAttribute.y); }

  public void kick() {
    spfzAnimation.currentAnimation = null;

    if (swap && meter >= 100f) {
      // if height restriction or on ground allow,
      if (spfzRect.y == ground && projact || spfzRect.y >= ground + 30f) {
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

      if (spfzAttribute.scaleX > 0) {
        if (!special || spfzRect.y > ground) {
          if (spfzRect.y == ground) {
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
            // if isJumping() is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping()) {
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
        if (!special || spfzRect.y > ground) {
          // Ground Kicks
          if (spfzRect.y == ground) {
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
            // if isJumping() is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping()) {
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

      setAttackingStatus();
      // if the move is not null technically
      // move = characterAttributes.moveset.indexOf(stage.normals[input]);

      if (move != -1) {
        spfzAnimation.currentAnimation = characterAttributes.moveset.get(slotIndex).get(move);
        spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation),
          characterAttributes.animFPS.get(slotIndex)
            .get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation)), Animation.PlayMode.NORMAL);
        //attackMove();
      }

    }

    setAttackingStatus();

    if (spfzAnimation.currentAnimation != null)
      spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation),
        characterAttributes.animFPS.get(slotIndex)
          .get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation)), Animation.PlayMode.NORMAL);
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
      //attacking = false; special move
      punchstuck = false;
      kickstuck = false;
      spfzAnimationState.paused = false;

    }
    if (isPunch && !isAttacked() && !isDashing()) {
      if (isAttacking()) {
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
    if (isKick && !isAttacked() && !dash) {
      if (isAttacking()) {
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


    if ((spfzAnimation.currentAnimation == "BDASH" || spfzAnimation.currentAnimation == "FDASH") ||
      dash && spfzRect.y == ground) {
      float totalFrames = currTotalFrames();
      float totalFrmTime = rtnFrametime(totalFrames);

      float progress = stateTime / totalFrmTime;

      if (startpt == 0) {
        startpt = spfzAttribute.x;
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

      spfzAttribute.x = dashpoints.x;

      if (spfzAnimationState.currentAnimation.isAnimationFinished(stateTime)) {
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

    if (spfzAttribute.scaleX > 0) {
      if (!special || spfzRect.y > ground) {
        if (spfzRect.y == ground) {
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
          // if isJumping() is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping()) {
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

          if (spfzRect.y > ground) {
            weight = "H";
          }
          else {
            createbox = false;
          }
        }
      }
      else {
        if (special && spfzRect.y == ground) {
          spfzAnimation.currentAnimation = "projectile";
          if (!projact && special) {
            spwnPrj();
            weight = "H";

            speccount = 0;
            createbox = false;
            if (cancelled == 1) {
              stateTime = 0;
              cancelled = 0;
              spfzAnimationState.paused = false;
            }
          }
        }
      }
    }
    else {
      if (!special || spfzRect.y > ground) {

        if (spfzRect.y == ground) {
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
          // if isJumping() is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping()) {
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
          if (spfzRect.y > ground) {
            weight = "H";
            createbox = true;
          }
        }

      }
      else {
        if (special && spfzRect.y == ground) {
          move = -1;
          spfzAnimation.currentAnimation = "projectile";
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

    setAttackingStatus();
    // if the move is not null technically
    //move = characterAttributes.moveset.indexOf(stage.normals[input]);
    if (move != -1) {
      spfzAnimation.currentAnimation = characterAttributes.moveset.get(slotIndex).get(move);
    }
    spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation),
      characterAttributes.animFPS.get(slotIndex).get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation)),
      Animation.PlayMode.NORMAL);
    //attackMove();
  }

  public void setneutral() {

    stateTime = 0;
    setNeutralStatus();
    //attacked = false;
    spfzAnimation.currentAnimation = "IDLE";
    if (isDown) {
      spfzAnimation.currentAnimation = "CRCH";
    }

    spfzAnimation.fps =
      characterAttributes.animFPS.get(slotIndex).get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation));
    spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation), spfzAnimation.fps,
      Animation.PlayMode.LOOP);
  }

  public void setanimations() {
    NodeComponent nc;

    nc = ComponentRetriever.get(spfzentity, NodeComponent.class);
    spfzaction = ComponentRetriever.get(spfzentity, ActionComponent.class);
    spfzAttribute = ComponentRetriever.get(spfzentity, TransformComponent.class);
    spfzAttribute.x = stage.centerOfStage() - 200f; // value may be initial start
    spfzAttribute.y = charGROUND();

    spfzDim = ComponentRetriever.get(spfzentity, DimensionsComponent.class);
    spfzAnimation = ComponentRetriever.get(nc.children.get(0), SpriteAnimationComponent.class);
    spfzAnimationState = ComponentRetriever.get(nc.children.get(0), SpriteAnimationStateComponent.class);
    List<String> keys;


    keys = new ArrayList<>(animations.get(slotIndex).keySet());

    // create frame ranges for all animations listed for each character
    //TODO change if check here
    if (all3CharactersFrameData.size() < 3) {
      for (int i = 0; i < characterAttributes.getAllAnimations().size(); i++)
        spfzAnimation.frameRangeMap.put(keys.get(i), new FrameRange(keys.get(i),
          characterAttributes.getAllAnimations().get(i).get(keys.get(i))[0],
          characterAttributes.getAllAnimations().get(i).get(keys.get(i))[1]));
    }
    else {
      for (int i = 0; i < animations.get(slotIndex).size(); i++) {

        spfzAnimation.frameRangeMap.put(keys.get(i),
          new FrameRange(keys.get(i), animations.get(slotIndex).get(keys.get(i))[0],
            animations.get(slotIndex).get(keys.get(i))[1]));
      }
    }

    setneutral();

  }

  public void setcombonum(int comboint) {
    combocount = comboint;
  }

  public ShapeRenderer renderHitBox() {
    if (spfzhitrect.x != posofhitbox.x || posofhitbox.y != posofhitbox.y ||
      hitboxsize.x != hitboxsize.x || hitboxsize.y != hitboxsize.y) {
      spfzhitrect.set(posofhitbox.x, posofhitbox.y, hitboxsize.x, hitboxsize.y);
      spfzHitbox.rect(posofhitbox.x, posofhitbox.y, hitboxsize.x, hitboxsize.y);
    }

    return spfzHitbox;
  }

  public Rectangle hitBox() { return spfzhitrect; }

  public void setPos() {

  }

  //TODO re-write stun logic

  public boolean projconfirm() {
    return false;
  }

  public Rectangle setrect() {
    if (spfzAttribute.scaleX > 0) {
      spfzRect.x = spfzAttribute.x + adjustX;
    }
    else {
      spfzRect.x = (spfzAttribute.x - adjustX) - spfzRect.width;
    }
    spfzRect.y = spfzAttribute.y + adjustY;

    return spfzRect;
  }

  public Rectangle setcross() {
    float box = spfzRect.width * .5f;
    crossrect.set(spfzRect.x + (box * .5f),
      spfzRect.y, spfzRect.width * .5f,
      spfzRect.height);

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
          isJumping() = true;

          if (walljump && spfzattribute.scaleX > 0)
          {
            walkandjump.y = jumpspeed / 4;
            walljump = false;
          }
        }
        else if (isLeft)
        {
          jumpdir = false;
          isJumping() = true;

          if (walljump && spfzattribute.scaleX < 0)
          {
            walkandjump.y = jumpspeed / 4;
            walljump = false;
          }
        }
      }
    }*/

  public float rtnFrametime(float frames) {
    return frames / characterAttributes.animFPS.get(slotIndex)
      .get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation));
  }

  @Override
  public void update() {
  }

  @Override
  public void returnmove() {
  }

  //TODO attack and attacked logic

  public float currTotalFrames() {
    return spfzAnimationState.currentAnimation.getKeyFrames().length;
  }

  public Rectangle hurtBox() {
    if (spfzCharRect != spfzRect)
      spfzCharRect = spfzRect;

    return spfzCharRect;
  }

  public Rectangle dimrectangle() {
    if (spfzAttribute.scaleX > 0)
      dimRect.set(spfzAttribute.x, spfzAttribute.y, spfzDim.width, spfzDim.height);
    else
      dimRect.set(spfzAttribute.x - spfzDim.width, spfzAttribute.y, spfzDim.width, spfzDim.height);

    return dimRect;
  }

  public ShapeRenderer drawcharbox() { return spfzCharBox; }

  public Rectangle setrflbox() { return null; }

  public ShapeRenderer drawrflbox() { return null; }

  public boolean[] movementInput() {
    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7 NEUTRAL = 8
    return new boolean[] {isLeft, isRight, isUp, isDown,
      (isLeft && isUp), (isRight && isUp), (isRight && isDown), (isLeft && isDown),
      (!isLeft && !isDown && !isRight && !isUp)};
  }

  public boolean isFacingLeft() { return spfzAttribute.scaleX < 0; }

  public boolean isFacingRight() { return spfzAttribute.scaleX > 0; }

  public SPFZFyterAnimation animation() { return spfzFyterAnimation; }

  public String currentAnimation() { return spfzAnimation.currentAnimation; }

  public TransformComponent transformAttributes() { return spfzAttribute; }

  public void setNewTransformAttributes(TransformComponent transformAttributes) {
    spfzAttribute = transformAttributes;
  }

  public void setOpponent(SPFZPlayer opponent) { this.opponent = opponent; }

  public SPFZPlayer opponent() { return opponent; }

  public List<ArrayList<Double>> characterData() { return currentCharacterData; }

  public List<HashMap<String, int[]>> animations() { return animations; }

  public List<ArrayList<String>> specials() { return specials; }

  public void setTotalHealth(float health) { this.health = health; }

  public float getHealth() { return health; }

  public float getMeter() { return meter; }

  public CharacterAttributes getCharacterAttributes() { return characterAttributes; }

  public int getSlotIndex() { return slotIndex; }

  public int currentMove() { return move; }

  // Player Status methods

  public PlayerStatus getPlayerStatus() { return playerStatus; }

  public boolean isAttacked() { return playerStatus == PlayerStatus.ATTACKED; }

  public boolean isAttacking() { return playerStatus == PlayerStatus.ATTACKING; }

  public boolean isBlocking() { return playerStatus == PlayerStatus.BLOCKING; }

  public boolean isDashing() { return playerStatus == PlayerStatus.DASHING; }

  public boolean isJumping() { return playerStatus == PlayerStatus.JUMPING; }

  public boolean isJumpingForwards() {
    return false;
  }

  public boolean isJumpingBackwards() {
    return false;
  }

  public boolean isInair() { return playerStatus == PlayerStatus.INAIR; }

  public boolean isNeutral() { return playerStatus == PlayerStatus.NEUTRAL; }

  public boolean isWalking() { return playerStatus == PlayerStatus.WALKING; }

  public void setAttackedStatus() { playerStatus = PlayerStatus.ATTACKED; }

  public void setAttackingStatus() { playerStatus = PlayerStatus.ATTACKING; }

  public void setBlockingStatus() { playerStatus = PlayerStatus.BLOCKING; }

  public void setDashingStatus() { playerStatus = PlayerStatus.DASHING; }

  public void setJumpingStatus() { playerStatus = PlayerStatus.JUMPING; }

  public void setInairStatus() { playerStatus = PlayerStatus.INAIR; }

  public void setNeutralStatus() { playerStatus = PlayerStatus.NEUTRAL; }

  public void setWalkingStatus() { playerStatus = PlayerStatus.WALKING; }

  public void setProjectionMatrix() {
    spfzCharBox.setProjectionMatrix(stage.camera().combined);
  }

  public int hitboxStartIndex() { return spfzFyterCollision.activeHitboxStartIndex(); }

  public int hitboxEndIndex() { return spfzFyterCollision.activeHitboxEndIndex(); }

  public int hitboxXPosition() { return spfzFyterCollision.activeHitboxXIndex(); }

  public int hitboxYPosition() { return spfzFyterCollision.activeHitboxYIndex(); }

  public int hitboxWidth() { return spfzFyterCollision.activeHitboxWidthIndex(); }

  public int hitboxHeight() { return spfzFyterCollision.activeHitboxHeightIndex(); }

  public SPFZResourceManager getResource() { return resManager; }

  //player directional methods

  public boolean isHoldingBack() {
    if (isFacingRight() && isLeft)
      return true;
    else if (isFacingLeft() && isRight)
      return true;
    else
      return false;
  }

  public boolean isHoldingForward() {
    if (isFacingLeft() && isLeft)
      return true;
    else if (isFacingRight() && isRight)
      return true;
    else
      return false;
  }

  public boolean isHoldingDownBack() { return isDown && isHoldingBack(); }

  public boolean isHoldingUpBack() { return isUp && isHoldingBack(); }

  public boolean isHoldingUpForward() { return isUp && isHoldingForward(); }

  public boolean isHoldingUp() { return isUp; }
}