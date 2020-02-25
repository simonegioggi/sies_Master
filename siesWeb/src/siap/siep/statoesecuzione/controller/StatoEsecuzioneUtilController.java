package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoesecuzione.config.StampaProperties;
import siap.siep.statoesecuzione.dao.StatoEsecuzioneSqlDAO;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * Classe di Utilità per le query su DB surante la creazione dello stato esecuzione
 * 
 * @author Giselda De Vita
 *
 */
@SuppressWarnings("rawtypes")
public class StatoEsecuzioneUtilController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	static StampaProperties mCostanti = StampaProperties.getInstance();

	/**
	 * getDataSopsensione - Restituisce la data sospensione. Metodo utile per quei provvedimenti interruttivi
	 * che al posto della data di emissione cdevono visualizzare la data di sospensione.
	 * 
	 * @return Data inizio esecuzione
	 */
	Date getDataSospensione(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		SospensioneSqlDAO lSospSql = null;
		Date lReturnDate = null;
		try {
			lConn = getDBConnection();
			lSospSql = new SospensioneSqlDAO(lConn);

			lSospSql = new SospensioneSqlDAO(lConn);
			lSospSql.ricercaSospensioneByFascicolo(aIdFascicolo);
			SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
			if (lSospMod != null)
				lReturnDate = lSospMod.getDataInizio();

			// fine lettura sospensione
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneUtilController.getDataSospensione: ", sqe);
			throw new F3BException("StatoEsecuzioneUtilController.getDataSospensione: " + sqe);
		}

		finally {
			cleanup(lSospSql);
			cleanup(lConn);
		}

		return lReturnDate;
	}

	/**
	 * get Anno Numero Sius
	 * 
	 * @param aEveSorv
	 */
	void getAnnoNumeroSius(EventoSorveglianzaModel aEveSorv) {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- getAnnoNumeroSius - di StatoEsecuzioneUTILController");
		Connection lConn = null;
		StatoEsecuzioneSqlDAO lStatEsec = null;
		try {
			lConn = getDBConnection();
			lStatEsec = new StatoEsecuzioneSqlDAO(lConn);
			lStatEsec.ricercaAnnoNumeroSius(aEveSorv.getIdEvento(), aEveSorv.getCodTipoProvvedimento());
			lStatEsec.start();

			if (lStatEsec.next()) {
				aEveSorv.setAnnoRegistro(lStatEsec.getBigDecimal("ANNO"));
				aEveSorv.setNumeroRegistro(lStatEsec.getBigDecimal("NUMERO"));
				aEveSorv.setFlagElaborato(lStatEsec.getString("ELABORATO"));
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in getAnnoNumeroSius");
			ex.printStackTrace();
		} finally {
			try {
				cleanup(lStatEsec);
				cleanup(lConn);
			} catch (Exception eee) {
			}

		}
	}

	/**
	 * getAnnotazioneTotale Restituisce la stringa totale delle Annotazioni
	 */
	public String getAnnotazioneTotale(Vector lAnnotazioni) throws F3BException {
		String lReturn = "";
		CalcoloPenaModel lAnnPenaMod = new CalcoloPenaModel();

		lAnnPenaMod.setIndulto(lAnnotazioni);

		String lReclusioneAnn = lAnnPenaMod.getBeneficiReclusione("-").getStringPerStampa();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("getBeneficiReclusione === " + lAnnPenaMod.getBeneficiReclusione("-"));

		if (lReclusioneAnn != null && lReclusioneAnn.length() > 0)
			lReturn += " " + lReclusioneAnn + " " + mCostanti.getProperty("CONC_RECLUSIONE");

		String lArrestoAnn = lAnnPenaMod.getBeneficiArresti("-").getStringPerStampa();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("getBeneficiArresti === " + lAnnPenaMod.getBeneficiArresti("-"));

		if (lArrestoAnn != null && lArrestoAnn.length() > 0)
			lReturn += " " + lArrestoAnn + " " + mCostanti.getProperty("CONC_ARRESTO");

		String lIndultoMulta = "";
		if (lAnnPenaMod.getBeneficiReclusione("-").getImportoMulta() > 0) {
			lIndultoMulta = StringUtils
					.toEuroFormat(new BigDecimal(lAnnPenaMod.getBeneficiReclusione("-").getImportoMulta()));
			lIndultoMulta = " Multa Euro " + lIndultoMulta;
		}

		String lIndultoAmmenda = "";
		if (lAnnPenaMod.getBeneficiArresti("-").getImportoAmmenda() > 0) {
			lIndultoAmmenda = StringUtils
					.toEuroFormat(new BigDecimal(lAnnPenaMod.getBeneficiArresti("-").getImportoAmmenda()));
			lIndultoAmmenda = " Ammenda Euro " + lIndultoAmmenda;
		}

		if (lIndultoMulta.length() > 0 || lIndultoAmmenda.length() > 0)
			lReturn += lIndultoMulta + lIndultoAmmenda;

		return lReturn;

	}

	/**
	 * getFungibilita
	 * 
	 * @param lIdEvento
	 * @return
	 * @throws F3BException
	 */
	public String getFungibilita(BigDecimal lIdEvento) throws F3BException {
		FungibilitaSqlDAO lFunDao = null;
		Connection lConn = null;
		String lFungibilita = "";

		try {
			// -----------------------------------------
			// Cerco la Fungibilità
			// -----------------------------------------
			lConn = getDBConnection();

			lFunDao = new FungibilitaSqlDAO(lConn);
			lFunDao.ricercaFungibilitaByKeyEvento(lIdEvento);
			FungibilitaModel lFunMod = (FungibilitaModel) lFunDao.getModelByKey();

			if (lFunMod != null) {// C'e' fungibilita'
				lFunMod.calcolaStringaFungibilita();
				if (lFunMod.getCodTipoFungibilita().equals("02"))// Espiate in eccesso
					lFunMod.setStringaFungibilita(
							mCostanti.getProperty("LA_FUNGIBILITA") + " " + lFunMod.getStringaFungibilita());
				else// conteggiata
					lFunMod.setStringaFungibilita(mCostanti.getProperty("INDU_FUNGIBILITA") + " "
							+ lFunMod.getStringaFungibilita());

				lFungibilita = lFunMod.getStringaFungibilita();
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StatoEsecuzioneIndulto-getFungibilita", ex);
			ex.printStackTrace();
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}
		return lFungibilita;
	}

}