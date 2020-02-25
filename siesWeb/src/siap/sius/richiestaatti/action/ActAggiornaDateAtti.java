package siap.sius.richiestaatti.action;

import java.math.BigDecimal;

import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;


/**
 * <p>Title: ActAggiornaDateAtti </p>
 * <p>Description: Azione specializzazione della  ActRicercaStatoAtti.
 * L'Azione attiva l'aggiornamento multiplo delle date di ricezione Atti
 * utilizzando l'apposita funzione nel Controller Evento; poi effettua la ricerca
 * degli Atti tramite la funzione <code>ricercaAtti()<code> dell'Azione padre restituendoli
 * nell'apposita pagina di view.
 * </p>
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */

public class ActAggiornaDateAtti extends ActRicercaStatoAtti
{
  public String processRequest() throws Exception
  {
    // pagina view
    String lPage = new String();
    NotificaModel[] lListaNot;

    // Lista degli IDNotifica
    String[] lListaID;

    // Lista delle date da aggiornare nelle Notifiche
    String[] lListaAnno;
    String[] lListaMese;
    String[] lListaGiorno;

    int i;             // indice dell'array di Notifiche
    int ii;            // indice dell'array delle solo Notifiche da aggiornare
    int lung =0;      // numero complessivo di Notifiche
    int lNumRec = 0; // numero delle Notifiche da aggiornare

    // Costruzione elenco ID dei record da aggiornare
    lListaID = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
    lung= lListaID.length;

    // Costruzione elenco date
    lListaAnno = getRequestStringParameters(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);
    lListaMese = getRequestStringParameters(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
    lListaGiorno = getRequestStringParameters(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);

    // Calcolo del numero di record da aggiornare
    for(i=0; i<lung; i++)
    {
      if (!lListaID[i].equals(""))
        lNumRec++;
    }

    if (lNumRec > 0)
    {
      // Si istanzia l'array atto a contenere i record da aggiornare
      lListaNot = new NotificaModel[lNumRec];

      // costruzione elenco di Notifiche da aggiornare
      ii = 0;
      for(i=0; i<lung; i++)
      {
        // i record da aggiornare sono solo quelli per cui sono presenti gli ID
        if (!lListaID[i].equals(""))
        {
          lListaNot[ii] = new NotificaModel();
          lListaNot[ii].setCodEsito(ICostantiProvvedimento.NOTIFICA_ESEGUITA);
          lListaNot[ii].setDataAvvenutaNotifica(DateUtils.getDate(lListaAnno[i],lListaMese[i],lListaGiorno[i]));
          lListaNot[ii].setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
          lListaNot[ii].setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
          lListaNot[ii].setDataAggiornamento(DateUtils.getSysDate());
          lListaNot[ii].setIdNotifica(new BigDecimal(lListaID[i]));
          ii++;
        }
      }
      // Viene effettuato l'aggiornamento
      INotifica lCtrlNt = SIEPLookupRemote.getNotificaRemote();
      lCtrlNt.ExAggiornaDateNotifica(lListaNot);

    }
    if (lNumRec>0)
      throw new F3BException( F3BException.USER_MESSAGE, "Aggiornate n° "+lNumRec+" date restituzioni di atti istruttori." );

    // si ottiene il FasicoloGPModel dalla session
    mFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    // si attiva la ricerca degli atti e la visualizzazione della lista
    lPage = ricercaAtti(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
    setRequestAttribute("modalita", "M");
    setFunctionsAvailableToRequest("siap.sius.richiestaatti.action.ActRicercaStatoAtti");
    return lPage;
  }
}