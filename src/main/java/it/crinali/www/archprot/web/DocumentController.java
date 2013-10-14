package it.crinali.www.archprot.web;
import it.crinali.www.archprot.domain.Contatto;
import it.crinali.www.archprot.domain.Document;
import it.crinali.www.archprot.domain.Progetto;
import it.crinali.www.archprot.domain.Struttura;
import it.crinali.www.archprot.domain.TipoProtocollo;
import it.crinali.www.archprot.service.ThumbnailService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@RequestMapping("/documents")
@Controller
@RooWebScaffold(path = "documents", formBackingObject = Document.class)
@RooWebFinder
public class DocumentController {

	private static final Log log = LogFactory.getLog(DocumentController.class);
	@Autowired
	ThumbnailService thumbnailService;
	
	@InitBinder
    protected void 	initBinder(HttpServletRequest request, 
    				ServletRequestDataBinder binder)
					throws ServletException {
		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
	

	


    @RequestMapping(value="savedoc",  method = RequestMethod.POST)
    public String createdoc(@Valid Document document,
    					 	BindingResult result, 
    					 	Model model,
    					 	@RequestParam("content") MultipartFile content,
    					 	HttpServletRequest request) {
    	
    	
    	
    	document.setContentType(content.getContentType());
    	document.setNomeFile(content.getOriginalFilename());
    	document.setSize(content.getSize());

    	//document.se content.getSize()
    	log.debug("Document: ");
    	log.debug("Name: "+content.getOriginalFilename());
    	log.debug("Description: "+document.getOggetto());
    	log.debug("File: " +content.getName());
    	log.debug("Type: "+content.getContentType());
    	log.debug("Size: "+content.getSize());
        if (result.hasErrors()) {
        	log.debug("errors: "+result.getErrorCount());
        	for(ObjectError o:result.getAllErrors()){
        		log.debug(o);
        		log.debug(o.getCode()+" "+o.getDefaultMessage());
        		
        	}
        	 populateEditForm(model, document);
            //model.addAttribute("document", document);
            return "documents/create";
        }
        try{
        document.setThumbnail(thumbnailService.generateThumbnail(document.getContent()));
        }
        catch(Exception e) {e.printStackTrace();}
        model.asMap().clear();
        try{
        document.persist();
        }
        catch(Exception e){
        	
        	e.printStackTrace();
        }
        return "redirect:/documents?page=1&size=10" + encodeUrlPathSegment(document.getId().toString(), request);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model,HttpServletRequest httpServletRequest) {
    	Document doc = Document.findDocument(id);
    	//doc.setUrl(httpServletRequest.getRequestURL()+"/showdoc/"+id);
    	doc.setUrl(getContextPathUrl(httpServletRequest)+"/documents/showdoc/"+id);
    	//doc.setUrl("http://localhost:8080/archprot/documents/showdoc/"+id);
        model.addAttribute("document", Document.findDocument(id));
        model.addAttribute("itemId", id);
        return "documents/show";
    }
    
  
    @RequestMapping(value = "/showdoc/{id}", method = RequestMethod.GET)
    public String showdoc(	@PathVariable("id") Long id,
    						HttpServletResponse response,
    						Model model) {
    	
    	Document doc = Document.findDocument(id);
    	

        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +doc.getNomeFile()+ "\"");

            OutputStream out = response.getOutputStream();
            response.setContentType(doc.getContentType());

           IOUtils.copy( new ByteArrayInputStream(doc.getContent()) , out);
            out.flush();
         
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/previewdoc/{id}", method = RequestMethod.GET)
    public String previewdoc(	@PathVariable("id") Long id,
    						HttpServletResponse response,
    						Model model) {
    	
    	Document doc = Document.findDocument(id);
    	
    	String imageName=doc.getNomeFile()+".png";
        try {
            response.setHeader("Content-Disposition", "inline;filename=\"" +imageName+ "\"");

            OutputStream out = response.getOutputStream();
            response.setContentType("image/png");

           IOUtils.copy( new ByteArrayInputStream(doc.getThumbnail()) , out);
            out.flush();
         
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
   /* @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model,HttpServletRequest httpServletRequest) {
    	Document document= Document.findDocument(id);
    	//document.setUrl(httpServletRequest.getRequestURL()+"/showdoc/"+id);
    	document.setUrl(getContextPathUrl(httpServletRequest)+"/documents/showdoc/"+id);
    	//document.setUrl("http://localhost:8080/archprot/documents/showdoc/"+id);
        model.addAttribute("document", document);
        return "documents/update";
    }
    */
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Document.findDocument(id));
        return "documents/update";
    }
    
    @RequestMapping(value="update",method = RequestMethod.PUT, produces = "text/html")
    public String updateDocument(Document document, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	//partial copy according to update form
    	Document tmpDoc=Document.findDocument(document.getId());
    	document.setContent(tmpDoc.getContent());
    	document.setContentType(tmpDoc.getContentType());
    	document.setSize(tmpDoc.getSize());
    	document.setThumbnail(tmpDoc.getThumbnail());
//    	if (bindingResult.hasErrors()) {
//            populateEditForm(uiModel, document);
//            return "documents/update";
//        }
    	
        uiModel.asMap().clear();
        document.merge();
        return "redirect:/documents/" + encodeUrlPathSegment(document.getId().toString(), httpServletRequest);
    }
	
	/*
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Document document, BindingResult bindingResult, Model uiModel,
           @RequestParam("content") CommonsMultipartFile content,
           HttpServletRequest httpServletRequest) {
       File dest = new File("/tmp/" + content.getOriginalFilename());
       try {
          content.transferTo(dest);
          document.setSize(content.getSize());
          document.setUrl(dest.getAbsolutePath());
          document.setContentType(content.getContentType());
       } catch (Exception e) {
          e.printStackTrace();
          return "documents/create";
       }

       uiModel.asMap().clear();
       document.persist();
       return "redirect:/documents/" + encodeUrlPathSegment(document.getId().toString(),
          httpServletRequest);
    }*/
    
    private String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();   // d=789
        if (queryString != null) {
            reqUrl += "?"+queryString;
        }
        return reqUrl;
    }
    
