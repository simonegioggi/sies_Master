package siap.siep.modulocumulo.action;

/**
* <p>Title: ActModificaMisuraSicurezzaCumulo</p>
* <p>Description: Classe Action per la modifica di MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaMisuraSicurezzaCumulo extends ActionModuloCumulo implements ICostantiMisuraSicurezzaCumulo
{
 /*****************************************************************************
  * Azione di Modifica del MisuraSicurezzaCumulo
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //========================================================================== 
    // Recupero i soli campi da aggiornare 
    //========================================================================== 
    IMisuraSicurezzaCumulo lCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
    MisuraSicurezzaCumuloModel lMisMod = lCtrl.ExRicercaMisuraSicurezzaCumuloById(getRequestBigDecimalParameter(CAMPO_ID_MISURA_SICUREZZA_CUMULO) );

    if(lMisMod != null && lMisMod.getIdMisuraSicurezzaCumulo()!=null);
    {
	    lMisMod.setCodNatura                 ( getRequestStringParameter     ( CAMPO_COD_NATURA) );
	    lMisMod.setCodTipo                   ( getRequestStringParameter     ( CAMPO_COD_TIPO) );
	    lMisMod.setNumAnni                   ( getRequestBigDecimalParameter ( CAMPO_NUM_ANNI) );
	    lMisMod.setNumMesi                   ( getRequestBigDecimalParameter ( CAMPO_NUM_MESI) );
	    lMisMod.setNumGiorni                 ( getRequestBigDecimalParameter ( CAMPO_NUM_GIORNI) );
	    lMisMod.setMotivoModifica            ( getRequestStringParameter     ( ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA) );
	    
	    lMisMod.setDataFineValidita(getRequestDateParameter( CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );
	    
	    if(!isRequestParameterNullObj(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO) && 
	    	getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO)!=null && 
	    	!getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO).equals("") )
	    {	
	    	lMisMod.setAnnoFascicoloSiepIV(getRequestBigDecimalParameter(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO));
	    	lMisMod.setNumeroFascicoloSiepIV(getRequestBigDecimalParameter(ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO));
	    }
	    else
	    {
	    	lMisMod.setAnnoFascicoloSiepIV(null);
	    	lMisMod.setNumeroFascicoloSiepIV(null);
	    }

	    if(!isRequestParameterNullObj(ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO) &&
	    	!getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO).equals("-") )
	    {	
	    	String lCodiceuf = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO),
																		getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO) );

	    	ComuneModel lCom = getCodComuneByDescr(getRequestStringParameter(ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO));
	    	lMisMod.setCodLuogoEmittenteIV(lCom.getCodComune());
	    	lMisMod.setCodAutoritaEmittenteIV(lCodiceuf);
	    }
	    else
	    {
	    	lMisMod.setCodAutoritaEmittenteIV(null);
	    	lMisMod.setCodLuogoEmittenteIV(null);
	    }
	    		
	    lMisMod.setFlagStatoMisura(getRequestStringParameter(CAMPO_COD_STATO_MISURA));

	    if(!getRequestStringParameter(CAMPO_FLAG_STATO).equals("I")){
	      // Cambio il flag a modificato solo se il dato di partenza era diverso da I (Inserito a Mano dopo l'estrazione)
	      lMisMod.setFlagStato ("M");
	    }
	    
	    lMisMod.setCodOperatoreAggiornamento ( getCodUtenteConnesso());
	    lMisMod.setDataAggiornamento         ( DateUtils.getSysDate());
	    lMisMod.setCodUfficioAggiornamento   ( getCodUfficioUtenteConnesso());
	
	    //=================================================== 
	    // Recupera il controller ed effettua la modifica 
	    //=================================================== 

	    lCtrl.ExModificaMisuraSicurezzaCumulo(lMisMod);
    }    

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActLoadDettaglioMisuraSicurezzaCumulo";
    lPage += "&" + CAMPO_ID_MISURA_SICUREZZA_CUMULO + "=" + lMisMod.getIdMisuraSicurezzaCumulo().toString();

    return lPage;
  }
}