package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.factory.component.ComponentFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import box2dLight.RayHandler;

public class SPFZParticleEffectComponentFactory extends ComponentFactory {


  public SPFZParticleEffectComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
      super(rayHandler, world, rm);
  }

  @Override
  public void createComponents(Entity root, Entity entity, MainItemVO vo) {
      createCommonComponents(entity, vo, EntityFactory.PARTICLE_TYPE);
      createParentNodeComponent(root, entity);
      createNodeComponent(root, entity);
      createParticleComponent(entity, (ParticleEffectVO) vo);
  }

  @Override
  protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
      DimensionsComponent component = new DimensionsComponent();

      ProjectInfoVO projectInfoVO = rm.getProjectVO();
      float boundBoxSize = 70f;
      component.boundBox = new Rectangle((-boundBoxSize/2f)/projectInfoVO.pixelToWorld, (-boundBoxSize/2f)/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld, boundBoxSize/projectInfoVO.pixelToWorld);

      entity.add(component);
      return component;
  }

  protected SPFZParticleComponent createParticleComponent(Entity entity, ParticleEffectVO vo) 
  {
     // ParticleComponent component = new ParticleComponent();
  	  SPFZParticleComponent component = new SPFZParticleComponent(entity);
      component.particleName = vo.particleName;
	    ParticleEffect particleEffect = new ParticleEffect(rm.getParticleEffect(vo.particleName));
      component.particleEffect = particleEffect;
      ProjectInfoVO projectInfoVO = rm.getProjectVO();
      component.worldMultiplyer = 1f/projectInfoVO.pixelToWorld;
      component.scaleEffect(1f);
      component.setParticle();

      entity.add(component);
      return component;
  }
}