    private String getContextPathUrl(HttpServletRequest request){
    	
    	/*String file = request.getRequestURI();
    	if (request.getQueryString() != null) {
    	   file += '?' + request.getQueryString();
    	}*/
    	URL reconstructedURL=null;
		try {
			reconstructedURL = new URL(request.getScheme(),
			                               request.getServerName(),
			                               request.getServerPort(),request.getContextPath());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return reconstructedURL.toString();
    	
    }
    
    @RequestMapping(params = { "find=ByTemplate", "form" }, method = RequestMethod.GET)
    public String findDocumentsByTemplate(Model uiModel) {
        uiModel.addAttribute("tipoprotocolloes", java.util.Arrays.asList(TipoProtocollo.class.getEnumConstants()));
        uiModel.addAttribute("strutturas", Struttura.findAllStrutturas());
        uiModel.addAttribute("progettoes", Progetto.findAllProgettoes());
        uiModel.addAttribute("contattoes", Contatto.findAllContattoes());
        addDateTimeFormatPatterns(uiModel);
        return "documents/findDocumentsByTemplate";
    }
    
    @RequestMapping(params = "find=ByTemplate", method = RequestMethod.GET)
    public String findDocumentsByTemplate(@RequestParam("oggetto") String oggetto, @RequestParam("numeroProtocollo") String numeroProtocollo, @RequestParam("tipoProtocollo") TipoProtocollo tipoProtocollo, @RequestParam("minDataProtocollo") @DateTimeFormat(style = "M-") Date minDataProtocollo, @RequestParam("maxDataProtocollo") @DateTimeFormat(style = "M-") Date maxDataProtocollo, @RequestParam("struttura") Struttura struttura, @RequestParam("progetto") Progetto progetto, @RequestParam("contatto") Contatto contatto, Model uiModel,HttpServletRequest request) {
        
    	 ArrayList<String> wheres = new ArrayList<String>();

         Iterator<String> i = (Iterator<String>) request.getParameterNames();
         
         while (i.hasNext()) {
             String fieldname = i.next();
             String value = request.getParameter(fieldname);

             if (value.length() == 0) {
                 continue;
             }
             if(fieldname.contains("find")){continue;}

             if (fieldname.equals("oggetto") || fieldname.equals("numeroProtocollo") ) {
            	 
                 value = value.replace('*', '%');
                 if (value.charAt(0) != '%') {
                     value = "%" + value;
                 }
                 if (value.charAt(value.length() - 1) != '%') {
                     value = value + "%";
                 }
                 wheres.add(" o." + fieldname + " LIKE '" + value + "'");
             }
             else if (fieldname.contains("min")) {
                 fieldname = fieldname.replace("min", "");
                 fieldname=Character.toLowerCase(fieldname.charAt(0)) + fieldname.substring(1);
                 wheres.add(" o." + fieldname + " >= '" + value + "' ");
             }
             else if (fieldname.contains("max")) {
                 fieldname = fieldname.replace("max", "");
                 fieldname=Character.toLowerCase(fieldname.charAt(0)) + fieldname.substring(1);

                 wheres.add(" o." + fieldname + " <= '" + value + "' ");

             }
             else {
                 wheres.add(" o." + fieldname + " = '" + value + "' ");
             }
         }
    	
         String query = "SELECT o FROM Document AS o ";
         if (wheres.size() > 0) {
             query += " WHERE ";
             for (String clause: wheres) {
                 query += clause + " AND "; 
             }
             query += " 1 = 1 ";
         }


         EntityManager em = Document.entityManager();
         TypedQuery<Document> q = em.createQuery(query, Document.class);
    	
    	uiModel.addAttribute("documents", q.getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "documents/list";
    }
    /*
    public static TypedQuery<Document> findAssetsByWebRequest( WebRequest request) {

        ArrayList<String> wheres = new ArrayList<String>();

        Iterator<String> i = request.getParameterNames();

        while (i.hasNext()) {
            String fieldname = i.next();
            String value = request.getParameter(fieldname);

            if (value.length() == 0) {
                continue;
            }

            if (fieldname.equals("manufacturer") || fieldname.equals("model") ) {

                value = value.replace('*', '%');
                if (value.charAt(0) != '%') {
                    value = "%" + value;
                }
                if (value.charAt(value.length() - 1) != '%') {
                    value = value + "%";
                }
                wheres.add(" o." + fieldname + " LIKE '" + value + "'");
            }
            else if (fieldname.contains("min")) {
                fieldname = fieldname.replace("min", "").toLowerCase();
                wheres.add(" o." + fieldname + " >= '" + value + "' ");
            }
            else if (fieldname.contains("max")) {
                fieldname = fieldname.replace("max", "").toLowerCase();
                wheres.add(" o." + fieldname + " <= '" + value + "' ");

            }
            else {
                wheres.add(" o." + fieldname + " = '" + value + "' ");
            }
        }


        String query = "SELECT o FROM Asset AS o ";
        if (wheres.size() > 0) {
            query += " WHERE ";
            for (String clause: wheres) {
                query += clause + " AND "; 
            }
            query += " 1 = 1 ";
        }


        EntityManager em = Document.entityManager();
        TypedQuery<Document> q = em.createQuery(query, Document.class);

        return q;
    }*/
    
}


