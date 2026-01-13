package com.shopping.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.MongoDBContainer;

import java.math.BigDecimal;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServicesApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

    @LocalServerPort
    private Integer port;
    @BeforeEach
    void setUp(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=port;
    }

    static{
        mongoDBContainer.start();
    }

	@Test
	void shouldCreateProduct() {
        String requestbody= """
                {
                	"name":"Iphone",
                    "description":"by apple",
                    "price":10000
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestbody)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name",Matchers.equalTo("Iphone"))
                .body("description",Matchers.equalTo("by apple"))
                .body("price", Matchers.comparesEqualTo(10000));




    }

}
