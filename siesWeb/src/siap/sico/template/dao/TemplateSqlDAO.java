package siap.sico.template.dao;


import java.sql.Connection;
import java.util.Collection;

import siap.dao.SIAPSqlDAO;
import siap.sico.template.model.TemplateModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: TemplateSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Template</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class TemplateSqlDAO extends SIAPSqlDAO
{
	 public TemplateSqlDAO (Connection con)
			{
			 super(con);
			}

 //
  // METODO RICERCA()
  //

  public void ricercaTemplate( TemplateModel  aModel)
  throws DAOException
  {
    String lSql = getSqlQuery();


    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

    @SuppressWarnings("rawtypes")
	public Collection ricercaAll()	 throws DAOException
		{
			 String lSql = getSqlQuery() + " ORDER BY ID_TEMPLATE";

			 setStatement(lSql);

       //Collection lColl = this.getModels(1,1000);
       Collection lColl = this.getModels();

       return lColl;
		}


  public void ricercaTemplateByKey( String aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

       lSql += " WHERE " + setCondizioniByKey(aKey);
			 setStatement(lSql);
		}

    public void ricercaTemplateByCodMotivo( String aCodMotivo )	 throws DAOException
		{
			 String lSql = getSqlQuery();

       lSql += " WHERE " + setCondizioniByCodMotivo(aCodMotivo);
			 setStatement(lSql);
		}

    public void ricercaTemplateByCodMotivoFlagTemplate( String aCodMotivo,String aFlagTemplate )	 throws DAOException
		{
			 String lSql = getSqlQuery();

       lSql += " WHERE " + setCondizioniByCodMotivo(aCodMotivo) + " AND " + setCondizioniByFlagTemplate(aFlagTemplate);

			 setStatement(lSql);
		}

  public void ricercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(String aTipoEvento,String aTipoProv,String aCodMotivo,String aFlagTemplate )	 throws DAOException
    {
       String lSql = getSqlQuery();

       lSql += " WHERE ID_TEMPLATE IS NOT NULL " ;
       if(aCodMotivo != null)
        lSql += " AND COD_MOTIVO = '"+ aCodMotivo+"'";

       if(aFlagTemplate != null)
        lSql += " AND FLAG_TEMPLATE = '"+ aFlagTemplate+"'";

       if(aTipoEvento != null)
        lSql += " AND COD_TIPO_EVENTO = '"+ aTipoEvento+"'";

       if(aTipoProv != null)
        lSql += " AND COD_TIPO_PROVVEDIMENTO = '"+ aTipoProv+"'";
       setStatement(lSql);
    }


   public String  getNomeFileTemplateByKey( String aKey)	 throws DAOException
		{
			 String lSql = getSqlQuery();

       lSql += " WHERE " + setCondizioniByKey(aKey);

			 setStatement(lSql);

       TemplateModel lTemp = (TemplateModel)getModelByKey();

       return lTemp.getPathRicerca() + lTemp.getNomeTemplate();
		}


  protected String getSqlQuery()
  {
      String lStatement = new String("");

      lStatement += " SELECT " +
                  "ID_TEMPLATE, "+
                  "NOME_TEMPLATE, "+
                  "DESCR, "+
                  "PATH_RICERCA, "+
                  "COD_TIPO_EVENTO, "+
                  "COD_TIPO_PROVVEDIMENTO, "+
                  "COD_MOTIVO, "+
                   "FLAG_TEMPLATE, "+
                   "COD_ESITO, "+
                  "COD_OGGETTO_PROCEDIMENTO, "+
                  "MAG_COD_MAGISTRATO, "+
                  "COD_TIPO_PROVVEDIMENTO_SIGE ";
      // NUOVA INFRASTRUTTURA: cambiato il nome della tabella
      lStatement += " FROM TEMPLATE";

      return lStatement;
  }


 //
  // METODO GETMODEL()
  //


  public GenericModel  	 getModel() throws DAOException
  {
        TemplateModel aModel = new  TemplateModel();

//Inserire le opportune set delle descrizioni!
        aModel.setIdTemplate(getString("ID_TEMPLATE") );
        aModel.setNomeTemplate(getString("NOME_TEMPLATE") );
        aModel.setDescr(getString("DESCR") );
        aModel.setPathRicerca(getString("PATH_RICERCA") );
        aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO") );
        //aModel.setDescrTipoEvento(getString("") );
        aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
        //aModel.setDescrTipoProvvedimento(getString("") );
        aModel.setCodMotivo(getString("COD_MOTIVO") );
        //aModel.setDescrMotivo(getString("") );
        aModel.setFlagTemplate(getString("FLAG_TEMPLATE") );
        aModel.setCodEsito(getString("COD_ESITO") );
        aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO") );
        aModel.setCodMagistrato(getString("MAG_COD_MAGISTRATO"));
        aModel.setCodTipoProvvedimentoSige(getString("COD_TIPO_PROVVEDIMENTO_SIGE") );
        return aModel;
  }


  public String  setCondizione(TemplateModel aModel)
  {
      String lCondizioni = new String("");
      boolean lInserito = false;


      if (aModel.getIdTemplate().length() > 1)
      {
        lCondizioni += " ID_TEMPLATE = '"+ aModel.getIdTemplate() +"'";
        lInserito = true;
      }
      if (aModel.getNomeTemplate().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " NOME_TEMPLATE = '"+ aModel.getNomeTemplate() +"'";
      }
      if (aModel.getDescr().length() > 1)
      {
         if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " DESCR = '"+ aModel.getDescr() +"'";
      }
      if (aModel.getPathRicerca().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " PATH_RICERCA = '"+ aModel.getPathRicerca() +"'";
      }
      if (aModel.getCodTipoEvento().length() > 1)
      {
         if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_TIPO_EVENTO = '"+ aModel.getCodTipoEvento() +"'";
      }
      if (aModel.getCodTipoProvvedimento().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_TIPO_PROVVEDIMENTO = '"+ aModel.getCodTipoProvvedimento() +"'";
      }
      // 21-05-2009
      if (aModel.getCodTipoProvvedimentoSige().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_TIPO_PROVVEDIMENTO_SIGE = '"+ aModel.getCodTipoProvvedimentoSige() +"'";
      }

      if (aModel.getCodMotivo().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_MOTIVO = '"+ aModel.getCodMotivo() +"'";
      }
      if (aModel.getFlagTemplate().length() > 0)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        // 01/06/2010 Solo per SIGE, in caso di valorizzazione di FLAG_TEMPLATE si includono anche i template con il flag = null.
        if (aModel.getCodTipoProvvedimentoSige().length() > 1)
        	lCondizioni += " (FLAG_TEMPLATE = '"+ aModel.getFlagTemplate() +"' OR FLAG_TEMPLATE is null)";
        else
        	lCondizioni += " FLAG_TEMPLATE = '"+ aModel.getFlagTemplate() +"'";
      }
      if (aModel.getCodEsito().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_ESITO = '"+ aModel.getCodEsito() +"'";
      }
      if (aModel.getCodOggettoProcedimento().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " COD_OGGETTO_PROCEDIMENTO = '"+ aModel.getCodOggettoProcedimento() +"'";
      }
      if (aModel.getCodMagistrato().length() > 1)
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += "( MAG_COD_MAGISTRATO = '"+ aModel.getCodMagistrato() +"' OR MAG_COD_MAGISTRATO IS NULL) ";
      }
      else
      {
        if (lInserito)
          lCondizioni += " AND ";
        else
          lInserito = true;
        lCondizioni += " MAG_COD_MAGISTRATO IS NULL ";
      }

        if (lInserito)
          // 22/05/2007 Aggiunto l'ordinamento per ID_TEMPLATE
          //lCondizioni = " WHERE " + lCondizioni;
          lCondizioni = " WHERE " + lCondizioni + " ORDER BY ID_TEMPLATE";

      return lCondizioni;
  }

  public String setCondizioniByKey(String aKey)
  {
      return " ID_TEMPLATE = '" + aKey + "'";
  }

  public String setCondizioniByCodMotivo(String aCodMotivo)
  {
      return " COD_MOTIVO = '" + aCodMotivo + "'";
  }

  public String setCondizioniByFlagTemplate(String aFlagTemplate)
  {
      return " FLAG_TEMPLATE = '" + aFlagTemplate + "'";
  }

}