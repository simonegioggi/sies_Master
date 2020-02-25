package siap.siep.statis.dao;

/**
* <p>Title: IspTempiRicezioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IspTempiRicezione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.statis.model.IspTempiRicezioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class IspTempiRicezioneDAO extends TableDAO 
{
	public IspTempiRicezioneDAO (Connection con) 
	{
			 super(con);
			 setTable("ISP_TEMPI_RICEZIONE");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("CHIAVE_ANNO", INTEGER);
			 setField("CHIAVE_PROGR", BIG_DECIMAL);
			 setField("COD_UFFICIO", STRING);
			 setField("DATA_ARRIVO_ATTO", DATE);
			 setField("DATA_IRREVOCABILITA", DATE);
			 setField("TEMPO_GIUDICATO_RICEZIONE", INTEGER);
			 setField("DESC_TIPO_AUTORITA_EMITTENTE", STRING);
			 setField("DESC_LUOGO_EMITTENTE", STRING);
			 setField("DESC_SEZIONE_AUTORITA", STRING);
		// NGG Statistiche SIEP
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("CHIAVE_PROGR_ORIG", BIG_DECIMAL);
			 setField("DESC_UFFICIO_INSERIMENTO", STRING);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("ID_FASCICOLO_SIEP"); } 
			public Integer 		 getChiaveAnno() 		throws DAOException	 { return getInteger("CHIAVE_ANNO"); } 
			public BigDecimal 		 getChiaveProgr() 		throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); } 
			public String 				 getCodUfficio() 		throws DAOException	 { return getString("COD_UFFICIO"); } 
			public Date 					 getDataArrivoAtto() 		throws DAOException	 { return getDate("DATA_ARRIVO_ATTO"); } 
			public Date 					 getDataIrrevocabilita() 		throws DAOException	 { return getDate("DATA_IRREVOCABILITA"); } 
			public Integer 		 getTempoGiudicatoRicezione() 		throws DAOException	 { return getInteger("TEMPO_GIUDICATO_RICEZIONE"); }
			public String 		getDescTipoAutoritaEmittente() 		throws DAOException	 { return getString("DESC_TIPO_AUTORITA_EMITTENTE"); }
			public String 		getDescLuogoEmittente() 		throws DAOException	 { return getString("DESC_LUOGO_EMITTENTE"); }
			public String 		getDescSezioneAutorita() 		throws DAOException	 { return getString("DESC_SEZIONE_AUTORITA"); }	
			// NGG Statistiche SIEP	
			public String 		getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }	
			public BigDecimal 	 getChiaveProgrOrig() 		throws DAOException	 { return getBigDecimal("CHIAVE_PROGR_ORIG"); }
			public String 		getDescUfficioInserimento() 		throws DAOException	 { return getString("DESC_UFFICIO_INSERIMENTO"); }	


  //
  // METODI SET()
  //

			public void  	 setIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("ID_FASCICOLO_SIEP", aValore); } 
			public void  	 setChiaveAnno(Integer aValore ) 			 { setInteger("CHIAVE_ANNO", aValore); } 
			public void  	 setChiaveProgr(BigDecimal aValore ) 			 { setBigDecimal("CHIAVE_PROGR", aValore); } 
			public void  	 setCodUfficio(String aValore ) 			 { setString("COD_UFFICIO", aValore); } 
			public void  	 setDataArrivoAtto(Date aValore ) 			 { setDate("DATA_ARRIVO_ATTO", aValore); } 
			public void  	 setDataIrrevocabilita(Date aValore ) 			 { setDate("DATA_IRREVOCABILITA", aValore); } 
			public void  	 setTempoGiudicatoRicezione(Integer aValore ) 			 { setInteger("TEMPO_GIUDICATO_RICEZIONE", aValore); }
			public void 	setDescTipoAutoritaEmittente(String aValore) 		{  setString("DESC_TIPO_AUTORITA_EMITTENTE", aValore); }
			public void 	setDescLuogoEmittente(String aValore) 		{  setString("DESC_LUOGO_EMITTENTE", aValore); }
			public void 	setDescSezioneAutorita(String aValore) 		{  setString("DESC_SEZIONE_AUTORITA", aValore); }
			// NGG Statistiche SIEP
			public void 	setCodUfficioInserimento(String aValore) 	{ setString("COD_UFFICIO_INSERIMENTO", aValore); } 	
			public void 	setChiaveProgrOrig(BigDecimal aValore) 			{ setBigDecimal("CHIAVE_PROGR_ORIG", aValore); }
			public void 	setDescUfficioInserimento(String aValore) 		{ setString("DESC_UFFICIO_INSERIMENTO", aValore); } 



	public GenericModel getModel() throws DAOException
  			 { 
 				 return new IspTempiRicezioneModel(  
								 getIdFascicoloSiep() , 
								 getChiaveAnno() , 
								 getChiaveProgr() , 
								 getCodUfficio() , 
								 "",
								 getDataArrivoAtto() , 
								 getDataIrrevocabilita() , 
								 getTempoGiudicatoRicezione(),
								 getDescTipoAutoritaEmittente(),
								 getDescLuogoEmittente(),
								 getDescSezioneAutorita(),
					// NGG Statistiche SIEP			 
								 getCodUfficioInserimento(),
								 getChiaveProgrOrig(),
								 getDescUfficioInserimento()
								 );  
								
		}

	public void selCondizione(int intervallo)
	{
		 String lCondizioni = new String();

		 if (intervallo == 5)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE < 6";
		 else if (intervallo == 20)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE between 6 and 20 ";
		 else if (intervallo == 30)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE between 21 and 30 ";
		 else if (intervallo == 60)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE between 31 and 60 ";
		 else if (intervallo == 90)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE between 61 and 90 ";		 
		 else if (intervallo == 0)
			 lCondizioni = " TEMPO_GIUDICATO_RICEZIONE > 90 ";		 
 		 setCondition(lCondizioni); 
	}


	public void setOrdinamento()
	{
		setOrder("DATA_ARRIVO_ATTO, DATA_IRREVOCABILITA");
 	}
}
