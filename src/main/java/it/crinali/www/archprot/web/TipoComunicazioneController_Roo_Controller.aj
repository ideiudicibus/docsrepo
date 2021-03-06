// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.web;

import it.crinali.www.archprot.domain.TipoComunicazione;
import it.crinali.www.archprot.web.TipoComunicazioneController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect TipoComunicazioneController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TipoComunicazioneController.create(@Valid TipoComunicazione tipoComunicazione, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoComunicazione);
            return "tipocomunicaziones/create";
        }
        uiModel.asMap().clear();
        tipoComunicazione.persist();
        return "redirect:/tipocomunicaziones/" + encodeUrlPathSegment(tipoComunicazione.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TipoComunicazioneController.createForm(Model uiModel) {
        populateEditForm(uiModel, new TipoComunicazione());
        return "tipocomunicaziones/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TipoComunicazioneController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("tipocomunicazione", TipoComunicazione.findTipoComunicazione(id));
        uiModel.addAttribute("itemId", id);
        return "tipocomunicaziones/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TipoComunicazioneController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("tipocomunicaziones", TipoComunicazione.findTipoComunicazioneEntries(firstResult, sizeNo));
            float nrOfPages = (float) TipoComunicazione.countTipoComunicaziones() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("tipocomunicaziones", TipoComunicazione.findAllTipoComunicaziones());
        }
        return "tipocomunicaziones/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TipoComunicazioneController.update(@Valid TipoComunicazione tipoComunicazione, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, tipoComunicazione);
            return "tipocomunicaziones/update";
        }
        uiModel.asMap().clear();
        tipoComunicazione.merge();
        return "redirect:/tipocomunicaziones/" + encodeUrlPathSegment(tipoComunicazione.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TipoComunicazioneController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TipoComunicazione.findTipoComunicazione(id));
        return "tipocomunicaziones/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TipoComunicazioneController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TipoComunicazione tipoComunicazione = TipoComunicazione.findTipoComunicazione(id);
        tipoComunicazione.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/tipocomunicaziones";
    }
    
    void TipoComunicazioneController.populateEditForm(Model uiModel, TipoComunicazione tipoComunicazione) {
        uiModel.addAttribute("tipoComunicazione", tipoComunicazione);
    }
    
    String TipoComunicazioneController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
