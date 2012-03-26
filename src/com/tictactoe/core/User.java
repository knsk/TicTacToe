package com.tictactoe.core;

import com.tictactoe.core.Game.IUserType;


public class User {
	private final IUserType userType; // O or X
	
	public User(IUserType type) {
		this.userType = type;
	}
	
	public void put(Board board, int movekey) {
		board.put(this.userType, movekey);
	}
	
	public IUserType getUserType() {
		return this.userType;
	}
}
