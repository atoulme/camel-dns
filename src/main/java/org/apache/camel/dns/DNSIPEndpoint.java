/**
 * 
 */
package org.apache.camel.dns;

import java.net.InetAddress;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultProducer;
import org.xbill.DNS.Address;

/**
 * @author Antoine Toulme
 * 
 * An endpoint to conduct IP address lookup from the host name.
 *
 */
public class DNSIPEndpoint extends DefaultEndpoint {

	public DNSIPEndpoint(CamelContext context) {
		super("dns://ip", context);
	}

	public Consumer createConsumer(Processor arg0) throws Exception {
		throw new UnsupportedOperationException();
	}

	public Producer createProducer() throws Exception {
		return new DefaultProducer(this) {

			public void process(Exchange exchange) throws Exception {
				Object domain = exchange.getIn().getHeader("dns.domain");
				if (!(domain instanceof String) || 
						((String) domain).length() == 0) {
					throw new IllegalArgumentException(
							"Invalid or null domain :" + domain);
				}
				InetAddress address = Address.getByName((String) domain);
				exchange.getOut().setBody(address.getHostAddress());
			}
		};
	}

	public boolean isSingleton() {
		return false;
	}

}
