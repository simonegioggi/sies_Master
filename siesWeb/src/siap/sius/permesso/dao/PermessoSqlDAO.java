package siap.sius.permesso.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.permesso.model.LicenzaModel;
import siap.sius.permesso.model.PermessoModel;
import siap.sius.permesso.model.ProvvedimentoPermessoLicenzaModel;

/**
 * PermessoSqlDAO - Realizza Sql DAO del Fascicolo Sius relativi a permessi
 *
 * @version 1.0
 */
public class PermessoSqlDAO extends SIAPSqlDAO {

	public PermessoSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaPermessiBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) {
		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getPermessiBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeRigettati, lCodPermesso, dataDalInCanc, dataAlInCanc);
		strQuery += setCondizione(aModel);

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	protected String getPermessiBySoggettoSqlQuery(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += " SELECT COUNT(*) NUM_FASCICOLI, SOGG.COGNOME COGNOME, SOGG.NOME NOME, ";
		lStatement += "	SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO,";
		lStatement += " SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA,";

		// paolo cherubini allineo modifiche x supersoggetto 14 settembre 2010
		// lStatement += " FASC.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, ";
		lStatement += " 1 SOG_ID_SOGGETTO ";

		String lSuperSogg = new String();
		lSuperSogg = ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
		lSuperSogg += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
		// MERGE v10 COLLAUDO: aggiunta estrazione campi eta' presunta
		lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA, sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI, ";
		lStatement += lSuperSogg;
		// fine

		lStatement += " DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA ";

		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, EVENTO EV, LICENZA_LIBANTICIPATA LLA,";
		lStatement += " CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF,";
		lStatement += " COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = EV.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND EV.ID_EVENTO = LLA.EVE_ID_EVENTO";
		lStatement += " AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO'";
		lStatement += " AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += " AND EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' ";

		/*
		 * if(lIncludeRigettati.equals("")) { lStatement += " AND ( EV.COD_ESITO = '0001' "; lStatement +=
		 * " OR EV.COD_ESITO = '0013' "; lStatement += " OR EV.COD_ESITO = '0006' )"; }
		 * if(lCodPermesso.equals("2020")) { lStatement += " AND (EV.COD_MOTIVO = '2020' "; lStatement +=
		 * " OR EV.COD_MOTIVO = '2250' "; lStatement += " OR EV.COD_MOTIVO = '2320' )"; }
		 * if(lCodPermesso.equals("2021")) { lStatement += " AND (EV.COD_MOTIVO = '2021' "; lStatement +=
		 * "   OR EV.COD_MOTIVO = '2321' )"; }
		 */

		if (lIncludeRigettati.equals("")) {
			lStatement += " AND ( EV.COD_ESITO = '0001' ";
			lStatement += " OR EV.COD_ESITO = '0013' ";
			lStatement += " OR EV.COD_ESITO = '0006' ) ";
		}

		if (lCodPermesso.equals("2020")) {
			lStatement += " AND (EV.COD_MOTIVO = '2020' ";
			lStatement += " OR EV.COD_MOTIVO = '2250' ";
			lStatement += " OR EV.COD_MOTIVO = '2320' ) ";
		} else if (lCodPermesso.equals("2021")) {
			lStatement += " AND (EV.COD_MOTIVO = '2021' ";
			lStatement += "   OR EV.COD_MOTIVO = '2321' ) ";
		} else if (lCodPermesso.equals("2680")) {
			lStatement += " AND EV.COD_MOTIVO = '2680' ";
		}

		if (dataDalInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		} else if (!strCodUffOTrib.equals("")) {
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
	 * Settaggio della condizione sul Soggetto
	 *
	 * @param aSm
	 * @return lCondizioni
	 */
	private String setCondizione(SoggettoModel aSm) {
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

	private String setGroupSoggetto() {
		String lOrder = new String();

		// paolo cherubini allineo modifiche x supersoggetto 14 settembre 2010
		// lOrder =
		// " group by Cognome, nome, SOGG.DATA_NASCITA,SOGG.COD_COMUNE_NASCITA,
		// FASC.SOG_ID_SOGGETTO,DESCR_COM_NASCITA.DESCRIZIONE,SOGG.DESC_COMUNE_NASCITA_ESTERO,SOGG.COD_PROVINCIA_NASCITA
		// ";

		lOrder = " group by Cognome, nome, SOGG.DATA_NASCITA,SOGG.COD_COMUNE_NASCITA, ";
		lOrder += " DESCR_COM_NASCITA.DESCRIZIONE,SOGG.DESC_COMUNE_NASCITA_ESTERO,SOGG.COD_PROVINCIA_NASCITA  ";

		String lSuperSogg = new String();
		lSuperSogg = ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, sogg.DATA_NASCITA_PRESUNTA";
		lSuperSogg += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, sogg.NOME_MADRE";
		// MERGE v10 COLLAUDO: aggiunta estrazione campi eta' presunta
		lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA, sogg.ETA_PRESUNTA_ANNI, sogg.ETA_PRESUNTA_MESI ";
		lOrder += lSuperSogg;
		// fine

		return lOrder;

	}

	private String setOrderCognome() {
		String lOrder = new String();
		lOrder = " ORDER BY COGNOME, NOME ";
		return lOrder;
	}

	/**
	 * Carica i dati dei Fascicoli relativi a permessi.
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getFascicoloSiusGPdelPermesso() throws DAOException {

		FascicoloGPModel lFascicolo = new FascicoloGPModel();

		// Inserire le opportune set delle descrizioni!
		lFascicolo.getFascicoloSiusModel().setNumFascicoli(getBigDecimal("NUM_FASCICOLI"));
		lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		// paolo cherubini x supersoggetto 14 settembre 2010
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

		// MERGE v10 COLLAUDO: aggiunta impostazione campi
		lSoggetto.setEtaPresuntaAnni(getBigDecimal("ETA_PRESUNTA_ANNI"));
		lSoggetto.setEtaPresuntaMesi(getBigDecimal("ETA_PRESUNTA_MESI"));

		lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggetto);

		return lFascicolo;
	}

	/**
	 * Metodo per la ricerca Permessi del soggetto.
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeRigettati
	 * @param lCodPermesso
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 */
	public void ricercaPermessiDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		lStatement += getPermessiDelSoggetto(aModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeRigettati, lCodPermesso, dataDalInCanc, dataAlInCanc);

		// paolo cherubini x supersoggetto 14 settembre 2010
		// inserisco la ricerca x soggetto
		lStatement += setCondizioneSuperSoggetto(aModel);
		// lStatement += setCondizione(aModel);
		// fine paolo

		lStatement += setOrderUfficioAnnoProgrEvento();
		setStatement(lStatement);
	}

	/**
	 * Esegue la ricerca dei fascicoli in base al super soggetto
	 *
	 * @param soggetto
	 *            model
	 */
	protected String setCondizioneSuperSoggetto(SoggettoModel aModel) {
		String lCondizioni = new String();

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
	 * Ritorna query SQL, per la ricerca dei permessi del Soggetto.
	 *
	 * @param aModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeRigettati
	 * @param lCodPermesso
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @return
	 */
	protected String getPermessiDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += "SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ " SOGG.ID_SOGGETTO ID_SOGGETTO, FASC.CHIAVE_PROGR CHIAVE_PROGR, ";
		lStatement += " FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "
				+ " DESCR_TIPO_UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO,  ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ " DESCR_TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO,DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_ESITO, ";
		lStatement += " EV.ID_EVENTO ID_EVENTO, EV.COD_ESITO COD_ESITO, EV.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO, "
				+ " EV.DATA_EMISSIONE DATA_EMISSIONE,  ";
		lStatement += " LLA.NUMERO_GIORNI NUMERO_GIORNI, LLA.NUMERO_ORE NUMERO_ORE, ";
		lStatement += " LLA.NUMERO_GIORNI_NO_FRUITI NUMERO_GIORNI_NO_FRUITI, "
				+ " LLA.NUMERO_ORE_NO_FRUITE NUMERO_ORE_NO_FRUITE,  NVL(LLA.COD_ESITO,'-') ";
		lStatement += " COD_ESITO_PERMESSO, DESCR_ESITO_PERMESSO.RV_MEANING DESCR_ESITO_PERMESSO ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, EVENTO EV, LICENZA_LIBANTICIPATA LLA,  ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO,  ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_ESITO_PERMESSO, CG_REF_CODES DESCR_TIPO_PROVVEDIMENTO, CG_REF_CODES DESCR_TIPO_UFFICIO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVVEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_MOTIVO = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFFICIO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO'  ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND NVL(LLA.COD_ESITO, '-') = DESCR_ESITO_PERMESSO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_ESITO_PERMESSO.RV_DOMAIN = 'ESITO_PERMESSO_LICENZA' ";
		lStatement += " AND LLA.EVE_ID_EVENTO = EV.ID_EVENTO ";
		lStatement += " AND EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' ";

		/*
		 * if(lIncludeRigettati.equals("")) { lStatement += " AND ( EV.COD_ESITO = '0001' "; lStatement +=
		 * " OR EV.COD_ESITO = '0013' "; lStatement += " OR EV.COD_ESITO = '0006' )"; }
		 * if(lCodPermesso.equals("2020")) { lStatement += " AND (EV.COD_MOTIVO = '2020' "; lStatement +=
		 * " OR EV.COD_MOTIVO = '2250' "; lStatement += " OR EV.COD_MOTIVO = '2320' )"; }
		 * if(lCodPermesso.equals("2021")) { lStatement += " AND (EV.COD_MOTIVO = '2021' "; lStatement +=
		 * "   OR EV.COD_MOTIVO = '2321' )"; }
		 */

		if (lIncludeRigettati.equals("")) {
			lStatement += " AND ( EV.COD_ESITO = '0001' ";
			lStatement += " OR EV.COD_ESITO = '0013' ";
			lStatement += " OR EV.COD_ESITO = '0006' )";
		}

		if (lCodPermesso.equals("2020")) {
			lStatement += " AND (EV.COD_MOTIVO = '2020' ";
			lStatement += " OR EV.COD_MOTIVO = '2250' ";
			lStatement += " OR EV.COD_MOTIVO = '2320')";
		} else if (lCodPermesso.equals("2021")) {
			lStatement += " AND (EV.COD_MOTIVO = '2021' ";
			lStatement += "   OR EV.COD_MOTIVO = '2321')";
		} else if (lCodPermesso.equals("2680")) {
			lStatement += " AND EV.COD_MOTIVO = '2680' ";
		}

		if (dataDalInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO IN (SELECT UFF.COD_UFFICIO FROM UFFICIO WHERE UFF.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		}

		else if (!strCodUffOTrib.equals("")) {
			lStatement += " AND FASC.CHIAVE_UFFICIO IN ('" + strCodUffOTrib + "','"
					+ strCodUfficioUtenteConnesso + "')";
		} else if (lCodDistretto.length() != 1) // Nella ricerca per tutto il DB viene passato
												// lCodDistretto="3"
		{
			lStatement += " AND FASC.CHIAVE_UFFICIO ='" + strCodUfficioUtenteConnesso + "'";
		}
		return lStatement;
	}

	private String setOrderUfficioAnnoProgrEvento() {
		String lOrder = new String();
		lOrder = " ORDER BY CHIAVE_UFFICIO, CHIAVE_ANNO, CHIAVE_PROGR";
		return lOrder;
	}

	/**
	 * Metodo che imposta lo statement per la ricerca dei permessi depositati.
	 *
	 * @param aIdFascicoloSius
	 *            IDFascicoloSius
	 */
	public void ricercaPermessoDepositato(BigDecimal aIdFascicoloSius) {
		String lStatement = new String();

		lStatement += getPermessoLicenzaSqlQuery();
		lStatement += setPermessoLicenzaByIdFasSius(aIdFascicoloSius);
		// 20110524 PM - Aggiunto codice "2680", per Permessi Internati
		lStatement += setPermessoLicenzaCodMotivo("'2020','2021','2680'");
		lStatement += setPermessoLicenzaCondComune();

		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta lo statement per la ricerca delle licenze depositate.
	 *
	 * @param aIdFascicoloSius
	 *            IDFascicoloSius
	 */
	public void ricercaLicenzaDepositata(BigDecimal aIdFascicoloSius) {

		String lStatement = new String();

		lStatement += getPermessoLicenzaSqlQuery();
		lStatement += setPermessoLicenzaByIdFasSius(aIdFascicoloSius);
		// 20110524 - PM : Inclusione codici : 2450-2451-2452-2460-2461 per licenza internati
		// MEV_2023-35: aggiungo per Licenza pene sostitutive (LP) i codici 3130, 3150 e 3151
		lStatement += setPermessoLicenzaCodMotivo(
				"'2025','2450','2451','2452','2460','2461','3130','3150','3151'");
		lStatement += setPermessoLicenzaCondComune();

		setStatement(lStatement);
	}

	/**
	 * Metodo che imposta statement per ricerca dei Permessi / Licenze Depositati.
	 *
	 * @param aIdLicLibAnt
	 *            IDLicLibAnt.
	 */
	public void ricercaPermessoLicenzaDepositati(BigDecimal aIdLicLibAnt) {
		String lStatement = new String();

		lStatement += getPermessoLicenzaSqlQuery();
		lStatement += setPermessoLicenzaByIdLicLibAnt(aIdLicLibAnt);
		lStatement += setPermessoLicenzaCondComune();

		setStatement(lStatement);
	}

	/**
	 * Metodo utilizzto per l'individuazione di un permesso o licenza depositata. N.B.: la query in essa
	 * impostata è senza decodifica dei codici, praticamente è ridotta all'osso ed è utilizzata solo per
	 * individuare permesso/licenza depositati e ricavare le info solo del tipo e il relativo id. Il metodo è
	 * utilizzato nel dettaglio del fascisolo sius.
	 *
	 * @param aIdFascicoloSius
	 *            id Fascicolo Sius.
	 * @return LicenzaLibAnticipataModel.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public LicenzaLibAnticipataModel getTipoPermessoLicenzaDepositata(BigDecimal aIdFascicoloSius)
			throws DAOException {

		String lStatement = " SELECT LL.ID_LICENZA_LIBANTICIPATA, LL.COD_TIPO_LICENZA "
				+ " FROM LICENZA_LIBANTICIPATA LL "
				+ " INNER JOIN EVENTO EV ON EV.ID_EVENTO = LL.EVE_ID_EVENTO "
				+ " INNER JOIN DEPOSITO_DECRETO DD ON DD.ID_EVENTO_GENERATO = EV.ID_EVENTO WHERE ";

		lStatement += setPermessoLicenzaByIdFasSius(aIdFascicoloSius);
		// 20110524 PM - Aggiunto codice : "2680", per Permessi Internati
		// 20110524 PM - Aggiunti codici : "2450,2451,2452,2460,2461", per Licenze per interati.
		// MEV_2023-35: aggiungo per Licenza pene sostitutive (LP) i codici 3130, 3150 e 3151
		lStatement += setPermessoLicenzaCodMotivo(
				"'2020','2021','2025','2680','2450','2451','2452','2460','2461','3130','3150','3151'");
		lStatement += setPermessoLicenzaCondComune();

		setStatement(lStatement);

		LicenzaLibAnticipataModel lModel = null;
		start();

		if (next()) {
			lModel = new LicenzaLibAnticipataModel();
			lModel.setIdLicenzaLibanticipata(getBigDecimal("ID_LICENZA_LIBANTICIPATA"));
			lModel.setCodTipoLicenza(getString("COD_TIPO_LICENZA"));
		}

		stop();

		return lModel;
	}

	protected String getPermessoLicenzaSqlQuery() {
		String lQuery = " SELECT DD.ID_DEPOSITO_DECRETO, DD.DATA_DEPOSITO, "
				+ " DD.DATA_EMISSIONE, DD.ANNO_S72, DD.NUM_S72, DD.NOTE, "
				+ " EV.ID_EVENTO, NVL( EV.COD_MOTIVO, '-' ) AS COD_MOTIVO, "
				+ " MOTIVO_PROVVEDIMENTO.RV_MEANING AS DESCR_MOTIVO, LL.ID_LICENZA_LIBANTICIPATA, "
				+ " LL.COD_TIPO_LICENZA, LL.NUMERO_GIORNI, LL.NUMERO_ORE, "
				+ " NVL( LL.COD_ESITO, '-' ) AS COD_ESITO, "
				+ " ESITO_PERMESSO_LICENZA.RV_MEANING AS DESCR_ESITO, LL.LUOGO_SVOLGIMENTO_PROVA, "
				+ " LL.NUMERO_GIORNI_NO_FRUITI, LL.NUMERO_ORE_NO_FRUITE, "
				+ " LL.DATA_ANNOTAZIONE_ESITO, LL.COD_UFFICIO_INSERIMENTO, LL.DATA_INSERIMENTO, "
				+ " LL.COD_UFFICIO_AGGIORNAMENTO, LL.DATA_AGGIORNAMENTO FROM LICENZA_LIBANTICIPATA LL "
				+ " INNER JOIN EVENTO EV ON EV.ID_EVENTO = LL.EVE_ID_EVENTO "
				+ " INNER JOIN DEPOSITO_DECRETO DD ON DD.ID_EVENTO_GENERATO = EV.ID_EVENTO "
				+ " INNER JOIN CG_REF_CODES ESITO_PERMESSO_LICENZA "
				+ " ON ESITO_PERMESSO_LICENZA.RV_DOMAIN ='ESITO_PERMESSO_LICENZA' "
				+ " AND NVL(LL.COD_ESITO, '-') = ESITO_PERMESSO_LICENZA.RV_LOW_VALUE "
				+ " INNER JOIN CG_REF_CODES MOTIVO_PROVVEDIMENTO "
				+ " ON MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO' "
				+ " AND NVL(EV.COD_MOTIVO, '-') = MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE WHERE ";
		return lQuery;
	}

	/**
	 * Ritorna select sql per interrogazione Permesso Licenza.
	 *
	 * @return ritorna la stringa
	 */
	protected String getProvvPermessoLicenzaSqlQuery() {
		String lQuery = "SELECT DD.ID_DEPOSITO_DECRETO, DD.DATA_DEPOSITO, SG.NOME,"
				+ " SG.COGNOME, SG.DATA_NASCITA, ID.DESCRIZIONE,"
				+ " LL.ID_LICENZA_LIBANTICIPATA, LL.COD_TIPO_LICENZA, LL.NUMERO_GIORNI,"
				+ " LL.NUMERO_ORE, LL.FLAG_SCORTA, LL.COD_MOTIVO_DETENZIONE," // MEV_2025-48: aggiunto campo in estrazione
				+ " ESITO_PERMESSO_LICENZA.RV_MEANING DESCR_ESITO,"
				+ " MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_MOTIVO, FS.ID_FASCICOLO_SIUS,"
				+ " FS.CHIAVE_ANNO, FS.CHIAVE_PROGR,"
				+ " (CASE WHEN IMP.DEP_DEC_ID_DEPOSITO_DECRETO IS NULL THEN 'No' ELSE 'Si' END) ESISTE_RICORSO"
				+ " FROM DEPOSITO_DECRETO DD INNER JOIN EVENTO EV"
				+ " ON EV.ID_EVENTO = DD.ID_EVENTO_GENERATO INNER JOIN LICENZA_LIBANTICIPATA LL"
				+ " ON LL.EVE_ID_EVENTO = EV.ID_EVENTO INNER JOIN FASCICOLO_SIUS FS"
				+ " ON FS.ID_FASCICOLO_SIUS = LL.FAS_SIU_ID_FASCICOLO_SIUS INNER JOIN SOGGETTO SG"
				+ " ON FS.SOG_ID_SOGGETTO = SG.ID_SOGGETTO LEFT JOIN LUOGO_DETENZIONE LD"
				+ " ON LD.FAS_SIU_ID_FASCICOLO_SIUS = FS.ID_FASCICOLO_SIUS"
				+ " AND LD.DATA_FINE_DETENZIONE IS NULL LEFT JOIN ISTITUTO_DETENZIONE ID"
				+ " ON ID.ID_ISTITUTO_DETENZIONE = LD.IST_DET_ID_ISTITUTO_DETENZIONE"
				+ " LEFT JOIN IMPUGNAZIONE IMP"
				+ " ON IMP.DEP_DEC_ID_DEPOSITO_DECRETO = DD.ID_DEPOSITO_DECRETO"
				+ " INNER JOIN CG_REF_CODES ESITO_PERMESSO_LICENZA"
				+ " ON ESITO_PERMESSO_LICENZA.RV_DOMAIN ='ESITO_PERMESSO_LICENZA'"
				+ " AND NVL(LL.COD_ESITO, '-') = ESITO_PERMESSO_LICENZA.RV_LOW_VALUE"
				+ " INNER JOIN CG_REF_CODES MOTIVO_PROVVEDIMENTO"
				+ " ON MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'"
				+ " AND NVL(EV.COD_MOTIVO, '-') = MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE WHERE ";
		return lQuery;
	}

	/**
	 * Metodo per la ricerca dei provvedimenti permessi e licenze
	 *
	 * @param aDataIniziale
	 * @param aDataFinale
	 * @param aCodMotivo
	 * @param aCodUfficio
	 * @param aPage
	 *            MEV_2025-48: paginata la ricerca
	 */
	public void ricercaProvvedimentiPermessiLicenze(Date aDataIniziale, Date aDataFinale, String aCodMotivo,
			String aCodUfficio, int aPage) {

		String lStatement = new String();

		lStatement += getProvvPermessoLicenzaSqlQuery();
		lStatement += setRangeDataDeposito(aDataIniziale, aDataFinale);
		lStatement += setPermessoLicenzaCodMotivo(aCodMotivo);
		lStatement += setPermessoLicenzaCondComune();
		lStatement += setFascicoloChiaveUfficio(aCodUfficio);
		lStatement += setGroupBy();
		lStatement += setOrderByDataDepositoChiaveFascicolo();

		// MEV_2025-48: paginata la ricerca
		if (aPage > 0) {
			String lPaginedStatement = new String("");
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
			lStatement = lPaginedStatement;
		}

		setStatement(lStatement);
	}

	/**
	 * Ritorna il numero di provvedimenti di permessi / licenze
	 *
	 * @param aDataIniziale
	 * @param aDataFinale
	 * @param aCodMotivo
	 * @return
	 * @throws DAOException
	 */
	public int getNumProvvedimentiPermessiLicenze(Date aDataIniziale, Date aDataFinale, String aCodMotivo,
			String aCodUfficio) throws DAOException {

		// MEV_2025-48: paginata la ricerca
		ricercaProvvedimentiPermessiLicenze(aDataIniziale, aDataFinale, aCodMotivo, aCodUfficio, 0);
		return super.getNumRowsSelected().intValue();
	}

	/**
	 * Imposta filtro su Codici Motivo.
	 *
	 * @param aCodici
	 * @return
	 */
	public String setPermessoLicenzaCodMotivo(String aCodici) {

		return " AND EV.COD_MOTIVO IN (" + aCodici + ") ";
	}

	/**
	 * Imposta filtro su IDFascisoloSius presente sull'evento.
	 *
	 * @param aIdFascicoloSius
	 * @return
	 */
	public String setPermessoLicenzaByIdFasSius(BigDecimal aIdFascicoloSius) {

		return " EV.FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicoloSius + " ";
	}

	/**
	 * Imposta il Permesso/Licenza per IdLicLibAnt
	 *
	 * @param aIdLicLibAnt
	 * @return
	 */
	public String setPermessoLicenzaByIdLicLibAnt(BigDecimal aIdLicLibAnt) {

		return " LL.ID_LICENZA_LIBANTICIPATA = " + aIdLicLibAnt + " ";
	}

	/**
	 * Imposta il Permesso/Licenza valido per tutti
	 *
	 * @return
	 */
	public String setPermessoLicenzaCondComune() {

		return " AND EV.COD_TIPO_EVENTO = '01' AND EV.COD_TIPO_PROVVEDIMENTO = '02' AND "
				// MEV_2023-35: aggiungo per Licenza pene sostitutive (LP) i codici 3150 e 3151
				// gli esiti '0006' or '0013'
				// + "AND EV.COD_ESITO = '0001' "
				+ "(EV.COD_ESITO IN case when EV.COD_MOTIVO IN ('3150', '3151') then '0006' "
				+ "else '0001' end OR EV.COD_ESITO IN case "
				+ "when EV.COD_MOTIVO IN ('3150', '3151') then '0013' else '0001' end) "
				// FINE MEV_2023-35
				+ "AND EV.FLAG_DOCUMENTO_REGISTRATO = 'S' AND DD.DATA_DEPOSITO IS NOT NULL ";
	}

	/**
	 * Imposta il range di data deposito per condizione di filtro.
	 *
	 * @param aDataIniziale
	 *            data iniziale di ricerca.
	 * @param aDataFinale
	 *            data finale di ricerca.
	 * @return
	 */
	public String setRangeDataDeposito(Date aDataIniziale, Date aDataFinale) {
		String lDataIniziale = DateUtils.getDateToString(aDataIniziale, "ddMMyyyy");
		String lDataFinale = DateUtils.getDateToString(aDataFinale, "ddMMyyyy");

		return " DD.DATA_DEPOSITO BETWEEN TO_DATE('" + lDataIniziale + "','DDMMYYYY') AND TO_DATE('"
				+ lDataFinale + "','DDMMYYYY') ";
	}

	public String setFascicoloChiaveUfficio(String aCodUfficio) {
		return " AND FS.CHIAVE_UFFICIO = '" + aCodUfficio + "'";
	}

	/**
	 *
	 * @return
	 */
	public String setOrderByDataDeposito() {
		return " ORDER BY DATA_DEPOSITO ";
	}

	/**
	 *
	 * @return
	 */
	public String setOrderByDataDepositoChiaveFascicolo() {
		return " ORDER BY DATA_DEPOSITO, CHIAVE_ANNO, CHIAVE_PROGR ";
	}

	public String setGroupBy() {

		return " GROUP BY DD.ID_DEPOSITO_DECRETO, DD.DATA_DEPOSITO, SG.NOME, SG.COGNOME,"
				+ " SG.DATA_NASCITA, ID.DESCRIZIONE, LL.ID_LICENZA_LIBANTICIPATA,"
				+ " LL.COD_TIPO_LICENZA, LL.NUMERO_GIORNI, LL.NUMERO_ORE, LL.FLAG_SCORTA,"
				+ " LL.COD_MOTIVO_DETENZIONE," // MEV_2025-48: aggiunto campo in estrazione
				+ " ESITO_PERMESSO_LICENZA.RV_MEANING, MOTIVO_PROVVEDIMENTO.RV_MEANING,"
				+ " FS.ID_FASCICOLO_SIUS, FS.CHIAVE_ANNO, FS.CHIAVE_PROGR,"
				+ " IMP.DEP_DEC_ID_DEPOSITO_DECRETO ";
	}

	/**
	 * @return Il Model di Permesso
	 * @throws DAOException
	 */
	public GenericModel getPermessoModel() throws DAOException {
		PermessoModel lPermesso = new PermessoModel();

		lPermesso.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lPermesso.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lPermesso.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lPermesso.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lPermesso.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lPermesso.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lPermesso.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lPermesso.setCodEsito(getString("COD_ESITO"));
		lPermesso.setDescrEsito(getString("DESCR_ESITO"));
		lPermesso.setIdEvento(getBigDecimal("ID_EVENTO"));
		lPermesso.setCodEsitoPermesso(getString("COD_ESITO_PERMESSO"));
		lPermesso.setDescrEsitoPermesso(getString("DESCR_ESITO_PERMESSO"));
		lPermesso.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lPermesso.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lPermesso.setDataEmissione(getDate("DATA_EMISSIONE"));
		lPermesso.setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		lPermesso.setNumeroOre(getBigDecimal("NUMERO_ORE"));
		lPermesso.setNumeroGiorniNoFruiti(getBigDecimal("NUMERO_GIORNI_NO_FRUITI"));
		lPermesso.setNumeroOreNoFruite(getBigDecimal("NUMERO_ORE_NO_FRUITE"));
		lPermesso.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		return lPermesso;
	}

	/**
	 * @return Il Model di Permesso
	 * @throws DAOException
	 */
	public GenericModel getDepositoDecretoMotivazioniLicenzaModel() throws DAOException {
		DepositoDecretoMotivazioniLicenzaModel lDepDecrMotLic = new DepositoDecretoMotivazioniLicenzaModel();

		// Inizializza i Model
		lDepDecrMotLic.setEvento(new EventoModel());
		lDepDecrMotLic.setDepositoDecreto(new DepositoDecretoModel());
		lDepDecrMotLic.setLicenza(new LicenzaLibAnticipataModel());

		// Popola l'Evento
		lDepDecrMotLic.getEvento().setIdEvento(getBigDecimal("ID_EVENTO"));
		lDepDecrMotLic.getEvento().setCodMotivo(getString("COD_MOTIVO"));
		lDepDecrMotLic.getEvento().setDescrMotivo(getString("DESCR_MOTIVO"));

		// Popola il Deposito Decreto
		lDepDecrMotLic.getDepositoDecreto().setIdDepositoDecreto(getBigDecimal("ID_DEPOSITO_DECRETO"));
		lDepDecrMotLic.getDepositoDecreto().setDataDeposito(getDate("DATA_DEPOSITO"));
		lDepDecrMotLic.getDepositoDecreto().setDataEmissione(getDate("DATA_EMISSIONE"));
		lDepDecrMotLic.getDepositoDecreto().setAnnoS72(getBigDecimal("ANNO_S72"));
		lDepDecrMotLic.getDepositoDecreto().setNumS72(getBigDecimal("NUM_S72"));
		lDepDecrMotLic.getDepositoDecreto().setNote(getString("NOTE"));

		// Popola il Permesso/Licenza
		lDepDecrMotLic.getLicenza().setIdLicenzaLibanticipata(getBigDecimal("ID_LICENZA_LIBANTICIPATA"));
		lDepDecrMotLic.getLicenza().setCodTipoLicenza(getString("COD_TIPO_LICENZA"));
		lDepDecrMotLic.getLicenza().setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		lDepDecrMotLic.getLicenza().setNumeroOre(getBigDecimal("NUMERO_ORE"));
		lDepDecrMotLic.getLicenza().setCodEsito(getString("COD_ESITO"));
		lDepDecrMotLic.getLicenza().setDescrEsito(getString("DESCR_ESITO"));
		lDepDecrMotLic.getLicenza().setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
		lDepDecrMotLic.getLicenza().setNumeroGiorniNoFruiti(getBigDecimal("NUMERO_GIORNI_NO_FRUITI"));
		lDepDecrMotLic.getLicenza().setNumeroOreNoFruite(getBigDecimal("NUMERO_ORE_NO_FRUITE"));
		lDepDecrMotLic.getLicenza().setDataAnnotazioneEsito(getDate("DATA_ANNOTAZIONE_ESITO"));
		lDepDecrMotLic.getLicenza().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lDepDecrMotLic.getLicenza().setDataInserimento(getDate("DATA_INSERIMENTO"));
		lDepDecrMotLic.getLicenza().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lDepDecrMotLic.getLicenza().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));

		return lDepDecrMotLic;
	}

	/**
	 * Recupera il modello Permesso/Licenza per provvedimento
	 *
	 * @return Il Model di Permesso
	 * @throws DAOException
	 */
	public GenericModel getProvvedimentoPermessoLicenzaModel() throws DAOException {
		ProvvedimentoPermessoLicenzaModel lModel = new ProvvedimentoPermessoLicenzaModel();

		// Inizializza i Model
		lModel.setDepositoDecreto(new DepositoDecretoModel());
		lModel.setLicenza(new LicenzaLibAnticipataModel());
		lModel.setSoggetto(new SoggettoModel());
		lModel.setIstitutoDetenzione(new IstitutoDetenzioneModel());
		lModel.setFascicolo(new FascicoloSiusModel());
		lModel.setEvento(new EventoModel());

		// Popola il Deposito Decreto
		lModel.getDepositoDecreto().setIdDepositoDecreto(getBigDecimal("ID_DEPOSITO_DECRETO"));
		lModel.getDepositoDecreto().setDataDeposito(getDate("DATA_DEPOSITO"));

		// Popola l'evento
		lModel.getEvento().setDescrMotivo(getString("DESCR_MOTIVO"));

		// Popola il Permesso/Licenza
		lModel.getLicenza().setIdLicenzaLibanticipata(getBigDecimal("ID_LICENZA_LIBANTICIPATA"));
		lModel.getLicenza().setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		lModel.getLicenza().setNumeroOre(getBigDecimal("NUMERO_ORE"));
		lModel.getLicenza().setDescrEsito(getString("DESCR_ESITO"));
		lModel.getLicenza().setCodTipoLicenza(getString("COD_TIPO_LICENZA"));
		lModel.getLicenza().setFlagScorta(getString("FLAG_SCORTA"));
		// MEV_2025-48: estratto nuovo campo
		lModel.getLicenza().setCodMotivoDetenzione(getString("COD_MOTIVO_DETENZIONE"));

		// Popola Fascicolo Sius
		lModel.getFascicolo().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.getFascicolo().setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.getFascicolo().setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));

		// Popola Soggetto
		lModel.getSoggetto().setNome(getString("NOME"));
		lModel.getSoggetto().setCognome(getString("COGNOME"));
		lModel.getSoggetto().setDataNascita(getDate("DATA_NASCITA"));

		// Popola Istituto Detenzione
		lModel.getIstitutoDetenzione().setDescrizione(getString("DESCRIZIONE"));

		// Valorizza l'attributo nel caso ci siano ricorsi
		lModel.setEsisteRicorso(getString("ESISTE_RICORSO"));

		return lModel;
	}

	public void ricercaLicenzeBySoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) {

		String strQuery = "";

		// Costruzione della query parametrizzata.
		strQuery += getLicenzeBySoggettoSqlQuery(aModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeRigettati, lCodLicenza, dataDalInCanc, dataAlInCanc);
		strQuery += setCondizione(aModel);

		strQuery += setGroupSoggetto();
		strQuery += setOrderCognome();

		setStatement(strQuery);
	}

	protected String getLicenzeBySoggettoSqlQuery(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) {

		String lStatement = new String();

		lStatement += "SELECT count(*) NUM_FASCICOLI, SOGG.COGNOME COGNOME, SOGG.NOME NOME, "
				+ "SOGG.DATA_NASCITA DATA_NASCITA, SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, ";
		lStatement += "SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, DESCR_COM_NASCITA.DESCRIZIONE "
				+ "DESCR_COMUNE_NASCITA, SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA ";

		// paolo cherubini allineo modifiche x supersoggetto 14 settembre 2010
		// lStatement += " FASC.SOG_ID_SOGGETTO SOG_ID_SOGGETTO, ";
		lStatement += ",1 SOG_ID_SOGGETTO";
		String lSuperSogg = new String();
		lSuperSogg = ", sogg.COD_FISCALE, sogg.COD_CS, sogg.COD_AFIS, sogg.ANNO_NASCITA, "
				+ "sogg.DATA_NASCITA_PRESUNTA";
		lSuperSogg += ", sogg.COD_STATO_NASCITA, sogg.NAZIONALITA, sogg.PATERNITA, sogg.COGNOME_MADRE, "
				+ "sogg.NOME_MADRE";
		// MERGE v10 COLLAUDO: aggiunta estrazione campi eta' presunta
		lSuperSogg += ", sogg.SESSO, sogg.ATTO_NASCITA, sogg.MESE_NASCITA, sogg.ETA_PRESUNTA_ANNI, "
				+ "sogg.ETA_PRESUNTA_MESI ";
		lStatement += lSuperSogg;
		// fine

		lStatement += "FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, GENERALE_PROCEDIMENTO GP, EVENTO EV, "
				+ "LICENZA_LIBANTICIPATA LLA, ";
		lStatement += "CG_REF_CODES DESCR_OGGETTO_PROCEDIMENTO,UFFICIO UFF, ";
		lStatement += "COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA ";
		lStatement += "WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += "AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "AND FASC.ID_FASCICOLO_SIUS = EV.FAS_SIU_ID_FASCICOLO_SIUS ";
		lStatement += "AND EV.ID_EVENTO = LLA.EVE_ID_EVENTO ";
		lStatement += "AND DESCR_OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' ";
		lStatement += "AND GP.COD_OGGETTO_PROCEDIMENTO = DESCR_OGGETTO_PROCEDIMENTO.RV_LOW_VALUE ";
		lStatement += "AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += "AND EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' ";

		if (lIncludeRigettati.equals("")) {
			lStatement += "AND (EV.COD_ESITO = '0001' ";
			lStatement += "or EV.COD_ESITO = '0013' ";
			lStatement += "or EV.COD_ESITO = '0006') ";
		}

		// 20110520 - Commentato - PM
		/*
		 * if(lCodLicenza.equals("LC")) { lStatement += "AND (EV.COD_MOTIVO = '2025' "; lStatement +=
		 * "  or EV.COD_MOTIVO = '2002' "; lStatement += "  or EV.COD_MOTIVO = '2185')"; }
		 * if(lCodLicenza.equals("RL")) lStatement += "AND EV.COD_MOTIVO = '2002' ";
		 * if(lCodLicenza.equals("EL")) lStatement += "AND EV.COD_MOTIVO = '2185' ";
		 */

		// Filtri per Codice Licenza
		if (lCodLicenza.equals("LC")) { // Inclusione dei COD_MOTIVO per LC (Licenza)
			lStatement += "AND (EV.COD_MOTIVO = '2025' ";
			lStatement += "OR EV.COD_MOTIVO = '2002' ";
			lStatement += "OR EV.COD_MOTIVO = '2185') ";
		} else if (lCodLicenza.equals("LI")) { // Inclusione dei COD_MOTIVO per LI (Licenza Internati)
			lStatement += "AND (EV.COD_MOTIVO = '2450' ";
			lStatement += "OR EV.COD_MOTIVO = '2451' ";
			lStatement += "OR EV.COD_MOTIVO = '2452' ";
			lStatement += "OR EV.COD_MOTIVO = '2460' ";
			lStatement += "OR EV.COD_MOTIVO = '2461') ";
		} else if (lCodLicenza.equals("RL")) { // Inclusione dei COD_MOTIVO per RL (Revoca Licenza)
			lStatement += "AND EV.COD_MOTIVO = '2002' ";
		} else if (lCodLicenza.equals("EL")) { // Inclusione dei COD_MOTIVO per EL (Esclusione Computo)
			lStatement += "AND EV.COD_MOTIVO = '2185' ";
		} else if (lCodLicenza.equals("LP")) {
			// MEV_2023-35: aggiungo Licenza pene sostitutive (LP)
			lStatement += "AND ((EV.COD_MOTIVO = '3130' AND EV.COD_ESITO = '0001') OR "
					+ "(EV.COD_MOTIVO in ('3150','3151') AND EV.COD_ESITO in ('0006','0013'))) ";
		}

		if (dataDalInCanc != null) {
			lStatement += "AND EV.DATA_EMISSIONE >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += "AND EV.DATA_EMISSIONE <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (lCodDistretto.length() > 1) {
			lStatement += "AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where "
					+ "uff.COD_DISTRETTO = '" + lCodDistretto + "') ";
		} else if (!strCodUffOTrib.equals("")) {
			lStatement += "AND FASC.CHIAVE_UFFICIO in ('" + strCodUffOTrib + "', '"
					+ strCodUfficioUtenteConnesso + "') ";
		} else if (lCodDistretto.length() != 1) {
			// Nella ricerca per tutto il DB viene passato lCodDistretto="3"
			lStatement += "AND FASC.CHIAVE_UFFICIO = '" + strCodUfficioUtenteConnesso + "' ";
		}
		lStatement += "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";

		return lStatement;
	}

	public void ricercaLicenzeDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = "";

		// Si costruisce la query relativa ai Fascicoli SIUS Con Provvedimenti.
		lStatement += getLicenzeDelSoggetto(aModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
				lCodDistretto, lIncludeRigettati, lCodLicenza, dataDalInCanc, dataAlInCanc);

		// paolo cherubini x supersoggetto 14 settembre 2010
		// inserisco la ricerca x soggetto
		lStatement += setCondizioneSuperSoggetto(aModel);
		// lStatement += setCondizione(aModel);
		// fine paolo

		lStatement += setOrderUfficioAnnoProgrEvento();
		setStatement(lStatement);
	}

	protected String getLicenzeDelSoggetto(SoggettoModel aModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) {
		String lStatement = new String();

		lStatement += " SELECT FASC.ID_FASCICOLO_SIUS ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO CHIAVE_ANNO, "
				+ " SOGG.ID_SOGGETTO ID_SOGGETTO, FASC.CHIAVE_PROGR CHIAVE_PROGR, ";
		lStatement += " FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO,  "
				+ " DESCR_TIPO_UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO,  ";
		lStatement += " DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, "
				+ " DESCR_TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, "
				+ " DESCR_ESITO_PROVVEDIMENTO.RV_MEANING DESCR_ESITO, ";
		lStatement += " EV.ID_EVENTO ID_EVENTO, EV.COD_ESITO COD_ESITO, "
				+ " EV.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO, EV.DATA_EMISSIONE DATA_EMISSIONE,  ";
		lStatement += " LLA.NUMERO_GIORNI NUMERO_GIORNI, LLA.NUMERO_ORE NUMERO_ORE, ";
		lStatement += " LLA.NUMERO_GIORNI_NO_FRUITI NUMERO_GIORNI_NO_FRUITI, "
				+ " LLA.NUMERO_ORE_NO_FRUITE NUMERO_ORE_NO_FRUITE, "
				+ " NVL(LLA.COD_ESITO,'-') COD_ESITO_LICENZA, "
				+ " DESCR_ESITO_LICENZA.RV_MEANING DESCR_ESITO_LICENZA ";
		lStatement += " FROM FASCICOLO_SIUS FASC, SOGGETTO  SOGG, "
				+ " EVENTO EV, LICENZA_LIBANTICIPATA LLA,  ";
		lStatement += " UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_NASCITA, "
				+ " CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO,  ";
		lStatement += " CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO, CG_REF_CODES DESCR_ESITO_LICENZA, "
				+ " CG_REF_CODES DESCR_TIPO_PROVVEDIMENTO, CG_REF_CODES DESCR_TIPO_UFFICIO ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO ";
		lStatement += " AND EV.COD_TIPO_PROVVEDIMENTO = DESCR_TIPO_PROVVEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE  ";
		lStatement += " AND DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN ='ESITO_PROVVEDIMENTO' ";
		lStatement += " AND EV.COD_MOTIVO = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN ='MOTIVO_PROVVEDIMENTO'  ";
		lStatement += " AND (EV.COD_TIPO_PROVVEDIMENTO = '02'  OR EV.COD_TIPO_PROVVEDIMENTO = '03') ";
		lStatement += " AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO ";
		lStatement += " AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE ";
		lStatement += " AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFFICIO.RV_LOW_VALUE ";
		lStatement += " AND DESCR_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO'  ";
		lStatement += " AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";
		lStatement += " AND EV.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS ";
		lStatement += " AND NVL(LLA.COD_ESITO, '-') = DESCR_ESITO_LICENZA.RV_LOW_VALUE ";
		lStatement += " AND DESCR_ESITO_LICENZA.RV_DOMAIN = 'ESITO_PERMESSO_LICENZA' ";
		lStatement += " AND EV.FLAG_DOCUMENTO_REGISTRATO <> 'A' ";
		lStatement += " AND LLA.EVE_ID_EVENTO = EV.ID_EVENTO ";

		if (lIncludeRigettati.equals("")) {
			lStatement += " AND (EV.COD_ESITO = '0001' ";
			lStatement += "   or EV.COD_ESITO = '0013' ";
			lStatement += "   or EV.COD_ESITO = '0006' )";
		}

		// 20110520 - Commentato - PM
		/*
		 * if(lCodLicenza.equals("LC")) { lStatement += " AND (EV.COD_MOTIVO = '2025' "; lStatement +=
		 * "   or EV.COD_MOTIVO = '2200' "; lStatement += "   or EV.COD_MOTIVO = '2185')"; }
		 * if(lCodLicenza.equals("RL")) lStatement += " AND EV.COD_MOTIVO = '2002' ";
		 * if(lCodLicenza.equals("EL")) lStatement += " AND EV.COD_MOTIVO = '2185' ";
		 */

		// Filtri per Codice Licenza
		if (lCodLicenza.equals("LC")) { // Inclusione dei COD_MOTIVO per LC (Licenza)
			lStatement += " AND (EV.COD_MOTIVO = '2025' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2002' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2185')";
		} else if (lCodLicenza.equals("LI")) { // Inclusione dei COD_MOTIVO per LI (Licenza Internati)
			lStatement += " AND (EV.COD_MOTIVO = '2450' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2451' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2452' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2460' ";
			lStatement += "  OR  EV.COD_MOTIVO = '2461')";
		} else if (lCodLicenza.equals("RL")) { // Inclusione dei COD_MOTIVO per RL (Revoca Licenza)
			lStatement += " AND EV.COD_MOTIVO = '2002' ";
		} else if (lCodLicenza.equals("EL")) { // Inclusione dei COD_MOTIVO per EL (Esclusione Computo)
			lStatement += " AND EV.COD_MOTIVO = '2185' ";
		} else if (lCodLicenza.equals("LP")) {
			// MEV_2023-35: aggiungo Licenza pene sostitutive (LP)
			lStatement += "AND ((EV.COD_MOTIVO = '3130' AND EV.COD_ESITO = '0001') OR "
					+ "(EV.COD_MOTIVO in ('3150','3151') AND EV.COD_ESITO in ('0006','0013'))) ";
		}

		if (dataDalInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE >= TO_DATE('"
					+ DateUtils.getDateToString(dataDalInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}
		if (dataAlInCanc != null) {
			lStatement += " AND EV.DATA_EMISSIONE <= TO_DATE('"
					+ DateUtils.getDateToString(dataAlInCanc, "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		if (lCodDistretto.length() > 1) {
			lStatement += " AND FASC.CHIAVE_UFFICIO in (select uff.cod_ufficio from ufficio where uff.COD_DISTRETTO='"
					+ lCodDistretto + "')";
		} else if (!strCodUffOTrib.equals("")) {
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
	 * @return Il Model di Licenza
	 * @throws DAOException
	 */
	public GenericModel getLicenzaModel() throws DAOException {
		LicenzaModel lLicenza = new LicenzaModel();

		lLicenza.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lLicenza.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lLicenza.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lLicenza.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lLicenza.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lLicenza.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		lLicenza.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lLicenza.setCodEsito(getString("COD_ESITO"));
		lLicenza.setDescrEsito(getString("DESCR_ESITO"));
		lLicenza.setIdEvento(getBigDecimal("ID_EVENTO"));
		lLicenza.setCodEsitoLicenza(getString("COD_ESITO_LICENZA"));
		lLicenza.setDescrEsitoLicenza(getString("DESCR_ESITO_LICENZA"));
		lLicenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lLicenza.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
		lLicenza.setDataEmissione(getDate("DATA_EMISSIONE"));
		lLicenza.setNumeroGiorni(getBigDecimal("NUMERO_GIORNI"));
		lLicenza.setNumeroOre(getBigDecimal("NUMERO_ORE"));
		lLicenza.setNumeroGiorniNoFruiti(getBigDecimal("NUMERO_GIORNI_NO_FRUITI"));
		lLicenza.setNumeroOreNoFruite(getBigDecimal("NUMERO_ORE_NO_FRUITE"));
		lLicenza.setSogIdSoggetto(getBigDecimal("ID_SOGGETTO"));

		return lLicenza;
	}

}