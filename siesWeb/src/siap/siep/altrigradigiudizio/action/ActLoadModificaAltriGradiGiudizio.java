package siap.siep.altrigradigiudizio.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadModificaSentenzaRiunita</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActLoadModificaAltriGradiGiudizio extends ActionSiap
		implements ICostantiAltriGradiGiudizio
{
	public String processRequest() throws F3BException
	{
    // Parse della request
		BigDecimal lId = super.getRequestBigDecimalParameter(CAMPO_ID_ALTRIGRADIGIUDIZIO);

		// Riempie il model
		AltriGradiGiudizioModel lSmod = new AltriGradiGiudizioModel();

		lSmod.setIdAltrigradigiudizio(lId);
		lSmod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Chiama il contreller
		IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();

		AltriGradiGiudizioModel lSmodRet = lCtrl.ExRicercaAltriGradiGiudizioByKey(lSmod.getIdAltrigradigiudizio());

		// Inserire Eventuali ComboBOX
		//Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), lSmodRet.getCodAutEmittSentIGrado());
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), lSmodRet.getCodAutEmittSentIGrado());

		// Imposta i provvedimenti Rif.
		setRequestAttribute("autoritaEmi", "" + lOption);
		
		// Inserire Eventuali ComboBOX
		Option lOptionIIgrado = new Option(DecodificheManager.getInstance().getTipoUfficioS(), lSmodRet.getCodAutEmittSentIiGrado());

		// Imposta i provvedimenti Rif.
		setRequestAttribute("autoritaProvRif", "" + lOptionIIgrado);

		lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), lSmodRet.getCodTipoDecisioneCassazione());
	    // Imposta la decisione cassazione
	    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );
	    
	    lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), lSmodRet.getCodTipoSentenzaIiGrado());

	    setRequestAttribute("tipoProvvedimentiRif", "" + lOption );
		
		setRequestAttribute("altrigradigiudizio", lSmodRet);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

    lOption = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), lSmodRet.getCodTipoRito());
    setRequestAttribute("tipoRito1", "" + lOption );
    setRequestAttribute("tipoRito2", "" + lOption );
		
		return PG_LOAD_INSERISCIALTRIGRADIGIUDIZIO; //restituisce la jsp di VIEW
	}
}