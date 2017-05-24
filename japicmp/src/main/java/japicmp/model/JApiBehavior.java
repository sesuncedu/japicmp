package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ApiAnnotationsAttribute;
import com.criticollab.japicmp.classinfo.api.ApiBehavior;
import com.criticollab.japicmp.classinfo.api.ApiConstructor;
import com.criticollab.japicmp.classinfo.api.ApiExceptionsAttribute;
import com.criticollab.japicmp.classinfo.api.ApiMethod;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.util.AnnotationHelper;
import japicmp.util.Constants;
import japicmp.util.ModifierHelper;
import japicmp.util.OptionalHelper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JApiBehavior implements JApiHasModifiers, JApiHasChangeStatus, JApiHasAccessModifier, JApiHasStaticModifier,
	JApiHasFinalModifier, JApiHasAbstractModifier, JApiCompatibility, JApiHasAnnotations, JApiHasBridgeModifier,
	JApiCanBeSynthetic, JApiHasLineNumber {
	private final JApiClass jApiClass;
	private final String name;
	private final JarArchiveComparator jarArchiveComparator;
	private final List<JApiParameter> parameters = new LinkedList<>();
	private final List<JApiAnnotation> annotations = new LinkedList<>();
	private final JApiModifier<AccessModifier> accessModifier;
	private final JApiModifier<FinalModifier> finalModifier;
	private final JApiModifier<StaticModifier> staticModifier;
	private final JApiModifier<AbstractModifier> abstractModifier;
	private final JApiModifier<BridgeModifier> bridgeModifier;
	private final JApiModifier<SyntheticModifier> syntheticModifier;
	private final JApiAttribute<SyntheticAttribute> syntheticAttribute;
	private final List<JApiException> exceptions;
	protected JApiChangeStatus changeStatus;
	private final Optional<Integer> oldLineNumber;
	private final Optional<Integer> newLineNumber;
	private final List<JApiCompatibilityChange> compatibilityChanges = new ArrayList<>();

	public JApiBehavior(JApiClass jApiClass, String name, Optional<? extends ApiBehavior> oldBehavior, Optional<? extends ApiBehavior> newBehavior, JApiChangeStatus changeStatus, JarArchiveComparator jarArchiveComparator) {
		this.jApiClass = jApiClass;
		this.name = name;
		this.jarArchiveComparator = jarArchiveComparator;
		computeAnnotationChanges(annotations, oldBehavior, newBehavior, jarArchiveComparator.getJarArchiveComparatorOptions());
		this.accessModifier = extractAccessModifier(oldBehavior, newBehavior);
		this.finalModifier = extractFinalModifier(oldBehavior, newBehavior);
		this.staticModifier = extractStaticModifier(oldBehavior, newBehavior);
		this.abstractModifier = extractAbstractModifier(oldBehavior, newBehavior);
		this.bridgeModifier = extractBridgeModifier(oldBehavior, newBehavior);
		this.syntheticModifier = extractSyntheticModifier(oldBehavior, newBehavior);
		this.syntheticAttribute = extractSyntheticAttribute(oldBehavior, newBehavior);
		this.exceptions = computeExceptionChanges(oldBehavior, newBehavior);
		this.changeStatus = evaluateChangeStatus(changeStatus);
		this.oldLineNumber = getLineNumber(oldBehavior);
		this.newLineNumber = getLineNumber(newBehavior);
	}

	private List<JApiException> computeExceptionChanges(Optional<? extends ApiBehavior> oldMethodOptional, Optional<? extends ApiBehavior> newMethodOptional) {
		List<JApiException> exceptionList = new ArrayList<>();
		if (oldMethodOptional.isPresent() && newMethodOptional.isPresent()) {
			List<String> oldExceptions = extractExceptions(oldMethodOptional);
			List<String> newExceptions = extractExceptions(newMethodOptional);
			for (String oldException : oldExceptions) {
				if (newExceptions.contains(oldException)) {
					exceptionList.add(new JApiException(jarArchiveComparator, oldException, jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.NEW, oldException), JApiChangeStatus.UNCHANGED));
					newExceptions.remove(oldException);
				} else {
					exceptionList.add(new JApiException(jarArchiveComparator, oldException, jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.OLD, oldException), JApiChangeStatus.REMOVED));
				}
			}
			for (String newException : newExceptions) {
				exceptionList.add(new JApiException(jarArchiveComparator, newException, jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.NEW, newException), JApiChangeStatus.NEW));
			}
		} else if (oldMethodOptional.isPresent()) {
			List<String> exceptions = extractExceptions(oldMethodOptional);
			for (String exception : exceptions) {
				exceptionList.add(new JApiException(jarArchiveComparator, exception, jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.OLD, exception), JApiChangeStatus.REMOVED));
			}
		} else if (newMethodOptional.isPresent()) {
			List<String> exceptions = extractExceptions(newMethodOptional);
			for (String exception : exceptions) {
				exceptionList.add(new JApiException(jarArchiveComparator, exception, jarArchiveComparator.loadClass(JarArchiveComparator.ArchiveType.NEW, exception), JApiChangeStatus.NEW));
			}
		}
		return exceptionList;
	}

	private List<String> extractExceptions(Optional<? extends ApiBehavior> methodOptional) {
		if (methodOptional.isPresent()) {
			ApiBehavior methodExpression = methodOptional.get();
			ApiExceptionsAttribute exceptionsAttribute = methodExpression.getMethodInfo().getExceptionsAttribute();
			String[] exceptions;
			if (exceptionsAttribute != null) {
				exceptions = exceptionsAttribute.getExceptions();
			} else {
				exceptions = new String[0];
			}
			List<String> list = new ArrayList<>(exceptions.length);
			Collections.addAll(list, exceptions);
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	private Optional<Integer> getLineNumber(Optional<? extends ApiBehavior> methodOptional) {
		Optional<Integer> lineNumberOptional = Optional.absent();
		if (methodOptional.isPresent()) {
			ApiBehavior apiMethod = methodOptional.get();
			int lineNumber = apiMethod.getMethodInfo().getLineNumber(0);
			if (lineNumber >= 0) {
				lineNumberOptional = Optional.of(lineNumber);
			}
		}
		return lineNumberOptional;
	}

	@SuppressWarnings("unchecked")
	private void computeAnnotationChanges(List<JApiAnnotation> annotations, Optional<? extends ApiBehavior> oldBehavior, Optional<? extends ApiBehavior> newBehavior, JarArchiveComparatorOptions options) {
		if (oldBehavior.isPresent()) {
			ApiBehavior apiBehavior = oldBehavior.get();
			if (apiBehavior instanceof ApiMethod) {
				computeAnnotationChangesMethod(annotations, (Optional<ApiMethod>) oldBehavior, (Optional<ApiMethod>) newBehavior, options);
			} else if (apiBehavior instanceof ApiConstructor) {
				computeAnnotationChangesConstructor(annotations, (Optional<ApiConstructor>) oldBehavior, (Optional<ApiConstructor>) newBehavior, options);
			}
		} else if (newBehavior.isPresent()) {
			ApiBehavior apiBehavior = newBehavior.get();
			if (apiBehavior instanceof ApiMethod) {
				computeAnnotationChangesMethod(annotations, (Optional<ApiMethod>) oldBehavior, (Optional<ApiMethod>) newBehavior, options);
			} else if (apiBehavior instanceof ApiConstructor) {
				computeAnnotationChangesConstructor(annotations, (Optional<ApiConstructor>) oldBehavior, (Optional<ApiConstructor>) newBehavior, options);
			}
		}
	}

	private void computeAnnotationChangesMethod(List<JApiAnnotation> annotations, Optional<ApiMethod> oldBehavior, Optional<ApiMethod> newBehavior, JarArchiveComparatorOptions options) {
		AnnotationHelper.computeAnnotationChanges(annotations, oldBehavior, newBehavior, options, new AnnotationHelper.AnnotationsAttributeCallback<ApiMethod>() {
			@Override
			public ApiAnnotationsAttribute getAnnotationsAttribute(ApiMethod method) {
				return (ApiAnnotationsAttribute) method.getMethodInfo().getAttribute(ApiAnnotationsAttribute.visibleTag);
			}
		});
	}

	private void computeAnnotationChangesConstructor(List<JApiAnnotation> annotations, Optional<ApiConstructor> oldBehavior, Optional<ApiConstructor> newBehavior, JarArchiveComparatorOptions options) {
		AnnotationHelper.computeAnnotationChanges(annotations, oldBehavior, newBehavior, options, new AnnotationHelper.AnnotationsAttributeCallback<ApiConstructor>() {
			@Override
			public ApiAnnotationsAttribute getAnnotationsAttribute(ApiConstructor method) {
				return (ApiAnnotationsAttribute) method.getMethodInfo().getAttribute(ApiAnnotationsAttribute.visibleTag);
			}
		});
	}

	private JApiChangeStatus evaluateChangeStatus(JApiChangeStatus changeStatus) {
		if (changeStatus == JApiChangeStatus.UNCHANGED) {
			if (this.staticModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.finalModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.accessModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.abstractModifier.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			if (this.syntheticAttribute.getChangeStatus() != JApiChangeStatus.UNCHANGED) {
				changeStatus = JApiChangeStatus.MODIFIED;
			}
			for (JApiException jApiException : exceptions) {
				if (jApiException.getChangeStatus() == JApiChangeStatus.NEW || jApiException.getChangeStatus() == JApiChangeStatus.REMOVED) {
					changeStatus = JApiChangeStatus.MODIFIED;
				}
			}
		}
		return changeStatus;
	}

	protected JApiAttribute<SyntheticAttribute> extractSyntheticAttribute(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		JApiAttribute<SyntheticAttribute> jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
		if (oldBehaviorOptional.isPresent() && newBehaviorOptional.isPresent()) {
			ApiBehavior oldBehavior = oldBehaviorOptional.get();
			ApiBehavior newBehavior = newBehaviorOptional.get();
			byte[] attributeOldBehavior = oldBehavior.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
			byte[] attributeNewBehavior = newBehavior.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
			if (attributeOldBehavior != null && attributeNewBehavior != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
			} else if (attributeOldBehavior != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.MODIFIED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
			} else if (attributeNewBehavior != null) {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.MODIFIED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.of(SyntheticAttribute.SYNTHETIC));
			} else {
				jApiAttribute = new JApiAttribute<>(JApiChangeStatus.UNCHANGED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
			}
		} else {
			if (oldBehaviorOptional.isPresent()) {
				ApiBehavior apiBehavior = oldBehaviorOptional.get();
				byte[] attribute = apiBehavior.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
				if (attribute != null) {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.REMOVED, Optional.of(SyntheticAttribute.SYNTHETIC), Optional.<SyntheticAttribute>absent());
				} else {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.REMOVED, Optional.of(SyntheticAttribute.NON_SYNTHETIC), Optional.<SyntheticAttribute>absent());
				}
			}
			if (newBehaviorOptional.isPresent()) {
				ApiBehavior apiBehavior = newBehaviorOptional.get();
				byte[] attribute = apiBehavior.getAttribute(Constants.JAVA_CONSTPOOL_ATTRIBUTE_SYNTHETIC);
				if (attribute != null) {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.NEW, Optional.<SyntheticAttribute>absent(), Optional.of(SyntheticAttribute.SYNTHETIC));
				} else {
					jApiAttribute = new JApiAttribute<>(JApiChangeStatus.NEW, Optional.<SyntheticAttribute>absent(), Optional.of(SyntheticAttribute.NON_SYNTHETIC));
				}
			}
		}
		return jApiAttribute;
	}

	public boolean hasSameParameter(JApiMethod method) {
		boolean hasSameParameter = true;
		List<JApiParameter> parameters1 = getParameters();
		List<JApiParameter> parameters2 = method.getParameters();
		if (parameters1.size() != parameters2.size()) {
			hasSameParameter = false;
		}
		if (hasSameParameter) {
			for (int i = 0; i < parameters1.size(); i++) {
				if (!parameters1.get(i).getType().equals(parameters2.get(i).getType())) {
					hasSameParameter = false;
				}
			}
		}
		return hasSameParameter;
	}

	private JApiModifier<StaticModifier> extractStaticModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<StaticModifier>() {
			@Override
			public StaticModifier getModifierForOld(ApiBehavior oldBehavior) {
				return Modifier.isStatic(oldBehavior.getModifiers()) ? StaticModifier.STATIC : StaticModifier.NON_STATIC;
			}

			@Override
			public StaticModifier getModifierForNew(ApiBehavior newBehavior) {
				return Modifier.isStatic(newBehavior.getModifiers()) ? StaticModifier.STATIC : StaticModifier.NON_STATIC;
			}
		});
	}

	private JApiModifier<FinalModifier> extractFinalModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<FinalModifier>() {
			@Override
			public FinalModifier getModifierForOld(ApiBehavior oldBehavior) {
				return Modifier.isFinal(oldBehavior.getModifiers()) ? FinalModifier.FINAL : FinalModifier.NON_FINAL;
			}

			@Override
			public FinalModifier getModifierForNew(ApiBehavior newBehavior) {
				return Modifier.isFinal(newBehavior.getModifiers()) ? FinalModifier.FINAL : FinalModifier.NON_FINAL;
			}
		});
	}

	private JApiModifier<AccessModifier> extractAccessModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<AccessModifier>() {
			@Override
			public AccessModifier getModifierForOld(ApiBehavior oldBehavior) {
				return ModifierHelper.translateToModifierLevel(oldBehavior.getModifiers());
			}

			@Override
			public AccessModifier getModifierForNew(ApiBehavior newBehavior) {
				return ModifierHelper.translateToModifierLevel(newBehavior.getModifiers());
			}
		});
	}

	private JApiModifier<AbstractModifier> extractAbstractModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<AbstractModifier>() {
			@Override
			public AbstractModifier getModifierForOld(ApiBehavior oldBehavior) {
				return Modifier.isAbstract(oldBehavior.getModifiers()) ? AbstractModifier.ABSTRACT : AbstractModifier.NON_ABSTRACT;
			}

			@Override
			public AbstractModifier getModifierForNew(ApiBehavior newBehavior) {
				return Modifier.isAbstract(newBehavior.getModifiers()) ? AbstractModifier.ABSTRACT : AbstractModifier.NON_ABSTRACT;
			}
		});
	}

	private JApiModifier<BridgeModifier> extractBridgeModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<BridgeModifier>() {
			@Override
			public BridgeModifier getModifierForOld(ApiBehavior oldBehavior) {
				return ModifierHelper.isBridge(oldBehavior.getModifiers()) ? BridgeModifier.BRIDGE : BridgeModifier.NON_BRIDGE;
			}

			@Override
			public BridgeModifier getModifierForNew(ApiBehavior newBehavior) {
				return ModifierHelper.isBridge(newBehavior.getModifiers()) ? BridgeModifier.BRIDGE : BridgeModifier.NON_BRIDGE;
			}
		});
	}

	private JApiModifier<SyntheticModifier> extractSyntheticModifier(Optional<? extends ApiBehavior> oldBehaviorOptional, Optional<? extends ApiBehavior> newBehaviorOptional) {
		return ModifierHelper.extractModifierFromBehavior(oldBehaviorOptional, newBehaviorOptional, new ModifierHelper.ExtractModifierFromBehaviorCallback<SyntheticModifier>() {
			@Override
			public SyntheticModifier getModifierForOld(ApiBehavior oldBehavior) {
				return ModifierHelper.isSynthetic(oldBehavior.getModifiers()) ? SyntheticModifier.SYNTHETIC : SyntheticModifier.NON_SYNTHETIC;
			}

			@Override
			public SyntheticModifier getModifierForNew(ApiBehavior newBehavior) {
				return ModifierHelper.isSynthetic(newBehavior.getModifiers()) ? SyntheticModifier.SYNTHETIC : SyntheticModifier.NON_SYNTHETIC;
			}
		});
	}

	@XmlElementWrapper(name = "modifiers")
	@XmlElement(name = "modifier")
	public List<? extends JApiModifier<? extends Enum<? extends Enum<?>>>> getModifiers() {
		return Arrays.asList(this.finalModifier, this.staticModifier, this.accessModifier, this.abstractModifier, this.bridgeModifier, this.syntheticModifier);
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlAttribute
	public JApiChangeStatus getChangeStatus() {
		return changeStatus;
	}

	@XmlElementWrapper(name = "parameters")
	@XmlElement(name = "parameter")
	public List<JApiParameter> getParameters() {
		return parameters;
	}

	public void addParameter(JApiParameter jApiParameter) {
		parameters.add(jApiParameter);
	}

	@XmlTransient
	public JApiModifier<AccessModifier> getAccessModifier() {
		return accessModifier;
	}

	@XmlTransient
	public JApiModifier<FinalModifier> getFinalModifier() {
		return finalModifier;
	}

	@XmlTransient
	public JApiModifier<StaticModifier> getStaticModifier() {
		return staticModifier;
	}

	public JApiModifier<AbstractModifier> getAbstractModifier() {
		return this.abstractModifier;
	}

	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	public List<JApiAttribute<? extends Enum<?>>> getAttributes() {
		List<JApiAttribute<? extends Enum<?>>> list = new ArrayList<>();
		list.add(this.syntheticAttribute);
		return list;
	}

	@XmlTransient
	public JApiModifier<BridgeModifier> getBridgeModifier() {
		return this.bridgeModifier;
	}

	@XmlTransient
	public JApiModifier<SyntheticModifier> getSyntheticModifier() {
		return this.syntheticModifier;
	}

	@XmlTransient
	public JApiAttribute<SyntheticAttribute> getSyntheticAttribute() {
		return syntheticAttribute;
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

	@Override
	public Optional<Integer> getOldLineNumber() {
		return this.oldLineNumber;
	}

	@Override
	public Optional<Integer> geNewLineNumber() {
		return this.newLineNumber;
	}

	@XmlAttribute(name = "oldLineNumber")
	public String getOldLineNumberAsString() {
		return OptionalHelper.optionalToString(this.oldLineNumber);
	}

	@XmlAttribute(name = "newLineNumber")
	public String getNewLineNumberAsString() {
		return OptionalHelper.optionalToString(this.newLineNumber);
	}

	@XmlElementWrapper(name = "exceptions")
	@XmlElement(name = "exception")
	public List<JApiException> getExceptions() {
		return exceptions;
	}

	@XmlTransient
	public JApiClass getjApiClass() {
		return this.jApiClass;
	}
}
