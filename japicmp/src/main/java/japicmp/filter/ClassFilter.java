package japicmp.filter;


import com.criticollab.japicmp.classinfo.ClassApiSignature;

public interface ClassFilter extends Filter {

	boolean matches(ClassApiSignature classApiSignature);
}
