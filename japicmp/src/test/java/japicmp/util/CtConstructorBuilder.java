package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ApiConstructor;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import javassist.CannotCompileException;

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

	public ApiConstructor addToClass(ClassApiSignature declaringClass) throws CannotCompileException {
//		ApiConstructor ctConstructor = CtNewConstructor.make(this.parameters, this.exceptions, this.body, declaringClass);
//		ctConstructor.setModifiers(this.modifier);
//		declaringClass.addConstructor(ctConstructor);
//		return ctConstructor;
		throw new NoSuchMethodError("not implemented");
	}
}
