// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.domain;

import it.crinali.www.archprot.domain.Contatto;
import it.crinali.www.archprot.domain.Destinatario;
import it.crinali.www.archprot.domain.Document;
import it.crinali.www.archprot.domain.Note;
import it.crinali.www.archprot.domain.Progetto;
import it.crinali.www.archprot.domain.Struttura;
import it.crinali.www.archprot.domain.TipoComunicazione;
import it.crinali.www.archprot.domain.TipoProtocollo;
import java.util.Date;
import java.util.Set;

privileged aspect Document_Roo_JavaBean {
    
    public String Document.getOggetto() {
        return this.oggetto;
    }
    
    public void Document.setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }
    
    public String Document.getNumeroProtocollo() {
        return this.numeroProtocollo;
    }
    
    public void Document.setNumeroProtocollo(String numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }
    
    public TipoProtocollo Document.getTipoProtocollo() {
        return this.tipoProtocollo;
    }
    
    public void Document.setTipoProtocollo(TipoProtocollo tipoProtocollo) {
        this.tipoProtocollo = tipoProtocollo;
    }
    
    public TipoComunicazione Document.getTipoComunicazione() {
        return this.tipoComunicazione;
    }
    
    public void Document.setTipoComunicazione(TipoComunicazione tipoComunicazione) {
        this.tipoComunicazione = tipoComunicazione;
    }
    
    public Note Document.getNote() {
        return this.note;
    }
    
    public void Document.setNote(Note note) {
        this.note = note;
    }
    
    public Progetto Document.getProgetto() {
        return this.progetto;
    }
    
    public void Document.setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }
    
    public Struttura Document.getStruttura() {
        return this.struttura;
    }
    
    public void Document.setStruttura(Struttura struttura) {
        this.struttura = struttura;
    }
    
    public Contatto Document.getContatto() {
        return this.contatto;
    }
    
    public void Document.setContatto(Contatto contatto) {
        this.contatto = contatto;
    }
    
    public Set<Destinatario> Document.getDestinatari() {
        return this.destinatari;
    }
    
    public void Document.setDestinatari(Set<Destinatario> destinatari) {
        this.destinatari = destinatari;
    }
    
    public String Document.getNomeFile() {
        return this.nomeFile;
    }
    
    public void Document.setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }
    
    public Long Document.getSize() {
        return this.size;
    }
    
    public void Document.setSize(Long size) {
        this.size = size;
    }
    
    public byte[] Document.getContent() {
        return this.content;
    }
    
    public void Document.setContent(byte[] content) {
        this.content = content;
    }
    
    public byte[] Document.getThumbnail() {
        return this.thumbnail;
    }
    
    public void Document.setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public String Document.getContentType() {
        return this.contentType;
    }
    
    public void Document.setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String Document.getUrl() {
        return this.url;
    }
    
    public void Document.setUrl(String url) {
        this.url = url;
    }
    
    public Date Document.getDataProtocollo() {
        return this.dataProtocollo;
    }
    
    public void Document.setDataProtocollo(Date dataProtocollo) {
        this.dataProtocollo = dataProtocollo;
    }
    
    public String Document.getPath() {
        return this.path;
    }
    
    public void Document.setPath(String path) {
        this.path = path;
    }
    
}
