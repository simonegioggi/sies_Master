package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActModificaCompFoglioComp </p>
 * <p>Description: Azione specializzazione per la modifica del foglio complementare.
 * <p>Copyright: Eutelia S.p.A.Copyright (c) 2007</p>
 * <p>Company: Eutelia S.p.A.</p>
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
   
    // Evento. Legge l'evento collegato al decreto.
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    EventoModel lEveMod = new EventoModel();
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

    // Valorizzazione del Documento Allegato
    DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
    aDocAllegato.setIdDocumentoAllegato( getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO) );
    aDocAllegato.setDataEmissione( getRequestDateParameter( ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE ) );

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

    // Prelievo Decreto e relativa modifica del Documento Allegato.
    // MEV 10: aggiunta sentenza
    if(lEveMod.getCodTipoProvvedimento().equals("01"))
    {
      IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
      DepositoSentenzaModel lSenMod = lCtrlDS.ExRicercaDepositoSentenzaByEvento(lIdEvento);
      lDocCtrl.ExModificaFoglioComplementare(aDocAllegato, lEveMod.getCodTipoProvvedimento(), lSenMod.getIdDepositoSentenza());
    }
    else if(lEveMod.getCodTipoProvvedimento().equals("02"))
    {
      IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
      DepositoDecretoModel lDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
      lDocCtrl.ExModificaFoglioComplementare(aDocAllegato, lEveMod.getCodTipoProvvedimento(), lDecMod.getIdDepositoDecreto());
    }
    else if(lEveMod.getCodTipoProvvedimento().equals("03"))
    {
      IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      DepositoOrdinanzaPcModel lDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
      lDocCtrl.ExModificaFoglioComplementare(aDocAllegato, lEveMod.getCodTipoProvvedimento(), lDepMod.getIdDepositoOrdinanzaPc());
    }

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return IWebConstants.PG_MAIN + "?" + 
           IWebConstants.ACTION_FIELD + 
           //"=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+
           "=siap.sius.provvedimento.action.ActLoadDettaglioCompFoglioComp&"+
           ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString()+"&Provenienza=null";
  }

}