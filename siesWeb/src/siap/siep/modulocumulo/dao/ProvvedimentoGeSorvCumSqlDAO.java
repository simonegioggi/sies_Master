package siap.siep.modulocumulo.dao;

/**
* <p>Title: ProvvedimentoGeSorvCumSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ProvvedimentoGeSorvCum</p>
* <p>author Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;

public class ProvvedimentoGeSorvCumSqlDAO extends SqlDAO {
	/*****************************************************************************
	 * Costruttore
	 * 
	 * @param con
	 ****************************************************************************/
	public ProvvedimentoGeSorvCumSqlDAO(Connection con) {
		super(con);
	}

	/*****************************************************************************
	 * Restituisce il numero di record dell'operazione di ricerca costruendo la clausola where con lo stesso
	 * model utilizzato per la ricerca
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void getCountProvvedimentoGeSorvCum(ProvvedimentoGeSorvCumModel aModel) throws DAOException {
		// Costruisce lo statement da eseguire
		String lStatement = "SELECT COUNT(*) HowManyRecords FROM PROVVEDIMENTO_GE_SORV_CUM ";

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		// Imposta lo statement da eseguire
		setStatement(lStatement);
	}

	/*****************************************************************************
	 * Effettua la ricerca e restituisce solo i risultati nel range di record che vanno inseriti nella pagfina
	 * passata in input
	 * 
	 * @param aModel
	 * @param aPage
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaProvvedimentoGeSorvCumPaged(ProvvedimentoGeSorvCumModel aModel, int aPage)
			throws DAOException {
		String lStatement = new String("");

		lStatement += getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = this.setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lStatement += " WHERE " + lCondizioni;

		lStatement += " " + getOrderBy() + " ";

		String lPaginedStatement = "";
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement
				+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
				+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;

		setStatement(lPaginedStatement);
	}

	/*****************************************************************************
	 * Effettua la generica ricerca in base ai dati specificati nel model
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaProvvedimentoGeSorvCum(ProvvedimentoGeSorvCumModel aModel) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		// Recupero la where condition in base al model
		String lCondizioni = setCondizioni(aModel);

		if (!lCondizioni.trim().equals(""))
			lSql += " WHERE " + lCondizioni;

		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	public void ricercaProvvedimentoGeSorvCumByIdRichiesta(BigDecimal aIdRichiesta) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += setCondizioniByIdRichiesta(aIdRichiesta);
		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo che imposta la statement di ricerca per chiave
	 * 
	 * @param aKey
	 * @throws DAOException
	 ****************************************************************************/
	public void ricercaProvvedimentoGeSorvCumByKey(BigDecimal aIdProvvedimentoGeSorvCum) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += setCondizioniByKey(aIdProvvedimentoGeSorvCum);

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************************************
	 * Metodo che imposta lo statement di ricerca PROVVEDIMENTO_GE_SORV_CUM By Ric_Id_Richieste_Inviate_Cum
	 * 
	 * @param For.Key
	 *            Ric_Id_Richieste_Inviate_Cum
	 * @throws DAOException
	 *****************************************************************************************************/
	public void ricercaProvvedimentoGeSorvCumByIdIstruttoriaCum(BigDecimal aIdIstruttoria)
			throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQuery();

		lSql += " and RIC_ID_RICHIESTE_PM_IN_CUMULO in (select ID_RICHIESTE_PM_IN_CUMULO "
				+ " from RICHIESTE_PM_IN_CUMULO" + " where ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria
				+ ")";
		lSql += " " + getOrderBy() + " ";

		// Imposta lo statement da eseguire
		setStatement(lSql);
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sql query
	 * 
	 * @return
	 ****************************************************************************/
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PROVVEDIMENTO_GE_SORV_CUM, "
				+ "COD_UFFICIO_EMITTENTE, UFFICIOEMI.RV_MEANING DescrUfficioEmittente, "
				+ "COD_LUOGO_EMITTENTE, COMUNE.DESCRIZIONE DescrLuogoUfficioEmittente, " + "DATA_D, "
				+ "ANNO_PROVV, " + "NUMERO_PROVV, " + "FLAG_CONFORME, " + "FLAG_PIU_MENO_D, "
				+ "NUM_ANNI_RECLUSIONE_D, " + "NUM_MESI_RECLUSIONE_D, " + "NUM_GIORNI_RECLUSIONE_D, "
				+ "IMPORTO_MULTA_D, " + "NUM_ANNI_ARRESTO_D, " + "NUM_MESI_ARRESTO_D, "
				+ "NUM_GIORNI_ARRESTO_D, " + "IMPORTO_AMMENDA_D, " + "NUM_GIORNI_LA_REV_D, "
				+ "NUM_GIORNI_LS_REV_D, " + "NUM_GIORNI_LI_REV_D, " + "MOTIVAZIONI_D, "
				+ "COD_TIPO_PROVVEDIMENTO, CODTIPOPROVVEDIMENTO.RV_MEANING DescrTipoProvvedimento, "
				+ "ANNO_SIUS, " + "NUMERO_SIUS, "
				+ "COD_TIPO_MS_D, CODTIPOMISURA.RV_MEANING DescrTipoMisura, " + "NUM_ANNI_MS_D, "
				+ "NUM_MESI_MS_D, " + "NUM_GIORNI_MS_D, " + "BEN_SOSP_COND," + "BEN_NON_MENZIONE,"
				+ "BEN_INDULTO,"
				+ "COD_TIPO_PENA_ACCESSORIA_D, PENAACCESSORIA.RV_MEANING DescrPenaAccessoria, "
				+ "COD_TIPO_DURATA_D, TIPODURATA.RV_MEANING DescrTipoDurata, "
				+ "NUM_ANNI_PA_D, NUM_MESI_PA_D, NUM_GIORNI_PA_D, " + "DATA_REVOCA, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "RIC_ID_RICHIESTE_PM_IN_CUMULO ";
		lStatement += " FROM PROVVEDIMENTO_GE_SORV_CUM";
		lStatement += " , CG_REF_CODES CODTIPOMISURA ";
		lStatement += " , CG_REF_CODES CODTIPOPROVVEDIMENTO ";
		lStatement += " , CG_REF_CODES UFFICIOEMI ";
		lStatement += " , CG_REF_CODES TIPODURATA ";
		lStatement += " , CG_REF_CODES PENAACCESSORIA ";
		lStatement += " , COMUNE ";
		lStatement += " , UFFICIO ";
		lStatement += " WHERE 1=1 ";
		lStatement += " and (	  nvl( PROVVEDIMENTO_GE_SORV_CUM.COD_UFFICIO_EMITTENTE, '-') = UFFICIO.COD_UFFICIO ) ";
		lStatement += " and (	  nvl( UFFICIOEMI.RV_LOW_VALUE, '-' ) = UFFICIO.COD_TIPO_UFFICIO  and  UFFICIOEMI.RV_DOMAIN = 'TIPO_UFFICIO' ) ";
		lStatement += " and (	  nvl( PROVVEDIMENTO_GE_SORV_CUM.COD_LUOGO_EMITTENTE, '-') = COMUNE.COD_COMUNE ) ";

		lStatement += " and (     nvl(PROVVEDIMENTO_GE_SORV_CUM.COD_TIPO_PROVVEDIMENTO,'-') = CODTIPOPROVVEDIMENTO.RV_LOW_VALUE AND CODTIPOPROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ) ";
		lStatement += " and (     nvl(PROVVEDIMENTO_GE_SORV_CUM.COD_TIPO_MS_D,'-') = CODTIPOMISURA.RV_LOW_VALUE AND CODTIPOMISURA.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' ) ";

		lStatement += " and ( nvl(PROVVEDIMENTO_GE_SORV_CUM.COD_TIPO_DURATA_D,'-') = TIPODURATA.RV_LOW_VALUE AND TIPODURATA.RV_DOMAIN = 'TIPO_DURATA' ) ";
		lStatement += " and ( nvl(PROVVEDIMENTO_GE_SORV_CUM.COD_TIPO_PENA_ACCESSORIA_D, '-') = PENAACCESSORIA.RV_LOW_VALUE AND PENAACCESSORIA.RV_DOMAIN = 'TIPO_PENA_ACCESSORIA' ) ";

		return lStatement;
	}

	/*****************************************************************************
	 * Metodo che carica il record del result set nel model
	 * 
	 * @return
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		ProvvedimentoGeSorvCumModel aModel = new ProvvedimentoGeSorvCumModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdProvvedimentoGeSorvCum(getBigDecimal("ID_PROVVEDIMENTO_GE_SORV_CUM"));
		aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
		aModel.setDescrUfficioEmittente(getString("DescrUfficioEmittente"));
		aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		aModel.setDescrLuogoEmittente(getString("DescrLuogoUfficioEmittente"));
		aModel.setDataD(getDate("DATA_D"));
		aModel.setAnnoProvv(getBigDecimal("ANNO_PROVV"));
		aModel.setNumeroProvv(getString("NUMERO_PROVV"));
		aModel.setFlagConforme(getString("FLAG_CONFORME"));
		aModel.setFlagPiuMenoD(getString("FLAG_PIU_MENO_D"));
		aModel.setNumAnniReclusioneD(getBigDecimal("NUM_ANNI_RECLUSIONE_D"));
		aModel.setNumMesiReclusioneD(getBigDecimal("NUM_MESI_RECLUSIONE_D"));
		aModel.setNumGiorniReclusioneD(getBigDecimal("NUM_GIORNI_RECLUSIONE_D"));
		aModel.setImportoMultaD(getBigDecimal("IMPORTO_MULTA_D"));
		aModel.setNumAnniArrestoD(getBigDecimal("NUM_ANNI_ARRESTO_D"));
		aModel.setNumMesiArrestoD(getBigDecimal("NUM_MESI_ARRESTO_D"));
		aModel.setNumGiorniArrestoD(getBigDecimal("NUM_GIORNI_ARRESTO_D"));
		aModel.setImportoAmmendaD(getBigDecimal("IMPORTO_AMMENDA_D"));
		aModel.setNumGiorniRevocaLaD(getBigDecimal("NUM_GIORNI_LA_REV_D"));
		aModel.setNumGiorniRevocaLsD(getBigDecimal("NUM_GIORNI_LS_REV_D"));
		aModel.setNumGiorniRevocaLiD(getBigDecimal("NUM_GIORNI_LI_REV_D"));
		aModel.setMotivazioniD(getString("MOTIVAZIONI_D"));
		aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		aModel.setDescrTipoProvvedimento(getString("DescrTipoProvvedimento"));
		aModel.setAnnoSIUS(getBigDecimal("ANNO_SIUS"));
		aModel.setNumeroSIUS(getString("NUMERO_SIUS"));
		aModel.setCodTipoMsD(getString("COD_TIPO_MS_D"));
		aModel.setDescrTipoMsD(getString("DescrTipoMisura"));
		aModel.setNumAnniMsD(getBigDecimal("NUM_ANNI_MS_D"));
		aModel.setNumMesiMsD(getBigDecimal("NUM_MESI_MS_D"));
		aModel.setNumGiorniMsD(getBigDecimal("NUM_GIORNI_MS_D"));
		aModel.setBenSospCond(getString("BEN_SOSP_COND"));
		aModel.setBenNonMenzione(getString("BEN_NON_MENZIONE"));
		aModel.setBenIndulto(getString("BEN_INDULTO"));

		aModel.setCodTipoPenaAccessoriaD(getString("COD_TIPO_PENA_ACCESSORIA_D"));
		aModel.setDescrTipoPenaAccessoriaD(getString("DescrPenaAccessoria"));
		aModel.setCodTipoDurataPaD(getString("COD_TIPO_DURATA_D"));
		aModel.setDescrTipoDurataPaD(getString("DescrTipoDurata"));
		aModel.setNumAnniPaD(getBigDecimal("NUM_ANNI_PA_D"));
		aModel.setNumMesiPaD(getBigDecimal("NUM_MESI_PA_D"));
		aModel.setNumGiorniPaD(getBigDecimal("NUM_GIORNI_PA_D"));
		aModel.setDataRevoca(getDate("DATA_REVOCA"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setRicIdRichiestePmInCumulo(getBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO"));
		return aModel;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public String setCondizioni(ProvvedimentoGeSorvCumModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdProvvedimentoGeSorvCum() != null) {
			lCondizioni += " and ID_PROVVEDIMENTO_GE_SORV_CUM = " + aModel.getIdProvvedimentoGeSorvCum() + "";
		}
		if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) {
			lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}
		if (aModel.getDataD() != null) {
			lCondizioni += " and to_char(DATA_D,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataD(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoProvv() != null) {
			lCondizioni += " and ANNO_PROVV = " + aModel.getAnnoProvv() + "";
		}
		if (aModel.getNumeroProvv() != null && aModel.getNumeroProvv().length() > 0) {
			lCondizioni += " and NUMERO_PROVV = '" + aModel.getNumeroProvv() + "' ";
		}
		if (aModel.getFlagConforme() != null && aModel.getFlagConforme().length() > 0) {
			lCondizioni += " and FLAG_CONFORME = '" + aModel.getFlagConforme() + "' ";
		}
		if (aModel.getFlagPiuMenoD() != null && aModel.getFlagPiuMenoD().length() > 0) {
			lCondizioni += " and FLAG_PIU_MENO_D = '" + aModel.getFlagPiuMenoD() + "' ";
		}
		if (aModel.getNumAnniReclusioneD() != null) {
			lCondizioni += " and NUM_ANNI_RECLUSIONE_D = " + aModel.getNumAnniReclusioneD() + "";
		}
		if (aModel.getNumMesiReclusioneD() != null) {
			lCondizioni += " and NUM_MESI_RECLUSIONE_D = " + aModel.getNumMesiReclusioneD() + "";
		}
		if (aModel.getNumGiorniReclusioneD() != null) {
			lCondizioni += " and NUM_GIORNI_RECLUSIONE_D = " + aModel.getNumGiorniReclusioneD() + "";
		}
		if (aModel.getImportoMultaD() != null) {
			lCondizioni += " and IMPORTO_MULTA_D = " + aModel.getImportoMultaD() + "";
		}
		if (aModel.getNumAnniArrestoD() != null) {
			lCondizioni += " and NUM_ANNI_ARRESTO_D = " + aModel.getNumAnniArrestoD() + "";
		}
		if (aModel.getNumMesiArrestoD() != null) {
			lCondizioni += " and NUM_MESI_ARRESTO_D = " + aModel.getNumMesiArrestoD() + "";
		}
		if (aModel.getNumGiorniArrestoD() != null) {
			lCondizioni += " and NUM_GIORNI_ARRESTO_D = " + aModel.getNumGiorniArrestoD() + "";
		}
		if (aModel.getImportoAmmendaD() != null) {
			lCondizioni += " and IMPORTO_AMMENDA_D = " + aModel.getImportoAmmendaD() + "";
		}
		if (aModel.getNumGiorniRevocaLaD() != null) {
			lCondizioni += " and NUM_GIORNI_LA_REV_D = " + aModel.getNumGiorniRevocaLaD() + "";
		}
		if (aModel.getNumGiorniRevocaLsD() != null) {
			lCondizioni += " and NUM_GIORNI_LS_REV_D = " + aModel.getNumGiorniRevocaLsD() + "";
		}
		if (aModel.getNumGiorniRevocaLiD() != null) {
			lCondizioni += " and NUM_GIORNI_LI_REV_D = " + aModel.getNumGiorniRevocaLiD() + "";
		}
		if (aModel.getMotivazioniD() != null && aModel.getMotivazioniD().length() > 0) {
			lCondizioni += " and MOTIVAZIONI_D = '" + aModel.getMotivazioniD() + "' ";
		}
		if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
		}
		if (aModel.getAnnoSIUS() != null) {
			lCondizioni += " and ANNO_SIUS = " + aModel.getAnnoSIUS() + "";
		}
		if (aModel.getNumeroSIUS() != null && aModel.getNumeroSIUS().length() > 0) {
			lCondizioni += " and NUMERO_SIUS = '" + aModel.getNumeroSIUS() + "' ";
		}
		if (aModel.getCodTipoMsD() != null && aModel.getCodTipoMsD().length() > 0) {
			lCondizioni += " and COD_TIPO_MS_D = '" + aModel.getCodTipoMsD() + "' ";
		}
		if (aModel.getNumAnniMsD() != null) {
			lCondizioni += " and NUM_ANNI_MS_D = " + aModel.getNumAnniMsD() + "";
		}
		if (aModel.getNumMesiMsD() != null) {
			lCondizioni += " and NUM_MESI_MS_D = " + aModel.getNumMesiMsD() + "";
		}
		if (aModel.getNumGiorniMsD() != null) {
			lCondizioni += " and NUM_GIORNI_MS_D = " + aModel.getNumGiorniMsD() + "";
		}
		if (aModel.getBenSospCond() != null && aModel.getBenSospCond().length() > 0) {
			lCondizioni += " and BEN_SOSP_COND = '" + aModel.getBenSospCond() + "' ";
		}
		if (aModel.getBenNonMenzione() != null && aModel.getBenNonMenzione().length() > 0) {
			lCondizioni += " and BEN_NON_MENZIONE = '" + aModel.getBenNonMenzione() + "' ";
		}
		if (aModel.getBenIndulto() != null && aModel.getBenIndulto().length() > 0) {
			lCondizioni += " and BEN_INDULTO = '" + aModel.getBenIndulto() + "' ";
		}
		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getRicIdRichiestePmInCumulo() != null) {
			lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aModel.getRicIdRichiestePmInCumulo()
					+ "";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}
		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di select per chiave
	 * 
	 * @param aKey
	 * @return
	 ****************************************************************************/
	public String setCondizioniByKey(BigDecimal aIdProvvedimentoGeSorvCum) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PROVVEDIMENTO_GE_SORV_CUM = " + aIdProvvedimentoGeSorvCum;

		// Elimino il primo and
		// if (lCondizioni.length() > 0) {
		// lCondizioni = lCondizioni.substring(4);
		// }

		return lCondizioni;
	}

	public String setCondizioniByIdRichiesta(BigDecimal aIdRichiesta) {
		String lCondizioni = new String();
		lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiesta;

		return lCondizioni;
	}

	/*****************************************************************************
	 * Metodo per la costruzione della sezione order by
	 * 
	 * @return
	 ****************************************************************************/
	protected String getOrderBy() {
		String orderBy = new String("");
		// orderBy = " ORDER BY ";
		return orderBy;
	}
}
