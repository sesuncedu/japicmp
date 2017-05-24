package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiExtractor;
import com.criticollab.japicmp.classinfo.api.ApiField;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import japicmp.exception.JApiCmpException;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtFieldBuilder;
import javassist.CannotCompileException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FieldFilterTest {

	@Test
	public void testOneFieldMatches() throws CannotCompileException {
		JavadocLikeFieldFilter fieldFilter = new JavadocLikeFieldFilter("japicmp.Test#field");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
		ApiField ApiField = CtFieldBuilder.create().name("field").addToClass(classApiSignature);
		assertThat(fieldFilter.matches(ApiField), is(true));
	}

	@Test
	public void testOneFieldMatchesNot() throws CannotCompileException {
		JavadocLikeFieldFilter fieldFilter = new JavadocLikeFieldFilter("japicmp.Test#field42");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
		ApiField ApiField = CtFieldBuilder.create().name("field").addToClass(classApiSignature);
		assertThat(fieldFilter.matches(ApiField), is(false));
	}

	@Test(expected = JApiCmpException.class)
	public void testTwoHashSigns() throws CannotCompileException {
		JavadocLikeFieldFilter fieldFilter = new JavadocLikeFieldFilter("japicmp.Test##field42");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
		ApiField ApiField = CtFieldBuilder.create().name("field").addToClass(classApiSignature);
		assertThat(fieldFilter.matches(ApiField), is(false));
	}

	public void testFieldOfInnerClass() throws CannotCompileException {
		JavadocLikeFieldFilter fieldFilter = new JavadocLikeFieldFilter("japicmp.Test$InnerClass#field");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test$InnerClass").addToClassPool(ApiExtractor.newSignatureSource());
		ApiField ApiField = CtFieldBuilder.create().name("field").addToClass(classApiSignature);
		assertThat(fieldFilter.matches(ApiField), is(false));
	}
}
