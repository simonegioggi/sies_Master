package siap.sige.reato.action;


import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaReato</p>
 * <p>Description: Classe Action per la load modifica di Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadModificaReatoSige extends ActionSige implements ICostantiReato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");
	  
	  gestioneRitorno();
	  
	  // lock
	  lockApplicativoSuReato();

    ReatoModel lMod = new ReatoModel();

      lMod.setIdReato(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ID_REATO));

      IReato lCtrl = SIEPLookupRemote.getReatoRemote();
      lMod = lCtrl.ExRicercaReatoByKey(lMod.getIdReato());

      setRequestAttribute("reato", lMod);

      Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lMod.getCodFonte());
      setRequestAttribute("TipiFontiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lMod.getCodSottonumerazione());
      setRequestAttribute("TipiSottonumerazione", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getTipoReato(), lMod.getCodTipoReato());
      setRequestAttribute("TipiReato", "" + lOption);

      lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione(), lMod.getCodPeriodoConsumazione());
      setRequestAttribute("PeriodoConsumazione", "" + lOption);

	  
      setRequestAttribute("modalita", "SIGE");
	   
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getPackage().getName() + ".processRequest : fine");

      return ICostantiReato.PG_LOAD_MODIFICAREATO; //restituisce la jsp di VIEW

  }
}