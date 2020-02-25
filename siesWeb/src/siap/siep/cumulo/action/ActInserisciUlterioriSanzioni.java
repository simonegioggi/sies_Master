package siap.siep.cumulo.action;

/**
 * <p>Title: ActInserisciUlterioriSanzioni</p>
 * <p>Description: Classe Action per l'inserimento dei record ULTERIORI_SANZIONI_CUMULO</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ulterioresanzionecumulo.action.ICostantiUlterioreSanzioneCumulo;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciUlterioriSanzioni extends ActionSiap implements ICostantiUlterioreSanzioneCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione per l'inserimento dei record ULTERIORI_SANZIONI_CUMULO. n.b. Anche se sono presenti più record
	 * CUMULO le sanzioni vengono inserite sul primo record inserito
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));

		// Ricerca cumulo per FasSieIdFascicoloSiep
		CumuloModel lCumMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		Vector cumuli = iCum
				.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());

		// Prendo il primo record CUMULO inserito. Le ulteriori snazioni vengono
		// collegate a tale record
		if (cumuli.size() > 0) {
			lCumMod = ((CumuloModel) (cumuli).get(0));
		}

		Vector lTable = new Vector();

		UlterioreSanzioneCumuloModel lCumModSemi = null;
		UlterioreSanzioneCumuloModel lCumModLib = null;
		UlterioreSanzioneCumuloModel lCumModEsp = null;
		UlterioreSanzioneCumuloModel lCumModSan = null;
		UlterioreSanzioneCumuloModel lCumModConv = null;
		UlterioreSanzioneCumuloModel lCumModMil = null;
		UlterioreSanzioneCumuloModel lCumModLavPub = null;
		UlterioreSanzioneCumuloModel lCumModLavSosp = null;
		UlterioreSanzioneCumuloModel lCumModSanzioni = null;

		// ==========================================================================
		// 01 - SANZIONE SOSTITUTIVA : SEMIDETENZIONE
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_SEMIDETENZIONE).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_SEMIDETENZIONE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_SEMIDETENZIONE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_SEMIDETENZIONE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_SEMIDETENZIONE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_SEMIDETENZIONE).equals("0")))
		// || (!this.getRequestStringParameter("Semidetenzione").equals("s") )
		) { // Dati inseriti o già presenti // CA Dati Injseriti o modificati
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SEMIDETENZIONE");

			lCumModSemi = new UlterioreSanzioneCumuloModel();

			lCumModSemi.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SEMIDETENZIONE));
			lCumModSemi.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_SEMIDETENZIONE));
			lCumModSemi.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_SEMIDETENZIONE));
			lCumModSemi.setCodTipoUlterioreSanzione("01");
			lCumModSemi.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModSemi.setDataInserimento(DateUtils.getSysDate());
			lCumModSemi.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModSemi);
		}
		/*
		 * else { // tutti i campi a null e record già presente (s), devo cancellarlo lCumModSemi = new
		 * UlterioreSanzioneCumuloModel();
		 * lCumModSemi.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDSemidetenzione"));
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("SEMIDETENZIONE222222222"); lTable.add(lCumModSemi); }
		 */
		// ==========================================================================
		// 02 - SANZIONE SOSTITUTIVA : LIBERTA' CONTROLLATA
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_LIBERTA).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_LIBERTA).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_LIBERTA).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_LIBERTA).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LIBERTA).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LIBERTA).equals("0")))
		// || !this.getRequestStringParameter("Liberta").equals("s")
		) {
			// CA Dati Injseriti o modificati
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Liberta' Controllata");
			lCumModLib = new UlterioreSanzioneCumuloModel();

			lCumModLib.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_LIBERTA));
			lCumModLib.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_LIBERTA));
			lCumModLib.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LIBERTA));
			lCumModLib.setCodTipoUlterioreSanzione("02");
			lCumModLib.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModLib.setDataInserimento(DateUtils.getSysDate());
			lCumModLib.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModLib);
		}
		/*
		 * else { lCumModLib = new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModLib.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDLiberta"));
		 * lTable.add(lCumModLib); }
		 */
		// ==========================================================================
		// 03 - SANZIONE SOSTITUTIVA : ESPULSIONE
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_ESPULSIONE).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_ESPULSIONE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_ESPULSIONE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_ESPULSIONE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_ESPULSIONE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_ESPULSIONE).equals("0")))
		// || !this.getRequestStringParameter("Espulsione").equals("s")
		) {
			// CA Dati Injseriti o modificati
			lCumModEsp = new UlterioreSanzioneCumuloModel();

			lCumModEsp.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ESPULSIONE));
			lCumModEsp.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_ESPULSIONE));
			lCumModEsp.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ESPULSIONE));
			lCumModEsp.setCodTipoUlterioreSanzione("03");
			lCumModEsp.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModEsp.setDataInserimento(DateUtils.getSysDate());
			lCumModEsp.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModEsp);
		}
		/*
		 * else { lCumModEsp = new UlterioreSanzioneCumuloModel();
		 * lCumModEsp.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDEspulsione"));
		 * lTable.add(lCumModEsp); }
		 */
		// ==========================================================================
		// 04 - SANZIONE SOSTITUTIVA : PENA PECUNIARIA
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE).equals("0")))
		// || !this.getRequestStringParameter("Pecuniaria").equals("s")
		) {
			lCumModSan = new UlterioreSanzioneCumuloModel();
			// lCumModSan.setSanzione(this.getRequestBigDecimalParameter(CAMPO_SANZIONE));

			if ((!getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE).equals(""))
					&& (!getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE).equals("0"))) {
				if ((!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals(""))
						&& (!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals("0"))) {
					lCumModSan.setSanzione(
							new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE)));
				} else {
					lCumModSan.setSanzione(
							new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_SANZIONE)));
				}
			} else if ((!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals(""))
					&& (!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE).equals("0"))) {
				lCumModSan.setSanzione(
						new BigDecimal("0." + getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_SANZIONE)));
			}

			lCumModSan.setCodTipoUlterioreSanzione("04");
			lCumModSan.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModSan.setDataInserimento(DateUtils.getSysDate());
			lCumModSan.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModSan);
		}
		/*
		 * else { lCumModSan =new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModSan.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDPecuniaria"));
		 * lTable.add(lCumModSan); }
		 */
		// ==========================================================================
		// 05 - PENA PECUNIARIA : LAVORO SOSTITUTIVO
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_SOST).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_SOST).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_SOST).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_SOST).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_SOST).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_SOST).equals("0")))
		// || !this.getRequestStringParameter("Sostitutivo").equals("s")
		) {
			lCumModConv = new UlterioreSanzioneCumuloModel();

			lCumModConv.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_LAV_SOST));
			lCumModConv.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_LAV_SOST));
			lCumModConv.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LAV_SOST));
			lCumModConv.setCodTipoUlterioreSanzione("05");
			lCumModConv.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModConv.setDataInserimento(DateUtils.getSysDate());
			lCumModConv.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModConv);
		}
		/*
		 * else { lCumModConv =new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModConv.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDSostitutivo"));
		 * lTable.add(lCumModConv); }
		 */
		// ==========================================================================
		// 06 - PENA MILITARE : RECLUSIONE
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_MILITARE).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_MILITARE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_MILITARE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_MILITARE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_MILITARE).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_MILITARE).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_MILITARE).equals("0")))
		// || !this.getRequestStringParameter("Reclusione").equals("s")
		) {
			lCumModMil = new UlterioreSanzioneCumuloModel();

			lCumModMil.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_MILITARE));
			lCumModMil.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_MILITARE));
			lCumModMil.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_MILITARE));

			if ((!getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA).equals(""))
					&& (!getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA).equals("0"))) {
				if ((!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA).equals(""))
						&& (!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA).equals("0"))) {
					lCumModMil
							.setSanzione(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)
									+ "." + getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)));
				} else {
					lCumModMil.setSanzione(
							new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)));
				}
			} else if ((!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA).equals(""))
					&& (!getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA).equals("0"))) {
				lCumModMil.setSanzione(
						new BigDecimal("0." + getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)));
			}

			lCumModMil.setCodTipoUlterioreSanzione("06");
			lCumModMil.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModMil.setDataInserimento(DateUtils.getSysDate());
			lCumModMil.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModMil);
		}
		/*
		 * else { lCumModMil =new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModMil.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDReclusione"));
		 * lTable.add(lCumModMil); }
		 */

		// ==========================================================================
		// 07 - GIUDICE DI PACE : PERMANENZA DOMICILIARE
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_SANZIONI).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_SANZIONI).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_SANZIONI).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_SANZIONI).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_SANZIONI).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_SANZIONI).equals("0")))
		// || !this.getRequestStringParameter("Domiciliare").equals("s")
		) {
			lCumModSanzioni = new UlterioreSanzioneCumuloModel();

			lCumModSanzioni.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SANZIONI));
			lCumModSanzioni.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_SANZIONI));
			lCumModSanzioni.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_SANZIONI));
			lCumModSanzioni.setCodTipoUlterioreSanzione("07");
			lCumModSanzioni.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModSanzioni.setDataInserimento(DateUtils.getSysDate());
			lCumModSanzioni.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModSanzioni);
		}
		/*
		 * else { lCumModSanzioni =new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModSanzioni.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDDomiciliare"));
		 * lTable.add(lCumModSanzioni); }
		 */

		// ==========================================================================
		// 08 - GIUDICE DI PACE : LAVORO SOSTITUTIVO
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_SOST_GP).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_SOST_GP).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_SOST_GP).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_SOST_GP).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_SOST_GP).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_SOST_GP).equals("0")))
		// || !this.getRequestStringParameter("sostitutivoGP").equals("s")
		) {
			lCumModLavSosp = new UlterioreSanzioneCumuloModel();

			lCumModLavSosp.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_LAV_SOST_GP));
			lCumModLavSosp.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_LAV_SOST_GP));
			lCumModLavSosp.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LAV_SOST_GP));
			lCumModLavSosp.setCodTipoUlterioreSanzione("08");
			lCumModLavSosp.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModLavSosp.setDataInserimento(DateUtils.getSysDate());
			lCumModLavSosp.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModLavSosp);
		}
		/*
		 * else { lCumModLavSosp = new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModLavSosp.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDSostitutivoGP"));
		 * lTable.add(lCumModLavSosp); }
		 */

		// ==========================================================================
		// 09 - GIUDICE DI PACE : LAVORO PUBBLICA UTILITA'
		// ==========================================================================
		if (((!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_PUB).equals(""))
				&& (!this.getRequestStringParameter(CAMPO_NUM_ANNI_LAV_PUB).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_PUB).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_MESI_LAV_PUB).equals("0")))
				|| ((!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_PUB).equals(""))
						&& (!this.getRequestStringParameter(CAMPO_NUM_GIORNI_LAV_PUB).equals("0")))
		// || !this.getRequestStringParameter("pubblica").equals("s")
		) {
			lCumModLavPub = new UlterioreSanzioneCumuloModel();

			lCumModLavPub.setNumAnni(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_LAV_PUB));
			lCumModLavPub.setNumMesi(this.getRequestBigDecimalParameter(CAMPO_NUM_MESI_LAV_PUB));
			lCumModLavPub.setNumGiorni(this.getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_LAV_PUB));
			lCumModLavPub.setCodTipoUlterioreSanzione("09");
			lCumModLavPub.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			lCumModLavPub.setDataInserimento(DateUtils.getSysDate());
			lCumModLavPub.setCumIdCumulo(lCumMod.getIdCumulo());
			lTable.add(lCumModLavPub);
		}
		/*
		 * else { lCumModLavPub =new UlterioreSanzioneCumuloModel();
		 * 
		 * lCumModLavPub.setIdUlterioreSanzioneCumulo(this.getRequestBigDecimalParameter("IDPubblica"));
		 * lTable.add(lCumModLavPub); }
		 */
		IUlterioreSanzioneCumulo lCtrl = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
		lCtrl.ExInserisciOModificaUlterioriSanzioniCumulo(lTable, lFascMod.getIdFascicoloSiep());

		String lPage = "";
		String azioneChiamante = "";
		String idPenaResidua = "";

		if (!isRequestParameterNullObj("AzioneChiamante")) {
			azioneChiamante = getRequestStringParameter("AzioneChiamante");
		}

		if (!isRequestParameterNullObj("IdPenaResidua")) {
			idPenaResidua = getRequestStringParameter("IdPenaResidua");
		}

		if (azioneChiamante != null && !azioneChiamante.equals("")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.cumulo.action.ActLoadDettaglioApplicazioneBenefici&"
					+ CAMPO_FAS_SIE_ID_FASCICOLO_SIEP + "=" + lFascMod.getIdFascicoloSiep() + "IdPenaResidua="
					+ idPenaResidua;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.cumulo.action.ActLoadDettaglioUlterioriSanzioni&"
					+ CAMPO_FAS_SIE_ID_FASCICOLO_SIEP + "=" + lFascMod.getIdFascicoloSiep();
		}

		return lPage;
	}

}