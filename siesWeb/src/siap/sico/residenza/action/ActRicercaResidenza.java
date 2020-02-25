package siap.sico.residenza.action;

/**
* <p>Title: ActRicercaResidenza</p>
* <p>Description: Classe Action per la ricerca di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActRicercaResidenza extends ActionSiap implements ICostantiResidenza {

	public String processRequest() throws F3BException {

		ResidenzaModel lResMod = new ResidenzaModel();

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
			lResMod.setSogIdSoggetto(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));

		/*
		 * if( ! isRequestParameterNullObj( ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP ) )
		 * lResMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP)
		 * );
		 */

		// 'Residenza' (R), 'Domicilio' (D)
		lResMod.setCodTipoResidenza("R");

		ResidenzaController lCtrl = new ResidenzaController();
		Vector lVect = lCtrl.ExRicercaResidenza(lResMod);

		setRequestAttribute("residenze", lVect);

		return PG_RICERCARESIDENZA;
	}

}