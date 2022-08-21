package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.systems.action.Actions;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class SPFZProjectile implements IScript {
  SPFZPlayer player;
  String projectileType;
  boolean init, multiple, hit, killpj, start, active;

  Entity composite, projectile;

  int hitpoints, iterator, spawn;
  float origX, origY, origScaleX, origScaleY;


  Rectangle hitbox;

  SpriteAnimationComponent spfzanimation;
  SPFZSceneLoader tempload;
  ShapeRenderer spfzsr = new ShapeRenderer();
  SpriteAnimationStateComponent spfzanimationstate;
  String[] animations = {"INIT", "LOOP", "END"};
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

  Vector2 spfzattribute, startup, loop, end;

  enum Projectile {
    FORWARD;
  }

  Projectile proj;

  public SPFZProjectile(SPFZPlayer player, String projectileType) {
    this.player = player;
    this.projectileType = projectileType;
  }

  public SPFZProjectile(Rectangle hitbox, int[] data, Entity projectile, SPFZSceneLoader root) {
    this.hitbox = hitbox;

    startup = new Vector2(data[0], data[1]);
    loop = new Vector2(data[2], data[3]);
    end = new Vector2(data[4], data[5]);
    setType(data[6]);
    spfzattribute = new Vector2(data[7], 0);
    spawn = data[8];
    this.projectile = projectile;
    tempload = root;

  }

  @Override
  public void init(Entity entity) {
    composite = entity;
    origY = composite.getComponent(TransformComponent.class).y;
    origScaleX = composite.getComponent(TransformComponent.class).scaleX;
    origScaleY = composite.getComponent(TransformComponent.class).scaleY;

    // composite.getComponent(ZIndexComponent.class).layerName = "players";
    initAnims();
    //initAnim();

  }

  @Override
  public void act(float delta) {
    if (start) {
      if (!init) {
        initAnim();
        init = true;
      }

      if (spfzanimationstate.paused) {
        spfzanimationstate.paused = false;
      }
      animation(proj);

      if (spfzanimation.currentAnimation == animations[1]) {
        if (!active) {
          setActive();
        }
        traject(proj);
      }
    }
  }


  public void initAnim() {
    float begScale = 1.5f;
    float endScale = 1f;
    float scaleX = composite.getComponent(TransformComponent.class).scaleX;
    float scaleY = composite.getComponent(TransformComponent.class).scaleY;
    float fullScale = begScale / scaleY;
    float y = composite.getComponent(TransformComponent.class).y;
    float yvalUp = (((y * fullScale) - y) * .75f) * -1f;
    float yvalDown = yvalUp * -1f;
    float totalframes = end.y + 1;
    float duration = (totalframes / spfzanimation.fps) * .5f;

    Actions.removeActions(composite);
    Actions.addAction(composite, Actions.sequence(
      Actions.parallel(Actions.moveBy(0, yvalUp, duration, Interpolation.sineOut),
        Actions.scaleTo(scaleX, begScale, duration, Interpolation.sineOut)),

      Actions.parallel(Actions.parallel(Actions.moveBy(0, yvalDown, duration, Interpolation.sineOut),
        Actions.scaleTo(scaleX, endScale, duration, Interpolation.sineOut))
      )));
  }

  public void loopAnim() {
    resetProj();
    float begScale = 1.5f;
    float endScale = 1f;
    float scaleX = composite.getComponent(TransformComponent.class).scaleX;
    float scaleY = composite.getComponent(TransformComponent.class).scaleY;
    float fullScale = begScale / scaleY;
    float y = composite.getComponent(TransformComponent.class).y;
    float yvalUp = (((y * fullScale) - y) * .75f) * -1f;
    float yvalDown = yvalUp * -1f;
    float totalframes = end.y + 1;
    float duration = ((totalframes * .5f) / spfzanimation.fps) * .5f;


    Actions.removeActions(composite);
    Actions.addAction(composite, Actions.sequence(
      Actions.parallel(Actions.moveBy(0, yvalDown, duration, Interpolation.sineOut),
        Actions.scaleTo(scaleX, begScale, duration, Interpolation.sineOut)),

      Actions.parallel(Actions.parallel(Actions.moveBy(0, yvalUp, duration, Interpolation.sineOut),
        Actions.scaleTo(scaleX, endScale, duration, Interpolation.sineOut))
      )));
  }

  public void hitAnim() {
    resetProj();
    float endtraj = spfzattribute.x * 2;
    float duration = .25f;
    float endScale = 1.5f;
    float scaleX = composite.getComponent(TransformComponent.class).scaleX;
    float scaleY = composite.getComponent(TransformComponent.class).scaleY;
    float fullScale = endScale / scaleY;
    float y = composite.getComponent(TransformComponent.class).y;
    float yval = (((y * fullScale) - y) * .75f) * -1f;


    //if projecitle is facing opposite direction
    if (scaleX < 0) {
      endtraj *= -1f;
    }

    Actions.removeActions(composite);
    Actions.addAction(composite, Actions.parallel(
      Actions.moveBy(endtraj, yval, duration, Interpolation.sineOut),
      Actions.scaleTo(scaleX, fullScale, duration, Interpolation.sineOut)));

    setHit();
  }

  public void resetProj() {
    //composite.getComponent(TransformComponent.class).x = origX;
    composite.getComponent(TransformComponent.class).y = origY;
    composite.getComponent(TransformComponent.class).scaleX = origScaleX;
    composite.getComponent(TransformComponent.class).scaleY = origScaleY;
  }

  public void setActive() {
    if (!active) {
      active = true;
    }
    else {
      active = false;
    }
  }

  public void setHit() {
    if (!hit) {
      hit = true;
    }
    else {
      hit = false;
    }
  }

  public void setType(int type) {
    switch (type) {
      case 1:
        proj = Projectile.FORWARD;
        break;
      default:
        break;
    }
  }

  public void initAnims() {

    spfzanimation = ComponentRetriever.get(projectile, SpriteAnimationComponent.class);
    spfzanimationstate = ComponentRetriever.get(projectile, SpriteAnimationStateComponent.class);

    spfzanimation.frameRangeMap.put(animations[0], new FrameRange(animations[0],
      (int) startup.x, (int) startup.y));
    spfzanimation.frameRangeMap.put(animations[1], new FrameRange(animations[1],
      (int) loop.x, (int) loop.y));
    spfzanimation.frameRangeMap.put(animations[2], new FrameRange(animations[2],
      (int) end.x, (int) end.y));

    //spfzanimation.fps = 8;
    spfzanimation.currentAnimation = animations[0];
    spfzanimationstate.set(spfzanimation.frameRangeMap.get(animations[0]), spfzanimation.fps,
      Animation.PlayMode.NORMAL);
    spfzanimationstate.paused = true;
  }

  public void animation(Projectile type) {
    switch (type) {
      case FORWARD:
        if (spfzanimationstate.currentAnimation.isAnimationFinished(spfzanimationstate.time) && spfzanimation.currentAnimation != animations[1] || hit && spfzanimation.currentAnimation != animations[2]) {
          if (iterator < animations.length - 1) {
            iterator++;

            if (animations[iterator] != null) {
              setAnimation(animations[iterator]);
            }
            else {
              dispose();
            }
            if (spfzanimation.currentAnimation == animations[2]) {

            }
          }
          else {
            dispose();
          }
        }
        else {
          if (spfzanimation.currentAnimation == animations[1] && currentframe() == 1) {
            //loopAnim();
          }
        }

        break;
      default:
        break;
    }

    /*if (spfzanimation.currentAnimation == animations[1])
    {
      spfzanimationstate.time += Gdx.graphics.getDeltaTime();
    }*/

  }

  public void setAnimation(String animation) {
    //spfzanimationstate.time = 0;
    //spfzanimation.fps = 10;
    spfzanimation.currentAnimation = animation;
    if (animation != animations[1]) {
      spfzanimationstate.set(spfzanimation.frameRangeMap.get(animation), spfzanimation.fps, Animation.PlayMode.NORMAL);
    }
    else {
      spfzanimationstate.set(spfzanimation.frameRangeMap.get(animation), spfzanimation.fps,
        Animation.PlayMode.LOOP);
    }

  }

  public void traject(Projectile type) {
    float projX = composite.getComponent(TransformComponent.class).x;
    float traject = spfzattribute.x;

    switch (type) {
      case FORWARD:

        composite.getComponent(TransformComponent.class).x += traject;


        break;
      default:
        break;
    }

    hitbox.x += traject;
  }

  public int currentframe() {
    return spfzanimationstate.currentAnimation.getKeyFrameIndex(spfzanimationstate.time);
  }

  public ShapeRenderer hitbox() {
    return spfzsr;
  }

  public Rectangle sethit() {
    if (!active && hitbox.width != 0) {
      hitbox.set(0, 0, 0, 0);
    }
    return hitbox;
  }

  @Override
  public void dispose() {
    hitbox.set(0, 0, 0, 0);
    tempload.getEngine().removeEntity(composite);
    composite = null;


  }

}
