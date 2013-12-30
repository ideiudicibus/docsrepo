package it.crinali.www.archprot.web;

import flexjson.JSONSerializer;
import it.crinali.www.archprot.domain.Contatto;
import it.crinali.www.archprot.domain.Destinatario;
import it.crinali.www.archprot.domain.Document;
import it.crinali.www.archprot.domain.Note;
import it.crinali.www.archprot.domain.Progetto;
import it.crinali.www.archprot.domain.Struttura;
import it.crinali.www.archprot.domain.TipoComunicazione;
import it.crinali.www.archprot.domain.TipoProtocollo;
import it.crinali.www.archprot.service.ThumbnailService;
import it.crinali.www.archprot.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {
		// Convert multipart object to byte[]
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	@RequestMapping(value = "savedoc", method = RequestMethod.POST)
	public String createdoc(@Valid Document document, BindingResult result,
			Model model, @RequestParam("content") MultipartFile content,
			HttpServletRequest request) {

		document.setContentType(content.getContentType());
		document.setNomeFile(content.getOriginalFilename());
		document.setSize(content.getSize());

		// document.se content.getSize()
		log.debug("Document: ");
		log.debug("Name: " + content.getOriginalFilename());
		log.debug("Description: " + document.getOggetto());
		log.debug("File: " + content.getName());
		log.debug("Type: " + content.getContentType());
		log.debug("Size: " + content.getSize());
		String destinatariSearchInList = request.getParameter("destinatari");
		if (destinatariSearchInList != null
				&& !destinatariSearchInList.isEmpty()) {
			document.setDestinatari(findDestinatarioInIds(destinatariSearchInList));

		}
		if (result.hasErrors()) {
			log.debug("errors: " + result.getErrorCount());
			for (ObjectError o : result.getAllErrors()) {
				log.debug(o);
				log.debug(o.getCode() + " " + o.getDefaultMessage());
			}
			populateEditForm(model, document);
			// model.addAttribute("document", document);
			return "documents/create";
		}

		String davDestination = getDavDestination(document);
		document.setPath(davDestination);

		try {
			document.setThumbnail(thumbnailService.generateThumbnail(document
					.getContent()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.asMap().clear();
		try {
			Note note = document.getNote();
			if (note != null && !note.getTesto().isEmpty()) {
				note.persist();
				document.setNote(note);
				document.persist();
			} else {
				document.setNote(null);
				document.persist();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileOutputStream fout = null;
		try {
			File theDir = new File(document.getPath());
			if (!theDir.exists()) {
				theDir.mkdirs();
			}
			fout = new FileOutputStream(new File(document.getPath()
					+ content.getOriginalFilename()));
			IOUtils.copy(new ByteArrayInputStream(content.getBytes()), fout);
			fout.close();
			fout = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (fout != null) {
				fout = null;
			}
			e.printStackTrace();
		}
		return "redirect:/documents?page=1&size=10"
				+ encodeUrlPathSegment(document.getId().toString(), request);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model,
			HttpServletRequest httpServletRequest) {
		Document doc = findDocumentByIdSmall(id);
		doc.setUrl(httpServletRequest.getRequestURL() + "/showdoc/" + id);

		doc.setUrl(getContextPathUrl(httpServletRequest)
				+ "/documents/showdoc/" + id);
		// doc.setUrl("http://localhost:8080/archprot/documents/showdoc/"+id);
		model.addAttribute("document", Document.findDocument(id));
		model.addAttribute("itemId", id);
		return "documents/show";
	}

	@RequestMapping(value = "/showdoc/{id}", method = RequestMethod.GET)
	public String showdoc(@PathVariable("id") Long id,
			HttpServletResponse response, Model model) {
		Document doc = Document.findDocument(id);
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ doc.getNomeFile() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(doc.getContentType());
			IOUtils.copy(new ByteArrayInputStream(doc.getContent()), out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/previewdoc/{id}", method = RequestMethod.GET)
	public String previewdoc(@PathVariable("id") Long id,
			HttpServletResponse response, Model model) {
		Document doc = Document.findDocument(id);
		String imageName = doc.getNomeFile() + ".png";
		try {
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ imageName + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType("image/png");
			IOUtils.copy(new ByteArrayInputStream(doc.getThumbnail()), out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		Document d = Document.findDocument(id);
		populateEditForm(uiModel, d);
		JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").exclude("*.documenti")
				.exclude("*.note").exclude("*.version")
				.deepSerialize(d.getDestinatari());
		uiModel.addAttribute("destinatari", json);
		addDateTimeFormatPatterns(uiModel);

		return "documents/update";
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT, produces = "text/html")
	public String updateDocument(Document document,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		// partial copy according to update form
		Document tmpDoc = Document.findDocument(document.getId());
		document.setContent(tmpDoc.getContent());
		document.setContentType(tmpDoc.getContentType());
		document.setSize(tmpDoc.getSize());
		document.setThumbnail(tmpDoc.getThumbnail());
		document.setPath(getDavDestination(document));
		document.setNomeFile(tmpDoc.getNomeFile());
		// if (bindingResult.hasErrors()) {
		// populateEditForm(uiModel, document);
		// return "documents/update";
		// }

		String destinatariSearchInList = httpServletRequest
				.getParameter("destinatari");
		if (destinatariSearchInList.startsWith(","))
			destinatariSearchInList = destinatariSearchInList.substring(1);

		if (destinatariSearchInList != null
				&& !destinatariSearchInList.isEmpty()) {
			document.setDestinatari(findDestinatarioInIds(destinatariSearchInList));

		}

		if (!document.getPath().equals(tmpDoc.getPath())) {
			File f = null;
			File destDir = null;
			try {
				f = new File(tmpDoc.getPath() + tmpDoc.getNomeFile());
				if (!f.exists()) {
					new File(tmpDoc.getPath()).mkdirs();
					FileOutputStream oFile = new FileOutputStream(f);
					IOUtils.copy(new ByteArrayInputStream(tmpDoc.getContent()),
							oFile);
					oFile.close();
					oFile = null;
				}
			} catch (Exception e) {
				log.error("cannot find origin", e);
			}
			try {
				destDir = new File(document.getPath());
				if (!destDir.exists()) {
					destDir.mkdirs();
				}

			} catch (Exception e) {
				log.error("cannot find destination", e);
			}
			try {
				FileUtils.moveFileToDirectory(f, destDir, true);
			} catch (IOException e) {
				log.error("cannot move from origin to destination", e);
			}

		}
		uiModel.asMap().clear();
		try {
			Note note = document.getNote();
			if (note != null) {
				note.persist();
				document.setNote(note);
				document.merge();
			} else {
				document.setNote(null);
				document.merge();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/documents/"
				+ encodeUrlPathSegment(document.getId().toString(),
						httpServletRequest);
	}

	private String getUrl(HttpServletRequest req) {
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

	private String getContextPathUrl(HttpServletRequest request) {

		URL reconstructedURL = null;
		try {
			reconstructedURL = new URL(request.getScheme(),
					request.getServerName(), request.getServerPort(),
					request.getContextPath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return reconstructedURL.toString();
	}

	@RequestMapping(params = { "find=ByTemplate", "form" }, method = RequestMethod.GET)
	public String findDocumentsByTemplate(Model uiModel) {
		uiModel.addAttribute("tipoprotocolloes",
				TipoProtocollo.findAllTipoProtocolloes());
		uiModel.addAttribute("tipocomunicazione",
				TipoComunicazione.findAllTipoComunicaziones());
		uiModel.addAttribute("strutturas", Struttura.findAllStrutturas());
		uiModel.addAttribute("progettoes", Progetto.findAllProgettoes());
		uiModel.addAttribute("contattoes", Contatto.findAllContattoes());
		addDateTimeFormatPatterns(uiModel);
		return "documents/findDocumentsByTemplate";
	}

	@RequestMapping(params = "find=ByTemplate", method = RequestMethod.GET)
	public String findDocumentsByTemplate(
			@RequestParam("oggetto") String oggetto,
			@RequestParam("numeroProtocollo") String numeroProtocollo,
			@RequestParam("tipoProtocollo") TipoProtocollo tipoProtocollo,
			@RequestParam("minDataProtocollo") @DateTimeFormat(style = "M-") Date minDataProtocollo,
			@RequestParam("maxDataProtocollo") @DateTimeFormat(style = "M-") Date maxDataProtocollo,
			@RequestParam("struttura") Struttura struttura,
			@RequestParam("progetto") Progetto progetto,
			@RequestParam("contatto") Contatto contatto, Model uiModel,
			HttpServletRequest request) {
		ArrayList<String> wheres = new ArrayList<String>();
		Iterator<String> i = (Iterator<String>) request.getParameterNames();
		ArrayList<String> inWheresClause = new ArrayList<String>();
		while (i.hasNext()) {
			String fieldname = i.next();
			String value = request.getParameter(fieldname);
			if (value.length() == 0) {
				continue;
			}
			if (fieldname.contains("find")) {
				continue;
			}
			// TODO query filter destinatari

			if (fieldname.equals("oggetto")
					|| fieldname.equals("numeroProtocollo")) {
				value = value.replace('*', '%');
				if (value.charAt(0) != '%') {
					value = "%" + value;
				}
				if (value.charAt(value.length() - 1) != '%') {
					value = value + "%";
				}
				wheres.add(" o." + Utils.unCamelize(fieldname, "_") + " LIKE '"
						+ value + "'");
			} else if (fieldname.contains("min")) {
				fieldname = fieldname.replace("min", "");
				fieldname = Character.toLowerCase(fieldname.charAt(0))
						+ fieldname.substring(1);
				wheres.add(" o." + fieldname + " >= '" + value + "' ");
			} else if (fieldname.contains("max")) {
				fieldname = fieldname.replace("max", "");
				fieldname = Character.toLowerCase(fieldname.charAt(0))
						+ fieldname.substring(1);
				wheres.add(" o." + Utils.unCamelize(fieldname, "_") + " <= '"
						+ value + "' ");
			} else if (fieldname.equals("destinatari")) {

				if (value.startsWith(","))
					value = value.substring(1);

				value = "(" + value + ")";

				inWheresClause.add(value);
			} else {
				wheres.add(" o." + Utils.unCamelize(fieldname, "_") + " = '"
						+ value + "' ");
			}
		}
		String query = "SELECT o.* FROM Document AS o ";
		if (wheres.size() > 0) {
			query += " WHERE ";
			for (String clause : wheres) {
				query += clause + " AND ";
			}
			query += " 1 = 1 ";
		}
		if (inWheresClause.size() > 0) {
			if (wheres.size() == 0) {
				query += " WHERE 1 = 1";
			}
			query += " AND ";
			for (String clause : inWheresClause) {
				query += "o.id in (select documenti from document_destinatari where destinatari in "
						+ clause + " ) AND";
			}
			query += " 1 = 1 ";
		}

		EntityManager em = Document.entityManager();
		Query q = em.createNativeQuery(query, Document.class);

		uiModel.addAttribute("documents", q.getResultList());
		addDateTimeFormatPatterns(uiModel);
		return "documents/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Long id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Document document = Document.findDocument(id);
		try {
			File f = new File(document.getPath() + document.getNomeFile());
			File destDir = new File(getDavTrashDestination(document, "trash"));
			FileUtils.moveFileToDirectory(f, destDir, true);
			f = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		document.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/documents";
	}

	@RequestMapping(value = "/api/destinatario", method = RequestMethod.GET)
	public ResponseEntity<String> getLocations(
			@RequestParam(value = "q", required = true) String ragioneSociale,
			@RequestParam(value = "limit", required = false) String limit,
			@RequestParam(value = "callback", required = false) String callback) {

		int maxResults = 0;

		try {
			maxResults = Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			if (log.isWarnEnabled())
				log.warn("A limit was passed that could not be parsed as an int.  Setting maxResults to default.");

			maxResults = 20;
		}

		String query = "SELECT o FROM Destinatario AS o where lower(o.ragioneSociale) LIKE lower('%"
				+ ragioneSociale + "%')";
		EntityManager em = Destinatario.entityManager();
		TypedQuery<Destinatario> q = em.createQuery(query, Destinatario.class)
				.setMaxResults(maxResults);
		List<Destinatario> list = q.getResultList();
		Set<Destinatario> resultSet = new HashSet<Destinatario>(list);
		Map<String, Set<Destinatario>> resultMap = new HashMap<String, Set<Destinatario>>();
		resultMap.put("searchresult", resultSet);

		JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").exclude("*.documenti")
				.exclude("*.note").exclude("*.version")
				.deepSerialize(resultMap);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods", "GET");
		responseHeaders.set("Access-Control-Allow-Headers", "");
		responseHeaders.set("Access-Control-Max-Age", "86400");

		if (callback.trim().length() > 0) {
			json = callback + "(" + json + ")";
		}

		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/destinatario/d", method = RequestMethod.GET)
	public ResponseEntity<String> getDestinatariByDocumentId(
			@RequestParam(value = "q", required = true) String queryTerm,
			@RequestParam(value = "callback", required = false) String callback) {

		long id = Long.parseLong(queryTerm);
		Document d = Document.findDocument(id);

		Set<Destinatario> resultSet = d.getDestinatari();
		Map<String, Set<Destinatario>> resultMap = new HashMap<String, Set<Destinatario>>();
		resultMap.put("searchresult", resultSet);

		JSONSerializer serializer = new JSONSerializer();
		String json = serializer.exclude("*.class").exclude("*.documenti")
				.exclude("*.note").exclude("*.version")
				.deepSerialize(resultMap);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		responseHeaders.set("Access-Control-Allow-Methods", "GET");
		responseHeaders.set("Access-Control-Allow-Headers", "");
		responseHeaders.set("Access-Control-Max-Age", "86400");

		if (callback.trim().length() > 0) {
			json = callback + "(" + json + ")";
		}

		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);

	}

	private Set<Destinatario> findDestinatarioInIds(String commaList) {

		String query = "SELECT o FROM Destinatario AS o where o.id in ("
				+ commaList + ") order by o.ragioneSociale desc";
		EntityManager em = Destinatario.entityManager();
		TypedQuery<Destinatario> q = em.createQuery(query, Destinatario.class);

		Set<Destinatario> resultList = new HashSet<Destinatario>(
				q.getResultList());

		return resultList;

	}

	private Document findDocumentByIdSmall(long id) {

		EntityManager em = Document.entityManager();
		String qlString = "SELECT o FROM Document AS o where o.id = :id";
		TypedQuery<Document> tquery = em.createQuery(qlString, Document.class);
		tquery.setParameter("id", id);
		Document tmpResult = tquery.getResultList().get(0);
		tmpResult.setContent(new byte[] {});
		return tmpResult;

	}

	private String getDavDestination(Document tmpDoc) {
		return File.separatorChar + "tmp" + File.separatorChar + "webdav"
				+ File.separatorChar + tmpDoc.getStruttura().getDescrizione()
				+ File.separatorChar + tmpDoc.getProgetto().getDescrizione()
				+ File.separatorChar
				+ tmpDoc.getTipoComunicazione().getDescrizione()
				+ File.separatorChar
				+ tmpDoc.getTipoProtocollo().getDescrizione()
				+ File.separatorChar;
	}

	private String getDavTrashDestination(Document tmpDoc, String trashFolder) {
		return File.separatorChar + "tmp" + File.separatorChar + "webdav"
				+ File.separatorChar + trashFolder + File.separatorChar
				+ tmpDoc.getStruttura().getDescrizione() + File.separatorChar
				+ tmpDoc.getProgetto().getDescrizione() + File.separatorChar
				+ tmpDoc.getTipoComunicazione().getDescrizione()
				+ File.separatorChar
				+ tmpDoc.getTipoProtocollo().getDescrizione()
				+ File.separatorChar;
	}

}
