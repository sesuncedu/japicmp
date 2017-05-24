package japicmp.test;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.google.common.base.Optional;
import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiChangeStatus;
import japicmp.model.JApiClass;
import japicmp.model.SyntheticAttribute;
import javassist.CannotCompileException;

import javassist.ApiField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static japicmp.test.util.Helper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SyntheticAttributeTest {

	@Test
	public void test() throws IOException, CannotCompileException, NotFoundException {
		JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
		options.setIncludeSynthetic(true);
		JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(options);
		JApiCmpArchive archiveV1 = getArchive("japicmp-test-v1.jar");
		JApiCmpArchive archiveV2 = getArchive("japicmp-test-v2.jar");
		JApiCmpArchive instrumentedArchiveV2 = instrumentClass(archiveV2);
		List<JApiClass> jApiClasses = jarArchiveComparator.compare(archiveV1, instrumentedArchiveV2);
		JApiClass syntheticClass = getJApiClass(jApiClasses, Synthetic.class.getName());
		assertThat(syntheticClass.getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(syntheticClass.getSyntheticAttribute().getChangeStatus(), is(JApiChangeStatus.MODIFIED));
		assertThat(syntheticClass.getSyntheticAttribute().getOldAttribute(), is(Optional.of(SyntheticAttribute.NON_SYNTHETIC)));
		assertThat(syntheticClass.getSyntheticAttribute().getNewAttribute(), is(Optional.of(SyntheticAttribute.SYNTHETIC)));
		assertThat(getJApiMethod(syntheticClass.getMethods(), "newMethod").getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(getJApiMethod(syntheticClass.getMethods(), "newMethod").getSyntheticAttribute().getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(getJApiMethod(syntheticClass.getMethods(), "newMethod").getSyntheticAttribute().getOldAttribute(), is(Optional.<SyntheticAttribute>absent()));
		assertThat(getJApiMethod(syntheticClass.getMethods(), "newMethod").getSyntheticAttribute().getNewAttribute(), is(Optional.of(SyntheticAttribute.SYNTHETIC)));
		assertThat(getJApiField(syntheticClass.getFields(), "newField").getSyntheticAttribute().getChangeStatus(), is(JApiChangeStatus.NEW));
		assertThat(getJApiField(syntheticClass.getFields(), "newField").getSyntheticAttribute().getOldAttribute(), is(Optional.<SyntheticAttribute>absent()));
		assertThat(getJApiField(syntheticClass.getFields(), "newField").getSyntheticAttribute().getNewAttribute(), is(Optional.of(SyntheticAttribute.SYNTHETIC)));
	}

	private JApiCmpArchive instrumentClass(JApiCmpArchive archive) throws IOException, CannotCompileException, NotFoundException {
		ClassApiSignatureSource classApiSignatureSource = ClassApiSignatureSource.getDefault();
		String path = Paths.get(System.getProperty("user.dir"), "target", "japicmp-test-v2-instrumented.jar").toString();
		try (ZipFile zipFile = new ZipFile(archive.getFile()); ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path))) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry zipEntry = entries.nextElement();
				String name = zipEntry.getName();
				if (name.contains(Synthetic.class.getSimpleName() + ".class")) {
					try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
						ClassApiSignature classApiSignature = classApiSignatureSource.makeClass(inputStream);
						CtMethod method = CtNewMethod.make("public int newMethod(int x) { return x; }", classApiSignature);
						javassist.bytecode.SyntheticAttribute syntheticAttribute = new javassist.bytecode.SyntheticAttribute(classApiSignature.getClassFile().getConstPool());
						method.setAttribute(syntheticAttribute.getName(), syntheticAttribute.get());
						classApiSignature.setAttribute(syntheticAttribute.getName(), syntheticAttribute.get());
						classApiSignature.addMethod(method);
						ApiField newField = new ApiField(classApiSignatureSource.get(String.class.getCanonicalName()), "newField", classApiSignature);
						newField.setAttribute(syntheticAttribute.getName(), syntheticAttribute.get());
						newField.setModifiers(Modifier.PUBLIC);
						classApiSignature.addField(newField);
						byte[] bytecode = classApiSignature.toBytecode();
						ZipEntry newEntry = new ZipEntry(zipEntry.getName());
						zos.putNextEntry(newEntry);
						zos.write(bytecode);
					}
				} else {
					zos.putNextEntry(zipEntry);
					InputStream is = zipFile.getInputStream(zipEntry);
					byte[] buf = new byte[1024];
					int len;
					while ((len = (is.read(buf))) != -1) {
						zos.write(buf, 0, len);
					}
				}
			}
		}
		return new JApiCmpArchive(new File(path), "n.a.");
	}
}
