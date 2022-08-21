package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SPFZFyterCollision {
   private final ItemWrapper stageWrapper;
   private SPFZPlayer spfzPlayer, opponent;
   private List<ArrayList<Double>> characterData;
   final int ACTSTARTBOX = 4;
   final int ACTENDBOX = 5;
   final int BOXX = 6;
   final int BOXY = 7;
   final int BOXWIDTH = 8;
   final int BOXHEIGHT = 9;

   public SPFZFyterCollision(SPFZPlayer spfzPlayer, ItemWrapper stageWrapper, List<ArrayList<Double>> characterData,
                             List<HashMap<String, int[]>> animations, List<ArrayList<String>> specials) {
      this.stageWrapper = stageWrapper;
      this.spfzPlayer = spfzPlayer;
      this.characterData = characterData;
      opponent = spfzPlayer.opponent();
   }


   public void processCollisionBoxes(int i) {

      setcollisionboxes(i);
      //boxes = Gdx.input.isKeyJustPressed(Input.Keys.B);
      if (boxes) {
         showcollisionboxes(i);
      }

      // Collision boxes that keep the players from crossing through each
      // other
      close = false;

      if (spfzPlayer.setrect().overlaps(opponent.setrect())) {
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
           .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete()) {
            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).x = 0;
            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).y = -20f;
            stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
              .getComponent(SPFZParticleComponent.class).pooledeffects
              .removeValue(stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
                .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);

         }
      }
      if (spfzPlayer.attacking() || spfzPlayer.projact || opponent.attacking()) {
         hitboxprocessing();
      }

   }

   public void hitboxprocessing() {
      Vector2 hitconfirm = new Vector2();

      // If player one attacked player 2
      if (spfzPlayer.sethitbox().overlaps(opponent.setcharbox()) && spfzPlayer.attacking && !spfzPlayer.getboxconfirm()
        || spfzPlayer.projconfirm()) {
         if (spfzPlayer.hitboxsize.x > 0 || spfzPlayer.projconfirm()) {
            spfzPlayer.hitboxconfirm(true);
            spfzPlayer.wallb = false;
            if (spfzPlayer.bouncer) {
               opponent.bounced = true;
               spfzPlayer.wallb = true;
               spfzPlayer.bouncer = false;
            }
            spfzPlayer.sethitbox().getCenter(hitconfirm);

            hitconfirm.set((opponent.setcharbox().x + spfzPlayer.sethitbox().x + spfzPlayer.sethitbox().width) * .5f,
              hitconfirm.y);

            //somehow extra call to hit() method is allowing the
            //pushback to work. Need to correct logic elsewhere
            opponent.hit();

            if (opponent.hit()) {
               opponent.attacked = true;
               opponent.blocking = false;
            }
            else {
               opponent.attacked = false;
               opponent.blocking = true;
            }
            if (!spfzPlayer.projconfirm()) {
               pausechar();
            }
            checkstun(p2);

            float tempflip;
            if (spfzPlayer.center() < opponent.center()) {
               tempflip = 1f;
            }
            else {
               tempflip = -1f;
            }

            //Set particle effects to appropriate scaling based on hitboxsize that the opponent was attacked by
            stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
              .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer.hitboxsize.y / 50f) * tempflip;
            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleY =
              (spfzPlayer.hitboxsize.y / 50f) * tempflip;


            stageWrapper.getChild("p1block").getChild("p1bconfirm").getEntity()
              .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer.hitboxsize.y / 50f) * tempflip;
            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleY = (spfzPlayer.hitboxsize.y / 50f) * tempflip;

            if (spfzPlayer.attributes().y > spfzPlayer.charGROUND()) {
               stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;
               stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;

            }
            else {
               stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = 0;
               stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = 0;
            }

            // Set the positioning of the particle effects and handle hit events
            if (opponent.attacked) {
               if (spfzPlayer.projconfirm()) {
                  if (spfzPlayer.projectile.spfzattribute.scaleX > 0) {
                     stageWrapper.getChild("p1hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       + spfzPlayer.projectile.spfzdim.width;
                  }
                  else {
                     stageWrapper.getChild("p1hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       - spfzPlayer.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p1hit").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer.projectile.spfzattribute.y
                    + spfzPlayer.projectile.spfzdim.height * .5f;
                  spfzPlayer.projectile.hit = false;
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
                  opponent.setcombonum(opponent.combonum() + 1);

                  // Hit.getComponent(TransformComponent.class).originY =
                  // Hit.getComponent(DimensionsComponent.class).height * .5f;
                  if (opponent.combonum() >= 2) {
                     if (opponent.combonum() == 2 && Hit.getComponent(TransformComponent.class).scaleY == 0
                       && cc.getComponent(TransformComponent.class).scaleY == 0) {

                        Actions.addAction(Hit, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));
                        Actions.addAction(cc, Actions.scaleBy(0, SCALE_TEXT, .6f, Interpolation.elastic));

                     }

                     Entity parent = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getEntity();
                     Entity p2cntTEN = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getChild("tenths").getEntity();
                     Entity p2cntONE = stageWrapper.getChild("ctrlandhud").getChild("p1cc").getChild("ones").getEntity();
                     LabelComponent combocount1;
                     combocount1 = stageWrapper.getChild("ctrlandhud").getChild("combocount1").getEntity()
                       .getComponent(LabelComponent.class);

                     // combocount1.setText(Integer.toString(opponent.combonum()) + "
                     // HITS");

                     combocounter(parent, p2cntTEN, p2cntONE, opponent.combonum());
                  }
                  else if (opponent.combonum() == 1 && Hit.getComponent(TransformComponent.class).scaleY >= 0 &&
                    cc.getComponent(TransformComponent.class).scaleY >= 0) {
                     Actions.removeActions(Hit);
                     Actions.removeActions(cc);
                     Actions.addAction(Hit, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
                     Actions.addAction(cc, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
                  }
                  //else if(opponent.combonum() == 1)

                  if (spfzPlayer.input == -1) {
                     p2health -= 200f;
                     p1spec += 120f;
                     p2spec += 120f;

                  }
                  else {
                     p2health -= player1data.get(p1).get(spfzPlayer.HITDMG).get(spfzPlayer.move).intValue();
                     p1spec += player1data.get(p1).get(spfzPlayer.HITMTR).get(spfzPlayer.move).intValue();
                     p2spec += player1data.get(p1).get(spfzPlayer.HITMTR).get(spfzPlayer.move).intValue() / 2;
                  }
                  damagedealt = true;
               }
            }
            else
            {
               if (spfzPlayer.projconfirm()) {
                  if (spfzPlayer.projectile.spfzattribute.scaleX > 0) {
                     stageWrapper.getChild("p1block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       + spfzPlayer.projectile.spfzdim.width;
                  }
                  else {
                     stageWrapper.getChild("p1block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       - spfzPlayer.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p1block").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer.projectile.spfzattribute.y
                    + spfzPlayer.projectile.spfzdim.height * .5f;
                  spfzPlayer.projectile.hit = false;
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
      else if ((opponent.sethitbox().overlaps(spfzPlayer.setcharbox()) && opponent.attacking()
        && !opponent.getboxconfirm()) || opponent.projhit) {
         // opponent.hit();
         if (opponent.hitboxsize.x > 0 || opponent.projhit) {
            opponent.hitboxconfirm(true);
            opponent.sethitbox().getCenter(hitconfirm);

            hitconfirm.set((spfzPlayer.setcharbox().x + opponent.sethitbox().x + opponent.sethitbox().width) * .5f,
              hitconfirm.y);


            if (spfzPlayer.hit()) {
               spfzPlayer.attacked = true;
               spfzPlayer.blocking = false;
            }
            else {
               //spfzp1move.attacked = true;
               spfzPlayer.blocking = true;

            }

            if (opponent.projhit) {
               opponent.projhit = false;

            }

            float tempflip;
            if (opponent.center() < spfzPlayer.center()) {
               tempflip = 1f;
            }
            else {
               tempflip = -1f;
            }
            //Set particle effects to appropriate scaling based on hitboxsize that the opponent was attacked by
            stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
              .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).scaleX = (opponent.hitboxsize.y / 100f) * tempflip;
            stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).scaleY =
              (opponent.hitboxsize.y / 100f) * tempflip;


            stageWrapper.getChild("p2block").getChild("p2bconfirm").getEntity()
              .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
            stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).scaleX = (opponent.hitboxsize.y / 100f) * tempflip;
            stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).scaleY =
              (opponent.hitboxsize.y / 100f) * tempflip;

            if (opponent.attributes().y > opponent.charGROUND()) {
               stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;
               stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;

            }
            else {
               stageWrapper.getChild("p2hit").getEntity().getComponent(TransformComponent.class).rotation = 0;
               stageWrapper.getChild("p2block").getEntity().getComponent(TransformComponent.class).rotation = 0;
            }

            // Set the positioning of the particle effects and handle hit events
            if (spfzPlayer.attacked) {
               if (spfzPlayer.projconfirm()) {
                  if (spfzPlayer.projectile.spfzattribute.scaleX > 0) {
                     stageWrapper.getChild("p2hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       + spfzPlayer.projectile.spfzdim.width;
                  }
                  else {
                     stageWrapper.getChild("p2hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       - spfzPlayer.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p2hit").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer.projectile.spfzattribute.y
                    + spfzPlayer.projectile.spfzdim.height * .5f;
                  spfzPlayer.projectile.hit = false;
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
                  opponent.setcombonum(opponent.combonum() + 1);

                  // Hit.getComponent(TransformComponent.class).originY =
                  // Hit.getComponent(DimensionsComponent.class).height * .5f;
                  if (opponent.combonum() >= 2) {
                     if (opponent.combonum() == 2 && Hit.getComponent(TransformComponent.class).scaleY == 0
                       && cc.getComponent(TransformComponent.class).scaleY == 0) {

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


                     combocounter(parent, p2cntTEN, p2cntONE, opponent.combonum());
                  }


                  if (opponent.move == -1) {
                     p1health -= 200f;
                     p2spec += 120f;
                     p2spec += 120f;

                  }
                  else {
                     //p1health -= player2data.get(p2).get(opponent.HITDMG).get(opponent.move).intValue();
                     //p2spec += player2data.get(p2).get(opponent.HITMTR).get(opponent.move).intValue();
                     //p1spec += player2data.get(p2).get(opponent.HITMTR).get(opponent.move).intValue() / 2;
                  }
                  damagedealt = true;
               }
            }
            else
            {
               if (spfzPlayer.projconfirm()) {
                  if (spfzPlayer.projectile.spfzattribute.scaleX > 0) {
                     stageWrapper.getChild("p2block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       + spfzPlayer.projectile.spfzdim.width;
                  }
                  else {
                     stageWrapper.getChild("p2block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer.projectile.spfzattribute.x
                       - spfzPlayer.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p2block").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer.projectile.spfzattribute.y
                    + spfzPlayer.projectile.spfzdim.height * .5f;
                  spfzPlayer.projectile.hit = false;
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

   public void createHitBox(int player, String animation) {
      posofhitbox.setZero();
      hitboxsize.setZero();
      float boxX;
      float boxY = spfzattribute.y + characterData.get(currentCharacter).get(BOXY).get(move).floatValue();
      float sizeW;
      float sizeH;

      if (spfzattribute.scaleX > 0) {
         boxX = this.center() + characterData.get(currentCharacter).get(BOXX).get(move).floatValue();
         sizeW = characterData.get(currentCharacter).get(BOXWIDTH).get(move).floatValue();
         sizeH = characterData.get(currentCharacter).get(BOXHEIGHT).get(move).floatValue();
      }
      else {
         boxX = this.center() - characterData.get(currentCharacter).get(BOXX).get(move).floatValue() - characterData.get(currentCharacter).get(BOXWIDTH).get(move).floatValue();
         sizeW = characterData.get(currentCharacter).get(BOXWIDTH).get(move).floatValue();
         sizeH = characterData.get(currentCharacter).get(BOXHEIGHT).get(move).floatValue();
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

      activeframes[0] = characterData.get(currentCharacter).get(ACTSTARTBOX).get(move).intValue() - spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame;
      activeframes[1] = characterData.get(currentCharacter).get(ACTENDBOX).get(move).intValue() - spfzanimation.frameRangeMap.get(spfzanimation.currentAnimation).startFrame;

      posofhitbox.x = boxX;
      posofhitbox.y = boxY + (spfzrect.y - boxY);
      hitboxsize.x = sizeW;
      hitboxsize.y = sizeH;


   }

   public void setcollisionboxes(int i) {
      float reversebox;
      // temp reach will be the box length incoming
      float tempreach = 10;

      // used to determine how to position hitbox when facing left or right
      if (((Attribs) arrscripts.get(i)).attributes().scaleX > 0) {
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

      if (opponent.reflect) {
         opponent.setreflect();
      }

   }

   public void setstrkbox(int i) {
      // set character full strike box

      if (!((Attribs) arrscripts.get(i)).invul())
      {
      /*((Attribs) arrscripts.get(i)).setcharbox().set(
        ((Attribs) arrscripts.get(i)).center() - ((Attribs) arrscripts.get(i)).setrect().width,

        ((Attribs) arrscripts.get(i)).attributes().y, ((Attribs) arrscripts.get(i)).setrect().width,
        ((Attribs) arrscripts.get(i)).setrect().height);*/
         ((Attribs) arrscripts.get(i)).setcharbox();
      }
      else
      {
         ((Attribs) arrscripts.get(i)).setcharbox().set(0, 0, 0, 0);
      }
   }

   public void shwstrkbox(int i) {
      // Show the character hit box

      ((Attribs) arrscripts.get(i)).drawcharbox().setProjectionMatrix(stageCamera.combined);
      ((Attribs) arrscripts.get(i)).drawcharbox().begin(ShapeRenderer.ShapeType.Line);

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

   public void setcrossbox(int i) {
      // set character cross box
      ((Attribs) arrscripts.get(i)).setcross();
      spfzPlayer.dimrectangle();
      opponent.dimrectangle();

   }

   public void shwcrossbox(int i) {
      // Shows the cross box that keeps characters from crossing each other

      ((Attribs) arrscripts.get(i)).drawrect().setProjectionMatrix(stageCamera.combined);
      ((Attribs) arrscripts.get(i)).drawrect().begin(ShapeRenderer.ShapeType.Line);

      if (i == p1)
      {
         ((Attribs) arrscripts.get(i)).drawrect().setColor(Color.WHITE);

      }

      else {
         ((Attribs) arrscripts.get(i)).drawrect().setColor(Color.WHITE);
      }

      ((Attribs) arrscripts.get(i)).drawrect().rect(((Attribs) arrscripts.get(i)).setcross().x,
        ((Attribs) arrscripts.get(i)).setcross().y, ((Attribs) arrscripts.get(i)).setcross().width,
        ((Attribs) arrscripts.get(i)).setcross().height);

      ((Attribs) arrscripts.get(i)).drawrect().end();

      spfzPlayer.spfzdimbox.setProjectionMatrix(stageCamera.combined);
      spfzPlayer.spfzdimbox.begin(ShapeRenderer.ShapeType.Line);
      spfzPlayer.spfzdimbox.setColor(Color.LIME);
      opponent.spfzdimbox.setProjectionMatrix(stageCamera.combined);
      opponent.spfzdimbox.begin(ShapeRenderer.ShapeType.Line);
      opponent.spfzdimbox.setColor(Color.LIME);

    /*spfzp1move.dimrectangle();
    opponent.dimrectangle();*/
      spfzPlayer.spfzdimbox.rect(spfzPlayer.dimrect.x, spfzPlayer.dimrect.y, spfzPlayer.dimrect.width, spfzPlayer.dimrect.height);
      opponent.spfzdimbox.rect(opponent.dimrect.x, opponent.dimrect.y, opponent.dimrect.width, opponent.dimrect.height);

      spfzPlayer.spfzdimbox.end();
      opponent.spfzdimbox.end();
   }

   public void sethitbox(int i, float reversebox, float tempreach) {
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
               spfzPlayer.inair = true;
            }

            ((Attribs) arrscripts.get(i)).sethitbox();
         }
         else if (((Attribs) arrscripts.get(i)).currentframe() > ((Attribs) arrscripts.get(i)).activeframes()[1])
         {
            if (i == p1)
            {
               spfzPlayer.inair = false;
            }
            ((Attribs) arrscripts.get(i)).hitboxpos().setZero();
            ((Attribs) arrscripts.get(i)).hitboxsize().setZero();
            ((Attribs) arrscripts.get(i)).hitboxconfirm(false);
         }
      }

   }

   public void shwhitbox(int i) {
      // Projectile Hitboxes
      if (spfzPlayer.projectile != null) {
         spfzPlayer.projectile.hitbox();
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

            ((Attribs) arrscripts.get(i)).drawhitbox().setProjectionMatrix(stageCamera.combined);
            ((Attribs) arrscripts.get(i)).drawhitbox().begin(ShapeRenderer.ShapeType.Filled);

            ((Attribs) arrscripts.get(i)).drawhitbox().setColor(Color.ORANGE);

            ((Attribs) arrscripts.get(i)).drawhitbox().rect(((Attribs) arrscripts.get(i)).sethitbox().x,
              ((Attribs) arrscripts.get(i)).sethitbox().y, ((Attribs) arrscripts.get(i)).sethitbox().width,
              ((Attribs) arrscripts.get(i)).sethitbox().height);

            ((Attribs) arrscripts.get(i)).drawhitbox().end();
         }
      }
   }

   public void showcollisionboxes(int i) {

      shwstrkbox(i);
      shwcrossbox(i);
      shwhitbox(i);

      if (opponent.reflect) {
         opponent.shwreflect();
      }

      if (stage.arrscripts != null) {
         for (int i = 0; i < stage.arrscripts.size(); i++) {
            if (i == stage.p1 || i == stage.p2) {
               stage.collision(i);
            }
         }
      }
   }

   public ShapeRenderer drawhitbox() { return spfzhitbox; }

   public ShapeRenderer drawrect() { return spfzsr; }

   public DimensionsComponent dimensions() {
      return spfzdim;
   }

   public Vector2 hitboxsize() { return hitboxsize; }

   public Vector2 hitboxpos() { return posofhitbox; }
}
