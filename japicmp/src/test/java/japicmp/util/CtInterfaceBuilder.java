package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import com.google.common.base.Optional;
import javassist.CannotCompileException;



public class CtInterfaceBuilder {
	private String name = "japicmp.Test";
	protected Optional<ClassApiSignature> superInterfaceOptional = Optional.absent();

	public CtInterfaceBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ClassApiSignature addToClassPool(ClassApiSignatureSource classApiSignatureSource) throws CannotCompileException {
//		ClassApiSignature classApiSignature;
//		if (superInterfaceOptional.isPresent()) {
//			classApiSignature = classApiSignatureSource.makeInterface(this.name, superInterfaceOptional.get());
//		} else {
//			classApiSignature = classApiSignatureSource.makeInterface(this.name);
//		}
//		return classApiSignature;
		throw new NoSuchMethodError("Not Implemented");
	}

	public static CtInterfaceBuilder create() {
		return new CtInterfaceBuilder();
	}

	public CtInterfaceBuilder withSuperInterface(ClassApiSignature superInterface) {
		this.superInterfaceOptional = Optional.fromNullable(superInterface);
		return this;
	}
}
