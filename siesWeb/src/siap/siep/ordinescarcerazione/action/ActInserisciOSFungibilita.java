package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActInserisciOSFungibilita</p>
 * <p>Description: Classe Action per la load dettaglio di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActInserisciOSFungibilita
    extends ActOrdineScarcerazione
    implements ICostantiOrdineScarcerazione
{

  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    EventoNotificaModel lEve = new EventoNotificaModel();
    lEve.getEvento().setDescrMotivo("SC");
    lEve.getEvento().setCodMotivo("0082");

    //Altre Posizioni Metterlo a 0000
    lEve=getEventoOrdineScarcerazione(lEve.getEvento());
    lEve.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

    //Inserisco l'array di Notifiche nell'Evento
    NotificaModel[] lNotifiche = this.setNotificheOS();

    lEve.setNotifiche(lNotifiche);
    BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
    PenaResiduaModel lPenaRes = new PenaResiduaModel();
    //  lPenaRes.setIdPenaResidua(lIdPenaRes);
    IPenaResidua lPenCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenaRes = lPenCtrl.ExRicercaPenaResiduaByKey(lIdPenaRes);
    //La Data fine viene posta  =  alla data di emissione
    lPenaRes.setDataFine(lEve.getEvento().getDataEmissione());


//MANCA IL RESTO DI PENA RESIDUA-------------------------------------


    //Inserimento Fungibilita
    FungibilitaModel lFunMod = new  FungibilitaModel();
    lFunMod.setCodTipoFungibilita("02");
    lFunMod.setFlagValidato("N");
    lFunMod.setNumAnni(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_ANNI)));
    lFunMod.setNumMesi(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_MESI)));
    lFunMod.setNumGiorni(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_GIORNI)));
    lFunMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lFunMod.setDataInserimento(DateUtils.getSysDate());
    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
    lFunMod.setCodOperatoreInserimento(lCodiceOperatore);
    lFunMod.setCodUfficioInserimento(lCodiceUfficio);
    lFunMod.setEveIdEvento(lEve.getEvento().getIdEvento());

     MisuraAlternativaAggregatoModel lAgg = new MisuraAlternativaAggregatoModel();
     lAgg.setEventoNotifica(lEve);
     lAgg.setFungibilita(lFunMod);
     lAgg.setPenaResidua(lPenaRes);

     IOrdineScarcerazione lCntrl = SIEPLookupRemote.getOrdineScarcerazione();
     EventoNotificaModel lRetModel = lCntrl.ExInserisciMAeOrdineScarcerazione(null,lAgg);
    //-------------------------------------------+++++++++++++++++++++++++++++++++++++

  String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
  + "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
  + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

    return lPage;
 }
}