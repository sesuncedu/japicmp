package japicmp.cmp;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import japicmp.filter.JavaDocLikeClassFilter;
import japicmp.filter.JavadocLikePackageFilter;
import japicmp.model.JApiClass;
import japicmp.util.CtClassBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static japicmp.util.Helper.getJApiClass;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FilterTest {

	@Test
	public void testOneClassNoExclude() throws Exception {
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
				return Collections.singletonList(classApiSignature);
			}
		});
		assertThat(jApiClasses.size(), is(1));
	}

	@Test
	public void testOneClassExcluded() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Test"));
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
		assertThat(jApiClasses.size(), is(0));
	}

	@Test
	public void testTwoClassesOneExclude() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Homer"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(1));
	}

	@Test
	public void testTwoClassesTwoExcludeWithWildcard() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.*"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(0));
	}

	@Test
	public void testTwoClassesTwoExcludeWithWildcardOneLetter() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.T*"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(0));
	}

	@Test
	public void testTwoClassesIncludePackageButExcludeClass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Test1"));
		options.getFilters().getIncludes().add(new JavadocLikePackageFilter("japicmp"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(1));
	}

	@Test
	public void testTwoClassesExcludePackageAndClass() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Test1"));
		options.getFilters().getExcludes().add(new JavadocLikePackageFilter("japicmp"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Test1").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Test2").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(0));
	}

	@Test
	public void testTwoClassesExcludeClassThatDoesNotExist() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Test1"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2);
			}
		});
		assertThat(jApiClasses.size(), is(2));
	}

	@Test
	public void testFourClassesFromTwoPackagesExcludeOnePerPackage() throws Exception {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("japicmp.Marge"));
		options.getFilters().getExcludes().add(new JavaDocLikeClassFilter("big.bang.theory.Sheldon"));
		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
			@Override
			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass3 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass4 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
			}

			@Override
			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
				ClassApiSignature ctClass1 = CtClassBuilder.create().name("japicmp.Homer").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass2 = CtClassBuilder.create().name("japicmp.Marge").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass3 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
				ClassApiSignature ctClass4 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
			}
		});
		assertThat(jApiClasses.size(), is(2));
		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Leonard"), is(notNullValue()));
		assertThat(getJApiClass(jApiClasses, "japicmp.Homer"), is(notNullValue()));
	}

	@Test
	public void testMethodExcluded() throws Exception {
		fail();
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getExcludes().add(new JavadocLikeBehaviorFilter("big.bang.theory.Sheldon#study()"));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiMethodWithName("study"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("knowItAll"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Leonard"), hasJApiMethodWithName("askSheldon"));
	}

//	@Test
//	public void testMethodIncluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new JavadocLikeBehaviorFilter("big.bang.theory.Sheldon#study()"));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("study"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiMethodWithName("knowItAll"));
//	}
//
//	@Test
//	public void testPackageExcludedMethodIncluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getExcludes().add(new JavadocLikePackageFilter("simpsons"));
//		options.getFilters().getIncludes().add(new JavadocLikeBehaviorFilter("big.bang.theory.Sheldon#study()"));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				ClassApiSignature ctClass3 = CtClassBuilder.create().name("simpsons.Homer").addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass4 = CtClassBuilder.create().name("simpsons.Marge").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				ClassApiSignature ctClass3 = CtClassBuilder.create().name("simpsons.Homer").addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass4 = CtClassBuilder.create().name("simpsons.Marge").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("study"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiMethodWithName("knowItAll"));
//	}
//
//	@Test
//	public void testPackageIncludedMethodExcluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new JavadocLikePackageFilter("big.bang.theory"));
//		options.getFilters().getExcludes().add(new JavadocLikeBehaviorFilter("big.bang.theory.Sheldon#study()"));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				ClassApiSignature ctClass3 = CtClassBuilder.create().name("simpsons.Homer").addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass4 = CtClassBuilder.create().name("simpsons.Marge").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				ClassApiSignature ctClass3 = CtClassBuilder.create().name("simpsons.Homer").addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass4 = CtClassBuilder.create().name("simpsons.Marge").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2, ctClass3, ctClass4);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiMethodWithName("study"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("knowItAll"));
//	}
//
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
//	public @interface Exclude {
//
//	}
//
//	@Test
//	public void testAnnotationClassExcluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getExcludes().add(new AnnotationClassFilter("@" + Exclude.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Exclude.class.getName()).addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Exclude.class.getName()).addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(jApiClasses.get(0).getFullyQualifiedName(), is("big.bang.theory.Leonard"));
//	}
//
//	@Test
//	public void testAnnotationMethodExcluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getExcludes().add(new AnnotationBehaviorFilter("@" + Exclude.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").withAnnotation(Exclude.class.getName()).addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").withAnnotation(Exclude.class.getName()).addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiMethodWithName("study"));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("knowItAll"));
//	}
//
//	@Test
//	public void testAnnotationFieldExcluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getExcludes().add(new AnnotationFieldFilter("@" + Exclude.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Exclude.class.getName()).addToClass(ctClass1);
//				CtFieldBuilder.create().name("name").type(classApiSignatureSource.getCtClass(String.class.getName())).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Exclude.class.getName()).addToClass(ctClass1);
//				CtFieldBuilder.create().name("name").type(classApiSignatureSource.getCtClass(String.class.getName())).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(2));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasNoJApiFieldWithName("age"));
//	}
//
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
//	public @interface Include {
//
//	}
//
//	@Test
//	public void testAnnotationClassIncluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new AnnotationClassFilter("@" + Include.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(jApiClasses.get(0).getFullyQualifiedName(), is("big.bang.theory.Sheldon"));
//	}
//
//	@Test
//	public void testAnnotationMethodIncluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new AnnotationBehaviorFilter("@" + Include.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("study").withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("knowItAll").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(jApiClasses.get(0).getMethods().size(), is(1));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiMethodWithName("study"));
//	}
//
//	@Test
//	public void testAnnotationFieldIncluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new AnnotationFieldFilter("@" + Include.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				CtFieldBuilder.create().name("name").type(classApiSignatureSource.getCtClass(String.class.getName())).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				CtFieldBuilder.create().name("name").type(classApiSignatureSource.getCtClass(String.class.getName())).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(ClassApiSignature.voidType).name("askSheldon").addToClass(ctClass2);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		assertThat(getJApiClass(jApiClasses, "big.bang.theory.Sheldon"), hasJApiFieldWithName("age"));
//	}
//
//	@Test
//	public void testAnnotationClassIncludedChangesDetected() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new AnnotationClassFilter("@" + Include.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				CtMethodBuilder.create().publicAccess().returnType(classApiSignatureSource.getCtClass(String.class.getName())).name("getName").addToClass(ctClass1);
//				CtFieldBuilder.create().type(classApiSignatureSource.getCtClass(Integer.class.getName())).name("age").addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		JApiClass jApiClass = getJApiClass(jApiClasses, "big.bang.theory.Sheldon");
//		JApiMethod jApiMethod = getJApiMethod(jApiClass.getMethods(), "getName");
//		assertThat(jApiMethod.getChangeStatus(), is(JApiChangeStatus.NEW));
//		JApiField jApiField = getJApiField(jApiClass.getFields(), "age");
//		assertThat(jApiField.getChangeStatus(), is(JApiChangeStatus.NEW));
//	}
//
//	@Test
//	public void testClassIncludedButMethodExcluded() throws Exception {
//		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
//		options.getFilters().getIncludes().add(new AnnotationClassFilter("@" + Include.class.getName()));
//		options.getFilters().getExcludes().add(new AnnotationBehaviorFilter("@" + Exclude.class.getName()));
//		List<JApiClass> jApiClasses = ClassesHelper.compareClasses(options, new ClassesHelper.ClassesGenerator() {
//			@Override
//			public List<ClassApiSignature> createOldClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//
//			@Override
//			public List<ClassApiSignature> createNewClasses(ClassApiSignatureSource classApiSignatureSource) throws Exception {
//				ClassApiSignature ctClass1 = CtClassBuilder.create().name("big.bang.theory.Sheldon").withAnnotation(Include.class.getName()).addToClassPool(classApiSignatureSource);
//				CtFieldBuilder.create().name("age").type(classApiSignatureSource.getCtClass(String.class.getName())).withAnnotation(Include.class.getName()).addToClass(ctClass1);
//				CtMethodBuilder.create().publicAccess().returnType(classApiSignatureSource.getCtClass(String.class.getName())).name("getName").withAnnotation(Exclude.class.getName()).addToClass(ctClass1);
//				ClassApiSignature ctClass2 = CtClassBuilder.create().name("big.bang.theory.Leonard").addToClassPool(classApiSignatureSource);
//				return Arrays.asList(ctClass1, ctClass2);
//			}
//		});
//		assertThat(jApiClasses.size(), is(1));
//		JApiClass jApiClass = getJApiClass(jApiClasses, "big.bang.theory.Sheldon");
//		getJApiField(jApiClass.getFields(), "age");
//		assertThat(jApiClass.getMethods().size(), is(0));
//	}
}
