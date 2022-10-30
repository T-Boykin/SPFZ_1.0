package com.dev.swapftrz.fyter;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPFZProjScript implements IScript {

  boolean hit, allhits, reflected, oppchar;
  SPFZStage stage;
  Vector2 walkandjump;

  byte numofhits = 0;

  float stateTime = 0, projspeed = 8f;

  int combocount, currentframe, buff, last, dashdir, lastcount, buffsize = 60;

  int[] activeframes, initial, travel, travelc;

  ArrayList<int[]> inputs = new ArrayList<int[]>();

  List<Boolean> p1movement = new ArrayList<Boolean>(), lastp1movement = new ArrayList<Boolean>();
  List<Integer> buffer = new ArrayList<Integer>(), last16 = new ArrayList<Integer>();

  HashMap<String, int[]> animations = new HashMap<String, int[]>();

  Entity spfzprojectile;

  ActionComponent spfzaction;
  TransformComponent spfzattribute;
  DimensionsComponent spfzdim;

  Rectangle spfzrect, spfzhitrect;

  ShapeRenderer spfzsr, spfzhitbox;

  SpriteAnimationComponent spfzanimation;
  SpriteAnimationStateComponent spfzanimationstate;

  Vector2 hitboxsize, posofhitbox;

  public SPFZProjScript(SPFZStage screen) {
    stage = screen;
  }

  @Override
  public void act(float delta) {
    animlogic();

    if (!allhits) {
      traject();
    }

    hitbox();

    // if projectile hits player 2

    //if (spfzhitrect.overlaps(stage.spfzp2move.setcharbox()) && !allhits && numofhits == 0)
    //{
    hit = true;
    // will eventually need to be number of hits coming in from a database
    // tied to a projectile

    // Also need to inlcude a hit timer within the projectile detailing
    numofhits = 1;
    //}
    //else if(spfzhitrect.overlaps(stage.spfzp1move.setcharbox()) && !allhits && numofhits == 0)
    //{
    hit = true;
    oppchar = true;
    //stage.spfzp2move.ownatk = true;
    //stage.spfzp1move.projconfirm();
    numofhits = 1;
    //	}
    //	else
    //	{
    if (numofhits > 0 && !allhits) {
      numofhits--;

      if (numofhits == 0) {
        allhits = true;
        spfzanimation.currentAnimation = "hit";
        spfzhitrect.setX(-500f);
        spfzhitrect.setY(-500f);
        stateTime = 0;
      }
    }
    //	}
  }

  public void hitbox() {
    //spfzhitbox.setProjectionMatrix(stage.access.viewportland.getCamera().combined);
    spfzhitbox.begin(ShapeType.Line);
    spfzhitbox.setColor(Color.WHITE);
    spfzhitbox.rect(spfzhitrect.x, spfzhitrect.y, spfzhitrect.width, spfzhitrect.height);

    spfzhitbox.end();

  }

  public void animlogic() {
    if (spfzanimation.currentAnimation != "travel") {
      //if animation of hit or initial is finished
      if (spfzanimationstate.currentAnimation.isAnimationFinished(stateTime)) {
        if (spfzanimation.currentAnimation != "hit") {
          spfzanimation.currentAnimation = "travel";
          spfzanimationstate.set(spfzanimation.frameRangeMap.get("travel"), 60, Animation.PlayMode.LOOP);
          stateTime = 0;
        }
        else {
          killpj();
        }
      }

      //start the hit effect animation
      if (allhits && stateTime == 0) {
        if (!oppchar) {
          //stage.spfzp1move.hitboxconfirm(false);
        }
        else {
          //stage.spfzp2move.hitboxconfirm(false);
        }
        spfzanimation.currentAnimation = "hit";
        spfzanimationstate.set(spfzanimation.frameRangeMap.get("hit"), 60, Animation.PlayMode.NORMAL);
        //stateTime = 0;
      }
      stateTime += Gdx.graphics.getDeltaTime();
    }
  }

  public void stun() {

  }

  @Override
  public void dispose() {

  }


  public ShapeRenderer drawhitbox() {
    return spfzhitbox;
  }

  public ShapeRenderer drawrect() {
    return spfzsr;
  }


  public TransformComponent transformAttributes() {
    return spfzattribute;
  }

  @Override
  public void init(Entity entity) {
    int init[] = {0, 6};
    int trav[] = {13, 24};
    int hit[] = {25, 35};
    spfzhitbox = new ShapeRenderer();
    spfzhitrect = new Rectangle();

    posofhitbox = new Vector2();

    spfzhitrect.set(0, -500f, entity.getComponent(DimensionsComponent.class).width * .5f,
      entity.getComponent(DimensionsComponent.class).height * .5f);

    initial = init;
    travel = trav;

    animations.put("initial", initial);
    animations.put("travel", travel);
    animations.put("hit", hit);

    spfzprojectile = entity;
    setanimations();

  }

  public void traject() {
    //projectile shooting right
    if (spfzattribute.scaleX == 1f) {
      //if opposite of initial direction
      if (!reflected) {
        spfzattribute.x -= spfzdim.width;
        projspeed *= 2;
        reflected = true;
      }

      spfzattribute.x += projspeed;
      if (spfzanimation.currentAnimation == "travel") {
        spfzhitrect.setX(spfzattribute.x + spfzdim.width * .5f);
      }

    }
    //projectile shooting left
    else {
      //if opposite of initial direction
      if (reflected) {
        spfzattribute.x += spfzdim.width;
        projspeed *= 2;
        reflected = false;
      }


      spfzattribute.x -= projspeed;
      if (spfzanimation.currentAnimation == "travel") {
        spfzhitrect.setX(spfzattribute.x - spfzdim.width);
      }
    }
    if (spfzanimation.currentAnimation == "travel"
      && spfzhitrect.y != spfzattribute.y + spfzdim.height * .25f) {
      spfzhitrect.setY(spfzattribute.y + spfzdim.height * .25f);
    }

    //if ((spfzattribute.x <= stage.access.viewportland.getCamera().position.x - 320f
    //		|| spfzattribute.x >= stage.access.viewportland.getCamera().position.x + 320f))
    //{

    killpj();
    //}
  }

  public void killpj() {
//		stage.spfzp1move.projact = false;
//		stage.access.land.engine.removeEntity(spfzprojectile);
//		stage.spfzp1move.projectile = null;

  }

  public void setanimations() {
    NodeComponent nc;

    nc = ComponentRetriever.get(spfzprojectile, NodeComponent.class);
    spfzaction = ComponentRetriever.get(spfzprojectile, ActionComponent.class);
    spfzattribute = ComponentRetriever.get(spfzprojectile, TransformComponent.class);

//		if (stage.spfzp1move.attributes().scaleX == 1f)
//		{
//			spfzattribute.x = stage.spfzp1move.attributes().x;
    reflected = true;
//		}
//		else
//		{
//			spfzattribute.x = stage.spfzp1move.attributes().x;
    spfzattribute.scaleX = -1f;
    reflected = false;

//		}
//		spfzattribute.y = stage.GROUND;

    spfzdim = ComponentRetriever.get(spfzprojectile, DimensionsComponent.class);
    spfzanimation = ComponentRetriever.get(nc.children.get(0), SpriteAnimationComponent.class);
    spfzanimationstate = ComponentRetriever.get(nc.children.get(0), SpriteAnimationStateComponent.class);

    List<String> keys = new ArrayList<String>(animations.keySet());

    // create frame ranges for all animations listed for each character
    for (int i = 0; i < animations.size(); i++) {

      spfzanimation.frameRangeMap.put(keys.get(i),
        new FrameRange(keys.get(i), animations.get(keys.get(i))[0], animations.get(keys.get(i))[1]));
    }

    spfzanimation.currentAnimation = "initial";
    spfzanimationstate.set(spfzanimation.frameRangeMap.get("initial"), 60, Animation.PlayMode.NORMAL);
  }

  public void setcombonum(int comboint) {
    combocount = comboint;
  }

  public Rectangle sethitbox() {

    return spfzhitrect;
  }

  public Rectangle setrect() {

    return spfzrect;
  }

}