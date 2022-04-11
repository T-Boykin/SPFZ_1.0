package com.dev.swapftrz.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Class controls the menu camera actions
 */
public class SPFZMenuCamera
{
  private static final short WORLD_WIDTH = 640;
  private static final short WORLD_HEIGHT = 400;
  private final Viewport viewport;

  public SPFZMenuCamera() {
    if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight())
      viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
    else
      viewport = new StretchViewport(WORLD_HEIGHT, WORLD_WIDTH);
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
}
