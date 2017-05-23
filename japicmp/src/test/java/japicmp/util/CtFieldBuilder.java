package japicmp.util;

import javassist.CannotCompileException;

import javassist.ApiField;
import javassist.Modifier;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;

import java.util.ArrayList;
import java.util.List;

public class CtFieldBuilder {
	public static final String DEFAULT_FIELD_NAME = "field";
	private ClassApiSignature type = ClassApiSignature.intType;
	private String name = DEFAULT_FIELD_NAME;
	private int modifier = Modifier.PUBLIC;
	private List<String> annotations = new ArrayList<>();
	private Object constantValue = null;

	public CtFieldBuilder type(ClassApiSignature classApiSignature) {
		this.type = classApiSignature;
		return this;
	}

	public CtFieldBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CtFieldBuilder syntheticModifier() {
		this.modifier = this.modifier | ModifierHelper.ACC_SYNTHETIC;
		return this;
	}

	public ApiField addToClass(ClassApiSignature classApiSignature) throws CannotCompileException {
		ApiField ApiField = new ApiField(this.type, this.name, classApiSignature);
		ApiField.setModifiers(this.modifier);
		if (constantValue != null) {
			if (constantValue instanceof Boolean) {
				classApiSignature.addField(ApiField, ApiField.Initializer.constant((Boolean) constantValue));
			} else if (constantValue instanceof Integer) {
				classApiSignature.addField(ApiField, ApiField.Initializer.constant((Integer) constantValue));
			} else if (constantValue instanceof Long) {
				classApiSignature.addField(ApiField, ApiField.Initializer.constant((Long) constantValue));
			} else if (constantValue instanceof String) {
				classApiSignature.addField(ApiField, ApiField.Initializer.constant((String) constantValue));
			} else {
				throw new IllegalArgumentException("Provided constant value for field is of unsupported type: " + constantValue.getClass().getName());
			}
		} else {
			classApiSignature.addField(ApiField);
		}
		for (String annotation : annotations) {
			ClassFile classFile = classApiSignature.getClassFile();
			ConstPool constPool = classFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			Annotation annot = new Annotation(annotation, constPool);
			attr.setAnnotation(annot);
			ApiField.getFieldInfo().addAttribute(attr);
		}
		return ApiField;
	}

	public static CtFieldBuilder create() {
		return new CtFieldBuilder();
	}

	public CtFieldBuilder withAnnotation(String annotation) {
		this.annotations.add(annotation);
		return this;
	}

	public CtFieldBuilder staticAccess() {
		this.modifier = this.modifier | Modifier.STATIC;
		return this;
	}

	public CtFieldBuilder privateAccess() {
		this.modifier = this.modifier & ~Modifier.PUBLIC;
		this.modifier = this.modifier | Modifier.PRIVATE;
		return this;
	}

	public CtFieldBuilder packageProtectedAccess() {
		this.modifier = this.modifier & ~Modifier.PUBLIC;
		return this;
	}

	public CtFieldBuilder protectedAccess() {
		this.modifier = this.modifier & ~Modifier.PROTECTED;
		return this;
	}

	public CtFieldBuilder finalAccess() {
		this.modifier = this.modifier | Modifier.FINAL;
		return this;
	}

	public CtFieldBuilder withConstantValue(Object value) {
		this.constantValue = value;
		return this;
	}
}
