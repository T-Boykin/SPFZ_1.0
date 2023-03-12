package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    if (isAnimationCompleted() && isBeingAttacked())
      //attacking = false ?

      if (spfzAnimationState.currentAnimation.getPlayMode() == Animation.PlayMode.NORMAL && !spfzAnimationState.paused)
        stateTime += Gdx.graphics.getDeltaTime();
  }

  public void checkstun() {
    float fd;
    String stunAnimation;

    if (spfzPlayer.isAttacked()) {
      stunAnimation = "stun";
    }
    else {
      if (spfzPlayer.isOnGround())
        stunAnimation = "block";
      else if (opponent.isDown)
        stunAnimation = "dblock";
      else
        stunAnimation = "ablock";
    }

    spfzPlayer.spfzAnimation.currentAnimation = stunAnimation;
    spfzPlayer.spfzAnimationState.set(
      spfzPlayer.spfzAnimation.frameRangeMap.get(stunAnimation), 60, Animation.PlayMode.NORMAL);
  }

  public void checkneutral() {

    if (spfzPlayer.center() > opponent.center()
      || spfzPlayer.setrect().x + spfzPlayer.setrect().width > opponent.center()) {
      if (spfzPlayer.setrect().x + (spfzPlayer.setrect().width * .5f) > opponent.setrect().x
        + (opponent.setrect().width * .5f)) {
        spfzPlayer.transformAttributes().x += 1.2f;
        opponent.transformAttributes().x -= 1.2f;
      }
      else {
        spfzPlayer.transformAttributes().x -= 1.2f;
        opponent.transformAttributes().x += 1.2f;
      }
    }

    if (opponent.center() > spfzPlayer.center()
      || opponent.setrect().x + opponent.setrect().width > spfzPlayer.center()) {
      if (opponent.setrect().x + (opponent.setrect().width * .5f) > spfzPlayer.setrect().x
        + (spfzPlayer.setrect().width * .5f)) {
        opponent.transformAttributes().x += 1.2f;
        spfzPlayer.transformAttributes().x -= 1.2f;
      }
      else {
        opponent.transformAttributes().x -= 1.2f;
        spfzPlayer.transformAttributes().x += 1.2f;
      }
    }

  }

  public void attackMovement() {
    //set the durations of the 3 stages of movement as well as the amount to push or pull the character.
    float startupMovement = 0, activeMovement = 0, recoveryMovement = 0;
    int FMOVE = 14, BMOVE = 15, BACK_START = 17, FWD_START = 18, BACK_ACTIVE = 19,
      FWD_ACTIVE = 20, BACK_RECOV = 21, FWD_RECOV = 22;
    int move = spfzPlayer.currentMove(),
      startIndex = spfzPlayer.hitboxStartIndex(), endIndex = spfzPlayer.hitboxEndIndex();
    String currentAnimation = spfzAnimation.currentAnimation;

    float startupDuration =
      spfzPlayer.rtnFrametime(characterData.get(startIndex).get(move).floatValue()
        - (float) Objects.requireNonNull(spfzAnimation.frameRangeMap.get(currentAnimation)).startFrame);
    float activeDuration =
      spfzPlayer.rtnFrametime(characterData.get(endIndex).get(move).floatValue()
        - characterData.get(startIndex).get(move).floatValue());
    float recoveryDuration =
      spfzPlayer.rtnFrametime(Objects.requireNonNull(spfzAnimation.frameRangeMap.get(currentAnimation)).endFrame
        - characterData.get(endIndex).get(move).floatValue());

    startupMovement = attackAnimationMovement(BACK_START, FWD_START, BMOVE, FMOVE, move);
    activeMovement = attackAnimationMovement(BACK_ACTIVE, FWD_ACTIVE, BMOVE, FMOVE, move);
    recoveryMovement = attackAnimationMovement(BACK_RECOV, FWD_RECOV, BMOVE, FMOVE, move);

    Actions.addAction(spfzPlayer.spfzentity,
      Actions.sequence(
        Actions.moveBy(startupMovement, 0, startupDuration, Interpolation.sineOut),
        Actions.moveBy(activeMovement, 0, activeDuration, Interpolation.sineOut),
        Actions.moveBy(recoveryMovement, 0, recoveryDuration, Interpolation.sineOut)));
    //createHitbox?
  }

  public void newcrossing() {
    if (spfzPlayer.center() < opponent.center()) {
      if (spfzPlayer.moveandjump().x > opponent.moveandjump().x) {
        spfzPlayer.transformAttributes().x -= spfzPlayer.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();

        opponent.transformAttributes().x += spfzPlayer.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();
      }
      else {
        spfzPlayer.transformAttributes().x -= opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
        opponent.transformAttributes().x += opponent.moveandjump().x * Gdx.graphics.getDeltaTime();
      }
    }
  }

  public void pausechar() {
    spfzPlayer.tempfdur = spfzPlayer.spfzAnimationState.currentAnimation.getFrameDuration();
    spfzPlayer.spfzAnimationState.currentAnimation.setFrameDuration(5f);
  }

  //saved the particleEffect animation logic
  public void switchCharacter() {
   /* // Start swap particle effect

    if (stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0) {
      stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects
        .removeValue(stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
    }
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = spfzPlayer.center();
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = spfzPlayer.transformAttributes().y
      + spfzPlayer.setrect().height * .5f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(TransformComponent.class).scaleX = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(SPFZParticleComponent.class).startEffect();

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
        root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = stage.spfzPlayer.transformAttributes().y
          + stage.spfzPlayer.dimensions().height / 2;
      }
    }*/
  }

  public void airAttackedAnimation() {
  }

  //TODO need to add hitStop animation?
  public void airAttackAnimation() {

  }

  public void airMovementAnimation() {
    // check jumping and jump direction variables
    if (spfzPlayer.isJumpingForwards())
      spfzAnimation.currentAnimation = "FJMP";
    else if (spfzPlayer.isJumpingBackwards())
      spfzAnimation.currentAnimation = "BJMP";
    else
      spfzAnimation.currentAnimation = "NJMP";
    //TODO add logic for wall jumping animation
  }

  public void attackedOnGround() {
    // Check if player one is attacked
    if (spfzPlayer.isBlocking()) {
      if (spfzPlayer.isHoldingDownBack())
        spfzAnimation.currentAnimation = "CBLK";

      if (spfzPlayer.isHoldingBack())
        spfzAnimation.currentAnimation = "SBLK";
    }
    else {
      spfzAnimation.currentAnimation = "SATKD";
    }

    //stage animation
      /*if (combocount != 0) {
        LabelComponent combocount1;
        combocount1 = stage.access.root.getChild("ctrlandhud").getChild("combocount1").getEntity()
          .getComponent(LabelComponent.class);

        Entity Hit = stage.access.root.getChild("ctrlandhud").getChild("p2himg").getEntity();
        Entity cc = stage.access.root.getChild("ctrlandhud").getChild("p2cc").getEntity();

        Actions.addAction(Hit, Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic));
        Actions.addAction(cc, Actions.scaleTo(stage.SCALE_TEXT, 0, .3f, Interpolation.elastic));

        combocount1.setText(" ");
        combocount = 0;
*/
  }

  public void grdatkanim() {
    // Normal has connected.
    if (spfzAnimationState.paused)
      // using this process to get the exact frame in order to setup the
      // recovery animation
      // pauseTime += .015f;
      spfzAnimationState.paused = false;
    // set recovery frame animation here

  }

  public void grdmvmtanim() {

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

  public float attackAnimationMovement(int movesBack, int movesForward, int backValue, int forwardValue, int move) {
    float movement = 0;

    if (characterData.get(movesBack).get(move) == 1)
      movement = characterData.get(backValue).get(move).floatValue();
    else if (characterData.get(movesForward).get(move) == 1)
      movement = characterData.get(forwardValue).get(move).floatValue();

    if (spfzPlayer.isFacingRight())
      movement *= -1f;

    return movement;
  }

  public boolean movementOnActive() { return false; }

  public boolean movementOnRecovery() { return false; }

}
