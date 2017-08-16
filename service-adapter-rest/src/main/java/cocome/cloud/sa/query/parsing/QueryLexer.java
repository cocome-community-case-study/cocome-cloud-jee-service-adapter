package cocome.cloud.sa.query.parsing;

import java.util.regex.Pattern;

import de.kit.ipd.java.utils.framework.statemachine.CharStreamStateMachine;
import de.kit.ipd.java.utils.framework.statemachine.Lexer;
import de.kit.ipd.java.utils.framework.statemachine.LexerVisitor;
import de.kit.ipd.java.utils.framework.statemachine.State;
import de.kit.ipd.java.utils.framework.statemachine.StateMachine;

public class QueryLexer implements Lexer<CharSequence> {

	private static final State<CharSequence> STATE_0 = new State0();

	private static final State<CharSequence> STATE_1 = new State1();

	private static final State<CharSequence> STATE_2 = new State2();

	private static final State<CharSequence> STATE_3 = new State3();

	private static final State<CharSequence> STATE_4 = new State4();

	private static final State<CharSequence> STATE_5 = new State5();

	private static final State<CharSequence> STATE_6 = new State6();

	/** State-Machine which does the scanning of the source. */
	private final StateMachine<CharSequence> machine = new CharStreamStateMachine();

	/**
	 * Initialize query lexer.
	 */
	QueryLexer() {
		this.machine.add(STATE_0);
		this.machine.add(STATE_1);
		this.machine.add(STATE_2);
		this.machine.add(STATE_3);
		this.machine.add(STATE_4);
		this.machine.add(STATE_5);
		this.machine.add(STATE_6);
	}

	@Override
	public StateMachine<CharSequence> getMachine() {
		return this.machine;
	}

	@Override
	public void addVisitor(final LexerVisitor<CharSequence> visitor) {
		this.machine.addVisitor(visitor);
	}

	/*************************************************************************
	 * STATES LOCAL CLASSES
	 *************************************************************************/

	/**
	 * State0
	 *
	 * @author unknown
	 *
	 */
	public static class State0 implements State<CharSequence> {
		/** Index of State */
		public static final int INDEX = 0;

		/** transition to State0 */
		private final String transitionState0 = "[\\w\\W\\p{Punct}&&[^;\"]&&[^=\\r\\n]]";
		/** transition to State6 */
		private final String transitionState6 = "[=]";
		/** transition to Default */
		private final String transitionDefault = "[\\r]";

		/** empty constructor. */
		State0() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			final String next = String.valueOf(machine.getNext());

			if (Pattern.matches(this.transitionState0, next)) {
				machine.appendToken(next);
				machine.setNextState(0);
				return;
			} else if (Pattern.matches(this.transitionState6, next)) {
				machine.setNextState(6);
				return;
			} else if (Pattern.matches(this.transitionDefault, next)) {
				machine.setNextState(0);
				return;
			}

			throw new IllegalStateException("Error:"

					+ this.transitionState0 + ", or,"
					+ this.transitionState6 + ", or,"
					+ this.transitionDefault + ", or,"
					+ " was expected." + next);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * State1 class.
	 *
	 */
	public static class State1 implements State<CharSequence> {
		/** Index of State */
		public static final int INDEX = 1;

		/** transition to State2 */
		private final String transitionState2 = "[\\w\\W\\p{Punct}&&[^;\"]&&[^\\r\\n]]";
		/** transition to State3 */
		private final String transitionState3 = "[\"]";
		/** transition to State4 */
		private final String transitionState4 = "[;\\n]";
		/** transition to State1 */
		private final String transitionState1 = "[\\r]";

		/** empty constructor. */
		public State1() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			final String next = String.valueOf(machine.getNext());

			if (Pattern.matches(this.transitionState2, next)) {
				machine.appendToken(next);
				machine.setNextState(2);
				return;
			} else if (Pattern.matches(this.transitionState3, next)) {
				machine.appendToken(next);
				machine.setNextState(3);
				return;
			} else if (Pattern.matches(this.transitionState4, next)) {
				machine.setNextState(4);
				return;
			} else if (Pattern.matches(this.transitionState1, next)) {
				machine.setNextState(1);
				return;
			}

			throw new IllegalStateException("Error:"

					+ this.transitionState2 + ", or,"
					+ this.transitionState3 + ", or,"
					+ this.transitionState4 + ", or,"
					+ this.transitionState1 + ", or,"
					+ " was expected." + next);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * STATE 2.
	 */
	public static class State2 implements State<CharSequence> {
		/** Index of state. */
		public static final int INDEX = 2;

		/** transition to State2. */
		private final String transitionState2 = "[\\w\\W\\p{Punct}&&[^;\"]&&[^\\r\\n]]";
		/** transition to State3. */
		private final String transitionState3 = "[\"]";
		/** transition to State4. */
		private final String transitionState4 = "[;\\n]";
		/** transition to Default. */
		private final String transitionDefault = "[\\r]";

		/** empty constructor. */
		public State2() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			final String next = String.valueOf(machine.getNext());

			if (Pattern.matches(this.transitionState2, next)) {
				machine.appendToken(next);
				machine.setNextState(2);
				return;
			} else if (Pattern.matches(this.transitionState3, next)) {
				machine.appendToken(next);
				machine.setNextState(3);
				return;
			} else if (Pattern.matches(this.transitionState4, next)) {
				machine.setNextState(4);
				return;
			} else if (Pattern.matches(this.transitionDefault, next)) {
				machine.setNextState(2);
				return;
			}

			throw new IllegalStateException("Error:"

					+ this.transitionState2 + ", or,"
					+ this.transitionState3 + ", or,"
					+ this.transitionState4 + ", or,"
					+ this.transitionDefault + ", or,"
					+ " was expected." + next);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * STATE 3.
	 */
	public static class State3 implements State<CharSequence> {
		/** Index of state. */
		public static final int INDEX = 3;

		/** transition to State3. */
		private final String transitionState3 = "[\\w\\W\\p{Punct}&&[^\"]]";
		/** transition to State2. */
		private final String transitionState2 = "[\"]";

		/** empty constructor. */
		public State3() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			final String next = String.valueOf(machine.getNext());

			if (Pattern.matches(this.transitionState3, next)) {
				machine.appendToken(next);
				machine.setNextState(3);
				return;
			} else if (Pattern.matches(this.transitionState2, next)) {
				machine.appendToken(next);
				machine.setNextState(2);
				return;
			}

			throw new IllegalStateException("Error:"

					+ this.transitionState3 + ", or,"
					+ this.transitionState2 + ", or,"
					+ " was expected." + next);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * STATE 4.
	 */
	public static class State4 implements State<CharSequence> {
		/** Index of state. */
		public static final int INDEX = 4;

		/** empty constructor. */
		public State4() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			machine.callVisitor(this.getIndex(), machine.getToken());
			machine.resetToken();
			machine.setNextState(1);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * STATE 5.
	 */
	public static class State5 implements State<CharSequence> {
		/** Index of state. */
		public static final int INDEX = 5;

		/** empty constructor. */
		public State5() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {
			machine.callVisitor(this.getIndex(), machine.getToken());
		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

	/**
	 * STATE 6.
	 */
	public static class State6 implements State<CharSequence> {
		/** Index of state. */
		public static final int INDEX = 6;

		/** empty constructor. */
		public State6() {}

		@Override
		public void run(final StateMachine<CharSequence> machine) {

			machine.callVisitor(this.getIndex(), machine.getToken());
			machine.resetToken();
			machine.setNextState(1);

		}

		@Override
		public int getIndex() {
			return INDEX;
		}
	}

}
