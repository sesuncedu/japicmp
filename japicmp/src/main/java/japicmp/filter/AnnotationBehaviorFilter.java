package japicmp.filter;

import com.criticollab.japicmp.classinfo.api.ApiBehavior;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationBehaviorFilter extends AnnotationFilterBase implements BehaviorFilter {
	private static final Logger LOGGER = Logger.getLogger(AnnotationBehaviorFilter.class.getName());

	public AnnotationBehaviorFilter(String filterString) {
		super(filterString.substring(1));
	}

	@Override
	public boolean matches(ApiBehavior apiBehavior) {
		List attributes = apiBehavior.getMethodInfo().getAttributes();
		boolean hasAnnotation = hasAnnotation(attributes);
		if (!hasAnnotation) {
			ClassApiSignature declaringClass = apiBehavior.getDeclaringClass();
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
