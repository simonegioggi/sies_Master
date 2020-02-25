package siap.siep.beneficio.action;

/**
* <p>Title: ActInserisciRevocandultoAP</p>
* <p>Description: Classe Action per l'inserimento della Revoca Beneficio (Indulto in Altro Provvedimento)</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
*/

import java.math.BigDecimal;
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
 * Azione di Inserimento della Revoca Beneficio (Revova Indulto)
 * 
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */
public class ActInserisciRevocaIndultoAP extends ActionSiap implements ICostantiBeneficio {

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

		lBenMod.setCodNaturaBeneficio("R");
		lBenMod.setCodTipoBeneficio("03");

		lBenMod.setCodTipoSospSubordinata("-");
		lBenMod.setCodSottotipoBeneficio("-");

		lBenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lBenMod.setDataInserimento(DateUtils.getSysDate());

		lBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));
		lBenMod.setRifCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
		lBenMod.setRifCodTipoAutoEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lBenMod.setRifCodLuogoAutoEmittente(lComMod.getCodComune());

		String SezioAuto = getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE);
		if (SezioAuto.compareTo("") != 0)
			lBenMod.setRifNumSezioneAutoEmittente(
					getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		String AnnoProv = getRequestStringParameter(CAMPO_ANNO_PROVVEDIMENTO);
		String MeseProv = getRequestStringParameter(CAMPO_MESE_PROVVEDIMENTO);
		String GiornoProv = getRequestStringParameter(CAMPO_GIORNO_PROVVEDIMENTO);
		lBenMod.setRifDataProvvedimento(DateUtils.getDate(AnnoProv, MeseProv, GiornoProv));

		// se il provvedimento è un ORDINANZA (cod = 03), NON c'è nessuna sentenza di riferimento:
		// Quindi non vangono inseriti Data Irrevocabilità,
		// Id Riferimento Sentenza,
		// Anno e Numero Sentenza di Riferimento

		if (!getRequestStringParameter(CAMPO_SEN_ID_SENTENZA).equals("")) {
			lBenMod.setRifIdProvvedimento(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
			ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
			SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(lBenMod.getRifIdProvvedimento());

			lBenMod.setRifAnnoProvvedimento(aSent.getAnnoSentenza());
			lBenMod.setRifNumeroProvvedimento(aSent.getNumeroSentenza());
		}

		String TipoProv = getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO);
		if (TipoProv.compareTo("03") != 0) {
			lBenMod.setRifDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_IRREVOCABILITA,
					CAMPO_MESE_IRREVOCABILITA, CAMPO_GIORNO_IRREVOCABILITA));
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

		if (!getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT").equals("")) {
			if (!getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC").equals(""))
				lBenMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")
						+ "." + getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")));
			else
				lBenMod.setImportoMulta(
						new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")));
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

		if (!getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT").equals("")) {
			if (!getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC").equals(""))
				lBenMod.setImportoAmmenda(
						new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") + "."
								+ getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")));
			else
				lBenMod.setImportoAmmenda(
						new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")));
		} else {
			lBenMod.setImportoAmmenda(new BigDecimal("0"));
		}

		// Fine Arresto Ammenda , Reclusione Multa

		String Note = getRequestStringParameter(CAMPO_NOTE);
		if (Note.compareTo("") != 0)
			lBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		VBMModel.add(lBenMod);
		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		lCtrl.ExInserisciBeneficio(VBMModel);

		lBenMod = new BeneficioModel();
		VBMModel.clear();

		// Prepara la destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.beneficio.action.ActRicercaRevocaIndultoAP";

		return lPage;
	}

}