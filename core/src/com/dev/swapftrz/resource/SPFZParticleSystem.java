package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.TransformComponent;

import java.util.Arrays;

public class SPFZParticleSystem extends IteratingSystem
{

	private ComponentMapper<SPFZParticleComponent> particleComponentMapper = ComponentMapper
			.getFor(SPFZParticleComponent.class);
	private ComponentMapper<TransformComponent> transformComponentMapper = ComponentMapper
			.getFor(TransformComponent.class);

	private boolean contin = false;
	private int iter;
	private int check = 0;
	private String currentscene;
	private SPFZParticleComponent particleComponent;
	private TransformComponent transcomp;
	private Array<PooledEffect> effects = new Array();
	//continuous partile effect scenes: sceneone and landscene

	public SPFZParticleSystem()
	{
		super(Family.all(SPFZParticleComponent.class).get());
	}

	public SPFZParticleSystem(String currentscene)
	{
		super(Family.all(SPFZParticleComponent.class).get());
		this.currentscene = currentscene;
		if (Arrays.asList("landscene", "sceneone").contains(currentscene))
		{
			contin = true;
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{

		particleComponent = particleComponentMapper.get(entity);
		transcomp = transformComponentMapper.get(entity);

		// logic adds the 4 particle necessary to reflect infinite particles
		iter = 0;

		for (PooledEffect effect : particleComponent.pooledeffects)
		{
			if (contin)
			{
				if (particleComponent.partsadded)
				{
					effect.getEmitters().get(0).durationTimer = 0;
				}
			  if (currentscene == "landscene" || currentscene == "sceneone")
				{

					if (!particleComponent.partsadded)
					{
						if (!particleComponent.addnew && iter == 0)

						{
							if (effect.getEmitters().get(0).durationTimer >= effect.getEmitters().get(0).duration * .25f)
							{
								PooledEffect effectx = particleComponent.particlepool.obtain();
								effectx.setPosition(transcomp.x, transcomp.y);
								particleComponent.pooledeffects.add(effectx);
								particleComponent.addnew = true;
							}
						}
						if (!particleComponent.addnew2 && iter == 0)

						{
							if (effect.getEmitters().get(0).durationTimer >= effect.getEmitters().get(0).duration * .5f)
							{
								PooledEffect effectx = particleComponent.particlepool.obtain();
								effectx.setPosition(transcomp.x, transcomp.y);
								particleComponent.pooledeffects.add(effectx);
								particleComponent.addnew2 = true;
							}
						}
						if (!particleComponent.addnew3 && iter == 0)

						{
							if (effect.getEmitters().get(0).durationTimer >= effect.getEmitters().get(0).duration * .75f)
							{
								PooledEffect effectx = particleComponent.particlepool.obtain();
								effectx.setPosition(transcomp.x, transcomp.y);
								particleComponent.pooledeffects.add(effectx);
								particleComponent.addnew3 = true;
								particleComponent.partsadded = true;
							}
						}
					}

				}
				iter++;
			}
			else
			{
				if (!particleComponent.init)
				{
					particleComponent.pooledeffects.removeValue(effect, true);
					effect.free();
					particleComponent.init = true;
				}

			}
			if (currentscene != "stageselscene")
			{

				if (effect.isComplete())
				{
					Complete(effect);
				}
			}
		}

	}
	
	private void Complete(PooledEffect effect)
	{
			particleComponent.pooledeffects.removeValue(effect, true);
			effect.free();
			if (contin)
			{
				PooledEffect effectx = particleComponent.particlepool.obtain();
				effectx.setPosition(transcomp.x, transcomp.y);
				particleComponent.pooledeffects.add(effectx);
			}
		
	}
	
	public void setscene(String scene)
	{
		currentscene = scene;
		
		if (currentscene == "landscene" || currentscene == "sceneone")
		{
			contin = true;
		}
		else
		{
			contin = false;
		}
	}
	

}
