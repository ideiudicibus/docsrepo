// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.web;

import it.crinali.www.archprot.domain.Destinatario;
import it.crinali.www.archprot.domain.Document;
import it.crinali.www.archprot.web.DestinatarioController;
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

privileged aspect DestinatarioController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DestinatarioController.create(@Valid Destinatario destinatario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, destinatario);
            return "destinatarios/create";
        }
        uiModel.asMap().clear();
        destinatario.persist();
        return "redirect:/destinatarios/" + encodeUrlPathSegment(destinatario.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DestinatarioController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Destinatario());
        return "destinatarios/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DestinatarioController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("destinatario", Destinatario.findDestinatario(id));
        uiModel.addAttribute("itemId", id);
        return "destinatarios/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DestinatarioController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("destinatarios", Destinatario.findDestinatarioEntries(firstResult, sizeNo));
            float nrOfPages = (float) Destinatario.countDestinatarios() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("destinatarios", Destinatario.findAllDestinatarios());
        }
        return "destinatarios/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DestinatarioController.update(@Valid Destinatario destinatario, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, destinatario);
            return "destinatarios/update";
        }
        uiModel.asMap().clear();
        destinatario.merge();
        return "redirect:/destinatarios/" + encodeUrlPathSegment(destinatario.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DestinatarioController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Destinatario.findDestinatario(id));
        return "destinatarios/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DestinatarioController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Destinatario destinatario = Destinatario.findDestinatario(id);
        destinatario.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/destinatarios";
    }
    
    void DestinatarioController.populateEditForm(Model uiModel, Destinatario destinatario) {
        uiModel.addAttribute("destinatario", destinatario);
        uiModel.addAttribute("documents", Document.findAllDocuments());
    }
    
    String DestinatarioController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
