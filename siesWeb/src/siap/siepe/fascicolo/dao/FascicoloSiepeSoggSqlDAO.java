package siap.siepe.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.fascicolo.model.FascicoloSiepeRicercaModel;
import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: FascicoloSiepeSoggSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta il FascicoloSiepe+Soggetto+Attività
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class FascicoloSiepeSoggSqlDAO extends SIAPSqlDAO {

	public FascicoloSiepeSoggSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	protected String getSqlQuery() {
		String lStatement = new String("");
		lStatement += " SELECT ID_FASCICOLO_SIEPE, CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCRIZIONE_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, NUM_UEPE, ANNO_UEPE, PROGR_UEPE, COD_STATO_FASCICOLO, "
				+ "FASC.COD_OPERATORE_INSERIMENTO, DATA_ISCRIZIONE, FASC.DATA_INSERIMENTO, FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO,"
				+ "FASC.DATA_AGGIORNAMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO, SOG_ID_SOGGETTO, FAS_SIE_ID_FASCICOLO_SIEP, FAS_SIU_ID_FASCICOLO_SIUS,"
				+ "COD_INCARICO, FASC.NOTE, SOGG.ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA,"
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA "
				+ "FROM FASCICOLO_SIEPE FASC, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, "
				+ "COMUNE DESCR_COM_UFF, SOGGETTO SOGG, COMUNE DESCR_COM_NASCITA "
				+ "WHERE UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO "
				+ "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE "
				+ "AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' "
				+ "AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		FascicoloSoggAttModel aFSModel = new FascicoloSoggAttModel();

		FascicoloSiepeRicercaModel aModel = new FascicoloSiepeRicercaModel();

		aModel.setIdFascicoloSiepe(getBigDecimal("ID_FASCICOLO_SIEPE"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		// aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") );
		aModel.setDescrTipoUfficio(getString("DESCRIZIONE_TIPO_UFFICIO"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		aModel.setNumUepe(getBigDecimal("NUM_UEPE"));
		aModel.setAnnoUepe(getBigDecimal("ANNO_UEPE"));
		aModel.setProgrUepe(getBigDecimal("PROGR_UEPE"));
		aModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		// aModel.setDescrStatoFascicolo(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setCodIncarico(getString("COD_INCARICO"));
		try {
			aModel.setDescrIncarico(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoIncaricoSiepe(), aModel.getCodIncarico()));
		} catch (Exception e) {
			throw new DAOException(e.toString());
		}

		aModel.setNote(getString("NOTE"));

		aFSModel.setFascicoloSiepeRicercaModel(aModel);

		// Soggetto
		SoggettoModel aSoggetto = new SoggettoModel();

		aSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		// aSoggetto.setCodFiscale(getString("COD_FISCALE") );
		// aSoggetto.setCodCs(getString("COD_CS") );
		// aSoggetto.setCodAfis(getString("COD_AFIS") );
		aSoggetto.setCognome(getString("COGNOME"));
		aSoggetto.setNome(getString("NOME"));
		// aSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
		aSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		// aSoggetto.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		// aSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
		aSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		aSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		/*
		 * aSoggetto.setDescrProvinciaNascita(getString("DESCR_PROVINCIA_NASCITA") );
		 * aSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA") );
		 * aSoggetto.setDescrStatoNascita(getString("DESCR_STATO_NASCITA") );
		 * aSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
		 * aSoggetto.setNazionalita(getString("NAZIONALITA") );
		 * aSoggetto.setDescrNazionalita(getString("DESCR_NAZIONALITA"));
		 * aSoggetto.setPaternita(getString("PATERNITA") );
		 * aSoggetto.setCognomeMadre(getString("COGNOME_MADRE") );
		 * aSoggetto.setNomeMadre(getString("NOME_MADRE") ); aSoggetto.setSesso(getString("SESSO") );
		 * aSoggetto.setAttoNascita(getString("ATTO_NASCITA") ); aSoggetto.setNote(getString("NOTE_SOGGETTO")
		 * ); aSoggetto.setCodComuneCasellario(getString("COD_COMUNE_CASELLARIO") );
		 */
		// aSoggetto.setDescrComuneCasellario(getString("") );
		// aSoggetto.setFlagPresenzaFascicolo(getString("FLAG_PRESENZA_FASCICOLO") );
		// aSoggetto.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
		// aSoggetto.setDataInserimento(getDate("DATA_INSERIMENTO") );
		// aSoggetto.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
		// aSoggetto.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
		// aSoggetto.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
		// aSoggetto.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );

		aFSModel.setSoggettoModel(aSoggetto);

		return aFSModel;
	}

	public String setCondizione(FascicoloSiepeModel aModel) {
		return new String();
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_FASCICOLO_SIEPE = " + aKey;
	}

	public void ricercaFascicoloSiepeSoggAtt(FascicoloSiepeRicercaModel aModel) {
		String FascicoloSiepeSoggAtt = "";

		// Costruisco la query relativa soltanto al model fascicolo SIEPE
		// FascicoloSiepePerModel += getFascicoloSiusPerNumeroSqlQuery();
		FascicoloSiepeSoggAtt += getSqlQuery();
		FascicoloSiepeSoggAtt += setCondizionePerModel(aModel);

		if ((aModel.getChiaveAnnoFinale() != null && aModel.getChiaveAnnoFinale().intValue() > 0)
				|| (aModel.getChiaveAnnoIniziale() != null && aModel.getChiaveAnnoIniziale().intValue() > 0))
			FascicoloSiepeSoggAtt += setOrderAnnoProgrAsc();
		else
			FascicoloSiepeSoggAtt += setOrderAnnoProgrDesc();

		// settaggio della stringa SQL appena costruita prima della query
		setStatement(FascicoloSiepeSoggAtt);
	}

	/**
	 * Settaggio delle condizioni da FascicoloSiepeModel
	 * 
	 * @param aSm
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerModel(FascicoloSiepeRicercaModel aSm) {
		String lCondizioni = new String();
		if (aSm.getChiaveProgr() != null)
			lCondizioni += " AND CHIAVE_PROGR  = " + aSm.getChiaveProgr();
		if (aSm.getChiaveAnno() != null)
			lCondizioni += " AND CHIAVE_ANNO  = " + aSm.getChiaveAnno();

		if (aSm.getAnnoUepe() != null)
			lCondizioni += " AND ANNO_UEPE  = " + aSm.getAnnoUepe();
		if (aSm.getNumUepe() != null)
			lCondizioni += " AND NUM_UEPE  = " + aSm.getNumUepe();
		if (aSm.getProgrUepe() != null)
			lCondizioni += " AND PROGR_UEPE  = " + aSm.getProgrUepe();

		if (aSm.getDescrComuneUfficio() != null && aSm.getDescrComuneUfficio().length() > 0)
			lCondizioni += " AND DESCR_COM_UFF.DESCRIZIONE = '"
					+ StringUtils.convertSqlString(aSm.getDescrComuneUfficio()) + "'";

		// Utilizzo getCodUfficioInserimento come veicolo per leggermi il codice ufficio
		if ((aSm.getCodUfficioInserimento() != null) && (aSm.getCodUfficioInserimento().trim().length() > 1))
			lCondizioni += " AND UFF.COD_UFFICIO = '" + aSm.getCodUfficioInserimento() + "'";

		// Utilizzo getChiaveUfficio come veicolo per leggermi il codice tipo ufficio
		if ((aSm.getChiaveUfficio() != null) && (aSm.getChiaveUfficio().trim().length() > 1))
			lCondizioni += " AND UFF.COD_TIPO_UFFICIO = '" + aSm.getChiaveUfficio() + "'";

		// Cerca i fascicoli a partire da una coppia Progressivo/Anno
		if ((aSm.getChiaveAnnoIniziale() != null) && (aSm.getChiaveAnnoIniziale().intValue() > 0)
				&& (aSm.getChiaveProgrIniziale() != null) && (aSm.getChiaveProgrIniziale().intValue() > 0)) {
			lCondizioni += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') >= " + aSm.getChiaveAnnoIniziale()
					+ "||LPAD(" + aSm.getChiaveProgrIniziale() + ",38,'0'))";
		}

		// Cerca i fascicoli fino ad una coppia Progressivo/Anno
		if ((aSm.getChiaveAnnoFinale() != null) && (aSm.getChiaveAnnoFinale().intValue() > 0)
				&& (aSm.getChiaveProgrFinale() != null) && (aSm.getChiaveProgrFinale().intValue() > 0)) {
			lCondizioni += " AND ( CHIAVE_ANNO||LPAD(CHIAVE_PROGR,38,'0') <= " + aSm.getChiaveAnnoFinale()
					+ "||LPAD(" + aSm.getChiaveProgrFinale() + ",38,'0'))";
		}

		// Cerca i fascicoli con una data iscrizione.
		if ((aSm.getDataIscrizione() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE, 'DDMMYYYY') = '"
					+ DateUtils.getDateToString(aSm.getDataIscrizione(), "ddMMyyyy") + "' ";

		// Cerca i fascicoli a partire da una data iscrizione.
		if ((aSm.getDataIscrizioneIniziale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') >= '"
					+ DateUtils.getDateToString(aSm.getDataIscrizioneIniziale(), "yyyyMMdd") + "'";

		// Cerca i fascicoli fino ad una data iscrizione.
		if ((aSm.getDataIscrizioneFinale() != null))
			lCondizioni += " AND TO_CHAR(DATA_ISCRIZIONE,'YYYYMMDD') <= '"
					+ DateUtils.getDateToString(aSm.getDataIscrizioneFinale(), "yyyyMMdd") + "'";

		return lCondizioni;
	}

	private String setOrderAnnoProgrAsc() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO ASC, CHIAVE_PROGR ASC";
		return lOrder;
	}

	private String setOrderAnnoProgrDesc() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_ANNO DESC, CHIAVE_PROGR DESC";
		return lOrder;
	}

	public void ricercaFascSiepeBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFascicoliBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, lIncludeArchiviati,
				lCodIncarico, dataDal, dataAl);
		strQuery += setCondizionePerSoggetto(aModel);

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	protected String getFascicoliBySoggettoSqlQuery(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) {
		String lStatement = new String();

		lStatement += " SELECT  count(*) NUM_FASCICOLI, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, FASC.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA";
		lStatement += " FROM FASCICOLO_SIEPE FASC, SOGGETTO  SOGG, CG_REF_CODES DESCR_INCARICO,";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND DESCR_INCARICO.RV_DOMAIN = 'TIPO_INCARICO_SIEPE'";
		lStatement += " AND FASC.COD_INCARICO = DESCR_INCARICO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		// if(lIncludeArchiviati.equals(""))
		// {
		// lStatement += " AND FASC.COD_STATO_FASCICOLO in ('02', '10') ";
		// }
		// Selezione dei soli fascicoli archiviati.
		// else if(lIncludeArchiviati.equals("A"))
		// {
		// lStatement += " AND FASC.COD_STATO_FASCICOLO in ('01','05','07') ";
		// }
		if (!lCodIncarico.equals("") && !lCodIncarico.equals("-")) {
			lStatement += " AND FASC.COD_INCARICO = '" + lCodIncarico + "'";
		}
		if (dataDal != null) {
			lStatement += " AND FASC.DATA_INVIO >= TO_DATE('" + DateUtils.getDateToString(dataDal, "ddMMyyyy")
					+ "', 'DDMMYYYY') ";
		}
		if (dataAl != null) {
			lStatement += " AND GP.DATA_INVIO <= TO_DATE('" + DateUtils.getDateToString(dataAl, "ddMMyyyy")
					+ "', 'DDMMYYYY') ";
		}

		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";

		return lStatement;
	}

	private String setGroupSoggetto() {
		String lOrder = new String();
		lOrder = " group by Cognome, nome, SOGG.DATA_NASCITA,SOGG.COD_COMUNE_NASCITA, FASC.SOG_ID_SOGGETTO,DESCR_COM_NASCITA.DESCRIZIONE,SOGG.DESC_COMUNE_NASCITA_ESTERO,SOGG.COD_PROVINCIA_NASCITA  ";
		return lOrder;
	}

	private String setOrderCognome() {
		String lOrder = new String();
		lOrder = " ORDER BY COGNOME, NOME ";
		return lOrder;
	}

	/**
	 * Settaggio della condizione sul Soggetto
	 * 
	 * @param aSm
	 *            ;
	 * @return lCondizioni
	 */
	private String setCondizionePerSoggetto(SoggettoModel aSm) {
		String lCondizioni = new String();
		if (aSm.getIdSoggetto().doubleValue() == 0) {
			if (!(aSm.getCognome().equals("")))
				lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			if (!(aSm.getNome().equals("")))
				lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			if (!(aSm.getCodComuneNascita().equals("")))
				lCondizioni += " AND COD_COMUNE_NASCITA = '"
						+ StringUtils.convertSqlString(aSm.getCodComuneNascita()) + "'";
			if (!(aSm.getPaternita().equals("")))
				lCondizioni += " AND PATERNITA LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
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
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}
		return lCondizioni;
	}

	/**
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getTotaleFascicoliSiepePerSoggetto() throws DAOException {
		FascicoloSoggAttModel lFascicolo = new FascicoloSoggAttModel();

		lFascicolo.getFascicoloSiepeRicercaModel().setNumFascicoli(getBigDecimal("NUM_FASCICOLI"));
		lFascicolo.getFascicoloSiepeRicercaModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));

		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		lFascicolo.setSoggettoModel(lSoggetto);

		return lFascicolo;
	}

	public void ricercaFascicoliBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFascicoliBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, lIncludeArchiviati,
				lCodIncarico, dataDal, dataAl);
		strQuery += setCondizionePerSoggetto(aModel);

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	public void ricercaFascSiepeDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getFascicoliSiepeDelSoggetto(aModel, strCodUfficioUtenteConnesso, lIncludeArchiviati,
				lCodIncarico, dataDal, dataAl);
		strQuery += setCondizionePerSoggetto(aModel);

		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	public String getFascicoliSiepeDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) {
		String lStatement = new String();

		lStatement += " SELECT ID_FASCICOLO_SIEPE, CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCRIZIONE_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, NUM_UEPE, ANNO_UEPE, PROGR_UEPE, COD_STATO_FASCICOLO, "
				+ "FASC.COD_OPERATORE_INSERIMENTO, DATA_ISCRIZIONE, FASC.DATA_INSERIMENTO, FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO,"
				+ "FASC.DATA_AGGIORNAMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO, SOG_ID_SOGGETTO, FAS_SIE_ID_FASCICOLO_SIEP, FAS_SIU_ID_FASCICOLO_SIUS,"
				+ "COD_INCARICO, FASC.NOTE, SOGG.ID_SOGGETTO, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA DATA_NASCITA,"
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA "
				+ "FROM FASCICOLO_SIEPE FASC, UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, "
				+ "COMUNE DESCR_COM_UFF, SOGGETTO SOGG, COMUNE DESCR_COM_NASCITA "
				+ "WHERE UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO "
				+ "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE "
				+ "AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' "
				+ "AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";
		if (!lCodIncarico.equals("") && !lCodIncarico.equals("-")) {
			lStatement += " AND FASC.COD_INCARICO = '" + lCodIncarico + "'";
		}
		if (dataDal != null) {
			lStatement += " AND FASC.DATA_INVIO >= TO_DATE('" + DateUtils.getDateToString(dataDal, "ddMMyyyy")
					+ "', 'DDMMYYYY') ";
		}
		if (dataAl != null) {
			lStatement += " AND GP.DATA_INVIO <= TO_DATE('" + DateUtils.getDateToString(dataAl, "ddMMyyyy")
					+ "', 'DDMMYYYY') ";
		}
		return lStatement;
	}

}