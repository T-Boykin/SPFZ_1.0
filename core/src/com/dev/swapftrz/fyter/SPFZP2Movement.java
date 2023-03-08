package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.action.Actions;

import java.util.ArrayList;
import java.util.List;

public class SPFZP2Movement implements IScript, BufferandInput {

  boolean isRight, isLeft, isUp, isDown, isPunch, isKick, special, hit, blk, dblk, left, pushed, right, isJumping,
    jumpdir, attacking, attacked, blocking, ownatk, projhit, projact, confirm, walljump, kickstuck, punchstuck,
    reflect, dash, bounced, roll, invul;

  CharacterAttributes spfzp2vals;

  SPFZStage stage;
  Vector2 walkandjump;

  float stun, gravity, jumpspeed, walkspeed, stateTime = 0, rfltime, intpush, adjustX, adjustY;

  final float REFLECT = 1f, COMBO_PUSH_L = 4f, COMBO_PUSH_M = 3f, COMBO_PUSH_H = 2f, GRAV_MULT_L = .90f,
    GRAV_MULT_M = .80f, GRAV_MULT_H = .75f, FL_L = .5f, FL_M = .6f, FL_H = .7f;

  int combocount, currentframe, lastcount, buff, move, buffsize = 60;

  int[] activeframes;

  final int STRTANIMFRME = 0;
  final int ENDANIMFRME = 1;
  final int BLKSTN = 2;
  final int HITSTN = 3;
  final int BLKDIST = 4;
  final int HITDIST = 5;
  final int ACTSTARTBOX = 6;
  final int ACTENDBOX = 7;
  final int BOXX = 8;
  final int BOXY = 9;
  final int BOXWIDTH = 10;
  final int BOXHEIGHT = 11;
  final int BLKDMG = 12;
  final int HITDMG = 13;
  final int BLKMTR = 14;
  final int HITMTR = 15;

  List<Boolean> p2movement = new ArrayList<Boolean>();
  List<Boolean> lastp2movement = new ArrayList<Boolean>();
  List<Integer> buffer = new ArrayList<Integer>();
  List<Integer> last16 = new ArrayList<Integer>();

  Entity spfzentity;

  ActionComponent spfzaction;
  TransformComponent spfzattribute;
  DimensionsComponent spfzdim;

  Rectangle spfzrect, spfzhitrect, spfzcharrect, spfzrflrect, crossrect, dimrect;
  ShapeRenderer spfzsr, spfzhitbox, spfzcharbox, spfzrflbox, spfzdimbox;

  SpriteAnimationComponent spfzanimation;
  SpriteAnimationStateComponent spfzanimationstate;

  Vector2 hitboxsize, posofhitbox;

  public SPFZP2Movement(SPFZStage screen)
  {
    stage = screen;
  }

  @Override
  public void act(float delta)
  {
    if (reflect)
    {
      //setreflect();
    }

    if (!reflect && rfltime < REFLECT)
    {
      //rfltime += Gdx.graphics.getDeltaTime();
    }
    else
    {
      if (rfltime >= REFLECT)
      {
        reflect = true;
      }

      if (reflect && rfltime > 0f)
      {
        // rfltime -= Gdx.graphics.getDeltaTime();

        if (rfltime <= 0)
        {
          //reflect = false;
          //isUp = true;
        }
      }
    }

    /*if (!stage.gameover)
    {
      movement(delta);

      if (isUp)
        isUp = false;
    }*/

    animlogic();
    //boundlogic();
    //jumplogic(delta);

    // storeinputs();

    if (buffer.size() == 60) {
      returnmove();
    }
  }

  @Override
  public void dispose() {

  }

