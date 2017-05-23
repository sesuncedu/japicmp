package japicmp.util;


import javassist.Modifier;

public abstract class CtBehaviorBuilder {
	protected int modifier = 0;
	protected ClassApiSignature[] parameters = new ClassApiSignature[]{};
	protected ClassApiSignature[] exceptions = new ClassApiSignature[]{};

	public CtBehaviorBuilder parameters(ClassApiSignature[] parameters) {
		this.parameters = parameters;
		return this;
	}

	public CtBehaviorBuilder parameter(ClassApiSignature parameter) {
		if (this.parameters == null) {
			this.parameters = new ClassApiSignature[]{parameter};
		} else {
			ClassApiSignature[] newParameters = new ClassApiSignature[this.parameters.length + 1];
			System.arraycopy(this.parameters, 0, newParameters, 0, this.parameters.length);
			newParameters[this.parameters.length] = parameter;
			this.parameters = newParameters;
		}
		return this;
	}

	public CtBehaviorBuilder exceptions(ClassApiSignature[] exceptions) {
		this.exceptions = exceptions;
		return this;
	}

	public CtBehaviorBuilder publicAccess() {
		this.modifier = this.modifier | Modifier.PUBLIC;
		return this;
	}

	public CtBehaviorBuilder privateAccess() {
		this.modifier = this.modifier | Modifier.PRIVATE;
		return this;
	}

	public CtBehaviorBuilder protectedAccess() {
		this.modifier = this.modifier | Modifier.PROTECTED;
		return this;
	}

	public CtBehaviorBuilder staticAccess() {
		this.modifier = this.modifier | Modifier.STATIC;
		return this;
	}

	public CtBehaviorBuilder abstractMethod() {
		this.modifier = this.modifier | Modifier.ABSTRACT;
		return this;
	}

	public CtBehaviorBuilder finalMethod() {
		this.modifier = this.modifier | Modifier.FINAL;
		return this;
	}
}
