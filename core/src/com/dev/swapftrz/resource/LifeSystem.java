package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class LifeSystem extends EntitySystem
{
  private ImmutableArray<Entity> entities;

  Batch batch;
  boolean init;
  Camera camera;
  Engine engine;
  ItemWrapper root;
  private ComponentMapper<LifeTextureComponent> ltc = ComponentMapper.getFor(LifeTextureComponent.class);
  private ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);


  float width, height, owidth, oheight, rootx, rooty, x, y, ox, oy, p1health;

  static final float ADJUSTMENTX = 22f;
  //static final float ADJUSTMENTY = 18f;

  //static final float ADJUSTOUTX = 89f;
  //static final float ADJUSTOUTY = 21f;

  float percentage;

  SPFZStage stage;

  Sprite helspr1, helspr2, helsprite;

  Texture healthtexture;
  Texture healthoutline;
  int count = 0;

  public LifeSystem(Batch batch, Camera camera, SPFZStage stage)
  {
    this.batch = batch;
    this.camera = camera;
    this.root = stage.access.root;
    this.stage = stage;


    init = true;
  }

  public void addedToEngine(Engine engine)
  {
    this.engine = engine;
    entities = engine.getEntitiesFor(Family.all(LifeTextureComponent.class, TransformComponent.class).get());


  }

  //Update draws the lifebars
  public void update(float deltaTime)
  {
    //0 = Player 1
    //1 = Player 2
    for (int i = 0; i < entities.size(); ++i)
    {
      if (i == 0)
      {
        percentage = stage.p1HPpercent;

      }
      else
      {
        percentage = stage.p2HPpercent;
      }

      Entity entity = entities.get(i);

      healthtexture = ltc.get(entity).getHealthTex();
      healthoutline = ltc.get(entity).getOutline();


      width = ltc.get(entity).width;
      height = ltc.get(entity).height;
      owidth = ltc.get(entity).outwidth;
      oheight = ltc.get(entity).outheight;

      rootx = root.getChild("ctrlandhud").getComponent(TransformComponent.class).x;
      rooty = root.getChild("ctrlandhud").getComponent(TransformComponent.class).y;
      x = tc.get(entity).x;
      y = tc.get(entity).y;
      ox = ltc.get(entity).outX;
      oy = ltc.get(entity).outY;

      //p1health = healthtexture.getWidth() / 2;

      drawhealth(i);
      if (i == 0)
      {
        stage.healthtex1 = healthtexture;

      }
      else
      {
        stage.healthtex2 = healthtexture;

      }

    }
  }

  public void drawhealth(int i)
  {

    batch.begin();

    batch.setColor(Color.BLACK);

    batch.draw(healthtexture, rootx + x - ADJUSTMENTX, rooty + y, width, height);
    //batch.draw(healthtexture, rootx + x, rooty + y);
    //batch.draw(healthtexture, rootx + x - ADJUSTMENTX, rooty + y, width, height);

    batch.setColor(Color.ORANGE);

    if (i == 0)
    {
      //Player one health bar will deplete the opposite direction
      p1health = width - (int) percentage;
     //batch.draw(healthtexture, rootx + x - ADJUSTMENTX + p1health, rooty + y, (int) p1health, 0, (int) width, (int) height);
      batch.draw(healthtexture, rootx + x - ADJUSTMENTX + p1health, rooty + y, 0f, 0f, width, height,
              1f, 1f, 0f, (int) p1health, 0, (int) healthtexture.getWidth(), (int) healthtexture.getHeight(), false, false);

    }
    else
    {
      //batch.draw(healthtexture, rootx + x - ADJUSTMENTX, rooty + y, width, height);
      //batch.draw(healthtexture, rootx + x, rooty + y);
      //batch.draw(healthtexture, rootx + x - ADJUSTMENTX, rooty + y, 0, 0, (int) percentage, (int) height);
      batch.draw(healthtexture, rootx + x - ADJUSTMENTX, rooty + y, 0f, 0f, width, height,
              1f, 1f, 0f, (int) 0, 0, (int) healthtexture.getWidth(), (int) healthtexture.getHeight(), false, false);

    }

    batch.setColor(Color.WHITE);
    //batch.draw(healthoutline, rootx + x - ADJUSTMENTX, rooty + y);
    batch.draw(healthoutline, rootx + ox - ADJUSTMENTX, rooty + oy, owidth, oheight);
    //batch.draw(healthoutline, rootx + x, rooty + y);
    batch.end();

  }
  public Texture returnlife1()
  {
    return healthtexture;
  }

  public Texture returnlife2()
  {
    return healthtexture;
  }

  @Override
  public void removedFromEngine(Engine engine)
  {

    healthtexture = null;
    healthoutline = null;
    helspr1 = null;
    helspr2 = null;
    helsprite = null;
    //batch.dispose();
    camera = null;
    root = null;
    //stage.dispose();

  }
}
