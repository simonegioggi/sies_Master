package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ActOrdineEsecuzione;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;



/**
 * Inserimento degli Ordini di Esecuzione per Rideterminazione pena
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActEmissioneProvvedimento extends ActOrdineEsecuzione implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws Exception
  {

   FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

   EventoNotificaModel lEveNot = new EventoNotificaModel();
   //  String lCodMotivo = this.getRequestStringParameter("codmotivo");
   //  setRequestAttribute("codmotivo",getRequestStringParameter("codmotivo"));

   lEveNot.setEvento(setEventoOrdineEsecuzione(lEveNot.getEvento()));

   // Aggancio l'OE al provvedimento di computo
   if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_EVE_ID_EVENTO)){
     BigDecimal lEveIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO);
     lEveNot.getEvento().setEveIdEvento(lEveIdEvento);
   }
   
   PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
   IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
   lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

    if(lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S"))
    {
      lEveNot.getEvento().setCodMotivo("0132");
    } 
    else
    {
      int lIntPos = Integer.parseInt(lPos.getCodPosizioneGiuridica());
     
      switch (lIntPos) {
        case 7:
        case 16:
        case 20:
        case 46:
        case 47:
        case 10: { // libero
          lEveNot.getEvento().setCodMotivo("0130");
          break;
        }
        case 1:
        case 3: { // detenuto
          lEveNot.getEvento().setCodMotivo("0131");
          break;
        }
        case 2:
        case 4: { // Arresti domiciliari
          lEveNot.getEvento().setCodMotivo("0134");
          break;
        }
        default: { //altri casi
          //modifica richiesta il 25-02-04 cambiare il codice del motivo in 0000
          //lEve.getEvento().setCodMotivo("0133");
          lEveNot.getEvento().setCodMotivo("0000");
          break;
        }
      }
    }

    lEveNot.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

    //Inserisco l'array di Notifiche nell'Evento
    NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
    lEveNot.setNotifiche(lNotifiche);

    BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
    PenaResiduaModel lPenaRes = new PenaResiduaModel();
    lPenaRes.setIdPenaResidua(lIdPenaRes);
    if(!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
      lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE, ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));


    // Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
    IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
    EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaEventoNotifica(lEveNot,lPenaRes);
 
    // setRequestAttribute("desmotivo",getRequestStringParameter("desmotivo"));
    String pageInizio = null;
    if(!this.isRequestParameterNullObj("flagPage"))
    {
      pageInizio = this.getRequestStringParameter("flagPage");
    }

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioEmissioneProvvedimento&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" +
    lRetModel.getEvento().getIdEvento() + "&modalita=I&flagPage="+pageInizio;

    return lPage;
  }
}