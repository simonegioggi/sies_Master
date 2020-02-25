/**
 * 
 */
package siap.siep.nuovaistanza.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * ActLoadInoltroPM
 * @author Giselda De Vita
 *
 */
public class ActLoadAnnullaAssociaRIaFascicoloSIEP extends ActionSiap  implements ICostantiNuovaIstanza
{


	/*Controllo se un fascicolo e' presente in sessione altrimenti lo faccio selezionare*/
	public String processRequest() throws F3BException {
		if( isSessionAttributeNullObj("fascicolo") )
		{
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			"=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
			ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
			"siap.siep.nuovaistanza.action.ActLoadAnnullaAssociaRIaFascicoloSIEP&" +
			"TipoFasc=NuovaIstanza";	
			return lPage;
		}

		isFascicoloSiepDiCompetenza();

		//ricerca Istanza per il fascicolo in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

		if(lFascMod.getChiaveProgr().intValue()<90000)
		{
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il procedimento corrente non è un Registro Istanza." +
					lFascMod.getChiaveAnno()+"/"+lFascMod.getChiaveProgr()+"<br>Impossibile procedere.");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "/jsp/Main.jsp?Action=siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=21120240"); 

			return IWebConstants.PG_MESSAGE;
		}
		
		if (lFascMod.getFasSieIdFascicoloSiep() == null)
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Il registro istanza non è collegato ad alcun fascicolo siep!");
		
		IFascicoloSiep fsCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasSiepMod = fsCtrl.ExRicercaFascicoloByKey(lFascMod.getFasSieIdFascicoloSiep());

		if (lFasSiepMod == null)
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Il registro istanza non è collegato ad alcun fascicolo siep!");
		
		setRequestAttribute("fascicoloSiep", lFasSiepMod);

		// Restituisce SUCCESS 
		return PG_LOAD_ANNULLA_ASSOCIA_ISTANZA;
		 
	}


}