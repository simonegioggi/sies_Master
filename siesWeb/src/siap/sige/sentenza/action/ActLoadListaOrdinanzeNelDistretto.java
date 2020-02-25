package siap.sige.sentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;

public class ActLoadListaOrdinanzeNelDistretto extends ActionSiap implements ICostantiFasSigeSentenza {

	public String processRequest() throws F3BException {

		// Ricerca Sentenze assegnate al SOGGETTO DEL FASCICOLO SIGE IN SESSIONE 
		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		SoggettoModel soggetto = null;
		BigDecimal idSoggetto = null;
		if (isSessionAttributeNullObj("FascicoloSigeEsteso")){
			// SE NON C'è IL FASCICOLO IN SESSIONE CERCO IL SOGGETTO
			if (isSessionAttributeNullObj("soggetto")){		
				// NEL CASO IN CUI NON ESISTE NEMMENO IL SOGGETTO IN SESSIONE RILANCIO L'ECCEZIONE
				throw new F3BException(F3BException.USER_MESSAGE, "Dati del Soggetto SIGE non in sessione !!");
			}
			else {
				//ID Soggetto.
		    	soggetto = (SoggettoModel)getSessionAttribute("soggetto");
		    	setRequestAttribute("IDSoggetto",soggetto.getIdSoggetto().toString());
			}
		}
		else{
			 lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
			if (lFascicoloEsteso == null || lFascicoloEsteso.getFascicoloSige() == null
					|| lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibile recuperare dalla sessione i dati fascicolo");
		}
		
		if(lFascicoloEsteso != null)
			idSoggetto = lFascicoloEsteso.getFascicoloSige().getSogIdSoggetto();
		else if(soggetto != null){
			idSoggetto = soggetto.getIdSoggetto();
		}else{
			throw new F3BException(F3BException.USER_MESSAGE, "Non è possibile recuperare dalla sessione i dati del soggetto");
		}

		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		String codTipoProvvedimento = "03";// ordinanza
		Vector<ProvvedimentoSigeEventoModel> lVect = mCtrl
				.ExRicercaProvvedimentiSigePerIdFasSigePerIdSoggetto(idSoggetto, codTipoProvvedimento);

		setRequestAttribute("ListaOrdinanze", lVect);
		setRequestAttribute("Funzione", "Lista " + "Ordinanze nel Distretto:");

		return PG_LISTA_ORDINANZE_NEL_DISTRETTO; // restituisce la jsp di VIEW
	}

}