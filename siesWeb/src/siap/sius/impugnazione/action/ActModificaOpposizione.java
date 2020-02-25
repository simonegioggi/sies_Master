package siap.sius.impugnazione.action;

/**
* <p>Title: ActModificaOpposizione</p>
* <p>Description: Classe Action per la modifica dell'opposizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* @since 06/2014
*/

import siap.sico.web.ActionSiap;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActModificaOpposizione extends ActionSiap implements ICostantiImpugnazione
{
/**
* Azione di Modifica del Opposizione
* @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws Exception
  {
    // Preleva i dati dell'oposizione dalla request
    ImpugnazioneModel lImpMod = new ImpugnazioneModel();
    
    lImpMod.setIdImpugnazione          ( getRequestBigDecimalParameter( CAMPO_ID_IMPUGNAZIONE) );
    
    lImpMod.setCodTipoImpugnazione     ( getRequestStringParameter( CAMPO_COD_TIPO_IMPUGNAZIONE) );
    lImpMod.setSoggettoImpugnante      ( getRequestStringParameter( CAMPO_SOGGETTO_IMPUGNANTE) );
    lImpMod.setDataRicorso             ( getRequestDateParameter( CAMPO_ANNO_DATA_RICORSO,CAMPO_MESE_DATA_RICORSO,CAMPO_GIORNO_DATA_RICORSO) );
    lImpMod.setDataArrivoCancelleria   ( getRequestDateParameter( CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,CAMPO_MESE_DATA_ARRIVO_CANCELLERIA,CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA) );
    lImpMod.setDataTrasmissioneAtti    ( null ); // non previsto per OPOSIZIONE
    lImpMod.setCodAutoritaDestinataria ( "-" ); // non previsto per OPOSIZIONE, trattino per la join
    lImpMod.setDataDecisione           ( getRequestDateParameter( CAMPO_ANNO_DATA_DECISIONE,CAMPO_MESE_DATA_DECISIONE,CAMPO_GIORNO_DATA_DECISIONE) );
    lImpMod.setCodTenoreDecisione      ( getRequestStringParameter( CAMPO_COD_TENORE_DECISIONE) );
    lImpMod.setDataRestituzioneAtti    ( getRequestDateParameter( CAMPO_ANNO_DATA_RESTITUZIONE_ATTI,CAMPO_MESE_DATA_RESTITUZIONE_ATTI,CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI) );
    lImpMod.setAnnotazione             ( getRequestStringParameter( CAMPO_NOTE) );
    lImpMod.setFlagSospEsec            ( null ); // non previsto per OPOSIZIONE

    lImpMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lImpMod.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lImpMod.setDataAggiornamento         (DateUtils.getSysDate());

    // Modifica
    IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    ImpugnazioneModel llImpModRet = lCtrl.ExModificaImpugnazione(lImpMod);

    //Setta la risposta nella request.
    setRequestAttribute("impugnazione", llImpModRet);


    // Prepara la pagina di destinazione, puntando all'azione di dettaglio.
    RedirectTo lPage = new RedirectTo();
    lPage.setPage(IWebConstants.PG_MAIN);
    lPage.setAction("siap.sius.impugnazione.action.ActLoadDettaglioOpposizione");
    lPage.setParameter(ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE,""+ llImpModRet.getIdImpugnazione());

    return "" + lPage;  

  }
}
