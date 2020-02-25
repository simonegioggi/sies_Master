package siap.sico.template.dao;

import java.sql.Connection;

import siap.dao.SIAPTableDAO;
import siap.sico.template.model.TemplateModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: TemplateDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Template</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class TemplateDAO extends SIAPTableDAO
{
	public TemplateDAO (Connection con)
	{
			 super(con);
			 setTable("TEMPLATE");

			 //Settare la Sequence e i campi chiave
       setFieldKey("ID_TEMPLATE", STRING);
			 setField("ID_TEMPLATE", STRING);
			 setField("NOME_TEMPLATE", STRING);
			 setField("DESCR", STRING);
			 setField("PATH_RICERCA", STRING);
			 setField("COD_TIPO_EVENTO", STRING);
			 setField("COD_TIPO_PROVVEDIMENTO", STRING);
			 setField("COD_MOTIVO", STRING);
       setField("FLAG_TEMPLATE", STRING);
			 setField("COD_ESITO", STRING);
			 setField("COD_OGGETTO_PROCEDIMENTO", STRING);
       setField("MAG_COD_MAGISTRATO", STRING);
			 setField("COD_TIPO_PROVVEDIMENTO_SIGE", STRING);
	}

  //
  // METODI GET()
  //
      public String 				 getIdTemplate() 		throws DAOException	 { return getString("ID_TEMPLATE"); }
      public String 				 getNomeTemplate() 		throws DAOException	 { return getString("NOME_TEMPLATE"); }
      public String 				 getDescr() 		        throws DAOException	 { return getString("DESCR"); }
      public String 				 getPathRicerca() 		throws DAOException	 { return getString("PATH_RICERCA"); }
      public String 				 getCodTipoEvento() 		throws DAOException	 { return getString("COD_TIPO_EVENTO"); }
      public String 				 getCodTipoProvvedimento() 	throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
      public String 				 getCodMotivo() 		throws DAOException	 { return getString("COD_MOTIVO"); }
      public String 				 getFlagTemplate() 		throws DAOException	 { return getString("FLAG_TEMPLATE"); }
      public String 				 getCodEsito() 		        throws DAOException	 { return getString("COD_ESITO"); }
      public String 				 getCodOggettoProcedimento() 	throws DAOException	 { return getString("COD_OGGETTO_PROCEDIMENTO"); }
      public String 				 getCodMagistrato() 	        throws DAOException	 { return getString("MAG_COD_MAGISTRATO"); }
      public String 				 getCodTipoProvvedimentoSige() 	throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO_SIGE"); }

  //
  // METODI SET()
  //
    public void  	 setIdTemplate(String aValore ) 	{ setString("ID_TEMPLATE", aValore); }
    public void  	 setNomeTemplate(String aValore ) { setString("NOME_TEMPLATE", aValore); }
    public void  	 setDescr(String aValore ) 			  { setString("DESCR", aValore); }
    public void  	 setPathRicerca(String aValore ) 	{ setString("PATH_RICERCA", aValore); }
    public void  	 setCodTipoEvento(String aValore ){ setString("COD_TIPO_EVENTO", aValore); }
    public void  	 setCodTipoProvvedimento(String aValore ) { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
    public void  	 setCodMotivo(String aValore ) 		{ setString("COD_MOTIVO", aValore); }
    public void  	 setFlagTemplate(String aValore ) 		{ setString("FLAG_TEMPLATE", aValore); }
    public void  	 setCodEsito(String aValore ) 			 { setString("COD_ESITO", aValore); }
    public void  	 setCodOggettoProcedimento(String aValore ) 		{ setString("COD_OGGETTO_PROCEDIMENTO", aValore); }
    public void  	 setCodMagistrato(String aValore ) 			 { setString("MAG_COD_MAGISTRATO", aValore); }
    public void  	 setCodTipoProvvedimentoSige(String aValore ) { setString("COD_TIPO_PROVVEDIMENTO_SIGE", aValore); }

    public GenericModel getModel() throws DAOException
    {
      return new TemplateModel(
                  getIdTemplate() ,
                  getNomeTemplate() ,
                  getDescr() ,
                  getPathRicerca() ,
                  getCodTipoEvento() ,
                   "",
                   getCodTipoProvvedimento() ,
                   "",
                   getCodMotivo(),
                  "",
                  getFlagTemplate(),
                  getCodEsito() ,
                  "",
                  getCodOggettoProcedimento(),
                  "",
                  getCodMagistrato(),
      						getCodTipoProvvedimentoSige());
    }


    public void setDAOFromModel(TemplateModel aModel) throws DAOException
    {
        setIdTemplate( aModel.getIdTemplate() );
        setNomeTemplate( aModel.getNomeTemplate() );
        setDescr( aModel.getDescr() );
        setPathRicerca( aModel.getPathRicerca() );
        setCodTipoEvento( aModel.getCodTipoEvento() );
        setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
        setCodMotivo( aModel.getCodMotivo() );
        setFlagTemplate( aModel.getFlagTemplate() );
        setCodEsito( aModel.getCodEsito() );
        setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
        setCodMagistrato(aModel.getCodMagistrato());
        setCodTipoProvvedimentoSige( aModel.getCodTipoProvvedimentoSige() );
    }


    public void setDAOFromModelForUpdate(TemplateModel aModel) throws DAOException
    {
      setIdTemplate( aModel.getIdTemplate() );
      setNomeTemplate( aModel.getNomeTemplate() );
      setDescr( aModel.getDescr() );
      setPathRicerca( aModel.getPathRicerca() );
      setCodTipoEvento( aModel.getCodTipoEvento() );
      setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
      setCodMotivo( aModel.getCodMotivo() );
      setFlagTemplate( aModel.getFlagTemplate() );
      setCodEsito( aModel.getCodEsito() );
      setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );
      setCodMagistrato(aModel.getCodMagistrato());
      setCodTipoProvvedimentoSige( aModel.getCodTipoProvvedimentoSige() );

      setCondizioneUpdate(aModel.getIdTemplate());
    }


    public void setCondizione(TemplateModel aModel)
    {
      String lCondizioni = new String();

      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }


    public void setCondizioneUpdate(String key)
    {
       setCondition(" ID_TEMPLATE = '" + key + "'" );
    }

    public void   setCondizioneById(String key)
    {
       setCondition(" ID_TEMPLATE = '" + key + "'" );
    }

}
