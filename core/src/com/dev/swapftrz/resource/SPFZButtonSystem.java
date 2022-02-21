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
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.TransformMathUtils;

public class SPFZButtonSystem extends IteratingSystem
{
   /* Rules of setting processing.
    * 1. processing boolean should be set prior to animation execution
    * 2. processing boolean should be unset when UI elements are at a point of being understood by the user
    * */
   private boolean processing = false;

   public SPFZButtonSystem() {
      super(Family.all(SPFZButtonComponent.class).get());
   }

   @Override
   protected void processEntity(Entity entity, float deltaTime) {
      NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);

      if (nodeComponent == null) return;

      for (Entity child : nodeComponent.children)
      {
         MainItemComponent childMainItemComponent = ComponentRetriever.get(child, MainItemComponent.class);
         childMainItemComponent.visible = true;
      }

      ViewPortComponent camera = ComponentRetriever.get(entity, ViewPortComponent.class);

      if (camera != null) return;

      if (!processing)
         processLayers(nodeComponent, entity);
   }

   private void processLayers(NodeComponent nodeComponent, Entity entity) {
      for (Entity child : nodeComponent.children)
      {
         MainItemComponent childMainItemComponent = ComponentRetriever.get(child, MainItemComponent.class);
         ZIndexComponent childZComponent = ComponentRetriever.get(child, ZIndexComponent.class);
         if (isTouched(entity))
         {
            if (childZComponent.layerName.equals("normal"))
               childMainItemComponent.visible = false;

            if (childZComponent.layerName.equals("pressed"))
               childMainItemComponent.visible = true;
         }
         else
         {
            if (childZComponent.layerName.equals("normal"))
               childMainItemComponent.visible = true;

            if (childZComponent.layerName.equals("pressed"))
               childMainItemComponent.visible = false;
         }
      }
   }

   private boolean isTouched(Entity entity) {
      SPFZButtonComponent buttonComponent = entity.getComponent(SPFZButtonComponent.class);
      if (Gdx.input.isTouched())
      {
         DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
         Vector2 localCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());

         TransformMathUtils.globalToLocalCoordinates(entity, localCoordinates);

         if (dimensionsComponent.hit(localCoordinates.x, localCoordinates.y))
         {
            buttonComponent.setTouchState(true);
            return true;
         }
      }
      buttonComponent.setTouchState(false);
      return false;
   }

   public boolean isProcessing() {
      return processing;
   }

   @Override
   public void setProcessing(boolean processing) {
      this.processing = processing;
   }
}
