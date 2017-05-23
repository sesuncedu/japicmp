package japicmp.util;

import javassist.CannotCompileException;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarUtil {

	public static void createJarFile(Path jarFileName, ClassApiSignature... ctClasses) throws IOException, CannotCompileException {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFileName.toString()), manifest)) {
			for (ClassApiSignature classApiSignature : ctClasses) {
				JarEntry entry = new JarEntry(classApiSignature.getSimpleName() + ".class");
				jarStream.putNextEntry(entry);
				byte[] bytecode = classApiSignature.toBytecode();
				jarStream.write(bytecode, 0, bytecode.length);
				jarStream.closeEntry();
			}
		}
	}
}
