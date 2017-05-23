package japicmp.filter;


import javassist.ApiField;
import javassist.NotFoundException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationFieldFilter extends AnnotationFilterBase implements FieldFilter {
	private static final Logger LOGGER = Logger.getLogger(AnnotationBehaviorFilter.class.getName());

	public AnnotationFieldFilter(String filterString) {
		super(filterString.substring(1));
	}

	@Override
	public boolean matches(ApiField ApiField) {
		List attributes = ApiField.getFieldInfo().getAttributes();
		boolean hasAnnotation = hasAnnotation(attributes);
		if (!hasAnnotation) {
			ClassApiSignature declaringClass = ApiField.getDeclaringClass();
			hasAnnotation = hasAnnotation(declaringClass.getClassFile().getAttributes());
			if (!hasAnnotation) {
				try {
					declaringClass = declaringClass.getDeclaringClass();
					if (declaringClass != null) {
						hasAnnotation = hasAnnotation(declaringClass.getClassFile().getAttributes());
					}
				} catch (NotFoundException e) {
					LOGGER.log(Level.FINE, "Failed to load class '" + declaringClass.getName() + "': " + e.getLocalizedMessage(), e);
				}
			}
		}
		return hasAnnotation;
	}

	@Override
	public String toString() {
		return "@" + annotationClassName;
	}
}
