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

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * 
 *         The DNS components creates endpoints of the form: <br/>
 *         dns:///... <br/>
 *         At this point, the DNS component works with these operations:<br/>
 *         <p>
 *         dns:///ip <br/>
 * 
 *         This will return the IP address associated with the domain passed in
 *         the header dns.domain.
 *         </p>
 *         <p>
 *         dns:///lookup This endpoint accepts three parameters.
 *         <ul>
 *         <li>dns.name: the lookup name. Usually the domain. Mandatory.</li>
 *         <li>dns.type: the type of the lookup. Should match the values of
 *         {@see org.xbill.dns.Type}. Optional.</li>
 *         <li>dns.class: the DNS class of the lookup. Should match the values
 *         of {@see org.xbill.dns.DClass}. Optional.</li>
 *         </ul>
 *         </p>
 * 
 *         <p>
 *         dns:///dig This endpoint takes a few parameters, most of them
 *         optional :
 *         <ul>
 *         <li>dns.server: the server in particular for the query. If none is
 *         given, the default one specified by the OS will be used.</li>
 *         <li>dns.query: the query itself. Mandatory.</li>
 *         <li>dns.type: the type of the lookup. Should match the values of
 *         {@see org.xbill.dns.Type}. Optional.</li>
 *         <li>dns.class: the DNS class of the lookup. Should match the values
 *         of {@see org.xbill.dns.DClass}. Optional.</li>
 *         </ul>
 * 
 *         </p>
 *         
 *         <p>
 *         dns:///wikipedia This endpoint takes one paramter :
 *         <ul>
 *         <li>term: the search term on wikipedia</li>
 *         </ul>
 * 
 *         </p>
 * 
 */
public class DNSComponent extends DefaultComponent {

    private static final String OPERATION_IP = "ip";

    private static final String OPERATION_LOOKUP = "lookup";

    private static final String OPERATION_DIG = "dig";
    
    private static final String OPERATION_WIKIPEDIA = "wikipedia";

    protected Endpoint createEndpoint(String uri, String remaining,
            Map<String, Object> parameters) throws Exception {
        if (OPERATION_IP.equals(remaining)) {
            return new DNSIPEndpoint(getCamelContext());
        } else if (OPERATION_LOOKUP.equals(remaining)) {
            return new DNSLookupEndpoint(getCamelContext());
        } else if (OPERATION_DIG.equals(remaining)) {
            return new DNSDigEndpoint(getCamelContext());
        } else if (OPERATION_WIKIPEDIA.equals(remaining)) {
            return new WikipediaEndpoint(getCamelContext());
        }else {
            throw new IllegalArgumentException(uri
                    + "is unsupported by the DNS component");
        }
    }

}
