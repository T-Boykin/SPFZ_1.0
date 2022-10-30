package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.stage.SPFZStage;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SPFZFyterAnimation {
  private SPFZStage stage;
  private SPFZPlayer spfzPlayer, opponent;
  private List<ArrayList<Double>> characterData;
  private List<HashMap<String, int[]>> animations;
  private List<ArrayList<String>> specials;
  private String[] loopAnimations = { };
  private SpriteAnimationComponent spfzAnimation;
  private SpriteAnimationStateComponent spfzAnimationState;
  private String previousAnimation;
  private float stateTime, activeFrames;
  //private int currentFrame;
  //SPFZBuffer may need to be used here

  public SPFZFyterAnimation(SPFZPlayer spfzPlayer, SPFZStage stage, List<ArrayList<Double>> characterData,
                            List<HashMap<String, int[]>> animations, List<ArrayList<String>> specials) {
    this.stage = stage;
    this.spfzPlayer = spfzPlayer;
    this.opponent = spfzPlayer.opponent();
    this.characterData = characterData;
    this.animations = animations;
    this.specials = specials;
  }

  public void processAnimation() {

  }

  public void characterAnimationSetup(Entity character) {
    NodeComponent characterNodeComponent = ComponentRetriever.get(character, NodeComponent.class);
    spfzAnimation = ComponentRetriever.get(characterNodeComponent.children.get(0), SpriteAnimationComponent.class);
    spfzAnimationState = ComponentRetriever.get(characterNodeComponent.children.get(0),
      SpriteAnimationStateComponent.class);
    setAnimationFrameRanges();
  }

  public void setAnimationFrameRanges() {
    for (int i = 0; i < animations.size() - 1; i++) {

    }
  }

  public void animation() {
    if (!previousAnimation.equals(spfzAnimation.currentAnimation))
      if (isLoopAnimation())
        spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation), spfzAnimation.fps,
          Animation.PlayMode.LOOP);
      else
        spfzAnimationState.set(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation), spfzAnimation.fps,
          Animation.PlayMode.NORMAL);
    //stateTime needs to be reset if it is a loopAnimation
    previousAnimation = spfzAnimation.currentAnimation;


    //logic may need to be pushed to the SPFZFyterCollision class
    //if (!attacking && !hitboxsize.isZero()) {
    //  hitboxsize.setZero();
    //}

    if (isAnimationCompleted() && isBeingAttacked()) {
      //attacking = false ?
    }
    if (spfzAnimationState.currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL && !spfzAnimationState.paused) {
      stateTime += Gdx.graphics.getDeltaTime();
    }

  }

  public void checkstun(int player) {
    float fd;
    if (((Attribs) arrscripts.get(player)).attacked()) {
      anim = "stun";
    }
    else {
      if (((Attribs) arrscripts.get(player)).attributes().y == GROUND) {
        anim = "block";
        if (opponent.isDown) {
          anim = "dblock";
        }
      }
      else {
        anim = "ablock";
      }
    }
    ((Attribs) arrscripts.get(player)).animationstate().set(
      ((Attribs) arrscripts.get(player)).animationcomponent().frameRangeMap.get(anim), 60, Animation.PlayMode.NORMAL);
  }

  public void checkneutral() {

    if (spfzPlayer.center() > opponent.center()
      || spfzPlayer.setrect().x + spfzPlayer.setrect().width > opponent.center()) {
      if (spfzPlayer.setrect().x + (spfzPlayer.setrect().width * .5f) > opponent.setrect().x
        + (opponent.setrect().width * .5f)) {
        spfzPlayer.attributes().x += 1.2f;
        opponent.attributes().x -= 1.2f;
      }
      else {
        spfzPlayer.attributes().x -= 1.2f;
        opponent.attributes().x += 1.2f;
      }
    }

    if (opponent.center() > spfzPlayer.center()
      || opponent.setrect().x + opponent.setrect().width > spfzPlayer.center()) {
      if (opponent.setrect().x + (opponent.setrect().width * .5f) > spfzPlayer.setrect().x
        + (spfzPlayer.setrect().width * .5f)) {
        opponent.attributes().x += 1.2f;
        spfzPlayer.attributes().x -= 1.2f;
      }
      else {
        opponent.attributes().x -= 1.2f;
        spfzPlayer.attributes().x += 1.2f;
      }
    }

  }

  public void attackMove() {
    //set the durations of the 3 stages of movement as well as the amount to push or pull the character.
    float strtupmov = 0;
    float actmov = 0;
    float recovmov = 0;

    float firstdur = rtnFrametime(characterData.get(currentCharacter).get(ACTSTARTBOX).get(move).floatValue() - (float) spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation).startFrame);
    float seconddur = rtnFrametime(characterData.get(currentCharacter).get(ACTENDBOX).get(move).floatValue() - characterData.get(currentCharacter).get(ACTSTARTBOX).get(move).floatValue());
    float thirddur = rtnFrametime(spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation).endFrame - characterData.get(currentCharacter).get(ACTENDBOX).get(move).floatValue());

    if (characterData.get(currentCharacter).get(BACK_START).get(move).doubleValue() == 1) {
      strtupmov = characterData.get(currentCharacter).get(BMOVE).get(move).floatValue();

      if (spfzPlayer.isFacingRight()) {
        strtupmov *= -1f;
      }

    }
    if (characterData.get(currentCharacter).get(FWD_START).get(move).doubleValue() == 1) {
      strtupmov = characterData.get(currentCharacter).get(FMOVE).get(move).floatValue();

      if (spfzattribute.scaleX < 0) {
        strtupmov *= -1f;
      }
    }

    if (characterData.get(currentCharacter).get(BACK_START).get(move).doubleValue() == 0 &&
      characterData.get(currentCharacter).get(FWD_START).get(move).doubleValue() == 0) {
      strtupmov = 0;
      //firstdur = 0;
    }

    if (characterData.get(currentCharacter).get(BACK_ACTIVE).get(move).doubleValue() == 1) {
      actmov = characterData.get(currentCharacter).get(BMOVE).get(move).floatValue();

      if (spfzPlayer.isFacingRight()) {
        actmov *= -1f;
      }
    }
    if (characterData.get(currentCharacter).get(FWD_ACTIVE).get(move).doubleValue() == 1) {
      actmov = characterData.get(currentCharacter).get(FMOVE).get(move).floatValue();

      if (spfzattribute.scaleX < 0) {
        actmov *= -1f;
      }
    }

    if (characterData.get(currentCharacter).get(BACK_ACTIVE).get(move).doubleValue() == 0 &&
      characterData.get(currentCharacter).get(FWD_ACTIVE).get(move).doubleValue() == 0) {
      actmov = 0;
      //seconddur = 0;
    }

    if (characterData.get(currentCharacter).get(BACK_RECOV).get(move).doubleValue() == 1) {
      recovmov = characterData.get(currentCharacter).get(BMOVE).get(move).floatValue();

      if (spfzPlayer.isFacingRight()) {
        recovmov *= -1f;
      }
    }
    if (characterData.get(currentCharacter).get(FWD_RECOV).get(move).doubleValue() == 1) {
      recovmov = characterData.get(currentCharacter).get(FMOVE).get(move).floatValue();

      if (spfzattribute.scaleX < 0) {
        recovmov *= -1f;
      }
    }

    if (characterData.get(currentCharacter).get(BACK_RECOV).get(move).doubleValue() == 0 &&
      characterData.get(currentCharacter).get(FWD_RECOV).get(move).doubleValue() == 0) {
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

  public void controlcrossing() {

    // /////////////////////////////////////////PLAYER ONE ON THE RIGHT
    // SIDE //////////////////////////////////////////////////////
    // /////////////////////////////////////////PLAYER TWO ON THE LEFT
    // SIDE //////////////////////////////////////////////////////

    // check to see if the players have both stopped moving that way they can be
    // readjusted
    for (int j = 0; j < 2; j++) {
      if (spfzPlayer.p1movement.get(j) || opponent.p2movement.get(j)) {
        neutral = false;
      }
    }
    // leave as is for now. was neutral
    if (neutral || (spfzPlayer.attributes().y > spfzPlayer.charGROUND() || opponent.attributes().y > opponent.charGROUND())) {
      checkneutral();
    }

    if (!spfzPlayer.dash) {
      if (p1xattr > p2xattr) {

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // LEFT //////////////////////////////////////////////////////

        if (spfzPlayer.p1movement.get(0)) {
          P1SRightMLeft();
        }

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // RIGHT //////////////////////////////////////////////////////

        else if (spfzPlayer.p1movement.get(1)) {
          P1SRightMRight();
        }
      }

      // /////////////////////////////////////////PLAYER ONE ON THE LEFT
      // SIDE //////////////////////////////////////////////////////
      // /////////////////////////////////////////PLAYER TWO ON THE RIGHT
      // SIDE /////////////////////////////////////////////////////

      else {

        // ///////////////////////////////// PLAYER ONE MOVING TO THE RIGHT
        // //////////////////////////////////////////////////////
        if (spfzPlayer.p1movement.get(1)) {
          P1SLeftMRight();
        }

        // ///////////////////////////////// PLAYER ONE MOVING TO THE LEFT
        // //////////////////////////////////////////////////////

        else if (spfzPlayer.p1movement.get(0)) {
          P1SLeftMLeft();
        }
      }
    }
    else {
      if (spfzPlayer.dash) {
        if (spfzPlayer.dashdir == 0) {
          if (stageCamera.position.x <= camboundary[0] + 1 && opponent.attributes().x <= stageboundary[0]) {
            spfzPlayer.attributes().x += 0f;
            opponent.attributes().x += 0f;
          }
          else {
            opponent.attributes().x -= (spfzPlayer.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzPlayer.attributes().x -= (spfzPlayer.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
        else {
          if (stageCamera.position.x + 1 >= camboundary[1] && opponent.attributes().x + 1 >= stageboundary[1]) {
            spfzPlayer.attributes().x += 0f;
            opponent.attributes().x += 0f;
          }
          else {
            opponent.attributes().x += (spfzPlayer.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzPlayer.attributes().x += (spfzPlayer.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
      }
    }

  }

  public void setface() {

    if (spfzPlayer.center() < opponent.center()) {
      if (spfzPlayer.attributes().scaleX < 0 && !faceright1) {
        faceright1 = true;
      }
      if (opponent.attributes().scaleX > 0 && !faceleft2) {
        faceleft2 = true;
      }
    }

    if (spfzPlayer.center() > opponent.center()) {
      if (spfzPlayer.attributes().scaleX > 0 && !faceleft1 && spfzPlayer.setrect().y <= GROUND) {
        faceleft1 = true;
      }
      if (opponent.attributes().scaleX < 0 && !faceright2 && opponent.setrect().y <= GROUND) {
        faceright2 = true;
      }
    }

    setfacingp1();
    setfacingp2();

  }

  public void setfacingp1() {


    if (faceright1) {
      faceleft1 = false;
      if (spfzPlayer.attributes().y <= spfzPlayer.charGROUND() && spfzPlayer.attributes().scaleX < 0) {

        spfzPlayer.attributes().scaleX *= -1f;
        spfzPlayer.attributes().x -= spfzPlayer.dimRect.width - (spfzPlayer.adjustX + (spfzPlayer.setrect().width * .5f));
        faceright1 = false;

      }
    }
    if (faceleft1) {
      faceright1 = false;
      if (spfzPlayer.attributes().y <= spfzPlayer.charGROUND() && spfzPlayer.attributes().scaleX > 0) {
        spfzPlayer.attributes().scaleX *= -1f;

        spfzPlayer.attributes().x += spfzPlayer.dimRect.width - (opponent.adjustX + (opponent.setrect().width * .5f));
        faceleft1 = false;
      }
    }
  }

  public void setfacingp2() {
    if (faceright2) {
      if (opponent.attributes().y <= opponent.charGROUND() && opponent.attributes().scaleX < 0) {
        opponent.attributes().scaleX *= -1f;
        opponent.attributes().x -= opponent.dimRect.width - (opponent.adjustX + (opponent.setrect().width * .5f));
        faceright2 = false;
      }
    }
    if (faceleft2) {
      if (opponent.attributes().y <= opponent.charGROUND() && opponent.attributes().scaleX > 0) {
        opponent.attributes().scaleX *= -1f;
        opponent.attributes().x += opponent.dimRect.width - (opponent.adjustX + (opponent.setrect().width * .5f));
        faceleft2 = false;
      }
    }
  }

  public void P1SRightMLeft() {
    // player 2 is moving left
    if (opponent.p2movement.get(0)) {

      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        opponent.attributes().x -= spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

      else if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {

        opponent.attributes().x -= opponent.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

    }
    // player 2 is moving right
    else if (opponent.p2movement.get(1)) {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        opponent.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {
        opponent.attributes().x += (opponent.moveandjump().x - spfzPlayer.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x += (opponent.moveandjump().x - spfzPlayer.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      else {
        opponent.attributes().x += 0f;
        spfzPlayer.attributes().x += 0f;
      }
    }
    // if player 2 is neutral
    else {

      if (stageCamera.position.x <= camboundary[0] + 1 && opponent.attributes().x <= stageboundary[0]) {
        spfzPlayer.attributes().x += 0f;
      }
      else {

        opponent.attributes().x -= (spfzPlayer.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x -= (spfzPlayer.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void P1SRightMRight() {
    // if player 2 is moving left
    if (opponent.p2movement.get(0)) {
      spfzPlayer.attributes().x += spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();
      opponent.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
    // if player 2 is moving right
    else if (opponent.p2movement.get(1)) {

      if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {
        spfzPlayer.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
        opponent.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();

        if (opponent.attributes().x <= stageboundary[1] - (opponent.setrect().width * .33f)) {

        }
      }
      else if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        if (opponent.attributes().x <= stageboundary[1] - (opponent.setrect().width * .33f)) {

        }
        else {
          spfzPlayer.attributes().x += spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();
          opponent.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
        }

      }
    }
    else {
      spfzPlayer.attributes().x += spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
  }

  public void P1SLeftMLeft() {
    // if player 2 is moving to the left
    if (opponent.p2movement.get(0)) {
      if (spfzPlayer.attributes().x <= stageboundary[0] + (spfzPlayer.setrect().width * .33f)) {
        opponent.attributes().x += 0f;
      }
      else {
        if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
          opponent.attributes().x -= spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();
          spfzPlayer.attributes().x -= opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
        }
        else if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {
          spfzPlayer.attributes().x += -(opponent.moveandjump().x * Gdx.graphics.getDeltaTime());
          opponent.attributes().x += -(opponent.moveandjump().x * Gdx.graphics.getDeltaTime());
        }

      }
    }

    // if player 2 is moving to right
    else if (opponent.p2movement.get(1)) {

      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        opponent.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {
        opponent.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        opponent.attributes().x -= (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      else {
        opponent.attributes().x -= 0f;
        opponent.attributes().x -= 0f;
      }
    }

    else {
      spfzPlayer.attributes().x -= spfzPlayer.moveandjump().x * Gdx.graphics.getDeltaTime();
    }
  }

  public void P1SLeftMRight() {
    // if player 2 is moving to the left
    if (opponent.p2movement.get(0)) {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        opponent.attributes().x += (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x += (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer.moveandjump().x < opponent.moveandjump().x) {
        opponent.attributes().x += (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        opponent.attributes().x += (spfzPlayer.moveandjump().x - opponent.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }

    }

    // if player 2 is neutral
    else if (!opponent.p2movement.get(0) && !opponent.p2movement.get(1)) {

      if (stageCamera.position.x + 1 >= camboundary[1] && opponent.attributes().x + 1 >= stageboundary[1]) {
        spfzPlayer.attributes().x += 0f;
      }
      else {

        opponent.attributes().x += (spfzPlayer.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzPlayer.attributes().x += (spfzPlayer.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
    // if player 2 is moving to right
    else if (opponent.p2movement.get(1)) {
      if (opponent.moveandjump().x < spfzPlayer.moveandjump().x) {

        opponent.attributes().x += (spfzPlayer.moveandjump().x * .25f) * Gdx.graphics.getDeltaTime();

      }
      else if (opponent.moveandjump().x > spfzPlayer.moveandjump().x) {
        opponent.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void newcrossing() {
    if (spfzPlayer.center() < opponent.center()) {
      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        spfzPlayer.attributes().x -= spfzPlayer.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();

        opponent.attributes().x += spfzPlayer.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();
      }
      else {
        spfzPlayer.attributes().x -= opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
        opponent.attributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
      }
    }
  }

  public void pausechar() {
    spfzPlayer.tempfdur = spfzPlayer.spfzanimationstate.currentAnimation.getFrameDuration();
    spfzPlayer.spfzanimationstate.currentAnimation.setFrameDuration(5f);
  }

  public void switchp1() {
    float facing;
    p1spec -= 100f;
    sigp1lock = true;
    strt1 = true;
    spfzPlayer.attacking = false;
    if (spfzPlayer.attributes().scaleX > 0) {
      facing = spfzPlayer.attributes().scaleX;
    }
    else {
      facing = spfzPlayer.attributes().scaleX * -1;
    }
    p1xattr = spfzPlayer.attributes().x;

    p1yattr = spfzPlayer.attributes().y;

    CompositeItemVO player1char1;
    CompositeItemVO player1char2;
    CompositeItemVO player1char3;
    ItemWrapper playerone;
    switch (p1) {
      case 0:

        p1++;
        stage.stageSSL().getEngine().removeEntity(p1c1);
        player1char2 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c2 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char2);
        p1c2.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c2.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c2.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c2, player1char2.composite);
        stage.stageSSL().getEngine().addEntity(p1c2);
        playerone = new ItemWrapper(p1c2);
        break;

      case 1:
        p1++;
        stage.stageSSL().getEngine().removeEntity(p1c2);
        player1char3 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c3 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char3);
        p1c3.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c3.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c3.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c3, player1char3.composite);
        stage.stageSSL().getEngine().addEntity(p1c3);
        playerone = new ItemWrapper(p1c3);

        break;

      case 2:
        p1 = 0;
        stage.stageSSL().getEngine().removeEntity(p1c3);
        player1char1 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c1 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char1);
        p1c1.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c1.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c1.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c1, player1char1.composite);
        stage.stageSSL().getEngine().addEntity(p1c1);
        playerone = new ItemWrapper(p1c1);
        break;
      default:
        playerone = new ItemWrapper(null);
        break;
    }

    playerone.addScript((IScript) arrscripts.get(p1));

    // Ensure collision box is setup correctly

    if (facing > 0) {

      spfzPlayer.setrect().set(spfzPlayer.setrect().x + spfzPlayer.adjustX,
        spfzPlayer.attributes().y + spfzPlayer.adjustY, spfzPlayer.setrect().width,
        spfzPlayer.setrect().height);
    }
    else {
      spfzPlayer.setrect().set(spfzPlayer.setrect().x - spfzPlayer.adjustX,
        spfzPlayer.attributes().y + spfzPlayer.adjustY, spfzPlayer.setrect().width, spfzPlayer.setrect().height);

    }
    spfzPlayer.attributes().scaleX = facing;

    // Start swap particle effect

    if (stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
      stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects
        .removeValue(stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
    }
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = spfzPlayer.center();
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = spfzPlayer.attributes().y
      + spfzPlayer.setrect().height * .5f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(TransformComponent.class).scaleX = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
    switchcount++;
    switchp1 = false;

  }

  //Swap particle effect - may go into a Particle Class?
  if(root.getChild("p1swap").

  getChild("swapp1").

  getEntity().

  getComponent(SPFZParticleComponent .class).pooledeffects!=null)

  {
    if (root.getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
      if ((!root.getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete()) && stage.strt1) {
        root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = stage.spfzPlayer.center();
        root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = stage.spfzPlayer.attributes().y
          + stage.spfzPlayer.dimensions().height / 2;
      }
    }
  }

  public void airatkedanim() {
  }

  public void airatkanim() {

    //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
    // Normal has connected.
    if (spfzAnimationState.paused && cancelled == 0 || pausefrm) {
      // using this process to get the exact frame in order to setup the
      // recovery animation
      if (!pausefrm) {
        pauseframe = currentframe;
        pausefrm = true;
        pauseTime = 0;
      }
      cancel = true;

      if (!attacking) {
        confirm = false;
      }
      if (special && isPunch && cancelled == 0 && cancel) {
        cancelled = 1;
      }
      // .10 will need to be a value coming in from the file
      // if (pauseTime >= .08f)
      if (pauseTime >= rtnFrametime(10f)) {
        confirm = false;
        pauseTime = 0f;
        spfzAnimationState.paused = false;
        pausefrm = false;
        if (cancelled != 1) {
          if (spfzAnimation.currentAnimation != null && spfzAnimation.currentAnimation != "recovery") {
            spfzAnimationState.set(
              new FrameRange("recovery", pauseframe,
                spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation).endFrame),
              60, Animation.PlayMode.NORMAL);
            stateTime = 0;
            spfzAnimation.currentAnimation = "recovery";
            previousAnimation = spfzAnimation.currentAnimation;
            cancel = false;
          }
          // set recovery frame animation here

        }
      }

      // pauseTime += .015f;
      pauseTime += rtnFrametime(5);
    }
  }

  public void airmvmtanim() {
    // check jumping and jump direction variables
    if (spfzPlayer.isJumping()) {
      //spfzanimation.fps = 15;
      // if jumpdir true, means jumping in right diagonal
      if (jumpdir) {
        if (spfzPlayer.isFacingRight()) {
          spfzAnimation.currentAnimation = "FJMP";
        }
        else {
          spfzAnimation.currentAnimation = "BJMP";
          if (walljump) {
            spfzAnimation.currentAnimation = "WJMP";
          }
        }
      }
      else {
        if (spfzPlayer.isFacingRight()) {
          spfzAnimation.currentAnimation = "BJMP";
          if (walljump) {
            spfzAnimation.currentAnimation = "WJMP";
          }
        }
        else {
          spfzAnimation.currentAnimation = "FJMP";
        }
      }
    }
    else {
      spfzAnimation.currentAnimation = "NJMP";
    }
  }

  public void grdatkedanim() {
    //////////// ATTACKED
    if (spfzAnimation.currentAnimation == "IDLE" || spfzAnimation.currentAnimation == "movement" || attacked) {
      attacking = false;
    }

    if (ownatk) {
      confirm = true;
      ownatk = false;
    }
    // Check if player one is attacked
    if (((Attribs) stage.arrscripts.get(opponent.currentCharacter)).getboxconfirm() && !ownatk) {
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
      if (blocking) {
        if (spfzrect.y == ground) {
          if (isDown) {
            spfzAnimation.currentAnimation = "CBLK";
          }
          else {
            spfzAnimation.currentAnimation = "SBLK";
          }
        }
        else {
          spfzAnimation.currentAnimation = "ABLK";
        }
      }
      else {
        spfzAnimation.currentAnimation = "SATKD";
      }
      setstun();
      stateTime = 0;
    }

    if (stateTime >= stun) {
      attacked = false;
      spfzAnimation.currentAnimation = "IDLE";
      stateTime = 0;
      pushed = false;
      if (blocking) {
        blocking = false;
      }

      if (combocount != 0) {
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

      if (stateTime != 0) {
        stateTime = 0;
      }
    }
    else {
      if (pushed && !hit) {

      }
      else {
        hit = false;
      }

      if ((spfzAnimation.currentAnimation == "SATKD" || spfzAnimation.currentAnimation == "SBLK"
        || spfzAnimation.currentAnimation == "ABLK" || spfzAnimation.currentAnimation == "CBLK")
        && stateTime < stun) {
        // modifier from file
        float pushback = 2f;
        float progress = Math.min(pushback, intpush * pushback);

        pushback = progress;

        if (intpush > 0f) {// .03 will be modifier from file
          intpush -= .03f;
        }

        // pushback after hit
        if (spfzPlayer.isFacingRight()) {
          if (stage.camera().position.x <= stageBoundaries[0] + 1 && spfzrect.x <= stageBoundaries[0]) {
            spfzattribute.x -= pushback;
          }
          else {
            spfzattribute.x -= pushback;
          }
        }
        // pushback if facing the opposite direction
        else {
          if (stage.camera().position.x + 1 >= cameraBoundaries[1] && spfzrect.x >= stageBoundaries[1]) {
            spfzattribute.x += pushback;
          }
          else {
            spfzattribute.x += pushback;
          }
        }

        pushed = true;

      }
      else {
        stun = 0;

      }
    }

    // once the animation is complete, return the character back to the
    // neutral state
    if (spfzAnimationState.currentAnimation.isAnimationFinished(stateTime) && spfzAnimation.currentAnimation != "SATKD"
      && spfzAnimation.currentAnimation != "SBLK" && spfzAnimation.currentAnimation != "ABLK"
      && spfzAnimation.currentAnimation != "CBLK") {

      attacking = false;
      spfzAnimation.currentAnimation = "IDLE";
      spfzAnimationState.set(spfzAnimation.frameRangeMap.get("IDLE"), characterAttributes.animFPS.get(characterAttributes.anims.indexOf(spfzAnimation.currentAnimation)), Animation.PlayMode.LOOP);
      stateTime = 0;
    }

    // stateTime += Gdx.graphics.getDeltaTime();
  }

  public void grdatkanim() {
    //spfzanimation.fps = 60;
    if (cancel && !hitboxsize.isZero()) {
      hitboxsize.setZero();
    }
    // Normal has connected.
    if (spfzAnimationState.paused && cancelled == 0 || pausefrm) {
      // using this process to get the exact frame in order to setup the
      // recovery animation
      if (!pausefrm) {
        pauseframe = currentframe;
        pausefrm = true;
        pauseTime = 0;
      }
      cancel = true;
      // pauseTime += .015f;

      if (!attacking) {
        confirm = false;
      }
      if (special && isPunch && cancelled == 0 && cancel) {
        cancelled = 1;
      }
      // .10 will need to be a value coming in from the file
      // if (pauseTime >= .08f)
      if (pauseTime >= rtnFrametime(25f)) {
        if (stage.shake) {
          stage.shake = false;
        }
        confirm = false;
        pauseTime = 0f;
        spfzAnimationState.paused = false;
        pausefrm = false;
        if (cancelled != 1) {
          if (spfzAnimation.currentAnimation != null && spfzAnimation.currentAnimation != "recovery") {

            spfzAnimationState.set(
              new FrameRange("recovery", pauseframe,
                spfzAnimation.frameRangeMap.get(spfzAnimation.currentAnimation).endFrame),
              60, Animation.PlayMode.NORMAL);
            stateTime = 0;
            spfzAnimation.currentAnimation = "recovery";
            previousAnimation = spfzAnimation.currentAnimation;
            cancel = false;
          }
          // set recovery frame animation here

        }
      }
      pauseTime += rtnFrametime(5);
    }
  }

  public void grdmvmtanim() {
    if (cancel) {
      cancel = false;
    }
    //spfzanimation.fps = 16;
    if (!isDown && !isUp && (isLeft || isRight || dash) && spfzrect.y == ground && !ltstuck) {

      if (!stwlk) {

        spfzAnimation.currentAnimation = "FTRANS";
        //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      }
      else {
        if (!dash) {
          //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
          if (spfzrect.y <= ground) {
            // spfzanimation.currentAnimation = "movement";
            if (isLeft) {
              if (spfzPlayer.isFacingRight()) {
                spfzAnimation.currentAnimation = "BWLK";
              }
              else {
                spfzAnimation.currentAnimation = "FWLK";
              }
            }
            else if (isRight) {
              if (spfzPlayer.isFacingRight()) {
                spfzAnimation.currentAnimation = "FWLK";
              }
              else {
                spfzAnimation.currentAnimation = "BWLK";
              }
            }
            if (isLeft && isRight) {
              spfzAnimation.currentAnimation = "IDLE";
            }
          }
        }
      }

      if (dash) {
        if (dashdir == 0 && spfzPlayer.isFacingRight() ||
          dashdir == 1 && spfzattribute.scaleX < 0) {
          spfzAnimation.currentAnimation = "BDASH";
        }
        else {
          spfzAnimation.currentAnimation = "FDASH";
        }
        //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
      }

    }
    else {
      if (!dash) {
        if (spfzrect.y <= ground) {
          if (stwlk) {
            if (spfzAnimation.currentAnimation != "STPTRANS") {
              stateTime = 0;
            }
            spfzAnimation.currentAnimation = "STPTRANS";
            //spfzanimation.fps = spfzp1vals.animFPS.get(spfzp1vals.anims.indexOf(spfzanimation.currentAnimation));
            //stateTime += Gdx.graphics.getDeltaTime();
            if (spfzAnimationState.currentAnimation.isAnimationFinished(stateTime)) {
              stwlk = false;
              setneutral();
            }
          }
          else {
            spfzAnimation.currentAnimation = "IDLE";
          }
        }

        if (isDown) {
          spfzAnimation.currentAnimation = "CRCH";
        }
      }
    }
  }

  public boolean isLoopAnimation() {
    for (String anim : loopAnimations)
      if (spfzAnimation.currentAnimation.equals(anim))
        return true;
    return false;
  }

  public boolean isAnimationCompleted() {
    return spfzAnimationState.currentAnimation.isAnimationFinished(stateTime);
  }

  public boolean isBeingAttacked() {
    return spfzAnimation.currentAnimation.equals("SATKD")
      || spfzAnimation.currentAnimation.equals("SBLK") || spfzAnimation.currentAnimation.equals("CBLK")
      || spfzAnimation.currentAnimation.equals("ABLK");
  }

  public int[] activeFrames() { return new int[] {0, 0}; }

  ;

  public SpriteAnimationComponent animationComponent() { return spfzAnimation; }

  public SpriteAnimationStateComponent animationState() {
    return spfzAnimationState;
  }

  public void setPreviousAnimation(String previousAnimation) { this.previousAnimation = previousAnimation; }

  public int currentFrame() {
    return spfzAnimationState.currentAnimation.getKeyFrameIndex(stateTime);
  }
}
