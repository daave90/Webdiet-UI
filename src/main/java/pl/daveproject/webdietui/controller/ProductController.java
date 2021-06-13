package pl.daveproject.webdietui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.product.Product;
import pl.daveproject.webdietui.service.AuthService;
import pl.daveproject.webdietui.service.ProductService;
import pl.daveproject.webdietui.validators.FormValidator;
import pl.daveproject.webdietui.validators.ProductRequestValidator;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final AuthService authService;
    private final ProductService productService;

    @GetMapping("/show-products")
    public String getAllProducts(Model model) {
        try {
            authService.checkIfAuthenticated();
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            return "products";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @GetMapping("/create-product")
    public String showCreateFrom(Model model) {
        try {
            authService.checkIfAuthenticated();
            model.addAttribute("productRequest", new Product());
            model.addAttribute("submitText", "Dodaj produkt");
            return "form-product";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @PostMapping("/create-product")
    public String createOrUpdateProduct(Product product, @RequestParam("id") String guid, Model model) {
        try {
            FormValidator validator = new ProductRequestValidator();
            validator.validate(product);
            System.out.println("Product guid: " + guid);
            productService.createOrUpdate(product, guid);
            return "redirect:/show-products";
        } catch (WebdietRestException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("productRequest", product);
            model.addAttribute("submitText", "Dodaj produkt");
            return "form-product";
        }
    }

    @GetMapping("/update-product")
    public String showUpdateForm(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            Product product = productService.getProduct(guid);
            model.addAttribute("productRequest", product);
            model.addAttribute("submitText", "Aktualizuj produkt");
            return "form-product";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }

    @GetMapping("/delete-product")
    public String deleteProduct(Model model, @RequestParam("id") String guid) {
        try {
            authService.checkIfAuthenticated();
            productService.remove(guid);
            return "redirect:/show-products";
        } catch (WebdietRestException exception) {
            return authService.handleNoAuthException(model, exception);
        }
    }
}
