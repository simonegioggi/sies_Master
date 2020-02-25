package siap.sige.impugnazione.action;

/**
* <p>Title: ActInserisciImpugnazione</p>
* <p>Description: Classe Action per l'inserimento di Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActInserisciImpugnazioneSige extends ActionSiap implements ICostantiImpugnazioneSige {
/**
* Azione di Inserimento dell' Impugnazione
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException {
    ImpugnazioneSigeModel lImpMod = new ImpugnazioneSigeModel();
   
    String lCodTipoImpugnazione=super.getRequestStringParameter(CAMPO_COD_TIPO_IMPUGNAZIONE);

    FascicoloSigeEstesoModel lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
    
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();

    // Preleva dalla request lIdProvvedimento.
    String lIdProvvedimento = this.getRequestStringParameter( ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE );

    lImpMod.setCodOperatoreInserimento(lCodiceOperatore);
    lImpMod.setCodUfficioInserimento(lCodiceUfficio);
    lImpMod.setDataInserimento(DateUtils.getSysDate());
    lImpMod.setCodTipoImpugnazione(lCodTipoImpugnazione);
    //Il campo Progr_S7 viene impostato nel controller.
    lImpMod.setAnnoS7( new BigDecimal(DateUtils.getSysDate("yyyy"))  );
    lImpMod.setSoggettoImpugnante( getRequestStringParameter( CAMPO_SOGGETTO_IMPUGNANTE) );
    lImpMod.setDataRicorso( getRequestDateParameter( CAMPO_ANNO_DATA_RICORSO,CAMPO_MESE_DATA_RICORSO,CAMPO_GIORNO_DATA_RICORSO) );
    lImpMod.setDataArrivoCancelleria( getRequestDateParameter( CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,CAMPO_MESE_DATA_ARRIVO_CANCELLERIA,CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA) );
    lImpMod.setCodAutoritaDestinataria( "-");  
    if (lCodTipoImpugnazione.equals("01")) {  
        lImpMod.setDataTrasmissioneAtti( getRequestDateParameter( CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,CAMPO_MESE_DATA_TRASMISSIONE_ATTI,CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI) );
        lImpMod.setCodAutoritaDestinataria( getRequestStringParameter( CAMPO_COD_AUTORITA_DESTINATARIA) );
    } 
    
    // Se l'esito dell'opposizione è "Converte Ricorso in Cassazione"
	 // Valorizzo il campo ID_OPPOSIZIONE_CONV_RICORSO
    if (!super.isRequestParameterNullEmptyObj(CAMPO_ID_IMPUGNAZIONE)) {
  	  IImpugnazioneSige ctrlImpugnazione = SIGELookupRemote.getImpugnazioneSigeRemote();
  	  ImpugnazioneSigeModel impugnazioneOpp = ctrlImpugnazione.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
  	  if(impugnazioneOpp != null && impugnazioneOpp.getCodTenoreDecisione() !=  null && 
  			impugnazioneOpp.getCodTenoreDecisione().equals(COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE)){
  		  lImpMod.setIdOpposizioneConvRicorso(impugnazioneOpp.getIdImpugnazioneSige());
  	  }	  
    }

    lImpMod.setAnnotazione( getRequestStringParameter( CAMPO_NOTE) ); // Da inserire nella form      
    IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
    ImpugnazioneSigeModel llImpModRet = lCtrl.ExInserisciImpugnazione(lImpMod, new BigDecimal(lIdProvvedimento), this.getEvento(), lFasEst.getFascicoloSige());
    
    //Setta la risposta nella request.
    setRequestAttribute("impugnazione", llImpModRet);
      
    if (!super.isRequestParameterNullEmptyObj(CAMPO_ID_IMPUGNAZIONE)) {
    	BigDecimal idImpugnazione = super.getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
    	this.rigettaOpposizione(idImpugnazione, lFasEst);
    }
  
    // Prepara la pagina di destinazione, puntando all'azione di dettaglio.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige");
    lPage.setParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE,""+ llImpModRet.getIdImpugnazioneSige());
    lPage.setParameter(IWebConstants.LINK_RITORNO, "10");
    return "" + lPage;
  }
  
  private void rigettaOpposizione (BigDecimal idImpugnazione, FascicoloSigeEstesoModel lFasEst) throws F3BException{
	  //FascicoloSigeEstesoModel lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
	  IImpugnazioneSige ctrlImpugnazione = SIGELookupRemote.getImpugnazioneSigeRemote();
	  ImpugnazioneSigeModel impugnazione = ctrlImpugnazione.ExRicercaImpugnazioneByKey(idImpugnazione);
	  // Modifica del 06/12/2016
	  if(impugnazione != null && impugnazione.getCodTenoreDecisione() !=  null && 
		 impugnazione.getCodTenoreDecisione().equals(COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE)){
	     // Se l'esito dell'opposizione è "Converte Ricorso in Cassazione" non aggiorno
		 // il codice Tenore Decisione
		 // Valorizzo il campo CONV_RICORSO_IN_CASS
		  impugnazione.setConvRicorsoInCass("S");
	  } else {
		  impugnazione.setCodTenoreDecisione("05");
	  }
	  ctrlImpugnazione.ExImpostaEsitoImpugnazione(impugnazione, this.getEvento(), lFasEst.getFascicoloSige().getIdFascicoloSige());
  }
  
  private EventoModel getEvento () throws F3BException{
	  EventoModel evento=new EventoModel ();
	  evento.setCodTipoEvento("13");
      evento.setCodTipoProvvedimento("15");
      evento.setCodEsito("-");
      evento.setCodMotivo("-");
      evento.setCodTipoUfficioDestinatario("-");
      evento.setCodLuogoDestinatario("-");
      evento.setCodLuogoEmittente("-");
      evento.setCodTipoUfficioEmittente("-");
      evento.setCodUfficioDestinatario(super.getCodUfficioUtenteConnesso());
      evento.setCodUfficioEmittente(super.getCodUfficioUtenteConnesso());
      evento.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
      evento.setCodOperatoreInserimento(super.getUtenteConnesso().getUserId());
      evento.setDataInserimento(new Date());
	  return evento;
  }
}
