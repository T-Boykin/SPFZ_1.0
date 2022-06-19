package com.dev.swapftrz.stage;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.dev.swapftrz.menu.SPFZ_MAct;
import com.dev.swapftrz.resource.SPFZO2DMethods;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZStageImagePack;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.List;

class SPFZStageHUD extends SPFZO2DMethods
{
   SPFZStageImagePack stageImagePack = new SPFZStageImagePack();
   SPFZResourceManager resManager;
   SPFZStageO2DObjects stage_o2d;
   ItemWrapper stageWrapper;
   private List<LabelComponent> charnames = new ArrayList<LabelComponent>();
   private Texture leftHealth,
     leftHealthOutline,
     leftSpecialMeter,
     leftSpecialMeterOutline,
     leftSpecialMeterDots,
     rightHealth,
     rightHealthOutline,
     rightSpecialMeter,
     rightSpecialMeterOutline,
     rightSpecialMeterDots;

   public SPFZStageHUD(SPFZResourceManager resManager) {
      this.resManager = resManager;
      stageWrapper = resManager.rootWrapper();
      stage_o2d = new SPFZStageO2DObjects();
   }

   public void createStageHUDTextures() {
      leftHealth = stageImagePack.getHealthTexture();
      rightHealth = stageImagePack.getHealthTexture();
      leftHealthOutline = stageImagePack.getHealthOutlineTexture();
      rightHealthOutline = stageImagePack.getHealthOutlineTexture();
      leftSpecialMeter = stageImagePack.getSpecialMeterTexture();
      rightSpecialMeter = stageImagePack.getSpecialMeterTexture();
      leftSpecialMeterOutline = stageImagePack.getSpecialMeterOutlineTexture();
      rightSpecialMeterOutline = stageImagePack.getSpecialMeterOutlineTexture();
      leftSpecialMeterDots = stageImagePack.getSpecialMeterDotsTexture();
      rightSpecialMeterDots = stageImagePack.getSpecialMeterDotsTexture();
   }

   public void setTimerTexture(boolean trainingMode) {
      if (trainingMode)
      {
         stageWrapper.getChild("ctrlandhud").getChild("time").getComponent(TintComponent.class).color.a = 0f;
         stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("tenths")
           .getComponent(TintComponent.class).color.a = 0f;
         stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("ones")
           .getComponent(TintComponent.class).color.a = 0f;
         stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("tenths")
           .getComponent(TintComponent.class).color.a = 0f;
         stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("ones")
           .getComponent(SpriteAnimationStateComponent.class).paused = true;
         stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("tenths")
           .getComponent(SpriteAnimationStateComponent.class).paused = true;
      }
      else
      {
         stageWrapper.getChild("ctrlandhud").getChild("infinite").getComponent(TintComponent.class).color.a = 0f;
      }
   }

   public void resetHUDTimer(float roundTime) {
      // New timer
      Entity tenths = stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("tenths").getEntity();
      Entity ones = stageWrapper.getChild("ctrlandhud").getChild("timeranim").getChild("ones").getEntity();

      tenths.getComponent(SpriteAnimationStateComponent.class)
        .set(new FrameRange("tens", ((int) roundTime % 100) / 10, ((int) roundTime % 100) / 10), 1, Animation.PlayMode.NORMAL);
      ones.getComponent(SpriteAnimationStateComponent.class)
        .set(new FrameRange("ones" + roundTime + "", (int) roundTime % 10, (int) roundTime % 10), 1, Animation.PlayMode.NORMAL);
      ones.getComponent(SpriteAnimationStateComponent.class).paused = true;
      tenths.getComponent(SpriteAnimationStateComponent.class).paused = true;
   }

