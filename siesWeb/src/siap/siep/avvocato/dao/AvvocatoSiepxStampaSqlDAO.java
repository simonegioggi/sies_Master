package siap.siep.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class AvvocatoSiepxStampaSqlDAO	extends SIAPSqlDAO
{
	public AvvocatoSiepxStampaSqlDAO(Connection con)
   {
		super(con);
   }

	public void ricercaAvvocatiByFascicolo(BigDecimal aKey) throws DAOException
   {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aKey;

		//Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO ASC";

		setStatement(lStatement);
    }

	public void ricercaAvvocatoByKeyAvvocatoFasSiep(BigDecimal aKey) throws DAOException
		{
		 String lStatement = getSqlQuery();

		 lStatement += " AND AVVOCATO_FASCICOLO_SIEP.ID_AVVOCATO_FASCICOLO_SIEP=" + aKey;

		 setStatement(lStatement);
	  }

	private String getSqlQuery()
   {

		String lStatement = new String("");

		lStatement += " SELECT " +
			"ID_AVVOCATO, " +
			"COGNOME, " +
			"NOME, " +
			"FORO, " +
			"INDIRIZZO, " +
			"TELEFONO, " +
			"FAX, " +
			"E_MAIL, " +
			"COD_FISCALE, " +
			"COD_COMUNE_RESIDENZA, " +
			"AVVOCATO.COD_OPERATORE_INSERIMENTO, " +
			"AVVOCATO.DATA_INSERIMENTO, " +
			"AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, " +
			"AVVOCATO.DATA_AGGIORNAMENTO, " +
			"AVVTIPODESC.RV_MEANING DESCRTIPO, " +
      "DESCRCOMUNE.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "+
			"ID_AVVOCATO_FASCICOLO_SIEP, " +
			"COD_TIPO_AVVOCATO, " +
			"DATA_INIZIO_VALIDITA, " +
			"DATA_FINE_VALIDITA, " +
			"AVVOCATO_FASCICOLO_SIEP.COD_OPERATORE_INSERIMENTO, " +
			"AVVOCATO_FASCICOLO_SIEP.DATA_INSERIMENTO, " +
			"AVVOCATO_FASCICOLO_SIEP.COD_UFFICIO_INSERIMENTO, " +
			"AVVOCATO_FASCICOLO_SIEP.COD_OPERATORE_AGGIORNAMENTO, " +
			"AVVOCATO_FASCICOLO_SIEP.DATA_AGGIORNAMENTO, " +
			"AVVOCATO_FASCICOLO_SIEP.COD_UFFICIO_AGGIORNAMENTO, " +
			"AVV_ID_AVVOCATO, " +
			"FAS_SIE_ID_FASCICOLO_SIEP ";
		lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,COMUNE DESCRCOMUNE";
		lStatement += " WHERE ";
		lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
    lStatement += " DESCRCOMUNE.COD_COMUNE = COD_COMUNE_RESIDENZA AND";
		lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIEP.AVV_ID_AVVOCATO AND ";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO AND ";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' ";

		return lStatement;

  }


	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException
   {
		AvvocatoSiepModel aModel = new AvvocatoSiepModel();

		//Inserire le opportune set delle descrizioni!
		aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.getAvvocato().setCognome(getString("COGNOME"));
		aModel.getAvvocato().setNome(getString("NOME"));
		aModel.getAvvocato().setForo(getString("FORO"));
		aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
		aModel.getAvvocato().setTelefono(getString("TELEFONO"));
		aModel.getAvvocato().setFax(getString("FAX"));
		aModel.getAvvocato().setEMail(getString("E_MAIL"));
		aModel.getAvvocato().setEMail(getString("COD_FISCALE"));
    aModel.getAvvocato().setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
    aModel.getAvvocato().setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));

	//	aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
	//	aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
	//	aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
	//	aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
		aModel.getAvvocatoFascicoloSiepModel().setIdAvvocatoFascicoloSiep(getBigDecimal("ID_AVVOCATO_FASCICOLO_SIEP"));
	  aModel.getAvvocatoFascicoloSiepModel().setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
	  aModel.getAvvocatoFascicoloSiepModel().setCodTipoAvvocato(getString("COD_TIPO_AVVOCATO"));
    aModel.getAvvocatoFascicoloSiepModel().setDescrTipoAvvocato(getString("DESCRTIPO"));

		//aModel.getAvvocatoFascicoloSiepModel().

		return aModel;
  }

}