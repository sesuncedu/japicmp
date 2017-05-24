package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ApiMethod;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import javassist.CannotCompileException;

import java.util.ArrayList;
import java.util.List;

public class CtMethodBuilder extends CtBehaviorBuilder {
	public static final String DEFAULT_METHOD_NAME = "method";
	protected String body = "return null;";
	private String name = DEFAULT_METHOD_NAME;
	private ClassApiSignature returnType;
	private List<String> annotations = new ArrayList<>();

	public CtMethodBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CtMethodBuilder modifier(int modifier) {
		this.modifier = modifier;
		return this;
	}

	public CtMethodBuilder returnType(ClassApiSignature classApiSignature) {
		this.returnType = classApiSignature;
		return this;
	}

	public CtMethodBuilder syntheticModifier() {
		this.modifier = this.modifier | ModifierHelper.ACC_SYNTHETIC;
		return this;
	}

	public CtMethodBuilder parameters(ClassApiSignature[] parameters) {
		return (CtMethodBuilder) super.parameters(parameters);
	}

	public CtMethodBuilder parameter(ClassApiSignature parameter) {
		return (CtMethodBuilder) super.parameter(parameter);
	}

	public CtMethodBuilder exceptions(ClassApiSignature[] exceptions) {
		return (CtMethodBuilder) super.exceptions(exceptions);
	}

	public CtMethodBuilder body(String body) {
		this.body = body;
		return this;
	}

	public CtMethodBuilder publicAccess() {
		return (CtMethodBuilder) super.publicAccess();
	}

	public CtMethodBuilder privateAccess() {
		return (CtMethodBuilder) super.privateAccess();
	}

	public CtMethodBuilder protectedAccess() {
		return (CtMethodBuilder) super.protectedAccess();
	}

	public CtMethodBuilder staticAccess() {
		return (CtMethodBuilder) super.staticAccess();
	}

	public CtMethodBuilder abstractMethod() {
		return (CtMethodBuilder) super.abstractMethod();
	}

	public CtMethodBuilder finalMethod() {
		return (CtMethodBuilder) super.finalMethod();
	}

	public CtMethodBuilder withAnnotation(String annotation) {
		this.annotations.add(annotation);
		return this;
	}

	public ApiMethod addToClass(ClassApiSignature declaringClass) throws CannotCompileException {
		if (this.returnType == null) {
			this.returnType = declaringClass;
		}
//		ApiMethod ctMethod = CtNewMethod.make(this.modifier, this.returnType, this.name, this.parameters, this.exceptions, this.body, declaringClass);
//		ctMethod.setModifiers(this.modifier);
//		declaringClass.addMethod(ctMethod);
//		for (String annotation : annotations) {
//			ClassFile classFile = declaringClass.getClassFile();
//			ConstPool constPool = classFile.getConstPool();
//			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
//			Annotation annot = new Annotation(annotation, constPool);
//			attr.setAnnotation(annot);
//			ctMethod.getMethodInfo().addAttribute(attr);
//		}
//		return ctMethod;
		throw new NoSuchMethodError("Not Implemented yet");
	}

	public static CtMethodBuilder create() {
		CtMethodBuilder ctMethodBuilder = new CtMethodBuilder();
		return ctMethodBuilder;
	}
}
