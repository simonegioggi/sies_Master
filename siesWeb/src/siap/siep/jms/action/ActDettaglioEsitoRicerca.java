package siap.siep.jms.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * Dettaglio Esito ricerca su altre BDI
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActDettaglioEsitoRicerca extends ActionSiap implements ICostantiSiepJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: inizio");
    
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: Trovato lMess = " + lMess);
    this.setRequestAttribute("Messaggio", lMess);

    ParserMessage lParser = null;

    // STUB 03/03/2005 Si parte dalla condizione di Fascicolo non trovato.
    // String lPageReturn = "files/siap/siep/jms/DettaglioMessaggioRicercaFascicolo.jsp";
    String lPageReturn = "files/siap/siep/jms/DettaglioFascicoloNonTrovato.jsp";

    if (lMess.getMessaggioCorrelato() != null)
    {
      if (lMess.getMessaggioCorrelato().getCodTipoOperazione().equals(ICostantiJMS.ESITO_RICERCA_FASCICOLO) )
      {
        lParser = new ParserMessage(lMess.getMessaggioCorrelato().getTreeModel());
        this.setRequestAttribute("fascicolo", lParser.getFascicolo());
        // STUB Commentato il 07/03/2005 this.setSessionAttribute("fascicolo", lParser.getFascicolo());
        lPageReturn = IWebConstants.ROOT_DIR + "files/siap/siep/jms/DettaglioMessaggioRicercaFascicolo.jsp";
      }
      if (lMess.getMessaggioCorrelato().getCodTipoOperazione().equals(ICostantiJMS.ESITO_FASCICOLO_PER_TRASFERIMENTO) )
      {
        if ((lMess.getMessaggioCorrelato().getCodEsito().equals(ICostantiJMS.POSITIVO) ) ||
            (lMess.getMessaggioCorrelato().getCodEsito().equals (ICostantiJMS.TROVATO) )) {
          
          lParser = new ParserMessage(lMess.getMessaggioCorrelato().getTreeModel());

          if (lParser.getDettaglioFascicoloSiep()==null || lParser.getDettaglioFascicoloSiep().getFascicoloSiep() == null)
            throw new SIEPException( SIEPException.USER_MESSAGE, "Errore nella Ricezione del Procedimento SIEP. " );

          /*
            DettaglioFascicoloModel lDett = lParser.getDettaglioFascicoloSiep();
          */

          this.setRequestAttribute("dettagliofascicolo", lParser.getDettaglioFascicoloSiep());
          lPageReturn = 
            IWebConstants.ROOT_DIR + "files/siap/siep/jms/DettaglioFascicoloSiepPerTrasferimento.jsp";
        }
        else
        {
          //elemento non trovato
          lPageReturn = 
            IWebConstants.ROOT_DIR + "files/siap/siep/jms/DettaglioFascicoloNonTrovato.jsp";
        }
      }
    }
    else
      lPageReturn = 
        IWebConstants.ROOT_DIR + "files/siap/siep/jms/DettaglioFascicoloNonTrovato.jsp";
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: " + lPageReturn);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("[JMS]: fine");

    return lPageReturn;
  }

}