package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.cmp.ClassesComparator;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
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
		ClassesComparator classesComparator = new ClassesComparator(jarArchiveComparator, options);
		List<ClassApiSignature> oldClasses = classesGenerator.createOldClasses(classApiSignatureSource);
		List<ClassApiSignature> newClasses = classesGenerator.createNewClasses(classApiSignatureSource);
		classesComparator.compare(oldClasses, newClasses);
		return classesComparator.getClasses();
	}
}
