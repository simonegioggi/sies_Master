package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciMAProsecuzione</p>
 * <p>Description: Classe Action per l'inserimento di </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * Questa Action viene invocata due volte. Una prima volta in fase di registrazione del provvedimento della
 * sorveglianza (decreto). Una seconda volta in fase di registrazione del provvedimento dell'esecuzione.
 *
 * @author d.fiorletta
 *
 */
public class ActInserisciMAProsecuzione extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Prosecuzione MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		/*
		 * Quest'azione viene chiamata due volte, la prima per inserire la misura e calcolare la penaresidua
		 * che metterà in sessione e la seconda volta per inserire il provvedimento e la pena che era in
		 * sessione!!
		 */

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String tipoMisura = getRequestStringParameter("tipomisura");
		String lAzione = null;

		if (tipoMisura.equals("DETENZIONE"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvDetDom";
		else if (tipoMisura.equals("AFFIDAMENTO"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvAffProva";
		else if (tipoMisura.equals("SEMILIBERTA"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvSemi";
		else if (tipoMisura.equals("DETENZIONECUMULO"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvDetDomCumulo";
		else if (tipoMisura.equals("AFFIDAMENTOCUMULO"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvAffProvaCumulo";
		else if (tipoMisura.equals("SEMILIBERTACUMULO"))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecProvvSemiCumulo";

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// String lCodiceOperatore = this.getCodUtenteConnesso();
		// String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenMod = new PenaResiduaModel();
		lPenMod = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaRes);

		PenaResiduaModel lNuovaPenaModel = new PenaResiduaModel(lPenMod);

		Date lDataInizioMisura = getRequestDateParameter(
				ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA);

		/***************************
		 * INIZIO CALCOLO DELLA PENA PER LA PROSECUZIONE
		 ***********************************/
		boolean isPenaRicalcolata = true;
		// se la penaresidua non è in sessione vuol dire che la devo ancora calcolare e quindi la calcolo
		if (this.isSessionAttributeNullObj("PROMApenaresidua")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena non in sessione la calcolo");

			isPenaRicalcolata = false;

//			String vedoDataIntermedia = "N";

			Date lDataFinePena = null;
			Date lDataInizioArresto = null;
			Date lDataFineReclusione = null;

			Date lDataInizio = lDataInizioMisura;

			if (lPenMod.getFlagErgastolo().equals("N")) {
				ICalcoloPena ICalPen = SIEPLookupRemote.getCalcoloPenaRemote();
				Vector lVectFine = ICalPen.exCalcolaDataFinePena(lDataInizio, lPenMod, true); // il flag true
																								// indica CON
																								// DIES_A_QUO

				if (lVectFine.size() == 0)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile calcolare la data di Fine Pena");

				if (lVectFine.size() == 1)
					lDataFinePena = (Date) lVectFine.get(0);

				if (lVectFine.size() == 2) {
					lDataFinePena = (Date) lVectFine.get(1);
					lDataFineReclusione = (Date) lVectFine.get(0);
					lDataInizioArresto = DateUtils.moveDateTo(lDataFineReclusione, Calendar.DAY_OF_MONTH, 1);
					lNuovaPenaModel.setDataInizioArresto(lDataInizioArresto);
					lNuovaPenaModel.setDataFineReclusione(lDataFineReclusione);
//					vedoDataIntermedia = "S";
				}

				// ==========================================================================
				// Anticipo il fine pena se presenti LA
				// n.b. vengono anticipati sia il fine reclusione che inizio arresto che
				// fine pena. Se fine reclusione<data inizio viene eliminato
				// ==========================================================================
				// ==========================================================================
				// Recupero i dati della pena
				// ==========================================================================
				CalcoloPenaModel lCalcoloPenaMod = null;
				ActCalcoloPenaMain lActCalcolaPenaMain = new ActCalcoloPenaMain();
				lCalcoloPenaMod = lActCalcolaPenaMain.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Recupero LA per anticipazione fine pena");
				// FIXME le LA vengono scaricate sulla Pena corrente e non su quella ricalcolata
				// (lNuovaPenaModel)
				// per cui non hanno effetto
				if (lCalcoloPenaMod.getLiberazioneAnticipata() > 0 && lDataFinePena != null) {
					lDataFinePena = DateUtils.moveDateTo(lDataFinePena, java.util.Calendar.DAY_OF_MONTH,
							-lCalcoloPenaMod.getLiberazioneAnticipata());

					// Arretro le data fine reclusione, inizio arresto
					// FIXME perchè su lPenMod??
					if (lPenMod.getDataFineReclusione() != null) {
						lPenMod.setDataFineReclusione(DateUtils.moveDateTo(lPenMod.getDataFineReclusione(),
								Calendar.DAY_OF_MONTH, -lCalcoloPenaMod.getLiberazioneAnticipata()));
					}

					if (lPenMod.getDataInizioArresto() != null) {
						lPenMod.setDataInizioArresto(DateUtils.moveDateTo(lPenMod.getDataInizioArresto(),
								Calendar.DAY_OF_MONTH, -lCalcoloPenaMod.getLiberazioneAnticipata()));
					}

					// Azzero le date se per effetto delle LA sono arretrate oltre la data inizio pena
					if (lPenMod.getDataFineReclusione() != null
							&& DateUtils.isGreater(lDataInizio, lPenMod.getDataFineReclusione())) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger
								.debug("Data fine reclusione < data inizio pena per effetto arretramento LA");
						lPenMod.setDataFineReclusione(null);
					}

					if (lPenMod.getDataInizioArresto() != null
							&& DateUtils.isGreater(lDataInizio, lPenMod.getDataInizioArresto())) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger
								.debug("Data inizio arresto < data inizio pena per effetto arretramento LA");
						lPenMod.setDataInizioArresto(null);
					}
				}

				lNuovaPenaModel.setDataFinePresunta(lDataFinePena);
			} else // inizio ergastolo
			{
				Date lDataFinePenaErga = new Date();
				lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
				lNuovaPenaModel.setDataFinePresunta(lDataFinePenaErga);
			} // fine ergastolo

			lNuovaPenaModel.setDataInizio(lDataInizio); // n.b. = data inizio misura
			lNuovaPenaModel.setDataFine(null); // = DataFinePresunta se l'utente non modificherà il fine pena
												// in form
			lNuovaPenaModel.setEveIdEvento(null);

			if (lPenMod.getFlagValidato().equals("S")) {
				lNuovaPenaModel.setFlagValidato("N");

				lNuovaPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lNuovaPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lNuovaPenaModel.setDataInserimento(DateUtils.getSysDate());

				lNuovaPenaModel.setCodUfficioAggiornamento(null);
				lNuovaPenaModel.setCodOperatoreAggiornamento(null);
				lNuovaPenaModel.setDataAggiornamento(null);
			}

			if (lPenMod.getFlagValidato().equals("N")) {
				lNuovaPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
				lNuovaPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
				lNuovaPenaModel.setDataAggiornamento(DateUtils.getSysDate());
			}

			this.setSessionAttribute("PROMApenaresidua", lNuovaPenaModel);
		}
		/***************************
		 * FINE CALCOLO DELLA PENA PER LA PROSECUZIONE
		 ***********************************/

		MisuraAlternativaModel lProsecuMod = null;
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIdOrdinanza = " + lIdOrdinanza);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lProsecuMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		String lPage = null;
		// ==========================================================================
		// Ordinanza non a sistema. Provengo
		// Perchè testa isPenaRicalcolata?
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lProsecuMod = " + lProsecuMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("isPenaRicalcolata = " + isPenaRicalcolata);
		if (lProsecuMod == null && !isPenaRicalcolata) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco MA");
			// ATTENZIONE!! LA MA viene inserita sempre in copia anche se selezionata dalla
			// lista

			// INSERISCO EVENTO DEL TDS
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

			String lTipoProvv = "02"; // Default il Decreto (vecchia gestione)
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE)) {
				lTipoProvv = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);
			}

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoProvv,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito decreto
			DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0068"); // Dispone la Prosecuzione Provvisoria
																		// e Trasmette gli Atti al Tds

			// misura alternativa
			String lNatura = null;
			if (tipoMisura.equals("DETENZIONECUMULO") || tipoMisura.equals("AFFIDAMENTOCUMULO")
					|| tipoMisura.equals("SEMILIBERTACUMULO"))
				lNatura = "PC";
			else
				lNatura = "PP";

			lMisMod = setMisuraAlternativa(lTipoProvv, lNatura, lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");
			lMisMod.setDataInizioMisura(lDataInizioMisura);

			MisuraAlternativaModel lMisuraModel = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod,
					lDepDecMod, lTenMod, lMisMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAzione + "&"
					+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
					+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
					+ lMisuraModel.getEveIdEvento();
		} else // la misura esiste
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Esiste lProsecuMod o isPenaRicalcolata");
			// ========================================================================
			// Ordinanza già a sistema (seleziona dalla lista) o già inserita allo
			// step precedente
			// ========================================================================
			// la misura esiste
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("presenzanuovopenaricalcolata =
			// "+getRequestStringParameter("presenzanuovopenaricalcolata"));
			if (this.isRequestParameterNullObj("presenzanuovopenaricalcolata")) { // presenzanuovopenaricalcolata
																					// è null (non presente in
																					// form) solo sulla
																					// registrazione
																					// dell'ordinanza
																					// Quindi entro qui se ho
																					// selezionato il decreto
																					// dalla lista. Aggiorno
																					// solo
																					// le note della MA

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Forse seleziono dalla lista: aggiorno solo le note MA");

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
					lProsecuMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

				lMisAltCtrl.ExModificaMisuraAlternativa(lProsecuMod);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAzione + "&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Carico i dati del provvedimento");
				// ======================================================================
				// La misura è presente e sto inserendo i dati del provvedimento SIEP.
				// Recupero i dati della sezione xxx
				// ======================================================================
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO)
						&& getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO) != null
						&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO)
								.equals("")) {
					BigDecimal lAnnoAltroTitolo = new BigDecimal(this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO));
					lProsecuMod.setAnnoAltroTitolo(lAnnoAltroTitolo);
				}

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ALTRO_TITOLO))
					lProsecuMod.setNumAltroTitolo(this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ALTRO_TITOLO));

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_AUTORITA_ALTRO_TITOLO))
					lProsecuMod.setCodAutoritaAltroTitolo(this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_COD_AUTORITA_ALTRO_TITOLO));

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO)) {
					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_ALTRO_TITOLO)));
					lProsecuMod.setCodLuogoAltroTitolo(lComMod.getCodComune());
				}

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO)
						|| !isRequestParameterNullObj(
								ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO)) {
					if (!(getRequestStringParameter(CAMPO_COD_AUTORITA_ALTRO_TITOLO).equals("-")
							&& getRequestStringParameter(CAMPO_COD_LUOGO_ALTRO_TITOLO).equals(""))) {
						// String lCodice = getCodUfficioByCodTipoUfficioDescrComune(
						// getRequestStringParameter(CAMPO_COD_AUTORITA_ALTRO_TITOLO),
						// getRequestStringParameter(CAMPO_COD_LUOGO_ALTRO_TITOLO));
					}
				}

				lProsecuMod.setDataAltroTitolo(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO));

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
					lProsecuMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

				String codiceMotivo = lProsecuMod.getCodTipoMisura();
				EventoNotificaModel lEve = new EventoNotificaModel();

				lEve.getEvento().setCodTipoProvvedimento("12");

				if (codiceMotivo.equals("2281"))
					lEve.getEvento().setCodMotivo("0374");
				else if (codiceMotivo.equals("2205"))
					lEve.getEvento().setCodMotivo("0375");
				else if (codiceMotivo.equals("2282"))
					lEve.getEvento().setCodMotivo("0376");
				else if (codiceMotivo.equals("2284"))
					lEve.getEvento().setCodMotivo("0377");
				else if (codiceMotivo.equals("2286"))
					lEve.getEvento().setCodMotivo("0378");
				else if (codiceMotivo.equals("2285"))
					lEve.getEvento().setCodMotivo("0379");
				else if (codiceMotivo.equals("2287"))
					lEve.getEvento().setCodMotivo("0380");
				else if (codiceMotivo.equals("2288"))
					lEve.getEvento().setCodMotivo("0381");
				else if (codiceMotivo.equals("2283"))
					lEve.getEvento().setCodMotivo("0382");
				else
					lEve.getEvento().setCodMotivo(codiceMotivo);

				lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
				lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
				lEve.getEvento().setEveIdEvento(lIdOrdinanza);

				// Inserisco l'array di Notifiche nell'Evento
				NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
				lEve.setNotifiche(lNotifiche);

				// inserisco la pena contestualmente al provvedimento. La pena è stata
				// calcolata e messa in sessione in fase di registrazione/aggancio
				// dell'ordinanza
				PenaResiduaModel lPenaRes = (PenaResiduaModel) this.getSessionAttribute("PROMApenaresidua");
				lPenaRes.setIdPenaResidua(null);

				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				// rimuovo la pena dalla sessione
				this.removeSessionAttribute("PROMApenaresidua");

				// ======================================================================
				//
				// ======================================================================
				lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, lProsecuMod, null);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

			}
		}
		return lPage;
	}

}