package br.com.alura.projeto.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home(Model model) {
        //TODO: Implementar a Questão 3 - Front-End da Página de Login aqui...

        return "login";
    }
}
