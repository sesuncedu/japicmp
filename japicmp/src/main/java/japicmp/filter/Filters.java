package japicmp.filter;

import com.criticollab.japicmp.classinfo.api.ApiBehavior;
import com.criticollab.japicmp.classinfo.api.ApiConstructor;
import com.criticollab.japicmp.classinfo.api.ApiField;
import com.criticollab.japicmp.classinfo.api.ApiMethod;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Filters {
	private static final Logger LOGGER = Logger.getLogger(Filters.class.getName());
	private final List<Filter> includes = new ArrayList<>();
	private final List<Filter> excludes = new ArrayList<>();

	public List<Filter> getIncludes() {
		return includes;
	}

	public List<Filter> getExcludes() {
		return excludes;
	}

	public boolean includeClass(ClassApiSignature classApiSignature) {
		String name = classApiSignature.getName();
		for (Filter filter : excludes) {
			if (filter instanceof ClassFilter) {
				ClassFilter classFilter = (ClassFilter) filter;
				if (classFilter.matches(classApiSignature)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Excluding class '" + name + "' because class filter '" + filter + "' matches.");
					}
					return false;
				}
			}
		}
		int includeCount = 0;
		for (Filter filter : includes) {
			includeCount++;
			if (filter instanceof BehaviorFilter) {
				BehaviorFilter behaviorFilter = (BehaviorFilter) filter;
				ApiMethod[] methods = classApiSignature.getDeclaredMethods();
				for (ApiMethod method : methods) {
					if (behaviorFilter.matches(method)) {
						return true;
					}
				}
				ApiConstructor[] constructors = classApiSignature.getDeclaredConstructors();
				for (ApiConstructor constructor : constructors) {
					if (behaviorFilter.matches(constructor)) {
						return true;
					}
				}
			} else if (filter instanceof FieldFilter) {
				FieldFilter fieldFilter = (FieldFilter) filter;
				ApiField[] fields = classApiSignature.getDeclaredFields();
				for (ApiField field : fields) {
					if (fieldFilter.matches(field)) {
						return true;
					}
				}
			} else {
				ClassFilter classFilter = (ClassFilter) filter;
				if (classFilter.matches(classApiSignature)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Including class '" + name + "' because class filter '" + filter + "' matches.");
					}

					return true;
				}
			}
		}
		if (includeCount > 0) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Excluding class '" + name + "' because no include matched.");
			}
			return false;
		}
		return true;
	}

	public boolean includeBehavior(ApiBehavior apiMethod) {
		for (Filter filter : excludes) {
			if (filter instanceof BehaviorFilter) {
				BehaviorFilter behaviorFilter = (BehaviorFilter) filter;
				if (behaviorFilter.matches(apiMethod)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Excluding method '" + apiMethod.getLongName() + "' because exclude method filter did match.");
					}
					return false;
				}
			}
		}
		int includesCount = 0;
		for (Filter filter : includes) {
			if (filter instanceof BehaviorFilter) {
				includesCount++;
				BehaviorFilter behaviorFilter = (BehaviorFilter) filter;
				if (behaviorFilter.matches(apiMethod)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Including method '" + apiMethod.getLongName() + "' because include method filter matched.");
					}
					return true;
				}
			}
		}
		if (includesCount > 0) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Excluding method '" + apiMethod.getLongName() + "' because no include matched.");
			}
			return false;
		}
		return true;
	}

	public boolean includeField(ApiField apiField) {
		for (Filter filter : excludes) {
			if (filter instanceof FieldFilter) {
				FieldFilter fieldFilter = (FieldFilter) filter;
				if (fieldFilter.matches(apiField)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Excluding field '" + apiField.getName() + "' because exclude field filter did match.");
					}
					return false;
				}
			}
		}
		int includesCount = 0;
		for (Filter filter : includes) {
			if (filter instanceof FieldFilter) {
				FieldFilter fieldFilter = (FieldFilter) filter;
				includesCount++;
				if (fieldFilter.matches(apiField)) {
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.log(Level.FINE, "Including field '" + apiField.getName() + "' because include field filter matched.");
					}
					return true;
				}
			}
		}
		if (includesCount > 0) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Excluding field '" + apiField.getName() + "' because no include matched.");
			}
			return false;
		}
		return true;
	}
}
