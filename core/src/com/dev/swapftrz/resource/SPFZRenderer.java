package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.commons.IExternalItemType;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.ArrayList;
import java.util.List;

import box2dLight.RayHandler;

public class SPFZRenderer extends IteratingSystem
{

	private boolean mapfilled;
	private int iterator;
	Camera camera;
	private final float TIME_STEP = 1f / 60;

	private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
	private ComponentMapper<CompositeTransformComponent> compositeTransformMapper = ComponentMapper
			.getFor(CompositeTransformComponent.class);
	private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	private ComponentMapper<ParentNodeComponent> parentNodeMapper = ComponentMapper.getFor(ParentNodeComponent.class);
	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<DimensionsComponent> dimensionMapper = ComponentMapper.getFor(DimensionsComponent.class);
	private ComponentMapper<MainItemComponent> mainItemComponentMapper = ComponentMapper.getFor(MainItemComponent.class);

	//MainItemComponent childMainItemComponent;
	//CompositeTransformComponent curCompositeTransformComponent;
	//TransformComponent transform;
    ViewPortComponent viewportcompnent;

	private List<ViewPortComponent> viewportlist = new ArrayList<ViewPortComponent>();
	private List<CompositeTransformComponent> comptranslist = new ArrayList<CompositeTransformComponent>();
	private List<NodeComponent> nodelist = new ArrayList<NodeComponent>();
	private List<ParentNodeComponent> parentnodelist = new ArrayList<ParentNodeComponent>();
	private List<TransformComponent> transformlist = new ArrayList<TransformComponent>();
	private List<DimensionsComponent> dimensionslist = new ArrayList<DimensionsComponent>();
	private List<MainItemComponent> mainitemlist = new ArrayList<MainItemComponent>();

	// private DrawableLogicMapper drawableLogicMapper;
	public SPFZDrawableLogicMapper drawableLogicMapper;
	private RayHandler rayHandler;

	private ParticleEffectPool sceneburnpool;
	private Array<PooledEffect> effects = new Array();

	private String fragmentshader, vertexshader;
	private ShaderProgram shaderProgram;

	// private World world;

	// private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public static float timeRunning = 0;

	public Batch batch;

	public SPFZRenderer(Batch batch, Camera camera)
	{
		super(Family.all(ViewPortComponent.class).get());
		this.batch = batch;
		this.camera = camera;
		vertexshader = Gdx.files.internal("vert.glsl").readString();
		fragmentshader = Gdx.files.internal("frag.glsl").readString();
		shaderProgram = new ShaderProgram(vertexshader, fragmentshader);

		// drawableLogicMapper = new DrawableLogicMapper();
		drawableLogicMapper = new SPFZDrawableLogicMapper();
	}

	public void addDrawableType(IExternalItemType itemType)
	{
		drawableLogicMapper.addDrawableToMap(itemType.getTypeId(), itemType.getDrawable());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime)
	{
		timeRunning += deltaTime;

		viewportcompnent = viewPortMapper.get(entity);
		/*if(!mapfilled)
		{
			
			if(viewportlist.contains(viewportcompnent))
			{
				mapfilled = true;
				iterator = 0;
			}
			else
			{
				viewportlist.add(viewportcompnent);
			}
		}
		else
		{
			viewportcompnent = viewportlist.get(iterator);
		}*/
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		/*batch.setShader(shaderProgram);
		batch.getShader().setUniformf("deltaTime", Gdx.graphics.getDeltaTime());
		batch.getShader().setUniformf("time", SPFZRenderer.timeRunning);

		GL20 gl = Gdx.gl20;
		int error;
		if ((error = gl.glGetError()) != GL20.GL_NO_ERROR)
		{
			Gdx.app.log("opengl", "Error: " + error);
			Gdx.app.log("opengl", batch.getShader().getLog());
		}*/
		//shaderprogram.unifo
		drawRecursively(entity, 1f);
		batch.end();

		// maybe?
		if (rayHandler != null)
		{
			rayHandler.setCulling(true);
			// OrthographicCamera orthoCamera = (OrthographicCamera) camera;
			camera.combined.scl(1f / PhysicsBodyLoader.getScale());
			// rayHandler.setCombinedMatrix(orthoCamera);
			rayHandler.setCombinedMatrix((OrthographicCamera) camera);
			rayHandler.updateAndRender();
		}

		/*if(mapfilled)
		{
			if(iterator == viewportlist.size() - 1)
			{
				iterator = 0;
			}
			else
			{
				iterator++;
			}
		}*/
		// debugRenderer.render(world, camera.combined);
	}

