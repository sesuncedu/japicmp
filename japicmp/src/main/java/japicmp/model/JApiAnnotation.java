package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ApiAnnotation;
import com.google.common.base.Optional;
import javassist.bytecode.annotation.MemberValue;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JApiAnnotation implements JApiHasChangeStatus, JApiCompatibility {
	private final String fullyQualifiedName;
	private final Optional<ApiAnnotation> oldAnnotation;
	private final Optional<ApiAnnotation> newAnnotation;
	private final List<JApiAnnotationElement> elements = new LinkedList<>();
	private JApiChangeStatus changeStatus;

	public JApiAnnotation(String fullyQualifiedName, Optional<ApiAnnotation> oldAnnotation, Optional<ApiAnnotation> newAnnotation, JApiChangeStatus changeStatus) {
		this.fullyQualifiedName = fullyQualifiedName;
		this.oldAnnotation = oldAnnotation;
		this.newAnnotation = newAnnotation;
		computeElements(this.elements, oldAnnotation, newAnnotation);
		this.changeStatus = evaluateChangeStatus(changeStatus);
	}

	private void computeElements(List<JApiAnnotationElement> elements, Optional<ApiAnnotation> oldAnnotationOptional, Optional<ApiAnnotation> newAnnotationOptional) {
		if (oldAnnotationOptional.isPresent() && newAnnotationOptional.isPresent()) {
			ApiAnnotation oldAnnotation = oldAnnotationOptional.get();
			ApiAnnotation newAnnotation = newAnnotationOptional.get();
			Map<String, Optional<MemberValue>> oldMemberValueMap = buildMemberValueMap(oldAnnotation);
			Map<String, Optional<MemberValue>> newMemberValueMap = buildMemberValueMap(newAnnotation);
			for (String memberName : oldMemberValueMap.keySet()) {
				Optional<MemberValue> foundOptional = newMemberValueMap.get(memberName);
				if (foundOptional == null) {
					JApiAnnotationElement jApiAnnotationElement = new JApiAnnotationElement(memberName, oldMemberValueMap.get(memberName), Optional.<MemberValue>absent(),
						JApiChangeStatus.REMOVED);
					elements.add(jApiAnnotationElement);
				} else {
					JApiAnnotationElement jApiAnnotationElement = new JApiAnnotationElement(memberName, oldMemberValueMap.get(memberName), foundOptional,
						JApiChangeStatus.UNCHANGED);
					elements.add(jApiAnnotationElement);
				}
			}
			for (String memberName : newMemberValueMap.keySet()) {
				Optional<MemberValue> foundOptional = oldMemberValueMap.get(memberName);
				if (foundOptional == null) {
					JApiAnnotationElement jApiAnnotationElement = new JApiAnnotationElement(memberName, Optional.<MemberValue>absent(), newMemberValueMap.get(memberName),
						JApiChangeStatus.NEW);
					elements.add(jApiAnnotationElement);
				}
			}
		} else {
			if (oldAnnotationOptional.isPresent()) {
				ApiAnnotation oldAnnotation = oldAnnotationOptional.get();
				Map<String, Optional<MemberValue>> oldMemberValueMap = buildMemberValueMap(oldAnnotation);
				for (String memberName : oldMemberValueMap.keySet()) {
					JApiAnnotationElement jApiAnnotationElement = new JApiAnnotationElement(memberName, oldMemberValueMap.get(memberName), Optional.<MemberValue>absent(),
						JApiChangeStatus.REMOVED);
					elements.add(jApiAnnotationElement);
				}
			}
			if (newAnnotationOptional.isPresent()) {
				ApiAnnotation newAnnotation = newAnnotationOptional.get();
				Map<String, Optional<MemberValue>> newMemberValueMap = buildMemberValueMap(newAnnotation);
				for (String memberName : newMemberValueMap.keySet()) {
					JApiAnnotationElement jApiAnnotationElement = new JApiAnnotationElement(memberName, Optional.<MemberValue>absent(), newMemberValueMap.get(memberName),
						JApiChangeStatus.NEW);
					elements.add(jApiAnnotationElement);
				}
			}
		}
	}

	private Map<String, Optional<MemberValue>> buildMemberValueMap(ApiAnnotation apiAnnotation) {
		Map<String, Optional<MemberValue>> map = new HashMap<>();
		@SuppressWarnings("unchecked")
		Set<String> memberNames = apiAnnotation.getMemberNames();
		if (memberNames != null) {
			for (String memberName : memberNames) {
				MemberValue memberValue = apiAnnotation.getMemberValue(memberName);
				if (memberValue == null) {
					map.put(memberName, Optional.<MemberValue>absent());
				} else {
					map.put(memberName, Optional.of(memberValue));
				}
			}
		}
		return map;
	}

	private JApiChangeStatus evaluateChangeStatus(JApiChangeStatus changeStatus) {
		if (changeStatus == JApiChangeStatus.UNCHANGED) {
			for (JApiAnnotationElement annotationElement : elements) {
				if (annotationElement.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
					changeStatus = JApiChangeStatus.MODIFIED;
				}
			}
		}
		return changeStatus;
	}

	@Override
	@XmlAttribute(name = "changeStatus")
	public JApiChangeStatus getChangeStatus() {
		return this.changeStatus;
	}

	@XmlAttribute(name = "fullyQualifiedName")
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	@XmlTransient
	public Optional<ApiAnnotation> getOldAnnotation() {
		return oldAnnotation;
	}

	@XmlTransient
	public Optional<ApiAnnotation> getNewAnnotation() {
		return newAnnotation;
	}

	@XmlElementWrapper(name = "elements")
	@XmlElement(name = "element")
	public List<JApiAnnotationElement> getElements() {
		return elements;
	}

	@Override
	@XmlAttribute
	public boolean isBinaryCompatible() {
		return true;
	}

	@Override
	@XmlAttribute
	public boolean isSourceCompatible() {
		return true;
	}

	@XmlElementWrapper(name = "compatibilityChanges")
	@XmlElement(name = "compatibilityChange")
	public List<JApiCompatibilityChange> getCompatibilityChanges() {
		return Collections.EMPTY_LIST;
	}
}
