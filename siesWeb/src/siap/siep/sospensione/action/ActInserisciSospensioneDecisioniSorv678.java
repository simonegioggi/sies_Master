package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>Title: ActInserisciSospensioneDecisioniSorv678</p>
 * <p>Description: Classe Action per l'inserimento di Decisione della sorveglianza di Sospensione della pena art. 678</p>

 * @since MEV_9-SIEP 03.2024
 */
public class ActInserisciSospensioneDecisioniSorv678 extends ActMisuraAlternativa
    implements ICostantiSospensione {

  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  private SospensioneModel cSospModel;
  private PenaResiduaModel cPenaResiduaNuova;
  
  public String processRequest() throws Exception {

	    String tipoOperazione = null;
	    if (!isRequestParameterNullObj("tipoOperazione"))
	      tipoOperazione = getRequestStringParameter("tipoOperazione");
	    
	    if ("MODIFICA".equals(tipoOperazione)){
	      BigDecimal idEventoOld = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
	      
	      siesLogger.debug("Sono in modifica procedo alla cancellazione dell'evento con id = "+idEventoOld);
	      IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
	      EventoModel lEveModRic = lCtrlEvento.ExRicercaEventoByKey(idEventoOld);
	      
	      IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	      lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
	      siesLogger.debug("Evento cancellato proseguo con un nuovo inserimento");
	    }
    
      // calcolo pena residua e sospensione 
      this.calcolaSospensione();
      
      MisuraAlternativaModel lMisuraAltModel = null;
      // Se ordinanza selezionata dalla lista e non modificata, recupero i dati
      // dalla tabella misura alternativa
      BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
      IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
      if (lIdOrdinanza != null)
        lMisuraAltModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
      
      if (lMisuraAltModel == null) {
      	// MA inserita a mano o modificata
      	lMisuraAltModel = inserisciMisuraAlternativa();
      } else {
      	// la MA è stata selezionata dalla lista ma potrei voler aggiornare il solo campo note
        if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
          lMisuraAltModel.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
        
        Date lDataSospensione = null;
        if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO)) 
          lDataSospensione = getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO,
              ICostantiSospensione.CAMPO_MESE_DATA_INIZIO,
              ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO);    
        lMisuraAltModel.setDataScarcerazione(lDataSospensione);
        // Aggiorno il campo note e data sospensione
        lMisAltCtrl.ExModificaMisuraAlternativa(lMisuraAltModel);
      }
      
      if (this.getRequestStringParameter("tipo").equals("scarcerato"))
        lMisuraAltModel.setCodTipoUfficioScarcerazione("SORV");
      else if (this.getRequestStringParameter("tipo").equals("scarcerare"))
        lMisuraAltModel.setCodTipoUfficioScarcerazione("PROC");

      String codiceMotivo = lMisuraAltModel.getCodTipoMisura();
      EventoNotificaModel lEve = new EventoNotificaModel();

      if ("SORV".equals(lMisuraAltModel.getCodTipoUfficioScarcerazione())) {
        // Detenuto già scarcerato - comunicazione
        lEve.getEvento().setCodTipoProvvedimento("12");
      } else {
        // Detenuto da scarcerare - Ordine di scarcerazione
        lEve.getEvento().setCodTipoProvvedimento("09");
      }

      if (codiceMotivo.equals("0684"))
        lEve.getEvento().setCodMotivo("1404");
      else if (codiceMotivo.equals("0695"))
        lEve.getEvento().setCodMotivo("1415");

      lEve.setEvento (setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
      lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
      lEve.getEvento().setEveIdEvento(lMisuraAltModel.getEveIdEvento());

      NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
      lEve.setNotifiche(lNotifiche);

      cPenaResiduaNuova.setIdPenaResidua(null);

      EventoNotificaModel lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, cPenaResiduaNuova, lMisuraAltModel,
          cSospModel);

      String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
          + "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv678&"
          + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
      return lPage;
    
  }

  /**
   * Calcola la pena residua a seguito della sospensione e il record sospensione mettendoli in sessione
   *  
   * 
   */
  private void calcolaSospensione () throws Exception{
  	
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
  	
    Date lDataSospensione = null;
    if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO)) {
      lDataSospensione = getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO,
          ICostantiSospensione.CAMPO_MESE_DATA_INIZIO,
          ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO);
    }

    // Pena Complessiva
    IPenaComplessiva lCtrlPenComp = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenMod = lCtrlPenComp
        .ExRicercaPenaComplessivaByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

    String lFlagErgastolo = "N";
    // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
    if (lPenMod.getCodTipoPenaDetentiva() != null && lPenMod.getCodTipoPenaDetentiva() != ""
        && (   lPenMod.getCodTipoPenaDetentiva().equals("03")
            || lPenMod.getCodTipoPenaDetentiva().equals("04"))) {
      lFlagErgastolo = "S";
    }

    // PENA RESIDUA
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    // Cerca l'ultima pena residua validata...
    PenaResiduaModel lUltimaPenaResidua = lPenResCtrl
        .ExRicercaPenaResiduaUltimaValidata(lFascicoloModel.getIdFascicoloSiep());
    // ...se non la trova cerca l'ultima in assoluto
    if (lUltimaPenaResidua == null)
      lUltimaPenaResidua = lPenResCtrl
          .ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    cPenaResiduaNuova = new PenaResiduaModel();
    CalendarModel lPenaEspiataSosp = new CalendarModel();
    BigDecimal lNumGiorniLA = new BigDecimal(0);

    if (lFlagErgastolo.equals("N")) {
      siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
      ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
      CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);

      // Pena Ricalcolata sul
      PenaResiduaModel lPenaResiduaIniziale = lCalcoloPenaModel.getPenaDaEspiare(lUltimaPenaResidua.getDataInizio(), null, "all");
      lCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale, lDataSospensione);
      cPenaResiduaNuova = lCalcoloPenaModel.getPenaResiduaRicalcolata();
      lPenaEspiataSosp = lCalcoloPenaModel.getPenaEspiata();
      lNumGiorniLA = new BigDecimal(lCalcoloPenaModel.getLiberazioneAnticipata());

      siesLogger.debug("lPenaResiduaNuova : " + cPenaResiduaNuova);
      siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

      // Vengono settati quei parametri
      // che non vengono gestiti nel CalcoloPenaModel
      cPenaResiduaNuova.setIdPenaResidua(lUltimaPenaResidua.getIdPenaResidua());
      cPenaResiduaNuova.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
      cPenaResiduaNuova.setDiesAQuo(lUltimaPenaResidua.getDiesAQuo());
      cPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
      cPenaResiduaNuova.setFlagValidato("N");
      cPenaResiduaNuova.setFlagPenaSospesa("S");
      cPenaResiduaNuova.setEveIdEvento(null);
    } else { // Se Ergastolo non ricalcolo la pena, ma copio i dati dell'ultimo record
    	cPenaResiduaNuova = lUltimaPenaResidua;

    	cPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
    	cPenaResiduaNuova.setEveIdEvento(null);
    	cPenaResiduaNuova.setFlagPenaSospesa("S");
    	cPenaResiduaNuova.setFlagValidato("N");
    }

    // Gestione della Data Inserimento
    cPenaResiduaNuova.setCodOperatoreInserimento(lCodiceOperatore);
    cPenaResiduaNuova.setCodUfficioInserimento(lCodiceUfficio);
    cPenaResiduaNuova.setDataInserimento(DateUtils.getSysDate());
    cPenaResiduaNuova.setCodOperatoreAggiornamento(null);
    cPenaResiduaNuova.setDataAggiornamento(null);
    cPenaResiduaNuova.setCodUfficioAggiornamento(null);

    if (lFlagErgastolo.equals("S")) {
    	cPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));

      if (lPenMod.getCodTipoPenaDetentiva().equals("03"))
      	cPenaResiduaNuova.setFlagErgastolo("S");
      else if (lPenMod.getCodTipoPenaDetentiva().equals("04"))
      	cPenaResiduaNuova.setFlagErgastolo("D");
    }

    siesLogger.debug("lPenaResiduaNuova completa: " + cPenaResiduaNuova);
    //this.setSessionAttribute("SOSPpenaresidua678", cPenaResiduaNuova);

    // ============================
    // INSERIMENTO SOSPENSIONE
    // ============================
		String PosizioneGiu = getRequestStringParameter (ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);
    if (!lPosMod.isLibero() && lUltimaPenaResidua.getDataInizio() != null) {
      siesLogger.debug("Prepara SopensioneModel");

      cSospModel = new SospensioneModel();
      cSospModel.setDataInizio(lDataSospensione);

      if (lFlagErgastolo.equals("N")) {
      	cSospModel.setNumAnniPenaResiduaReclus   (cPenaResiduaNuova.getNumAnniReclusione());
      	cSospModel.setNumMesiPenaResiduaReclus   (cPenaResiduaNuova.getNumMesiReclusione());
      	cSospModel.setNumGiorniPenaResiduaReclus (cPenaResiduaNuova.getNumGiorniReclusione());
      	cSospModel.setNumAnniPenaResiduaArres    (cPenaResiduaNuova.getNumAnniArresto());
      	cSospModel.setNumMesiPenaResiduaArres    (cPenaResiduaNuova.getNumMesiArresto());
      	cSospModel.setNumGiorniPenaResiduaArres  (cPenaResiduaNuova.getNumGiorniArresto());

      	cSospModel.setAmmendaResidua(cPenaResiduaNuova.getImportoAmmenda());
      	cSospModel.setMultaResidua(cPenaResiduaNuova.getImportoMulta());

      	cSospModel.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
      	cSospModel.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
      	cSospModel.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
      } 
      else // Nel caso di ergastolo
      {
        // Calcolo la pena espiata come intervallo tra la data inizio e la data
        // di sospensione (considerato come giorno espiato)
        CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
        CalendarUtil lCalUtil = new CalendarUtil();

        lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
        lCalPenaEspiataCalcoloErg.setDataFine(lDataSospensione);

        lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
        lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

        cSospModel.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
        cSospModel.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
        cSospModel.setNumGiorniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
      }

      cSospModel.setNumGiorniLibanticipata(lNumGiorniLA);
      cSospModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

      cSospModel.setCodOperatoreInserimento(lCodiceOperatore);
      cSospModel.setCodUfficioInserimento(lCodiceUfficio);
      cSospModel.setDataInserimento(DateUtils.getSysDate());

      //this.setSessionAttribute("SOSPENSIONE_678", lSospModel);  	
    }
  }

  /**
   * Inserisce il provvedimento della sorveglianza Decreto/Ordinanza se simulato SIEP o modificato SIEP
   * 
   */
  private MisuraAlternativaModel inserisciMisuraAlternativa() throws Exception {
    // inserisco evento del TDS
    String lTipoDecisione = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);
    String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
                                                                    getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
    ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
    
    Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
                                                 ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
                                                 ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

    EventoNotificaModel lEveMod = new EventoNotificaModel();
    lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

    lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
        lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

    lEveMod.getEvento().setCodEsito("0270"); // Applica ex art. 678 comma 1 ter cpp 
    
    // setto il deposito ordinanza
    DepositoDecretoModel lDepDecMod = null;
    DepositoOrdinanzaPcModel lDepOrdMod = null;

    if (lTipoDecisione.equals("03"))
      lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
    else if (lTipoDecisione.equals("02"))
      lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

    // setto il tenore
    TenoreModel lTenMod = setTenore(new BigDecimal(1), "0270");

    // misura alternativa
    String lUfficioScarc = "-";
    if (!this.isRequestParameterNullObj("tipo")) {
      if (this.getRequestStringParameter("tipo").equals("scarcerato"))
        lUfficioScarc = "SORV";
      else if (this.getRequestStringParameter("tipo").equals("scarcerare"))
        lUfficioScarc = "PROC";
    }

    MisuraAlternativaModel lMisMod = null;
    lMisMod = setMisuraAlternativa(lTipoDecisione, "CO", lCodiceUffEmi,
        getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);
    
    Date lDataSospensione = null;
    if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO)) 
      lDataSospensione = getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO,
          ICostantiSospensione.CAMPO_MESE_DATA_INIZIO,
          ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO);    
    lMisMod.setDataScarcerazione(lDataSospensione);

    MisuraAlternativaModel lMisuraModel = null;
    IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
    if (lTipoDecisione.equals("03"))
      lMisuraModel = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod,
          lTenMod, lMisMod);
    else if (lTipoDecisione.equals("02"))
      lMisuraModel = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod, lTenMod,
          lMisMod);  	
    
    return lMisuraModel;
  }
}