/**
 * 
 */
package org.apache.camel.dns;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Antoine Toulme
 * 
 * Tests for the dig endpoint.
 *
 */
public class DNSDigEndpointTest extends CamelSpringTestSupport {

	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("DNSDig.xml");
	}
	
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint _resultEndpoint;
	
	@Produce(uri = "direct:start")
    protected ProducerTemplate _template;
	
	@Test
	public void testDigForMonkey() throws Exception {
		_resultEndpoint.expectedMessageCount(1);
		_resultEndpoint.expectedMessagesMatches(new Predicate() {
			public boolean matches(Exchange exchange) {
				System.err.println(exchange.getIn().getBody());
				throw new IllegalArgumentException("TODO make sure this works");
			}
		});
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("dns.name", "monkey.wp.dg.cx");
		headers.put("dns.type", "TXT");
		_template.sendBodyAndHeaders("hello", headers);
		_resultEndpoint.assertIsSatisfied();
	}

}
