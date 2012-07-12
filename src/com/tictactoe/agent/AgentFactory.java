package com.tictactoe.agent;

public class AgentFactory {
	public enum AgentType {RANDOM, RATIONAL};
	
	public static IAgent createAgent(AgentType agentType) {
		if (agentType.equals(AgentType.RANDOM)) {
			return new RandomAgent();
		} else if (agentType.equals(AgentType.RATIONAL)) {
			return new RationalAgent();
		} else {
			return null;
		}
	}
}