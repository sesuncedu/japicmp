package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.model.AccessModifier;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtInterfaceBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SuperclassTest {

	@Test
	public void testClassHasNoSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = classApiSignatureSource.get("java.lang.Object");
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = classApiSignatureSource.get("java.lang.Object");
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "java.lang.Object");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
	}

	@Test
	public void testNewClass() throws Exception {
		fail();
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.EMPTY_LIST;
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuper = CtClassBuilder.create().name("japicmp.Super").addToClassPool(classApiSignatureSource);
//				classApiSignature.setSuperclass(ctClassSuper);
				return Arrays.asList(classApiSignature, ctClassSuper);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiClass.getSuperclass().getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiClass.getSuperclass().getOldSuperclass().isPresent(), is(false));
		assertThat(jApiClass.getSuperclass().getNewSuperclass().isPresent(), is(true));
		assertThat(jApiClass.getSuperclass().getNewSuperclass().get().getName(), is("japicmp.Super"));
	}

	@Test
	public void testRemovedClass() throws Exception {
		fail();
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuper = CtClassBuilder.create().name("japicmp.Super").addToClassPool(classApiSignatureSource);
//				classApiSignature.setSuperclass(ctClassSuper);
				return Arrays.asList(classApiSignature, ctClassSuper);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.EMPTY_LIST;
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(jApiClass.getSuperclass().getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(jApiClass.getSuperclass().getOldSuperclass().isPresent(), is(true));
		assertThat(jApiClass.getSuperclass().getNewSuperclass().isPresent(), is(false));
		assertThat(jApiClass.getSuperclass().getOldSuperclass().get().getName(), is("japicmp.Super"));
	}

	@Test
	public void testClassHierarchyHasOneMoreLevel() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtInterfaceBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtClassBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassIntermediate = CtClassBuilder.create().name("Intermediate").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassIntermediate).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, ctClassIntermediate, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getCompatibilityChanges(), not(hasItem(JApiCompatibilityChange.SUPERCLASS_REMOVED)));
		assertThat(jApiClass.getSuperclass().getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		jApiClass = getJApiClass(jApiClasses, "Intermediate");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.NEW));
	}

	@Test
	public void testClassHierarchyHasOneMoreLevelWithExistingClasses() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtInterfaceBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassIntermediate = CtClassBuilder.create().name("Intermediate").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, ctClassIntermediate, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtClassBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassIntermediate = CtClassBuilder.create().name("Intermediate").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassIntermediate).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, ctClassIntermediate, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getSuperclass().getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		jApiClass = getJApiClass(jApiClasses, "Intermediate");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
	}

	@Test
	public void testClassHierarchyHasOneLessLevel() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtClassBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassIntermediate = CtClassBuilder.create().name("Intermediate").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassIntermediate).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, ctClassIntermediate, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassBase = CtInterfaceBuilder.create().name("Base").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassBase).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassBase, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		assertThat(jApiClass.getSuperclass().getCompatibilityChanges(), hasItem(JApiCompatibilityChange.SUPERCLASS_REMOVED));
		assertThat(jApiClass.getSuperclass().getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		jApiClass = getJApiClass(jApiClasses, "Intermediate");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.REMOVED));
	}
}
