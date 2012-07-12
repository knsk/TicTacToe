package com.tictactoe.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.tictactoe.agent.AgentFactory;
import com.tictactoe.agent.AgentFactory.AgentType;
import com.tictactoe.agent.IAgent;

public class Game {
	private Board board;
	private Random random;
	private final int difficulty; // How strong the agent is
	private int movecount;
	private int prevmove = -1;
	private IUserType currentTurn; // UserType of the current user holding the turn
	
	private User user;
	private User agentUser;
	
	private IAgent randomAgent;
	private IAgent rationalAgent;
	
	public interface IUserType {}
	
	private enum UserType implements IUserType { 
		O {
		    public String toString() {
		    	return "O";
		    }
		}, 
		X {
			public String toString() {
				return "X";
		    }
		}
	};
	
	private Map<IUserType, User> typeUserMap = new HashMap<IUserType, User>();
	
	private static int MIN_DIFFICULTY = 0;
	private static int MAX_DIFFICULTY = 10;
	private static int DEFAULT_DIFFICULTY = 10;
	private static int DEFAULT_BOARD_SIZE = 3;
	
	Game() {
		this(DEFAULT_DIFFICULTY);
	}
	
	Game(int difficulty) {
		this(difficulty, DEFAULT_BOARD_SIZE);
	}
	
	Game(int difficulty, int boardsize) {
		if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
			throw new IllegalArgumentException();
		}
		if (boardsize < 0) {
			throw new IllegalArgumentException();
		}
		this.board = new Board(boardsize);
		this.random = new Random();
		this.difficulty = difficulty;
		this.user = new User(Game.getUserUserType());
		this.agentUser = new User(Game.getAgentUserType());
		this.typeUserMap.put(user.getUserType(), this.user);
		this.typeUserMap.put(agentUser.getUserType(), this.agentUser);
		BoardIndex boardIndex = new BoardIndex(typeUserMap);
		this.board.setBoardIndex(boardIndex);
		this.randomAgent = AgentFactory.createAgent(AgentType.RANDOM);
		this.rationalAgent = AgentFactory.createAgent(AgentType.RATIONAL);
	}
	
	public void put(IUserType userType, int movekey) {
		this.typeUserMap.get(userType).put(this.board, movekey);
		this.board.getBoardIndex().addUserPosition(userType, movekey);
		this.prevmove = movekey;
		this.movecount++;
		changeTurn();
	}
	
	private void changeTurn() {
		if (this.currentTurn == UserType.O) {
			this.currentTurn = UserType.X;
		} else if (this.currentTurn == UserType.X) {
			this.currentTurn = UserType.O;
		}
	}
	
	public static IUserType getUserUserType() {
		return UserType.O;
	}
	
	public static IUserType getAgentUserType() {
		return UserType.X;
	}
	
	/**
	 * Select the agent based on the difficulty of the game and decide the next move
	 * @return next movekey
	 */
	public int getAgentMove() {
		int val = this.random.nextInt(MAX_DIFFICULTY);
		if (val >= this.difficulty) {
			return this.randomAgent.selectNextMove(this.board);
		} else {
			return this.rationalAgent.selectNextMove(this.board);
		}
	}
	
	public int getMoveCount() {
		return this.movecount;
	}
	
	public int getBoardSize() {
		return this.board.getSize();
	}
	
	public String getWelcomeMessage() {
		return "Welecome to Tic-Tac-Toe ...";
	}
	
	public String getStrBoardState() {
		return this.board.getStrBoardState();
	}
	
	public String getStrMovekeyMatrix() {
		return this.board.getStrMovekeyMatrix();
	}
	
	public boolean isValidMovekey(int movekey) {
		return this.board.isValidMovekey(movekey);
	}
	
	public boolean isAvailable(int movekey) {
		return this.board.isAvailable(movekey);
	}
	
	public boolean isGameOver(IUserType userType) {
		return this.board.isGameOver(userType, this.prevmove);
	}
	
}
