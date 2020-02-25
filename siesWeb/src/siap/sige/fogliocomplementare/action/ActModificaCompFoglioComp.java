package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActModificaCompFoglioComp </p>
 * <p>Description: Azione per la modifica del Foglio Complementare.
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public class ActModificaCompFoglioComp extends ActionSige implements ICostantiFoglioComp
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    setLinkRitorno(); // Gestione del punto di ritorno
    
    // Evento. Legge l'evento collegato al decreto.
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    EventoModel lEveMod = new EventoModel();
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

    // Valorizzazione del Documento Allegato
    DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
    aDocAllegato.setIdDocumentoAllegato( getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO) );
    aDocAllegato.setDataEmissione( getRequestDateParameter( ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE,
    														ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE,
    														ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE ) );

    aDocAllegato.setCodTipoDocumento("06");
    aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));

    // La data trasmissione viene impostata solo quando si effettua la Trasmissione del FC al SIC
    // In fase di modifica trasmissione viene aggiornata la data di invio ultimo aggiornamento (DATA_ULT_INVIO)
    // mentre la data trasmissione rimane invariata
/*
    aDocAllegato.setDataTrasmissione( DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")),"dd/MM/yyyy") );
    aDocAllegato.setDataUltInvio( DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")),"dd/MM/yyyy") );
*/
    aDocAllegato.setEveIdEvento(lIdEvento);
    aDocAllegato.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    aDocAllegato.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    aDocAllegato.setDataAggiornamento(DateUtils.getSysDate());
    //aDocAllegato.setComuneSedeGiudiziaria(lDescSedeGiudiziaria);
    aDocAllegato.setComuneSedeGiudiziaria(null);
    // Recupero il valore della combo "Motivazione non Inviato" 
    String flagMotivoNonInvio = null;
    if(!this.isRequestParameterNullObj(ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO)){
    	flagMotivoNonInvio = getRequestStringParameter(ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO);
    }

    aDocAllegato.setCodMotivazioneNonInvio(flagMotivoNonInvio);
    
    if(flagMotivoNonInvio != null && flagMotivoNonInvio.equals("01")){
    	// recupero il campo "Data Inserimento Manuale" 
    	aDocAllegato.setDataInsMan(getRequestDateParameter( ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE,
    														ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE,
    														ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE ));   	
    } else if (flagMotivoNonInvio != null && flagMotivoNonInvio.equals("02")){
        aDocAllegato.setDataInsMan(null);
    }
    
    if (!isRequestParameterNullObj(ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO)){
    	aDocAllegato.setDescrizioneNonInvio(getRequestStringParameter(ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO));    
    }
    
    
    if(!this.isRequestParameterNullObj("Provenienza") && super.getRequestStringParameter("Provenienza").equalsIgnoreCase("tastoFunzione")){
        aDocAllegato.setDataInsMan(getRequestDateParameter( ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE,
		ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE,
		ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE ));  
    }
    
    
    // viene modificata solo quando si effettua una nuova trasmissione    
    aDocAllegato.setDataUltInvio(null);

    // Controller del DocumentoAllegato
    IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    lDocCtrl.ExModificaFoglioComplementare(aDocAllegato, "-", null);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return IWebConstants.PG_MAIN + "?" + 
           IWebConstants.ACTION_FIELD + 
           "=siap.sige.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&"+
           ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString()+"&Provenienza=null";
  }

}