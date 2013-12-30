package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.Destinatario;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/destinatarios")
@Controller
@RooWebScaffold(path = "destinatarios", formBackingObject = Destinatario.class)
public class DestinatarioController {
}
