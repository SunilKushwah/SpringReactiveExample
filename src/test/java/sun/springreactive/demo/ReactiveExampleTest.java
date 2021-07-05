package sun.springreactive.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ReactiveExampleTest {

    Person sunil = new Person("Sunil", "Kushwah");
    Person jitu = new Person("jitu", "singh");
    Person dev = new Person("dev", "chaudary");
    Person golu = new Person("golu", "sharma");

    @Test
    public void monoTest() throws Exception{
        Mono<Person> personMono = Mono.just(sunil);
        Person person = personMono.block();
        log.info(person.sayMyName());
    }

    @Test
    public void monoTransformTest() throws Exception{
        Mono<Person> personMono = Mono.just(jitu);
        PersonCommand personCommand = personMono.map(person -> new PersonCommand(person)).block();
        log.info(personCommand.sayMyName());
    }

    @Test
    public void monoFilterTest() throws Exception{
        Mono<Person> personMono = Mono.just(dev);
        Person person = personMono.filter(p -> p.getFirstName().equalsIgnoreCase("foo")).block();
        log.info(person.sayMyName());//throw NPE
    }

    @Test
    public void fluxTest() throws Exception{
        Flux<Person> personFlux = Flux.just(sunil,jitu,dev,golu);
        personFlux.subscribe(p->log.info(p.sayMyName()));
    }

    @Test
    public void fluxFilterTest() throws Exception{
        Flux<Person> personFlux = Flux.just(sunil,jitu,dev,golu);
        personFlux.filter(person -> person.getFirstName().equalsIgnoreCase(jitu.getFirstName())).subscribe(p->log.info(p.sayMyName()));
    }

    @Test
    public void fluxTestDelayNoOutput() throws Exception{
        Flux<Person> personFlux = Flux.just(sunil,jitu,dev,golu);
        personFlux.delayElements(Duration.ofSeconds(1)).subscribe(p->log.info(p.sayMyName()));
    }

    @Test
    public void fluxTestDelay() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Person> personFlux = Flux.just(sunil,jitu,dev,golu);
        personFlux.delayElements(Duration.ofSeconds(1))
                .doOnComplete(countDownLatch::countDown)
                .subscribe(p->log.info(p.sayMyName()));
        countDownLatch.await();
    }

    @Test
    public void fluxTestFilterDelay() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Person> personFlux = Flux.just(sunil,jitu,dev,golu);
        personFlux.delayElements(Duration.ofSeconds(1))
                .doOnComplete(countDownLatch::countDown)
                .filter(p->p.getFirstName().contains("i"))
                .subscribe(p->log.info(p.sayMyName()));
        countDownLatch.await();
    }

}
