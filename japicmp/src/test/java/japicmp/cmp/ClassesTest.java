package japicmp.cmp;

import japicmp.model.AccessModifier;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.JApiMethod;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtInterfaceBuilder;
import japicmp.util.CtMethodBuilder;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static japicmp.util.Helper.getJApiMethod;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClassesTest {

	@Test
	public void testAbstractMethodAdded() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, subClass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(subClass);
				return Arrays.asList(superClass, subClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Subclass");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(false));
	}

	@Test
	public void testAbstractMethodAddedThatOverridesExistingAbstractMethod() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(superClass);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, subClass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(superClass);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(subClass);
				return Arrays.asList(superClass, subClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Subclass");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(true));
	}

	@Test
	public void testAbstractMethodAddedThatOverridesNewAbstractMethod() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, subClass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(superClass);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(subClass);
				return Arrays.asList(superClass, subClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Subclass");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(false));
	}

	@Test
	public void testAbstractMethodAddedViaNewSuperclass() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(superClass);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, subClass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(superClass);
				ClassApiSignature subClass = CtClassBuilder.create().name("japicmp.Subclass").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, subClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Subclass");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(false));
	}
}
