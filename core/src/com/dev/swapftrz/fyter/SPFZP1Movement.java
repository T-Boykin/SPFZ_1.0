package com.dev.swapftrz.fyter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class SPFZP1Movement implements IScript, Attribs, BufferandInput
{

  boolean special, swap, hit, blk, dblk, dash, left, right, isJumping, jumpdir, attacking, attacked, confirm, walljump,
    blocking, punchstuck, kickstuck, runscript, cancel, isRight, isLeft, isUp, isDown, isPunch, isKick, pushed,
    createbox, projact, pausefrm, inair, stwlk, ltstuck, ownatk, projhit, bouncer, wallb, invul;

  CharAttributes spfzp1vals;

  SPFZStage stage;
  Vector2 walkandjump, dashpoints;

  float stun, tempfdur, gravity, jumpspeed, walkspeed, dashspeed, tempspeed, tempdist, tempdur, pauseTime,
    stateTime = 0, intpush, spectime = 0, swaptime = 0, duration = .13f, distance = 1f, startpt, adjustX, adjustY;

  int combocount, currentframe, buff, last, dashdir, lastcount, buffsize = 60, move, input, lastfps;

  int[] activeframes;

  final int BLKSTN = 0;
  final int HITSTN = 1;
  final int BLKDIST = 2;
  final int HITDIST = 3;
  final int ACTSTARTBOX = 4;
  final int ACTENDBOX = 5;
  final int BOXX = 6;
  final int BOXY = 7;
  final int BOXWIDTH = 8;
  final int BOXHEIGHT = 9;
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
  final int PROJ_STARTUP_START = 23;
  final int PROJ_STARTUP_END = 24;
  final int PROJ_LOOP_START = 25;
  final int PROJ_LOOP_END = 26;
  final int PROJ_END_START = 27;
  final int PROJ_END_FINAL = 28;
  final int PROJ_TYPE = 29;
  final int PROJ_SPEED = 30;
  final int PROJ_POSX = 31;
  final int PROJ_POSY = 32;

  int NEUTRAL = 0;
  ArrayList<int[]> inputs = new ArrayList<int[]>();

  List<Boolean> p1movement = new ArrayList<Boolean>(), lastp1movement = new ArrayList<Boolean>();
  List<Integer> buffer = new ArrayList<Integer>(), last16 = new ArrayList<Integer>();

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

  SPFZProjScript projectile;

  public SPFZP1Movement(SPFZStage screen)
  {
    stage = screen;
  }

  @Override
  public void act(float delta)
  {
    if (!stage.gameover)
    {

      movement(delta);

      stage.setface();
      if (createbox)
      {
        createHitBox(stage.processed.get(stage.p1), spfzanimation.currentAnimation);
        createbox = false;

      }
    }

    animation();

    boundlogic();
    jumplogic(delta);
  }

  @Override
  public int[] activeframes()
  {

    return activeframes;
  }

  @Override
  public SpriteAnimationComponent animationcomponent()
  {

    return spfzanimation;
  }

  @Override
  public SpriteAnimationStateComponent animationstate()
  {
    return spfzanimationstate;
  }

  public void animation()
  {

    if (spfzrect.y <= stage.GROUND)
    {
      ground();

      if (lastanim != spfzanimation.currentAnimation)
      {
        boolean loopfound = false;
        for (String anim : loopanims)
        {
          if (spfzanimation.currentAnimation == anim)
          {
            loopfound = true;
          }
        }

        if (loopfound)
        {

          if (spfzanimation.currentAnimation != null)
          {
            spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
              Animation.PlayMode.LOOP);
          }
        }
        else
        {
          if (spfzanimation.currentAnimation != null)
          {
            spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
              Animation.PlayMode.NORMAL);
          }
        }
        if (!loopfound)
        {
          stateTime = 0;
        }
        lastanim = spfzanimation.currentAnimation;
      }
    }
    else
    {
      inair();

      if (lastanim != spfzanimation.currentAnimation)
      {
        stateTime = 0;
        boolean loopfound = false;
        for (String anim : loopanims)
        {
          if (spfzanimation.currentAnimation == anim)
          {
            loopfound = true;
          }
        }

        if (loopfound)
        {

          if (spfzanimation.currentAnimation != null)
          {
            spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
              Animation.PlayMode.LOOP);
          }
        }
        else
        {
          if (spfzanimation.currentAnimation != null)
          {
            spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
              Animation.PlayMode.NORMAL);
          }
        }

        lastanim = spfzanimation.currentAnimation;

      }

      if (!attacking && !hitboxsize.isZero())
      {
        hitboxsize.setZero();
      }
    }

    checkputp1();

    if (runscript)
    {
      if (spfzanimation.currentAnimation == "IDLE")
      {
        setneutral();
      }
    }

    // grab the current frame of animation
    currentframe = spfzanimationstate.currentAnimation.getKeyFrameIndex(stateTime);

    if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime) && spfzanimation.currentAnimation != "SATKD"
      && spfzanimation.currentAnimation != "SBLK" && spfzanimation.currentAnimation != "CBLK"
      && spfzanimation.currentAnimation != "ABLK")
    {
      if (spfzanimation.currentAnimation != "FTRANS")
      {
        if (attacking && !spfzanimationstate.paused)
        {
          attacking = false;
        }
        if (spfzrect.y <= stage.GROUND)
        {
          setneutral();
        }
      }
      else
      {
        stwlk = true;
      }
    }
    if (spfzanimationstate.currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL && !spfzanimationstate.paused)
    {
      stateTime += Gdx.graphics.getDeltaTime();
    }

  }

  public void inair()
  {
    if (attacked && !attacking)
    {
      airatkedanim();
    }
    else if (attacking && !attacked)
    {
      airatkanim();
    }
    else if (!attacking && !attacked)
    {
      // conditioning for movement in air animation
      airmvmtanim();
    }
    if(lastanim != spfzanimation.currentAnimation)
    {
      spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      lastfps = spfzanimation.fps;
    }
  }

  public void ground()
  {
    if (attacked || blocking)
    {
      grdatkedanim();
    }
    else if (attacking)
    {
      grdatkanim();
    }
    else if (!attacking && !attacked)
    {
      grdmvmtanim();
    }
    if(lastanim != spfzanimation.currentAnimation)
    {
      spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      lastfps = spfzanimation.fps;
    }
  }

  public void airatkedanim()
  {

  }

  public void airatkanim()
  {

    //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
    // Normal has connected.
    if (spfzanimationstate.paused && cancelled == 0 || pausefrm)
    {
      // using this process to get the exact frame in order to setup the
      // recovery animation
      if (!pausefrm)
      {
        pauseframe = currentframe;
        pausefrm = true;
        pauseTime = 0;
      }
      cancel = true;

      if (!attacking)
      {
        confirm = false;
      }
      if (special && isPunch && cancelled == 0 && cancel)
      {
        cancelled = 1;
      }
      // .10 will need to be a value coming in from the file
      // if (pauseTime >= .08f)
      if (pauseTime >= rtnFrametime(10f))
      {
        if (stage.shake)
        {
          stage.shake = false;
        }
        confirm = false;
        pauseTime = 0f;
        spfzanimationstate.paused = false;
        pausefrm = false;
        if (cancelled != 1)
        {
          if (spfzanimation.currentAnimation != null && spfzanimation.currentAnimation != "recovery")
          {
            spfzanimationstate.set(
              new FrameRange("recovery", pauseframe,
                spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).endFrame),
              60, Animation.PlayMode.NORMAL);
            stateTime = 0;
            spfzanimation.currentAnimation = "recovery";
            lastanim = spfzanimation.currentAnimation;
            cancel = false;
          }
          // set recovery frame animation here

        }
      }

      // pauseTime += .015f;
      pauseTime += rtnFrametime(5);
    }
  }

  public void airmvmtanim()
  {
    // check jumping and jump direction variables
    if (isJumping)
    {
      //spfzanimation.fps = 15;
      // if jumpdir true, means jumping in right diagonal
      if (jumpdir)
      {
        if (spfzattribute.scaleX > 0)
        {
          spfzanimation.currentAnimation = "FJMP";
        }
        else
        {
          spfzanimation.currentAnimation = "BJMP";
          if (walljump)
          {
            spfzanimation.currentAnimation = "WJMP";
          }
        }
      }
      else
      {
        if (spfzattribute.scaleX > 0)
        {
          spfzanimation.currentAnimation = "BJMP";
          if (walljump)
          {
            spfzanimation.currentAnimation = "WJMP";
          }
        }
        else
        {
          spfzanimation.currentAnimation = "FJMP";
        }
      }
    }
    else
    {
      spfzanimation.currentAnimation = "NJMP";
    }
  }

  public void grdatkedanim()
  {
    //////////// ATTACKED
    if (spfzanimation.currentAnimation == "IDLE" || spfzanimation.currentAnimation == "movement" || attacked)
    {
      attacking = false;
    }

    if (ownatk)
    {
      confirm = true;
      ownatk = false;
    }
    // Check if player one is attacked
    if (((Attribs) stage.arrscripts.get(stage.p2)).getboxconfirm() && !ownatk)
    {
      // This combo counter is the combo counter for player 2
      hit = true;
      stun = 0;
      lastcount = combocount;
      intpush = 1;
      attacking = false;
      // if (!stage.spfzp2move.projact)
      // {
      // stage.spfzp2move.spfzanimationstate.paused = true;
      // }
      if (blocking)
      {
        if (spfzrect.y == stage.GROUND)
        {
          if (isDown)
          {
            spfzanimation.currentAnimation = "CBLK";
          }
          else
          {
            spfzanimation.currentAnimation = "SBLK";
          }
        }
        else
        {
          spfzanimation.currentAnimation = "ABLK";
        }
      }
      else
      {
        spfzanimation.currentAnimation = "SATKD";
      }
      setstun();
      stateTime = 0;
    }

    if (stateTime >= stun)
    {
      attacked = false;
      spfzanimation.currentAnimation = "IDLE";
      stateTime = 0;
      pushed = false;
      if (blocking)
      {
        blocking = false;
      }

      if (combocount != 0)
      {
        LabelComponent combocount1;
        combocount1 = stage.access.root.getChild("ctrlandhud").getChild("combocount1").getEntity()
          .getComponent(LabelComponent.class);

        Entity Hit = stage.access.root.getChild("ctrlandhud").getChild("p2himg").getEntity();
        Entity cc = stage.access.root.getChild("ctrlandhud").getChild("p2cc").getEntity();

        Actions.addAction(Hit, Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic));
        Actions.addAction(cc, Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic));

        combocount1.setText(" ");
        combocount = 0;
      }

      if (stateTime != 0)
      {
        stateTime = 0;
      }
      if (stage.training)
      {
        stage.p1health = stage.startp1;
      }
    }
    else
    {
      if (pushed && !hit)
      {

      }
      else
      {
        hit = false;
      }

      if ((spfzanimation.currentAnimation == "SATKD" || spfzanimation.currentAnimation == "SBLK"
        || spfzanimation.currentAnimation == "ABLK" || spfzanimation.currentAnimation == "CBLK")
        && stateTime < stun)

      {
        // modifier from file
        float pushback = 2f;
        float progress = Math.min(pushback, intpush * pushback);

        pushback = progress;

        if (intpush > 0f)
        {// .03 will be modifier from file
          intpush -= .03f;
        }

        // pushback after hit
        if (spfzattribute.scaleX > 0)
        {
          if (stage.cam.position.x <= stage.camboundary[0] + 1 && spfzrect.x <= stage.stageboundary[0])
          {
            spfzattribute.x -= pushback;
          }
          else
          {
            spfzattribute.x -= pushback;
          }
        }
        // pushback if facing the opposite direction
        else
        {
          if (stage.cam.position.x + 1 >= stage.camboundary[1] && spfzrect.x >= stage.stageboundary[1])
          {
            spfzattribute.x += pushback;
          }
          else
          {
            spfzattribute.x += pushback;
          }
        }

        pushed = true;

      }
      else
      {
        stun = 0;

      }
    }

    // grab the current frame of animation
    currentframe = currentframe();

    // once the animation is complete, return the character back to the
    // neutral state
    if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime) && spfzanimation.currentAnimation != "SATKD"
      && spfzanimation.currentAnimation != "SBLK" && spfzanimation.currentAnimation != "ABLK"
      && spfzanimation.currentAnimation != "CBLK")
    {

      attacking = false;
      spfzanimation.currentAnimation = "IDLE";
      spfzanimationstate.set(spfzanimation.frameRangeMap.get("IDLE"), spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation)), Animation.PlayMode.LOOP);
      stateTime = 0;
    }

    // stateTime += Gdx.graphics.getDeltaTime();
  }

  public void grdatkanim()
  {
    //spfzanimation.fps = 60;
    if (cancel && !hitboxsize.isZero())
    {
      hitboxsize.setZero();
    }
    // Normal has connected.
    if (spfzanimationstate.paused && cancelled == 0 || pausefrm)
    {
      // using this process to get the exact frame in order to setup the
      // recovery animation
      if (!pausefrm)
      {
        pauseframe = currentframe;
        pausefrm = true;
        pauseTime = 0;
      }
      cancel = true;
      // pauseTime += .015f;

      if (!attacking)
      {
        confirm = false;
      }
      if (special && isPunch && cancelled == 0 && cancel)
      {
        cancelled = 1;
      }
      // .10 will need to be a value coming in from the file
      // if (pauseTime >= .08f)
      if (pauseTime >= rtnFrametime(25f))
      {
        if (stage.shake)
        {
          stage.shake = false;
        }
        confirm = false;
        pauseTime = 0f;
        spfzanimationstate.paused = false;
        pausefrm = false;
        if (cancelled != 1)
        {
          if (spfzanimation.currentAnimation != null && spfzanimation.currentAnimation != "recovery")
          {

            spfzanimationstate.set(
              new FrameRange("recovery", pauseframe,
                spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).endFrame),
              60, Animation.PlayMode.NORMAL);
            stateTime = 0;
            spfzanimation.currentAnimation = "recovery";
            lastanim = spfzanimation.currentAnimation;
            cancel = false;
          }
          // set recovery frame animation here

        }
      }
      pauseTime += rtnFrametime(5);
    }
  }

  public void grdmvmtanim()
  {
    if (cancel)
    {
      cancel = false;
    }
    //spfzanimation.fps = 16;
    if (!isDown && !isUp && (isLeft || isRight || dash) && spfzrect.y == stage.GROUND && !ltstuck)
    {

      if (!stwlk)
      {

        spfzanimation.currentAnimation = "FTRANS";
        //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      }
      else
      {
        if (!dash)
        {
          //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
          if (spfzrect.y <= stage.GROUND)
          {
            // spfzanimation.currentAnimation = "movement";
            if (isLeft)
            {
              if (spfzattribute.scaleX > 0)
              {
                spfzanimation.currentAnimation = "BWLK";
              }
              else
              {
                spfzanimation.currentAnimation = "FWLK";
              }
            }
            else if (isRight)
            {
              if (spfzattribute.scaleX > 0)
              {
                spfzanimation.currentAnimation = "FWLK";
              }
              else
              {
                spfzanimation.currentAnimation = "BWLK";
              }
            }
            if(isLeft && isRight)
            {
              spfzanimation.currentAnimation = "IDLE";
            }
          }
        }
      }

      if (dash)
      {
        if(dashdir == 0 && spfzattribute.scaleX > 0 ||
           dashdir == 1 && spfzattribute.scaleX < 0)
        {
          spfzanimation.currentAnimation = "BDASH";
        }
        else
        {
          spfzanimation.currentAnimation = "FDASH";
        }
        //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      }

    }
    else
    {
      if (!dash)
      {
        if (spfzrect.y <= stage.GROUND)
        {
          if (stwlk)
          {
            if (spfzanimation.currentAnimation != "STPTRANS")
            {
              stateTime = 0;
            }
            spfzanimation.currentAnimation = "STPTRANS";
            //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
            //stateTime += Gdx.graphics.getDeltaTime();
            if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime))
            {
              stwlk = false;
              setneutral();
            }
          }
          else
          {
            spfzanimation.currentAnimation = "IDLE";
          }
        }

        if (isDown)
        {
          spfzanimation.currentAnimation = "CRCH";
        }
      }
    }
  }

  @Override
  public boolean attacked()
  {
    return attacked;
  }

  @Override
  public boolean attacking()
  {
    return attacking;
  }

  @Override
  public TransformComponent attributes()
  {

    return spfzattribute;
  }

  public void boundlogic()
  {

    // If the player has reached the left bound facing right
    if (spfzrect.x <= stage.cam.position.x - stage.HALF_WORLDW && spfzattribute.scaleX > 0)
    {
      spfzattribute.x = stage.cam.position.x - charX() - stage.HALF_WORLDW;
    }
    // If the player has reached the right bound facing right
    else if (spfzrect.x + spfzrect.width >= stage.cam.position.x + stage.HALF_WORLDW && spfzattribute.scaleX > 0)
    {
      spfzattribute.x = stage.cam.position.x + stage.HALF_WORLDW - (spfzrect.width);
    }

    // If the player has reached the right bound facing left
    if (spfzrect.x + spfzrect.width >= stage.cam.position.x + stage.HALF_WORLDW && spfzattribute.scaleX < 0)
    {
      spfzattribute.x = stage.cam.position.x + stage.HALF_WORLDW - (spfzrect.width + charX());
    }

    // If the player has reached the left bound facing left
    else if (spfzrect.x - spfzrect.width <= stage.cam.position.x - stage.HALF_WORLDW && spfzattribute.scaleX < 0)
    {
      spfzattribute.x = stage.cam.position.x - stage.HALF_WORLDW + spfzrect.width;
    }

  }

  @Override
  public float center()
  {
    return setrect().x + spfzrect.width * .5f;
  }

  public void checkputp1()
  {
    if (!lastp1movement.equals(p1movement))
    {
      // set flag for check scripts to be called
      for (int i = 0; i < p1movement.size(); i++)
      {
        if (lastp1movement.get(i) != p1movement.get(i))
        {
          runscript = true;
          lastp1movement.set(i, p1movement.get(i));
        }
      }
    }
    else
    {
      runscript = false;
    }

    if (dash && (spfzanimation.currentAnimation == "BDASH" || spfzanimation.currentAnimation == "FDASH") &&
        spfzanimationstate.currentAnimation.isAnimationFinished(stateTime)&& !stage.standby)
    {
      runscript = true;
      dash = false;
    }
  }

  @Override
  public int combonum()
  {
    return combocount;
  }

  public void createHitBox(int player, String animation)
  {
    posofhitbox.setZero();
    hitboxsize.setZero();
    float boxX;
    float boxY = spfzattribute.y + stage.player1data.get(stage.p1).get(BOXY).get(move).floatValue();
    float sizeW;
    float sizeH;

    if(spfzattribute.scaleX > 0)
    {
      boxX =  this.center() + stage.player1data.get(stage.p1).get(BOXX).get(move).floatValue();
      sizeW = stage.player1data.get(stage.p1).get(BOXWIDTH).get(move).floatValue();
      sizeH = stage.player1data.get(stage.p1).get(BOXHEIGHT).get(move).floatValue();
    }
    else
    {
      boxX =  this.center() - stage.player1data.get(stage.p1).get(BOXX).get(move).floatValue() - stage.player1data.get(stage.p1).get(BOXWIDTH).get(move).floatValue();
      sizeW = stage.player1data.get(stage.p1).get(BOXWIDTH).get(move).floatValue();
      sizeH = stage.player1data.get(stage.p1).get(BOXHEIGHT).get(move).floatValue();
    }
    /*
     * New hitbox logic:
     *
     * 1. get the animation based on user input
     *
     * 2. pass animation and character into process that will then pass back
     * these values: - active frames beginning to end - hitbox size - hitbox
     * position - amount of stun move will do
     *
     */

    activeframes[0] = stage.player1data.get(stage.p1).get(ACTSTARTBOX).get(move).intValue() - spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame;
    activeframes[1] = stage.player1data.get(stage.p1).get(ACTENDBOX).get(move).intValue() - spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame;

    posofhitbox.x = boxX;
    posofhitbox.y = boxY + (spfzrect.y - boxY);
    hitboxsize.x = sizeW;
    hitboxsize.y = sizeH;



  }

  @Override
  public int currentframe()
  {
    //spfzanimationstate.currentAnimation.setFrameDuration(1/spfzanimation.fps);
    return spfzanimationstate.currentAnimation.getKeyFrameIndex(stateTime);
  }

  @Override
  public DimensionsComponent dimensions()
  {

    return spfzdim;
  }

  @Override
  public void dispose()
  {

  }

  public void down(float delta)
  {
    // if user is pressing down on the control pad

    p1movement.set(0, false);
    p1movement.set(1, false);
    p1movement.set(3, true);
    p1movement.set(6, false);
    p1movement.set(7, false);

    if (isLeft)
    {
      p1movement.set(3, false);
      p1movement.set(6, false);
      p1movement.set(7, true);
    }
    else if (isRight)
    {
      p1movement.set(3, false);
      p1movement.set(6, true);
      p1movement.set(7, false);
    }

  }

  public ShapeRenderer drawhitbox()
  {
    return spfzhitbox;
  }

  public ShapeRenderer drawrect()
  {
    return spfzsr;
  }

  @Override
  public boolean getboxconfirm()
  {
    return confirm;
  }

  public TransformComponent getspfzattribute()
  {
    return spfzattribute;
  }

  public float getWalkspeed()
  {
    return walkspeed;
  }

  @Override
  public void hitboxconfirm(boolean confirm)
  {
    this.confirm = confirm;
  }

  @Override
  public Vector2 hitboxpos()
  {
    return posofhitbox;
  }

  @Override
  public Vector2 hitboxsize()
  {
    return hitboxsize;
  }

  @Override
  public void init(Entity entity)
  {
    ComponentMapper<MainItemComponent> mc = ComponentMapper.getFor(MainItemComponent.class);
    mc.get(entity);

    spfzp1vals = new CharAttributes(mc.get(entity).itemIdentifier, stage.player1data.size());

    if (stage.player1data.size() != 3)
    {
      stage.player1data.add(spfzp1vals.tempplayer);
    }

    spfzsr = new ShapeRenderer();
    spfzhitbox = new ShapeRenderer();
    spfzcharbox = new ShapeRenderer();
    spfzdimbox = new ShapeRenderer();

    //spfzrect = new Rectangle();
    spfzrect = new Rectangle(spfzp1vals.getCharDims());
    adjustX = spfzrect.x;
    adjustY = spfzrect.y;
    spfzhitrect = new Rectangle();
    spfzcharrect = new Rectangle();
    dimrect = new Rectangle();
    crossrect = new Rectangle();
    hitboxsize = new Vector2();
    posofhitbox = new Vector2();
    activeframes = new int[]{0, 0};
    hitboxsize = new Vector2(0, 0);
    posofhitbox = new Vector2(0, 0);
    spfzentity = entity;
    setanimations();

    // get the values of the character from the Character attributes class
    gravity = spfzp1vals.getGravity();
    jumpspeed = spfzp1vals.getJump();
    walkspeed = spfzp1vals.getWalkspeed();
    walkandjump = spfzp1vals.getWandj();
    inputs = spfzp1vals.getmoveinputs();
    dashspeed = spfzp1vals.getdashadv();
    tempspeed = walkandjump.x;


    setPos();

  }

  @Override
  public boolean isplayerone()
  {
    return true;
  }

  public void jumplogic(float delta)
  {
    // Apply gravity for spfzattribute calculations
    if (setrect().y > stage.GROUND)
    {
      if (!spfzanimationstate.paused)
      {
        walkandjump.y += gravity * delta;
      }
    }
    // assign the new jump value to the spfzattribute attribute to
    // apply gravity to the spfzattribute
    if (!spfzanimationstate.paused)
    {
      spfzattribute.y += walkandjump.y;
    }

    if (isJumping)
    {
      if (spfzrect.y > stage.GROUND)
      {
        if (spfzrect.y > stage.GROUND + stage.WALLJRES
          && (stage.cam.position.x <= stage.camboundary[0] + 1 || stage.cam.position.x + 1 >= stage.camboundary[1])
          && ((spfzattribute.x + adjustX <= stage.stageboundary[0] && spfzattribute.scaleX > 0)
          || spfzattribute.x - adjustX + 1 >= stage.stageboundary[1] && spfzattribute.scaleX < 0))
        {
          if (isLeft && isUp && !isRight && spfzattribute.scaleX < 0
            || isRight && isUp && !isLeft && spfzattribute.scaleX > 0)
          {
            // Needs modification, character keeps riding up on wall
            walljump = true;
            spfzattribute.y += walkandjump.y;
          }
        }
        else if (spfzrect.y < stage.GROUND + stage.WALLJRES)
        {
          walljump = false;
        }
        // if Jump direction is true(right) it will advance the player
        // to the
        // right
        // else Jump direction is false(left) it will advance the player
        // to the
        // left
        if (jumpdir)
        {
          if (walljump)
          {
            if (!spfzanimationstate.paused)
            {

              spfzattribute.x += walkandjump.x * .0150f;
            }
            // code for wall jump particle effect
          }
          else
          {
            if (!spfzanimationstate.paused)
            {

              spfzattribute.x += walkandjump.x * .0150f;

            }
          }
        }
        else
        {
          if (walljump)
          {
            // wall jump particle effect will be here

            if (!spfzanimationstate.paused)
            {
              spfzattribute.x -= walkandjump.x * .0150f;
            }
          }
          else
          {
            if (!spfzanimationstate.paused)
            {
              spfzattribute.x -= walkandjump.x * .0150f;
            }
          }
        }
      }
      else
      {
          if(!isUp && (!isRight || !isLeft) && isJumping)
          {
              isJumping = false;
          }
      }
    }

    // If spfzattribute has reached the boundary of the ground, set to the
    // boundary of the ground
    if (spfzrect.y < stage.GROUND)
    {

      if (stage.access.root.getChild("p1land").getChild("landp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
      {
        stage.access.root.getChild("p1land").getChild("landp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects
          .removeValue(stage.access.root.getChild("p1land").getChild("landp1").getEntity()
            .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
      }
      stage.access.root.getChild("p1land").getEntity().getComponent(TransformComponent.class).x = center();
      stage.access.root.getChild("p1land").getEntity().getComponent(TransformComponent.class).y = stage.GROUND;
      stage.access.root.getChild("p1land").getChild("landp1").getEntity()
        .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
      stage.access.root.getChild("p1land").getChild("landp1").getEntity()
        .getComponent(TransformComponent.class).scaleX = 1f;
      stage.access.root.getChild("p1land").getChild("landp1").getEntity().getComponent(SPFZParticleComponent.class)
        .startEffect();
      attacking = false;
      confirm = false;
      hitboxsize.setZero();
      walkandjump.y = 0;
      spfzattribute.y = charGROUND();
    }

  }

  public float charX()
  {
    return spfzrect.x - spfzattribute.x;
  }
  public float charY()
  {
    return spfzrect.y - spfzattribute.y;
  }
  public float charGROUND()
  {
    return stage.GROUND - (spfzrect.y - spfzattribute.y);
  }

  public void kick()
  {
    spfzanimation.currentAnimation = null;

    if (swap && stage.p1spec >= 100f)
    {
      // if height restriction or on ground allow,
      if (spfzrect.y == stage.GROUND && projact || spfzrect.y >= stage.GROUND + 30f)
      {
        stage.switchp1 = true;
        stage.sigp1lock = true;
        stage.switchcount = 0;
        swap = false;
        isPunch = false;
        spectime = 0;
        swaptime = 0;
        System.out.println("****************************** SWAP USED ******************************");
      }
      else
      {
        swap = false;
        System.out
          .println("****************************** SWAP LOST - HEIGHT RESTRICTION  ******************************");
      }
    }
    else
    {
      move = -1;

      if (spfzattribute.scaleX > 0)
      {
        if (!special || spfzrect.y > stage.GROUND)
        {
          if (spfzrect.y == stage.GROUND)
          {
            if (isLeft)
            {
              input = 9;
              if(isDown)
              {
                input = 3;
              }
              weight = "L";

            }
            else if (isRight)
            {
              input = 11;
              if(isDown)
              {
                input = 5;
              }
              weight = "H";


            }
            else if (!isLeft && !isRight)
            {
              input = 10;
              if(isDown)
              {
                input = 4;
              }
              weight = "M";

            }
          }
          else
          {
            // if isJumping is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping)
            {
              // if jumpdir is true, means we are jumping forwrds,
              // otherwise, we
              // are jumping backwards
              if (jumpdir)
              {
                input = 17;
              }
              else
              {
                input = 15;
              }
            }
            else
            {
              input = 16;
            }
            weight = "H";
          }
        }
      }
      else
      {
        if (!special || spfzrect.y > stage.GROUND)
        {
          // Ground Kicks
          if (spfzrect.y == stage.GROUND)
          {
            if (isLeft)
            {
              input = 11;
              if(isDown)
              {
                input = 5;
              }
              weight = "H";

            }
            else if (isRight)
            {
              input = 9;
              if(isDown)
              {
                input = 3;
              }
              weight = "L";
            }
            else if (!isLeft && !isRight)

            {
              input = 10;
              if(isDown)
              {
                input = 4;
              }
              weight = "M";
            }
          }
          // Air Kicks
          else
          {
            // if isJumping is true, means we are jumping either
            // forwards or
            // backwards.
            if (isJumping)
            {
              // if jumpdir is true, means we are jumping
              // backwards, otherwise,
              // we are jumping forwards
              if (jumpdir)
              {
                input = 15;
              }
              else
              {
                input = 17;
              }
            }
            else
            {
              input = 16;
            }
            weight = "H";
          }
        }

      }

      attacking = true;
      // if the move is not null technically
      move = spfzp1vals.moveset.indexOf(stage.normals[input]);

      if (move != -1)
      {
        spfzanimation.currentAnimation = spfzp1vals.moveset.get(move);
        spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation)),
          Animation.PlayMode.NORMAL);
        attackMove();
      }

    }

    attacking = true;
    if (spfzanimation.currentAnimation != null)
    {
      spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation)),
        Animation.PlayMode.NORMAL);
    }
  }

  @Override
  public boolean invul()
  {
    return invul;
  }

  public void attackMove()
  {
    //set the durations of the 3 stages of movement as well as the amount to push or pull the character.
    float strtupmov = 0;
    float actmov = 0;
    float recovmov = 0;

    float firstdur = rtnFrametime(stage.player1data.get(stage.p1).get(ACTSTARTBOX).get(move).floatValue() - (float) spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame);
    float seconddur = rtnFrametime(stage.player1data.get(stage.p1).get(ACTENDBOX).get(move).floatValue() - stage.player1data.get(stage.p1).get(ACTSTARTBOX).get(move).floatValue());
    float thirddur = rtnFrametime(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).endFrame - stage.player1data.get(stage.p1).get(ACTENDBOX).get(move).floatValue());

    if (stage.player1data.get(stage.p1).get(BACK_START).get(move).doubleValue() == 1)
    {
      strtupmov = stage.player1data.get(stage.p1).get(BMOVE).get(move).floatValue();

      if(spfzattribute.scaleX > 0)
      {
        strtupmov *= -1f;
      }

    }
    if (stage.player1data.get(stage.p1).get(FWD_START).get(move).doubleValue() == 1)
    {
      strtupmov = stage.player1data.get(stage.p1).get(FMOVE).get(move).floatValue();

      if(spfzattribute.scaleX < 0)
      {
        strtupmov *= -1f;
      }
    }

    if (stage.player1data.get(stage.p1).get(BACK_START).get(move).doubleValue() == 0 &&
            stage.player1data.get(stage.p1).get(FWD_START).get(move).doubleValue() == 0)
    {
      strtupmov = 0;
      //firstdur = 0;
    }

    if (stage.player1data.get(stage.p1).get(BACK_ACTIVE).get(move).doubleValue() == 1)
    {
      actmov = stage.player1data.get(stage.p1).get(BMOVE).get(move).floatValue();

      if(spfzattribute.scaleX > 0)
      {
        actmov *= -1f;
      }
    }
    if (stage.player1data.get(stage.p1).get(FWD_ACTIVE).get(move).doubleValue() == 1)
    {
      actmov = stage.player1data.get(stage.p1).get(FMOVE).get(move).floatValue();

      if(spfzattribute.scaleX < 0)
      {
        actmov *= -1f;
      }
    }

    if (stage.player1data.get(stage.p1).get(BACK_ACTIVE).get(move).doubleValue() == 0 &&
            stage.player1data.get(stage.p1).get(FWD_ACTIVE).get(move).doubleValue() == 0)
    {
      actmov = 0;
      //seconddur = 0;
    }

    if (stage.player1data.get(stage.p1).get(BACK_RECOV).get(move).doubleValue() == 1)
    {
      recovmov = stage.player1data.get(stage.p1).get(BMOVE).get(move).floatValue();

      if(spfzattribute.scaleX > 0)
      {
        recovmov *= -1f;
      }
    }
    if (stage.player1data.get(stage.p1).get(FWD_RECOV).get(move).doubleValue() == 1)
    {
      recovmov = stage.player1data.get(stage.p1).get(FMOVE).get(move).floatValue();

      if(spfzattribute.scaleX < 0)
      {
        recovmov *= -1f;
      }
    }

    if (stage.player1data.get(stage.p1).get(BACK_RECOV).get(move).doubleValue() == 0 &&
            stage.player1data.get(stage.p1).get(FWD_RECOV).get(move).doubleValue() == 0)
    {
      recovmov = 0;
      //thirddur = 0;
    }

    Actions.addAction(spfzentity,
      Actions.sequence(
              Actions.moveBy(strtupmov, 0, firstdur, Interpolation.sineOut),
              Actions.moveBy(actmov, 0, seconddur, Interpolation.sineOut),
              Actions.moveBy(recovmov, 0, thirddur, Interpolation.sineOut)));
    createbox = true;
  }
  public void left(float delta)
  {
    // If sprite is on the ground
    if (spfzrect.y == stage.GROUND)
    {
      if (!stage.close)
      {
        // move sprite horizontally
        if (!dash)
        {
          spfzattribute.x -= walkandjump.x * delta;
        }
      }
    }
  }

  @Override
  public Vector2 moveandjump()
  {

    return walkandjump;
  }

  public void movement(float delta)
  {
    storeinputs();

    for (int i = 0; i < inputs.size(); i++)
    {
      if (i >= 2)
      {
        getmove(inputs.get(i));
      }
      else
      {
        if (!dash)
        {
          execuniv(inputs.get(i));
        }
      }
    }

    stage.p1xattr = spfzattribute.x;

    if (isLeft)
    {
      p1movement.set(0, true);
      if (!attacking && !attacked && !isDown && !stage.standby && !ltstuck && !blocking)
      {

        // If Player 2 is on the right side, and their center is greater
        // than
        // Player 2's center, make player 2 block

        if (stage.spfzp2move.center() > center())
        {
          blk = true;
        }
        else
        {
          blk = false;
        }

        left(delta);

      }
    }
    else
    {
      p1movement.set(0, false);

    }

    if (isRight)
    {
      p1movement.set(1, true);
      if (!attacking && !attacked && !isDown && !stage.standby && !ltstuck && !blocking)
      {
        if (!attacking && !attacked)
        {
          // If Player one is on the left side, and their center is greater
          // than
          // Player 2's center, make player 2 block
          if (stage.spfzp2move.center() < center())
          {
            blk = true;
          }
          else
          {
            blk = false;
          }

        }
        right(delta);

      }
    }
    else
    {
      p1movement.set(1, false);
    }
    if (isUp)
    {
      up(delta);
    }
    else
    {
      p1movement.set(2, false);
      p1movement.set(4, false);
      p1movement.set(5, false);
    }

    if (isDown)
    {

      down(delta);
    }
    else
    {
      p1movement.set(3, false);
      p1movement.set(6, false);
      p1movement.set(7, false);
    }

    if (cancelled == 1)
    {
      isPunch = true;
      special = true;
      attacking = false;
      punchstuck = false;
      kickstuck = false;
      spfzanimationstate.paused = false;

    }
    if (isPunch && !attacked && !dash && !stage.standby)
    {
      if (attacking)
      {
        punchstuck = true;
      }
      if (!punchstuck)
      {
        punch();
      }
    }
    else
    {
      if (!isPunch)
      {

        punchstuck = false;
      }
    }
    if (isKick && !attacked && !dash && !stage.standby)
    {
      if (attacking)
      {
        if (!swap)
        {
          kickstuck = true;
        }
      }
      if (!kickstuck)
      {
        kick();
      }
    }
    else
    {
      if (!isKick)
      {
        kickstuck = false;
      }
    }

    if (!stage.standby)
    {
      if (intpol != 1 && !dash)
      {
        intpol = 1;
      }


      if ((spfzanimation.currentAnimation == "BDASH" || spfzanimation.currentAnimation == "FDASH") ||
        dash && spfzrect.y == stage.GROUND)
      {
        float totalFrames = currTotalFrames();
        float totalFrmTime = rtnFrametime(totalFrames);

        float progress = stateTime / totalFrmTime;

        if(startpt == 0)
        {
          startpt = spfzattribute.x;
          dashpoints = new Vector2(startpt, 0);
        }

        if (dashdir == 0)
        {
          if (!stage.close)
          {
            dashpoints.interpolate(new Vector2(startpt - dashspeed, 0), progress, Interpolation.exp10In);
          }
        }
        else
        {
          if (!stage.close)
          {
            dashpoints.interpolate(new Vector2(startpt + dashspeed, 0), progress, Interpolation.exp10In);
          }
        }

        spfzattribute.x = dashpoints.x;

        if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime))
        {
          walkandjump.x = tempspeed;
          startpt = 0;
        }

      }
      if (special)
      {
        spectime += Gdx.graphics.getDeltaTime();
        if (spectime >= rtnFrametime(stage.SPECIAL_WINDOW))
        {
          System.out.println("****************************** SPECIAL NOT AVAILABLE ******************************");
          special = false;
          spectime = 0;
          speccount = 0;
        }
      }
      if (swap)
      {
        swaptime += Gdx.graphics.getDeltaTime();
        if (swaptime >= rtnFrametime(stage.SWAP_WINDOW))
        {
          System.out.println("****************************** SWAP NOT AVAILABLE ******************************");
          swap = false;
          swaptime = 0;
          speccount = 0;
        }
      }
    }
  }

  public void punch()
  {
    // "null" move
    bouncer = false;
    move = -1;

    if (stage.p1charzoom)
    {
      stage.p1charzoom = false;
    }
    if (spfzattribute.scaleX > 0)
    {
      if (!special || spfzrect.y > stage.GROUND)
      {
        if (spfzrect.y == stage.GROUND)
        {
          if (isLeft)
          {

            input = 6;
            if(isDown)
            {
              input = 0;
            }
            weight = "L";
            ltstuck = true;
          }
          else if (isRight)
          {
            input = 8;
            if(isDown)
            {
              input = 2;
              bouncer = true;
            }
            weight = "H";
          }
          else if (!isLeft && !isRight)
          {
            input = 7;
            if(isDown)
            {
              input = 1;
            }
            weight = "M";
          }
        }
        else
        {
          // if isJumping is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping)
          {
            // if jumpdir is true, means we are jumping forwards,
            // otherwise, we
            // are jumping backwards
            if (jumpdir)
            {
              input = 14;
            }
            else
            {
              input = 12;
            }
          }
          else
          {
            input = 13;
          }

          if (spfzrect.y > stage.GROUND)
          {
            weight = "H";
          }
          else
          {
            createbox = false;
          }
        }
      }
      else
      {
        if (special && spfzrect.y == stage.GROUND)
        {
          spfzanimation.currentAnimation = "projectile";
          if (!projact && special)
          {
            spwnPrj();
            weight = "H";
            if (speccount >= 2)
            {
              stage.p1charzoom = true;
              // speccount = 0;
            }
            else
            {
              // speccount = 0;
            }
            speccount = 0;
            createbox = false;
            if (cancelled == 1)
            {
              stateTime = 0;
              cancelled = 0;
              spfzanimationstate.paused = false;
            }
          }
        }
      }
    }
    else
    {
      if (!special || spfzrect.y > stage.GROUND)
      {

        if (spfzrect.y == stage.GROUND)
        {
          if (isLeft)
          {
            input = 8;
            weight = "H";
            if(isDown)
            {
              input = 2;
              bouncer = true;
            }
          }
          else if (isRight)
          {
            input = 6;
            if(isDown)
            {
              input = 0;
            }
            weight = "L";
            ltstuck = true;
          }
          else if (!isRight && !isLeft)
          {
            weight = "M";
            input = 7;
            if(isDown)
            {
              input = 1;
            }
          }
        }
        else
        {
          // if isJumping is true, means we are jumping either
          // forwards or
          // backwards.
          if (isJumping)
          {
            // if jumpdir is true, means we are jumping backwards,
            // otherwise, we
            // are jumping forwards
            if (jumpdir)
            {
              input = 12;
            }
            else
            {
              input = 14;
            }
          }
          else
          {
            input = 13;
          }
          if (spfzrect.y > stage.GROUND)
          {
            weight = "H";
            createbox = true;
          }
        }

      }
      else
      {
        if (special && spfzrect.y == stage.GROUND)
        {
          move = -1;
          spfzanimation.currentAnimation = "projectile";
          if (!projact && special)
          {
            spwnPrj();
            weight = "H";
            createbox = false;
            if (cancelled == 1)
            {
              stateTime = 0;
              cancelled = 0;
            }
          }
        }
      }
    }

    attacking = true;
    // if the move is not null technically
    move = spfzp1vals.moveset.indexOf(stage.normals[input]);
    if (move != -1)
    {
      spfzanimation.currentAnimation = spfzp1vals.moveset.get(move);
    }
    spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation)),
      Animation.PlayMode.NORMAL);
    attackMove();
  }

  public void execuniv(int[] move)
  {
    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7 NEUTRAL = 8

    // special = false;
    int buffmove = 12;

    // passed in array
    if (buffer.get(buffer.size() - 1) == move[3] && buffer.size() >= 16)
    {

      if (buffer.get(buffer.size() - 2) == move[2])
      {

        for (int i = buffer.size() - 3; i > buffer.size() - 3 - (buffmove / 2); i--)
        {

          if (buffer.get(i) == move[1])
          {

            for (int k = i - 1; k > i - (buffmove / 2); k--)
            {
              if (buffer.get(k) == move[0] && !attacking && !dash)
              {

                dash = true;
                dashdir = move[1];
                k = i - (buffmove / 2);
                i = buffer.size() - 2 - (buffmove / 2);

                // line is executing twice. means this code is
                // executing more
                // than it should
                System.out.println("dash executed ------------------------------------");
              }
            }
          }
        }

      }

    }
  }

  public void getmove(int[] move)
  {
    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7 NEUTRAL = 8

    // special = false;
    int buffmove = 12;

    // firstread(move, buffmove);
    inputread(move, buffmove);

  }

  public void inputread(int[] move, int buffmove)
  {
    int check = 1;

    for (int i = move.length - 2; i >= 0; i--)
    {
      if (buffer.size() - check > 0)
      {
        if (i != 0)
        {
          if (buffer.get(buffer.size() - check) == move[i])
          {
            check++;
            continue;
          }
          else
          {
            i = -1;
          }
        }
        else
        {
          // for loop checking for last input
          for (int j = buffer.size() - check; j > buffer.size() - check - buffmove; j--)
          {
            if (j >= 0)
            {

              if (buffer.get(j) == move[i])
              {
                if (move[move.length - 1] == 1 && spfzattribute.scaleX > 0
                  || move[move.length - 1] == 0 && spfzattribute.scaleX < 0)
                {
                  special = true;
                  swap = true;
                  speccount++;
                  System.out
                    .println("****************************** SPECIAL - BUFFER FOR ******************************");
                  j = buffer.size() - check - buffmove;
                  i = -1;
                }
              }
            }
            else
            {

              for (int k = last16.size() - 1; k > last16.size() - 1 - buffmove; k--)
              {
                if (last16.size() >= 16)
                {
                  if (last16.get(k) == move[i])
                  {
                    if (move[move.length - 1] == 1 && spfzattribute.scaleX > 0
                      || move[move.length - 1] == 0 && spfzattribute.scaleX < 0)
                    {
                      special = true;
                      swap = true;
                      speccount++;

                      System.out.println(
                        "****************************** SPECIAL - LAST16 FOR MID BUFF ******************************");
                      k = k - 1 - buffmove;
                      j = buffer.size() - check - buffmove;
                      i = -1;
                    }
                  }
                }
              }
            }
          }
        }
      }
      else
      {
        // check needs to be reset back to 1 to start at the "top" of
        // the last16
        // input buffer list.
        check = 1;
        if (last16.size() > 0)
        {
          for (int k = i; k > 0; k--)
          {
            if (k != 0)
            {
              if (last16.get(last16.size() - check) == move[k])
              {
                check++;
                continue;
              }
              else
              {
                k = -1;
                i = -1;
              }
            }
            else
            {
              for (int j = last16.size() - 1; j > last16.size() - 1 - buffmove; j--)
              {

                if (last16.get(j) == move[i])
                {
                  if (move[move.length - 1] == 1 && spfzattribute.scaleX > 0
                    || move[move.length - 1] == 0 && spfzattribute.scaleX < 0)
                  {
                    special = true;
                    swap = true;
                    speccount++;

                    System.out.println(
                      "****************************** SPECIAL - LAST16 OUT BUFF ******************************");
                    j = last16.size() - 1 - buffmove;
                    k = -1;
                    i = -1;
                  }
                }
                else
                {
                  j = last16.size() - 1 - buffmove;
                }
              }
            }
          }
        }
      }
    }

  }

  public void setneutral()
  {

    stateTime = 0;
    attacking = false;
    attacked = false;
    spfzanimation.currentAnimation = "IDLE";
    if(isDown)
    {
      spfzanimation.currentAnimation = "CRCH";
    }

    spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
    spfzanimationstate.set(spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation), spfzanimation.fps,
            Animation.PlayMode.LOOP);
  }

  public void right(float delta)
  {
    // if user is pressing down RIGHT on the control pad

    // If sprite is on the ground
    if (spfzrect.y == stage.GROUND)
    {
      if (!stage.close)
      {
        // move sprite horizontally
        if (!dash)
        {
          spfzattribute.x += walkandjump.x * delta;
        }
      }
    }
  }

  public void setanimations()
  {
    NodeComponent nc;

    nc = ComponentRetriever.get(spfzentity, NodeComponent.class);
    spfzaction = ComponentRetriever.get(spfzentity, ActionComponent.class);
    spfzattribute = ComponentRetriever.get(spfzentity, TransformComponent.class);
    spfzattribute.x = stage.STAGE_CENTER - 200f;
    spfzattribute.y = charGROUND();

    spfzdim = ComponentRetriever.get(spfzentity, DimensionsComponent.class);
    spfzanimation = ComponentRetriever.get(nc.children.get(0), SpriteAnimationComponent.class);
    spfzanimationstate = ComponentRetriever.get(nc.children.get(0), SpriteAnimationStateComponent.class);
    List<String> keys = new ArrayList<String>();

    if (stage.player1anims.size() < 3)
    {
      stage.player1anims.add(spfzp1vals.animations);
      stage.player1moves.add(spfzp1vals.moveset);
      keys = new ArrayList<String>(spfzp1vals.animations.keySet());
    }
    else
    {
      keys = new ArrayList<String>(stage.player1anims.get(stage.p1).keySet());
    }

    // create frame ranges for all animations listed for each character

    if (stage.player1data.size() < 3)
    {
      for (int i = 0; i < spfzp1vals.getAnimations().size(); i++)
      {

        spfzanimation.frameRangeMap.put(keys.get(i), new FrameRange(keys.get(i),
          spfzp1vals.animations.get(keys.get(i))[0], spfzp1vals.getAnimations().get(keys.get(i))[1]));
      }
    }
    else
    {
      for (int i = 0; i < stage.player1anims.get(stage.p1).size(); i++)
      {

        spfzanimation.frameRangeMap.put(keys.get(i),
          new FrameRange(keys.get(i), stage.player1anims.get(stage.p1).get(keys.get(i))[0],
            stage.player1anims.get(stage.p1).get(keys.get(i))[1]));
      }
    }

    setneutral();

  }

  public void setcombonum(int comboint)
  {
    combocount = comboint;
  }

  public Rectangle sethitbox()
  {
    spfzhitrect.set(posofhitbox.x, posofhitbox.y, hitboxsize.x, hitboxsize.y);
    return spfzhitrect;
  }

  public void setPos()
  {
    if (stage.switchp1)
    {
      spfzattribute.x = stage.p1xattr;
      spfzattribute.y = stage.p1yattr;
      attacking = false;
    }
    else
    {

      float startpos = stage.STAGE_CENTER - stage.CHAR_SPACE;

      // 320 is the middle of the stage. As long as
      spfzattribute.x = startpos;
      spfzattribute.x = (spfzattribute.x - adjustX) - (spfzrect.width * .5f);
      spfzattribute.y = charGROUND();

    }
  }

  public void setstun()
  {
    if (blk || dblk)
    {
      if (projhit)
      {
        if (stage.spfzp1move.move != -1 && stun == 0)
        {
          stun = rtnFrametime(stage.player1data.get(stage.p1).get(BLKSTN).get(stage.spfzp1move.move).floatValue());
        }
        else
        {
          if (stun == 0)
          {
            stun = .5f;
          }
        }
      }
      else
      {
        if (stage.spfzp1move.move != -1 && stun == 0)
        {
          stun = rtnFrametime(stage.player2data.get(stage.p2).get(BLKSTN).get(stage.spfzp2move.move).floatValue());
        }
        else
        {
          if (stun == 0)
          {
            stun = .5f;
          }
        }
      }

    }
    else
    {
      if (projhit)
      {
        if (stage.spfzp1move.move != -1 && stun == 0)
        {
          stun = rtnFrametime(stage.player1data.get(stage.p1).get(HITSTN).get(stage.spfzp1move.move).floatValue());
        }
        else
        {
          if (stun == 0)
          {
            stun = .5f;
          }
        }
      }
      else
      {
        if (stage.spfzp1move.move != -1 && stun == 0)
        {
          stun = rtnFrametime(stage.player2data.get(stage.p2).get(HITSTN).get(stage.spfzp2move.move).floatValue());
        }
        else
        {
          if (stun == 0)
          {
            stun = .5f;
          }
        }
      }
    }
  }

  public boolean projconfirm()
  {
    if (projectile != null)
    {
      if (projectile.hit && !projectile.oppchar)
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

  public Rectangle setrect()
  {
      if(spfzattribute.scaleX > 0)
      {
          spfzrect.x = spfzattribute.x + adjustX;
      }
      else
      {
          spfzrect.x = (spfzattribute.x - adjustX) - spfzrect.width;
      }
      spfzrect.y = spfzattribute.y + adjustY;

    return spfzrect;
  }

  public Rectangle setcross()
  {
    float box = spfzrect.width * .5f;
    crossrect.set(spfzrect.x + (box * .5f),
            spfzrect.y, spfzrect.width * .5f,
            spfzrect.height);

    return crossrect;
  }

  @Override
  public void storeinputs()
  {
    // iterate through the buffer and store the integer value which
    // signifies
    // the inputs
    // LEFT = 0 UP = 2 LEFT & UP = 4 RIGHT & DOWN = 6
    // RIGHT = 1 DOWN = 3 RIGHT & UP = 5 LEFT & DOWN = 7 NEUTRAL = 8

    if (buff == 15)
    {
      last16.clear();
      last = 0;
    }
    if (buffer.size() == buffsize)
    {
      buffer.clear();
      buff = 0;
      System.out.println("buffer has been cleared");

    }
    buffer.add(buff, null);

    for (int j = 0; j < p1movement.size(); j++)
    {
      if (buffer.size() > 0)
      {

        if (p1movement.get(j))
        {
          buffer.set(buff, j);

          j = p1movement.size();

        }

      }
    }
    if (buffer.get(buff) == null)
    {
      buffer.set(buff, 8);
    }

    if (buff >= 43 && buff <= 59)
    {
      last16.add(buffer.get(buff));
      /*
       * System.out.println(buffer.get(buff) + " added to buffer. ---- index[" +
       * (buffer.size() - 1) + "] ------------ last stored input -----------" +
       * (last16.get(last)));
       */
      last++;
    }
    else
    {

      /*
       * System.out.println(buffer.get(buff) + " added to buffer. ---- index[" +
       * (buffer.size() - 1) + "]");
       */
    }
    System.out.println("Buffer input ----- " + buffer.get(buff));
    buff++;
  }

  public void spwnPrj()
  {
    SPFZProjScript projecter = new SPFZProjScript(stage);
    // SPFZProjScript2 projecter = new SPFZProjScript2(stage);
    projectile = projecter;
    Entity project;
    CompositeItemVO projVO = stage.access.land.loadVoFromLibrary("projectile");
    project = stage.access.land.entityFactory.createEntity(stage.access.root.getEntity(), projVO);
    ItemWrapper projwrapper = new ItemWrapper(project);
    project.getComponent(ZIndexComponent.class).setZIndex(4);
    project.getComponent(ZIndexComponent.class).needReOrder = false;
    project.getComponent(ZIndexComponent.class).layerName = "Default";
    project.getComponent(MainItemComponent.class).itemIdentifier = "projectile";
    stage.access.land.entityFactory.initAllChildren(stage.access.land.getEngine(), project, projVO.composite);
    stage.access.land.getEngine().addEntity(project);

    projwrapper.addScript((IScript) projectile);
    projact = true;
    special = false;

  }

  public void up(float delta)
  {
    // if user is pressing down UP on the control pad

    p1movement.set(0, false);
    p1movement.set(1, false);
    p1movement.set(2, true);
    p1movement.set(4, false);
    p1movement.set(5, false);

    if (isLeft)
    {
      p1movement.set(2, false);
      p1movement.set(4, true);
      p1movement.set(5, false);
    }
    else if (isRight)
    {
      p1movement.set(2, false);
      p1movement.set(4, false);
      p1movement.set(5, true);
    }

    if (!attacking && !attacked && !dash && !stage.standby)
    {

      if (stage.GROUND == spfzrect.y || walljump)
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
    }
  }

  public float rtnFrametime(float frames)
  {
      //return frames / 60f;
    return frames / spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
  }

  @Override
  public void update()
  {

  }

  @Override
  public void returnmove()
  {

  }

  @Override
  public boolean hit()
  {
    if (stage.spfzp1move.blk || stage.spfzp1move.dblk)
    {
      hit = false;
    }
    else
    {
      hit = true;
    }
    return hit;
  }

  public void reflect()
  {

  }

  public void parry()
  {

  }

  public float currTotalFrames()
  {
    return spfzanimationstate.currentAnimation.getKeyFrames().length;
  }
  @Override
  public Rectangle setcharbox()
  {
    spfzcharrect = spfzrect;
    return spfzcharrect;
  }

  public Rectangle dimrectangle() {
      if (spfzattribute.scaleX > 0) {
          dimrect.set(spfzattribute.x, spfzattribute.y, spfzdim.width, spfzdim.height);
      }
      else
      {
          dimrect.set(spfzattribute.x - spfzdim.width, spfzattribute.y, spfzdim.width, spfzdim.height);
      }
    return dimrect;
  }

  @Override
  public ShapeRenderer drawcharbox()
  {
    return spfzcharbox;
  }

  @Override
  public Rectangle setrflbox()
  {
    return null;
  }

  @Override
  public ShapeRenderer drawrflbox()
  {
    return null;
  }
}