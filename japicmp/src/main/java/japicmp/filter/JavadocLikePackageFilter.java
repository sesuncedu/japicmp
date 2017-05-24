package japicmp.filter;


import com.criticollab.japicmp.classinfo.api.ClassApiSignature;

import java.util.regex.Pattern;

public class JavadocLikePackageFilter implements ClassFilter {
	private final Pattern pattern;
	private final String packageName;

	public JavadocLikePackageFilter(String packageName) {
		this.packageName = packageName;
		String regEx = packageName.replace(".", "\\.");
		regEx = regEx.replace("*", ".*");
		regEx = regEx + "(\\.[^\\.]+)*";
		pattern = Pattern.compile(regEx);
	}

	@Override
	public String toString() {
		return this.packageName;
	}

	@Override
	public boolean matches(ClassApiSignature classApiSignature) {
		String name = classApiSignature.getPackageName();
		name = name == null ? "" : name;
		return pattern.matcher(name).matches();
	}
}
