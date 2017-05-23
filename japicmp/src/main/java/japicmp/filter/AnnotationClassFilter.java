package japicmp.filter;


import javassist.NotFoundException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationClassFilter extends AnnotationFilterBase implements ClassFilter {
	private static final Logger LOGGER = Logger.getLogger(AnnotationBehaviorFilter.class.getName());

	public AnnotationClassFilter(String filterString) {
		super(filterString.substring(1));
	}

	@Override
	public boolean matches(ClassApiSignature classApiSignature) {
		List attributes = classApiSignature.getClassFile().getAttributes();
		boolean hasAnnotation = hasAnnotation(attributes);
		if (!hasAnnotation) {
			try {
				ClassApiSignature declaringClass = classApiSignature.getDeclaringClass();
				if (declaringClass != null) {
					attributes = declaringClass.getClassFile().getAttributes();
					hasAnnotation = hasAnnotation(attributes);
				}
			} catch (NotFoundException e) {
				LOGGER.log(Level.FINE, "Failed to load class '" + classApiSignature.getName() + "': " + e.getLocalizedMessage(), e);
			}
		}
		return hasAnnotation;
	}

	@Override
	public String toString() {
		return "@" + annotationClassName;
	}
}
