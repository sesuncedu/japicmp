package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.model.JApiClass;
import japicmp.util.CtClassBuilder;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AnnotationsTest {

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
	public @interface Include {

	}

	@Test
	public void testNoAnnotationsClass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setNoAnnotations(true);
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(2));
		JApiClass jApiClass = getJApiClass(jApiClasses, "big.bang.theory.Sheldon");
		assertThat(jApiClass.getAnnotations().size(), is(0));
	}

	@Test
	public void testNoAnnotationsMethod() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.setNoAnnotations(true);
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("excel").withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("excel").withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		JApiClass jApiClass = getJApiClass(jApiClasses, "big.bang.theory.Sheldon");
//		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "excel");
//		assertThat(jApiMethod.getAnnotations().size(), is(0));
		fail();
	}

	@Test
	public void testNoAnnotationsField() throws Exception {
		fail();
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.setNoAnnotations(true);
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		JApiClass jApiClass = getJApiClass(jApiClasses, "big.bang.theory.Sheldon");
//		JApiField jApiField = getJApiField(jApiClass.getFields(), "age");
//		assertThat(jApiField.getAnnotations().size(), is(0));
	}
}
