package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadDettaglioPenaComplessiva - Classe Action per la load dettaglio di PenaComplessiva
 *
 * @version 1.0
 */
public class ActLoadDettaglioPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva {
	
	public String processRequest() throws Exception {

		// paramentro passato solo nel caso di iscrizione guidata
		if (!isRequestAttributeNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestAttribute("lTipoFunzione"));

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();

		DettaglioPenaComplessivaModel lDettMod = null;

		if (!isRequestParameterNullObj(CAMPO_ID_PENA_COMPLESSIVA)) {
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);
			lDettMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByKey(lId);
		} else if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			BigDecimal lIdFascicolo = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			lDettMod = lCtrl.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(lIdFascicolo);
		}

		if (lDettMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Pena Complessiva non presente");

		setRequestAttribute("dettaglioPenaComplessiva", lDettMod);

		// richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena
		// paolo cherubini lunedi 11/10/2010
		// Ricerca l'ultima pena residua per quel fascicolo
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equals("N")) {
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenaResMod = lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
			if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
				setRequestAttribute("lPenaResMod", lPenaResMod);
		}
		// fine a9/rr/075

		// pagina di ritorno
		return PG_LOAD_DETTAGLIOPENACOMPLESSIVA;
	}

}