package japicmp.util;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import javassist.CannotCompileException;

import java.io.IOException;
import java.nio.file.Path;

public class JarUtil {

	public static void createJarFile(Path jarFileName, ClassApiSignature... ctClasses) throws IOException, CannotCompileException {
		throw new NoSuchMethodError("Not implemented yet");
//		Manifest manifest = new Manifest();
//		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
//		try (JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(jarFileName.toString()), manifest)) {
//			for (ClassApiSignature classApiSignature : ctClasses) {
//				JarEntry entry = new JarEntry(classApiSignature.getSimpleName() + ".class");
//				jarStream.putNextEntry(entry);
//				byte[] bytecode = classApiSignature.toBytecode();
//				jarStream.write(bytecode, 0, bytecode.length);
//				jarStream.closeEntry();
//			}
//		}
	}
}
