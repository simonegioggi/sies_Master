package siap.sige.impugnazione.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;
import f3b.web.html.Option;


/**
* <p>Title: ActLoadModificaImpugnazioneSige</p>
* <p>Description: Classe Action per la load Modifica di Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige {
  public String processRequest() throws Exception {
     setLinkRitorno();
     // Recupero del record di Impugnazione
     IImpugnazioneSige lCtrlImp = SIGELookupRemote.getImpugnazioneSigeRemote();
     ImpugnazioneSigeModel impugnazione = lCtrlImp.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
  
      // Lock
      LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Impugnazione",impugnazione.getIdImpugnazioneSige().toString(),getCodUtenteConnesso(),getSession().getId());
      if (lck!=null) {
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il/la  "+lck.getEntity()+" è in gestione ad un altro utente!<BR />Riprovare più tardi !");
          return IWebConstants.PG_MESSAGE;
      }

      // Si passa nella request l'impugnazione.
      setRequestAttribute("impugnazione", impugnazione);
      setRequestAttribute("provvedimento", impugnazione.getProvvedimentoSige());
    
      // Imposta ComboBOX Soggetto Impugnante    
      Option lOptionSoggetto = new Option( DecodificheManager.getInstance().getSoggettoImpugnanteSige(), impugnazione.getSoggettoImpugnante());
      //String[] lFilterSoImp = {"01","02","03","05"};
      //lOptionSoggetto.setFilter( lFilterSoImp );
      setRequestAttribute("soggettoImpugnante", "" + lOptionSoggetto );
      
      // Imposta ComboBOX Autorita Destinataria
      Option  lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
      String[] lFilterCSS = {"CSS"};
      lOption.setFilter( lFilterCSS );
      setRequestAttribute("ListaUffici", ""+ lOption);
      
      Option optUfficiRecuperCrediti= new Option(DecodificheManager.getInstance().getTipoAutorita(), "-", 75);
      optUfficiRecuperCrediti.setFilter(new String []{"-","37","54","57","98"});
      optUfficiRecuperCrediti.setSelected(this.getTipoUfficioRecuperoCrediti(impugnazione.getNotifiche()));
      
      setRequestAttribute("ufficiRecuperoCrediti", optUfficiRecuperCrediti.toString());
      
      Option lOptionUffici = new Option(DecodificheManager.getInstance().getAutoritaCompetente());
      lOptionUffici.setFilter(new String[]{"-","PGCAP","PM","PMM"});
      lOptionUffici.setSelected(this.getTipoUfficioPubblicoMinistero(impugnazione.getNotifiche()));
      setRequestAttribute("ufficiPubbliciMinisteri", lOptionUffici.toString());
      String sedePubblicoMinistero =this.getSedeUfficioPubblicoMinistero(impugnazione.getNotifiche());
      String sedeUfficioRecuperoCrediti = this.getSedeUfficioRecuperoCrediti(impugnazione.getNotifiche());
      
      setRequestAttribute("sedeRecuperoCrediti", sedeUfficioRecuperoCrediti);
      setRequestAttribute("sedePubblicoMinistero", sedePubblicoMinistero);
      
      setRequestAttribute("showDestinatari", String.valueOf(showDestinatariImpugnazioni(impugnazione)));
      setRequestAttribute("notifiche", impugnazione.getNotifiche());
      return PG_LOAD_MODIFICAIMPUGNAZIONESIGE;
  }
  
  private String getTipoUfficioPubblicoMinistero (Vector<NotificaModel> notifiche) {
	  String codiceUfficioPubblicoMinistero ="-";
	  for (NotificaModel notifica  : notifiche) {
		  if (notifica.getUfficio() != null) {
			  codiceUfficioPubblicoMinistero=notifica.getUfficio().getCodTipoUfficio();
		  }
	  }
	  
	  return codiceUfficioPubblicoMinistero ;
  }
  
  private String getSedeUfficioPubblicoMinistero (Vector<NotificaModel> notifiche) {
	  String sedeUfficioPubblicoMinistero ="";
	  for (NotificaModel notifica  : notifiche) {
		  if (notifica.getUfficio() != null) {
			  sedeUfficioPubblicoMinistero =notifica.getUfficio().getDescrComune();
		  }
	  }
	  return sedeUfficioPubblicoMinistero ;
  }
  
  private String getTipoUfficioRecuperoCrediti (Vector<NotificaModel> notifiche) {
	  String tipoUfficioRecuperoCrediti="-";
	  for (NotificaModel notifica  : notifiche) {
		  if (notifica.getAutoritaEsterna() != null) {
			  tipoUfficioRecuperoCrediti=notifica.getAutoritaEsterna().getCodTipoAutorita();
		  }
	  }
	  return tipoUfficioRecuperoCrediti ;
  }
  
  private String getSedeUfficioRecuperoCrediti (Vector<NotificaModel> notifiche) {
	  String sedeUfficioRecuperoCrediti="-";
	  for (NotificaModel notifica  : notifiche) {
		  if (notifica.getAutoritaEsterna() != null) {
			  sedeUfficioRecuperoCrediti=notifica.getAutoritaEsterna().getDescrSede();
		  }
	  }
	  return sedeUfficioRecuperoCrediti ;
  }
  
 
}