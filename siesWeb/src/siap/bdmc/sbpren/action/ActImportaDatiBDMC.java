package siap.bdmc.sbpren.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbpren.controller.IImportaDati;
import siap.bdmc.sbpren.model.EsitoImportModel;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/*********************************************************
 * <p>
 * Title: ActImportaDatiBDMC
 * </p>
 * <p>
 * Description: Importa i dati in SIEP selezionati da BDMC
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 ********************************************************/
public class ActImportaDatiBDMC extends ActionSiap implements ICostantiSbPren {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		ProvvedimentoModelBDMC lProvvedimento = this.getProvvedimentoBDMCInSession();
		lProvvedimento.setUtente(getUtenteConnesso().getUserId());
		lProvvedimento.setUfficio(getUtenteConnesso().getUfficioUtente().getCodUfficio());
		lProvvedimento.setDataInserimento(DateUtils.getSysDate());

		lProvvedimento.setFascicoloSiep(setFascicoloSiepModel());

		if (!(lProvvedimento.getIdSoggettoOmonimo() != null && lProvvedimento.getSoggetto() != null))
			this.setSoggettoPerImport(lProvvedimento);

		EsitoImportModel lEsito = new EsitoImportModel();
		lEsito.setProvvedimento(lProvvedimento);
		// Importa i dati in SIep
		IImportaDati lCtrl = BDMCLookupRemote.getImportaDati();
		BigDecimal lKey = null;
		if (this.getSessionAttribute("esitoSoggetto") != null
				&& this.getSessionAttribute("esitoSoggetto").toString().compareTo("0") == 0)
			lKey = lCtrl.ExImportaProvvedimentoBDMC(lEsito);

