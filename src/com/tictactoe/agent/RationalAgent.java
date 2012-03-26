package com.tictactoe.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tictactoe.core.Board;
import com.tictactoe.util.BoardUtil;
import com.tictactoe.core.Game;
import com.tictactoe.core.Game.IUserType;


public class RationalAgent extends AbstractAgent {
	
	RationalAgent() {}
	
	/**
	 * Strategy: (from Wikipedia: http://en.wikipedia.org/wiki/Tic-tac-toe)
	 * 1) Win: If the player has two in a row, play the third to get three in a row.
	 * 	  	# of winning patterns: size + size + 2 (horizontal + vertical + diagonal)
	 * 		sizeCsize-1 situations of one step before win for each winning pattern
	 * 2) Block: If the [opponent] has two in a row, play the third to block them.
	 * 3) Fork: Create an opportunity where you can win in two ways.
	 * 4) Block opponent's Fork: 
	 * 		Option 1: Create two in a row to force the opponent into defending, as long as it doesn't result in the opponent to fork. 
	 * 				  For example, if "X" has a corner, "O" has the center, and "X" has the opposite corner as well, "O" must not play a corner in order to win. 
	 * 				  (Playing a corner in this scenario creates a fork for "X" to win.)
	 *		Option 2: If there is a configuration where the opponent can fork, block that fork.
	 * 5) Center: Play the center as long as it doesn't result in the opponent to fork.
	 * 6) Opposite corner: If the opponent is in the corner, play the opposite corner.
	 * 7) Empty corner: Play in a corner square.
	 * 8) Empty side: Play in a middle square on any of the 4 sides.
	 *  TODO There is a pattern that can be beaten: 1 5 9 ... Need to prevent multiple forkable ways to happen
	 */
	@Override
	public int selectNextMove(Board board) {
		IUserType[][] matrix = board.getMatrix();
		List<Integer> agentPositionList = board.getBoardIndex().getPositionList(Game.getAgentUserType());
		List<Integer> userPositionList = board.getBoardIndex().getPositionList(Game.getUserUserType());
		Random random = new Random();
		
		if (agentPositionList.size() >= (board.getSize() - 1)) {
			// 1) Check if able to win the game
			List<Integer> winmoveList = getWinningMoveList(matrix, Game.getAgentUserType());
			if (!winmoveList.isEmpty()) { 
				int winmoveListIndex = random.nextInt(winmoveList.size());
				return winmoveList.get(winmoveListIndex);
			}
		}
		if (userPositionList.size() >= (board.getSize() - 1)) {
			// 2) Check if loose the game without a block
			List<Integer> blockmoveList = getWinningMoveList(matrix, Game.getUserUserType());
			if (!blockmoveList.isEmpty()) { 
				int blockmoveListIndex = random.nextInt(blockmoveList.size());
				System.out.println("Block a win");
				return blockmoveList.get(blockmoveListIndex); // Just do the best to block one of them in case multiple blocks are needed
			}
		}
		if (agentPositionList.size() >= (board.getSize() - 1)) {
			// 3) Check if able to create a fork
			List<Integer> forkmoveList = getForkingMoveList(matrix, Game.getAgentUserType());
			if (!forkmoveList.isEmpty()) {
				int forkmoveListIndex = random.nextInt(forkmoveList.size());
				System.out.println("Create a fork");
				return forkmoveList.get(forkmoveListIndex);
			}
		}
		
		if (userPositionList.size() >= (board.getSize() - 1)) {
			// 4) Check if need to block a fork as long as it doesn't result in the opponent to fork
			List<Integer> forkblockList = getForkingMoveList(matrix, Game.getUserUserType());
			if (!forkblockList.isEmpty()) {
				List<Integer> reachingList = new ArrayList<Integer>(); // List of fork block moves that force opponent to defend
				List<Integer> forkAllowingList = new ArrayList<Integer>(); // List of moves that allow a later fork
				for (int movekey : forkblockList) {
					if (allowFork(matrix, Game.getAgentUserType(), movekey)) {
						forkAllowingList.add(movekey);
					} else {
						if (canReach(matrix, Game.getAgentUserType(), movekey)) {
							reachingList.add(movekey);
						}
					}
				}
				if (!reachingList.isEmpty()) {
					System.out.println("Block a fork forcing to defend");
					return reachingList.get(random.nextInt(reachingList.size())); // Create a reach to force the opponent into defending
				} else {
					System.out.println("Just block a fork");
					forkblockList.removeAll(forkAllowingList);
					if (!forkblockList.isEmpty()) {
						return forkblockList.get(random.nextInt(forkblockList.size()));
					}
					
				}
			}
		}
		
		// 5) Get the center if available as long as it doesn't result in the opponent to fork
		int centerMovekey = BoardUtil.getCenterMovekey(board.getSize());
		if (centerMovekey != -1 && board.isAvailable(centerMovekey) && !allowFork(matrix, Game.getAgentUserType(), centerMovekey)) { // If center exists, is available and don't allow fork
			System.out.println("Get the center");
			return centerMovekey;
		}
		
		// 6) Opposite Corner: If the opponent is in the corner, play the opposite corner as long as it doesn't result in the opponent to fork
		List<Integer> userCornerList = getUserCornerList(matrix, Game.getUserUserType());
		if (!userCornerList.isEmpty()) {
			List<Integer> availableOppositeCornerList = new ArrayList<Integer>();
			for (Integer cornerMovekey : userCornerList) {
				int oppositeCornerMovekey = getOppositeCornerMovekey(cornerMovekey, board.getSize());
				if (board.isAvailable(oppositeCornerMovekey)) {
					availableOppositeCornerList.add(oppositeCornerMovekey);
				}
			}
			// TODO Prevent chosing opposite corner when it results in a later fork ex. O: 1 X: 9 (opposite corner) O: 3, X: 2 (forced to defend) O: 7 (fork)
			if (!availableOppositeCornerList.isEmpty()) {
				List<Integer> unforkableOppositeConerList = new ArrayList<Integer>();
				for (int oppositeCornerMove : availableOppositeCornerList) {
					if (!allowFork(matrix, Game.getUserUserType(), oppositeCornerMove)) {
						unforkableOppositeConerList.add(oppositeCornerMove);
					}
				}
				if (!unforkableOppositeConerList.isEmpty()) {
					System.out.println("Get an opposit corner");
					int availableOppositeCornerListIndex = random.nextInt(availableOppositeCornerList.size());
					return availableOppositeCornerList.get(availableOppositeCornerListIndex);
				}
			}
		}
		
		// 7) Empty corner: Play in a corner square.
		List<Integer> availableCornerList = getUserCornerList(matrix, null);
		if (!availableCornerList.isEmpty()) {
			System.out.println("Get an availabe corner");
			int availableCornerListIndex = random.nextInt(availableCornerList.size());
			return availableCornerList.get(availableCornerListIndex);
		}
		
		// 8) Empty side: Play in a middle square on any of the 4 sides.
		List<Integer> availableSideList = new ArrayList<Integer>();
		for (int i = 1; i <= board.getSize(); i++) {
			if (board.isAvailable(i)) { // Top 
				availableSideList.add(i);
			}
			if (board.isAvailable(board.getSize() * board.getSize() - i + 1)) { // Bottom
				availableSideList.add(board.getSize() * board.getSize() - i + 1);
			}
			if (board.isAvailable(board.getSize() * (i - 1) + i)) { // Left
				availableSideList.add(board.getSize() * (i - 1) + i);
			}
			if (board.isAvailable(board.getSize() * i)) { // Right
				availableSideList.add(board.getSize() * i);
			}
		}
			System.out.println("Get an available side");
			int availableSideListIndex = random.nextInt(availableSideList.size());
			return availableSideList.get(availableSideListIndex);
	}
	
