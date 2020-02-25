package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * Famiglia RIDETERM per gestire eventi con associati computi.
 * 
 * Recupera i dati della Annotazioni Manuali ma non effettua per ora particolari formattazioni o calcoli (es
 * totali)
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneRideterminazione extends StatoEsecuzioneElement {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneRideterminazione() {
	}

	public StatoEsecuzioneRideterminazione(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"StatoEsecuzioneRideterminazione" + aEvento.getIdEvento() + " - "
							+ aEvento.getCodMotivo());

			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("RIDETERM");

			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);

			// lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if (lEvento.getCodTipoProvvedimento().equals("26")
					|| lEvento.getCodTipoProvvedimento().equals("12")
					|| lEvento.getCodTipoProvvedimento().equals("03"))
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			// Formatto la pena residua
			if (mHashPenaResidua.containsKey(aEvento.getIdEvento())
					&& !aEvento.getCodTipoProvvedimento().equals("12")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Pena residua presente: formatto i dati");

				PenaResiduaModel lPena = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getIdEvento());
				lEvento.setPenaResidua(lPena);
				// flag ergastolo
				boolean flagErgastolo = false;

				// Valorizzo la Stringa Decorrenza pena
				if (lPena.getDataInizio() != null) {
					String lPenaDate = mCostanti.getProperty("PENA_DECORRENZA");
					lPenaDate += DateUtils.getDateToString(lPena.getDataInizio(), "dd-MM-yyyy");
					// verifico che non si tratti di ergastolo
					if (lPena.getFlagErgastolo().equals("S") || lPena.getFlagErgastolo().equals("D")) {
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");
					} else if (lPena.getDataFine() != null) {
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
						lPenaDate += DateUtils.getDateToString(lPena.getDataFine(), "dd-MM-yyyy");
					}
					lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);
				}

				// Valorizzo la Stringa pena residua
				// solo se il flag per l'ergastolo = false
				if (!flagErgastolo) {
					lPena.calcolaStringaReclusione();
					lPena.calcolaStringaArresto();
					BigDecimal tmpIndex = new BigDecimal(0);
					String tempPenaRes = "";
					boolean flag_str = false;
					// pena rideterminata RECLUSIONE - MULTA
					if (lPena.getStringaReclusione() != null) {
						flag_str = true;
						tempPenaRes += mCostanti.getProperty("PENA_RIDETERMINATA") + " ";
						tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
						tempPenaRes += " " + lPena.getStringaReclusione();
					}
					if (lPena.getImportoMulta().compareTo(tmpIndex) > 0) {
						tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
						tempPenaRes += " " + lPena.getImportoMulta();
					}
					// ARRESTO - AMMENDA
					if (lPena.getStringaArresto() != null) {
						if (!flag_str)
							tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
						tempPenaRes += " " + mCostanti.getProperty("PENA_ARRESTO");
						tempPenaRes += " " + lPena.getStringaArresto();
					}
					if (lPena.getImportoAmmenda().compareTo(tmpIndex) > 0) {
						tempPenaRes += " " + mCostanti.getProperty("PENA_AMMENDA_EURO");
						tempPenaRes += " " + lPena.getImportoAmmenda();
					}
					lEvento.setStringaPenaResidua(tempPenaRes);
				}
			}

			// ================================================================================
			// Annotazioni Manuali
			// ================================================================================
			// Cerco l'annotazione manuale nell'hash table
			// -------------------------------------------------------------------------
			// lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento());
			// List lListAnnMod = (ArrayList) lAnnoSqlDao.getModels();
			// -------------------------------------------------------------------------
			Vector lListAnnMod = (Vector) mHashAnnotazioni.get(lEvento.getIdEvento());
			Vector lAnnotazioni = new Vector();

			if (lListAnnMod != null && lListAnnMod.size() > 0) {
				for (Iterator i = lListAnnMod.iterator(); i.hasNext();) {
					AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next();

					lAnnMod.calcolaStringaArresto();
					lAnnMod.calcolaStringaReclusione();
					if ("-".equals(lAnnMod.getFlagConforme())
							&& ("0121".compareTo(aEvento.getCodMotivo()) == 0
									|| "0212".compareTo(aEvento.getCodMotivo()) == 0
									|| "0213".compareTo(aEvento.getCodMotivo()) == 0 || isRideterminazionePenaAltro(aEvento
										.getCodMotivo()))) {
						lAnnMod.setFlagConforme(null); // Altrimenti sul template esce
														// "esito: Richiesta Accolta"
					}

					lAnnotazioni.add(lAnnMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("lAnnMod == " + lAnnMod);
				}
			}
			lEvento.setAnnotazioniManuali(lAnnotazioni);

			this.mEventoStatoEsecuzione = lEvento;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Errore in StampaProperties", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param aCodMotivo
	 * @return
	 */
	private boolean isRideterminazionePenaAltro(String aCodMotivo) {
		boolean isRidet = false;

		if (DecodificheUtils.containsCode(DecodificheManager.getInstance()
				.getRideterminazionePenaAltroDufficio(), aCodMotivo))
			return true; // D'ufficio
		if (DecodificheUtils.containsCode(DecodificheManager.getInstance()
				.getRideterminazionePenaAltroAUfficio(), aCodMotivo))
			return true; // Altro Ufficio - Altra Autorita
		if (DecodificheUtils.containsCode(DecodificheManager.getInstance().getRideterminazionePenaAltroGE(),
				aCodMotivo))
			return true; // Altro Ufficio - Giudice esecuzione
		if (DecodificheUtils.containsCode(
				DecodificheManager.getInstance().getRideterminazionePenaAltroSORV(), aCodMotivo))
			return true; // Altro Ufficio - Sorveglianza

		return isRidet;
	}

}