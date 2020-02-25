package siap.siep.fogliocomplementare.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>Title: ActModificaCompFoglioComp </p>
 * <p>Description: Azione specializzazione per la modifica del foglio complementare NSC
 * <p>Created: A.S.</p> 
 * @version 1.0
 */
public class ActModificaCompFoglioComp extends ActionSiap implements ICostantiProvvedimento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    setLinkRitorno(); // Gestione del punto di ritorno
    
    // Controller del DocumentoAllegato
    IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    
   
    // Legge l'evento
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO); 
    
    // Legge l'Documento Allegato
    BigDecimal IdDocumentoAllegato =  getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO) ; 
    
    DocumentoAllegatoModel aDocAllegato = null;
    IDocumentoAllegato mDocAllCtrl = null;
    mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();       
    //mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lEveNotMod.getEvento().getEveIdEvento(),"06");
    aDocAllegato = mDocAllCtrl.ExRicercaDocumentoAllegatoByKey(IdDocumentoAllegato);
    
    EventoModel lEveMod = new EventoModel();
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

    // Valorizzazione del Documento Allegato
    aDocAllegato.setDataEmissione( aDocAllegato.getDataEmissione() );
    aDocAllegato.setCodTipoDocumento("06");
    aDocAllegato.setAnnoFoglioComplementare(aDocAllegato.getAnnoFoglioComplementare());
    aDocAllegato.setDataTrasmissione(null );
    aDocAllegato.setDataUltInvio( null );
    aDocAllegato.setEveIdEvento(lIdEvento);
    aDocAllegato.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    aDocAllegato.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    aDocAllegato.setDataAggiornamento(DateUtils.getSysDate());
    aDocAllegato.setComuneSedeGiudiziaria(null);
    
    // Imposta la Combo contenente le Motivazioni non Inviato FC.
    Option lOption = new Option( DecodificheManager.getInstance().getMotivoNonInvio() );
    setRequestAttribute("motivoNonInvio", "" + lOption );
    setRequestAttribute("documentoAllegato", aDocAllegato);
    setRequestAttribute("evento", lEveMod);
    
    
    // Recupero il valore della combo "Motivazione non Inviato" 
    String flagMotivoNonInvio = null;
    if(!this.isRequestParameterNullObj(ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO)){
    	flagMotivoNonInvio = getRequestStringParameter(ICostantiProvvedimento.CAMPO_COD_MOTIVO_NON_INVIO);
    }

    aDocAllegato.setCodMotivazioneNonInvio(flagMotivoNonInvio);
    
    if(flagMotivoNonInvio != null && flagMotivoNonInvio.equals("01")){ 
    	// recupero il campo "Data Inserimento Manuale" 
    	aDocAllegato.setDataInsMan(getRequestDateParameter( ICostantiProvvedimento.CAMPO_ANNO_DATA_INS_MANUALE,
                                                            ICostantiProvvedimento.CAMPO_MESE_DATA_INS_MANUALE,
                                                            ICostantiProvvedimento.CAMPO_GIORNO_DATA_INS_MANUALE ));   	
    } else if (flagMotivoNonInvio != null && flagMotivoNonInvio.equals("02")){
        aDocAllegato.setDataInsMan(null);
    }
    
    if (!isRequestParameterNullObj(ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO)){
    	aDocAllegato.setDescrizioneNonInvio(getRequestStringParameter(ICostantiProvvedimento.CAMPO_DESCR_MOTIVO_NON_INVIO));    
    }
    // viene modificata solo quando si effettua una nuova trasmissione    
    aDocAllegato.setDataUltInvio(null);

    // modifica Foglio Complementare Nsc
    lDocCtrl.ExModificaFoglioComplementareNsc(aDocAllegato, lEveMod.getCodTipoProvvedimento());
    
    // Parametro per individuare se provengo dall'icona "Compilazione Foglio Complementare".
    String provenienza = null;
    if (!isRequestParameterNullObj("Provenienza")){
	    provenienza = getRequestStringParameter("Provenienza");
	    setRequestAttribute("provenienza", provenienza);
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return IWebConstants.PG_MAIN + "?" + 
           IWebConstants.ACTION_FIELD + 
           "=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&"+
           ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString()+"&Provenienza=null";
    
  }

}