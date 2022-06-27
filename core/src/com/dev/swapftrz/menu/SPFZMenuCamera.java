package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dev.swapftrz.resource.SPFZResourceManager;

/**
 * Class controls the menu camera actions
 */
public class SPFZMenuCamera extends Camera
{
  private float WORLD_WIDTH, WORLD_HEIGHT;
  private Viewport viewport;
  private SPFZResourceManager resManager;

  Vector3 credits = new Vector3(320, 400 + (int) (400 * .5), 0), tomenu = new Vector3(320, 400 * .5f, 0),
    termov = new Vector3(160f, 700f, 0), treymov = new Vector3(480f, 700f, 0), migmov = new Vector3(160f, 500f, 0),
    mikmov = new Vector3(480f, 500f, 0), credmov = new Vector3(320f, 600f, 0), move = new Vector3(0, 0, 0);

  public SPFZMenuCamera(SPFZResourceManager resManager) {
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

  public Camera menuCamera() {
    return viewport.getCamera();
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

  /**
   * method performs action when the user swipes up or down
   */
  public void swipecheck() {
    if (!optionsup)
    {
      if (flingup)
      {
        if (this.position.y == credits.y)
        {
          flingup = false;
        }

        this.position.lerp(credits, 0.2f);

      }
      if (flingdown)
      {
        if (this.position.y == tomenu.y)
        {
          flingdown = false;
        }

        this.position.lerp(tomenu, 0.2f);
        if (((OrthographicCamera) this).zoom != ZOOMCOUT)
        {
          ((OrthographicCamera) this).zoom = ZOOMCOUT;
        }
      }
    }
  }

  public void creditprocessing() {
    if (flingup && ((OrthographicCamera) this).position.y >= credits.y - 1)
    {
      flingup = false;
      flingdown = false;
    }
    if (flingdown && ((OrthographicCamera) this).position.y <= tomenu.y + 1)
    {
      flingdown = false;
      flingup = false;

    }
    if (credpress)
    {
      if (flingdown || flingup)
      {
        credpress = false;
      }
      else
      {
        // Zoom into credits, passing in the distance of zoom, time it should
        // take, and the positioning of the camera
        Zoom(ZOOMCIN, ZOOMCIDUR, move.x, move.y);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
          processback();
        }
      }
    }
    else
    {
      if (((OrthographicCamera) this).zoom != ZOOMCOUT && !flingdown && !flingup
        && this.position.y >= tomenu.y)
      {
        // Zoom out, passing in the distance of zoom, time it should take, and
        // the positioning of the camera
        Zoom(ZOOMCOUT, ZOOMCODUR, credits.x, credits.y);
      }
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
    {
      if (stage == null)
      {
        processback();
      }
    }
  }

}
