package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAggiornaNotificheLS</p>
 * <p>Description: Classe Action </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAggiornaNotificheLS
    extends ActionSiap
    implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    BigDecimal lIdNotificaE = null;
    
    BigDecimal lIdEvento = getRequestBigDecimalParameter("idEvento");
    
    String[] lArrayNotificheN = null;   
    if (!this.isRequestParameterNullObj("idNotificaE"))
    {
      lIdNotificaE = this.getRequestBigDecimalParameter("idNotificaE");
    }

    if (!this.isRequestParameterNullObj("idNotifica"))
    {
      lArrayNotificheN = this.getRequestStringParameters("idNotifica");
    }

    String[] lListaAnno = null;
    String[] lListaMese = null;
    String[] lListaGiorno = null;

    List lNotificheDaMod = new ArrayList();
    if (!this.isRequestParameterNullObj("idNotifica"))
    {
// Costruzione elenco date
      lListaAnno = getRequestStringParameters(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV);
      lListaMese = getRequestStringParameters(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV);
      lListaGiorno = getRequestStringParameters(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV);
    }

    if (lIdNotificaE != null)
    {
      NotificaModel lNotE = new NotificaModel();
      lNotE.setDataAvvenutaNotifica(this.getRequestDateParameter( ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA, 
                                                                  ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA,
                                                                  ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA) );
      lNotE.setCodEsito("-");
      lNotE.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
      lNotE.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      lNotE.setDataAggiornamento(DateUtils.getSysDate());

      lNotE.setIdNotifica(lIdNotificaE);
      lNotificheDaMod.add(lNotE);
    }
    
    if ( lArrayNotificheN != null )
    {
      for (int i = 0; i < lArrayNotificheN.length; i++)
      {
        BigDecimal lIdNot = new BigDecimal(lArrayNotificheN[i]);
        NotificaModel lNot = new NotificaModel();

        lNot.setDataAvvenutaNotifica(DateUtils.getDate(lListaAnno[i], lListaMese[i], lListaGiorno[i]));
        lNot.setCodEsito("-");
        lNot.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
        lNot.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
        lNot.setDataAggiornamento(DateUtils.getSysDate());

        lNot.setIdNotifica(lIdNot);
        lNotificheDaMod.add(lNot);
      }
    }
    
    NotificaModel[] lNotModel = (NotificaModel[]) lNotificheDaMod.toArray(new NotificaModel[0]);
    INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
    lCtrl.ExAggiornaDateNotifica(lNotModel);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notifica.action.ActDettaglioNotificheLSAggiornate&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" +lIdEvento.toString();

    return lPage;
  }
}