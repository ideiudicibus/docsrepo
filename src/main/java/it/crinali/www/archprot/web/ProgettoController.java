package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.Progetto;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/progettoes")
@Controller
@RooWebScaffold(path = "progettoes", formBackingObject = Progetto.class)
public class ProgettoController {
}
