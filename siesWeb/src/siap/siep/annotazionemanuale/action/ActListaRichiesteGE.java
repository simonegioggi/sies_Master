package siap.siep.annotazionemanuale.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActListaRichiesteGE extends ActionSiap implements ICostantiAnnotazioneManuale {

	/**
	 * Recupera la lista delle annotazioni manuali con richiesta la GE da caricare nella popup con e esenza
	 * anticipazione degli effetti, ma già validate
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		String lBeneficio = getRequestStringParameter("beneficio");
		String lCodAnnotazione = "000";
		if (lBeneficio.compareTo("AMNI") == 0)
			lCodAnnotazione = "002";
		else if (lBeneficio.compareTo("DEPEN") == 0)
			lCodAnnotazione = "004";
		else if (lBeneficio.compareTo("INCOST") == 0)
			lCodAnnotazione = "013";

		// ==========================================================================
		// Recupero le richieste al GE di Amnistia/Indulto con e senza anticipazione
		// validate. Tali annotazioni verranno mostrate nella form
		// ==========================================================================
		AnnotazioneManualeModel lAnnPerRicerca = new AnnotazioneManualeModel();
		lAnnPerRicerca.setFasSieIdFascicoloSiep(lIdFascicoloSiep);

		// lAnnPerRicerca.setCodTipoAnnotazione("002"); // ricerca sia 002 che 003
		lAnnPerRicerca.setCodTipoAnnotazione(lCodAnnotazione);
		lAnnPerRicerca.setFlagAppProvvisoria("RICHIESTE"); // FLAG_APP_PROVVISORIA='R' OR
															// FLAG_APP_PROVVISORIA='A'
		lAnnPerRicerca.setFlagValidato("S");

		Vector lListaRichieste = new Vector();
		try {
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lListaRichieste = IAnn.ExRicercaAnnotazioneManualeGenerico(lAnnPerRicerca);
		} catch (F3BException ex) {
			if (ex.getErrorCode() == SIEPException.EX_NOT_FOUND) {
				// non faccio nulla
			} else
				throw ex;
		}

		setRequestAttribute("RichiesteAlGE", lListaRichieste);
		setRequestAttribute("beneficio", lBeneficio);

		return PG_LISTA_RICHIESTE_AL_GE;
	}

}