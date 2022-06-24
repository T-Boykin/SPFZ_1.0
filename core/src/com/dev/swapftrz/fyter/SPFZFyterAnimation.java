package com.dev.swapftrz.fyter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.dev.swapftrz.resource.SPFZParticleComponent;
import com.dev.swapftrz.stage.SPFZStage;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class SPFZFyterAnimation
{
  private SPFZStage stage;
  private SPFZPlayer spfzPlayer, opponent;
  private List<List<ArrayList<Double>>> characterData;
  private List<HashMap<String, int[]>> animations;
  private List<ArrayList<String>> specials;
  //SPFZBuffer may need to be used here

  public SPFZFyterAnimation(SPFZPlayer spfzPlayer, SPFZStage stage, List<List<ArrayList<Double>>> characterData,
                            List<HashMap<String, int[]>> animations, List<ArrayList<String>> specials) {
    this.stage = stage;
    this.spfzPlayer = spfzPlayer;
    this.opponent = spfzPlayer.opponent();
    this.characterData = characterData;
    this.animations = animations;
    this.specials = specials;
  }

  public void checkstun(int player) {
    float fd;
    if (((Attribs) arrscripts.get(player)).attacked())
    {
      anim = "stun";
    }
    else
    {
      if (((Attribs) arrscripts.get(player)).attributes().y == GROUND)
      {
        anim = "block";
        if (spfzp2move.isDown)
        {
          anim = "dblock";
        }
      }
      else
      {
        anim = "ablock";
      }
    }
    ((Attribs) arrscripts.get(player)).animationstate().set(
      ((Attribs) arrscripts.get(player)).animationcomponent().frameRangeMap.get(anim), 60, Animation.PlayMode.NORMAL);
  }

  public void checkneutral() {

    if (spfzPlayer1.center() > spfzp2move.center()
      || spfzPlayer1.setrect().x + spfzPlayer1.setrect().width > spfzp2move.center())
    {
      if (spfzPlayer1.setrect().x + (spfzPlayer1.setrect().width * .5f) > spfzp2move.setrect().x
        + (spfzp2move.setrect().width * .5f))
      {
        spfzPlayer1.attributes().x += 1.2f;
        spfzp2move.attributes().x -= 1.2f;
      }
      else
      {
        spfzPlayer1.attributes().x -= 1.2f;
        spfzp2move.attributes().x += 1.2f;
      }
    }

    if (spfzp2move.center() > spfzPlayer1.center()
      || spfzp2move.setrect().x + spfzp2move.setrect().width > spfzPlayer1.center())
    {
      if (spfzp2move.setrect().x + (spfzp2move.setrect().width * .5f) > spfzPlayer1.setrect().x
        + (spfzPlayer1.setrect().width * .5f))
      {
        spfzp2move.attributes().x += 1.2f;
        spfzPlayer1.attributes().x -= 1.2f;
      }
      else
      {
        spfzp2move.attributes().x -= 1.2f;
        spfzPlayer1.attributes().x += 1.2f;
      }
    }

  }

  public void controlcrossing() {
    close = true;

    // /////////////////////////////////////////PLAYER ONE ON THE RIGHT
    // SIDE //////////////////////////////////////////////////////
    // /////////////////////////////////////////PLAYER TWO ON THE LEFT
    // SIDE //////////////////////////////////////////////////////

    neutral = true;
    // check to see if the players have both stopped moving that way they can be
    // readjusted
    for (int j = 0; j < 2; j++)
    {
      if (spfzPlayer1.p1movement.get(j) || spfzp2move.p2movement.get(j))
      {
        neutral = false;
      }
    }
    // leave as is for now. was neutral
    if (neutral || (spfzPlayer1.attributes().y > spfzPlayer1.charGROUND() || spfzp2move.attributes().y > spfzp2move.charGROUND()))
    {
      checkneutral();
    }

    if (!spfzPlayer1.dash)
    {
      if (p1xattr > p2xattr)
      {

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // LEFT //////////////////////////////////////////////////////

        if (spfzPlayer1.p1movement.get(0))
        {
          P1SRightMLeft();
        }

        // /////////////////////////////////////// PLAYER ONE MOVING TO THE
        // RIGHT //////////////////////////////////////////////////////

        else if (spfzPlayer1.p1movement.get(1))
        {
          P1SRightMRight();
        }
      }

      // /////////////////////////////////////////PLAYER ONE ON THE LEFT
      // SIDE //////////////////////////////////////////////////////
      // /////////////////////////////////////////PLAYER TWO ON THE RIGHT
      // SIDE /////////////////////////////////////////////////////

      else
      {

        // ///////////////////////////////// PLAYER ONE MOVING TO THE RIGHT
        // //////////////////////////////////////////////////////
        if (spfzPlayer1.p1movement.get(1))
        {
          P1SLeftMRight();
        }

        // ///////////////////////////////// PLAYER ONE MOVING TO THE LEFT
        // //////////////////////////////////////////////////////

        else if (spfzPlayer1.p1movement.get(0))
        {
          P1SLeftMLeft();
        }
      }
    }
    else
    {
      if (spfzPlayer1.dash)
      {
        if (spfzPlayer1.dashdir == 0)
        {
          if (stageCamera.position.x <= camboundary[0] + 1 && spfzp2move.attributes().x <= stageboundary[0])
          {
            spfzPlayer1.attributes().x += 0f;
            spfzp2move.attributes().x += 0f;
          }
          else
          {
            spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzPlayer1.attributes().x -= (spfzPlayer1.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
        else
        {
          if (stageCamera.position.x + 1 >= camboundary[1] && spfzp2move.attributes().x + 1 >= stageboundary[1])
          {
            spfzPlayer1.attributes().x += 0f;
            spfzp2move.attributes().x += 0f;
          }
          else
          {
            spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
            spfzPlayer1.attributes().x += (spfzPlayer1.moveandjump().x * .400f) * Gdx.graphics.getDeltaTime();
          }
        }
      }
    }

  }

  public void setface() {

    if (spfzPlayer1.center() < spfzp2move.center())
    {
      if (spfzPlayer1.attributes().scaleX < 0 && !faceright1)
      {
        faceright1 = true;
      }
      if (spfzp2move.attributes().scaleX > 0 && !faceleft2)
      {
        faceleft2 = true;
      }
    }

    if (spfzPlayer1.center() > spfzp2move.center())
    {
      if (spfzPlayer1.attributes().scaleX > 0 && !faceleft1 && spfzPlayer1.setrect().y <= GROUND)
      {
        faceleft1 = true;
      }
      if (spfzp2move.attributes().scaleX < 0 && !faceright2 && spfzp2move.setrect().y <= GROUND)
      {
        faceright2 = true;
      }
    }

    setfacingp1();
    setfacingp2();

  }

  public void setfacingp1() {


    if (faceright1)
    {
      faceleft1 = false;
      if (spfzPlayer1.attributes().y <= spfzPlayer1.charGROUND() && spfzPlayer1.attributes().scaleX < 0)
      {

        spfzPlayer1.attributes().scaleX *= -1f;
        spfzPlayer1.attributes().x -= spfzPlayer1.dimrect.width - (spfzPlayer1.adjustX + (spfzPlayer1.setrect().width * .5f));
        faceright1 = false;

      }
    }
    if (faceleft1)
    {
      faceright1 = false;
      if (spfzPlayer1.attributes().y <= spfzPlayer1.charGROUND() && spfzPlayer1.attributes().scaleX > 0)
      {
        spfzPlayer1.attributes().scaleX *= -1f;

        spfzPlayer1.attributes().x += spfzPlayer1.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
        faceleft1 = false;
      }
    }
  }

  public void setfacingp2() {
    if (faceright2)
    {
      if (spfzp2move.attributes().y <= spfzp2move.charGROUND() && spfzp2move.attributes().scaleX < 0)
      {
        spfzp2move.attributes().scaleX *= -1f;
        spfzp2move.attributes().x -= spfzp2move.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
        faceright2 = false;
      }
    }
    if (faceleft2)
    {
      if (spfzp2move.attributes().y <= spfzp2move.charGROUND() && spfzp2move.attributes().scaleX > 0)
      {
        spfzp2move.attributes().scaleX *= -1f;
        spfzp2move.attributes().x += spfzp2move.dimrect.width - (spfzp2move.adjustX + (spfzp2move.setrect().width * .5f));
        faceleft2 = false;
      }
    }
  }

  public void P1SRightMLeft() {
    // player 2 is moving left
    if (spfzp2move.p2movement.get(0))
    {

      if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

      else if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
      {

        spfzp2move.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

      }

    }
    // player 2 is moving right
    else if (spfzp2move.p2movement.get(1))
    {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzp2move.moveandjump().x - spfzPlayer1.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x += (spfzp2move.moveandjump().x - spfzPlayer1.moveandjump().x)
          * Gdx.graphics.getDeltaTime();

      }
      else
      {
        spfzp2move.attributes().x += 0f;
        spfzPlayer1.attributes().x += 0f;
      }
    }
    // if player 2 is neutral
    else
    {

      if (stageCamera.position.x <= camboundary[0] + 1 && spfzp2move.attributes().x <= stageboundary[0])
      {
        spfzPlayer1.attributes().x += 0f;
      }
      else
      {

        spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x -= (spfzPlayer1.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void P1SRightMRight() {
    // if player 2 is moving left
    if (spfzp2move.p2movement.get(0))
    {
      spfzPlayer1.attributes().x += spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();
      spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
    // if player 2 is moving right
    else if (spfzp2move.p2movement.get(1))
    {

      if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzPlayer1.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

        if (spfzp2move.attributes().x <= stageboundary[1] - (spfzp2move.setrect().width * .33f))
        {

        }
      }
      else if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        if (spfzp2move.attributes().x <= stageboundary[1] - (spfzp2move.setrect().width * .33f))
        {

        }
        else
        {
          spfzPlayer1.attributes().x += spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();
          spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        }

      }
    }
    else
    {
      spfzPlayer1.attributes().x += spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();

    }
  }

  public void P1SLeftMLeft() {
    // if player 2 is moving to the left
    if (spfzp2move.p2movement.get(0))
    {
      if (spfzPlayer1.attributes().x <= stageboundary[0] + (spfzPlayer1.setrect().width * .33f))
      {
        spfzp2move.attributes().x += 0f;
      }
      else
      {
        if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
        {
          spfzp2move.attributes().x -= spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();
          spfzPlayer1.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        }
        else if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
        {
          spfzPlayer1.attributes().x += -(spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime());
          spfzp2move.attributes().x += -(spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime());
        }

      }
    }

    // if player 2 is moving to right
    else if (spfzp2move.p2movement.get(1))
    {

      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x -= (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      else
      {
        spfzp2move.attributes().x -= 0f;
        spfzp2move.attributes().x -= 0f;
      }
    }

    else
    {
      spfzPlayer1.attributes().x -= spfzPlayer1.moveandjump().x * Gdx.graphics.getDeltaTime();
    }
  }

  public void P1SLeftMRight() {
    // if player 2 is moving to the left
    if (spfzp2move.p2movement.get(0))
    {
      // if player one walkspeed is greater than player 2's walkspeed
      if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x += (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }
      // force player one to move to the left since player 2's walkspeed is
      // faster
      else if (spfzPlayer1.moveandjump().x < spfzp2move.moveandjump().x)
      {
        spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x - spfzp2move.moveandjump().x)
          * Gdx.graphics.getDeltaTime();
      }

    }

    // if player 2 is neutral
    else if (!spfzp2move.p2movement.get(0) && !spfzp2move.p2movement.get(1))
    {

      if (stageCamera.position.x + 1 >= camboundary[1] && spfzp2move.attributes().x + 1 >= stageboundary[1])
      {
        spfzPlayer1.attributes().x += 0f;
      }
      else
      {

        spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();
        spfzPlayer1.attributes().x += (spfzPlayer1.moveandjump().x * .125f) * Gdx.graphics.getDeltaTime();

      }
    }
    // if player 2 is moving to right
    else if (spfzp2move.p2movement.get(1))
    {
      if (spfzp2move.moveandjump().x < spfzPlayer1.moveandjump().x)
      {

        spfzp2move.attributes().x += (spfzPlayer1.moveandjump().x * .25f) * Gdx.graphics.getDeltaTime();

      }
      else if (spfzp2move.moveandjump().x > spfzPlayer1.moveandjump().x)
      {
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();

      }
    }
  }

  public void newcrossing() {
    if (spfzPlayer1.center() < spfzp2move.center())
    {
      if (spfzPlayer1.moveandjump().x > spfzp2move.moveandjump().x)
      {
        spfzPlayer1.attributes().x -= spfzPlayer1.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();

        spfzp2move.attributes().x += spfzPlayer1.moveandjump().x / 5 * Gdx.graphics.getDeltaTime();
      }
      else
      {
        spfzPlayer1.attributes().x -= spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
        spfzp2move.attributes().x += spfzp2move.moveandjump().x * Gdx.graphics.getDeltaTime();
      }
    }
  }

  public void pausechar() {
    spfzPlayer1.tempfdur = spfzPlayer1.spfzanimationstate.currentAnimation.getFrameDuration();
    spfzPlayer1.spfzanimationstate.currentAnimation.setFrameDuration(5f);
  }

  public void switchp1() {
    float facing;
    p1spec -= 100f;
    sigp1lock = true;
    strt1 = true;
    spfzPlayer1.attacking = false;
    if (spfzPlayer1.attributes().scaleX > 0)
    {
      facing = spfzPlayer1.attributes().scaleX;
    }
    else
    {
      facing = spfzPlayer1.attributes().scaleX * -1;
    }
    p1xattr = spfzPlayer1.attributes().x;

    p1yattr = spfzPlayer1.attributes().y;

    CompositeItemVO player1char1;
    CompositeItemVO player1char2;
    CompositeItemVO player1char3;
    ItemWrapper playerone;
    switch (p1)
    {
      case 0:

        p1++;
        stage.stageSSL().getEngine().removeEntity(p1c1);
        player1char2 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c2 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char2);
        p1c2.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c2.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c2.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c2, player1char2.composite);
        stage.stageSSL().getEngine().addEntity(p1c2);
        playerone = new ItemWrapper(p1c2);
        break;

      case 1:
        p1++;
        stage.stageSSL().getEngine().removeEntity(p1c2);
        player1char3 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c3 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char3);
        p1c3.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c3.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c3.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c3, player1char3.composite);
        stage.stageSSL().getEngine().addEntity(p1c3);
        playerone = new ItemWrapper(p1c3);

        break;

      case 2:
        p1 = 0;
        stage.stageSSL().getEngine().removeEntity(p1c3);
        player1char1 = stage.stageSSL().loadVoFromLibrary(characters.get(p1));
        p1c1 = stage.stageSSL().entityFactory.createEntity(stage.stageWrapper().getEntity(), player1char1);
        p1c1.getComponent(ZIndexComponent.class).layerIndex = 2;
        p1c1.getComponent(ZIndexComponent.class).setZIndex(4);
        p1c1.getComponent(MainItemComponent.class).itemIdentifier = characters.get(p1);
        stage.stageSSL().entityFactory.initAllChildren(stage.stageSSL().getEngine(), p1c1, player1char1.composite);
        stage.stageSSL().getEngine().addEntity(p1c1);
        playerone = new ItemWrapper(p1c1);
        break;
      default:
        playerone = new ItemWrapper(null);
        break;
    }

    playerone.addScript((IScript) arrscripts.get(p1));

    // Ensure collision box is setup correctly

    if (facing > 0)
    {

      spfzPlayer1.setrect().set(spfzPlayer1.setrect().x + spfzPlayer1.adjustX,
        spfzPlayer1.attributes().y + spfzPlayer1.adjustY, spfzPlayer1.setrect().width,
        spfzPlayer1.setrect().height);
    }
    else
    {
      spfzPlayer1.setrect().set(spfzPlayer1.setrect().x - spfzPlayer1.adjustX,
        spfzPlayer1.attributes().y + spfzPlayer1.adjustY, spfzPlayer1.setrect().width, spfzPlayer1.setrect().height);

    }
    spfzPlayer1.attributes().scaleX = facing;

    // Start swap particle effect

    if (stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
    {
      stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects
        .removeValue(stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
          .getComponent(SPFZParticleComponent.class).pooledeffects.get(0), true);
    }
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = spfzPlayer1.center();
    stage.stageWrapper().getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = spfzPlayer1.attributes().y
      + spfzPlayer1.setrect().height * .5f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).worldMultiplyer = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(TransformComponent.class).scaleX = 1f;
    stage.stageWrapper().getChild("p1swap").getChild("swapp1").getEntity().getComponent(SPFZParticleComponent.class).startEffect();
    switchcount++;
    switchp1 = false;

  }

  //Swap particle effect - may go into a Particle Class?
  if(root.getChild("p1swap").

  getChild("swapp1").

  getEntity()
      .

  getComponent(SPFZParticleComponent .class).pooledeffects !=null)

  {
    if (root.getChild("p1swap").getChild("swapp1").getEntity()
      .getComponent(SPFZParticleComponent.class).pooledeffects.size != 0)
    {
      if ((!root.getChild("p1swap").getChild("swapp1").getEntity()
        .getComponent(SPFZParticleComponent.class).pooledeffects.get(0).isComplete()) && stage.strt1)
      {
        root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).x = stage.spfzplayer1.center();
        root.getChild("p1swap").getEntity().getComponent(TransformComponent.class).y = stage.spfzplayer1.attributes().y
          + stage.spfzplayer1.dimensions().height / 2;
      }
    }
  }
}