	private void drawRecursively(Entity rootEntity, float parentAlpha)
	{

		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		TransformComponent transform = transformMapper.get(rootEntity);
		// currentComposite = rootEntity;
		/*
		 * if(!mapfilled) {
		 * 
		 * if(comptranslist.contains(curCompositeTransformComponent)) { mapfilled =
		 * true; iterator = 0; } else {
		 * comptranslist.add(curCompositeTransformComponent);
		 * comptranslist.add(curCompositeTransformComponent); mapfilled = false; } }
		 * else { curCompositeTransformComponent = comptranslist.get(iterator); }
		 */

		if (curCompositeTransformComponent.transform || transform.rotation != 0 || transform.scaleX != 1
				|| transform.scaleY != 1)
		{
			// MainItemComponent childMainItemComponent =
			// mainItemComponentMapper.get(rootEntity);
			// childMainItemComponent = mainItemComponentMapper.get(rootEntity);
			computeTransform(rootEntity);

			applyTransform(rootEntity, batch);
		}
		TintComponent tintComponent = ComponentRetriever.get(rootEntity, TintComponent.class);
		parentAlpha *= tintComponent.color.a;

		if (parentAlpha != 0.0f)
		{
			drawChildren(rootEntity, batch, curCompositeTransformComponent, parentAlpha);
			if (curCompositeTransformComponent.transform || transform.rotation != 0 || transform.scaleX != 1
					|| transform.scaleY != 1)
				resetTransform(rootEntity, batch);
		}
	}

	private void drawChildren(Entity rootEntity, Batch batch, CompositeTransformComponent curCompositeTransformComponent,
			float parentAlpha)
	{
		NodeComponent nodeComponent = nodeMapper.get(rootEntity);
		Entity[] children = nodeComponent.children.begin();
		TransformComponent transform = transformMapper.get(rootEntity);
		if (curCompositeTransformComponent.transform || transform.rotation != 0 || transform.scaleX != 1
				|| transform.scaleY != 1)
		{
			for (int i = 0, n = nodeComponent.children.size; i < n; i++)
			{
				Entity child = children[i];

				LayerMapComponent rootLayers = ComponentRetriever.get(rootEntity, LayerMapComponent.class);
				ZIndexComponent childZIndexComponent = ComponentRetriever.get(child, ZIndexComponent.class);

				if (!rootLayers.isVisible(childZIndexComponent.layerName))
				{
					continue;
				}

				MainItemComponent childMainItemComponent = mainItemComponentMapper.get(child);
				if (!childMainItemComponent.visible)
				{
					continue;
				}

				int entityType = childMainItemComponent.entityType;

				NodeComponent childNodeComponent = nodeMapper.get(child);
				TransformComponent transformc = transformMapper.get(child);

				if (childNodeComponent == null)
				{
					drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
				}
				else
				{
					// Step into Composite
					drawRecursively(child, parentAlpha);
				}
			}
		}
		else
		{
			// No transform for this group, offset each child.
			TransformComponent compositeTransform = transformMapper.get(rootEntity);

			float offsetX = compositeTransform.x, offsetY = compositeTransform.y;

			if (viewPortMapper.has(rootEntity))
			{
				offsetX = 0;
				offsetY = 0;
			}

			for (int i = 0, n = nodeComponent.children.size; i < n; i++)
			{
				Entity child = children[i];

				LayerMapComponent rootLayers = ComponentRetriever.get(rootEntity, LayerMapComponent.class);
				ZIndexComponent childZIndexComponent = ComponentRetriever.get(child, ZIndexComponent.class);

				if (!rootLayers.isVisible(childZIndexComponent.layerName))
				{
					continue;
				}

				MainItemComponent childMainItemComponent = mainItemComponentMapper.get(child);
				if (!childMainItemComponent.visible)
				{
					continue;
				}

				TransformComponent childTransformComponent = transformMapper.get(child);
				// DimensionsComponent dimensionComponent = dimensionMapper.get(child);
				float cx = childTransformComponent.x, cy = childTransformComponent.y;
				childTransformComponent.x = cx + offsetX;
				childTransformComponent.y = cy + offsetY;

				NodeComponent childNodeComponent = nodeMapper.get(child);
				int entityType = mainItemComponentMapper.get(child).entityType;

				if (childNodeComponent == null)
				{
					// Find the logic from mapper and draw it
					/*
					 * if (childMainItemComponent.entityType ==
					 * EntityFactory.PARTICLE_TYPE) {
					 * child.getComponent(SPFZParticleComponent.class).particleEffect.
					 * setEmittersCleanUpBlendFunction(false);
					 * 
					 * drawableLogicMapper.getDrawable(entityType).draw(batch, child,
					 * parentAlpha);
					 * 
					 * batch.setBlendFunction(GL20.GL_SRC_ALPHA,
					 * GL20.GL_ONE_MINUS_SRC_ALPHA);
					 * 
					 * } else {
					 */
					drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
					// }
				}
				else
				{
					// Step into Composite
					drawRecursively(child, parentAlpha);
				}
				childTransformComponent.x = cx;
				childTransformComponent.y = cy;
			}
		}
		nodeComponent.children.end();
	}

