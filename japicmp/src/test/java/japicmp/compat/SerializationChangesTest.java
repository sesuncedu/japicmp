package japicmp.compat;

import japicmp.cmp.ClassesHelper;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.*;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtFieldBuilder;
import japicmp.util.CtMethodBuilder;


import org.junit.Test;

import java.io.Serializable;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SerializationChangesTest {

	@Test
	public void testEnumUnchanged() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.ENUM));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(false));
	}

	@Test
	public void testEnumElementAdded() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION2").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.ENUM));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(false));
	}

	@Test
	public void testEnumElementRemoved() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION2").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().enumModifier().name("japicmp.Test").withSuperclass(classApiSignatureSource.get(Enum.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(classApiSignature).staticAccess().finalAccess().name("OPTION1").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.ENUM));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible(), is(JApiJavaObjectSerializationCompatibility.JApiJavaObjectSerializationChangeStatus.SERIALIZABLE_INCOMPATIBLE_FIELD_REMOVED));
	}

	@Test
	 public void testClassCompatible() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.CLASS));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(false));
		assertThat(jApiClass.getJavaObjectSerializationCompatible(), is(JApiJavaObjectSerializationCompatibility.JApiJavaObjectSerializationChangeStatus.SERIALIZABLE_COMPATIBLE));
	}

	@Test
	public void testClassCompatibleWithSerialVersionUid() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtFieldBuilder.create().type(ClassApiSignature.longType).staticAccess().finalAccess().name("serialVersionUID").withConstantValue(1L).addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtFieldBuilder.create().type(ClassApiSignature.longType).staticAccess().finalAccess().name("serialVersionUID").withConstantValue(1L).addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.CLASS));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(false));
		assertThat(jApiClass.getJavaObjectSerializationCompatible(), is(JApiJavaObjectSerializationCompatibility.JApiJavaObjectSerializationChangeStatus.SERIALIZABLE_COMPATIBLE));
	}

	@Test
	public void testClassSerialVersionUidChanged() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtFieldBuilder.create().type(ClassApiSignature.longType).staticAccess().finalAccess().name("serialVersionUID").withConstantValue(1L).addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtFieldBuilder.create().type(ClassApiSignature.longType).staticAccess().finalAccess().name("serialVersionUID").withConstantValue(2L).addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.CLASS));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible(), is(JApiJavaObjectSerializationCompatibility.JApiJavaObjectSerializationChangeStatus.SERIALIZABLE_INCOMPATIBLE_SERIALVERSIONUID_MODIFIED));
	}

	@Test
	public void testClassSerialVersionUidRemovedAndNotMatchesNewDefault() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtFieldBuilder.create().type(ClassApiSignature.longType).staticAccess().finalAccess().name("serialVersionUID").withConstantValue(1L).addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").implementsInterface(classApiSignatureSource.get(Serializable.class.getName())).addToClassPool(classApiSignatureSource);
				CtFieldBuilder.create().type(ClassApiSignature.intType).staticAccess().finalAccess().name("CONST").addToClass(classApiSignature);
				CtMethodBuilder.create().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
				return Collections.singletonList(classApiSignature);
			}
		});
		JApiClass jApiClass = getJApiClass(jApiClasses, "japicmp.Test");
		assertThat(jApiClass.getClassType().getChangeStatus(), is(JApiChangeStatus.UNCHANGED));
		assertThat(jApiClass.getClassType().getNewTypeOptional().get(), is(JApiClassType.ClassType.CLASS));
		assertThat(jApiClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(jApiClass.getSerialVersionUid().isSerializableOld(), is(true));
		assertThat(jApiClass.getSerialVersionUid().isSerializableNew(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible().isIncompatible(), is(true));
		assertThat(jApiClass.getJavaObjectSerializationCompatible(), is(JApiJavaObjectSerializationCompatibility.JApiJavaObjectSerializationChangeStatus.SERIALIZABLE_INCOMPATIBLE_SERIALVERSIONUID_REMOVED_AND_NOT_MACHTES_NEW_DEFAULT));
	}
}
