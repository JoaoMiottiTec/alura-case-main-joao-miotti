package br.com.alura.projeto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/oi")
    public String home(Model model) {

        var message = """
                    <h1>Projeto Alura</h1>
                    <p>Bem-vinda ao teste para <b>Pessoa Desenvolvedora Java</b> da Alura!</p>
                """;

        model.addAttribute("message", message);

        return "home/oi";
    }
}
