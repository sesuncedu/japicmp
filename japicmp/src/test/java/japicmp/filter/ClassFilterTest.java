package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiExtractor;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import japicmp.util.CtClassBuilder;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ClassFilterTest {

	@Test
	public void testOneClassMatches() {
		JavaDocLikeClassFilter classFilter = new JavaDocLikeClassFilter("japicmp.Test");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Test").addToClassPool(ApiExtractor.newSignatureSource());
		assertThat(classFilter.matches(classApiSignature), is(true));
	}

	@Test
	public void testOneClassMatchesNot() {
		JavaDocLikeClassFilter classFilter = new JavaDocLikeClassFilter("japicmp.Homer");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(ApiExtractor.newSignatureSource());
		assertThat(classFilter.matches(classApiSignature), is(false));
	}

	@Test
	public void testInnerClass() {
		JavaDocLikeClassFilter classFilter = new JavaDocLikeClassFilter("japicmp.Homer");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.Homer$InnerHomer").addToClassPool(ApiExtractor.newSignatureSource());
		assertThat(classFilter.matches(classApiSignature), is(true));
	}

	@Test
	public void testInnerClassAsFilter() {
		JavaDocLikeClassFilter classFilter = new JavaDocLikeClassFilter("japicmp.MyClass$InnerClass");
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("japicmp.MyClass$InnerClass").addToClassPool(ApiExtractor.newSignatureSource());
		assertThat(classFilter.matches(classApiSignature), is(true));
	}
}
