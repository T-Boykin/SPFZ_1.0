package com.dev.swapftrz.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class SPFZDBOperations implements java.sql.Driver {
  private final SPFZResourceManager resourceManager;
  private final String atk_table = "attacks", char_table = "characters", anim_table = "animations",
    move_table = "movement", proj_table = "projectiles", in_table = "inputs", z_code = "spfzcode";
  private String charcode, char_name, qry;

  private ResultSet set;
  private PreparedStatement pst;
  private Connection connection;

  public SPFZDBOperations(SPFZResourceManager resourceManager) {
    //Context c = //Setup constructor to accept android context
    // Setup connection to SPFZ database
    this.resourceManager = resourceManager;
  }

  public String urlString() {
    int device = resourceManager.appDevice();
    String file = resourceManager.preferencesFile();
    Preferences spfzprefs = Gdx.app.getPreferences(file);
    String driver = spfzprefs.getString("driver");
    String assetPath = spfzprefs.getString("assetPath");
    String androidDevicePath = spfzprefs.getString("androidDevicePath");
    String desktopPath = spfzprefs.getString("desktopPath");
    String filePath;

    filePath = spfzprefs.getString(driver);

    if (device == resourceManager.ANDROID) {
      filePath = filePath.concat(androidDevicePath);
    }
    else if (device == resourceManager.DESKTOP) {
      filePath = filePath.concat(desktopPath);
    }
    else {
      filePath = "N/A";
    }

    filePath = filePath.concat(assetPath.concat(file));

    return filePath;
  }

  public Connection createDBConnection() {
    Connection connection = null;

    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection(urlString(), "", "");
    }
    catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("1st open successful");
    //init();
    this.connection = connection;

    return this.connection;
  }

  public Rectangle retrieveCharacterPhysicalDimension(String character) throws SQLException {
    String char_dimx = "spfzdimx";
    String char_dimy = "spfzdimy";
    String char_dimw = "spfzdimw";
    String char_dimh = "spfzdimh";
    int dimx = 0, dimy = 0, dimw = 0, dimh = 0;

    //Grab the character code associated with the character name and set character values
    qry = "SELECT * " +
      "FROM " + char_table + " " +
      "WHERE " + char_name + " = ?";

    pst = connection.prepareStatement(qry);
    pst.setString(1, character);
    set = pst.executeQuery();

    if (set.next()) {
      setCharacterCode(set.getString(z_code));
      dimx = set.getInt(char_dimx);
      dimy = set.getInt(char_dimy);
      dimw = set.getInt(char_dimw);
      dimh = set.getInt(char_dimh);

      pst.close();
      set.close();
    }
    else {
      System.exit(0);
    }

    return new Rectangle(dimx, dimy, dimw, dimh);
  }

  //TODO set apppropriate decision logic for retrieve which player attributes
  public HashMap<String, float[]> retrieveCharacterAttributes(String character) throws SQLException {
    HashMap<String, float[]> temporaryCharacterAttributes = character.equals("") ?
      resourceManager.stageObject().player1().getCharacterAttributes().characterAttributeData(
        resourceManager.stageObject().player1().getSlotIndex()) :
      resourceManager.stageObject().player2().getCharacterAttributes().characterAttributeData(
        resourceManager.stageObject().player1().getSlotIndex());

    float[] health = temporaryCharacterAttributes.get("health");
    float[] jump = temporaryCharacterAttributes.get("jump");
    float[] walk = temporaryCharacterAttributes.get("walk");
    float[] dash = temporaryCharacterAttributes.get("dash");
    float[] gravity = temporaryCharacterAttributes.get("gravity");

    String char_name = "spfzname";
    String char_health = "spfzhealth";
    String char_jump = "spfzjppwr";
    String char_walk = "spfzwalkspd";
    String char_dash = "spfzdashspd";
    String char_grav = "spfzgrav";

    qry = "SELECT * " +
      "FROM " + char_table + " " +
      "WHERE " + char_name + " = ?";

    pst = resourceManager.getDBConnection().prepareStatement(qry);
    pst.setString(1, character);
    set = pst.executeQuery();

    if (set.next()) {
      for (int i = 0; i < health.length; i++)
        if (health[i] == 0) {
          health[i] = set.getFloat(char_health);
          jump[i] = set.getFloat(char_jump);
          walk[i] = set.getFloat(char_walk);
          dash[i] = set.getFloat(char_dash);
          gravity[i] = set.getFloat(char_grav);
        }

      temporaryCharacterAttributes.put("health", health);
      temporaryCharacterAttributes.put("jump", jump);
      temporaryCharacterAttributes.put("walk", walk);
      temporaryCharacterAttributes.put("dash", dash);
      temporaryCharacterAttributes.put("gravity", gravity);

      pst.close();
      set.close();
    }
    else {
      System.out.println("DB execution failed at retrieving Character Attributes");
      System.exit(0);
    }

    return temporaryCharacterAttributes;
  }

  public HashMap<String, int[]> retrieveAnimations(HashMap<String, int[]> animations) { return animations; }

  public ArrayList<Integer> retrieveAnimationsFPS(ArrayList<Integer> animationFPS) { return animationFPS; }

  public ArrayList<String> retrieveAnimationCodes(ArrayList<String> animationCodes) { return animationCodes; }

  public ArrayList<int[]> retrieveInputs(ArrayList<int[]> inputs) { return inputs; }

  public ArrayList<String> retrieveAttacks(ArrayList<String> attacksList) {
    return attacksList;
  }

  //parses the data within the file to represent data into arrays
  public List<ArrayList<Double>> retrieveAttacksAndAnimationsData(Connection c, String chr) {
    List<ArrayList<Double>> tempCharacterData = new ArrayList<>();
    ArrayList<String> attacksList = new ArrayList<>();
    HashMap<String, int[]> animations = new HashMap<>();
    ArrayList<Integer> animFPS = new ArrayList<>();
    ArrayList<String> animationCodes = new ArrayList<>();
    ArrayList<int[]> inputs = new ArrayList<>();

    //Animation table
    ArrayList<Double> tempSTA = new ArrayList<Double>();
    ArrayList<Double> tempEDA = new ArrayList<Double>();
    ArrayList<Double> tempASPD = new ArrayList<Double>();
    //Attacks table

    ArrayList<Double> tempBSTN = new ArrayList<Double>();
    ArrayList<Double> tempHSTN = new ArrayList<Double>();
    ArrayList<Double> tempACTS = new ArrayList<Double>();
    ArrayList<Double> tempACTE = new ArrayList<Double>();
    ArrayList<Double> tempBOXX = new ArrayList<Double>();
    ArrayList<Double> tempBOXY = new ArrayList<Double>();
    ArrayList<Double> tempBOXW = new ArrayList<Double>();
    ArrayList<Double> tempBOXH = new ArrayList<Double>();
    ArrayList<Double> tempBDMG = new ArrayList<Double>();
    ArrayList<Double> tempHDMG = new ArrayList<Double>();
    ArrayList<Double> tempBTMR = new ArrayList<Double>();
    ArrayList<Double> tempHMTR = new ArrayList<Double>();
    ArrayList<Double> tempHADV = new ArrayList<Double>();
    ArrayList<Double> tempBADV = new ArrayList<Double>();
    ArrayList<Double> tempPUSH = new ArrayList<Double>();
    ArrayList<Double> tempWBNC = new ArrayList<Double>();
    ArrayList<Double> tempPRI = new ArrayList<Double>();

    //Movement table
    ArrayList<Double> tempFMOVE = new ArrayList<Double>();
    ArrayList<Double> tempBMOVE = new ArrayList<Double>();
    ArrayList<Double> tempBDIS = new ArrayList<Double>();
    ArrayList<Double> tempHDIS = new ArrayList<Double>();
    ArrayList<Double> tempJUGG = new ArrayList<Double>();
    ArrayList<Double> tempBCKS = new ArrayList<Double>();
    ArrayList<Double> tempFWDS = new ArrayList<Double>();
    ArrayList<Double> tempBCKA = new ArrayList<Double>();
    ArrayList<Double> tempFWDA = new ArrayList<Double>();
    ArrayList<Double> tempBCKR = new ArrayList<Double>();
    ArrayList<Double> tempFWDR = new ArrayList<Double>();


    //Projectiles table
    ArrayList<String> projectile = new ArrayList<String>();
    ArrayList<Double> strtstart = new ArrayList<Double>();
    ArrayList<Double> strtend = new ArrayList<Double>();
    ArrayList<Double> loopstart = new ArrayList<Double>();
    ArrayList<Double> loopend = new ArrayList<Double>();
    ArrayList<Double> endstart = new ArrayList<Double>();
    ArrayList<Double> endfin = new ArrayList<Double>();
    ArrayList<Double> type = new ArrayList<Double>();
    ArrayList<Double> speed = new ArrayList<Double>();
    ArrayList<Double> posX = new ArrayList<Double>();
    ArrayList<Double> posY = new ArrayList<Double>();

    String charcode = "";
    String z_code = "spfzcode";
    String char_name = "spfzname";
    String char_type = "spfztype";
    String char_health = "spfzhealth";
    String char_jump = "spfzjppwr";
    String char_walk = "spfzwalkspd";
    String char_dash = "spfzdashspd";
    String char_grav = "spfzgrav";

    String atk_atk = "spfzatk";
    String atk_beg_act = "actfrmbeg";
    String atk_end_act = "actfrmend";
    String atk_bstun = "blkstun";
    String atk_hstun = "hitstun";
    String atk_boxX = "boxposx";
    String atk_boxY = "boxposy";
    String atk_boxW = "boxposw";
    String atk_boxH = "boxposh";
    String atk_b_dmg = "blkdmg";
    String atk_h_dmg = "hitdmg";
    String atk_h_time = "hittime";
    String atk_b_mtr_g = "blkmtrgn";
    String atk_h_mtr_g = "hitmtrgn";
    String atk_blkadv = "atkadvblk";
    String atk_hitadv = "atkadvhit";
    String atk_pushb = "pshbck";
    String atk_wallb = "wallbnc";
    String atk_prty = "priority";

    String anim_acode = "spfzanimcode";
    String anim_strtfrm = "stranimfrm";
    String anim_endfrm = "endanimfrm";
    String anim_speed = "animspd";

    String move_fwd = "fwdmvm";
    String move_bck = "bckmvm";
    String move_b_dist = "blkdist";
    String move_h_dist = "hitdist";
    String move_jugg = "jugpwr";
    String move_back_s = "bdstrt";
    String move_fwd_s = "fdstrt";
    String move_back_a = "bdact";
    String move_fwd_a = "fdact";
    String move_back_r = "bdrec";
    String move_fwd_r = "fdrec";

    String proj_name = "proj";
    String proj_s_beg = "strtupbeg";
    String proj_s_end = "strtupend";
    String proj_l_beg = "loopstrt";
    String proj_l_end = "loopend";
    String proj_e_beg = "endstrt";
    String proj_e_end = "endfin";
    String proj_type = "type";
    String proj_speed = "speed";
    String proj_posx = "posx";
    String proj_posy = "posy";

    String in_seq = "inseq";


    try {

      //Grab the character code associated with the character name and set character values
      qry = "SELECT * " +
        "FROM " + char_table + " " +
        "WHERE " + char_name + " = ?";

      pst = c.prepareStatement(qry);
      pst.setString(1, chr);
      set = pst.executeQuery();

      if (set.next()) {
        setCharacterCode(set.getString(z_code));
        pst.close();
        set.close();
      }
      else {
        System.exit(0);
      }

      //Step 2 Grab the infromation from the attacks(table_1) and the animations(table_2)

      qry = "SELECT DISTINCT " + anim_table + "." + anim_strtfrm + ", " +
        anim_table + "." + anim_endfrm + ", " + atk_table + ".*, " +
        proj_table + ".*, " + move_table + ".* " +
        "FROM " + atk_table + " " +
        "LEFT JOIN " + anim_table + " ON " + anim_table + "." + anim_acode +
        " = " + atk_table + "." + atk_atk + " " +
        "LEFT JOIN " + move_table + " ON " + move_table + "." + anim_acode +
        " = " + atk_table + "." + atk_atk + " " +
        "LEFT JOIN " + proj_table + " ON " + proj_table + "." + proj_name +
        " = " + atk_table + "." + atk_atk + " " +
        "WHERE " + anim_table + "." + z_code + " = ? " +
        "AND " + atk_table + "." + z_code + " = ? " +
        "GROUP BY " + atk_table + "." + atk_atk;

      pst = c.prepareStatement(qry);
      pst.setString(1, charcode);
      pst.setString(2, charcode);

      set = pst.executeQuery();

      while (set.next()) {
        //block stun
        tempBSTN.add(set.getDouble(atk_bstun));
        //hit stun
        tempHSTN.add(set.getDouble(atk_hstun));
        //active starting frame
        tempACTS.add(set.getDouble(atk_beg_act));
        //active ending frame
        tempACTE.add(set.getDouble(atk_end_act));
        //box x
        tempBOXX.add(set.getDouble(atk_boxX));
        //box y
        tempBOXY.add(set.getDouble(atk_boxY));
        //box width
        tempBOXW.add(set.getDouble(atk_boxW));
        //box height
        tempBOXH.add(set.getDouble(atk_boxH));
        //block damage
        tempBDMG.add(set.getDouble(atk_b_dmg));
        //hit damage
        tempHDMG.add(set.getDouble(atk_h_dmg));
        //block meter
        tempBTMR.add(set.getDouble(atk_b_mtr_g));
        //hit meter
        tempHMTR.add(set.getDouble(atk_h_mtr_g));

        //forward movement
        tempFMOVE.add(set.getDouble(move_fwd));
        //backwards movement
        tempBMOVE.add(set.getDouble(move_bck));
        //block distance
        tempBDIS.add(set.getDouble(move_b_dist));
        //hit distance
        tempHDIS.add(set.getDouble(move_h_dist));
        //juggle power
        tempJUGG.add(set.getDouble(move_jugg));
        //player attack move backwards on startup
        tempBCKS.add(set.getDouble(move_back_s));
        //player attack move forwrads on startup
        tempFWDS.add(set.getDouble(move_fwd_s));
        //player attack move backwards on active frames
        tempBCKA.add(set.getDouble(move_back_a));
        //player attack move forwards on active frames
        tempFWDA.add(set.getDouble(move_fwd_a));
        //player attack move backwards on recovery frames
        tempBCKR.add(set.getDouble(move_back_r));
        //player attack move forwards on recovery frames
        tempFWDR.add(set.getDouble(move_fwd_r));

        //If the attack is a projectile, setup the projectile information
        if (isProjectile(set.getString(atk_atk))) {
          projectile.add(set.getString(proj_name));
          strtstart.add(set.getDouble(proj_s_beg));
          strtend.add(set.getDouble(proj_s_end));
          loopstart.add(set.getDouble(proj_l_beg));
          loopend.add(set.getDouble(proj_l_end));
          endstart.add(set.getDouble(proj_e_beg));
          endfin.add(set.getDouble(proj_e_end));
          type.add(set.getDouble(proj_type));
          speed.add(set.getDouble(proj_speed));
          posX.add(set.getDouble(proj_posx));
          posY.add(set.getDouble(proj_posy));
        }

        attacksList.add(set.getString(atk_atk));
      }
      //Set the values coming from the animations table.
      //startup anim frame
      pst.close();
      set.close();

      retrieveAttacks(attacksList);
      System.out.println("Values for " + charcode + " have been populated.");

      //Step 2 set all animations within the animations array

      qry = "SELECT * " +
        "FROM " + anim_table + " " +
        "WHERE " + z_code + " = ?";

      pst = c.prepareStatement(qry);
      pst.setString(1, charcode);

      set = pst.executeQuery();


      while (set.next()) {
        int actstart = set.getInt(anim_strtfrm);
        int actend = set.getInt(anim_endfrm);
        actstart -= 1;
        actend -= 1;

        animations.put(set.getString(anim_acode),
          new int[] {actstart, actend});
        animationCodes.add(set.getString(anim_acode));
        animFPS.add(set.getInt(anim_speed));
      }


      // set the inputs for the character
      //Step 3 get all inputs and store them in inputs array

      qry = "SELECT * " +
        "FROM " + in_table + " " +
        "WHERE " + z_code + " = ?";


      pst = c.prepareStatement(qry);
      pst.setString(1, charcode);

      set = pst.executeQuery();

      //Read all inputs, using split String function to separate integers coming from inputs table
      //then create new temporary integer array in order to place into inputs array
      while (set.next()) {
        String[] in = Integer.toString(set.getInt(in_seq)).split("");
        int[] hold = new int[in.length];
        int i = 0;
        for (String str : in) {
          hold[i] = Integer.parseInt(in[i]);
          i++;
        }
        inputs.add(hold);
      }
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    finally {

      //Set all of the variables coming in from the database to the List of ArrayList and pass it back
      //to set up the actual list.
      System.out.println("Character data processed from database.");

      tempCharacterData.add(tempBSTN); //1
      tempCharacterData.add(tempHSTN); //2
      tempCharacterData.add(tempBDIS); //3
      tempCharacterData.add(tempHDIS); //4
      tempCharacterData.add(tempACTS); //5
      tempCharacterData.add(tempACTE); //6
      tempCharacterData.add(tempBOXX); //7
      tempCharacterData.add(tempBOXY); //8
      tempCharacterData.add(tempBOXW); //9
      tempCharacterData.add(tempBOXH); //10
      tempCharacterData.add(tempBDMG); //11
      tempCharacterData.add(tempHDMG); //12
      tempCharacterData.add(tempBTMR); //13
      tempCharacterData.add(tempHMTR); //14
      tempCharacterData.add(tempFMOVE); //15
      tempCharacterData.add(tempBMOVE); //16
      tempCharacterData.add(tempJUGG); //17
      tempCharacterData.add(tempBCKS); //18
      tempCharacterData.add(tempFWDS); //19
      tempCharacterData.add(tempBCKA); //20
      tempCharacterData.add(tempFWDA); //21
      tempCharacterData.add(tempBCKR); //22
      tempCharacterData.add(tempFWDR); //23
      tempCharacterData.add(strtstart); //24
      tempCharacterData.add(strtend); //25
      tempCharacterData.add(loopstart); //26
      tempCharacterData.add(loopend); //27
      tempCharacterData.add(endstart); //28
      tempCharacterData.add(endfin); //29
      tempCharacterData.add(type); //30
      tempCharacterData.add(speed); //31
      tempCharacterData.add(posX); //32
      tempCharacterData.add(posY); //33
    }

    retrieveAttacks(attacksList);
    retrieveAnimationsFPS(animFPS);
    retrieveAnimations(animations);
    retrieveInputs(inputs);
    retrieveAnimationCodes(animationCodes);

    return tempCharacterData;
  }

  public boolean isProjectile(String actionName) { return actionName.equals("PROJ"); }

  @Override
  public Connection connect(String s, Properties properties) throws SQLException {
    return null;
  }

  @Override
  public boolean acceptsURL(String s) throws SQLException {
    return false;
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
    return new DriverPropertyInfo[0];
  }

  @Override
  public int getMajorVersion() {
    return 0;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    return false;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }

  public void setCharacterCode(String charcode) { this.charcode = charcode; }
}
