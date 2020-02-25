package siap.siep.beneficio.action;

/**
* <p>Title: ActModificaRevocaSospCondAP</p>
* <p>Description: Classe Action per la Modifica della Revoca Beneficio (Sospensione Condizionale)</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione di Modifica della Revoca Beneficio (Sospensione/Non Menzione)
 * 
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */

public class ActModificaRevocaSospCondAP extends ActionSiap implements ICostantiBeneficio {
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("beneficio", getRequestStringParameter(CAMPO_ID_BENEFICIO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare la Modifica più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// per iscrizione guidata
		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		// BENEFICI
		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		BeneficioModel lBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));

		lBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataAggiornamento(DateUtils.getSysDate());

		// Se non viene passato ID_Sentenza, non si devono cambiare icampi relativi ?????
		if (!getRequestStringParameter(CAMPO_SEN_ID_SENTENZA).equals("")) {
			lBenMod.setRifIdProvvedimento(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
			ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
			SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(lBenMod.getRifIdProvvedimento());

			lBenMod.setRifAnnoProvvedimento(aSent.getAnnoSentenza());
			lBenMod.setRifNumeroProvvedimento(aSent.getNumeroSentenza());
		}

		lBenMod.setRifCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
		lBenMod.setRifCodTipoAutoEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lBenMod.setRifCodLuogoAutoEmittente(lComMod.getCodComune());

		lBenMod.setRifNumSezioneAutoEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_IRREVOCABILITA,
				CAMPO_MESE_IRREVOCABILITA, CAMPO_GIORNO_IRREVOCABILITA));

		String AnnoProv = getRequestStringParameter(CAMPO_ANNO_PROVVEDIMENTO);
		String MeseProv = getRequestStringParameter(CAMPO_MESE_PROVVEDIMENTO);
		String GiornoProv = getRequestStringParameter(CAMPO_GIORNO_PROVVEDIMENTO);
		lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		// IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote(); è fatta sopra
		lCtrl.ExModificaBeneficio(lBenMod);

		// Prepara la destinazione
		String lPage = "";

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActRicercaRevocaSospCondAP";

		return lPage;
	}
}