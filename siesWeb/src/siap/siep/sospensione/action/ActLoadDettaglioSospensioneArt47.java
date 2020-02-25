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
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioSospensioneArt47</p>
* <p>Description: Classe Action per la load dettaglio di Sospensione Art. 47</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioSospensioneArt47 extends ActionSiap
                                         implements ICostantiSospensione,
                                                    ICostantiEvento
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    BigDecimal lIdEvento= getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();

    EventoNotificaModel lEveNotMod= lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

    setRequestAttribute("eventonotifica", lEveNotMod);

    // magistratoSorv
    IUfficio lUff = SICOLookupRemote.getUfficioRemote();

    IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
    NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
   for (int i=0; i < lNotifiche.length; i++)
   {
     // magistratoSorv
     if( lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("C"))
     {

       UfficioModel lUffModMag  = lUff.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
       setRequestAttribute("uffMagistrato", lUffModMag);

     }
//TDS
     if( lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("E"))
     {

       UfficioModel lUffModTDS  = lUff.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
       setRequestAttribute("uffTDS", lUffModTDS);

     }
     if( lNotifiche[i].getIstDetIdIstitutoDetenzione() != null && lNotifiche[i].getCodTipoNotifica().equals("N"))
     {

       IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
       setRequestAttribute("Istituto", lModIst);

     }


   }

// **  Ricerca su DECRETO_ORDINANZA_SIEP **
   IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
   DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd.ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(lEveNotMod.getEvento().getEveIdEvento());

    setRequestAttribute("decretoordinanza", lDecOrd);
    String lFlagDec = (lDecOrd==null ? "N" : "S");
    setRequestAttribute("flagdecretoordinanza", lFlagDec);

    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());

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

    IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);

    setRequestAttribute("penaresidua", lPenaResidua);

    ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
    SospensioneModel lSospensione = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

    setRequestAttribute("sospensione", lSospensione);

    //MAGISTRATO COMPETENTE
    IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
     MagistratoCompetenteMagistratoModel lMagMod = lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
     if (lMagMod != null)
       setRequestAttribute("magistratocompetente", lMagMod);


    return PG_LOAD_DETTAGLIO_SOSPENSIONE_ART47;
  }
}