package com.tictactoe.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tictactoe.core.Game.IUserType;

public class BoardIndex {
	private Map<IUserType, List<Integer>> userPositionlistMap = new HashMap<IUserType, List<Integer>>();
	
	BoardIndex(Map<IUserType, User> typeUserMap) {
		for (IUserType userType : typeUserMap.keySet()) {
			List<Integer> positionList = new ArrayList<Integer>();
			this.userPositionlistMap.put(userType, positionList);
		}
	}
	
	public void addUserPosition(IUserType userType, int movekey) {
		this.userPositionlistMap.get(userType).add(movekey);
	}
	
	public List<Integer> getPositionList(IUserType userType) {
		return this.userPositionlistMap.get(userType);
	}
}
