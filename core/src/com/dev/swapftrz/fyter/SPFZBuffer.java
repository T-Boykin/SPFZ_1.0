package com.dev.swapftrz.fyter;

import java.util.ArrayList;
import java.util.List;

class SPFZBuffer
{
  private final int buffsize = 60; //could be fps for the game
  private List<Integer> buffer = new ArrayList<Integer>(), last16 = new ArrayList<Integer>();
  private ArrayList<int[]> specialMoveInputs = new ArrayList<int[]>();
  SPFZPlayer player;

  public SPFZBuffer(SPFZPlayer player) {
    this.player = player;
    specialMoveInputs = player.inputs;
  }

  public void addToBuffer(boolean[] movement) {
    if (buffer.size() == buffsize)
      buffer.clear();

    if (last16.size() == 15)
      last16.clear();

    for (int i = 0; i < movement.length; i++)
      if (movement[i])
      {
        buffer.add(i);
        last16.add(i);
      }
  }

  public void bufferUpdate() {
    addToBuffer(player.movement());
    System.out.println("Buffer input ----- " + buffer.get(buffer.size() - 1));
  }

  public void setSpecialMoveInputs(ArrayList<int[]> specialMoveInputs) {
    this.specialMoveInputs = specialMoveInputs;
  }
}
