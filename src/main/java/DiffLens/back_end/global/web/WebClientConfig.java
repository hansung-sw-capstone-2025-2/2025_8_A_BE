package DiffLens.back_end.global.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient fastApiWebClient(@Value("${fast-api.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
    }

    @Bean(name = "googleTokenWebClient")
    public WebClient googleTokenWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://oauth2.googleapis.com").build();
    }

    @Bean(name = "googleUserInfoWebClient")
    public WebClient googleUserInfoWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://www.googleapis.com").build();
    }

}
