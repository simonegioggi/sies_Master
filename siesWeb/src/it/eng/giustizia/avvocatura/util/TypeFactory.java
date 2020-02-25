package it.eng.giustizia.avvocatura.util;

import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.ATTO;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.AVVOCATOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.DATATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.EVENTOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.FASCICOLOSIEP;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.FASCICOLOSIUS;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.IMPUGNAZIONETYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.MAGISTRATO;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.MOVIMENTIUDIENZATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.NOTETYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.NOTIFICATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.PENATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.RESIDENZATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.RIFERIMENTOFASCSIEPTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.SENTENZATYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.SOGGETTOTYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.TENORETYPE;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.UDIENZA;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TypeFactory {

	public static String schemaName = "";

	public static void setSchemaName(String nomeSchema) {
		schemaName = nomeSchema + ".";
	}

	/**
	 * Metodo che controlla e chiama il metodo di create opportuno, in base allla tipologia della struttura in
	 * ingresso
	 *
	 * @param struct
	 *            Struttura sql da convertire in oggetto java
	 * @return Oggetto java ottenuto dalla Struct oracle passato in input
	 * @throws java.sql.SQLException
	 *             L'eccezione sollevata qualora si riscontri un fallimento nelle chiamate sql
	 */
	public static Object create(java.sql.Struct struct) throws java.sql.SQLException {

		if (struct == null)
			throw new IllegalArgumentException("Failed to create type");

		if ((schemaName + "TY_SENTENZA").equalsIgnoreCase(struct.getSQLTypeName())) {
			SENTENZATYPE model = createSentenza(struct);
			return model;
		} else if ((schemaName + "TY_PENA").equalsIgnoreCase(struct.getSQLTypeName())) {
			PENATYPE model = createPena(struct);
			return model;
		} else if ((schemaName + "TY_OGGETTO").equalsIgnoreCase(struct.getSQLTypeName())) {
			TENORETYPE model = createTenore(struct);
			return model;
		} else if ((schemaName + "TY_ATTO").equalsIgnoreCase(struct.getSQLTypeName())) {
			ATTO model = createAtto(struct);
			return model;
		} else if ((schemaName + "TY_LISTA_NOTE").equalsIgnoreCase(struct.getSQLTypeName())) {
			NOTETYPE model = createNote(struct);
			return model;
		} else if ((schemaName + "TY_MAGISTRATO").equalsIgnoreCase(struct.getSQLTypeName())) {
			MAGISTRATO model = createMagistrato(struct);
			return model;
		} else if ((schemaName + "TY_RESIDENZA").equalsIgnoreCase(struct.getSQLTypeName())) {
			RESIDENZATYPE model = createResidenza(struct);
			return model;
		} else if ((schemaName + "TY_SOGGETTO").equalsIgnoreCase(struct.getSQLTypeName())) {
			SOGGETTOTYPE model = createSoggetto(struct);
			return model;
		} else if ((schemaName + "TY_AVVOCATO").equalsIgnoreCase(struct.getSQLTypeName())) {
			AVVOCATOTYPE model = createAvvocato(struct);
			return model;
		} else if ((schemaName + "TY_FASCICOLO_SIEP").equalsIgnoreCase(struct.getSQLTypeName())) {
			FASCICOLOSIEP model = createFascicoloSiep(struct);
			return model;
		} else if ((schemaName + "TY_FASCICOLO_SIUS").equalsIgnoreCase(struct.getSQLTypeName())) {
			FASCICOLOSIUS model = createFascicoloSius(struct);
			return model;
		} else if ((schemaName + "TY_RIFER_SIEP").equalsIgnoreCase(struct.getSQLTypeName())) {
			RIFERIMENTOFASCSIEPTYPE model = createRiferimentiSiep(struct);
			return model;
		} else if ((schemaName + "TY_IMPUGNAZIONI").equalsIgnoreCase(struct.getSQLTypeName())) {
			IMPUGNAZIONETYPE model = createImpugnazione(struct);
			return model;
		} else if ((schemaName + "TY_PROVVEDIMENTI").equalsIgnoreCase(struct.getSQLTypeName())) {
			EVENTOTYPE model = createEvento(struct);
			return model;
		} else if ((schemaName + "TY_RICH_ISTR").equalsIgnoreCase(struct.getSQLTypeName())) {
			NOTIFICATYPE model = createNotifica(struct);
			return model;
		} else
			throw new IllegalArgumentException(struct.getSQLTypeName());
	}

	public static MOVIMENTIUDIENZATYPE createMovimentiUdienza(Struct struct) throws SQLException {

		// DATA_UDIENZA DATE,
		// DATA_CAMERA_CONSIGLIO DATE,
		// FLAG_RINVIATA VARCHAR2(1),
		// DESC_PRESIDENTE VARCHAR2(144),
		// DESC_TIPO_OPERAZIONE VARCHAR2(30),
		// DATA_INSERIMENTO DATE,
		// DATA_MODIFICA DATE

		final int DATA_UDIENZA = 0;
		final int DATA_CAMERA_CONSIGLIO = DATA_UDIENZA + 1;
		final int FLAG_RINVIATA = DATA_CAMERA_CONSIGLIO + 1;
		final int DESC_PRESIDENTE = FLAG_RINVIATA + 1;
		final int DESC_TIPO_OPERAZIONE = DESC_PRESIDENTE + 1;
		final int DATA_INSERIMENTO = DESC_TIPO_OPERAZIONE + 1;
		final int DATA_MODIFICA = DATA_INSERIMENTO + 1;

		Object[] attributes = struct.getAttributes();
		MOVIMENTIUDIENZATYPE movimentiUdienza = new MOVIMENTIUDIENZATYPE();

		Date data = (java.util.Date) attributes[DATA_UDIENZA];
		if (data != null) {
			movimentiUdienza.setDataUdienza(createDataType((java.util.Date) attributes[DATA_UDIENZA]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_OPERAZIONE])) {
			movimentiUdienza.setTipoOperazione((String) attributes[DESC_TIPO_OPERAZIONE]);
		}

		Date dataIns = (java.util.Date) attributes[DATA_INSERIMENTO];
		if (dataIns != null) {
			movimentiUdienza
					.setDataInserimento(createDataType((java.util.Date) attributes[DATA_INSERIMENTO]));
		}

		Date dataMod = (java.util.Date) attributes[DATA_MODIFICA];
		if (dataMod != null) {
			movimentiUdienza.setDataModifica(createDataType((java.util.Date) attributes[DATA_MODIFICA]));
		}

		return movimentiUdienza;
	}

	/**
	 * @param struct
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static NOTIFICATYPE createNotifica(Struct struct) throws java.sql.SQLException {
		// DESC_NOTIFICA VARCHAR(100),
		// DESC_DESTINATARIO VARCHAR(100),
		// DATA_INVIO DATE,
		// DATA_AVVENUTA_NOTIFICA DATE
		final int DESC_NOTIFICA = 0;
		final int DESC_DESTINATARIO = DESC_NOTIFICA + 1;
		final int DATA_INVIO = DESC_DESTINATARIO + 1;
		final int DATA_AVVENUTA_NOTIFICA = DATA_INVIO + 1;

		Object[] attributes = struct.getAttributes();

		NOTIFICATYPE notifica = new NOTIFICATYPE();
		// valorizzazione delle propietà del modello notifica
		if (PropertyUtil.isPresent((String) attributes[DESC_NOTIFICA])) {
			notifica.setMDescrizione((String) attributes[DESC_NOTIFICA]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_DESTINATARIO])) {
			notifica.setMDestinatario((String) attributes[DESC_DESTINATARIO]);
		}

		Date data = (java.util.Date) attributes[DATA_INVIO];
		if (data != null) {
			notifica.setMDataInvio(createDataType((java.util.Date) attributes[DATA_INVIO]));
		}

		data = (java.util.Date) attributes[DATA_AVVENUTA_NOTIFICA];
		if (data != null) {
			notifica.setMDataInvio(createDataType((java.util.Date) attributes[DATA_AVVENUTA_NOTIFICA]));
		}

		return notifica;
	}

	/**
	 * Metodo che restituisce un oggetto DATATYPE a partire da un oggetto java.util.Date
	 * 
	 * @param dataInput
	 * @return
	 */
	private static DATATYPE createDataType(Date dataInput) {
		DATATYPE data = new DATATYPE();
		if (dataInput != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(dataInput);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			data.setAnno(year);
			data.setMese(month);
			data.setGiorno(day);
		}
		return data;
	}

	private static EVENTOTYPE createEvento(Struct struct) throws java.sql.SQLException {
		// ID_EVENTO NUMBER,
		// DATA_EMISSIONE DATE,
		// DESC_TIPO_PROVV VARCHAR(100),
		// DESC_MOTIVO VARCHAR(100),
		// DESC_ESITO VARCHAR(100),
		// DATA_DEPOSITO DATE,
		// ID_TIPO_PROVV VARCHAR2(4) ,
		// ID_EVENTO_REVOCA NUMBER,
		// LISTA_OPPOS_IMP RC_IMPUGNAZIONI,
		// FLAG_DOC_REGISTRATO VARCHAR2(1),
		// FLAG_DEPOSITO_VALIDATO VARCHAR2(2)

		final int ID_EVENTO = 0;
		final int DATA_EMISSIONE = ID_EVENTO + 1;
		final int DESC_TIPO_PROVV = DATA_EMISSIONE + 1;
		final int DESC_MOTIVO = DESC_TIPO_PROVV + 1;
		final int DESC_ESITO = DESC_MOTIVO + 1;
		final int DATA_DEPOSITO = DESC_ESITO + 1;
		final int ID_TIPO_PROVV = DATA_DEPOSITO + 1;
		final int ID_EVENTO_REVOCA = ID_TIPO_PROVV + 1;
		final int LISTA_OPPOS_IMP = ID_EVENTO_REVOCA + 1;
		final int FLAG_DOC_REGISTRATO = LISTA_OPPOS_IMP + 1;
		final int FLAG_DEPOSITO_VALIDATO = FLAG_DOC_REGISTRATO + 1;
		final int ALTRE_INFO = FLAG_DEPOSITO_VALIDATO + 1;

		Object[] attributes = struct.getAttributes();

		EVENTOTYPE evento = new EVENTOTYPE();		
		
		if (PropertyUtil.isPresent((Number) attributes[ID_EVENTO])) {
			Number value = (Number) attributes[ID_EVENTO];
			evento.setIdEvento(BigInteger.valueOf(value.longValue()));
		}

		Date data = (java.util.Date) attributes[DATA_EMISSIONE];
		if (data != null) {
			evento.setDataEmissione(createDataType((java.util.Date) attributes[DATA_EMISSIONE]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_PROVV])) {
			evento.setDescrTipoProvvedimento((String) attributes[DESC_TIPO_PROVV]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_MOTIVO])) {
			evento.setDescrMotivo((String) attributes[DESC_MOTIVO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_ESITO])) {
			evento.setDescrEsito((String) attributes[DESC_ESITO]);
		}

		data = (java.util.Date) attributes[DATA_DEPOSITO];
		if (data != null) {
			evento.setDataDeposito(createDataType((java.util.Date) attributes[DATA_DEPOSITO]));
		}

		if (PropertyUtil.isPresent((String) attributes[ID_TIPO_PROVV])) {
			evento.setCodTipoProvvedimento((String) attributes[ID_TIPO_PROVV]);
		}

		if (PropertyUtil.isPresent((Number) attributes[ID_EVENTO_REVOCA])) {
			evento.setEventoIdEventoRevoca(((Number) attributes[ID_EVENTO_REVOCA]).toString());
		}

		// lista di impugnazioni o opposizioni

		Array array = (Array) attributes[LISTA_OPPOS_IMP];
		if (array != null) {
			Object[] children = (Object[]) array.getArray();
			IMPUGNAZIONETYPE[] impugnazioni = new IMPUGNAZIONETYPE[children.length];
			for (int i = 0; i < children.length; i++) {
				impugnazioni[i] = (IMPUGNAZIONETYPE) TypeFactory.create((Struct) children[i]);
			}
			// evento.setElencoImpugnazioniOpposizioni(impugnazioni);
			List<IMPUGNAZIONETYPE> list = Arrays.asList(impugnazioni);
			evento.getElencoImpugnazioniOpposizioni().addAll(list);				
		}
		if (PropertyUtil.isPresent((String) attributes[FLAG_DOC_REGISTRATO])) {
			evento.setFlagDocumentoRegistrato((String) attributes[FLAG_DOC_REGISTRATO]);
		}
		if (PropertyUtil.isPresent((String) attributes[FLAG_DEPOSITO_VALIDATO])) {
			evento.setFlagDepositoValidato((String) attributes[FLAG_DEPOSITO_VALIDATO]);
		}
		
		if (PropertyUtil.isPresent((String) attributes[ALTRE_INFO])) {
			evento.setAltreInformazioni((String) attributes[ALTRE_INFO]);
		}
		
		return evento;
	}

	/**
	 * @param struct
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static IMPUGNAZIONETYPE createImpugnazione(Struct struct) throws java.sql.SQLException {
		// FLAG_IO VARCHAR2(1),
		// DESC_TIPO_IMPUGNAZIONI VARCHAR2(100),
		// DATA_RISCORSO DATE

		Object[] attributes = struct.getAttributes();
		final int FLAG_IO = 0;
		final int DESC_TIPO_IMPUGNAZIONI = FLAG_IO + 1;
		final int DATA_RISCORSO = DESC_TIPO_IMPUGNAZIONI + 1;

		IMPUGNAZIONETYPE impugnazione = new IMPUGNAZIONETYPE();

		if (PropertyUtil.isPresent((String) attributes[FLAG_IO])) {
			impugnazione.setFlagTipo((String) attributes[FLAG_IO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_IMPUGNAZIONI])) {
			impugnazione.setDescrTipoImpugnazione((String) attributes[DESC_TIPO_IMPUGNAZIONI]);
		}

		Date data = (java.util.Date) attributes[DATA_RISCORSO];
		if (data != null) {
			impugnazione.setDataRicorso(createDataType((java.util.Date) attributes[DATA_RISCORSO]));
		}
		return impugnazione;
	}

	private static RIFERIMENTOFASCSIEPTYPE createRiferimentiSiep(Struct struct) throws java.sql.SQLException {
		// ID_RIF_FASC_SIEP NUMBER,
		// FLAG_MS VARCHAR2(1),
		// CHIAVE_ANNO NUMBER(4),
		// CHIAVE_PROG NUMBER,
		// DESC_TIPO_UFFICIO VARCHAR2(100),
		// DATA_PROVVEDIMENTO DATE,
		// DESC_TIPO_PROVV VARCHAR2(100),
		// DESC_TIPO_AUTOR_EMIT VARCHAR2(100),
		// DESC_LUOGO_AUT_EMIT VARCHAR2(72),
		// DATA_IRREV DATE,
		// ANNO_PROV NUMBER(4),
		// NUME_PROV VARCHAR2(6)
		final int ID_RIF_FASC_SIEP = 0;
		final int FLAG_MS = ID_RIF_FASC_SIEP + 1;
		final int CHIAVE_ANNO = FLAG_MS + 1;
		final int CHIAVE_PROG = CHIAVE_ANNO + 1;
		final int DESC_TIPO_UFFICIO = CHIAVE_PROG + 1;
		final int DATA_PROVVEDIMENTO = DESC_TIPO_UFFICIO + 1;
		final int DESC_TIPO_PROVV = DATA_PROVVEDIMENTO + 1;
		final int DESC_TIPO_AUTOR_EMIT = DESC_TIPO_PROVV + 1;
		final int DESC_LUOGO_AUT_EMIT = DESC_TIPO_AUTOR_EMIT + 1;
		final int DATA_IRREV = DESC_LUOGO_AUT_EMIT + 1;
		final int ANNO_PROV = DATA_IRREV + 1;
		final int NUME_PROV = ANNO_PROV + 1;

		Object[] attributes = struct.getAttributes();

		RIFERIMENTOFASCSIEPTYPE rifer = new RIFERIMENTOFASCSIEPTYPE();

		if (PropertyUtil.isPresent((Number) attributes[ID_RIF_FASC_SIEP])) {
			Number value = (Number) attributes[ID_RIF_FASC_SIEP];
			rifer.setIdRiferimentoFascicoloSiep(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((String) attributes[FLAG_MS])) {
			rifer.setFlagMS((String) attributes[FLAG_MS]);
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_ANNO])) {
			Number value = (Number) attributes[CHIAVE_ANNO];
			rifer.setAnnoFascicoloSiep(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_PROG])) {
			rifer.setProgrFascicoloSiep(attributes[CHIAVE_PROG].toString());
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_UFFICIO])) {
			rifer.setDescrUffFascicoloSiep((String) attributes[DESC_TIPO_UFFICIO]);
		}

		Date data = (java.util.Date) attributes[DATA_PROVVEDIMENTO];
		if (data != null) {
			rifer.setDataProvvedimento(createDataType((java.util.Date) attributes[DATA_PROVVEDIMENTO]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_PROVV])) {
			rifer.setDescrTipoProvvedimento((String) attributes[DESC_TIPO_PROVV]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_AUTOR_EMIT])) {
			rifer.setDescrTipoAutoritaEmittente((String) attributes[DESC_TIPO_AUTOR_EMIT]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_LUOGO_AUT_EMIT])) {
			rifer.setDescrLuogoEmittente((String) attributes[DESC_LUOGO_AUT_EMIT]);
		}

		data = (java.util.Date) attributes[DATA_IRREV];
		if (data != null) {
			rifer.setDataIrrevocabilita(createDataType((java.util.Date) attributes[DATA_IRREV]));
		}

		if (PropertyUtil.isPresent((Number) attributes[ANNO_PROV])) {
			Number value = (Number) attributes[ANNO_PROV];
			rifer.setAnnoProvvedimento(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((String) attributes[NUME_PROV])) {
			rifer.setNumeroProvvedimento((String) attributes[NUME_PROV]);
		}
		return rifer;

	}

	/**
	 * @param struct
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static FASCICOLOSIUS createFascicoloSius(Struct struct) throws java.sql.SQLException {

		final int ID_FASC_SIUS = 0;
		final int CHIAVE_PROG = ID_FASC_SIUS + 1;
		final int CHIAVE_ANNO = CHIAVE_PROG + 1;
		final int DATA_ISCRIZIONE = CHIAVE_ANNO + 1;
		final int DESC_POS_MATER = DATA_ISCRIZIONE + 1;
		final int DESC_STATO_FASCICOLO = DESC_POS_MATER + 1;
		final int DESC_FASC_UNIFICANTE = DESC_STATO_FASCICOLO + 1;
		final int DATA_DEFINIZIONE = DESC_FASC_UNIFICANTE + 1;
		final int DESC_DEFINIZIONE = DATA_DEFINIZIONE + 1;
		final int TIPO_DEFINZIONE = DESC_DEFINIZIONE + 1;
		final int DESCR_TIPO_REGISTRO = TIPO_DEFINZIONE + 1;
		final int CHIAVE_ANNO_S1 = DESCR_TIPO_REGISTRO + 1;
		final int CHIAVE_PROG_S1 = CHIAVE_ANNO_S1 + 1;
		final int DESC_FASC_UNIFICATI = CHIAVE_PROG_S1 + 1;
		final int DESC_FASC_COLLEGATI = DESC_FASC_UNIFICATI + 1;
		final int DESC_FASCICOLO_PADRE = DESC_FASC_COLLEGATI + 1;
		final int UDIENZA = DESC_FASCICOLO_PADRE + 1;
		final int DESC_CONTENUTO = UDIENZA + 1;
		final int MAGISTRATO = DESC_CONTENUTO + 1;
		final int DESC_CANCEL = MAGISTRATO + 1;
		final int DESC_ANNOT_PROCED = DESC_CANCEL + 1;
		final int NOTE = DESC_ANNOT_PROCED + 1;
		final int DESC_ULTER_ISTANZE = NOTE + 1;

		Object[] attributes = struct.getAttributes();
		FASCICOLOSIUS fascicoloSIUS = new FASCICOLOSIUS();
		Struct myStruct = null;

		if (PropertyUtil.isPresent((Number) attributes[ID_FASC_SIUS])) {
			Number value = (Number) attributes[ID_FASC_SIUS];
			fascicoloSIUS.setIdFascicoloSius(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_PROG])) {
			Number value = (Number) attributes[CHIAVE_PROG];
			fascicoloSIUS.setChiaveProg(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_ANNO])) {
			Number value = (Number) attributes[CHIAVE_ANNO];
			fascicoloSIUS.setChiaveAnno(BigInteger.valueOf(value.longValue()));
		}

		Date data = (java.util.Date) attributes[DATA_ISCRIZIONE];
		if (data != null) {
			fascicoloSIUS.setDataIscrizione(createDataType((java.util.Date) attributes[DATA_ISCRIZIONE]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_POS_MATER])) {
			fascicoloSIUS.setDescrPosizioneMateriale((String) attributes[DESC_POS_MATER]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_STATO_FASCICOLO])) {
			fascicoloSIUS.setDescrStatoFascicolo((String) attributes[DESC_STATO_FASCICOLO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_FASC_UNIFICANTE])) {
			fascicoloSIUS.setFascicoloUnificante((String) attributes[DESC_FASC_UNIFICANTE]);
		}

		data = (java.util.Date) attributes[DATA_DEFINIZIONE];
		if (data != null) {
			fascicoloSIUS.setDataDefinizione(createDataType((java.util.Date) attributes[DATA_DEFINIZIONE]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_DEFINIZIONE])) {
			fascicoloSIUS.setDescrDefinizione((String) attributes[DESC_DEFINIZIONE]);
		}

		if (PropertyUtil.isPresent((String) attributes[TIPO_DEFINZIONE])) {
			fascicoloSIUS.setTipoDefinizione((String) attributes[TIPO_DEFINZIONE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESCR_TIPO_REGISTRO])) {
			fascicoloSIUS.setDescrTipoRegistro((String) attributes[DESCR_TIPO_REGISTRO]);
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_ANNO_S1])) {
			Number value = (Number) attributes[CHIAVE_ANNO_S1];
			fascicoloSIUS.setAnnoS1(BigInteger.valueOf(value.longValue()).toString());
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_PROG_S1])) {
			Number value = (Number) attributes[CHIAVE_PROG_S1];
			fascicoloSIUS.setProgrS1(BigInteger.valueOf(value.longValue()).toString());
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_FASC_UNIFICATI])) {
			fascicoloSIUS.setElencoFascicoliUnificati((String) attributes[DESC_FASC_UNIFICATI]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_FASC_COLLEGATI])) {
			fascicoloSIUS.setElencoFascicoliCollegati((String) attributes[DESC_FASC_COLLEGATI]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_FASCICOLO_PADRE])) {
			fascicoloSIUS.setFascicoloPadre((String) attributes[DESC_FASCICOLO_PADRE]);
		}

		// Oggetto Udienza
		UDIENZA udienza = null;
		myStruct = (Struct) attributes[UDIENZA];
		if (myStruct != null) {
			udienza = (UDIENZA) TypeFactory.createUdienza(myStruct);
			fascicoloSIUS.setUDIENZA(udienza);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_CONTENUTO])) {
			fascicoloSIUS.setContenuto((String) attributes[DESC_CONTENUTO]);
		}

		// Oggetto MAGISTRATO
		MAGISTRATO magistrato = null;
		myStruct = (Struct) attributes[MAGISTRATO];
		if (myStruct != null) {
			magistrato = (MAGISTRATO) TypeFactory.create(myStruct);
			fascicoloSIUS.setMAGISTRATO(magistrato);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_CANCEL])) {
			fascicoloSIUS.setDescrCancelleriaAssegnataria((String) attributes[DESC_CANCEL]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_ANNOT_PROCED])) {
			fascicoloSIUS.setAnnotazioniProcedimento((String) attributes[DESC_ANNOT_PROCED]);
		}

		// IL TYPE NOTE è la lista dei NOTETYPE
		Array myNote = (Array) attributes[NOTE];
		if (myNote != null) {
			Object[] children = (Object[]) myNote.getArray();
			NOTETYPE[] noteList = new NOTETYPE[children.length];
			for (int i = 0; i < children.length; i++) {
				noteList[i] = (NOTETYPE) TypeFactory.create((Struct) children[i]);
			}
			// fascicoloSIUS.setListaNote(noteList);
			List<NOTETYPE> list = Arrays.asList(noteList);
			fascicoloSIUS.getListaNote().addAll(list);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_ULTER_ISTANZE])) {
			String value = ((String) attributes[DESC_ULTER_ISTANZE]).equals("N") ? "false" : "true";
			fascicoloSIUS.setUlterioriIstanze(new Boolean(value));
		}
		return fascicoloSIUS;
	}

	private static FASCICOLOSIEP createFascicoloSiep(Struct struct) throws java.sql.SQLException {

		// CHIAVE_PROG NUMBER,
		// CHIAVE_ANNO NUMBER(4),
		// DESC_CUMULO VARCHAR2(10),
		// DESC_TIPO_UFFICIO VARCHAR2(100),
		// DESC_COMUNE_UFFICIO VARCHAR2(100),
		// DATA_ISCRIZIONE DATE,
		// SENTENZA TY_SENTENZA,
		// DESC_POS_GIURIDICA VARCHAR2(100),
		// PENA_RESIDUA TY_PENA,
		// PENA_COMPLESSIVA TY_PENA
		final int CHIAVE_PROG = 0;
		final int CHIAVE_ANNO = CHIAVE_PROG + 1;
		final int DESC_CUMULO = CHIAVE_ANNO + 1;
		final int DESC_TIPO_UFFICIO = DESC_CUMULO + 1;
		final int DESC_COMUNE_UFFICIO = DESC_TIPO_UFFICIO + 1;
		final int DATA_ISCRIZIONE = DESC_COMUNE_UFFICIO + 1;
		final int SENTENZA = DATA_ISCRIZIONE + 1;
		final int DESC_POS_GIURIDICA = SENTENZA + 1;
		final int PENA_RESIDUA = DESC_POS_GIURIDICA + 1;
		final int PENA_COMPLESSIVA = PENA_RESIDUA + 1;

		Object[] attributes = struct.getAttributes();
		FASCICOLOSIEP fascicoloSIEP = new FASCICOLOSIEP();
		Struct myStruct = null;

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_PROG])) {
			Number value = (Number) attributes[CHIAVE_PROG];
			fascicoloSIEP.setChiaveProgrSIEP(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[CHIAVE_ANNO])) {
			Number value = (Number) attributes[CHIAVE_ANNO];
			fascicoloSIEP.setChiaveAnnoSIEP(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_CUMULO])) {
			fascicoloSIEP.setFlagCumulante((String) attributes[DESC_CUMULO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_UFFICIO])) {
			fascicoloSIEP.setDescrTipoUfficio((String) attributes[DESC_TIPO_UFFICIO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_COMUNE_UFFICIO])) {
			fascicoloSIEP.setDescrComuneUfficio((String) attributes[DESC_COMUNE_UFFICIO]);
		}

		Date data = (java.util.Date) attributes[DATA_ISCRIZIONE];
		if (data != null) {
			fascicoloSIEP.setDataIscrizione(createDataType((java.util.Date) attributes[DATA_ISCRIZIONE]));
		}

		// Oggetto SENTENZA
		SENTENZATYPE sentenza = null;
		myStruct = (Struct) attributes[SENTENZA];
		if (myStruct != null) {
			sentenza = (SENTENZATYPE) TypeFactory.create(myStruct);
			fascicoloSIEP.setSENTENZA(sentenza);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_POS_GIURIDICA])) {
			fascicoloSIEP.setDescrPosizioneGiuridica((String) attributes[DESC_POS_GIURIDICA]);
		}

		// Oggetto PENA_RESIDUA
		PENATYPE penaRes = null;
		myStruct = (Struct) attributes[PENA_RESIDUA];
		if (myStruct != null) {
			penaRes = (PENATYPE) TypeFactory.create(myStruct);
			fascicoloSIEP.setPENARESIDUA(penaRes);
		}

		// Oggetto PENA_COMPLESSIVA
		PENATYPE penaCompl = null;
		myStruct = (Struct) attributes[PENA_COMPLESSIVA];
		if (myStruct != null) {
			penaCompl = (PENATYPE) TypeFactory.create(myStruct);
			fascicoloSIEP.setPENACOMPLESSIVA(penaCompl);
		}

		return fascicoloSIEP;

	}

	private static AVVOCATOTYPE createAvvocato(Struct struct) throws java.sql.SQLException {
		// DESC_FORO VARCHAR2(72),
		// COGNOME VARCHAR2(35),
		// NOME VARCHAR2(35),
		// DESC_TIPO VARCHAR2(35),
		// COD_FISCALE VARCHAR2(16)
		final int DESC_FORO = 0;
		final int COGNOME = DESC_FORO + 1;
		final int NOME = COGNOME + 1;
		final int DESC_TIPO = NOME + 1;
		final int COD_FISCALE = DESC_TIPO + 1;

		Object[] attributes = struct.getAttributes();
		AVVOCATOTYPE avvocato = new AVVOCATOTYPE();

		if (PropertyUtil.isPresent((String) attributes[COGNOME])) {
			avvocato.setCognome((String) attributes[COGNOME]);
		}

		if (PropertyUtil.isPresent((String) attributes[NOME])) {
			avvocato.setNome((String) attributes[NOME]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO])) {
			avvocato.setDescrTipo((String) attributes[DESC_TIPO]);
		}

		if (PropertyUtil.isPresent((String) attributes[COD_FISCALE])) {
			avvocato.setCodiceFiscale((String) attributes[COD_FISCALE]);
		}

		return avvocato;
	}

	private static SOGGETTOTYPE createSoggetto(Struct struct) throws java.sql.SQLException {

		// COGNOME VARCHAR2(35),
		// NOME VARCHAR2(35),
		// SESSO VARCHAR2(1),
		// ANNO_NASCITA VARCHAR2(4),
		// MESE_NASCITA VARCHAR2(2),
		// GIORNO_NASCITA VARCHAR2(2),
		// DESC_COMUNE VARCHAR2(72),
		// DESC_STATO_NASCITA VARCHAR2(72),
		// DESC_PATENITA VARCHAR2(72),
		// DESC_COGNOME_MADRE VARCHAR2(72),
		// DESC_POSIZIONE_GIURIDICA VARCHAR2(200),
		// DATA_FINE_PENA DATE,
		// DESC_LUOGO_DETENZIONE VARCHAR2(200),
		// ID_PROVINCIA VARCHAR2(2),
		// ID_SOGGETTO NUMBER,
		// RESIDENZE RC_RESIDENZA
		final int COGNOME = 0;
		final int NOME = COGNOME + 1;
		final int SESSO = NOME + 1;
		final int ANNO_NASCITA = SESSO + 1;
		final int MESE_NASCITA = ANNO_NASCITA + 1;
		final int GIORNO_NASCITA = MESE_NASCITA + 1;
		final int DESC_COMUNE = GIORNO_NASCITA + 1;
		final int DESC_STATO_NASCITA = DESC_COMUNE + 1;
		final int DESC_PATENITA = DESC_STATO_NASCITA + 1;
		final int DESC_COGNOME_MADRE = DESC_PATENITA + 1;
		final int DESC_POSIZIONE_GIURIDICA = DESC_COGNOME_MADRE + 1;
		final int DATA_FINE_PENA = DESC_POSIZIONE_GIURIDICA + 1;
		final int DESC_LUOGO_DETENZIONE = DATA_FINE_PENA + 1;
		final int ID_PROVINCIA = DESC_LUOGO_DETENZIONE + 1;
		final int ID_SOGGETTO = ID_PROVINCIA + 1;
		final int RESIDENZE = ID_SOGGETTO + 1;

		Object[] attributes = struct.getAttributes();
		SOGGETTOTYPE soggetto = new SOGGETTOTYPE();

		if (PropertyUtil.isPresent((String) attributes[COGNOME])) {
			soggetto.setCognome((String) attributes[COGNOME]);
		}

		if (PropertyUtil.isPresent((String) attributes[NOME])) {
			soggetto.setNome((String) attributes[NOME]);
		}

		if (PropertyUtil.isPresent((String) attributes[SESSO])) {
			soggetto.setSesso((String) attributes[SESSO]);
		}

		DATATYPE dataNascita = new DATATYPE();
		if (PropertyUtil.isPresent((String) attributes[ANNO_NASCITA])) {
			String annoN = (String) attributes[ANNO_NASCITA];
			dataNascita.setAnno(Integer.parseInt(annoN));
		}
		if (PropertyUtil.isPresent((String) attributes[MESE_NASCITA])) {
			String meseN = (String) attributes[MESE_NASCITA];
			dataNascita.setMese(Integer.parseInt(meseN));
		}

		if (PropertyUtil.isPresent((String) attributes[GIORNO_NASCITA])) {
			String giornoN = (String) attributes[GIORNO_NASCITA];
			dataNascita.setGiorno(Integer.parseInt(giornoN));
		}
		soggetto.setDataNascita(dataNascita);

		if (PropertyUtil.isPresent((String) attributes[DESC_COMUNE])) {
			soggetto.setDescrComuneNascita((String) attributes[DESC_COMUNE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_STATO_NASCITA])) {
			soggetto.setDescrStatoNascita((String) attributes[DESC_STATO_NASCITA]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_PATENITA])) {
			soggetto.setPaternita((String) attributes[DESC_PATENITA]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_COGNOME_MADRE])) {
			soggetto.setCognomeMadre((String) attributes[DESC_COGNOME_MADRE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_POSIZIONE_GIURIDICA])) {
			soggetto.setDescrPosGiuridica((String) attributes[DESC_POSIZIONE_GIURIDICA]);
		}

		Date data = (java.util.Date) attributes[DATA_FINE_PENA];
		if (data != null) {
			soggetto.setDataFinePena(createDataType((java.util.Date) attributes[DATA_FINE_PENA]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_LUOGO_DETENZIONE])) {
			soggetto.setLuogoDetenzione((String) attributes[DESC_LUOGO_DETENZIONE]);
		}

		if (PropertyUtil.isPresent((String) attributes[ID_PROVINCIA])) {
			soggetto.setCodProvinciaNascita((String) attributes[ID_PROVINCIA]);
		}

		// ID_SOGGETTO
		if (PropertyUtil.isPresent((Number) attributes[ID_SOGGETTO])) {
			Number value = (Number) attributes[ID_SOGGETTO];
			soggetto.setIdSoggetto(BigInteger.valueOf(value.longValue()));
		}

		// IL RESIDENZE è la lista dei RESIDENZE
		Array myRes = (Array) attributes[RESIDENZE];
		if (myRes != null) {
			Object[] children = (Object[]) myRes.getArray();
			RESIDENZATYPE[] resList = new RESIDENZATYPE[children.length];
			for (int i = 0; i < children.length; i++) {
				resList[i] = (RESIDENZATYPE) TypeFactory.create((Struct) children[i]);
			}
			// soggetto.setListaResidenze(resList);
			List<RESIDENZATYPE> list = Arrays.asList(resList);
			soggetto.getListaResidenze().addAll(list);
		}

		return soggetto;
	}

	private static RESIDENZATYPE createResidenza(Struct struct) throws java.sql.SQLException {
		// TIPO_RESIDENZA VARCHAR2(10),
		// ID_STATO VARCHAR2(3),
		// DESC_STATO VARCHAR2(72),
		// ID_PROVINCIA VARCHAR2(2),
		// DESC_COMUNE VARCHAR2(72),
		// DESC_INDIRIZZO VARCHAR2(200),
		// DESC_COMUNE_ESTERO VARCHAR2(200));
		final int TIPO_RESIDENZA = 0;
		final int ID_STATO = TIPO_RESIDENZA + 1;
		final int DESC_STATO = ID_STATO + 1;
		final int ID_PROVINCIA = DESC_STATO + 1;
		final int DESC_COMUNE = ID_PROVINCIA + 1;
		final int DESC_INDIRIZZO = DESC_COMUNE + 1;
		final int DESC_COMUNE_ESTERO = DESC_INDIRIZZO + 1;

		Object[] attributes = struct.getAttributes();
		RESIDENZATYPE residenza = new RESIDENZATYPE();

		if (PropertyUtil.isPresent((String) attributes[TIPO_RESIDENZA])) {
			residenza.setCodTipoResidenza((String) attributes[TIPO_RESIDENZA]);
		}

		if (PropertyUtil.isPresent((String) attributes[ID_STATO])) {
			residenza.setCodStato((String) attributes[ID_STATO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_STATO])) {
			residenza.setDescrStato((String) attributes[DESC_STATO]);
		}

		if (PropertyUtil.isPresent((String) attributes[ID_PROVINCIA])) {
			residenza.setCodProvincia((String) attributes[ID_PROVINCIA]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_COMUNE])) {
			residenza.setDescrComune((String) attributes[DESC_COMUNE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_INDIRIZZO])) {
			residenza.setIndirizzo((String) attributes[DESC_INDIRIZZO]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_COMUNE_ESTERO])) {
			residenza.setDescComuneEstero((String) attributes[DESC_COMUNE_ESTERO]);
		}
		return residenza;
	}

	private static MAGISTRATO createMagistrato(Struct struct) throws java.sql.SQLException {
		// COGNOME_MAG VARCHAR2(72),
		// NOME_MAG VARCHAR2(72)

		final int COGNOME_MAG = 0;
		final int NOME_MAG = COGNOME_MAG + 1;

		Object[] attributes = struct.getAttributes();
		MAGISTRATO magistrato = new MAGISTRATO();

		if (PropertyUtil.isPresent((String) attributes[COGNOME_MAG])) {
			magistrato.setMCognome((String) attributes[COGNOME_MAG]);
		}

		if (PropertyUtil.isPresent((String) attributes[NOME_MAG])) {
			magistrato.setMNome((String) attributes[NOME_MAG]);
		}
		return magistrato;
	}

	private static NOTETYPE createNote(Struct struct) throws java.sql.SQLException {
		// DATA_NOTE DATE,
		// DESCR_NOTE VARCHAR2(300)
		final int DATA_NOTE = 0;
		final int DESCR_NOTE = DATA_NOTE + 1;

		Object[] attributes = struct.getAttributes();
		NOTETYPE nota = new NOTETYPE();

		Date data = (java.util.Date) attributes[DATA_NOTE];
		if (data != null) {
			nota.setData(createDataType((java.util.Date) attributes[DATA_NOTE]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESCR_NOTE])) {
			nota.setDescrizione((String) attributes[DESCR_NOTE]);
		}

		return nota;
	}

	/**
	 * @param struct
	 * @return
	 * @throws java.sql.SQLException
	 */
	private static ATTO createAtto(Struct struct) throws java.sql.SQLException {

		// DATA_RICHIESTA DATE,
		// DESC_TIPO_ATTO VARCHAR2(200),
		// DATA_ARRIVO_CANCELLERIA DATE,
		// DESCR_TIPO_MITTENTE VARCHAR2(100),
		// DESC_SEDE_MITTENTE VARCHAR2(72),
		// DESC_MITTENTE VARCHAR2(200)

		final int DATA_RICHIESTA = 0;
		final int DESC_TIPO_ATTO = DATA_RICHIESTA + 1;
		final int DATA_ARRIVO_CANCELLERIA = DESC_TIPO_ATTO + 1;
		final int DESCR_TIPO_MITTENTE = DATA_ARRIVO_CANCELLERIA + 1;
		final int DESC_SEDE_MITTENTE = DESCR_TIPO_MITTENTE + 1;
		final int DESC_MITTENTE = DESC_SEDE_MITTENTE + 1;

		Object[] attributes = struct.getAttributes();
		ATTO atto = new ATTO();

		Date data = (java.util.Date) attributes[DATA_RICHIESTA];
		if (data != null) {
			atto.setDataRichiesta(createDataType((java.util.Date) attributes[DATA_RICHIESTA]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_TIPO_ATTO])) {
			atto.setDescrTipoAtto((String) attributes[DESC_TIPO_ATTO]);
		}

		data = (java.util.Date) attributes[DATA_ARRIVO_CANCELLERIA];
		if (data != null) {
			atto.setDataArrivoCancelleria(createDataType((java.util.Date) attributes[DATA_ARRIVO_CANCELLERIA]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESCR_TIPO_MITTENTE])) {
			atto.setDescrTipoMittenteAtto((String) attributes[DESCR_TIPO_MITTENTE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_SEDE_MITTENTE])) {
			atto.setDescrSedeMittente((String) attributes[DESC_SEDE_MITTENTE]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_MITTENTE])) {
			atto.setDescrMittente((String) attributes[DESC_MITTENTE]);
		}

		return atto;
	}

	private static TENORETYPE createTenore(Struct struct) throws java.sql.SQLException {

		// DESC_OGGETTO VARCHAR2(200),
		// NOTE VARCHAR2(200)

		final int DESC_OGGETTO = 0;
		final int NOTE = DESC_OGGETTO + 1;

		Object[] attributes = struct.getAttributes();
		TENORETYPE tenore = new TENORETYPE();

		if (PropertyUtil.isPresent((String) attributes[DESC_OGGETTO])) {
			tenore.setDescrOggettoTenore((String) attributes[DESC_OGGETTO]);
		}

		if (PropertyUtil.isPresent((String) attributes[NOTE])) {
			tenore.setNote((String) attributes[NOTE]);
		}

		return tenore;
	}

	private static UDIENZA createUdienza(Struct struct) throws java.sql.SQLException {
		// DATA_UDIENZA DATE,
		// DATA_CAMERA_CONSIGLIO DATE,
		// FLAG_RINVIATA VARCHAR2(1),
		// DESC_PRESIDENTE VARCHAR2(144),
		// DESC_TIPO_OPERAZIONE VARCHAR2(30),
		// DATA_INSERIMENTO DATE,
		// DATA_MODIFICA DATE

		final int DATA_UDIENZA = 0;
		final int DATA_CAMERA_CONSIGLIO = DATA_UDIENZA + 1;
		final int FLAG_RINVIATA = DATA_CAMERA_CONSIGLIO + 1;
		final int DESC_PRESIDENTE = FLAG_RINVIATA + 1;

		Object[] attributes = struct.getAttributes();
		UDIENZA udienza = new UDIENZA();

		Date data = (java.util.Date) attributes[DATA_UDIENZA];
		if (data != null) {
			udienza.setDataUdienza(createDataType((java.util.Date) attributes[DATA_UDIENZA]));
		}

		data = (java.util.Date) attributes[DATA_CAMERA_CONSIGLIO];
		if (data != null) {
			udienza.setDataCameraConsiglio(createDataType((java.util.Date) attributes[DATA_CAMERA_CONSIGLIO]));
		}

		if (PropertyUtil.isPresent((String) attributes[FLAG_RINVIATA])) {
			udienza.setFlagRinviata((String) attributes[FLAG_RINVIATA]);
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_PRESIDENTE])) {
			udienza.setDescrPresidente((String) attributes[DESC_PRESIDENTE]);
		}

		return udienza;
	}

	private static PENATYPE createPena(Struct struct) throws java.sql.SQLException {
		// DATA_INIZIO_PENA DATE,
		// DATA_FINE_PENA DATE,
		// NUME_SANZ_SOST_RES NUMBER(4),
		// FLAG_ERGASTOLO VARCHAR2(1),
		// ANNI_ISOL_DIURNO NUMBER(4),
		// MESI_ISOL_DIURNO NUMBER(4),
		// GIOR_ISOL_DIURNO NUMBER(4),
		// ANNI_RECLUSIONE NUMBER(4),
		// MESI_RECLUSIONE NUMBER(4),
		// GIOR_RECLUSIONE NUMBER(4),
		// ANNI_ARRESTO NUMBER(4),
		// MESI_ARRESTO NUMBER(4),
		// GIOR_ARRESTO NUMBER(4),
		// IMPO_MULTA NUMBER(16,2),
		// IMPO_AMMENDA NUMBER(16,2),
		// DESC_PENA_DETENTIVA VARCHAR2(30),
		// CODI_PENA_DETENTIVA VARCHAR2(3)

		final int DATA_INIZIO_PENA = 0;
		final int DATA_FINE_PENA = DATA_INIZIO_PENA + 1;
		final int NUME_SANZ_SOST_RES = DATA_FINE_PENA + 1;
		final int FLAG_ERGASTOLO = NUME_SANZ_SOST_RES + 1;
		final int ANNI_ISOL_DIURNO = FLAG_ERGASTOLO + 1;
		final int MESI_ISOL_DIURNO = ANNI_ISOL_DIURNO + 1;
		final int GIOR_ISOL_DIURNO = MESI_ISOL_DIURNO + 1;
		final int ANNI_RECLUSIONE = GIOR_ISOL_DIURNO + 1;
		final int MESI_RECLUSIONE = ANNI_RECLUSIONE + 1;
		final int GIOR_RECLUSIONE = MESI_RECLUSIONE + 1;
		final int ANNI_ARRESTO = GIOR_RECLUSIONE + 1;
		final int MESI_ARRESTO = ANNI_ARRESTO + 1;
		final int GIOR_ARRESTO = MESI_ARRESTO + 1;
		final int IMPO_MULTA = GIOR_ARRESTO + 1;
		final int IMPO_AMMENDA = IMPO_MULTA + 1;
		final int DESC_PENA_DETENTIVA = IMPO_AMMENDA + 1;
		final int CODI_PENA_DETENTIVA = DESC_PENA_DETENTIVA + 1;

		Object[] attributes = struct.getAttributes();
		PENATYPE pena = new PENATYPE();

		Date data = (java.util.Date) attributes[DATA_INIZIO_PENA];
		if (data != null) {
			pena.setDataInizioPena(createDataType((java.util.Date) attributes[DATA_INIZIO_PENA]));
		}

		data = (java.util.Date) attributes[DATA_FINE_PENA];
		if (data != null) {
			pena.setDataFinePena(createDataType((java.util.Date) attributes[DATA_FINE_PENA]));
		}

		if (PropertyUtil.isPresent((String) attributes[FLAG_ERGASTOLO])) {
			pena.setFlagErgastolo((String) attributes[FLAG_ERGASTOLO]);
		}

		if (PropertyUtil.isPresent((Number) attributes[ANNI_ISOL_DIURNO])) {
			Number value = (Number) attributes[ANNI_ISOL_DIURNO];
			pena.setNumAnniIsolamentoDiurno(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[MESI_ISOL_DIURNO])) {
			Number value = (Number) attributes[MESI_ISOL_DIURNO];
			pena.setNumMesiIsolamentoDiurno(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[GIOR_ISOL_DIURNO])) {
			Number value = (Number) attributes[GIOR_ISOL_DIURNO];
			pena.setNumGiorniIsolamentoDiurno(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[ANNI_RECLUSIONE])) {
			Number value = (Number) attributes[ANNI_RECLUSIONE];
			pena.setNumAnniReclusione(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[MESI_RECLUSIONE])) {
			Number value = (Number) attributes[MESI_RECLUSIONE];
			pena.setNumMesiReclusione(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[GIOR_RECLUSIONE])) {
			Number value = (Number) attributes[GIOR_RECLUSIONE];
			pena.setNumGiorniReclusione(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[ANNI_ARRESTO])) {
			Number value = (Number) attributes[ANNI_ARRESTO];
			pena.setNumAnniArresto(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[MESI_ARRESTO])) {
			Number value = (Number) attributes[MESI_ARRESTO];
			pena.setNumMesiArresto(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((Number) attributes[GIOR_ARRESTO])) {
			Number value = (Number) attributes[GIOR_ARRESTO];
			pena.setNumGiorniArresto(BigInteger.valueOf(value.longValue()));
		}

		if (attributes[IMPO_MULTA] != null)
			pena.setImportoMulta((BigDecimal) attributes[IMPO_MULTA]);

		if (attributes[IMPO_AMMENDA] != null)
			pena.setImportoAmmenda((BigDecimal) attributes[IMPO_AMMENDA]);

		if (PropertyUtil.isPresent((String) attributes[DESC_PENA_DETENTIVA])) {
			pena.setDescrTipoPenaDetentiva((String) attributes[DESC_PENA_DETENTIVA]);
		}

		if (PropertyUtil.isPresent((String) attributes[CODI_PENA_DETENTIVA])) {
			pena.setCodTipoPenaDetentiva((String) attributes[CODI_PENA_DETENTIVA]);
		}

		return pena;
	}

	private static SENTENZATYPE createSentenza(Struct struct) throws java.sql.SQLException {
		// ID_SENTENZA NUMBER,
		// NUME_SENTENZA VARCHAR2(8),
		// ANNO_SENTENZA NUMBER(4),
		// DATA_PROVVEDIMENTO DATE,
		// DESC_AUTOR_EMIT VARCHAR2(100)

		final int ID_SENTENZA = 0;
		final int NUME_SENTENZA = ID_SENTENZA + 1;
		final int ANNO_SENTENZA = NUME_SENTENZA + 1;
		final int DATA_PROVVEDIMENTO = ANNO_SENTENZA + 1;
		final int DESC_AUTOR_EMIT = DATA_PROVVEDIMENTO + 1;

		Object[] attributes = struct.getAttributes();
		SENTENZATYPE sentenza = new SENTENZATYPE();

		if (PropertyUtil.isPresent((Number) attributes[ID_SENTENZA])) {
			Number value = (Number) attributes[ID_SENTENZA];
			sentenza.setIdSentenza(BigInteger.valueOf(value.longValue()));
		}

		if (PropertyUtil.isPresent((String) attributes[NUME_SENTENZA])) {
			sentenza.setNumeroSentenza((String) attributes[NUME_SENTENZA]);
		}

		if (PropertyUtil.isPresent((Number) attributes[ANNO_SENTENZA])) {
			Number value = (Number) attributes[ANNO_SENTENZA];
			sentenza.setAnnoSentenza(BigInteger.valueOf(value.longValue()));
		}

		Date data = (java.util.Date) attributes[DATA_PROVVEDIMENTO];
		if (data != null) {
			sentenza.setDataProvvedimento(createDataType((java.util.Date) attributes[DATA_PROVVEDIMENTO]));
		}

		if (PropertyUtil.isPresent((String) attributes[DESC_AUTOR_EMIT])) {
			sentenza.setDescrTipoAutoritaEmittente((String) attributes[DESC_AUTOR_EMIT]);
		}

		return sentenza;
	}

}