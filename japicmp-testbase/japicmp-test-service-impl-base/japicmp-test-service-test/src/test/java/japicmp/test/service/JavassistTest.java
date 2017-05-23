package japicmp.test.service;



import javassist.NotFoundException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static japicmp.test.service.util.Helper.getArchive;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JavassistTest {

	@Test
	public void test() throws NotFoundException {
		ClassApiSignatureSource classPoolOld = new ClassApiSignatureSource();
		classPoolOld.appendClassPath(getArchive("japicmp-test-service-v1.jar").getFile().getAbsolutePath());
		classPoolOld.appendClassPath(getArchive("japicmp-test-service-impl-v1.jar").getFile().getAbsolutePath());
		ClassApiSignature oldClass = classPoolOld.get(SubclassAddsNewStaticField.class.getName());
		assertThat(oldClass.getSuperclass().getFields().length, is(1));
		ClassApiSignatureSource classPoolNew = new ClassApiSignatureSource();
		classPoolNew.appendClassPath(getArchive("japicmp-test-service-v2.jar").getFile().getAbsolutePath());
		classPoolNew.appendClassPath(getArchive("japicmp-test-service-impl-v2.jar").getFile().getAbsolutePath());
		ClassApiSignature newClass = classPoolNew.get(SubclassAddsNewStaticField.class.getName());
		assertThat(newClass.getSuperclass().getFields().length, is(1));
	}

	@Test
	public void testMakeClass() throws NotFoundException, IOException {
		ClassApiSignatureSource classPoolOld = new ClassApiSignatureSource();
		classPoolOld.appendClassPath(getArchive("japicmp-test-service-v1.jar").getFile().getAbsolutePath());
		classPoolOld.appendClassPath(getArchive("japicmp-test-service-impl-v1.jar").getFile().getAbsolutePath());
		classPoolOld.appendSystemPath();
		List<ClassApiSignature> ctClasses = loadClasses(getArchive("japicmp-test-service-impl-v1.jar").getFile(), classPoolOld);
		assertThat(ctClasses.get(0).getSuperclass().getFields().length, is(1));
		ClassApiSignatureSource classPoolNew = new ClassApiSignatureSource();
		classPoolNew.appendClassPath(getArchive("japicmp-test-service-v2.jar").getFile().getAbsolutePath());
		classPoolNew.appendClassPath(getArchive("japicmp-test-service-impl-v2.jar").getFile().getAbsolutePath());
		classPoolNew.appendSystemPath();
		ctClasses = loadClasses(getArchive("japicmp-test-service-impl-v2.jar").getFile(), classPoolNew);
		assertThat(ctClasses.get(0).getSuperclass().getFields().length, is(1));
	}

	private List<ClassApiSignature> loadClasses(File archive, ClassApiSignatureSource classApiSignatureSource) throws IOException, NotFoundException {
		List<ClassApiSignature> classes = new ArrayList<>();
		try (JarFile jarFile = new JarFile(archive)) {
			Enumeration<JarEntry> entryEnumeration = jarFile.entries();
			while (entryEnumeration.hasMoreElements()) {
				JarEntry jarEntry = entryEnumeration.nextElement();
				String name = jarEntry.getName();
				if (name.contains(SubclassAddsNewStaticField.class.getSimpleName())) {
					ClassApiSignature classApiSignature = classApiSignatureSource.makeClass(jarFile.getInputStream(jarEntry));
					System.out.println(classApiSignature.getSuperclass().getFields().length);
					classes.add(classApiSignature);
				}
			}
		}
		return classes;
	}
}
