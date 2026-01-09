package it.mig.sies.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.mig.sies.exception.LoadException;
import it.mig.sies.model.DatiPubblicoMinistero;
import it.mig.sies.model.DettagliFascicolo;
import it.mig.sies.model.Sinonimo;
import it.mig.sies.model.Soggetto;
import it.mig.sies.model.TitoloGiudiziario;
import it.mig.sies.type.esecuzione_NEW.Anagrafica;
import it.mig.sies.type.esecuzione_NEW.ChiaviAnagrafica;
import it.mig.sies.type.esecuzione_NEW.ChiaviProvvedimentoGiudiziario;
import it.mig.sies.type.esecuzione_NEW.DatiTribunaleSorveglianza;
import it.mig.sies.type.esecuzione_NEW.DatiUfficioSorveglianza;
import it.mig.sies.type.esecuzione_NEW.Durata;
import it.mig.sies.type.esecuzione_NEW.MisuraSicurezza;
import it.mig.sies.type.esecuzione_NEW.PeriodoLibertaAnticipata;
import it.mig.sies.type.esecuzione_NEW.ProvvedimentoGiudiziario;
import it.mig.sies.type.esecuzione_NEW.ResponseData;
import it.mig.sies.type.esecuzione_NEW.Ufficio;
import it.mig.sies.type.foglicomplementari.ArrayOmonimi.Omonimo;
import it.mig.sies.type.foglicomplementari.Utente;

/**
 * SIES FASE 2 - Classe di utility per il mapping tra model SIES e model richiesto per la trasmissione dei
 * dati
 *
 * @author Federico Paparoni
 *
 */
public class Mapper {

	private final static String SUFFISSO_MESSAGGI = "messaggio.";

	/**
	 * Effettua il mapping tra l'utente estratto e la struttura utente da inviare
	 *
	 * @param utenteSies
	 */
	public static it.mig.sies.type.esecuzione_NEW.Utente map(it.mig.sies.model.Utente utenteSies) {
		// Ufficio relativo all'utente SIES
		Ufficio ufficio = new Ufficio();
		ufficio.setCodiceDistretto(utenteSies.getSedeDistretto());
		ufficio.setCodiceSede(utenteSies.getSedeUfficio());
		ufficio.setCodiceSistema(utenteSies.getCodiceSistema());
		ufficio.setCodiceTipo(utenteSies.getCodiceTipoUfficio());

		// Utente SIES
		it.mig.sies.type.esecuzione_NEW.Utente utente = new it.mig.sies.type.esecuzione_NEW.Utente();
		utente.setNome(utenteSies.getNome());
		utente.setCognome(utenteSies.getCognome());
		utente.setIpServer(utenteSies.getIpServer());
		utente.setUsername(utenteSies.getUsername());
		utente.setUfficio(ufficio);
		return utente;
	}

	/**
	 * Effettua il mapping tra il titolo giudiziario estratto e la struttura dati da inviare
	 *
	 * @param titoloGiudiziario
	 */
	public static ProvvedimentoGiudiziario map(TitoloGiudiziario titoloGiudiziario)
			throws DatatypeConfigurationException {
		ProvvedimentoGiudiziario provvedimentoGiudiziario = new ProvvedimentoGiudiziario();

		if (titoloGiudiziario.getAnnoSentenza() != 0) {
			provvedimentoGiudiziario.setAnnoSentenza("" + titoloGiudiziario.getAnnoSentenza());
		}

		provvedimentoGiudiziario.setCodiceAutorita(titoloGiudiziario.getCodiceAutoritaCentrale());
		// Informazione non gestita
		provvedimentoGiudiziario.setCodiceSedeAutoritaPrincipale(null);
		provvedimentoGiudiziario
				.setCodiceSedeAutoritaPrincipaleDistaccata(titoloGiudiziario.getSedeAutorita());

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(titoloGiudiziario.getDataProvvedimento());
		provvedimentoGiudiziario
				.setDataProvvedimento(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		// MEV_ENG_31_BIS: aggiunto campo in impostazione
		if (titoloGiudiziario.getDataImpugnazione() != null) {
			GregorianCalendar calendarImp = new GregorianCalendar();
			calendarImp.setTime(titoloGiudiziario.getDataImpugnazione());
			provvedimentoGiudiziario
					.setDataImpugnazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendarImp));
		}
		provvedimentoGiudiziario.setNumeroSentenza(titoloGiudiziario.getNumeroSentenza());
		// Informazione non gestita
		provvedimentoGiudiziario.setTipoAtto(null);

		// Questo flag indica se il provvedimento
		// e' un cumulo di provvedimenti
		if ((titoloGiudiziario.getFlagCumulante() != null)
				&& (titoloGiudiziario.getFlagCumulante().equals("S")))
			provvedimentoGiudiziario.setFlagCumulante(true);
		else
			provvedimentoGiudiziario.setFlagCumulante(false);

		ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario = new ChiaviProvvedimentoGiudiziario();
		chiaviProvvedimentoGiudiziario.setNsc(titoloGiudiziario.getChiaveNSC());
		chiaviProvvedimentoGiudiziario.setSies(titoloGiudiziario.getChiaveSies());

