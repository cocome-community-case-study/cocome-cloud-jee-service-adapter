package de.kit.ipd.java.utils.framework.statemachine;


public interface ParserVisitor {
	
	
	void visit(StateMachine machine, int state, CharSequence token);

}
