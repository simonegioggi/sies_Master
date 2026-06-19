/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.controller;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.controller.GenericController;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.util.TypeFactory;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.ATTO;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.AVVOCATOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATIPROCEDIMENTOOUTPUT;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.EVENTOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.FASCICOLOSIEP;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.FASCICOLOSIUS;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.MOVIMENTIUDIENZATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.NOTIFICATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.RIFERIMENTOFASCSIEPTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.SOGGETTOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.TENORETYPE;

/**
 * @author Gioggi
 */
public class AvvocaturaSiusController extends GenericController implements IAvvocaturaSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	private static final String T_FASCICOLOSIUS_OUT = "TY_FASCICOLO_SIUS";
	private static final String T_RIF_FASC_SIEP_OUT = "RC_RIFER_SIEP";
	private static final String T_AVVOCATI_OUT = "RC_AVVOCATO";
	private static final String T_UDIENZA_OUT = "RC_UDIENZA";
	private static final String T_PROVVEDIMENTI_OUT = "RC_PROVVEDIMENTI";
	private static final String T_ALTRI_ATTI_OUT = "RC_PROVVEDIMENTI";
	private static final String T_RIC_ISTR_OUT = "RC_RICH_ISTR";
	private static final String T_SOGGETTO = "TY_SOGGETTO";
	private static final String T_FASCICOLO_SIEP = "TY_FASCICOLO_SIEP";
	private static final String T_ATTO = "TY_ATTO";
	private static final String T_OGGETTO = "RC_OGGETTO";
	private static final String T_OGGETTO_STRALCIATO = "RC_OGGETTO";

	/**
	 * Metodo per la ricerca del dettaglio del procedimento
	 *
	 * @param codDistretto
	 * @param codTipoUfficio
	 * @param codFiscaleAvvocato
	 * @param annnoProcedimento
	 * @param numeroProcedimento
	 * @param codTipoUfficio
	 * @return DATIPROCEDIMENTOOUTPUT
	 * @throws F3BException
	 */
	public DATIPROCEDIMENTOOUTPUT callRicercaFascicoloSius(String codDistretto, String codTipoUfficio,
			String codFiscaleAvvocato, int annoProcedimento, int numeroProcedimento, String codUfficio)
			throws F3BException {// MEV_20_Avvocatura_SIES_Sede aggiunto parametro codUfficio

		// info per il log
		avvocaturaLogger.info(
				"Starting Point della classe: DettaglioProcedimentoSiusController, metodo: callRicercaFascicoloSius");

		Connection connection = null;
		CallableStatement stmt = null;
		DATIPROCEDIMENTOOUTPUT datiProcedimentoOutput = new DATIPROCEDIMENTOOUTPUT();
		String codiceErrore = "";
		String descrErrore = "";

		try {
			connection = getDBConnection();

			// definizione della procedura da chiamare
			// AVVO_SIUS_IN IN INP_AVV,
			// T_FASCICOLO_SIUS_OUT OUT TY_FASCICOLO_SIUS,
			// T_RIF_FASC_SIEP_OUT OUT RC_RIFER_SIEP,
			// T_AVVOCATI_OUT OUT RC_AVVOCATO,
			// T_UDIENZA_OUT OUT RC_UDIENZA,
			// T_PROVVEDIMENTI_OUT OUT RC_PROVVEDIMENTI,
			// T_ALTRI_ATTI_OUT OUT RC_PROVVEDIMENTI,
			// T_RIC_ISTR_OUT OUT RC_RICH_ISTR,
			// T_SOGGETTO OUT TY_SOGGETTO,
			// T_FASCICOLO_SIEP OUT TY_FASCICOLO_SIEP,
			// T_ATTO OUT TY_ATTO,
			// T_OGGETTO OUT RC_OGGETTO,
			// T_OGGETTO_STRALCIATO OUT RC_OGGETTO,
			// ERR OUT VARCHAR2,
			// DESC_ERRORE OUT VARCHAR2

			stmt = connection.prepareCall(
					"{call AVVOCATURA_SIUS.CERCA_FASIUS_PER_ESTREMI(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			// setto i parametri di input e output della procedura
			stmt.registerOutParameter(2, java.sql.Types.STRUCT, T_FASCICOLOSIUS_OUT);
			stmt.registerOutParameter(3, java.sql.Types.ARRAY, T_RIF_FASC_SIEP_OUT);
			stmt.registerOutParameter(4, java.sql.Types.ARRAY, T_AVVOCATI_OUT);
			stmt.registerOutParameter(5, java.sql.Types.ARRAY, T_UDIENZA_OUT);
			stmt.registerOutParameter(6, java.sql.Types.ARRAY, T_PROVVEDIMENTI_OUT); // provvedimenti
			stmt.registerOutParameter(7, java.sql.Types.ARRAY, T_ALTRI_ATTI_OUT); // altri provvedimenti
			stmt.registerOutParameter(8, java.sql.Types.ARRAY, T_RIC_ISTR_OUT);
			stmt.registerOutParameter(9, java.sql.Types.STRUCT, T_SOGGETTO);
			stmt.registerOutParameter(10, java.sql.Types.STRUCT, T_FASCICOLO_SIEP);
			stmt.registerOutParameter(11, java.sql.Types.STRUCT, T_ATTO);
			stmt.registerOutParameter(12, java.sql.Types.ARRAY, T_OGGETTO);
			stmt.registerOutParameter(13, java.sql.Types.ARRAY, T_OGGETTO_STRALCIATO);
			stmt.registerOutParameter(14, java.sql.Types.VARCHAR); // Error Code
			stmt.registerOutParameter(15, java.sql.Types.VARCHAR); // Error Descr

			TypeFactory.setSchemaName(connection.getMetaData().getUserName());

			// parametri di passaggio
			Object[] itemAtributes = new Object[] { Integer.valueOf(annoProcedimento),
					Integer.valueOf(numeroProcedimento), codTipoUfficio, codDistretto, codFiscaleAvvocato,
					// MEV_20_Avvocatura_SIES_Sede aggiunto parametro codUfficio
					codUfficio };

			// definisco la struttura dati di input
			Struct itemObject1 = connection.createStruct("INP_AVV", itemAtributes);
			// e la imposto
			stmt.setObject(1, itemObject1);

			// AVVOCATURA: calcolo tempo esecuzione query
			long millis = System.currentTimeMillis();
			// ESECUZIONE DELLA PROCECURA
			stmt.execute();
			// info per il log
			avvocaturaLogger.info(
					"########## Tempo di Esecuzione x Store Procedure 'AVVOCATURA_SIUS.CERCA_FASIUS_PER_ESTREMI': "
							+ (System.currentTimeMillis() - millis) + " (ms) ##########");

			// RECUPERO DEI PARAMETRI DI OUTPUT
			// su output devo settare gli oggetti:
			// FASCICOLO_SIUS -- 2
			// elencoRiferimentiFascicoliSIEP -- 3
			// elencoAvvocati -- 4
			// elencoMovimentiUdienza -- 5
			// elencoProvvedimenti -- 6
			// elencoAtti -- 7
			// elencoRichiesteIstruttorie -- 8
			// SOGGETTO -- 9
			// FASCICOLO_SIEP -- 10
			// ATTO -- 11
			// elenco oggetti -- 12
			// elenco oggetti stralciati -- 13
			// CODICE ERRORE -- 14
			// DESCRIZIONE ERRORE -- 15

			Struct fascicoloSius = (Struct) stmt.getObject(2);
			if (fascicoloSius != null) {
				datiProcedimentoOutput.setFASCICOLOSIUS((FASCICOLOSIUS) TypeFactory.create(fascicoloSius));
			}

			Array listaRiferSIEP = (Array) stmt.getObject(3);
			if (listaRiferSIEP != null) {
				Object[] children = (Object[]) listaRiferSIEP.getArray();
				RIFERIMENTOFASCSIEPTYPE[] elencoRiferimentiFascicoliSIEP = new RIFERIMENTOFASCSIEPTYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					elencoRiferimentiFascicoliSIEP[i] = (RIFERIMENTOFASCSIEPTYPE) TypeFactory
							.create((Struct) children[i]);
				}
				List<RIFERIMENTOFASCSIEPTYPE> list = Arrays.asList(elencoRiferimentiFascicoliSIEP);
				datiProcedimentoOutput.getElencoRiferimentiFascicoliSIEP().addAll(list);
			}

			Array avvocati = (Array) stmt.getObject(4);
			if (avvocati != null) {
				Object[] children = (Object[]) avvocati.getArray();
				AVVOCATOTYPE[] listaAvvocati = new AVVOCATOTYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					listaAvvocati[i] = (AVVOCATOTYPE) TypeFactory.create((Struct) children[i]);
				}
				List<AVVOCATOTYPE> list = Arrays.asList(listaAvvocati);
				datiProcedimentoOutput.getListaAvvocati().addAll(list);
			}

			Array udienze = (Array) stmt.getObject(5);
			if (udienze != null) {
				Object[] children = (Object[]) udienze.getArray();
				MOVIMENTIUDIENZATYPE[] elencoMovimentiUdienza = new MOVIMENTIUDIENZATYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					elencoMovimentiUdienza[i] = TypeFactory.createMovimentiUdienza((Struct) children[i]);
				}
				List<MOVIMENTIUDIENZATYPE> list = Arrays.asList(elencoMovimentiUdienza);
				datiProcedimentoOutput.getElencoMovimentiUdienza().addAll(list);
			}

			Array provvedimenti = (Array) stmt.getObject(6);
			if (provvedimenti != null) {
				Object[] children = (Object[]) provvedimenti.getArray();
				EVENTOTYPE[] elencoProvvedimenti = new EVENTOTYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					elencoProvvedimenti[i] = (EVENTOTYPE) TypeFactory.create((Struct) children[i]);
				}
				List<EVENTOTYPE> list = Arrays.asList(elencoProvvedimenti);
				datiProcedimentoOutput.getElencoProvvedimenti().addAll(list);
			}

			Array atti = (Array) stmt.getObject(7);
			if (atti != null) {
				Object[] children = (Object[]) atti.getArray();
				EVENTOTYPE[] elencoAtti = new EVENTOTYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					elencoAtti[i] = (EVENTOTYPE) TypeFactory.create((Struct) children[i]);
				}
				List<EVENTOTYPE> list = Arrays.asList(elencoAtti);
				datiProcedimentoOutput.getElencoAtti().addAll(list);
			}

			Array richiesteIstru = (Array) stmt.getObject(8);
			if (atti != null) {
				Object[] children = (Object[]) richiesteIstru.getArray();
				NOTIFICATYPE[] elencoRichiesteIstruttorie = new NOTIFICATYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					elencoRichiesteIstruttorie[i] = (NOTIFICATYPE) TypeFactory.create((Struct) children[i]);
				}
				List<NOTIFICATYPE> list = Arrays.asList(elencoRichiesteIstruttorie);
				datiProcedimentoOutput.getElencoRichiesteIstruttorie().addAll(list);
			}

			Struct soggetto = (Struct) stmt.getObject(9);
			if (soggetto != null) {
				datiProcedimentoOutput.setSOGGETTO((SOGGETTOTYPE) TypeFactory.create(soggetto));
			}

			Struct fascicoloSiep = (Struct) stmt.getObject(10);
			if (fascicoloSiep != null) {
				datiProcedimentoOutput.setFASCICOLOSIEP((FASCICOLOSIEP) TypeFactory.create(fascicoloSiep));
			}

			Struct atto = (Struct) stmt.getObject(11);
			if (atto != null) {
				datiProcedimentoOutput.setATTO((ATTO) TypeFactory.create(atto));
			}

			Array oggetti = (Array) stmt.getObject(12);
			if (oggetti != null) {
				Object[] children = (Object[]) oggetti.getArray();
				TENORETYPE[] listaOggetti = new TENORETYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					listaOggetti[i] = (TENORETYPE) TypeFactory.create((Struct) children[i]);
				}
				List<TENORETYPE> list = Arrays.asList(listaOggetti);
				datiProcedimentoOutput.getOggetto().addAll(list);
			}

			Array oggettiStralciati = (Array) stmt.getObject(13);
			if (oggettiStralciati != null) {
				Object[] children = (Object[]) oggettiStralciati.getArray();
				TENORETYPE[] listaOggettiStralciati = new TENORETYPE[children.length];
				for (int i = 0; i < children.length; i++) {
					listaOggettiStralciati[i] = (TENORETYPE) TypeFactory.create((Struct) children[i]);
				}
				List<TENORETYPE> list = Arrays.asList(listaOggettiStralciati);
				datiProcedimentoOutput.getOggettiStralciati().addAll(list);
			}

			// gestione del messaggio di errore di output
			codiceErrore = stmt.getString(14);
			descrErrore = stmt.getString(15);
			String descErrore = (PropertyUtil.isPresent(descrErrore)) ? ": " + descrErrore : "";
			if ("OK".equals(codiceErrore))
				codiceErrore = "000";
			else {
				if ("NF".equals(codiceErrore))
					codiceErrore = "018";
				else
					codiceErrore = "009";
			}
			String descrizioneErrore = AvvocaturaProperties
					.getProperty(AvvocaturaProperties.PREFISSO_ERRORI.concat(codiceErrore));
			// info per il log
			avvocaturaLogger.info("Errore tornato dalla Procedura 'CERCA_FASIUS_PER_ESTREMI': " + codiceErrore
					+ "; " + descrizioneErrore + descErrore);

			// imposto l'ERRORE
			datiProcedimentoOutput.setERRORE(Mapper.mapErroreProcedimento(codiceErrore, ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore Imprevisto: " + e.getMessage());
			throw new F3BException(e.getMessage());
			// fvender 02/08/2018: aggiunto blocco finally per la gestione della problematica della chiusura
			// delle connessioni
		} finally {
			try {
				if (stmt != null && !stmt.isClosed()) {
					stmt.close();
				}
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException ex) {
				avvocaturaLogger.error("Errore nella chiusura: " + ex.getMessage());
				throw new F3BException(ex.getMessage());
			}
		}

		// valore di ritorno
		return datiProcedimentoOutput;
	}

}