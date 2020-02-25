package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.IspRelatoreSelezionatiModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: IspConteggioOggettiDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IspConteggioOggetti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class IspRelatoreSelezionatiDAO extends TableDAO  {
	public IspRelatoreSelezionatiDAO (Connection con) {
		super(con);
		setTable("ISP_RELATORE_SELEZIONATI");
		setField("COD_MAGISTRATO", STRING);
		setField("TIPO_UFFICIO", STRING);
		setField("COD_UFFICIO", STRING);		
	}

  //
  // METODI GET()
  //

	public String 	getCodMagistrato() 	throws DAOException	 { return getString("COD_MAGISTRATO"); } 
	public String 	getTipoUfficio() 	throws DAOException	 { return getString("TIPO_UFFICIO"); } 
	public String 	getCodUfficio() 	throws DAOException	 { return getString("COD_UFFICIO"); }
	
	
  //
  // METODI SET()
  //

	public void  setCodMagistrato(String aValore ) 	{ setString("COD_MAGISTRATO", aValore); } 
	public void  setTipoUfficio(String aValore ) 	{ setString("TIPO_UFFICIO", aValore); } 
	public void  setCodUfficio(String aValore ) 	{ setString("COD_UFFICIO", aValore); } 
		
	public GenericModel getModel() throws DAOException {
		IspRelatoreSelezionatiModel lModel = new IspRelatoreSelezionatiModel();
		lModel.setCodMagistrato(getCodMagistrato());
		lModel.setTipoUfficio(getTipoUfficio());
		lModel.setCodUfficio(getCodUfficio());
		return lModel;
	}

	public void setDAOFromModel(IspRelatoreSelezionatiModel aModel) throws DAOException {
		setCodMagistrato(aModel.getCodMagistrato() );  
		setTipoUfficio(aModel.getTipoUfficio());
		setCodUfficio(aModel.getCodUfficio() );
	}

	public void setCondizione(IspRelatoreSelezionatiModel aModel) {
		String lCondizioni = new String(); 
		boolean lInserito = false; 
		if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizione(String lCondizioni){
		if ( lCondizioni.compareTo("") != 0  ) setCondition(lCondizioni); 
	}
	
	public void setCondizioneByUfficio( String aCodUfficio ) {
		setCondition("COD_UFFICIO = " + "'" + aCodUfficio + "'");
	}

}
