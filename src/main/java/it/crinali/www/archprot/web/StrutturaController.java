package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.Struttura;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/strutturas")
@Controller
@RooWebScaffold(path = "strutturas", formBackingObject = Struttura.class)
public class StrutturaController {
}
