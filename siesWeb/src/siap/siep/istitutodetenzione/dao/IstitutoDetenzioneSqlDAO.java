package siap.siep.istitutodetenzione.dao;

import java.sql.Connection;

import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.StringUtils;

/**
* <p>Title: IstitutoDetenzioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella IstitutoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class IstitutoDetenzioneSqlDAO extends SqlDAO
{
	 public IstitutoDetenzioneSqlDAO (Connection con)
			{
			 super(con);
			}


 //
  // METODO RICERCA()
  //


  public void ricercaIstitutoDetenzione( IstitutoDetenzioneModel  aModel)	 throws DAOException
		{
      if ((aModel.getDescrComune()==null) || (aModel.getDescrComune().equals("")))
      {
			 String lSql = getSqlQuery();
       lSql += " " + setCondizioneIstituto(aModel);

			 setStatement(lSql);
       } else {
       String lSqla = " SELECT " +
				 "IST.ID_ISTITUTO_DETENZIONE, "+
				 "IST.COD_TIPO_ISTITUTO, ISTITUTO.RV_MEANING ISTITUTI, "+
				 "IST.COD_COMUNE, C.DESCRIZIONE COMUNI, "+
				 "IST.COD_PROVINCIA,PRO.RV_MEANING PROVINCIE, "+
				 "IST.INDIRIZZO, "+
				 "IST.DESCRIZIONE, "+
				 "IST.NOTE, "+
         "IST.COD_DISTRETTO ";
			 lSqla += " FROM ISTITUTO_DETENZIONE IST,(SELECT * "+
       " FROM COMUNE WHERE DESCRIZIONE LIKE '"+ StringUtils.convertSqlString(aModel.getDescrComune().toUpperCase())+
        "%') C, CG_REF_CODES ISTITUTO,CG_REF_CODES PRO";
			 lSqla += " WHERE C.COD_COMUNE = IST.COD_COMUNE AND ISTITUTO.RV_DOMAIN = 'TIPO_ISTITUTO' AND ISTITUTO.RV_LOW_VALUE = IST.COD_TIPO_ISTITUTO";
			 lSqla += " AND PRO.RV_DOMAIN = 'PROVINCIA' AND PRO.RV_LOW_VALUE = IST.COD_PROVINCIA";
       lSqla += " " + setCondizioneComune(aModel);
       lSqla += " " + setCondizione();
       setStatement(lSqla);
      }
		}


  public void ricercaIstitutoDetenzionePerDistretto( IstitutoDetenzioneModel  aModel)	 throws DAOException
  {
    String lStatement = " SELECT " +
				 "IST.ID_ISTITUTO_DETENZIONE, "+
				 "IST.COD_TIPO_ISTITUTO, ISTITUTO.RV_MEANING ISTITUTI, "+
				 "IST.COD_COMUNE, C.DESCRIZIONE COMUNI, "+
				 "IST.COD_PROVINCIA, "+
				 "IST.INDIRIZZO, "+
				 "IST.DESCRIZIONE, "+
				 "IST.NOTE, "+
         "IST.COD_DISTRETTO ";
			 lStatement += " FROM ISTITUTO_DETENZIONE IST, COMUNE C, CG_REF_CODES ISTITUTO ";
       lStatement += " WHERE C.COD_COMUNE = IST.COD_COMUNE AND ISTITUTO.RV_DOMAIN = 'TIPO_ISTITUTO' AND ISTITUTO.RV_LOW_VALUE = IST.COD_TIPO_ISTITUTO";

       if (!(aModel.getDescrComune()==null) && !(aModel.getDescrComune().equals("")))
       {
        lStatement += setCondizioneComune(aModel);
       }

       if (!(aModel.getCodDistretto()==null) && !(aModel.getCodDistretto().equals("")))
       {
        lStatement += setCondizioneDistretto(aModel);
       }

       if (!(aModel.getCodTipoIstituto().equals("-")) && !(aModel.getCodTipoIstituto().equals("")))
       {
        lStatement += setCondizioneIstituto(aModel);
       }

       lStatement += setCondizione();
       setStatement(lStatement);
  }

  public void ricercaIstitutoDetenzioneByKey( String aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

		 lSql += " " + setCondizioniByKey(aKey);
			 setStatement(lSql);
		}

  public void listaIstitutoDetenzione() throws DAOException
  {
     String lSql = getSqlQuery();

    // lSql += " ORDER BY ID_CSSA";

     setStatement( lSql );
   }



  protected String getSqlQuery()
		{			 String lStatement = new String("");

			 lStatement += " SELECT " +
				 "IST.ID_ISTITUTO_DETENZIONE, "+
				 "IST.COD_TIPO_ISTITUTO, ISTITUTO.RV_MEANING ISTITUTI, "+
				 "IST.COD_COMUNE, C.DESCRIZIONE COMUNI, "+
				 "IST.COD_PROVINCIA,PRO.RV_MEANING PROVINCIE, "+
				 "IST.INDIRIZZO, "+
				 "IST.DESCRIZIONE, "+
				 "IST.NOTE, "+
         "IST.COD_DISTRETTO ";
			 lStatement += " FROM ISTITUTO_DETENZIONE IST, COMUNE C, CG_REF_CODES ISTITUTO,CG_REF_CODES PRO";
			 lStatement += " WHERE C.COD_COMUNE = IST.COD_COMUNE AND ISTITUTO.RV_DOMAIN = 'TIPO_ISTITUTO' AND ISTITUTO.RV_LOW_VALUE = IST.COD_TIPO_ISTITUTO";
			 lStatement += " AND PRO.RV_DOMAIN = 'PROVINCIA' AND PRO.RV_LOW_VALUE = IST.COD_PROVINCIA";
                         return lStatement;		}


 //
  // METODO GETMODEL()
  //


	 public GenericModel  	 getModel() throws DAOException
  		{
				 IstitutoDetenzioneModel aModel = new  IstitutoDetenzioneModel();

//Inserire le opportune set delle descrizioni!
				 aModel.setIdIstitutoDetenzione(getString("ID_ISTITUTO_DETENZIONE") );
				 aModel.setCodTipoIstituto(getString("COD_TIPO_ISTITUTO") );
				 aModel.setDescrTipoIstituto(getString("ISTITUTI") );
				 aModel.setCodComune(getString("COD_COMUNE") );
				 aModel.setDescrComune(getString("COMUNI") );
				 aModel.setCodProvincia(getString("COD_PROVINCIA") );
				 aModel.setDescrProvincia("");
				 aModel.setIndirizzo(getString("INDIRIZZO") );
				 aModel.setDescrizione(getString("DESCRIZIONE") );
				 aModel.setNote(getString("NOTE") );
         aModel.setCodDistretto(getString("COD_DISTRETTO") );
				 return aModel;
		}


	public String  setCondizioneIstituto(IstitutoDetenzioneModel aModel)
		{
		 String lCondizioni = new String();
     lCondizioni += " AND IST.COD_TIPO_ISTITUTO = '" + aModel.getCodTipoIstituto() + "'";
 		 return lCondizioni;
 		}

  public String  setCondizioneDistretto(IstitutoDetenzioneModel aModel)
		{
		 String lCondizioni = new String();
     lCondizioni += " AND IST.COD_DISTRETTO = '" + aModel.getCodDistretto() + "'";
 		 return lCondizioni;
 		}

  public String  setCondizioneComune(IstitutoDetenzioneModel aModel)
		{
		 String lCondizioni = new String();
     lCondizioni += " AND C.DESCRIZIONE LIKE '"+ StringUtils.convertSqlString(aModel.getDescrComune().toUpperCase())+
        "%'";
 		 return lCondizioni;
 		}

  public String setCondizione()
    {
       String lCondizioni = new String();
       lCondizioni += " ORDER BY ISTITUTO.RV_MEANING , C.DESCRIZIONE ";
       return lCondizioni;
    }

  public String setCondizioniByKey(String aKey)
		{
		 return " AND ID_ISTITUTO_DETENZIONE = '" + aKey +"'";
		}
  public String setCondizioniAutoritaCompetenteByKey(String aKey)
		{
		 return " AND AUTORITA_COMPETENTE = '" + aKey +"'";
		}
}