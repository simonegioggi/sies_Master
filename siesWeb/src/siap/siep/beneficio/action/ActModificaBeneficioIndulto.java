package siap.siep.beneficio.action;

/**
* <p>Title: ActModificaBeneficioIndulto</p>
* <p>Description: Classe Action per la modifica di Beneficio Indulto</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 3.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.penaaccessoria.action.ICostantiPenaAccessoria;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaBeneficioIndulto extends ActionSiap implements ICostantiBeneficio {

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

		// ArrayList lTipologie = null;
		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		BeneficioModel lBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));

		lBenMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_COD_TIPO_BENEFICIO));
		lBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
		lBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));
		lBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		// ===========================================
		// reclusione
		// ===========================================
		String GRec = getRequestStringParameter("GRec");
		String MRec = getRequestStringParameter("MRec");
		String ARec = getRequestStringParameter("ARec");
		String Multa = getRequestStringParameter("Multa");
		String Multa_dec = getRequestStringParameter("Mul_dec");

		if (!ARec.equals(""))
			lBenMod.setNumAnniReclusione(new BigDecimal(ARec));
		else
			lBenMod.setNumAnniReclusione(null);

		if (!MRec.equals(""))
			lBenMod.setNumMesiReclusione(new BigDecimal(MRec));
		else
			lBenMod.setNumMesiReclusione(null);

		if (!GRec.equals(""))
			lBenMod.setNumGiorniReclusione(new BigDecimal(GRec));
		else
			lBenMod.setNumGiorniReclusione(null);

		if (!Multa.equals("")) {
			if (!Multa_dec.equals("")) {
				lBenMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			} else {
				lBenMod.setImportoMulta(new BigDecimal(Multa));
			}
		} else if (!Multa_dec.equals("")) {
			lBenMod.setImportoMulta(new BigDecimal("0." + Multa_dec));
		} else {
			lBenMod.setImportoMulta(null);
		}

		// ===========================================
		// arresto
		// ===========================================
		String GArr = getRequestStringParameter("GArr");
		String MArr = getRequestStringParameter("MArr");
		String AArr = getRequestStringParameter("AArr");
		String Ammenda = getRequestStringParameter("Ammenda");
		String Ammenda_dec = getRequestStringParameter("Amm_dec");

		if (!AArr.equals(""))
			lBenMod.setNumAnniArresto(new BigDecimal(AArr));
		else
			lBenMod.setNumAnniArresto(null);

		if (!MArr.equals(""))
			lBenMod.setNumMesiArresto(new BigDecimal(MArr));
		else
			lBenMod.setNumMesiArresto(null);

		if (!GArr.equals(""))
			lBenMod.setNumGiorniArresto(new BigDecimal(GArr));
		else
			lBenMod.setNumGiorniArresto(null);

		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals("")) {
				lBenMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			} else {
				lBenMod.setImportoAmmenda(new BigDecimal(Ammenda));
			}
		} else if (!Ammenda_dec.equals("")) {
			lBenMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));
		} else {
			lBenMod.setImportoAmmenda(null);
		}
		lBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataAggiornamento(DateUtils.getSysDate());

		String[] lIdPenAcc = null;
		if (!"07".equals(lBenMod.getCodSottotipoBeneficio())) {
			if (!isRequestParameterNullObj(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA)) {
				lIdPenAcc = getRequestStringParameters(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
			}
		}

		// chiama il controller
		/* BeneficioModel llBenModRet = */lCtrl.ExModificaBeneficioTipologiaOrario(lBenMod, null, lIdPenAcc,
				null);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActLoadDettaglioBeneficioIndulto&" + CAMPO_ID_BENEFICIO + "="
				+ lBenMod.getIdBeneficio().toString();
		return lPage;
	}

}