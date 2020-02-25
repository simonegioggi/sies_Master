package siap.sige.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.statistiche.action.ICostantiStatistiche;
import siap.sige.statistiche.model.EveFasGepSogModel;
import siap.sige.statistiche.model.EveFasGepSogProvModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: EveFasGepSogSqlDAO</p>
* <p>Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO 
* che aggiunge alla JOIN di tabelle già rappresentate dall'Ancestor 
* anche la tabella DEPOSITO_ORDINANZA_PC.</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class EveFasGepSogOrdSqlDAO extends EveFasGepSogSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	 public EveFasGepSogOrdSqlDAO (Connection con)
	 {
		super(con);
	 }

/**
 * Restituisce la parte dello  statement Sql "Select .... FROM .."
 */
  protected String getSqlQuery()
  {			 
	 String lStatement = getSqlQuerySelect() + ", " + getSqlQueryProvvedimentoSige() + ", " + getSqlQueryUdienzaSige() + getSqlQueryFromJoinProvvSige("02") + getSqlQueryJoin("02") ;
    
     return lStatement;
  }
  
  protected String getSqlQueryFromJoinProvvSige(String aCodTipoDocumentoAllegato)
  {			 
	  String lStatement = new String("");

	  lStatement += " FROM PROVVEDIMENTO_SIGE P join EVENTO E" +
	  " on (P.ID_EVENTO_GENERATO = E.ID_EVENTO AND E.COD_TIPO_PROVVEDIMENTO = '" + aCodTipoDocumentoAllegato + "')";
	  return lStatement;
  }

  protected String getSqlQueryJoin(String aCodTipoDocumentoAllegato)
  {			 
	  String lStatement = getSqlQueryJoinOrdNoDocAll();
	 
	  lStatement += getSqlQueryJoinDocumentoAllegato(aCodTipoDocumentoAllegato);
	  
	  lStatement += getSqlQueryJoinUdienzaSige();
	 
	  return lStatement;
  }

  protected String getSqlQueryJoinDocumentoAllegato(String aCodTipoDocumentoAllegato)
  {			 
	  String lStatement = new String("");

	  lStatement += " left outer join DOCUMENTO_ALLEGATO DA" +
	  " on (DA.EVE_ID_EVENTO = E.ID_EVENTO";
	  if (aCodTipoDocumentoAllegato != null)
		  lStatement += " AND DA.COD_TIPO_DOCUMENTO = " + aCodTipoDocumentoAllegato + ""; 
	  
	  lStatement += ")";
	  return lStatement;
  }
  
  protected String getSqlQueryJoinUdienzaSige()
  {			 
	  String lStatement = new String("");

	  lStatement += " left outer join UDIENZA_PROCEDIMENTO_SIGE UP" +
	  " on (UP.FAS_ID_FASCICOLO_SIGE = F.ID_FASCICOLO_SIGE AND UP.EVE_ID_EVENTO = E.ID_EVENTO)";
	  
	  lStatement += " left outer join UDIENZA_SIGE US" +
	  " on US.ID_UDIENZA_SIGE = UP.UDI_ID_UDIENZA_SIGE";

	  return lStatement;
  }  
  
  public GenericModel getModel() throws DAOException
  {
	EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModel();
	EveFasGepSogProvModel lModel = new EveFasGepSogProvModel(lAncestorModel, getProvvedimentoSigeOrdModel(), getUdienzaSigeModel() );
	return lModel;
  }

  private ProvvedimentoSigeModel getProvvedimentoSigeOrdModel() throws DAOException
  {
	// Lettura dei campi dalla tabella PROVVEDIMENTO_SIGE
	
	ProvvedimentoSigeModel lProvvSige = new ProvvedimentoSigeModel();
	lProvvSige.setIdProvvedimentoSige(getBigDecimal("ID_PROVVEDIMENTO_SIGE"));
	lProvvSige.setChiaveAnno(getBigDecimal("CHIAVE_ANNO_PROV"));
	lProvvSige.setChiaveProgr(getBigDecimal("CHIAVE_PROGR_PROV"));
	lProvvSige.setDataDeposito(getDate("DATA_DEPOSITO_PROV"));

	return lProvvSige;
  }

  private UdienzaSigeModel getUdienzaSigeModel() throws DAOException
  {
	// Lettura dei campi dalla tabella UDIENZA_SIGE
	
	UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
	lUdienzaSige.setIdUdienzaSige(getBigDecimal("ID_UDIENZA_SIGE"));
	lUdienzaSige.setDataUdienza(getDate("DATA_UDIENZA"));

	return lUdienzaSige;
  }

  /**
   * Il metodo restituisce la parte dello statement 
   * di select che elenca i campi della tabella 
   * PROVVEDIMENTO_SIGE.
   * @return
   */
  protected String getSqlQueryProvvedimentoSige()
  {
	  String lStatement = new String("");
  
	  lStatement += " P.ID_PROVVEDIMENTO_SIGE, "+
	  "P.CHIAVE_ANNO CHIAVE_ANNO_PROV, " +
	  "P.CHIAVE_PROGR CHIAVE_PROGR_PROV, " +
	  "P.DATA_DEPOSITO DATA_DEPOSITO_PROV";
	  return lStatement;
  }

  /**
   * Il metodo restituisce la parte dello statement 
   * di select che elenca i campi della tabella 
   * UDIENZA_SIGE.
   * @return
   */
  protected String getSqlQueryUdienzaSige()
  {
	  String lStatement = new String("");
  
	  lStatement += " US.ID_UDIENZA_SIGE, "+
	  "US.DATA_UDIENZA ";
	  return lStatement;
  }

  private String setOrderbyAnnoNum()
  {
	  String lStatement = " ORDER BY P.CHIAVE_ANNO ASC, P.CHIAVE_PROGR ASC";
	  return lStatement;
  }

  
  // variabile flag utilizzata per comporre la condizione di filtro sulla select
  private boolean lInserito = false;
  
  /**
   * Il metodo prepara le condizioni di ricerca nello statement 
   * in preparazione in base al contenuto del model di ricerca 
   * passato come argomento.
   * La ricerca può essere di 2 tipi: 
   * per estremi dell'ordinanza espressi in intervallo di ANNOS3/NUMS3;
   * per intervallo di DATA_DEPOSITO dell'Ordinanza.
   * @param aModel
   * @return
   */
  protected String  setCondizioni(RicercaFogliCompModel aModel)
  {
	  lInserito = false;
	  
    // Condizione di WHERE resituita 
    String lCondizioni = new String("");
    // Stringa di appoggio usata per la preparazione della singola condizione
    String lAppoggio;

    // Condizione Ricerca Ordinanze Prive di Foglio Complementare
    if(aModel.getModalitaRicerca() != null){
    	if(aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_ORDINANZE_PRIVE_DI_FC)){
    		lAppoggio = " DESCR_ESITO_PROVVEDIMENTO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' ";
    		lAppoggio += " AND E.COD_ESITO = DESCR_ESITO_PROVVEDIMENTO.RV_LOW_VALUE ";
    		lAppoggio += " AND DESCR_MOTIVO_PROVVEDIMENTO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' ";
    		lAppoggio += " AND NVL(E.COD_MOTIVO, '-') = DESCR_MOTIVO_PROVVEDIMENTO.RV_LOW_VALUE ";
    		lAppoggio += " AND E.ID_EVENTO IN (SELECT DISTINCT DAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAA ";
    		lAppoggio += "                     MINUS "; 
    		lAppoggio += "                     SELECT DISTINCT DAAA.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO DAAA ";
    		lAppoggio += "                     WHERE DAAA.COD_TIPO_DOCUMENTO = '06')";
    		lCondizioni += setAND (lAppoggio);
    	}
    }
    
    // Condizione sull'ufficio
    if (aModel.getCodUfficioInserimento()!= null && aModel.getCodUfficioInserimento().trim().length()> 0 )
    {
     	lAppoggio = " P.COD_UFFICIO_INSERIMENTO = '"  + aModel.getCodUfficioInserimento()  + "'";
     	lCondizioni += setAND (lAppoggio);
    }
    
    // Condizioni sullo Stato di Validazione
    if(aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI))
    {
   	 lAppoggio = " E.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
   	 lCondizioni += setAND (lAppoggio);
    }
    else if(aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI))
    {
   	 lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND DA.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
   	 lCondizioni += setAND (lAppoggio);
    }
    else if(aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI))
    {
   	 lAppoggio = "E.FLAG_DOCUMENTO_REGISTRATO <> 'A' AND  DA.FLAG_DOCUMENTO_REGISTRATO <> 'S' ";
   	 lCondizioni += setAND (lAppoggio);
    }
 
    lCondizioni += setCondizioniOrdinanza(aModel);

    // Infine la WHERE  
	if (lInserito)
		lCondizioni = " WHERE " + lCondizioni;

    return lCondizioni;
  }

  
  protected String  setCondizioniOrdinanza(RicercaFogliCompModel aModel)
  {
    // Condizione resituita 
    String lCondizioni = new String("");
    // Stringa di appoggio usata per la preparazione della singola condizione
    String lAppoggio;
        
    
    if (aModel.isTipoIntervalloRicercaXEstremiProvvedimento())
    {
    	if (aModel.getAnnoIniziale() != null && aModel.getNumIniziale() != null)
    	{	 		
    		if (aModel.getAnnoFinale() != null && aModel.getNumFinale() != null)		
    		{
    			// AnnoIniziale = AnnoFinale
    			if ( aModel.getAnnoIniziale().compareTo(aModel.getAnnoFinale()) == 0)
    			{
    				lAppoggio = " P.CHIAVE_ANNO = " + aModel.getAnnoIniziale();
    				lCondizioni += setAND (lAppoggio);
    				lAppoggio = " P.CHIAVE_PROGR <= " + aModel.getNumFinale();
    				lCondizioni += setAND (lAppoggio);
    				lAppoggio = " P.CHIAVE_PROGR >= " + aModel.getNumIniziale();
    				lCondizioni += setAND (lAppoggio);  				
    			}
    			else
    			{
    				lAppoggio = " P.CHIAVE_ANNO > " + aModel.getAnnoIniziale();
    				lAppoggio += " AND P.CHIAVE_ANNO < " + aModel.getAnnoFinale();
    				lAppoggio = "(" + lAppoggio + ")";
    				lAppoggio += " OR (P.CHIAVE_ANNO = " + aModel.getAnnoFinale() + " AND"+
    				" P.CHIAVE_PROGR <= " + aModel.getNumFinale() + ")";
    	 			lAppoggio += " OR (P.CHIAVE_ANNO = " + aModel.getAnnoIniziale() + " AND"+
    	 			" P.CHIAVE_PROGR >= " + aModel.getNumIniziale() + ")";
    	 			lAppoggio = "(" + lAppoggio + ")";
    	 			lCondizioni += setAND (lAppoggio);
    			}
    		}
    		else
    		{
    			// Solo limite inferiore
    			lAppoggio = " P.CHIAVE_ANNO >= " + aModel.getAnnoIniziale();
    			lCondizioni += setAND (lAppoggio);
    			lAppoggio = " P.CHIAVE_PROGR >= " + aModel.getNumIniziale();
    		}
    	}
    }
    else if (aModel.isRicercaXDateDeposito())
    {
    	if (aModel.getDataDepositoIniziale() != null)
    	{
    		lAppoggio = " TO_CHAR(P.DATA_DEPOSITO,'YYYYMMDD') >='" + DateUtils.getDateToString(aModel.getDataDepositoIniziale(), "yyyyMMdd") + "'";
    		lCondizioni += setAND (lAppoggio);
    	}
    	if (aModel.getDataDepositoFinale() != null)
    	{
    		lAppoggio = " TO_CHAR(P.DATA_DEPOSITO,'YYYYMMDD') <='" + DateUtils.getDateToString(aModel.getDataDepositoFinale(), "yyyyMMdd") + "'";
    		lCondizioni += setAND (lAppoggio);
    	}
    } 
   
    return lCondizioni;
  }
  
  private String setAND(String aCondizioni)
  {
	  if (lInserito)
		  aCondizioni = " AND " + aCondizioni;
	  
	  lInserito = true;
	  
	  return aCondizioni;
  }

  protected String getSqlQueryFromDocJoinCodUnivMap()
	{			 
	  String lStatement = new String("");

	  lStatement += " ,CG_REF_CODES DESCR_ESITO_PROVVEDIMENTO, CG_REF_CODES DESCR_MOTIVO_PROVVEDIMENTO ";
	  return lStatement;
	}

  /**
   * Il metodo prepara lo statment sql che effettua la ricerca 
   * in base alle condizioni espresse dal model di ricerca passato 
   * come argomento.
   * @param aModel
   * @throws DAOException
   */
  public void ricercaProcedimentiSige( RicercaFogliCompModel aModel  )
		  throws DAOException
  {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( "" + getClass().getName() + " .ricercaProcedimentiSige(): inizio " );
	  
	    String lStatement = getSqlQuery();
	
	    // Ricerca Ordinanze Prive di Foglio Complementare
	    if(aModel.getModalitaRicerca() != null && aModel.getModalitaRicerca().equals(ICostantiStatistiche.RICERCA_ORDINANZE_PRIVE_DI_FC)){
	    	lStatement += getSqlQueryFromDocJoinCodUnivMap();
	    }
	
	    lStatement +=  setCondizioni(aModel);
	    lStatement += setOrderbyAnnoNum();

	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( "" + getClass().getName() + " .ricercaProcedimentiSige(): fine " );
	
	    setStatement(lStatement);
  }

}