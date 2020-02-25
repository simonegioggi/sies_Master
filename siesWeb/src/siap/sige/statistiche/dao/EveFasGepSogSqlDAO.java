package siap.sige.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.statistiche.model.EveFasGepSogModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: EveFasGepSogSqlDAO</p>
* <p>Description: Classe SqlDAO che gestisce l'accesso per ricerca 
* alla JOIN delle seguenti tabelle: Evento, Fascicolo_Sige, 
* ProvvedimentoSige, Soggetto. </p>
* La classe definisce un metodo di ricerca abstract 
* che viene definito nelle classi figlie, per questo motivo 
* la classe è abstract e quindi non istanziabile.
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public abstract class EveFasGepSogSqlDAO extends SIAPSqlDAO
{
	 public EveFasGepSogSqlDAO (Connection con)
	 {
		super(con);
	 }

    protected String getSqlQuerySelect()
	{			 
	  String lStatement = getSqlQuerySelectNoDocAll();
		  
	  lStatement += ", " + 
	  getSqlQueryDocumentoAllegato(); 
	 
	  return lStatement;
	}

    protected String getSqlQueryJoinNoDocAll()
  	{			 
  	  String lStatement = new String("");

  	  lStatement += "  join PROVVEDIMENTO_SIGE P" +
  	  " on P.ID_EVENTO_GENERATO = E.ID_EVENTO " +
  	  " join FASCICOLO_SIGE F on F.ID_FASCICOLO_SIGE = P.FAS_ID_FASCICOLO_SIGE " +
  	  " join SOGGETTO S on (S.ID_SOGGETTO = F.SOG_ID_SOGGETTO ) " +
  	  " left join COMUNE SCN ON (S.COD_COMUNE_NASCITA = SCN.COD_COMUNE) "; 
  	  
  	  return lStatement;
  	}

    protected String getSqlQueryJoinOrdNoDocAll()
  	{			 
  	  String lStatement = new String("");

  	  lStatement += "  join FASCICOLO_SIGE F on F.ID_FASCICOLO_SIGE = P.FAS_ID_FASCICOLO_SIGE " +
  	  " join SOGGETTO S on (S.ID_SOGGETTO = F.SOG_ID_SOGGETTO ) " +
  	  " left join COMUNE SCN ON (S.COD_COMUNE_NASCITA = SCN.COD_COMUNE) "; 
  	  
  	  return lStatement;
  	}
    
    protected String getSqlQuerySelectNoDocAll()
  	{			 
  	  String lStatement = new String("");

  	  lStatement += " SELECT " + 
  	  getSqlQueryEvento() + ", " + 
  	  getSqlQueryFascicoloSige() + ", " + 
  	  getSqlQueryProvvedimentoSige() + ", " + 
  	  getSqlQuerySoggetto();
  	  return lStatement;
  	}

    protected String getSqlQueryEvento()
    {
  	  String lStatement = new String("");
    
  	  lStatement += " E.ID_EVENTO, "+
  	  " E.COD_TIPO_EVENTO," +
  	  " E.COD_TIPO_PROVVEDIMENTO,"+
  	  " E.COD_MOTIVO,"+
  	  " E.DATA_EMISSIONE,"+
  	  " E.COD_ESITO, " +
  	  " E.FLAG_DOCUMENTO_REGISTRATO," +
  	  " E.EVE_ID_EVENTO";
  	  return lStatement;
    }

    protected String getSqlQueryFascicoloSige()
    {
  	  String lStatement = new String("");
    
  	  lStatement += " F.ID_FASCICOLO_SIGE, "+
  	  " F.CHIAVE_ANNO," +
  	  " F.CHIAVE_UFFICIO,"+
  	  " F.CHIAVE_PROGR,"+
  	  " F.COD_STATO_FASCICOLO";
  	  return lStatement;
    }

    protected String getSqlQueryProvvedimentoSige()
    {
  	  String lStatement = new String("");
    
  	  lStatement += " P.ID_PROVVEDIMENTO_SIGE, "+
  	  " P.CHIAVE_ANNO," +
  	  " P.CHIAVE_PROGR,"+
  	  " P.DATA_EMISSIONE";
  	  return lStatement;
    }

    protected String getSqlQuerySoggetto()
    {
  	  String lStatement = new String("");
    
  	  lStatement += 
  	          " S.ID_SOGGETTO, "+
          	  " S.COGNOME, " +
          	  " S.NOME, "+
          	  " S.DATA_NASCITA, " +
          	  " S.COD_COMUNE_NASCITA, " +
          	  " SCN.DESCRIZIONE LUOGO_NASCITA, " +
          	  " S.COD_PROVINCIA_NASCITA, " +
          	  " S.DESC_COMUNE_NASCITA_ESTERO, " +
          	  " S.COD_STATO_NASCITA, " + 
          	  " S.NAZIONALITA ";
  	  return lStatement;
    }

    /**
     * Il metodo restituisce la parte dello statement 
     * di select che riporta i campi da leggere 
     * dalla tabella DOCUMENTO_ALLEGATO.
     */
    protected String getSqlQueryDocumentoAllegato()
    {
  	  String lStatement = new String("");
    
  	  lStatement += " DA.ID_DOCUMENTO_ALLEGATO ID_ALLEGATO, DA.FLAG_DOCUMENTO_REGISTRATO FLAG_ALLEGATO_REGISTRATO, DA.ANNO_FOGLIO_COMPLEMENTARE, DA.PROGR_FOGLIO_COMPLEMENTARE, DA.DATA_EMISSIONE DATA_EMISSIONE_ALLEGATO ";

  	  return lStatement;
    }

    /**
     * Attraverso la lettura della fetch valorizza il model strutturato EveFasGepSogModel.
     * A tal fine utilizza 5 funzioni di tipo getModel che restituiscono i 5 model semplici 
     * di cui si compone EveFasGepSogModel e che sono:
     * EventoModel, FascicoloSigeModel, ProvvedimentoSigeModel, SoggettoModel, DocumentoAllegatoModel.
     * @return GenericModel.
     */
    public GenericModel getModel() throws DAOException
    {
	  	EventoModel lEvento = getEventoModel();
	  	FascicoloSigeModel lFascicoloSige = getFascicoloSigeModel();
	  	ProvvedimentoSigeModel lProvvedimentoSige = getProvvedimentoSigeModel();
	  	ProvvedimentoSigeEventoModel lProvvEventoSigeModel = new ProvvedimentoSigeEventoModel();
	  	EventoNotificaModel lEventoNotificaModel = new EventoNotificaModel();
	  	lProvvEventoSigeModel.setProvvedimento(lProvvedimentoSige);
	  	lProvvEventoSigeModel.setEventoNotifica(lEventoNotificaModel);
	  	SoggettoModel lSoggetto = getSoggettoModel(); 
	  	DocumentoAllegatoModel lDocAllegato = getDocumentoAllegatoModel();
	  	
	  	EveFasGepSogModel lModel = new EveFasGepSogModel(lEvento, lFascicoloSige, lProvvEventoSigeModel, lSoggetto, lDocAllegato );
	  	return lModel;
    }

    protected ProvvedimentoSigeModel getProvvedimentoSigeModel() throws DAOException
    {
    	ProvvedimentoSigeModel lModel = new ProvvedimentoSigeModel();
  	  
        lModel.setIdProvvedimentoSige(getBigDecimal("ID_PROVVEDIMENTO_SIGE") );
        lModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
        lModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
        lModel.setDataEmissione(getDate("DATA_EMISSIONE") );
        return lModel;
   }

    protected FascicoloSigeModel getFascicoloSigeModel() throws DAOException
    {
    	FascicoloSigeModel lModel = new FascicoloSigeModel();
  	  // Lettura campi Tabella Fascicolo_sius
  	  lModel.setIdFascicoloSige(getBigDecimal("ID_FASCICOLO_SIGE") );
  	  lModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
  	  lModel.setChiaveUfficio(getString("CHIAVE_UFFICIO") );
  	  lModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
  	  lModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
  	  lModel.setDescrStatoFascicolo("");
  	  return lModel;
    }

    protected EventoModel getEventoModel() throws DAOException
    {
	  	// Lettura dei campi dalla tabella EVNTO
	  	EventoModel lEvento = new EventoModel();
	  	
	  	lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));
	  	lEvento.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
	  	lEvento.setDescrTipoEvento("");
	  	lEvento.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
	  	lEvento.setDescrTipoProvvedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),lEvento.getCodTipoProvvedimento()));
	  	lEvento.setCodMotivo(getString("COD_MOTIVO"));
	  	lEvento.setDescrMotivo(decodificaCodMotivo(lEvento.getCodMotivo()));
	   	lEvento.setDataEmissione(getDate("DATA_EMISSIONE"));
	  	lEvento.setCodEsito(getString("COD_ESITO"));
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
	
	  	lEvento.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
	  	lEvento.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
	  	
	  	return lEvento;
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

    protected SoggettoModel getSoggettoModel() throws DAOException
    {
  	  SoggettoModel lModel = new SoggettoModel();

  	    lModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO") );
  	    lModel.setCognome(getString("COGNOME") );
  	    lModel.setNome(getString("NOME") );
  	    lModel.setDataNascita(getDate("DATA_NASCITA") );
  	    
//  	    lModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
  	    lModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
  	    lModel.setDescrComuneNascita(getString("LUOGO_NASCITA"));
  	    
  	    lModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
  	    // La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
  	    lModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getProvincie(),lModel.getCodProvinciaNascita()));
  	     
  	    lModel.setCodStatoNascita(getString("COD_STATO_NASCITA") );
  	    // La descrizione dello stato di nascita la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
  	    lModel.setDescrStatoNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getNazioni(),lModel.getCodStatoNascita()));
  	    lModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );

  	    lModel.setNazionalita(getString("NAZIONALITA") );
  	    // La descrizione della nazionalità la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
  	    lModel.setDescrNazionalita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getNazionalita(),lModel.getNazionalita()));

  	    return lModel;
   }

    protected DocumentoAllegatoModel getDocumentoAllegatoModel() throws DAOException
    {
  	  DocumentoAllegatoModel lModel = new DocumentoAllegatoModel();
  	  lModel.setIdDocumentoAllegato(getBigDecimal("ID_ALLEGATO"));
  	  lModel.setFlagDocumentoRegistrato(getString("FLAG_ALLEGATO_REGISTRATO") );
  	  lModel.setAnnoFoglioComplementare(getBigDecimal("ANNO_FOGLIO_COMPLEMENTARE"));
  	  lModel.setProgrFoglioComplementare(getBigDecimal("PROGR_FOGLIO_COMPLEMENTARE"));
  	  lModel.setDataEmissione(getDate("DATA_EMISSIONE_ALLEGATO"));
  	  
  	  return lModel;
    }
    
    /**
      * Metodo abstract che verrà implementato in maniera specifica 
      * nelle classi specializzate.
      * Questo è l'unico metodo abstract della classe.
      * Se fosse necessario rendere la classe istanziabile 
      * si deve creare una definizione per questo metodo.
      * @param aModel
      * @throws DAOException
     */
     abstract public void ricercaProcedimentiSige( RicercaFogliCompModel aModel  )
		  throws DAOException;

}