package siap.siep.modulocumulo.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;

public class ModuloCumuloSqlDao extends SqlDAO
{
  public ModuloCumuloSqlDao (Connection con) {
    super(con);
  }
  
  public void RicercaProcedimentiPerTitoloSoggetto(SentenzaModel aSentenzaMod, SoggettoModel aSoggettoMod, FascicoloSiepModel aFascicoloSiepMod, int aPage)  throws DAOException
  {
		String lStatement ="SELECT " +
				"s.ID_SENTENZA, s.DATA_PROVVEDIMENTO, " +
				"s.COD_TIPO_PROVVEDIMENTO, D_TIPROV.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, " +
				"s.COD_TIPO_AUTORITA_EMITTENTE, D_UFFICIOEMI.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE, " +
				"s.COD_LUOGO_EMITTENTE, s.NUM_SEZIONE_AUTORITA_EMITTENTE, " +
				"luo.DESCRIZIONE DESCR_LUOGO_EMITTENTE, "+
				"s.ANNO_SENTENZA, s.NUMERO_SENTENZA, " +
				"f.DATA_IRREVOCABILITA, f.DATA_ISCRIZIONE, "+
				"f.COD_STATO_FASCICOLO, D_STATOFASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO, "+
				"f.ID_FASCICOLO_SIEP, f.CHIAVE_ANNO, f.CHIAVE_PROGR, f.CHIAVE_UFFICIO, " + 
				"f.SEN_ID_SENTENZA, f.SOG_ID_SOGGETTO, " +
				"ud.DESCR_TIPO_UFFICIO, ud.DESCR_COMUNE, " +
				"c.DESCRIZIONE DESC_COMUNE_NAS, "+
				"sg.COGNOME, sg.NOME, sg.COD_AFIS, " +
				"sg.ANNO_NASCITA, sg.DATA_NASCITA, sg.COD_COMUNE_NASCITA, sg.COD_PROVINCIA_NASCITA "+
				
				"FROM " +	

				"SOGGETTO sg, SENTENZA s, FASCICOLO_SIEP f, COMUNE luo, COMUNE c, " +
				"CG_REF_CODES D_TIPROV,CG_REF_CODES D_UFFICIOEMI, CG_REF_CODES D_STATOFASCICOLO, UFFICIO_DESCR ud "+
		
		 		"WHERE";
		
		lStatement +=" D_TIPROV.RV_DOMAIN='TIPO_PROVVEDIMENTO' AND D_TIPROV.RV_LOW_VALUE=s.COD_TIPO_PROVVEDIMENTO ";
		lStatement +=" AND D_UFFICIOEMI.RV_DOMAIN='TIPO_UFFICIO_EMITTENTE' AND D_UFFICIOEMI.RV_LOW_VALUE=s.COD_TIPO_AUTORITA_EMITTENTE ";
		lStatement +=" AND D_STATOFASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND f.COD_STATO_FASCICOLO = D_STATOFASCICOLO.RV_LOW_VALUE  ";
		lStatement +=" AND luo.COD_COMUNE = s.COD_LUOGO_EMITTENTE";
		lStatement +=" AND sg.COD_COMUNE_NASCITA = c.COD_COMUNE";
		lStatement +=" AND s.ID_SENTENZA = f.SEN_ID_SENTENZA " ;
		lStatement +=" AND f.CHIAVE_UFFICIO = '"+aFascicoloSiepMod.getChiaveUfficio()+"'";
		lStatement +=" AND ud.COD_UFFICIO = f.CHIAVE_UFFICIO ";
		lStatement +=" AND sg.ID_SOGGETTO = f.SOG_ID_SOGGETTO" ;
		
		if(aFascicoloSiepMod.getDataIrrevocabilita()!=null)
			lStatement +=" AND f.DATA_IRREVOCABILITA = TO_DATE('" + DateUtils.getDateToString (aFascicoloSiepMod.getDataIrrevocabilita(),"ddMMyyyy") + "', 'DDMMYYYY') " ;

		lStatement += "" + setCondizioneSentenza(aSentenzaMod);
		lStatement += "" + setCondizioneCognomeNome(aSoggettoMod);
		
		String lPaginedStatement=new String("");
		
		lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lStatement+"  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;

	    setStatement(lPaginedStatement);
	
	}

