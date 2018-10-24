package be.covisionit.deliverapp;

import io.atomicbits.DeliverAppEndpoint;
import io.atomicbits.dsl.javajackson.Response;
import io.atomicbits.dsl.javajackson.client.ClientConfig;
import io.atomicbits.dsl.javajackson.json.Json;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class EndpointTest {

    private static final String HOST = "localhost";

    @Test
    @Ignore("Test ignored, will not work without the endpoint service running")
    public void testEchoService() throws IOException, ExecutionException, InterruptedException {
        DeliverAppEndpoint endpoint = getEndpoint();

        DespatchAdviceDocument despatchAdviceDocument = getDespatchAdviceDocument();

        Response<String> res = endpoint.rest.despatchadvice.post(despatchAdviceDocument).get();
        Assert.assertEquals(res.getStringBody(), 200, res.getStatus());

        Assert.assertEquals(despatchAdviceDocument.getDespatchAdvice().getID(), res.getStringBody());
    }

    private DeliverAppEndpoint getEndpoint() {
        return new DeliverAppEndpoint(
                HOST,
                80,
                "http",
                "",
                new ClientConfig(),
                Collections.emptyMap()
        );
    }

    private DespatchAdviceDocument getDespatchAdviceDocument() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonString = IOUtils.resourceToString("json/MyDespatch.json", StandardCharsets.UTF_8, classLoader);

        return Json.parseBodyToObject(jsonString, "be.covisionit.deliverapp.DespatchAdviceDocument");
    }
}
