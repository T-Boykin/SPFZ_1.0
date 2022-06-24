package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SPFZFyterCollision
{
   private final ItemWrapper stageWrapper;

   public SPFZFyterCollision(SPFZPlayer spfzPlayer, ItemWrapper stageWrapper, List<List<ArrayList<Double>>> characterData,
                             List<HashMap<String, int[]>> animations, List<ArrayList<String>> specials) {
      this.stageWrapper = stageWrapper;
   }

   public void collision(int i) {

      setcollisionboxes(i);
      //boxes = Gdx.input.isKeyJustPressed(Input.Keys.B);
      if (boxes)
      {
         showcollisionboxes(i);
      }

      // Collision boxes that keep the players from crossing through each
      // other
      close = false;

      if (spfzPlayer1.setrect().overlaps(spfzp2move.setrect()))
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
      if (spfzPlayer1.attacking() || spfzPlayer1.projact || spfzp2move.attacking())
      {
         hitboxprocessing();
      }

   }

   public void hitboxprocessing() {
      Vector2 hitconfirm = new Vector2();

      // If player one attacked player 2
      if (spfzPlayer1.sethitbox().overlaps(spfzp2move.setcharbox()) && spfzPlayer1.attacking && !spfzPlayer1.getboxconfirm()
        || spfzPlayer1.projconfirm())
      {
         if (spfzPlayer1.hitboxsize.x > 0 || spfzPlayer1.projconfirm())
         {
            spfzPlayer1.hitboxconfirm(true);
            spfzPlayer1.wallb = false;
            if (spfzPlayer1.bouncer)
            {
               spfzp2move.bounced = true;
               spfzPlayer1.wallb = true;
               spfzPlayer1.bouncer = false;
            }
            spfzPlayer1.sethitbox().getCenter(hitconfirm);

            hitconfirm.set((spfzp2move.setcharbox().x + spfzPlayer1.sethitbox().x + spfzPlayer1.sethitbox().width) * .5f,
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
            if (!spfzPlayer1.projconfirm())
            {
               pausechar();
            }
            checkstun(p2);

            float tempflip;
            if (spfzPlayer1.center() < spfzp2move.center())
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
            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer1.hitboxsize.y / 50f) * tempflip;
            stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleY =
              (spfzPlayer1.hitboxsize.y / 50f) * tempflip;


            stageWrapper.getChild("p1block").getChild("p1bconfirm").getEntity()
              .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer1.hitboxsize.y / 50f) * tempflip;
            stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleY = (spfzPlayer1.hitboxsize.y / 50f) * tempflip;

            if (spfzPlayer1.attributes().y > spfzPlayer1.charGROUND())
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
               if (spfzPlayer1.projconfirm())
               {
                  if (spfzPlayer1.projectile.spfzattribute.scaleX > 0)
                  {
                     stageWrapper.getChild("p1hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       + spfzPlayer1.projectile.spfzdim.width;
                  }
                  else
                  {
                     stageWrapper.getChild("p1hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       - spfzPlayer1.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p1hit").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer1.projectile.spfzattribute.y
                    + spfzPlayer1.projectile.spfzdim.height * .5f;
                  spfzPlayer1.projectile.hit = false;
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
                  else if (spfzp2move.combonum() == 1 && Hit.getComponent(TransformComponent.class).scaleY >= 0 &&
                    cc.getComponent(TransformComponent.class).scaleY >= 0)
                  {
                     Actions.removeActions(Hit);
                     Actions.removeActions(cc);
                     Actions.addAction(Hit, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
                     Actions.addAction(cc, Actions.scaleBy(0, 0, .01f, Interpolation.elastic));
                  }
                  //else if(spfzp2move.combonum() == 1)

                  if (spfzPlayer1.input == -1)
                  {
                     p2health -= 200f;
                     p1spec += 120f;
                     p2spec += 120f;

                  }
                  else
                  {
                     p2health -= player1data.get(p1).get(spfzPlayer1.HITDMG).get(spfzPlayer1.move).intValue();
                     p1spec += player1data.get(p1).get(spfzPlayer1.HITMTR).get(spfzPlayer1.move).intValue();
                     p2spec += player1data.get(p1).get(spfzPlayer1.HITMTR).get(spfzPlayer1.move).intValue() / 2;
                  }
                  damagedealt = true;
               }
            }
            else
            {
               if (spfzPlayer1.projconfirm())
               {
                  if (spfzPlayer1.projectile.spfzattribute.scaleX > 0)
                  {
                     stageWrapper.getChild("p1block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       + spfzPlayer1.projectile.spfzdim.width;
                  }
                  else
                  {
                     stageWrapper.getChild("p1block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       - spfzPlayer1.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p1block").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer1.projectile.spfzattribute.y
                    + spfzPlayer1.projectile.spfzdim.height * .5f;
                  spfzPlayer1.projectile.hit = false;
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
      else if ((spfzp2move.sethitbox().overlaps(spfzPlayer1.setcharbox()) && spfzp2move.attacking()
        && !spfzp2move.getboxconfirm()) || spfzp2move.projhit)
      {
         // spfzp2move.hit();
         if (spfzp2move.hitboxsize.x > 0 || spfzp2move.projhit)
         {
            spfzp2move.hitboxconfirm(true);
            spfzp2move.sethitbox().getCenter(hitconfirm);

            hitconfirm.set((spfzPlayer1.setcharbox().x + spfzp2move.sethitbox().x + spfzp2move.sethitbox().width) * .5f,
              hitconfirm.y);


            if (spfzPlayer1.hit())
            {
               spfzPlayer1.attacked = true;
               spfzPlayer1.blocking = false;
            }
            else
            {
               //spfzp1move.attacked = true;
               spfzPlayer1.blocking = true;

            }

            if (spfzp2move.projhit)
            {
               spfzp2move.projhit = false;

            }

            float tempflip;
            if (spfzp2move.center() < spfzPlayer1.center())
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
            if (spfzPlayer1.attacked)
            {
               if (spfzPlayer1.projconfirm())
               {
                  if (spfzPlayer1.projectile.spfzattribute.scaleX > 0)
                  {
                     stageWrapper.getChild("p2hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       + spfzPlayer1.projectile.spfzdim.width;
                  }
                  else
                  {
                     stageWrapper.getChild("p2hit").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       - spfzPlayer1.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p2hit").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer1.projectile.spfzattribute.y
                    + spfzPlayer1.projectile.spfzdim.height * .5f;
                  spfzPlayer1.projectile.hit = false;
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
               if (spfzPlayer1.projconfirm())
               {
                  if (spfzPlayer1.projectile.spfzattribute.scaleX > 0)
                  {
                     stageWrapper.getChild("p2block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       + spfzPlayer1.projectile.spfzdim.width;
                  }
                  else
                  {
                     stageWrapper.getChild("p2block").getEntity()
                       .getComponent(TransformComponent.class).x = spfzPlayer1.projectile.spfzattribute.x
                       - spfzPlayer1.projectile.spfzdim.width;
                  }

                  stageWrapper.getChild("p2block").getEntity()
                    .getComponent(TransformComponent.class).y = spfzPlayer1.projectile.spfzattribute.y
                    + spfzPlayer1.projectile.spfzdim.height * .5f;
                  spfzPlayer1.projectile.hit = false;
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

   public void setcollisionboxes(int i) {
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
      spfzPlayer1.dimrectangle();
      spfzp2move.dimrectangle();

   }

   public void shwcrossbox(int i) {
      // Shows the cross box that keeps characters from crossing each other

      ((Attribs) arrscripts.get(i)).drawrect().setProjectionMatrix(stageCamera.combined);
      ((Attribs) arrscripts.get(i)).drawrect().begin(ShapeRenderer.ShapeType.Line);

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

      spfzPlayer1.spfzdimbox.setProjectionMatrix(stageCamera.combined);
      spfzPlayer1.spfzdimbox.begin(ShapeRenderer.ShapeType.Line);
      spfzPlayer1.spfzdimbox.setColor(Color.LIME);
      spfzp2move.spfzdimbox.setProjectionMatrix(stageCamera.combined);
      spfzp2move.spfzdimbox.begin(ShapeRenderer.ShapeType.Line);
      spfzp2move.spfzdimbox.setColor(Color.LIME);

    /*spfzp1move.dimrectangle();
    spfzp2move.dimrectangle();*/
      spfzPlayer1.spfzdimbox.rect(spfzPlayer1.dimrect.x, spfzPlayer1.dimrect.y, spfzPlayer1.dimrect.width, spfzPlayer1.dimrect.height);
      spfzp2move.spfzdimbox.rect(spfzp2move.dimrect.x, spfzp2move.dimrect.y, spfzp2move.dimrect.width, spfzp2move.dimrect.height);

      spfzPlayer1.spfzdimbox.end();
      spfzp2move.spfzdimbox.end();
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
               spfzPlayer1.inair = true;
            }

            ((Attribs) arrscripts.get(i)).sethitbox();
         }
         else if (((Attribs) arrscripts.get(i)).currentframe() > ((Attribs) arrscripts.get(i)).activeframes()[1])
         {
            if (i == p1)
            {
               spfzPlayer1.inair = false;
            }
            ((Attribs) arrscripts.get(i)).hitboxpos().setZero();
            ((Attribs) arrscripts.get(i)).hitboxsize().setZero();
            ((Attribs) arrscripts.get(i)).hitboxconfirm(false);
         }
      }

   }

   public void shwhitbox(int i) {
      // Projectile Hitboxes
      if (spfzPlayer1.projectile != null)
      {
         spfzPlayer1.projectile.hitbox();
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

      if (spfzp2move.reflect)
      {
         spfzp2move.shwreflect();
      }

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
   }
}
