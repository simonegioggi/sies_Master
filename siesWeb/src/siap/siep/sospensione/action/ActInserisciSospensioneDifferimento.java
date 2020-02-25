package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciSospensioneDifferimento</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione Differimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciSospensioneDifferimento extends ActionSiap
                                                 implements ICostantiSospensione,
                                                            ICostantiDecretoOrdinanzaSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Azione di Inserimento del Sospensione Differimento
   * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    //==========================================================================
    // Carico i dati del Decreto Ordinanza SIEP recuperandoli dalla form
    //==========================================================================
    DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

    // l'id dell'eventuale record presente
    lDecMod.setIdDecretoOrdinanzaSiep(getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP));

    lDecMod.setDataRicezioneProvvedimento (getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO, CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO, CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO));
    lDecMod.setDataEmissioneProvvedimento (getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO, CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO, CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO));
lDecMod.setCodTipoRegistroOrdinanza("0001"); //SIUS
    lDecMod.setAnnoRegistro               (getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO));
    lDecMod.setNumRegistro                (getRequestBigDecimalParameter(CAMPO_NUM_REGISTRO));
    lDecMod.setAnnoProvvedimento          (getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
    lDecMod.setNumProvvedimento           (getRequestBigDecimalParameter(CAMPO_NUM_PROVVEDIMENTO));
    lDecMod.setCodTipoProvvedimento       (getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
    lDecMod.setCodOggettoProcedimento     (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO));
    lDecMod.setCodTipoAutoritaEmittente   (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
    lDecMod.setCodLuogoEmittente(lComMod.getCodComune());

//Controllo sull'esistenza dell'ufficio per quel comune
    String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE) );

    lDecMod.setDataDifferimento (getRequestDateParameter(CAMPO_ANNO_DATA_DIFFERIMENTO, CAMPO_MESE_DATA_DIFFERIMENTO, CAMPO_GIORNO_DATA_DIFFERIMENTO));
    lDecMod.setDataRinvio       (getRequestDateParameter(CAMPO_ANNO_DATA_RINVIO, CAMPO_MESE_DATA_RINVIO, CAMPO_GIORNO_DATA_RINVIO));
//    lDecMod.setDataFineInterruzione( getRequestDateParameter( CAMPO_ANNO_DATA_FINE_INTERRUZIONE,CAMPO_MESE_DATA_FINE_INTERRUZIONE,CAMPO_GIORNO_DATA_FINE_INTERRUZIONE) );
//    lDecMod.setDataDepositoIstanza( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,CAMPO_MESE_DATA_DEPOSITO_ISTANZA,CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA) );
//    lDecMod.setDataInterruzionePena( getRequestDateParameter( CAMPO_ANNO_DATA_INTERRUZIONE_PENA,CAMPO_MESE_DATA_INTERRUZIONE_PENA,CAMPO_GIORNO_DATA_INTERRUZIONE_PENA) );
    lDecMod.setCodOggettoDecisione(getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE));

    // Combo "Contenuto decisione"
    // - (C014) Concessione Rinvio Dell'Esecuzione della Pena (Art. 684 Cpp)
    // - (U003) Rinvio Esecuzione Ex Art.684 Cpp C.2
    if(getRequestStringParameter(CAMPO_COD_OGGETTO_DECISIONE).equals("C014"))
    {
      lDecMod.setCodEsito("0035");  // "Concede per un periodo"
    }
    else
    {
      lDecMod.setCodEsito("0001");  // "Concede"
    }

    lDecMod.setMotivazioni              (getRequestStringParameter(CAMPO_MOTIVAZIONI));
    lDecMod.setFlagScarcerareScarcerato (getRequestStringParameter(CAMPO_FLAG_SCARCERARE_SCARCERATO));
    lDecMod.setCodContenutoDecreto      ("-");
    lDecMod.setAltraAutorita            (getRequestStringParameter(CAMPO_ALTRA_AUTORITA));
//    lDecMod.setAltroLuogo( getRequestStringParameter( CAMPO_ALTRO_LUOGO) );


    lDecMod.setFlagElaborato("N");

