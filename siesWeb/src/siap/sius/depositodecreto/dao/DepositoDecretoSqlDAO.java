package siap.sius.depositodecreto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

/**
 * <p>
 * Title: DepositoDecretoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella DepositoDecreto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DepositoDecretoSqlDAO extends SIAPSqlDAO {

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 *
	 * @param aConn
	 *            Connessione al dbase.
	 */
	public DepositoDecretoSqlDAO(Connection aConn) {
		super(aConn);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto.
	 * <p>
	 *
	 * @param aModel
	 *            dati del deposito decreto.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecreto(DepositoDecretoModel aModel) throws DAOException {
		String lSql = getSqlQuery();
		// lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto per il corrispondente id.
	 * <p>
	 *
	 * @param aKey
	 *            id del record
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecretoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto per l'id di generale procedimento.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento,
	 * @param aCodTipo
	 *            : codice Tipo Decreto
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecretoByIdGenProc(BigDecimal aKey, String aCodTipo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + this.setCondizioniByIdGenProcCodTipoDec(aKey, aCodTipo);
		setStatement(lSql);
	}

	// STUB 03/03/2006 Nuova ricerca.
	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto per l'id di generale procedimento.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento,
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecretoByIdGenProc(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + this.setCondizioniByIdGenProc(aKey);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto per l'id Evento generato.
	 * <p>
	 *
	 * @param aKey
	 *            .
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQueryNoDescTipoDecreto();

		lSql += " " + setCondizioniByIdEveGeneratoNoDescTipoDecreto(aKey);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto per l'id Evento generato.
	 * <p>
	 *
	 * @param aKey
	 *            .
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public void ricercaDepositoDecretoByIdEveGenerato(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdEveGenerato(aKey);
		setStatement(lSql);
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto di tipo Permesso o Licenza, cioè un
	 * Deposito Decreto cui sia collegato una Licenza o un Permesso con NUMERO_GIORNI > 0. La ricerca viene
	 * effettuata per Soggetto, Tipo di decreto, Ufficio.
	 * <p>
	 *
	 * @param: BigDecimal
	 *             aIdSoggetto; String aTipoDecreto, String aCodUfficio.
	 * @return Vector : Elenco di Decreti trovati.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public Vector RicercaDecretoLicenzaByIdSoggetto(BigDecimal aIdSoggetto, String aTipoDecreto,
			String aCodTipoLicenza, String aCodUff) throws DAOException {
		Vector lVect = new Vector();
		DepositoDecretoFascicoloModel lModel = null;
		String lSql = "SELECT DD.*, FS.ID_FASCICOLO_SIUS, FS.CHIAVE_ANNO, FS.CHIAVE_PROGR, '' AS  DESC_TIPO_DECRETO ";
		lSql += " , null AS DESC_TIPO_CONTROLLO_ESECUZIONE ";
		lSql += " FROM DEPOSITO_DECRETO DD ";
		lSql += " INNER JOIN  LICENZA_LIBANTICIPATA LL ON LL.EVE_ID_EVENTO = DD.ID_EVENTO_GENERATO and (LL.NUMERO_GIORNI > 0 or LL.NUMERO_ORE > 0) ";
		lSql += " INNER JOIN FASCICOLO_SIUS FS ON FS.ID_FASCICOLO_SIUS = LL.FAS_SIU_ID_FASCICOLO_SIUS ";
		lSql += " WHERE DD.COD_TIPO_DECRETO ='" + aTipoDecreto + "' ";
		lSql += " AND DD.COD_UFFICIO_INSERIMENTO = '" + aCodUff + "' ";
		lSql += " AND LL.COD_TIPO_LICENZA ='" + aCodTipoLicenza + "' ";
		lSql += " AND FS.SOG_ID_SOGGETTO =" + aIdSoggetto;
		lSql += " ORDER BY DD.DATA_EMISSIONE DESC ";

		setStatement(lSql);
		start();
		while (next()) {
			lModel = new DepositoDecretoFascicoloModel();
			lModel.setDepositoDecreto((DepositoDecretoModel) getModel());
			lModel.getFascicolo().setIdFascicoloSius(this.getBigDecimal("ID_FASCICOLO_SIUS"));
			lModel.getFascicolo().setChiaveAnno(this.getBigDecimal("CHIAVE_ANNO"));
			lModel.getFascicolo().setChiaveProgr(this.getBigDecimal("CHIAVE_PROGR"));
			lVect.add(lModel);
		}
		stop();
		return lVect;
	}

	/**
	 * Imposta lo statement SQL per la ricerca del Deposito Decreto di tipo Permesso o Licenza, cioè un
	 * Deposito Decreto cui sia collegato una Licenza o un Permesso con NUMERO_GIORNI > 0. La ricerca viene
	 * effettuata per SuperSoggetto, Tipo di decreto, Ufficio.
	 * <p>
	 *
	 * @param: BigDecimal
	 *             SuperSoggetto; String aTipoDecreto, String aCodUfficio.
	 * @return Vector : Elenco di Decreti trovati.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public Vector RicercaDecretoLicenzaBySuperSoggetto(SoggettoModel aSogModel, String aTipoDecreto,
			String aCodTipoLicenza, String aCodUff) throws DAOException {
		Vector lVect = new Vector();
		DepositoDecretoFascicoloModel lModel = null;
		String lSql = "SELECT DD.*, FS.ID_FASCICOLO_SIUS, FS.CHIAVE_ANNO, FS.CHIAVE_PROGR, '' AS  DESC_TIPO_DECRETO  ";
		lSql += " , null AS DESC_TIPO_CONTROLLO_ESECUZIONE ";
		lSql += " FROM SOGGETTO SOGG, DEPOSITO_DECRETO DD   ";
		// MEV Segnalazione N.7
		// lSql +=
		// " INNER JOIN LICENZA_LIBANTICIPATA LL ON LL.EVE_ID_EVENTO = DD.ID_EVENTO_GENERATO and
		// (LL.NUMERO_GIORNI > 0 or LL.NUMERO_ORE > 0) "
		// ;
		lSql += " INNER JOIN  LICENZA_LIBANTICIPATA LL ON LL.EVE_ID_EVENTO = DD.ID_EVENTO_GENERATO and ";
		lSql += "(LL.NUMERO_MESI > 0 or LL.NUMERO_GIORNI > 0 or LL.NUMERO_ORE > 0) ";
		// End N.7
		lSql += " INNER JOIN FASCICOLO_SIUS FS ON FS.ID_FASCICOLO_SIUS = LL.FAS_SIU_ID_FASCICOLO_SIUS ";
		lSql += " WHERE DD.COD_TIPO_DECRETO ='" + aTipoDecreto + "' ";
		lSql += " AND DD.COD_UFFICIO_INSERIMENTO = '" + aCodUff + "' ";
		lSql += " AND LL.COD_TIPO_LICENZA ='" + aCodTipoLicenza + "' ";
		// lSql += " AND FS.SOG_ID_SOGGETTO =" + aIdSoggetto;
		lSql += " AND SOGG.ID_SOGGETTO = FS.SOG_ID_SOGGETTO";
		lSql += setCondizioneSuperSoggetto(aSogModel);

		lSql += " ORDER BY DD.DATA_EMISSIONE DESC ";

		setStatement(lSql);
		start();
		while (next()) {
			lModel = new DepositoDecretoFascicoloModel();
			lModel.setDepositoDecreto((DepositoDecretoModel) getModel());
			lModel.getFascicolo().setIdFascicoloSius(this.getBigDecimal("ID_FASCICOLO_SIUS"));
			lModel.getFascicolo().setChiaveAnno(this.getBigDecimal("CHIAVE_ANNO"));
			lModel.getFascicolo().setChiaveProgr(this.getBigDecimal("CHIAVE_PROGR"));
			lVect.add(lModel);
		}
		stop();
		return lVect;
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
	 * Effettua la ricerca del Deposito Decreto e del Fascicolo SIUS
	 * <p>
	 *
	 * @param: BigDecimal
	 *             aIEvento.
	 * @return DepositoDecretoFascicoloModel
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public DepositoDecretoFascicoloModel RicercaDecretoFascicoloByIdEvento(BigDecimal aIEvento)
			throws DAOException {
		DepositoDecretoFascicoloModel lModel = null;
		String lSql = "SELECT DD.*, FS.ID_FASCICOLO_SIUS, FS.CHIAVE_ANNO, FS.CHIAVE_PROGR, '' AS  DESC_TIPO_DECRETO  ";
		lSql += " , null AS DESC_TIPO_CONTROLLO_ESECUZIONE ";
		lSql += " FROM DEPOSITO_DECRETO DD ";
		lSql += " INNER JOIN  LICENZA_LIBANTICIPATA LL ON LL.EVE_ID_EVENTO = DD.ID_EVENTO_GENERATO ";
		lSql += " INNER JOIN FASCICOLO_SIUS FS ON FS.ID_FASCICOLO_SIUS = LL.FAS_SIU_ID_FASCICOLO_SIUS ";
		lSql += " WHERE DD.ID_EVENTO_GENERATO =" + aIEvento;

		setStatement(lSql);
		start();
		if (next()) {
			lModel = new DepositoDecretoFascicoloModel();
			lModel.setDepositoDecreto((DepositoDecretoModel) getModel());
			lModel.getFascicolo().setIdFascicoloSius(this.getBigDecimal("ID_FASCICOLO_SIUS"));
			lModel.getFascicolo().setChiaveAnno(this.getBigDecimal("CHIAVE_ANNO"));
			lModel.getFascicolo().setChiaveProgr(this.getBigDecimal("CHIAVE_PROGR"));
		}
		stop();
		return lModel;
	}

	/**
	 * Imposta select count per uso verifica esistenza di un deposito decreto per id generale procedimento.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento.
	 */
	public void ricercaEsistenzaDepositoDecretoByIdGenProc(BigDecimal aKey) {
		String lSql = "SELECT COUNT(*) AS NUM_REC FROM DEPOSITO_DECRETO ";
		lSql += " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;

		setStatement(lSql);
	}

	/**
	 * Imposta select count per uso verifica esistenza di un deposito decreto per id generale procedimento e
	 * cod_tipo_decreto. La select conta il numero di decreti legati ad uno specificato Generale_Procedimento,
	 * di tipo specificato e non annullati.
	 * <p>
	 *
	 * @param aKey
	 *            : id generale procedimento,
	 * @param aCod
	 *            : Tipo decreto.
	 */
	public void ricercaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey, String aCod) {
		String lStatement = "select count(*) as NUM_REC from DEPOSITO_DECRETO D join EVENTO E ON  (D.ID_EVENTO_GENERATO = E.ID_EVENTO  AND (E.FLAG_DOCUMENTO_REGISTRATO IS NULL OR E.FLAG_DOCUMENTO_REGISTRATO <> 'A'))";
		lStatement += " WHERE D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		lStatement += " AND D.COD_TIPO_DECRETO = '" + aCod + "'";

		setStatement(lStatement);
	}

	/**
	 * Imposta select count per uso verifica esistenza di un deposito decreto per id generale procedimento e
	 * cod_tipo_decreto, quest'ultimo viene passato come array.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento.
	 */
	public void ricercaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey, String[] aCods)
			throws DAOException {
		String lSql = "SELECT COUNT(*) AS NUM_REC FROM DEPOSITO_DECRETO ";
		lSql += " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		lSql += " AND (" + getSQLStringFromArray("COD_TIPO_DECRETO", aCods) + ")";

		setStatement(lSql);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di record in DEPOSITO_DECRETO di tipo decreto di
	 * relativi ad un generale procedimento e con un record evento collegato con il cod_esito specificato.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Generale Procedimento
	 * @param String
	 *            aCod : codice esito nell'evento generato
	 * @return int : numero di record trovati
	 * @throws DAOException
	 */

	public int getNumDepDecretoByGenProcCodEsito(BigDecimal aKey, String aCod) throws DAOException {

		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DEPOSITO_DECRETO D, EVENTO E WHERE D.ID_EVENTO_GENERATO = E.ID_EVENTO ";
		lStatement += " AND E.COD_ESITO = '" + aCod + "'";
		lStatement += " AND D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di record in DEPOSITO_DECRETO di tipo decreto di
	 * relativi ad un generale procedimento e con un record evento collegato con data di emissione
	 * specificata.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Generale Procedimento
	 * @param Date
	 *            aDataEmissione : Data Emissione
	 * @return int : numero di record trovati
	 * @throws DAOException
	 */

	public int getNumDepDecretoByGenProcDataEmissione(BigDecimal aKey, Date aDataEmissione)
			throws DAOException {

		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DEPOSITO_DECRETO D, EVENTO E WHERE D.ID_EVENTO_GENERATO = E.ID_EVENTO ";
		lStatement += " AND E.DATA_EMISSIONE = TO_DATE("
				+ DateUtils.getDateToString(aDataEmissione, "yyyyMMdd") + ",'YYYYMMDD')";
		lStatement += " AND D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce il numero di record in DEPOSITO_DECRETO relativi ad un
	 * generale procedimento specificato dalla sua chiave passata come primo argomento e di tipo non presente
	 * tra quelli passati nella liata secondo argomento della funzione.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Generale Procedimento
	 * @param aTipi
	 *            : String[] elenco dei tipi decreto esclusi dalla ricerca.
	 * @return int : numero di record trovati
	 * @throws DAOException
	 */

	public int getNumDepDecretoByGenProcEccettoTipi(BigDecimal aKey, String[] aTipi) throws DAOException {
		// Numero di tipi decreti da escludere dalla ricerca
		int lNumTipi = (aTipi != null) ? aTipi.length : 0;
		// numero di record trovati
		BigDecimal lCount = null;
		int retNum = -1;

		String lStatement = "select count(*) as COUNT from DEPOSITO_DECRETO D join EVENTO E ON  (D.ID_EVENTO_GENERATO = E.ID_EVENTO  AND (E.FLAG_DOCUMENTO_REGISTRATO IS NULL OR E.FLAG_DOCUMENTO_REGISTRATO <> 'A'))";
		lStatement += " WHERE D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		if (lNumTipi > 0) {
			lStatement += " AND D.COD_TIPO_DECRETO NOT IN ('" + aTipi[0] + "'";
			for (int i = 1; i < lNumTipi; i++) {
				lStatement += ", '" + aTipi[i] + "'";
			}
			lStatement += ")";
		}

		setStatement(lStatement);

		this.start();
		if (this.next()) {
			lCount = this.getBigDecimal("COUNT");
			retNum = lCount.intValue();
		}
		return retNum;
	}

	/**
	 * Crea Query SQL.
	 * <p>
	 *
	 * @return la stringa della quesry sql.
	 */
	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT  ";
		lStatement += "ID_DEPOSITO_DECRETO, ";
		lStatement += "ANNO_S72, ";
		lStatement += "NUM_S72, ";
		lStatement += "COD_TIPO_DECRETO, ";
		lStatement += "TIPO_DECRETO.RV_MEANING DESC_TIPO_DECRETO, ";
		lStatement += "DATA_EMISSIONE, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ALTRI_DESTINATARI, ";
		lStatement += "DATA_PARERE_PG, ";
		lStatement += "COD_TIPO_PARERE_PG, ";
		lStatement += "DATA_RICORSO_IMPUGNAZIONE, ";
		lStatement += "DATA_INVIO_ATTI_IMPUGNAZIONE, ";
		lStatement += "DATA_SENTENZA_IMPUGNAZIONE, ";
		lStatement += "TENORE_SENTENZA_IMPUGNAZIONE, ";
		lStatement += "NOTE, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "SENTENZE_RIFERIMENTO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		// nuovi campi Luigi 17-11-2003
		lStatement += "COD_UFFICIO_COMP, ";
		lStatement += "COD_PROCURA_ESECUZIONE, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		// nuovi campi Luigi 27-01-2004
		lStatement += "COD_TDS_COMP, ";
		lStatement += "IST_DET_ID_ISTITUTO_DETENZIONE, ";
		lStatement += "STATUS_PERSONA, ";
		lStatement += "TOT_ORE_RAGGIUNGIMENTO, ";
		lStatement += "ANNO_PROC_REVOCATO, ";
		lStatement += "PROGR_PROC_REVOCATO, ";
		lStatement += "UFFICIO_PROC_REVOCATO, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA, ";
		lStatement += "FLAG_NOMINA_COMM_ACTA, ";
		lStatement += "DESCR_COMM_ACTA, ";
		lStatement += "TIPO_CONTROLLO_ESECUZIONE, ";
		// 07/2014
		lStatement += "NUM_GIORNI_REVOCA_LA, ";
		// DL 92 2014 Violazione CEDU
		lStatement += "NUM_GIORNI_RIDUZIONE_PENA, SOMMA_RISARC_DANNI, ";
		// MEV_9 aggiunto campo DATA_TERMINE_EMISSIONE
		lStatement += "DATA_TERMINE_EMISSIONE, ";
		lStatement += "TIPO_CTRL_ES.RV_MEANING AS DESC_TIPO_CONTROLLO_ESECUZIONE";
		lStatement += " FROM DEPOSITO_DECRETO, CG_REF_CODES TIPO_DECRETO, CG_REF_CODES TIPO_CTRL_ES";
		lStatement += " WHERE TIPO_DECRETO.RV_DOMAIN = 'TIPO_DECRETO' AND COD_TIPO_DECRETO = TIPO_DECRETO.RV_LOW_VALUE";
		// lStatement +=
		// " AND TIPO_CTRL_ES.RV_DOMAIN = 'TIPO_CONTROLLO_ESECUZIONE' AND TIPO_CONTROLLO_ESECUZIONE =
		// TIPO_CTRL_ES.RV_LOW_VALUE ";
		lStatement += " AND (TIPO_CTRL_ES.RV_DOMAIN = 'TIPO_CONTROLLO_ESECUZIONE' AND NVL (TIPO_CONTROLLO_ESECUZIONE,'-') = TIPO_CTRL_ES.RV_LOW_VALUE)";

		return lStatement;
	}

	/**
	 * Crea Query SQL . getSqlQueryNoDescTipoDecreto
	 *
	 * @return la stringa della quesry sql.
	 */
	// Svillupata per Siep che non tiene conto del COD_TIPO_DECRETO valorizzato.
	// Dario -- Viviana --22-05-06
	protected String getSqlQueryNoDescTipoDecreto() {
		String lStatement = new String("");

		lStatement += " SELECT  ";
		lStatement += "ID_DEPOSITO_DECRETO, ";
		lStatement += "ANNO_S72, ";
		lStatement += "NUM_S72, ";
		lStatement += "COD_TIPO_DECRETO, ";
		lStatement += "DATA_EMISSIONE, ";
		lStatement += "DATA_DEPOSITO, ";
		lStatement += "COD_MAGISTRATO, ";
		lStatement += "ALTRI_DESTINATARI, ";
		lStatement += "DATA_PARERE_PG, ";
		lStatement += "COD_TIPO_PARERE_PG, ";
		lStatement += "DATA_RICORSO_IMPUGNAZIONE, ";
		lStatement += "DATA_INVIO_ATTI_IMPUGNAZIONE, ";
		lStatement += "DATA_SENTENZA_IMPUGNAZIONE, ";
		lStatement += "TENORE_SENTENZA_IMPUGNAZIONE, ";
		lStatement += "NOTE, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_UFFICIO_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "COD_UFFICIO_AGGIORNAMENTO, ";
		lStatement += "GEN_PRID_GENERALE_PROCEDIMENTO, ";
		lStatement += "SENTENZE_RIFERIMENTO, ";
		lStatement += "ID_EVENTO_GENERATO, ";
		lStatement += "COD_UFFICIO_COMP, ";
		lStatement += "COD_PROCURA_ESECUZIONE, ";
		lStatement += "LUOGO_SVOLGIMENTO_PROVA, ";
		lStatement += "COD_TDS_COMP, ";
		lStatement += "IST_DET_ID_ISTITUTO_DETENZIONE, ";
		lStatement += "STATUS_PERSONA, ";
		lStatement += "TOT_ORE_RAGGIUNGIMENTO, ";
		lStatement += "ANNO_PROC_REVOCATO, ";
		lStatement += "PROGR_PROC_REVOCATO, ";
		lStatement += "UFFICIO_PROC_REVOCATO, ";
		lStatement += "DATA_COMP_FOGLIO_COMPLEMENTARE, ";
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		lStatement += "DATA_SOSPENSIONE_SS, ";
		lStatement += "GIORNI_RECUPERO_SS, ";
		lStatement += "FLAG_RECUPERO_SS, ";
		lStatement += "DATA_SCADENZA_SOSPENSIONE_SS, ";
		lStatement += "SOSPENSIONE_GG, ";
		lStatement += "SOSPENSIONE_MM, ";
		lStatement += "SOSPENSIONE_AA, ";
		lStatement += "FLAG_NOMINA_COMM_ACTA, ";
		lStatement += "DESCR_COMM_ACTA ";

		lStatement += " FROM DEPOSITO_DECRETO ";
		lStatement += " WHERE ";

		return lStatement;
	}

	//
	// METODO getModelNoDescTipoDecreto()
	//
	/**
	 * Ritorna il model popolato con i dati del record corrispondente.
	 * <p>
	 *
	 * @return ritorna il model popolato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public GenericModel getModelNoDescTipoDecreto() throws DAOException {
		DepositoDecretoModel aModel = new DepositoDecretoModel();

		aModel.setIdDepositoDecreto(getBigDecimal("ID_DEPOSITO_DECRETO"));
		aModel.setAnnoS72(getBigDecimal("ANNO_S72"));
		aModel.setNumS72(getBigDecimal("NUM_S72"));
		aModel.setCodTipoDecreto(getString("COD_TIPO_DECRETO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setAltriDestinatari(getString("ALTRI_DESTINATARI"));
		aModel.setDataParerePg(getDate("DATA_PARERE_PG"));
		aModel.setCodTipoParerePg(getString("COD_TIPO_PARERE_PG"));
		aModel.setDataRicorsoImpugnazione(getDate("DATA_RICORSO_IMPUGNAZIONE"));
		aModel.setDataInvioAttiImpugnazione(getDate("DATA_INVIO_ATTI_IMPUGNAZIONE"));
		aModel.setDataSentenzaImpugnazione(getDate("DATA_SENTENZA_IMPUGNAZIONE"));
		aModel.setTenoreSentenzaImpugnazione(getString("TENORE_SENTENZA_IMPUGNAZIONE"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setSentenzeRiferimento(getString("SENTENZE_RIFERIMENTO"));
		aModel.setIdEventoGenerato(getBigDecimal("ID_EVENTO_GENERATO"));
		aModel.setCodUfficioCompetente(getString("COD_UFFICIO_COMP"));
		aModel.setCodProcuraEsecuzione(getString("COD_PROCURA_ESECUZIONE"));
		aModel.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
		aModel.setCodTdsComp(getString("COD_TDS_COMP"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setStatusPersona(getString("STATUS_PERSONA"));
		aModel.setTotOreRaggiungimento(getString("TOT_ORE_RAGGIUNGIMENTO"));
		aModel.setAnnoProcRevocato(getString("ANNO_PROC_REVOCATO"));
		aModel.setProgrProcRevocato(getString("PROGR_PROC_REVOCATO"));
		aModel.setUfficioProcRevocato(getString("UFFICIO_PROC_REVOCATO"));
		aModel.setDataCompFoglioComplementare(getDate("DATA_COMP_FOGLIO_COMPLEMENTARE"));
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		aModel.setDataSospensioneSS(getDate("DATA_SOSPENSIONE_SS"));
		aModel.setGiorniRecuperoSS(getBigDecimal("GIORNI_RECUPERO_SS"));
		aModel.setFlagRecuperoSS(getString("FLAG_RECUPERO_SS"));
		aModel.setDataScadenzaSospensioneSS(getDate("DATA_SCADENZA_SOSPENSIONE_SS"));
		aModel.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
		aModel.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
		aModel.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
		aModel.setFlagNominaComActa(getString("FLAG_NOMINA_COMM_ACTA"));
		aModel.setDescrCommActa(getString("DESCR_COMM_ACTA"));

		return aModel;
	}

	//
	// METODO GETMODEL()
	//
	/**
	 * Ritorna il model popolato con i dati del record corrispondente.
	 * <p>
	 *
	 * @return ritorna il model popolato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public GenericModel getModel() throws DAOException {
		DepositoDecretoModel aModel = new DepositoDecretoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdDepositoDecreto(getBigDecimal("ID_DEPOSITO_DECRETO"));
		aModel.setAnnoS72(getBigDecimal("ANNO_S72"));
		aModel.setNumS72(getBigDecimal("NUM_S72"));
		aModel.setCodTipoDecreto(getString("COD_TIPO_DECRETO"));
		aModel.setDescrTipoDecreto(getString("DESC_TIPO_DECRETO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setDataDeposito(getDate("DATA_DEPOSITO"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		// aModel.setDescrMagistrato(getString("") );
		aModel.setAltriDestinatari(getString("ALTRI_DESTINATARI"));
		aModel.setDataParerePg(getDate("DATA_PARERE_PG"));
		aModel.setCodTipoParerePg(getString("COD_TIPO_PARERE_PG"));
		// aModel.setDescrTipoParerePg(getString("") );
		aModel.setDataRicorsoImpugnazione(getDate("DATA_RICORSO_IMPUGNAZIONE"));
		aModel.setDataInvioAttiImpugnazione(getDate("DATA_INVIO_ATTI_IMPUGNAZIONE"));
		aModel.setDataSentenzaImpugnazione(getDate("DATA_SENTENZA_IMPUGNAZIONE"));
		aModel.setTenoreSentenzaImpugnazione(getString("TENORE_SENTENZA_IMPUGNAZIONE"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
		aModel.setSentenzeRiferimento(getString("SENTENZE_RIFERIMENTO"));
		aModel.setIdEventoGenerato(getBigDecimal("ID_EVENTO_GENERATO"));
		aModel.setCodUfficioCompetente(getString("COD_UFFICIO_COMP"));
		// aModel.setDescrUfficioComp(getString("") );
		aModel.setCodProcuraEsecuzione(getString("COD_PROCURA_ESECUZIONE"));
		// aModel.setDescrProcuraEsecuzione(getString("") );
		aModel.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));
		// nuovi campi Luigi 27-01-2004
		aModel.setCodTdsComp(getString("COD_TDS_COMP"));
		// aModel.setDescrTdsComp(getString("") );
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		aModel.setStatusPersona(getString("STATUS_PERSONA"));
		aModel.setTotOreRaggiungimento(getString("TOT_ORE_RAGGIUNGIMENTO"));
		aModel.setAnnoProcRevocato(getString("ANNO_PROC_REVOCATO"));
		aModel.setProgrProcRevocato(getString("PROGR_PROC_REVOCATO"));
		aModel.setUfficioProcRevocato(getString("UFFICIO_PROC_REVOCATO"));
		aModel.setDataCompFoglioComplementare(getDate("DATA_COMP_FOGLIO_COMPLEMENTARE"));
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		aModel.setDataSospensioneSS(getDate("DATA_SOSPENSIONE_SS"));
		aModel.setGiorniRecuperoSS(getBigDecimal("GIORNI_RECUPERO_SS"));
		aModel.setFlagRecuperoSS(getString("FLAG_RECUPERO_SS"));
		aModel.setDataScadenzaSospensioneSS(getDate("DATA_SCADENZA_SOSPENSIONE_SS"));
		aModel.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
		aModel.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
		aModel.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
		aModel.setFlagNominaComActa(getString("FLAG_NOMINA_COMM_ACTA"));
		aModel.setDescrCommActa(getString("DESCR_COMM_ACTA"));
		aModel.setCodTipoControlloEsecuzione(getString("TIPO_CONTROLLO_ESECUZIONE"));
		// if (super.mStatement.indexOf("DESC_TIPO_CONTROLLO_ESECUZIONE")>-1){
		aModel.setDescrTipoControlloEsecuzione(getString("DESC_TIPO_CONTROLLO_ESECUZIONE"));
		// }
		aModel.setNumeroGiorniRevocaLA(getBigDecimal("NUM_GIORNI_REVOCA_LA"));
		// DL 92 2014 Violazione CEDU
		aModel.setNumeroGiorniRiduzionePena(getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA"));
		aModel.setSommaRisarcimentoDanni(getBigDecimal("SOMMA_RISARC_DANNI"));
		// MEV_9 aggiunto campo DATA_TERMINE_EMISSIONE
		aModel.setDataTermineEmissione(getDate("DATA_TERMINE_EMISSIONE"));

		return aModel;
	}

	/**
	 * Imposta le condizioni di ricerca
	 * <p>
	 *
	 * @param aModel
	 *            model con i dati per la ricerca.
	 * @return ritorna la stringa con le condizioni.
	 * @deprecated metodo da implementare
	 */
	public String setCondizione(DepositoDecretoModel aModel) {
		return new String();
	}

	/**
	 * Imposta la condizione di ricerca per id.
	 * <p>
	 *
	 * @param aKey
	 *            id del deposito decreto da ricercare.
	 * @return la condizione.
	 */
	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_DEPOSITO_DECRETO = " + aKey;
	}

	/**
	 * Imposta la condizione per effettuare la ricerca di un deposito decreto per id generale procedimento.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento.
	 * @return la condizione di filtro.
	 */
	public String setCondizioniByIdGenProc(BigDecimal aKey) {
		return " AND GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;

	}

	/**
	 * Imposta la condizione per effettuare la ricerca di un deposito decreto per id generale procedimento e
	 * per il tipo di decreto.
	 * <p>
	 *
	 * @param aKey
	 *            id generale procedimento,
	 * @param aCod
	 *            : codice Tipo Decreto
	 * @return la condizione di filtro.
	 */
	public String setCondizioniByIdGenProcCodTipoDec(BigDecimal aKey, String aCod) {
		String lCondizioni = " AND GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
		// MEV_39: aggiunta casisitica
		String all = "('22','37','38','39','40','41','42','MS','TM')";
		if ("ALL".equals(aCod))
			lCondizioni += " AND COD_TIPO_DECRETO IN " + all;
		else
			lCondizioni += " AND COD_TIPO_DECRETO = '" + aCod + "'";
		lCondizioni += " ORDER BY DATA_EMISSIONE DESC ";
		return lCondizioni;
	}

	/**
	 * Imposta la condizione per effettuare la ricerca di un deposito decreto per Evento Generato.
	 * <p>
	 *
	 * @param aIdEveGenerato
	 * @return la condizione di filtro.
	 */
	public String setCondizioniByIdEveGenerato(BigDecimal aIdEveGenerato) {
		return " AND ID_EVENTO_GENERATO = " + aIdEveGenerato;
	}

	/**
	 * Imposta la condizione per effettuare la ricerca di un deposito decreto per Evento Generato.
	 * <p>
	 *
	 * @param aIdEveGenerato
	 * @return la condizione di filtro.
	 */
	public String setCondizioniByIdEveGeneratoNoDescTipoDecreto(BigDecimal aIdEveGenerato) {
		return " ID_EVENTO_GENERATO = " + aIdEveGenerato;
	}

	/**
	 * Calcola il Massimo NUM_S72 relativo ad un certo ufficio e all'anno in corso. Il massimo NUM_S72
	 * rappresenta l'ultimo NUM_S72 inserito all'interno dell'ufficio trattato.
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void getProgressivoS72(DepositoDecretoModel aModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(NUM_S72) aMAX";
		lStatement += " FROM DEPOSITO_DECRETO ";
		lStatement += " WHERE ANNO_S72 = " + aModel.getAnnoS72();
		lStatement += " AND COD_UFFICIO_INSERIMENTO = " + aModel.getCodUfficioInserimento();

		setStatement(lStatement);
	}

	/**
	 * <p>
	 * Description: metodo di ricerca, restituisce la DATA_DEPOSITO di record in DEPOSITO_DECRETO selezionato
	 * tramite ID_EVENTO_GENERATO.
	 * </p>
	 *
	 * @param BigDecimal
	 *            aKey : Identificativo Evento
	 * @return Date : Data di deposito Decreto
	 * @throws DAOException
	 */
	public Date getDataDepositoByEve(BigDecimal aIdEve) throws DAOException {
		Date retData = null;

		String lStatement = "select DATA_DEPOSITO from DEPOSITO_DECRETO D WHERE D.ID_EVENTO_GENERATO = "
				+ aIdEve;
		setStatement(lStatement);

		this.start();
		if (this.next()) {
			retData = getDate("DATA_DEPOSITO");
		}
		return retData;
	}

	public void RicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(BigDecimal idFascicoloSiep)
			throws DAOException {

		String lSql = "SELECT DISTINCT FASC.ID_FASCICOLO_SIUS," + "                FASC.CHIAVE_ANNO,"
				+ "                FASC.CHIAVE_PROGR," + "                FASC.CHIAVE_UFFICIO,"
				+ "                EVE.COD_TIPO_PROVVEDIMENTO," + "                EVE.COD_MOTIVO,"
				+ "                EVE.DATA_EMISSIONE," + "                EVE.ID_EVENTO,"
				+ "                EVE.COD_ESITO," + "                EVE.ANNO_PROTOCOLLO,"
				+ "                EVE.PROGR_PROTOCOLLO,"
				+ "				   UFF.COD_TIPO_UFFICIO            COD_TIPO_UFFICIO,"
				+ "				   UFF.COD_UFFICIO		         COD_UFFICIO,"
				+ "                UFD.DESCR_TIPO_UFFICIO          DESCR_TIPO_UFFICIO,"
				+ "                DESCR_COM_UFF.DESCRIZIONE       DESCR_COMUNE_UFFICIO,"
				+ "                MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_OGGETTO,"
				+ "                ESITO_PROVVEDIMENTO.RV_MEANING  DESCR_ESITO,"
				+ "                TIPO_PROVVEDIMENTO.RV_MEANING   DESCR_PROVVEDIMENTO,"
				+ "                NULL							 FLAG_DECISIONE_TRIBUNALE,"
				+ "                P.LUOGO_SVOLGIMENTO_PROVA," + "" + ""
				+ "                P.Data_Sospensione_Ss," + "                P.Data_Scadenza_Sospensione_Ss,"
				+ " " + "                P.SOSPENSIONE_GG," + "                P.SOSPENSIONE_MM,"
				+ "                P.SOSPENSIONE_AA," + "                MA.DATA_SCARCERAZIONE,"
				+ "                MA.COD_TIPO_UFFICIO_SCARCERAZIONE,"
				+ "				   MA.ID_MISURA_ALTERNATIVA," + "				 MA.DATA_INIZIO_MISURA,"
				+ "				   MA.DATA_FINE_MISURA, "
				// AGGIUNGO PER ESTRAPOLE ANNO_PROTOCOLLO E NUMERO_PROTOCOLLO
				+ "                P.anno_s72 ANNO_PROTOCOLLO_S, "
				+ "                P.NUM_S72  NUMERO_PROTOCOLLO_S "
				// FINE
				+ "        FROM FASCICOLO_SIUS        FASC," + "        EVENTO                EVE,"
				+ "       UFFICIO               UFF," + "       COMUNE                DESCR_COM_UFF,"
				+ "       UFFICIO_DESCR         UFD," + "       CG_REF_CODES          MOTIVO_PROVVEDIMENTO,"
				+ "       CG_REF_CODES          ESITO_PROVVEDIMENTO,"
				+ "       CG_REF_CODES          TIPO_PROVVEDIMENTO," + "       DEPOSITO_DECRETO P,"
				+ "       MISURA_ALTERNATIVA    MA ," + "       FASCICOLO_SIEP FS "

				+ " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ "   AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ "   AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ "   AND EVE.ID_EVENTO = P.ID_EVENTO_GENERATO(+)"
				+ "   AND EVE.COD_TIPO_PROVVEDIMENTO IN ('02')" +
				// 20190919 [SG]: modificata condizione per mancanza codici
				// + " AND EVE.COD_MOTIVO IN"
				// + " ('0428', '0429', '0430', '0431', '0432', '0433', '2550', '2551', '2552', '2553',
				// '2554', '2555', '2610', '2611')"
				// + " AND MOTIVO_PROVVEDIMENTO.rv_alt2_value in ('1148','1138','1139')" in alternativa
				"   AND MOTIVO_PROVVEDIMENTO.rv_high_value in ('C036','U077','U082')"
				+ "   AND EVE.COD_ESITO IN"
				+ " ('0002', '0003', '0004', '0005', '0035', '0119', '0145', '0360')"
				+ "   AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO"
				+ "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ "   AND UFF.COD_UFFICIO = UFD.COD_UFFICIO"
				+ "   AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'"
				+ "   AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO"
				+ "   AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'"
				+ "   AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO"
				+ "   AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'"
				+ "   AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_ESITO"

				+ "   AND MA.FAS_SIE_ID_FASCICOLO_SIEP(+) =  " + idFascicoloSiep
				+ "	AND MA.EVE_ID_EVENTO(+) = EVE.ID_EVENTO "
				+ "    AND FS.ID_FASCICOLO_SIEP=FASC.FAS_SIE_ID_FASCICOLO_SIEP  AND FS.ID_FASCICOLO_SIEP NOT IN("
				+ "  (SELECT EE.FAS_SIE_ID_FASCICOLO_SIEP  FROM EVENTO EE WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP = FS.ID_FASCICOLO_SIEP"
				+ "  AND EE.COD_MOTIVO IN ('1132') AND EE.FLAG_DOCUMENTO_REGISTRATO <> 'A' )  )"
				+ " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.ID_FASCICOLO_SIUS";

		setStatement(lSql);
	}

	public void RicercaEventoProvvDiffSIUSByFascSiepEFascSius(BigDecimal idFascicoloSiep,
			BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento) throws DAOException {

		String lSql = "SELECT DISTINCT FASC.ID_FASCICOLO_SIUS," + "                FASC.CHIAVE_ANNO,"
				+ "                FASC.CHIAVE_PROGR," + "                FASC.CHIAVE_UFFICIO,"
				+ "                EVE.COD_TIPO_PROVVEDIMENTO," + "                EVE.COD_MOTIVO,"
				+ "                EVE.DATA_EMISSIONE," + "                EVE.ID_EVENTO,"
				+ "                EVE.COD_ESITO," + "                EVE.ANNO_PROTOCOLLO,"
				+ "                EVE.PROGR_PROTOCOLLO,"
				+ "				 UFF.COD_TIPO_UFFICIO            COD_TIPO_UFFICIO,"
				+ "				 UFF.COD_UFFICIO		         COD_UFFICIO,"
				+ "                UFD.DESCR_TIPO_UFFICIO          DESCR_TIPO_UFFICIO,"
				+ "                DESCR_COM_UFF.DESCRIZIONE       DESCR_COMUNE_UFFICIO,"
				+ "                MOTIVO_PROVVEDIMENTO.RV_MEANING DESCR_OGGETTO,"
				+ "                ESITO_PROVVEDIMENTO.RV_MEANING  DESCR_ESITO,"
				+ "                TIPO_PROVVEDIMENTO.RV_MEANING   DESCR_PROVVEDIMENTO,"
				+ "                NULL							 FLAG_DECISIONE_TRIBUNALE,"

				+ "                P.LUOGO_SVOLGIMENTO_PROVA," + " "
				+ "                P.Data_Sospensione_Ss," + "                P.Data_Scadenza_Sospensione_Ss,"
				+ " " + "                P.SOSPENSIONE_GG," + "                P.SOSPENSIONE_MM," + " "
				+ "                P.SOSPENSIONE_AA,"
				// AGGIUNGO PER ESTRAPOLE ANNO_PROTOCOLLO E NUMERO_PROTOCOLLO
				+ "                P.anno_s72 ANNO_PROTOCOLLO_S, "
				+ "                P.NUM_S72  NUMERO_PROTOCOLLO_S, "
				// FINE
				+ "                MA.DATA_SCARCERAZIONE,"
				+ "                MA.COD_TIPO_UFFICIO_SCARCERAZIONE,"
				+ "				   MA.ID_MISURA_ALTERNATIVA," + "                MA.DATA_INIZIO_MISURA,"
				+ "				   MA.DATA_FINE_MISURA" + "  FROM FASCICOLO_SIUS        FASC,"
				+ "       EVENTO                EVE," + "       UFFICIO               UFF,"
				+ "       COMUNE                DESCR_COM_UFF," + "       UFFICIO_DESCR         UFD,"
				+ "       CG_REF_CODES          MOTIVO_PROVVEDIMENTO,"
				+ "       CG_REF_CODES          ESITO_PROVVEDIMENTO,"
				+ "       CG_REF_CODES          TIPO_PROVVEDIMENTO," + " "

				+ "      DEPOSITO_DECRETO P,"

				+ "       MISURA_ALTERNATIVA    MA" + " WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = "
				+ idFascicoloSiep + "   AND EVE.FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep
				+ "	AND FASC.ID_FASCICOLO_SIUS = " + idFascSius;
		if (idEveFascSius != null)
			lSql = lSql + "   AND EVE.ID_EVENTO = " + idEveFascSius;
		lSql = lSql + "   AND EVE.FAS_SIU_ID_FASCICOLO_SIUS = FASC.ID_FASCICOLO_SIUS"
				+ "   AND EVE.ID_EVENTO = P.ID_EVENTO_GENERATO(+)"
				+ "   AND EVE.COD_TIPO_PROVVEDIMENTO IN ('02')"

				+ "   AND MOTIVO_PROVVEDIMENTO.rv_high_value in ('C036','U077','U082')"
				+ "   AND EVE.COD_ESITO IN"
				+ " ('0002', '0003', '0004', '0005', '0035', '0119', '0145', '0360')"
				+ "   AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO"
				+ "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE"
				+ "   AND UFF.COD_UFFICIO = UFD.COD_UFFICIO"
				+ "   AND TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'"
				+ "   AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_TIPO_PROVVEDIMENTO"
				+ "   AND MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'"
				+ "   AND MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_MOTIVO"
				+ "   AND ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'"
				+ "   AND ESITO_PROVVEDIMENTO.RV_LOW_VALUE = EVE.COD_ESITO"
				+ "   AND MA.FAS_SIE_ID_FASCICOLO_SIEP =  " + idFascicoloSiep + "   AND MA.EVE_ID_EVENTO = "
				+ idEvento + " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.ID_FASCICOLO_SIUS";

		setStatement(lSql);
	}

	public GenericModel getModelEsitoDiffMisSic() throws DAOException {

		DecretoEventoTenoriFascicoloSiusModel aModel = new DecretoEventoTenoriFascicoloSiusModel();

		FascicoloSiusModel lFas = new FascicoloSiusModel();
		lFas.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lFas.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFas.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFas.setChiaveUfficio(getString("CHIAVE_UFFICIO"));

		aModel.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
		aModel.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));

		EventoModel lEve = new EventoModel();
		lEve.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEve.setCodMotivo(getString("COD_MOTIVO"));
		lEve.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEve.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEve.setCodEsito(getString("COD_ESITO"));
		lEve.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		lEve.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		lEve.setCodTipoUfficioEmittente(getString("COD_TIPO_UFFICIO"));
		lEve.setCodLuogoEmittente(getString("COD_UFFICIO"));

		aModel.setDescrOggetto(getString("DESCR_OGGETTO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));
		aModel.setDescrProvvedimento(getString("DESCR_PROVVEDIMENTO"));
		//
		aModel.setAnnoProtocollo(getString("ANNO_PROTOCOLLO_S"));
		aModel.setNumeroProtocollo(getString("NUMERO_PROTOCOLLO_S"));

		DepositoDecretoModel dopm = new DepositoDecretoModel();
		dopm.setDataSospensioneSS(getDate("Data_Sospensione_Ss"));
		dopm.setDataScadenzaSospensioneSS(getDate("Data_Scadenza_Sospensione_Ss"));
		dopm.setSospensioneAASS(getBigDecimal("SOSPENSIONE_AA"));
		dopm.setSospensioneMMSS(getBigDecimal("SOSPENSIONE_MM"));
		dopm.setSospensioneGGSS(getBigDecimal("SOSPENSIONE_GG"));
		dopm.setLuogoSvolgimentoProva(getString("LUOGO_SVOLGIMENTO_PROVA"));

		MisuraAlternativaModel mam = new MisuraAlternativaModel();
		mam.setCodTipoUfficioScarcerazione(getString("COD_TIPO_UFFICIO_SCARCERAZIONE"));
		mam.setDataScarcerazione(getDate("DATA_SCARCERAZIONE"));
		mam.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
		mam.setIdMisuraAlternativa(getBigDecimal("ID_MISURA_ALTERNATIVA"));
		mam.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
		mam.setDataFineMisura(getDate("DATA_FINE_MISURA"));

		aModel.setFascicoloSius(lFas);
		aModel.setEvento(lEve);
		aModel.setDeceto(dopm);
		aModel.setMisuraAlternativa(mam);

		// valore di ritorno
		return aModel;
	}

}