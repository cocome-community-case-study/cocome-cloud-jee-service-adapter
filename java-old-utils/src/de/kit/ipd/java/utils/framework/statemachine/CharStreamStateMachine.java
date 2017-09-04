package de.kit.ipd.java.utils.framework.statemachine;


public class CharStreamStateMachine extends AbstractStateMachine<CharSequence> {
	
	/************************************************************************
	 * FIELDS
	 ***********************************************************************/
	
	private int pointer = 0;
	
	private char nextChar;
	
	private StringBuilder builder = new StringBuilder();
	
	@Override
	protected void _moveNextToken() {
		nextChar = input.charAt(pointer);
		pointer++;
		if(pointer >= input.length()){
			setMachineStopped();
		}
	}

	@Override
	protected CharSequence _getToken() {
		return builder.toString();
	}

	@Override
	protected CharSequence _appendToken(CharSequence token) {
		return builder.append(token);
	}

	@Override
	protected void _resetToken() {
		builder.delete(0, builder.length());
	}

	@Override
	protected CharSequence _next() {
		return String.valueOf(nextChar);
	}
	

}
