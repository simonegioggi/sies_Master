package siap.sius.fascicolo.action;

/**
* <p>Title: ActRicercaResidenzaByProcedimentoSius</p>
* <p>Description: Classe Action per la ricerca di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActRicercaResidenzaByProcedimentoSius extends ActionSius implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();
		/*
		 * if( ! isRequestParameterNullObj( ICostantiSoggetto.CAMPO_ID_SOGGETTO ) ) lResMod.setSogIdSoggetto(
		 * getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO) );
		 * 
		 * 
		 * if( ! isRequestParameterNullObj( ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS ) )
		 * lResMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP)
		 * );
		 */

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// 'Residenza' (R), 'Domicilio' (D)
		FascicoloSiusController lCtrl = new FascicoloSiusController();
		Vector lVect = lCtrl.ExRicercaResidenzaByProcedimentoSius(lIdFascicoloSius, 'R');

		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("residenze", lVect);

		return PG_RICERCARESIDENZASIUS;
	}

}