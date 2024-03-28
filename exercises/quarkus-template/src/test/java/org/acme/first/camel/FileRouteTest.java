/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.first.camel;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

/**
 * JVM mode tests.
 */
@QuarkusTest
public class FileRouteTest extends CamelQuarkusTestSupport {

    @Produce("direct:start")
    private ProducerTemplate startEp;

    @EndpointInject("mock:result")
    private MockEndpoint resultEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() throws Exception {

                from("direct:start")
                        .log("Got message: ${body}")
                        .to("file:target/messages/in");

                from("file:target/messages/out")
                        .to("mock:result");
            }
        };
    }

    @Test
    public void test_message_goes_from_in_to_out() throws InterruptedException {
        String msgIn = "Gepardec";
        String msgExpected = "Hello Gepardec!";

        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.expectedBodiesReceived(msgExpected);

        startEp.sendBody("direct:start", msgIn);
        resultEndpoint.assertIsSatisfied();
    }

}
