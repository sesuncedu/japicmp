package japicmp;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import japicmp.util.CtClassBuilder;
import japicmp.util.CtConstructorBuilder;
import javassist.CannotCompileException;


import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.LogMode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static japicmp.cli.JApiCli.IGNORE_MISSING_CLASSES_BY_REGEX;
import static japicmp.util.JarUtil.createJarFile;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JApiCmpTest {
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	@Rule
	public final ErrLogRule errLog = new ErrLogRule(LogMode.LOG_ONLY);
	@Rule
	public final OutLogRule outLog = new OutLogRule(LogMode.LOG_ONLY);

	@Test
	public void testWithoutArguments() {
		exit.expectSystemExitWithStatus(1);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), containsString("E: Required option".trim()));
				assertThatUseHelpOptionIsPrinted();
			}
		});
		JApiCmp.main(new String[]{});
	}

	private void assertThatUseHelpOptionIsPrinted() {
		assertThat(outLog.getLog(), containsString(JApiCmp.USE_HELP_OR_H_FOR_MORE_INFORMATION));
	}

	private void assertThatHelpIsPrinted() {
		assertThat(outLog.getLog(), containsString("NAME"));
		assertThat(outLog.getLog(), containsString("SYNOPSIS"));
		assertThat(outLog.getLog(), containsString("OPTIONS"));
	}

	@Test
	public void testHelp() {
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), not(containsString("E: ".trim())));
				assertThatHelpIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"-h"});
	}

	@Test
	public void testHelpLongOption() {
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), not(containsString("E: ".trim())));
				assertThatHelpIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"--help"});
	}

	@Test
	public void testWithNewArchiveOptionButWithoutArgument() {
		exit.expectSystemExitWithStatus(128);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), containsString("E: Required values for option 'pathToNewVersionJar' not provided".trim()));
				assertThatUseHelpOptionIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"-n"});
	}

	@Test
	public void testWithOldArchiveOptionButWithoutArgument() {
		exit.expectSystemExitWithStatus(128);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), containsString("E: Required values for option 'pathToOldVersionJar' not provided".trim()));
				assertThatUseHelpOptionIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"-o"});
	}


	@Test
	public void testWithNewArchiveOptionButWithInvalidArgument() {
		exit.expectSystemExitWithStatus(1);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				String errLogTrimmed = errLog.getLog().trim();
				assertThat(errLogTrimmed, containsString("E: File".trim()));
				assertThat(errLogTrimmed, containsString("does not exist.".trim()));
				assertThatUseHelpOptionIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"-n", "xyz.jar", "-o", "zyx.jar"});
	}

	@Test
	public void testWithOldArchiveOptionButWithInvalidArgument() {
		exit.expectSystemExitWithStatus(1);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				String errLogTrimmed = errLog.getLog().trim();
				assertThat(errLogTrimmed, containsString("E: File".trim()));
				assertThat(errLogTrimmed, containsString("does not exist.".trim()));
				assertThatUseHelpOptionIsPrinted();
			}
		});
		JApiCmp.main(new String[]{"-n", pathTo("new.jar"), "-o", "xyz.jar"});
	}

	@Test
	public void testWithOldArchiveOptionAndNewArchiveOption() {
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				assertThat(errLog.getLog().trim(), not(containsString("E: ".trim())));
			}
		});
		JApiCmp.main(new String[]{"-n", pathTo("new.jar"), "-o", pathTo("old.jar")});
	}

	private String pathTo(String jarFileName) {
		return Paths.get(System.getProperty("user.dir"), "src", "test", "resources", jarFileName).toString();
	}

	static void assertListsEquals(ImmutableList<String> expected, ImmutableList<String> actual) {
		Joiner nlJoiner = Joiner.on("\n");
		assertEquals(nlJoiner.join(expected), nlJoiner.join(actual));
	}

	@Test
	public void testIgnoreMissingClassesByRegExCouldNotLoad() throws IOException, CannotCompileException {
		exit.expectSystemExitWithStatus(1);
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				String errLogTrimmed = errLog.getLog().trim();
				assertThat(errLogTrimmed, containsString("E: Could not load 'NotExistingSuperclass'".trim()));
			}
		});
		ClassApiSignatureSource cp = new ClassApiSignatureSource(true);
		ClassApiSignature ctClassSuperclass = CtClassBuilder.create().name("NotExistingSuperclass").addToClassPool(cp);
		CtConstructorBuilder.create().addToClass(ctClassSuperclass);
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassSuperclass).addToClassPool(cp);
		Path oldPath = Paths.get(System.getProperty("user.dir"), "target", JApiCmpTest.class.getSimpleName() + "_old.jar");
		createJarFile(oldPath, classApiSignature);
		Path newPath = Paths.get(System.getProperty("user.dir"), "target", JApiCmpTest.class.getSimpleName() + "_new.jar");
		createJarFile(newPath, classApiSignature);
		JApiCmp.main(new String[]{"-n", newPath.toString(), "-o", oldPath.toString()});
	}

	@Test
	public void testIgnoreMissingClassesByRegExMissingAreIgnore() throws IOException, CannotCompileException {
		exit.checkAssertionAfterwards(new Assertion() {
			public void checkAssertion() {
				String outLog = JApiCmpTest.this.outLog.getLog().trim();
				assertThat(outLog, containsString("Comparing".trim()));
				assertThat(outLog, containsString("WARNING: You have ignored certain classes".trim()));
			}
		});
		ClassApiSignatureSource cp = new ClassApiSignatureSource(true);
		ClassApiSignature ctClassSuperclass = CtClassBuilder.create().name("NotExistingSuperclass").addToClassPool(cp);
		CtConstructorBuilder.create().addToClass(ctClassSuperclass);
		ClassApiSignature classApiSignature = CtClassBuilder.create().name("Test").withSuperclass(ctClassSuperclass).addToClassPool(cp);
		Path oldPath = Paths.get(System.getProperty("user.dir"), "target", JApiCmpTest.class.getSimpleName() + "_old.jar");
		createJarFile(oldPath, classApiSignature);
		Path newPath = Paths.get(System.getProperty("user.dir"), "target", JApiCmpTest.class.getSimpleName() + "_new.jar");
		createJarFile(newPath, classApiSignature);
		JApiCmp.main(new String[]{"-n", newPath.toString(), "-o", oldPath.toString(), IGNORE_MISSING_CLASSES_BY_REGEX, ".*Superc.*"});
	}
}
