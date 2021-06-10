package maciej.grochowski.reactivespring.fluxmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoTest {

    @Test
    public void fluxTest() {

        Flux<String> stringFlux = Flux.just("Spring", "Maciej", "IntelliJ")
                .concatWith(Flux.just("Before Exception"))
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After Exception"))
                .log();

        stringFlux
                .subscribe(System.out::println, e -> System.err.println(e), () -> System.out.println("Completed"));
    }

    @Test
    public void fluxTestElements_WithoutErrors() {

        Flux<String> stringFlux = Flux.just("Spring", "Maciej", "IntelliJ")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Maciej")
                .expectNext("IntelliJ")
                .verifyComplete()
        ;
    }

    @Test
    public void fluxTestElements_WithErrors() {

        Flux <String> stringFlux = Flux.just("Spring", "Maciej", "IntelliJ")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Maciej", "IntelliJ")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestElementsCount_WithError() {

        Flux <String> stringFlux = Flux.just("Spring", "Maciej", "IntelliJ")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectErrorMessage("Exception occurred")
                .verify();
    }

    @Test
    public void monoTest() {
        Mono<String> stringMono = Mono.just("Maciek")
                .log();

        StepVerifier.create(stringMono)
                .expectNext("Maciek")
                .verifyComplete();
    }

    @Test
    public void monoTestError() {

        StepVerifier.create(Mono.error(new RuntimeException("Exception occurred")))
                .expectError(RuntimeException.class)
                .verify();
    }
}
