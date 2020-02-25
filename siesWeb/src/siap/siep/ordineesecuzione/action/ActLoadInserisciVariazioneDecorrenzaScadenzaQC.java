package siap.siep.ordineesecuzione.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciVariazioneDecorrenzaScadenzaQC extends ActionSiap
		implements ICostantiOrdineEsecuzione {
	/**
	 * Action per correggere la data decorrenza pena. Prende in input la nuova decorrenza e ricalcola la pena.
	 * Mettendola in sessione (?)
	 * 
	 * Viene recuperata la nuova data inizio pena e rieffettuati i calcoli di decorrenza/scadenza.
	 * 
	 * n.b. NON VENGONO EFFETTUATE MODIFICHE AI DATI IN QUESTA FASE
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

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Eutorità per l'esecuzione se agli Arresti Domiciliari
		if (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("autoritaEsternaE", "" + lOption);
		}

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("avvocati", lAvvocati);

		// ==========================================================================
		//
		// ==========================================================================
		Date lNuovaDataInizio = getRequestDateParameter(ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO,
				ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO);

		// Date lDataRichiesta =
		// getRequestDateParameter(ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE,
		// ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE,
		// ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE);

		PenaResiduaModel lNuovaPenaModel = new PenaResiduaModel();

		// ==========================================================================
		// Recupero l'ultima pena per verificare se trattasi di ergastolo o meno
		// ed eventualmente per duplicarla modificando silo l'inizio pena
		// ==========================================================================
		PenaResiduaModel lUltimaPenMod = new PenaResiduaModel();
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lUltimaPenMod = IPenRes
				.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(lFascMod.getIdFascicoloSiep());

		lNuovaPenaModel.setFlagPenaSospesa(null);

		// ========================================================================
		// Recupero i quantum di pena Validati che concorrono al calcolo della
		// pena
		// ========================================================================
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),
				null);

		// ==========================================================================
		// NO ERGASTOLO: calcolo le date in funzione del quantum a sistema e della
		// data di arresto (data inizio)
		// ==========================================================================
		// String vedoDataIntermedia = "N";
		if (lUltimaPenMod.getFlagErgastolo() == null || lUltimaPenMod.getFlagErgastolo().equals("N")) {
			lNuovaPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lNuovaDataInizio, null, "all");
			lNuovaPenaModel.setDataFinePresunta(lNuovaPenaModel.getDataFine());
			if (lNuovaPenaModel != null && (lNuovaPenaModel.getDataFineReclusione() != null
					|| lNuovaPenaModel.getDataInizioArresto() != null)) {
				// vedoDataIntermedia = "S";
			}
		} else // inizio ergastolo
		{
			lNuovaPenaModel = new PenaResiduaModel(lUltimaPenMod);
			Date lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
			lNuovaPenaModel.setDataFine(lDataFinePenaErga);
		} // fine ergastolo

		lNuovaPenaModel.setDataInizio(lNuovaDataInizio);
		lNuovaPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lNuovaPenaModel.setFlagErgastolo(lUltimaPenMod.getFlagErgastolo());
		lNuovaPenaModel.setDiesAQuo("S");

		setRequestAttribute("penaresidua", lNuovaPenaModel);

		//
		setRequestAttribute("ggdataperv", getRequestStringParameter(CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("mmdataperv", getRequestStringParameter(CAMPO_MESE_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("aadataperv", getRequestStringParameter(CAMPO_ANNO_PERVENIMENTO_VARIAZIONE));
		setRequestAttribute("motivazioni", getRequestStringParameter(CAMPO_MOTIVAZIONI_VARIAZIONE));

		return PG_LOAD_INSERISCI_VARIAZIONE_DECORRENZA_SCADENZA_QC; // restituisce la jsp di VIEW
	}

}