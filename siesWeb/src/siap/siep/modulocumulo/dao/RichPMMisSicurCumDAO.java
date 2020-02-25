package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMMisSicurCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichPM_MisSicur_Cum</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;


public class RichPMMisSicurCumDAO extends TableDAO { 

  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public RichPMMisSicurCumDAO (Connection con) {
    super(con);
    setTable("RICHPM_MISSICUR_CUM");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    		//setSequenceField("ID_TITOLO_CUMULATO","TITOLO_CUMULATO_SEQ");

    //setField("ID_TITOLO_CUMULATO", BIG_DECIMAL);
    setField("RIC_ID_RICHIESTE_PM_IN_CUMULO"   	, BIG_DECIMAL);
    setField("MIS_ID_MISSICUR_CUMULO" 			, BIG_DECIMAL);
    setField("FLAG_CONDONO"						, STRING	 );	

  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================
  public  BigDecimal  getRichIdRichiestePMinCumulo() throws DAOException  { return getBigDecimal ("RIC_ID_RICHIESTE_PM_IN_CUMULO" ); }
  public  BigDecimal  getMisIdMisSicurCumulo()  	 throws DAOException  { return getBigDecimal ("MIS_ID_MISSICUR_CUMULO"   ); } 
  public  String  	  getFlagCondono()		  		 throws DAOException  { return getString 	 ("FLAG_CONDONO"   ); } 

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setRichIdRichiestePMinCumulo (BigDecimal  aValore )   { setBigDecimal ("RIC_ID_RICHIESTE_PM_IN_CUMULO"   , aValore); }
  public void  setMisIdMisSicurCumulo     	(BigDecimal  aValore )   { setBigDecimal ("MIS_ID_MISSICUR_CUMULO"   , aValore); } 
  public void  setFlagCondono     			(String 	 aValore )   { setString 	 ("FLAG_CONDONO"   , aValore); } 
  
  
  /***************************************************************************** 
   * Imposta la condizione di where per l'operazione di Delete o update puntuale 
   * @param key_Richiesta_PM_in_Cumulo 
   ****************************************************************************/ 
  public void selCondizioneUpdate( BigDecimal aIdRichiestePmInCumulo) 
  {
    String lCondizioni = new String();

    lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateFlagCondono( BigDecimal aIdRichiestePmInCumulo, BigDecimal aIdMisSicCum) 
  {
    String lCondizioni = new String();

    lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;
    lCondizioni += " and MIS_ID_MISSICUR_CUMULO = " + aIdMisSicCum;
    
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    }
    setCondition(lCondizioni);
  }
  

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException 
  { 
	  return new RichPMMisSicCumModel (  
			  getRichIdRichiestePMinCumulo() , 
			  getMisIdMisSicurCumulo(),
			  getFlagCondono()
      );
  }
 

}
