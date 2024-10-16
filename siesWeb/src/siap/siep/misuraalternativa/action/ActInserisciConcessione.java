package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penaresidua.util.PenaResiduaUtil;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * ActInserisciConcessione - Classe Action per l'inserimento di Concessione della Misura Alternativa
 *
 * @version 1.0
 */
public class ActInserisciConcessione extends ActConcessione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// questa classe java serve per l'inserimento della concessione delle seguenti misure alternative:
		// affidamento in prova
		// detenzione domiciliare
		// Semilibertà
		// L. 207
		// Detenzione Domiciare a termine
		// Esecuzione presso domicilio

		// Partendo dalla posizione giuridica Libero si inserisce la Misura alternativa se non caricata da
		// SIUS
		// e si effettua la richiesta del verbale di sottoposizione agli obblighi
		// Effettuando il verbale modifica la posizione giuridica cosi come sottoriportato
		// affidamento in prova 13 (aff. In prova)
		// detenzione domiciliare 12 (Det. Domiciliare)
		// Semilibertà 14 (Semilibertà)
		// L. 207 27 (sosp. Cond)
		// Detenzione Domiciare a termine 12 (Det. Domiciliare)
		// Esecuzione presso domicilio 50 (esec. Dom)
		// poi riapre nuovamente la concessione che modifica la data inizio della misura alternativa inserendo
		// la data di sottoposizione agli obblighi.

		// Partendo dalla posizione giuridica Detenuto si inserisce la Misura alternativa se non caricata da
		// SIUS
		// non richiede il verbale quindi non cicla una seconda volta ma cambia immediatamente la posizione
		// giuridica
		// e la data inizio misura
		// Affidamento in prova Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 13 (aff. In
		// prova)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 13 (aff. In prova)
		// Detenzione Domiciare Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 12 (Det.
		// Domiciliare)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 12 (Det. Domiciliare)
		// Semilibertà - Data Inizio Misura = Data Emissione PM pos. giu. 14 (Semilibertà)
		// L. 207 Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 27 (sosp. Cond)
		// Da Scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 27 (sosp. Cond)
		// Det. Dom. a termine Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 12 (Det.
		// Domiciliare)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 12 (Det. Domiciliare)
		// Esecu. presso domicilio Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 50 (esec. Dom)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 50 (esec. Dom)

		// Partendo dalla posizione giuridica Arresti Domiciliari si inserisce la Misura alternativa se non
		// caricata da SIUS
		// non richiede il verbale quindi non cicla una seconda volta ma cambia immediatamente la posizione
		// giuridica
		// e la data inizio misura
		// Affidamento in prova Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 13 (aff. In
		// prova)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 13 (aff. In prova)
		// Detenzione Domiciare Eseguito Data Inizio Misura = Data esecuzione pos. giu. 12 (Det. Domiciliare)
		// Da eseguire Data Inizio Misura = Data Emissione PM pos. giu. 12 (Det. Domiciliare)
		// Semilibertà - Data Inizio Misura = Data Emissione PM pos. giu. 14 (Semilibertà)
		// L. 207 Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 27 (sosp. Cond)
		// Da Scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 27 (sosp. Cond)
		// Det. Dom. a termine Eseguito Data Inizio Misura = Data esecuzione pos. giu. 12 (Det. Domiciliare)
		// Da eseguire Data Inizio Misura = Data Emissione PM pos. giu. 12 (Det. Domiciliare)
		// Esecu. presso domicilio Scarcerato Data Inizio Misura = Data Scarcerazione pos. giu. 50 (esec. Dom)
		// Da scarcerare Data Inizio Misura = Data Emissione PM pos. giu. 50 (esec. Dom)

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: inizio");

		// solo nel caso di annotazione affidamento in prova ossia sanzione sostitutiva
		String lFlagSan = null;
		if (!isRequestParameterNullObj("lFlagSanzione")
				&& getRequestStringParameter("lFlagSanzione").equals("S"))
			lFlagSan = "S";

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// MEV_2019-09-SIEP: aggiunta nuova gestione per modifica
		String tipoOperazione = null;
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");
		if ("MODIFICA".equals(tipoOperazione)) {
			BigDecimal idEventoOld = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			siesLogger
					.debug("Sono in modifica procedo alla cancellazione dell'evento con id = " + idEventoOld);
			IEvento ie = SICOLookupRemote.getEventoRemote();
			EventoModel lEveModRic = ie.ExRicercaEventoByKey(idEventoOld);
			IOrdineEsecuzione ioe = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			ioe.ExCancellaEventoConStoreProcedure(lEveModRic);
			siesLogger.debug("Evento cancellato proseguo con un nuovo inserimento");
		}

		// setto la natura della MA
		String tipoMisura = getRequestStringParameter("tipomisura");
		String lPage = null;

		String lPosizione = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		setRequestAttribute("posizionegiuridica", lPosizione);

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(lPosizione);

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

		// ricerco Pena residua
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		// ricerca posizione giuridica precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// cosa vuol dire lFlagAffi = "S";
		// si parte da una posizione giuridica di libero e si concede una misura alternativa
		// se la misura concessa corrisponde alla relativa misura alternativa vuol dire che stiamo ciclando
		// la seconda volta sulla concessione dopo aver effettuato il verbale
		String lFlagAffi = "N";
		String lPosAtt = null;
		if (tipoMisura.equals("AFFIDAMENTO"))
			lPosAtt = "13";
		else if (tipoMisura.equals("DETENZIONE"))
			lPosAtt = "12";
		else if (tipoMisura.equals("SEMILIBERTA"))
			lPosAtt = "14";
		else if (tipoMisura.equals("INDULTINO"))
			lPosAtt = "27";
		else if (tipoMisura.equals("ESP_PRESSO_DOM"))
			lPosAtt = "50";
		if (lPosPre != null && lPosPre.getCodPosizioneGiuridica() != null && lPosizione != null
				&& lPosPre.isLibero() && lPosizione.equals(lPosAtt)) {
			lFlagAffi = "S";
		}

		// 2024.10.15 DF - Attenzione. Nel giro nomale lFlagAffi = S vuol dire che ho un condannato
		// precedentemente libero poi passato in misura.
		// Questo si verifica se si effettua una concessione da libero e si registra il verbale
		// sottoscrizione. Il verbale modifica la PG in "misura" e dal dettaglio si ha il tasto per
		// l'emissione del provvedimento.
		// In questo caso in form viene precaricata l'unica ordinanza di concessione non modificabile.
		// La data inizio misura la ha registrata sempre il verbale. Per cui la MA ha la data inizio misura e
		// non va ricalcolata ovvero lFlagAffi = S.
		// Nel caso di ratifica invece se è stata eseguita la provvisoria da libero (caso non previsto dalla
		// concessione) il soggetto è passato in misura, ma la ratifica è un nuovo provvedimento della sorv
		// per cui sulla MA non è presente la data inizio che va ricalcolata. lFlagAffi = "S";
		// Nel codice successiovo si forza lFlagAffi = N se ratifica.
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		if (lMisAlModConcessa == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS
			// si accede qui quando la misura alternativa non è stata selezionata dalla lista
			// siamo in questa condizione quando si accede la prima volta alla concessione indipendentemente
			// dalla posizione giuridica
			// e dal fatto che si richieda o no il verbale e che quindi ci sia o meno una seconda volta
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
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lDepOrdMod.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA))
				lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0001");

			lMisMod = setMisuraAlternativa("03", "CO", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");

			// fine composizione delle tabelle per la misura alternativa simulata da SIEP
			// --------------------------------------------

			// 2024.10.15 DF
			// In caso di ratifica forzo il lFlagAffi a N per recuperare la data inizio misura
			if ("0723".equals(lMisMod.getCodTipoMisura()))
				lFlagAffi = "N";

			Date lDataInizio = SettaDataInizioMisura(lPosMod, lFlagAffi, lFlagSan, tipoMisura);

			if (lDataInizio != null)
				lMisMod.setDataInizioMisura(lDataInizio);

			lMisMod.setCodTipoUfficioScarcerazione("PROC");
			if (!isRequestParameterNullObj("tipo")
					&& getRequestStringParameter("tipo").equals("scarcerato")) {
				lMisMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lMisMod.setCodTipoUfficioScarcerazione("SORV");
			}
			if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA))
				lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			if (lPenaResMod != null && lPenaResMod.getDataFine() != null && !lPosMod.isLibero())
				lMisMod.setDataFineMisura(lPenaResMod.getDataFine());

			// se la data viaggia la prendo altrimenti no Paolo Cherubini 26/01/2001
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaResMod.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			if (!lPosMod.isLibero())
				lMisMod = SettaReclusioneArresto(lPenaResMod, lMisMod);

			// MEV_2019-09-SIEP: aggiunto metodo
			settaDatiOrdinanzaProvvisoria(lMisMod);

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = setNotificheMisuraAlternativa();

			// inserisco la misura
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);
			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot = SettaProvvedimento(lFlagSan, tipoMisura, lPosizione, lFlagAffi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lMisMod);
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			// notifiche
			// NotificaModel[] lNotificheMod = setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			PenaResiduaModel prm = new PenaResiduaModel();
			prm.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				prm.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot, prm,
					null, null);

			// pagina di ritorno
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioConcessione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			// la misura alternativa esiste (NEL CASO DEI MINORENNI GIA' ESISTE PERCHE' INSERITA DALLA
			// SORVEGLIANZA)
			// MEV_62 [EC] 15/05/2018 - INIZIO
			if (lMisAlModConcessa.getCodTipoUfficioScarcerazione() == null
					|| "".equals(lMisAlModConcessa.getCodTipoUfficioScarcerazione())) {
				lMisAlModConcessa.setCodTipoUfficioScarcerazione("PROC");
			}
			// MEV_62 [EC] 15/05/2018 - FINE
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot = SettaProvvedimento(lFlagSan, tipoMisura, lPosizione, lFlagAffi,
					lMisAlModConcessa.getCodTipoMisura(), lMisAlModConcessa);
			lEveNot.getEvento().setEveIdEvento(lIdOrdinanza);

			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotifiche);

			PenaResiduaModel prm = new PenaResiduaModel();
			prm.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				prm.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// paolo cherubini 24/11/2010 caso "misura da SIUS"
			// se esiste un fine pena quindi è un detenuto metto la fine misura
			if (lPenaResMod != null && lPenaResMod.getDataFine() != null && !lPosMod.isLibero()) {
				lMisAlModConcessa.setDataFineMisura(lPenaResMod.getDataFine());
				// fine paolo cherubini 24/11/2010
			}

			// setto la misura alternativa che deve essere modificata
			EventoNotificaModel lRetModel = new EventoNotificaModel();

			// 2024.10.15 DF
			// In caso di ratifica forzo il lFlagAffi a N per recuperare la data inizio misura
			if ("0723".equals(lMisAlModConcessa.getCodTipoMisura()))
				lFlagAffi = "N";

			Date lDataInizio = SettaDataInizioMisura(lPosMod, lFlagAffi, lFlagSan, tipoMisura);
			if (lDataInizio != null)
				lMisAlModConcessa.setDataInizioMisura(lDataInizio);

			if (!lPosMod.isLibero())
				lMisAlModConcessa = SettaReclusioneArresto(lPenaResMod, lMisAlModConcessa);

			lMisAlModConcessa.setCodTipoUfficioScarcerazione("PROC");
			if (!isRequestParameterNullObj("tipo")
					&& getRequestStringParameter("tipo").equals("scarcerato")) {
				lMisAlModConcessa.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lMisAlModConcessa.setCodTipoUfficioScarcerazione("SORV");
			}

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModConcessa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// MEV_2019-09-SIEP: aggiunta impostazione di variabile
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisAlModConcessa.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// MEV_2019-09-SIEP: aggiunto metodo
			settaDatiOrdinanzaProvvisoria(lMisAlModConcessa);

			// model di ritorno
			lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEveNot, prm, lMisAlModConcessa, null);

			// pagina di ritorno
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioConcessione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: fine");

		// pagina di ritorno
		return lPage;
	}

	// MEV_2019-09-SIEP: aggiunto metodo
	private void settaDatiOrdinanzaProvvisoria(MisuraAlternativaModel mam) throws F3BException {

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT))
			mam.setAnnoRegistroMaAt(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT));
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT))
			mam.setNumeroRegistroMaAt(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT));
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT))
			mam.setDataDecisioneMaAt(
					getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT));
	}

	/**
	 * SettaReclusioneArresto
	 *
	 * @param aPenaResMod
	 * @param aMisMod
	 * @return
	 * @throws Exception
	 */
	public MisuraAlternativaModel SettaReclusioneArresto(PenaResiduaModel aPenaResMod,
			MisuraAlternativaModel aMisMod) throws Exception {

		if (aPenaResMod != null && aPenaResMod.getDataFine() != null) {
			// aggiunga questa if Paolo Cherubini 26/01/2011 - Corretta il 21/11/2011
			PenaResiduaModel lPenaModel = null;
			lPenaModel = PenaResiduaUtil.calcolaPenaNuovaDataInizio(aMisMod.getDataInizioMisura(),
					aPenaResMod, false);
			if (lPenaModel != null) {
				if (lPenaModel.getNumAnniArresto() != null && lPenaModel.getNumAnniArresto().intValue() > 99)
					aMisMod.setNumAnniRevocaArresto(new BigDecimal("99"));
				else
					aMisMod.setNumAnniRevocaArresto(lPenaModel.getNumAnniArresto());
				aMisMod.setNumGiorniRevocaArresto(lPenaModel.getNumGiorniArresto());
				aMisMod.setNumMesiRevocaArresto(lPenaModel.getNumMesiArresto());

				if (lPenaModel.getNumAnniReclusione() != null
						&& lPenaModel.getNumAnniReclusione().intValue() > 99)
					aMisMod.setNumAnniRevocaReclusione(new BigDecimal("99"));
				else
					aMisMod.setNumAnniRevocaReclusione(lPenaModel.getNumAnniReclusione());
				aMisMod.setNumGiorniRevocaReclusione(lPenaModel.getNumGiorniReclusione());
				aMisMod.setNumMesiRevocaReclusione(lPenaModel.getNumMesiReclusione());
			}
		}

		return aMisMod;
	}

	/**
	 * SettaProvvedimento
	 *
	 * @param aFlagSan
	 * @param atipoMisura
	 * @param aPosizione
	 * @param aFlagAffi
	 * @param aCodice
	 * @param aMisMod
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel SettaProvvedimento(String aFlagSan, String atipoMisura, String aPosizione,
			String aFlagAffi, String aCodice, MisuraAlternativaModel aMisMod) throws F3BException {

		EventoNotificaModel aEveNot = new EventoNotificaModel();
		if (atipoMisura.equals("AFFIDAMENTO")) {
			aEveNot = getProvvedimentoMotivoAffidamento(aFlagSan, aPosizione, aFlagAffi,
					aMisMod.getCodTipoUfficioScarcerazione(), aCodice,
					getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		} else if (atipoMisura.equals("DETENZIONE"))
			aEveNot = getProvvedimentoMotivoDetDom(aPosizione, aFlagAffi,
					aMisMod.getCodTipoUfficioScarcerazione(), aCodice);
		else if (atipoMisura.equals("SEMILIBERTA"))
			// MEV_2019-09-SIEP: cambiata firma al metodo aggiunto codMotivo
			aEveNot = getProvvedimentoMotivoSemiliberta(aPosizione, aFlagAffi, aCodice);
		else if (atipoMisura.equals("INDULTINO"))
			aEveNot = getProvvedimentoMotivoIndultino(aPosizione, aFlagAffi,
					aMisMod.getCodTipoUfficioScarcerazione(), aCodice);
		else if (atipoMisura.equals("ESP_PRESSO_DOM"))
			aEveNot = getProvvedimentoMotivoIndultino(aPosizione, aFlagAffi,
					aMisMod.getCodTipoUfficioScarcerazione(), aCodice);

		aEveNot.getEvento().setCodEsito("0112");
		aEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(aEveNot.getEvento()));

		return aEveNot;
	}

	/**
	 * SettaDataInizioMisura
	 *
	 * @param aPosMod
	 * @param aFlagAffi
	 * @param aFlagSan
	 * @param atipoMisura
	 * @return
	 * @throws F3BException
	 */
	public Date SettaDataInizioMisura(PosizioneGiuridicaModel aPosMod, String aFlagAffi, String aFlagSan,
			String atipoMisura) throws F3BException {

		Date aDataInizio = null;

		// Controlla che la posizione giuridica sia "29"
		// se il soggetto è in detenzione domiciliare provvisoria (29) e viene concessa la detenzione
		// mantengo la stessa data inizio misura della detenzione domiciliare provvisoria (precedentemente
		// caricata in maschera)
		if ((aPosMod.getCodPosizioneGiuridica().equals("29") && atipoMisura.equals("DETENZIONE"))
				// MEV_2019-09-SIEP: aggiunta or condition per gestione ordinanza applicazione provvisoria
				|| (aPosMod.isLibero() && atipoMisura.equals("AFFIDAMENTO"))) {
			if (!isRequestParameterNullEmptyObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA))
				aDataInizio = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA);
		}
		// Controlla che la posizione giuridica sia "13" o "54"
		// premessa: inizialmente affidamento in prova provvisorio è stato registrato come posizione giuridica
		// 13 per distinguerla quindi
		// dalla concessione che è sempre 13 controllo che esista l'evento di ammissione provvisoria
		// se il soggetto è in affidamento in prova provvisorio (54 o 13 + evento ammissione) e viene concessa
		// affidamento in prova
		// mantengo la stessa data inizio misura affidamento in prova provvisorio (precedentemente caricata in
		// maschera)
		else if (atipoMisura.equals("AFFIDAMENTO")
				&& (!isRequestParameterNullEmptyObj(ICostantiEvento.CAMPO_ID_EVENTO)
						// MEV_2019-09-SIEP: aggiunta or condition per gestione ordinanza applicazione provvisoria
						|| !isRequestParameterNullEmptyObj(ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO))
				&& (aPosMod.getCodPosizioneGiuridica().equals("13") // Affidamento in prova
						|| aPosMod.getCodPosizioneGiuridica().equals("54") // Affidamento Provvisorio
				)) {
			if (!isRequestParameterNullEmptyObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA))
				aDataInizio = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA);
		}

		// occorre avvalorare la data inizio misura alternativa le regole sono specificate ampiamente ad
		// inizio pagina
		// comunque non si inserisce la data per libero poichè poi occorre fare il verbale e il riciclo II
		// volta sulla concessione
		// per la semilibertà la data inizio misura è sempre la data emissione PM (naturalmente se non è
		// libero)
		// quando il soggetto non è libero sicuramente lFlagAffi.equals("N")
		// lFlagAffi.equals("N") potrebbe indicare anche un soggetto gia in misura e viene concessa un'altra
		// misura
		else if (!(aPosMod.isLibero()) && aFlagAffi.equals("N")) { // && !atipoMisura.equals("SEMILIBERTA"))
			// se non è libero compare la possibilità di inserire la data di scarcerazione o data esecuzione
			// per i domiciliari
			// se si indica la data di scarcerazione si inserisce questa data come data inizio misura e come
			// ufficio "SORV" per sorveglianza
			if (!isRequestParameterNullEmptyObj("tipo")
					&& getRequestStringParameter("tipo").equals("scarcerato")) {
				// lTipoUffScar = "SORV";
				aDataInizio = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
			} else {
				// se non è libero compare la possibilità di inserire la data di scarcerazione o data
				// esecuzione per i domiciliari
				// se non si indica la data di scarcerazione si inserisce la data di emissione provvedimento
				// del PM come data inizio misura
				// e come ufficio "PROC" per procura
				// lTipoUffScar = "PROC";
				aDataInizio = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
			}
		}
		// valore di ritorno
		return aDataInizio;
	}

}