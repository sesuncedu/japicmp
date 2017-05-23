package japicmp.model;

import com.criticollab.japicmp.classinfo.ClassApiSignature;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparator;

import javax.xml.bind.annotation.XmlAttribute;

public class JApiException implements JApiHasChangeStatus {
	private final String name;
	private final JApiChangeStatus changeStatus;
	private final boolean checkedException;

	public JApiException(JarArchiveComparator jarArchiveComparator, String name, Optional<ClassApiSignature> ctClassOptional, JApiChangeStatus changeStatus) {
		this.name = name;
		this.changeStatus = changeStatus;
		this.checkedException = isCheckedException(ctClassOptional, jarArchiveComparator);
	}

	private boolean isCheckedException(Optional<ClassApiSignature> ctClassOptional, JarArchiveComparator jarArchiveComparator) throws OutOfMemoryError {
		boolean checkedException = false;
		if (ctClassOptional.isPresent()) {
			boolean subClassOfException = false;
			ClassApiSignature classApiSignature = ctClassOptional.get();
			Optional<ClassApiSignature> exceptionOptional = jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.NEW, Exception.class.getName());
			if (exceptionOptional.isPresent()) {
				if (classApiSignature.subclassOf(exceptionOptional.get())) {
					subClassOfException = true;
				}
			}
			if (subClassOfException) {
				Optional<ClassApiSignature> runtimeExceptionOptional = jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.NEW, RuntimeException.class.getName());
				if (runtimeExceptionOptional.isPresent()) {
					if (!classApiSignature.subclassOf(runtimeExceptionOptional.get())) {
						checkedException = true;
					}
				}
			}
		}
		return checkedException;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	@XmlAttribute(name = "changeStatus")
	public JApiChangeStatus getChangeStatus() {
		return changeStatus;
	}

	public boolean isCheckedException() {
		return checkedException;
	}
}
