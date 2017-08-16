package de.kit.ipd.java.utils.framework.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractStateMachine<T> implements StateMachine<T>{
	
	/************************************************************************
	 * FIELDS
	 ***********************************************************************/
	
	private State<T> currentState;
	
	private int eolState;
	
	private boolean running = false;

	protected T input;
	
	private Map<Integer, State<T>> states = new HashMap<>();
	
	private List<LexerVisitor<T>> visitors = new ArrayList<LexerVisitor<T>>();
	
	/************************************************************************
	 * CONSTRUCTOR
	 ***********************************************************************/
	
	
	
	/************************************************************************
	 * PUBLIC
	 ***********************************************************************/
	public void setEOLState(int index) {
		this.eolState = index;
	}
	
	@Override
	public void setInput(T input) {
		this.input = input;
	}
	
	@Override
	public void add(State<T>...states) {
		if(states!=null){
			for(State<T> nextState:states){
				this.states.put(nextState.getIndex(), nextState);
			}
			return;
		}
		throw new IllegalArgumentException("list of states empty or null");
	}
	
	/**
	 * Run the 
	 * @param init
	 */
	@Override
	public void run(int state){
		currentState = getState(state);
		setMachineRunning();
		while(isMachineRunning()) {
			currentState.run(this);
		}
		getState(eolState).run(this);
	}
	
	@Override
	public T getToken() {
		return _getToken();
	}
	
	@Override
	public void appendToken(T token) {
		_appendToken(token);
	}
	
	public void resetToken() {
		_resetToken();
	}
	
	@Override
	public int size() {
		return states.size();
	}

	@Override
	public T getNext() {
		_moveNextToken();
		return _next();
	}
	
	@Override
	public void setNextState(State<T> s) {
		this.currentState = s;
	}
	
	@Override
	public void setNextState(int index) {
		this.currentState = getState(index);
	}
	
	@Override
	public State<T> getState(int index) {
		return states.get(index);
	}
	
	@Override
	public void addVisitor(LexerVisitor<T> visitor) {
		if(visitor!=null){
			for(LexerVisitor<T> nextVisitor:visitors){
				if(nextVisitor.equals(visitor)){
					return;
				}
			}
			this.visitors.add(visitor);
		}
	}
	
	@Override
	public void removeVisitor(LexerVisitor<T> visitor) {
		if(visitor!=null){
			this.visitors.remove(visitor);
		}
	}
	
	@Override
	public void callVisitor(int index, T token) {
		for(LexerVisitor<T> nextVisitor:visitors){
			nextVisitor.visit(this, index, token);
		}
	}
	
	/************************************************************************
	 * PRIVATE
	 ***********************************************************************/
	
	protected T getInput() {
		return input;
	}
	
	protected void setMachineRunning() {
		running = true;
	}
	
	protected void setMachineStopped() {
		running = false;
	}
	
	protected boolean isMachineRunning(){
		return running;
	}
	
	protected abstract void _moveNextToken();
	
	protected abstract T _getToken();
	
	protected abstract T _appendToken(T token);
	
	protected abstract void _resetToken();
	
	protected abstract T _next();
}
