package japicmp.filter;

import com.criticollab.japicmp.classinfo.api.ApiBehavior;
import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.google.common.base.Splitter;
import japicmp.exception.JApiCmpException;
import japicmp.util.SignatureParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JavadocLikeBehaviorFilter implements BehaviorFilter {
	private final String filterString;
	private final Pattern classPattern;
	private final Pattern methodPattern;
	private final List<Pattern> parameterPatterns = new ArrayList<>();

	public JavadocLikeBehaviorFilter(String filterString) {
		this.filterString = filterString;
		String regEx = filterString.replace(".", "\\.");
		regEx = regEx.replace("*", ".*");
		regEx = regEx.replace("$", "\\$");
		String[] parts = regEx.split("#");
		if (parts.length != 2) {
			throw new JApiCmpException(JApiCmpException.Reason.CliError, "Filter option '" + filterString + "' should not contain more than one '#'.");
		}
		classPattern = Pattern.compile(parts[0]);
		String methodPart = parts[1];
		int indexOpeningBracket = methodPart.indexOf('(');
		int indexClosingBracket = methodPart.indexOf(')');
		if (indexOpeningBracket == -1) {
			throw new JApiCmpException(JApiCmpException.Reason.CliError, "Filter option '" + filterString + "' should contain one opening '('.");
		}
		if (indexClosingBracket == -1) {
			throw new JApiCmpException(JApiCmpException.Reason.CliError, "Filter option '" + filterString + "' should contain one opening ')'.");
		}
		if (indexClosingBracket <= indexOpeningBracket) {
			throw new JApiCmpException(JApiCmpException.Reason.CliError, "Filter option '" + filterString + "' should contain first an opening '(' and then a closing ')'.");
		}
		if (indexOpeningBracket == 0) {
			throw new JApiCmpException(JApiCmpException.Reason.CliError, "Filter option '" + filterString + "' should contain at least one character before the opening '('.");
		}
		String methodName = methodPart.substring(0, indexOpeningBracket);
		methodPattern = Pattern.compile(methodName);
		String paramList = methodPart.substring(indexOpeningBracket + 1, indexClosingBracket);
		if (paramList.length() > 0) {
			for (String param : Splitter.on(",").trimResults().omitEmptyStrings().split(paramList)) {
				param = param.replaceAll("\\s+", "");
				parameterPatterns.add(Pattern.compile(param));
			}
		}
	}

	@Override
	public boolean matches(ApiBehavior apiBehavior) {
		boolean classMatches = true;
		boolean methodMatches = true;
		boolean parameterMatches = true;
		ClassApiSignature declaringClass = apiBehavior.getDeclaringClass();
		String name = declaringClass.getName();
		if (!classPattern.matcher(name).matches()) {
			classMatches = false;
		}
		String methodName = apiBehavior.getName();
		if (!methodPattern.matcher(methodName).matches()) {
			methodMatches = false;
		}
		SignatureParser signatureParser = new SignatureParser();
		signatureParser.parse(apiBehavior.getSignature());
		List<String> parameters = signatureParser.getParameters();
		if (parameters.size() != parameterPatterns.size()) {
			parameterMatches = false;
		} else {
			for (int i = 0; i < parameters.size(); i++) {
				Pattern pattern = parameterPatterns.get(i);
				if (!pattern.matcher(parameters.get(i)).matches()) {
					parameterMatches = false;
				}
			}
		}
		return (classMatches && methodMatches && parameterMatches);
	}

	@Override
	public String toString() {
		return this.filterString;
	}
}
