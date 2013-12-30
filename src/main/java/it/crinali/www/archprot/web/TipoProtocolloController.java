package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.TipoProtocollo;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tipoprotocolloes")
@Controller
@RooWebScaffold(path = "tipoprotocolloes", formBackingObject = TipoProtocollo.class)
public class TipoProtocolloController {
}
