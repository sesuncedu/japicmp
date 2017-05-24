package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.google.common.base.Optional;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiClassType;
import japicmp.util.ClassHelper;
import japicmp.util.ModifierHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassesComparator {
	private List<JApiClass> classes = new LinkedList<>();
	private JarArchiveComparator jarArchiveComparator;
	private final JarArchiveComparatorOptions options;

	public ClassesComparator(JarArchiveComparator jarArchiveComparator, JarArchiveComparatorOptions options) {
		this.jarArchiveComparator = jarArchiveComparator;
		this.options = options;
	}

	public void compare(List<ClassApiSignature> oldClassesArg, List<ClassApiSignature> newClassesArg) {
		classes = new LinkedList<>();
		Map<String, ClassApiSignature> oldClassesMap = createClassMap(oldClassesArg);
		Map<String, ClassApiSignature> newClassesMap = createClassMap(newClassesArg);
		sortIntoLists(oldClassesMap, newClassesMap);
	}

	private void sortIntoLists(Map<String, ClassApiSignature> oldClassesMap, Map<String, ClassApiSignature> newClassesMap) {
		for (ClassApiSignature oldCtClass : oldClassesMap.values()) {
			ClassApiSignature newCtClass = newClassesMap.get(oldCtClass.getName());
			if (newCtClass == null) {
				JApiClassType classType = new JApiClassType(Optional.of(ClassHelper.getType(oldCtClass)), Optional.<JApiClassType.ClassType>absent(), JApiChangeStatus.REMOVED);
				JApiClass jApiClass = new JApiClass(this.jarArchiveComparator, oldCtClass.getName(), Optional.of(oldCtClass), Optional.<ClassApiSignature>absent(), JApiChangeStatus.REMOVED, classType);
				if (includeClass(jApiClass)) {
					classes.add(jApiClass);
				}
			} else {
				JApiChangeStatus changeStatus = JApiChangeStatus.UNCHANGED;
				JApiClassType.ClassType oldType = ClassHelper.getType(oldCtClass);
				JApiClassType.ClassType newType = ClassHelper.getType(newCtClass);
				if (oldType != newType) {
					changeStatus = JApiChangeStatus.MODIFIED;
				}
				JApiClassType classType = new JApiClassType(Optional.of(oldType), Optional.of(newType), changeStatus);
				JApiClass jApiClass = new JApiClass(this.jarArchiveComparator, oldCtClass.getName(), Optional.of(oldCtClass), Optional.of(newCtClass), changeStatus, classType);
				if (includeClass(jApiClass)) {
					classes.add(jApiClass);
				}
			}
		}
		for (ClassApiSignature newCtClass : newClassesMap.values()) {
			ClassApiSignature oldCtClass = oldClassesMap.get(newCtClass.getName());
			if (oldCtClass == null) {
				JApiClassType.ClassType newType = ClassHelper.getType(newCtClass);
				JApiClassType classType = new JApiClassType(Optional.<JApiClassType.ClassType>absent(), Optional.of(newType), JApiChangeStatus.NEW);
				JApiClass jApiClass = new JApiClass(this.jarArchiveComparator, newCtClass.getName(), Optional.<ClassApiSignature>absent(), Optional.of(newCtClass), JApiChangeStatus.NEW, classType);
				if (includeClass(jApiClass)) {
					classes.add(jApiClass);
				}
			}
		}
	}

	private boolean includeClass(JApiClass jApiClass) {
		return ModifierHelper.matchesModifierLevel(jApiClass, options.getAccessModifier());
	}

	private Map<String, ClassApiSignature> createClassMap(List<ClassApiSignature> oldClassesArg) {
		Map<String, ClassApiSignature> oldClassesMap = new HashMap<String, ClassApiSignature>();
		for (ClassApiSignature classApiSignature : oldClassesArg) {
			oldClassesMap.put(classApiSignature.getName(), classApiSignature);
		}
		return oldClassesMap;
	}

	public List<JApiClass> getClasses() {
		return classes;
	}
}
