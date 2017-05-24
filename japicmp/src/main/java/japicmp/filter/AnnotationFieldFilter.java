package japicmp.filter;


import com.criticollab.japicmp.classinfo.api.ApiField;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationFieldFilter extends AnnotationFilterBase implements FieldFilter {
	private static final Logger LOGGER = Logger.getLogger(AnnotationBehaviorFilter.class.getName());

	public AnnotationFieldFilter(String filterString) {
		super(filterString.substring(1));
	}

	@Override
	public boolean matches(ApiField apiField) {
		List attributes = apiField.getFieldInfo().getAttributes();
		boolean hasAnnotation = hasAnnotation(attributes);
		if (!hasAnnotation) {
			ClassApiSignature declaringClass = apiField.getDeclaringClass();
			hasAnnotation = hasAnnotation(declaringClass.getClassFile().getAttributes());
			if (!hasAnnotation) {
				try {
					declaringClass = declaringClass.getDeclaringClass();
					if (declaringClass != null) {
						hasAnnotation = hasAnnotation(declaringClass.getClassFile().getAttributes());
					}
				} catch (ClassNotFoundException e) {
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
