package siap.siep.ordineesecuzione.action;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciVariazioneDecorrenzaScadenza extends ActionSiap
		implements ICostantiOrdineEsecuzione {
	/**
	 * Viene recuperata la NUOVA data fine pena altra causa e rieffettuati i calcoli di decorrenza/scadenza
	 * questa causa utilizzando come data di decorrenza il fine pena altra causa + 1 giorno.
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// recupero fascicolo
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// recupero posizione giuridica e annessi
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo2(
				lFascMod.getIdFascicoloSiep());

		// inserisco nuova posizione giuridica con
		// nuova data scadenza pena altra causa
		PosizioneGiuridicaModel lPosizioneGiuridicaModel = new PosizioneGiuridicaModel(
				lPos.getPosizioneGiuridica());

		lPosizioneGiuridicaModel.setDataInizio(DateUtils.getSysDate());
		lPosizioneGiuridicaModel.setDataFine(null);
		lPosizioneGiuridicaModel.setIdEventoRiferimento(null);

		lPosizioneGiuridicaModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosizioneGiuridicaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPosizioneGiuridicaModel.setDataInserimento(DateUtils.getSysDate());

		lPosizioneGiuridicaModel.setCodOperatoreAggiornamento("");
		lPosizioneGiuridicaModel.setDataAggiornamento(null);
		lPosizioneGiuridicaModel.setCodUfficioAggiornamento("");

		LuogoDetenzioneModel lLuogoDetenzioneModel = null;

		if (lPos.getLuogoDetenzione() != null) {
			lLuogoDetenzioneModel = new LuogoDetenzioneModel(lPos.getLuogoDetenzione());
		} else {
			lLuogoDetenzioneModel = new LuogoDetenzioneModel();
			// lLuogoDetenzioneModel.setIstDetIdIstitutoDetenzione("");
			// lLuogoDetenzioneModel.setAltroLuogo("");
			lLuogoDetenzioneModel.setDataInizioDetenzione(DateUtils.getSysDate()); // ?
			lLuogoDetenzioneModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			// lLuogoDetenzioneModel.setFasSiuIdFascicoloSius(null);
			lLuogoDetenzioneModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLuogoDetenzioneModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLuogoDetenzioneModel.setDataInserimento(DateUtils.getSysDate());
		}

		// ===================================
		// Aggiorno il record ALTRA_CAUSA
		// ===================================
		AltraCausaModel lAltraCausaModel = new AltraCausaModel(lPos.getAltraCausa());

		Date lDataFineAltro = getRequestDateParameter(ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO);

		lAltraCausaModel.setDataScadenza(lDataFineAltro);

		lPosCtrl.ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(lPosizioneGiuridicaModel,
				lFascMod, lLuogoDetenzioneModel, lAltraCausaModel);

		// aggiorno model da passare alla jsp
		lPos.getAltraCausa().setDataScadenza(lDataFineAltro);
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("avvocati", lAvvocati);

		// ****************************************Pena Residua*****************

		// String vedoDataIntermedia = "N";

		Date lDataInizio = getRequestDateParameter(ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO);
		// aggiungo un giorno alla data di fine altra pena per settare
		// correttamente la data inizio della pena per questa causa
		lDataInizio = DateUtils.moveDateTo(lDataInizio, GregorianCalendar.DATE, 1);

		PenaResiduaModel lPenaModel = new PenaResiduaModel();

		PenaResiduaModel lPenMod = new PenaResiduaModel();
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascMod.getIdFascicoloSiep());

		lPenaModel.setFlagPenaSospesa(null);

		// ========================================================================
		// Recupero i quantum di pena Validati che concorrono alla calcolo della
		// pena (n.b. non vengono recuperati i dati correnti da computare in
		// quanto non validati)
		// ========================================================================

		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),
				null);

		// ==========================================================================
		// NO ERGASTOLO: calcolo le date in funzione del quantum a sistema e della
		// data di arresto (data inizio)
		// ==========================================================================
		if (lPenMod.getFlagErgastolo() == null || lPenMod.getFlagErgastolo().equals("N")) {

			lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio, null, "all");
			lPenaModel.setDataFinePresunta(lPenaModel.getDataFine());
			// if (lPenaModel != null && (lPenaModel.getDataFineReclusione() != null
			// || lPenaModel.getDataInizioArresto() != null)) {
			// vedoDataIntermedia = "S";
			// }
		} else { // inizio ergastolo
			lPenaModel = new PenaResiduaModel(lPenMod);
			Date lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
			lPenaModel.setDataFine(lDataFinePenaErga);
		} // fine ergastolo

		lPenaModel.setDataInizio(lDataInizio);
		lPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lPenaModel.setFlagErgastolo(lPenMod.getFlagErgastolo());
		lPenaModel.setDiesAQuo("S");

		// Nel caso di ergastolo la pena viene validata
		if (lPenaModel.getFlagErgastolo() != null && !lPenaModel.getFlagErgastolo().equals("N"))
			lPenaModel.setFlagValidato("S");
		else
			lPenaModel.setFlagValidato("N");

		// ==========================================================================
		// Inserisco o aggiorno la pena residua con decorrenza e scadenza ricalcolate
		// ==========================================================================
		IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();

		if (lPenMod.getFlagValidato().equals("S")) {
			lPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenaModel.setDataInserimento(DateUtils.getSysDate());
			lPenaModel = lPPres.ExInserisciPenaResidua(lPenaModel);
		} else if (lPenMod.getFlagValidato().equals("N")) {
			lPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lPenaModel.setDataAggiornamento(DateUtils.getSysDate());
			lPenaModel.setIdPenaResidua(lPenMod.getIdPenaResidua());
			lPenaModel = lPPres.ExModificaPenaResidua(lPenaModel);
		}

		setRequestAttribute("penaresidua", lPenaModel);
		setRequestAttribute("ggdataperv", getRequestStringParameter(CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("mmdataperv", getRequestStringParameter(CAMPO_MESE_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("aadataperv", getRequestStringParameter(CAMPO_ANNO_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("motivazioni", getRequestStringParameter(CAMPO_MOTIVAZIONI_VARIAZIONE));

		return PG_LOAD_INSERISCI_VARIAZIONE_DECORRENZA_SCADENZA; // restituisce la jsp di VIEW
	}

}