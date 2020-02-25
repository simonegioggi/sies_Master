package siap.siep.fascicolo.action;

/**
* <p>Title: ActRicercaFascicolo</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: </p>
* @author unascribed
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadFascicoloInSessione extends ActionSiap implements ICostantiFascicoloSiep
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
public String processRequest() throws Exception
{
      //Istanzio il Model
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();

      BigDecimal lChiaveProgr = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
      BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);

      lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
      lFasMod.setChiaveProgr(lChiaveProgr);
      lFasMod.setChiaveAnno(lChiaveAnno);

			//FascicoloSiepController lCtrl = new FascicoloSiepController();
      IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      if (lFasRet == null)
        throw new SIEPException(F3BException.USER_MESSAGE,"Nessun Fascicolo con Anno "+ lChiaveAnno+" e Progressivo " +lChiaveProgr);

      String lReturnPage = "";

      lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
          "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
          CAMPO_ID_FASCICOLO_SIEP + "=" +
          lFasRet.getIdFascicoloSiep().toString();

			setRequestAttribute("fascicolo", lFasRet);
      setSessionAttribute("fascicolo",lFasRet);
      setSessionAttribute("soggetto",lFasRet.getSoggetto());
      setSessionAttribute("sentenza",lFasRet.getSentenza());

      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("<<<<<<<" + getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE));

      if (!isRequestParameterNullObj(CAMPO_AZIONE_CHIAMANTE))
      {
        // lPage = getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE);
        lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
            "=" + getRequestStringParameter(CAMPO_AZIONE_CHIAMANTE) +"&"+FASCICOLO_RICERCATO+"=SI" ;
      }

      return lReturnPage;

  }
}
