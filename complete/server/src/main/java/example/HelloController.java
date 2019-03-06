package example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/")
public class HelloController {

    @Get("/{name}")
    HttpResponse<String> hello(String name) {
        return HttpResponse.ok("Hello, " + name + "!");
    }
}
