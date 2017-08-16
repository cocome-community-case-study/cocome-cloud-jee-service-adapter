package de.kit.ipd.java.utils.framework.statemachine;


public interface Lexer<T> {
	
	StateMachine<T> getMachine();
	
	void addVisitor(LexerVisitor<T> visitor);
}
