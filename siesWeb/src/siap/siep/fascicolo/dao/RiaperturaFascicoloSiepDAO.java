package siap.siep.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.fascicolo.model.RiaperturaFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RiaperturaFascicoloSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RiaperturaFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company: Intersistemi S.p.A.</p>
* @version 1.0
*/

public class RiaperturaFascicoloSiepDAO extends TableDAO
{
	public RiaperturaFascicoloSiepDAO (Connection con)
	{
		 super(con);
		 setTable("RIAPERTURA_FASCICOLO_SIEP");

	     setSequenceField("ID_RIAPERTURA_FASCICOLO_SIEP", "RIA_FAS_SIE_SEQ");

	     setFieldKey("ID_RIAPERTURA_FASCICOLO_SIEP", BIG_DECIMAL);

		 setField("ID_RIAPERTURA_FASCICOLO_SIEP", BIG_DECIMAL);
		 setField("COD_MOTIVO", STRING);
		 setField("DATA_RIAPERTURA", DATE);
		 setField("COD_OPERATORE_INSERIMENTO", STRING);
		 setField("DATA_INSERIMENTO", DATE);
		 setField("COD_UFFICIO_INSERIMENTO", STRING);
		 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		 setField("DATA_AGGIORNAMENTO", DATE);
		 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//

		public BigDecimal 	getIdRiaperturaFascicoloSiep() 	throws DAOException	{ return getBigDecimal("ID_RIAPERTURA_FASCICOLO_SIEP"); }
		public String 		getCodMotivo() 					throws DAOException	{ return getString("COD_MOTIVO"); }
		public Date 		getDataRiapertura()				throws DAOException	{ return getDate("DATA_RIAPERTURA"); }
		public String     	getCodOperatoreInserimento()	throws DAOException	{ return getString("COD_OPERATORE_INSERIMENTO"); }
		public Date 	    getDataInserimento() 			throws DAOException	{ return getDate("DATA_INSERIMENTO"); }
		public String     	getCodUfficioInserimento()  	throws DAOException	{ return getString("COD_UFFICIO_INSERIMENTO"); }
		public String     	getCodOperatoreAggiornamento()	throws DAOException { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
		public Date 	    getDataAggiornamento() 			throws DAOException	{ return getDate("DATA_AGGIORNAMENTO"); }
		public String     	getCodUfficioAggiornamento()	throws DAOException	{ return getString("COD_UFFICIO_AGGIORNAMENTO"); }
		public BigDecimal  	getFasSieIdFascicoloSiep()		throws DAOException	{ return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }

	//
	// METODI SET()
	//

		public void setIdRiaperturaFascicoloSiep(BigDecimal aValore)	{ setBigDecimal("ID_RIAPERTURA_FASCICOLO_SIEP", aValore); }
		public void setCodMotivo(String aValore ) 			 			{ setString("COD_MOTIVO", aValore); }
		public void setDataRiapertura(Date aValore) 			      	{ setDate("DATA_RIAPERTURA", aValore); }
		public void setCodOperatoreInserimento(String aValore) 	  		{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
		public void setDataInserimento(Date aValore) 			      	{ setDate("DATA_INSERIMENTO", aValore); }
		public void setCodUfficioInserimento(String aValore) 		  	{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
		public void setCodOperatoreAggiornamento(String aValore)    	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
		public void setDataAggiornamento(Date aValore) 		      		{ setDate("DATA_AGGIORNAMENTO", aValore); }
		public void setCodUfficioAggiornamento(String aValore) 	  		{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
		public void setFasSieIdFascicoloSiep(BigDecimal aValore)		{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }

	public GenericModel getModel() throws DAOException
		{
			 return new RiaperturaFascicoloSiepModel(
							 getIdRiaperturaFascicoloSiep() ,
							 getCodMotivo() ,
							 DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(), getCodMotivo()),
							 getDataRiapertura(),
							 getCodOperatoreInserimento(),
							 getDataInserimento(),
							 getCodUfficioInserimento(),
							 getCodOperatoreAggiornamento(),
							 getDataAggiornamento(),
							 getCodUfficioAggiornamento(),
							 getFasSieIdFascicoloSiep()
							);
		}


	 public void 	 setDAOFromModel(RiaperturaFascicoloSiepModel aModel) throws DAOException
  		{
				 setIdRiaperturaFascicoloSiep( aModel.getIdRiaperturaFascicoloSiep() );
				 setCodMotivo( aModel.getCodMotivo() );
				 setDataRiapertura( aModel.getDataRiapertura() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
		}

	public void setCondizione(RiaperturaFascicoloSiepModel aModel)
	{
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
	}

}
