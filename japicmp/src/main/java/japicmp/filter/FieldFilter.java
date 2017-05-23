package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiField;

public interface FieldFilter extends Filter {

	boolean matches(ApiField apiField);
}
