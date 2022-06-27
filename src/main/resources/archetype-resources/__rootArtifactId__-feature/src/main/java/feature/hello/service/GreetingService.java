#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.feature.hello.service;

import ${package}.data.entity.Person;
import ${package}.model.dto.Greeting;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GreetingService {

    public String getGreeting() {
        final List<Person> people = Person.listAll();
        return new Greeting(people.isEmpty() ? new Person() : people.get(0)).toString();
    }
}
