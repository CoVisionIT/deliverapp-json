package be.covisionit.deliverapp;

import io.atomicbits.dsl.javajackson.json.Json;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonParserTest {

    @Test
    public void testParseExample() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String jsonString = IOUtils.resourceToString("json/MyDespatch.json", StandardCharsets.UTF_8, classLoader);

        DespatchAdviceDocument despatchAdviceDocument =
                Json.parseBodyToObject(jsonString, DespatchAdviceDocument.class.getCanonicalName());

        DespatchAdvice da = despatchAdviceDocument.getDespatchAdvice();
        assertEquals("07529e54-1f87-4ffb-97da-9dc286a567e8", da.getID());

        assertEquals(da.getOrderReference().getID(), "oREF");

        Party supplier = da.getDespatchSupplierParty().getParty();
        assertEquals("supplier id", supplier.getPartyIdentification().getID());
        assertEquals("supplier name", supplier.getPartyName().getName());
        assertEquals("supplier mail", supplier.getContact().getElectronicMail());

        Party customer = da.getDeliveryCustomerParty().getParty();
        assertEquals("customer id", customer.getPartyIdentification().getID());
        assertEquals("customer name", customer.getPartyName().getName());


        Shipment shipment = da.getShipment();
        assertEquals("delivery note number", shipment.getID());

        Delivery delivery = shipment.getDelivery();
        Despatch despatch = delivery.getDespatch();
        assertEquals("2018-10-15", despatch.getActualDespatchDate());
        assertEquals("17:50:41", despatch.getActualDespatchTime());
        Address despatchAddress = despatch.getDespatchAddress();
        assertEquals("site reference", despatchAddress.getID());
        assertEquals("Schelle", despatchAddress.getCityName());
        assertEquals("2627", despatchAddress.getPostalZone());
        assertEquals("BE", despatchAddress.getCountry().getIdentificationCode());

        Location deliveryLocation = delivery.getDeliveryLocation();
        LocationCoordinate locationCoordinate = deliveryLocation.getLocationCoordinate();
        assertEquals(50.894824, locationCoordinate.getLatitudeDegreesMeasure(), 0.0001);
        assertEquals(4.341777, locationCoordinate.getLongitudeDegreesMeasure(), 0.0001);

        DespatchLine despatchLine = da.getDespatchLine().get(0);
        assertEquals("1", despatchLine.getID());
        assertEquals("Not OK Line", despatchLine.getStatus().getRemarks());
        assertEquals(DespatchLineStatusName.REMARKS, despatchLine.getStatus().getName());
        assertEquals("1", despatchLine.getNote());
        assertEquals(1, despatchLine.getDeliveredQuantity(), 0);
        assertEquals("EA", despatchLine.getDeliveredQuantityUnitCode());
        assertEquals("d", despatchLine.getItem().getName());

        DespatchLine despatchLineTime = da.getDespatchLine().get(1);
        assertEquals("work", despatchLineTime.getID());
        assertEquals(5, despatchLineTime.getDeliveredQuantity(), 0);
        assertEquals("HUR", despatchLineTime.getDeliveredQuantityUnitCode());
        assertEquals("crane", despatchLineTime.getItem().getName());
        assertEquals("2019-12-06", despatchLineTime.getItem().getManufactureDate());

        switch (da.getStatus().getName()) {
            case ACCEPTED:
                Assert.fail("expected REMARKS");
                break;
            case REFUSED:
                Assert.fail("expected REMARKS");
                break;
            case REMARKS:
                assertEquals("Not OK", da.getStatus().getRemarks());
                break;
        }

        List<AdditionalDocument> additionalDocuments = da.getAdditionalDocuments();
        assertEquals(1, additionalDocuments.size());
        AdditionalDocument additionalDocument = additionalDocuments.get(0);
        assertEquals("image/jpeg", additionalDocument.getBinaryMimeCode());
        byte[] imageBytes = Base64.getDecoder().decode(additionalDocument.getBinaryContent());

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        assertEquals(25, image.getWidth());

    }
}
