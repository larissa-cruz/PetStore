//1 - Pacote de classes
package petstore;

// 2 - Bibliotecas

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

//3 - Classe
public class Pet {
    //3.1 - Atributos (adjetivos do objeto)
    String uri = "https://petstore.swagger.io/v2/pet"; //endereço da entidade Pet

    //3.2 - Métodos e Funções
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    //Incluir - Create - Post
    @Test(priority = 1) //Identifica o método ou função como um teste para o TestNG. Prioridade 1, ou seja, mais alta.
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        //Sintaxe Gherkin
        //Dado - Quando - Então
        //Given - When - Then

        given() //Dado
                .contentType("application/json") //Tipo de dado que eu vou utilizar, comum em API REST, antigas eram text/xml
                .log().all()
                .body(jsonBody)
        .when() //Quando
                .post(uri)
        .then()//Então
                .log().all()
                .statusCode(200)
                .body("name", is("Atena")) //linha de validação, verifica se o nome do cachorro é Snoopy.
                .body("status", is("available")) //linha de validação, verifica se o status é avaliable.
                .body("category.name", is("AX5672LORD"))//linha de validação, verifica se o nome dentro de categoria é dog.
                .body("tags.name", contains("data"))//linha de validação, como agora quero o nome que está dentro da tag (que é uma lista), precisei usar o contains, para ver se contém dentro da lista o nome "sta".
        ;
    }
    @Test(priority = 2) //prioridade 2, ou seja, primeiro inclui depois consulta
    public void consultarPet() {
        String petId = "1608199527";

        String token =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("category.name", is("AX5672LORD"))
                .body("status", is("available"))
        .extract()
                .path("category.name")
        ;
                System.out.println("O token é " + token);
    }
    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name",is("Atena"))
                .body("status", is("sold"))
        ;
    }
}
