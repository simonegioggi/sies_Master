package siap.sige.impugnazione.action;

/**
* <p>Title: ActRicercaFSPImpugnazioneSige</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIGE finalizzata alla trasmissione atti</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;


public class ActLoadProvveddimentiOpposizioni extends ActionSige implements ICostantiImpugnazioneSige {
	
  public String processRequest() throws Exception {    
      setLinkRitorno();
      FascicoloSigeEstesoModel lFasEst = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
      ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
      lProvSige.setFasIdFascicoloSige(lFasEst.getFascicoloSige().getIdFascicoloSige());
      IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
    
      // 30//11/2018 aggiungo blocco su richiesta Nunzia (email del 29/11/2018 -Unificazione procedimento.docx)
      if (IsFascicoloUnificato() || IsFascicoloDefinito())
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Non è possibile emettere un ricorso/opposizione per questo Procedimento!");
      
      Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvedimentiSigePerOpposizioni(lFasEst.getFascicoloSige().getIdFascicoloSige());
      //BigDecimal countImpugnazioni = new BigDecimal ( lImpugnazioni.size());
      String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
      setRequestAttribute("tipoUfficio", strCodTipoUfficio);

      setRequestAttribute("provvedimenti", lVect);
      String lReturnPage = "";
      if (lVect != null && (lVect.size() == 0 || lVect.size() > 1))
    	  // 0 o piu di un elemento allora mostra elenco
    	  lReturnPage = PG_ELENCOPROVVEDIMENTISIGEXIMPUGNAZIONE;
      else {
    	  // un solo elemento allora mostra il dettaglio
    	  ProvvedimentoSigeEventoModel lPSMod  = (ProvvedimentoSigeEventoModel)lVect.get(0);
    	  setRequestAttribute("provvedimento", lPSMod);

          // Imposta ComboBOX Soggetto Impugnante.
          //Option lOption = new Option( DecodificheManager.getInstance().getSoggettoImpugnante());
          //String[] lFilterSoImp = {"01","02","03","05"};
          //lOption.setFilter( lFilterSoImp );
    	  Option lOption = new Option( DecodificheManager.getInstance().getSoggettoImpugnanteSige());
          setRequestAttribute("soggettoImpugnante", "" + lOption );
          
          // Imposta ComboBOX Autorita Destinataria
          lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
          String[] lFilterCSS = {"CSS"};
          lOption.setFilter( lFilterCSS );
     
          setRequestAttribute("ListaUffici", ""+ lOption);

    	  lReturnPage = PG_LOAD_INSERISCIIMPUGNAZIONESIGE;
      }
      
      return lReturnPage;
  }
}