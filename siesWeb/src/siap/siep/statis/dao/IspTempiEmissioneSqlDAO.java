package siap.siep.statis.dao;

/**
* <p>Title: IspTempiEmissioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella IspTempiEmissione</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import siap.siep.statis.model.IspTempiEmissioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

public class IspTempiEmissioneSqlDAO extends SqlDAO 
{
  public IspTempiEmissioneSqlDAO (Connection con) 
  {
      super(con);
  }

  /**
   * METODO RICERCA()
   */
  public void ricercaDettaglioTempiEmissione(int intervallo, String codMag)  throws DAOException
  {
    String lStatement = new String("");

    //==========================================================================
    // Nuova select
    //==========================================================================
    lStatement += " SELECT " +
                  " A.CHIAVE_ANNO, A.CHIAVE_PROGR,  "+
                  " A.COD_MAGISTRATO, decode (B.NOME,'-','',null,'',B.NOME)||' '||decode(B.COGNOME,'-','MAGISTRATO NON ASSEGNATO (-)',null,'MAGISTRATO NULLO',B.COGNOME) DESCR_MAGISTRATO, "+
                  " A.DATA_PASSATO_GIUDICATO, A.DATA_ARRIVO_ATTO, A.TEMPI_GIUDICATO_ARRIVO, " +
                  " A.DATA_ISCRIZIONE_FASCICOLO, A.TEMPI_ARRIVO_ISCRIZIONE, "+
                  " A.DATA_PRIMO_ATTO, A.DESCRIZIONE_MOTIVO_PRIMO_ATTO, " + 
                  " A.DATA_ORDINE_ESECUZIONE, A.DESCRIZIONE_MOTIVO_ESECUZIONE, "+
                  " A.TEMPI_ISCRIZIONE_EMISSIONE,  " +
                  " A.DATA_INIZIO_ISTRUTTORIA, A.DATA_FINE_ISTRUTTORIA , A.TEMPI_ISTRUTTORIA, A.DATA_INIZIO_INATTIVITA, "+
                  " A.DATA_FINE_INATTIVITA, A.TEMPI_INATTIVITA, A.GIORNI_DA_NEUTRALIZZARE, "+
               //NGG Statistiche SIEP
               " A.COD_UFFICIO_INSERIMENTO, A.CHIAVE_PROGR_ORIG, A.DESC_UFFICIO_INSERIMENTO ";
    			//
    lStatement += " FROM ISP_TEMPI_EMISSIONE A, W_MAGISTRATO B ";
    lStatement += " WHERE A.COD_MAGISTRATO = B.COD_MAGISTRATO(+) AND ";

    lStatement += " " + setCondizioni(intervallo, codMag);
    lStatement += " " + setOrder();
      
    setStatement(lStatement);
  }

  /**
   *  METODO GETMODEL()
   */
  public GenericModel getModel() throws DAOException
  { 
    IspTempiEmissioneModel aModel = new  IspTempiEmissioneModel(); 

    aModel.setChiaveAnno                  (getInteger    ("CHIAVE_ANNO")                   ); 
    aModel.setChiaveProgr                 (getBigDecimal ("CHIAVE_PROGR")                  ); 
    aModel.setDescrMagistrato             (getString     ("DESCR_MAGISTRATO")              );
    aModel.setDataPassatoGiudicato        (getDate       ("DATA_PASSATO_GIUDICATO")        ); 
    aModel.setDataArrivoAtto              (getDate       ("DATA_ARRIVO_ATTO")              ); 
    aModel.setTempiGiudicatoArrivo        (getBigDecimal ("TEMPI_GIUDICATO_ARRIVO")        );
    aModel.setDataIscrizioneFascicolo     (getDate       ("DATA_ISCRIZIONE_FASCICOLO")     ); 
    aModel.setTempiArrivoIscrizione       (getBigDecimal ("TEMPI_ARRIVO_ISCRIZIONE")       ); 
    aModel.setDataPrimoAtto               (getDate       ("DATA_PRIMO_ATTO")               ); 
    aModel.setDescrizioneMotivoPrimoAtto  (getString     ("DESCRIZIONE_MOTIVO_PRIMO_ATTO") );
    aModel.setDataOrdineEsecuzione        (getDate       ("DATA_ORDINE_ESECUZIONE")        );
    aModel.setDescrizioneMotivoEsecuzione (getString     ("DESCRIZIONE_MOTIVO_ESECUZIONE") );
    aModel.setTempoIscrizioneEmissione    (getBigDecimal ("TEMPI_ISCRIZIONE_EMISSIONE")    );
    aModel.setDataInizioIstruttoria       (getDate       ("DATA_INIZIO_ISTRUTTORIA")       ); 
    aModel.setDataFineIstruttoria         (getDate       ("DATA_FINE_ISTRUTTORIA")         ); 
    aModel.setTempiIstruttoria            (getBigDecimal ("TEMPI_ISTRUTTORIA")             ); 
    aModel.setDataInizioInattivita        (getDate       ("DATA_INIZIO_INATTIVITA")        ); 
    aModel.setDataFineInattivita          (getDate       ("DATA_FINE_INATTIVITA")          ); 
    aModel.setTempiInattivita             (getBigDecimal ("TEMPI_INATTIVITA")              ); 
    aModel.setGiorniDaNeutralizzare       (getBigDecimal ("GIORNI_DA_NEUTRALIZZARE")       );
    aModel.setCodUfficioInserimento		  (getString	 ("COD_UFFICIO_INSERIMENTO")	   );
    aModel.setChiaveProgrOrig      		  (getBigDecimal ("CHIAVE_PROGR_ORIG")             );
    aModel.setDescUfficioInserimento	  (getString	 ("DESC_UFFICIO_INSERIMENTO")	   );

    return aModel;
  }


  public String setOrder()
  {
     String lOrder = new String(); 
     
//     lOrder = " order by DATA_ISCRIZIONE_FASCICOLO ";
     lOrder = " order by A.CHIAVE_ANNO, A.CHIAVE_PROGR ";
     
     return lOrder; 
  }


  public String setCondizioni(int intervallo, String codMag)
  {
     String lCondizioni = new String();
     
     if (intervallo == 5)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE < 6";
     else if (intervallo == 20)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE between 6 and 20 ";
     else if (intervallo == 30)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE between 21 and 30 ";
     else if (intervallo == 60)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE between 31 and 60 ";
     else if (intervallo == 90)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE between 61 and 90 ";     
     else if (intervallo == 0)
       lCondizioni = " A.TEMPI_ISCRIZIONE_EMISSIONE > 90 ";
     
     if (!codMag.equals("0")) {
       
       if (codMag.equals("null"))
         lCondizioni = lCondizioni + " AND A.COD_MAGISTRATO IS " + codMag + " ";
       else
         lCondizioni = lCondizioni + " AND A.COD_MAGISTRATO = '" + codMag + "' ";
     }
     
    return lCondizioni; 
  }
}
