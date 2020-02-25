package siap.sico.jms.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;

/**
 * <p>Title: ActDettaglioSoggettoTrovato</p>
 * <p>Description: Dettaglio di un soggetto e dei suoi fascicoli</p>
 * <p>Copyright: Copyright (c) 2004</p>
 */
public class ActDettaglioSoggettoTrovato extends ActionSiap
  implements ICostantiSicoJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = new ParserMessage(lMess.getTreeModel());

    if (getRequestStringParameter("daElenco").compareTo("SI")==0)
    {
      int indice = new BigDecimal(getRequestStringParameter("indice")).intValue();
      this.setRequestAttribute("soggetto",  (SoggettoModel)lParser.getSoggettoArray().get(indice));
    }
    else
      this.setRequestAttribute("soggetto", lParser.getSoggetto());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("SOGGETTO = " + lParser.getSoggetto());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("N.RO SOGGETTI = " + lParser.getSoggettoArray().size());


    //Ricerco se eventualmente il soggetto è stato già precedentemente importato nella BDI.
    /*--- in futuro---
    ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
    Vector lidFascicoliPerSoggetto = new Vector();
    lSoggetti = lSogCtrl.ExRicercaSoggettoPerDistretto(lSogMod,getCodDistrettoUtenteConnesso(),Integer.parseInt(lPagina));
    */
    // STUB 31/05/2005 Se provengo daElenco soggetti Trovati in una BDI,
    // devo puntare al dettaglio del singolo soggetto.

    if (lParser.getSoggettoArray().size()==1 ||
        getRequestStringParameter("daElenco").compareTo("SI")==0)
      return PG_DETTAGLIO_SOGGETTO_TROVATO;
    else
      return PG_DETTAGLIO_SOGGETTI_TROVATI;
  }

}