package com.dev.swapftrz.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SPFZStageImagePack
{
   private static final TextureAtlas healthTextureAtlas = new TextureAtlas(Gdx.files.internal("orig/pack.atlas"));
   private static final String health = "health/health.png",
     healthOutline = "health/healthbar.png",
     specialMeter = "super/superbarone.png",
     specialMeterDots = "super/filltwo.png",
     specialMeterOutline = "super/supoutone.png";

   public SPFZStageImagePack() {
   }

   public Texture getHealthTexture() {
      return new Texture(Gdx.files.internal(health));
   }

   public Texture getHealthOutlineTexture() {
      return new Texture(Gdx.files.internal(healthOutline));
   }

   public Texture getSpecialMeterTexture() {
      return new Texture(Gdx.files.internal(specialMeter));
   }

   public Texture getSpecialMeterDotsTexture() {
      return new Texture(Gdx.files.internal(specialMeterDots));
   }

   public Texture getSpecialMeterOutlineTexture() {
      return new Texture(Gdx.files.internal(specialMeterOutline));
   }
}
