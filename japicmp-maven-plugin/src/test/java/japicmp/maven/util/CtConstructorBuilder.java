package japicmp.maven.util;

import javassist.CannotCompileException;

import javassist.CtConstructor;
import javassist.CtNewConstructor;

public class CtConstructorBuilder extends CtBehaviorBuilder {
	protected String body = "System.out.println(\"a\");";

	public static CtConstructorBuilder create() {
		return new CtConstructorBuilder();
	}

	public CtConstructorBuilder modifier(int modifier) {
		this.modifier = modifier;
		return this;
	}

	public CtConstructorBuilder parameters(ClassApiSignature[] parameters) {
		return (CtConstructorBuilder) super.parameters(parameters);
	}

	public CtConstructorBuilder parameter(ClassApiSignature parameter) {
		return (CtConstructorBuilder) super.parameter(parameter);
	}

	public CtConstructorBuilder exceptions(ClassApiSignature[] exceptions) {
		return (CtConstructorBuilder) super.exceptions(exceptions);
	}

	public CtConstructorBuilder body(String body) {
		this.body = body;
		return this;
	}

	public CtConstructorBuilder publicAccess() {
		return (CtConstructorBuilder) super.publicAccess();
	}

	public CtConstructorBuilder protectedAccess() {
		return (CtConstructorBuilder) super.protectedAccess();
	}

	public CtConstructorBuilder privateAccess() {
		return (CtConstructorBuilder) super.privateAccess();
	}

	public CtConstructor addToClass(ClassApiSignature declaringClass) throws CannotCompileException {
		CtConstructor ctConstructor = CtNewConstructor.make(this.parameters, this.exceptions, this.body, declaringClass);
		ctConstructor.setModifiers(this.modifier);
		declaringClass.addConstructor(ctConstructor);
		return ctConstructor;
	}
}
