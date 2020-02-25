package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActInserisciAnnotazioneEspulsione</p> 
 * <p>Description: Classe Action per la l'Inserimento dell'annotazione avvenuta
 * espulsione.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActInserisciAnnotazioneEspulsione extends ActionSiap implements ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * La funzione prevede l'inserimento contestuale di due eventi:
   * 07-27-0926 Il verbale di espulsione
   * 01-12-0927 La Comunicazione della scadenza termini (collegata al verbale)
   * 
   * Quest'ultima può o meno prevedere la stampa in funzione della scelta effettuata
   * sulla form 'Invio Comunicazioni'
   */
  public String processRequest() throws F3BException
  {
    // 
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    //==========================================================================
    // Recupero la Sanzione Sostitutiva In Sentenza
    //==========================================================================
    IPenaComplessiva lCtrlPenaComp = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaSanzioneSostitutivaModel lPenaCompSanzModel = lCtrlPenaComp.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicolo);
    
    SanzioneSostitutivaModel lSanzSost = lPenaCompSanzModel.getSanzioneSostitutiva();
    
    boolean flagNotifiche = false;
    if (getRequestStringParameter("flagInvioComunicazione").equals("SI")){
      flagNotifiche = true;
    }

    //======================================
    // Carico i dati dell'evento Verbale
    //======================================
    EventoModel lEventoVerbale = new EventoModel();

    lEventoVerbale.setFasSieIdFascicoloSiep(lIdFascicolo);

    lEventoVerbale.setCodTipoEvento("07");        //   07 - Verbale
    lEventoVerbale.setCodTipoProvvedimento("27"); //   27 - Verbale Espulsione
    lEventoVerbale.setCodMotivo("0926");          // 0926 - Espulsione Straniero a Titolo  di Sanzione Sostitutiva Alla Detenzione
    lEventoVerbale.setCodEsito("-");
    
    // n.b. questi dati compaiono nel campo 'Autorità' dell'elenco provvedimenti 
    //      del PM
    // n.b. Deve essere un ufficio censito sulla tabella UFFICIO altrimenti falliscono 
    //      le join delle select sugli eventi
    // NON PUO' QUINDI ESSERE L'AUTORITA' FIRMATARIA DEL VERBALE, MA NECESSARIAMENTE
    // L'UFFICIO COLLEGATO ANCHE SE NON SIGNIFICATIVO    
    lEventoVerbale.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
    lEventoVerbale.setCodLuogoEmittente(getCodComuneUtenteConnesso());  

    lEventoVerbale.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, 
                                                            ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, 
                                                            ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
