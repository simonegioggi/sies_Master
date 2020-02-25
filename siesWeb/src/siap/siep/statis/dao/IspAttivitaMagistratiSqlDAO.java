package siap.siep.statis.dao;

/**
 * <p>Title: IspAttivitaMagistratiSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella IspAttivitaMagistrati</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.statis.model.IspAttivitaMagistratiModel;

public class IspAttivitaMagistratiSqlDAO extends SqlDAO {
	public IspAttivitaMagistratiSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRiepilogoGeneraleAttivita(int aAnno) throws DAOException {
		String lStatement = new String("");

		lStatement += "select COUNT(A.TIPOLOGIA) CONTA, " + aAnno
				+ " ANNO, b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA " + "FROM  "
				+ "(select TIPOLOGIA from ISP_ATTIVITA_MAGISTRATI where  "
				+ "	(DATA_EMISSIONE > to_date('3112" + (aAnno - 1)
				+ ":235959', 'ddmmyyyy:hh24miss') and DATA_EMISSIONE < to_date('0101" + (aAnno + 1)
				+ "', 'ddmmyyyy'))) a,  " + "ISP_TIPOLOGIA_ATTIVITA b  " + "where  "
				+ "a.TIPOLOGIA(+) = b.COD_ATTIVITA " + "group by b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA "
				+ "order by b.COD_ATTIVITA";

		setStatement(lStatement);
	}

	public void ricercaAttivitaMagistrato(int aAnno, String codMag) throws DAOException {
		String lStatement = new String("");

		lStatement += "select COUNT(A.TIPOLOGIA) CONTA, " + aAnno
				+ " ANNO, b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA " + "FROM  ";
		if ((codMag == null) || (codMag != null && codMag.equals("null")))
			lStatement += "(select TIPOLOGIA, COD_MAGISTRATO from ISP_ATTIVITA_MAGISTRATI where COD_MAGISTRATO is null and ";
		else
			lStatement += "(select TIPOLOGIA, COD_MAGISTRATO from ISP_ATTIVITA_MAGISTRATI where COD_MAGISTRATO = '"
					+ codMag + "' and ";

		lStatement += "	(DATA_EMISSIONE > to_date('3112" + (aAnno - 1)
				+ ":235959', 'ddmmyyyy:hh24miss') and DATA_EMISSIONE < to_date('0101" + (aAnno + 1)
				+ "', 'ddmmyyyy'))) a,  " + "ISP_TIPOLOGIA_ATTIVITA b  " + "where  "
				+ "a.TIPOLOGIA(+) = b.COD_ATTIVITA "
				+ "group by b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA, a.COD_MAGISTRATO "
				+ "order by b.COD_ATTIVITA";

		setStatement(lStatement);
	}

	// NGG - Statistiche Magistrato - Nuova Ricerca per COD_MOTIVO

	public void ricercaMotivoPerAttivita(String Tipo) throws DAOException {
		String lStatement = new String("");

		lStatement += "select COD_MOTIVO, DESCRIZIONE_MOTIVO, COD_ATTIVITA " + "FROM ISP_MOTIVO_ATTIVITA  "
				+ "where  " + "COD_ATTIVITA = '" + Tipo + "' " + "order by COD_MOTIVO";

		setStatement(lStatement);
	}

	public void ricercaTotaleMotivoPerAnno(int anno) throws DAOException {
		String lStatement = new String("");

		lStatement += "select count(*) CONTA, TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY') ANNO_FAS"
				+ " from ISP_ATTIVITA_MAGISTRATI" + " WHERE to_char(DATA_EMISSIONE, 'YYYY') = '" + anno + "'"
				+ " GROUP BY TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY')"
				+ " order by to_char(DATA_EMISSIONE, 'YYYY'), TIPOLOGIA, COD_MOTIVO";

		setStatement(lStatement);
	}

	public void ricercaTotaleMotivoPerMagistratoAnno(int anno) throws DAOException {
		String lStatement = new String("");

		lStatement += "select count(*) CONTA, TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY') ANNO_FAS, COD_MAGISTRATO"
				+ " from ISP_ATTIVITA_MAGISTRATI" + " WHERE to_char(DATA_EMISSIONE, 'YYYY') = '" + anno + "'"
				+ " GROUP BY TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY'), COD_MAGISTRATO"
				+ " order by COD_MAGISTRATO, to_char(DATA_EMISSIONE, 'YYYY'), TIPOLOGIA, COD_MOTIVO";

		setStatement(lStatement);
	}

	public void RicercaElencoMotiviPerMagistratoAnno(String codMag) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT A.tipologia, a.cod_motivo, chiave_anno, chiave_progr, "
				+ " A.COD_MAGISTRATO, decode (c.NOME,'-','',null,'',c.NOME)||' '||decode(c.COGNOME,'-','MAGISTRATO NON ASSEGNATO (-)',null,'MAGISTRATO NULLO',c.COGNOME) DESCR_MAGISTRATO,"
				+ " cod_ufficio_inserimento, chiave_progr_orig, desc_ufficio_inserimento, "
				+ " data_emissione, descrizione_motivo " +
                 //
                        ", DESCRIZIONE_MOTIVO_ORIG.RV_MEANING DESCR_MOTIVO_ORIG " +
                 //       
				" , ISP_TIPOLOGIA_ATTIVITA.DESCRIZIONE_ATTIVITA "
				+ " FROM ISP_ATTIVITA_MAGISTRATI a, ISP_MOTIVO_ATTIVITA b, W_MAGISTRATO c, ISP_TIPOLOGIA_ATTIVITA "
				+ " , CG_REF_CODES DESCRIZIONE_MOTIVO_ORIG "
				+ " WHERE A.cod_magistrato = C.COD_MAGISTRATO(+) " + " AND A.COD_MOTIVO = B.COD_MOTIVO "
				+ " AND A.TIPOLOGIA = B.COD_ATTIVITA "
				+ " AND b.COD_ATTIVITA = ISP_TIPOLOGIA_ATTIVITA.COD_ATTIVITA " +
                    
				" AND DESCRIZIONE_MOTIVO_ORIG.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ " AND DESCRIZIONE_MOTIVO_ORIG.RV_LOW_VALUE = A.COD_MOTIVO_ORIG ";
    
    if (!codMag.equals("0")) {
			if (codMag.equals("null"))
				lStatement += " AND A.COD_MAGISTRATO IS " + codMag + " ";
			else
				lStatement += " AND A.COD_MAGISTRATO = '" + codMag + "' ";
		}

		lStatement += " order by a.tipologia, a.cod_motivo, chiave_anno, chiave_progr";

		setStatement(lStatement);
	}

	// END NGG
	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodUfficio(getString("COD_UFFICIO"));
		aModel.setDescrUfficio(getString(""));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setDescrMagistrato(getString(""));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString(""));
		aModel.setTipologia(getString("TIPOLOGIA"));
		aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
		return aModel;
	}

	public GenericModel getModelAttivita(String aDescrMagistrato) throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setConta(getInteger("CONTA"));
		aModel.setAnno(getInteger("ANNO"));
		aModel.setTipologia(getString("COD_ATTIVITA"));
		aModel.setDescrTipologia(getString("DESCRIZIONE_ATTIVITA"));
		aModel.setDescrMagistrato(aDescrMagistrato);
		return aModel;
	}

	// NGG

	public GenericModel getModelAttivitaCodMag(String aDescrMagistrato, String aCodmag) throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setConta(getInteger("CONTA"));
		aModel.setAnno(getInteger("ANNO"));
		aModel.setTipologia(getString("COD_ATTIVITA"));
		aModel.setDescrTipologia(getString("DESCRIZIONE_ATTIVITA"));
		aModel.setDescrMagistrato(aDescrMagistrato);
		aModel.setCodMagistrato(aCodmag);
		return aModel;
	}

	public void selCondizione(IspAttivitaMagistratiModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(IspAttivitaMagistratiModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	// NGG - statistiche SIEP -
	public GenericModel getModelMotivo() throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setTipologia(getString("COD_ATTIVITA"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCRIZIONE_MOTIVO"));

		return aModel;
	}

	public int getTotale() throws DAOException {
		int tota = 0;
		tota = getInt("MOTIVO_PER_ANNO");
		return tota;
	}

	public GenericModel getTotaliPerAnno() throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setConta(getInteger("CONTA"));
		aModel.setTipologia(getString("TIPOLOGIA"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setAnno(getInteger("ANNO_FAS"));
		// aModel.setDataEmissione(getDate("DATA_EMISSIONE"));

		return aModel;
	}

	public GenericModel getTotaliPerMagAnno() throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setConta(getInteger("CONTA"));
		aModel.setTipologia(getString("TIPOLOGIA"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setAnno(getInteger("ANNO_FAS"));
		// aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));

		return aModel;
	}

	public GenericModel getElencoMotiviPerMagAnno() throws DAOException {
		IspAttivitaMagistratiModel aModel = new IspAttivitaMagistratiModel();

		aModel.setTipologia(getString("TIPOLOGIA"));
		aModel.setDescrTipologia(getString("DESCRIZIONE_ATTIVITA"));

		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCRIZIONE_MOTIVO"));

    aModel.setDescrMotivoOrig       (getString     ("DESCR_MOTIVO_ORIG"));

		aModel.setChiaveAnno(getInteger("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
		aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));

		aModel.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG"));
		aModel.setdescUfficioInserimento(getString("DESC_UFFICIO_INSERIMENTO"));

		aModel.setDescrMagistrato(getString("DESCR_MAGISTRATO"));

		return aModel;
	}

	/**
	 * Costriusce lo statement per recuperare il numero di record della tabella ISP_TIPOLOGIA_ATTIVITA
	 * 
	 * @throws DAOException
	 */
	public void getNumTipologieAttivita() throws DAOException {
		String lStatement = new String("");

		lStatement += "select COUNT(*) contaTipologie FROM ISP_TIPOLOGIA_ATTIVITA";

		setStatement(lStatement);
	}
	// END NGG

} // Chiude IspAttivitaMagistratiSqlDAO