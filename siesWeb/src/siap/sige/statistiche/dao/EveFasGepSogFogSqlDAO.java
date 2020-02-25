package siap.sige.statistiche.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.sige.statistiche.action.ICostantiStatistiche;
import siap.sige.statistiche.model.EveFasGepSogModel;
import siap.sige.statistiche.model.EveFasGepSogProvModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: EveFasGepSogFogSqlDAO</p>
* <p>Description: Classe SqlDAO estensione della EveFasGepSogSqlDAO 
*    che gestisce la ricerca per "Foglio Complementare", dato registrato nella tabella DOCUMENTO_ALLEGATO.</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class EveFasGepSogFogSqlDAO extends EveFasGepSogSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public EveFasGepSogFogSqlDAO (Connection con)
  {
	 super(con);
  }

  /**
   * Restituisce la parte dello  statement Sql "Select .... FROM .."
   */
  protected String getSqlQuery()
  {			 
	 String lStatement = getSqlQuerySelect() +  getSqlQueryFromDocJoinEve() + getSqlQueryJoinNoDocAll();
    
     return lStatement;
  }
   
  // STUB: forse non ce n'è bisogno !!
  public GenericModel getModel() throws DAOException
  {
	EveFasGepSogModel lAncestorModel = (EveFasGepSogModel) super.getModel();
	EveFasGepSogProvModel lModel = null;
	lModel = new EveFasGepSogProvModel(lAncestorModel);
	return lModel;
  }

  protected String getSqlQueryFromDocJoinEve()
  {			 
	  String lStatement = new String("");

	  lStatement += " FROM DOCUMENTO_ALLEGATO DA " ;
	  lStatement += " join EVENTO E on (DA.EVE_ID_EVENTO = E.ID_EVENTO)";
	 
	  return lStatement;
  }

  private String setOrderbyAnnoNum()
  {
	  String lStatement = " ORDER BY  ANNO_FOGLIO_COMPLEMENTARE ASC, PROGR_FOGLIO_COMPLEMENTARE ASC";
	  return lStatement;
  }
  
  // variabile flag utilizzata per comporre la condizione di filtro sulla select
  private boolean lInserito = false;
  
  protected String  setCondizioniIntervallo(RicercaFogliCompModel aModel)
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
    				lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoIniziale();
    				lCondizioni += setAND (lAppoggio);
    				lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE <= " + aModel.getNumFinale();
    				lCondizioni += setAND (lAppoggio);
    				lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale();
    				lCondizioni += setAND (lAppoggio);  				
    			}
    			else
    			{
    				lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE > " + aModel.getAnnoIniziale();
    				lAppoggio += " AND  DA.ANNO_FOGLIO_COMPLEMENTARE < " + aModel.getAnnoFinale();
    				lAppoggio = "(" + lAppoggio + ")";
    				lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoFinale() + " AND"+
    				" DA.PROGR_FOGLIO_COMPLEMENTARE <= " + aModel.getNumFinale() + ")";
    	 			lAppoggio += " OR (DA.ANNO_FOGLIO_COMPLEMENTARE = " + aModel.getAnnoIniziale() + " AND"+
    	 			" DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale() + ")";
    	 			lAppoggio = "(" + lAppoggio + ")";
    	 			lCondizioni += setAND (lAppoggio);
    			}
    		}
    		else
    		{
    			// Solo limite inferiore
    			lAppoggio = " DA.ANNO_FOGLIO_COMPLEMENTARE >= " + aModel.getAnnoIniziale();
    			lCondizioni += setAND (lAppoggio);
    			lAppoggio = " DA.PROGR_FOGLIO_COMPLEMENTARE >= " + aModel.getNumIniziale();
    		}
    	}
    }
    else if (aModel.isRicercaXDateEmissione())
    {
    	if (aModel.getDataEmissioneIniziale() != null)
    	{
    		lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') >='" + DateUtils.getDateToString(aModel.getDataEmissioneIniziale(), "yyyyMMdd") + "'";
    		lCondizioni += setAND (lAppoggio);
    	}
    	if (aModel.getDataEmissioneFinale() != null)
    	{
    		lAppoggio = " TO_CHAR(DA.DATA_EMISSIONE,'YYYYMMDD') <='" + DateUtils.getDateToString(aModel.getDataEmissioneFinale(), "yyyyMMdd") + "'";
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

  protected String  setCondizioni(RicercaFogliCompModel aModel)
  {
	lInserito = false;
    // Condizione di WHERE resituita 
    String lCondizioni = new String("");
    // Stringa di appoggio usata per la preparazione della singola condizione
    String lAppoggio;
    lInserito = false;
    
    // Condizione fissa per filtrare nella tabella DOCUMENTO_ALLEGATO 
    // solo i Fogli Complementari.
    lCondizioni += setAND (" DA.COD_TIPO_DOCUMENTO = '06'");
    
    // Condizione sull'ufficio
    if (aModel.getCodUfficioInserimento()!= null && aModel.getCodUfficioInserimento().trim().length()> 0 )
    {
     	lAppoggio = " DA.COD_UFFICIO_INSERIMENTO = '"  + aModel.getCodUfficioInserimento()  + "'";
     	lCondizioni += setAND (lAppoggio);
    }
    
    // Condizioni sullo Stato di Validazione
    if(aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI))
    {
   	 lAppoggio = " DA.FLAG_DOCUMENTO_REGISTRATO = 'A' ";
   	 lCondizioni += setAND (lAppoggio);
    }
    else if(aModel.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI))
    {
   	 lAppoggio = " (DA.FLAG_DOCUMENTO_REGISTRATO IS NULL OR DA.FLAG_DOCUMENTO_REGISTRATO <> 'A' )";
   	 lCondizioni += setAND (lAppoggio);
    }
 
    lCondizioni += setCondizioniIntervallo(aModel);

    // Infine la WHERE  
	if (lInserito)
		lCondizioni = " WHERE " + lCondizioni;

    return lCondizioni;
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
  
    String lStatement = "";
    	
   	lStatement = getSqlQuery();
    
    lStatement +=  setCondizioni(aModel);
    lStatement += setOrderbyAnnoNum();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .ricercaProcedimentiSige(): fine " );

    setStatement(lStatement);
  }
}