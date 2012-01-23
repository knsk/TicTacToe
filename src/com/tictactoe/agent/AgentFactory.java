package com.tictactoe.agent;

public class AgentFactory {
	public static enum AgentType {RANDOM};
	
	public static IAgent createAgent(AgentType agentType) {
		return new RandomAgent();
	}
}