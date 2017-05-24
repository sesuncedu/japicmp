package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.util.CtClassBuilder;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ClassesComparatorTest {
	@Test
	public void testMethodAdded() throws Exception {
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(new JarArchiveComparatorOptions(), new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.singletonList(createClassWithoutMethod(classApiSignatureSource));
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				return Collections.singletonList(createClassWithMethod(classApiSignatureSource));
			}
		});
		assertThat(jApiClasses.size(), is(1));
		assertThat(jApiClasses.get(0).getMethods().size(), is(1));
		assertThat(jApiClasses.get(0).getMethods().get(0).getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(jApiClasses.get(0).getMethods().get(0).isBinaryCompatible(), is(true));
		assertThat(jApiClasses.get(0).isBinaryCompatible(), is(true));
		assertThat(jApiClasses.get(0).getChangeStatus(), is(JApiChangeStatus.MODIFIED));
	}

	private ClassApiSignature createClassWithoutMethod(ClassApiSignatureSource classApiSignatureSource) {
		return new CtClassBuilder().name("japicmp.Test").addToClassPool(classApiSignatureSource);
	}

	private ClassApiSignature createClassWithMethod(ClassApiSignatureSource classApiSignatureSource) throws NotFoundException, CannotCompileException {
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
//		CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.intType).name("method").body("return 42;").addToClass(classApiSignature);
//		return classApiSignature;
		throw new NoSuchMethodError("Not Implemented yet");
	}
}
