package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadRichiestaApplicazioneBenefici
 * </p>
 * <p>
 * Description: Azione Load per l'inserimento della Richiesta di Applicazione Benefici.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadRichiestaApplicazioneBenefici extends ActionSiap implements ICostantiCumulo {
	/**
	 * Azione di caricamento della form d'inserimento della richiesta Applicazione Benefici
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lPage = checkFascicolo();

		if (lPage != null) {
			// verifico se presente errore perchè validato
			if (!isRequestAttributeNullObj(IWebConstants.MESSAGE_TEXT)) {
				String lMessaggio = (String) this.getRequestAttribute(IWebConstants.MESSAGE_TEXT);
				// Richieste indulto anche sui fascicoli archiviati
				if (lMessaggio.indexOf("Definito") == -1) {
					return lPage;
				}
			} else {
				return lPage;
			}
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero la Posizione Giuridica
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero la Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// Identificativo della Pena Residua
		BigDecimal lIdPenaResidua = null;
		lIdPenaResidua = getRequestBigDecimalParameter("IdPenaResidua");
		setRequestAttribute("IdPenaResidua", lIdPenaResidua.toString());

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
		setRequestAttribute("PenaComplessiva", lPenComMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici(),
				"002");
		setRequestAttribute("TipoAnnotazioneManuale", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDPR());
		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();
		DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();
		lOption.setSelected(lDecMod.getCode());
		setRequestAttribute("listaDPR", "" + lOption);

		return PG_LOAD_RICHIESTA_APPLICAZIONE_BENEFICI;
	}

	protected String checkFascicolo() throws F3BException {
		String lPage = null;

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		this.isFascicoloSiepDiCompetenza();

		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		if (this.isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		return lPage;

	}
}