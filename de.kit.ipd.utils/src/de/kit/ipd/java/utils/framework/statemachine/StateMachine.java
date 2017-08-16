package de.kit.ipd.java.utils.framework.statemachine;


public interface StateMachine<T> {
	
	void setEOLState(int index);
	
	void setInput(T input);
	
	void add(State<T>... states);
	
	void run(int state);

	T getNext();
	
	void setNextState(State<T> s);
	
	void setNextState(int index);
	
	State<T> getState(int index);
	
	int size();
	
	void callVisitor(int index, T token);
	
	void addVisitor(LexerVisitor<T> visitor);
	
	void removeVisitor(LexerVisitor<T> visitor);
	
	void appendToken(T token);
	
	T getToken();
	
	void resetToken();
	
	
}
