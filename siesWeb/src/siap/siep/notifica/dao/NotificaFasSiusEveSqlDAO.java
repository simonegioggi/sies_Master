package siap.siep.notifica.dao;


import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.model.NotificaFasSiusEveModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: NotificaSqlDAO</p>
* <p>Description: Classe SqlDAO estensione 
* della NotificaSqlDAO utilizzata per ricerche 
* sulla tabella Notifica in join con EVENTO; 
* FASCICOLO_SIUS e SOGGETTO.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class NotificaFasSiusEveSqlDAO extends NotificaSqlDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	 public NotificaFasSiusEveSqlDAO (Connection con)
	{
		 super(con);
	}


 //
  // METODO RICERCA()
  //

  /**
   * La funzione restituisce la select necessaria alla ricerca 
   * di notifiche collegate a Fascicoli SIUS.
  */
  protected String getSqlQuery()
  {			 
	 String lStatement = getSqlQuerySelect() + getSqlQuerySelectEstesa() + getSqlQueryFrom() + getSqlQueryJoinEventoFascicoloSoggetto();
    
     return lStatement;
  }
  
/*
 * La funzione aggiunge alla select base le join necessarie 
 * ad includere le tabelle: FASCICOLO_SIUS, EVENTO, SOGGETTO .
 */
  protected String getSqlQueryJoinEventoFascicoloSoggetto()
	{			 
	  String lStatement = new String("");

	  lStatement += " join EVENTO EVE" +
	  " on (EVE.ID_EVENTO = N.EVE_ID_EVENTO )"+
	  " join FASCICOLO_SIUS FASC" +
	  " on (FASC.ID_FASCICOLO_SIUS = EVE.FAS_SIU_ID_FASCICOLO_SIUS)"+
	  " join SOGGETTO SOGG" +
	  " on (SOGG.ID_SOGGETTO = FASC.SOG_ID_SOGGETTO)";
	  
	  return lStatement;
	}
  
  
  /*
   * La funzione aggiunge alla select la join necessaria 
   * ad includere la tabella: AUTORITA_ESTERNA e le condizioni di filtro su questa.
   */
    protected String getSqlQueryJoinAutoritaEsterna(AutoritaEsternaModel aAutorita, boolean aEsclusoAutorita, boolean aEsclusoSedeAutorita)
  	{			 
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .getSqlQueryJoinAutoritaEsterna(): inizio " );

  	  String lStatement = new String("");

  	  lStatement += " join AUTORITA_ESTERNA AUT" +
  	  " on (AUT.ID_AUTORITA_ESTERNA = N.AUT_EST_ID_AUTORITA_ESTERNA ";
  	  if (aAutorita != null)
  	  {
      	if (aAutorita.getCodTipoAutorita() != null && aAutorita.getCodTipoAutorita().trim().length() > 0)
    	{
      		if (aEsclusoAutorita)
      		{
      			// Se si esclude dai destinatari una autorità specifica saranno esclusi anche gli Istituti di detenzione
      			lStatement += " AND AUT.COD_TIPO_AUTORITA <> '"  + aAutorita.getCodTipoAutorita()  + "'";
      			lStatement += " AND N.IST_DET_ID_ISTITUTO_DETENZIONE IS NULL ";
      		}
      		else
      		{
      			lStatement += " AND AUT.COD_TIPO_AUTORITA = '"  + aAutorita.getCodTipoAutorita()  + "'";
      	      	if (aAutorita.getCodSede() != null && aAutorita.getCodSede().trim().length() > 0)
      	    	{
      	      		if (aEsclusoSedeAutorita)
      	      			lStatement += " AND AUT.COD_SEDE <> '"  + aAutorita.getCodSede()  + "'";
      	      		else
      	         	    lStatement += " AND AUT.COD_SEDE = '"  + aAutorita.getCodSede()  + "'";
      	      	}
      		}
    	}
      	lStatement += ")";
  	  }
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .getSqlQueryJoinAutoritaEsterna(): fine " );

  	  return lStatement;
  	}
  
    /*
     * La funzione aggiunge alla select la join necessaria 
     * ad includere la tabella: UFFICIO e le condizioni di filtro su questa.
     */
    protected String getSqlQueryJoinUfficio(UfficioModel aUfficio, boolean aEsclusoUfficio, boolean aEsclusoSedeUfficio)
  	{			 
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .getSqlQueryJoinUfficio(): inizio " );

  	  String lStatement = new String("");

  	  lStatement += " join UFFICIO UFF" +
  	  " on (UFF.COD_UFFICIO = N.UFF_COD_UFFICIO ";
  	  if (aUfficio != null)
  	  {
      	if (aUfficio.getCodTipoUfficio() != null && aUfficio.getCodTipoUfficio().trim().length() > 0)
    	{
      		if (aEsclusoUfficio)
      			lStatement += " AND UFF.COD_TIPO_UFFICIO <> '"  + aUfficio.getCodTipoUfficio()  + "'";
      		else
      		{
      			lStatement += " AND UFF.COD_TIPO_UFFICIO = '"  + aUfficio.getCodTipoUfficio()  + "'";
      	      	if (aUfficio.getCodComune() != null && aUfficio.getCodComune().trim().length() > 0)
      	    	{
      	      		if (aEsclusoSedeUfficio)
      	      			lStatement += " AND UFF.COD_COMUNE <> '"  + aUfficio.getCodComune()  + "'";
      	      		else
      	         	    lStatement += " AND UFF.COD_COMUNE = '"  + aUfficio.getCodComune()  + "'";
      	      	}
      		}
    	}
      	lStatement += ")";
  	  }
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "" + getClass().getName() + " .getSqlQueryJoinUfficio(): fine " );

  	  return lStatement;
  	}
     
    
    
    
