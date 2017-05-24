package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.google.common.base.Optional;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

public class JApiImplementedInterface implements JApiHasChangeStatus, JApiCompatibility {
	private final ClassApiSignature classApiSignature;
	private final String fullyQualifiedName;
	private final JApiChangeStatus changeStatus;
	private final List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>();
	private Optional<JApiClass> correspondingJApiClass = Optional.absent();

	public JApiImplementedInterface(ClassApiSignature classApiSignature, String fullyQualifiedName, JApiChangeStatus changeStatus) {
		this.classApiSignature = classApiSignature;
		this.fullyQualifiedName = fullyQualifiedName;
		this.changeStatus = changeStatus;
	}

	@XmlAttribute
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	@XmlAttribute
	public JApiChangeStatus getChangeStatus() {
		return changeStatus;
	}

	@Override
	@XmlAttribute
	public boolean isBinaryCompatible() {
		boolean binaryCompatible = true;
		for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
			if (!compatibilityChange.isBinaryCompatible()) {
				binaryCompatible = false;
			}
		}
		if (binaryCompatible && correspondingJApiClass.isPresent()) {
			if (!correspondingJApiClass.get().isBinaryCompatible()) {
				binaryCompatible = false;
			}
		}
		return binaryCompatible;
	}

	@Override
	@XmlAttribute
	public boolean isSourceCompatible() {
		boolean sourceCompatible = true;
		for (JApiCompatibilityChange compatibilityChange : compatibilityChanges) {
			if (!compatibilityChange.isSourceCompatible()) {
				sourceCompatible = false;
			}
		}
		if (sourceCompatible && correspondingJApiClass.isPresent()) {
			if (!correspondingJApiClass.get().isSourceCompatible()) {
				sourceCompatible = false;
			}
		}
		return sourceCompatible;
	}

	@XmlElementWrapper(name = "compatibilityChanges")
	@XmlElement(name = "compatibilityChange")
	public List<JApiCompatibilityChange> getCompatibilityChanges() {
		return compatibilityChanges;
	}

	public void setJApiClass(JApiClass jApiClass) {
		this.correspondingJApiClass = Optional.of(jApiClass);
	}

	@XmlTransient
	public ClassApiSignature getCtClass() {
		return classApiSignature;
	}
}
