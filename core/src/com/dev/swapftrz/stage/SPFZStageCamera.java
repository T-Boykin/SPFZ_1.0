package com.dev.swapftrz.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dev.swapftrz.fyter.SPFZPlayer;
import com.dev.swapftrz.resource.SPFZResourceManager;

/**
 * Class controls the stage camera
 */
public class SPFZStageCamera extends Camera
{
  private float WORLD_WIDTH, WORLD_HEIGHT;
  private final Viewport viewport;
  private SPFZResourceManager resManager;
  private SPFZPlayer player, opponent;
  private SPFZStage stage;

  public SPFZStageCamera(SPFZResourceManager resManager) {
    WORLD_WIDTH = resManager.getWorldWidth();
    WORLD_HEIGHT = resManager.getWorldHeight();

    if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight())
      viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
    else
      viewport = new StretchViewport(WORLD_HEIGHT, WORLD_WIDTH);

    this.resManager = resManager;
    viewport.setCamera(this);
    viewport.setScreenWidth(Gdx.graphics.getWidth());
    viewport.setScreenHeight(Gdx.graphics.getHeight());
    //centers camera
    viewport.apply(true);
  }

  public void newcam() {
    Vector3 movecamera = new Vector3();
    float camX, camY;
    // "camX" will be placed between player one and player two. It will stop
    // whenever it has reached either the left
    // or right bound.

    //p1locX = spfzp1move.center();
    // p2locX = opponent.center();
    //p1locY = spfzp1move.setrect().y + spfzp1move.setrect().height * .5f;
    //p2locY = opponent.setrect().y + opponent.setrect().height * .5f;

    camX = (player.center() + opponent.center()) * .5f;
    camY = ((player.setrect().y + player.setrect().height * .5f) + (opponent.setrect().y + opponent.setrect().height * .5f)) * .5f;

    if (camX < stage.stageBoundaries()[0])
    {
      camX = stage.stageBoundaries()[0];
    }
    else if (camX > stage.stageBoundaries()[1])
    {
      camX = stage.stageBoundaries()[1];
    }

    if (camY < WORLD_HEIGHT * .5f)
    {
      movecamera.set(camX, WORLD_HEIGHT * .5f, 0);
    }
    else
    {
      movecamera.set(camX, camY, 0);
    }

    if (!p1charzoom && !p2charzoom)
    {
      if (((OrthographicCamera) stageCamera).zoom != 1f)
      {
        zoom(1f, .3f, movecamera.x, movecamera.y);

        if (((OrthographicCamera) stageCamera).zoom > .998f)
        {
          ((OrthographicCamera) stageCamera).zoom = 1f;
        }
      }
      stageCamera.position.lerp(movecamera, .3f);
      if (camcon == 3)
      {
        camcon = 0;
      }
    }
    else
    {
      if (p1charzoom)
      {

        if (p2charzoom)
        {
          p2charzoom = false;
        }

        if (player.attributes().scaleX > 0)
        {
          if (camcon == 0 || camcon == 2)
          {
            access.Zoom(.25f, .2f, player.attributes().x + (player.spfzrect.width),
              player.attributes().y + (player.spfzrect.height * .5f));

            if (camcon != 2)
            {
              if (((OrthographicCamera) stageCamera).zoom <= .26f)
              {
                camcon = 1;
              }
            }
            else
            {
              if (((OrthographicCamera) stageCamera).zoom <= .26f)
              {
                camcon = 3;
              }
            }
          }
          else
          {
            if (camcon != 3)
            {
              access.Zoom(.75f, .1f, movecamera.x, movecamera.y);
              if (((OrthographicCamera) stageCamera).zoom > .749f)
              {
                camcon = 2;
              }
            }
          }
        }
        else
        {
          access.Zoom(.25f, .5f, player.attributes().x - (player.spfzrect.width),
            player.attributes().y + (player.spfzrect.height * .5f));
        }

      }
      else if (p2charzoom)
      {
        p1charzoom = false;
        if (opponent.attributes().scaleX > 0)
        {
          access.Zoom(.25f, .5f, opponent.attributes().x + (opponent.spfzrect.width),
            opponent.attributes().y + (opponent.spfzrect.height * .5f));
        }
        else
        {
          access.Zoom(.25f, .5f, opponent.attributes().x - (opponent.spfzrect.width),
            opponent.attributes().y + (opponent.spfzrect.height * .5f));
        }

      }
    }

    if (shake)
    {
      int rand = MathUtils.random(1);
      float incrementx = MathUtils.random(1);
      float incrementy = MathUtils.random(5);
      float newx, newy = WORLD_HEIGHT * .5f;
      if (rand == 0)
      {
        newx = stageCamera.position.x + incrementx;
        newy = stageCamera.position.y + incrementy;
      }
      else
      {
        newx = stageCamera.position.x - incrementx;
        newy = stageCamera.position.y - incrementy;
      }
      if (newy < WORLD_HEIGHT * .5f)
      {
        movecamera.set(newx, WORLD_HEIGHT * .5f, 0);
      }
      else
      {
        movecamera.set(newx, newy, 0);

      }
      // movecamera.set(newx, camY, 0);
      if (!player.bouncer)
      {
        stageCamera.position.lerp(movecamera, 1f);
      }
    }
  }

  public void zoom(float targetzoom, float duration, float movex, float movey) {
    // set current vals to process interpolation smoothly
    zoompoint = ((OrthographicCamera) viewportland.getCamera()).zoom;
    endzoom = targetzoom;
    targetduration = startingduration = duration;

    if (((OrthographicCamera) viewportland.getCamera()).zoom >= targetzoom && credpress
      || ((OrthographicCamera) viewportland.getCamera()).zoom <= targetzoom && !credpress)
    {
      targetduration -= Gdx.graphics.getDeltaTime();
      float progress = targetduration < 0 ? 1 : 1f - targetduration / startingduration;

      ((OrthographicCamera) viewportland.getCamera()).zoom = Interpolation.pow3Out.apply(zoompoint, endzoom, progress);

      viewportland.getCamera().position.x = Interpolation.pow3Out.apply(viewportland.getCamera().position.x, movex,
        progress);
      viewportland.getCamera().position.y = Interpolation.pow3Out.apply(viewportland.getCamera().position.y, movey,
        progress);
    }
    else if (stage != null)
    {
      if (stage.p1charzoom || stage.p2charzoom)
      {
        targetduration -= Gdx.graphics.getDeltaTime();
        float progress = targetduration < 0 ? 1 : 1f - targetduration / startingduration;

        ((OrthographicCamera) viewportland.getCamera()).zoom = Interpolation.pow5Out.apply(zoompoint, endzoom,
          progress);

        viewportland.getCamera().position.x = Interpolation.pow5Out.apply(viewportland.getCamera().position.x, movex,
          progress);
        viewportland.getCamera().position.y = Interpolation.pow5Out.apply(viewportland.getCamera().position.y, movey,
          progress);
      }
    }

  }


  public Camera stageCamera() {
    return this;
  }

  public void changeViewport(float width, float height) {
    viewport.setWorldSize(width, height);
  }

  public Viewport getViewport() {
    return viewport;
  }

  @Override
  public void update() {

  }

  @Override
  public void update(boolean updateFrustum) {

  }

  public void setPlayers(SPFZPlayer player, SPFZPlayer opponent) {
    this.player = player;
    this.opponent = opponent;
  }

  public void setStage(SPFZStage stage) {
    this.stage = stage;
  }
}
