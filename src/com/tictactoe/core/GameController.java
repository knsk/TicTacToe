package com.tictactoe.core;

import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class GameController {
	public static void main(String args[]) {
		Game game = new Game();
		// Game start
		System.out.println(game.getWelcomeMessage());
		System.out.println(game.getStrBoardState());
		System.out.println(game.getStrMovekeyMatrix());
		
		boolean gameover = false;
		// Continue the game until game is over
		while (!gameover) {
			// 1) Wait for the user's input and put the user's selected move
	        int movekey = getMovekeyFromInput();
	        // Check the validity of the move key on the board
	        while (!game.isValidMovekey(movekey)) {
	        	System.out.println("The move key is invalid because it's out of the range. Please input the valid move key.");
				// Wait for the next input
		        movekey = getMovekeyFromInput();
	        }
	        // Check the availability of the move key on the board
	        while (!game.isAvailable(movekey)) {
				System.out.println("You cannot put on that position because it's already taken. Please input the available move key.");
				// Wait for the next input
		        movekey = getMovekeyFromInput();
		        // Check the validity of the move key on the board again
		        while (!game.isValidMovekey(movekey)) {
		        	System.out.println("The move key is invalid because it's out of the range. Please input the valid move key.");
					// Wait for the next input
			        movekey = getMovekeyFromInput();
		        }
			}
	        game.put(Game.getUserUserType(), movekey);
				
			// 2) Check if the user won the game or draw
			gameover = game.isGameOver(Game.getUserUserType());
			if (gameover) {
				System.out.println("You won");
				break;
			}
			if (!moreMovePossible(game.getMoveCount(), game.getBoardSize())) {
				System.out.println("Draw");
				break;
			}
			
			// Print the state of the board and move key matrix
			System.out.println(game.getStrBoardState());
			// System.out.println(board.getStrMovekeyMatrix());
			
			// 3) Agent user considers next move and put the decided move
			int nextmove =game.getAgentMove();
			game.put(Game.getAgentUserType(), nextmove);
			System.out.println("Agent's move: " + nextmove);
			
			// Print the state of the board and move key matrix
			System.out.println(game.getStrBoardState());
			System.out.println(game.getStrMovekeyMatrix());
			
			// 4) Check if the agent won the game or draw
			gameover = game.isGameOver(Game.getAgentUserType());
			if (gameover) {
				System.out.println("I won");
				break;
			}
			if (!moreMovePossible(game.getMoveCount(), game.getBoardSize())) {
				System.out.println("Draw");
				break;
			}
		}
		
		// Print the final state of the board
		System.out.println(game.getStrBoardState());
		
	}
	
	private static int getMovekeyFromInput() {
		System.out.println("Please input your next move:");
        Scanner sc = new Scanner(System.in);
        String strMovekey = sc.nextLine();
        while (!StringUtils.isNumeric(strMovekey)) {
        	System.out.println("Move key needs to be an integer. Please enter your next move:");
        	strMovekey = sc.nextLine();
        }
        return Integer.parseInt(strMovekey);
	}
	
	/**
	 * Check if there still remains a place for move 
	 * @param movecount
	 * @param boardsize
	 * @return
	 */
	private static boolean moreMovePossible(int movecount, int boardsize) {
		if (movecount < boardsize * boardsize) {
			return true;
		} else {
			return false;
		}
	}
}
