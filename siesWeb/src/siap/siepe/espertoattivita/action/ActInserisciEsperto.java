package siap.siepe.espertoattivita.action;


/**
* <p>Title: ActInserisciEspertoAttivita</p>
* <p>Description: Classe Action per l'inserimento di EspertoAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.attivita.action.ICostantiAttivita;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.esperto.action.ICostantiEsperto;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActInserisciEsperto extends ActionSiap implements ICostantiEspertoAttivita
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Inserimento del EspertoAttivita
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
   public String processRequest() throws Exception
   {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(this.getClass().getName()  + ".processRequest(): inizio");

      EspertoAttivitaModel lEspMod = new EspertoAttivitaModel();
      lEspMod.setDataInizio( getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
      lEspMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lEspMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lEspMod.setDataInserimento(DateUtils.getSysDate());
      lEspMod.setEspIdEsperto( getRequestBigDecimalParameter(ICostantiEsperto.CAMPO_ID_ESPERTO ) );
      lEspMod.setAttIdAttivita( getRequestBigDecimalParameter(ICostantiAttivita.CAMPO_ID_ATTIVITA ) );


      //---Aggiungere in SIEPELookupRemote il metodo getEspertoAttivitaRemote()

      IEspertoAttivita lCtrl = SIEPELookupRemote.getEspertoAttivitaRemote();
      EspertoAttivitaModel llEspModRet = lCtrl.ExInserisciEspertoAttivita(lEspMod);		 // setta la risposta nella request
      setRequestAttribute("espertoattivita", llEspModRet);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(this.getClass().getName()  + ".processRequest(): fine");

      return ritornoDopoCancellazione("Inserito Esperto", null);

      //Prepara la pagina di destinazione
     /* String lPage = "";
      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.espertoattivita.action.ActLoadDettaglioEspertoAttivita&"+CAMPO_ID_ESPERTO_ATTIVITA+"="+llEspModRet.getIdEspertoAttivita().toString();
      return lPage;
          */
   }
}