package com.tictactoe.core;

import com.tictactoe.core.Game.IUserType;
import com.tictactoe.util.BoardUtil;

public class Board {
	private final int size;
	private IUserType[][] matrix;
	private BoardIndex boardIndex = null;
	
	public Board(int size) {
		if (size <= 0) { // Board size needs to be non-zero positive integer
			throw new IllegalArgumentException();
		}
		this.size = size;
		this.matrix = new IUserType[size][size];
	}
	
	/**
	 * Put O or X to specified place on the board
	 */
	public void put(IUserType userType, int movekey) {
		// Range check the move key with the board size
		if (!isValidMovekey(movekey)) {
			System.err.println("Invalid movekey: " + movekey);
			throw new IllegalArgumentException();
		}
		if (!isAvailable(movekey)) {
			System.err.println("Already placed movekey: " + movekey);
			throw new IllegalArgumentException();
		}
		// Compute the array indices from move key
		int[] indices = BoardUtil.computeIndicesFromMovekey(movekey, getSize());
		// Put the move on the board
		this.matrix[indices[0]][indices[1]] = userType;	
	}
	
	public BoardIndex getBoardIndex() {
		return this.boardIndex;
	}
	
	public void setBoardIndex(BoardIndex boardIndex) {
		this.boardIndex = boardIndex;
	}
	
	/**
	 * Check if the move key is a valid key in the range of board size
	 * @param movekey  move key to check the validity
	 * @return         if the move key is valid in the board size
	 */
	public boolean isValidMovekey(int movekey) {
		if (movekey < 1 || movekey > getSize()*getSize()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Check the availability of the move key position on the board
	 */
	public boolean isAvailable(int movekey) {
		int[] indices = BoardUtil.computeIndicesFromMovekey(movekey, this.size);
		if (this.matrix[indices[0]][indices[1]] == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check the current state of the board and return if the game is over
	 * Note: Game can be over only by the previous move
	 * @param userType      the type of user who made the previous move
	 * @param prevmove  previous move key
	 * @return          if the game is over
	 */
	public boolean isGameOver(IUserType userType, int prevmove) {
		// Check game over depending on the place of the move
		if (prevmove == BoardUtil.getCenterMovekey(getSize())) { // (1) Center point
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getVericalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 3) Check Left top to right bottom diagonal line
			if (BoardUtil.getRightDownDiagonalCount(getMatrix(), userType) == getSize()) {
				return true;
			}
			// 4) Check Left bottom to right top diagonal line
			if (BoardUtil.getRightUpDiagonalCount(getMatrix(), userType) == getSize()) {
				return true;
			}
			return false;
		} else if (BoardUtil.isRightDownDiagonalMovekey(prevmove, getSize())) { // (2) Left top to right bottom diagonal line
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getVericalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 3) Check Left top to right bottom diagonal line
			if (BoardUtil.getRightDownDiagonalCount(getMatrix(), userType) == getSize()) {
				return true;
			}
			return false;
		} else if (BoardUtil.isRightUpDiagonalMovekey(prevmove, getSize())) { // (3) Left bottom to right top diagonal line
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getVericalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 3) Check Left bottom to right top diagonal line
			if (BoardUtil.getRightUpDiagonalCount(getMatrix(), userType) == getSize()) {
				return true;
			}
			return false;
		} else { // (4) On the side
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getVericalCount(getMatrix(), userType, prevmove) == getSize()) {
				return true;
			}
			return false;
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
	public IUserType[][] getMatrix() {
		// Deep copy the matrix
		IUserType[][] matrix = new IUserType[getSize()][getSize()];
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				matrix[i][j] = this.matrix[i][j];
			}
		}
		
		return matrix;
	}
	
	/** Create a string of board state something like below
	* X| | |
	*  |X| |
	* O|O| |
	*/
	public String getStrBoardState() {
		StringBuilder strBoardState = new StringBuilder("Board: " + System.getProperty("line.separator"));
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				if (matrix[i][j] == Game.getUserUserType()) {
					strBoardState.append(Game.getUserUserType() + "|");
				} else if (matrix[i][j] == Game.getAgentUserType()) {
					strBoardState.append(Game.getAgentUserType() + "|");
				} else {
					strBoardState.append(" |");
				}
			}
			strBoardState.append(System.getProperty("line.separator"));
		}
		return strBoardState.toString();
	}
	
	/** Create a string of move key matrix like below
	* 1|2|3|
	* 4|5|6|
	* 7|8|9|
	*/
	public String getStrMovekeyMatrix() {
		StringBuilder strMovekeyMatrix = new StringBuilder("Move key: " + System.getProperty("line.separator"));
		int movekey = 1;
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				strMovekeyMatrix.append(movekey++ + "|");
			}
			strMovekeyMatrix.append(System.getProperty("line.separator"));
		}
		return strMovekeyMatrix.toString();
	}
}
