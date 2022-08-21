package com.dev.swapftrz.fyter;

import com.badlogic.gdx.math.Rectangle;
import com.dev.swapftrz.resource.SPFZResourceManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterAttributes {

  private SPFZResourceManager resManager;
  boolean charfound = false;
  private final int characterLimit = 3;
  int health, player;
  float dash, gravity, jump, pushback, stuntime, walkspeed;
  Rectangle chardims;

  private final List<List<ArrayList<Double>>> characterFrameData;
  List<HashMap<String, int[]>> animations;
  List<HashMap<String, float[]>> characterAttributeData;
  List<ArrayList<Integer>> animFPS = new ArrayList<>();
  List<ArrayList<String>> anims = new ArrayList<>();
  List<ArrayList<String>> moveset = new ArrayList<>();
  List<ArrayList<int[]>> inputs = new ArrayList<>();
  //List of player array data

  public CharacterAttributes(SPFZResourceManager resManager, String[] characters) {
    this.resManager = resManager;
    // Read the file that will contain the frame information

    characterFrameData = setupCharacterFrameData(characters, resManager.getDBConnection());
  }


  public List<List<ArrayList<Double>>> setupCharacterFrameData(String[] characters, Connection c) {
    List<List<ArrayList<Double>>> frameDataTable = new ArrayList<>();
    List<ArrayList<Double>> characterInformation;

    for (int i = 0; i < characters.length; i++) {
      characterInformation = resManager.dbRetrieveCharacterFrameData(characters[i]);
      frameDataTable.add(characterInformation);
    }

    System.out.println("character information successfully retrieved");

    return frameDataTable;
  }

  public Boolean isProjectile(String proj) {
    return proj.equalsIgnoreCase("PROJ1") || proj.equalsIgnoreCase("PROJ2") ||
      proj.equalsIgnoreCase("PROJ3");
  }

  public void populateAttributeData() {

  }

  public List<List<ArrayList<Double>>> retrieveFrameData() { return characterFrameData; }

  public HashMap<String, float[]> characterAttributeData(int slotIndex) {
    return characterAttributeData.get(slotIndex);
  }

  public float getStuntime() {
    return stuntime;
  }

  public float getPushback() {
    return pushback;
  }

  public int getHealth() {
    return health;
  }

  public float getGravity() {
    return -gravity;
  }

  public float getJump() {
    return jump;
  }

  public float getWalkspeed() {
    return walkspeed;
  }

  public float getdashadv() {
    return dash;
  }

  public Rectangle getCharDims() {
    return chardims;
  }

  public ArrayList<int[]> getmoveinputs() {
    return inputs;
  }

  public HashMap<String, int[]> getAnimations() {
    return animations;
  }
}
