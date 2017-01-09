package multi_agent_painting.mas.behaviours;

import java.lang.reflect.Method;
import java.util.HashSet;

import multi_agent_painting.mas.agents.Agent;
import multi_agent_painting.mas.agents.PhysicalForces;
import multi_agent_painting.mas.exceptions.AgentRuntimeException;
import multi_agent_painting.mas.exceptions.RoleInitException;
import multi_agent_painting.physics.PhysicsVector;
import multi_agent_painting.physics.Space;
import tools.appControl.Logger;
import tools.drawing.PhysicalInfo;

public abstract class Behaviours {
	protected HashSet<Method>	behaviours	= new HashSet<Method>();
	private Method				initMethod;

	public Behaviours() throws RoleInitException {
		final Class<? extends Behaviours> behavioursClass = this.getClass();
		final Method[] methods = behavioursClass.getDeclaredMethods();
		final String behaviourClass = this.getClass().getSimpleName();
		for (final Method method : methods) {
			if (method.isAnnotationPresent(Behaviour.class)) {
				this.behaviours.add(method);
				Logger.debug(behaviourClass + " initing - " + method.getName()
						+ " - blind method");
			} else if (method.isAnnotationPresent(InitAgent.class)) {
				if (getInitMethod() == null) {
					setInitMethod(method);
				} else {
					throw new RoleInitException(
							"Please, only one InitAgent method...");
				}
				Logger.debug(behaviourClass + " initing - " + method.getName()
						+ " - initializer");
			} else {
				final Class<? extends Behaviours> currentClass = this
						.getClass();
				Logger.debug(behaviourClass + " initing - " + method.getName()
						+ " of " + currentClass.getName()
						+ " ignored - not carrying a behavioural annotation.");
			}
		}
		Logger.info("init done for " + this);
	}

	public void blindBehaviours(final Agent agent) throws AgentRuntimeException {
		for (final Method behaviour : this.behaviours) {
			try {
				Logger.debug("agent " + agent.hashCode() + "/"
						+ this.getClass().getSimpleName() + "::blind::"
						+ behaviour.getName());
				behaviour.invoke(this, agent);
			} catch (final Exception e) {
				throw new AgentRuntimeException(e);
			}
		}
	}

	public Method getInitMethod() {
		return this.initMethod;
	}

	public void init(final Agent hostingAgent) throws RoleInitException {
		if (this.initMethod != null) {
			try {
				this.initMethod.invoke(this, hostingAgent);
			} catch (final Exception e) {
				throw new RoleInitException(e);
			}
		}
	}

	public PhysicalForces react(
			final Agent hostingAgent,
			final PhysicsVector vector,
			final PhysicalInfo bodyPhysicalInfo,
			final Space space) throws AgentRuntimeException {
		final PhysicalForces result = PhysicalForces.NONE.modifiableCopy();
		for (final Method reactiveMethod : this.behaviours) {
			try {
				final PhysicalForces partialResult = (PhysicalForces) reactiveMethod
						.invoke(this, hostingAgent, vector, bodyPhysicalInfo, space);
				result.add(partialResult);
			} catch (final Exception e) {
				throw new AgentRuntimeException(e);
			}
		}
		return result;
	}

	public void setInitMethod(final Method initMethod) {
		this.initMethod = initMethod;
	}

	@Override
	public String toString() {
		return "[(" + this.getClass().getSimpleName() + this.behaviours + ")]";
	}

	public void setMusicalValue(Double value) {
		// TODO Auto-generated method stub
		
	}

}
