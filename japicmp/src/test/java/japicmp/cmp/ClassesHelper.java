package japicmp.cmp;

import japicmp.model.JApiClass;



import java.util.List;

public class ClassesHelper {

	public interface ClassesGenerator {
		List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception;

		List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception;
	}

	public static List<JApiClass> compareClasses(JarArchiveComparatorOptions options, ClassesGenerator classesGenerator) throws Exception {
		JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(options);
		ClassApiSignatureSource classApiSignatureSource = jarArchiveComparator.getCommonClassPool();
		List<ClassApiSignature> oldClasses = classesGenerator.createOldClasses(classApiSignatureSource);
		List<ClassApiSignature> newClasses = classesGenerator.createNewClasses(classApiSignatureSource);
		return jarArchiveComparator.compareClassLists(options, oldClasses, newClasses);
	}
}
