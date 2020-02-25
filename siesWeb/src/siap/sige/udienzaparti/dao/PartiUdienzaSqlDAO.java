package siap.sige.udienzaparti.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PartiUdienzaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ANAGRAFICA_PARTI_UDIENZA</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/

public class PartiUdienzaSqlDAO extends SIAPSqlDAO
{
  public PartiUdienzaSqlDAO (Connection con)
  {
    super(con);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += "SELECT " +
		            " PARTI.ID_SOGGETTO, " +
		            " PARTI.COD_TIPO_PART, " +
		            " PARTI.COD_PARTE, " +
		            " PARTI.COD_FISCALE, " +
		            " PARTI.COGNOME, " +
		            " PARTI.NOME, " +
		            " PARTI.DENOMINAZIONE, " +
		            " PARTI.DATA_NASCITA, " +
		            " PARTI.COD_COMUNE_NASCITA, " +
		            " COM.DESCRIZIONE COMUNE_NASCITA," +
		            " PARTI.COD_STATO_NASCITA, " +
		            " DESCR_STATO.RV_MEANING DESCR_STATO_NASCITA,"+
		            " PARTI.DESC_COMUNE_NASCITA_ESTERO, " +
		            " PARTI.SESSO, " +
		            " PARTI.RAG_SOCIALE, " +
		            " PARTI.COD_PROVINCIA, " +
		            " PARTI.IND_SEDE_LEGALE, " +
		            " PARTI.IND_SEDE_OPERATIVA, " +
		            " PARTI.FLG_CONV_UDIENZA, " +
		            " PARTI.COD_OPERATORE_INSERIMENTO, " +
		            " PARTI.DATA_INSERIMENTO, " +
		            " PARTI.COD_UFFICIO_INSERIMENTO, " +
		            " PARTI.COD_OPERATORE_AGGIORNAMENTO, " +
		            " PARTI.DATA_AGGIORNAMENTO, " +
		            " PARTI.COD_UFFICIO_AGGIORNAMENTO, " +
		            " PARTI.COD_FISCALE_RAP, " +
		            " PARTI.COD_PROVINCIA_NASCITA, " +
		            " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA " +
		          "FROM ANAGRAFICA_PARTI_UDIENZA PARTI " +
		          " INNER JOIN UDIENZA_PARTI UDPART ON PARTI.ID_SOGGETTO = UDPART.ID_SOGGETTO " +
		          " LEFT OUTER JOIN SOGGETTO SOGG ON (PARTI.ID_SOGGETTO = SOGG.ID_SOGGETTO) " +
		          " LEFT OUTER JOIN COMUNE COM ON (PARTI.COD_COMUNE_NASCITA = COM.COD_COMUNE) " +
		          " LEFT OUTER JOIN CG_REF_CODES DESCR_PROVINCIA ON (PARTI.COD_PROVINCIA = DESCR_PROVINCIA.RV_LOW_VALUE AND DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA') " +
		          " LEFT OUTER JOIN CG_REF_CODES DESCR_STATO ON (DESCR_STATO.RV_DOMAIN = 'NAZIONE' AND DESCR_STATO.RV_LOW_VALUE = PARTI.COD_STATO_NASCITA) " +
		          " WHERE 1 = 1 ";
	    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel()
		  throws DAOException
  {
    AnagraficaPartiUdienzaModel aModel = new  AnagraficaPartiUdienzaModel();

	aModel.setIdSoggetto( getBigDecimal("ID_SOGGETTO") );
	aModel.setCodTipoPart( getString("COD_TIPO_PART") );
	aModel.setCodParte( getString("COD_PARTE") );
	aModel.setCodFiscale( getString("COD_FISCALE") );
	aModel.setCognome( getString("COGNOME") );
	aModel.setNome( getString("NOME") );
	aModel.setDenominazione( getString("DENOMINAZIONE") );
	aModel.setDataNascita( getDate("DATA_NASCITA") );
	aModel.setCodComuneNascita( getString("COD_COMUNE_NASCITA") );
	aModel.setDescComuneNascita( getString("COMUNE_NASCITA") );
	aModel.setCodStatoNascita( getString("COD_STATO_NASCITA") );
	aModel.setDescrStatoNascita( getString("DESCR_STATO_NASCITA") );
	aModel.setDescComuneNascitaEstero( getString("DESC_COMUNE_NASCITA_ESTERO") );
	aModel.setSesso( getString("SESSO") );
	aModel.setRagSociale( getString("RAG_SOCIALE") );
	aModel.setCodProvincia( getString("COD_PROVINCIA") );
	aModel.setIndSedeLegale( getString("IND_SEDE_LEGALE") );
	aModel.setIndSedeOperativa( getString("IND_SEDE_OPERATIVA") );
	aModel.setFlagConvUdienza( getString("FLG_CONV_UDIENZA") );
	aModel.setCodOperatoreInserimento( getString("COD_OPERATORE_INSERIMENTO") );
	aModel.setDataInserimento( getDate("DATA_INSERIMENTO") );
	aModel.setCodUfficioInserimento( getString("COD_UFFICIO_INSERIMENTO") );
	aModel.setCodOperatoreAggiornamento( getString("COD_OPERATORE_AGGIORNAMENTO") );
	aModel.setDataAggiornamento( getDate("DATA_AGGIORNAMENTO") );
	aModel.setCodUfficioAggiornamento( getString("COD_UFFICIO_AGGIORNAMENTO") );
	aModel.setCodFiscaleRap( getString("COD_FISCALE_RAP") );
	aModel.setCodProvinciaNascita( getString("COD_PROVINCIA_NASCITA") );
	aModel.setDescrProvincia( getString("DESCR_PROVINCIA") );
    
    return aModel;
  }

  public void ricercaPartiUdienzaByIdUdienza(BigDecimal aIdUdienza, String aCodTipoPart)
		  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  " + setCondizioniByIdUdienza(aIdUdienza, aCodTipoPart);
    
    setStatement(lSql);
  }

  /**
   * Metodo che imposta il filtro di condizione con
   * l'id dell'udienza e il tipo di parte interessata<p>
   * @param aIdUdienza
   * @param aCodTipoPart
   * @return String stringa di ritorno con la condizione.
   */
  public String setCondizioniByIdUdienza(BigDecimal aIdUdienza, String aCodTipoPart) {
	  String lCondizioni = new String();
	  
	  lCondizioni += " AND UDPART.ID_UDIENZA_PROCEDIMENTO_SIGE = " + aIdUdienza;
	  lCondizioni += " AND PARTI.COD_TIPO_PART = '" + aCodTipoPart + "'";
	  lCondizioni += " ORDER BY PARTI.COGNOME, PARTI.NOME, PARTI.DENOMINAZIONE ";
	  
	  return lCondizioni;
  }

  public void ricercaParteUdienzaByKey(BigDecimal aKey)
    throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " " + setCondizionByKey(aKey);

    setStatement(lStatement);
  }

  /**
   * Set condizione sulla query di SQL
   * @param aId chiave tabella
   * @return String
   */
  private String setCondizionByKey(BigDecimal aId)
  {
    String lCondizioni = " AND PARTI.ID_SOGGETTO = " + aId;

    return lCondizioni;
  }

}