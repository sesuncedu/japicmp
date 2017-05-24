package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiExtractor;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.exception.JApiCmpException;
import japicmp.util.CtClassBuilder;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class BehaviorFilterTest {

	@Test
	public void testMethodTwoParamsIntLongSuccessful() throws CannotCompileException {
		fail();
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#method(int,long)");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(
			ApiExtractor.newSignatureSource());
//		CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new ClassApiSignature[]{ClassApiSignature.intType, ClassApiSignature.longType}).addToClass(classApiSignature);
//		assertThat(filter.matches(ctBehavior), is(true));
	}

	@Test
	public void testMethodOneParamsStringSuccessful() throws CannotCompileException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#method(japicmp.Param)");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
		ClassApiSignature paramCtClass = CtClassBuilder.create().name("japicmp.Param").addToClassPool(classApiSignatureSource);
//		CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new ClassApiSignature[]{paramCtClass}).addToClass(classApiSignature);
//		assertThat(filter.matches(ctBehavior), is(true));
		fail();
	}

	@Test
	public void testMethodNoParamsSuccessful() throws CannotCompileException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#method()");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
		fail();
	}

	@Test(expected = JApiCmpException.class)
	public void testMethodMissingParenthesis() throws CannotCompileException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#method(");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
//		CtBehavior ctBehavior = CtMethodBuilder.create().name("method").parameters(new ClassApiSignature[]{}).addToClass(classApiSignature);
//		assertThat(filter.matches(ctBehavior), is(true));
		fail();
	}

	@Test
	public void testConstructorNoParamsSuccessful() throws CannotCompileException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#Test()");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
//		CtConstructor ctConstructor = CtConstructorBuilder.create().addToClass(classApiSignature);
//		assertThat(filter.matches(ctConstructor), is(true));
		fail();
	}

	@Test
	public void testConstructorOneParamLongSuccessful() throws CannotCompileException, NotFoundException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#Test(java.lang.Long)");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
//		CtConstructor ctConstructor = CtConstructorBuilder.create().parameter(classApiSignatureSource.get("java.lang.Long")).addToClass(classApiSignature);
//		assertThat(filter.matches(ctConstructor), is(true));
		fail();
	}

	@Test
	public void testConstructorOneParamLongUnsuccessful() throws CannotCompileException, NotFoundException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#Test(java.lang.Long)");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
//		CtConstructor ctConstructor = CtConstructorBuilder.create().parameter(classApiSignatureSource.get("java.lang.Double")).addToClass(classApiSignature);
//		assertThat(filter.matches(ctConstructor), is(false));
		fail();
	}

	@Test
	public void testConstructorNoParamLongUnsuccessful() throws CannotCompileException, NotFoundException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test#Test()");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
		fail();
	}

	@Test
	public void testCoberturaMethodWithWildcards() throws CannotCompileException, NotFoundException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.*#__cobertura*()");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(classApiSignatureSource);
//		CtMethod ctMethod = CtMethodBuilder.create().name("__cobertura_classmap").addToClass(classApiSignature);
//		assertThat(filter.matches(ctMethod), is(true));
		fail();
	}

	@Test
	public void testMethodOfInnerClass() throws CannotCompileException, NotFoundException {
		JavadocLikeBehaviorFilter filter = new JavadocLikeBehaviorFilter("japicmp.Test$InnerClass#method()");
		ClassApiSignatureSource classApiSignatureSource = ApiExtractor.newSignatureSource();
		classApiSignatureSource.appendSystemPath();
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test$InnerClass").addToClassPool(classApiSignatureSource);
//		CtMethod ctMethod = CtMethodBuilder.create().name("method").addToClass(classApiSignature);
//		assertThat(filter.matches(ctMethod), is(true));
		fail();
	}
}
