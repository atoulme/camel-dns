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
import org.xbill.DNS.Record;

/**
 * @author Antoine Toulme
 * 
 * A set of test cases to make DNS lookups.
 *
 */
public class DNSLookupEndpointTest extends CamelSpringTestSupport {

	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("DNSLookup.xml");
	}
	
	@EndpointInject(uri = "mock:result")
    protected MockEndpoint _resultEndpoint;
	
	@Produce(uri = "direct:start")
    protected ProducerTemplate _template;
	
	@Test
	public void testDNSWithNoHeaders() throws Exception {
		_resultEndpoint.expectedMessageCount(0);
		try {
		_template.sendBody("hello");
		} catch (Throwable t) {
			assert(t.getCause() instanceof IllegalArgumentException);
		}
		_resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void testDNSWithEmptyNameHeader() throws Exception {
		_resultEndpoint.expectedMessageCount(0);
		try {
		_template.sendBodyAndHeader("hello", "dns.name", "");
		} catch (Throwable t) {
			assert(t.getCause() instanceof IllegalArgumentException);
		}
		_resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void testDNSWithNameHeader() throws Exception {
		_resultEndpoint.expectedMessageCount(1);
		_resultEndpoint.expectedMessagesMatches(new Predicate() {
			public boolean matches(Exchange exchange) {
				Record[] record = (Record[]) exchange.getIn().getBody();
				return record[0].getName().toString().equals("www.example.com.");
			}
		});
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("dns.name", "www.example.com");
		_template.sendBodyAndHeaders("hello", headers);
		_resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void testDNSWithNameHeaderAndType() throws Exception {
		_resultEndpoint.expectedMessageCount(1);
		_resultEndpoint.expectedMessagesMatches(new Predicate() {
			public boolean matches(Exchange exchange) {
				Record[] record = (Record[]) exchange.getIn().getBody();
				System.err.println(record[0]);
				return record[0].getName().toString().equals("www.example.com.");
			}
		});
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("dns.name", "monkey.wp.dg.cx");
		headers.put("dns.type", "TXT");
		_template.sendBodyAndHeaders("hello", headers);
		_resultEndpoint.assertIsSatisfied();
	}
}
