package com.dev.swapftrz.fyter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class SPFZFyterCollision {
  private final ItemWrapper stageWrapper;
  private SPFZPlayer spfzPlayer, opponent;
  private List<ArrayList<Double>> characterData;
  private int[] activeframes;
  private final int ACTSTARTBOX = 4;
  private final int ACTENDBOX = 5;
  private final int BOXX = 6;
  private final int BOXY = 7;
  private final int BOXWIDTH = 8;
  private final int BOXHEIGHT = 9;

  private boolean showCollisionBoxes;

  public SPFZFyterCollision(SPFZPlayer spfzPlayer, ItemWrapper stageWrapper, List<ArrayList<Double>> characterData,
                            List<HashMap<String, int[]>> animations, List<ArrayList<String>> specials) {
    this.stageWrapper = stageWrapper;
    this.spfzPlayer = spfzPlayer;
    this.characterData = characterData;
    opponent = spfzPlayer.opponent();
  }

  public void processCollisionBoxes(int i) {

    setCollisionBoxes(i);
    //boxes = Gdx.input.isKeyJustPressed(Input.Keys.B);
    if (showCollisionBoxes)
      showcollisionboxes(i);

    // Collision boxes that keep the players from crossing through each
    // other

    if (spfzPlayer.setrect().overlaps(opponent.setrect())) {
      // needs to be just a variable stating that the objects are within close
      // range so we
      // will need to alter the speeds

    }

    // Collision boxes that will process the health as well as other
    // functionalities
    // when dealing with hitboxes
    if (stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
      if (stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete()) {
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).x = 0;
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).y = -20f;

        stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects
          .removeValue(stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
            .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);

      }
    }
    if (stageWrapper.getChild("p2hit").getChild("p2confirm").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
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
    if (spfzPlayer.isAttacking() || spfzPlayer.projact || opponent.isAttacking()) {
      hitboxprocessing();
    }

  }

  /**
   * set the hitConfirm location in between the center of
   * the attacker's hitbox and the attacked's hurtbox
   *
   * @param hitConfirm
   */
  public void setHitConfirmLocation(Vector2 hitConfirm, boolean isSpfzPlayer) {
    Vector2 newHitConfirmLocation = new Vector2(), newHurtBoxLocation = new Vector2();

    if (isSpfzPlayer) {
      spfzPlayer.hitBox().getCenter(newHitConfirmLocation);
      opponent.hurtBox().getCenter(newHurtBoxLocation);
    }
    else {
      opponent.hitBox().getCenter(newHitConfirmLocation);
      spfzPlayer.hurtBox().getCenter(newHurtBoxLocation);
    }

    hitConfirm.x = (newHitConfirmLocation.x + newHurtBoxLocation.x) * .5f;
    hitConfirm.y = (newHitConfirmLocation.y + newHurtBoxLocation.y) * .5f;
  }

  public void hitboxprocessing() {
    Vector2 hitconfirm = new Vector2();

    // If spfzPlayer one attacked opponent
    if (spfzPlayer.hitBox().overlaps(opponent.hurtBox())) {
      opponent.setAttackedStatus();
      spfzPlayer.hitboxconfirm(true);
      //spfzPlayer.wallb = false;
      /*if (spfzPlayer.bouncer) {
        //opponent.bounced = true;
        spfzPlayer.wallb = true;
        spfzPlayer.bouncer = false;
      }*/
      setHitConfirmLocation(hitconfirm, true);

      //TODO need to handle particle effects when attacks land
      /*//Set particle effects to appropriate scaling based on hitboxsize that the opponent was attacked by
      stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity()
        .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
      stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer.hitboxsize.y / 50f) * tempflip;
      stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).scaleY =
        (spfzPlayer.hitboxsize.y / 50f) * tempflip;


      stageWrapper.getChild("p1block").getChild("p1bconfirm").getEntity()
        .getComponent(SPFZParticleComponent.class).worldMultiplyer = tempflip;
      stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleX = (spfzPlayer.hitboxsize.y / 50f) * tempflip;
      stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).scaleY = (spfzPlayer.hitboxsize.y / 50f) * tempflip;

      if (spfzPlayer.transformAttributes().y > spfzPlayer.charGROUND()) {
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;
        stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = -45 * tempflip;

      }
      else {
        stageWrapper.getChild("p1block").getEntity().getComponent(TransformComponent.class).rotation = 0;
        stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).rotation = 0;
      }

      // Set the positioning of the particle effects and handle hit events
      if (opponent.isAttacked()) {
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
        else {
          stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).x = hitconfirm.x;

          stageWrapper.getChild("p1hit").getEntity().getComponent(TransformComponent.class).y = hitconfirm.y;
        }

        stageWrapper.getChild("p1hit").getChild("p1confirm").getEntity().getComponent(SPFZParticleComponent.class)
          .startEffect();
        // shake = true;*/

      //if (!damagedealt) {
      /*
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
        */
      //}
    }
    else if (opponent.hitBox().overlaps(spfzPlayer.hurtBox())) {

    }
  }

  public Rectangle createHitBox(int move, String animation) {
    float boxX, sizeW, sizeH,
      boxY = spfzPlayer.transformAttributes().y + characterData.get(BOXY).get(move).floatValue();

    if (spfzPlayer.isFacingRight())
      boxX = spfzPlayer.center() + characterData.get(BOXX).get(move).floatValue();
    else
      boxX = spfzPlayer.center() - characterData.get(BOXX).get(move).floatValue() - characterData.get(BOXWIDTH).get(move).floatValue();

    sizeW = characterData.get(BOXWIDTH).get(move).floatValue();
    sizeH = characterData.get(BOXHEIGHT).get(move).floatValue();

    activeframes[0] = characterData.get(ACTSTARTBOX).get(move).intValue()
      - Objects.requireNonNull(spfzPlayer.animation().animationComponent().frameRangeMap.get(spfzPlayer.currentAnimation())).startFrame;
    activeframes[1] = characterData.get(ACTENDBOX).get(move).intValue()
      - Objects.requireNonNull(spfzPlayer.animation().animationComponent().frameRangeMap.get(spfzPlayer.currentAnimation())).endFrame;
    ;

    return new Rectangle(boxX, boxY, sizeW, sizeH);
  }

  public void setCollisionBoxes(int i) {
    float collisionBox;
    // temp reach will be the box length incoming
    float tempreach = 10;

    // used to determine how to position hitbox when facing left or right
    if (spfzPlayer.isFacingRight()) {
      tempreach *= 1;
    }
    else {
    }

    setstrkbox(i);

    setcrossbox(i);

    setupHitbox(i, tempreach);

    //if (opponent.reflect)
    // opponent.setreflect();

  }

  public void setstrkbox(int i) {
    //hurtbox strikebox, etc.
  }

  public void shwstrkbox(int i) {
    // Show the character hit box

    spfzPlayer.setProjectionMatrix();
    spfzPlayer.getResource().getCurrentSSL().renderBox(spfzPlayer.drawcharbox(), ShapeRenderer.ShapeType.Line,
      Color.RED);
  }

  public void setcrossbox(int i) {
    // set character cross box
    spfzPlayer.setcross();
    spfzPlayer.dimrectangle();
    opponent.dimrectangle();
  }

  public void shwcrossbox(int i) {
    //opponent.spfzdimbox.setProjectionMatrix(stageCamera.combined);
    //opponent.spfzdimbox.begin(ShapeRenderer.ShapeType.Line);
    //opponent.spfzdimbox.setColor(Color.LIME);

    /*spfzp1move.dimrectangle();
    opponent.dimrectangle();*/
    spfzPlayer.spfzdimbox.rect(spfzPlayer.dimRect.x, spfzPlayer.dimRect.y, spfzPlayer.dimRect.width, spfzPlayer.dimRect.height);
    opponent.spfzdimbox.rect(opponent.dimRect.x, opponent.dimRect.y, opponent.dimRect.width, opponent.dimRect.height);

    spfzPlayer.spfzdimbox.end();
    opponent.spfzdimbox.end();
  }

  public void setupHitbox(int i, float tempreach) {
    // When setting boxes(position x, position y, width, height)
    if (spfzPlayer.isAttacking())
      if (spfzPlayer.animation().currentFrame() >= activeframes[0]
        && spfzPlayer.animation().currentFrame() <= activeframes[1])
        if (!spfzPlayer.isOnGround())
          spfzPlayer.setInairStatus();

  }

  public void shwhitbox(int i) {
    // Projectile Hitboxes
    //if (spfzPlayer.projectile != null)
    // spfzPlayer.projectile.hitbox();

    // Shows the hitbox for attacks.
    // if the character is attacking, and the current frame of animation is
    // the active frames based on the
    // activeframe array, draw the hitbox

    if (spfzPlayer.isAttacking()) {
      if (spfzPlayer.animation().currentFrame() >= activeframes[0]
        && spfzPlayer.animation().currentFrame() <= activeframes[1]) {
        //set positioning of hitbox prior to render setup of box
        spfzPlayer.setProjectionMatrix(); // questionable, need to test if necessary to do twice in 1 frame
        spfzPlayer.getResource().getCurrentSSL().renderBox(spfzPlayer.renderHitBox(), ShapeRenderer.ShapeType.Filled,
          Color.ORANGE);
      }
    }
  }

  public void showcollisionboxes(int i) {

    shwstrkbox(i);
    shwcrossbox(i);
    shwhitbox(i);

    // if (opponent.reflect)
    //  opponent.shwreflect();


    //COLLISION PROCESSING TO BE CALLED
  }

  public void toggleShowCollisionBoxes(boolean show) {
    showCollisionBoxes = show;
  }

  public int activeHitboxStartIndex() { return ACTSTARTBOX; }

  public int activeHitboxEndIndex() { return ACTENDBOX; }

  public int activeHitboxXIndex() { return BOXX; }

  public int activeHitboxYIndex() { return BOXY; }

  public int activeHitboxWidthIndex() { return BOXWIDTH; }

  public int activeHitboxHeightIndex() { return BOXHEIGHT; }
}