//    lDecMod.setDataEspulsione( getRequestDateParameter( CAMPO_ANNO_DATA_ESPULSIONE,CAMPO_MESE_DATA_ESPULSIONE,CAMPO_GIORNO_DATA_ESPULSIONE) );

    lDecMod.setFlagDecisioneTribunale(getRequestStringParameter("flagDecisione"));

    lDecMod.setNumAnniRinvio   (getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_ANNI_RINVIO));
    lDecMod.setNumMesiRinvio   (getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_MESI_RINVIO));
    lDecMod.setNumGiorniRinvio (getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_GIORNI_RINVIO));
//    lDecMod.setDataRevocaSospensione( getRequestDateParameter( CAMPO_ANNO_DATA_REVOCA_SOSPENSIONE,CAMPO_MESE_DATA_REVOCA_SOSPENSIONE,CAMPO_GIORNO_DATA_REVOCA_SOSPENSIONE) );
    lDecMod.setFasSieIdFascicoloSiep(lIdFascicolo);

    lDecMod.setCodOperatoreInserimento (this.getCodUtenteConnesso());
    lDecMod.setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());
    lDecMod.setDataInserimento         (DateUtils.getSysDate());

    //==========================================================================
    // Calcolo la DATA RINVIO FINO AL se non specificata
    //==========================================================================
    // Nel caso sia stato inserito il quantum di rinvio e non sia stata inserita
    // la DATA_RINVIO, la DATA_RINVIO viene calcolata in automatico a partire
    // dalla DATA_DIFFERIMENTO (in ogni caso non viene calcolata se inserita dall'utente)
    CalendarModel lCalMod = new CalendarModel();
    lCalMod.setNumAnni   (lDecMod.getNumAnniRinvio());
    lCalMod.setNumMesi   (lDecMod.getNumMesiRinvio());
    lCalMod.setNumGiorni (lDecMod.getNumGiorniRinvio());

    CalendarUtil lCalUtil = new CalendarUtil();

    if(  !lCalUtil.isZero(lCalMod)
      && lDecMod.getDataRinvio() == null
      && lDecMod.getDataDifferimento() != null )
    {
      ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
      Date lDataRinvio = lCalPenCtrl.exCalcolaNuovaDataFine(lDecMod.getDataDifferimento(), lCalMod, false);
      lDecMod.setDataRinvio(lDataRinvio);
    }

    //==========================================================================
    // Creo l'EVENTO sius (01) da associare al decreto/ordinanza
    //==========================================================================
    EventoModel lEveMod = new EventoModel();

    lEveMod.setCodTipoEvento("01");  // Provvedimento
    lEveMod.setCodTipoProvvedimento (lDecMod.getCodTipoProvvedimento());
    lEveMod.setCodMotivo            (lDecMod.getCodOggettoProcedimento());
    lEveMod.setCodUfficioEmittente  (lCodiceUffEmittente);
    lEveMod.setCodLuogoEmittente    (lComMod.getCodComune());
    lEveMod.setCodTipoUfficioDestinatario("-");
    lEveMod.setCodLuogoDestinatario("-");
    lEveMod.setCodEsito("-");
    lEveMod.setDataEmissione        (lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setDataTrasmissioneAtti (lDecMod.getDataEmissioneProvvedimento());
    lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lEveMod.setFlagVideoSiep("S");
    lEveMod.setFlagStampaSiep("S");

    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    //lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lEveMod.setCodUfficioInserimento(lCodiceUffEmittente);  // STUB 15/09/2005
    lEveMod.setDataInserimento(DateUtils.getSysDate());

    //==========================================================================
    //
    //==========================================================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

    IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrdModRet = lCtrl.ExInserisciOModificaDecretoSospensione(lDecMod, lEveMod, "D", lCalcoloPenaModel);

    // Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneDifferimento&" + CAMPO_ID_DECRETO_ORDINANZA_SIEP +
         "=" + lDecOrdModRet.getIdDecretoOrdinanzaSiep().toString();

    return lPage;
  }
}