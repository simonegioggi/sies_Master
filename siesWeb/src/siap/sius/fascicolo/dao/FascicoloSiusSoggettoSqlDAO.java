package siap.sius.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import siap.dao.SIAPSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.util.MinorMask;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

/**
 * <p>
 * Title: FascicoloSiusSoggettoSqlDAO
 * </p>
 * <p>
 * Description: Realizza Sql DAO del Fascicolo Sius
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class FascicoloSiusSoggettoSqlDAO extends SIAPSqlDAO {

	// AVVOCATURA: aggiunto logger
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	public FascicoloSiusSoggettoSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaFascicoloSiusSoggetto(SoggettoModel aModel) {
		String soggettoFascicoloSius = "";

		// soggettoFascicoloSius += getFascicoloSiusSqlSoggettoQuery();
		soggettoFascicoloSius += getFascicoloSiusSqlQuery();
		soggettoFascicoloSius += setCondizione(aModel);
		// STUB 16/02/2005
		// soggettoFascicoloSius += setOrderAnnoProgr();
		soggettoFascicoloSius += setOrderUfficioAnnoProgrEvento();
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(soggettoFascicoloSius);
	}

	public void ricercaFascicoloSiusSoggettoForStorico(SoggettoModel aModel) {
		String soggettoFascicoloSius = "";

		// soggettoFascicoloSius += getFascicoloSiusSqlSoggettoQuery();
		soggettoFascicoloSius += getFascicoloSiusSqlQueryForStorico();
		soggettoFascicoloSius += setCondizione(aModel);
		soggettoFascicoloSius += setOrderAnnoProgr();
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(soggettoFascicoloSius);
	}

	public void ricercaFascicoloSiusSoggettoTenoreEvento(SoggettoModel aModel) {
		String soggettoFascicoloTenoreEvento = "";
		// soggettoFascicoloSius += getFascicoloSiusSqlSoggettoQuery();
		// Costruisco la query relativa soltanto all model fascicolo SIUS
		soggettoFascicoloTenoreEvento += getFascicoloSiusSqlQueryTenoreEvento();
		soggettoFascicoloTenoreEvento += setCondizione(aModel);
		// soggettoFascicoloSius += setOrderSoggetto();
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(soggettoFascicoloTenoreEvento);
	}

	public void ricercaEsisteEventoNullo(SoggettoModel aModel) {
		String soggettoFascicoloEsisteEvento = "";

		soggettoFascicoloEsisteEvento += getFascicoloEsisteEvento(aModel);
		// settaggio della stringa SQL appena costruita prima della query
		setStatement(soggettoFascicoloEsisteEvento);
	}

	protected String getFascicoloEsisteEvento(SoggettoModel aModel) {
		String lStatement = new String();

		// Query che conta, per un soggetto, il numero di eventi legati al fascicolo al fascicolo SIUS
		lStatement = " SELECT COUNT(*) AS NUM_REC from evento ev, soggetto sogg, fascicolo_sius fasc ";
		lStatement += " WHERE sogg.id_soggetto = fasc.sog_id_soggetto and fasc.ID_FASCICOLO_SIUS = ev.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " and sogg.id_soggetto =  " + aModel.getIdSoggetto();

		return lStatement;
	}

	public void ricercaFascicoloSiusPerNumeroSius(FascicoloGPModel aModel) {
		String FascicoloSiusPerNumeroSius = "";

		// soggettoFascicoloSius += getFascicoloSiusSqlSoggettoQuery();
		// Costruisco la query relativa soltanto all model fascicolo SIUS
		FascicoloSiusPerNumeroSius += getFascicoloSiusPerNumeroSqlQuery();
		FascicoloSiusPerNumeroSius += setCondizionePerNumero(aModel);

		// STUB 10/06/2004 Sostituito l'order in caso di ricerca per range di Anno/Numero.
		if ((aModel.getFascicoloSiusModel().getChiaveAnnoFinale() != null
				&& aModel.getFascicoloSiusModel().getChiaveAnnoFinale().intValue() > 0)
				|| (aModel.getFascicoloSiusModel().getChiaveAnnoIniziale() != null
						&& aModel.getFascicoloSiusModel().getChiaveAnnoIniziale().intValue() > 0))
			FascicoloSiusPerNumeroSius += setOrderAnnoProgrAsc();
		else
			FascicoloSiusPerNumeroSius += setOrderAnnoProgr();

		// settaggio della stringa SQL appena costruita prima della query
		setStatement(FascicoloSiusPerNumeroSius);
	}

	public void ricercaFascicoloSiusPerNumeroSius(FascicoloGPModel aModel, String majorOffice) {
		String FascicoloSiusPerNumeroSius = "";

		// soggettoFascicoloSius += getFascicoloSiusSqlSoggettoQuery();
		// Costruisco la query relativa soltanto all model fascicolo SIUS
		FascicoloSiusPerNumeroSius += getFascicoloSiusPerNumeroSqlQuery(majorOffice);
		FascicoloSiusPerNumeroSius += setCondizionePerNumero(aModel);

		if (StringUtils.checkValidValue(majorOffice)) {
			FascicoloSiusPerNumeroSius += MinorMask.minorCondition("vse", "FASC", majorOffice);
		}

		// STUB 10/06/2004 Sostituito l'order in caso di ricerca per range di Anno/Numero.
		if ((aModel.getFascicoloSiusModel().getChiaveAnnoFinale() != null
				&& aModel.getFascicoloSiusModel().getChiaveAnnoFinale().intValue() > 0)
				|| (aModel.getFascicoloSiusModel().getChiaveAnnoIniziale() != null
						&& aModel.getFascicoloSiusModel().getChiaveAnnoIniziale().intValue() > 0))
			FascicoloSiusPerNumeroSius += setOrderAnnoProgrAsc();
		else
			FascicoloSiusPerNumeroSius += setOrderAnnoProgr();

		// settaggio della stringa SQL appena costruita prima della query
		setStatement(FascicoloSiusPerNumeroSius);
	}

	public void ricercaFascicoliBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc, String majorOffice) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFascicoliBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc, majorOffice);
		strQuery += setCondizione(aModel);

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();
		setStatement(strQuery);
	}

	public void ricercaFascicoliPerDataFinePena(String lUfficioUtenteConnesso, Date dataDalIscrizione,
			Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena, String lIncludeArchiviati,
			String lCodPosGiuridica, String lCodContenuto) {
		String strQuery = "";

		// Costruzione della query parametrizzata.

		strQuery += getFascicoloSiusPerNumeroSqlQuery();
		strQuery += setCondizionePerDataFinePena(lUfficioUtenteConnesso, dataDalIscrizione, dataAlIscrizione,
				dataDalFinePena, dataAlFinePena, lIncludeArchiviati, lCodPosGiuridica, lCodContenuto);
		if (dataDalIscrizione != null && dataAlIscrizione != null)
			strQuery += setOrderDataIscrizione();
		else if (dataDalFinePena != null && dataAlFinePena != null)
			strQuery += setOrderDataFinePena();
		else
			strQuery += setOrderAnnoProgr();

		setStatement(strQuery);
	}

	/**
	 *
	 * @param aFascModel
	 * @param sTipoAtto
	 * @param aCancAssFascSius
	 * @param aFiltroCollaboratore
	 * @param aIsJoinPendenti
	 *            se true indica alla select dei pendenti di andare in LEFT OUTER JOIN con EVENTO e DOCUMENTO
	 *            allegato, operazione molto lenta utilizzata solo nel caso di produzione foglio exel ma non
	 *            utilizzata in caso di ricerca paginata
	 */
	public void ricercaFascicoloSiusProcedimentoPerEstremi(FascicoloSiusModel aFascModel, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore, boolean aIsJoinPendenti) {
		String soggettoFascicoloSius = "";

		// Costruita la query relativa soltanto all model fascicolo SIUS
		if (aFascModel.getDescrStatoFascicolo().toLowerCase().compareTo("pendenti") == 0) {
			// d.f. 17/02/2014 per i "pendenti" si cambia query
			soggettoFascicoloSius += getFascicoloSiusSqlQueryPerEstremiPendenti(aFascModel, aIsJoinPendenti);
		} else
			soggettoFascicoloSius += getFascicoloSiusSqlQueryPerEstremiSenzaEvento();

		// Creare la condizione Per Estremi
		soggettoFascicoloSius += setCondizionePerEstremi(aFascModel, sTipoAtto, aCancAssFascSius,
				aFiltroCollaboratore, "NO");

		if (aFascModel.getDescrStatoFascicolo().toLowerCase().compareTo("definiti") == 0
				|| aFascModel.getDescrStatoFascicolo().toLowerCase().compareTo("tutti") == 0) {
			soggettoFascicoloSius += " UNION ";
			soggettoFascicoloSius += getFascicoloSiusSqlQueryPerEstremiConEvento();
		}
		soggettoFascicoloSius += setCondizionePerEstremi(aFascModel, sTipoAtto, aCancAssFascSius,
				aFiltroCollaboratore, "SI");

		soggettoFascicoloSius += setOrderPerEstremi();

		// Settaggio della stringa SQL appena costruita prima della query
		setStatement(soggettoFascicoloSius);
	}

	/**
	 * Settaggio della condizione sul Soggetto MERGE CON AVVOCATURA: aggiunto alias --> SOGG.
	 *
	 * @param aSm
	 * @return lCondizioni
	 */
	private String setCondizione(SoggettoModel aSm) {
		String lCondizioni = new String();
		if (aSm.getIdSoggetto().doubleValue() == 0) {
			if (!(aSm.getCognome().equals("")))
				lCondizioni += " AND SOGG.COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome())
						+ "%'";
			if (!(aSm.getNome().equals("")))
				lCondizioni += " AND SOGG.NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			if (!(aSm.getCodComuneNascita().equals("")))
				lCondizioni += " AND COD_COMUNE_NASCITA = '"
						+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";
			if (!(aSm.getPaternita().equals("")))
				lCondizioni += " AND PATERNITA LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			// 20190301 [SG]: AGGIUNTO CAMPO_COD_AFIS IN RICERCA
			if (!(aSm.getCodAfis().equals("")))
				lCondizioni += " AND COD_AFIS LIKE '"
						+ StringUtils.convertSqlString(aSm.getCodAfis().toUpperCase()) + "%'";
			if (!(aSm.getCodCs().equals("")))
				lCondizioni += " AND COD_CS LIKE '"
						+ StringUtils.convertSqlString(aSm.getCodCs().toUpperCase()) + "%'";
			if (!(aSm.getNomeMadre().equals("")))
				lCondizioni += " AND NOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCognomeMadre().equals("")))
				lCondizioni += " AND COGNOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			if (!(aSm.getCodStatoNascita().equals("")))
				lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			if (aSm.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(SOGG.DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}
		return lCondizioni;
	}

	/**
	 * Esegue la ricerca dei fascicoli in base al super soggetto
	 *
	 * @param strCodiceDistrettoUtente
	 */
	protected String setCondizioneSuperSoggetto(SoggettoModel aModel) {
		String lCondizioni = new String();
		// 27/10/2010 Utilizzo di UPPER e toUpperCase per normalizzare il controllo di uguaglianza x
		// (PATERNITA, COGNOME_MADRE, ATTO_NASCITA, COD_AFIS)

		if (aModel.getAnnoNascita() != null)
			lCondizioni += " AND sogg.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
		else
			lCondizioni += " AND sogg.ANNO_NASCITA is null";

		// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
		if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
			lCondizioni += " AND UPPER(sogg.ATTO_NASCITA) = '"
					+ StringUtils.convertSqlString(aModel.getAttoNascita().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.ATTO_NASCITA is null";

		if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
			lCondizioni += " AND UPPER(sogg.COD_AFIS) = '"
					+ StringUtils.convertSqlString(aModel.getCodAfis().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.COD_AFIS is null";

		if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
			lCondizioni += " AND sogg.COD_COMUNE_NASCITA = '"
					+ StringUtils.convertSqlString(aModel.getCodComuneNascita()) + "'";
		else
			lCondizioni += " AND sogg.COD_COMUNE_NASCITA is null";

		if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
			lCondizioni += " AND sogg.COD_CS = '"
					+ StringUtils.convertSqlString(aModel.getCodCs().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.COD_CS is null";

		if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
			lCondizioni += " AND sogg.COD_FISCALE = '"
					+ StringUtils.convertSqlString(aModel.getCodFiscale().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.COD_FISCALE is null";

		if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
			lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
		else
			lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA is null";

		if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
			lCondizioni += " AND sogg.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
		else
			lCondizioni += " AND sogg.COD_STATO_NASCITA is null";

		if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
			lCondizioni += " AND sogg.COGNOME = '" + StringUtils.convertSqlString(aModel.getCognome()) + "'";
		else
			lCondizioni += " AND sogg.COGNOME is null";

		if (aModel.getNome() != null && aModel.getNome().length() > 0)
			lCondizioni += " AND sogg.NOME = '" + StringUtils.convertSqlString(aModel.getNome()) + "'";
		else
			lCondizioni += " AND sogg.NOME is null";

		if (aModel.getDataNascita() != null)
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(sogg.DATA_NASCITA) = to_date('"
					+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy") + "','DD-MM-YYYY')";
		else
			lCondizioni += " AND sogg.DATA_NASCITA is null";

		if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
			lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
		else
			lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA is null";

		if (aModel.getDescComuneNascitaEstero() != null && aModel.getDescComuneNascitaEstero().length() > 0)
			lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO = '"
					+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero()) + "'";
		else
			lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO is null";

		if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
			lCondizioni += " AND sogg.NAZIONALITA = '" + aModel.getNazionalita() + "'";
		else
			lCondizioni += " AND sogg.NAZIONALITA is null";

		if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
			lCondizioni += " AND UPPER(sogg.PATERNITA) = '"
					+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.PATERNITA is null";

		if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
			lCondizioni += " AND UPPER(sogg.COGNOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.COGNOME_MADRE is null";

		if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
			lCondizioni += " AND UPPER(sogg.NOME_MADRE) = '"
					+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
		else
			lCondizioni += " AND sogg.NOME_MADRE is null";

		if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
			lCondizioni += " AND sogg.SESSO = '" + aModel.getSesso() + "'";
		else
			lCondizioni += " AND sogg.SESSO is null";

		if (aModel.getMeseNascita() != null)
			lCondizioni += " AND sogg.MESE_NASCITA = " + aModel.getMeseNascita();
		else
			lCondizioni += " AND sogg.MESE_NASCITA is null";

		return lCondizioni;
	}

	/**
	 * Settaggio della condizione su Estremi Atto
	 *
	 * @param aSm
	 *            ;
	 * @param sTipoAtto
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerEstremi(FascicoloSiusModel aSm, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore, String flagEvento) {
		String lCondizioni = new String();
		// Cerca i fascicoli dell'Oggetto Procedimento selezionato
		if (!(sTipoAtto.equalsIgnoreCase("-")))
			lCondizioni += " AND COD_OGGETTO_PROCEDIMENTO = '" + sTipoAtto + "'";

		// Utilizzo getCodUfficioInserimento come veicolo per leggermi il codice ufficio
		if ((aSm.getCodUfficioInserimento() != null))
			lCondizioni += " AND UFF.COD_UFFICIO = nvl('" + aSm.getCodUfficioInserimento()
					+ "', UFF.COD_UFFICIO )";

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aSm.getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSm.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aSm.getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSm.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da una data arrivo.
		if ((aSm.getDataArrivoIniziale() != null))
			lCondizioni += " AND TO_CHAR(GP.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSm.getDataArrivoIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data arrivo.
		if ((aSm.getDataArrivoFinale() != null))
			lCondizioni += " AND TO_CHAR(GP.DATA_ARRIVO_CANCELLERIA,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSm.getDataArrivoFinale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli a partire da una data atto.
		if ((aSm.getDataAttoIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_RICHIESTA,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSm.getDataAttoIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data atto.
		if ((aSm.getDataAttoFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_RICHIESTA,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSm.getDataAttoFinale(), "yyyyMMdd") + "'";

		// STUB 21/02/2005 Cerca i fascicoli per Magistrato Relatore (appoggiato in CodStatoFascicolo).
		if ((aSm.getCodStatoFascicolo() != null) && (aSm.getCodStatoFascicolo().compareTo("Tutti") != 0))
			lCondizioni += " AND (GP.COD_AUTORITA_DELEGATA = nvl('" + aSm.getCodStatoFascicolo()
					+ "', GP.COD_AUTORITA_DELEGATA ) )";

		// STUB 21/02/2005 Cerca i fascicoli per Stato Procedimento.
		if (aSm.getDescrStatoFascicolo() != null) {
			// 19/04/2011 Condizioni per data di Fine Pendenza.
			if (aSm.getDescrStatoFascicolo().toLowerCase().compareTo("pendenti") == 0) {
				if (aSm.getDataFinePendenza() != null) {
					// 16/10/2013 Vincenzo: il sistema non estrae i fascicoli definiti con data >
					// data_fine_pendenza
					// 16/10/2013 lCondizioni += " AND ( ( COD_STATO_FASCICOLO not in ('01','05','07') ";
					// 16/10/2013 lCondizioni += " AND TO_CHAR(fasc.data_inserimento,'YYYYMMDD') <= '" +
					// DateUtils.getDateToString( aSm.getDataFinePendenza(),"yyyyMMdd" ) + "' ) " ;
					if (flagEvento == "SI") {
						// lCondizioni += " AND ( ( COD_STATO_FASCICOLO not in ('07') )"; // 16/10/2013
						// lCondizioni += " OR (COD_STATO_FASCICOLO = '07' ";
						// lCondizioni += " AND ( (FASC.ID_FASCICOLO_SIUS ) = ";
						// lCondizioni += " (select FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV ";
						// lCondizioni +=
						// " left outer join DOCUMENTO_ALLEGATO da ON da.eve_id_evento = ev.id_evento AND
						// da.cod_tipo_documento in ('02','03') ";
						// // 11/12/2013
						// lCondizioni += " where EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						// lCondizioni +=
						// " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
						// lCondizioni += " AND EV.DATA_INSERIMENTO = ";
						// lCondizioni += " (select max (DATA_INSERIMENTO) from EVENTO EV2 ";
						// lCondizioni +=
						// " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS " ;
						// lCondizioni +=
						// " and (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03')";
						// // 16/10/2013 lCondizioni +=
						// " AND TO_CHAR (EV2.data_trasmissione_atti, 'YYYYMMDD') > '" +
						// DateUtils.getDateToString( aSm.getDataFinePendenza(),"yyyyMMdd" ) + "' " ;
						// lCondizioni += " AND TO_CHAR (da.data_emissione, 'YYYYMMDD') > '" +
						// DateUtils.getDateToString( aSm.getDataFinePendenza(),"yyyyMMdd" ) + "' " ;
						// lCondizioni += " ) ) ) ) ) ";

						// 17/02/2014 D.F. mod a seguito indicazioni di Umberto per recuperare la
						// DATA_DEFINIZIONE
						lCondizioni += " AND (   ( COD_STATO_FASCICOLO not in ('07') )"; // 16/10/2013
						lCondizioni += "      OR (COD_STATO_FASCICOLO = '07' ";
						lCondizioni += "          AND ( (FASC.ID_FASCICOLO_SIUS ) = ";
						lCondizioni += "                    (SELECT FAS_SIU_ID_FASCICOLO_SIUS "
								+ " FROM EVENTO EV ";
						lCondizioni += " left outer join DOCUMENTO_ALLEGATO da ON da.eve_id_evento = ev.id_evento "
								+ " AND da.cod_tipo_documento in ('01','02','03') "; // 11/12/2013
						lCondizioni += "  WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						lCondizioni += " AND (    EV.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
						lCondizioni += " AND EV.cod_esito not in ('0601','0602','0603','0604','0605') "; // new
						lCondizioni += " AND EV.flag_documento_registrato = 'S' "; // new
						lCondizioni += " ) ";
						lCondizioni += " AND EV.DATA_INSERIMENTO = (SELECT max (DATA_INSERIMENTO) "
								+ " FROM EVENTO EV2 ";
						lCondizioni += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS  ";
						lCondizioni += "      and (    EV2.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
						lCondizioni += "           AND EV2.cod_esito not in ('0601','0602','0603','0604','0605') "; // new
						lCondizioni += "           AND EV2.flag_documento_registrato = 'S' "; // new
						lCondizioni += "          ) ";
						lCondizioni += "    AND TO_CHAR (da.data_emissione, 'YYYYMMDD') > '"
								+ DateUtils.getDateToString(aSm.getDataFinePendenza(), "yyyyMMdd") + "' ";
						lCondizioni += "    ) ) ) ) ) ";

					} else {
						lCondizioni += " AND ( ( COD_STATO_FASCICOLO not in ('01','05') "; // 16/10/2013
						lCondizioni += " AND TO_CHAR(fasc.data_inserimento,'YYYYMMDD') <= '"
								+ DateUtils.getDateToString(aSm.getDataFinePendenza(), "yyyyMMdd") + "' ) "; // 16/10/2013
						lCondizioni += "  OR (COD_STATO_FASCICOLO in ('01','05') ";
						lCondizioni += " AND TO_CHAR(fasc.data_definizione,'YYYYMMDD') > '"
								+ DateUtils.getDateToString(aSm.getDataFinePendenza(), "yyyyMMdd") + "' ) ) ";
					}
				}
			}
			// 19/04/2011 Condizioni per date di Definizione.
			else if (aSm.getDescrStatoFascicolo().toLowerCase().compareTo("definiti") == 0) {
				if (aSm.getDataDefinizioneIniziale() != null) {
					if (flagEvento == "SI") {
						// lCondizioni += " AND (COD_STATO_FASCICOLO = '07' ";
						// lCondizioni += " AND (FASC.ID_FASCICOLO_SIUS, EV.ID_EVENTO ) = ";
						// lCondizioni += " (select FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO from EVENTO EV ";
						// lCondizioni += " where EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						// lCondizioni +=
						// " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
						// lCondizioni += " AND EV.DATA_INSERIMENTO = ";
						// lCondizioni += " (select max (DATA_INSERIMENTO) from EVENTO EV2 ";
						// lCondizioni +=
						// " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						// lCondizioni +=
						// " and (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03')";
						// lCondizioni += " AND TO_CHAR (EV2.data_trasmissione_atti, 'YYYYMMDD') >= '" +
						// DateUtils.getDateToString( aSm.getDataDefinizioneIniziale(),"yyyyMMdd" ) + "' ";
						// lCondizioni += " AND TO_CHAR (EV2.data_trasmissione_atti, 'YYYYMMDD') <= '" +
						// DateUtils.getDateToString( aSm.getDataDefinizioneFinale(),"yyyyMMdd" ) + "' ) ) )";

						// lCondizioni += " AND ( COD_STATO_FASCICOLO = '07' ";
						// lCondizioni += " AND (FASC.ID_FASCICOLO_SIUS, EV.ID_EVENTO ) = ";
						// lCondizioni +=
						// " (SELECT FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO " +
						// " FROM EVENTO EV ";
						// lCondizioni += " WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						// lCondizioni +=
						// " AND ( EV.COD_TIPO_PROVVEDIMENTO in ('02','03') ";
						// lCondizioni += " AND EV.COD_ESITO not in ('0601','0602','0603','0604','0605') ";
						// lCondizioni += " AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' " ;
						// lCondizioni += " ) ";
						// lCondizioni +=
						// " AND EV.DATA_INSERIMENTO = (SELECT max (DATA_INSERIMENTO) FROM EVENTO EV2 ";
						// lCondizioni += " WHERE EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						// lCondizioni += " AND ( EV2.COD_TIPO_PROVVEDIMENTO in ('02','03') ";
						// lCondizioni += " AND EV2.COD_ESITO not in ('0601','0602','0603','0604','0605') ";
						// lCondizioni += " AND EV2.FLAG_DOCUMENTO_REGISTRATO = 'S' " ;
						// lCondizioni += " ) ";
						// lCondizioni += " AND TO_CHAR (EV2.data_trasmissione_atti, 'YYYYMMDD') >= '" +
						// DateUtils.getDateToString( aSm.getDataDefinizioneIniziale(),"yyyyMMdd" ) + "' ";
						// lCondizioni += " AND TO_CHAR (EV2.data_trasmissione_atti, 'YYYYMMDD') <= '" +
						// DateUtils.getDateToString( aSm.getDataDefinizioneFinale(),"yyyyMMdd" ) + "' ";
						// lCondizioni += " ) ";
						// lCondizioni += " ) ";
						// lCondizioni += " AND DA.eve_id_evento = ev.id_evento ";
						// lCondizioni += " ) ";
						// ==================================================================
						// n.b. la query selezione un insieme di fascicoli definiti in un certo intervallo di
						// date.
						// La data definizione va recuperata dalla tabella DOCUMENTO_ALLEGATO.DATA_EMISSIONE
						// e solo dai DA di tipo 01, 02 o 03. Conta il DA con data emissione massima.
						// Sulla tabella DA possono essere presenti più record per lo stesso
						// evento ma solo uno di tipo 01, 02 o 03
						// ==================================================================

						lCondizioni += " AND (    COD_STATO_FASCICOLO = '07' ";
						lCondizioni += " AND DA.eve_id_evento = ev.id_evento ";
						lCondizioni += " AND DA.cod_tipo_documento IN ('02', '03') "; // add 29/05/2014
						// Condizione che seleziona un evento che ha collegato un DA di tipo 02-03 con
						// data_emissione massima
						lCondizioni += " AND (FASC.ID_FASCICOLO_SIUS, EV.ID_EVENTO ) = ";
						lCondizioni += " (SELECT FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO "
								+ " FROM EVENTO EV, DOCUMENTO_ALLEGATO DA ";
						lCondizioni += " WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						lCondizioni += " AND (    EV.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
						lCondizioni += " AND EV.COD_ESITO not in ('0601','0602','0603','0604','0605') ";
						lCondizioni += " AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
						lCondizioni += " ) ";
						lCondizioni += " AND DA.eve_id_evento = ev.id_evento ";
						lCondizioni += " AND DA.cod_tipo_documento IN ('01','02', '03') "; // 08/04/2014 add
																							// d.f. siu
																							// indicazione
																							// U.M.
						// La query per max(DATA_EMISSIONE) su alcuni DB sporchi restituisce più di un
						// risultato mandando in errore il sql.
						// 2 soluzioni: 1) si entra per max (ID_DOCUMENTO_ALLEGATO) ma potrebbe avere la
						// data_emissione errata
						// 2) si recupera un solo record (ROWNUM = 1) evento. Quale che sia va bene in quanto
						// comunque
						// tutti i record restituiti hanno la DATA_EMISSIONE uguale che è il parametro a cui
						// siamo interessati
						// sostituito con ID_DOCUMENTO_ALLEGATO
						lCondizioni += " AND DA.DATA_EMISSIONE = (SELECT max (DA2.DATA_EMISSIONE) ";
						// lCondizioni +=
						// " AND DA.ID_DOCUMENTO_ALLEGATO = (SELECT max (DA2.ID_DOCUMENTO_ALLEGATO) " ;
						lCondizioni += " FROM EVENTO EV2, DOCUMENTO_ALLEGATO DA2 ";
						lCondizioni += " WHERE EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
						lCondizioni += " AND (    EV2.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
						lCondizioni += " AND EV2.COD_ESITO not in ('0601','0602','0603','0604','0605') ";
						lCondizioni += " AND EV2.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
						lCondizioni += " ) ";
						lCondizioni += " AND DA2.eve_id_evento = EV2.id_evento "; // add 29/05/2014
						lCondizioni += " AND DA2.cod_tipo_documento IN ('01','02', '03')  ";
						lCondizioni += " AND TO_CHAR (DA2.DATA_EMISSIONE, 'YYYYMMDD') >= '"
								+ DateUtils.getDateToString(aSm.getDataDefinizioneIniziale(), "yyyyMMdd")
								+ "'  ";
						lCondizioni += " AND TO_CHAR (DA2.DATA_EMISSIONE, 'YYYYMMDD') <= '"
								+ DateUtils.getDateToString(aSm.getDataDefinizioneFinale(), "yyyyMMdd")
								+ "'  ";
						lCondizioni += " ) ";
						// la condizione AND ROWNUM = 1 serve per fare in modo che la query restituisca 1 solo
						// risultato anche
						// quando sono presenti due DA con stessa DATA_EMISSIONE = MAX o legati allo stesso
						// evento o ad eventi distinti
						lCondizioni += " AND ROWNUM = 1 ";
						lCondizioni += " ) ";
						lCondizioni += " ) ";
					} else {
						lCondizioni += " AND (     COD_STATO_FASCICOLO in ('01','05') ";
						lCondizioni += " AND TO_CHAR(fasc.data_definizione,'YYYYMMDD') >= '"
								+ DateUtils.getDateToString(aSm.getDataDefinizioneIniziale(), "yyyyMMdd")
								+ "' ";
						lCondizioni += " AND TO_CHAR(fasc.data_definizione,'YYYYMMDD') <= '"
								+ DateUtils.getDateToString(aSm.getDataDefinizioneFinale(), "yyyyMMdd")
								+ "' ";
						lCondizioni += " ) ";
					}
				}
			}
			// 27/05/2011 Condizioni per Stato procedimento = tutti.
			else if (aSm.getDescrStatoFascicolo().toLowerCase().compareTo("tutti") == 0) {
				if (flagEvento == "SI") {
					// lCondizioni += " AND ( (COD_STATO_FASCICOLO = '07' ";
					// lCondizioni += " AND (FASC.ID_FASCICOLO_SIUS, EV.ID_EVENTO ) = ";
					// lCondizioni += " (select FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO from EVENTO EV ";
					// lCondizioni += " where EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
					// lCondizioni +=
					// " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
					// lCondizioni += " AND EV.DATA_INSERIMENTO = ";
					// lCondizioni += " (select max (DATA_INSERIMENTO) from EVENTO EV2 ";
					// lCondizioni += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
					// lCondizioni +=
					// " and (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ) ) ) ";
					// lCondizioni += " OR ( COD_STATO_FASCICOLO in ('01','05') ) ) ";

					// 28/05/2014 aggiunta LEFT OUTER JOIN tra EVENTO e DOCUMENTO_ALLEGATO
					// per evitare prodotto cartesiano generato dalla condizione:
					// " OR COD_STATO_FASCICOLO in ('01','05') ";
					// che non imponendo where sulla DOCUMENTO_ALLEGATO genera il prodotto cartesiano
					lCondizioni += " AND ev.id_evento = DA.eve_id_evento(+) ";

					lCondizioni += " AND (   (     COD_STATO_FASCICOLO = '07' ";
					// lCondizioni += " AND DA.eve_id_evento = ev.id_evento ";
					// 28/05/2014 agiunta condizione 02-03 per evitare righe doppie gli eventi che avevano 2
					// doc allegati (ovvero il foglio complementare)
					lCondizioni += " AND (DA.eve_id_evento = ev.id_evento AND DA.cod_tipo_documento in ('01','02','03')) ";
					lCondizioni += " AND (FASC.ID_FASCICOLO_SIUS, EV.ID_EVENTO ) = ";
					lCondizioni += " (SELECT FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO ";
					lCondizioni += " FROM  EVENTO EV ";
					lCondizioni += " WHERE EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
					lCondizioni += " AND (     EV.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
					lCondizioni += " AND EV.COD_ESITO not in ('0601','0602','0603','0604','0605') ";
					lCondizioni += " AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
					lCondizioni += " ) ";
					lCondizioni += " AND EV.DATA_INSERIMENTO =  (SELECT max (DATA_INSERIMENTO) ";
					lCondizioni += " FROM EVENTO EV2 ";
					lCondizioni += " WHERE EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
					lCondizioni += " AND (    EV2.COD_TIPO_PROVVEDIMENTO in ('01','02','03') ";
					lCondizioni += " AND EV2.cod_esito not in ('0601','0602','0603','0604','0605') ";
					lCondizioni += " AND EV2.flag_documento_registrato = 'S' ";
					lCondizioni += " ) ";
					lCondizioni += " ) "; // chiude select max
					lCondizioni += " ) "; // chiude la SELECT FAS_SIU_ID_FASCICOLO_SIUS, ID_EVENTO
					lCondizioni += " ) "; //
					lCondizioni += " OR COD_STATO_FASCICOLO in ('01','05')  ";
					lCondizioni += " ) "; //
				} else {
					lCondizioni += " AND ( COD_STATO_FASCICOLO not in ('01','05','07') ) ";
				}
			}
		}

		// Ricerca per Cancelleria Assegnataria
		if (aCancAssFascSius != null) {
			if (aCancAssFascSius.getCodCancelleriaAssegnataria().compareTo("nocanc") != 0) {
				lCondizioni += " AND ID_FASCICOLO_SIUS IN (SELECT FAS_SIUS_ID_FASCICOLO_SIUS FROM CANC_ASS_FASC_SIUS WHERE COD_CANCELLERIA_ASSEGNATARIA = ";
				lCondizioni += "'" + aCancAssFascSius.getCodCancelleriaAssegnataria() + "'";
				lCondizioni += " AND COD_UFFICIO = '" + aCancAssFascSius.getCodUfficio() + "'";
				lCondizioni += " AND DATA_FINE IS NULL) ";
			} else {
				lCondizioni += " AND ID_FASCICOLO_SIUS NOT IN (SELECT FAS_SIUS_ID_FASCICOLO_SIUS FROM CANC_ASS_FASC_SIUS WHERE ";
				lCondizioni += " COD_UFFICIO = '" + aCancAssFascSius.getCodUfficio() + "'";
				lCondizioni += " AND DATA_FINE IS NULL) ";
			}

		}

		// Filtro sul Collaboratore di Giustizia
		if (aFiltroCollaboratore != null) {
			if (aFiltroCollaboratore.trim().equalsIgnoreCase("SI"))
				lCondizioni += " AND COLLA.COLL.isCollab(COD_UFFICIO, ID_FASCICOLO_SIUS) > 0 ";
			else if (aFiltroCollaboratore.trim().equalsIgnoreCase("NO"))
				lCondizioni += " AND  COLLA.COLL.isCollab(COD_UFFICIO, ID_FASCICOLO_SIUS) = 0 ";
		}

		return lCondizioni;
	}

	/**
	 * Settaggio della condizione sul NumeroSius
	 *
	 * @param aSm
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerNumero(FascicoloGPModel aSm) {
		String lCondizioni = new String();
		if (aSm.getFascicoloSiusModel().getChiaveProgr() != null)
			lCondizioni += " AND CHIAVE_PROGR  = " + aSm.getFascicoloSiusModel().getChiaveProgr();
		if (aSm.getFascicoloSiusModel().getChiaveAnno() != null)
			lCondizioni += " AND CHIAVE_ANNO  = " + aSm.getFascicoloSiusModel().getChiaveAnno();

		if (aSm.getFascicoloSiusModel().getDescrComuneUfficio() != null
				&& aSm.getFascicoloSiusModel().getDescrComuneUfficio().length() > 0)
			lCondizioni += " AND DESCR_COM_UFF.DESCRIZIONE = '"
					+ StringUtils.convertSqlString(aSm.getFascicoloSiusModel().getDescrComuneUfficio()) + "'";

		// Utilizzo getCodUfficioInserimento come veicolo per leggermi il codice ufficio
		if ((aSm.getFascicoloSiusModel().getCodUfficioInserimento() != null)
				&& (aSm.getFascicoloSiusModel().getCodUfficioInserimento().trim().length() > 1))
			lCondizioni += " AND UFF.COD_UFFICIO = '" + aSm.getFascicoloSiusModel().getCodUfficioInserimento()
					+ "'";

		// Utilizzo getChiaveUfficio come veicolo per leggermi il codice tipo ufficio
		if ((aSm.getFascicoloSiusModel().getChiaveUfficio() != null)
				&& (aSm.getFascicoloSiusModel().getChiaveUfficio().trim().length() > 1))
			lCondizioni += " AND UFF.COD_TIPO_UFFICIO = '" + aSm.getFascicoloSiusModel().getChiaveUfficio()
					+ "'";

		// STUB 10/06/2004 Aggiunte condizioni per ricerca estesa.
		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aSm.getFascicoloSiusModel().getChiaveAnnoIniziale() != null)
				&& (aSm.getFascicoloSiusModel().getChiaveAnnoIniziale().intValue() > 0)
				&& (aSm.getFascicoloSiusModel().getChiaveProgrIniziale() != null)
				&& (aSm.getFascicoloSiusModel().getChiaveProgrIniziale().intValue() > 0)) {
			// STUB 30/06/2005 miglioramento query.
			// lCondizioni +=
			// " AND ( (CHIAVE_ANNO > "+aSm.getFascicoloSiusModel().getChiaveAnnoIniziale()+")";
			// lCondizioni +=
			// " OR (CHIAVE_ANNO = "+aSm.getFascicoloSiusModel().getChiaveAnnoIniziale()+" AND CHIAVE_PROGR >=
			// "+aSm.getFascicoloSiusModel().getChiaveProgrIniziale()+"))";
			lCondizioni += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') >= "
					+ aSm.getFascicoloSiusModel().getChiaveAnnoIniziale() + "||LPAD("
					+ aSm.getFascicoloSiusModel().getChiaveProgrIniziale() + ",38,'0'))";
		}

		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if ((aSm.getFascicoloSiusModel().getChiaveAnnoFinale() != null)
				&& (aSm.getFascicoloSiusModel().getChiaveAnnoFinale().intValue() > 0)
				&& (aSm.getFascicoloSiusModel().getChiaveProgrFinale() != null)
				&& (aSm.getFascicoloSiusModel().getChiaveProgrFinale().intValue() > 0)) {
			// STUB 30/06/2005 miglioramento query.
			// lCondizioni += " AND ( (CHIAVE_ANNO < "+aSm.getFascicoloSiusModel().getChiaveAnnoFinale()+")";
			// lCondizioni +=
			// " OR (CHIAVE_ANNO = "+aSm.getFascicoloSiusModel().getChiaveAnnoFinale()+" AND CHIAVE_PROGR <=
			// "+aSm.getFascicoloSiusModel().getChiaveProgrFinale()+"))";
			lCondizioni += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') <= "
					+ aSm.getFascicoloSiusModel().getChiaveAnnoFinale() + "||LPAD("
					+ aSm.getFascicoloSiusModel().getChiaveProgrFinale() + ",38,'0'))";
		}

		// Cerca i fascicoli con una data iscrizione.
		if ((aSm.getFascicoloSiusModel().getDataIscrizione() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE, 'DDMMYYYY') = '"
					+ DateUtils.getDateToString(aSm.getFascicoloSiusModel().getDataIscrizione(), "ddMMyyyy")
					+ "' ";

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aSm.getFascicoloSiusModel().getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '" + DateUtils.getDateToString(
					aSm.getFascicoloSiusModel().getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aSm.getFascicoloSiusModel().getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '" + DateUtils
					.getDateToString(aSm.getFascicoloSiusModel().getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		return lCondizioni;
	}

	protected String getFascicoloSiusSqlSoggettoQuery() {
		String lStatement = new String();
		lStatement += " SELECT FASC.ID_FASCICOLO_SIEP ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += " SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO, TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO,";
		lStatement += " SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE,TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE,";
		lStatement += " SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
		lStatement += " SENT.DATA_IRREVOCABILITA DATA_IRREVOCABILITA,FASC.CHIAVE_ANNO CHIAVE_ANNO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO , DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		lStatement += " FROM FASCICOLO_SIEP FASC, SENTENZA SENT, SOGGETTO SOGG, CG_REF_CODES TIPO_PROVVEDIMENTO,";
		lStatement += " CG_REF_CODES TIPO_AUTORITA_EMITTENTE, COMUNE LUOGO_EMITTENTE, UFFICIO UFF, ";
		lStatement += " CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.SEN_ID_SENTENZA = SENT.ID_SENTENZA";
		lStatement += " AND FASC.FLAG_VALIDATO = 'S'";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = SENT.COD_TIPO_PROVVEDIMENTO ";
		lStatement += " AND TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = SENT.COD_TIPO_AUTORITA_EMITTENTE";
		lStatement += " AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQuery() {
		String lStatement = new String();

		// Query relativa soltanto al fascicolo SIUS

		// Modifica del 05/09/2013 "Implementazione SIES per accorpamento uffici"
		// Recuperati campi DATA_DEFINIZIONE e DATA_DEFINIZIONE_EVENTO per correggere errore preesistente
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA, FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, null DATA_DEFINIZIONE_EVENTO,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA,";
		lStatement += " NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryForStorico() {
		String lStatement = new String();

		// Query relativa soltanto al fascicolo SIUS

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_INSERIMENTO DATA_INSERIMENTO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA,CG_REF_CODES STATO_FASCICOLO";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND ( STATO_FASCICOLO.RV_DOMAIN  = 'STATO_FASCICOLO' AND NVL(STATO_FASCICOLO.RV_LOW_VALUE,'-') = FASC.COD_STATO_FASCICOLO)";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryTenore() {
		String lStatement = new String();

		// Query relativa soltanto al fascicolo SIUS

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO VERO_COD_STATO, EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,   ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,";
		lStatement += " CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV2.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement +=
		// " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += "	AND   EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ) ) ";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryTenoreEvento() {
		String lStatement = new String();
		// Query relativa soltanto al fascicolo senza l'evento.
		lStatement += "  SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO,UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, ";
		lStatement += " SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, '-' DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, ";
		lStatement += " GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO,  ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO VERO_COD_STATO, null DATA_RICHIESTA, ";
		lStatement += " GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP, ";
		lStatement += "  UFFICIO UFF, COMUNE DESCR_COM_UFF, ";
		lStatement += " CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryTenoreNotInEvento(SoggettoModel aModel) {
		String lStatement = new String();

		// Query relativa soltanto al fascicolo senza l'evento.
		lStatement += "  SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO,UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, ";
		lStatement += " SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, '-' DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, ";
		lStatement += " GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " '-' COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO VERO_COD_STATO, null DATA_RICHIESTA, ";
		lStatement += " GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, ";
		lStatement += "  UFFICIO UFF, COMUNE DESCR_COM_UFF, ";
		lStatement += " CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2   ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV2.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS  ";
		// lStatement +=
		// " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += "	AND   EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ) ) ";
		// lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS ";
		lStatement += setCondizione(aModel);
		lStatement += " ) ";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryPerEstremiSenzaEvento() {
		String lStatement = new String();
		// Query relativa soltanto al fascicolo SIUS
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, ";
		lStatement += " SOGG.ID_SOGGETTO ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		// lStatement += " GP.DATA_ARRIVO_CANCELLERIA "; 22/12/2008
		lStatement += " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, null DATA_DEFINIZIONE_EVENTO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, ";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";

		return lStatement;
	}

	/**
	 * Nuova query specifica per l'estrazione dei procedimenti "pendenti"
	 *
	 * @return
	 * @since 17/02/2014
	 */
	protected String getFascicoloSiusSqlQueryPerEstremiPendenti(FascicoloSiusModel aFascModel,
			boolean aIsJoinPendenti) {
		String lStatement = new String();
		// Query relativa soltanto al fascicolo SIUS
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += "       FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += "       FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, ";
		lStatement += "       SOGG.ID_SOGGETTO ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += "       DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,";
		lStatement += "       DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += "       DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += "       GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += "       GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO ";

		if (aIsJoinPendenti)
			lStatement += " , da.data_emissione data_definizione_evento "; // serve per recuperare la data
																			// definizione
		else
			lStatement += " , null data_definizione_evento ";

		// MOD d.f. 05/06/2014 su 9.2.0.4.0 NA e 9.2.0.7.0 RM restituiva ORA--01799
		// lStatement += " FROM FASCICOLO_SIUS FASC " ;
		// lStatement +=
		// " LEFT OUTER JOIN evento ev ON ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +

		/*
		 * lStatement += " FROM EVENTO ev  " ; lStatement +=
		 * " RIGHT OUTER JOIN FASCICOLO_SIUS FASC ON ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
		 * "  AND ev.id_evento = ( SELECT MAX (id_evento) " + " FROM evento " +
		 * " WHERE fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius " +
		 * " AND cod_tipo_provvedimento IN ('02','03') " +
		 * " AND ev.cod_esito NOT IN ('0601','0602','0603','0604','0605') " +
		 * " AND ev.flag_documento_registrato = 'S' ) " ; lStatement +=
		 * " LEFT OUTER JOIN documento_allegato da ON da.eve_id_evento = ev.id_evento " +
		 * " AND da.cod_tipo_documento IN ('02','03' ) ";
		 */
		// ==========================================================================
		// eliminata la select MAX annidata per errore su ROMA 9.2.0.7.0 RM restituiva ORA--01799
		// la queri adesso restituisce più record per ogni Fascicolo (uno per ogni evento), la acation deve
		// filtrare
		// n.b. aggiunto order by id_evento desc per ordinare comunue i record dal max
		/*
		 * lStatement +=
		 * "FROM FASCICOLO_SIUS FASC LEFT OUTER JOIN evento ev ON ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius "
		 * ; lStatement += " AND ev.cod_tipo_provvedimento IN ('02', '03') "; lStatement +=
		 * " AND ev.cod_esito NOT IN ('0601', '0602', '0603', '0604', '0605') "; lStatement +=
		 * " AND ev.flag_documento_registrato = 'S' ";
		 *
		 * lStatement += " LEFT OUTER JOIN documento_allegato da ON da.eve_id_evento = ev.id_evento " +
		 * " AND da.cod_tipo_documento IN ('02','03' ) ";
		 */
		// ==========================================================================

		// ==========================================================================
		// 29/07/2014 Workaround per errore su ROMA 9.2.0.7.0 RM restituiva ORA--01799
		// per la query annidata (Select MAX) nelle outer join (left/right)
		// Si crea una tabella fittizia (evento_app) caricata con id_evento max per ogni fascicolo sius
		// dell'ufficio e si mette in LEFT OUTER JOIN con FASCICOLO_SIUS
		// n.b. è lenta, necessita di indice su FASC.CHIAVE_UFFICIO e non viene
		// utilizzata in caso di ricerca paginata ma solo nell'estrazione dei
		// dati del foglio excel dove la lentezza e tollerabile
		if (aIsJoinPendenti) {
			lStatement += "FROM FASCICOLO_SIUS FASC LEFT OUTER JOIN ( ";
			lStatement += " SELECT ev.id_evento, ev.fas_siu_id_fascicolo_sius ";
			lStatement += " FROM evento ev, FASCICOLO_SIUS FASC ";
			lStatement += " WHERE ev.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius ";
			lStatement += " AND FASC.CHIAVE_UFFICIO = '" + aFascModel.getCodUfficioInserimento() + "' ";
			lStatement += " AND TO_CHAR (FASC.DATA_INSERIMENTO, 'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aFascModel.getDataFinePendenza(), "yyyyMMdd") + "'";

			if ((aFascModel.getDataIscrizioneIniziale() != null))
				lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
						+ DateUtils.getDateToString(aFascModel.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

			// Cerca i fascicoli fino ad una data iscrizione.
			if ((aFascModel.getDataIscrizioneFinale() != null))
				lStatement += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
						+ DateUtils.getDateToString(aFascModel.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

			lStatement += " AND ev.id_evento = (SELECT MAX (id_evento) ";
			lStatement += " FROM evento ";
			lStatement += " WHERE fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius ";
			lStatement += " AND cod_tipo_provvedimento IN ('02', '03') ";
			lStatement += " AND ev.cod_esito NOT IN ('0601', '0602', '0603', '0604', '0605') ";
			lStatement += " AND ev.flag_documento_registrato = 'S' ";
			lStatement += " ) ";
			lStatement += ") evento_app  ON evento_app.fas_siu_id_fascicolo_sius = fasc.id_fascicolo_sius ";
			lStatement += " LEFT OUTER JOIN documento_allegato da ON da.eve_id_evento = evento_app.id_evento ";
			lStatement += " AND da.cod_tipo_documento IN ('02', '03') ";
		} else {
			lStatement += " FROM FASCICOLO_SIUS FASC ";
		}

		lStatement += "	   , SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, ";
		lStatement += "      CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += "      COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += "   AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += "   AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += "   AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += "   AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += "   AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += "   AND TO_CHAR (FASC.DATA_INSERIMENTO, 'YYYYMMDD') <= '"
				+ DateUtils.getDateToString(aFascModel.getDataFinePendenza(), "yyyyMMdd") + "'";

		return lStatement;
	}

	protected String getFascicoloSiusSqlQueryPerEstremiConEvento() {
		String lStatement = new String();
		// Query relativa soltanto al fascicolo SIUS
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_DEFINIZIONE DATA_DEFINIZIONE, ";
		lStatement += " SOGG.ID_SOGGETTO ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO,";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		// lStatement += " GP.DATA_ARRIVO_CANCELLERIA "; 22/12/2008
		lStatement += " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO";
		// lStatement += ", ev.data_trasmissione_atti DATA_DEFINIZIONE_EVENTO ";
		lStatement += ", DA.data_emissione DATA_DEFINIZIONE_EVENTO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, EVENTO EV, ";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		lStatement += " , DOCUMENTO_ALLEGATO DA "; // 17/02/2014 aggiunta join con DOCUMENTO_ALLEGATO per
													// recuperare la "data Definizione evento"
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = EV.FAS_SIU_ID_FASCICOLO_SIUS(+)";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";

		return lStatement;
	}

	protected String getFascicoloSiusUnicoSqlQuery() {
		String lStatement = new String();

		// Query relativa soltanto al fascicolo SIUS
		lStatement += " SELECT  count(*) NUM_FASCICOLI, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, FASC.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";

		return lStatement;
	}

	protected String getFascicoloSiusPerNumeroSqlQuery() {
		return getFascicoloSiusPerNumeroSqlQuery("");
	}

	protected String getFascicoloSiusPerNumeroSqlQuery(String majorOffice) {
		String lStatement = new String();
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, FASC.DATA_INSERIMENTO DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO , UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		lStatement += " SOGG.ID_SOGGETTO ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.COD_PROVINCIA_NASCITA,";
		lStatement += " DESCR_OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_PROCEDIMENTO, ";
		lStatement += " POS_GIURIDICA.RV_MEANING DESCR_POS_GIURIDICA, ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, GP.COD_POSIZIONE_GIURIDICA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, GP.DATA_FINE_PENA ";
		lStatement += " FROM SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO, CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, ";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA,  ";

		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += " FASCICOLO_SIUS FASC LEFT OUTER JOIN V_SOGSIUS_ETA vse ON (FASC.id_fascicolo_sius = vse.ID_FASCICOLO_SIUS)";
		} else {
			lStatement += " FASCICOLO_SIUS FASC ";
		}

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND POS_GIURIDICA.RV_DOMAIN = 'POSIZIONE_GIURIDICA' ";
		lStatement += " AND (nvl (GP.COD_POSIZIONE_GIURIDICA, '-') = POS_GIURIDICA.RV_LOW_VALUE) ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += " AND FASC.ID_FASCICOLO_SIUS = vse.ID_FASCICOLO_SIUS ";
		}

		return lStatement;
	}

	private String setGroupSoggetto() {
		String lGroupBy = new String();
		lGroupBy = " group by SOGG.Cognome, SOGG.nome, SOGG.DATA_NASCITA,SOGG.COD_COMUNE_NASCITA, ";

		// paolo cherubini x supersoggetto 24 luglio 2009
		// commento la seguente riga
		// lGroupBy += " FASC.SOG_ID_SOGGETTO,";
		// fine

		lGroupBy += " DESCR_COM_NASCITA.DESCRIZIONE,SOGG.DESC_COMUNE_NASCITA_ESTERO,SOGG.COD_PROVINCIA_NASCITA  ";

		// paolo cherubini x supersoggetto 24 luglio 2009
		// aggiungo le seguenti righe
		String lSuperSogg = new String();
		lSuperSogg = ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
		lSuperSogg += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
		lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA ";
		lSuperSogg += ", sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI ";
		// sogg.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poichè non è inserito nella query
		// successiva che fa anch'essa
		// un raggruppamento ma senza questo campo Paolo 05/08/2010
		lGroupBy += lSuperSogg;
		// fine

		return lGroupBy;
	}

	// private String setOrder() {
	// String lOrder = new String();
	// lOrder = " ORDER BY 2, 3";
	// return lOrder;
	// }

	// private String setOrderSoggetto() {
	// String lOrder = new String();
	// lOrder = " ORDER BY COGNOME, NOME, FASC.CHIAVE_ANNO ,FASC.CHIAVE_ANNO ";
	// return lOrder;
	// }

	private String setOrderAnnoProgr() {
		String lOrder = new String();
		lOrder = " ORDER BY FASC.CHIAVE_ANNO DESC ,FASC.CHIAVE_PROGR DESC";
		return lOrder;
	}

	private String setOrderAnnoProgrAsc() {
		String lOrder = new String();
		lOrder = " ORDER BY FASC.CHIAVE_ANNO ASC ,FASC.CHIAVE_PROGR ASC";
		return lOrder;
	}

	// private String setOrderAnnoProgrEvento() {
	// String lOrder = new String();
	// lOrder = " ORDER BY 2, 4";
	// return lOrder;
	// }

	private String setOrderUfficioAnnoProgrEvento() {
		String lOrder = new String();
		// STUB 08/04/2004 lOrder = " ORDER BY 5, 2, 4";
		lOrder = " ORDER BY CHIAVE_UFFICIO, CHIAVE_ANNO, CHIAVE_PROGR";
		return lOrder;
	}

	private String setOrderCognome() {
		String lOrder = new String();
		lOrder = " ORDER BY COGNOME, NOME ";
		return lOrder;
	}

	private String setOrderPerEstremi() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO,CHIAVE_PROGR,COGNOME, NOME ";
		return lOrder;
	}

	private String setOrderDataFinePena() {
		String lOrder = new String();
		lOrder = " ORDER BY GP.DATA_FINE_PENA DESC ";
		return lOrder;
	}

	private String setOrderDataIscrizione() {
		String lOrder = new String();
		lOrder = " ORDER BY FASC.DATA_ISCRIZIONE DESC ";
		return lOrder;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FascicoloSiusModel lFascicolo = new FascicoloSiusModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.setDescrStatoFascicolo(getString(""));
		lFascicolo.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lFascicolo.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		lFascicolo.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));

		SoggettoModel lSoggetto = new SoggettoModel();

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		/*
		 * lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		 * lSoggetto.setDataNascita(getDate("DATA_NASCITA") );
		 * lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		 * lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		 * lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA") );
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.setSoggetto(lSoggetto);
		return lFascicolo;
	}

	public GenericModel getFascicoloSiusGPModelForStorico() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		/*
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.

		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));

		return lFascicolo;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusPerNumeroSius() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		SoggettoModel lSoggetto = new SoggettoModel();

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		/*
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		// lFascicolo.getGeneraleProcedimentoModel().setDataFinePena(getDate("DATA_FINE_PENA"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusEsecuzioneMisureAlternative() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		SoggettoModel lSoggetto = new SoggettoModel();
		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		/*
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.

		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		// lFascicolo.getGeneraleProcedimentoModel().setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		// Utilizzo setDescrDefinizione come vettore per DESCR_MISURA_ALTERNATIVA
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_MISURA_ALTERNATIVA"));
		lFascicolo.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		lFascicolo.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		// Utilizzo setDataDefinizione come vettore per DATA_INIZIO_MISURA
		lFascicolo.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_INIZIO_MISURA"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	/*
	 * STUB 11/03/2005 public GenericModel getFascicoloSiusGPModel() throws DAOException { FascicoloGPModel
	 * lFascicolo = new FascicoloGPModel();
	 *
	 * lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS") );
	 * lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
	 * lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO") );
	 * lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
	 * lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
	 * lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") ); // N.B.
	 * Utilizzo CodStatoFascicolo come contenitore del cod evento prelevato dalla tabella EVENTO associata
	 * lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
	 * //lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
	 * //lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
	 * ); //lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
	 * //lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE") );
	 * //lFascicolo.getFascicoloSiusModel
	 * ().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
	 * //lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
	 * //lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
	 * lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP")
	 * );
	 *
	 * SoggettoModel lSoggetto = new SoggettoModel();
	 *
	 * lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
	 * //lSoggetto.setCodFiscale(getString("COD_FISCALE") ); //lSoggetto.setCodCs(getString("COD_CS") );
	 * //lSoggetto.setCodAfis(getString("COD_AFIS") ); lSoggetto.setCognome(getString("COGNOME") );
	 * lSoggetto.setNome(getString("NOME") );
	 *
	 * //lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
	 * lSoggetto.setDataNascita(getDate("DATA_NASCITA") );
	 * //lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
	 * //lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
	 * lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA") );
	 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
	 * //lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
	 * //lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
	 * //lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
	 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
	 * /*lSoggetto.setNazionalita(getString("NAZIONALITA") );
	 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
	 * lSoggetto.setPaternita(getString("PATERNITA") ); lSoggetto.setCognomeMadre(getString("COGNOME_MADRE")
	 * ); lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
	 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO") );
	 * lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
	 * //lSoggetto.setDescrComuneCasellario(getString("") );
	 * //lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
	 * //lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
	 * //lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
	 * //lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
	 * //lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
	 * //lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
	 * //lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
	 *
	 * lFascicolo.getFascicoloSiusModel().setSoggetto( lSoggetto );
	 *
	 * //Generale procedimento.
	 * lFascicolo.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(getBigDecimal
	 * ("ID_GENERALE_PROCEDIMENTO"));
	 * lFascicolo.getGeneraleProcedimentoModel().setCodOggettoProcedimento(getString
	 * ("COD_OGGETTO_PROCEDIMENTO"));
	 * lFascicolo.getGeneraleProcedimentoModel().setDescrOggettoProcedimento(getString
	 * ("DESCR_TIPO_PROCEDIMENTO")); //Utilizzo impropriamente DescrPosGiuridica per trasportare le
	 * informazioni relative a DESCR_COD_PROCEDIMENTO
	 * lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_COD_PROCEDIMENTO"));
	 *
	 * // Utilizzo DescrTipoAtto momentaneamente per ospitare DESCR_PROVVEDIMENTO // e DescrDefinizione per
	 * ospitare DESCR_DEFINIZIONE
	 * lFascicolo.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_PROVVEDIMENTO"));
	 * lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
	 *
	 * // utilizzo "DATA_RICHIESTA " per riportare la DATA_EMISSIONE dell'evento
	 * lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
	 * lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
	 *
	 * return lFascicolo; }
	 */

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPModelForProvv() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// N.B. Utilizzo CodStatoFascicolo come contenitore del cod evento prelevato dalla tabella EVENTO
		// associata
		lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		// Poichè CodStatoFascicolo è utilizzato per altro dato utilizzo DescrStatoFascicolo. Luigi 30-6-2005
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("VERO_COD_STATO"));
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(
		// DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(),
		// getString("VERO_COD_STATO") ));

		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );

		// STUB 11/03/2005 N.B. Utilizzo : SogIdSoggetto come contenitore dell' ID evento prelevato dalla
		// tabella EVENTO associata
		lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("ID_EVENTO"));

		lFascicolo.getFascicoloSiusModel()
				.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		// lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		// lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		/*
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_TIPO_PROCEDIMENTO"));
		// Utilizzo impropriamente DescrPosGiuridica per trasportare le informazioni relative a
		// DESCR_COD_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_COD_PROCEDIMENTO"));

		// Utilizzo DescrTipoAtto momentaneamente per ospitare DESCR_PROVVEDIMENTO
		// e DescrDefinizione per ospitare DESCR_DEFINIZIONE
		lFascicolo.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_PROVVEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));

		// utilizzo "DATA_RICHIESTA " per riportare la DATA_EMISSIONE dell'evento
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		// 11/03/2008 aggiunta "DATA_DEPOSITO"
		lFascicolo.getGeneraleProcedimentoModel().setDataAggiornamento(getDate("DATA_DEPOSITO"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusPerNumeroSIEP() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// N.B. Utilizzo CodStatoFascicolo come contenitore del cod evento prelevato dalla tabella EVENTO
		// associata
		lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		// N.B. Per CodStatoFascicolo utilizzo DescrStatoFascicolo.
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("VERO_COD_STATO"));
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );

		// STUB 24/02/2005 N.B. Utilizzo : SogIdSoggetto come contenitore dell' ID evento prelevato dalla
		// tabella EVENTO associata
		lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("ID_EVENTO"));

		lFascicolo.getFascicoloSiusModel()
				.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		SoggettoModel lSoggetto = new SoggettoModel();

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		// lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		// lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		/*
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_TIPO_PROCEDIMENTO"));
		// Utilizzo impropriamente DescrPosGiuridica per trasportare le informazioni relative a
		// DESCR_COD_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_COD_PROCEDIMENTO"));

		// Utilizzo "DATA_RICHIESTA " per riportare la DATA_EMISSIONE dell'evento
		// Utilizzo DescrTipoAtto momentaneamente per ospitare DESCR_PROVVEDIMENTO
		// e DescrDefinizione per ospitare DESCR_DEFINIZIONE
		lFascicolo.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_PROVVEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));

		lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		// Utilizzo setDataRichiesta per trasportare EV.DATA_EMISSIONE
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setSezione(getString("SEZIONE")); // 06/06/2008 Caricamento
																					// di
																					// FLAG_DOCUMENTO_REGISTRATO
																					// di EVENTO in appoggio
																					// su GP.SEZIONE.
		return lFascicolo;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getDettaglioFascicoloSiusEsecuzioneMisureAlternative() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		lFascicolo.getFascicoloSiusModel().setChiaveAnnoS22(getBigDecimal("ANNO_S07"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgrS22(getBigDecimal("PROGR_S07"));

		// Dati di SIEP
		lFascicolo.getFascicoloSiusModel().setChiaveAnnoSIEP(getBigDecimal("CHIAVE_ANNO_SIEP"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgrSIEP(getBigDecimal("CHIAVE_PROGR_SIEP"));
		lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_INSERIMENTO_SIEP"));

		SoggettoModel lSoggetto = new SoggettoModel();

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		/*
		 * lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));

		// Utilizzo setDescrDefinizione come vettore per EV.DESCR_DEFINIZIONE
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
		lFascicolo.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1"));
		lFascicolo.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1"));
		// Utilizzo setDataDefinizione come vettore per EV.DATA_EMISSIONE
		lFascicolo.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_EMISSIONE"));
		// Utilizzo setDescrMittente come vettore per EV.DESCR_TIPO_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_TIPO_PROCEDIMENTO"));

		// Utilizzo setDescrRichiestaDelegazione come vettore per EV.DESCR_PROVVEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrRichiestaDelegazione(getString("DESCR_PROVVEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrTipoMittenteAtto(getString("DESCR_MISURA_ALTERNATIVA"));
		// In setDescrUfficioAggiornamento appoggio il tipo di ufficio che ha fssato l'udienza
		lFascicolo.getGeneraleProcedimentoModel().setDescrUfficioAggiornamento(getString("TIPO_UFFICIO_EMA"));
		// In setDescrAutoritaDelegata appoggio il tipo di ufficio SIEP
		lFascicolo.getGeneraleProcedimentoModel().setDescrAutoritaDelegata(getString("TIPO_UFFICIO_SIEP"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getDettaglioFascicoloSiusGPModel() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// N.B. Utilizzo CodStatoFascicolo come contenitore del cod evento prelevato dalla tabella EVENTO
		// associata
		lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		// N.B. Per CodStatoFascicolo utilizzo DescrStatoFascicolo.
		lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("VERO_COD_STATO"));
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		SoggettoModel lSoggetto = new SoggettoModel();
		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		// lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		// lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));
		/*
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_TIPO_PROCEDIMENTO"));
		// Utilizzo impropriamente DescrPosGiuridica per trasportare le informazioni relative a
		// DESCR_COD_PROCEDIMENTO
		lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_COD_PROCEDIMENTO"));

		// Utilizzo DescrTipoAtto momentaneamente per ospitare DESCR_PROVVEDIMENTO
		// e DescrDefinizione per ospitare DESCR_DEFINIZIONE
		lFascicolo.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_PROVVEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));

		// utilizzo "DATA_RICHIESTA " per riportare la DATA_EMISSIONE dell'evento
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPModelPerEstremi() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		// lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.getFascicoloSiusModel().setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		lFascicolo.getFascicoloSiusModel().setDataDefinizioneFinale(getDate("DATA_DEFINIZIONE_EVENTO"));
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );
		// lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		// lSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// lSoggetto.setCodCs(getString("COD_CS") );
		// lSoggetto.setCodAfis(getString("COD_AFIS") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		// lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		/*
		 * lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * lSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * lSoggetto.setPaternita(getString("PATERNITA") );
		 * lSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * lSoggetto.setNomeMadre(getString("NOME_MADRE") ); lSoggetto.setSesso(getString("SESSO") );
		 * lSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); lSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.

		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA"));
		lFascicolo.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPModelUnico() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setNumFascicoli(getBigDecimal("NUM_FASCICOLI"));
		// lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
		// lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO") );
		// lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
		// lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
		// lFascicolo.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
		// lFascicolo.getFascicoloSiusModel().setDescrStatoFascicolo(getString("") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lFascicolo.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO")
		// );
		// lFascicolo.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		// lFascicolo.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS")
		// );

		SoggettoModel lSoggetto = new SoggettoModel();

		// paolo cherubini x supersoggetto 29 luglio 2009
		// inserisco le seguenti righe
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setAttoNascita(getString("ATTO_NASCITA"));
		lSoggetto.setCodAfis(getString("COD_AFIS"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setCodCs(getString("COD_CS"));
		lSoggetto.setCodFiscale(getString("COD_FISCALE"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA"));
		lSoggetto.setNazionalita(getString("NAZIONALITA"));
		lSoggetto.setPaternita(getString("PATERNITA"));
		lSoggetto.setCognomeMadre(getString("COGNOME_MADRE"));
		lSoggetto.setNomeMadre(getString("NOME_MADRE"));
		lSoggetto.setSesso(getString("SESSO"));
		lSoggetto.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		// fine aggiunta
		lSoggetto.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		lSoggetto.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		// lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// lSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		// lSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		// lSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		// lSoggetto.setNote(getString("NOTE_SOGGETTO") );
		// lSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		// lSoggetto.setDescrComuneCasellario(getString("") );
		// lSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// lSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// lSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// lSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// lSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// lSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// lSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		return lFascicolo;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPModelPerDataFinePena() throws DAOException {
		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		lFascicolo.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFascicolo.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lFascicolo.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lFascicolo.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		// Generale procedimento.
		lFascicolo.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel()
				.setDescrOggettoProcedimento(getString("DESCR_PROCEDIMENTO"));
		lFascicolo.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA"));
		lFascicolo.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE"));
		lFascicolo.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_POS_GIURIDICA"));
		lFascicolo.getGeneraleProcedimentoModel().setDataFinePena(getDate("DATA_FINE_PENA"));

		return lFascicolo;
	}

	protected String getFascicoliBySoggettoSqlQuery(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc, String majorOffice) {
		String lStatement = new String();

		lStatement += " SELECT  count(*) NUM_FASCICOLI, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, ";

		// paolo cherubini x supersoggetto 27 luglio 2009
		// lStatement += " FASC.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, ";
		lStatement += " 1 SOG_ID_SOGGETTO, ";
		// fine

		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";

		// paolo cherubini x supersoggetto 27 luglio 2009
		String lSuperSogg = new String();
		lSuperSogg = ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
		lSuperSogg += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
		lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA ";
		lSuperSogg += ", sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI ";
		// sogg.PROG_ANAG_RES, tolgo dal raggrupamento il seguente campo poichè non è inserito nella query
		// successiva che fa anch'essa
		// un raggruppamento ma senza questo campo Paolo 05/08/2010
		lStatement += lSuperSogg;
		// fine

		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";

		lStatement += " , v_sogsius_eta vse ";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";

		lStatement += " AND FASC.ID_FASCICOLO_SIUS = vse.id_fascicolo_sius ";

		if ("".equals(lIncludeArchiviati)) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		}
		// STUB 29/06/2005 Selezione dei soli fascicoli archiviati.
		else if ("A".equals(lIncludeArchiviati)) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		}
		if (!"".equals(lCodContenuto) && !"-".equals(lCodContenuto)) {
			lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + lCodContenuto + "'";
		}
		if (dataDalInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio uff where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		}

		else if (!"".equals(strCodUffOTrib)) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in ('" + strCodUffOTrib + "','"
					+ strCodUfficioUtenteConnesso + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND FASC.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";

		if (StringUtils.checkValidValue(majorOffice)) {
			lStatement += MinorMask.minorCondition("vse", "FASC", majorOffice);
		}

		return lStatement;
	}

	/**
	 * Settaggio della condizione per Data Fine Pena.
	 *
	 * @param lUfficioUtenteConnesso
	 *            ;
	 * @param dataDalIscrizione
	 *            ;
	 * @param dataAlIscrizione
	 *            ;
	 * @param dataDalFinePena
	 *            ;
	 * @param dataAlFinePena
	 *            ;
	 * @param lIncludeArchiviati
	 *            ;
	 * @param lCodPosGiuridica
	 *            ;
	 * @param lCodContenuto
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerDataFinePena(String lUfficioUtenteConnesso, Date dataDalIscrizione,
			Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena, String lIncludeArchiviati,
			String lCodPosGiuridica, String lCodContenuto) {
		String lCondizioni = new String();
		lCondizioni += " AND FASC.CHIAVE_UFFICIO ='" + lUfficioUtenteConnesso + "'";

		if (lIncludeArchiviati.equals("0")) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		}
		// STUB 29/06/2005 Selezione dei soli fascicoli archiviati.
		else if (lIncludeArchiviati.equals("A")) {
			lCondizioni += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		}

		if (!lCodContenuto.equals("") && !lCodContenuto.equals("-"))
			lCondizioni += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + lCodContenuto + "'";

		if (!lCodPosGiuridica.equals("") && !lCodPosGiuridica.equals("-"))
			lCondizioni += " AND GP.COD_POSIZIONE_GIURIDICA = '" + lCodPosGiuridica + "'";

		if (dataDalIscrizione != null)
			// lCondizioni += " AND FASC.DATA_ISCRIZIONE >= TO_DATE('" + DateUtils.getDateToString
			// (dataDalIscrizione, "ddMMyyyy") + "', 'DDMMYYYY') " ;
			lCondizioni += " AND TO_CHAR(FASC.DATA_ISCRIZIONE, 'yyyyMMdd') >= '"
					+ DateUtils.getDateToString(dataDalIscrizione, "yyyyMMdd") + "'";

		if (dataAlIscrizione != null)
			// lCondizioni += " AND FASC.DATA_ISCRIZIONE <= TO_DATE('" + DateUtils.getDateToString
			// (dataAlIscrizione, "ddMMyyyy") + "', 'DDMMYYYY') " ;
			lCondizioni += " AND TO_CHAR(FASC.DATA_ISCRIZIONE, 'yyyyMMdd') <= '"
					+ DateUtils.getDateToString(dataAlIscrizione, "yyyyMMdd") + "'";

		if (dataDalFinePena != null)
			lCondizioni += " AND GP.DATA_FINE_PENA >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalFinePena, "ddMMyyyy") + "', 'DDMMYYYY') ";

		if (dataAlFinePena != null)
			lCondizioni += " AND GP.DATA_FINE_PENA <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlFinePena, "ddMMyyyy") + "', 'DDMMYYYY') ";

		// Solo i Procedimenti non fissati (o prefissati) ad un'udiena.
		lCondizioni += " AND GP.UDI_ID_UDIENZA IS NULL ";

		return lCondizioni;
	}

	// 14/03/2008 Rimodulazione della ricerca Fascicoli Del Soggetto (separazione Ordinanze/Decreti per
	// recupero DataDeposito)
	public void ricercaFascicoliDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Ordinanze.
		lStatement += getFascicoliConOrdinanzeDelSoggetto(strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc);

		// paolo cherubini x supersoggetto 24 luglio 2009
		// inserisco la ricerca x soggetto
		lStatement += setCondizioneSuperSoggetto(aModel);
		// lStatement += setCondizione(aModel);
		// fine paolo

		lStatement += " UNION ";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Decreti.
		lStatement += getFascicoliConDecretiDelSoggetto(strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc);

		// paolo cherubini x supersoggetto 24 luglio 2009
		// inserisco la ricerca x soggetto
		lStatement += setCondizioneSuperSoggetto(aModel);
		// lStatement += setCondizione(aModel);
		// fine paolo

		lStatement += " UNION ";

		// Si costruisce la query relativa ai Fascicoli SIUS Senza Provvedimenti.
		lStatement += getFascicoliSenzaProvvedimentiDelSoggetto(aModel, strCodUfficioUtenteConnesso,
				strCodUffOTrib, lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc,
				dataAlInCanc);

		// paolo cherubini x supersoggetto 24 luglio 2009
		// inserisco la ricerca x soggetto
		lStatement += setCondizioneSuperSoggetto(aModel);
		// lStatement += setCondizione(aModel);
		// fine paolo

		lStatement += setOrderUfficioAnnoProgrEvento();
		setStatement(lStatement);
	}

	public void ricercaFascicoliPerNumeroSIEP(BigDecimal lId_FascicoloSiep) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		// lStatement += getFascicoliConProvvedimentiDelSoggetto(aModel, strCodUfficioUtenteConnesso,
		// strCodUffOTrib, lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc );
		// lStatement += setCondizione(aModel);

		lStatement += getFascicoliConProvvedimentiPerNumeroSIEP(lId_FascicoloSiep);
		lStatement += setOrderUfficioAnnoProgrEvento();
		setStatement(lStatement);
	}

	protected String getFascicoliConProvvedimentiPerNumeroSIEP(BigDecimal lId_FascicoloSiep) {
		String lStatement = new String();

		// STUB 09/11/2006 - Migliorata la query.
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP,  ";
		lStatement += "FASC.CHIAVE_ANNO CHIAVE_ANNO, EV.ID_EVENTO ID_EVENTO, FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, ";
		lStatement += "TIPO_UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += "DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA, DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += "EV.COD_ESITO COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO VERO_COD_STATO, EV.DATA_EMISSIONE DATA_RICHIESTA, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, ";
		lStatement += "DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO ";
		lStatement += ", NVL (EV.FLAG_DOCUMENTO_REGISTRATO, '-') SEZIONE "; // 06/06/2008 Caricamento di
																			// FLAG_DOCUMENTO_REGISTRATO di
																			// EVENTO in appoggio su
																			// GP.SEZIONE.
		lStatement += "FROM FASCICOLO_SIUS FASC,  GENERALE_PROCEDIMENTO GP, UFFICIO UFF, COMUNE DESCR_COM_NASCITA, SOGGETTO SOGG, ";
		lStatement += "CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO, ";
		lStatement += "CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO,CG_REF_CODES TIPO_UFFICIO, ";
		/*
		 * 13/04/2010 lStatement +=
		 * "(SELECT id_evento, max(data_inserimento) FROM evento where cod_tipo_provvedimento in ('02','03', '14') group by id_evento) EV2, EVENTO EV "
		 * ;
		 */
		// NUOVA INFRASTRUTTURA: modifica alla query per ottimizzazione in due punti
		lStatement += "(SELECT id_evento FROM evento WHERE data_inserimento in (select max(data_inserimento) FROM evento WHERE cod_tipo_provvedimento IN ('02', '03', '14') AND FAS_SIE_ID_FASCICOLO_SIEP = '"
				+ lId_FascicoloSiep
				+ "' group by fas_siu_id_fascicolo_sius) AND FAS_SIE_ID_FASCICOLO_SIEP = '"
				+ lId_FascicoloSiep + "') EV2, EVENTO EV ";
		lStatement += "WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = '" + lId_FascicoloSiep + "' ";
		lStatement += "AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += "AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO' AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += "AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO AND TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND TIPO_UFFICIO.RV_LOW_VALUE = UFF.COD_TIPO_UFFICIO ";
		/*
		 * 15/10/2010 lStatement +=
		 * "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE AND EV2.ID_EVENTO=EV.ID_EVENTO AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS and DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE and DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE "
		 * ;
		 */
		lStatement += "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE AND EV2.ID_EVENTO=EV.ID_EVENTO AND EV.COD_TIPO_PROVVEDIMENTO IN ('02', '03', '14') AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS and DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' and EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE and DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
		lStatement += "AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += "UNION ";
		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += "FASC.CHIAVE_ANNO CHIAVE_ANNO, null ID_EVENTO, FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, ";
		lStatement += "TIPO_UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, ";
		lStatement += "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, ";
		lStatement += "DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA, '-' DESCR_TIPO_PROCEDIMENTO, ";
		lStatement += "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,  GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, ";
		lStatement += "GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, '-'COD_STATO_FASCICOLO, FASC.COD_STATO_FASCICOLO VERO_COD_STATO, null DATA_RICHIESTA, ";
		lStatement += "GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, '-' DESCR_PROVVEDIMENTO, '-' DESCR_DEFINIZIONE, ";
		lStatement += "DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO ";
		lStatement += ", '-' SEZIONE "; // 06/06/2008
		lStatement += "FROM FASCICOLO_SIUS FASC, COMUNE DESCR_COM_NASCITA, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, UFFICIO UFF, ";
		lStatement += "COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO, CG_REF_CODES TIPO_UFFICIO ";
		lStatement += "WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO  ";
		lStatement += "AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "AND FASC.FAS_SIE_ID_FASCICOLO_SIEP = '" + lId_FascicoloSiep + "' ";
		lStatement += "AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO' ";
		lStatement += "AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += "AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "AND TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "AND TIPO_UFFICIO.RV_LOW_VALUE = UFF.COD_TIPO_UFFICIO ";
		lStatement += "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += "AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS   ";
		lStatement += "     FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV  ";
		lStatement += "     WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += "     AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "     AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO, FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "                from EVENTO EV2 where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV2.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += "                AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) ";
		lStatement += "                        from EVENTO EV3 ";
		lStatement += "                        where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement +=
		// " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += "                          AND EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ) ) ";
		// lStatement += "AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "AND EV.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += "AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS  ) ";

		return lStatement;
	}

	protected String getFascicoliSenzaProvvedimentiDelSoggetto(SoggettoModel aModel,
			String strCodUfficioUtenteConnesso, String strCodUffOTrib, String lCodDistretto,
			String lIncludeArchiviati, String lCodContenuto, Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS,FASC.COD_STATO_FASCICOLO VERO_COD_STATO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO, ";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO,UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO , ";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA, ";
		lStatement += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, ";
		lStatement += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " '-' COD_STATO_FASCICOLO, null DATA_RICHIESTA, null ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO, ";
		lStatement += " '-' DESCR_PROVVEDIMENTO, '-'  DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, null  DATA_DEPOSITO ";
		lStatement += "FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, UFFICIO UFF, ";
		lStatement += " COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += "WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'  ";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO  ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS  ";
		lStatement += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV ";
		lStatement += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND (EV2.COD_TIPO_PROVVEDIMENTO = '02' OR EV2.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND   EV2.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS  ";
		// Patch 24/10/2008 per contemplare le unificazioni da Verbale (TIPO_PROVVEDIMENTO = 14)
		// lStatement +=
		// " AND (EV3.COD_TIPO_PROVVEDIMENTO = '02' OR EV3.COD_TIPO_PROVVEDIMENTO = '03') ) ) ";
		lStatement += " AND EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ) ) ";
		// lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02' OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += "	AND EV.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14') ";
		lStatement += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL )";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS ";
		lStatement += " ) ";
		if (lIncludeArchiviati.equals("")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		}
		// STUB 29/06/2005 Selezione dei soli fascicoli archiviati.
		else if (lIncludeArchiviati.equals("A")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		}
		if (!lCodContenuto.equals("") && !lCodContenuto.equals("-"))
			lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + lCodContenuto + "'";
		if (dataDalInCanc != null)
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		if (dataAlInCanc != null)
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		if (lCodDistretto.length() > 1)
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		else if (!strCodUffOTrib.equals(""))
			lStatement += " AND FASC.CHIAVE_UFFICIO in ('" + strCodUffOTrib + "','"
					+ strCodUfficioUtenteConnesso + "')";
		else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato lCodDistretto="3"
			lStatement += " AND FASC.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";

		return lStatement;
	}

	protected String getFascicoliConOrdinanzeDelSoggetto(String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.COD_STATO_FASCICOLO VERO_COD_STATO,FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, EV.ID_EVENTO ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,   ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, DEOR.DATA_DEPOSITO  ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, DEPOSITO_ORDINANZA_PC DEOR, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// lStatement += " AND EV2.COD_TIPO_PROVVEDIMENTO = '03' ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 04/08/2008 e successiva 24/10/2008 per contemplare le unificazioni da Verbale
		// (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND EV3.COD_TIPO_PROVVEDIMENTO = '03' ) ) ";
		lStatement += "	AND   EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')  ) ) ";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO in ('03','14')";
		lStatement += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL )";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = DEOR.ID_EVENTO_GENERATO(+) ";
		if (lIncludeArchiviati.equals("")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		}
		// Selezione dei soli fascicoli archiviati.
		else if (lIncludeArchiviati.equals("A")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		}
		if (!lCodContenuto.equals("") && !lCodContenuto.equals("-")) {
			lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + lCodContenuto + "'";
		}
		if (dataDalInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		}

		else if (!strCodUffOTrib.equals("")) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in ('" + strCodUffOTrib + "','"
					+ strCodUfficioUtenteConnesso + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND FASC.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		return lStatement;
	}

	protected String getFascicoliConDecretiDelSoggetto(String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.COD_STATO_FASCICOLO VERO_COD_STATO,FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		lStatement += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO ,";
		lStatement += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		lStatement += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
		lStatement += " EV.COD_ESITO COD_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, EV.ID_EVENTO ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		lStatement += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,   ";
		lStatement += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, DEDE.DATA_DEPOSITO  ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, DEPOSITO_DECRETO DEDE, ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2 ";
		lStatement += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// lStatement += " AND EV2.COD_TIPO_PROVVEDIMENTO = '02' ";
		lStatement += " AND EV2.DATA_INSERIMENTO = (select max (EV3.DATA_INSERIMENTO) from EVENTO EV3 ";
		lStatement += "	where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		// Patch 04/08/2008 e successiva 24/10/2008 per contemplare le unificazioni da Verbale
		// (TIPO_PROVVEDIMENTO = 14)
		// lStatement += " AND EV3.COD_TIPO_PROVVEDIMENTO = '02' ) ) ";
		lStatement += "	AND   EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')  ) ) ";
		lStatement += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		lStatement += " AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO in ('02','14')";
		lStatement += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL )";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = DEDE.ID_EVENTO_GENERATO(+) ";
		if (lIncludeArchiviati.equals("")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		}
		// Selezione dei soli fascicoli archiviati.
		else if (lIncludeArchiviati.equals("A")) {
			lStatement += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		}
		if (!lCodContenuto.equals("") && !lCodContenuto.equals("-")) {
			lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = '" + lCodContenuto + "'";
		}
		if (dataDalInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND GP.DATA_ARRIVO_CANCELLERIA <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		}

		else if (!strCodUffOTrib.equals("")) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in ('" + strCodUffOTrib + "','"
					+ strCodUfficioUtenteConnesso + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND FASC.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		return lStatement;
	}

	/**
	 * Seleziona la DATA_EMISSIONE dell'eventuale documento allegato per il fascicolo SIUS indicato. Tale data
	 * viene utilizzata come data definizione procedimento in caso di ricerche per estremi atto PEDENTI e
	 * FASC.DATA_DEFINIZIONE null
	 *
	 * @param aIdFascicoloSius
	 * @throws DAOException
	 * @since 30/07/2014 spezzata la query del metodo ricercaFascicoloSiusProcedimentoPerEstremi troppo lenta
	 *        se aIsJoinPendenti = true
	 */
	public void ricercaDataDefinizioneFinaleByIdFasc(BigDecimal aIdFascicoloSius) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ev.id_evento, ev.fas_siu_id_fascicolo_sius ";
		lStatement += " , da.data_emissione DATA_DEFINIZIONE_EVENTO ";
		lStatement += " FROM EVENTO ev LEFT OUTER JOIN DOCUMENTO_ALLEGATO da ON da.eve_id_evento = ev.id_evento ";
		lStatement += " AND da.cod_tipo_documento IN ('02', '03')  ";
		lStatement += " WHERE ev.fas_siu_id_fascicolo_sius in (" + aIdFascicoloSius + ") ";
		lStatement += " AND ev.id_evento = (SELECT MAX (id_evento) ";
		lStatement += " FROM evento ";
		lStatement += " WHERE fas_siu_id_fascicolo_sius in  (" + aIdFascicoloSius + ") ";
		lStatement += " AND ev.cod_tipo_provvedimento IN ('02', '03') ";
		lStatement += " AND ev.cod_esito NOT IN ('0601', '0602', '0603', '0604', '0605') ";
		lStatement += " AND ev.flag_documento_registrato = 'S' ";
		lStatement += " ) ";

		setStatement(lStatement);

	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 *
	 * @param sm
	 * @param codDistretto
	 * @param codFiscaleAvvocato
	 * @param codTipoUfficio
	 */
	public void ricercaSoggettiConProcedimenti(SoggettoModel sm, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info(
				"Starting Point della classe: FascicoloSiusSoggettoSqlDAO, metodo: ricercaSoggettiConProcedimenti");

		// Costruzione della query parametrizzata.
		String query = new String();
		query += " SELECT COUNT(*) NUM_FASCICOLI, XXX.Cognome, XXX.nome, XXX.DATA_NASCITA, XXX.COD_COMUNE_NASCITA, XXX.SOG_ID_SOGGETTO,";
		query += " XXX.DESCR_COMUNE_NASCITA, XXX.DESC_COMUNE_NASCITA_ESTERO, XXX.COD_PROVINCIA_NASCITA";
		query += ", XXX.COD_FISCALE, XXX.COD_CS, NULL COD_AFIS, XXX.ANNO_NASCITA, XXX.DATA_NASCITA_PRESUNTA";
		query += ", XXX.COD_STATO_NASCITA, NULL NAZIONALITA, XXX.PATERNITA, XXX.COGNOME_MADRE, XXX.NOME_MADRE";
		query += ", XXX.SESSO, XXX.ATTO_NASCITA, XXX.MESE_NASCITA";
		query += ", XXX.ETA_PRESUNTA_ANNI, XXX.ETA_PRESUNTA_MESI";
		query += " from (";
		query += " SELECT SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		query += " SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA,";
		query += " MAX(SOGG.ID_SOGGETTO) OVER(partition by SOGG.COGNOME, SOGG.NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO, SOGG.COD_COMUNE_NASCITA, sogg.ANNO_NASCITA, sogg.MESE_NASCITA, sogg.COD_AFIS) SOG_ID_SOGGETTO,";
		// query += " SOGG.ID_SOGGETTO SOG_ID_SOGGETTO,";
		query += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";
		query += ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
		query += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
		query += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA";
		query += ", sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI";
		query += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP,";
		query += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		query += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";
		query += " , v_sogsius_eta vse, AVVOCATO AVV, AVVOCATO_FASCICOLO_SIUS AFS";
		query += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		query += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		query += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		query += " AND FASC.ID_FASCICOLO_SIUS = vse.id_fascicolo_sius";
		// query += " AND FASC.COD_STATO_FASCICOLO in ('02', '10')";
		if (codDistretto.length() > 1)
			query += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio uff where uff.COD_DISTRETTO = '"
					+ codDistretto + "')";
		query += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		query += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		if (PropertyUtil.isPresent(codTipoUfficio))
			query += " AND UFF.COD_TIPO_UFFICIO = '" + codTipoUfficio + "'";
		query += " AND GP.FAS_SIU_ID_FASCICOLO_SIUS = AFS.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO";
		query += " AND AFS.DATA_FINE_VALIDITA IS NULL";
		query += " AND AVV.COD_FISCALE = '" + codFiscaleAvvocato + "'";
		query += setCondizione(sm);
		query += ") XXX"; // XXX.COD_AFIS, XXX.NAZIONALITA,
		query += " group by XXX.Cognome, XXX.nome, XXX.DATA_NASCITA,XXX.COD_COMUNE_NASCITA, XXX.SOG_ID_SOGGETTO,";
		query += " XXX.DESCR_COMUNE_NASCITA, XXX.DESC_COMUNE_NASCITA_ESTERO, XXX.COD_PROVINCIA_NASCITA";
		query += ", XXX.COD_FISCALE, XXX.COD_CS, XXX.ANNO_NASCITA, XXX.DATA_NASCITA_PRESUNTA";
		query += ", XXX.COD_STATO_NASCITA, XXX.PATERNITA, XXX.COGNOME_MADRE, XXX.NOME_MADRE";
		query += ", XXX.SESSO, XXX.ATTO_NASCITA, XXX.MESE_NASCITA ";
		query += ", XXX.ETA_PRESUNTA_ANNI, XXX.ETA_PRESUNTA_MESI ";
		query += " ORDER BY COGNOME, NOME";
		setStatement(query);
		// info per il log
		avvocaturaLogger.info("query");
	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 *
	 * @param idSoggetto
	 * @param codDistretto
	 * @param codTipoUfficio
	 * @param codFiscaleAvvocato
	 */
	public void elencoProcedimentiDelSoggetto(BigDecimal idSoggetto, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) {

		// info per il log
		avvocaturaLogger.info(
				"Starting Point della classe: FascicoloSiusSoggettoSqlDAO, metodo: elencoProcedimentiDelSoggetto");

		String query = new String();
		// scrivo prima query
		query += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.COD_STATO_FASCICOLO VERO_COD_STATO,FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO, SOGG.ID_SOGGETTO ID_SOGGETTO,";
		query += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		query += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		query += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		query += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		query += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE,";
		query += " EV.COD_ESITO COD_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, EV.ID_EVENTO ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		// Ticket#202103110112 — versione 2.4.0 - rif Ticket#202103040111 (da Versione 2.3.0 - rif. ticket
		// 20210205017)
		// query += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING||'#'||d.FLAG_DOCUMENTO_REGISTRATO DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,";
		query += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,";
		query += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, DEOR.DATA_DEPOSITO";
		query += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO,";
		// Ticket#202104060113 — Ticket#202103310113 non corretto — vers. 2.5.0.0 - rif #202103110112
		// (derivato da #202103040111 e da ticket 20210205017)
		// ORA-01719: l'operatore di join esterno (+) non consentito nell'operando di OR o IN ???
		// allora cambio outer join su EVENTO per queste due tabelle:
		// DEPOSITO_ORDINANZA_PC DEOR, DOCUMENTO_ALLEGATO D (poi commentato),
		query += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA,";
		// query += " EVENTO EV left outer join DOCUMENTO_ALLEGATO D on ev.id_evento = d.EVE_ID_EVENTO";
		// query += " and (d.flag_documento_registrato is null or d.flag_documento_registrato <> 'A') and
		// d.cod_tipo_documento in ('01', '02', '03')";
		query += " EVENTO EV left outer join DEPOSITO_ORDINANZA_PC DEOR on EV.ID_EVENTO = DEOR.ID_EVENTO_GENERATO,";
		query += " CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO,";
		query += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, AVVOCATO AVV, AVVOCATO_FASCICOLO_SIUS AFS,";
		query += " (SELECT A.COGNOME,";
		query += " A.NOME,";
		query += " NVL(A.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA,";
		query += " nvl(A.COD_COMUNE_NASCITA, '0') COD_COMUNE_NASCITA,";
		query += " nvl(A.desc_comune_nascita_estero, '0') desc_comune_nascita_estero,";
		query += " A.COD_STATO_NASCITA,";
		query += " NVL(A.DATA_NASCITA_PRESUNTA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA_PRESUNTA,";
		query += " NVL(A.ANNO_NASCITA, '0') ANNO_NASCITA,";
		query += " NVL(a.mese_nascita, '0') mese_nascita";
		query += " FROM SOGGETTO A";
		query += " WHERE A.ID_SOGGETTO = '" + idSoggetto + "') X";
		query += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		query += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2";
		query += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		// Ticket#20210416017 - problema visibilità emerso nella risoluzione del ticket n. 202104120116
		query += " AND EV2.DATA_INSERIMENTO = (select max(EV3.DATA_INSERIMENTO)"
				+ " from EVENTO EV3, DOCUMENTO_ALLEGATO D2"
				+ " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ " AND EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')"
				+ " and (ev3.id_evento = d2.EVE_ID_EVENTO and" + " d2.FLAG_DOCUMENTO_REGISTRATO = 'S'"
				+ " and d2.cod_tipo_documento in ('01', '02', '03'))))";
		query += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		query += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		query += " AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'";
		query += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		query += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND EV.COD_TIPO_PROVVEDIMENTO in ('03','14')";
		query += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		query += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		query += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		query += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		query += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		// query += " AND EV.ID_EVENTO = DEOR.ID_EVENTO_GENERATO(+)";
		if (codDistretto.length() > 1)
			query += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ codDistretto + "')";
		if (PropertyUtil.isPresent(codTipoUfficio))
			query += " AND UFF.COD_TIPO_UFFICIO = '" + codTipoUfficio + "'";
		query += " AND GP.FAS_SIU_ID_FASCICOLO_SIUS = AFS.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO";
		query += " AND AFS.DATA_FINE_VALIDITA IS NULL";
		query += " AND AVV.COD_FISCALE = '" + codFiscaleAvvocato + "'";
		query += " AND SOGG.COD_COMUNE_NASCITA = x.COD_COMUNE_NASCITA";
		query += " and nvl(SOGG.desc_comune_nascita_estero, '0') = x.desc_comune_nascita_estero";
		query += " AND SOGG.COD_STATO_NASCITA = x.COD_STATO_NASCITA";
		query += " AND SOGG.COGNOME = x.cognome";
		query += " AND SOGG.NOME = x.nome";
		query += " AND NVL(SOGG.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA";
		query += " AND NVL(sogg.DATA_NASCITA_presunta,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA_presunta";
		query += " AND NVL(SOGG.ANNO_NASCITA, '0') = x.ANNO_NASCITA";
		query += " AND NVL(SOGG.MESE_NASCITA, '0') = x.MESE_NASCITA";
		// Ticket#202103110112 — versione 2.4.0 - rif Ticket#202103040111 (da Versione 2.3.0 - rif. ticket
		// 20210205017)
		// query += " and ev.id_evento = d.EVE_ID_EVENTO(+)";
		// Ticket#202103310113 — vers. 2.5.0.0 - rif #202103110112 (derivato da #202103040111 e da ticket
		// 20210205017)
		// query += " and (d.flag_documento_registrato is null or d.flag_documento_registrato <> 'A')";
		// query += " and d.cod_tipo_documento(+) in ('01','02', '03')"; // Deposito Sentenza, Ordinanza,
		// Decreto
		query += " UNION ";
		// scrivo seconda query
		query += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.COD_STATO_FASCICOLO VERO_COD_STATO,FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		query += " FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		query += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		query += " DESCR_TIPO_PROCEDIMENTO.RV_MEANING DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
		query += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, DESCR_COM_NASCITA.COD_PROVINCIA COD_PROVINCIA_NASCITA,";
		query += " GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE,";
		query += " EV.COD_ESITO COD_STATO_FASCICOLO, EV.DATA_EMISSIONE DATA_RICHIESTA, EV.ID_EVENTO ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		query += " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_PROVVEDIMENTO, DESCR_MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_DEFINIZIONE,";
		query += " DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, DEDE.DATA_DEPOSITO";
		query += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP, CG_REF_CODES DESCR_TIPO_PROCEDIMENTO, DEPOSITO_DECRETO DEDE,";
		query += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, EVENTO EV,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO,";
		query += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_COD_PROCEDIMENTO, AVVOCATO AVV, AVVOCATO_FASCICOLO_SIUS AFS,";
		query += " (SELECT A.COGNOME,";
		query += " A.NOME,";
		query += " NVL(A.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA,";
		query += " nvl(A.COD_COMUNE_NASCITA,'0') COD_COMUNE_NASCITA, ";
		query += " nvl(A.desc_comune_nascita_estero, '0') desc_comune_nascita_estero,";
		query += " A.COD_STATO_NASCITA,";
		query += " NVL(A.DATA_NASCITA_PRESUNTA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA_PRESUNTA,";
		query += " NVL(A.ANNO_NASCITA, '0') ANNO_NASCITA,";
		query += " NVL(a.mese_nascita, '0') mese_nascita";
		query += " FROM SOGGETTO A";
		query += " WHERE A.ID_SOGGETTO = '" + idSoggetto + "') X";
		query += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		query += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2";
		query += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		// Ticket#20210416017 - problema visibilità emerso nella risoluzione del ticket n. 202104120116
		query += " AND EV2.DATA_INSERIMENTO = (select max(EV3.DATA_INSERIMENTO)"
				+ " from EVENTO EV3, DOCUMENTO_ALLEGATO D2"
				+ " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ " AND EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')"
				+ " and ((ev3.id_evento = d2.EVE_ID_EVENTO and" + " d2.FLAG_DOCUMENTO_REGISTRATO = 'S' and"
				+ " d2.cod_tipo_documento in ('01', '02', '03')) or" + " ev3.cod_esito = '0601')))";
		query += " AND DESCR_TIPO_PROCEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		query += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO'";
		query += " AND NVL(EV.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'";
		query += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE";
		query += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		query += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND EV.COD_TIPO_PROVVEDIMENTO in ('02','14')";
		query += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		query += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		query += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		query += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		query += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		query += " AND EV.ID_EVENTO = DEDE.ID_EVENTO_GENERATO(+)";
		if (codDistretto.length() > 1)
			query += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ codDistretto + "')";
		if (PropertyUtil.isPresent(codTipoUfficio))
			query += " AND UFF.COD_TIPO_UFFICIO = '" + codTipoUfficio + "'";
		query += " AND GP.FAS_SIU_ID_FASCICOLO_SIUS = AFS.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO";
		query += " AND AFS.DATA_FINE_VALIDITA IS NULL";
		query += " AND AVV.COD_FISCALE = '" + codFiscaleAvvocato + "'";
		query += " AND SOGG.COD_COMUNE_NASCITA = x.COD_COMUNE_NASCITA";
		query += " and nvl(SOGG.desc_comune_nascita_estero, '0') = x.desc_comune_nascita_estero";
		query += " AND SOGG.COD_STATO_NASCITA = x.COD_STATO_NASCITA";
		query += " AND SOGG.COGNOME = x.cognome";
		query += " AND SOGG.NOME = x.nome";
		query += " AND NVL(SOGG.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA";
		query += " AND NVL(sogg.DATA_NASCITA_presunta,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA_presunta";
		query += " AND NVL(SOGG.ANNO_NASCITA, '0') = x.ANNO_NASCITA";
		query += " AND NVL(SOGG.MESE_NASCITA, '0') = x.MESE_NASCITA";
		query += " UNION ";
		// scrivo terza query
		query += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS,FASC.COD_STATO_FASCICOLO VERO_COD_STATO, FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIE_ID_FASCICOLO_SIEP, FASC.CHIAVE_ANNO CHIAVE_ANNO,SOGG.ID_SOGGETTO ID_SOGGETTO,";
		query += " FASC.CHIAVE_PROGR CHIAVE_PROGR,FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO,UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO,";
		query += " SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		query += " '-' DESCR_TIPO_PROCEDIMENTO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, '-' DESCR_COMUNE_NASCITA,";
		query += " '-' COD_PROVINCIA_NASCITA, GP.COD_OGGETTO_PROCEDIMENTO COD_OGGETTO_PROCEDIMENTO, GP.DATA_CAMERA_CONSIGLIO DATA_CAMERA_CONSIGLIO,";
		query += " NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE,";
		query += " '-' COD_STATO_FASCICOLO, null DATA_RICHIESTA, null ID_EVENTO, GP.ID_GENERALE_PROCEDIMENTO ID_GENERALE_PROCEDIMENTO,";
		query += " '-' DESCR_PROVVEDIMENTO, '-' DESCR_DEFINIZIONE, DESCR_COD_PROCEDIMENTO.RV_MEANING DESCR_COD_PROCEDIMENTO, null DATA_DEPOSITO";
		query += " FROM FASCICOLO_SIUS FASC, SOGGETTO SOGG, GENERALE_PROCEDIMENTO GP, UFFICIO UFF,";
		query += " COMUNE DESCR_COM_UFF, CG_REF_CODES DESCR_COD_PROCEDIMENTO, AVVOCATO AVV, AVVOCATO_FASCICOLO_SIUS AFS,";
		query += " (SELECT A.COGNOME,";
		query += " A.NOME,";
		query += " NVL(A.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA,";
		query += " nvl(A.COD_COMUNE_NASCITA,'0') COD_COMUNE_NASCITA, ";
		query += " nvl(A.desc_comune_nascita_estero, '0') desc_comune_nascita_estero,";
		query += " A.COD_STATO_NASCITA,";
		query += " NVL(A.DATA_NASCITA_PRESUNTA,to_date('01/01/1900','dd/mm/yyyy')) DATA_NASCITA_PRESUNTA,";
		query += " NVL(A.ANNO_NASCITA, '0') ANNO_NASCITA,";
		query += " NVL(a.mese_nascita, '0') mese_nascita";
		query += " FROM SOGGETTO A";
		query += " WHERE A.ID_SOGGETTO = '" + idSoggetto + "') X";
		query += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		query += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND DESCR_COD_PROCEDIMENTO.RV_DOMAIN ='OGGETTO_PROCEDIMENTO'";
		query += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_COD_PROCEDIMENTO.RV_LOW_VALUE";
		query += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		query += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		query += " AND (FASC.ID_FASCICOLO_SIUS) NOT IN (select FASC2.ID_FASCICOLO_SIUS";
		query += " FROM FASCICOLO_SIUS FASC2, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP,EVENTO EV";
		query += " WHERE FASC2.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		query += " AND FASC2.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND (EV.DATA_INSERIMENTO,ID_FASCICOLO_SIUS) = (select EV2.DATA_INSERIMENTO,FAS_SIU_ID_FASCICOLO_SIUS from EVENTO EV2";
		query += " where EV2.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS";
		query += " AND   EV2.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')";
		// Ticket#20210416017 - problema visibilità emerso nella risoluzione del ticket n. 202104120116
		query += " AND EV2.DATA_INSERIMENTO = (select max(EV3.DATA_INSERIMENTO)"
				+ " from EVENTO EV3, DOCUMENTO_ALLEGATO D2"
				+ " where EV3.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ " AND EV3.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')"
				+ " and ((ev3.id_evento = d2.EVE_ID_EVENTO and" + " d2.FLAG_DOCUMENTO_REGISTRATO = 'S' and"
				+ " d2.cod_tipo_documento in ('01', '02', '03')) or" + " ev3.cod_esito = '0601')))";
		query += " AND EV.COD_TIPO_PROVVEDIMENTO in ('02', '03', '14')";
		query += " AND (EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' OR EV.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
		query += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC2.ID_FASCICOLO_SIUS";
		query += " )";
		if (codDistretto.length() > 1)
			query += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ codDistretto + "')";
		if (PropertyUtil.isPresent(codTipoUfficio))
			query += " AND UFF.COD_TIPO_UFFICIO = '" + codTipoUfficio + "'";
		query += " AND GP.FAS_SIU_ID_FASCICOLO_SIUS = AFS.FAS_SIU_ID_FASCICOLO_SIUS";
		query += " AND AFS.AVV_ID_AVVOCATO = AVV.ID_AVVOCATO";
		query += " AND AFS.DATA_FINE_VALIDITA IS NULL";
		query += " AND AVV.COD_FISCALE = '" + codFiscaleAvvocato + "'";
		query += " AND SOGG.COD_COMUNE_NASCITA = x.COD_COMUNE_NASCITA";
		query += " and nvl(SOGG.desc_comune_nascita_estero, '0') = x.desc_comune_nascita_estero";
		query += " AND SOGG.COD_STATO_NASCITA = x.COD_STATO_NASCITA";
		query += " AND SOGG.COGNOME = x.cognome";
		query += " AND SOGG.NOME = x.nome";
		query += " AND NVL(SOGG.DATA_NASCITA,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA";
		query += " AND NVL(sogg.DATA_NASCITA_presunta,to_date('01/01/1900','dd/mm/yyyy')) = x.DATA_NASCITA_presunta";
		query += " AND NVL(SOGG.ANNO_NASCITA, '0') = x.ANNO_NASCITA";
		query += " AND NVL(SOGG.MESE_NASCITA, '0') = x.MESE_NASCITA";
		query += " ORDER BY CHIAVE_UFFICIO, CHIAVE_ANNO, CHIAVE_PROGR";
		setStatement(query);
		// info per il log
		avvocaturaLogger.info("query");
	}

}