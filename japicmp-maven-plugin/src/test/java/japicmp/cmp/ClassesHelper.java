package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.model.JApiClass;

import java.util.List;

public class ClassesHelper {

	public interface ClassesGenerator {
		List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception;

		List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception;
	}

	public static class CompareClassesResult {
		List<JApiClass> jApiClasses;
		JarArchiveComparator jarArchiveComparator;

		public CompareClassesResult(List<JApiClass> jApiClasses, JarArchiveComparator jarArchiveComparator) {
			this.jApiClasses = jApiClasses;
			this.jarArchiveComparator = jarArchiveComparator;
		}

		public List<JApiClass> getjApiClasses() {
			return jApiClasses;
		}

		public JarArchiveComparator getJarArchiveComparator() {
			return jarArchiveComparator;
		}
	}

	public static CompareClassesResult compareClasses(JarArchiveComparatorOptions options, ClassesGenerator classesGenerator) throws Exception {
		JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(options);
		ClassApiSignatureSource classApiSignatureSource = jarArchiveComparator.getCommonClassPool();
		List<ClassApiSignature> oldClasses = classesGenerator.createOldClasses(classApiSignatureSource);
		List<ClassApiSignature> newClasses = classesGenerator.createNewClasses(classApiSignatureSource);
		List<JApiClass> jApiClasses = jarArchiveComparator.compareClassLists(options, oldClasses, newClasses);
		return new CompareClassesResult(jApiClasses, jarArchiveComparator);
	}
}
