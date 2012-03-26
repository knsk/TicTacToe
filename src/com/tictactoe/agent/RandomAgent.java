package com.tictactoe.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tictactoe.core.Board;
import com.tictactoe.core.Game.IUserType;

public class RandomAgent extends AbstractAgent {
	
	RandomAgent() {}
	
	/**
	 * Decide the next move at random based on the current state of the board
	 * @param board current state of the board
	 * @return the movekey decided 
	 */
	@Override
	public int selectNextMove(Board board) {
		// Analyze the state of the board and decide the next move
		IUserType[][] matrix = board.getMatrix();
		// Create a list of available move keys
		List<Integer> availableMovekeyList = new ArrayList<Integer>();
		int movekey = 1;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (board.isAvailable(movekey)) {
					availableMovekeyList.add(movekey);
				}
				movekey++;
			}
		}
		
		// Pick the next move at random
		Random random = new Random();
		int nextMovekeyIndex = random.nextInt(availableMovekeyList.size());
		return availableMovekeyList.get(nextMovekeyIndex);
	}
}
