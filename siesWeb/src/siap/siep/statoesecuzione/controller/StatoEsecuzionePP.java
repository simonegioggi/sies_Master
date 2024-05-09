package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * aggiunto un model specifico per gli evento delle cartabia chec devono riportare anche le
 * notifiche Condannato, Difensore e Civilmente obbligato
 *
 * @since MEV_2023-33
 */
public class StatoEsecuzionePP extends StatoEsecuzioneElement {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzionePP() {
	}

	public StatoEsecuzionePP(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * elabora EventoModel
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {

		try {
			siesLogger.debug("Sono in StatoEsecuzionePP...");

			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("PENEPECUNIARIE");

			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);

			if (aEvento.getCodMotivo().equals("0061") || aEvento.getCodMotivo().equals("0104")) {
				String lDescrMotivo = aEvento.getDescrMotivo().replaceAll(" - Libero", "");
				lEvento.setDescrMotivo(lDescrMotivo);
			}

			// lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			// if ( lEvento.getCodTipoProvvedimento().equals("26")
			// || lEvento.getCodTipoProvvedimento().equals("12")
			// || lEvento.getCodTipoProvvedimento().equals("03"))
			// lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			// ======================================//
			// PENA
			// ======================================//
			if (mHashPenaResidua.containsKey(aEvento.getIdEvento())) {
				siesLogger.info("Cerco Pena Residua " + aEvento.getIdEvento());
				PenaResiduaModel lPena = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getIdEvento());
				lEvento.setPenaResidua(lPena);
				// flag ergastolo
				boolean flagErgastolo = false;

				// Valorizzo la Stringa Decorrenza pena
				if (lPena.getDataInizio() != null) {
					String lPenaDate = mCostanti.getProperty("PENA_DECORRENZA");
					lPenaDate += " " + DateUtils.getDateToString(lPena.getDataInizio(), "dd-MM-yyyy");
					// verifico che non si tratti di ergastolo
					if (lPena.getFlagErgastolo().equals("S") || lPena.getFlagErgastolo().equals("D")) {
						lPenaDate += mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");
					} else if (lPena.getDataFine() != null) {
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
						lPenaDate += " " + DateUtils.getDateToString(lPena.getDataFine(), "dd-MM-yyyy");
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
					// RECLUSIONE - MULTA
					if (lPena.getStringaReclusione() != null) {
						flag_str = true;
						tempPenaRes += mCostanti.getProperty("PENA_DA_ESPIARE") + " ";
						tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
						tempPenaRes += " " + lPena.getStringaReclusione();
					}
					if (lPena.getImportoMulta().compareTo(tmpIndex) > 0) {
						tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
						tempPenaRes += " " + StringUtils.toEuroFormat(lPena.getImportoMulta());
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
						tempPenaRes += " " + StringUtils.toEuroFormat(lPena.getImportoAmmenda());
					}

					lEvento.setStringaPenaResidua(tempPenaRes);
				}
			}

			// ======================================//
			// Notifiche
			// ======================================//
			{
				Vector lNotifiche = null;
				INotifica INotifica = SIEPLookupRemote.getNotificaRemote();
				try {
					lNotifiche = INotifica.ExRicercaNotificaByKeyEvento(aEvento.getIdEvento());
				} catch (F3BException fex) {
					siesLogger.info("* * * --> Nessun Elemento Trovato!");
				}

				if (lNotifiche != null) {
					Iterator lItx = lNotifiche.iterator();
					int lContaAvvocati = 0;
					int lContaObbligati = 0;

					while (lItx.hasNext()) {
						String lNotifica = "";

						NotificaModel lNot = (NotificaModel) lItx.next();

						// Notifica al Condannato
						if (lNot.getDataAvvenutaNotifica() != null
								// && lNot.getCodTipoNotifica().equals("E")
								&& lNot.getAvvIdAvvocatoFascicoloSiep() == null
								&& lNot.getIdCivilmenteObbligato() == null) {
							siesLogger.debug("Notifica al Condannato..." + lNot.getIdNotifica());
							lNotifica = mCostanti.getProperty("NOTIFICA") + " "
									+ DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(), "dd/MM/yyyy")
									+ " " + mCostanti.getProperty("NOTIFICA_CONDANNATO");

							lEvento.setNotifica1(lNotifica);
						}

						// Notifica all'Avvocato
						if (lNot.getDataAvvenutaNotifica() != null
								// && lNot.getCodTipoNotifica().equals("N")
								&& lNot.getAvvIdAvvocatoFascicoloSiep() != null) {
							siesLogger.debug("Notifica all'Avvocato..." + lNot.getIdNotifica());
							lContaAvvocati++;
							lNotifica = mCostanti.getProperty("NOTIFICA") + " "
									+ DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(), "dd/MM/yyyy")
									+ " " + mCostanti.getProperty("NOTIFICA_AVVOCATO");
							if (lContaAvvocati == 1)// primo difensore
								lEvento.setNotifica2(lNotifica);
							else // secondo difensore
								lEvento.setNotifica3(lNotifica);
						}
						// VERIFICARE IN CASO DI PI§ AVVOCATI QUALE NOTIFICA DEVE USCIRE

						// Notifica al Civilmente Obbligato
						if (lNot.getDataAvvenutaNotifica() != null
								// && lNot.getCodTipoNotifica().equals("E")
								&& lNot.getIdCivilmenteObbligato() != null) {
							siesLogger.debug("Notifica al Civilmente Obbligato..." + lNot.getIdNotifica());
							lContaObbligati++;
							lNotifica = mCostanti.getProperty("NOTIFICA") + " "
									+ DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(), "dd/MM/yyyy")
									+ " " + mCostanti.getProperty("NOTIFICA_CIVILMENTE");

							if (lContaObbligati == 1)// primo Obbligato
								lEvento.setNotifica4(lNotifica);
							else // secondo difensore
								lEvento.setNotifica5(lNotifica);
						}
					}
				}
			}

			// MEV_2023-33
			if (mHashRateizzazioni.containsKey(aEvento.getIdEvento())) {
				lEvento.setListaRateizzazioni(
						(Vector<RateizzazionePPModel>) mHashRateizzazioni.get(aEvento.getIdEvento()));
			}
			// MEV_2023-33

			this.mEventoStatoEsecuzione = lEvento;
			siesLogger.info("* * * --> Settato Evento" + lEvento);
		} catch (Exception ex) {
			siesLogger.error("Errore in StampaProperties", ex);
		}
	}

}