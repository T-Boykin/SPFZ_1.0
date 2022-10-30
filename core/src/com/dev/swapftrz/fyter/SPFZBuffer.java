package com.dev.swapftrz.fyter;

import java.util.ArrayList;
import java.util.List;

//TODO look into handling buffer processing on a separate thread
class SPFZBuffer {
  private final int buffsize = 60, //could be fps for the game
    bufferWindow = 12;
  private List<Integer> buffer = new ArrayList<Integer>();
  private ArrayList<int[]> commandInputs = new ArrayList<int[]>();
  private int[] command;
  SPFZPlayer player;

  public SPFZBuffer(SPFZPlayer player) {
    this.player = player;
    commandInputs = player.inputs;
  }

  public void addToBuffer(boolean[] movement) {
    if (buffer.size() == buffsize)
      buffer.clear();

    for (int i = 0; i < movement.length; i++)
      if (movement[i]) {
        buffer.add(i);
        break;
      }
  }

  public void bufferUpdate() {
    addToBuffer(player.movementInput());

    if (command == null)
      command = inputCommandCheck();
    System.out.println("Buffer input ----- " + buffer.get(buffer.size() - 1));
  }

  //TODO will need to check for inverse command input for left facing
  public int[] inputCommandCheck() {
    for (int i = 0; i < commandInputs.size() - 1; i++) {
      int[] currentCommand = commandInputs.get(i);
      int bufferSize = buffer.size(), amountToSkip = (bufferSize - 1) - bufferWindow,
        remainingInputLength = currentCommand.length - 2;

      //check for the 1st input of a command reading the buffer backwards
      if (buffer.get(bufferSize - 1) == currentCommand[currentCommand.length - 1]) {
        Object[] bufferWindowInputsObjectArray = buffer.stream().skip(amountToSkip).limit(bufferWindow).toArray();
        Integer[] bufferWindowInputs = new Integer[bufferWindowInputsObjectArray.length];
        System.arraycopy(bufferWindowInputsObjectArray, 0, bufferWindowInputs, 0, bufferWindowInputs.length);
        int bufferInputWindowIndex = 0, lastInputWindowIndex = 0;

        for (int j = remainingInputLength; j > 0; j--) {
          for (int k = bufferWindowInputs.length - 1; k > 0; k--) {
            //found the next input for the command within the buffer window
            if (currentCommand[j] == bufferWindowInputs[k]) {
              bufferInputWindowIndex = k;
              break;
            }
          }
          //input out of sequence for the command
          if (bufferInputWindowIndex > lastInputWindowIndex && lastInputWindowIndex != 0 && j != remainingInputLength) {
            break;
          }
          else {
            lastInputWindowIndex = bufferInputWindowIndex;
            //reached last possible input within window
            if (lastInputWindowIndex == 0)
              return currentCommand;
          }
        }
        //input completed successfully
        if (bufferInputWindowIndex == lastInputWindowIndex)
          return currentCommand;
      }
    }
    return null;
  }

  public void setCommandInputs(ArrayList<int[]> commandInputs) { this.commandInputs = commandInputs; }

  public boolean dash() { return command == commandInputs.get(0) || command == commandInputs.get(1); }

  public void clearCommand() { command = null; }
}
