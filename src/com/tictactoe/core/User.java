package com.tictactoe.core;


public class User {
	private final Board.UserType userType; // O or X
	
	public User(Board.UserType type) {
		this.userType = type;
	}
	
	public void put(Board board, int movekey) {
		board.put(movekey, this.userType);
	}
	
	public Board.UserType getUserType() {
		return this.userType;
	}
}
