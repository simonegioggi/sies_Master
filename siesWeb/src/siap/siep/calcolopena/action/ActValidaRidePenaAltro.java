package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe per la validazione diretta dei provvedimenti di 'Rideterminazione pena
 * - Altro'
 * 
 * @author diego
 * @since 4.0
 */
public class ActValidaRidePenaAltro extends ActionSiap implements ICostantiEvento,ICostantiAnnotazioneManuale
{
  /**
   * Effettua la validazioe del provvedimento di rideterminazione pena.
   * Valida:
   * 
   * - Evento
   * - Annotazioni Manuali (nella nuova versione ne può esistere più di una)
   * - Pena Residua (rideterminata associata all'evento)
   *
   * @return jsp di visualizzazione del detteglio
   */
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    BigDecimal lIdEvento =  getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
    
    //==========================================================================
    // Verifico che l'evento non sia già validato
    //==========================================================================
    IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEveModel = lEveCtrl.ExRicercaEventoByKey(lIdEvento);
    if (   lEveModel.getFlagDocumentoRegistrato()!=null 
        && lEveModel.getFlagDocumentoRegistrato().equals("S")
       )
    {
      //Prepara la "pagina" di Risposta
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Provvedimento Risulta Già Validato!");

      if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
      {
        RedirectTo lRedirigi = new RedirectTo();
        lRedirigi.setPage(IWebConstants.PG_MAIN);
        lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadGrigliaRidetPenaAltro" + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
      }

      return IWebConstants.PG_MESSAGE;      
    }
    
    //==========================================================================
    // Procedo alla Validazione
    //==========================================================================
    EventoModel lModel = new EventoModel();
    lModel.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );
    lModel.setDataAggiornamento( DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );
    lModel.setFlagDocumentoRegistrato("S");

    IAnnotazioneManuale lCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
    lCtrl.ExValidaRideterminazionePenaAltro(lModel, lFascMod, null);
    
    //Prepara la "pagina" di Risposta
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Validazione Provvedimento Avvenuta Correttamente!");


    if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadGrigliaRidetPenaAltro" + "&" + CAMPO_ID_EVENTO + "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
    }

    return IWebConstants.PG_MESSAGE;
  }
}


