package siap.siep.penapecuniaria.action;

/**
* <p>Title: ActInserisciAnnullaPartita</p>
* <p>Description: Classe Action per l'inserimento di </p> 
* <p>"Annotazione Annullamento Partita di Credito" </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAnnullaPartita extends ActionSiap 
                                              implements ICostantiPenaPecuniaria 
{
 /*****************************************************************************
  * Azione di Inserimento Annulla Partita di Credito
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException 
  {
    
	FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    DettaglioFascicoloModel lDettaglioFascicolo = null;
    IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
    lDettaglioFascicolo = lCtrlFas.ExDettaglioFascicoloSiep(lFasMod.getIdFascicoloSiep());
    
    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
	
	
	RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
    lRicMod.setAnnoPartita                 ( getRequestBigDecimalParameter ( CAMPO_ANNO_PARTITA) );
    lRicMod.setNumPartita                  ( getRequestBigDecimalParameter ( CAMPO_NUM_PARTITA) );
    lRicMod.setNumExCampione               ( getRequestStringParameter     ( CAMPO_NUM_EX_CAMPIONE) );
  //  lRicMod.setProtCircosrizioneDoganale   ( getRequestStringParameter     ( CAMPO_PROT_CIRCOSRIZIONE_DOGANALE) );
    lRicMod.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
     
	ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE )) );
	lRicMod.setCodLuogoEmittente( lComMod.getCodComune());
    lRicMod.setDataRicezioneAtto           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_RICEZIONE_ATTO,CAMPO_MESE_DATA_RICEZIONE_ATTO,CAMPO_GIORNO_DATA_RICEZIONE_ATTO) );
    lRicMod.setDataIscrizioneAtto          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ISCRIZIONE_ATTO,CAMPO_MESE_DATA_ISCRIZIONE_ATTO,CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO) );
    lRicMod.setDataEsazione                ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ANNULLA,CAMPO_MESE_DATA_ANNULLA,CAMPO_GIORNO_DATA_ANNULLA) );
    lRicMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lRicMod.setDataInserimento(DateUtils.getSysDate());
    lRicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lRicMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
    lRicMod.setCodTipoSanzione("03"); // annullamento partita di credito
		

// mettiamo codtipoevento = 01 , come provvedimento generico, 
//	tanto poi lo specifico è in codtipoprovvedimento = 25 (Annotazione), e
//                           in codmotivo = 0943 (Annulla Partita)		
    EventoModel lEveMod = new EventoModel();
	lEveMod.setCodTipoEvento("01");
	lEveMod.setCodTipoProvvedimento("12");
	lEveMod.setCodMotivo("0943");
	
	lEveMod.setFasSieIdFascicoloSiep( lFasMod.getIdFascicoloSiep() );
	lEveMod.setDataEmissione( getRequestDateParameter( CAMPO_ANNO_DATA_RICEZIONE_ATTO,CAMPO_MESE_DATA_RICEZIONE_ATTO,CAMPO_GIORNO_DATA_RICEZIONE_ATTO) );
	  
	lEveMod.setCodLuogoEmittente(lRicMod.getCodLuogoEmittente());
	lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
	lEveMod.setCodEsito("-");
	lEveMod.setCodLuogoDestinatario("-");
	lEveMod.setCodTipoUfficioDestinatario("-");
	lEveMod.setCodUfficioDestinatario("-");
	lEveMod.setFlagStampaSiep("S");
	lEveMod.setFlagVideoSiep("S");
	lEveMod.setFlagDocumentoRegistrato("S");

	lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	lEveMod.setDataInserimento(DateUtils.getSysDate());
	lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	lEveMod.setCodMagistrato(lDettaglioFascicolo.getMagistratoCompetente().getMagistrato().getCodMagistrato());

	/* Insert EVENTO	
		EventoModel lEveRitMod = new EventoModel();
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
	lEveRitMod = lCtrlEve.ExInserisciEvento(lEveMod);   
		*/	
	
	// Controllo Esistenza pena residua per quel fascicolo
	PenaResiduaModel lPenaResMod = new PenaResiduaModel();
	IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
	lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFasMod.getIdFascicoloSiep());	  
	 
	EventoNotificaModel lEveNotMod = new EventoNotificaModel();
	lEveNotMod.setEvento(lEveMod);
	
//	 Inserisco Stato_Procedimento se Fascicolo è ARCHIVIATO/DEFINITO
	String StatoPro = null;
	if(lFasMod.getCodStatoFascicolo().equals("01")) StatoPro ="0344";
	
	IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
	RichiestaConversioneModel lRicRetMod= lCtrlRic.ExInserisciRichiestaEvento
												(lRicMod, lEveNotMod, lPenaResMod,StatoPro);

    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.penapecuniaria.action.ActLoadDettaglioAnnullaPartita";
    lPage += "&" + CAMPO_ID_RICHIESTA_CONVERSIONE + "=" + lRicRetMod.getIdRichiestaConversione().toString();

    return lPage;
  }
}