  public void animlogic()
  {
    if (spfzanimation.currentAnimation == "IDLE" || spfzanimation.currentAnimation == "movement")
      if (attacking)
        attacking = false;
    else
      stateTime += Gdx.graphics.getDeltaTime();

      // If the stun time has been reached after being attacked, rather than
      // basing it
      // off of the time of the animation, set player back to neutral


    // Check if player is attacked by checking if the player one has a hit
    // confirm
    if (ownatk) {
      confirm = true;
      projhit = true;
      ownatk = false;
    }

    //TODO STUN LOGIC
    /*if (((Attribs) stage.arrscripts.get(stage.p1)).getboxconfirm() && !ownatk)
    {
      hit = true;
      stun = 0;
      lastcount = combocount;
      intpush = 1;
      if (!stage.spfzp1move.projact)
      {
        stage.spfzp1move.spfzanimationstate.paused = true;
      }
      attacking = false;
      if (blocking)
      {
        if (spfzrect.y == stage.ground())
        {
          if (isDown)
          {
            spfzanimation.currentAnimation = "dblock";
          }
          else
          {
            spfzanimation.currentAnimation = "block";
          }
        }
        else
        {
          spfzanimation.currentAnimation = "ablock";
        }
      }
      else
      {
        spfzanimation.currentAnimation = "stun";

        if (spfzrect.y > stage.ground())
        {
          spfzanimation.currentAnimation = "astun";
          floatchar();
        }
      }

      setstun();

      stateTime = 0;
    }*/

    // If current animation is anything other than "neutral" or "movement", it
    // must be
    // timed as it is not looping
    if (spfzanimation.currentAnimation != "IDLE" && spfzanimation.currentAnimation != "movement")
    {

      // If the stun time has been reached after being attacked, rather than
      // basing it
      // off of the time of the animation, set player back to neutral

      if (stateTime >= stun && spfzanimation.currentAnimation != "roll")
      {
        attacked = false;
        spfzanimation.currentAnimation = "IDLE";
        spfzanimationstate.set(spfzanimation.frameRangeMap.get("IDLE"), 60, Animation.PlayMode.LOOP);
        stateTime = 0;
        pushed = false;
        if (roll)
        {
          roll = false;
        }

        // Set counter display to 0
        /*if (stage.spfzp1move.combonum() == 0)
        {
          LabelComponent combocount1;
          combocount1 = stage.access.root.getChild("ctrlandhud").getChild("combocount1").getEntity()
            .getComponent(LabelComponent.class);
          combocount1.setText(" ");
          if (stage.training)
          {
            stage.p2health = stage.startp2;
          }
        }*/
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

        if ((spfzanimation.currentAnimation == "stun" || spfzanimation.currentAnimation == "astun"
          || spfzanimation.currentAnimation == "block"
          || spfzanimation.currentAnimation == "ablock" || spfzanimation.currentAnimation == "dblock")
          && stateTime < stun)

        {
          /*if (!stage.spfzp1move.wallb)
          {

            //pushback logic
            if (spfzrect.y <= stage.ground())
            {

              // modifier from file
              float pushback;

              if (blk || dblk)
              {
                pushback = rtnFrametime(stage.player1data.get(stage.p1).get(BLKDIST).get(stage.spfzp1move.move).floatValue()) - stateTime;

              }
              else
              {
                if (stage.spfzp1move.move != -1)
                {

                  pushback = rtnFrametime(stage.player1data.get(stage.p1).get(HITDIST).get(stage.spfzp1move.move).floatValue()) - stateTime;
                }
                else
                {
                  pushback = rtnFrametime(110f) - stateTime;
                }
              }

              if (spfzattribute.scaleX > 0)
              {

                spfzattribute.x -= pushback(pushback);
              }
              else
              {

                spfzattribute.x += pushback(pushback);
              }


              pushed = true;


            }
            else
            {
              float switcher = 0f;

              if (center() < stage.spfzp1move.center())
              {
                switcher = -1f;
              }
              else
              {
                switcher = 1f;
              }

              // all air logic for when character is being attacked
              if (stage.spfzp1move.weight != null)
              {

                switch (stage.spfzp1move.weight)
                {
                  case "L":
                    spfzattribute.x += (COMBO_PUSH_L * switcher);
                    break;
                  case "M":
                    spfzattribute.x += (COMBO_PUSH_M * switcher);
                    break;
                  case "H":
                    spfzattribute.x += (COMBO_PUSH_H * switcher);
                    break;
                  default:
                    spfzattribute.x += (COMBO_PUSH_H * switcher);
                    break;
                }
              }
            }
          }
          else
          {
            if (bounced)
            {

              wallbouncelogic(true);
            }
            else
            {
              wallbouncelogic(false);
            }
          }
        }
        else
        {
          if (spfzrect.y <= stage.ground() && spfzanimation.currentAnimation != "roll")
          {
            stun = 0;

            if (stage.spfzp1move.wallb || stage.spfzp1move.bouncer)
            {
              stage.spfzp1move.wallb = false;
              stage.spfzp1move.bouncer = false;
            }
            bounced = false;
          }
        }*/
      }

      // grab the current frame of animation
      currentframe = spfzanimationstate.currentAnimation.getKeyFrameIndex(stateTime);

      // once the animation is complete, return the character back to the
      // neutral state
      if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime) && spfzanimation.currentAnimation != "stun"
        && spfzanimation.currentAnimation != "astun" && spfzanimation.currentAnimation != "block"
        && spfzanimation.currentAnimation != "ablock" && spfzanimation.currentAnimation != "dblock")
      {

        attacking = false;
        spfzanimation.currentAnimation = "IDLE";
        spfzanimationstate.set(spfzanimation.frameRangeMap.get("IDLE"), 60, Animation.PlayMode.LOOP);
        stateTime = 0;
        if (roll)
        {
          roll = false;
        }
      }


        //  //logic for ground stun. When the character is getting attacked, stun shall not apply
        if (spfzanimation.currentAnimation == "stun" && spfzrect.y <= stage.ground())
        // || spfzanimation.currentAnimation == "roll")
        {
          stateTime += Gdx.graphics.getDeltaTime();
        }

      }
    }
    else {
      // combo counter and state time should remain 0 until an event occurs such
      // as
      // This class ing, or being attacked
      //TODO combo counter logic
      /*if (combocount != 0)
      {
        LabelComponent count;
        count = stage.access.root.getChild("ctrlandhud").getChild("combocount2").getEntity()
          .getComponent(LabelComponent.class);

        Entity Hit = stage.access.root.getChild("ctrlandhud").getChild("p1himg").getEntity();
        Entity cc = stage.access.root.getChild("ctrlandhud").getChild("p1cc").getEntity();

        Actions.addAction(Hit, Actions.sequence(Actions.delay(.5f), Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic)));
        Actions.addAction(cc, Actions.sequence(Actions.delay(.5f), Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic)));

        count.setText(" ");
        combocount = 0;
      }*/
    }
  }

  private void wallbouncelogic(boolean push)
  {
    float side;

    if (spfzattribute.scaleX < 0)
    {
      side = 1f;
    }
    else
    {
      side = -1f;
    }
    if (push)
    {
      spfzattribute.x += 25f * side;
      if (spfzrect.y == stage.ground()) {
        Actions.addAction(spfzentity, Actions.moveBy(0, 50f, .5f));
      }

    }
    else
    {
      if (side > 0)
      {
        side = -1f;
      }
      else
      {
        side = 1f;
      }
      spfzattribute.x += 7.5f * side;
    }
  }

  //mechanic that activates when character lands from being attacked in air
  private void rollback(String roll)
  {
    float switcher;
    float rollamt = 0;

    if(spfzattribute.scaleX > 0)
    {
      switcher = -1f;
    }
    else
    {
      switcher = 1f;
    }

    switch(roll)
    {
      case "L":
        rollamt = 50f;
        break;
      case "M":
        rollamt = 100f;
        break;
      case "H":
        rollamt = 120f;
        break;

    }

    Actions.addAction(spfzentity,
      Actions.sequence(Actions.moveBy(rollamt * switcher, 0, .5f, Interpolation.circleOut),
        Actions.run(new Runnable()
        {

          @Override
          public void run()
          {
            invul = false;
          }
        })
    ));

  }

  public void setstun()
  {
   // float fd;
    //int totframes =  spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).endFrame -
     // spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame;
    if (blk || dblk)
    {
      /*if (stage.spfzp1move.move != -1 && stun == 0)
      {
        stun = rtnFrametime(stage.player1data.get(stage.p1).get(BLKSTN).get(stage.spfzp1move.move).floatValue());
      }
      else
      {
        if (stun == 0)
        {
          stun = .5f;
        }
      }*/
    }
    else
    {
      if (spfzrect.y == stage.ground()) {
        /*if (stage.spfzp1move.move != -1 && stun == 0)
        {
          stun = rtnFrametime(stage.player1data.get(stage.p1).get(HITSTN).get(stage.spfzp1move.move).floatValue());
        }
        else
        {
          if (stun == 0)
          {
            stun = .5f;
          }
        }*/
      }
      else
      {
        //character being air attacked
        stun = 2f;
      }
    }
    //fd = stun / totframes;
    //spfzanimationstate.currentAnimation.setFrameDuration(fd);
   // spfzanimation.fps = (int) fd * 60;
  }

  public void createHitBox(int player, String animation)
  {
    switch (player)
    {
      case 0:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 16;
          activeframes[1] = 20;

          hitboxsize.x = spfzrect.width * .8f;
          hitboxsize.y = spfzrect.height * .43f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 3);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .5f;

        }

        break;
      case 1:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 3;
          activeframes[1] = 4;

          hitboxsize.x = spfzrect.width * .5f;
          hitboxsize.y = spfzrect.height * .33f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 3);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .5f;

        }
        break;
      case 2:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 5;
          activeframes[1] = 6;

          hitboxsize.x = spfzrect.width * .7f;
          hitboxsize.y = spfzrect.height * .60f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 3);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .5f;

        }
        break;
      case 3:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 7;
          activeframes[1] = 10;

          hitboxsize.x = spfzrect.width * .9f;
          hitboxsize.y = spfzrect.height * .13f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 3);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .5f;

        }

        break;
      case 4:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 4;
          activeframes[1] = 7;

          hitboxsize.x = spfzrect.width * .8f;
          hitboxsize.y = spfzrect.height * .93f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 3);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .5f;

        }
        break;
      case 5:
        if (spfzanimation.currentAnimation == "attack")
        {
          activeframes[0] = 2;
          activeframes[1] = 3;

          hitboxsize.x = spfzrect.width * .2f;
          hitboxsize.y = spfzrect.height * .53f;

        }
        break;

      case 6:
        if (spfzanimation.currentAnimation == "attack")
        {
          // This is under the assumption that the
          // startup frames are happening before the
          // active frames
          activeframes[0] = 2;
          activeframes[1] = 3;

          hitboxsize.x = spfzrect.width * .5f;
          hitboxsize.y = spfzrect.height * .33f;

          if (spfzattribute.scaleX > 0)
          {
            posofhitbox.x = spfzrect.x + spfzrect.width + (spfzrect.width * .25f);
          }
          else
          {
            posofhitbox.x = spfzrect.x - ((spfzrect.width * .25f) * 2.8f);
          }
          posofhitbox.y = spfzrect.y + spfzrect.height * .375f;

        }
        break;
      default:
        break;
    }
  }

  public TransformComponent transformAttributes() {
    return spfzattribute;
  }

  public float getWalkspeed()
  {
    return walkspeed;
  }

  @Override
  public void init(Entity entity)
  {
    ComponentMapper<MainItemComponent> mc = ComponentMapper.getFor(MainItemComponent.class);
    mc.get(entity);
    //spfzp2vals = new CharacterAttributes(mc.get(entity).itemIdentifier);

    spfzsr = new ShapeRenderer();
    spfzhitbox = new ShapeRenderer();
    spfzcharbox = new ShapeRenderer();
    spfzrflbox = new ShapeRenderer();
    spfzdimbox = new ShapeRenderer();
    dimrect = new Rectangle();
    spfzrect = new Rectangle(spfzp2vals.getCharDims());
    adjustX = spfzrect.x;
    adjustY = spfzrect.y;
    //spfzrect = new Rectangle();
    crossrect = new Rectangle();
    spfzhitrect = new Rectangle();
    spfzcharrect = new Rectangle();
    spfzrflrect = new Rectangle();

    activeframes = new int[]{0, 0};
    hitboxsize = new Vector2(0, 0);
    posofhitbox = new Vector2(0, 0);

    spfzentity = entity;
    //setanimations();

    // get the values of the character from the Character attributes class
    gravity = spfzp2vals.getGravity();
    jumpspeed = spfzp2vals.getJump();
    walkspeed = spfzp2vals.getWalkspeed();
    //setPos();

    // System.out.println("character created");

  }

  public void kick() {

    spfzanimation.currentAnimation = "attack";
    attacking = true;

    //createHitBox(stage.processed.get(stage.p2), spfzanimation.currentAnimation);

  }


  @Override
  public void returnmove() {

  }

  public void setcombonum(int comboint) {
    combocount = comboint;
  }

  public void setreflect()
  {
    spfzrflrect.width = spfzdim.width * 1.25f;
    spfzrflrect.height = spfzdim.height;
    //spfzrflrect.x = center() - (spfzrflrect.width * .5f);
    spfzrflrect.y = spfzattribute.y;

  }

  public void applygrav()
  {

   /* //works alongside floatchar()
    switch (stage.spfzp1move.weight)
    {
      case "L":
        walkandjump.y += gravity * GRAV_MULT_L;
        break;
      case "M":
        walkandjump.y += gravity * GRAV_MULT_M;
        break;
      case "H":
        walkandjump.y += gravity * GRAV_MULT_H;
        break;
      default:
        walkandjump.y += gravity * GRAV_MULT_H;
        break;
    }*/
  }

  public void floatchar()
  {
    // Apply y value each time character is attacked. works alongside applygrav()
    walkandjump.y = 0;


    /*switch (stage.spfzp1move.weight)
    {
      case "L":
        walkandjump.y = jumpspeed * FL_L;
        break;
      case "M":
        walkandjump.y = jumpspeed * FL_M;
        break;
      case "H":
        walkandjump.y = jumpspeed * FL_H;
        break;
      default:
        walkandjump.y = jumpspeed * FL_H;
        break;
    }*/
  }

 /* public void shwreflect()
  {
    spfzrflbox.setProjectionMatrix(stage.access.viewportland.getCamera().combined);
    spfzrflbox.begin(ShapeType.Filled);

    spfzrflbox.setColor(Color.PURPLE);

    spfzrflbox.rect(spfzrflrect.x, spfzrflrect.y, spfzrflrect.width, spfzrflrect.height);

    spfzrflbox.end();

    // Reflect logic

    if (stage.spfzp1move.projectile != null)
    {
      if (stage.spfzp1move.projectile.spfzhitrect.overlaps(spfzrflrect))
      {
        stage.spfzp1move.projectile.spfzattribute.scaleX *= -1f;
        reflect = false;
      }
    }

  }*/

  public void parry() {
  }

  public float rtnFrametime(float frames)
  {
    return frames / 60f;
  }

  public float pushback(float pushvalue)
  {

    //there will be a standard value coming in from file basing how much pushback there will be applied on character

    float temppush = pushvalue;
    boolean set = false;
    float maxslow = 1f;
    float slowdown;

    //initpush @ 5% of pushamt - hard
    //midpush @ 50% of pushamt - soft
    //finpush @ 90% of push amt - stopped
    //if stateTime is greater than said percentage of the stun amount, slow the character push down

    if (stateTime > stun * .9f && !set)
    {
      set = true;
    }
    else if (stateTime > stun * .1f && !set)
    {
      slowdown = stateTime / maxslow;
      temppush = (temppush + (temppush * slowdown));
      set = true;
    }
    else if (stateTime > stun * .0001f && !set)
    {
      temppush = (temppush + (temppush * 10f));
      set = true;
    }

    return temppush;
  }


  @Override
  public void update() {
  }

}