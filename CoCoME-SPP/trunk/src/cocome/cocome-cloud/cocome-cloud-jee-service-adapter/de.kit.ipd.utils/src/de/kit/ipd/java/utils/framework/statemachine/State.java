package de.kit.ipd.java.utils.framework.statemachine;

public interface State<T> {

	void run(StateMachine<T> machine);
	
	int getIndex();

}
