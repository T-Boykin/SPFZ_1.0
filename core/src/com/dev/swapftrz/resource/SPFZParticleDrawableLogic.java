package com.dev.swapftrz.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.systems.render.logic.Drawable;

public class SPFZParticleDrawableLogic implements Drawable
{
	public String currScene;

	private ComponentMapper<SPFZParticleComponent> particleComponentMapper = ComponentMapper
			.getFor(SPFZParticleComponent.class);
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper
			.getFor(TransformComponent.class);
	private Array<Float> timers = new Array();
	List<SPFZParticleComponent> particlelist = new ArrayList<SPFZParticleComponent>();
	List<TransformComponent> transformlist = new ArrayList<TransformComponent>();
	protected HashMap<String, SPFZParticleComponent> particlehash = new HashMap<String, SPFZParticleComponent>();
	SPFZParticleComponent particleComponent;
	TransformComponent transformComponent;
	PooledEffect peffect;

	public SPFZParticleDrawableLogic()
	{
	}

	@Override
	public void draw(Batch batch, Entity entity, float parentAlpha)
	{
		particleComponent = particleComponentMapper.get(entity);
			
		if (particleComponent.pooledeffects.size > 0)
		{
			transformComponent = transformComponentMapper.get(entity);

			
			for (PooledEffect effect : particleComponent.pooledeffects)
			{

				effect.setPosition(transformComponent.x, transformComponent.y);
				effect.update(Gdx.graphics.getDeltaTime());

				effect.draw(batch);
				if (particleComponent.partsadded)
				{
					effect.getEmitters().get(0).durationTimer = 0;
				}

			}

			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		}
/*		Gdx.app.log("name: " + particleComponent.particleName,
				"active: " + particleComponent.pooledeffects.size + " | back in pool: "
						+ particleComponent.particlepool.getFree() + "/" + particleComponent.particlepool.max
						+ " | highest number of pool: " + particleComponent.particlepool.peak);
*/
	}

	public SPFZParticleDrawableLogic getpartdraw()
	{
		return this;
	}

	public void setscene(String scene)
	{
		currScene = scene;
	}

}
