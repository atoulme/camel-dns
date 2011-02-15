/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.camel.component.dns;

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
 *         A set of test cases to make DNS lookups.
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
            assertTrue(t.getCause() instanceof IllegalArgumentException);
        }
        _resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testDNSWithEmptyNameHeader() throws Exception {
        _resultEndpoint.expectedMessageCount(0);
        try {
            _template.sendBodyAndHeader("hello", "dns.name", "");
        } catch (Throwable t) {
            t.printStackTrace();
            assertTrue(t.toString(), t.getCause() instanceof IllegalArgumentException);
        }
        _resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testDNSWithNameHeader() throws Exception {
        _resultEndpoint.expectedMessageCount(1);
        _resultEndpoint.expectedMessagesMatches(new Predicate() {
            public boolean matches(Exchange exchange) {
                Record[] record = (Record[]) exchange.getIn().getBody();
                return record[0].getName().toString()
                        .equals("www.example.com.");
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
                return record[0].getName().toString()
                        .equals("www.example.com.");
            }
        });
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("dns.name", "www.example.com");
        headers.put("dns.type", "A");
        _template.sendBodyAndHeaders("hello", headers);
        _resultEndpoint.assertIsSatisfied();
    }
}
