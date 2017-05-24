package japicmp.filter;

import com.criticollab.japicmp.classinfo.api.ApiBehavior;

public interface BehaviorFilter extends Filter {

	boolean matches(ApiBehavior apiBehavior);
}
