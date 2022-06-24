package com.dev.swapftrz;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dev.swapftrz.device.AndroidInterfaceLIBGDX;
import com.dev.swapftrz.menu.SPFZMenu;
import com.dev.swapftrz.resource.SPFZCharButtonSystem;
import com.dev.swapftrz.resource.SPFZParticleDrawableLogic;
import com.dev.swapftrz.resource.SPFZResourceManager;
import com.dev.swapftrz.resource.SPFZSceneLoader;
import com.dev.swapftrz.stage.SPFZStage;
import com.dev.swapftrz.stage.SPFZStageSystem;
import com.uwsoft.editor.renderer.components.ActionComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class SwapFyterzMain extends ApplicationAdapter implements InputProcessor, GestureListener
{
  private final SPFZResourceManager resourceManager;
  private final AndroidInterfaceLIBGDX android;
  private final SPFZMenu spfzmenu;

  boolean transition, flingup, flingdown, optionsup, adjustbright, adjustsound, credpress;
  ComponentMapper<ActionComponent> ac = ComponentMapper.getFor(ActionComponent.class);

  float soundamount, brightamount;

  int INTRO = 0;

  GestureDetector gd;
  InputMultiplexer im;

  // ItemWrapper grabs all of the entities created within the Overlap2d
  // Application
  ItemWrapper root;

  SPFZSceneLoader port, land;
  SPFZCharButtonSystem spfzbsystem;
  SPFZStageSystem stagesystem;
  SPFZParticleDrawableLogic logic;
  // Stage needed for Controls when within the fight interface
  SPFZStage stage;
  public SPFZState state;
  Sprite texhel1, texhel2;

  //ShaderProgram shaderProgram;

  String view;

  // String[] opponents = { "spriteball", "spriteballred", "spriteballblack",
  // "spriteblock", "redblotch", "spritepurplex",
  // "walksprite" };

  // String[] stages = { "halloweenstage", "cathedralstage", "clubstage",
  // "egyptstage", "futurestage", "gargoyle",
  // "junglestage", "skullstage", "undergroundstage" };

  //TransformComponent transform;

  Viewport viewportland, viewportport;

  // Default Constructor - mainly for Desktop setup
  public SwapFyterzMain() {
    resourceManager = new SPFZResourceManager();
    spfzmenu = new SPFZMenu(resourceManager);
    android = null;
  }

  //Constructor set to receive the custom interfacing for Android System
  //Utilization
  public SwapFyterzMain(AndroidInterfaceLIBGDX tools, SPFZResourceManager resManager) {
    resourceManager = resManager;
    android = tools;
    spfzmenu = new SPFZMenu(resourceManager, android);
  }

  @Override
  public void create() {


  }

  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    // The fling process controls when the user can go to the credits or back to
    // the main menu
    // Current process will allow you to fling up and down only when the user is
    // at the main menu,
    // and if the user has not pressed any of the constellations.

    if (!optionsup && view == "landscape" && ac != null && resourceManager.currentScene() == "landscene")
    {
      if (velocityY > 1000f && !credpress && !transition)
      {
        flingup = true;
        flingdown = false;
      }
      else if (velocityY < -1000f && !credpress && !transition)
      {
        flingdown = true;
        flingup = false;
      }
    }
    return true;
  }

  public void init() {
    state = SPFZState.RUNNING;

    // set screen for inputs and set the starting point for the main menu
    // screen
    // music and start music
    //mainmenu.setPosition(AFTER_INTRO);
    resourceManager.setManager(resourceManager.getManager());
    // initialize the resource manager

    // spfzbsystem = new SPFZButtonSystem(this);
    spfzbsystem = new SPFZCharButtonSystem();
    stagesystem = new SPFZStageSystem();

    //resourceManager = new SPFZResourceManager(this);
    // initialize viewports
    //viewportland = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT);
    //viewportport = new StretchViewport(WORLD_HEIGHT, WORLD_WIDTH);
    //resourceManager.currentScene() = "landscene";

    //port = new SPFZSceneLoader(resourceManager, this, "", "");
    //land = new SPFZSceneLoader(resourceManager, this, "", "");
    //pause = new SPFZSceneLoader(resourceManager, this, "", "");
    logic = (SPFZParticleDrawableLogic) land.renderer.drawableLogicMapper.getDrawable(7);

    //}

    // Set the GestureDetector in order to utilize the camera lerping function
    // for Credits
    im = new InputMultiplexer();
    gd = new GestureDetector(this);

    im.addProcessor(gd);
    im.addProcessor(this);
    // initialize all resources

    resourceManager.initAllResources();
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean longPress(float x, float y) {

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {

    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }

  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {

    return false;
  }

  @Override
  public boolean panStop(float x, float y, int pointer, int button) {

    return false;
  }

  @Override
  public void pause() {
  }

  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

    return false;
  }

  @Override
  public void pinchStop() {

  }

  public void render() {
    //clearing for next frame
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  public void resize(int width, int height) {
  }

  public void resume() {
    //after pause logic
  }

  // ------------- END OF CODE BEFORE GESTURE DETECTOR METHODS
  // ----------------------- //[

  //prepopulated objects for testing
  /*public void preload() {
    selectedStage = "halloweenstage";
    p1char1 = "spriteballred";
    p1char2 = "spriteballblack";
    p1char3 = "spritepurplex";
    p2char1 = "spriteballred";
    p2char2 = "spriteballblack";
    p2char3 = "spritepurplex";

    characters.set(0, p1char1);
    characters.set(1, p1char2);
    characters.set(2, p1char3);
    characters.set(3, p2char1);
    characters.set(4, p2char2);
    characters.set(5, p2char3);
    istraining = true;
  }*/

  @Override
  public boolean tap(float x, float y, int count, int button) {

    return false;
  }

  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    //Slider processing
    if (optionsup)
    {
      TransformComponent transcomp = new TransformComponent();
      TransformComponent transcomponent = new TransformComponent();
      DimensionsComponent dimcompon = new DimensionsComponent();
      DimensionsComponent dimcomponent = new DimensionsComponent();
      Vector2 dimwh = new Vector2();
      Vector2 dimwhs = new Vector2();
      Vector3 vec3 = new Vector3();
      Vector3 transpar = new Vector3(0, 0, 0);
      String option = new String();

      if (view == "portrait")
      {
        option = "optionscreen";
      }
      else
      {
        option = "optdialog";
      }

      if (adjustbright)
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          DimensionsComponent.class);
      }
      else
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          DimensionsComponent.class);
      }
      if (adjustsound)
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("soundslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("soundbar").getEntity(),
          DimensionsComponent.class);
      }
      else
      {
        transcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          TransformComponent.class);
        dimcomponent = ComponentRetriever.get(root.getChild(option).getChild("brightslider").getEntity(),
          DimensionsComponent.class);
        transcomp = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          TransformComponent.class);
        dimcompon = ComponentRetriever.get(root.getChild(option).getChild("brightbar").getEntity(),
          DimensionsComponent.class);
      }

      transpar.x = ComponentRetriever.get(root.getChild(option).getEntity(), TransformComponent.class).x;
      transpar.y = ComponentRetriever.get(root.getChild(option).getEntity(), TransformComponent.class).y;

      int MAX_VOL = 1;
      int MAX_BRIGHT = 255;
      float fullbarpercent;


      if (view == "portrait")
      {
        viewportport.getCamera().update();
        vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewportport.unproject(vec3);
      }
      else
      {
        viewportland.getCamera().update();
        vec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewportland.unproject(vec3);
      }

      if (adjustbright || adjustsound)
      {
        dimwh.x = dimcomponent.width * transcomponent.scaleX;
        dimwh.y = dimcomponent.height * transcomponent.scaleY;
        dimwhs.x = dimcompon.width * transcomp.scaleX;
        dimwhs.y = dimcompon.height * transcomp.scaleY;

        // bar full percentage
        fullbarpercent = dimwhs.x;

        if (transcomponent.x + transpar.x >= (transcomp.x + transpar.x - (dimwh.x * .5f))
          && (transpar.x + transcomponent.x + (dimwh.x * .5f)) <= (transpar.x + transcomp.x + dimwhs.x))
        {
          // transcomponent.x = (vec3.x - (dimwh.x / 2)) - transpar.x;
          transcomponent.x = (vec3.x - (dimwh.x * .5f)) - transpar.x;
        }

        // If the Slider(transcomponent.x + parent entity.x) value is less
        // than
        // the beginning of the bar(transcomp.x + parent entity.x),
        // set the slider to the beginning of the bar

        //if (transcomponent.x + transpar.x < (transcomp.x + transpar.x - (dimwh.x * .5f)))
        if (transcomponent.x + transpar.x < (transcomp.x + transpar.x - (dimwh.x * .5f)))
        {
          // transcomponent.x = transcomp.x - (dimwh.x / 2);
          transcomponent.x = transcomp.x - (dimwh.x * .5f);
        }

        // If the end of the Slider(transcomponent.x + parent entity.x +
        // slider
        // width) value is less than the end of the bar(transcomp.x + parent
        // entity.x + bar width),
        // set the slider to the end of the slider to the end of the bar

        if ((transpar.x + transcomponent.x + (dimwh.x * .5f)) > (transpar.x + transcomp.x + dimwhs.x))
        {
          transcomponent.x = transcomp.x + dimwhs.x - (dimwh.x * .5f) - .5f;
        }

        // Adjust brightness or sound with the new slider value

        if (adjustbright)
        {
          brightamount = 100 * (((dimwh.x * .5f) + transcomponent.x) - transcomp.x) / fullbarpercent;

          brightamount = (brightamount * .01f) * MAX_BRIGHT;


          if (brightamount >= 5f && brightamount <= MAX_BRIGHT)
          {

            //adjustBrightness(brightamount);
          }
        }

        if (adjustsound)
        {
          soundamount = 100 * (((dimwh.x * .5f) + transcomponent.x) - transcomp.x) / fullbarpercent;

          soundamount = (soundamount * .01f) * MAX_VOL;


          if (soundamount >= 0f && soundamount <= MAX_VOL)
          {
            //mainmenu.setVolume(soundamount);
          }
        }

      }
    }
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return true;
  }

  /**
   * Method returns SceneLoader based on Screen orientation
   */
  public SPFZSceneLoader update(String orientation) {
    if (orientation == ("portrait"))
    {
      return port;
    }
    else
    {
      return land;
    }
  }

  @Override
  public boolean zoom(float initialDistance, float distance) {

    return false;
  }

  public SPFZResourceManager resourceManager() {
    return resourceManager;
  }
}
