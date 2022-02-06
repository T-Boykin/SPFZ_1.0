package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class SPFZParticleComponent implements Component
{
	//need to add some form of processing to check if particle needs to process 
	//when it is within the viewport
	
	public Entity entity;
	public String particleName = "";
	public String SceneName = "";
	public ParticleEffect particleEffect;
	public float worldMultiplyer = 1f;
	private float scaleFactor = 1f;
	public ParticleEffectPool particlepool;
	public PooledEffect peffect;
	public boolean addnew, addnew2, addnew3, partsadded, init;
	
	
	public Array<PooledEffect> pooledeffects = new Array();
	
	public boolean[] startnext = {false, false, false};
	
	//public Array<Integer> timers = new Array();
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper.getFor(TransformComponent.class);
	public float freetimer = 1.2f;
	public float outputtimer = 1f;
	public SPFZParticleComponent(Entity entity)
	{
		this.entity = entity;
	}

	public void scaleEffect(float scale)
	{
		scaleFactor = scale;
		particleEffect.scaleEffect(scaleFactor * worldMultiplyer);
		
		
	}

	public float getScaleFactor()
	{
		return scaleFactor;
	}

	public void setParticle()
	{
		particlepool = new ParticleEffectPool(particleEffect, 0, 10);
		PooledEffect pooledeffect = particlepool.obtain();
		particleEffect.dispose();
		TransformComponent transcomponent = transformComponentMapper.get(entity);
		pooledeffect.setPosition(transcomponent.x, transcomponent.y);
	
		pooledeffects.add(pooledeffect);
	
	}
	// please use this method to start effects for the scale to be applied
	public void startEffect()
	//public void startEffect(PooledEffect effect)
	{
		//pooledeffects.removeValue(effect, true);
		//effect.free();
		PooledEffect effectx = particlepool.obtain();
		TransformComponent transcomponent = transformComponentMapper.get(entity);
		effectx.setPosition(transcomponent.x, transcomponent.y);
		pooledeffects.add(effectx);
		
	}
	
	public void setscenename()
	{
		
	}
}
