package pl.daveproject.webdietui.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import pl.daveproject.webdietui.exception.ErrorCode;
import pl.daveproject.webdietui.exception.WebdietRestException;
import pl.daveproject.webdietui.model.request.LoginRequest;
import pl.daveproject.webdietui.model.request.UserRequest;
import pl.daveproject.webdietui.util.AuthObject;
import pl.daveproject.webdietui.util.WebdietServerData;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthObject authObject;
    private final WebdietServerData serverData;
    private final RestTemplate restTemplate = new RestTemplate();

    public void register(UserRequest request) {
        try {
            restTemplate.postForEntity(serverData.getUserEndpoint(), request, String.class);
        } catch (Exception ex) {
            String jsonMessage = ex.getMessage().substring(7, ex.getMessage().length() - 1);
            JSONObject jsonObject = new JSONObject(jsonMessage);
            throw new WebdietRestException(jsonObject.get("message").toString());
        }
    }

    public void login(LoginRequest loginRequest) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverData.getLoginEndpoint(), loginRequest, String.class);
            authObject.setToken(response.getHeaders().get("Authorization").get(0));
        } catch (Exception ex) {
            if (ex.getMessage().startsWith("401")) {
                throw new WebdietRestException(ErrorCode.LOGIN_FAILED);
            }
            throw new WebdietRestException(ex.getMessage());
        }
    }

    public void logout() {
        try {
            authObject.setToken("");
            restTemplate.exchange(serverData.getLogoutEndpoint(),
                    HttpMethod.POST,
                    getHttpEntity(),
                    String.class);
        } catch (Exception ex) {
            throw new WebdietRestException(ex.getMessage());
        }
    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authObject.getToken());
        return new HttpEntity<>("", headers);
    }

    public void checkIfAuthenticated() {
        if (StringUtils.isEmpty(authObject.getToken())) {
            throw new WebdietRestException(ErrorCode.NO_AUTHENTICATED);
        }
    }

    public String handleNoAuthException(Model model, WebdietRestException exception) {
        if (exception.getErrorCode() == ErrorCode.NO_AUTHENTICATED) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", exception.getMessage());
            return "errorPage";
        }
    }
}
