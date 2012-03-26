package com.tictactoe.util;

import java.util.Arrays;

import com.tictactoe.core.Game.IUserType;

public class BoardUtil {

	/**
	 * Compute the corresponding movekey from indices on the matrix of boardSize
	 * @param boardSize  Size of the board to compute the movekey from indices
	 * @param i  Index to indicate the row of the matrix
	 * @param j  Index to indicate the column of the matrix
	 * @return   Corresponding movekey of the given indices 
	 */
	public static int computeMovekeyFromIndices(int boardSize, int i, int j) {
		return i * boardSize + j + 1;
	}

	/**
	 * Compute the corresponding indices of the matrix from the movekey
	 * @param movekey
	 * @return matrix indices array in 0th and 1st elements
	 */
	public static int[] computeIndicesFromMovekey(int movekey, int boardSize) {
		int x = (int) Math.floor((movekey - 1) / boardSize);
		int y = (movekey - 1) % boardSize;
		int[] indices = {x, y};	
		return indices;
	}
	
	/**
	 * Get the center movekey of a board with the boardSize
	 * @param boardSize Size of the board to get the center movekey
	 * @return          Movekey of the center on the board, -1 if center doesn't exist 
	 */
	public static int getCenterMovekey(int boardSize) {
		if (boardSize % 2 == 0) {
			return -1; // No center point
		} else {
			return (boardSize * boardSize + 1) / 2;
		}
	}
	
	/**
	 * Tell if the given movekey is on the corner of the board
	 * @param  movekey   Movekey to check if on the corner
	 * @param  boardSize Size of the board
	 * @return           True if the movekey is on the corner, false if not
	 */
	public static boolean isCornerMovekey(int movekey, int boardSize) {
		int[] indices = computeIndicesFromMovekey(movekey, boardSize);
		if (indices[0] == 0 && indices[1] == 0) {
			return true;
		} else if (indices[0] == 0 && indices[1] == (boardSize - 1)) {
			return true;
		} else if (indices[0] == (boardSize - 1) && indices[1] == 0) {
			return true;
		} else if (indices[0] == (boardSize - 1) && indices[1] == (boardSize - 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tell if the given movekey is on a diagonal line
	 * @param movekey   Movekey to check if on the diagonal
	 * @param boardSize Size of the board
	 * @return          True if on a diagonal line, false if not
	 */
	public static boolean isDiagonalMovekey(int movekey, int boardSize) {
		if (isRightDownDiagonalMovekey(movekey, boardSize) || isRightUpDiagonalMovekey(movekey, boardSize)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tell if the given movekey is on the left top to right bottom diagonal line
	 * @param movekey   Movekey to check if on the left top to right bottom diagonal
	 * @param boardSize Size of the board
	 * @return          True if on the left top to right bottom diagonal line, false if not
	 */
	public static boolean isRightDownDiagonalMovekey(int movekey, int boardSize) {
		int[] indices = computeIndicesFromMovekey(movekey, boardSize);
		if (indices[0] == indices[1]) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Tell if the given movekey is on the right top to left bottom diagonal line
	 * @param movekey   Movekey to check if on the left top to right bottom diagonal
	 * @param boardSize Size of the board
	 * @return          True if on the left bottom to right top diagonal line, false if not
	 */
	public static boolean isRightUpDiagonalMovekey(int movekey, int boardSize) {
		int[] indices = computeIndicesFromMovekey(movekey, boardSize);
		if ((indices[0] + indices[1]) == (boardSize - 1)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the count of userType on the horizontal line of matrix where movekey is on
	 * @param matrix   Current state of the board
	 * @param userType UserType to count
	 * @param movekey  Movekey whose horizontal line to count
	 * @return         Count of userType on the horizontal line where movekey is on
	 */
	public static int getHorizontalCount(IUserType[][] matrix, IUserType userType, int movekey) {
		int boardSize = matrix.length;
		int[] indices = computeIndicesFromMovekey(movekey, boardSize);
		int count = 0;
		for (int y = 0; y < boardSize; y++) {
			if (userType.equals(matrix[indices[0]][y])) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Get the count of userType on the vertical line of matrix where movekey is on
	 * @param matrix   Current state of the board
	 * @param userType UserType to count
	 * @param movekey  Movekey whose vertical line to count
	 * @return         Count of userType on the vertical line where movekey is on
	 */
	public static int getVericalCount(IUserType[][] matrix, IUserType userType, int movekey) {
		int boardSize = matrix.length;
		int[] indices = computeIndicesFromMovekey(movekey, boardSize);
		int count = 0;
		for (int x = 0; x < boardSize; x++) {
			if (userType.equals(matrix[x][indices[1]])) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Get the count of userType on the left top to right bottom diagonal line of matrix
	 * @param matrix   Current state of the board
	 * @param userType UserType to count
	 * @return         Count of userType on the left top to right bottom diagonal line
	 */
	public static int getRightDownDiagonalCount(IUserType[][] matrix, IUserType userType) {
		int boardSize = matrix.length;
		int count = 0;
		for (int i = 0; i < boardSize; i++) { 
			if (userType.equals(matrix[i][i])) { // Left top to right bottom
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Get the count of userType on the left bottom to right top diagonal line of matrix
	 * @param matrix   Current state of the board
	 * @param userType UserType to count
	 * @return         Count of userType on the left bottom to right top diagonal line
	 */
	public static int getRightUpDiagonalCount(IUserType[][] matrix, IUserType userType) {
		int boardSize = matrix.length;
		int count = 0;
		for (int i = 0; i < boardSize; i++) {
			if (userType.equals(matrix[i][boardSize - 1 - i])) { // Left bottom to right top
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Deep copy the giveb matrix
	 * @param matrix  Current state of the board
	 * @return        Deep copied matrix
	 */
	public static IUserType[][] getDeepCopy(IUserType[][] matrix) {
		IUserType[][] matrixCopy = new IUserType[matrix.length][matrix[0].length];
		for (int k = 0; k < matrix.length; k++) {
			matrixCopy[k] = Arrays.copyOf(matrix[k], matrix[k].length);
		}
		return matrixCopy;
	}

}
