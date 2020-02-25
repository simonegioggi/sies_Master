package siap.siep.beneficio.action;

/**
* <p>Title: ActInserisciRevocaSospCondAP</p>
* <p>Description: Classe Action per l'inserimento della Revoca Beneficio (Sospensione Condizionale/Non Menzione)</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione di Inserimento della Revoca Beneficio (In particolare Sospensione Condizionale e Non Menzione
 * 
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */

public class ActInserisciRevocaSospCondAP extends ActionSiap implements ICostantiBeneficio {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// per iscrizione guidata
		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// BENEFICI
		BeneficioModel lBenMod = new BeneficioModel();
		Vector VBMModel = new Vector();

		String InseritoSiNo = "";

		lBenMod.setCodNaturaBeneficio("R");
		lBenMod.setCodDpr("-");
		lBenMod.setCodTipoSospSubordinata("-");
		lBenMod.setCodSottotipoBeneficio("-");

		lBenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataInserimento(DateUtils.getSysDate());

		// Se non viene passato ID_Sentenza, la revoca inserita non ha alcun riferimento
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

		String SezioAuto = getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE);
		if (SezioAuto.compareTo("") != 0)
			lBenMod.setRifNumSezioneAutoEmittente(
					getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_IRREVOCABILITA,
				CAMPO_MESE_IRREVOCABILITA, CAMPO_GIORNO_IRREVOCABILITA));

		String AnnoProv = getRequestStringParameter(CAMPO_ANNO_PROVVEDIMENTO);
		String MeseProv = getRequestStringParameter(CAMPO_MESE_PROVVEDIMENTO);
		String GiornoProv = getRequestStringParameter(CAMPO_GIORNO_PROVVEDIMENTO);
		lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		// - - - - - - Insert Revoca di Tipo SOSPENSIONE CONDIZIONALE - - - - - - - -
		// - - - - - - aggiungendo model "lBenMod" al Vettore "VBMModel" - - - - - - - -

		if (isRequestChecked(CAMPO_FLAG_SOSP_COND)) {
			lBenMod.setCodTipoBeneficio("01");
			VBMModel.add(lBenMod);
			lBenMod = new BeneficioModel();
			InseritoSiNo = "SI";
		}

		if (InseritoSiNo == "SI") {
			lBenMod.setCodNaturaBeneficio("R");
			lBenMod.setCodDpr("-");
			lBenMod.setCodTipoSospSubordinata("-");
			lBenMod.setCodSottotipoBeneficio("-");

			lBenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

			lBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lBenMod.setDataInserimento(DateUtils.getSysDate());

			// Se non viene passato ID_Sentenza, la revoca inserita non ha alcun riferimento
			if (!getRequestStringParameter(CAMPO_SEN_ID_SENTENZA).equals("")) {
				lBenMod.setRifIdProvvedimento(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
				ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
				SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(lBenMod.getRifIdProvvedimento());

				lBenMod.setRifAnnoProvvedimento(aSent.getAnnoSentenza());
				lBenMod.setRifNumeroProvvedimento(aSent.getNumeroSentenza());
			}

			lBenMod.setRifCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
			lBenMod.setRifCodTipoAutoEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

			lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
			lBenMod.setRifCodLuogoAutoEmittente(lComMod.getCodComune());

			SezioAuto = getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE);
			if (SezioAuto.compareTo("") != 0)
				lBenMod.setRifNumSezioneAutoEmittente(
						getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

			lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_IRREVOCABILITA,
					CAMPO_MESE_IRREVOCABILITA, CAMPO_GIORNO_IRREVOCABILITA));

			AnnoProv = getRequestStringParameter(CAMPO_ANNO_PROVVEDIMENTO);
			MeseProv = getRequestStringParameter(CAMPO_MESE_PROVVEDIMENTO);
			GiornoProv = getRequestStringParameter(CAMPO_GIORNO_PROVVEDIMENTO);
			lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		}

		// - - - - - - (se presente) Insert Revoca di Tipo NON MENZIONE - - - - - - - -
		// - - - - - - aggiungendo l'eventuale model "lBenMod" al Vettore "VBMModel" - - - - - - - -
		if (isRequestChecked(CAMPO_FLAG_NON_MENZIONE)) {
			lBenMod.setCodTipoBeneficio("02");
			VBMModel.add(lBenMod);
			lBenMod = new BeneficioModel();
		}

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		lCtrl.ExInserisciBeneficio(VBMModel);

		VBMModel.clear();

		// Prepara la destinazione
		String lPage = "";

		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.beneficio.action.ActRicercaRevSospCondAP&" +
		// CAMPO_COD_NATURA_BENEFICIO + "=" + lBenMod.getCodNaturaBeneficio();

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActRicercaRevocaSospCondAP";

		return lPage;
	}

}