  public void CountProcedimentiPerTitoloSoggetto(SentenzaModel aSentenza, SoggettoModel aSoggetto, FascicoloSiepModel aFascicoloSiep )  throws DAOException
  {
		String lStatement ="SELECT COUNT(*) HowManyRecords " ;
		lStatement += "FROM SOGGETTO sg, SENTENZA s, FASCICOLO_SIEP f ";  			
		lStatement += " WHERE ";
		lStatement +=" s.ID_SENTENZA = f.SEN_ID_SENTENZA " ;
		lStatement +=" AND f.CHIAVE_UFFICIO = "+aFascicoloSiep.getChiaveUfficio();
		lStatement +=" AND sg.ID_SOGGETTO = f.SOG_ID_SOGGETTO" ;
		
		if(aFascicoloSiep.getDataIrrevocabilita()!=null)
			lStatement +=" AND f.DATA_IRREVOCABILITA = TO_DATE('" + DateUtils.getDateToString (aFascicoloSiep.getDataIrrevocabilita(),"ddMMyyyy") + "', 'DDMMYYYY') " ;

		lStatement += "" + setCondizioneSentenza(aSentenza);
		lStatement += "" + setCondizioneCognomeNome(aSoggetto);
		
		setStatement(lStatement);
	}
  
  public String  setCondizioneSentenza(SentenzaModel aSentenzaMod)
  {
	  String lCondizioni = new String();
	  
	  lCondizioni +=" AND s.COD_TIPO_PROVVEDIMENTO = '"+aSentenzaMod.getCodTipoProvvedimento()+"' ";
	  
	  if(aSentenzaMod.getAnnoSentenza()!=null && aSentenzaMod.getNumeroSentenza()!=null)
	  {
		  lCondizioni +=" AND s.ANNO_SENTENZA = "+aSentenzaMod.getAnnoSentenza();
		  lCondizioni +=" AND s.NUMERO_SENTENZA = '"+aSentenzaMod.getNumeroSentenza()+"' ";
	  }
		
	  // in realtà Data Provvedimento, Tipo e Sede Autorità Emittente dovrebbero essere Dati Obbligatori in aSentenzaModel:
	  if(aSentenzaMod.getDataProvvedimento()!=null)
		  lCondizioni +=" AND s.DATA_PROVVEDIMENTO = TO_DATE('" + DateUtils.getDateToString (aSentenzaMod.getDataProvvedimento(),"ddMMyyyy") + "', 'DDMMYYYY') " ;
		
	  if(aSentenzaMod.getCodTipoAutoritaEmittente()!=null)
		  lCondizioni +=" AND s.COD_TIPO_AUTORITA_EMITTENTE = '"+aSentenzaMod.getCodTipoAutoritaEmittente()+"' ";
		
	  if(aSentenzaMod.getCodLuogoEmittente()!=null)
		  lCondizioni +=" AND s.COD_LUOGO_EMITTENTE = '"+aSentenzaMod.getCodLuogoEmittente()+"' ";
	  
	  return lCondizioni;
  }
  