/*
 * La funzione estende la select base aggiungendo ai campi base 
 * già previsti quelli relativi alle tabelle:  
 * FASCICOLO_SIUS, EVENTO, SOGGETTO .
 */
  protected String getSqlQuerySelectEstesa()
	{			 
	  String lStatement = new String("");

	  lStatement += ", FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO," +
	  " SOGG.ID_SOGGETTO, SOGG.COGNOME, SOGG.NOME," +
	  " EVE.ID_EVENTO, EVE.COD_TIPO_PROVVEDIMENTO, EVE.COD_ESITO COD_ESITO_EVENTO, EVE.DATA_EMISSIONE, EVE.DATA_TRASMISSIONE_ATTI, EVE.COD_MOTIVO" ;
	  return lStatement;
	}


  //
  // METODO GETMODEL()
  //

  public GenericModel  	 getModel() throws DAOException
  {
	// Viene istanziato e valorizzato il model relativo al record Notifica
    NotificaModel lNotificaModel = (NotificaModel) super.getModel();
    
    // Viene istanziato il model del Fascicolo SIUS e vengono letti i campi ad esso relativi
    FascicoloSiusModel lFascicolo = new  FascicoloSiusModel();
    lFascicolo.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS") );
    lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
    lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO") ); 
    lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
    
    // Viene istanziato il Soggetto model e valorizzati i campi ad esso relativi
    SoggettoModel lSoggetto = new SoggettoModel();
    lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
    lSoggetto.setCognome(getString("COGNOME") );
    lSoggetto.setNome(getString("NOME") );
    
    // Il soggetto model è contenuto nel Fascicolo Model
    lFascicolo.setSoggetto(lSoggetto);
    
    //  Viene istanziato l'Evento model e valorizzati i campi ad esso relativi
    EventoModel lEvento = new EventoModel();
    lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));
    
    lEvento.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
    // si ricava la descrizione del TipoProvvedimento  dalle Decodifiche in memoria per risparmiare una JOIN
    lEvento.setDescrTipoProvvedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),lEvento.getCodTipoProvvedimento()));
 
    lEvento.setCodEsito(getString("COD_ESITO_EVENTO"));
    try
    {
    // si ricava la descrizione dell'Esito Provvedimento dalle Decodifiche in memoria per risparmiare una JOIN
    lEvento.setDescrEsito(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoProvvedimento(),lEvento.getCodEsito()));
    }
    catch(DAOException de)
    {
    	throw de;
    }    
    catch(Exception e)
    {
    	throw new DAOException(e.getMessage());
    }
    
    lEvento.setCodMotivo(getString("COD_MOTIVO"));
    lEvento.setDescrMotivo(decodificaCodMotivo(lEvento.getCodMotivo()));
    
    lEvento.setDataEmissione(getDate("DATA_EMISSIONE"));
    lEvento.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
    
    // Viene istanziato ed assemblato il Model Esteso
    NotificaFasSiusEveModel lModel = new NotificaFasSiusEveModel(lNotificaModel, lFascicolo, lEvento);
    return lModel;
  }
  
  /* 
   * COD_MOTIVO nell'Evento può essere tradotto come MOTIVO_PROVVEDIMENTO 
   oppure come OGGETTO_PROCEDIMENTO (!!)
   La funzione tenta i due dominii che sono alternativi.
   */

  private String decodificaCodMotivo (String aCodMotivo)throws DAOException
  {
	  String lDecodifica = new String ("");
	  if (aCodMotivo != null)
	  {
	    // Si prova prima come MOTIVO_PROVVEDIMENTO
	  lDecodifica = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),aCodMotivo);
	  // Poi come OGGETTO_PROCEDIMENTO
	  if (!(lDecodifica.trim().length() > 0))
		  lDecodifica = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimento(),aCodMotivo);  
	  }
	  return lDecodifica;
  }

  
  // variabile flag utilizzata per comporre la condizione di filtro sulla select
  private boolean lInserito = false;
  
  /**
   * Prepara le condizioni di filtro sull'Evento e la Notifica.
   * @param aModel : RicercaNotificheSiusModel;
   * @param aDataInizio;
   * @param aDataFine;
   * @return
   */
  public String  setCondizione(RicercaNotificheSiusModel aModel,  Date aDataInizio, Date aDataFine)
  {
		
    String lCondizioni = new String("");
    String lAppoggio = new String("");
    lInserito = false;
    
    // Condizioni sull'EVENTO
    if (aModel.getEvento() != null)
    {
    	if (aModel.getEvento().getCodTipoEvento() != null && aModel.getEvento().getCodTipoEvento().trim().length() > 0 )
    	{
    		lAppoggio = " EVE.COD_TIPO_EVENTO = '"  + aModel.getEvento().getCodTipoEvento()  + "'";
    		lCondizioni += setAND (lAppoggio);
    	}
    	if (aModel.getEvento().getCodEsito() != null && aModel.getEvento().getCodEsito().trim().length() > 0 )
    	{
    		// Nell'Evento sono passati codice esito da includere o escludere dalla ricerca
    		if (aModel.getNoDecretoCitazione())
    			lAppoggio = " EVE.COD_ESITO <> '"  + aModel.getEvento().getCodEsito()  + "'";
    		else
    			lAppoggio = " EVE.COD_ESITO = '"  + aModel.getEvento().getCodEsito()  + "'";
    		lCondizioni += setAND (lAppoggio);
    	}  	
    }
    
    // Condizioni sulla NOTIFICA
    
    if(aModel.getCodTipoNotifica().trim().length() > 0)
    {
    	lAppoggio = " N.COD_TIPO_NOTIFICA = '"  + aModel.getCodTipoNotifica()  + "'";
    	lCondizioni += setAND (lAppoggio);
    }
    
    if(aModel.getCodOperatoreInserimento().trim().length() > 0)
    {
    	lAppoggio = " N.COD_OPERATORE_INSERIMENTO = '"  + aModel.getCodOperatoreInserimento()  + "'";
    	lCondizioni += setAND (lAppoggio);
    }
    
    if(aModel.getCodUfficioInserimento().trim().length() > 0)
    {
    	lAppoggio = " N.COD_UFFICIO_INSERIMENTO = '"  + aModel.getCodUfficioInserimento()  + "'";
    	lCondizioni += setAND (lAppoggio);
    }
    
    if (aDataInizio != null)
    {
    	lAppoggio = " TO_CHAR(N.DATA_INSERIMENTO,'YYYYMMDD') >='" + DateUtils.getDateToString(aDataInizio, "yyyyMMdd") + "'";
    	lCondizioni += setAND (lAppoggio);
    }
    if (aDataFine != null)
    {
    	lAppoggio = " TO_CHAR(N.DATA_INSERIMENTO,'YYYYMMDD') <='" + DateUtils.getDateToString(aDataFine, "yyyyMMdd") + "'";
    	lCondizioni += setAND (lAppoggio);
    }
    
    if (aModel.isIstitutoDiDetenzione())
    {
    	lAppoggio = " N.IST_DET_ID_ISTITUTO_DETENZIONE IS NOT NULL ";
    	lCondizioni += setAND (lAppoggio);
    }
	
    if (aModel.getFiltroNote() != null && aModel.getFiltroNote().trim().length() > 0)
    {
    	lAppoggio = " N.NOTE LIKE '%" +  aModel.getFiltroNote() + "%' ";
    	lCondizioni += setAND (lAppoggio);
    }

    
    // Infine la WHERE  
	if (lInserito)
		lCondizioni = " WHERE " + lCondizioni;

    return lCondizioni;
  }

  private String setAND(String aCondizioni)
  {
	  if (lInserito)
		  aCondizioni = " AND " + aCondizioni;
	  
	  lInserito = true;
	  
	  return aCondizioni;
  }
  
  /**
   * La funzione predispone la ricerca di Notifiche-FascicoloSius-Provvedimento-Soggetto.
   * Il Model parametro di ingresso contiene il filtro di ricerca da realizzare.
   * Viene richiamata la f.ne getSqlQuery() per preparare la select, 
   * se esistono delle condizioni sulle Autorità Esterne destinatarie
   * delle notifiche queste vengono preparate tramite la f.ne getSqlQueryJoinAutoritaEsterna,
   * infine tramite la f.ne setCondizione() vengono preparate le condizioni di filtro 
   * su tipo provvedimenti e notifiche.
   * @param aModel : RicercaNotificheSiusModel
   * @throws DAOException
   */
  
  public void ricercaNotificheFasSiusProvSog( RicercaNotificheSiusModel aModel  )
  throws DAOException
{
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .ricercaNotificheFasSiusProvSog(): inizio " );
  
    String lStatement = getSqlQuery();
    
    // Se è stato istanziato AutoritaEsternaModel si effettua la join con la
    // tabella AUTORITA_ESTERNA con le condizioni di filtro selezionate.
    if(aModel.getAutoritaEsterna() != null)
    	lStatement += getSqlQueryJoinAutoritaEsterna(aModel.getAutoritaEsterna(), aModel.getNoUNEP(), aModel.getNoSedeUNEP());
    
    // Se è stato istanziato UfficioModel si effettua la join con la
    // tabella UFFICIO con le condizioni di filtro selezionate.
    if (aModel.getUfficio() != null)
    	lStatement += getSqlQueryJoinUfficio(aModel.getUfficio(),aModel.getNoUff(), aModel.getNoSedeUff() );
    
    lStatement +=  setCondizione(aModel, aModel.getDataIniziale(), aModel.getDataFinale());
    lStatement += setOrdinamento(aModel.getOrdinamento());

    //lStatement += setOrder();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .ricercaNotificheFasSiusProvSog(): fine " );

    setStatement(lStatement);
}

  
  private String setOrdinamento (String aOrdinamento)
  {
	  String lOrdine = "";
	  
	  if (aOrdinamento != null)
	  {
		  if (aOrdinamento.equalsIgnoreCase("P"))
			  lOrdine = " ORDER BY FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR  ";
		  else if (aOrdinamento.equalsIgnoreCase("S"))
			  lOrdine = " ORDER BY SOGG.COGNOME, SOGG.NOME ";
		  else if (aOrdinamento.equalsIgnoreCase("D"))
			  lOrdine = " ORDER BY N.DATA_INSERIMENTO ";
	  }
	
	  return lOrdine;
	  
  }
  
  
  


}