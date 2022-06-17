package com.dev.swapftrz.resource;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Json;
import com.dev.swapftrz.menu.SPFZMenuCamera;
import com.dev.swapftrz.stage.SPFZStageCamera;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import com.uwsoft.editor.renderer.resources.IResourceLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SPFZResourceManager implements IResourceRetriever, IResourceLoader
{

  protected float resMultiplier;
  private final SPFZDBOperations dbOperations;
  private AssetManager assetManager = new AssetManager();
  // public boolean setup;
  // public int init = 0;
  private static final String packResolutionName = "orig";
  private static final String preferencesFile = "spfzfile";
  public final byte ANDROID = 0, DESKTOP = 1, IOS = 2;
  public static final String LANDSCAPE = "landscape";
  public static final String PORTRAIT = "portrait";

  //private final AndroidInterfaceLIBGDX android;

  private int appDevice;
  private String currentScene;
  // public String particleEffectsPath = "particles";

  // public String fontsPath = "freetypefonts";

  // public String[] menuscenes = { "landscene", "sceneone", "arcadeselscn",
  // "charselscene" };

  protected ProjectInfoVO projectVO = new ProjectInfoVO();
  protected String orientation;
  protected boolean inMenu;
  protected ArrayList<String> preparedSceneNames = new ArrayList<>();
  protected HashMap<String, SceneVO> loadedSceneVOs = new HashMap<>();
  protected HashSet<String> spriteAnimNamesToLoad = new HashSet<>();
  protected HashSet<String> particleEffectNamesToLoad = new HashSet<>();

  protected HashSet<FontSizePair> fontsToLoad = new HashSet<>();

  protected TextureAtlas mainPack;
  protected HashMap<String, ParticleEffect> particleEffects = new HashMap<>();

  protected HashMap<String, TextureAtlas> skeletonAtlases = new HashMap<>();
  protected HashMap<String, FileHandle> skeletonJSON = new HashMap<>();
  protected HashMap<String, TextureAtlas> spriteAnimations = new HashMap<>();
  protected HashMap<FontSizePair, BitmapFont> bitmapFonts = new HashMap<>();
  protected SPFZSceneLoader portraitSSL;
  protected SPFZSceneLoader landscapeSSL;
  protected SPFZSceneLoader stagePauseSSL;
  protected ItemWrapper rootWrapper;
  private SPFZMenuCamera spfzMCamera;
  private SPFZStageCamera spfzSCamera;

  //Resolutions
  //Map<String, Integer> resolutionWidth = new HashMap<String, Integer>();
  //Map<String, Integer> resolutionHeight = new HashMap<String, Integer>();

  // fontpairs = new ArrayList<String>(fontsToLoad.);
  List<String> particlenames;
  List<String> fontpairs;
  // private final Pool<ParticleEffect>

  // String fontpath;
  // String fontimagepath;
  // String partstr;
  // String invalidfnt = "Sim";
  // String invalidfnt2 = "Aha";
  // String invalidfnt3 = "LCD";

  public SPFZResourceManager() {
    projectVO.pixelToWorld = 3;
    portraitSSL = new SPFZSceneLoader(this);
    landscapeSSL = new SPFZSceneLoader(this);
    //stagePauseSL = new SPFZSceneLoader(this, appMain);
    setAppDevice();
    setWorkingResolution(packResolutionName);
    dbOperations = new SPFZDBOperations(this);
  }

  public void dispose() {
    mainPack.dispose();
  }

  @Override
  public BitmapFont getBitmapFont(String name, int size) {

    /*
     * for (FontSizePair pair : fontsToLoad) {
     *
     * //if(pair.fontName != "SimSun-ExtB") if(pair.fontName.equals(name) &&
     * pair.fontSize == size) { fontpair = pair; } } return
     * bitmapFonts.get(fontpair);
     */
    return bitmapFonts.get(new FontSizePair(name, size));
  }

  @Override
  public ResolutionEntryVO getLoadedResolution() {
    //if (packResolutionName.equals("orig"))
    //{
    //	return getProjectVO().originalResolution;
    //}

    return getProjectVO().getResolution(packResolutionName);
  }

  @Override
  public ParticleEffect getParticleEffect(String name) {

    return particleEffects.get(name);
  }

  @Override
  public ProjectInfoVO getProjectVO() {
    return projectVO;
  }

  @Override
  public SceneVO getSceneVO(String sceneName) {
    return loadedSceneVOs.get(sceneName);
  }

  @Override
  public FileHandle getSCMLFile(String name) {
    return null;
  }

  @Override
  public ShaderProgram getShaderProgram(String shaderName) {
    return null;
  }

  @Override
  public TextureAtlas getSkeletonAtlas(String name) {
    return skeletonAtlases.get(name);
  }

  @Override
  public FileHandle getSkeletonJSON(String name) {
    return skeletonJSON.get(name);
  }

  @Override
  public MySkin getSkin() {
    return null;
  }

  @Override
  public TextureAtlas getSpriteAnimation(String name) {
    return spriteAnimations.get(name);
  }

  @Override
  public TextureRegion getTextureRegion(String name) {
    return mainPack.findRegion(name);
  }

  /**
   * Easy use loader Iterates through all scenes and schedules all for loading
   * Prepares all the assets to be loaded that are used in scheduled scenes
   * finally loads all the prepared assets
   */
  public void initAllResources() {
    loadProjectVO();
    for (int i = 0; i < projectVO.scenes.size(); i++)
    {
      loadSceneVO(projectVO.scenes.get(i).sceneName);
      scheduleScene(projectVO.scenes.get(i).sceneName);
    }
    prepareAssetsToLoad();
    loadAssets();
    //loadSpriteAnimations();

  }

  public void initGame() {
    String[] scenes = {"landscene", "sceneone", "arcadeselscn"};

    loadProjectVO();
    for (String scene : scenes)
    {
      loadSceneVO(scene);
      scheduleScene(scene);
    }
    prepareAssetsToLoad();
    loadAssets();
  }

  public void unloadGame() {
    String[] scenes = {"landscene", "sceneone", "arcadeselscn"};

    for (String scene : scenes)
    {
      unLoadScene(scene);
    }
    loadAssets();
  }

  public void initarcade() {
    // arcade select
    // story
    String scene = "storyscene";

    // loadProjectVO();

    loadSceneVO(scene);
    scheduleScene(scene);

    prepareAssetsToLoad();
    loadAssets();
  }

  public void unloadarcade() {
    String scene = "storyscene";

    unLoadScene(scene);
    // loadAssets();

  }

  public void initsix() {
    // characterselect
    // stageselect
    String[] scenes = {"charselscene", "stageselscene"};

    // loadProjectVO();
    for (String scene : scenes)
    {
      loadSceneVO(scene);
      scheduleScene(scene);
    }
    prepareAssetsToLoad();
    loadAssets();

  }

  public void unloadsix() {
    String[] scenes = {"charselscene", "stageselscene"};

    for (String scene : scenes)
    {
      unLoadScene(scene);
    }
    loadAssets();
  }

  public void initstage() {
    // stage
    // pause
    String[] scenes = {"stagescene", "pausescene"};

    // loadProjectVO();
    for (String scene : scenes)
    {
      loadSceneVO(scene);
      scheduleScene(scene);
    }
    prepareAssetsToLoad();
    loadAssets();
  }

  public void unloadstage() {
    String[] scenes = {"stagescene", "pausescene"};

    for (String scene : scenes)
    {
      unLoadScene(scene);
    }
    loadAssets();
  }

  /**
   * Initializes scene by loading it's VO data object and loading all the assets
   * needed for this particular scene only
   *
   * @param sceneName - scene file name without ".dt" extension
   */
  public void initScene(String sceneName) {
    loadSceneVO(sceneName);
    scheduleScene(sceneName);
    prepareAssetsToLoad();
    // loadAssets(sceneName);
  }

  /**
   * Loads all the scheduled assets into memory including main atlas pack,
   * particle effects, sprite animations, spine animations and fonts
   */
  public void loadAssets() {
    loadAtlasPack();
    loadParticleEffects();
    loadSpriteAnimations();
    loadFonts();
    // loadShaders();

  }

  @Override
  public void loadAtlasPack() {
    FileHandle packFile = Gdx.files.internal(packResolutionName + File.separator + "pack.atlas");

    if (!packFile.exists())

    {
      return;
    }

    mainPack = new TextureAtlas(packFile);
  }

  public void loadFont(FontSizePair pair) {

    //FileHandle filehandle = Gdx.files
    //		.internal("bitmapfonts" + File.separator + pair.fontName + "_" + pair.fontSize + ".fnt");
    FileHandle filehandle = Gdx.files
      .internal("bitmapfonts" + File.separator + pair.fontName + pair.fontSize + File.separator
        + pair.fontName + "_" + pair.fontSize + ".fnt");

    BitmapFont font = new BitmapFont(filehandle, mainPack.findRegion(pair.fontName + pair.fontSize));

    bitmapFonts.put(pair, font);
  }

  @Override
  public void loadFonts() {
    String invalidfnt = "Sim";
    String invalidfnt2 = "Aha";
    // String invalidfnt3 = "LCD";

    // resolution related stuff
    ResolutionEntryVO curResolution = getProjectVO().getResolution(packResolutionName);
    resMultiplier = 1;
    if (!packResolutionName.equals("orig"))
    {

      if (curResolution.base == 0)
      {
        resMultiplier = (float) curResolution.width / (float) getProjectVO().originalResolution.width;
      }
      else
      {
        resMultiplier = (float) curResolution.height / (float) getProjectVO().originalResolution.height;
      }
    }

    // empty existing ones that are not scheduled to load

    for (FontSizePair pair : bitmapFonts.keySet())
    {
      if (!fontsToLoad.contains(pair))
      {

        if (!fontsToLoad.contains(invalidfnt))
        {

        }
      }
    }

    for (FontSizePair pair : fontsToLoad)
    {

      if (!pair.fontName.toLowerCase().contains(invalidfnt.toLowerCase())
        && (!pair.fontName.toLowerCase().contains(invalidfnt2.toLowerCase()) || pair.fontSize != 80))

      {
        loadFont(pair);
      }
    }

  }

  @Override
  public void loadParticleEffects() {
    String particleEffectsPath = "particles";
    // empty existing ones that are not scheduled to load
    particleEffects.clear();

    // need to load only the particle effects for the given scene

    // load scheduled
    for (String name : particleEffectNamesToLoad)
    {
      ParticleEffect effect = new ParticleEffect();
      effect.load(Gdx.files.internal(particleEffectsPath + File.separator + name), mainPack, "");
      particleEffects.put(name, effect);
    }
    particlenames = new ArrayList<>(particleEffects.keySet());
  }

  @Override
  public ProjectInfoVO loadProjectVO() {
    FileHandle filehandle = Gdx.files.internal("project.dt");
    Json json = new Json();
    projectVO = json.fromJson(ProjectInfoVO.class, filehandle.readString());

    return projectVO;
  }

  @Override
  public SceneVO loadSceneVO(String sceneName) {
    String scenesPath = "scenes";
    Json json = new Json();
    FileHandle filehandle = Gdx.files.internal(scenesPath + File.separator + sceneName + ".dt");
    SceneVO sceneVO = json.fromJson(SceneVO.class, filehandle.readString());

    loadedSceneVOs.put(sceneName, sceneVO);

    return sceneVO;
  }

  @Override
  public void loadShaders() {
  }

  @Override
  public void loadSpineAnimations() {
  }

  public void loadSpriteAnimations(String scene) {
    String spriteAnimationsPath = "sprite_animations";

    // empty existing ones that are not scheduled to load
    for (String key : spriteAnimations.keySet())
    {
      if (!spriteAnimNamesToLoad.contains(key))
      {
        spriteAnimations.remove(key);
      }
    }

    loadSpriteAnimOfScene();

    for (String name : spriteAnimNamesToLoad)
    {
      TextureAtlas animAtlas = new TextureAtlas(Gdx.files.internal(packResolutionName + File.separator
        + spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas"));
      spriteAnimations.put(name, animAtlas);
    }

  }

  // Method needed to load sprites from Library
  public void loadSpriteAnimOfScene() {
    for (SceneVO sceneVO : loadedSceneVOs.values())
    { // for each scene loaded
		/*	if(sceneVO == null)
			{
				
			}*/
      for (CompositeItemVO item : projectVO.libraryItems.values())
      { // for each item in a scene
        for (SpriteAnimationVO spriteAnimation : item.composite.sSpriteAnimations)
        { // for each sprite animation in a item
          spriteAnimNamesToLoad.add(spriteAnimation.animationName);
        }
      }
    }
  }

  @Override
  public void loadSpriterAnimations() {
  }

  public void prepareAssetsToLoad() {
    if (!currentScene.equals("arcadeselscn"))
    {
      particleEffectNamesToLoad.clear();
      fontsToLoad.clear();
      spriteAnimNamesToLoad.clear();
    }

    for (String preparedSceneName : preparedSceneNames)
    {
      // temp fix
      /*
       * if(currentScene.equals("arcadeselscn" && preparedSceneName != "storyscene")
       * { continue; }
       */
      CompositeVO composite;
      composite = loadedSceneVOs.get(preparedSceneName).composite;
      if (composite == null)
      {
        continue;
      }
      //Objects within the respective scene(Composite)
      String[] particleFX = composite.getRecursiveParticleEffectsList();
      String[] spriteAnims = composite.getRecursiveSpriteAnimationList();
      FontSizePair[] fonts = composite.getRecursiveFontList();

      //Grab all of the sprites
      if (Stream.of("arcadeselscn", "charselscene", "stagescene").anyMatch(scene -> currentScene.equals(scene)))
      {
        for (CompositeItemVO library : projectVO.libraryItems.values())
        {

          // FontSizePair[] libFonts = library.composite.getRecursiveFontList();
          // Collections.addAll(fontsToLoad, libFonts);
          Collections.addAll(spriteAnimNamesToLoad, spriteAnims);
          // loading particle effects used in library items
          // String[] libEffects =
          // library.composite.getRecursiveParticleEffectsList();
          // Collections.addAll(particleEffectNamesToLoad, libEffects);
        }
      }
      //
      // Collections.addAll(particleEffectNamesToLoad, particleEffects);
      Collections.addAll(particleEffectNamesToLoad, particleFX);
      Collections.addAll(fontsToLoad, fonts);
      Collections.addAll(spriteAnimNamesToLoad, spriteAnims);
      // Collections.addAll(spriteAnimNamesToLoad, spriteAnimations);

    }
  }

  public void releaseMenu() {
    unLoadScene("landscene");
    unLoadScene("sceneone");
    unLoadScene("scenetwo");
    unLoadScene("scenethree");
    unLoadScene("scenefour");
    unLoadScene("scenefive");
  }

  /**
   * Schedules scene for later loading if later prepareAssetsToLoad function
   * will be called it will only prepare assets that are used in scheduled scene
   *
   * @param name - scene file name without ".dt" extension
   */
  public void scheduleScene(String name) {
    if (loadedSceneVOs.containsKey(name))
    {
      preparedSceneNames.add(name);
    }
    else
    {

    }

  }

  public void setManager(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public AssetManager getManager() {
    return assetManager;
  }

  /**
   * Sets working resolution, please set before doing any loading
   *
   * @param resolution String resolution name, default is "orig" later use resolution
   *                   names created in editor
   */
  public void setWorkingResolution(String resolution) {
    ResolutionEntryVO resolutionObject = getProjectVO().getResolution(resolution);

    if (resolutionObject == null)
      System.out.println("resolutionObject is null");
  }

  /**
   * Anloads scene from the memory, and clears all the freed assets
   *
   * @param sceneName - scene file name without ".dt" extension
   */
  public void unLoadScene(String sceneName) {
    unScheduleScene(sceneName);
    // preparedSceneNames.remove(sceneName);
    loadedSceneVOs.remove(sceneName);
    // loadAssets();

  }

  public void unLoadSceneVO(String sceneName) {
    loadedSceneVOs.remove(sceneName);
  }

  /**
   * Unschedule scene from later loading
   *
   * @param name - name of scene to unschedule
   */
  public void unScheduleScene(String name) {
    preparedSceneNames.remove(name);
  }

  public void unschedulemenu() {
    preparedSceneNames.remove("landscene");
    preparedSceneNames.remove("sceneone");
    preparedSceneNames.remove("arcadeselscn");
  }

  @Override
  public void loadSpriteAnimations() {
    String spriteAnimationsPath = "sprite_animations";
    // empty existing ones that are not scheduled to load
    //if (!spriteAnimations.isEmpty())
    //{
    /*
     * for (String key : spriteAnimations.keySet()) { if
     * (!spriteAnimNamesToLoad.contains(key)) { spriteAnimations.remove(key);
     * } }
     */
    //}
    // if (spriteAnimations.size() > 0)
    // {
    //if (currentScene.equals("arcadeselscn" || currentScene.equals("charselscene")
    //{
    loadSpriteAnimOfScene();

    for (String name : spriteAnimNamesToLoad)
    {
      TextureAtlas animAtlas = new TextureAtlas(Gdx.files.internal(packResolutionName + File.separator
        + spriteAnimationsPath + File.separator + name + File.separator + name + ".atlas"));
      spriteAnimations.put(name, animAtlas);
    }
    //}
    // }
  }

  public SPFZSceneLoader getPortraitSSL() {
    return portraitSSL;
  }

  public SPFZSceneLoader getLandscapeSSL() {
    return landscapeSSL;
  }

  public void setLandscapeSSL(String scene) {
    currentScene = scene;
    //setting with new SPFZSceneLoader object as it helps get desired result for main menu
    landscapeSSL = new SPFZSceneLoader(this);
    landscapeSSL.loadScene(currentScene, spfzMCamera.getViewport());
    setRootWrapper(new ItemWrapper(landscapeSSL.getRoot()));
  }

  public SPFZSceneLoader getStagePauseSSL() {
    return stagePauseSSL;
  }

  public SPFZSceneLoader getCurrentSSL() {
    if (orientation.equals(PORTRAIT))
      return portraitSSL;
    else
      //TODO will need to add stage check logic here
      return landscapeSSL;
  }

  public void setRootWrapper(ItemWrapper rootWrapper) {
    this.rootWrapper.getEntity().removeAll();
    this.rootWrapper = new ItemWrapper(getCurrentSSL().getRoot());
  }

  public ItemWrapper rootWrapper() {
    return rootWrapper;
  }

  public void setCurrentScene(String scene) {
    currentScene = scene;
  }

  public String currentScene() {
    return currentScene;
  }

  public void setCurrentOrientation() {
    int rotation = Gdx.input.getRotation();

    if ((rotation == 90 || rotation == 270) && !orientation.equals(LANDSCAPE))
      orientation = LANDSCAPE;
    else if (!orientation.equals(PORTRAIT))
      orientation = PORTRAIT;
  }

  public String getCurrentOrientation() {
    return orientation;
  }

  public Camera getMenuCam() {
    return spfzMCamera.menuCamera();
  }

  public Camera getStageCam() {
    return spfzSCamera.stageCamera();
  }

  public boolean isInMenu() {
    return inMenu;
  }

  public void setInMenu(boolean inMenu) {
    if (this.inMenu != inMenu)
      this.inMenu = inMenu;
  }

  public ProjectInfoVO projectVO() {
    return projectVO;
  }

  public String preferencesFile() {
    return preferencesFile;
  }

  public int appDevice() {
    return appDevice;
  }

  public void setAppDevice() {
    switch (Gdx.app.getType())
    {
      case Android:
        appDevice = ANDROID;
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        break;
      case iOS:
        appDevice = IOS;
        break;
      case Desktop:
        appDevice = DESKTOP;
        break;
      default:
        break;
    }
  }

  public float[] getOptionsSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return new float[]{spfzprefs.getFloat("brightness"), spfzprefs.getFloat("sound")};
  }

  public float getBrightSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("brightness");
  }

  public float getBrightIncrementSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("brightnessIncrements");
  }

  public float getMinBrightSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("MinBrightness");
  }


  public float getMaxBrightSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("MaxBrightness");
  }

  public float getSoundSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("sound");
  }

  public float getMaxSoundSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("MaxSound");
  }

  public float getSoundIncrementsSettingsValues() {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    return spfzprefs.getFloat("soundIncrements");
  }

  public void saveBrightSettings(float brightness) {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    spfzprefs.putFloat("brightness", brightness);
    spfzprefs.flush();
  }

  public void saveSoundSettings(float sound) {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    spfzprefs.putFloat("sound", sound);
    spfzprefs.flush();
  }

  public void saveSoundSettings(Map<String, String> settings) {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    spfzprefs.put(settings);
    spfzprefs.flush();
  }

  public void setRoundTimeSettings(float seconds) {
    Preferences spfzprefs = Gdx.app.getPreferences(preferencesFile);

    spfzprefs.putFloat("time", seconds);
    spfzprefs.flush();
  }
}