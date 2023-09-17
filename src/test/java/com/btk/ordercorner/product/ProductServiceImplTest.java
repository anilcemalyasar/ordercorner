package com.btk.ordercorner.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductServiceImplTest {
    
    @Autowired
    private TestRestTemplate template;


    @Test
    void retrieve_all_products() throws JSONException {

        String url = "http://localhost:8080/api/products/all";

        ResponseEntity<String> responseEntity = template.getForEntity(url, String.class);

        String expected = """
            [
                {
                    "productId": 5,
                    "productName": "Pantolon",
                    "productDescription": "Pantolon",
                    "productPrice": 800.0,
                    "stockAmount": 5,
                    "categoryId": 2
                },
                {
                    "productId": 3,
                    "productName": "Telefon",
                    "productDescription": "Telefon",
                    "productPrice": 10000.0,
                    "stockAmount": 9,
                    "categoryId": 1
                },
                {
                    "productId": 1,
                    "productName": "Bilgisayar",
                    "productDescription": "Bilgisayar",
                    "productPrice": 1000.0,
                    "stockAmount": 6,
                    "categoryId": 1
                },
                {
                    "productId": 4,
                    "productName": "Gömlek",
                    "productDescription": "Gömlek",
                    "productPrice": 500.0,
                    "stockAmount": 0,
                    "categoryId": 2
                },
                {
                    "productId": 2,
                    "productName": "Televizyon",
                    "productDescription": "Televizyon",
                    "productPrice": 5000.0,
                    "stockAmount": 4,
                    "categoryId": 1
                },
                {
                    "productId": 6,
                    "productName": "Iphone 14",
                    "productDescription": "Apple firmasının son çıkardığı telefon modeli",
                    "productPrice": 10000.0,
                    "stockAmount": 5,
                    "categoryId": 1
                }
            ]
                """;

        // burada stricti true yapmamın sebebi her field'ın eşleşip eşleşmediğini 
        // kontrol etmesini istiyorum
        JSONAssert.assertEquals(expected, responseEntity.getBody(), true);
        
    }

    @Test
    void retrieve_specific_product_by_id() throws JSONException {

        String url = "http://localhost:8080/api/products/5";

        ResponseEntity<String> responseEntity = template.getForEntity(url, String.class);

        String expected = """
            {
                "productId": 5,
                "productName": "Pantolon",
                "productDescription": "Pantolon",
                "productPrice": 800.0,
                "stockAmount": 5,
                "categoryId": 2
            }
                """;

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expected, responseEntity.getBody(), true);
    }

    @Test
    void add_new_product_and_check_if_database_updated() throws JSONException {
        String url = "http://localhost:8080/api/products/";

        String requestBody = """
            {
                "productName": "Yarım Boğazlı Kazak",
                "productDescription": "Kışın giyilebilecek klas bir ürün",
                "productPrice": 650,
                "stockAmount": 10,
                "categoryId": 2
            }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<String> responseEntity = 
                            template.exchange(url, HttpMethod.POST, httpEntity, String.class);
        
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        
        String checkUrl = "http://localhost:8080/api/products/product/search/Yarım Boğazlı Kazak";

        ResponseEntity<String> responseEntity2 = 
                                template.getForEntity(checkUrl, String.class);
        
        assertTrue(responseEntity2.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals(requestBody, responseEntity2.getBody(), false);
    }

}
