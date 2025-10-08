package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import siap.sius.tenore.model.TenoreModel;

/**
 * ActInserisciMAAmmProvvisoria - Classe Action per l'inserimento di MisuraAlternativa
 * Questa Action viene richiamata sia in fase di iscrizione del provvedimento di 'esecuzione'
 * dell'ordinanza/decreto SIUS, sia in fase di registrazione data inizio misura, quindi in fase di emissione
 * dell'OS
 *
 * @version 1.0
 */
/**
 * Azione di Inserimento del provvedimento di esecuzione dell'ammissione provvisoria all'affidamento in prova
 * o alladetenzione domiciliare. La Action viene chiamata sia in fase di inserimento della provvedimento della
 * Sorveglianza, sia dopo la registrazione del verbale di sottoposizione agli obblighi (Decisioni della
 * Sorveglianza --> Registrazione data inizio misura)
 *
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 *
 * @version 1.0
 */
public class ActInserisciMAAmmProvvisoria extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	PenaResiduaModel lPenaResiduaModel = new PenaResiduaModel();
	String tipoMisura = null;

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// MEV_9
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
		// MEV_9 - FINE

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResiduaModel = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		// setto la natura della MA che può essere AFFIDAMENTO o DETENZIONE (x
		// detenzione domiciliare)
		tipoMisura = getRequestStringParameter("tipomisura");
		setRequestAttribute("tipoMisura", tipoMisura);

		// se non esiste il verbale significa che stiamo accendo la prima volta se esiste il verbale significa
		// che sto accedendo la seconda volta
		// ossia la prima volta provenivo da libero ha eseguito la procura facendo la richiesta del verbale
		// quando è pervenuto il verbale dopo l'immissione a sistema del verbale entro la seconda volta
		String flagverbale = "N";
		if (!this.isRequestParameterNullObj("flagverbale"))
			flagverbale = getRequestStringParameter("flagverbale");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		EventoNotificaModel lRetModel = new EventoNotificaModel();
		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);
		String lPage = null;

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

		// AMBROSINO - Ammissione Provvisoria a detenzione Domiciliare -
		// SOGGETTO IN CUSTODIA CAUTELARE ARRESTI DOMICILIARI (02)-
		// SOGGETTO IN CUSTODIA CAUTELARE ARRESTI DOMICILIARI ex 656 (04)-
		// servono le 2 posizioni giuridiche
		PosizioneGiuridicaModel lPosGiuMod = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosGiuMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		// posizione Precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// Controllo se esiste la misura alternativa che potrebbe essere stata inserita da SIUS
		// (seleziona dalla lista), se non esiste la inserisco simulata da SIEP.
		// La misura alternativa esiste anche quando accedo la seconda volta dopo il verbale
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModAMM = null;
		BigDecimal lIdDecreto = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdDecreto != null && !lIdDecreto.toString().equals(""))
			lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdDecreto);

		// ==========================================================================
		// Se non esiste la misura alternativa la inserisco simulata da SIEP
		// n.b. in questo caso sto sicuramente inserendo il primo provvedimento
		// SIEP e non provengo dal verbale sottoscrizione
		// ==========================================================================
		if (lMisAlModAMM == null) {
			siesLogger.debug("Primo giro: lMisAlModAMM is null");
			// INSERISCO EVENTO E NOTIFICA DEL MDS
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

			// new! DL 146 L'AFFIDAMENTO può essere concesso anche con Ordinanza per cui
			// devo recuperare il tipo Provvedimento dalla FORM
			String lTipoProvvedimento = "";
			// MEV_9 anche per la DET DOM sui può emettere una ordinanza
			// if (tipoMisura.equals("AFFIDAMENTO")) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE)) {
				lTipoProvvedimento = getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);
			} else {
				lTipoProvvedimento = "02"; // ????
			}
			// } else {
			// lTipoProvvedimento = "02"; // Decreto
			// }

			// Popola l'EVENTO SIUS (decreto / ordinanza)
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(),
					lTipoProvvedimento, lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// DepositoDecretoModel lDepDecMod = null;
			// DepositoOrdinanzaPcModel lDepOrdMod = null;
			// if (lTipoProvvedimento.equals("02")){
			// // setto il deposito decreto
			// lDepDecMod = setDepositoDecreto(lCodiceUffEmi);
			// }
			// else if (lTipoProvvedimento.equals("03")){
			// lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			// }

			DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0001"); // 0001 - Concede

			Set<String> codiciAffidamentoSorvNew = new HashSet<>(
					Arrays.asList(new String[] { "0680", "0681", "0690", "0691", "0692" }));
			Set<String> codiciDetenzioneSorvNew = new HashSet<>(
					Arrays.asList(new String[] { "0682", "0693" }));

			// MEV_9-SIEP - Per l'ammissione provvisoria dei nuovi codici 678 va scritto l'esito anche
			// sull'evento e sul tenore l'esito e 0270 e non 0001 (vedi SIUS)
			if (codiciAffidamentoSorvNew.contains(lEveMod.getEvento().getCodMotivo())
					|| codiciDetenzioneSorvNew.contains(lEveMod.getEvento().getCodMotivo())) {
				lEveMod.getEvento().setCodEsito("0270"); // Applica ex art. 678 comma 1 ter cpp
				lTenMod.setCodEsitoTenore("0270");
			}
			// MEV_9-SIEP - FINE

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
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE)
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE).equals("")) {
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
				this.SettaDateMisuraPROC(lMisMod, lPosGiuMod, lPosPre, lDataEmissione);
			}

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			// ========================================================================
			// inserisco fisicamente sul DB la misura alternativa simulata da SIEP
			// ========================================================================
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciDecretoSospEventoNotifica(lEveMod,
					lDepDecMod, lTenMod, lMisMod);

			// =====================================================
			// Provvedimento SIEP
			// =====================================================
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			String lCodTipoProvvedimento = this.SettaCodTipoProvvedimento(tipoMisura, PosizioneGiu,
					flagverbale, lMisMod, lPosMod);

			lEveNot.getEvento().setCodTipoProvvedimento(lCodTipoProvvedimento);
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

			if (tipoMisura.equals("AFFIDAMENTO") && lCodTipoProvvedimento.equals("26")) {
				if ("2006".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5420");
				else if ("2008".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5421");
				// MEV_9 si rimappano i nuovi codici SIUS in caso di richiesta verbale sottoscrizione agli
				// obblighi
				else if ("0680".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5422");
				else if ("0681".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5423");
				else if ("0690".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5424");
				else if ("0691".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5425");
				else if ("0692".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("5426");
				// MEV_9 - FINE
				else
					lEveNot.getEvento()
							.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			} else {
				// MEV_9 Si rimappano tutti i codici sius dell'Applicazione Provvisoria su nuovi codici SIEP
				// per evere una descizione
				// più parlante anche se non trattasi di richiesta verbale
				if ("0680".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1400");
				else if ("0681".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1401");
				else if ("0690".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1410");
				else if ("0691".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1411");
				else if ("0692".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1412");
				else if ("0682".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1402");
				else if ("0693".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEveNot.getEvento().setCodMotivo("1413");
				else
					// MEV_9 - FINE
					lEveNot.getEvento()
							.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			}

			lEveNot.setEvento(super.setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));

			// notifica
			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			// if (1==1){
			// for (int i=0; i<lNotificheMod.length; i++){
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lNotMod ["+i+"] = "+lNotificheMod[i]);
			// }
			// }

			// ========================================================================
			// inserimento provvedimento del PM e le relative notifiche
			// ========================================================================
			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
					lPenaResiduaModel, null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		} // fine dell'inserimento della misura alternativa simulata da SIEP
		else {
			siesLogger.debug("Secondo giro");

			// passo qui per 2 ipotesi diverse
			// 1) la misura alternativa era stata già stata inserita da SIUS (seleziona dalla lista)
			// 2) la misura alternativa è gia presente perchè è la seconda volta che accedo
			// ossia la prima volta provenivo da libero ha eseguito la procura facendo la richiesta del
			// verbale,
			// e adesso sto emettendo un provvedimento dopo la registrazione del verbale inizio misura

			// Setto il provvedimento SIEP da Inserire: può essere il primo provvedimento
			// o la registrazione del verbale di sottomissione.
			// flagverbale = S = sto registrando il verbale di sottomissione
			// flagverbale = N sto inseremndo il primo provvedimento
			// In entrambi i casi ho già lo misura a sistema (lMisAlModAMM).
			// Se sto inserendo il provvedimento, la misura riporta come
			// che

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

			// MEV_9 - Non salvava/aggiornava i dati delle NOTE editabili in form
			siesLogger.debug("Carico le note");
			siesLogger.debug("Prima lMisAlModAMM.getNote() = " + lMisAlModAMM.getNote());
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE)) {
				siesLogger.debug("note da request = "
						+ getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
				lMisAlModAMM.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
				siesLogger.debug("Dopo lMisAlModAMM.getNote() = " + lMisAlModAMM.getNote());
			}

			if (flagverbale.equals("N")) {
				// Sto inserendo il provvedimento di esecuzione dell'ordinanza e quindi
				// non sto registrando la data inizio misura dal verbale.
				// Entro quì perchè ho selezionato l'ordinanza dalla lista

				// MEV_9: la modifica del luogo della prova è sempre consentita e non determina la
				// duplicazione del
				// provvedimento SIUS ma devo aggiornare il campo su MA
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
					lMisAlModAMM.setDescrLuogoProva(
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
				// MEV_9 - FINE

				// prendo la data scarcerazione o inizio misura dalla maschera se è stata digitata
				if (!this
						.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)
						&& !this.isRequestParameterNullObj(
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE)
						&& !this.isRequestParameterNullObj(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE)
						&& !this.getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE).equals("")
						&& !this.getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE).equals("")
						&& !this.getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE).equals("")) {
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

				// Claudio AMBROSINO 1Bis : 12-2010 -
				// penaresidua-DataFine era gestita diversamente a secondo se è AFFIDAMENTO o DETENZIONE;
				// In caso di AmmProvv a Detenz Dom. non scriveva DataFineMisura.
				if (lMisAlModAMM.getDataInizioMisura() != null && lPosMod.isLibero()
						&& lUffScar.equals("SORV")) {
					SettaDataFineMisuraSORV(lMisAlModAMM);
				}

				if (lUffScar.equals("PROC")) {
					// se invece esegue la procura quindi non ho inserito la data inizio misura
					Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
					SettaDateMisuraPROC(lMisAlModAMM, lPosGiuMod, lPosPre, lDataEmissione);
				}
			}

			// ========================================================================
			// Preparo l'evento SIEP
			// ========================================================================
			EventoNotificaModel lEve = new EventoNotificaModel();

			String lCodTipoProvvedimento = SettaCodTipoProvvedimento(tipoMisura, PosizioneGiu, flagverbale,
					lMisAlModAMM, lPosMod);

			lEve.getEvento().setCodTipoProvvedimento(lCodTipoProvvedimento);
			lEve.getEvento().setEveIdEvento(lIdDecreto);

			if (tipoMisura.equals("AFFIDAMENTO") && lCodTipoProvvedimento.equals("26")) {
				if ("2006".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5420");
				else if ("2008".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5421");
				// MEV_9 si rimappano i nuovi codici SIUS in caso di richiesta verbale sottoscrizione agli
				// obblighi
				else if ("0680".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5422");
				else if ("0681".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5423");
				else if ("0690".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5424");
				else if ("0691".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5425");
				else if ("0692".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
					lEve.getEvento().setCodMotivo("5426");
				// MEV_9 - FINE
				else
					lEve.getEvento().setCodMotivo(lMisAlModAMM.getCodTipoMisura());
			} else {
				// MEV_9 Si rimappano tutti i codici sius dell'Applicazione Provvisoria su nuovi codici SIEP
				// per evere una descizione
				// più parlante anche se non trattasi di richiesta verbale
				if ("0680".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1400");
				else if ("0681".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1401");
				else if ("0690".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1410");
				else if ("0691".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1411");
				else if ("0692".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1412");
				else if ("0682".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1402");
				else if ("0693".equals(lMisAlModAMM.getCodTipoMisura()))
					lEve.getEvento().setCodMotivo("1413");
				else
					// MEV_9 - FINE
					lEve.getEvento().setCodMotivo(lMisAlModAMM.getCodTipoMisura());
			}
			// lEve.getEvento().setCodMotivo(lMisAlModAMM.getCodTipoMisura());

			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);

			// modifico l'ufficio di scarcerazione in base a quello digitato in maschera anche se la MA è
			// stata inserita da SIUS
			// serve per i template e la posizione giuridica
			// FIXME Attenzione!!! A che serve questa istruzione?? La MA è già a sistema
			// ho già settato l'ufficio con quanto recuperato dalla maschera
			// Se sto registrando il verbale lUffScar = PROC ma lo era anche prima
			lMisAlModAMM.setCodTipoUfficioScarcerazione(lUffScar);

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaResiduaModel, lMisAlModAMM,
					null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}
		return lPage;
	}

	/**
	 * Restitiusce il tipo provvedimento da emettere in funzione del tipo di misura, AFFIDAMENTO o DETENZIONE,
	 * della posizione giuridica
	 *
	 * @param tipoMisura
	 * @param PosizioneGiu
	 * @param flagverbale
	 * @param aMisMod
	 * @param lPosMod
	 * @return
	 */
	public String SettaCodTipoProvvedimento(String tipoMisura, String PosizioneGiu, String flagverbale,
			MisuraAlternativaModel aMisMod, PosizioneGiuridicaModel aPosMod) {

		// inserisco il provvedimento del PM che per libero ed esegue la procura è una richiesta del verbale
		// negli altri casi è un ordine di esecuzione o scarcerazione
		String aCodTipoProvedimento = "";

		if (tipoMisura.equals("AFFIDAMENTO")) {
			// if ("2008".equals(aMisMod.getCodTipoMisura())){
			// // 03 Espiazione Pena in Regime Carcerario
			// // 04 Arresti Domiciliari ex art. 656/10
			// // 12 Espiazione Pena in Regime di Detenzione Domiciliare
			// // 13 Espiazione Pena in Regime di Affidamento in Prova
			// // 14 Espiazione Pena in Regime di Semiliberta'
			// // 29 Detenzione Domiciliare Provvisoria
			// // 51 Espiazione presso Domicilio (etc..)
			// // 54 Affidamento in Prova Provvisorio
			// if (PosizioneGiu != null && (PosizioneGiu.equals("03")||PosizioneGiu.equals("14")||
			// PosizioneGiu.equals("13")||
			// PosizioneGiu.equals("04")||PosizioneGiu.equals("12")|| PosizioneGiu.equals("54")|| // AMBROSINO
			// 01/2011 -chanege codice da 51 a 54
			// PosizioneGiu.equals("29")))
			// {
			// if ( ("PROC").equals(aMisMod.getCodTipoUfficioScarcerazione())) {
			// aCodTipoProvedimento = "09"; // Ordine Scarcerazione
			// }
			// else { // esegue MDS
			// aCodTipoProvedimento = "12"; // Comunicazione
			// }
			// }
			// else if( aPosMod.isLibero()
			// && ("PROC").equals(aMisMod.getCodTipoUfficioScarcerazione())
			// )
			// {
			// aCodTipoProvedimento = "26"; // d.f. 23/01/2014 L'OE diventa una Richiesta (di verbale
			// sottoposizione agli obblighi)
			// }
			// else
			// aCodTipoProvedimento = "04"; // Provvedimento Tutte le altre posizioni
			// }
			// else
			// {
			// 03 Espiazione Pena in Regime Carcerario
			// 04 Arresti Domiciliari ex art. 656/10
			// 12 Espiazione Pena in Regime di Detenzione Domiciliare
			// 13 Espiazione Pena in Regime di Affidamento in Prova
			// 14 Espiazione Pena in Regime di Semiliberta'
			// 29 Detenzione Domiciliare Provvisoria
			// 51 Espiazione presso Domicilio (etc..)
			// 54 Affidamento in Prova Provvisorio
			if (PosizioneGiu != null && (PosizioneGiu.equals("03") || PosizioneGiu.equals("14")
					|| PosizioneGiu.equals("13") || PosizioneGiu.equals("04") || PosizioneGiu.equals("12")
					|| PosizioneGiu.equals("54") || // AMBROSINO 01/2011 -chanege codice da 51 a 54
					PosizioneGiu.equals("29"))) {
				// MEV_2024-092: rework Potrei entrare con 13 se ho emesso una applicazione (non provvisoria)
				// if ("S".equals(flagverbale) && PosizioneGiu.equals("54")) {
				if ("S".equals(flagverbale) && (PosizioneGiu.equals("54") || PosizioneGiu.equals("13"))) {
					// Sto registrando il provvedimento successivo al verbale
					aCodTipoProvedimento = "12";
				} else if (("PROC").equals(aMisMod.getCodTipoUfficioScarcerazione())) {
					aCodTipoProvedimento = "09"; // Ordine Scarcerazione
				} else {
					aCodTipoProvedimento = "12"; // Comunicazione
				}
			} else if (aPosMod.isLibero() && ("PROC").equals(aMisMod.getCodTipoUfficioScarcerazione())) {
				// aCodTipoProvedimento = "06"; // Ordine Esecuzione
				aCodTipoProvedimento = "26"; // d.f. 23/01/2014 L'OE diventa una Richiesta (di verbale
												// sottoposizione agli obblighi)
			} else
				aCodTipoProvedimento = "04"; // Provvedimento Tutte le altre posizioni
			// }
		} else if (tipoMisura.equals("DETENZIONE")) {
			if ((PosizioneGiu != null && PosizioneGiu.equals("03")) || flagverbale.equals("S")) // espiazione
																								// pena in
																								// regime
																								// carcerario
			{
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione() != null
						&& aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					aCodTipoProvedimento = "09"; // Ordine Scarcerazione
				else
					aCodTipoProvedimento = "12"; // Comunicazione
			} else if (aPosMod != null && aPosMod.isLibero()
					&& (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione() != null
							&& aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))) {
				aCodTipoProvedimento = "06"; // Ordine Esecuzione
			} else
				aCodTipoProvedimento = "04"; // Provvedimento
		}
		return aCodTipoProvedimento;
	}

	/**
	 * No caso di ESEGUE SORVEGLIANZA Calcola decorrenza e scadenza della pena a partire dell'ultima pena a
	 * sistema (quantum) e dalla data inizio misura estratta dal MisuraAlternativaModel. Setta il fine misura
	 * = fine pena calcolato.
	 *
	 * @param lMisMod
	 * @throws F3BException
	 */
	public void SettaDataFineMisuraSORV(MisuraAlternativaModel lMisMod) throws F3BException {

		try {
			PenaResiduaModel lPenRes = calcolaPenaResidua(lMisMod.getDataInizioMisura());

			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			lPenRes.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			lPenRes.setDiesAQuo("S");
			lPenRes.setFlagValidato("N");
			lPenRes.setFlagErgastolo("N");

			lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			lPenaResiduaModel = lPenRes;

			lMisMod.setDataFineMisura(lPenRes.getDataFine());
		} catch (Exception e) {
			throw new F3BException(e);
		}

		/**
		 * ICalcoloPena lCtrPena = SIEPLookupRemote.getCalcoloPenaRemote(); Vector lDateFine =
		 * lCtrPena.exCalcolaDataFinePena(lMisMod.getDataInizioMisura(), lPenaResiduaModel, true);
		 *
		 * lPenaResiduaModel.setDataInizio(lMisMod.getDataInizioMisura());
		 *
		 * if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi // solo data fine
		 * lMisMod.setDataFineMisura((Date) lDateFine.get(0)); lPenaResiduaModel.setDataFine((Date)
		 * lDateFine.get(0)); lPenaResiduaModel.setDataFinePresunta((Date) lDateFine.get(0));
		 *
		 * } else if (lDateFine.size() == 2) { // Sono presenti sia Reclusione che // Arresti Date
		 * lDataFineReclusione = (Date) lDateFine.get(0); Date lDataInizioArresto =
		 * DateUtils.moveDateTo(lDataFineReclusione, Calendar.DAY_OF_MONTH, 1);
		 *
		 * lPenaResiduaModel.setDataInizioArresto(lDataInizioArresto);
		 * lPenaResiduaModel.setDataFineReclusione(lDataFineReclusione); lPenaResiduaModel.setDataFine((Date)
		 * lDateFine.get(1)); lPenaResiduaModel.setDataFinePresunta((Date) lDateFine.get(1));
		 *
		 * lMisMod.setDataFineMisura((Date) lDateFine.get(1)); }
		 */
	}

	/**
	 * Se esegue PROCURA, si determina l'eventuale inizio misura e fine misura.
	 *
	 * Il fine misura è sempre uguale al fine pena se disponibile. L'inizio misura dipende dalla misura
	 *
	 * AFFIDAMENTO: se non libero, inizio misura = data emissione provvedimento se libero non viene settato ne
	 * inizio ne fine misura DETENZIONE: ...
	 *
	 * @param lMisMod
	 * @param lPosGiuMod
	 *            - Posizione giuridica corrente
	 * @param lPosPre
	 *            - Posizione giuridica precedente
	 * @param lDataEmissione
	 */
	public void SettaDateMisuraPROC(MisuraAlternativaModel lMisMod, PosizioneGiuridicaModel lPosGiuMod,
			PosizioneGiuridicaModel lPosPre, Date lDataEmissione) {

		// x Detenzione Domiciliare
		// se è in custodia cautelare 02 data inizio misura = data inizio custodia cautelare
		// se è agli arresti domiciliari 04 e la precedende è custodia cautelare uguale a sopra 02 data inizio
		// misura = data inizio custodia cautelare
		// se è agli arresti domiciliari 04 e la precedente non è custodia cautelare 02 data inizio misura =
		// data inizio arresti domiciliari
		// se non è libero data inizio misura = data emissione provvedimento del PM
		// se è libero non inserisco la data ed il PM emette la richiesta del verbale
		// quando prendo la data inizio misura di conseguentemente setto anche la data fine misura
		// Claudio AMBROSINO 2 - Ammissione Provvisoria a detenzione Domiciliare -
		// SOGGETTO IN CUSTODIA CAUTELARE ARRESTI DOMICILIARI (02) -
		// SOGGETTO IN CUSTODIA CAUTELARE ARRESTI DOMICILIARI ex 656(04) -

		// x Affidamento in prova
		// se non è libero data inizio misura = data emissione provvedimento del PM

		if (tipoMisura.equals("DETENZIONE")) {
			if (lPosGiuMod.getCodPosizioneGiuridica().equals("02")) {
				lMisMod.setDataInizioMisura(lPosGiuMod.getDataInizio());
				lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
			}

			if (lPosGiuMod.getCodPosizioneGiuridica().equals("04")
					&& lPosPre.getCodPosizioneGiuridica().equals("02")) {
				lMisMod.setDataInizioMisura(lPosPre.getDataInizio());
				lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
			} else if (lPosGiuMod.getCodPosizioneGiuridica().equals("04")) { // se è solo 04
				lMisMod.setDataInizioMisura(lPosGiuMod.getDataInizio());
				lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
			} else if (!lPosGiuMod.isLibero()) { // se non è libero
				lMisMod.setDataInizioMisura(lDataEmissione);
				lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
			}
		}

		if (tipoMisura.equals("AFFIDAMENTO")) {
			if (!lPosGiuMod.isLibero()) { // se non è libero
				lMisMod.setDataInizioMisura(lDataEmissione);
				lMisMod.setDataFineMisura(lPenaResiduaModel.getDataFine());
			}
		}

	}

	/**
	 * Restituisce la pena residua da espiare. Se libero esegue SORV e quindi si ha la data inizio misura =
	 * inizio pena, è necessario calcolare decorrenza e scadenza in base ai quantum a sistema e alle eventuali
	 * LA.
	 *
	 * @return
	 */
	private PenaResiduaModel calcolaPenaResidua(Date aDataInizio) throws Exception {

		PenaResiduaModel lPenaResiduaModel = null;

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// CalcoloPenaModel lCalcoloPenaMod = null;

		// ========================================================================
		// Recupero i quantum di pena Validati che concorrono alla calcolo della
		// pena
		// ========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");

		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain
				.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);
		lPenaResiduaModel = lCalcoloPenaModel.getPenaDaEspiare(aDataInizio, null, "all");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena rideterminata = " + lPenaResiduaModel);

		return lPenaResiduaModel;
	}

}