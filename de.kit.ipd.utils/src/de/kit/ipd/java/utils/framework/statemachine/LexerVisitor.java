package de.kit.ipd.java.utils.framework.statemachine;


public interface LexerVisitor<T> {

	void visit(StateMachine<T> machine, int state, T token);
	
}
