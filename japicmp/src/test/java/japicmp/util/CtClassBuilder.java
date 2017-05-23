package japicmp.util;

import com.google.common.base.Optional;


import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import java.util.ArrayList;
import java.util.List;

public class CtClassBuilder {
	public static final String DEFAULT_CLASS_NAME = "japicmp.Test";
	private String name = DEFAULT_CLASS_NAME;
	private int modifier = Modifier.PUBLIC;
	private List<String> annotations = new ArrayList<>();
	private Optional<ClassApiSignature> superclass = Optional.absent();
	private List<ClassApiSignature> interfaces = new ArrayList<>();

	public CtClassBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CtClassBuilder syntheticModifier() {
		this.modifier = this.modifier | ModifierHelper.ACC_SYNTHETIC;
		return this;
	}

	public CtClassBuilder withAnnotation(String annotation) {
		this.annotations.add(annotation);
		return this;
	}

	public CtClassBuilder abstractModifier() {
		this.modifier = this.modifier | Modifier.ABSTRACT;
		return this;
	}

	public CtClassBuilder finalModifier() {
		this.modifier = this.modifier | Modifier.FINAL;
		return this;
	}

	public CtClassBuilder privateModifier() {
		this.modifier = this.modifier & ~Modifier.PUBLIC;
		this.modifier = this.modifier & ~Modifier.PROTECTED;
		this.modifier |= Modifier.PRIVATE;
		return this;
	}

	public CtClassBuilder enumModifier() {
		this.modifier = this.modifier | Modifier.ENUM;
		return this;
	}

	public CtClassBuilder withSuperclass(ClassApiSignature superclass) {
		this.superclass = Optional.of(superclass);
		return this;
	}

	public ClassApiSignature addToClassPool(ClassApiSignatureSource classApiSignatureSource) {
		ClassApiSignature classApiSignature;
		if (this.superclass.isPresent()) {
			classApiSignature = classApiSignatureSource.makeClass(this.name, this.superclass.get());
		} else {
			classApiSignature = classApiSignatureSource.makeClass(this.name);
		}
		classApiSignature.setModifiers(this.modifier);
		for (String annotation : annotations) {
			ClassFile classFile = classApiSignature.getClassFile();
			ConstPool constPool = classFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
			Annotation annot = new Annotation(annotation, constPool);
			attr.setAnnotation(annot);
			classApiSignature.getClassFile2().addAttribute(attr);
		}
		for (ClassApiSignature interfaceCtClass : interfaces) {
			classApiSignature.addInterface(interfaceCtClass);
		}
		return classApiSignature;
	}

	public static CtClassBuilder create() {
		return new CtClassBuilder();
	}

	public CtClassBuilder implementsInterface(ClassApiSignature classApiSignature) {
		interfaces.add(classApiSignature);
		return this;
	}
}
