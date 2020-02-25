package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.dao.StatoEsecuzioneSqlDAO;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.log.LogF3B;

/**
 * StatoEsecuzioneSius - Classe che realizza l'elemento Provvedimento SIUS Oridnanza/Decreto
 * 
 * @author Giselda De Vita
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneSius extends StatoEsecuzioneElement {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneSius() {}

	public StatoEsecuzioneSius(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	public void elabora(siap.sico.evento.model.EventoModel aEvento) {

		Connection lConn = null;
		StatoEsecuzioneSqlDAO lStatEsec = null;
		try {
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("SIUS");

			// paolo cherubini Prova 07/12/2010
			// Se non trovo un provv collegato invece si scartare il provvedimento
			// lo carico lo stesso nell'XML poi lo scarto nello stato esecuzione
			if (!this.searchEveIdEvento(aEvento.getIdEvento())) {
				lEvento.setFamiglia("SIUS");
			} else {
				lEvento.setFamiglia("PROVV");
			}

			// Se non trovo un provv collegato
			// if(!this.searchEveIdEvento(aEvento.getIdEvento()))
			// {
			// fine paolo

			EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(aEvento);
			lEvento.setDescrMotivo(null);
			lEvento.setIsSius("S");

			StatoEsecuzioneUtilController lUtil = new StatoEsecuzioneUtilController();
			lUtil.getAnnoNumeroSius(lEveSorv);

			// descrizione_data
			if (aEvento.getCodTipoProvvedimento().equals("02"))
				lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if (aEvento.getCodTipoProvvedimento().equals("03"))
				lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			// data
			lEveSorv.setData(aEvento.getDataEmissione());
			lEvento.setEventoSorveglianza(lEveSorv);

			// caso Ordinanza Decisione del GE 0284- Indulto
			if (aEvento.getCodTipoProvvedimento().equals("03") && aEvento.getCodMotivo().equals("0284")) {
				lEveSorv.setDescrTipoProvvedimento(aEvento.getDescrUfficioEmittente() + " "
						+ aEvento.getDescrLuogoEmittente() + " con " + aEvento.getDescrTipoProvvedimento()
						+ " Sige");

				lEveSorv.setDescrUfficioEmittente(null);
				lEveSorv.setDescrLuogoEmittente(null);

				lEvento.setDescrUfficioEmittente(null);
				lEvento.setDescrLuogoEmittente(null);

				// ================================================================================
				// Annotazioni Manuali
				// ================================================================================
				// Cerco l'annotazione manuale nell'hash table per l'Evento provv collegato
				// -------------------------------------------------------------------------
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("aEvento = " + aEvento);
				// Recupero le annotazioni. Forse MEV 29 - Punto 11
				// n.b. se l'ordinanza è iscritta SIEP, le annotazioni sono presenti
				// sul provvedimento di annotazione. Ma se iscritte SIGE sono
				// presenti direttamente sull'ordinanza la quale tra le altre cose
				// NON punta (eve_id_evento) il provvedimento SIEP
				Vector lListAnnMod = null;

				if (aEvento.getEveIdEvento() != null) {
					// Iscritto SIEP
					lListAnnMod = (Vector) mHashAnnotazioni.get(aEvento.getEveIdEvento());
				} else {
					// Iscritto SIGE
					lListAnnMod = (Vector) mHashAnnotazioni.get(aEvento.getIdEvento());
				}

				Vector lAnnotazioni = new Vector();
				String lLegge = "";
//				String lDpr = "";

				if (lListAnnMod != null) {
					for (Iterator i = lListAnnMod.iterator(); i.hasNext();) {
						AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next();

						// con o senza anticipazione sembra uguale
						// if (lAnnMod.getFlagAppProvvisoria().equals("A"))
						// lAnticipazioneEffetti =true;

						if (lEvento.getCodTipoProvvedimento() != null)
						// && !lEvento.getCodTipoProvvedimento().equals("03"))
						{
							if (lAnnMod != null && lAnnMod.getDataReclusioneDa() != null
									&& lAnnMod.getDataReclusioneA() != null)
								lAnnMod.setBeneficio("N");
							else
								lAnnMod.setBeneficio("S");
						}

						if (lLegge.length() == 0) {
							lLegge = lAnnMod.getDescrTipoAnnotazione() + " " + lAnnMod.getDescrDpr();
//							lDpr = lAnnMod.getDescrDpr();
							// setto la frase dopo piuttosto che il motivo dell'ordinanza
							lEveSorv.setDescrMotivo(null);
							// AMBROS a8-rr-022 05/2013

							// if(lAnnMod.getBeneficio().equals("S"))
							// lEvento.setStringaDopoProvvedimento("Concede " + lLegge);
							// else
							// lEvento.setStringaDopoProvvedimento("Rigetta " + lLegge);

							// MERGE v10: modificate le diciture conformità e difformità
							if (lAnnMod.getFlagConforme().equals("-"))
								lEvento.setStringaDopoProvvedimento("Concede " + lLegge);
							else if (lAnnMod.getFlagConforme().equals("D"))
								lEvento.setStringaDopoProvvedimento("In Difformita' concede " + lLegge);
							else if (lAnnMod.getFlagConforme().equals("C"))
								lEvento.setStringaDopoProvvedimento("In Conformita' concede " + lLegge);
							else if (lAnnMod.getFlagConforme().equals("R"))
								lEvento.setStringaDopoProvvedimento("Rigetta " + lLegge);
							else if (lAnnMod.getFlagConforme().equals("I"))
								lEvento.setStringaDopoProvvedimento("Dichiara Inammissibile " + lLegge);
							else if (lAnnMod.getFlagConforme().equals("U"))
								lEvento.setStringaDopoProvvedimento("Riunisce " + lLegge);
							// END AMBROS
						}

						lAnnMod.calcolaStringaArresto();
						lAnnMod.calcolaStringaReclusione();
						lAnnotazioni.add(lAnnMod);

						// Numero/Anno declaratoria
						// MEV29 - visualizzo anno e numero Procedimento SIGE se valorizzati
						if (lAnnMod.getChiaveAnnoSige() != null && lAnnMod.getChiaveNumeroSige() != null) {
							lEveSorv.setAnnoRegistro(lAnnMod.getChiaveAnnoSige());
							lEveSorv.setNumeroRegistro(lAnnMod.getChiaveNumeroSige());
						} else if (lAnnMod.getNumeroGe() != null && lAnnMod.getNumeroGe().length() > 0) {
							lEveSorv.setAnnoRegistro(lAnnMod.getAnnoGe());
							lEveSorv.setNumeroRegistro(new BigDecimal(lAnnMod.getNumeroGe()));
						}

					}// fine for su Annotazioni

					// If libero si dovrebbe far vedere anche la pena residua....
				}

				//

				lEvento.setAnnotazioniManuali(lAnnotazioni);

				String lStringAnn = mCostanti.getProperty("MISURA_INDULTO");
				String lTotaleAnnotazione = lUtil.getAnnotazioneTotale(lAnnotazioni);

				if (lTotaleAnnotazione.length() > 0)
					lEvento.setStringaAnnotazioniTotale(lStringAnn + " " + lTotaleAnnotazione);
			}// Fine caso Evento Ordinanza GE

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("* * * --> Settato Evento" + lEveSorv);
			this.mEventoStatoEsecuzione = lEvento;

			// Ricerca i Tenori
			lConn = getDBConnection();
			lStatEsec = new StatoEsecuzioneSqlDAO(lConn);

			Vector lTenori = lStatEsec
					.ricercaTenori(aEvento.getIdEvento(), aEvento.getCodTipoProvvedimento());

			if (lTenori != null && lTenori.size() > 0)
				lEveSorv.setTenori(lTenori);

			// Devo verificare se il decreto(/ordinanza è di differimento
			// in questo caso devo verificare fino al...

			// paolo cherubini Prova 07/12/2010 collegata alla modifica sopra
			// }
			// else
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.info("* * * --> Ordinanza legata ad un provvedimento!");
			// this.mEventoStatoEsecuzione = null;
			// }
			// fine paolo

			// Cerco la Pena Residua sull'hash table
			PenaResiduaModel lPenResStatoMod = (PenaResiduaModel) this.mHashPenaResidua.get(aEvento
					.getIdEvento());

			/*
			 * //Caso Ordinanza Indulto if (aEvento.getCodTipoProvvedimento().equals("03") &&
			 * aEvento.getCodMotivo().equals("0284") && aEvento.getEveIdEvento() != null) { //Caso soggetto
			 * scarcerato //Per questo eveto ho gia' cercato l'annotazione manuale //Non devo farlo in seguito
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Ordinanza Concessione INDULTO scarcerato = " +
			 * aEvento.getDescrTipoProvvedimento() + " " + aEvento.getDescrMotivo());
			 * 
			 * lPenResStatoMod = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getEveIdEvento());
			 * 
			 * if (lPenResStatoMod != null && lPenResStatoMod.getDataFine() != null) {
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Data Fine <<<< = " + lPenResStatoMod.getDataFine() + " * * * " +
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * aEvento.getDataEmissione()); siesLogger.info("COMPARE = = = " +
			 * lPenResStatoMod.getDataFine().compareTo(aEvento.getDataEmissione())); if
			 * (lPenResStatoMod.getDataFine().compareTo(aEvento.getDataEmissione()) < 0) {
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Ordinanza SCARCERATO O O O O O<<<< = " + aEvento.getDescrTipoProvvedimento() +
			 * " " + aEvento.getDescrMotivo()); } lPenResStatoMod.setDiesAQuo(null); } }
			 */

			// Se la Pena è significativa ( nel senso
			// che i quantum e gli importi sono != 0 )
			// setta a null il campo dies a quo
			// per problemi di compatibilità sui template
			// con i dati migrati da RES
			if (lPenResStatoMod != null && !lPenResStatoMod.isSignificativa())
				lPenResStatoMod.setDiesAQuo(null);
			/*
			 * // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * // all'OS siesLogger.info(" >>> Ordine Scar INDULTO = " + lEvento.getDescrTipoProvvedimento() + " " +
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * lEvento.getDescrMotivo()); siesLogger.info(" >>> EveIdEvento = " + lEvento.getIdEvento());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> IdEvento = " + lEvento.getIdEvento()); if (aEvento.getEveIdEvento() != null)
			 * lPenResStatoMod = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getEveIdEvento());
			 * 
			 * String lDescrMotivo = " a seguito di concessione indulto emesso in data " +
			 * DateUtils.getDateToString(lEvento.getDataEmissione(), "dd-MM-yyyy");
			 * 
			 * if (lPenResStatoMod != null) {
			 * 
			 * lPenResStatoMod.setDiesAQuo("S"); //Soggetto Scarcerato
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Ordine Scar Data Fine = " +
			 * DateUtils.getDateToString(lPenResStatoMod.getDataFine(), "dd/MM/yyyy"));
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Ordine Scar Data Emissione = " +
			 * DateUtils.getDateToString(lEvento.getDataEmissione(), "dd/MM/yyyy"));
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * if (lPenResStatoMod.getDataFine() != null) siesLogger.info(" >>> Ordine Scar Compare = " +
			 * lPenResStatoMod.getDataFine().compareTo(lEvento.getDataEmissione()));
			 * 
			 * if (lPenResStatoMod.getDataFine() != null &&
			 * lPenResStatoMod.getDataFine().compareTo(lEvento.getDataEmissione()) < 0) { //Cambia la dicitura
			 * per l'OS di soggetto scarcerato //data fine pena < data provvedimento lDescrMotivo +=
			 * ". Scarcerazione avvenuta in data " + DateUtils.getDateToString(lPenResStatoMod.getDataFine(),
			 * "// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * "dd-MM-yyyy"); } siesLogger.info(" >>> >>> DESCR OS INDULTO = " + lEvento.getDescrTipoProvvedimento()
			 * + " " + lEvento.getDescrMotivo()); lEvento.setDescrMotivo(lDescrMotivo); }
			 */

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties", ex);
			ex.printStackTrace();
		} finally {
			try {
				cleanup(lStatEsec);
				cleanup(lConn);
			} catch (Exception eee) {
			}

		}

	}
}