//lEventoVerbale.setDataRicezioneAtti(aValore);
    lEventoVerbale.setDataEspulsioneSanzSost(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_ESPULSIONE_SANZ_SOST, 
                                                                     ICostantiEvento.CAMPO_MESE_DATA_ESPULSIONE_SANZ_SOST, 
                                                                     ICostantiEvento.CAMPO_GIORNO_DATA_ESPULSIONE_SANZ_SOST));
    
    lEventoVerbale.setFlagDocumentoRegistrato("N"); // Viene Validato contestualmente alla Comunicazione
    lEventoVerbale.setFlagStampaSiep("S");
    lEventoVerbale.setFlagVideoSiep("S");
    

    lEventoVerbale.setCodMagistrato("-");
    
    lEventoVerbale.setCodTipoUfficioDestinatario("-");
    lEventoVerbale.setCodUfficioDestinatario("-");
    lEventoVerbale.setCodLuogoDestinatario("-");

    lEventoVerbale.setCodOperatoreInserimento (getCodUtenteConnesso());
    lEventoVerbale.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lEventoVerbale.setDataInserimento         (DateUtils.getSysDate());
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lEventoVerbale = "+lEventoVerbale);
    
    //=====================
    // Carico il verbale
    //=====================
    VerbaleModel lVerbaleMod = new VerbaleModel();
    
    lVerbaleMod.setCodTipoVerbale("05"); // Verbale Espulsione 
    lVerbaleMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));
    
    // Data Emissione = Data Verbale
    lVerbaleMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, 
                                                         ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, 
                                                         ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
    // Data Pervenimento = Data Annotazione
    lVerbaleMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, 
                                                            ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, 
                                                            ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
    // Ufficio Firmatario    
    lVerbaleMod.setCodTipoUfficioFirmatario(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));
    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)));
    lVerbaleMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
    lVerbaleMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));

    lVerbaleMod.setNumGiorniEspulsione (lSanzSost.getNumGiorni());
    lVerbaleMod.setNumMesiEspulsione   (lSanzSost.getNumMesi());
    lVerbaleMod.setNumAnniEspulsione   (lSanzSost.getNumAnni());
    
    lVerbaleMod.setCodOperatoreInserimento (getCodUtenteConnesso());
    lVerbaleMod.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    lVerbaleMod.setDataInserimento         (lEventoVerbale.getDataInserimento());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Verbale = "+lVerbaleMod);
    //.............................
    
    //=====================================================
    // Carico i dati dell'evento Comunicazione/Annotazione
    //=====================================================
    EventoModel lEventoComunicazione = new EventoModel();
    lEventoComunicazione.setFasSieIdFascicoloSiep(lIdFascicolo);

    lEventoComunicazione.setCodTipoEvento("01");        //   01 - Provvedimento
    if (flagNotifiche){
      // Invio comunicazione
      lEventoComunicazione.setCodTipoProvvedimento("12"); // 12 - Comunicazione
    }
    else{
      // Semplice annotazione
      lEventoComunicazione.setCodTipoProvvedimento("25"); // 25 - Annotazione
    }
    lEventoComunicazione.setCodMotivo("0927");          // 0927 - Scadenza termini espulsione
    lEventoComunicazione.setCodEsito("-");
    
    lEventoComunicazione.setCodUfficioEmittente(getCodUfficioUtenteConnesso()); 
    lEventoComunicazione.setCodLuogoEmittente(getCodComuneUtenteConnesso());

    // Data Emissione = Data Annotazione della form 
    lEventoComunicazione.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, 
                                                                  ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, 
                                                                  ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
    
    
    lEventoComunicazione.setFlagDocumentoRegistrato("N");
    lEventoComunicazione.setFlagStampaSiep("S");
    lEventoComunicazione.setFlagVideoSiep("S");
    
    if (flagNotifiche && !this.isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO )){
      lEventoComunicazione.setCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
    }
    else {
      lEventoComunicazione.setCodMagistrato("-");
    }
    
    lEventoComunicazione.setCodLuogoDestinatario("-");
    lEventoComunicazione.setCodTipoUfficioDestinatario("-");
    lEventoComunicazione.setCodUfficioDestinatario("-");
    
    lEventoComunicazione.setDataEspulsioneSanzSost(lEventoVerbale.getDataEspulsioneSanzSost());
    
    lEventoComunicazione.setCodOperatoreInserimento (getCodUtenteConnesso());
    lEventoComunicazione.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
    // Aggiungo 1 sec per visualizzare l'evento dopo il verbale nell'elenco provv del PM
    lEventoComunicazione.setDataInserimento ( DateUtils.moveDateTo(lEventoVerbale.getDataInserimento(), Calendar.SECOND, 1));

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lEventoComunicazione = "+lEventoComunicazione);

    EventoNotificaModel lEvNotModel = new EventoNotificaModel();
    lEvNotModel.setEvento(lEventoComunicazione);
    
      
    //===========================================
    // Carico i dati delle notifiche se previste
    //===========================================
    if (flagNotifiche){
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Carico le notifiche");
      //==========================================================================
      // Notifica a Autorità di Polizia
      //==========================================================================
      NotificaModel lNotModPol = new NotificaModel();

      lNotModPol.setCodEsito("-");
      lNotModPol.setCodTipoNotifica("C"); 
      // Data Invio = Data Annotazione
      lNotModPol.setDataInvio(lEventoComunicazione.getDataEmissione()); 

      lNotModPol.setCodOperatoreInserimento (lEventoComunicazione.getCodOperatoreInserimento());
      lNotModPol.setCodUfficioInserimento   (lEventoComunicazione.getCodUfficioInserimento());
      lNotModPol.setDataInserimento         (lEventoComunicazione.getDataInserimento());

      // Autorità esterna
      AutoritaEsternaModel lAut = new AutoritaEsternaModel();
      String lPolizia     = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
      String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);

      lAut.setCodTipoAutorita(lPolizia);

      lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
      lAut.setCodSede(lComMod.getCodComune());

      if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE)) {
        String lIndirizzo = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE);
        lAut.setDescrizione(lIndirizzo);
      }
      
      lAut.setCodOperatoreInserimento (lEventoComunicazione.getCodOperatoreInserimento());
      lAut.setCodUfficioInserimento   (lEventoComunicazione.getCodUfficioInserimento());
      lAut.setDataInserimento         (lEventoComunicazione.getDataInserimento());

      lNotModPol.setAutoritaEsterna(lAut);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lNotModPol = "+lNotModPol);
      //
      NotificaModel[] lNotifiche = new NotificaModel[1];
      lNotifiche[0] = lNotModPol;
     
      lEvNotModel.setNotifiche(lNotifiche);
    }
    
    //============================================================
    // Effettuo i calcoli della pena: pena espiata - sospensione.
    //============================================================
  	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  	siesLogger.debug("Calcolo Espiato e da Espiare");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
    ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
    CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);
    
    Date lDataDecorrenzaPena = lCalcoloPenaMain.getDataDecorrenzaPena(lIdFascicolo, null);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lDataDecorrenzaPena = "+lDataDecorrenzaPena);
    
    PenaResiduaModel lPenaResiduaNuova = null;
    SospensioneModel lSospensione = null;

    CalendarModel lPenaEspiataSosp = new CalendarModel();

    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaModel lPosGiuRicerca = new PosizioneGiuridicaModel();
    lPosGiuRicerca.setFasSieIdFascicoloSiep(lIdFascicolo);

    PosizioneGiuridicaModel lPosMod = lPosCtrl.ExRicercaPosizioneGiuridicaCorrente(lPosGiuRicerca);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Posizione Giuridica = "+lPosMod);
 
    // Recupero l'ultima pena validata se esiste, altrimenti l'ultima non validata
    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
    if (lUltimaPenaValidata==null){
      lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
    }
      
    // Recupero la data fine pena forzata
    Date lDataFinePenaForzata = null;
    if (!this.isRequestParameterNullObj("dataeditabile")) {
      String isDataEditabile = this.getRequestStringParameter("dataeditabile");
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("isDataEditabile = "+isDataEditabile);
      if (isDataEditabile!=null && isDataEditabile.equals("S"))
      {
        // forzo la data fine pena
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("data editabile");
        if (!this.isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE)){
          lDataFinePenaForzata = getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE, 
                                                         ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, 
                                                         ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE);
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("Data Forzata = "+lDataFinePenaForzata);
        }
      }  
    }
    
    
    if ( !lPosMod.isLibero() )
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Non Libero: calcolo la pena residua");

      // Pena Ricalcolata sul
      try {
        PenaResiduaModel lPenaResiduaIniziale = lCalcoloPenaModel.getPenaDaEspiare(lDataDecorrenzaPena, null,"all");
        if (lDataFinePenaForzata!=null){
          lPenaResiduaIniziale.setDataFine(lDataFinePenaForzata);
        }
        lCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale, lEventoVerbale.getDataEspulsioneSanzSost());
      }
      catch (Exception e){
        throw new F3BException(e);
      }
      
      lPenaResiduaNuova = lCalcoloPenaModel.getPenaResiduaRicalcolata();
      lPenaEspiataSosp = lCalcoloPenaModel.getPenaEspiata();

      //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
      //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

      // Vengono settati quei parametri
      // che non vengono gestiti nel CalcoloPenaModel
      //lPenaResiduaNuova.setIdPenaResidua( lUltimaPenaResidua.getIdPenaResidua() );
      //lPenaResiduaNuova.setFlagErgastolo( lUltimaPenaResidua.getFlagErgastolo() );

      lPenaResiduaNuova.setIdPenaResidua(null);
      lPenaResiduaNuova.setFlagValidato("N");
      lPenaResiduaNuova.setDiesAQuo("S");
    }
    else
    {
      // In caso di soggetto libero devo semplicemente duplicare la pena
      // residua impostando opportunamente il FLAG_PENA_SOSPESA
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Libero: duplico la pena resiua e aggiorno FLAG_PENA_SOSPESA");

      lPenaResiduaNuova = lUltimaPenaValidata;

      lPenaResiduaNuova.setIdPenaResidua(null);

      lPenaResiduaNuova.setFlagValidato("N");
      lPenaResiduaNuova.setDiesAQuo("S");

      lPenaResiduaNuova.setDataInizio (null);
      lPenaResiduaNuova.setDataFine   (null);
      //lPenaResiduaNuova.setFlagErgastolo(lFlagErgastolo);

    }
    
    lPenaResiduaNuova.setFlagPenaSospesa("S"); // S = Sospesa, n.b. il flag E non viene mai usato, solo su pena manuale
    lPenaResiduaNuova.setFlagErgastolo("N");
    lPenaResiduaNuova.setFasSieIdFascicoloSiep(lIdFascicolo);
    lPenaResiduaNuova.setCodOperatoreInserimento (lEventoComunicazione.getCodOperatoreInserimento());
    lPenaResiduaNuova.setCodUfficioInserimento   (lEventoComunicazione.getCodUfficioInserimento());
    lPenaResiduaNuova.setDataInserimento         (lEventoComunicazione.getDataInserimento());
    
    //========================================================================
    // Carico la SOSPENSIONE 
    // n.b. la sospensione va sempre inserita anche per espulsione da libero
    //      in quanto contiene la data espulsione e fine termini
    //========================================================================
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Carico la SOSPENSIONE");
    lSospensione = new SospensioneModel();
    lSospensione.setFasSieIdFascicoloSiep(lIdFascicolo);
    
    lSospensione.setDataInizio( lEventoVerbale.getDataEspulsioneSanzSost() );
    // n.b. la durata della sospensione va recuperata dalla sentenza (?) 
    
    lSospensione.setDataFine(lSospensione.getDataInizio());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Data Fine  = "+lSospensione.getDataFine());
    if (lSanzSost.getNumGiorni()!=null){
      lSospensione.setDataFine  ( DateUtils.moveDateTo(lSospensione.getDataFine(), Calendar.DAY_OF_MONTH, lSanzSost.getNumGiorni().intValue()));
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Data Fine  = "+lSospensione.getDataFine());
    }
    if (lSanzSost.getNumMesi()!=null) {
      lSospensione.setDataFine  ( DateUtils.moveDateTo(lSospensione.getDataFine(), Calendar.MONTH, lSanzSost.getNumMesi().intValue()));
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Data Fine  = "+lSospensione.getDataFine());
    }
    if (lSanzSost.getNumAnni()!=null) {
      lSospensione.setDataFine  ( DateUtils.moveDateTo(lSospensione.getDataFine(), Calendar.YEAR, lSanzSost.getNumAnni().intValue()));
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Data Fine  = "+lSospensione.getDataFine());
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Data Fine  = "+lSospensione.getDataFine());
    lSospensione.setFlagInterruzione("S");  // Sempre S
    
    // Reclusione Residua
    lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
    lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
    lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
    
    lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

    // Arresti Residui
    lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
    lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
    lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

    lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());

    // Espiato
    if ( !lPosMod.isLibero() ){
      lSospensione.setNumAnniPenaEspiata   (new BigDecimal(lPenaEspiataSosp.getNumAnni()));
      lSospensione.setNumMesiPenaEspiata   (new BigDecimal(lPenaEspiataSosp.getNumMesi()));
      lSospensione.setNumGiorniPenaEspiata (new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
    }
    else{
      lSospensione.setNumAnniPenaEspiata   (new BigDecimal(0));
      lSospensione.setNumMesiPenaEspiata   (new BigDecimal(0));
      lSospensione.setNumGiorniPenaEspiata (new BigDecimal(0));
    }
    // LA        
    //lSospensione.setNumGiorniLibanticipata(aValore)
    
    lSospensione.setCodOperatoreInserimento (lEventoComunicazione.getCodOperatoreInserimento());
    lSospensione.setCodUfficioInserimento   (lEventoComunicazione.getCodUfficioInserimento());
    lSospensione.setDataInserimento         (lEventoComunicazione.getDataInserimento());
    
      
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lPenaResiduaNuova = "+lPenaResiduaNuova);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSospensione = "+lSospensione);
    
	  //===========================
	  // Effettuo la registrazione 
    //===========================
	  //EventoNotificaModel lEventoNotInserito = null;
    ISanzioneSostitutiva lSanzioneCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    
    EventoNotificaModel lEventoNotInserito = lSanzioneCtrl.exInserisciAnnotazioneEspulsione(lEventoVerbale, lEvNotModel, lVerbaleMod, lPenaResiduaNuova, lSospensione);
    
    //==============================================
    // Restituisco la pagina di Dettaglio
    //==============================================
    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioAnnotazioneEspulsione&"+
            ICostantiEvento.CAMPO_ID_EVENTO+"="+lEventoNotInserito.getEvento().getIdEvento().toString();
    return lPage;
    
  }
}