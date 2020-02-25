package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * Provvedimento Generico
 * 
 * @author Giselda De Vita
 *
 */
@SuppressWarnings("rawtypes")
public class StatoEsecuzioneSimeone extends StatoEsecuzioneElement {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneSimeone() {
	}

	public StatoEsecuzioneSimeone(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {

		// boolean lSorveglianza = false;

		try {
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("SIMEONE");

			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);

			if (aEvento.getCodMotivo().equals("0061") || aEvento.getCodMotivo().equals("0104")) {
				String lDescrMotivo = aEvento.getDescrMotivo().replaceAll(" - Libero", "");
				lEvento.setDescrMotivo(lDescrMotivo);
			}

			// lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if (lEvento.getCodTipoProvvedimento().equals("26")
					|| lEvento.getCodTipoProvvedimento().equals("12")
					|| lEvento.getCodTipoProvvedimento().equals("03"))
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			if (mHashEventiRiferimento != null && aEvento.getEveIdEvento() != null
					&& mHashEventiRiferimento.containsKey(aEvento.getEveIdEvento())) {
				// lSorveglianza = true;

				siap.sico.evento.model.EventoModel lEventoOrdinanza = (siap.sico.evento.model.EventoModel) mHashEventiRiferimento
						.get(aEvento.getEveIdEvento());

				EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);

				if (lEveSorv.getCodTipoProvvedimento().equals("26")
						|| lEveSorv.getCodTipoProvvedimento().equals("12")
						|| lEveSorv.getCodTipoProvvedimento().equals("03"))
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
				else
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));

				lEvento.setEventoSorveglianza(lEveSorv);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("* * * --> Addizionato MA e SORV" + lEvento);
			} else {// Non c'e' Ordinanza
					// lEvento.setDescrTipoProvvedimento(lEvento.getDescrTipoProvvedimento() + " " +
					// lEvento.getDescrMotivo());
			}

			// ======================================//
			// PENA
			// ======================================//

			if (mHashPenaResidua.containsKey(aEvento.getIdEvento())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
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
			// Notifiche all'avvocato
			// ======================================//

			if (!aEvento.getCodMotivo().equals("0063")) {// Se l'evento non è per arresti domiciliari prendo
															// le notifiche
															// Notifiche
				Vector lNotifiche = null;
				INotifica INotifica = SIEPLookupRemote.getNotificaRemote();
				try {
					lNotifiche = INotifica.ExRicercaNotificaByKeyEvento(aEvento.getIdEvento());
				} catch (F3BException fex) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("* * * --> Nessun Elemento Trovato!");
				}

				if (lNotifiche != null) {
					Iterator lItx = lNotifiche.iterator();
					int lContaAvvocati = 0;
					while (lItx.hasNext()) {
						String lNotifica = "";
						lContaAvvocati++;
						NotificaModel lNot = (NotificaModel) lItx.next();

						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						//// posto di mLog
						// siesLogger.debug("Notifica " + lContaAvvocati +" = " + lNot);

						if (lNot.getDataAvvenutaNotifica() != null && lNot.getCodTipoNotifica().equals("E")) {
							lNotifica = mCostanti.getProperty("NOTIFICA") + " "
									+ DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(), "dd/MM/yyyy")
									+ " " + mCostanti.getProperty("NOTIFICA_CONDANNATO");

							lEvento.setNotifica1(lNotifica);
						}
						if (lNot.getDataAvvenutaNotifica() != null && lNot.getCodTipoNotifica().equals("N")) {
							lNotifica = mCostanti.getProperty("NOTIFICA") + " "
									+ DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(), "dd/MM/yyyy")
									+ " " + mCostanti.getProperty("NOTIFICA_AVVOCATO");
							if (lContaAvvocati <= 2)// primo difensore
								lEvento.setNotifica2(lNotifica);
							else // secondo difensore
								lEvento.setNotifica3(lNotifica);
						}
						// ======================================//
						// ISTANZA PRESENTE - prendo l'ufficio uds per la frase sulla istanza
						// ======================================//
						if (aEvento.getEveIdEvento() != null && lNot.getCodTipoNotifica().equals("N")
								&& lNot.getUffCodUfficio() != null) {
							String lIStSimeone = mCostanti.getProperty("ISTANZA_SIMEONE");
							IUfficio lUff = SICOLookupRemote.getUfficioRemote();
							UfficioModel lUffMod = lUff.ExRicercaUfficioByCod(lNot.getUffCodUfficio());

							lIStSimeone += " " + lUffMod.getDescrComune();
							lEvento.setStringaDopoProvvedimento(lIStSimeone);
						}

					}
				}

			}

			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("* * * --> Settato Evento" + lEvento);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties", ex);
			ex.printStackTrace();
		}
	}

}