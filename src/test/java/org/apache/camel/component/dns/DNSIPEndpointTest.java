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

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Antoine Toulme
 * 
 *         A series of tests to check the IP lookup operation.
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
            assertTrue(t.getCause() instanceof IllegalArgumentException);
        }
        _resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testEmptyIPRequests() throws Exception {
        _resultEndpoint.expectedMessageCount(0);
        try {
            _template.sendBodyAndHeader("hello", "dns.domain", "");
        } catch (Throwable t) {
            assertTrue(t.getCause() instanceof IllegalArgumentException);
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
