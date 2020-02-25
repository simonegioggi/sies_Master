package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAggiornaDateAtti </p>
 * </p>
 * La Action esegue l'aggionamento delle date di avvenuta notifica per più notifiche.
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */

public class ActAggiornaDateNotifiche  extends ActionSiap implements ICostantiProvvedimento
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  private FascicoloGPModel lFasGPMod = null;

   // Valorizzazione del record  da inserire nello scadenzario
  private ScadenzarioSiusModel valorizzaScadenzarioInserimento (Date adata)
  throws F3BException
  {
          ScadenzarioSiusModel lScad = null;

          lScad = new ScadenzarioSiusModel();
          lScad.setCodTipoScadenzario(SCADENZARIO_IRREVOCABILITA);
          lScad.setCodOperatoreInserimento(this.getCodUtenteConnesso());
          lScad.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
          // 02/08/2004 Aggiunto EveIdEvento.
          lScad.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

          if (adata != null)
          {
            lScad.setDataInizioScadenza(adata);
            lScad.setDataFineScadenza(DateUtils.moveDateTo(adata,java.util.Calendar.DAY_OF_MONTH,15));
          }
          else
          {
            //lScad.setDataInizioScadenza(null);
            lScad.setDataFineScadenza(null);
          }
          lScad.setDataInserimento(DateUtils.getSysDate());
          lScad.setFlagVisto("N");
          lScad.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
          return lScad;
  }

  // Valorizzazione del record  da aggiornare nello scadenzario
  private ScadenzarioSiusModel valorizzaScadenzarioAggiornamento (ScadenzarioSiusModel aScad, Date adata)
  throws F3BException
  {

          ScadenzarioSiusModel lScad = null;

          lScad = new ScadenzarioSiusModel(aScad);
          lScad.setCodTipoScadenzario(SCADENZARIO_IRREVOCABILITA);
          lScad.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
          lScad.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

          if (adata != null)
          {
            lScad.setDataInizioScadenza(adata);
            lScad.setDataFineScadenza(DateUtils.moveDateTo(adata,java.util.Calendar.DAY_OF_MONTH,15));
          }
          else
          {
            //lScad.setDataInizioScadenza(null);
            lScad.setDataFineScadenza(null);
          }
          lScad.setDataAggiornamento(DateUtils.getSysDate());
          lScad.setFlagVisto("N");
          lScad.setDataVisto(null);
          lScad.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
          return lScad;
  }


  public String processRequest() throws Exception
  {
     ScadenzarioSiusModel lScad = null;

     NotificaModel[] lListaNot;

    // Lista degli IDNotifica
    String[] lListaID;

    // Lista delle date da aggiornare nelle Notifiche
    String[] lListaAnno;
    String[] lListaMese;
    String[] lListaGiorno;

    BigDecimal IdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    // pagina view
    //String lPage = new String();
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.provvedimento.action.ActDettaglioNotificheEvento&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+IdEve;

    int i;             // indice dell'array di Notifiche
    int ii;            // indice dell'array delle solo Notifiche da aggiornare
    int lung =0;      // numero complessivo di Notifiche
    int lNumRec = 0; // numero delle Notifiche da aggiornare

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.warn("-------- ActAggiornaDateNotifiche: inizio");
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
          lListaNot[ii].setCodEsito(NOTIFICA_ESEGUITA);
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

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.warn("-------- ActAggiornaDateNotifiche aggiornati ->: "+lNumRec);

    // Ricerca Provvedimento per controllare motivo
    IEvento mCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEvento = mCtrl.ExRicercaEventoByKey ( IdEve);

    // Il Decreto di Fissazione Udienza non richiede SCADENZARIO
    if (!(lEvento.getCodMotivo().equals(MOTIVO_FISSAZIONE_UDIENZA) ))
    {

      // si risale al fascicolo in sessione
      lFasGPMod = (FascicoloGPModel) this.getSessionAttribute("fascicoloSiusGP");

      // ricerca del record di Scadenzario
      IScadenzarioSius lCtrlSc = SIUSLookupRemote.getScadenzarioRemote();
      lScad = lCtrlSc.ExRicercaScadenzarioSiusByIdFascicoloTipo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),SCADENZARIO_IRREVOCABILITA);

      // Se sono state notificate tutte si inserisce
      // un record nello SCADENZARIO
      if( lCtrlNt.ExSonoNotificate(IdEve))
      {
        // data di notifica
        Date lData = lCtrlNt.ExRicercaDataNotifica(IdEve);
        if (lData != null)
        {

          // Se non esiste lo Scadenzario viene inserito
          if(lScad == null)
          {
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.warn("-------- ActAggiornaDateNotifiche: inserimento scadenziario");
            lScad = valorizzaScadenzarioInserimento(lData);
            // inserimento
            lCtrlSc.ExInserisciScadenzarioSius(lScad);
          }
          else
          {
            // Se lo Scadenzario esiste va aggiornato
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.warn("-------- ActAggiornaDateNotifiche: aggiornamento scadenziario");
            lScad = valorizzaScadenzarioAggiornamento(lScad,lData);
            lCtrlSc.ExModificaScadenzario(lScad);
          }
        }
      } else // Non sono tutte notificate
      {
          // Se esiste lo Scadenzario occorre annullare le date
          if(lScad != null)
          {
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.warn("-------- ActAggiornaDateNotifiche: aggiornamento scadenziario");
            lScad = valorizzaScadenzarioAggiornamento(lScad,null);
            lCtrlSc.ExModificaScadenzario(lScad);
          }
        }
      } // endif Fissazione Udienza
    }  // endif NumRec
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.warn("-------- ActAggiornaDateNotifiche: fine");
    return lPage;
  }
}