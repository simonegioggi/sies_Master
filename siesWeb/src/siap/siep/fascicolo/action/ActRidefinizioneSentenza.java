package siap.siep.fascicolo.action;

/**
* <p>Title: ActRidefinizioneSentenza</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: </p>
* @author unascribed
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActRidefinizioneSentenza extends ActionSiap implements ICostantiFascicoloSiep
{
public String processRequest() throws Exception
{
      //Istanzio il Model
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();

      BigDecimal lChiaveProgr = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
      BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);

      lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
      lFasMod.setChiaveProgr(lChiaveProgr);
      lFasMod.setChiaveAnno(lChiaveAnno);

	  FascicoloSiepController lCtrl = new FascicoloSiepController();      
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

      if (lFasRet == null)
        throw new SIEPException(F3BException.USER_MESSAGE,"Nessun Procedimento con Anno "+ lChiaveAnno+" e Progressivo " +lChiaveProgr);

      if (lFasRet.getFlagValidato().equalsIgnoreCase("S"))
        throw new SIEPException(F3BException.USER_MESSAGE,"Impossibile ridefinire la sentenza. Il procedimento con Anno "+ lChiaveAnno+" e Progressivo " +lChiaveProgr + " è validato.");
      
      BigDecimal idFasSiep = lFasRet.getIdFascicoloSiep();
      
      lCtrl.ExRidefinisciSentenzaFascicoloSiep(idFasSiep, getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
                  
      String lReturnPage = "";

      lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
          "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
          CAMPO_ID_FASCICOLO_SIEP + "=" + idFasSiep.toString();

	  //setRequestAttribute("fascicolo", lFasRet);
      //setSessionAttribute("fascicolo",lFasRet);

      return lReturnPage;

  }
}