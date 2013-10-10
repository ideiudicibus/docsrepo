package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.Contatto;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/contattoes")
@Controller
@RooWebScaffold(path = "contattoes", formBackingObject = Contatto.class)
public class ContattoController {
}
