package siap.siep.sospensione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioNotificheDifferimento</p>
 * <p>Description: Classe Action per la load dettaglio di Notifiche del Differimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioNotificheDifferimento
    extends ActSIESDettaglioProvvedimento
    implements ICostantiSospensione,
    ICostantiEvento
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();

    EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

    setRequestAttribute("eventonotifica", lEveNotMod);

// magistratoSorv
    IUfficio lUff = SICOLookupRemote.getUfficioRemote();

    IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
    NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
    for (int i = 0; i < lNotifiche.length; i++)
    {

//TDS
      if (lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("E"))
      {

        UfficioModel lUffModTDS = lUff.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
        setRequestAttribute("uffTDS", lUffModTDS);

      }
      if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null && lNotifiche[i].getCodTipoNotifica().equals("E"))
      {

        IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
        setRequestAttribute("Istituto", lModIst);

      }
      if (lNotifiche[i].getAutoritaEsterna() != null && lNotifiche[i].getCodTipoNotifica().equals("E"))
      {

        AutoritaEsternaModel lModAut = lNotifiche[i].getAutoritaEsterna();
        setRequestAttribute("autorita", lModAut);

      }

    }

// **  Ricerca su DECRETO_ORDINANZA_SIEP **
    IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
    DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd.ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(lEveNotMod.getEvento().getEveIdEvento());
    setRequestAttribute("decretoordinanza", lDecOrd);
    String lFlagDec = (lDecOrd == null ? "N" : "S");
    setRequestAttribute("flagdecretoordinanza", lFlagDec);

	/* REWORK DETTAGLIO
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
    */

    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,lFascMod.getIdFascicoloSiep());

    setRequestAttribute("posizioneluogoaltra", lPos);

    IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
    PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

    if (lPenComMod == null)
      throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile eseguire la richiesta.");

    String lFlagErgastolo = "N";
// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
    if(  lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "")
    {
      if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
      {
        lFlagErgastolo = "S";
      }
      else
      if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
      {
       lFlagErgastolo = "D";
      }
    }


    setRequestAttribute("flagergastolo", lFlagErgastolo);

	/* REWORK DETTAGLIO
    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);
    */
    PenaResiduaModel lPenaResMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());

    setRequestAttribute("penaresidua", lPenaResMod);

    ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
    SospensioneModel lSospensione = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenaResMod.getIdPenaResidua());

    setRequestAttribute("sospensione", lSospensione);

//MAGISTRATO COMPETENTE
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
    if (lMagMod != null)
      setRequestAttribute("magistratocompetente", lMagMod);

    if (lEveNotMod.getEvento().getCodMotivo().equals("0276"))
    {
      return PG_LOAD_DETTAGLIO_NOTIFICHE_ESPULSIONE;
    }
    else
    {
      return PG_LOAD_DETTAGLIO_NOTIFICHE_DIFFERIMENTO;
    }

  }
}