package pl.daveproject.webdietui.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.product.Product;
import pl.daveproject.webdietui.util.AuthObject;
import pl.daveproject.webdietui.util.WebdietServerData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final WebdietServerData serverData;
    private final AuthObject authObject;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> getAllProducts() {
        try {
            ResponseEntity<Product[]> response = restTemplate
                    .exchange(serverData.getProductEndpoint(),
                            HttpMethod.GET,
                            getHttpEntity(new Product()),
                            Product[].class);
            List<Product> products = Arrays.asList(response.getBody());
            Collections.sort(products);
            return products;
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public Product getProduct(String guid) {
        try {
            ResponseEntity<Product> response = restTemplate
                    .exchange(serverData.getProductEndpoint() + "/" + guid,
                            HttpMethod.GET,
                            getHttpEntity(new Product()),
                            Product.class);
            return response.getBody();
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public void createOrUpdate(Product product, String guid) {
        try {
            if (StringUtils.isEmpty(guid)) {
                restTemplate.exchange(serverData.getProductEndpoint(),
                        HttpMethod.POST,
                        getHttpEntity(product),
                        Product.class);
            } else {
                restTemplate.exchange(serverData.getProductEndpoint() + "/" + guid,
                        HttpMethod.PUT,
                        getHttpEntity(product),
                        Product.class);
            }
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    public void remove(String guid) {
        try {
            restTemplate.exchange(serverData.getProductEndpoint() + "/" + guid,
                    HttpMethod.DELETE,
                    getHttpEntity(new Product()),
                    Product.class);
        } catch (Exception exception) {
            throw new WebdietRestException(exception.getMessage());
        }
    }

    private HttpEntity<Product> getHttpEntity(Product product) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authObject.getToken());
        return new HttpEntity<>(product, headers);
    }
}
