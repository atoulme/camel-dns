/**
 * 
 */
package org.apache.camel.dns;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
 * @author Antoine Toulme
 * 
 * A series of tests to check the IP lookup operation.
 *
 */
public class DNSIPEndpointTest extends CamelSpringTestSupport {

	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("IPCheck.xml");
	}
	
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint _resultEndpoint;
	
	@Produce(uri = "direct:start")
    protected ProducerTemplate _template;
	
	@Test
	public void testNullIPRequests() throws Exception {
		_resultEndpoint.expectedMessageCount(0);
		try {
		_template.sendBodyAndHeader("hello", "dns.domain", null);
		} catch (Throwable t) {
			assert(t.getCause() instanceof IllegalArgumentException);
		}
		_resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void testEmptyIPRequests() throws Exception {
		_resultEndpoint.expectedMessageCount(0);
		try {
		_template.sendBodyAndHeader("hello", "dns.domain", "");
		} catch (Throwable t) {
			assert(t.getCause() instanceof IllegalArgumentException);
		}
		_resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void testValidIPRequests() throws Exception {
		_resultEndpoint.expectedMessageCount(1);

		_resultEndpoint.expectedBodiesReceived("192.0.32.10");
		
		_template.sendBodyAndHeader("hello", "dns.domain", "www.example.com");
		_resultEndpoint.assertIsSatisfied();
	}
}