   public void setHUDCharacterNames(List<String> characters) {
      for (int i = 0; i < characters.size(); i++)
      {
         switch (i)
         {
            case 0:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("charonetxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));
               break;
            case 1:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("chartwotxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));

               break;
            case 2:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("charthreetxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));

               break;
            case 3:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("charfourtxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));

               break;
            case 4:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("charfivetxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));

               break;
            case 5:

               charnames.add(i,
                 stageWrapper.getChild("ctrlandhud").getChild("charsixtxt").getEntity().getComponent(LabelComponent.class));
               charnames.get(i).setText(characters.get(i));

               break;

            default:
               break;
         }
      }
   }

   public void preFightFade() {
      Runnable preFightFadeRunnable;
      Entity fader = stageWrapper.getChild(stage_o2d.fader()).getEntity();

      preFightFadeRunnable = () -> {
         Actions.addAction(fader, Actions.sequence(delayO2dObject(1f), fadeOutO2dObject(.3f, null), Actions.run(() -> {
            roundtextset();
            prefighttimer();
         })));
      };

      preFightFadeRunnable.run();
   }

   private void roundtextset() {
      Entity roundtext = stageWrapper.getChild(stage_o2d.controllerHUD()).getChild(stage_o2d.roundtext()).getEntity();
      Entity roundimg = stageWrapper.getChild(stage_o2d.controllerHUD()).getChild(stage_o2d.roundimg()).getEntity();
      //TODO need to come back and finish this runnable
      Runnable roundImageRunnable = () -> {
         Actions.addAction(roundimg, scaleO2dObj(StageValues.roundimagescale(), StageValues.roundimagescale(),
           1f, StageValues.roundimageduration(), Interpolation.elastic));
      };

      Actions.addAction(roundimg, Actions.scaleTo(1f, 0f, .3f, Interpolation.elastic));

      if (finishedrd && gameover)
      {
         if (p1rdcount == 2 && p2rdcount != 2)
         {
            if (access.isArcade)
            {
               roundtext.getComponent(LabelComponent.class).setText("YOU WIN");
            }
            else
            {
               roundtext.getComponent(LabelComponent.class).setText("PLAYER ONE WINS");
            }
         }
         else if (p2rdcount == 2 && p1rdcount != 2)
         {
            if (access.isArcade)
            {
               roundtext.getComponent(LabelComponent.class).setText("YOU LOSE");
            }
            else
            {
               roundtext.getComponent(LabelComponent.class).setText("PLAYER TWO WINS");
            }
         }
         else if (p1rdcount == 2 && p2rdcount == 2)
         {
            if (access.isArcade)
            {
               roundtext.getComponent(LabelComponent.class).setText("DRAW GAME\n YOU LOSE");
            }
            else
            {
               roundtext.getComponent(LabelComponent.class).setText("DRAW GAME");
            }
         }

         Actions.addAction(roundtext, Actions.sequence(Actions.fadeIn(3f), Actions.run(new Runnable()
         {

            @Override
            public void run() {
               access.paused = false;
               // access.getscreenshot();
               access.pause();

            }
         })));
      }

      else
      {
         Actions.addAction(roundtext,
           Actions.sequence(Actions.fadeIn(1.5f), Actions.fadeOut(.3f), Actions.run(new Runnable()
           {

              @Override
              public void run() {
                 // Entity roundtext =
                 // stageItemWrapper.getChild("ctrlandhud").getChild("roundtext").getEntity();;

                 // Entity fightimg =
                 // stageItemWrapper.getChild("ctrlandhud").getChild("fightimg").getEntity();

                 // Actions.addAction(fightimg,
                 // Actions.sequence(Actions.scaleTo(1f, 1f, .3f,
                 // Interpolation.elastic), Actions.delay(.5f), Actions.scaleTo(1f,
                 // 0f, .4f, Interpolation.elastic)));

                 // roundtext.getComponent(LabelComponent.class).setText("FIGHT");
                 // Actions.addAction(roundtext, Actions.fadeIn(.8f));

              }

           })));
         Actions.addAction(roundimg, Actions.sequence(Actions.scaleTo(1f, 1f, .4f, Interpolation.elastic),
           Actions.delay(1f), Actions.scaleTo(1f, 0f, .4f, Interpolation.elastic), Actions.run(new Runnable()
           {

              @Override
              public void run() {
                 // Entity roundtext =
                 // stageItemWrapper.getChild("ctrlandhud").getChild("roundtext").getEntity();;

                 Entity fightimg = stageWrapper.getChild("ctrlandhud").getChild("fightimg").getEntity();

                 Actions.addAction(fightimg, Actions.sequence(Actions.scaleTo(1f, 1f, .3f, Interpolation.elastic),
                   Actions.delay(.5f), Actions.scaleTo(1f, 0f, .4f, Interpolation.elastic)));

                 // roundtext.getComponent(LabelComponent.class).setText("FIGHT");
                 // Actions.addAction(roundtext, Actions.fadeIn(.8f));

              }

           })));
      }
   }

   private void prefighttimer() {

      // This method contains the actions to perform at the beginning of each
      // round
      Timer.schedule(new Timer.Task()
      {

         @Override
         public void run() {
            Entity pausebtn = stageWrapper.getChild("ctrlandhud").getChild("pausebutton").getEntity();
            Entity roundtext = stageWrapper.getChild("ctrlandhud").getChild("roundtext").getEntity();

            standby = false;
            initcheck = true;
            // check = 500;

            pausebtn.getComponent(
              TransformComponent.class).originY = pausebtn.getComponent(DimensionsComponent.class).height * .5f;
            Actions.addAction(pausebtn, Actions.scaleTo(1f, 1f, .3f, Interpolation.circle));

            timeleft = roundTime;
            time = System.currentTimeMillis();
            Actions.addAction(roundtext, Actions.fadeOut(.3f));

         }
      }, 3);
   }

}
