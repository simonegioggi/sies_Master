package siap.siep.notiziareato.action;


/**
* <p>Title: ActInserisciNotiziaReato</p>
* <p>Description: Classe Action per l'inserimento della Notizia di Reato</p>
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

public class ActInserisciNotiziaReato extends ActionSiap implements ICostantiNotiziaReato
{
/**
* Azione di Inserimento del NotiziaReato
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
 		 {
     // istanzia il Model della Notizia di Reato
 		 NotiziaReatoModel lNotMod = new NotiziaReatoModel();
 		 
     

     // riempie il Model della Notizia di Reato
     lNotMod.setDataPervenimento( getRequestDateParameter( CAMPO_ANNO_DATA_PERVENIMENTO,CAMPO_MESE_DATA_PERVENIMENTO,CAMPO_GIORNO_DATA_PERVENIMENTO) );
     lNotMod.setDataAcquisizione( getRequestDateParameter( CAMPO_ANNO_DATA_ACQUISIZIONE,CAMPO_MESE_DATA_ACQUISIZIONE,CAMPO_GIORNO_DATA_ACQUISIZIONE) );
     lNotMod.setAcquisizioneDiretta( getRequestStringParameter( CAMPO_ACQUISIZIONE_DIRETTA) );
     lNotMod.setDataFatto( getRequestDateParameter( CAMPO_ANNO_DATA_FATTO,CAMPO_MESE_DATA_FATTO,CAMPO_GIORNO_DATA_FATTO) );
     lNotMod.setDescrizioneFonte( getRequestStringParameter( CAMPO_DESCRIZIONE_FONTE) );
     //  nuovi campi per integrazione REGE_SIES
     lNotMod.setDataArresto(getRequestDateParameter( CAMPO_ANNO_DATA_ARRESTO,CAMPO_MESE_DATA_ARRESTO,CAMPO_GIORNO_DATA_ARRESTO) );
     if(!getRequestStringParameter(CAMPO_ANNO_DATA_ARRESTO).equals(""))lNotMod.setFlagArrestato("S");
     lNotMod.setDataFermo(getRequestDateParameter( CAMPO_ANNO_DATA_FERMO,CAMPO_MESE_DATA_FERMO,CAMPO_GIORNO_DATA_FERMO) );     
     lNotMod.setFlagFotosegnalato(getRequestStringParameter(CAMPO_FLAG_FOTOSEGNALATO));
     lNotMod.setDataFoto(getRequestDateParameter( CAMPO_ANNO_DATA_FOTO,CAMPO_MESE_DATA_FOTO,CAMPO_GIORNO_DATA_FOTO));
     lNotMod.setCodAutoritaFoto(getRequestStringParameter(CAMPO_AUTORITA_FOTO));
     // Comune Fotosegnalamento
     ComuneModel IComFoto = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_FOTO)));
     lNotMod.setCodComuneFoto(IComFoto.getCodComune());
     
     // chiama il Model dei Comuni per reperire la descrizione del Comune
     ComuneModel IComMOd = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_FONTE)));
     lNotMod.setCodComuneFonte(IComMOd.getCodComune());

     lNotMod.setNumRegAutorita( getRequestStringParameter( CAMPO_NUM_REG_AUTORITA) );
     lNotMod.setLuogoProvenienza( getRequestStringParameter( CAMPO_LUOGO_PROVENIENZA) );
     lNotMod.setNumeroRicevuta( getRequestStringParameter( CAMPO_NUMERO_RICEVUTA) );

     lNotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
     lNotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
     lNotMod.setDataInserimento(DateUtils.getSysDate());

     
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
     
  
     //---Aggiungere in SIEPLookupRemote il metodo getNotiziaReatoRemote()

     // chiama il controller
		 // INotiziaReato lCtrl = SIEPLookupRemote.getNotiziaReatoRemote();
     NotiziaReatoController lCtrl = new NotiziaReatoController();

     NotiziaReatoModel llNotModRet = lCtrl.ExInserisciNotiziaReato(lNotMod);
     // setta la risposta nella request
     setRequestAttribute("notiziareato", llNotModRet);
     
     //   IMPOSTAZIONI PER FUNZIONALITà BACK
     if(!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)){    	
     	setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
     }

		 String lPage = "";
     // Apre la pagina di dettaglio della Notizia di Reato
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notiziareato.action.ActDettaglioNotiziaReato&"+CAMPO_ID_NOTIZIA_REATO+"="+llNotModRet.getIdNotiziaReato().toString();
		 return lPage;
	 }



}