		provvedimentoGiudiziario.setChiaviProvvedimentoGiudiziario(chiaviProvvedimentoGiudiziario);
		return provvedimentoGiudiziario;
	}

	/**
	 * Effettua il mapping tra i dati del tribunale di sorveglianza estratti e la struttura dati da inviare
	 *
	 * @param datiTribunaleSorveglianza
	 * @param dettagliFascicolo
	 */
	public static it.mig.sies.type.esecuzione_NEW.DatiTribunaleSorveglianza map(
			it.mig.sies.model.DatiTribunaleSorveglianza datiTribunaleSorveglianza,
			DettagliFascicolo dettagliFascicolo) throws LoadException {
		try {
			it.mig.sies.type.esecuzione_NEW.DatiTribunaleSorveglianza dts = new it.mig.sies.type.esecuzione_NEW.DatiTribunaleSorveglianza();

			dts.setCodiceAutorita(datiTribunaleSorveglianza.getCodiceAutorita());
			dts.setCodiceSedeAutoritaPrincipale(datiTribunaleSorveglianza.getSedeAutoritaPrinc());
			dts.setCodiceSedeAutoritaPrincipaleDistaccata(
					datiTribunaleSorveglianza.getSedeAutoritaPrinDist());
			dts.setCodiceUnivocoProvvedimento(datiTribunaleSorveglianza.getCodiceUnivocoProvvedimento());
			dts.setTipoProvvedimento(datiTribunaleSorveglianza.getTipoProvvedimento());
			dts.setGiorniLibertaAnticipata(
					BigInteger.valueOf(datiTribunaleSorveglianza.getGiorniLibertaAnticipata()));
			dts.setGiorniLibertaAnticipataLs(
					BigInteger.valueOf(datiTribunaleSorveglianza.getGiorniLibertaAnticipataLs()));
			dts.setGiorniLibertaAnticipataLi(
					BigInteger.valueOf(datiTribunaleSorveglianza.getGiorniLibertaAnticipataLi()));

			// Mapping di tutte le date
			// presenti nel DatiTribunaleSorveglianza
			mapDate(dts, datiTribunaleSorveglianza);
			// Mapping di tutte le durate
			// presenti nel DatiTribunaleSorveglianza
			mapDurata(dts, datiTribunaleSorveglianza);
			// Gestione del dettaglio fascicolo
			evaluateFascicolo(dettagliFascicolo, dts);
			dts.setNote(datiTribunaleSorveglianza.getNote());

			if (datiTribunaleSorveglianza.getPeriodoLibertaAnticipataList() != null) {
				dts.getPeriodoLibertaAnticipata()
						.addAll(mapLA(datiTribunaleSorveglianza.getPeriodoLibertaAnticipataList()));
			}

			// Terzo collegato
			// MEV 23010 - Viene mappato anche l'id SIES per il provv collegato ma non viene passato a NSC
			if ((datiTribunaleSorveglianza.getProvvedimentoCollegato() != null) && (PropertyUtil
					.isPresent(datiTribunaleSorveglianza.getProvvedimentoCollegato().getChiaveNSC())))
				dts.setIdProvvedimentoRevocato(BigInteger.valueOf(Long
						.parseLong(datiTribunaleSorveglianza.getProvvedimentoCollegato().getChiaveNSC())));

			return dts;
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	/**
	 * Metodo di mapping relativo alle informazioni di durata per i DatiTribunaleSorveglianza
	 *
	 * @param dts
	 * @param datiTribunaleSorveglianza
	 */
	private static void mapDurata(DatiTribunaleSorveglianza dts,
			it.mig.sies.model.DatiTribunaleSorveglianza datiTribunaleSorveglianza) {
		dts.setDurataMisura(evaluateDurata(datiTribunaleSorveglianza.getGiorniDurataMisura(),
				datiTribunaleSorveglianza.getMesiDurataMisura(),
				datiTribunaleSorveglianza.getAnniDurataMisura()));
		dts.setPenaRideterminataArresto(evaluateDurata(datiTribunaleSorveglianza.getGiorniPenaRidetArresto(),
				datiTribunaleSorveglianza.getMesiPenaRidetArresto(),
				datiTribunaleSorveglianza.getAnniPenaRidetArresto()));
		dts.setPenaRideterminataReclusione(
				evaluateDurata(datiTribunaleSorveglianza.getGiorniPenaRidetReclusione(),
						datiTribunaleSorveglianza.getMesiPenaRidetReclusione(),
						datiTribunaleSorveglianza.getAnniPenaRidetReclusione()));
		dts.setDurataBeneficio(evaluateDurata(datiTribunaleSorveglianza.getGiorniDurataBeneficio(),
				datiTribunaleSorveglianza.getMesiDurataBeneficio(),
				datiTribunaleSorveglianza.getAnniDurataBeneficio()));
		dts.setPenaDetentivaDaEspiareArresto(
				evaluateDurata(datiTribunaleSorveglianza.getGiorniPenaDetArresto(),
						datiTribunaleSorveglianza.getMesiPenaDetArresto(),
						datiTribunaleSorveglianza.getAnniPenaDetArresto()));
		dts.setPenaDetentivaDaEspiareReclusione(
				evaluateDurata(datiTribunaleSorveglianza.getGiorniPenaDetReclusione(),
						datiTribunaleSorveglianza.getMesiPenaDetReclusione(),
						datiTribunaleSorveglianza.getAnniPenaDetReclusione()));
		dts.setPenaRideterminata(evaluateDurata(datiTribunaleSorveglianza.getGiorniPenaRideterminata(),
				datiTribunaleSorveglianza.getMesiPenaRideterminata(),
				datiTribunaleSorveglianza.getAnniPenaRideterminata()));
	}

	/**
	 * Metodo di mapping relativo alle date per il DatiTribunaleSorveglianza
	 *
	 * @param dts
	 * @param datiTribunaleSorveglianza
	 */
	private static void mapDate(DatiTribunaleSorveglianza dts,
			it.mig.sies.model.DatiTribunaleSorveglianza datiTribunaleSorveglianza)
			throws DatatypeConfigurationException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(datiTribunaleSorveglianza.getDataProvvedimento());
		dts.setDataProvvedimento(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		dts.setDataFineBeneficio(evaluateDate(datiTribunaleSorveglianza.getDataFineBeneficio()));
		dts.setDataTermineMisura(evaluateDate(datiTribunaleSorveglianza.getDataTermineMisura()));
		dts.setDataDecorrenzaRevoca(evaluateDate(datiTribunaleSorveglianza.getDataDecorrenzaRevoca()));
		dts.setDataInizioNonEspiata(evaluateDate(datiTribunaleSorveglianza.getDataInizioNonEspiata()));
		dts.setDataFineNonEspiata(evaluateDate(datiTribunaleSorveglianza.getDataFineNonEspiata()));
		dts.setDataInizioDifferimentoPena(
				evaluateDate(datiTribunaleSorveglianza.getDataInizioDifferimentoPena()));
		dts.setDataFineDifferimentoPena(
				evaluateDate(datiTribunaleSorveglianza.getDataFineDifferimentoPena()));
		dts.setDataInizioRevoca(evaluateDate(datiTribunaleSorveglianza.getDataInizioRevoca()));
	}

	/**
	 * Metodo di mapping relativo ai dettagli del fascicolo per il DatiTribunaleSorveglianza
	 *
	 * @param dts
	 * @param dettagliFascicolo
	 */
	private static void evaluateFascicolo(DettagliFascicolo dettagliFascicolo,
			DatiTribunaleSorveglianza dts) {
		// Il dettaglio del fascicolo non e' presente per ogni provvedimento
		// dell'esecuzione
		if (dettagliFascicolo != null) {
			dts.setAnnoSentenza(BigInteger.valueOf(dettagliFascicolo.getAnnoSentenza()));
			dts.setNumeroSentenza(BigInteger.valueOf(dettagliFascicolo.getNumeroSentenza()));
			dts.setAnnoOrdinanza(BigInteger.valueOf(dettagliFascicolo.getAnnoOrdinanza()));
			dts.setNumeroOrdinanza(BigInteger.valueOf(dettagliFascicolo.getNumeroOrdinanza()));
			dts.setAnnoSIUS(BigInteger.valueOf(dettagliFascicolo.getAnnoSius()));
			dts.setNumeroSIUS(BigInteger.valueOf(dettagliFascicolo.getNumeroSius()));
			dts.setCodiceSedePM(dettagliFascicolo.getSedePm());
		}
	}

	/**
	 * Metodo di mapping relativo ai dettagli del fascicolo per il DatiUfficioSorveglianza
	 *
	 * @param dus
	 * @param dettagliFascicolo
	 */
	private static void evaluateFascicolo(DettagliFascicolo dettagliFascicolo, DatiUfficioSorveglianza dus) {
		// Il dettaglio del fascicolo non e' presente per ogni provvedimento
		// dell'esecuzione
		if (dettagliFascicolo != null) {
			dus.setAnnoSentenza(BigInteger.valueOf(dettagliFascicolo.getAnnoSentenza()));
			dus.setNumeroSentenza(BigInteger.valueOf(dettagliFascicolo.getNumeroSentenza()));
			dus.setAnnoOrdinanza(BigInteger.valueOf(dettagliFascicolo.getAnnoOrdinanza()));
			dus.setNumeroOrdinanza(BigInteger.valueOf(dettagliFascicolo.getNumeroOrdinanza()));
			dus.setAnnoSIUS(BigInteger.valueOf(dettagliFascicolo.getAnnoSius()));
			dus.setNumeroSIUS(BigInteger.valueOf(dettagliFascicolo.getNumeroSius()));
			dus.setCodiceSedePM(dettagliFascicolo.getSedePm());
		}
	}

	/**
	 * Crea un oggetto Durata in base ai parametri in input
	 *
	 * @param giorniDurata
	 * @param mesiDurata
	 * @param anniDurata
	 */
	private static Durata evaluateDurata(int giorniDurata, int mesiDurata, int anniDurata) {
		Durata durata = new Durata();
		durata.setGiorni(BigInteger.valueOf(giorniDurata));
		durata.setMesi(BigInteger.valueOf(mesiDurata));
		durata.setAnni(BigInteger.valueOf(anniDurata));
		return durata;
	}

	/**
	 * Crea un XMLGregorianCalendar in base alla data passata come input
	 *
	 * @param date
	 */
	private static XMLGregorianCalendar evaluateDate(Date date) throws DatatypeConfigurationException {
		XMLGregorianCalendar xmlCalendar = null;
		if (date != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		}
		return xmlCalendar;
	}

	/**
	 * Effettua il mapping tra i dati dell'ufficio di sorveglianza estratti e la struttura dati da inviare
	 *
	 * @param datiUfficioSorveglianza
	 * @param dettagliFascicolo
	 */
	public static it.mig.sies.type.esecuzione_NEW.DatiUfficioSorveglianza map(
			it.mig.sies.model.DatiUfficioSorveglianza datiUfficioSorveglianza,
			DettagliFascicolo dettagliFascicolo) throws LoadException {
		try {
			it.mig.sies.type.esecuzione_NEW.DatiUfficioSorveglianza dus = new it.mig.sies.type.esecuzione_NEW.DatiUfficioSorveglianza();

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(datiUfficioSorveglianza.getDataProvvedimento());
			dus.setDataProvvedimento(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			dus.setCodiceAutorita(datiUfficioSorveglianza.getCodiceAutorita());
			dus.setCodiceSedeAutoritaPrincipale(datiUfficioSorveglianza.getSedeAutoritaPrinc());
			dus.setCodiceSedeAutoritaPrincipaleDistaccata(datiUfficioSorveglianza.getSedeAutoritaPrinDist());
			dus.setCodiceUnivocoProvvedimento(datiUfficioSorveglianza.getCodiceUnivocoProvvedimento());
			dus.setTipoProvvedimento(datiUfficioSorveglianza.getTipoProvvedimento());
			dus.setTestoLibero(datiUfficioSorveglianza.getTestoLibero());
			dus.setGiorniLibertaAnticipata(
					new BigInteger("" + datiUfficioSorveglianza.getNumGiorniLibAnticipata()));
			dus.setGiorniLibertaAnticipataLs(
					new BigInteger("" + datiUfficioSorveglianza.getNumGiorniLibAnticipataLs()));
			dus.setGiorniLibertaAnticipataLi(
					new BigInteger("" + datiUfficioSorveglianza.getNumGiorniLibAnticipataLi()));
			dus.setImportoAmmenda(
					new BigDecimal(Double.valueOf(datiUfficioSorveglianza.getImportoAmmenda()).toString()));
			dus.setImportoMulta(
					new BigDecimal(Double.valueOf(datiUfficioSorveglianza.getImportoMulta()).toString()));

			dus.setDurataLibertaControllata(
					evaluateDurata(datiUfficioSorveglianza.getGiorniDurataLibControllata(),
							datiUfficioSorveglianza.getMesiDurataLibControllata(),
							datiUfficioSorveglianza.getAnniDurataLibControllata()));

			dus.setDurataLavoroSostitutivo(evaluateDurata(datiUfficioSorveglianza.getGiorniDurataLavoroSost(),
					datiUfficioSorveglianza.getMesiDurataLavoroSost(),
					datiUfficioSorveglianza.getAnniDurataLavoroSost()));

			dus.setDataDecorrenzaPrimaRata(
					evaluateDate(datiUfficioSorveglianza.getDataDecorrenzaPrimaRata()));
			dus.setDataDecorrenzaSospensione(
					evaluateDate(datiUfficioSorveglianza.getDataDecorrenzaSospensione()));

			dus.setNumeroRate(new BigInteger("" + datiUfficioSorveglianza.getNumeroRate()));
			dus.setImportoRata(
					new BigDecimal(Double.valueOf(datiUfficioSorveglianza.getImportoRate()).toString()));
			dus.setImportoUltimaRata(new BigDecimal(
					Double.valueOf(datiUfficioSorveglianza.getImportoUltimaRata()).toString()));
			dus.setGiorniDallaNotifica(new BigInteger("" + datiUfficioSorveglianza.getGiorniDallaNotifica()));

			if (datiUfficioSorveglianza.getPeriodoLibertaAnticipataList() != null) {
				dus.getPeriodoLibertaAnticipata()
						.addAll(mapLA(datiUfficioSorveglianza.getPeriodoLibertaAnticipataList()));
			}

			// Gestione del dettaglio fascicolo
			evaluateFascicolo(dettagliFascicolo, dus);
			// Terzo collegato
			if (datiUfficioSorveglianza.getIdProvvRevocato() != 0)
				dus.setIdProvvedimentoRevocato(
						new BigInteger("" + datiUfficioSorveglianza.getIdProvvRevocato()));

			return dus;
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	/**
	 * Metodo di mapping relativo alla lista di PeriodoLibertaAnticipata
	 *
	 * @param periodoList
	 */
	public static List<PeriodoLibertaAnticipata> mapLA(
			List<it.mig.sies.model.PeriodoLibertaAnticipata> periodoList)
			throws DatatypeConfigurationException {
		List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList = new ArrayList<>();
		/**
		 * E' possibile avere una lista di PeriodoLibertaAnticipata dove ogni istanza ha una coppia di
		 * informazioni data inizio-data fine
		 */
		for (it.mig.sies.model.PeriodoLibertaAnticipata pla : periodoList) {
			PeriodoLibertaAnticipata temp = new PeriodoLibertaAnticipata();
			GregorianCalendar calendar = new GregorianCalendar();
			GregorianCalendar calendar2 = new GregorianCalendar();
			calendar.setTime(pla.getDataLibertaAnticipataFine());
			calendar2.setTime(pla.getDataLibertaAnticipataInizio());
			temp.setDataLibertaAnticipataFine(
					DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			temp.setDataLibertaAnticipataInizio(
					DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar2));
			temp.setStatoPermesso(pla.getStatoPermesso());
			periodoLibertaAnticipataList.add(temp);
		}

		return periodoLibertaAnticipataList;
	}

	/**
	 * Metodo di mapping relativo alla Soggetto estratto che deve essere inviato
	 *
	 * @param soggetto
	 */
	public static Anagrafica map(Soggetto soggetto) throws DatatypeConfigurationException {
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCodiceFiscale(soggetto.getCodiceFiscale());
		anagrafica.setCodiceImprontaDigitale(soggetto.getCodiceAfis());
		anagrafica.setCodiceLuogoNascita(soggetto.getLuogoNascita());
		anagrafica.setCodiceStatoEsteroNascita(soggetto.getNazioneNascita());
		anagrafica.setDescrizioneComuneEstero(soggetto.getDescComuneEsteroNascita());
		anagrafica.setCognome(soggetto.getCognome());
		anagrafica.setDataNascita(evaluateDate(soggetto.getDataNascita()));
		anagrafica.setNome(soggetto.getNome());
		anagrafica.setNomePadre(soggetto.getPaternita());
		anagrafica.setSesso(soggetto.getSesso());

		ChiaviAnagrafica chiaviAnagrafica = new ChiaviAnagrafica();
		chiaviAnagrafica.setNsc(BigInteger.valueOf(soggetto.getChiaveNSC()));
		chiaviAnagrafica.setSies(BigInteger.valueOf(soggetto.getChiaveSies()));

		anagrafica.setChiaviAnagrafica(chiaviAnagrafica);
		return anagrafica;
	}

	/**
	 * MEV 31: modificata firma del metodo per gestire la sinonimia Effettua il mapping relativo alla response
	 * tornata dal webservice, popolando la risposta con i dati del soggetto,titolo principale ed esecutivo.
	 *
	 * @param idEvento
	 * @param operazione
	 * @param responseData
	 * @param soggetto
	 * @param dao
	 */
	public static it.mig.sies.model.ResponseData map(String idEvento, String operazione,
			it.mig.sies.type.esecuzione_NEW.ResponseData responseData, SiesDAO dao, Soggetto soggetto) {
		it.mig.sies.model.ResponseData response = new it.mig.sies.model.ResponseData();

		String esito = decodeEsito(operazione, responseData);
		response.setEsito(esito);

		/**
		 * Nel caso in cui l'esito ha un codice numerico della classe 2XX il trasferimento e' avvenuto con
		 * successo, quindi vengono settate le informazioni da visualizzare all'utente
		 */
		if (responseData.getEsito().getCodice().value().startsWith("2")) {
			response.setCompleted(true);
			response.setChiaveNSC(responseData.getChiaviProvvedimentoEsecutivo().getNsc().longValue());
			response.setChiaveSies(responseData.getChiaviProvvedimentoEsecutivo().getSies().longValue());
			response.setEstratto(responseData.getEstratto());
		} else {
			response.setChiaveSies(Long.parseLong(idEvento));
			response.setCompleted(false);
			// MEV 23010 - Contempla la possibilita' che venga restituito il certificato di controllo
			if (PropertyUtil.isPresent(responseData.getEstratto()))
				response.setEstratto(responseData.getEstratto());
			// MEV 31: SINONIMIA
			if (responseData.getArrayOmonimi() != null
					&& responseData.getArrayOmonimi().getOmonimo().size() > 0)
				response.setElencoSinonimi(
						mapOmonimoToSinonimo(responseData.getArrayOmonimi().getOmonimo(), dao, soggetto));
		}

		return response;
	}

	private static List<Sinonimo> mapOmonimoToSinonimo(
			List<it.mig.sies.type.esecuzione_NEW.ArrayOmonimi.Omonimo> elencoOmonimi, SiesDAO dao,
			Soggetto soggetto) {

		List<Sinonimo> sinonimi = new ArrayList<>();
		for (it.mig.sies.type.esecuzione_NEW.ArrayOmonimi.Omonimo omonimo : elencoOmonimi) {
			Sinonimo sinonimo = new Sinonimo();
			// scrivo sempre nel campo CF (anche per gli stranieri anche se hanno il CI)
			// il cf ed il ci sono sempre scritti nel campo CF dal ws di ricerca sinonimi
			if (PropertyUtil.isPresent(omonimo.getAnagrafica().getCodiceFiscale())) {
				if (omonimo.getAnagrafica().getCodiceFiscale().length() > 7)
					sinonimo.setCodiceFiscaleSinonimo(omonimo.getAnagrafica().getCodiceFiscale());
				else
					sinonimo.setCodiceFiscaleSinonimo(omonimo.getAnagrafica().getCodiceImprontaDigitale());
			}
			sinonimo.setCodiceLuogoNascitaSinonimo(omonimo.getAnagrafica().getCodiceLuogoNascita());
			sinonimo.setCodiceNazioneNascitaSinonimo(omonimo.getAnagrafica().getCodiceStatoEsteroNascita());
			sinonimo.setCognomeSinonimo(omonimo.getAnagrafica().getCognome());
			GregorianCalendar gc = omonimo.getAnagrafica().getDataNascita().toGregorianCalendar();
			sinonimo.setDataNascitaSinonimo(gc.getTime());
			sinonimo.setDescComuneEsteroNascitaSinonimo(omonimo.getAnagrafica().getDescrizioneComuneEstero());
			String descLuogoNascitaSinonimo = "", descNazioneNascitaSinonimo = "";
			if ("03900".equals(omonimo.getAnagrafica().getCodiceStatoEsteroNascita()))
				try {
					descLuogoNascitaSinonimo = dao
							.getDescComune(omonimo.getAnagrafica().getCodiceLuogoNascita());
				} catch (Exception e) {
					descLuogoNascitaSinonimo = "-";
				}
			descNazioneNascitaSinonimo = dao
					.getDescNazione(omonimo.getAnagrafica().getCodiceStatoEsteroNascita());
			sinonimo.setDescLuogoNascitaSinonimo(descLuogoNascitaSinonimo);
			sinonimo.setDescNazioneNascitaSinonimo(descNazioneNascitaSinonimo);
			sinonimo.setIdSinonimo(omonimo.getAnagrafica().getChiaviAnagrafica().getNsc());
			sinonimo.setNomeSinonimo(omonimo.getAnagrafica().getNome());
			sinonimo.setPaternitaSinonimo(omonimo.getAnagrafica().getNomePadre());
			sinonimo.setSessoSinonimo(omonimo.getAnagrafica().getSesso());
			// certificato di controllo
			if (PropertyUtil.isPresent(omonimo.getCertificatoControllo())) {
				sinonimo.setCertificatoControlloSinonimo(omonimo.getCertificatoControllo());
				sinonimo.setIdCertificatoControlloSinonimo(omonimo.getCertificatoControllo().hashCode());
			}
			sinonimo.setFlagAliasRichiamoSinonimo(omonimo.getAnagrafica().getFlagAliasRichiamo());

			// comparazione soggetto - sinonimo
			sinonimo.setIsEqualCognome("" + soggetto.getCognome().equals(sinonimo.getCognomeSinonimo()));
			sinonimo.setIsEqualNome("" + soggetto.getNome().equals(sinonimo.getNomeSinonimo()));

			String descLNSoggetto = "", descLNSinonimo = "";
			if ("03900".equals(sinonimo.getCodiceNazioneNascitaSinonimo()))
				descLNSinonimo = sinonimo.getDescLuogoNascitaSinonimo();
			else {
				if (PropertyUtil.isPresent(sinonimo.getDescComuneEsteroNascitaSinonimo()))
					descLNSinonimo = sinonimo.getDescComuneEsteroNascitaSinonimo() + " "
							+ sinonimo.getDescNazioneNascitaSinonimo();
				else
					descLNSinonimo = sinonimo.getDescNazioneNascitaSinonimo();
			}
			if ("03900".equals(soggetto.getNazioneNascita()))
				descLNSoggetto = soggetto.getDescComuneNascita();
			else {
				if (PropertyUtil.isPresent(soggetto.getDescComuneEsteroNascita()))
					descLNSoggetto = soggetto.getDescComuneEsteroNascita() + " "
							+ soggetto.getDescNazioneNascita();
				else
					descLNSoggetto = soggetto.getDescNazioneNascita();
			}
			sinonimo.setIsEqualLuogoNascita("" + descLNSoggetto.equals(descLNSinonimo));

			if (soggetto.getDataNascita() != null && sinonimo.getDataNascitaSinonimo() != null)
				sinonimo.setIsEqualDataNascita(
						soggetto.getDataNascita().compareTo(sinonimo.getDataNascitaSinonimo()) == 0 ? "true"
								: "false");
			else if (soggetto.getDataNascita() == null && sinonimo.getDataNascitaSinonimo() == null)
				sinonimo.setIsEqualDataNascita("true");
			else
				sinonimo.setIsEqualDataNascita("false");

			sinonimo.setIsEqualSesso("" + soggetto.getSesso().equals(sinonimo.getSessoSinonimo()));

			if (soggetto.getPaternita() != null && sinonimo.getPaternitaSinonimo() != null)
				sinonimo.setIsEqualPaternita(
						"" + soggetto.getPaternita().equals(sinonimo.getPaternitaSinonimo()));
			else if (soggetto.getPaternita() == null && sinonimo.getPaternitaSinonimo() == null)
				sinonimo.setIsEqualPaternita("true");
			else
				sinonimo.setIsEqualPaternita("false");

			// per italiani CF, per stranieri CI
			if ("03900".equals(soggetto.getNazioneNascita())) {
				if (soggetto.getCodiceFiscale() != null && sinonimo.getCodiceFiscaleSinonimo() != null)
					sinonimo.setIsEqualCodiceFiscale(
							"" + soggetto.getCodiceFiscale().equals(sinonimo.getCodiceFiscaleSinonimo()));
				else if (soggetto.getCodiceFiscale() == null && sinonimo.getCodiceFiscaleSinonimo() == null)
					sinonimo.setIsEqualCodiceFiscale("true");
				else
					sinonimo.setIsEqualCodiceFiscale("false");
			} else {
				if (soggetto.getCodiceAfis() != null && sinonimo.getCodiceIdentificativoSinonimo() != null)
					sinonimo.setIsEqualCodiceFiscale(
							"" + soggetto.getCodiceAfis().equals(sinonimo.getCodiceIdentificativoSinonimo()));
				else if (soggetto.getCodiceAfis() == null
						&& sinonimo.getCodiceIdentificativoSinonimo() == null)
					sinonimo.setIsEqualCodiceFiscale("true");
				else
					sinonimo.setIsEqualCodiceFiscale("false");
			}

			// if (soggetto.getFlagAliasRichiamo() != null && sinonimo.getFlagAliasRichiamoSinonimo() != null)
			// sinonimo.setIsEqualFlagAliasRichiamo(""
			// + soggetto.getFlagAliasRichiamo().equals(sinonimo.getFlagAliasRichiamoSinonimo()));
			// else if (soggetto.getFlagAliasRichiamo() == null && sinonimo.getFlagAliasRichiamoSinonimo() ==
			// null)
			// sinonimo.setIsEqualFlagAliasRichiamo("true");
			// else
			// sinonimo.setIsEqualFlagAliasRichiamo("false");

			// aggiungo alla lista
			sinonimi.add(sinonimo);
		}

		// valore di ritorno
		return sinonimi;
	}

	// MEV 31: cambiata la visibilitï¿½ del metodo
	public static String decodeEsito(String operazione, ResponseData responseData) {

		StringBuilder esito = new StringBuilder();
		ApplicationProperties properties = ApplicationProperties.getIstance();

		/*
		 * Per questa tipologia di codici deve essere specificato anche il tipo di operazione
		 */
		if (responseData.getEsito().getCodice().value().equals("204")
				|| responseData.getEsito().getCodice().value().equals("205")
				|| responseData.getEsito().getCodice().value().equals("206")
				|| responseData.getEsito().getCodice().value().equals("207")) {
			esito.append(properties.getProperty(SUFFISSO_MESSAGGI + operazione));
		}

		esito.append(properties.getProperty(SUFFISSO_MESSAGGI + responseData.getEsito().getCodice().value()));
		return esito.toString();
	}

	/**
	 * Effettua il mapping relativo alle misure di sicurezza presenti sul provvedimento dell'esecuzione
	 *
	 * @param idEvento
	 * @param operazione
	 * @param responseData
	 */
	public static Collection<MisuraSicurezza> mapMS(List<it.mig.sies.model.MisuraSicurezza> msList) {
		List<MisuraSicurezza> misuraSicurezzaList = new ArrayList<>();
		for (it.mig.sies.model.MisuraSicurezza ms : msList) {
			MisuraSicurezza misuraSicurezza = new MisuraSicurezza();
			Durata durataMisura = new Durata();
			durataMisura.setAnni(BigInteger.valueOf(ms.getAnni()));
			durataMisura.setMesi(BigInteger.valueOf(ms.getMesi()));
			durataMisura.setGiorni(BigInteger.valueOf(ms.getGiorni()));
			misuraSicurezza.setDurataMisura(durataMisura);
			Durata durataMisuraOld = new Durata();
			durataMisuraOld.setAnni(BigInteger.valueOf(ms.getAnniOld()));
			durataMisuraOld.setMesi(BigInteger.valueOf(ms.getMesiOld()));
			durataMisuraOld.setGiorni(BigInteger.valueOf(ms.getGiorniOld()));
			misuraSicurezza.setDurataMisuraOld(durataMisuraOld);
			misuraSicurezza.setCodiceMisuraSicurezzaProvvedimentoEsecuzione(ms.getCodice());
			misuraSicurezza.setCodiceMisuraSicurezzaTitoloEsecutivo(ms.getCodiceOld());

			misuraSicurezzaList.add(misuraSicurezza);
		}
		return misuraSicurezzaList;
	}

	/**
	 * MEV 16: Effettua il mapping tra i dati del Pubblico Ministero estratti e la struttura dati da inviare
	 *
	 * @param datiPubblicoMinistero
	 * @param dettagliFascicolo
	 */
	public static it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero mapDatiProvvedimentoPMFC(
			DatiPubblicoMinistero datiPubblicoMinistero, DettagliFascicolo dettagliFascicolo)
			throws LoadException {

		try {
			it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero dpm = new it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero();

			dpm.setCodiceAutorita(datiPubblicoMinistero.getCodiceAutorita());
			dpm.setSedeAutoritaPrinc(datiPubblicoMinistero.getSedeAutoritaPrinc());
			dpm.setSedeAutoritaPrinDist(datiPubblicoMinistero.getSedeAutoritaPrinDist());
			// CodiceUnivocoProvvedimento ha lunghezza 12 nell'xsd, allora modifico il valore
			if ("0424DEFI_SORV".equals(datiPubblicoMinistero.getCodiceUnivocoProvvedimento()))
				datiPubblicoMinistero.setCodiceUnivocoProvvedimento("0424DEF_SORV");
			else if ("0083SC-LIBARA".equals(datiPubblicoMinistero.getCodiceUnivocoProvvedimento()))
				datiPubblicoMinistero.setCodiceUnivocoProvvedimento("0083SC-LIBAR");
			// MEV 16 CUMULO: aggiungo casistiche particolari
			else if (datiPubblicoMinistero.getCodiceUnivocoProvvedimento().endsWith("_NEW"))
				datiPubblicoMinistero.setCodiceUnivocoProvvedimento(
						datiPubblicoMinistero.getCodiceUnivocoProvvedimento().replace("_NEW", ""));
			dpm.setCodiceUnivocoProvvedimento(datiPubblicoMinistero.getCodiceUnivocoProvvedimento());
			dpm.setTipoProvvedimento(datiPubblicoMinistero.getTipoProvvedimento());
			dpm.setErgastolo(datiPubblicoMinistero.getErgastolo());
			if (datiPubblicoMinistero.getImportoAmmenda() != 0)
				dpm.setImportoAmmenda(
						new BigDecimal(Double.valueOf(datiPubblicoMinistero.getImportoAmmenda()).toString()));
			if (datiPubblicoMinistero.getImportoMulta() != 0)
				dpm.setImportoMulta(
						new BigDecimal(Double.valueOf(datiPubblicoMinistero.getImportoMulta()).toString()));

			// Mapping di tutte le date presenti nel model datiPubblicoMinistero
			mapDatePMFC(dpm, datiPubblicoMinistero);
			// Mapping di tutte le durate presenti nel datiPubblicoMinistero
			mapDurataPMFC(dpm, datiPubblicoMinistero);
			// Gestione del dettaglio fascicolo
			evaluateFascicoloPMFC(dettagliFascicolo, dpm);

			// MEV 16 CUMULO: aggiunta associazione valori MS & PA per il cumulo
			// if (PropertyUtil.isPresent(datiPubblicoMinistero.getListaMisureSicurezzaCumulo())) {
			// for (MisuraSicurezzaCumulo msc : datiPubblicoMinistero.getListaMisureSicurezzaCumulo()) {
			// it.mig.sies.type.foglicomplementari.MisuraSicurezza ms = mapDatiMS(msc);
			// dpm.getMisuraSicurezza().add(ms);
			// }
			// }
			// if (PropertyUtil.isPresent(datiPubblicoMinistero.getListaPeneAccessorieCumulo())) {
			// for (PenaAccessoriaCumulo pac : datiPubblicoMinistero.getListaPeneAccessorieCumulo()) {
			// it.mig.sies.type.foglicomplementari.PenaAccessoria pa = mapDatiPA(pac);
			// dpm.getPenaAccessoria().add(pa);
			// }
			// }

			// associazione altri dati
			// if (datiPubblicoMinistero.getLiberazioneAnticipataCumulo() != null)
			// dpm.setLiberazioneAnticipataConcessaDetrarreCumulo(mapDatiLiberazioneAnticipataCumulo(
			// datiPubblicoMinistero.getLiberazioneAnticipataCumulo()));
			// if (datiPubblicoMinistero.getSanzioniGPCumulo() != null)
			// dpm.setSanzioniGiudicePace(
			// mapDatiSanzioniGPCumulo(datiPubblicoMinistero.getSanzioniGPCumulo()));
			// if (datiPubblicoMinistero.getSanzioniSostitutiveCumulo() != null)
			// dpm.setSanzioniSostitutive(mapDatiSanzioniSostitutiveCumulo(
			// datiPubblicoMinistero.getSanzioniSostitutiveCumulo()));
			// if (datiPubblicoMinistero.getConversionePPCumulo() != null)
			// dpm.setPenaConversionePenaPecuniaria(
			// mapDatiConversionePPCumulo(datiPubblicoMinistero.getConversionePPCumulo()));
			// if (PropertyUtil.isPresent(datiPubblicoMinistero.getListaRichiesteGECumulo())) {
			// for (RichiesteGECumulo rgec : datiPubblicoMinistero.getListaRichiesteGECumulo()) {
			// it.mig.sies.type.foglicomplementari.RichiesteGEAnticipazioneEffetti rgeae = mapDatiRichiestaGE(
			// rgec);
			// dpm.getRichiesteGEAnticipazioneEffetti().add(rgeae);
			// }
			// }

			// valore di ritorno
			return dpm;
		} catch (Exception e) {
			throw new LoadException(e);
		}
	}

	/**
	 * MEV 16: Metodo di mapping relativo ai dettagli del fascicolo per il DatiPubblicoMinistero
	 *
	 * @param dpm
	 * @param dettagliFascicolo
	 */
	private static void evaluateFascicoloPMFC(DettagliFascicolo dettagliFascicolo,
			it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero dpm) {

		// Il dettaglio del fascicolo non e' presente per ogni provvedimento dell'esecuzione
		if (dettagliFascicolo != null) {
			dpm.setAnnoSentenza(BigInteger.valueOf(dettagliFascicolo.getAnnoSentenza()));
			dpm.setNumeroSentenza(BigInteger.valueOf(dettagliFascicolo.getNumeroSentenza()));
			dpm.setAnnoOrdinanza(BigInteger.valueOf(dettagliFascicolo.getAnnoOrdinanza()));
			dpm.setNumeroOrdinanza(BigInteger.valueOf(dettagliFascicolo.getNumeroOrdinanza()));
			dpm.setAnnoSIEP(BigInteger.valueOf(dettagliFascicolo.getAnnoSiep()));
			dpm.setNumeroSIEP(BigInteger.valueOf(dettagliFascicolo.getNumeroSiep()));
			dpm.setCodiceSedePM(dettagliFascicolo.getSedePm());
		}
	}

	/**
	 * MEV 16: Metodo di mapping relativo alle date per il DatiPubblicoMinistero
	 *
	 * @param dpm
	 * @param datiPubblicoMinistero
	 */
	private static void mapDatePMFC(it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero dpm,
			DatiPubblicoMinistero datiPubblicoMinistero) throws DatatypeConfigurationException {

		dpm.setDataProvvedimento(evaluateDate(datiPubblicoMinistero.getDataProvvedimento()));
		dpm.setDataFinePena(evaluateDate(datiPubblicoMinistero.getDataFinePena()));
		dpm.setDataFinePenaDal(evaluateDate(datiPubblicoMinistero.getDataFinePenaDal()));
		dpm.setDataFinePenaAl(evaluateDate(datiPubblicoMinistero.getDataFinePenaAl()));
	}

	/**
	 * MEV 16: Metodo di mapping relativo alle informazioni di durata per i DatiPubblicoMinistero
	 *
	 * @param dpm
	 * @param datiPubblicoMinistero
	 */
	private static void mapDurataPMFC(it.mig.sies.type.foglicomplementari.DatiPubblicoMinistero dpm,
			DatiPubblicoMinistero datiPubblicoMinistero) {

		dpm.setArresto(evaluateDurataPMFC(datiPubblicoMinistero.getGiorniArresto(),
				datiPubblicoMinistero.getMesiArresto(), datiPubblicoMinistero.getAnniArresto()));
		dpm.setReclusione(evaluateDurataPMFC(datiPubblicoMinistero.getGiorniReclusione(),
				datiPubblicoMinistero.getMesiReclusione(), datiPubblicoMinistero.getAnniReclusione()));
		dpm.setIsolamentoDiurno(evaluateDurataPMFC(datiPubblicoMinistero.getGiorniIsolamentoDiurno(),
				datiPubblicoMinistero.getMesiIsolamentoDiurno(),
				datiPubblicoMinistero.getAnniIsolamentoDiurno()));
	}

	/**
	 * MEV 16: metodo di mappatura dati utente
	 *
	 * @param utenteSies
	 * @return
	 */
	public static Utente mapUtenteFC(it.mig.sies.model.Utente utenteSies) {

		// Ufficio relativo all'utente SIES
		it.mig.sies.type.foglicomplementari.Ufficio ufficio = new it.mig.sies.type.foglicomplementari.Ufficio();
		ufficio.setCodiceDistretto(utenteSies.getSedeDistretto());
		ufficio.setCodiceSede(utenteSies.getSedeUfficio());
		ufficio.setCodiceSistema(utenteSies.getCodiceSistema());
		ufficio.setCodiceTipo(utenteSies.getCodiceTipoUfficio());

		// Utente SIES
		Utente utente = new Utente();
		utente.setNome(utenteSies.getNome());
		utente.setCognome(utenteSies.getCognome());
		utente.setIpServer(utenteSies.getIpServer());
		utente.setUsername(utenteSies.getUsername());
		utente.setUfficio(ufficio);
		return utente;
	}

	/**
	 * MEV 16: metodo di mappatura dati soggetto
	 *
	 * @param soggetto
	 * @param idSinonimo
	 * @return Anagrafica
	 * @throws DatatypeConfigurationException
	 */
	public static it.mig.sies.type.foglicomplementari.Anagrafica mapSoggettoFC(Soggetto soggetto,
			String idSinonimo) throws DatatypeConfigurationException {

		// MEV 16 CUMULO: aggiunti parametri di passaggio
		it.mig.sies.type.foglicomplementari.Anagrafica anagrafica = new it.mig.sies.type.foglicomplementari.Anagrafica();
		anagrafica.setCodiceFiscale(soggetto.getCodiceFiscale());
		anagrafica.setCodiceImprontaDigitale(soggetto.getCodiceAfis());
		anagrafica.setCodiceLuogoNascita(soggetto.getLuogoNascita());
		anagrafica.setCodiceStatoEsteroNascita(soggetto.getNazioneNascita());
		anagrafica.setDescrizioneComuneEstero(soggetto.getDescComuneEsteroNascita());
		anagrafica.setCognome(soggetto.getCognome());
		anagrafica.setDataNascita(evaluateDate(soggetto.getDataNascita()));
		anagrafica.setNome(soggetto.getNome());
		anagrafica.setNomePadre(soggetto.getPaternita());
		anagrafica.setSesso(soggetto.getSesso());

		it.mig.sies.type.foglicomplementari.ChiaviAnagrafica chiaviAnagrafica = new it.mig.sies.type.foglicomplementari.ChiaviAnagrafica();
		// MEV 16 CUMULO: aggiunto controllo
		if (PropertyUtil.isPresent(idSinonimo) && !idSinonimo.contains("REPLICA")) {
			if (idSinonimo.contains("#TFCCUM")) {
				String[] split = idSinonimo.split("#");
				chiaviAnagrafica.setNsc(BigInteger.valueOf(new Long(split[0]).longValue()));
			} else
				chiaviAnagrafica.setNsc(BigInteger.valueOf(new Long(idSinonimo).longValue()));
		} else
			chiaviAnagrafica.setNsc(BigInteger.valueOf(soggetto.getChiaveNSC()));
		chiaviAnagrafica.setSies(BigInteger.valueOf(soggetto.getChiaveSies()));

		anagrafica.setChiaviAnagrafica(chiaviAnagrafica);
		return anagrafica;
	}

	/**
	 * MEV 16: Crea un oggetto Durata in base ai parametri in input
	 *
	 * @param giorniDurata
	 * @param mesiDurata
	 * @param anniDurata
	 */
	private static it.mig.sies.type.foglicomplementari.Durata evaluateDurataPMFC(int giorniDurata,
			int mesiDurata, int anniDurata) {

		it.mig.sies.type.foglicomplementari.Durata durata = new it.mig.sies.type.foglicomplementari.Durata();
		durata.setGiorni(BigInteger.valueOf(giorniDurata));
		durata.setMesi(BigInteger.valueOf(mesiDurata));
		durata.setAnni(BigInteger.valueOf(anniDurata));
		return durata;
	}

	/**
	 * MEV 16: Effettua il mapping tra il titolo giudiziario estratto e la struttura dati da inviare
	 *
	 * @param titoloGiudiziario
	 * @param idSoggetto
	 * @param dao
	 * @param idSinonimo
	 * @return ProvvedimentoGiudiziario
	 * @throws DatatypeConfigurationException
	 */
	public static it.mig.sies.type.foglicomplementari.ProvvedimentoGiudiziario mapPGFC(
			TitoloGiudiziario titoloGiudiziario, long idSoggetto, SiesDAO dao, String idSinonimo)
			throws DatatypeConfigurationException {

		// MEV 16 CUMULO: aggiunti parametri di passaggio
		it.mig.sies.type.foglicomplementari.ProvvedimentoGiudiziario provvedimentoGiudiziario = new it.mig.sies.type.foglicomplementari.ProvvedimentoGiudiziario();

		if (titoloGiudiziario.getAnnoSentenza() != 0) {
			provvedimentoGiudiziario.setAnnoSentenza("" + titoloGiudiziario.getAnnoSentenza());
		}

		provvedimentoGiudiziario.setCodiceAutorita(titoloGiudiziario.getCodiceAutoritaCentrale());
		// Informazione non gestita
		provvedimentoGiudiziario.setCodiceSedeAutoritaPrincipale(null);
		provvedimentoGiudiziario
				.setCodiceSedeAutoritaPrincipaleDistaccata(titoloGiudiziario.getSedeAutorita());

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(titoloGiudiziario.getDataProvvedimento());
		provvedimentoGiudiziario
				.setDataProvvedimento(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		provvedimentoGiudiziario.setNumeroSentenza(titoloGiudiziario.getNumeroSentenza());

		// Questo flag indica se il provvedimento e' un cumulo di provvedimenti
		if ((titoloGiudiziario.getFlagCumulante() != null)
				&& ("S".equals(titoloGiudiziario.getFlagCumulante())))
			provvedimentoGiudiziario.setFlagCumulante(true);
		else
			provvedimentoGiudiziario.setFlagCumulante(false);

		it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario = new it.mig.sies.type.foglicomplementari.ChiaviProvvedimentoGiudiziario();
		chiaviProvvedimentoGiudiziario.setNsc(titoloGiudiziario.getChiaveNSC());
		chiaviProvvedimentoGiudiziario.setSies(titoloGiudiziario.getChiaveSies());
		provvedimentoGiudiziario.setChiaviProvvedimentoGiudiziario(chiaviProvvedimentoGiudiziario);

		// MEV 16 CUMULO: recupero nuovi dati
		// Soggetto soggetto = null;
		// if (PropertyUtil.isPresent(titoloGiudiziario.getChiaveAnagraficaSies())) {
		// // trattasi di cumulo
		// soggetto = dao.getSoggettoByID(titoloGiudiziario.getChiaveAnagraficaSies().toString());
		// provvedimentoGiudiziario.setAnagrafica(mapSoggettoFC(soggetto, idSinonimo));
		// } else {
		// // trattasi di a.e.p. oppure s.p.
		// soggetto = dao.getSoggettoByID("" + idSoggetto);
		// provvedimentoGiudiziario.setAnagrafica(mapSoggettoFC(soggetto, ""));
		// }

		// valore di ritorno
		return provvedimentoGiudiziario;
	}

	/**
	 * MEV 16: Effettua il mapping relativo alla response tornata dal webservice, popolando la risposta con i
	 * dati del soggetto,titolo principale ed esecutivo
	 *
	 * @param idEvento
	 * @param operazione
	 * @param responseData
	 * @param dao
	 * @param soggetto
	 * @return it.mig.sies.model.ResponseData
	 */
	public static it.mig.sies.model.ResponseData mapResponseDataFC(String idEvento, String operazione,
			it.mig.sies.type.foglicomplementari.ResponseData responseData, SiesDAO dao, Soggetto soggetto) {

		it.mig.sies.model.ResponseData response = new it.mig.sies.model.ResponseData();
		String esito = decodeEsitoFC(operazione, responseData);
		response.setEsito(esito);
		// MEV 16 CUMULO: aggiunta impostazione di proprietÃ 
		// ApplicationProperties properties = ApplicationProperties.getIstance();
		// if (PropertyUtil.isPresent(responseData.getProvvedimentoNSC())
		// && PropertyUtil.isPresent(responseData.getProvvedimentoNSC().getEsito().getCodice())) {
		// String codEsito = responseData.getProvvedimentoNSC().getEsito().getCodice().value();
		// response.setCodiceEsito(codEsito);
		// response.setDescCodiceEsito(properties.getProperty(SUFFISSO_MESSAGGI + codEsito));
		// } else {
		response.setCodiceEsito(responseData.getEsito().getCodice().value());
		response.setDescCodiceEsito(esito);
		// }
		// Nel caso in cui l'esito ha un codice numerico della classe 2XX
		// il trasferimento e' avvenuto con successo, quindi vengono settate
		// le informazioni da visualizzare all'utente
		if (responseData.getEsito().getCodice().value().startsWith("2")) {
			response.setCompleted(true);
			response.setChiaveNSC(responseData.getChiaviProvvedimentoEsecutivo().getNsc().longValue());
			response.setChiaveSies(responseData.getChiaviProvvedimentoEsecutivo().getSies().longValue());
			response.setEstratto(responseData.getEstratto());
		} else {
			response.setChiaveSies(Long.parseLong(idEvento));
			if ("405".equals(responseData.getEsito().getCodice().value()))
				response.setCompleted(true);
			else
				response.setCompleted(false);
			if (PropertyUtil.isPresent(responseData.getEstratto()))
				response.setEstratto(responseData.getEstratto());
			// SINONIMIA
			if (responseData.getArrayOmonimi() != null
					&& responseData.getArrayOmonimi().getOmonimo().size() > 0) {
				response.setElencoSinonimi(
						mapOmonimoToSinonimoFC(responseData.getArrayOmonimi().getOmonimo(), dao, soggetto));
			}
			// MEV 16 CUMULO: aggiunte impostazioni elenco provvedimenti cumulabili e chiave NSC
			// if (responseData.getProvvedimentoNSC() != null
			// && responseData.getProvvedimentoNSC().getProvvedimentoGiudiziario() != null
			// && responseData.getProvvedimentoNSC().getProvvedimentoGiudiziario().size() > 0)
			// response.setElencoProvvedimentiNSC(mapProvvedimentoToTitoloFC(
			// responseData.getProvvedimentoNSC().getProvvedimentoGiudiziario(), dao, response));
			if (PropertyUtil.isPresent(responseData.getChiaviAnagrafica())
					&& PropertyUtil.isPresent(responseData.getChiaviAnagrafica().getNsc()))
				soggetto.setChiaveNSC(responseData.getChiaviAnagrafica().getNsc().longValue());
		}

		// valore di ritorno
		return response;
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di mappatura
	 *
	 * @param provvedimentiGiudiziari
	 * @param dao
	 * @param response
	 * @return List<TitoloGiudiziario>
	 */
	// private static List<TitoloGiudiziario> mapProvvedimentoToTitoloFC(
	// List<it.mig.sies.type.foglicomplementari.ProvvedimentoGiudiziario> provvedimentiGiudiziari,
	// SiesDAO dao, it.mig.sies.model.ResponseData response) {
	//
	// List<TitoloGiudiziario> ltg = new ArrayList<>();
	// for (it.mig.sies.type.foglicomplementari.ProvvedimentoGiudiziario pg : provvedimentiGiudiziari) {
	// TitoloGiudiziario tg = new TitoloGiudiziario();
	// if (PropertyUtil.isPresent(pg.getAnnoSentenza()))
	// tg.setAnnoSentenza(new Integer(pg.getAnnoSentenza()).intValue());
	// tg.setNumeroSentenza(pg.getNumeroSentenza());
	// if (PropertyUtil.isPresent(pg.getCodiceAutorita())) {
	// String codiceAutorita = pg.getCodiceAutorita();
	// tg.setCodiceAutoritaCentrale(codiceAutorita);
	// String ca = codiceAutorita;
	// List<String> codiciAutorita = Arrays.asList("024", "063");
	// if (ca.startsWith("0") && !codiciAutorita.contains(ca))
	// ca = ca.substring(1);
	// tg.setDescrizioneAutorita(dao.getDescAutorita(ca));
	// }
	// tg.setSedeAutorita(pg.getCodiceSedeAutoritaPrincipaleDistaccata());
	// tg.setDescrizioneSedeAutorita(
	// dao.getDescSedeAutorita(pg.getCodiceSedeAutoritaPrincipaleDistaccata()));
	// GregorianCalendar dp = pg.getDataProvvedimento().toGregorianCalendar();
	// tg.setDataProvvedimento(dp.getTime());
	// if (pg.getChiaviProvvedimentoGiudiziario() != null) {
	// tg.setChiaveNSC(pg.getChiaviProvvedimentoGiudiziario().getNsc());
	// tg.setChiaveSies(pg.getChiaviProvvedimentoGiudiziario().getSies());
	// }
	// if (PropertyUtil.isPresent(pg.getCodiceEsito()) && pg.getCodiceEsito().length() == 2) {
	// // il valore di ritorno Ã¨ del tipo "V1"
	// String stato = pg.getCodiceEsito().substring(0, 1);
	// String presente = pg.getCodiceEsito().substring(1);
	// tg.setPresenteSIC(mapResultProcedure(presente));
	// tg.setStatoTitoloEsecSIC(mapResultProcedure(stato));
	// tg.setCodiceEsito(pg.getCodiceEsito());
	// String alNomeDi = "";
	// if ("4".equals(presente) || "5".equals(presente)) {
	// if (PropertyUtil.isPresent(pg.getAnagrafica())) {
	// String soggetto = pg.getAnagrafica().getCognome() + " "
	// + pg.getAnagrafica().getNome();
	// String sesso = pg.getAnagrafica().getSesso();
	// if ("M".equals(sesso))
	// sesso = "nato";
	// else
	// sesso = "nata";
	// Date dn = pg.getAnagrafica().getDataNascita().toGregorianCalendar().getTime();
	// String dataNascita = PropertyUtil.getDateToString(dn, "dd/MM/yyyy");
	// String descNazioneNascita = "", luogoNascita = "";
	// descNazioneNascita = dao
	// .getDescNazione(pg.getAnagrafica().getCodiceStatoEsteroNascita());
	// String descComuneEsteroNascita = pg.getAnagrafica().getDescrizioneComuneEstero();
	// if ("03900".equals(pg.getAnagrafica().getCodiceStatoEsteroNascita())) {
	// luogoNascita = dao.getDescComune(pg.getAnagrafica().getCodiceLuogoNascita());
	// } else {
	// if (PropertyUtil.isPresent(descComuneEsteroNascita))
	// luogoNascita = descComuneEsteroNascita + "(" + descNazioneNascita + ")";
	// else
	// luogoNascita = descNazioneNascita;
	// }
	//
	// alNomeDi = soggetto + " " + sesso + " il " + dataNascita + " in " + luogoNascita;
	// }
	// // MEV 16 CUMULO: aggiunta impostazione variabile
	// if ("4".equals(presente))
	// response.setDescEsitoCumulo("REPLICA");
	// }
	// tg.setAlNomeDi(alNomeDi);
	// }
	//
	// // aggiungo alla lista
	// ltg.add(tg);
	// }
	//
	// // valore di ritorno
	// return ltg;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo per mappare i risultari della procedure di ricerca cumulo
	 *
	 * @param codEsito
	 * @return String
	 */
	// private static String mapResultProcedure(String codEsito) {
	//
	// ApplicationProperties properties = ApplicationProperties.getIstance();
	// String result = properties.getProperty(SUFFISSO_MESSAGGI + codEsito);
	//
	// // valore di ritorno
	// return result;
	// }

	/**
	 * MEV 16: metodo per la mappatura dei dati tra omonimo e sinonimo
	 *
	 * @param elencoOmonimi
	 * @param dao
	 * @param soggetto
	 * @return List<Sinonimo>
	 */
	private static List<Sinonimo> mapOmonimoToSinonimoFC(List<Omonimo> elencoOmonimi, SiesDAO dao,
			Soggetto soggetto) {

		List<Sinonimo> sinonimi = new ArrayList<>();
		for (Omonimo omonimo : elencoOmonimi) {
			Sinonimo sinonimo = new Sinonimo();
			// scrivo sempre nel campo CF (anche per gli stranieri anche se hanno il CI)
			// il cf ed il ci sono sempre scritti nel campo CF dal ws di ricerca sinonimi
			if (PropertyUtil.isPresent(omonimo.getAnagrafica().getCodiceFiscale())) {
				if (omonimo.getAnagrafica().getCodiceFiscale().length() > 7)
					sinonimo.setCodiceFiscaleSinonimo(omonimo.getAnagrafica().getCodiceFiscale());
				else
					sinonimo.setCodiceFiscaleSinonimo(omonimo.getAnagrafica().getCodiceImprontaDigitale());
			}
			sinonimo.setCodiceLuogoNascitaSinonimo(omonimo.getAnagrafica().getCodiceLuogoNascita());
			sinonimo.setCodiceNazioneNascitaSinonimo(omonimo.getAnagrafica().getCodiceStatoEsteroNascita());
			sinonimo.setCognomeSinonimo(omonimo.getAnagrafica().getCognome());
			GregorianCalendar gc = omonimo.getAnagrafica().getDataNascita().toGregorianCalendar();
			sinonimo.setDataNascitaSinonimo(gc.getTime());
			sinonimo.setDescComuneEsteroNascitaSinonimo(omonimo.getAnagrafica().getDescrizioneComuneEstero());
			String descLuogoNascitaSinonimo = "", descNazioneNascitaSinonimo = "";
			if ("03900".equals(omonimo.getAnagrafica().getCodiceStatoEsteroNascita()))
				try {
					descLuogoNascitaSinonimo = dao
							.getDescComune(omonimo.getAnagrafica().getCodiceLuogoNascita());
				} catch (Exception e) {
					descLuogoNascitaSinonimo = "-";
				}
			descNazioneNascitaSinonimo = dao
					.getDescNazione(omonimo.getAnagrafica().getCodiceStatoEsteroNascita());
			sinonimo.setDescLuogoNascitaSinonimo(descLuogoNascitaSinonimo);
			sinonimo.setDescNazioneNascitaSinonimo(descNazioneNascitaSinonimo);
			sinonimo.setIdSinonimo(omonimo.getAnagrafica().getChiaviAnagrafica().getNsc());
			sinonimo.setNomeSinonimo(omonimo.getAnagrafica().getNome());
			sinonimo.setPaternitaSinonimo(omonimo.getAnagrafica().getNomePadre());
			sinonimo.setSessoSinonimo(omonimo.getAnagrafica().getSesso());
			// certificato di controllo
			if (PropertyUtil.isPresent(omonimo.getCertificatoControllo())) {
				sinonimo.setCertificatoControlloSinonimo(omonimo.getCertificatoControllo());
				sinonimo.setIdCertificatoControlloSinonimo(omonimo.getCertificatoControllo().hashCode());
			}
			sinonimo.setFlagAliasRichiamoSinonimo(omonimo.getAnagrafica().getFlagAliasRichiamo());

			// comparazione soggetto - sinonimo
			sinonimo.setIsEqualCognome("" + soggetto.getCognome().equals(sinonimo.getCognomeSinonimo()));
			sinonimo.setIsEqualNome("" + soggetto.getNome().equals(sinonimo.getNomeSinonimo()));

			String descLNSoggetto = "", descLNSinonimo = "";
			if ("03900".equals(sinonimo.getCodiceNazioneNascitaSinonimo()))
				descLNSinonimo = sinonimo.getDescLuogoNascitaSinonimo();
			else {
				if (PropertyUtil.isPresent(sinonimo.getDescComuneEsteroNascitaSinonimo()))
					descLNSinonimo = sinonimo.getDescComuneEsteroNascitaSinonimo() + " "
							+ sinonimo.getDescNazioneNascitaSinonimo();
				else
					descLNSinonimo = sinonimo.getDescNazioneNascitaSinonimo();
			}
			if ("03900".equals(soggetto.getNazioneNascita()))
				descLNSoggetto = soggetto.getDescComuneNascita();
			else {
				if (PropertyUtil.isPresent(soggetto.getDescComuneEsteroNascita()))
					descLNSoggetto = soggetto.getDescComuneEsteroNascita() + " "
							+ soggetto.getDescNazioneNascita();
				else
					descLNSoggetto = soggetto.getDescNazioneNascita();
			}
			sinonimo.setIsEqualLuogoNascita("" + descLNSoggetto.equals(descLNSinonimo));

			if (soggetto.getDataNascita() != null && sinonimo.getDataNascitaSinonimo() != null)
				sinonimo.setIsEqualDataNascita(
						soggetto.getDataNascita().compareTo(sinonimo.getDataNascitaSinonimo()) == 0 ? "true"
								: "false");
			else if (soggetto.getDataNascita() == null && sinonimo.getDataNascitaSinonimo() == null)
				sinonimo.setIsEqualDataNascita("true");
			else
				sinonimo.setIsEqualDataNascita("false");

			sinonimo.setIsEqualSesso("" + soggetto.getSesso().equals(sinonimo.getSessoSinonimo()));

			if (soggetto.getPaternita() != null && sinonimo.getPaternitaSinonimo() != null)
				sinonimo.setIsEqualPaternita(
						"" + soggetto.getPaternita().equals(sinonimo.getPaternitaSinonimo()));
			else if (soggetto.getPaternita() == null && sinonimo.getPaternitaSinonimo() == null)
				sinonimo.setIsEqualPaternita("true");
			else
				sinonimo.setIsEqualPaternita("false");

			// per italiani CF, per stranieri CI
			if ("03900".equals(soggetto.getNazioneNascita())) {
				if (soggetto.getCodiceFiscale() != null && sinonimo.getCodiceFiscaleSinonimo() != null)
					sinonimo.setIsEqualCodiceFiscale(
							"" + soggetto.getCodiceFiscale().equals(sinonimo.getCodiceFiscaleSinonimo()));
				else if (soggetto.getCodiceFiscale() == null && sinonimo.getCodiceFiscaleSinonimo() == null)
					sinonimo.setIsEqualCodiceFiscale("true");
				else
					sinonimo.setIsEqualCodiceFiscale("false");
			} else {
				if (soggetto.getCodiceAfis() != null && sinonimo.getCodiceIdentificativoSinonimo() != null)
					sinonimo.setIsEqualCodiceFiscale(
							"" + soggetto.getCodiceAfis().equals(sinonimo.getCodiceIdentificativoSinonimo()));
				else if (soggetto.getCodiceAfis() == null
						&& sinonimo.getCodiceIdentificativoSinonimo() == null)
					sinonimo.setIsEqualCodiceFiscale("true");
				else
					sinonimo.setIsEqualCodiceFiscale("false");
			}

			// if (soggetto.getFlagAliasRichiamo() != null && sinonimo.getFlagAliasRichiamoSinonimo() != null)
			// sinonimo.setIsEqualFlagAliasRichiamo(""
			// + soggetto.getFlagAliasRichiamo().equals(sinonimo.getFlagAliasRichiamoSinonimo()));
			// else if (soggetto.getFlagAliasRichiamo() == null && sinonimo.getFlagAliasRichiamoSinonimo() ==
			// null)
			// sinonimo.setIsEqualFlagAliasRichiamo("true");
			// else
			// sinonimo.setIsEqualFlagAliasRichiamo("false");

			// aggiungo alla lista
			sinonimi.add(sinonimo);
		}

		// valore di ritorno
		return sinonimi;
	}

	/**
	 * MEV 16: metodo di decodifica
	 *
	 * @param operazione
	 * @param responseData
	 * @return String
	 */
	public static String decodeEsitoFC(String operazione,
			it.mig.sies.type.foglicomplementari.ResponseData responseData) {

		StringBuilder esito = new StringBuilder();
		ApplicationProperties properties = ApplicationProperties.getIstance();

		// Per questa tipologia di codici
		// deve essere specificato anche il tipo di operazione
		if (responseData.getEsito().getCodice().value().equals("204")
				|| responseData.getEsito().getCodice().value().equals("205")
				|| responseData.getEsito().getCodice().value().equals("206")
				|| responseData.getEsito().getCodice().value().equals("207")) {
			esito.append(properties.getProperty(SUFFISSO_MESSAGGI + operazione));
		}

		esito.append(properties.getProperty(SUFFISSO_MESSAGGI + responseData.getEsito().getCodice().value()));
		return esito.toString();
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param msc
	 * @return MisuraSicurezza
	 */
	// private static it.mig.sies.type.foglicomplementari.MisuraSicurezza mapDatiMS(MisuraSicurezzaCumulo msc)
	// {
	//
	// it.mig.sies.type.foglicomplementari.MisuraSicurezza ms = new
	// it.mig.sies.type.foglicomplementari.MisuraSicurezza();
	// ms.setCodiceTipoDurataMS(msc.getCodiceTipoDurataMS());
	// ms.setCodiceTipoMS(msc.getCodiceTipoMS());
	// ms.setNumeroAnniMS(new BigInteger("" + msc.getNumeroAnniMS()));
	// ms.setNumeroGiorniMS(new BigInteger("" + msc.getNumeroGiorniMS()));
	// ms.setNumeroMesiMS(new BigInteger("" + msc.getNumeroMesiMS()));
	//
	// // valore di ritorno
	// return ms;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param pac
	 * @return PenaAccessoria
	 */
	// private static PenaAccessoria mapDatiPA(PenaAccessoriaCumulo pac) {
	//
	// it.mig.sies.type.foglicomplementari.PenaAccessoria pa = new
	// it.mig.sies.type.foglicomplementari.PenaAccessoria();
	// pa.setCodiceTipoDurataPA(pac.getCodiceTipoDurataPA());
	// pa.setCodiceTipoPA(pac.getCodiceTipoPA());
	// pa.setNumeroAnniPA(new BigInteger("" + pac.getNumeroAnniPA()));
	// pa.setNumeroGiorniPA(new BigInteger("" + pac.getNumeroGiorniPA()));
	// pa.setNumeroMesiPA(new BigInteger("" + pac.getNumeroMesiPA()));
	//
	// boolean existDurata = PropertyUtil.isPresent(pa.getNumeroAnniPA())
	// || PropertyUtil.isPresent(pa.getNumeroMesiPA())
	// || PropertyUtil.isPresent(pa.getNumeroGiorniPA());
	//
	// if (existDurata)
	// pa.setCodiceTipoDurataPA("T");
	// if ("1".equals(pa.getCodiceTipoPA()) && existDurata)
	// pa.setCodiceTipoPA("10");
	//
	// // valore di ritorno
	// return pa;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param rgec
	 * @return RichiesteGEAnticipazioneEffetti
	 */
	// private static RichiesteGEAnticipazioneEffetti mapDatiRichiestaGE(RichiesteGECumulo rgec) {
	//
	// RichiesteGEAnticipazioneEffetti rgeae = new RichiesteGEAnticipazioneEffetti();
	// rgeae.setArresto(
	// evaluateDurataPMFC(rgec.getGiorniArresto(), rgec.getMesiArresto(), rgec.getAnniArresto()));
	// rgeae.setReclusione(evaluateDurataPMFC(rgec.getGiorniReclusione(), rgec.getMesiReclusione(),
	// rgec.getAnniReclusione()));
	// if (rgec.getImportoAmmenda() != 0)
	// rgeae.setImportoAmmenda(new BigDecimal(Double.valueOf(rgec.getImportoAmmenda()).toString()));
	// if (rgec.getImportoMulta() != 0)
	// rgeae.setImportoMulta(new BigDecimal(Double.valueOf(rgec.getImportoMulta()).toString()));
	// rgeae.setTipoRichiesta(rgec.getCodiceTipoRichiesta());
	//
	// // valore di ritorno
	// return rgeae;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param ssc
	 * @return SanzioniSostitutiveCumulo
	 */
	// private static SanzioniSostitutive mapDatiSanzioniSostitutiveCumulo(SanzioniSostitutiveCumulo ssc) {
	//
	// SanzioniSostitutive ss = new SanzioniSostitutive();
	// ss.setEspulsioneStato(evaluateDurataPMFC(ssc.getGiorniEspulsioneStato(), ssc.getMesiEspulsioneStato(),
	// ssc.getAnniEspulsioneStato()));
	// if (ssc.getImportoAmmenda() != 0)
	// ss.setImportoAmmenda(new BigDecimal(Double.valueOf(ssc.getImportoAmmenda()).toString()));
	// if (ssc.getImportoMulta() != 0)
	// ss.setImportoMulta(new BigDecimal(Double.valueOf(ssc.getImportoMulta()).toString()));
	// ss.setLavoroPubblicaUtilita(evaluateDurataPMFC(ssc.getGiorniLavoroPubblicaUtilita(),
	// ssc.getMesiLavoroPubblicaUtilita(), ssc.getAnniLavoroPubblicaUtilita()));
	// ss.setLibertaControllata(evaluateDurataPMFC(ssc.getGiorniLibertaControllata(),
	// ssc.getMesiLibertaControllata(), ssc.getAnniLibertaControllata()));
	// ss.setNumeroOreLPU(new BigInteger("" + ssc.getOreLavoroPubblicaUtilita()));
	// ss.setSemiDetenzione(evaluateDurataPMFC(ssc.getGiorniSemidetenzione(), ssc.getMesiSemidetenzione(),
	// ssc.getAnniSemidetenzione()));
	// ss.setTipoEspulsioneStato(ssc.getCodiceTipoEspulsioneStato());
	// ss.setTipoLPU(ssc.getCodTipoLavoroPubblicaUtilita());
	//
	// // valore di ritorno
	// return ss;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param sgpc
	 * @return SanzioniGiudicePace
	 */
	// private static SanzioniGiudicePace mapDatiSanzioniGPCumulo(SanzioniGPCumulo sgpc) {
	//
	// SanzioniGiudicePace sgp = new SanzioniGiudicePace();
	// sgp.setEspulsioneStato(evaluateDurataPMFC(sgpc.getGiorniEspulsioneStato(),
	// sgpc.getMesiEspulsioneStato(), sgpc.getAnniEspulsioneStato()));
	// sgp.setLavoroPubblicaUtilita(evaluateDurataPMFC(sgpc.getGiorniLavoroPubblicaUtilita(),
	// sgpc.getMesiLavoroPubblicaUtilita(), sgpc.getAnniLavoroPubblicaUtilita()));
	// sgp.setLavoroSostitutivo(evaluateDurataPMFC(sgpc.getGiorniLavoroSostitutivo(),
	// sgpc.getMesiLavoroSostitutivo(), sgpc.getAnniLavoroSostitutivo()));
	// sgp.setPermanenzaDomiciliare(evaluateDurataPMFC(sgpc.getGiorniPermanenzaDomiciliare(),
	// sgpc.getMesiPermanenzaDomiciliare(), sgpc.getAnniPermanenzaDomiciliare()));
	// sgp.setTipoEspulsioneStato(sgpc.getCodiceTipoEspulsioneStato());
	//
	// // valore di ritorno
	// return sgp;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param lac
	 * @return LiberazioneAnticipataCumulo
	 */
	// private static LiberazioneAnticipataConcessaDetrarreCumulo mapDatiLiberazioneAnticipataCumulo(
	// LiberazioneAnticipataCumulo lac) {
	//
	// LiberazioneAnticipataConcessaDetrarreCumulo lacdc = new LiberazioneAnticipataConcessaDetrarreCumulo();
	// lacdc.setGiorniLAIntegrazione(new BigInteger("" + lac.getGiorniLAIntegrazione()));
	// lacdc.setGiorniLAOrdinaria(new BigInteger("" + lac.getGiorniLAOrdinaria()));
	// lacdc.setGiorniLARisarcimento(new BigInteger("" + lac.getGiorniLARisarcimento()));
	// lacdc.setGiorniLASpeciale(new BigInteger("" + lac.getGiorniLASpeciale()));
	//
	// // valore di ritorno
	// return lacdc;
	// }

	/**
	 * MEV 16 CUMULO: aggiunto metodo di associazione model-type
	 *
	 * @param cppc
	 * @return ConversionePPCumulo
	 */
	// private static PenaConversionePenaPecuniaria mapDatiConversionePPCumulo(ConversionePPCumulo cppc) {
	//
	// PenaConversionePenaPecuniaria pcpp = new PenaConversionePenaPecuniaria();
	// pcpp.setLavoroSostitutivo(evaluateDurataPMFC(cppc.getGiorniLavoroSostitutivo(),
	// cppc.getMesiLavoroSostitutivo(), cppc.getAnniLavoroSostitutivo()));
	// pcpp.setLibertaControllata(evaluateDurataPMFC(cppc.getGiorniLibertaControllata(),
	// cppc.getMesiLibertaControllata(), cppc.getAnniLibertaControllata()));
	//
	// // valore di ritorno
	// return pcpp;
	// }

}