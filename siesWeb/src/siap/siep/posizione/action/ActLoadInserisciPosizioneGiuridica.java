package siap.siep.posizione.action;

import siap.sico.lock.model.LockModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneGiuridica
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneGiuridica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciPosizioneGiuridica extends ActLoadGestionePosizioneGiuridica implements
		ICostantiPosizioneGiuridica {

	/**
	 * Azione di Load Inserisci Posizione Giuridica
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFasMod = null;

		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		} else {
			lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			isFascicoloSiepDiCompetenza();
			// Controllo che non si stia lavorando su una entità in modifica ad altri
			LockModel lck = lockIfNotLocked("posizione giuridica", lFasMod.getIdFascicoloSiep().toString(),
					getCodUtenteConnesso());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lck.getEntity()
						+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
				return IWebConstants.PG_MESSAGE;
			}

			if (isFascicoloArchiviatoDefinito())
				return IWebConstants.PG_MESSAGE;
		}

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = loadPosGiuridica(lFasMod, true);
		setRequestAttribute("PosizioneGiuridicaLuogoDetenzioneAltraCausaModel", lPosLuoAltMod);

		PosizioneGiuridicaModel lPosGiu = lPosLuoAltMod.getPosizioneGiuridica();
		if (lPosGiu != null) {
			// la posizione è gia stata inserita
			setRequestAttribute("modalita", "M");
		} else {
			// la posizione non è mai stata inserita
			setRequestAttribute("modalita", "I");
		}

		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE))
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));

		// Recupero i dati per caricare la combo per un determinato "tipo ufficio"
		UtenteModel utenteConnesso = getUtenteConnesso();
		// String codTipoUfficio = "";
		String sedeTipoUfficio = "";
		// String descComune = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			// codTipoUfficio = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
			sedeTipoUfficio = utenteConnesso.getUfficioUtente().getDescrComune();
			// descComune = utenteConnesso.getUfficioUtente().getDescrComune();
		}
		setRequestAttribute("defaultSedeTipoUfficioPM", sedeTipoUfficio);

		return PG_LOAD_INSERISCIPOSIZIONEGIURIDICA;
	}

}