	/**
	 * Returns the transform for this group's coordinate system.
	 * 
	 * @param rootEntity
	 */
	protected Matrix4 computeTransform(Entity rootEntity)
	{
		float originX = 0;
		float originY = 0;
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		// NodeComponent nodeComponent = nodeMapper.get(rootEntity);
	  MainItemComponent childMainItemComponent = mainItemComponentMapper.get(rootEntity);
		ParentNodeComponent parentNodeComponent = parentNodeMapper.get(rootEntity);
		TransformComponent curTransform = transformMapper.get(rootEntity);
		Affine2 worldTransform = curCompositeTransformComponent.worldTransform;

		

		
		//See if there is a way to change the origin of the round circles
		
		
		//float originX = curTransform.x / 2;
		//float originY = curTransform.y / 2;
		
		originX = curTransform.originX;
		originY = curTransform.originY;
		float x = curTransform.x;
		float y = curTransform.y;
		float rotation = curTransform.rotation;
		float scaleX = curTransform.scaleX;
		float scaleY = curTransform.scaleY;

		worldTransform.setToTrnRotScl(x + originX, y + originY, rotation, scaleX, scaleY);
		if (originX != 0 || originY != 0)
			worldTransform.translate(-originX, -originY);

		// Find the first parent that transforms.

		CompositeTransformComponent parentTransformComponent = null;
		// NodeComponent parentNodeComponent;

		Entity parentEntity = null;
		if (parentNodeComponent != null)
		{
			parentEntity = parentNodeComponent.parentEntity;
		}
		// if (parentEntity != null){
		//
		// }

		// while (parentEntity != null) {
		// parentNodeComponent = nodeMapper.get(parentEntity);
		// if (parentTransformComponent.transform) break;
		// System.out.println("Gand");
		// parentEntity = parentNodeComponent.parentEntity;
		// parentTransformComponent = compositeTransformMapper.get(parentEntity);
		//
		// }

		if (parentEntity != null)
		{
			parentTransformComponent = compositeTransformMapper.get(parentEntity);
			TransformComponent transform = transformMapper.get(parentEntity);
			if (curCompositeTransformComponent.transform || transform.rotation != 0 || transform.scaleX != 1
					|| transform.scaleY != 1)
				worldTransform.preMul(parentTransformComponent.worldTransform);
			// MainItemComponent main =
			// parentEntity.getComponent(MainItemComponent.class);
			// System.out.println("NAME " + main.itemIdentifier);
		}

		curCompositeTransformComponent.computedTransform.set(worldTransform);
		return curCompositeTransformComponent.computedTransform;
	}

	protected void applyTransform(Entity rootEntity, Batch batch)
	{
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		curCompositeTransformComponent.oldTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(curCompositeTransformComponent.computedTransform);
	}

	protected void resetTransform(Entity rootEntity, Batch batch)
	{
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		batch.setTransformMatrix(curCompositeTransformComponent.oldTransform);
	}

	public void setRayHandler(RayHandler rayHandler)
	{
		this.rayHandler = rayHandler;
	}

	// public void setBox2dWorld(World world) {
	// this.world = world;
	// }

	// this method has been left to avoid any compatibility issue
	// setPhysicsOn has been moved in PhysicsSystem class
	// Physics is now totally decoupled from rendering
	@Deprecated
	public void setPhysicsOn(boolean isPhysicsOn)
	{
		// empty
	}

	public Batch getBatch()
	{
		return batch;
	}
}
