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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class SpecialSystem extends EntitySystem
{
  private ImmutableArray<Entity> entities;

  Batch batch;
  boolean init;
  Camera camera;
  Engine engine;
  ItemWrapper root;
  private ComponentMapper<SpecialTexComponent> ltc = ComponentMapper.getFor(SpecialTexComponent.class);
  private ComponentMapper<TransformComponent> tc = ComponentMapper.getFor(TransformComponent.class);

  float width;
  float height;
  float rootx;
  float rooty;
  float lockval;
  float x;
  float y;
  float p1meter;

  static final float ADJUSTMENTX = -60;
  static final float ADJUSTMENTY = -10;
  //static final float ADJUSTMENTY = 0f;

  static final float ADJUSTOUTX = 1f;
  static final float ADJUSTOUTY = 21f;

  float percentage;
  //exact needed to correctly reflect ex meter
  float exact1[] = {100f, 80f, 65f, 45f, 20f, 0f};
  float exact2[] = {20f, 40f, 65f, 85f, 100f, 120f};

  //exvals needed in order to base the amounts to set the ex meter texture
  float exvals[] = {20f, 40.5f, 61.5f, 82.1f, 102.5f, 123f};
  int exmet;
  SPFZStage stage;


  Texture spectexture, extex, superout;


  public SpecialSystem(Batch batch, Camera camera, SPFZStage stage)
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
    entities = engine.getEntitiesFor(Family.all(SpecialTexComponent.class, TransformComponent.class).get());

  }

  //Update draws the lifebars
  public void update(float deltaTime)
  {

    //1 = Player 1
    //0 = Player 2
    for (int i = 0; i < entities.size(); ++i)
    {
      //if (i == 0)
      if (entities.get(i).getComponent(MainItemComponent.class).itemIdentifier.equals("supbarone"))
      {
        percentage = stage.p1SPpercent;
      }
      else
      {
        percentage = stage.p2SPpercent;
      }

      Entity entity = entities.get(i);

      spectexture = ltc.get(entity).getSpecial();
      extex = ltc.get(entity).getEx();
      superout = ltc.get(entity).getsupout();


      width = ltc.get(entity).width;

      height = ltc.get(entity).height;
      rootx = root.getChild("ctrlandhud").getComponent(TransformComponent.class).x;
      rooty = root.getChild("ctrlandhud").getComponent(TransformComponent.class).y;
      x = tc.get(entity).x;
      y = tc.get(entity).y;


      batch.begin();


      batch.setColor(Color.WHITE);

      if (entities.get(i).getComponent(MainItemComponent.class).itemIdentifier.equals("supbarone"))
      {
        //Player one super bar will deplete the opposite direction

        p1meter = width - (int) percentage;
         batch.draw(spectexture, rootx + x - ADJUSTMENTX + p1meter, rooty + y - ADJUSTMENTY, (int) p1meter, 0, spectexture.getWidth(), spectexture.getHeight());



        for (int j = 0; j < exvals.length; j++)
        {
          if (percentage >= exvals[j] || stage.sigp1lock)
          {
            if (j == ltc.get(entity).excount && !stage.sigp1lock)
            {
              ltc.get(entity).excount++;
              ltc.get(entity).lock = false;

            }
            if (j < ltc.get(entity).excount && percentage < exvals[j])
            {
              ltc.get(entity).excount = j;
              ltc.get(entity).lock = false;

            }
            if (!ltc.get(entity).lock)
            {
              ltc.get(entity).lock = true;

              if (stage.sigp1lock)
              {

                if (j == 0)
                {

                  ltc.get(entity).lockval = width;
                }
                else
                {

                  ltc.get(entity).lockval = exact1[j];
                }
                stage.sigp1lock = false;
              }
              else
              {
                ltc.get(entity).lockval = exact1[j];
              }


            }
          }

        }

        batch.draw(extex, rootx + x - ADJUSTMENTX + ltc.get(entity).lockval, rooty + y - ADJUSTMENTY,
          (int) ltc.get(entity).lockval, 0, extex.getWidth(), extex.getHeight());


        batch.draw(superout, rootx + x - ADJUSTMENTX + ADJUSTOUTX, rooty + y - ADJUSTMENTY,
          0, 0, extex.getWidth(), extex.getHeight());


      }
      else
      {
        batch.draw(spectexture, rootx + x - ADJUSTMENTX, rooty + y - ADJUSTMENTY, 0, 0, (int) percentage, spectexture.getHeight());


        for (int j = 0; j < exvals.length; j++)
        {
          if (percentage >= exvals[j])
          {
            if (j == ltc.get(entity).excount)
            {
              ltc.get(entity).excount++;
              ltc.get(entity).lock = false;
            }
            if (!ltc.get(entity).lock)
            {
              ltc.get(entity).lock = true;

              ltc.get(entity).lockval = exact2[j];

            }
          }
        }

        batch.draw(extex, rootx + x - ADJUSTMENTX, rooty + y - ADJUSTMENTY, 0, 0, (int) ltc.get(entity).lockval, extex.getHeight());

        batch.draw(superout, rootx + x - ADJUSTMENTX, rooty + y - ADJUSTMENTY,
          0, 0, extex.getWidth(), extex.getHeight());
      }


      batch.end();


    }
  }

  public Texture returnlife()
  {
    return spectexture;
  }


}
