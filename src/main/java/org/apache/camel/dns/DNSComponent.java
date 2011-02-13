/**
 * 
 */
package org.apache.camel.dns;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * @author Antoine Toulme
 * 
 * The DNS components creates endpoints of the form:
 * <br/>
 * dns:///...
 * <br/>
 * At this point, the DNS component works with these operations:<br/>
 * <p>
 * dns:///ip
 * <br/>
 * 
 * This will return the IP address associated with the domain 
 * passed in the header dns.domain.
 * </p>
 * <p>
 * dns:///lookup
 * This endpoint accepts three parameters.
 * <ul>
 *   <li>dns.name: the lookup name. Usually the domain. Mandatory.</li>
 *   <li>dns.type: the type of the lookup. Should match the values of 
 *   {@see org.xbill.dns.Type}. Optional.</li>
 *   <li>dns.class: the DNS class of the lookup. Should match the values of 
 *   {@see org.xbill.dns.DClass}. Optional.</li>
 * </ul>
 * </p>
 * 
 */
public class DNSComponent extends DefaultComponent {

	private static final String OPERATION_IP = "ip";
	
	private static final String OPERATION_LOOKUP = "lookup";
	
	protected Endpoint createEndpoint(String uri, String remaining,
			Map<String, Object> parameters) throws Exception {
		if (OPERATION_IP.equals(remaining)) {
			return new DNSIPEndpoint(getCamelContext());
		} else if (OPERATION_LOOKUP.equals(remaining)) {
			return new DNSLookupEndpoint(getCamelContext());
		} else {
			throw new IllegalArgumentException(uri + 
					"is unsupported by the DNS component");
		}
	}

}