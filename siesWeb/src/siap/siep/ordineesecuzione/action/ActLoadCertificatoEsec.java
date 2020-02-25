package siap.siep.ordineesecuzione.action;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 *
 * <p>Title: ActLoadCertificaEsec</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadCertificatoEsec extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws Exception
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    if (lFascMod.getFlagValidato().equalsIgnoreCase("N"))
    {
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage(IWebConstants.PG_MAIN);
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                          "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
      lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&" +
                         ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
      setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

      return IWebConstants.PG_MESSAGE;
    }
    
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

    //  campo discriminante per la produzione di pdf/doc  
	String uffCompetenza = "1";
	if (!lUffUtente.equals(lFascMod.getCodUfficioInserimento()))
		uffCompetenza = "0";	
	setRequestAttribute("UfficioCompetenza", uffCompetenza);
	
	//this.isFascicoloSiepDiCompetenza();
      
   
    return PG_LOAD_STAMPA_CERTIFICATO;
  }
}