package com.dev.swapftrz.fyter;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterAttirbutes
{
  boolean charfound = false;
  private final int characterLimit = 3;
  int health, player;
  float gravity;
  float jump;
  float walkspeed;
  float dash;
  float pushback;
  float stuntime;
    Rectangle chardims;

    List<ArrayList<Double>> tempplayer = new ArrayList<ArrayList<Double>>();
    HashMap<String, int[]> animations = new HashMap<String, int[]>();
    ArrayList<Integer> animFPS = new ArrayList<>();
    ArrayList<String> anims = new ArrayList<>();
    ArrayList<String> moveset = new ArrayList<String>();
    ArrayList<int[]> inputs = new ArrayList<int[]>();
    //List of player array data

    String[] charvals, values, movelist;

    Vector2 wandj;


  public CharacterAttirbutes(String character, int total) {
    Connection conn = null;

    conn = dbconnect();

    // Read the file that will contain the frame information
    if (total != characterLimit)
    {
      tempplayer = getCharData(character, conn);
    }
    }


    public List<ArrayList<Double>> getCharData(String charac, Connection c)
    {
        List<ArrayList<Double>> player = new ArrayList<ArrayList<Double>>();
        System.out.println("2nd open successful");
        return process(c, charac, player);
    }


    //parses the data within the file to represent data into arrays
    public List<ArrayList<Double>> process(Connection c, String chr, List<ArrayList<Double>> t)
    {
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
        ArrayList<Double> posY= new ArrayList<Double>();

        String charcode = "";
        String char_table = "characters";
        String z_code = "spfzcode";
        String char_name = "spfzname";
        String char_type = "spfztype";
        String char_health = "spfzhealth";
        String char_jump = "spfzjppwr";
        String char_walk = "spfzwalkspd";
        String char_dash = "spfzdashspd";
        String char_grav = "spfzgrav";
        String char_dimx = "spfzdimx";
        String char_dimy = "spfzdimy";
        String char_dimw = "spfzdimw";
        String char_dimh = "spfzdimh";


        String atk_table = "attacks";
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

        String anim_table = "animations";
        String anim_acode = "spfzanimcode";
        String anim_strtfrm = "stranimfrm";
        String anim_endfrm = "endanimfrm";
        String anim_speed = "animspd";

        String move_table = "movement";
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

        String proj_table = "projectiles";
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

        String in_table = "inputs";
        String in_seq = "inseq";

        String qry;
        ResultSet set = null;
        PreparedStatement pst = null;

        try
        {
            //Grab the character code associated with the character name and set character values
            qry =   "SELECT * " +
                    "FROM " + char_table + " " +
                    "WHERE " + char_name + " = ?";

            pst = c.prepareStatement(qry);
            pst.setString(1, chr);
            set = pst.executeQuery();

            if(set.next())
            {

                chardims = new Rectangle(set.getInt(char_dimx), set.getInt(char_dimy), set.getInt(char_dimw), set.getInt(char_dimh));
                charcode = set.getString(z_code);
                health = set.getInt(char_health);
                gravity = set.getInt(char_grav);
                jump = set.getInt(char_jump);
                walkspeed = set.getInt(char_walk);
                dash = set.getInt(char_dash);
                wandj = new Vector2(walkspeed, 0);
                pst.close();
                set.close();
            }
            else
            {
                System.exit(0);
            }

            //Step 2 Grab the infromation from the attacks(table_1) and the animations(table_2)

            qry =   "SELECT DISTINCT " + anim_table + "." + anim_strtfrm + ", " +
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

            while (set.next())
            {
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
                if (isProjectile(set.getString(atk_atk)))
                {
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

                moveset.add(set.getString(atk_atk));
            }
            //Set the values coming from the animations table.
            //startup anim frame
            pst.close();
            set.close();
            System.out.println("Values for " +  charcode + " have been populated.");

            //Step 2 set all animations within the animations array

            qry =   "SELECT * " +
                    "FROM " + anim_table + " " +
                    "WHERE " + z_code + " = ?";

            pst = c.prepareStatement(qry);
            pst.setString(1, charcode);

            set = pst.executeQuery();

            while (set.next())
            {
                int actstart = set.getInt(anim_strtfrm);
                int actend = set.getInt(anim_endfrm);
                actstart -= 1;
                actend -= 1;

                animations.put(set.getString(anim_acode),
                        new int[]{actstart, actend});
                anims.add(set.getString(anim_acode));
                animFPS.add(set.getInt(anim_speed));

            }


            // set the inputs for the character
            //Step 3 get all inputs and store them in inputs array

            qry =   "SELECT * " +
                    "FROM " + in_table + " " +
                    "WHERE " + z_code + " = ?";


            pst = c.prepareStatement(qry);
            pst.setString(1, charcode);

            set = pst.executeQuery();

            //Read all inputs, using split String function to separate integers coming from inputs table
            //then create new temporary integer array in order to place into inputs array
            while (set.next())
            {
                String[] in = Integer.toString(set.getInt(in_seq)).split("");
                int[] hold = new int[in.length];
                int i = 0;
                for(String str: in)
                {
                    hold[i] = Integer.parseInt(in[i]);
                    i++;
                }
                inputs.add(hold);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {

            //Set all of the variables coming in from the database to the List of ArrayList and pass it back
            //to set up the actual list.
            System.out.println("Character data processed from database.");

            t.add(tempBSTN); //1
            t.add(tempHSTN); //2
            t.add(tempBDIS); //3
            t.add(tempHDIS); //4
            t.add(tempACTS); //5
            t.add(tempACTE); //6
            t.add(tempBOXX); //7
            t.add(tempBOXY); //8
            t.add(tempBOXW); //9
            t.add(tempBOXH); //10
            t.add(tempBDMG); //11
            t.add(tempHDMG); //12
            t.add(tempBTMR); //13
            t.add(tempHMTR); //14
            t.add(tempFMOVE); //15
            t.add(tempBMOVE); //16
            t.add(tempJUGG); //17
            t.add(tempBCKS); //18
            t.add(tempFWDS); //19
            t.add(tempBCKA); //20
            t.add(tempFWDA); //21
            t.add(tempBCKR); //22
            t.add(tempFWDR); //23
            t.add(strtstart); //24
            t.add(strtend); //25
            t.add(loopstart); //26
            t.add(loopend); //27
            t.add(endstart); //28
            t.add(endfin); //29
            t.add(type); //30
            t.add(speed); //31
            t.add(posX); //32
            t.add(posY); //33

            return t;
        }
    }


    public Boolean isProjectile(String proj)
    {
        if (proj.equalsIgnoreCase("PROJ1") || proj.equalsIgnoreCase("PROJ2") ||
            proj.equalsIgnoreCase("PROJ3"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    public List<ArrayList<Double>> CharInfo(List<ArrayList<Double>> populate)
    {
        return populate;
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
    public float getdashadv()
    {
        return dash;
    }
    public Rectangle getCharDims()
    {
        return chardims;
    }
    public Vector2 getWandj() {
        return wandj;
    }

    public ArrayList<int[]> getmoveinputs() {
        return inputs;
    }

    public HashMap<String, int[]> getAnimations() {
        return animations;
    }

    public Connection dbconnect()
    {
        //File spfzfile;
        Connection c;
        // Setup connection to SPFZ database
        //String url = "jdbc:sqlite:S:/SPFZPROJBACKUP/spfzalpha/android/assets/charinfo/spfz.sqlite";
        String url = "jdbc:sqlite:S:/SPFZPROJBACKUP/spfzalpha/android/assets/charinfo/spfz.sqlite";
        //String url = "jdbc:sqlite://data/data/com.spfz.alpha/android/assets/charinfo/spfz.sqlite";
        c = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url, "", "");


        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return c;

    }
}
