package org.pingpong.restjson;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
// importado a mano equalTo
import static org.hamcrest.Matchers.equalTo;

import javax.transaction.Transactional;


import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;


/**
 * Behavioral testing
 */


@QuarkusTest
@Transactional //hay que ponerlo siempre para que sea una transacción
public class FruitResourceTest {


    @Test
    public void testHelloEndpoint() {
        // si el content-type de la peticion es TEXT
        // responde el endpoint hello
        given() //dado contentipe
            .contentType(ContentType.TEXT) // porque en resource he puesto  @Consumes(MediaType.TEXT_PLAIN)
                .when().get("/fruits") //cundo haga un get de fruits
            .then()
                .statusCode(200) //quiero que el status code sea este
                .body(is("Holi caracoli :)")); //y el body este
    }

    @Test
    public void testListEndpoint() {
        // Si el content-type de la peticion es JSON      @Consumes(MediaType.APPLICATION_JSON)
        // responde el endpoint list
        // list() endpoint devuelve lista de maps [{}, {}]
        List<Map<String, Object>> products = 
            given()
                .contentType(ContentType.JSON)
                .when().get("/fruits")
                .as(new TypeRef<List<Map<String, Object>>>() {});

        //ESTO RULA PORQUE EN RESOURCES HAY UN SCRIPT DONDE HE AÑADIDO PROPIEDADES OJO,
        // Y ADEMÁS EN APPLICATION PROPERTIES ESTÁ DEFINIDO

        Assertions.assertThat(products).hasSize(2);
        Assertions.assertThat(products.get(0)).containsValue("Piña");
        Assertions.assertThat(products.get(1)).containsEntry("description", "Es rojita y pequeñita");
    }

    @Test
    @TestTransaction
    public void testList() {
        given()
            .contentType(ContentType.JSON)
            .when().get("/fruits/")
            .then()
                .statusCode(200)
                .body("$.size()", is(2),
                "name", containsInAnyOrder("Piña", "Cereza"),
                        "name", containsInAnyOrder("Cereza", "Piña"),
                //esto es redundante, no tiene sentido poner dos veces el mismo test, solo lo repito a mode de autoapuntes
                        //para recordarme que da igual el orden
                "description", containsInAnyOrder("Es grande y sabe bien", "Es rojita y pequeñita"));
    }

    @Test
    public void testAdd() {
        given()
            .body("{\"name\": \"Banana\", \"description\": \"Embrace monke :)\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                    .post("/fruits")
                    .then()
                        .statusCode(200)
                        .body("$.size()", is(3),
                              "name", containsInAnyOrder("Piña", "Cereza", "Banana"),
                              "description", containsInAnyOrder("Es grande y sabe bien", "Es rojita y pequeñita", "Embrace monke :)"));
        
        given()

                .contentType(ContentType.TEXT) // porque en resource he puesto  @Consumes(MediaType.TEXT_PLAIN)
                .when()
                .when()
                    .delete("/fruits/banana")
                    .then()
                        .statusCode(200)
                        .body("$.size()", is(2),
                              "name", containsInAnyOrder("Piña", "Cereza"),
                              "description", containsInAnyOrder("Es grande y sabe bien", "Es rojita y pequeñita"));
    }

    @Test
    public void getTest() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/fruits/piña")

        .then()
            .contentType(ContentType.JSON)
            .body("name", equalTo("Piña"));

        // no fruit
        given()
                .contentType(ContentType.JSON)
        .when()
            .get("/fruits/mangofango")
        .then()
            .statusCode(404);
    }
}