		else {
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. "
					+ "Il fascicolo in esame è stato già creato");
		}

		// return PG_ESITO_IMPORT;

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info("Chiave Fascicolo = " + lKey);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info("lCtrl.getEsito() = " + lCtrl.getEsito());

		if (lKey == null) {
			lKey = new BigDecimal(0);
		} else {
			this.setSessionAttribute("esitoSoggetto", lCtrl.getEsito().getEsitoSoggetto());

		}

		this.setRequestAttribute("esito", lCtrl.getEsito());
		this.setRequestAttribute("KeyFascicolo", lKey);

		String lPage = PG_ESITO_IMPORT;

		/*
		 * String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		 * "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" +
		 * ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lKey;
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("lPage = " + lPage);
		return lPage;
	}

	/******************************
	 * setta il FascicoloSiepModel
	 * 
	 * @return
	 * @throws F3BException
	 *****************************/
	protected FascicoloSiepModel setFascicoloSiepModel() throws F3BException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		lFascicolo.setChiaveUfficio(this.getUtenteConnesso().getUfficioUtente().getCodUfficio()); // Ufficio
																									// dell'operatore
																									// che
																									// inserisce

		lFascicolo.setCodStatoFascicolo("02"); // Stato fascicolo settato ad aperto
		lFascicolo.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFascicolo.setCodTipoPosLibero("-"); // Motivo di archiviazione '-' per le join

		lFascicolo.setDataIscrizione(getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_ATTI,
				CAMPO_MESE_ISCRIZIONE_ATTI, CAMPO_GIORNO_ISCRIZIONE_ATTI));
		lFascicolo.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));

		lFascicolo.setNote(getRequestStringParameter(CAMPO_NOTE));
		lFascicolo.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
		lFascicolo.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione
											// giuridica

		lFascicolo.setCodOperatoreInserimento(getUtenteConnesso().getUserId());
		lFascicolo.setDataInserimento(DateUtils.getSysDate());
		lFascicolo.setCodUfficioInserimento(getUtenteConnesso().getUfficioUtente().getCodUfficio());

		lFascicolo.setTipoProgressivo(getRequestIntParameter("tipo"));

		return lFascicolo;
	}

	/**
	 * Imposta il soggetto nel modellone settandolo con i dati passati da BDMC
	 * 
	 * @param ProvvedimentoModelBDMC
	 */
	private void setSoggettoPerImport(ProvvedimentoModelBDMC lProvvedimento) throws F3BException {
		SbPrenModel aSbPren = lProvvedimento.getSbPren();
		// leggo il Soggetto dal modello SbPren e lo setto in quello SIES
		SoggettoModel lSogg = new SoggettoModel();
		lSogg.setCodAfis(aSbPren.getCodiIdenAfis());
		lSogg.setCognome(aSbPren.getCognSogg());
		lSogg.setNome(aSbPren.getNomeSogg());
		lSogg.setDataNascitaPresunta("N");
		// lSogg.setAnnoNascita(new BigDecimal(this.getAnnoNascita()));
		lSogg.setDataNascita(aSbPren.getDataNasc());
		// ??lSogg.setCodComuneNascita(aSbPren.getLuogNasc());
		lSogg.setDescrComuneNascita(aSbPren.getLuogNasc());
		// lSogg.setCodProvinciaNascita(??);
		// lSogg.setDescrProvinciaNascita(??);
		lSogg.setCodStatoNascita(aSbPren.getCodiStat());
		lSogg.setDescrStatoNascita(aSbPren.getDescriStat());
		// lSogg.setDescComuneNascitaEstero(??);
		// lSogg.setNazionalita(??);
		// lSogg.setPaternita(this.getPaternita());
		// lSogg.setCognomeMadre(this.getCognomeMadre());
		// lSogg.setNomeMadre(this.getNomeMadre());
		lSogg.setSesso(aSbPren.getFlagSess());
		// lSogg.setAttoNascita(this.getAttoNascita());
		// lSogg.setNote(this.getNote());
		// lSogg.setCodOperatoreInserimento(this.getCodOperatoreInserimento());
		// lSogg.setDataInserimento(this.getDataInserimento());
		// lSogg.setCodUfficioInserimento(this.getCodUfficioInserimento());
		// lSogg.setDescrUfficioInserimento(this.getDescrUfficioInserimento());
		// lSogg.setCodOperatoreAggiornamento(this.getCodOperatoreAggiornamento());
		// lSogg.setDataAggiornamento(this.getDataAggiornamento());
		// lSogg.setCodUfficioAggiornamento(this.getCodUfficioAggiornamento());
		// lSogg.setDescrUfficioAggiornamento(this.getDescrUfficioAggiornamento());
		// lSogg.setCodComuneCasellario(this.getCodComuneCasellario());

		/*
		 * if (lSogg.getCodStatoNascita() != null) { if (lSogg.getCodStatoNascita().equals("039")) { //Setta
		 * la sede giudiziaria del soggetto ComuneModel lComMod = new
		 * ComuneModel(getCodComuneByDescr(lSogg.getDescrComuneNascita()));
		 * lSogg.setCodProvinciaNascita(lComMod.getCodProvincia());
		 * lSogg.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria()); lSogg.setNazionalita("I");
		 * lSogg.setCodComuneNascita(lComMod.getCodComune()); } else { lSogg.setCodComuneCasellario("342");
		 * lSogg.setNazionalita("E"); } }
		 */
		if (lSogg.getDescrComuneNascita() != null) {
			// Setta la sede giudiziaria del soggetto
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSogg.getDescrComuneNascita()));
			lSogg.setCodProvinciaNascita(lComMod.getCodProvincia());
			lSogg.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
			lSogg.setNazionalita("I");
			lSogg.setCodComuneNascita(lComMod.getCodComune());
		}

		lSogg.setCodOperatoreInserimento(getUtenteConnesso().getUserId());
		lSogg.setDataInserimento(DateUtils.getSysDate());
		lSogg.setCodUfficioInserimento(getUtenteConnesso().getUfficioUtente().getCodUfficio());
		lProvvedimento.setSoggetto(lSogg);
	}

	/**
	 * Restuituisce il ProvvedimentoBDMC in sessione
	 * 
	 * @return il provvedimento BDMC in sessione
	 * @throws F3BException
	 *             - se il Provvedimento non è in sessione
	 */
	protected ProvvedimentoModelBDMC getProvvedimentoBDMCInSession() throws F3BException {
		ProvvedimentoModelBDMC lProvv = (ProvvedimentoModelBDMC) getSession().getAttribute(
				"provvedimentoBDMC");
		if (lProvv == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. "
					+ "Non è stato selezionato nessun Provvedimento BDMC");

		return lProvv;
	}

	/**
	 * Verifica che il provvedimento BDMC sia in sessione
	 * 
	 * @throws F3BException
	 *             - se il Provvedimento non è in sessione
	 */
	protected void isProvvedimentoBDMCInSession() throws F3BException {
		ProvvedimentoModelBDMC lProvv = (ProvvedimentoModelBDMC) getSession().getAttribute(
				"provvedimentoBDMC");
		if (lProvv == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. "
					+ "Non è stato selezionato nessun Provvedimento BDMC");
	}

}