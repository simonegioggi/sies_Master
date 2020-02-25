package siap.siep.scadenzario.action;

import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
//import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;



/**
* <p>Title: ActLoadDettaglioScadenzarioVaneRicerche</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioScadenzarioVaneRicerche extends ActionSiap implements ICostantiScadenzario
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_SCADENZARIO);
 		 // riempie il model
		 // chiama il controller
 		 
		 IScadenzario lCtrl1 = SIEPLookupRemote.getScadenzarioRemote();
		 ScadenzarioModel llScaMod = lCtrl1.ExRicercaScadenzarioByKey(new BigDecimal(lId));
		 setRequestAttribute("scadenzario", llScaMod);
		 
         llScaMod.setFlagVisto("S");
         llScaMod.setDataVisto(DateUtils.getSysDate());
         lCtrl1.ExModificaScadenzario(llScaMod);
         
         
         /*
         
// AMBROSINO a6-rr-238         
         
	     EventoNotificaModel lEveNot = new EventoNotificaModel();
 	     IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
 	    

 	    // Paolo Cherubini 17/05/2011 cambio la query, prendo tutti gli eventi che creano lo scadenzario 03 VANE RICERCHE
 	    // verificare anche con la procedura AGGIORNA_SCADENZARIO_TIPO03
 	    //	  lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(llScaMod.getFascicoloModel().getIdFascicoloSiep(),"OE");
 	    // 	  '0218',  -- per la carcerazione e Ordine di Scarcerazione
 	    // 	  '0057',	-- Ordine di Esecuzione per la carcerazione (condannato libero)
 	    // 	  '0078',	-- Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.- omessa istanza libero / Revoca decr.sosp.- rigetto istanza libero
		// 	  '0079',	-- Revoca decr.sosp.- rigetto istanza det.a.c. / Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.- rigetto istanza det.a.c. decorr/scad / Revoca decr.sosp.- omessa istanza det.a.c.decorr/scad / Revoca decr.sosp.- omessa istanza det.a.c.
		// 	  '0080',	-- Revoca decr.sosp.- rigetto istanza arr.dom. / Revoca decr.sosp. - revoca da PM. / Revoca decr.sosp.- rigetto istanza sosp.arr.dom.
		// 	  '0130',	-- Ordine Esecuzione per carcerazione con rideterminazione pena - Libero
		// 	  '0131',	-- Ordine Esecuzione per carcerazione con rideterminazione pena e ordine di scarcerazione - Detenuto per questa causa
		// 	  '0132',	-- Ordine Esecuzione per carcerazione con rideterminazione pena - Detenuto per altra causa / Ordine Esecuzione per carcerazione con rideterminazione pena e ordine scarcerazione - Detenuto per altra causa
		// 	  '0133',	-- Ordine Esecuzione per Carcerazione con Rideterminazione Pena
		// 	  '0134',	-- Ordine Esecuzione per carcerazione con rideterminazione pena e ordine scarcerazione - Detenuto agli arresti domiciliari
		// 	  '0217',	-- Ordine di Esecuzione per la carcerazione - sospensione
		// 	  '0222',	-- Esecuzione pene concorrenti - libero
		// 	  '0271',	-- Ordine Esecuzione - Libero
		// 	  '0354',	-- Ordine esecuzione per la carcerazione (Rigetto Differimento)
		// 	  '0355',	-- Ordine esecuzione per la carcerazione (Revoca Differimento)
		// 	  '0356',	-- Cumulo stesso ufficio
		// 	  '0357',	-- Cumulo altro ufficio
		// 	  '0397',	-- Ordine di Esecuzione per sanzione sostitutiva - libero
		// 	  '0398',	-- Ordine di Esecuzione per sanzione sostitutiva -  detenuto altra causa
		// 	  '0399',	-- OE Nuovo Residuo Pena per Revoca Misure Cautelari - Det Questa Causa
		// 	  '0490',	-- Ordine di Esecuzione per sanzione sostitutiva - libero
		// 	  '0491',	-- Ordine di Esecuzione con sospensione per sanzione sostitutiva - detenuto altra causa
		// 	  '0494',	-- Ordine di Esecuzione Revoca decreto sosp. oe per la carcerazione - sospensione
		// 	  '0495',	-- Revoca decr.sosp.- rigetto istanza libero-  legge 199/2010
		// 	  '0935',	-- OE Nuovo Residuo Pena per Revoca Misure Cautelari - In Mis Alt
		// 	  '0960',	-- Ordine Esecuzione Ridet. Pena Altro - Libero
		// 	  '0961',	-- Ordine Esecuzione Ridet. Pena Altro - Arresti Domiciliari
		// 	  '0962',  -- Ordine Esecuzione per la Carcerazione con rideterminazione pena - detenuto per altra causa
		// 	  '2630'   -- Paolo 04/03/2011 Concessione esecuzione presso domicilio
        
 	    String[] motivo ={"0218","0057","0078","0079","0080","0130","0131","0132","0133","0134","0217","0222","0271","0354",
 	    				  "0355","0356","0357","0397","0398","0399","0490","0491","0494","0495","0935","0960","0961","0962","2630"};
 	    lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloCodiceMotivo(llScaMod.getFascicoloModel().getIdFascicoloSiep(),motivo);
 	   */
 	     
         // Paolo Cherubini 07/06/2011 accedo direttamente per id_Evento 
         // ho commentato tutta la parte sopra
        EventoNotificaModel lEveNot = new EventoNotificaModel();
        IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
   	  	lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(llScaMod.getEveIdEvento());
         
        NotificaModel lNotifica = new NotificaModel();

 	      if(lEveNot.getNotifiche() != null && lEveNot.getNotifiche().length > 0)
 	      { 
 	    	  lNotifica = null;
 	    	  for(int i=0;i<lEveNot.getNotifiche().length;i++)
 	    	  {
 	    		  NotificaModel lNotMod = new NotificaModel();
 	    		  lNotMod =lEveNot.getNotifiche()[i];
 	    		  if(lNotMod.getCodTipoNotifica().equals("E"))
 	    		  {
 	    			  lNotifica = lNotMod;
 	    		  }
 	    	  }
 	      }	  	  

 	       setRequestAttribute("evento",lEveNot.getEvento());
 	       setRequestAttribute("notifica", lNotifica);         

		 return PG_LOAD_DETTAGLIOSCADENZARIOVANERICERCHE;
	 }



}