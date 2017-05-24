package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ApiMethod;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparator;
import japicmp.util.MethodDescriptorParser;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class JApiMethod extends JApiBehavior {
	private final Optional<ApiMethod> oldMethod;
	private final Optional<ApiMethod> newMethod;
	private final JApiReturnType returnType;

	public JApiMethod(JApiClass jApiClass, String name, JApiChangeStatus changeStatus, Optional<ApiMethod> oldMethod, Optional<ApiMethod> newMethod, JarArchiveComparator jarArchiveComparator) {
		super(jApiClass, name, oldMethod, newMethod, changeStatus, jarArchiveComparator);
		this.oldMethod = oldMethod;
		this.newMethod = newMethod;
		this.returnType = computeReturnTypeChanges(oldMethod, newMethod);
		this.changeStatus = evaluateChangeStatus(this.changeStatus);
	}

	private JApiChangeStatus evaluateChangeStatus(JApiChangeStatus changeStatus) {
		if (changeStatus == JApiChangeStatus.UNCHANGED) {
			if (this.returnType.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
		}
		return changeStatus;
	}

	private JApiReturnType computeReturnTypeChanges(Optional<ApiMethod> oldMethodOptional, Optional<ApiMethod> newMethodOptional) {
		JApiReturnType jApiReturnType = new JApiReturnType(JApiChangeStatus.UNCHANGED, Optional.<String>absent(), Optional.<String>absent());
		if (oldMethodOptional.isPresent() && newMethodOptional.isPresent()) {
			String oldReturnType = computeReturnType(oldMethodOptional.get());
			String newReturnType = computeReturnType(newMethodOptional.get());
			JApiChangeStatus changeStatusReturnType = JApiChangeStatus.UNCHANGED;
			if (!oldReturnType.equals(newReturnType)) {
				changeStatusReturnType = JApiChangeStatus.MODIFIED;
			}
			jApiReturnType = new JApiReturnType(changeStatusReturnType, Optional.of(oldReturnType), Optional.of(newReturnType));
		} else {
			if (oldMethodOptional.isPresent()) {
				String oldReturnType = computeReturnType(oldMethodOptional.get());
				jApiReturnType = new JApiReturnType(JApiChangeStatus.REMOVED, Optional.of(oldReturnType), Optional.<String>absent());
			}
			if (newMethodOptional.isPresent()) {
				String newReturnType = computeReturnType(newMethodOptional.get());
				jApiReturnType = new JApiReturnType(JApiChangeStatus.NEW, Optional.<String>absent(), Optional.of(newReturnType));
			}
		}
		return jApiReturnType;
	}

	private String computeReturnType(ApiMethod oldMethod) {
		MethodDescriptorParser parser = new MethodDescriptorParser();
		parser.parse(oldMethod.getSignature());
		return parser.getReturnType();
	}

	public boolean hasSameReturnType(JApiMethod otherMethod) {
		boolean haveSameReturnType = false;
		JApiReturnType otherReturnType = otherMethod.getReturnType();
		if (otherReturnType.getChangeStatus() == JApiChangeStatus.UNCHANGED || otherReturnType.getChangeStatus() == JApiChangeStatus.MODIFIED) {
			if (this.returnType.getChangeStatus() == JApiChangeStatus.UNCHANGED || this.returnType.getChangeStatus() == JApiChangeStatus.MODIFIED) {
				if (otherReturnType.getOldReturnType().equals(this.returnType.getOldReturnType()) && otherReturnType.getNewReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.NEW) {
				if (otherReturnType.getNewReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.REMOVED) {
				if (otherReturnType.getOldReturnType().equals(this.returnType.getOldReturnType())) {
					haveSameReturnType = true;
				}
			}
		} else if (otherReturnType.getChangeStatus() == JApiChangeStatus.NEW) {
			if (this.returnType.getChangeStatus() == JApiChangeStatus.UNCHANGED || this.returnType.getChangeStatus() == JApiChangeStatus.MODIFIED) {
				if (otherReturnType.getNewReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.NEW) {
				if (otherReturnType.getNewReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.REMOVED) {
				if (otherReturnType.getNewReturnType().equals(this.returnType.getOldReturnType())) {
					haveSameReturnType = true;
				}
			}
		} else {
			if (this.returnType.getChangeStatus() == JApiChangeStatus.UNCHANGED || this.returnType.getChangeStatus() == JApiChangeStatus.MODIFIED) {
				if (otherReturnType.getOldReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.NEW) {
				if (otherReturnType.getOldReturnType().equals(this.returnType.getNewReturnType())) {
					haveSameReturnType = true;
				}
			} else if (this.returnType.getChangeStatus() == JApiChangeStatus.REMOVED) {
				if (otherReturnType.getOldReturnType().equals(this.returnType.getOldReturnType())) {
					haveSameReturnType = true;
				}
			}
		}
		return haveSameReturnType;
	}

	public boolean hasSameSignature(JApiMethod jApiMethod) {
		return hasSameReturnType(jApiMethod) && hasSameParameter(jApiMethod);
	}

	@XmlTransient
	public Optional<ApiMethod> getNewMethod() {
		return newMethod;
	}

	@XmlTransient
	public Optional<ApiMethod> getOldMethod() {
		return oldMethod;
	}

	@XmlElement(name = "returnType")
	public JApiReturnType getReturnType() {
		return returnType;
	}
}
