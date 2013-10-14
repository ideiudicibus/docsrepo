package it.crinali.www.archprot.domain;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.classpath.operations.jsr303.RooUploadedFile;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = {"findDocumentsByOggettoLikeOrNumeroProtocolloLikeOrTipoProtocolloOrDataProtocolloBetweenOrStrutturaOrProgettoOrContatto" })
@DynamicUpdate
public class Document {
    /**
     */
    @NotNull
    @Size(max = 100)
    private String oggetto;

    /**
     */
    @NotNull
    private String numeroProtocollo;

    /**
     */
    @Enumerated
    @NotNull
    private TipoProtocollo tipoProtocollo;

    /**
     */
    @NotNull
    @OneToOne
    private Progetto progetto;

    /**
     */
    @NotNull
    @OneToOne
    private Struttura struttura;

    /**
     */
    @NotNull
    @OneToOne
    private Contatto contatto;

    /**
     */
    private String nomeFile;

    /**
     */
    private Long size;
    /**
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotNull
    private byte[] content;
    /**
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] thumbnail;

    /**
     */
    private String contentType;

    @Transient
    @Size(max = 100)
    private String url;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @NotNull
    private Date dataProtocollo;

	public static Query findDocumentsByTemplate(String oggetto,
			String numeroProtocollo, TipoProtocollo tipoProtocollo,
			Date minDataProtocollo, Date maxDataProtocollo,
			Struttura struttura, Progetto progetto, Contatto contatto) {
		return null;
		
	}
}
