package japicmp.model;

import com.criticollab.japicmp.classinfo.api.ApiConstructor;
import com.google.common.base.Optional;
import japicmp.cmp.JarArchiveComparator;

import javax.xml.bind.annotation.XmlTransient;

public class JApiConstructor extends JApiBehavior {
	private final Optional<ApiConstructor> oldConstructor;
	private final Optional<ApiConstructor> newConstructor;

	public JApiConstructor(JApiClass jApiClass, String name, JApiChangeStatus changeStatus, Optional<ApiConstructor> oldConstructor, Optional<ApiConstructor> newConstructor, JarArchiveComparator jarArchiveComparator) {
		super(jApiClass, name, oldConstructor, newConstructor, changeStatus, jarArchiveComparator);
		this.oldConstructor = oldConstructor;
		this.newConstructor = newConstructor;
	}

	@XmlTransient
	public Optional<ApiConstructor> getNewConstructor() {
		return newConstructor;
	}

	@XmlTransient
	public Optional<ApiConstructor> getOldConstructor() {
		return oldConstructor;
	}
}
