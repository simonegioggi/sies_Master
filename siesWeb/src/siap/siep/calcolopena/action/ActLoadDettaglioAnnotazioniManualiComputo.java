package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/*******************************************************************************
 * ATTENZIONE!!!!!! Questa classe non viene referenziata nel codice ne invocata dalle jsp 29/03/2006
 *
 *
 *
 */
public class ActLoadDettaglioAnnotazioniManualiComputo extends ActionSiap
		implements ICostantiAnnotazioneManuale {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lMotivoProvvedimento = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
		String lTipoAnnotazione = getRequestStringParameter(
				ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE);

		setRequestAttribute("MotivoProvvedimento", lMotivoProvvedimento);

		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn(
				lIdFascicolo, lTipoAnnotazione, "N");

		setRequestAttribute("ListaAnnotazioni", lListAnnMan);

		setRequestAttribute("lFlagPage", this.getRequestStringParameter("lFlagPage"));
		// per vedere se arrivo da computo altro titolo stesso o senza

		return PG_LOAD_DETTAGLIO_COMPUTO;
	}

}