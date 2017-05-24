package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ApiAnnotationsAttribute;
import com.criticollab.japicmp.classinfo.api.ApiField;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.util.AnnotationHelper;
import japicmp.util.Constants;
import japicmp.util.MethodDescriptorParser;
import japicmp.util.ModifierHelper;
import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JApiField implements JApiHasChangeStatus, JApiHasModifiers, JApiHasAccessModifier, JApiHasStaticModifier,
	JApiHasFinalModifier, JApiHasTransientModifier, JApiCompatibility, JApiHasAnnotations, JApiCanBeSynthetic {
	private final JApiChangeStatus changeStatus;
	private final JApiClass jApiClass;
	private final Optional<ApiField> oldFieldOptional;
	private final Optional<ApiField> newFieldOptional;
	private final List<JApiAnnotation> annotations = new LinkedList<>();
	private final JApiModifier<AccessModifier> accessModifier;
	private final JApiModifier<StaticModifier> staticModifier;
	private final JApiModifier<FinalModifier> finalModifier;
	private final JApiModifier<TransientModifier> transientModifier;
	private final JApiModifier<SyntheticModifier> syntheticModifier;
	private final JApiAttribute<SyntheticAttribute> syntheticAttribute;
	private final List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>();
	private final JApiType type;

	public JApiField(JApiClass jApiClass, JApiChangeStatus changeStatus, Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional, JarArchiveComparatorOptions options) {
		this.jApiClass = jApiClass;
		this.oldFieldOptional = oldFieldOptional;
		this.newFieldOptional = newFieldOptional;
		computeAnnotationChanges(this.annotations, oldFieldOptional, newFieldOptional, options);
		this.accessModifier = extractAccessModifier(oldFieldOptional, newFieldOptional);
		this.staticModifier = extractStaticModifier(oldFieldOptional, newFieldOptional);
		this.finalModifier = extractFinalModifier(oldFieldOptional, newFieldOptional);
		this.transientModifier = extractTransientModifier(oldFieldOptional, newFieldOptional);
		this.syntheticModifier = extractSyntheticModifier(oldFieldOptional, newFieldOptional);
		this.syntheticAttribute = extractSyntheticAttribute(oldFieldOptional, newFieldOptional);
		this.type = extractType(oldFieldOptional, newFieldOptional);
		this.changeStatus = evaluateChangeStatus(changeStatus);
	}

	private void computeAnnotationChanges(List<JApiAnnotation> annotations, Optional<ApiField> oldBehavior, Optional<ApiField> newBehavior, JarArchiveComparatorOptions options) {
		AnnotationHelper.computeAnnotationChanges(annotations, oldBehavior, newBehavior, options, new AnnotationHelper.AnnotationsAttributeCallback<ApiField>() {
			@Override
			public ApiAnnotationsAttribute getAnnotationsAttribute(ApiField field) {
				return (ApiAnnotationsAttribute) field.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag);
			}
		});
	}

	private JApiType extractType(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		JApiType jApiType = new JApiType(Optional.<String>absent(), Optional.<String>absent(), JApiChangeStatus.UNCHANGED);
		if (oldFieldOptional.isPresent() && newFieldOptional.isPresent()) {
			ApiField oldField = oldFieldOptional.get();
			ApiField newField = newFieldOptional.get();
			String oldType = signatureToType(oldField.getSignature());
			String newType = signatureToType(newField.getSignature());
			if (oldType.equals(newType)) {
				jApiType = new JApiType(Optional.of(oldType), Optional.of(newType), JApiChangeStatus.UNCHANGED);
			} else {
				jApiType = new JApiType(Optional.of(oldType), Optional.of(newType), JApiChangeStatus.MODIFIED);
			}
		} else {
			if (oldFieldOptional.isPresent()) {
				ApiField oldField = oldFieldOptional.get();
				String oldType = signatureToType(oldField.getSignature());
				jApiType = new JApiType(Optional.of(oldType), Optional.<String>absent(), JApiChangeStatus.REMOVED);
			} else if (newFieldOptional.isPresent()) {
				ApiField newField = newFieldOptional.get();
				String newType = signatureToType(newField.getSignature());
				jApiType = new JApiType(Optional.<String>absent(), Optional.of(newType), JApiChangeStatus.NEW);
			}
		}
		return jApiType;
	}

	private String signatureToType(String signature) {
		MethodDescriptorParser methodDescriptorParser = new MethodDescriptorParser();
		List<String> types = methodDescriptorParser.parseTypes(signature);
		if (types.size() > 0) {
			return types.get(0);
		}
		return "n.a.";
	}

	private JApiChangeStatus evaluateChangeStatus(JApiChangeStatus changeStatus) {
		if (changeStatus == JApiChangeStatus.UNCHANGED) {
			if (this.accessModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.staticModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.finalModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.transientModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.syntheticAttribute.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.type.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
		}
		return changeStatus;
	}

	private JApiAttribute<SyntheticAttribute> extractSyntheticAttribute(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		JApiAttribute<SyntheticAttribute> jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
		if (oldFieldOptional.isPresent() && newFieldOptional.isPresent()) {
			ApiField oldField = oldFieldOptional.get();
			ApiField newField = newFieldOptional.get();
			byte[] attributeOldField = oldField.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
			byte[] attributeNewField = newField.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
			if (attributeOldField != null && attributeNewField != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
			} else if (attributeOldField != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.MODIFIED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
			} else if (attributeNewField != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.MODIFIED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
			} else {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
			}
		} else {
			if (oldFieldOptional.isPresent()) {
				ApiField ApiField = oldFieldOptional.get();
				byte[] attribute = ApiField.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
				if (attribute != null) {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.REMOVED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.<SyntheticAttribute>absent());
				} else {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.REMOVED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.<SyntheticAttribute>absent());
				}
			}
			if (newFieldOptional.isPresent()) {
				ApiField ApiField = newFieldOptional.get();
				byte[] attribute = ApiField.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
				if (attribute != null) {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.NEW, Optional.<SyntheticAttribute>absent(), Optional.of(SyntheticAttribute.SYNTHETIC));
				} else {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.NEW, Optional.<SyntheticAttribute>absent(), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
				}
			}
		}
		return jApiAttribute;
	}

	private JApiModifier<StaticModifier> extractStaticModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<StaticModifier>() {
			@Override
			public StaticModifier getModifierForOld(ApiField oldField) {
				return Modifier.isStatic(oldField.getModifiers()) ? StaticModifier.STATIC : StaticModifier.NON_STATIC;
			}

			@Override
			public StaticModifier getModifierForNew(ApiField newField) {
				return Modifier.isStatic(newField.getModifiers()) ? StaticModifier.STATIC : StaticModifier.NON_STATIC;
			}
		});
	}

	private JApiModifier<FinalModifier> extractFinalModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<FinalModifier>() {
			@Override
			public FinalModifier getModifierForOld(ApiField oldField) {
				return Modifier.isFinal(oldField.getModifiers()) ? FinalModifier.FINAL : FinalModifier.NON_FINAL;
			}

			@Override
			public FinalModifier getModifierForNew(ApiField newField) {
				return Modifier.isFinal(newField.getModifiers()) ? FinalModifier.FINAL : FinalModifier.NON_FINAL;
			}
		});
	}

	private JApiModifier<AccessModifier> extractAccessModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<AccessModifier>() {
			@Override
			public AccessModifier getModifierForOld(ApiField oldField) {
				return ModifierHelper.translateToModifierLevel(oldField.getModifiers());
			}

			@Override
			public AccessModifier getModifierForNew(ApiField newField) {
				return ModifierHelper.translateToModifierLevel(newField.getModifiers());
			}
		});
	}

	private JApiModifier<AbstractModifier> extractAbstractModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<AbstractModifier>() {
			@Override
			public AbstractModifier getModifierForOld(ApiField oldField) {
				return Modifier.isAbstract(oldField.getModifiers()) ? AbstractModifier.ABSTRACT : AbstractModifier.NON_ABSTRACT;
			}

			@Override
			public AbstractModifier getModifierForNew(ApiField newField) {
				return Modifier.isAbstract(newField.getModifiers()) ? AbstractModifier.ABSTRACT : AbstractModifier.NON_ABSTRACT;
			}
		});
	}

	private JApiModifier<TransientModifier> extractTransientModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<TransientModifier>() {
			@Override
			public TransientModifier getModifierForOld(ApiField oldField) {
				return Modifier.isTransient(oldField.getModifiers()) ? TransientModifier.TRANSIENT : TransientModifier.NON_TRANSIENT;
			}

			@Override
			public TransientModifier getModifierForNew(ApiField newField) {
				return Modifier.isTransient(newField.getModifiers()) ? TransientModifier.TRANSIENT : TransientModifier.NON_TRANSIENT;
			}
		});
	}

	private JApiModifier<SyntheticModifier> extractSyntheticModifier(Optional<ApiField> oldFieldOptional, Optional<ApiField> newFieldOptional) {
		return ModifierHelper.extractModifierFromField(oldFieldOptional, newFieldOptional, new ModifierHelper.ExtractModifierFromFieldCallback<SyntheticModifier>() {
			@Override
			public SyntheticModifier getModifierForOld(ApiField oldField) {
				return ModifierHelper.isSynthetic(oldField.getModifiers()) ? SyntheticModifier.SYNTHETIC : SyntheticModifier.NON_SYNTHETIC;
			}

			@Override
			public SyntheticModifier getModifierForNew(ApiField newField) {
				return ModifierHelper.isSynthetic(newField.getModifiers()) ? SyntheticModifier.SYNTHETIC : SyntheticModifier.NON_SYNTHETIC;
			}
		});
	}

	@XmlAttribute(name = "changeStatus")
	public JApiChangeStatus getChangeStatus() {
		return changeStatus;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		String name = "n.a.";
		if (oldFieldOptional.isPresent()) {
			name = oldFieldOptional.get().getName();
		}
		if (newFieldOptional.isPresent()) {
			name = newFieldOptional.get().getName();
		}
		return name;
	}

	@XmlTransient
	public Optional<ApiField> getOldFieldOptional() {
		return oldFieldOptional;
	}

	@XmlTransient
	public Optional<ApiField> getNewFieldOptional() {
		return newFieldOptional;
	}

	@XmlElementWrapper(name = "modifiers")
	@XmlElement(name = "modifier")
	public List<? extends JApiModifier<? extends Enum<? extends Enum<?>>>> getModifiers() {
		return Arrays.asList(this.accessModifier, this.staticModifier, this.finalModifier, this.syntheticModifier);
	}

	@XmlTransient
	public JApiModifier<StaticModifier> getStaticModifier() {
		return staticModifier;
	}

	@XmlTransient
	public JApiModifier<FinalModifier> getFinalModifier() {
		return finalModifier;
	}

	@XmlTransient
	public JApiModifier<TransientModifier> getTransientModifier() {
		return transientModifier;
	}

	@XmlTransient
	public JApiModifier<AccessModifier> getAccessModifier() {
		return accessModifier;
	}

	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	public List<JApiAttribute<? extends Enum<?>>> getAttributes() {
		List<JApiAttribute<? extends Enum<?>>> list = new ArrayList<>();
		list.add(this.syntheticAttribute);
		return list;
	}

	@XmlTransient
	public JApiModifier<SyntheticModifier> getSyntheticModifier() {
		return this.syntheticModifier;
	}

	@XmlTransient
	public JApiAttribute<SyntheticAttribute> getSyntheticAttribute() {
		return syntheticAttribute;
	}

	@XmlElement(name = "type")
	public JApiType getType() {
		return type;
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
		return sourceCompatible;
	}

	@XmlElementWrapper(name = "compatibilityChanges")
	@XmlElement(name = "compatibilityChange")
	public List<JApiCompatibilityChange> getCompatibilityChanges() {
		return compatibilityChanges;
	}

	@XmlElementWrapper(name = "annotations")
	@XmlElement(name = "annotation")
	public List<JApiAnnotation> getAnnotations() {
		return annotations;
	}

	@XmlTransient
	public JApiClass getjApiClass() {
		return jApiClass;
	}
}
