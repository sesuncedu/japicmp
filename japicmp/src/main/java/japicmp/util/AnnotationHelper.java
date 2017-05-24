package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ApiAnnotation;
import com.criticollab.japicmp.classinfo.api.ApiAnnotationsAttribute;
import com.criticollab.japicmp.classinfo.api.ApiClassFile;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiAnnotation;
import japicmp.model.JApiChangeStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationHelper {

	public interface AnnotationsAttributeCallback<T> {
		ApiAnnotationsAttribute getAnnotationsAttribute(T t);
	}

	public static <T> void computeAnnotationChanges(List<JApiAnnotation> annotations, Optional<T> oldClassOptional, Optional<T> newClassOptional,
													JarArchiveComparatorOptions options, AnnotationsAttributeCallback<T> annotationsAttributeCallback) {
		if (!options.isNoAnnotations()) {
			if (oldClassOptional.isPresent() && newClassOptional.isPresent()) {
				T oldClass = oldClassOptional.get();
				T newClass = newClassOptional.get();
				ApiAnnotationsAttribute oldAnnotationsAttribute = annotationsAttributeCallback.getAnnotationsAttribute(oldClass);
				ApiAnnotationsAttribute newAnnotationsAttribute = annotationsAttributeCallback.getAnnotationsAttribute(newClass);
				Map<String, ApiAnnotation> oldAnnotationMap;
				Map<String, ApiAnnotation> newAnnotationMap;
				if (oldAnnotationsAttribute != null) {
					oldAnnotationMap = buildAnnotationMap(oldAnnotationsAttribute.getAnnotations());
				} else {
					oldAnnotationMap = new HashMap<>();
				}
				if (newAnnotationsAttribute != null) {
					newAnnotationMap = buildAnnotationMap(newAnnotationsAttribute.getAnnotations());
				} else {
					newAnnotationMap = new HashMap<>();
				}
				for (ApiAnnotation apiAnnotation : oldAnnotationMap.values()) {
					ApiAnnotation foundAnnotation = newAnnotationMap.get(apiAnnotation.getTypeName());
					if (foundAnnotation != null) {
						JApiAnnotation jApiAnnotation = new JApiAnnotation(apiAnnotation.getTypeName(), Optional.of(apiAnnotation), Optional.of(foundAnnotation), JApiChangeStatus.UNCHANGED);
						annotations.add(jApiAnnotation);
					} else {
						JApiAnnotation jApiAnnotation = new JApiAnnotation(apiAnnotation.getTypeName(), Optional.of(apiAnnotation), Optional.<ApiAnnotation>absent(), JApiChangeStatus.REMOVED);
						annotations.add(jApiAnnotation);
					}
				}
				for (ApiAnnotation apiAnnotation : newAnnotationMap.values()) {
					ApiAnnotation foundAnnotation = oldAnnotationMap.get(apiAnnotation.getTypeName());
					if (foundAnnotation == null) {
						JApiAnnotation jApiAnnotation = new JApiAnnotation(apiAnnotation.getTypeName(), Optional.<ApiAnnotation>absent(), Optional.of(apiAnnotation), JApiChangeStatus.NEW);
						annotations.add(jApiAnnotation);
					}
				}
			} else {
				if (oldClassOptional.isPresent()) {
					T oldClass = oldClassOptional.get();
					ApiAnnotationsAttribute oldAnnotationsAttribute = annotationsAttributeCallback.getAnnotationsAttribute(oldClass);
					if (oldAnnotationsAttribute != null) {
						Map<String, ApiAnnotation> oldAnnotationMap = buildAnnotationMap(oldAnnotationsAttribute.getAnnotations());
						for (ApiAnnotation apiAnnotation : oldAnnotationMap.values()) {
							JApiAnnotation jApiAnnotation = new JApiAnnotation(apiAnnotation.getTypeName(), Optional.of(apiAnnotation), Optional.<ApiAnnotation>absent(),
								JApiChangeStatus.REMOVED);
							annotations.add(jApiAnnotation);
						}
					}
				}
				if (newClassOptional.isPresent()) {
					T newClass = newClassOptional.get();
					ApiAnnotationsAttribute newAnnotationsAttribute = annotationsAttributeCallback.getAnnotationsAttribute(newClass);
					if (newAnnotationsAttribute != null) {
						Map<String, ApiAnnotation> newAnnotationMap = buildAnnotationMap(newAnnotationsAttribute.getAnnotations());
						for (ApiAnnotation apiAnnotation : newAnnotationMap.values()) {
							JApiAnnotation jApiAnnotation = new JApiAnnotation(apiAnnotation.getTypeName(), Optional.<ApiAnnotation>absent(), Optional.of(apiAnnotation), JApiChangeStatus.NEW);
							annotations.add(jApiAnnotation);
						}
					}
				}
			}
		}
	}

	private static Map<String, ApiAnnotation> buildAnnotationMap(ApiAnnotation[] annotations) {
		Map<String, ApiAnnotation> map = new HashMap<>();
		for (ApiAnnotation apiAnnotation : annotations) {
			map.put(apiAnnotation.getTypeName(), apiAnnotation);
		}
		return map;
	}

	public static boolean hasAnnotation(ApiClassFile classFile, String annotationClassName) {
		List attributes = classFile.getAttributes();
		if (hasAnnotation(attributes, annotationClassName)) {
			return true;
		}
		return false;
	}

	public static boolean hasAnnotation(List attributes, String annotationClassName) {
		for (Object obj : attributes) {
			if (obj instanceof ApiAnnotationsAttribute) {
				ApiAnnotationsAttribute annotationsAttribute = (ApiAnnotationsAttribute) obj;
				ApiAnnotation[] annotations = annotationsAttribute.getAnnotations();
				for (ApiAnnotation apiAnnotation : annotations) {
					if (apiAnnotation.getTypeName().equals(annotationClassName)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
