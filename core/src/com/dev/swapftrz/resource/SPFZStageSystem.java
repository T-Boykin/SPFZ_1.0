package com.dev.swapftrz.resource;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;

public class SPFZStageSystem extends IteratingSystem
{

	public SPFZStageSystem(Family family)
	{
		super(Family.all(SPFZStageComponent.class).get());

	}
	public SPFZStageSystem()
	{
		super(Family.all(SPFZStageComponent.class).get());
		
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);

		if (nodeComponent == null)
			return;

		for (int i = 0; i < nodeComponent.children.size; i++)
		{
			Entity childEntity = nodeComponent.children.get(i);
			MainItemComponent childMainItemComponent = ComponentRetriever.get(childEntity, MainItemComponent.class);
			childMainItemComponent.visible = true;
		}

		ViewPortComponent camera = ComponentRetriever.get(entity, ViewPortComponent.class);
		if (camera != null)
		{
			// if camera is on this entity, then it should not be processed
			return;
		}

		for (int i = 0; i < nodeComponent.children.size; i++)
		{
			Entity childEntity = nodeComponent.children.get(i);
			MainItemComponent childMainItemComponent = ComponentRetriever.get(childEntity, MainItemComponent.class);
			ZIndexComponent childZComponent = ComponentRetriever.get(childEntity, ZIndexComponent.class);
			if (isTouched(entity))
			{
				if (childZComponent.layerName.equals("normal"))
				{
					childMainItemComponent.visible = false;
				}
				if (childZComponent.layerName.equals("pressed"))
				{
					childMainItemComponent.visible = true;
				}
			}
			else
			{
				if (childZComponent.layerName.equals("normal"))
				{
					childMainItemComponent.visible = true;
				}
				if (childZComponent.layerName.equals("pressed"))
				{
					childMainItemComponent.visible = false;
				}
			}
		}

	}

	private boolean isTouched(Entity entity)
	{
		SPFZStageComponent buttonComponent = entity.getComponent(SPFZStageComponent.class);
		//Implementing multiple touch
		for (int i = 0; i < 2; i++)
		{
			if (Gdx.input.isTouched(i))
			{
				DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
				Vector2 localCoordinates = new Vector2(Gdx.input.getX(i), Gdx.input.getY(i));

				TransformMathUtils.globalToLocalCoordinates(entity, localCoordinates);

				if (dimensionsComponent.hit(localCoordinates.x, localCoordinates.y))
				{
					buttonComponent.setTouchState(true);
					return true;
				}
			}
			buttonComponent.setTouchState(false);
		}
		return false;
	}
}
