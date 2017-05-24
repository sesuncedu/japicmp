package japicmp.filter;


import com.criticollab.japicmp.classinfo.api.ClassApiSignature;

public interface ClassFilter extends Filter {

	boolean matches(ClassApiSignature classApiSignature);
}
