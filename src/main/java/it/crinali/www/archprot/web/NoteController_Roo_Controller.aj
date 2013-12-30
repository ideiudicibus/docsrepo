// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.web;

import it.crinali.www.archprot.domain.Note;
import it.crinali.www.archprot.web.NoteController;
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

privileged aspect NoteController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String NoteController.create(@Valid Note note, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, note);
            return "notes/create";
        }
        uiModel.asMap().clear();
        note.persist();
        return "redirect:/notes/" + encodeUrlPathSegment(note.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String NoteController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Note());
        return "notes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String NoteController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("note", Note.findNote(id));
        uiModel.addAttribute("itemId", id);
        return "notes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String NoteController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("notes", Note.findNoteEntries(firstResult, sizeNo));
            float nrOfPages = (float) Note.countNotes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("notes", Note.findAllNotes());
        }
        return "notes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String NoteController.update(@Valid Note note, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, note);
            return "notes/update";
        }
        uiModel.asMap().clear();
        note.merge();
        return "redirect:/notes/" + encodeUrlPathSegment(note.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String NoteController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Note.findNote(id));
        return "notes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String NoteController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Note note = Note.findNote(id);
        note.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/notes";
    }
    
    void NoteController.populateEditForm(Model uiModel, Note note) {
        uiModel.addAttribute("note", note);
    }
    
    String NoteController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
