package japicmp.cmp;

import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiMethod;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtMethodBuilder;


import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static japicmp.util.Helper.getJApiMethod;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExceptionsTest {

	@Test
	public void testMethodThrowsUnchangedExceptions() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
	}

	@Test
	public void testMethodThrowsNewExceptions() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), is(JApiChangeStatus.NEW));
	}

	@Test
	public void testMethodThrowsRemovedExceptions() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), is(JApiChangeStatus.REMOVED));
	}

	@Test
	public void testMethodRemovedThrowsExceptions() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), is(JApiChangeStatus.REMOVED));
	}

	@Test
	public void testMethodAddedThrowsExceptions() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.Exception")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), is(JApiChangeStatus.NEW));
	}
}
