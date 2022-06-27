#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.feature.hello.service;

import ${package}.data.entity.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
@ApplicationScoped
public class PeopleService {

    @Transactional
    public void addPerson(String name, int age) {
        new Person(name, age).persist();
    }

    public List<Person> getAllPeople() {
        return Person.listAll();
    }
}