  public String  setCondizioneCognomeNome(SoggettoModel aSoggetto)
  {
	  String lCondizioni = new String();
	  
/*	  if(aSoggetto.getCognome()!=null && aSoggetto.getNome()!=null)
	  {	
		  lCondizioni +=" AND sg.COGNOME = '"+ StringUtils.convertSqlString(aSoggetto.getCognome())+ "'";
		  lCondizioni +=" AND sg.NOME = '"+ StringUtils.convertSqlString(aSoggetto.getNome())+ "'";
	  }
*/	  
	  // CHG: la ricerca per soggetto va fatta per nome e cognome OR CUI
	  // ovvero deve restituire anche i soggetti che hanno Nome e cognome 
	  // differenti da quelli incdicati ma CUI = a quello indicato
	  // AND (   (sg.COGNOME like 'xx' AND sg.NOME like 'xx') OR sg.COD_AFIS = '')
	  // AND (   (sg.COGNOME like 'xx' AND sg.NOME like 'xx'))
	  // AND sg.COD_AFIS = ''
	  
    boolean isCognome = false;
	  boolean isNome    = false;
	  boolean isCUI     = false;
	  String lStrCognome = null;
	  String lStrNome = null;
	  String lStrCUI = null;
	  
	  if (aSoggetto.getCognome() != null && aSoggetto.getCognome().length()>0) {
	    isCognome = true;
	    lStrCognome = " sg.COGNOME like '" + StringUtils.convertSqlString(aSoggetto.getCognome())+ "%'";
	  }
	  
	  if (aSoggetto.getNome() != null && aSoggetto.getNome().length()>0) {
	    isNome = true;
	    lStrNome = " sg.NOME like '" + StringUtils.convertSqlString(aSoggetto.getNome())+ "%'";
	  }

	  if (aSoggetto.getCodAfis()!= null && aSoggetto.getCodAfis().length()>0) {
	    isCUI = true;
	    lStrCUI = " sg.COD_AFIS = '" + aSoggetto.getCodAfis()+ "'";
	  }
	  
	  if ( (isCognome || isNome) && isCUI){
	    // Cognome/Nome e CUI
	    //AND (   (sg.COGNOME like 'xx' AND sg.NOME like 'xx') OR sg.COD_AFIS = '')
      //AND (   sg.COGNOME like 'xx' OR sg.COD_AFIS = '')
      //AND (   sg.NOME like 'xx' OR sg.COD_AFIS = '')
	    
	    lCondizioni += " AND (   ";
	    if (isCognome && isNome)  
	      lCondizioni += " ("+lStrCognome+" AND "+lStrNome+" ) ";
	    else if (isCognome)  
        lCondizioni += " "+lStrCognome;
	    else if (isNome)  
        lCondizioni += " "+lStrNome;
	    
	    lCondizioni += " OR "+lStrCUI+" ) ";
	  }
	  else if ( (isCognome||isNome) && !isCUI) {
	    // Solo nome o cognome vado in AND
	    if (isCognome)
	      lCondizioni += " AND "+lStrCognome;
      if (isNome)
        lCondizioni += " AND "+lStrNome;
	  }
	  else if ( !(isCognome||isNome) && isCUI) {
	    // solo CUI
	    lCondizioni += " AND "+lStrCUI;
	  }
	    
//	  if (aSoggetto.getCognome() != null && aSoggetto.getCognome().length()>0) 
//		  lCondizioni += " AND sg.COGNOME like '" + StringUtils.convertSqlString(aSoggetto.getCognome())+ "%'";
//
//	  if (aSoggetto.getNome() != null && aSoggetto.getNome().length()>0)
//		  lCondizioni += " AND sg.NOME like '" + StringUtils.convertSqlString(aSoggetto.getNome())+ "%'";
//	   
//    if (aSoggetto.getCodAfis()!= null && aSoggetto.getCodAfis().length()>0)
//      lCondizioni += " AND sg.COD_AFIS = '" + aSoggetto.getCodAfis()+ "'";
	  
	  return lCondizioni;
  }	
  
  public GenericModel getModelTitoliProcSoggetto() throws DAOException
  {
	    SentenzaSoggettoFascicoloModel lModel = new SentenzaSoggettoFascicoloModel();

	    lModel.setIdSentenza(getBigDecimal("ID_SENTENZA") );
	    lModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
	    lModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO") );
	    lModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO") );
	    
	    lModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE") );
	    lModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE") );
	    lModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
	    lModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE") );
	    lModel.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE") );
	    lModel.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA") );
	    lModel.setNumSentenza(getString("NUMERO_SENTENZA") );
	    lModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );

	    lModel.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP") );
	    lModel.setCodiceStatoFascicolo(getString("COD_STATO_FASCICOLO") );
	    lModel.setDataIscrizione(getDate("DATA_ISCRIZIONE") );
	    lModel.setChiaveAnnoFascicolo(getBigDecimal("CHIAVE_ANNO") );
	    lModel.setChiaveNumeroFascicolo(getBigDecimal("CHIAVE_PROGR") );
	    lModel.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA") );
	    lModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
	    lModel.setChiaveUfficio(getString("CHIAVE_UFFICIO") );
	    
	    lModel.setDescrStatoFasc(getString("DESCR_STATO_FASCICOLO"));
	    lModel.setDescrChiaveUfficio(getString("DESCR_TIPO_UFFICIO"));
	    lModel.setDescrComune(getString("DESCR_COMUNE"));
	    
	    lModel.setNome(getString("NOME") );
	    lModel.setCognome(getString("COGNOME") );
	  
	    lModel.setCodiceCUI(getString("COD_AFIS") );
	    lModel.setAnnoNascita(getBigDecimal("ANNO_NASCITA") );
	    lModel.setDataNascita(getDate("DATA_NASCITA") );
	    lModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA") );
	    lModel.setDescrComuneNascita(getString("DESC_COMUNE_NAS") );
	    lModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA") );
	    
	    // La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
	    lModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getProvincie(),lModel.getCodProvinciaNascita()));

	    return lModel;
  }
  
}
