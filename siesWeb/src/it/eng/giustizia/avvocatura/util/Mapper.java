/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DURATATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.ERRORE;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * @author Gioggi
 */
public class Mapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'impostazione dell'errore sul decreto
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static ERRORE mapErroreDecreto(String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreDecreto");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		ERRORE errore = new ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione dell'errore sul procedimento
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.ERRORE mapErroreProcedimento(
			String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreProcedimento");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione del tipo data decreto
	 * 
	 * @param data
	 * @return DATATYPE
	 */
	public static DATATYPE creaDataTypeDecreto(Date data) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeDecreto");

		DATATYPE dt = new DATATYPE();
		if (data != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione del tipo durata decreto
	 * 
	 * @param numeroGiorni
	 * @param numeroMesi
	 * @param numeroOre
	 * @return DURATATYPE
	 */
	public static DURATATYPE creaDurataTypeDecreto(BigDecimal numeroGiorni, BigDecimal numeroMesi, BigDecimal numeroAnni,
			BigDecimal numeroOre) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDurataTypeDecreto");

		DURATATYPE dt = new DURATATYPE();
		if (numeroGiorni != null)
			dt.setGiorni(numeroGiorni.toBigInteger());
		else
			dt.setGiorni(null);
		if (numeroMesi != null)
			dt.setMesi(numeroMesi.toBigInteger());
		else
			dt.setMesi(null);
		if (numeroAnni != null)
			dt.setAnni(numeroAnni.toBigInteger());
		else
			dt.setAnni(null);
		if (numeroOre != null)
			dt.setOre(numeroOre.toBigInteger());
		else
			dt.setOre(null);

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione dell'errore sull'ordinanza
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.ERRORE mapErroreOrdinanza(
			String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreOrdinanza");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione del tipo data ordinanza
	 * 
	 * @param data
	 * @return DATATYPE
	 */
	public static it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATATYPE creaDataTypeOrdinanza(
			Date data) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeOrdinanza");

		it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATATYPE dt = new it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATATYPE();
		if (data != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione del tipo durata ordinanza
	 * 
	 * @param numeroGiorni
	 * @param numeroMesi
	 * @param numeroAnni
	 * @return DURATATYPE
	 */
	public static it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DURATATYPE creaDurataTypeOrdinanza(
			BigDecimal numeroGiorni, BigDecimal numeroMesi, BigDecimal numeroAnni) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDurataTypeOrdinanza");

		it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DURATATYPE dt = new it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DURATATYPE();
		if (numeroGiorni != null)
			dt.setGiorni(numeroGiorni.toBigInteger());
		else
			dt.setGiorni(null);
		if (numeroMesi != null)
			dt.setMesi(numeroMesi.toBigInteger());
		else
			dt.setMesi(null);
		if (numeroAnni != null)
			dt.setAnni(numeroAnni.toBigInteger());
		else
			dt.setAnni(null);

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione del tipo data per i soggetti con procedimenti
	 * 
	 * @param dataNascita
	 * @return DATATYPE
	 */
	public static it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATATYPE creaDataTypeSoggettiConProcedimenti(
			Date dataNascita) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeSoggettiConProcedimenti");

		it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATATYPE dt = new it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATATYPE();
		if (dataNascita != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataNascita);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione dell'errore sull'elenco dei procedimenti
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.ERRORE mapErroreElencoProcedimenti(
			String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreProcedimento");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione della data sull'elenco dei procedimenti
	 * 
	 * @param data
	 * @return DATATYPE
	 */
	public static it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATATYPE creaDataTypeElencoProcedimenti(
			Date data) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeElencoProcedimenti");

		it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATATYPE dt = new it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATATYPE();
		if (data != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione dell'errore sul rinvio udienza
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.ERRORE mapErroreRinvioUdienza(
			String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreRinvioUdienza");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione del tipo data rinvio udienza
	 * 
	 * @param data
	 * @return DATATYPE
	 */
	public static it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATATYPE creaDataTypeRinvioUdienza(
			Date data) {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: creaDataTypeRinvioUdienza");

		it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATATYPE dt = new it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATATYPE();
		if (data != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			dt.setGiorno(gc.get(Calendar.DAY_OF_MONTH));
			dt.setMese(gc.get(Calendar.MONTH) + 1);
			dt.setAnno(gc.get(Calendar.YEAR));
		}

		// valore di ritorno
		return dt;
	}

	/**
	 * Metodo per l'impostazione dell'errore sulla richiesta della stampa
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE mapErroreStampaRichiesta(
			String codiceErrore, String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreStampaRichiesta");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione dell'errore sugli avvisi
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.ERRORE mapErroreAvviso(String codiceErrore,
			String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreAvviso");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

	/**
	 * Metodo per l'impostazione dell'errore sulla richiesta della stampa
	 * 
	 * @param codiceErrore
	 * @param eccezione
	 * @return ERRORE
	 * @throws F3BException
	 */
	public static it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE mapErroreRichiestaStampa(String codiceErrore,
			String eccezione) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: Mapper, metodo: mapErroreRichiestaStampa");

		// istanzio ed inizializzo un oggetto di tipo "ERRORE"
		it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE errore = new it.eng.giustizia.avvocatura.ws.type.richiestaStampa.ERRORE();
		String descrErrore = AvvocaturaProperties.getProperty(AvvocaturaProperties.PREFISSO_ERRORI
				.concat(codiceErrore));
		errore.setCODICEERRORE(codiceErrore);
		errore.setDESCRERRORE(descrErrore + eccezione);

		// valore di ritorno
		return errore;
	}

}