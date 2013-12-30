package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.TipoComunicazione;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tipocomunicaziones")
@Controller
@RooWebScaffold(path = "tipocomunicaziones", formBackingObject = TipoComunicazione.class)
public class TipoComunicazioneController {
}
