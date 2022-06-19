package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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
}
