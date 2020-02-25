package siap.siep.notiziareato.action;


/**
* <p>Title: ActModificaNotiziaReato</p>
* <p>Description: Classe Action per la modifica di NotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notiziareato.controller.NotiziaReatoController;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.web.ISIAPCostantiWeb;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaNotiziaReato extends ActionSiap implements ICostantiNotiziaReato
{
/**
* Azione di Modifica del NotiziaReato
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

    String lId = getRequestStringParameter(CAMPO_ID_NOTIZIA_REATO);

    // istanzia il Model della Notizia di Reato
    NotiziaReatoModel lNotMod = new NotiziaReatoModel ();

    // riempie il Model della Notizia di Reato
    lNotMod.setIdNotiziaReato(new BigDecimal(lId));
    lNotMod.setDataPervenimento( getRequestDateParameter( CAMPO_ANNO_DATA_PERVENIMENTO,CAMPO_MESE_DATA_PERVENIMENTO,CAMPO_GIORNO_DATA_PERVENIMENTO) );
    lNotMod.setDataAcquisizione( getRequestDateParameter( CAMPO_ANNO_DATA_ACQUISIZIONE,CAMPO_MESE_DATA_ACQUISIZIONE,CAMPO_GIORNO_DATA_ACQUISIZIONE) );
    lNotMod.setAcquisizioneDiretta( getRequestStringParameter( CAMPO_ACQUISIZIONE_DIRETTA) );
    lNotMod.setDataFatto( getRequestDateParameter( CAMPO_ANNO_DATA_FATTO,CAMPO_MESE_DATA_FATTO,CAMPO_GIORNO_DATA_FATTO) );
    lNotMod.setDescrizioneFonte( getRequestStringParameter( CAMPO_DESCRIZIONE_FONTE) );
    
    //  nuovi campi per integrazione REGE_SIES    
    lNotMod.setFlagFotosegnalato(getRequestStringParameter(CAMPO_FLAG_FOTOSEGNALATO));
    lNotMod.setDataArresto(getRequestDateParameter( CAMPO_ANNO_DATA_ARRESTO,CAMPO_MESE_DATA_ARRESTO,CAMPO_GIORNO_DATA_ARRESTO) );
    if(!getRequestStringParameter(CAMPO_ANNO_DATA_ARRESTO).equals(""))lNotMod.setFlagArrestato("S");
    lNotMod.setDataFermo(getRequestDateParameter( CAMPO_ANNO_DATA_FERMO,CAMPO_MESE_DATA_FERMO,CAMPO_GIORNO_DATA_FERMO) );
    lNotMod.setDataFoto(getRequestDateParameter( CAMPO_ANNO_DATA_FOTO,CAMPO_MESE_DATA_FOTO,CAMPO_GIORNO_DATA_FOTO) );
    lNotMod.setCodAutoritaFoto(getRequestStringParameter(CAMPO_AUTORITA_FOTO));
    // Comune Fotosegnalamento
    ComuneModel IComFoto = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_FOTO)));
    lNotMod.setCodComuneFoto(IComFoto.getCodComune());
    //lNotMod.setCodComuneFoto(getRequestStringParameter(CAMPO_COD_COMUNE_FOTO));
 
    // chiama il Model dei Comuni per reperire la descrizione del Comune
    ComuneModel IComMOd = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_FONTE)));
    lNotMod.setCodComuneFonte(IComMOd.getCodComune());

    lNotMod.setNumRegAutorita( getRequestStringParameter( CAMPO_NUM_REG_AUTORITA) );
    lNotMod.setLuogoProvenienza( getRequestStringParameter( CAMPO_LUOGO_PROVENIENZA) );
    lNotMod.setNumeroRicevuta( getRequestStringParameter( CAMPO_NUMERO_RICEVUTA) );

    ProfileModel lProfilo =(ProfileModel) this.getUtenteConnesso().getUserProfile();
	 if( lProfilo.isSige()) {
		// ANGELA 25.05.2009
		 // Fascicolo Sige Esteso in sessione.
	    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
	    lNotMod.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige()); 
	}
	else{
		// prende dalla Session l'ID del procedimento
	    BigDecimal lIdFascicolo = ( (FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep();
	    lNotMod.setFasSieIdFascicoloSiep(lIdFascicolo);
	}	

    lNotMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lNotMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lNotMod.setDataAggiornamento(DateUtils.getSysDate());
    
		 // chiama il controller
		 //INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
     NotiziaReatoController lCtrl = new NotiziaReatoController();
		 NotiziaReatoModel llNotModRet = lCtrl.ExModificaNotiziaReato(lNotMod);

     // Imposta la Modalità di Modifica.
		 setRequestAttribute("modalita", "M");
     // setta la risposta nella request
		 setRequestAttribute("notiziareato", llNotModRet);
	 

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notiziareato.action.ActDettaglioNotiziaReato&"+CAMPO_ID_NOTIZIA_REATO+"="+llNotModRet.getIdNotiziaReato().toString();
		 //IMPOSTAZIONI PER FUNZIONALITà BACK
	     if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){    	
	     	//setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
	    	 lPage += "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE+"="+getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
	     }
		 
		 return lPage;
	 }



}