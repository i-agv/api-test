package apitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringBootRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestApplication.class, args);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate rt = new RestTemplate();
//
//        // Mapping objects to JSON
//        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//        // Custom error handling
//        rt.setErrorHandler(new DefaultResponseErrorHandler() {
//            protected boolean hasError(HttpStatus statusCode) {
//                return false;
//            }
//        });
//
//        // Because of default java.net.HttpURLConnection
//        // SimpleClientHttpRequestFactory does not support PATCH method
//        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//
//        return rt;
//    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();

        // Mapping objects to JSON
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // Custom error handling
        rt.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
        return rt;
    }
}
