package japicmp.filter;

import com.criticollab.japicmp.classinfo.ApiBehavior;

public interface BehaviorFilter extends Filter {

	boolean matches(ApiBehavior apiBehavior);
}
