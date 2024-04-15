package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciMAAmmProvSemiliberta extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Perchè sono variabili di classe??????
	PenaResiduaModel cPenaResiduaModel = new PenaResiduaModel();

	public String processRequest() throws F3BException {

		String lPage = null;

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String tipoOperazione = null;
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");

		if ("MODIFICA".equals(tipoOperazione)) {
			BigDecimal idEventoOld = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			siesLogger
					.debug("Sono in modifica procedo alla cancellazione dell'evento con id = " + idEventoOld);
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEveModRic = lCtrlEvento.ExRicercaEventoByKey(idEventoOld);

			IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
			siesLogger.debug("Evento cancellato proseguo con un nuovo inserimento");
		}

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		cPenaResiduaModel = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		String tipoMisura = getRequestStringParameter("tipomisura");
		setRequestAttribute("tipoMisura", tipoMisura);

		// ==========================================================================
		// Se non esiste il verbale significa che stiamo emettendo il provvedimento
		// di esecuzione el decreato/ordinanza
		// ==========================================================================
		String flagverbale = "N";
		if (!this.isRequestParameterNullObj("flagverbale"))
			flagverbale = getRequestStringParameter("flagverbale");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);

		// nella variabile lUffScar imposto la modalità di esecuzione se esegue procura
		// o esegue sorveglianza
		String lUffScar = "PROC";
		if (!this.isRequestParameterNullObj(("tipo"))) {
			if (this.getRequestStringParameter("tipo").equals("mds"))
				lUffScar = "SORV";
			else if (this.getRequestStringParameter("tipo").equals("procura"))
				lUffScar = "PROC";
		} else {
			// Sto emettendo un provvedimento dopo la registrazione Inizio Misura
			// quindi esegue sempre Procura
		}

		// Posizione Corrente
		PosizioneGiuridicaModel lPosGiuMod = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosGiuMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// posizione Precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		siesLogger.debug("PosizioneGiuridicaModel: " + lPosPre);

		// Controllo se esiste la misura alternativa che potrebbe essere stata inserita da SIUS
		// (seleziona dalla lista), se non esiste la inserisco simulata da SIEP.
		// La misura alternativa esiste anche quando accedo la seconda volta dopo il verbale
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModAMM = null;
		BigDecimal lIdEventoSIUS = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdEventoSIUS != null)
			lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoSIUS);

		// ==========================================================================
		// Se non esiste la misura alternativa la inserisco simulata da SIEP
		// n.b. in questo caso sto sicuramente inserendo il primo provvedimento
		// SIEP e non provengo dal verbale sottoscrizione
		// ==========================================================================
		if (lMisAlModAMM == null) {
			siesLogger.debug("Primo giro: lMisAlModAMM is null");
			// INSERISCO EVENTO E NOTIFICA(?) SIUS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));

			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));

			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

			String lTipoProvvedimento = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			// Popola l'EVENTO SIUS (decreto / ordinanza)
			lEveMod.setEvento(super.setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(),
					lTipoProvvedimento, lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			DepositoDecretoModel lDepDecMod = null;
			DepositoOrdinanzaPcModel lDepOrdMod = null;
			if ("02".equals(lTipoProvvedimento))
				lDepDecMod = super.setDepositoDecreto(lCodiceUffEmi);
			else if ("03".equals(lTipoProvvedimento))
				lDepOrdMod = super.setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0270"); // 0001 - Concede - 0270 = Applica
																		// Provvisoriamente

			lMisMod = setMisuraAlternativa(lTipoProvvedimento, "CO", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUffScar);

			// prendo i dati dalla maschera poichè non esiste la misura alternativa sto accedendo sicuramente
			// la prima volta
			if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA))
				lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// ========================================================================
			// prendo la data scarcerazione o inizio misura dalla maschera se è stata digitata
			// ========================================================================
			if (!this.isRequestParameterNullEmptyObj(
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)) {
				lMisMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lMisMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
			}

			// Claudio AMBROSINO 1 : 12-2010 -
			// penaresidua-DataFine era gestita diversamente a secondo se è AFFIDAMENTO o DETENZIONE;
			// In caso di AmmProvv a Detenz Dom. non scriveva DataFineMisura.
			// se ho inserito la data inizio misura quindi esegue SORV, provengo da libero e calcolo la data
			// fine misura
			if (lMisMod.getDataInizioMisura() != null && lPosMod.isLibero() && lUffScar.equals("SORV")) {
				SettaDataFineMisuraSORV(lMisMod);
			}

			// se invece esegue la procura quindi non ho inserito la data inizio misura
			if (lUffScar.equals("PROC")) {
				Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

				if (!lPosGiuMod.isLibero()) {
					lMisMod.setDataInizioMisura(lDataEmissione);
					lMisMod.setDataFineMisura(cPenaResiduaModel.getDataFine());
				}
			}

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			// n.b sono le notifiche dell'evento SIEP ?????
			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			// ========================================================================
			// inserisco fisicamente sul DB la misura alternativa simulata da SIEP
			// ========================================================================
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciDecretoOrdinanzaMisAlt(lEveMod,
					lDepDecMod, lDepOrdMod, lTenMod, lMisMod);

			// =====================================================
			// Provvedimento SIEP
			// =====================================================
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			String lCodTipoProvvedimento = this.SettaCodTipoProvvedimento(PosizioneGiu, flagverbale, lMisMod,
					lPosMod);

			lEveNot.getEvento().setCodTipoProvvedimento(lCodTipoProvvedimento);
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

			if ("2007".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEveNot.getEvento().setCodMotivo("2007");
			else if ("0683".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEveNot.getEvento().setCodMotivo("1403");
			else if ("0694".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEveNot.getEvento().setCodMotivo("1414");

			lEveNot.setEvento(super.setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));

			// notifica
			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			// ========================================================================
			// inserimento provvedimento del PM e le relative notifiche
			// ========================================================================
			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
					cPenaResiduaModel, null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvSemiliberta&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		} // fine dell'inserimento della misura alternativa simulata da SIEP
		else {
			siesLogger.debug("Secondo giro");

			// passo qui per 2 ipotesi diverse
			// 1) la misura alternativa era stata già stata inserita da SIUS (seleziona dalla lista)
			// 2) la misura alternativa è gia presente perchè è la seconda volta che accedo
			// ossia la prima volta provenivo da libero ha eseguito la procura facendo la richiesta del
			// verbale, e adesso sto emettendo un provvedimento dopo la registrazione del verbale inizio
			// misura

			// Setto il provvedimento SIEP da Inserire: può essere il primo provvedimento
			// o la registrazione del verbale di sottomissione.
			// flagverbale = S = provengo dal verbale di sottomissione
			// flagverbale = N = sto inseremndo il primo provvedimento
			// In entrambi i casi ho già lo misura a sistema (lMisAlModAMM).

			// ======================================================================
			// Aggiorno i dati della MA.
			// Se primo provvedimento e seleziona dalla Lista allora devo comunque
			// aggiornare il flag CodTipoUfficioScarcerazione secondo quanto indicato
			// in maschera oltre all'eventuale data di inizio Misura se esegue SORV
			//
			// Se sto registrando il verbale di sottomissione agli obblighi devo aggiornare
			// comunque la data di inizio misura e fine misura
			// ======================================================================
			lMisAlModAMM.setCodUfficioAggiornamento(lCodiceUfficio);
			lMisAlModAMM.setCodOperatoreAggiornamento(lCodiceOperatore);
			lMisAlModAMM.setDataAggiornamento(DateUtils.getSysDate());

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE)) {
				lMisAlModAMM.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
			}

			if (flagverbale.equals("N")) {
				// Sto inserendo il provvedimento di esecuzione dell'ordinanza e quindi
				// non sto registrando la data inizio misura dal verbale.
				// Entro quì perchè ho selezionato l'ordinanza dalla lista

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
					lMisAlModAMM.setDescrLuogoProva(
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

				// prendo la data scarcerazione o inizio misura dalla maschera se è stata digitata
				if (!this.isRequestParameterNullObj(
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)) {
					lMisAlModAMM.setDataScarcerazione(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
					lMisAlModAMM.setDataInizioMisura(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				}

				if (!this.isRequestParameterNullObj(("tipo"))) {
					if (this.getRequestStringParameter("tipo").equals("mds"))
						lMisAlModAMM.setCodTipoUfficioScarcerazione("SORV");
					else if (this.getRequestStringParameter("tipo").equals("procura"))
						lMisAlModAMM.setCodTipoUfficioScarcerazione("PROC");
				}

				if (lUffScar.equals("SORV") && lMisAlModAMM.getDataInizioMisura() != null
						&& lPosMod.isLibero()) {
					SettaDataFineMisuraSORV(lMisAlModAMM);
				}

				if (lUffScar.equals("PROC")) {
					// se invece esegue la procura quindi non ho inserito la data inizio misura
					Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
					if (!lPosGiuMod.isLibero()) {
						lMisAlModAMM.setDataInizioMisura(lDataEmissione);
						lMisAlModAMM.setDataFineMisura(cPenaResiduaModel.getDataFine());
					}
				}
			}

			// ========================================================================
			// Preparo l'evento SIEP
			// ========================================================================
			EventoNotificaModel lEveNot = new EventoNotificaModel();

			String lCodTipoProvvedimento = this.SettaCodTipoProvvedimento(PosizioneGiu, flagverbale,
					lMisAlModAMM, lPosMod);

			lEveNot.getEvento().setCodTipoProvvedimento(lCodTipoProvvedimento);
			lEveNot.getEvento().setEveIdEvento(lIdEventoSIUS);

			if ("2007".equals(lMisAlModAMM.getCodTipoMisura()))
				lEveNot.getEvento().setCodMotivo("2007");
			else if ("0683".equals(lMisAlModAMM.getCodTipoMisura()))
				lEveNot.getEvento().setCodMotivo("1403");
			else if ("0694".equals(lMisAlModAMM.getCodTipoMisura()))
				lEveNot.getEvento().setCodMotivo("1414");

			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotifiche);

			// modifico l'ufficio di scarcerazione in base a quello digitato in maschera anche se la MA è
			// stata inserita da SIUS serve per i template e la posizione giuridica
			// FIXME Attenzione!!! A che serve questa istruzione?? La MA è già a sistema
			// ho già settato l'ufficio con quanto recuperato dalla maschera
			// Se sto registrando il verbale lUffScar = PROC ma lo era anche prima
			lMisAlModAMM.setCodTipoUfficioScarcerazione(lUffScar);

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEveNot,
					cPenaResiduaModel, lMisAlModAMM, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvSemiliberta&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}
		return lPage;
	}

	/**
	 * Restitiusce il tipo provvedimento da emettere in funzione 
	 * della posizione giuridica
	 *
	 * @param tipoMisura
	 * @param PosizioneGiu
	 * @param flagverbale
	 * @param aMisMod
	 * @param lPosMod
	 * @return
	 */
	public String SettaCodTipoProvvedimento(String PosizioneGiu, String flagverbale,
			MisuraAlternativaModel aMisMod, PosizioneGiuridicaModel aPosMod) {

		String aCodTipoProvedimento = "";

		if (flagverbale.equals("S")) {
			// in caso di provvedimento successivo al verbale si emette sempre una comunicazione
			aCodTipoProvedimento = "09"; // Comunicazione
		} else if ("PROC".equals(aMisMod.getCodTipoUfficioScarcerazione()) && aPosMod.isLibero()) {
			aCodTipoProvedimento = "06"; // Ordine esecuzione
		} else if ("PROC".equals(aMisMod.getCodTipoUfficioScarcerazione()) && PosizioneGiu.equals("03")) {
			aCodTipoProvedimento = "12"; // Comunicazione
		} else if ("PROC".equals(aMisMod.getCodTipoUfficioScarcerazione()) && (PosizioneGiu.equals("04") 
				|| PosizioneGiu.equals("12") || PosizioneGiu.equals("13") || PosizioneGiu.equals("14") 
				|| PosizioneGiu.equals("29") // Det Dom PROVV
				|| PosizioneGiu.equals("54") // Aff Prov PROVV
		)) { // FIXME da verificare
				// Esegue procura con soggetto in espiazione, emetto un Ordine di Scarcerazione
			aCodTipoProvedimento = "06"; // Ordine Esecuzione
		} else {
			// In tutti gli altri casi (Es esegue sorveglianza) emetto un provvedimento generico 04
			aCodTipoProvedimento = "04";
		}

		return aCodTipoProvedimento;
	}

	/**
	 * Nel caso di ESEGUE SORVEGLIANZA soggetto libero va data decorrenza anche alla pena residua e calcolata
	 * la data fine misura = data fine pena. Calcola decorrenza e scadenza della pena a partire dell'ultima
	 * pena a sistema (quantum) e dalla data inizio misura estratta dal MisuraAlternativaModel.
	 * 
	 * Setta il fine misura = fine pena calcolato.
	 *
	 * @param lMisMod
	 * @throws F3BException
	 */
	public void SettaDataFineMisuraSORV(MisuraAlternativaModel lMisMod) throws F3BException {

		try {
			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain
					.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);

			// Calcolo la nuova Pena Residua con decorrenza = data inizio misura
			PenaResiduaModel lPenaResiduaModel = lCalcoloPenaModel
					.getPenaDaEspiare(lMisMod.getDataInizioMisura(), null, "all");

			lPenaResiduaModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			lPenaResiduaModel.setDiesAQuo("S");
			lPenaResiduaModel.setFlagValidato("N");
			lPenaResiduaModel.setFlagErgastolo("N");

			lPenaResiduaModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenaResiduaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenaResiduaModel.setDataInserimento(DateUtils.getSysDate());

			cPenaResiduaModel = lPenaResiduaModel;

			// Imposto il fine misura = fine pena
			lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
		} catch (Exception e) {
			throw new F3BException(e);
		}

	}

}