	/**
	 * Investigate the current state of the board to see if there are moves that winningUserType can win
	 * @param matrix               Current state of the board
	 * @param winningUserType      UserType of the user to get the winning move
	 * @return                     List of winning movekeys if exist, null if not
	 */
	private static List<Integer> getWinningMoveList(IUserType[][] matrix, IUserType winningUserType) {
		int boardSize = matrix.length;
		List<Integer> winningMoveList = new ArrayList<Integer>();
		// Analyze the board and add winning movekeys to the list
		// 1) Horizontal line
		int winmove = -1;
		int winusercount = 0; // Count of winningUserType on each line
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (winningUserType.equals(matrix[i][j])) {
					winusercount++;
				}
				if (matrix[i][j] == null) {
					winmove = BoardUtil.computeMovekeyFromIndices(boardSize, i, j);
				}
			}
			if (winusercount == (boardSize - 1) && winmove != -1) {
				winningMoveList.add(winmove);
			}
			winmove = -1;
			winusercount = 0;
		}
		
		// Reinitialize winmove and winusercount for vertical line check
		winmove = -1; 
		winusercount = 0;
		// 2) Vertical line
		for (int j = 0; j < boardSize; j++) {
			for (int i = 0; i < boardSize; i++) {
				if (winningUserType.equals(matrix[i][j])) {
					winusercount++;
				}
				if (matrix[i][j] == null) {
					winmove = BoardUtil.computeMovekeyFromIndices(boardSize, i, j);
				}
			}
			if (winusercount == (boardSize - 1) && winmove != -1) {
				winningMoveList.add(winmove);
			}
			winmove = -1;
			winusercount = 0;
		}
		
		// 3) diagonal line
		for (int i = 0; i < boardSize; i++) { // Left top to right bottom
			if (winningUserType.equals(matrix[i][i])) {
				winusercount++;
			}
			if (matrix[i][i] == null) {
				winmove = BoardUtil.computeMovekeyFromIndices(boardSize, i, i);
			}
		}
		if (winusercount == (boardSize - 1) && winmove != -1) {
			winningMoveList.add(winmove);
		}
		// Reinitialize winmove and winusercount for the next diagonal line check
		winmove = -1; 
		winusercount = 0;
		for (int i = 0; i < boardSize; i++) { // Right top to left bottom
			if (winningUserType.equals(matrix[i][boardSize - 1 - i])) {
				winusercount++;
			}
			if (matrix[i][boardSize - 1 - i] == null) {
				winmove = BoardUtil.computeMovekeyFromIndices(boardSize, i, boardSize - 1 - i);
			}
		}
		if (winusercount == (boardSize - 1) && winmove != -1) {
			winningMoveList.add(winmove);
		}
		
		return winningMoveList;
	}
	
	/**
	 * Investigate the current state of the board to see if there are moves that forkingUserType can fork
	 * @param matrix               Current state of the board
	 * @param forkingUserType      UserType of the user to get the forking move
	 * @return                     List of forking movekeys
	 */
	private static List<Integer> getForkingMoveList(IUserType[][] matrix, IUserType forkingUserType) {
		int boardSize = matrix.length;
		List<Integer> forkingMoveList = new ArrayList<Integer>();
		IUserType[][] matrixCopy = new IUserType[boardSize][boardSize]; // Copy of the current matrix for temporal modification	
		// Analyze the board and add forking movekeys to the list
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (matrix[i][j] == null) { // Check if available
					matrixCopy = BoardUtil.getDeepCopy(matrix); // Deep copy the current matrix
					matrixCopy[i][j] = forkingUserType; // Put the forking User's move temporally to see if it makes a fork
					List<Integer> winningMoveList = getWinningMoveList(matrixCopy, forkingUserType);
					if (winningMoveList.size() >= 2) { // Check if forked
						forkingMoveList.add(BoardUtil.computeMovekeyFromIndices(boardSize, i, j));
					}
				}
			}
		}
		return forkingMoveList;
	}
	
	/**
	 * Get the list of corner moves userType has made
	 * @param matrix      	Current state of the board
	 * @param userType      UserType of the user to get the list of corner moves, null to check the list of available corners
	 * @return              List of userType's corner movekeys
	 */
	private static List<Integer> getUserCornerList(IUserType[][] matrix, IUserType userType) {
		int boardSize = matrix.length;
		List<Integer> cornerMoveList = new ArrayList<Integer>();
		if ((userType == null && matrix[0][0] == null) || userType == matrix[0][0]) {
			cornerMoveList.add(BoardUtil.computeMovekeyFromIndices(boardSize, 0, 0));
		}
		if ((userType == null && matrix[0][boardSize - 1] == null) || userType == matrix[0][boardSize - 1]) {
			cornerMoveList.add(BoardUtil.computeMovekeyFromIndices(boardSize, 0, boardSize - 1));
		}
		if ((userType == null && matrix[boardSize - 1][0] == null) || userType == matrix[boardSize - 1][0]) {
			cornerMoveList.add(BoardUtil.computeMovekeyFromIndices(boardSize, boardSize - 1, 0));
		}
		if ((userType == null && matrix[boardSize - 1][boardSize - 1] == null) || userType == matrix[boardSize - 1][boardSize - 1]) {
			cornerMoveList.add(BoardUtil.computeMovekeyFromIndices(boardSize, boardSize - 1, boardSize - 1));
		}

		return cornerMoveList;
	}
	
	/**
	 * Tell if userType allows the opponent to fork after placing at movekey
	 * @param matrix   Current state of the board
	 * @param userType UserType of the user to place the move
	 * @param movekey  movekey for the userType of the user to make a placement
	 * @return         true if the placement allows a reach, false if not
	 */
	private boolean allowFork(IUserType[][] matrix, IUserType userType, int movekey) {
		IUserType opponentUserType;
		if (userType.equals(Game.getAgentUserType())) {
			opponentUserType = Game.getUserUserType();
		} else {
			opponentUserType = Game.getAgentUserType();
		}
		IUserType[][] matrixCopy = BoardUtil.getDeepCopy(matrix); // Deep copy the current matrix for temporal modification
		int boardSize = matrix.length;
		int[] indices = BoardUtil.computeIndicesFromMovekey(movekey, boardSize);
		matrixCopy[indices[0]][indices[1]] = userType;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (matrixCopy[i][j] == null) {
					IUserType[][] matrixCopyCopy = BoardUtil.getDeepCopy(matrixCopy); // Deep copy the current matrix for temporal modification
					matrixCopyCopy[i][j] = opponentUserType;
					int forks = getForkingMoveList(matrixCopyCopy, opponentUserType).size();
					if (forks >= 2) { // Allow multiple forks with this move
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Tell if userType can reach a step before win by placing at movekey
	 * @param matrix   Current state of the board
	 * @param userType UserType of the user to check the reachability
	 * @param movekey  movekey to make a placement for the reach
	 * @return         true if the placement make a reach, false if not
	 */
	private static boolean canReach(IUserType[][] matrix, IUserType userType, int movekey) {
		int boardSize = matrix.length;
		IUserType[][] matrixCopy = new IUserType[boardSize][boardSize]; // Copy of the current matrix for temporal modification
		matrixCopy = BoardUtil.getDeepCopy(matrixCopy); // Deep copy the current matrix
		int[] indices = BoardUtil.computeIndicesFromMovekey(movekey, boardSize);
		matrixCopy[indices[0]][indices[1]] = userType; // Place the temporal move to see if it makes a reach
		
		// Check a reach depending on the place of the move
		if (movekey == BoardUtil.getCenterMovekey(boardSize)) { // (1) Center point
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 3) Check Left top to right bottom diagonal line
			if (BoardUtil.getRightDownDiagonalCount(matrixCopy, userType) == (boardSize - 1)) {
				return true;
			}
			// 4) Check Left bottom to right top diagonal line
			if (BoardUtil.getRightUpDiagonalCount(matrixCopy, userType) == (boardSize - 1)) {
				return true;
			}
			return false;
		} else if (BoardUtil.isRightDownDiagonalMovekey(movekey, boardSize)) { // (2) Left top to right bottom diagonal line
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 3) Check Left top to right bottom diagonal line
			if (BoardUtil.getRightDownDiagonalCount(matrixCopy, userType) == (boardSize - 1)) {
				return true;
			}
			return false;
		} else if (BoardUtil.isRightUpDiagonalMovekey(movekey, boardSize)) { // (3) Left bottom to right top diagonal line
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 3) Check Left bottom to right top diagonal line
			if (BoardUtil.getRightUpDiagonalCount(matrixCopy, userType) == (boardSize - 1)) {
				return true;
			}
			return false;
		} else { // (4) On the side
			// 1) Check horizontal line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			// 2) Check vertical line
			if (BoardUtil.getHorizontalCount(matrixCopy, userType, movekey) == (boardSize - 1)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Get the opposite corner movekey
	 * ex. Opposite corner of movkey: 1 on boardSize: 3 is 9
	 * @param cornerMovekey Movekey of a corner
	 * @param boardSize		Size of the board
	 * @return              Movekey of opposite corner
	 */
	private static int getOppositeCornerMovekey(int cornerMovekey, int boardSize) {
		int[] cornerIndices = BoardUtil.computeIndicesFromMovekey(cornerMovekey, boardSize);
		if (boardSize < 0 || !BoardUtil.isCornerMovekey(cornerMovekey, boardSize)) {
			throw new IllegalArgumentException();
		}
		int oppositCornerMovekey;
		if (cornerIndices[0] == cornerIndices[1]) { // Top left or bottom right corner
			oppositCornerMovekey = (boardSize - cornerIndices[0]) * (boardSize - cornerIndices[1]);
		} else { // Top right or bottom left corner
			oppositCornerMovekey = BoardUtil.computeMovekeyFromIndices(boardSize, cornerIndices[1], cornerIndices[0]); // Switch the index and compute the movekey
		}
		return oppositCornerMovekey;
	}
}
