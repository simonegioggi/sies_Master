package siap.regesies.regesentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IImportaDati;
import siap.regesies.regesentenza.model.EsitoImportModel;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/*******************************************************************************
 * <p>
 * Title: ActImportaDatiRege
 * </p>
 * <p>
 * Description: Importa i dati in SIEP selezionati da Rege
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 ******************************************************************************/
public class ActImportaDatiRege extends ActionRegeSiap implements ICostantiRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		ProvvedimentoModel lProvvedimento = getProvvedimentoRegeInSession();
		lProvvedimento.setUtente(getUtenteConnesso().getUserId());
		lProvvedimento.setUfficio(getUtenteConnesso().getUfficioUtente().getCodUfficio());
		lProvvedimento.setDataInserimento(DateUtils.getSysDate());

		lProvvedimento.setFascicoloSiep(setFascicoloSiepModel());

		if (lProvvedimento.getRegeSentenza() != null) {
			lProvvedimento.getRegeSentenza().setCodOperatoreInserimento(getUtenteConnesso().getUserId());
			lProvvedimento.getRegeSentenza().setCodUfficioInserimento(
					getUtenteConnesso().getUfficioUtente().getCodUfficio());
			lProvvedimento.getRegeSentenza().setDataInserimento(DateUtils.getSysDate());
		}

		if (!(lProvvedimento.getIdSoggettoOmonimo() != null && lProvvedimento.getSoggetto() != null))
			setSoggettoPerImport(lProvvedimento.getRegeSoggetto());

		EsitoImportModel lEsito = new EsitoImportModel();
		lEsito.setProvvedimento(lProvvedimento);

		// Importa i dati in SIep
		IImportaDati lCtrl = RegeSiesLookupRemote.getImportaDati();
		BigDecimal lKey = lCtrl.ExImportaProvvedimentoRege(lEsito);

		// return PG_ESITO_IMPORT;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Chiave Fascicolo = " + lKey);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("lCtrl.getEsito() = " + lCtrl.getEsito());

		if (lKey == null) {
			lKey = new BigDecimal(0);
		}

		setRequestAttribute("esito", lCtrl.getEsito());
		setRequestAttribute("KeyFascicolo", lKey);

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

	/***************************************************************************
	 * setta il FascicoloSiepModel
	 * 
	 * @return
	 * @throws F3BException
	 **************************************************************************/
	protected FascicoloSiepModel setFascicoloSiepModel() throws F3BException {

		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno
																				// corrente
		lFascicolo.setChiaveUfficio(getUtenteConnesso().getUfficioUtente().getCodUfficio()); // Ufficio
																								// dell'operatore
																								// che
																								// inserisce

		lFascicolo.setCodStatoFascicolo("02"); // Stato fascicolo settato ad
												// aperto
		lFascicolo.setCodMotivoArchiviazione("-"); // Motivo di archiviazione
													// '-' per le join
		lFascicolo.setCodTipoPosLibero("-"); // Motivo di archiviazione '-'
												// per le join

		lFascicolo.setDataIscrizione(getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_ATTI,
				CAMPO_MESE_ISCRIZIONE_ATTI, CAMPO_GIORNO_ISCRIZIONE_ATTI));
		lFascicolo.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));

		lFascicolo.setNote(getRequestStringParameter(CAMPO_NOTE));
		lFascicolo.setFlagValidato("N"); // Il flag di validazione viene
											// impostato a 'NO'
		lFascicolo.setFlagAltraCausa("N"); // Il flag altra causa viene gestito
											// nella gestione della posizione
											// giuridica

		lFascicolo.setCodOperatoreInserimento(getUtenteConnesso().getUserId());
		lFascicolo.setDataInserimento(DateUtils.getSysDate());
		lFascicolo.setCodUfficioInserimento(getUtenteConnesso().getUfficioUtente().getCodUfficio());

		lFascicolo.setTipoProgressivo(getRequestIntParameter("tipo"));

		return lFascicolo;
	}

	/**
	 * Imposta i parametri nel soggetto che in Rege non vengono valorizzati
	 * 
	 * @param aRegeSogg
	 */
	private void setSoggettoPerImport(RegeSoggettoModel aRegeSogg) throws F3BException {

		if (aRegeSogg.getCodStatoNascita().equals("039")) {
			// Setta la sede giudiziaria del soggetto che non è presente in rege
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(aRegeSogg.getDescrComuneNascita()));
			aRegeSogg.setCodProvinciaNascita(lComMod.getCodProvincia());
			aRegeSogg.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
			aRegeSogg.setNazionalita("I");
		} else {
			aRegeSogg.setCodComuneCasellario("342");
			aRegeSogg.setNazionalita("E");
		}
		aRegeSogg.setCodOperatoreInserimento(getUtenteConnesso().getUserId());
		aRegeSogg.setDataInserimento(DateUtils.getSysDate());
		aRegeSogg.setCodUfficioInserimento(getUtenteConnesso().getUfficioUtente().getCodUfficio());

	}

}