package cocome.cloud.sa.serviceprovider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.kit.ipd.java.utils.xml.Marshable;

@XmlRootElement(name = "ServiceProviderCatalog")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceProviderCatalog", propOrder = { "listServiceProvider" })
public class ServiceProviderCatalog implements Marshable {

	@XmlElementWrapper(name = "ServiceProviders")
	@XmlElement(name = "ServiceProvider")
	private List<ServiceProvider> listServiceProvider = new ArrayList<>();

	public ServiceProviderCatalog() {}

	public void setListServiceProvider(final List<ServiceProvider> listServiceProvider) {
		this.listServiceProvider = listServiceProvider;
	}

	public List<ServiceProvider> getListServiceProvider() {
		return this.listServiceProvider;
	}

	@Override
	public Class<?> getObjectFactory() {
		return ServiceProviderObjectFactory.class;
	}

}
