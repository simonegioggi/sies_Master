package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAggiornaAvvenutaNotificaCondannato</p>
 * <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActAggiornaAvvenutaNotificaCondannato extends ActionSiap implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFasc = (FascicoloSiepModel)this.getSessionAttribute("fascicolo");

    String IdNotifica = this.getRequestStringParameter(ICostantiNotifica.CAMPO_ID_NOTIFICA);
    NotificaModel lNotMod = new NotificaModel();

    String lGiornoNot = this.getRequestStringParameter(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);
    String lMeseNot   = this.getRequestStringParameter(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
    String lAnnoNot   = this.getRequestStringParameter(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);

    String lAutoritaDelegata    = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
    String lSedeAutoritaDelegata = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
    String lIndirizzo = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE);

    lNotMod.setDataAvvenutaNotifica(DateUtils.getDate(lAnnoNot,lMeseNot,lGiornoNot));
    lNotMod.setIdNotifica(new BigDecimal(IdNotifica));
    lNotMod.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
    lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lNotMod.setDataAggiornamento(DateUtils.getSysDate());
    lNotMod.setCodEsito("03");

    if(   lAutoritaDelegata != null 
        && !lAutoritaDelegata.equals("") 
        && !lAutoritaDelegata.equals("-")
        )
     {
      AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

      lAutMod.setCodTipoAutorita(lAutoritaDelegata);
      ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeAutoritaDelegata));
      lAutMod.setCodSede(lComModel.getCodComune());
      lAutMod.setDescrizione(lIndirizzo);

      lAutMod.setCodOperatoreInserimento(getCodUtenteConnesso());
      lAutMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lAutMod.setDataInserimento(DateUtils.getSysDate());

      lNotMod.setIstDetIdIstitutoDetenzione("");

      lNotMod.setAutoritaEsternaDelegata(lAutMod);
     }       

    NotificaModel[] lNotModArray = new NotificaModel[1];
    lNotModArray[0] = lNotMod;

    Vector lEveVectUno = new Vector();
    IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();

    lEveVectUno = lCtrl.ExAggiornaAvvenutaNotifica(lNotModArray,lFasc.getIdFascicoloSiep(), false);
    setRequestAttribute("notifiche", lEveVectUno);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notifica.action.ActLoadRicercaOECondannato&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+((NotificaModel)lEveVectUno.get(0)).getEveIdEvento().toString();
    return lPage;
  }
}