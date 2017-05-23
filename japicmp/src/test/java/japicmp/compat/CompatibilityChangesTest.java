package japicmp.compat;

import japicmp.cmp.ClassesHelper;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.*;
import japicmp.util.*;


import javassist.CtMethod;
import javassist.Modifier;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static japicmp.util.Helper.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompatibilityChangesTest {

	@Test
	public void testClassRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.emptyList();
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_REMOVED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testClassNowAbstract() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_NOW_ABSTRACT));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testClassNowFinal() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().finalModifier().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_NOW_FINAL));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testClassNoLongerPublic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().privateModifier().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_NO_LONGER_PUBLIC));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testClassTypeChanged() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtInterfaceBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_TYPE_CHANGED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testSuperclassRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtInterfaceBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSuperclass().getCompatibilityChanges(), hasItem(JApiCompatibilityChange.SUPERCLASS_REMOVED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testSuperclassChanged() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass2").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSuperclass().getCompatibilityChanges(), hasItem(JApiCompatibilityChange.SUPERCLASS_REMOVED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testSuperclassAdded() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSuperclass().getCompatibilityChanges(), hasItem(JApiCompatibilityChange.SUPERCLASS_ADDED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		JApiSuperclass superclass = jApiClass.getSuperclass();
		assertThat(superclass.isBinaryCompatible(), is(true));
		assertThat(superclass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.SUPERCLASS_ADDED));
	}

	@Test
	public void testSuperclassUnchangedObject() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getCompatibilityChanges().size(), is(0));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		JApiSuperclass superclass = jApiClass.getSuperclass();
		assertThat(superclass.isBinaryCompatible(), is(true));
		assertThat(superclass.getCompatibilityChanges().size(), is(0));
	}

	@Test
	public void testMethodRemovedInSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(superclass).name("getInstance").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_REMOVED_IN_SUPERCLASS));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodRemovedInSuperclassButOverriddenInSubclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(superclass).name("getInstance").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(superclass).name("getInstance").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(superclass).name("getInstance").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testFieldRemovedInSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_REMOVED_IN_SUPERCLASS));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldRemovedInSuperclassButOverriddenInSubclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, superclass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, superclass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testMethodRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "isRemoved");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_REMOVED));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
		JApiSuperclass superclass = jApiClass.getSuperclass();
		assertThat(superclass.isBinaryCompatible(), is(true));
		assertThat(superclass.getCompatibilityChanges().size(), is(0));
	}

	@Test
	public void testMethodLessAccessiblePublicToPrivate() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().privateAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "isRemoved");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_LESS_ACCESSIBLE));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodLessAccessibleThanInSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superclass, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().protectedAccess().returnType(ClassApiSignature.booleanType).name("isRemoved").body("return true;").addToClass(classApiSignature);
				return Arrays.asList(superclass, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "isRemoved");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_LESS_ACCESSIBLE_THAN_IN_SUPERCLASS));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodStaticOverridesNonStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isOverridden").body("return true;").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superclass, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("isOverridden").body("return true;").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().staticAccess().returnType(ClassApiSignature.booleanType).name("isOverridden").body("return true;").addToClass(classApiSignature);
				return Arrays.asList(superclass, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "isOverridden");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_IS_STATIC_AND_OVERRIDES_NOT_STATIC));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodReturnTypeChanges() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.intType).name("returnTypeChanges").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("returnTypeChanges").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "returnTypeChanges");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_RETURN_TYPE_CHANGED));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodNowAbstract() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("methodBecomesAbstract").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().returnType(ClassApiSignature.booleanType).name("methodBecomesAbstract").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "methodBecomesAbstract");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_NOW_ABSTRACT));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodNowFinal() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("methodBecomesFinal").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().finalMethod().returnType(ClassApiSignature.booleanType).name("methodBecomesFinal").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "methodBecomesFinal");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_NOW_FINAL));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodNowStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("methodBecomesStatic").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().staticAccess().returnType(ClassApiSignature.booleanType).name("methodBecomesStatic").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "methodBecomesStatic");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_NOW_STATIC));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodNoLongerStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().staticAccess().returnType(ClassApiSignature.booleanType).name("methodNoLongerStatic").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.booleanType).name("methodNoLongerStatic").body("return true;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "methodNoLongerStatic");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_NO_LONGER_STATIC));
		assertThat(jApiMethod.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldStaticOverridesStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().staticAccess().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().staticAccess().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().staticAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_STATIC_AND_OVERRIDES_STATIC));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldLessAccessibleThanInSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superclass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(superclass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superclass).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().packageProtectedAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_LESS_ACCESSIBLE_THAN_IN_SUPERCLASS));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldNowFinal() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().finalAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_NOW_FINAL));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldNowStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().staticAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_NOW_STATIC));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldNoLongerStatic() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().staticAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_NO_LONGER_STATIC));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldTypeChanged() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.floatType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_TYPE_CHANGED));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_REMOVED));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testFieldLessAccessible() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().packageProtectedAccess().type(ClassApiSignature.intType).name("field").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiField jApiField = getJApiField(jApiClass.getFields(), "field");
		assertThat(jApiField.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.FIELD_LESS_ACCESSIBLE));
		assertThat(jApiField.isBinaryCompatible(), is(false));
	}

	@Test
	public void testConstructorRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtConstructorBuilder.create().publicAccess().parameter(ClassApiSignature.intType).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiConstructor jApiConstructor = getJApiConstructor(jApiClass.getConstructors(), Collections.singletonList("int"));
		assertThat(jApiConstructor.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CONSTRUCTOR_REMOVED));
		assertThat(jApiConstructor.isBinaryCompatible(), is(false));
	}

	@Test
	public void testConstructorLessAccessible() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtConstructorBuilder.create().publicAccess().parameter(ClassApiSignature.intType).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtConstructorBuilder.create().protectedAccess().parameter(ClassApiSignature.intType).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(false));
		JApiConstructor jApiConstructor = getJApiConstructor(jApiClass.getConstructors(), Collections.singletonList("int"));
		assertThat(jApiConstructor.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CONSTRUCTOR_LESS_ACCESSIBLE));
		assertThat(jApiConstructor.isBinaryCompatible(), is(false));
	}

	@Test
	public void testMethodAddedToInterface() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtInterfaceBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtInterfaceBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().name("method").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_ADDED_TO_INTERFACE));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(false));
	}

	@Test
	public void testAbstractClassNowExtendsAnotherAbstractClass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().abstractModifier().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superClass);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().abstractModifier().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).abstractMethod().name("newAbstractMethod").addToClass(superClass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(classApiSignature, superClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		jApiClass = getJApiClass(jApiClasses, "japicmp.Superclass");
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "newAbstractMethod");
		assertThat(jApiMethod.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_ABSTRACT_ADDED_TO_CLASS));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(false));
	}

	@Test
	public void testMethodAddedToNewInterface() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.emptyList();
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtInterfaceBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().publicAccess().abstractMethod().name("method").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getCompatibilityChanges().size(), is(0));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(true));
	}

	@Test
	public void testInterfaceMovedToAbstractClass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(ctInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).name("method").body("int a = 42;").addToClass(classApiSignature);
				return Arrays.asList(ctInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(ctInterface);
				ClassApiSignature ctAbstractClass = CtClassBuilder.create().name("AbstractTest").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctAbstractClass).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.voidType).name("method").body("int a = 42;").addToClass(classApiSignature);
				return Arrays.asList(ctInterface, classApiSignature, ctAbstractClass);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getInterfaces().size(), is(1));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getCompatibilityChanges().size(), is(0));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(true));
	}

	@Test
	public void testAbstractMethodMovedToInterface() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(classApiSignature);
				return Arrays.asList(ctInterface, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("Interface").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().abstractMethod().returnType(ClassApiSignature.voidType).name("method").addToClass(ctInterface);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctInterface, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
		assertThat(jApiClass.getInterfaces().size(), is(1));
		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.REMOVED));
		assertThat(jApiMethod.isBinaryCompatible(), is(true));
		assertThat(jApiMethod.isSourceCompatible(), is(true));
	}

	@Test
	public void testMethodMovedFromOneInterfaceToAnother() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface1 = CtInterfaceBuilder.create().name("Interface1").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).abstractMethod().name("method").addToClass(ctInterface1);
				ClassApiSignature ctInterface2 = CtInterfaceBuilder.create().name("Interface2").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").implementsInterface(ctInterface1).implementsInterface(ctInterface2).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, ctInterface1, ctInterface2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface1 = CtInterfaceBuilder.create().name("Interface1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctInterface2 = CtInterfaceBuilder.create().name("Interface2").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).abstractMethod().name("method").addToClass(ctInterface2);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").implementsInterface(ctInterface1).implementsInterface(ctInterface2).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, ctInterface1, ctInterface2);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testMethodMovedFromOneAbstractClassToAnother() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature abstractClass1 = CtClassBuilder.create().abstractModifier().name("AbstractClass1").addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).abstractMethod().name("method").addToClass(abstractClass1);
				ClassApiSignature abstractClass2 = CtClassBuilder.create().abstractModifier().name("AbstractClass2").withSuperclass(abstractClass1).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").withSuperclass(abstractClass2).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, abstractClass1, abstractClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature abstractClass1 = CtClassBuilder.create().abstractModifier().name("AbstractClass1").addToClassPool(classApiSignatureSource);
				ClassApiSignature abstractClass2 = CtClassBuilder.create().abstractModifier().name("AbstractClass2").withSuperclass(abstractClass1).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).abstractMethod().name("method").addToClass(abstractClass2);
				ClassApiSignature classApiSignature = CtClassBuilder.create().abstractModifier().name("japicmp.Test").withSuperclass(abstractClass2).addToClassPool(classApiSignatureSource);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Arrays.asList(classApiSignature, abstractClass1, abstractClass2);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testClassNowCheckedException() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setAccessModifier(AccessModifier.PRIVATE);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(classApiSignatureSource.get("java.lang.Exception")).addToClassPool(classApiSignatureSource);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(false));
		assertThat(jApiClass.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.CLASS_NOW_CHECKED_EXCEPTION));
	}

	@Test
	public void testMethodThrowsNewCheckedException() throws Exception {
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
		assertThat(method.getExceptions().size(), Is.is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), Is.is(JApiChangeStatus.NEW));
		assertThat(method.getExceptions().get(0).isCheckedException(), Is.is(true));
		assertThat(method.isBinaryCompatible(), is(true));
		assertThat(method.isSourceCompatible(), is(false));
		assertThat(method.getCompatibilityChanges(), hasItem(JApiCompatibilityChange.METHOD_NOW_THROWS_CHECKED_EXCEPTION));
	}

	@Test
	public void testMethodThrowsNewRuntimeException() throws Exception {
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
				CtMethodBuilder.create().publicAccess().name("method").exceptions(new ClassApiSignature[] {classApiSignatureSource.get("java.lang.RuntimeException")}).addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		JApiMethod method = getJApiMethod(jApiClass.getMethods(), "method");
		assertThat(method.getExceptions().size(), Is.is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), Is.is(JApiChangeStatus.NEW));
		assertThat(method.getExceptions().get(0).isCheckedException(), Is.is(false));
		assertThat(method.isBinaryCompatible(), is(true));
		assertThat(method.isSourceCompatible(), is(true));
		assertThat(method.getCompatibilityChanges().size(), is(0));
	}

	@Test
	public void testNewMethodThrowsCheckedException() throws Exception {
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
		assertThat(method.getExceptions().size(), Is.is(1));
		assertThat(method.getExceptions().get(0).getChangeStatus(), Is.is(JApiChangeStatus.NEW));
		assertThat(method.getExceptions().get(0).isCheckedException(), Is.is(true));
		assertThat(method.isBinaryCompatible(), is(true));
		assertThat(method.isSourceCompatible(), is(true));
	}

	@Test
	public void testMemberVariableMovedToSuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().protectedAccess().type(ClassApiSignature.intType).name("test").addToClass(classApiSignature);
				return Arrays.asList(superClass, classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().protectedAccess().type(ClassApiSignature.intType).name("test").addToClass(superClass);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}

	@Test
	public void testInterfaceImplementedBySuperclass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("japicmp.Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superClass).implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, classApiSignature, ctInterface);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctInterface = CtInterfaceBuilder.create().name("japicmp.Interface").addToClassPool(classApiSignatureSource);
				ClassApiSignature superClass = CtClassBuilder.create().name("japicmp.Superclass").implementsInterface(ctInterface).addToClassPool(classApiSignatureSource);
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").withSuperclass(superClass).addToClassPool(classApiSignatureSource);
				return Arrays.asList(superClass, classApiSignature, ctInterface);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}
}
