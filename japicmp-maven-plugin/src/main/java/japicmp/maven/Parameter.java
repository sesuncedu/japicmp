package japicmp.maven;

import java.util.List;

public class Parameter {
	private String accessModifier;
	private List<String> includes;
	private List<String> excludes;
	private String onlyBinaryIncompatible;
	private String onlyModified;
	private String breakBuildOnModifications;
	private String breakBuildOnBinaryIncompatibleModifications;
	private String breakBuildOnSourceIncompatibleModifications;
	private String breakBuildBasedOnSemanticVersioning;
	private String includeSynthetic;
	private String ignoreMissingClasses;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private List<String> ignoreMissingClassesByRegularExpressions;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String skipPomModules;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String htmlStylesheet;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String htmlTitle;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String noAnnotations;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String ignoreNonResolvableArtifacts;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private List<String> packagingSupporteds;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String postAnalysisScript;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String skipHtmlReport;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String skipXmlReport;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private boolean skipDiffReport;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String ignoreMissingOldVersion;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private String oldVersionPattern;
	@org.apache.maven.plugins.annotations.Parameter(required = false)
	private boolean breakBuildIfCausedByExclusion = true;

	public String getNoAnnotations() {
		return noAnnotations;
	}

	public void setNoAnnotations(String noAnnotations) {
		this.noAnnotations = noAnnotations;
	}

	public String getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}

	public String getOnlyModified() {
		return onlyModified;
	}

	public void setOnlyModified(String onlyModified) {
		this.onlyModified = onlyModified;
	}

	public String getOnlyBinaryIncompatible() {
		return onlyBinaryIncompatible;
	}

	public void setOnlyBinaryIncompatible(String onlyBinaryIncompatible) {
		this.onlyBinaryIncompatible = onlyBinaryIncompatible;
	}

	public String getBreakBuildOnModifications() {
		return breakBuildOnModifications;
	}

	public void setBreakBuildOnModifications(String breakBuildOnModifications) {
		this.breakBuildOnModifications = breakBuildOnModifications;
	}

	public String getBreakBuildOnBinaryIncompatibleModifications() {
		return breakBuildOnBinaryIncompatibleModifications;
	}

	public void setBreakBuildOnBinaryIncompatibleModifications(String breakBuildOnBinaryIncompatibleModifications) {
		this.breakBuildOnBinaryIncompatibleModifications = breakBuildOnBinaryIncompatibleModifications;
	}

	public String getIncludeSynthetic() {
		return includeSynthetic;
	}

	public void setIncludeSynthetic(String includeSynthetic) {
		this.includeSynthetic = includeSynthetic;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public String getIgnoreMissingClasses() {
		return ignoreMissingClasses;
	}

	public void setIgnoreMissingClasses(String ignoreMissingClasses) {
		this.ignoreMissingClasses = ignoreMissingClasses;
	}

	public String getSkipPomModules() {
		return skipPomModules;
	}

	public void setSkipPomModules(String skipPomModules) {
		this.skipPomModules = skipPomModules;
	}

	public String getHtmlStylesheet() {
		return htmlStylesheet;
	}

	public void setHtmlStylesheet(String htmlStylesheet) {
		this.htmlStylesheet = htmlStylesheet;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}

	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}

	public String getIgnoreNonResolvableArtifacts() {
		return ignoreNonResolvableArtifacts;
	}

	public void setIgnoreNonResolvableArtifacts(String ignoreNonResolvableArtifacts) {
		this.ignoreNonResolvableArtifacts = ignoreNonResolvableArtifacts;
	}

	public List<String> getPackagingSupporteds() {
		return packagingSupporteds;
	}

	public void setPackagingSupporteds(List<String> packagingSupporteds) {
		this.packagingSupporteds = packagingSupporteds;
	}

	public String getBreakBuildOnSourceIncompatibleModifications() {
		return breakBuildOnSourceIncompatibleModifications;
	}

	public void setBreakBuildOnSourceIncompatibleModifications(String breakBuildOnSourceIncompatibleModifications) {
		this.breakBuildOnSourceIncompatibleModifications = breakBuildOnSourceIncompatibleModifications;
	}

	public String getBreakBuildBasedOnSemanticVersioning() {
		return breakBuildBasedOnSemanticVersioning;
	}

	public void setBreakBuildBasedOnSemanticVersioning(String breakBuildBasedOnSemanticVersioning) {
		this.breakBuildBasedOnSemanticVersioning = breakBuildBasedOnSemanticVersioning;
	}

	public String getPostAnalysisScript() {
		return postAnalysisScript;
	}

	public void setPostAnalysisScript(String postAnalysisScript) {
		this.postAnalysisScript = postAnalysisScript;
	}

	public String getSkipHtmlReport() {
		return skipHtmlReport;
	}

	public void setSkipHtmlReport(String skipHtmlReport) {
		this.skipHtmlReport = skipHtmlReport;
	}

	public String getSkipXmlReport() {
		return skipXmlReport;
	}

	public void setSkipXmlReport(String skipXmlReport) {
		this.skipXmlReport = skipXmlReport;
	}

	public boolean isSkipDiffReport() {
		return skipDiffReport;
	}

	public void setSkipDiffReport(boolean skipDiffReport) {
		this.skipDiffReport = skipDiffReport;
	}

	public String getIgnoreMissingOldVersion() {
		return ignoreMissingOldVersion;
	}

	public void setIgnoreMissingOldVersion(String ignoreMissingOldVersion) {
		this.ignoreMissingOldVersion = ignoreMissingOldVersion;
	}

	public String getOldVersionPattern() {
		return oldVersionPattern;
	}

	public void setOldVersionPattern(String oldVersionPattern) {
		this.oldVersionPattern = oldVersionPattern;
	}

	public boolean isBreakBuildIfCausedByExclusion() {
		return breakBuildIfCausedByExclusion;
	}

	public void setBreakBuildIfCausedByExclusion(boolean breakBuildIfCausedByExclusion) {
		this.breakBuildIfCausedByExclusion = breakBuildIfCausedByExclusion;
	}

	public List<String> getIgnoreMissingClassesByRegularExpressions() {
		return ignoreMissingClassesByRegularExpressions;
	}

	public void setIgnoreMissingClassesByRegularExpressions(List<String> ignoreMissingClassesByRegularExpressions) {
		this.ignoreMissingClassesByRegularExpressions = ignoreMissingClassesByRegularExpressions;
	}
}
