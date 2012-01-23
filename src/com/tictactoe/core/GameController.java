package com.tictactoe.core;

import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import com.tictactoe.agent.AgentFactory;
import com.tictactoe.agent.IAgent;

public class GameController {

	public static void main(String args[]) {
		final int boardsize = 3;
		Board board = new Board(boardsize);
		User user = new User(Board.UserType.O);
		User agentUser = new User(Board.UserType.X);
		IAgent agent = AgentFactory.createAgent(AgentFactory.AgentType.RANDOM);
		
		// Game start
		System.out.println("Welecome to Tic-Tac-Toe ...");
		System.out.println(board.getStrBoardState());
		System.out.println(board.getStrMovekeyMatrix());
		boolean gameover = false;
		// Continue the game until game is over
		int movecount = 0;
		while (!gameover) {
			// 1) Wait for the user's input and put the user's selected move
	        int movekey = getMovekeyFromInput();
	        // Check the validity of the move key on the board
	        while (!board.isValidMovekey(movekey)) {
	        	System.out.println("The move key is invalid because it's out of the range. Please input the valid move key.");
				// Wait for the next input
		        movekey = getMovekeyFromInput();
	        }
	        // Check the availability of the move key on the board
	        while (!board.isAvailable(movekey)) {
				System.out.println("You cannot put on that position because it's already taken. Please input the available move key.");
				// Wait for the next input
		        movekey = getMovekeyFromInput();
		        // Check the validity of the move key on the board again
		        while (!board.isValidMovekey(movekey)) {
		        	System.out.println("The move key is invalid because it's out of the range. Please input the valid move key.");
					// Wait for the next input
			        movekey = getMovekeyFromInput();
		        }
			} 
			user.put(board, movekey);
			movecount++;
				
			// 2) Check if the user won the game or draw
			gameover = board.isGameOver(user.getUserType(), movekey);
			if (gameover) {
				System.out.println("You won");
				break;
			}
			if (!moreMovePossible(movecount, boardsize)) {
				System.out.println("Draw");
				break;
			}
			
			// Print the state of the board and move key matrix
			System.out.println(board.getStrBoardState());
			// System.out.println(board.getStrMovekeyMatrix());
			
			// 3) Agent user considers next move and put the decided move
			int nextmove = agent.selectNextMove(board);
			agentUser.put(board, nextmove);
			System.out.println("Agent's move: " + nextmove);
			movecount++;
			
			// Print the state of the board and move key matrix
			System.out.println(board.getStrBoardState());
			System.out.println(board.getStrMovekeyMatrix());
			
			// 4) Check if the agent won the game or draw
			gameover = board.isGameOver(agentUser.getUserType(), nextmove);
			if (gameover) {
				System.out.println("I won");
				break;
			}
			if (!moreMovePossible(movecount, boardsize)) {
				System.out.println("Draw");
				break;
			}
		}
		
		// Print the final state of the board
		System.out.println(board.getStrBoardState());
		
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
