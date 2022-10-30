package com.dev.swapftrz.stage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.systems.action.Actions;

class SPFZStageAnimation
{
   private final SPFZStage stage;

   public SPFZStageAnimation(SPFZStage stage) {
      this.stage = stage;
   }

   public void combocounter(Entity parent, Entity tens, Entity ones, int cc) {
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
      Entity Hit = stage.stageWrapper().getChild("ctrlandhud").getChild("p2himg").getEntity();
      parent.getComponent(TransformComponent.class).y = Hit.getComponent(TransformComponent.class).y;
      // parent.getComponent(TransformComponent.class).y = holder1;

   }

   public void timer() {
      Entity tenths = stage.stageWrapper().getChild("ctrlandhud").getChild("timeranim").getChild("tenths").getEntity();
      Entity ones = stage.stageWrapper().getChild("ctrlandhud").getChild("timeranim").getChild("ones").getEntity();

      int tempten = (timeleft % 100) / 10;
      int tempone = timeleft % 10;
      double tedbl;

      if (timeleft > 0 && !pausetime && !standby)
      {
         setneut = false;
         timeElapsed = (System.currentTimeMillis() - time) / 1000;
         timeleft = (int) (stage.roundTime() - timeElapsed);

         // Double time is correct. Need to figure out way to translate it into the
         // timer
         tedbl = ((System.currentTimeMillis() - (long) time) / 1000.0);
         timeleftdbl = (double) stage.roundTime() - tedbl;

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

   public void updateclock() {
      Entity fader, p1round1, p1round2, p2round1, p2round2, roundtext;

      p1round1 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundonep1").getEntity();
      p1round1.getComponent(TransformComponent.class).originX -= p1round1.getComponent(TransformComponent.class).originX
        / 2;
      p1round1.getComponent(TransformComponent.class).originY -= p1round1.getComponent(TransformComponent.class).originY
        / 2;
      p1round1.getComponent(TransformComponent.class).x += p1round1.getComponent(TransformComponent.class).originX / 2;
      p1round1.getComponent(TransformComponent.class).y += p1round1.getComponent(TransformComponent.class).originY / 2;

      p1round2 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtwop1").getEntity();
      p1round2.getComponent(TransformComponent.class).originX -= p1round2.getComponent(TransformComponent.class).originX
        / 2;
      p1round2.getComponent(TransformComponent.class).originY -= p1round2.getComponent(TransformComponent.class).originY
        / 2;
      p1round2.getComponent(TransformComponent.class).x += p1round2.getComponent(TransformComponent.class).originX / 2;
      p1round2.getComponent(TransformComponent.class).y += p1round2.getComponent(TransformComponent.class).originY / 2;

      p2round1 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundonep2").getEntity();
      p2round1.getComponent(TransformComponent.class).originX -= p2round1.getComponent(TransformComponent.class).originX
        / 2;
      p2round1.getComponent(TransformComponent.class).originY -= p2round1.getComponent(TransformComponent.class).originY
        / 2;
      p2round1.getComponent(TransformComponent.class).x += (p2round1.getComponent(TransformComponent.class).originX
        + p2round1.getComponent(TransformComponent.class).originX / 2);
      p2round1.getComponent(TransformComponent.class).y += p2round1.getComponent(TransformComponent.class).originY / 2;

      p2round2 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtwop2").getEntity();
      p2round2.getComponent(TransformComponent.class).originX -= p2round2.getComponent(TransformComponent.class).originX
        / 2;
      p2round2.getComponent(TransformComponent.class).originY -= p2round2.getComponent(TransformComponent.class).originY
        / 2;
      p2round2.getComponent(TransformComponent.class).x += (p2round2.getComponent(TransformComponent.class).originX
        + p2round2.getComponent(TransformComponent.class).originX / 2);
      p2round2.getComponent(TransformComponent.class).y += p2round2.getComponent(TransformComponent.class).originY / 2;

      roundtext = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtext").getEntity();

      Actions.addAction(roundtext, Actions.fadeOut(.01f));
   }

   public void animatenum(Entity num) {

      Actions.addAction(num, Actions.sequence(Actions.scaleTo(0, 0, 0f),
        Actions.parallel(Actions.rotateBy(720f, .360f), Actions.scaleBy(1f, 1f, .6f, Interpolation.elastic))));

   }

   public void animatecount(Entity num) {

      Actions.addAction(num, Actions.scaleTo(1f, 1f, .3f, Interpolation.elastic));

   }

   public void roundover() {

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
         fader = stage.stageWrapper().getChild("fader").getEntity();
         // timer =
         // stageItemWrapper.getChild("ctrlandhud").getChild("time").getEntity().getComponent(LabelComponent.class);

         eoround = false;
         resettimer();
         standby = true;
         finishedrd = false;

         // reset clock back to the round time then trigger the timer for the
         // next round
         timeleft = stage.roundTime();
         time = System.currentTimeMillis();
         // timer.setText(Integer.toString(timeleft));
         Actions.addAction(fader, Actions.run(new Runnable()
         {

            @Override
            public void run() {

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
         stage.stageWrapper().getChild("ctrlandhud").getChild("pausebutton").getEntity()
           .getComponent(TransformComponent.class).scaleX = 1f;
         stage.stageWrapper().getChild("ctrlandhud").getChild("pausebutton").getEntity()
           .getComponent(TransformComponent.class).scaleY = 0f;

         roundtextset();
         prefighttimer();

         // center stage and characters


         stage.camera().position.set(CENTER);
         stage.camera().update();

         if (stage.camera().position.idt(CENTER)) {
            stage.player1().setPos();
            spfzp2move.setPos();
         }

      }
      else if (finishedrd && gameover)
      {
         roundtextset();
      }

   }

   public void animateround(int roundcount) {
      Entity roundtext;
      roundtext = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtext").getEntity();
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


   public void p1round1() {
      Entity p1round1;

      p1round1 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundonep1").getEntity();

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
              fader = stage.stageWrapper().getChild("fader").getEntity();
              if (p2rdcount != 2)
              {
                 Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                 {

                    @Override
                    public void run() {
                       Entity fader;
                       fader = stage.stageWrapper().getChild("fader").getEntity();

                       finishedrd = true;
                       Actions.addAction(fader, Actions.fadeOut(1f));
                    }
                 })));
              }

           }
        })));

   }

   public void p2round1() {
      Entity p2round1;

      p2round1 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundonep2").getEntity();

      Actions.addAction(p2round1,
        Actions.sequence(Actions.parallel(Actions.rotateBy(-720, 1f),
          Actions.sequence(Actions.scaleBy(.6f, -.6f, .5f), Actions.scaleBy(-.6f, .6f, .5f)),
          Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
        {

           @Override
           public void run() {
              Entity fader;
              fader = stage.stageWrapper().getChild("fader").getEntity();
              ;
              if (p1rdcount != 2)
              {
                 Actions.addAction(fader, Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable()
                 {

                    @Override
                    public void run() {
                       Entity fader;
                       fader = stage.stageWrapper().getChild("fader").getEntity();

                       finishedrd = true;
                       Actions.addAction(fader, Actions.fadeOut(1f));
                    }
                 })));
              }
           }
        })));

   }

   public void p1round2() {
      Entity p1round2;

      p1round2 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtwop1").getEntity();

      Actions.addAction(p1round2,
        Actions.sequence(Actions.parallel(Actions.rotateBy(720, 1f),
          Actions.sequence(Actions.scaleBy(-.6f, -.6f, .5f), Actions.scaleBy(.6f, .6f, .5f)),
          Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
        {

           @Override
           public void run() {
              finishedrd = true;
              gameover = true;
           }
        })));
   }

   public void p2round2() {
      Entity p2round2;

      p2round2 = stage.stageWrapper().getChild("ctrlandhud").getChild("roundtwop2").getEntity();

      Actions.addAction(p2round2,
        Actions.sequence(Actions.parallel(Actions.rotateBy(-720, 1f),
          Actions.sequence(Actions.scaleBy(.6f, -.6f, .5f), Actions.scaleBy(-.6f, .6f, .5f)),
          Actions.color(Color.BLUE, 1f)), Actions.run(new Runnable()
        {

           @Override
           public void run() {
              finishedrd = true;
              gameover = true;
           }
        })));

   }

   public void lifeandround() {
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

      root.getChild("ctrlandhud").getEntity()
        .getComponent(TransformComponent.class).x = viewportland.getCamera().position.x - (HALF_WORLDW);

      root.getChild("ctrlandhud").getEntity()
        .getComponent(TransformComponent.class).y = viewportland.getCamera().position.y - (HALF_WORLDH);

   }
}
