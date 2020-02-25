package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.sius.statistiche.model.IspMotivoOggettoSelezionatiModel;
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

public class IspMotivoOggettoSelezionatiDAO extends TableDAO  {
	public IspMotivoOggettoSelezionatiDAO (Connection con) {
		super(con);
		setTable("ISP_MOTIVO_OGGETTO_SELEZIONATI");
		setField("COD_OGGETTO", STRING);
		setField("DESC_OGGETTO", STRING);
		setField("TIPO_UFFICIO", STRING);
		setField("COD_UFFICIO", STRING);
		setField("COD_MOTIVO", STRING);
		setField("DESC_MOTIVO", STRING);

		
	}

  //
  // METODI GET()
  //

	public String 	getCodOggetto() 	throws DAOException	 { return getString("COD_OGGETTO"); } 
	public String 	getDescOggetto() 	throws DAOException	 { return getString("DESC_OGGETTO"); }
	public String 	getCodMotivo() 		throws DAOException	 { return getString("COD_MOTIVO"); } 
	public String 	getDescMotivo() 	throws DAOException	 { return getString("DESC_MOTIVO"); } 
	public String 	getTipoUfficio() 	throws DAOException	 { return getString("TIPO_UFFICIO"); } 
	public String 	getCodUfficio() 	throws DAOException	 { return getString("COD_UFFICIO"); }
	
	
  //
  // METODI SET()
  //

	public void  setCodOggetto(String aValore ) 	{ setString("COD_OGGETTO", aValore); } 
	public void  setDescOggetto(String aValore ) 	{ setString("DESC_OGGETTO", aValore); }
	public void  setCodMotivo(String aValore ) 		{ setString("COD_MOTIVO", aValore); } 
	public void  setDescMotivo(String aValore ) 	{ setString("DESC_MOTIVO", aValore); } 
	public void  setTipoUfficio(String aValore ) 	{ setString("TIPO_UFFICIO", aValore); } 
	public void  setCodUfficio(String aValore ) 	{ setString("COD_UFFICIO", aValore); } 
		
	public GenericModel getModel() throws DAOException {
		IspMotivoOggettoSelezionatiModel lModel = new IspMotivoOggettoSelezionatiModel();
		lModel.setCodOggetto(getCodOggetto());
		lModel.setDescOggetto(getDescOggetto());
		lModel.setCodMotivo(getCodMotivo());
		lModel.setDescMotivo(getDescMotivo());
		lModel.setTipoUfficio(getTipoUfficio());
		lModel.setCodUfficio(getCodUfficio());
		return lModel;
	}

	public void setDAOFromModel(IspMotivoOggettoSelezionatiModel aModel) throws DAOException {
		setCodOggetto(aModel.getCodOggetto() );  
		setDescOggetto(aModel.getDescOggetto() );
		setCodMotivo(aModel.getCodMotivo() );  
		setDescMotivo(aModel.getDescMotivo() );  
		setTipoUfficio(aModel.getTipoUfficio());
		setCodUfficio(aModel.getCodUfficio() );
	}

	public void setCondizione(IspMotivoOggettoSelezionatiModel aModel) {
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
