package org.pingpong.restjson;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Component Unit testing
 */

@QuarkusTest
@Transactional
public class ServiceFruitTest {

    @Inject
    ServiceFruit service;

    // @Test de jupiter, no el de junit
    @Test
    public void testList() {
        Assertions.assertThat(service.list()).hasSize(2);

    } //la cereza y la piña

    @Test
    public void containsTest() {
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equals("Piña"))).isTrue(); //testing Fruit Class
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equals("Cereza"))).isTrue();
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getDescription().equals("Es rojita y pequeñita"))).isTrue();
    }
    
    @Test
    public void addTest() {
        service.add(new Fruit("Banana", "And an attached Gorilla"));
        Assertions.assertThat(service.list()).hasSize(3);
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equals("Banana"))).isTrue();

        // handmade rollback gracias al antipatron ActiveRecord ;)
        Fruit fruit = Fruit.find("name", "Banana").firstResult();
        fruit.delete();
        Assertions.assertThat(Fruit.count()).isEqualTo(2);
    }
    @Test
    public void removeTest(){
        service.remove("Piña"); //quitas piña y testeas con un stream que no exista
        Assertions.assertThat(service.list()).hasSize(1);
        Assertions.assertThat(service.list().stream().anyMatch(f -> f.getName().equals("Piña"))).isFalse();

        // handmade rollback gracias al antipatron ActiveRecord ;)
        Fruit.persist(new Fruit("Piña", "Es grande y sabe bien"));
        Assertions.assertThat(Fruit.count()).isEqualTo(2);
    }

    @Test
    public void getFruitTest() {
        Assertions.assertThat(service.getFruit("Piña")).get().hasFieldOrPropertyWithValue("name", "Piña").hasFieldOrPropertyWithValue("description", "Es grande y sabe bien");
        Assertions.assertThat(service.getFruit("TATATATA")).isEmpty();
    }    
}
