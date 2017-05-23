package japicmp.util;

import com.criticollab.japicmp.classinfo.ClassApiSignature;
import japicmp.model.JApiClassType;


public class ClassHelper {

	private ClassHelper() {

	}

	public static JApiClassType.ClassType getType(ClassApiSignature classApiSignature) {
		if (classApiSignature.isAnnotation()) {
			return JApiClassType.ClassType.ANNOTATION;
		} else if (classApiSignature.isEnum()) {
			return JApiClassType.ClassType.ENUM;
		} else if (classApiSignature.isInterface()) {
			return JApiClassType.ClassType.INTERFACE;
		} else {
			return JApiClassType.ClassType.CLASS;
		}
	}
}
