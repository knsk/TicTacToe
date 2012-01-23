package com.tictactoe.core;

public class Board {
	private final int size;
	public enum UserType {O, X};
	private UserType[][] matrix;
	
	public Board(int size) {
		if (size <= 0) { // Board size needs to be non-zero positive integer
			throw new IllegalArgumentException();
		}
		this.size = size;
		this.matrix = new UserType[size][size];
	}
	
	/**
	 * Put O or X to specified place on the board
	 */
	public void put(int movekey, UserType userType) {
		// Range check the move key with the board size
		if (!isValidMovekey(movekey)) {
			throw new IllegalArgumentException();
		}
		// Compute the array indices from move key
		int[] indices = computeIndicesFromMovekey(movekey);
		// Put the move on the board
		this.matrix[indices[0]][indices[1]] = userType;
	}
	
	/**
	 * Check if the move key is a valid key in the range of board size
	 * @param movekey  move key to check the validity
	 * @return         if the move key is valid in the board size
	 */
	public boolean isValidMovekey(int movekey) {
		if (movekey < 1 || movekey > this.size*this.size) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Check the availability of the move key position on the board
	 */
	public boolean isAvailable(int movekey) {
		int[] indices = computeIndicesFromMovekey(movekey);
		if (this.matrix[indices[0]][indices[1]] == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Compute the corresponding indices of the matrix from the move key
	 * @param movekey
	 * @return matrix indices array in 0th and 1st elements
	 */
	private int[] computeIndicesFromMovekey(int movekey) {
		int x = (int) Math.floor((movekey - 1) / this.size);
		int y = (movekey - 1) % this.size;
		// System.out.println("movekey: " + movekey + " = " + "(" + x + "," + y + ")");
		int[] indices = {x, y};	
		return indices;
	}
	
	/**
	 * Check the current state of the board and return if the game is over
	 * Note: Game can be over only by the previous move
	 * @param userType      the type of user who made the previous move
	 * @param prevmove  previous move key
	 * @return          if the game is over
	 */
	public boolean isGameOver(UserType userType, int prevmove) {
		int[] indices = computeIndicesFromMovekey(prevmove);
		boolean gameover = true;
		// 1) Check horizontal line
		for (int y = 0; y < this.size; y++) {
			if (this.matrix[indices[0]][y] != userType) {
				gameover = false;
			}
		}
		if (gameover) return true;
		
		// 2) Check vertical line
		gameover = true;
		for (int x = 0; x < this.size; x++) {
			if (this.matrix[x][indices[1]] != userType) {
				gameover = false;
			}
		}
		if (gameover) return true;
		
		// 3) Check diagonal if the previous move is on a diagonal line
		gameover = true;
		if ((this.size % 2 == 1) && (indices[0] == indices[1]) && (indices[0] == (this.size - 1) / 2)) { // Center point
			for (int i = 0; i < this.size; i++) { 
				if (this.matrix[i][i] != userType) { // Left top to right bottom
					gameover = false;
				}
			}
			if (gameover) return true;
			gameover = true;
			for (int i = 0; i < this.size; i++) {
				if (this.matrix[i][this.size - 1 - i] != userType) { // Right top to left bottom
					gameover = false;
				}
			}
			if (gameover) return true;
		} else if (indices[0] == indices[1]) { // on a left top to right bottom diagonal line
			for (int i = 0; i < this.size; i++) { 
				if (this.matrix[i][i] != userType) { // Left top to right bottom
					gameover = false;
				}
			}
			if (gameover) return true;
		} else if ((indices[0] + indices[1]) == (this.size - 1)) { // on a right top to left bottom diagonal line
			for (int i = 0; i < this.size; i++) {
				if (this.matrix[i][this.size - 1 - i] != userType) { // Right top to left bottom
					gameover = false;
				}
			}
			if (gameover) return true;
		} else {
			return false;
		}
		
		return gameover;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public UserType[][] getMatrix() {
		// Deep copy the matrix
		UserType[][] matrix = new UserType[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
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
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (matrix[i][j] == UserType.O) {
					strBoardState.append("O|");
				} else if (matrix[i][j] == UserType.X) {
					strBoardState.append("X|");
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
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				strMovekeyMatrix.append(movekey++ + "|");
			}
			strMovekeyMatrix.append(System.getProperty("line.separator"));
		}
		return strMovekeyMatrix.toString();
	}
}
