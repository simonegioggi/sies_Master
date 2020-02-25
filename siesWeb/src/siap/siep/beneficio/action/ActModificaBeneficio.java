package siap.siep.beneficio.action;

/**
* <p>Title: ActModificaBeneficio</p>
* <p>Description: Classe Action per la modifica di Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.tipologiaorario.action.ICostantiTipologiaOrario;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaBeneficio extends ActionSiap implements ICostantiBeneficio, ICostantiTipologiaOrario {

	/**
	 * Azione di Modifica del Beneficio
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("beneficio", getRequestStringParameter(CAMPO_ID_BENEFICIO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		ArrayList lTipologie = null;
		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		BeneficioModel lBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));

		lBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
		if (!"-".equals(lBenMod.getCodSottotipoBeneficio())) {
			lBenMod.setCodTipoBeneficio("01");
			lBenMod.setCodNaturaBeneficio("C");
			lBenMod.setCodDpr("-");
		} else {
			lBenMod.setCodTipoBeneficio("02");
		}

		BeneficioModel lBenNMMod = null;
		// creazione beneficio per non menzione
		if (isRequestChecked(CAMPO_FLAG_NON_MENZIONE)) {
			lBenNMMod = new BeneficioModel();
			lBenNMMod.setCodTipoBeneficio("02");
			lBenNMMod.setCodSottotipoBeneficio("-");
			lBenNMMod.setCodNaturaBeneficio("C");
			lBenNMMod.setCodDpr("-");

			lBenNMMod.setCodTipoSospSubordinata("-");

			lBenNMMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lBenNMMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lBenNMMod.setDataInserimento(DateUtils.getSysDate());
			lBenNMMod.setFasSieIdFascicoloSiep(lBenMod.getFasSieIdFascicoloSiep());
			lBenNMMod.setRifCodTipoAutoEmittente("-");
			lBenNMMod.setRifCodTipoProvvedimento("-");
		}

		lBenMod.setNumAnniSospensione(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SOSPENSIONE));

		if (!this.isRequestParameterNullObj(CAMPO_COD_TIPO_SOSP_SUBORDINATA)) {
			lBenMod.setCodTipoSospSubordinata(getRequestStringParameter(CAMPO_COD_TIPO_SOSP_SUBORDINATA));

			lBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

			lBenMod.setNumAnniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ADEMPIMENTO));
			lBenMod.setNumMesiAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ADEMPIMENTO));
			lBenMod.setNumGiorniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ADEMPIMENTO));
		} else {
			lBenMod.setCodTipoSospSubordinata("-");
			lBenMod.setNote(null);
			lBenMod.setNumAnniAdempimento(null);
			lBenMod.setNumMesiAdempimento(null);
			lBenMod.setNumGiorniAdempimento(null);
		}

		lBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataAggiornamento(DateUtils.getSysDate());

		if (!this.isRequestParameterNullObj(CAMPO_NUM_GIORNI_PRESTAZIONE)) {
			lBenMod.setNumGiorniPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PRESTAZIONE));
			lBenMod.setNumMesiPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PRESTAZIONE));
			lBenMod.setNumOreSettimanali(getRequestBigDecimalParameter(CAMPO_NUM_ORE_SETTIMANALI));

			if (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null
					&& (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("1")) {
				lBenMod.setFlagFrequenzaSettimanale("N"); // Non determinata
			} else if (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null
					&& (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("2")) {

				lBenMod.setFlagFrequenzaSettimanale("D"); // Determinata
			}

			if (lBenMod.getFlagFrequenzaSettimanale().equals("D")) {
				// TIPOLOGIA ORARIO
				lTipologie = new ArrayList();
				// Lunedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_LUN)) {
					TipologiaOrarioModel lTipOrLunMod = new TipologiaOrarioModel();
					lTipOrLunMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_LUN));
					lTipOrLunMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_LUN));
					lTipOrLunMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_LUN));
					lTipOrLunMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrLunMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrLunMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrLunMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrLunMod);
				}

				// Martedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MAR)) {
					TipologiaOrarioModel lTipOrMarMod = new TipologiaOrarioModel();
					lTipOrMarMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MAR));
					lTipOrMarMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MAR));
					lTipOrMarMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MAR));
					lTipOrMarMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrMarMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrMarMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrMarMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrMarMod);
				}

				// Mercoledì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MER)) {
					TipologiaOrarioModel lTipOrMerMod = new TipologiaOrarioModel();
					lTipOrMerMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MER));
					lTipOrMerMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MER));
					lTipOrMerMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MER));
					lTipOrMerMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrMerMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrMerMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrMerMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrMerMod);
				}

				// Giovedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_GIOV)) {
					TipologiaOrarioModel lTipOrGiovMod = new TipologiaOrarioModel();
					lTipOrGiovMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_GIOV));
					lTipOrGiovMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_GIOV));
					lTipOrGiovMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_GIOV));
					lTipOrGiovMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrGiovMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrGiovMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrGiovMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrGiovMod);
				}

				// Venerdì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_VEN)) {
					TipologiaOrarioModel lTipOrVenMod = new TipologiaOrarioModel();
					lTipOrVenMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_VEN));
					lTipOrVenMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_VEN));
					lTipOrVenMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_VEN));
					lTipOrVenMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrVenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrVenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrVenMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrVenMod);
				}

				// Sabato
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_SAB)) {
					TipologiaOrarioModel lTipOrSabMod = new TipologiaOrarioModel();
					lTipOrSabMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_SAB));
					lTipOrSabMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_SAB));
					lTipOrSabMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_SAB));
					lTipOrSabMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrSabMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrSabMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrSabMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrSabMod);
				}

				// Domenica
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_DOM)) {
					TipologiaOrarioModel lTipOrDomMod = new TipologiaOrarioModel();
					lTipOrDomMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_DOM));
					lTipOrDomMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_DOM));
					lTipOrDomMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_DOM));
					lTipOrDomMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrDomMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrDomMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrDomMod.setDataInserimento(DateUtils.getSysDate());

					lTipologie.add(lTipOrDomMod);
				}
			}
		} else {
			lBenMod.setNumGiorniPrestazione(null);
			lBenMod.setNumMesiPrestazione(null);
			lBenMod.setNumOreSettimanali(null);
			lBenMod.setFlagFrequenzaSettimanale(null);
		}

		// chiama il controller
		BeneficioModel llBenModRet = new BeneficioModel();
		llBenModRet = lCtrl.ExModificaBeneficioTipologiaOrario(lBenMod, lTipologie, null, lBenNMMod);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActLoadDettaglioBeneficio&" + CAMPO_ID_BENEFICIO + "="
				+ llBenModRet.getIdBeneficio().toString();
		return lPage;
	}

}