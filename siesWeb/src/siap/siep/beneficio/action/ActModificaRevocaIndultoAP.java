package siap.siep.beneficio.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Azione di Modofica della Revoca Beneficio (Indulto)
 * 
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */
public class ActModificaRevocaIndultoAP extends ActionSiap implements ICostantiBeneficio {

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

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		// BENEFICI
		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		BeneficioModel lBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));

		/*BigDecimal Fasc_ID = */lFascMod.getIdFascicoloSiep();

		lBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataAggiornamento(DateUtils.getSysDate());

		lBenMod.setRifCodTipoAutoEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		lBenMod.setRifCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lBenMod.setRifCodLuogoAutoEmittente(lComMod.getCodComune());

		lBenMod.setRifNumSezioneAutoEmittente(getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
		lBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));

		String AnnoProv = getRequestStringParameter(CAMPO_ANNO_PROVVEDIMENTO);
		String MeseProv = getRequestStringParameter(CAMPO_MESE_PROVVEDIMENTO);
		String GiornoProv = getRequestStringParameter(CAMPO_GIORNO_PROVVEDIMENTO);
		lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		// se il provvedimento è un ORDINANZA (cod = 03), NON c'è nessuna sentenza di riferimento:
		// Quindi non vengono inseriti Data Irrevocabilità,
		// Id Riferimento Sentenza,
		// Anno e Numero Sentenza di Riferimento

		String TipoProv = getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO);
		if (TipoProv.compareTo("03") != 0) {
			lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_IRREVOCABILITA,
					CAMPO_MESE_IRREVOCABILITA, CAMPO_GIORNO_IRREVOCABILITA));
		} else {
			// lBenMod.setRifDataIrrevocabilita("");
			// lBenMod.setRifIdProvvedimento("");
			lBenMod.setRifNumeroProvvedimento("");
			// lBenMod.setRifAnnoProvvedimento();
		}

		if (!getRequestStringParameter(CAMPO_SEN_ID_SENTENZA).equals("")) {
//			String IdProv = getRequestStringParameter(CAMPO_SEN_ID_SENTENZA);
			lBenMod.setRifIdProvvedimento(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));

			ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
			SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(lBenMod.getRifIdProvvedimento());

			lBenMod.setRifAnnoProvvedimento(aSent.getAnnoSentenza());
			lBenMod.setRifNumeroProvvedimento(aSent.getNumeroSentenza());
		}

		// - - - Reclusione e Multa

		String AArecl = getRequestStringParameter(CAMPO_NUM_ANNI_RECLUSIONE);
		if (!AArecl.equals(""))
			lBenMod.setNumAnniReclusione(new BigDecimal(AArecl));
		else
			lBenMod.setNumAnniReclusione(new BigDecimal("0"));

		String MMrecl = getRequestStringParameter(CAMPO_NUM_MESI_RECLUSIONE);
		if (!MMrecl.equals(""))
			lBenMod.setNumMesiReclusione(new BigDecimal(MMrecl));
		else
			lBenMod.setNumMesiReclusione(new BigDecimal("0"));

		String GGrecl = getRequestStringParameter(CAMPO_NUM_GIORNI_RECLUSIONE);
		if (!GGrecl.equals(""))
			lBenMod.setNumGiorniReclusione(new BigDecimal(GGrecl));
		else
			lBenMod.setNumGiorniReclusione(new BigDecimal("0"));

		String Multa_INT = getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT");
		String Multa_DEC = getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC");

		if (!Multa_INT.equals("")) {
			if (!Multa_DEC.equals(""))
				lBenMod.setImportoMulta(new BigDecimal(Multa_INT + "." + Multa_DEC));
			else
				lBenMod.setImportoMulta(new BigDecimal(Multa_INT));
		} else {
			lBenMod.setImportoMulta(new BigDecimal("0"));
		}

		// - - - Arresto e Ammenda

		String AAarr = getRequestStringParameter(CAMPO_NUM_ANNI_ARRESTO);
		if (!AAarr.equals(""))
			lBenMod.setNumAnniArresto(new BigDecimal(AAarr));
		else
			lBenMod.setNumAnniArresto(new BigDecimal("0"));

		String MMarr = getRequestStringParameter(CAMPO_NUM_MESI_ARRESTO);
		if (!MMarr.equals(""))
			lBenMod.setNumMesiArresto(new BigDecimal(MMarr));
		else
			lBenMod.setNumMesiArresto(new BigDecimal("0"));

		String GGarr = getRequestStringParameter(CAMPO_NUM_GIORNI_ARRESTO);
		if (!GGarr.equals(""))
			lBenMod.setNumGiorniArresto(new BigDecimal(GGarr));
		else
			lBenMod.setNumGiorniArresto(new BigDecimal("0"));

		String Ammenda_INT = getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT");
		String Ammenda_DEC = getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC");

		if (!Ammenda_INT.equals("")) {
			if (!Ammenda_DEC.equals(""))
				lBenMod.setImportoAmmenda(new BigDecimal(Ammenda_INT + "." + Ammenda_DEC));
			else
				lBenMod.setImportoAmmenda(new BigDecimal(Ammenda_INT));
		} else {
			lBenMod.setImportoAmmenda(new BigDecimal("0"));
		}

		// Fine Arresto Ammenda , Reclusione Multa

		lCtrl.ExModificaBeneficio(lBenMod);

		// Prepara la destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActRicercaRevocaIndultoAP";

		return lPage;
	}

}