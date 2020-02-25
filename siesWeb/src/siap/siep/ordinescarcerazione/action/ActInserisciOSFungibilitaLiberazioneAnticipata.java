package siap.siep.ordinescarcerazione.action;

/**
 * <p>Title: ActDettaglioLSLiberoIstanzaProdotta</p>
 * <p>Description: Classe Action per la load dettaglio di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciOSFungibilitaLiberazioneAnticipata extends ActOrdineScarcerazione implements ICostantiOrdineScarcerazione
{

  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio = this.getCodUfficioUtenteConnesso();


    String tipoMisura = "OS_LIBERAZIONE_ANTICIPATA";
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    EventoNotificaModel lRetModel = new EventoNotificaModel();
    EventoNotificaModel lEveModOS = null;

    NotificaModel[] lNotifiche = this.setNotificheOS();
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    PenaResiduaModel lPenaResMod = new PenaResiduaModel();
    IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaPerFascicolo(lFascicoloModel.getIdFascicoloSiep());
    lPenaResMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lPenaResMod.setDataInserimento(DateUtils.getSysDate());
    lPenaResMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
    lPenaResMod.setFlagValidato("N");
    lPenaResMod.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE, ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

    MisuraAlternativaAggregatoModel lMAAggSimulata = null;
    MisuraAlternativaAggregatoModel lOSMisAltAgg = null;

    MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();

    if (getRequestStringParameter("flagmisura").equals("N"))
    {
      //Non c'è misura alternativa devo inserirla
     // String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),

      String lCodiceUff = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);

    //  getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));

      lEveMod.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
      lEveMod.getEvento().setCodUfficioEmittente(lCodiceUff);
      ComuneModel lComModAut = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));

      lEveMod.getEvento().setCodLuogoEmittente(lComModAut.getCodComune());

      //setto il deposito ordinanza
      DepositoOrdinanzaPcModel lDepOrdMod = getDepositoOrdinanza(lEveMod.getEvento());
      //setto il tenore
      TenoreModel[] lTenMod = getTenoreModel(lEveMod.getEvento());
      //setto la misuraalternativa
      lMisAlModConcessa = getMisuraAlternativaModel(lEveMod.getEvento());
      lMisAlModConcessa.setDataFineMisura(lPenaResMod.getDataFine());
      lMisAlModConcessa.setCodUfficioSorveglianza(lCodiceUff);
      lMisAlModConcessa.setChiaveUfficioFascicoloSius(lCodiceUff);

      lEveMod = getEventoMASimulata(lEveMod.getEvento());
      lEveMod.setNotifiche(lNotifiche);
      lEveMod.getEvento().setCodMotivo("0081");
      lEveModOS = getEventoOrdineScarcerazione(lEveMod.getEvento());
      lEveModOS.getEvento().setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
      lEveModOS.getEvento().setCodUfficioEmittente(lCodiceUfficio);
      lEveModOS.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
      // INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
      lEveModOS.setNotifiche(lNotifiche);

      lMAAggSimulata = new MisuraAlternativaAggregatoModel();

      lMAAggSimulata.setEventoNotifica(lEveMod);
      lMAAggSimulata.setDepositoOrdinanzaPc(lDepOrdMod);
      lMAAggSimulata.setTenori(lTenMod);
      lMAAggSimulata.setMisuraAlternativa(lMisAlModConcessa);
      lMAAggSimulata.setTipoMisura(tipoMisura);

      //   lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipata&" +
      //    ICostantiEvento.CAMPO_ID_EVENTO + "=" +
      //  lEveNotModel.getEvento().getIdEvento();
    } //FINE NON C'E' la misura alterantiva
    else
    { //C'e' la MISURA ALTERNATIVA
      //Inserisco l'array di Notifiche nell'Evento
      lNotifiche = this.setNotificheOS();

      if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
        lPenaResMod.setDataFine(lPenaResMod.getDataFine());

      lEveMod.getEvento().setCodMotivo("0081");
      lEveModOS = getEventoOrdineScarcerazione(lEveMod.getEvento());
      lEveModOS.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
      lEveModOS.setNotifiche(lNotifiche);

    } //FINE if Esiste la misura alternativa!!!


    //Inserimento Fungibilita
    FungibilitaModel lFunMod = new FungibilitaModel();
    lFunMod.setCodTipoFungibilita("02");
    lFunMod.setFlagValidato("N");
    lFunMod.setNumAnni(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_ANNI)));
    lFunMod.setNumMesi(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_MESI)));
    lFunMod.setNumGiorni(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_GIORNI)));
    lFunMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lFunMod.setDataInserimento(DateUtils.getSysDate());
    lFunMod.setCodOperatoreInserimento(lCodiceOperatore);
    lFunMod.setCodUfficioInserimento(lCodiceUfficio);
    //---  lFunMod.setEveIdEvento(lEve.getEvento().getIdEvento());

    lOSMisAltAgg = new MisuraAlternativaAggregatoModel();
    lOSMisAltAgg.setEventoNotifica(lEveModOS);
    lOSMisAltAgg.setTipoMisura(tipoMisura);
    lOSMisAltAgg.setFungibilita(lFunMod);
    lOSMisAltAgg.setPenaResidua(lPenaResMod);

    //--------------++++++++++++++++-----------------------------------------
    IOrdineScarcerazione lOScarc = SIEPLookupRemote.getOrdineScarcerazione();
    lRetModel = lOScarc.ExInserisciMAeOrdineScarcerazione(lMAAggSimulata, lOSMisAltAgg);
    //--------------++++++++++++++++-----------------------------------------

    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipata&" +
      ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

    return lPage;
  }
}