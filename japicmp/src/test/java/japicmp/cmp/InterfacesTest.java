package japicmp.cmp;

import japicmp.compat.CompatibilityChanges;
import japicmp.model.*;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtInterfaceBuilder;
import japicmp.util.CtMethodBuilder;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static japicmp.util.Helper.getJApiImplementedInterface;
import static japicmp.util.Helper.getJApiMethod;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class InterfacesTest {

	@Test
	public void testMethodAddedToInterfaceDefinedInSuperInterface() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, subInterface);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(subInterface);
				return Arrays.asList(superInterface, subInterface);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "SubInterface");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(true));
	}

	@Test
	public void testMethodAddedToInterfaceAndInSuperInterface() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, subInterface);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(subInterface);
				return Arrays.asList(superInterface, subInterface);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "SubInterface");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(false));
	}

	@Test
	public void testInterfaceMarkerAdded() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(superInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testInterfaceAdded() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(superInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.INTERFACE_ADDED));
	}

	@Test
	public void testMethodPulledUp() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(subInterface);
				return Arrays.asList(superInterface, subInterface);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superInterface = CtInterfaceBuilder.create().name("SuperInterface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(superInterface);
				ClassApiSignature subInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(superInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superInterface, subInterface);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "SuperInterface");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		jApiClass = getJApiClass(jApiClasses, "SubInterface");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testClassImplementsInterface() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(ctClassInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(ctClassInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().name("method").addToClass(classApiSignature);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testClassNoLongerImplementsInterface() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(ctClassInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().name("method").addToClass(classApiSignature);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).publicAccess().abstractMethod().name("method").addToClass(ctClassInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getCompatibilityChanges(), hasItem(JApiCompatibilityChange.INTERFACE_REMOVED));
	}

	@Test
	public void testInterfaceHierarchyHasOneMoreLevel() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSubInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassSubInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSubInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getInterfaces().size(), is(2));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "SubInterface").getChangeStatus(), is(JApiChangeStatus.NEW));
	}

	@Test
	public void testInterfaceHierarchyHasOneLessLevel() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSubInterface = CtInterfaceBuilder.create().name("SubInterface").withSuperInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassSubInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSubInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		assertThat(jApiClass.getInterfaces().size(), is(2));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "SubInterface").getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "SubInterface").getCompatibilityChanges(), hasItem(JApiCompatibilityChange.INTERFACE_REMOVED));
	}

	@Test
	public void testInterfaceMovedToSuperclass() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuperClass = CtClassBuilder.create().name("SuperClass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).withSuperclass(ctClassSuperClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSuperClass, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuperClass = CtClassBuilder.create().name("SuperClass").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassSuperClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSuperClass, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getInterfaces().size(), is(1));
		JApiSuperclass jApiSuperclass = jApiClass.getSuperclass();
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiSuperclass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
	}

	@Test
	public void testInterfaceMovedToSubclass() throws Exception {
		JarArchiveComparatorOptions jarArchiveComparatorOptions = new JarArchiveComparatorOptions();
		jarArchiveComparatorOptions.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(jarArchiveComparatorOptions, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuperClass = CtClassBuilder.create().name("SuperClass").implementsInterface(ctClassInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassSuperClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSuperClass, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClassInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClassSuperClass = CtClassBuilder.create().name("SuperClass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctClassInterface).withSuperclass(ctClassSuperClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClassInterface, ctClassSuperClass, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getInterfaces().size(), is(1));
		JApiSuperclass jApiSuperclass = jApiClass.getSuperclass();
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.NEW));
		// not has INTERFACE_ADDED because it is only moved not added/removed
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getCompatibilityChanges(), not(hasItem(JApiCompatibilityChange.INTERFACE_ADDED)));
		assertThat(jApiSuperclass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		jApiClass = getJApiClass(jApiClasses, "SuperClass");
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		assertThat(jApiClass.getInterfaces().size(), is(1));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(getJApiImplementedInterface(jApiClass.getInterfaces(), "Interface").getCompatibilityChanges(), hasItem(JApiCompatibilityChange.INTERFACE_REMOVED));
	}
}
