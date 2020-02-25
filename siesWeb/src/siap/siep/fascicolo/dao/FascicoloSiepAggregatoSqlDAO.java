package siap.siep.fascicolo.dao;

/**
 * <p>Title: FascicoloSiepAggregatoSqlDAO</p>
 * <p>Description: Realizza Sql DAO del Fascicolo Siep Aggregato
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepAggregatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

public class FascicoloSiepAggregatoSqlDAO extends SIAPSqlDAO
{
  public FascicoloSiepAggregatoSqlDAO(Connection aCon)
  {
    super(aCon);
  }

   public void ricercaFascicoloSiepSospesiInterrotti(FascicoloSiepModel aModel,String[] aMotivo,int aPage,String aAggiuntoUnion )
   {
    String lFascicoli = "";
    String lPaginedStatement = "";
    
    lFascicoli += getFascicoloSqlAggregatoQuery(aModel,aMotivo);
    lFascicoli += setCondizione(aModel);
    if("S".equals(aAggiuntoUnion))
     lFascicoli += getUnion(aModel,aMotivo,"N","S");
    else
     lFascicoli += setOrder();   	
  
    lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lFascicoli+"  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;

    setStatement(lPaginedStatement);    
   }

   public void getCountFascicoliSospesiInterrotti(FascicoloSiepModel aModel,String[] aMotivo,String aAggiuntoUnion)
   throws DAOException
 {
   String lStatement ="SELECT COUNT(*)HowManyRecords FROM ( ";
   lStatement += " " + getFascicoloSqlAggregatoQuery(aModel,aMotivo);
   lStatement += " " + setCondizione(aModel);
   if("S".equals(aAggiuntoUnion))
	  lStatement += getUnion(aModel,aMotivo,"S","S");
   else
	   lStatement += ")";	
   
   setStatement(lStatement);
 }   

   
   public void ricercaFascicoloSiepSospesiInterrottiAll(FascicoloSiepModel aModel,String[] aMotivo,String aAggiuntoUnion)
   {
    String lFascicoli = "";
    
    lFascicoli += getFascicoloSqlAggregatoQuery(aModel,aMotivo);
    lFascicoli += setCondizione(aModel);
    if("S".equals(aAggiuntoUnion))
    	lFascicoli += getUnion(aModel,aMotivo,"N","S");   
    else
        lFascicoli += setOrder();	


    setStatement(lFascicoli);    
   }  
   
 /**
   * Settaggio della condizione sul Fascicolo ed evento
   * @param aSm
   * @return
   */
   
  private String setCondizione(FascicoloSiepModel aModel)
  {
    String lCondizioni= new String();
    
    // Cerca i fascicoli a partire da una coppia Progressivo/Anno
    if ( (aModel.getChiaveAnnoIniziale() != null ) && (aModel.getChiaveAnnoIniziale().intValue() >= 0)
         && (aModel.getChiaveProgrIniziale() != null ) && (aModel.getChiaveProgrIniziale().intValue() >= 0) )
    {
      lCondizioni += " AND ( (CHIAVE_ANNO > "+aModel.getChiaveAnnoIniziale()+")";
      lCondizioni +=      " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoIniziale()+" AND CHIAVE_PROGR >= "+aModel.getChiaveProgrIniziale()+"))";
    }
   // Cerca i fascicoli fino ad una coppia Progressivo/Anno
   if ( (aModel.getChiaveAnnoFinale() != null ) && (aModel.getChiaveAnnoFinale().intValue() >= 0)
         && (aModel.getChiaveProgrFinale() != null ) && (aModel.getChiaveProgrFinale().intValue() >= 0) )
    {
      // Nel caso non venga specificata la coppia di ricerca iniziale,
      // vengono cercati i fascicoli
      // a partire dal primo fascicolo dell'anno finale specificato
      if ( (aModel.getChiaveAnnoIniziale() == null ) || (aModel.getChiaveAnnoIniziale().intValue() <= 0)
          && (aModel.getChiaveProgrIniziale() == null ) || (aModel.getChiaveProgrIniziale().intValue() <= 0) )
      {
        lCondizioni += " AND ( (CHIAVE_ANNO > "+aModel.getChiaveAnnoFinale()+")";
        lCondizioni +=      " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoFinale()+" AND CHIAVE_PROGR >= 1))";
      }

      lCondizioni += " AND ( (CHIAVE_ANNO < "+aModel.getChiaveAnnoFinale()+")";
      lCondizioni +=      " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoFinale()+" AND CHIAVE_PROGR <= "+aModel.getChiaveProgrFinale()+"))";
    }    
      
  
    return lCondizioni;
  }


  private String condizioneMotivo(String[] aMotivo)
  {
    String lCondizioni = " AND evento.cod_motivo IN (";
    for(int i=0;i<aMotivo.length;i++)
    {
      lCondizioni += "'" +aMotivo[i]+ "'";
      if(aMotivo.length>1 && i<aMotivo.length-1)
        lCondizioni += ",";
    }

    lCondizioni += ")";
    
    return lCondizioni;
  }
  
  protected String getFascicoloSqlAggregatoQuery(FascicoloSiepModel aModel,String[] aMot)
  {
    String lStatement = new String();
  
      lStatement += " SELECT fascicolo_siep.id_fascicolo_siep, fascicolo_siep.chiave_anno, fascicolo_siep.chiave_progr, soggetto.nome,evento.cod_motivo, cg_ref_codes.rv_meaning DESCMOTIVO,";
      lStatement += " soggetto.cognome, soggetto.COD_COMUNE_NASCITA, comune.DESCRIZIONE COMUNE,soggetto.cod_stato_nascita, naz.rv_meaning stato, soggetto.data_nascita, soggetto.DESC_COMUNE_NASCITA_ESTERO, data_inizio_misura,";
      lStatement += " data_fine_misura,num_anni_misura,num_mesi_misura ,";
      lStatement += " num_giorni_misura,";
      lStatement += " sospensione.NUM_ANNI_PENA_ESPIATA,sospensione.NUM_MESI_PENA_ESPIATA,";
      lStatement += " sospensione.NUM_GIORNI_PENA_ESPIATA,sospensione.DATA_INIZIO,pena_residua.num_anni_reclusione,pena_residua.num_mesi_reclusione,";
      lStatement += " pena_residua.num_giorni_reclusione,pena_residua.num_anni_arresto,pena_residua.num_mesi_arresto,";
      lStatement += " pena_residua.num_giorni_arresto,null data_inizio_scadenza,null data_fine_scadenza,";  
      lStatement += " null num_anni_espulsione, null num_mesi_espulsione,null num_giorni_espulsione ";
      lStatement += " " + getFromAndWhere(aModel,aMot);
  
    return lStatement;
  }
  
  private String getFromAndWhere(FascicoloSiepModel aModel,String[] aMot)
  {
	  String lStatement = new String();
	  
      lStatement += " FROM evento, cg_ref_codes naz, comune ,fascicolo_siep, soggetto, sospensione, pena_residua, cg_ref_codes, misura_alternativa, ";
      lStatement += " (SELECT max(data_emissione) max_data_emi,max(data_inserimento) max_data_ins,fas_sie_id_fascicolo_siep ";
      lStatement += " FROM evento ";
      lStatement += " WHERE cod_tipo_evento = '01'"; 
      lStatement += " AND  cod_tipo_provvedimento NOT IN ('02', '03')";      
      lStatement += " group by fas_sie_id_fascicolo_siep) eve";			 
      lStatement += " WHERE EVENTO.COD_UFFICIO_INSERIMENTO = "+aModel.getChiaveUfficio();   	
      
        if(aMot.length>0)
        {
        	lStatement +=condizioneMotivo(aMot);
        }
      lStatement += " AND evento.fas_sie_id_fascicolo_siep = eve.fas_sie_id_fascicolo_siep";        
      lStatement += " AND evento.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";
      lStatement += " AND evento.data_emissione=eve.max_data_emi";      
      lStatement += " AND evento.data_inserimento=eve.max_data_ins ";
      lStatement += " AND naz.rv_domain = 'NAZIONE' ";
      lStatement += " AND naz.rv_low_value = soggetto.cod_stato_nascita ";  
      
      lStatement += " AND soggetto.id_soggetto = fascicolo_siep.sog_id_soggetto";
      lStatement += " AND pena_residua.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";
      lStatement += " AND pena_residua.eve_id_evento = evento.id_evento";
      lStatement += " AND pena_residua.flag_validato = 'S'";
      lStatement += " AND sospensione.pen_res_id_pena_residua = pena_residua.id_pena_residua";
      lStatement += " AND cg_ref_codes.rv_domain = 'MOTIVO_PROVVEDIMENTO'";
      lStatement += " AND cg_ref_codes.rv_low_value = evento.cod_motivo";
      lStatement += " AND misura_alternativa.eve_id_evento (+)= evento.eve_id_evento";
      lStatement += " AND soggetto.COD_COMUNE_NASCITA = comune.COD_COMUNE";	  
      
      return lStatement;	  
  }

  private String setOrder()
  {
    String lOrder = new String();
    
    lOrder = " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR";
    
    return lOrder;
  }
  
  private String getUnion(FascicoloSiepModel aModel,String[] aMot,String lCount,String lUnion)
  {
    String lStatement = new String();

  
    lStatement += " UNION SELECT fascicolo_siep.id_fascicolo_siep,fascicolo_siep.chiave_anno, fascicolo_siep.chiave_progr, soggetto.nome,evento.cod_motivo, cg_ref_codes.rv_meaning DESCMOTIVO,";
    lStatement += " soggetto.cognome, soggetto.COD_COMUNE_NASCITA, comune.DESCRIZIONE COMUNE,soggetto.cod_stato_nascita, naz.rv_meaning stato, soggetto.data_nascita, soggetto.DESC_COMUNE_NASCITA_ESTERO,";
    lStatement += " null data_fine_misura, null num_anni_misura, null num_mesi_misura ,";
    lStatement += " null num_giorni_misura,null data_inizio_misura,";
    lStatement += " sospensione.NUM_ANNI_PENA_ESPIATA,sospensione.NUM_MESI_PENA_ESPIATA,";
    lStatement += " sospensione.NUM_GIORNI_PENA_ESPIATA,sospensione.DATA_INIZIO,pena_residua.num_anni_reclusione,pena_residua.num_mesi_reclusione,";
    lStatement += " pena_residua.num_giorni_reclusione,pena_residua.num_anni_arresto,pena_residua.num_mesi_arresto,";
    lStatement += " pena_residua.num_giorni_arresto,data_inizio_scadenza,data_fine_scadenza,";
    lStatement += " verbale.num_anni_espulsione, verbale.num_mesi_espulsione,verbale.num_giorni_espulsione ";
    lStatement += " FROM evento, cg_ref_codes naz, verbale, comune,fascicolo_siep, soggetto,sospensione, pena_residua, cg_ref_codes, scadenzario_siep, ";
    lStatement += " (SELECT max(data_inserimento)max_data,fas_sie_id_fascicolo_siep from scadenzario_siep group by fas_sie_id_fascicolo_siep) scad,";
    lStatement += " (SELECT max(data_emissione) max_data_emi,max(data_inserimento) max_data_ins,fas_sie_id_fascicolo_siep ";
    lStatement += " FROM evento ";
    lStatement += " WHERE cod_tipo_evento = '01'"; 
    lStatement += " AND  cod_tipo_provvedimento NOT IN ('02', '03')";      
    lStatement += " group by fas_sie_id_fascicolo_siep) eve";			 
  	lStatement += " WHERE EVENTO.COD_UFFICIO_INSERIMENTO = "+aModel.getChiaveUfficio();   	
    lStatement += " AND evento.cod_motivo = '2141'"; 
    lStatement += " AND evento.fas_sie_id_fascicolo_siep = eve.fas_sie_id_fascicolo_siep";   
    lStatement += " AND evento.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";
    lStatement += " AND evento.data_emissione=eve.max_data_emi";      
    lStatement += " AND evento.data_inserimento=eve.max_data_ins "; 
    lStatement += " AND scadenzario_siep.data_inserimento = scad.max_data "; 
    lStatement += " AND scadenzario_siep.fas_sie_id_fascicolo_siep = scad.fas_sie_id_fascicolo_siep";     
    lStatement += " AND naz.rv_domain = 'NAZIONE' ";
    lStatement += " AND naz.rv_low_value = soggetto.cod_stato_nascita ";
    lStatement += " AND scadenzario_siep.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";
    lStatement += " AND verbale.eve_id_evento = evento.eve_id_evento";     
    lStatement += " AND soggetto.id_soggetto = fascicolo_siep.sog_id_soggetto";
    lStatement += " AND pena_residua.fas_sie_id_fascicolo_siep = fascicolo_siep.id_fascicolo_siep";
    lStatement += " AND pena_residua.eve_id_evento = evento.id_evento";
    lStatement += " AND pena_residua.flag_validato = 'S'";
    lStatement += " AND soggetto.cod_comune_nascita = comune.cod_comune";    
    lStatement += " AND sospensione.pen_res_id_pena_residua = pena_residua.id_pena_residua";
    lStatement += " AND cg_ref_codes.rv_domain = 'MOTIVO_PROVVEDIMENTO'";
    lStatement += " AND cg_ref_codes.rv_low_value = evento.cod_motivo";
	 
    lStatement += setCondizione(aModel);  
  
    if("S".equals(lCount)) 
    {
        lStatement += ")";     
    }    
    else
        lStatement += setOrder(); 
  
   return lStatement;   
  }  

 /**
  *
  * @return Il Model dei dati selezionati
  * @throws DAOException
  */
  public GenericModel getModel() throws DAOException
  {
	FascicoloSiepAggregatoModel  lAggregato = new FascicoloSiepAggregatoModel();
    	  
    FascicoloSiepModel lFascicolo = new  FascicoloSiepModel();
    lFascicolo.setIdFascicoloSiep(getBigDecimal("id_fascicolo_siep"));
    lFascicolo.setChiaveAnno(getBigDecimal("chiave_anno") );
    lFascicolo.setChiaveProgr(getBigDecimal("chiave_progr") );
  
    SoggettoModel lSoggetto = new  SoggettoModel();
    lSoggetto.setCognome(getString("cognome") );
    lSoggetto.setNome(getString("nome") );
    lSoggetto.setDataNascita(getDate("data_nascita") );
    lSoggetto.setDescrComuneNascita(getString("COMUNE") );
    lSoggetto.setDescrStatoNascita(getString("stato"));
    lSoggetto.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO") );
  
    MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
    lMisMod.setDataInizioMisura(getDate("data_inizio_misura"));
    lMisMod.setDataFineMisura(getDate("data_fine_misura"));
    lMisMod.setNumAnniMisura(getBigDecimal("num_anni_misura") );
    lMisMod.setNumMesiMisura(getBigDecimal("num_mesi_misura") );
    lMisMod.setNumGiorniMisura(getBigDecimal("num_giorni_misura") );

    SospensioneModel lSospMod = new SospensioneModel();
    lSospMod.setNumAnniPenaEspiata(getBigDecimal("NUM_ANNI_PENA_ESPIATA") );
    lSospMod.setNumMesiPenaEspiata(getBigDecimal("NUM_MESI_PENA_ESPIATA") );   
    lSospMod.setNumGiorniPenaEspiata(getBigDecimal("NUM_GIORNI_PENA_ESPIATA") ); 
    lSospMod.setDataInizio(getDate("DATA_INIZIO"));
 
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    lPenMod.setNumAnniReclusione(getBigDecimal("num_anni_reclusione") );
    lPenMod.setNumMesiReclusione(getBigDecimal("num_mesi_reclusione") );   
    lPenMod.setNumGiorniReclusione(getBigDecimal("num_giorni_reclusione") ); 
    lPenMod.setNumAnniArresto(getBigDecimal("num_anni_arresto") );
    lPenMod.setNumMesiArresto(getBigDecimal("num_mesi_arresto") );   
    lPenMod.setNumGiorniArresto(getBigDecimal("num_giorni_arresto") );    
    
    EventoModel lEveMod = new EventoModel();
    lEveMod.setDescrMotivo(getString("DESCMOTIVO"));
    lEveMod.setCodMotivo(getString("cod_motivo"));
    
    ScadenzarioModel lScaMod = new ScadenzarioModel();
    lScaMod.setDataInizioScadenza(getDate("data_inizio_scadenza"));
    lScaMod.setDataFineScadenza(getDate("data_fine_scadenza"));
    
    VerbaleModel lVerMod = new VerbaleModel();
    lVerMod.setNumAnniEspulsione(getBigDecimal("num_anni_espulsione") );
    lVerMod.setNumMesiEspulsione(getBigDecimal("num_mesi_espulsione") );
    lVerMod.setNumGiorniEspulsione(getBigDecimal("num_giorni_espulsione") );
    
    lAggregato.setSoggetto(lSoggetto);
    lAggregato.setFascicoloSiep(lFascicolo);
    lAggregato.setMisuraAlternativa(lMisMod);
    lAggregato.setSospensione(lSospMod);
    lAggregato.setPenaResidua(lPenMod);
    lAggregato.setEvento(lEveMod);
    lAggregato.setScadenzario(lScaMod);
    lAggregato.setVerbale(lVerMod);

    return lAggregato;
  }
}