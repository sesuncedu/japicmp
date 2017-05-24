package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiExtractor;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.cmp.ClassesHelper;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;
import japicmp.util.CtClassBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PackageFilterTest {

	@Test
	public void testWithoutWildcard() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("de.test.package");
		assertThat(pf.matches(createCtClassForPackage("de.test.package")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.")), is(false));
		assertThat(pf.matches(createCtClassForPackage("de.test.packag")), is(false));
		assertThat(pf.matches(createCtClassForPackage("de.test")), is(false));
	}

	@Test
	public void testWithWildcardAfterDot() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("de.test.package.*");
		assertThat(pf.matches(createCtClassForPackage("de.test.package")), is(false));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.p2")), is(true));
	}

	@Test
	public void testWithWildcardWithoutDot() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("de.test.package*");
		assertThat(pf.matches(createCtClassForPackage("de.test.package")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.p2")), is(true));
	}

	@Test
	public void testWithWildcardAndFollowingPackagename() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("de.test.package.*.test");
		assertThat(pf.matches(createCtClassForPackage("de.test.package")), is(false));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.p.test")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.test")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.test2")), is(false));
	}

	@Test
	public void testWithOnlyWildcard() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("*");
		assertThat(pf.matches(createCtClassForPackage("de.test.package")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.p.test")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.test")), is(true));
		assertThat(pf.matches(createCtClassForPackage("de.test.package.packageOne.test2")), is(true));
	}

	@Test
	public void testMatchAgainstDefaultPackage() {
		JavadocLikePackageFilter pf = new JavadocLikePackageFilter("foo");
		assertThat(pf.matches(createCtClassForPackage("")), is(false));
	}

	private ClassApiSignature createCtClassForPackage(String packageName) {
		String className = packageName + (packageName.isEmpty() ? "" : ".") + "Test";
		return CtClassBuilder.create().name(className).addToClassPool(ApiExtractor.newSignatureSource());
	}

	@Test
	public void testExcludeInnerPackageWhenOuterPackageIsIncluded() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getIncludes().add(new JavadocLikePackageFilter("include"));
		options.getFilters().getExcludes().add(new JavadocLikePackageFilter("include.exclude"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature toInclude = CtClassBuilder.create().name("include.ToInclude").addToClassPool(classApiSignatureSource);
				ClassApiSignature toExclude = CtClassBuilder.create().name("include.exclude.ToExclude").addToClassPool(classApiSignatureSource);
				return Arrays.asList(toInclude, toExclude);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature toInclude = CtClassBuilder.create().name("include.ToInclude").addToClassPool(classApiSignatureSource);
				ClassApiSignature toExclude = CtClassBuilder.create().name("include.exclude.ToExclude").addToClassPool(classApiSignatureSource);
				return Arrays.asList(toInclude, toExclude);
			}
		});
		assertThat(jApiClasses.size(), is(1));
		JApiClass jApiClass = getJApiClass(jApiClasses, "include.ToInclude");
		assertThat(jApiClass.isBinaryCompatible(), is(true));
		assertThat(jApiClass.isSourceCompatible(), is(true));
	}
}
