package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadCalcoloPena</p>
 * <p>Description: Azione Load del Calcolo Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadCalcoloPena extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Action invocata per il primo calcolo della pena, o direttamente dal Menu Assegnazioni/Calcolo Pena, o,
	 * tramite l'operazione di Redirect, dalle action che richiedono il preventivo calcolo pena. Questa Action
	 * effettua i controlli preliminari e cerca di recuparare la DataInizio, quindi invoca la
	 * siap.siep.calcolopena.action.ActCalcoloPena passandogli sulla request la data inizio pena. La data
	 * inizio = data inizio misure cautelari ancora in espiazione La data inizio = data fine pena + 1g se
	 * detenuto altra causa
	 * 
	 * @return ritorna la chiamata alla Main.jsp con impostata l'opportuna Action e relativi parametri
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		// throw new SIEPException( SIEPException.USER_MESSAGE, "Selezionare il procedimento." );

		this.isFascicoloSiepDiCompetenza();

		Date datainizio = null;

		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		// Recupero le posizioni giuridiche associate al fascicolo ordinate per
		// data inserimento (meno recente più recente)
		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
		List lList = lPG.ExRicercaPosizioneGiuridicaByIdFascicoloNoError(lFascID);

		boolean detenutoQuestaCausa = false;
		boolean libero = false;
		String lPage = new String("");

		if (lList.size() == 0) { // Manca la posizione giuridica
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserire Posizione Giuridica !");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// Verifico se detenuto per questa causa (01,02,03,04)
		// ==========================================================================
		// Recupero la posizione giuridica corrente (la prima della lista)
		// 04/04/2007 eliminato il ciclo for che non era significativo
		// ==========================================================================
		// for (int i=0;i<lList.size();i++)
		// {
		// PGMod=(PosizioneGiuridicaModel)lList.get(i);
		PGMod = (PosizioneGiuridicaModel) lList.get(lList.size() - 1);

		if (PGMod.getCodPosizioneGiuridica().equals("01") // Custodia Cautelare per Questa Causa in Regime di
															// Detenzione (PRIMA)
				|| PGMod.getCodPosizioneGiuridica().equals("02") // Custodia Cautelare per Questa Causa in
																	// Regime di Arresti Domiciliari (PRIMA)
				|| PGMod.getCodPosizioneGiuridica().equals("03") // Espiazione Pena in Regime Carcerario
				|| PGMod.getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari ex art. 656/10
				|| PGMod.getCodPosizioneGiuridica().equals("70") // Custodia Cautelare in Regime di Arresti
																	// Domiciliare ex art 89 dpr 309/90
				|| PGMod.getCodPosizioneGiuridica().equals("71") // Custodia Cautelare in Regime di Permanenza
																	// in Casa
				|| PGMod.getCodPosizioneGiuridica().equals("72") // Custodia Cautelare in Collocamento in
																	// Comunità
				|| PGMod.getCodPosizioneGiuridica().equals("73") // Custodia Cautelare in Misura di Sicurezza
																	// Applicata in via Provvisoria
		) {
			detenutoQuestaCausa = true;
		}
		// 07 = libero (prima), 10 = Libero
		else if (PGMod.getCodPosizioneGiuridica().equals("07")
				|| PGMod.getCodPosizioneGiuridica().equals("10")) {
			libero = true;
		}
		// }

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PosGiuCorr = " + PGMod.getCodPosizioneGiuridica());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("detenutoQuestaCausa = " + detenutoQuestaCausa);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("libero = " + libero);
		// ==========================================================================
		// Cerco di recuperare la data inizio da data inizio misure cautelari,
		// se detenuto per questa causa. Data fine pena + 1g, se detenuto altra
		// causa.
		// ==========================================================================
		if (detenutoQuestaCausa) {
			// Verifica se presenti misure cautelari
			MisuraCautelareModel lMisCau = new MisuraCautelareModel();
			IMisuraCautelare lMC = SIEPLookupRemote.getMisuraCautelareRemote();
			Vector lVect = lMC.ExRicercaMisureCautelariByIdFascicolo(lFascID);

			if (lVect.size() == 0) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);

				// inizio modifica A.S. settembre 2015 dopo il message "Impossibile effettuare il calcolo
				// della pena . Rivedere Misure Cautelari !"
				// non l'applicativo non deve andare in "Dettaglio Procedimento" ma in "Aggiornamento
				// Posizione Giuridica"
				// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Rivedere Misure Cautelari o Posizione
				// Giuridica !" );
				// lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				// +ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep()
				// );

				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Rivedere Misure Cautelari o Posizione Giuridica !");
				lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

				// fine modifica A.S. settembre 2015

				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}

			for (int i = 0; i < lVect.size(); i++) {
				lMisCau = (MisuraCautelareModel) lVect.get(i);
				if (lMisCau.getDataFine() == null
						&& lMisCau.getDataInizio() != null) { /*
																 * è ancora detenuto, utilizzo la data inizio
																 * delle misure cautelari come data di
																 * partenza per il calcolo della pena residua
																 */
					datainizio = lMisCau.getDataInizio();
					lPage = f3b.web.IWebConstants.PG_MAIN + "?" + f3b.web.IWebConstants.ACTION_FIELD
							+ "=siap.siep.calcolopena.action.ActCalcoloPena&GiornoInizio="
							+ DateUtils.getDayToString(datainizio) + "&MeseInizio="
							+ DateUtils.getMonthToString(datainizio) + "&AnnoInizio="
							+ DateUtils.getYearToString(datainizio);
				}
			}

			if (lPage.equals("")) { // Risulta detenuto per questa causa come posizione giuridica, ma
									// ma la data fine misura cautelare risulta valorizzata, quindi in teoria
									// non dovrebbe essere detenuto
									// throw new SIEPException(SIEPException.USER_MESSAGE,"Impossibile
									// effettuare il calcolo della pena . Rivedere Misure Cautelari !");
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);

				// inizio modifica A.S. settembre 2015 dopo il message "Impossibile effettuare il calcolo
				// della pena . Rivedere Misure Cautelari !"
				// l'applicativo non deve andare in "Dettaglio Procedimento" ma in "Aggiornamento Posizione
				// Giuridica"
				// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Impossibile effettuare il calcolo della
				// pena . Rivedere Misure Cautelari !" );
				// lRedirigi.setAction( "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				// +ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep()
				// );
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Impossibile effettuare il calcolo della pena . Rivedere Misure Cautelari !");
				lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
				// fine modifica A.S. settembre 2015

				// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
		} else { // non detenuto per questa causa, verifico se detenuto per altra causa e
					// in questo caso verifico se è possibile utilizzare la data scadenza
					// del periodo di detenzione per altra causa, come data inizio di espiazione
					// della pena corrente
			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			IFascicoloSiep lFS = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascMod = lFS.ExRicercaFascicoloByKey(lFascID);

			if (lFascMod.getFlagAltraCausa() != null && "S".equals(lFascMod.getFlagAltraCausa())) {
				AltraCausaModel lAcModel = new AltraCausaModel();
				IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascID);

				if (lAcModel != null && lAcModel.getDataDecorrenza() != null
						&& lAcModel.getDataScadenza() != null) {
					datainizio = DateUtils.getDayAfter(lAcModel.getDataScadenza());
					lPage = f3b.web.IWebConstants.PG_MAIN + "?" + f3b.web.IWebConstants.ACTION_FIELD
							+ "=siap.siep.calcolopena.action.ActCalcoloPena&GiornoInizio="
							+ DateUtils.getDayToString(datainizio) + "&MeseInizio="
							+ DateUtils.getMonthToString(datainizio) + "&AnnoInizio="
							+ DateUtils.getYearToString(datainizio);
				} else
					lPage = f3b.web.IWebConstants.PG_MAIN + "?" + f3b.web.IWebConstants.ACTION_FIELD
							+ "=siap.siep.calcolopena.action.ActCalcoloPena&GiornoInizio=-";

			} else {
				datainizio = null;
				lPage = f3b.web.IWebConstants.PG_MAIN + "?" + f3b.web.IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActCalcoloPena&GiornoInizio=-";
			}

		}

		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		return lPage;
	}
}