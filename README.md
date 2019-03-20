# DeliverApp-json

#### Get the library
Add the deliverapp-json repository and the deliverapp-json dependency:

    <repositories>
        <repository>
            <id>deliverapp-json</id>
            <url>https://raw.githubusercontent.com/CoVisionIT/deliverapp-json/master/artifacts</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>be.covisionit</groupId>
        <artifactId>deliverapp-json</artifactId>
        <version>1.0.3</version>
    </dependency>

Alternatively copy the [JSON schema files](https://github.com/CoVisionIT/deliverapp-json/tree/master/src/main/resources/model) 
to your project and configure [Scraml](https://github.com/atomicbits/scraml) to generate the library at compile time.

#### Parse a despatch advice


    import be.covisionit.deliverapp.DespatchAdviceDocument;
    import io.atomicbits.dsl.javajackson.json.Json;

    String jsonString = "{\"DespatchAdvice\":{\"ID\":\"123456\"}}";
    DespatchAdviceDocument daDoc = Json.parseBodyToObject(jsonString, DespatchAdviceDocument.class.getCanonicalName());
