package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.scadenzario.controller.IScadenzarioSige;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActRicercaFSPNotifica</p>
* <p>Description: Classe Action per la ricerca puntuale del Fascicolo SIGE finalizzata alla notifica provvedimenti</p>
* Viene effettuata la ricerca del Fascicolo SIGE e dei provvedimenti ad essi collegati.
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaFSigePNotifica extends ActRicercaFSigePuntuale implements ICostantiFascicoloSige, ICostantiProvvedimentoSige
{
  public String processRequest() throws Exception
  {

	    if (isRequestParameterNullObj("noQuery"))
	    {
	    	super.processRequest();
	    	setLinkRitorno();
	    }

    // Recupero del FascicoloSigeEsteso. Se non in sessione solleva un errore di eccezione.
    if( isSessionAttributeNullObj("FascicoloSigeEsteso") )
      throw new SIGEException( SIGEException.USER_MESSAGE, "Procedimento non selezionato" );

    String lmodificabile; // modificabilità delle date di notifica
    FascicoloSigeEstesoModel lFasSigeEstMod = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    
    // Recupero del vettore dei provvedimenti (compreso evento) emessi per il fascicolo

    IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();

    // Vector lVect = mCtrl.ExRicercaProvvedimentiSigePerIdFasSige(lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige());
    Vector <ProvvedimentoSigeEventoModel>lVect = mCtrl.ExRicercaProvvSigePerIdFasSigeTipiProvv(lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige(), TIPI_PROVVEDIMENTI);

    setRequestAttribute("provvedimenti", lVect);
    setRequestAttribute("flag_valida", "NO");
    
    // Ricerca dell'eventuale prima impugnazione valida per ciascun provvedimento
    Iterator <ProvvedimentoSigeEventoModel>itx = lVect.iterator();
  	IImpugnazioneSige ctrIS = SIGELookupRemote.getImpugnazioneSigeRemote();
  	Vector <ImpugnazioneSigeModel>impugnazioniProvvedimento = new Vector<ImpugnazioneSigeModel>();
  	Vector <ImpugnazioneSigeModel>impugnazioniProvvedimenti = new Vector<ImpugnazioneSigeModel>();
      while ( itx.hasNext()) {
          ProvvedimentoSigeEventoModel lProvEve = itx.next();
          impugnazioniProvvedimento = ctrIS.ExRicercaImpugnazioniProvvedimentoSige(lProvEve.getProvvedimento().getIdProvvedimentoSige());
          if (impugnazioniProvvedimento != null && impugnazioniProvvedimento.size() >0)
      	      impugnazioniProvvedimenti.addElement((ImpugnazioneSigeModel)impugnazioniProvvedimento.firstElement());
        
          lProvEve.setAllNotified(this.isAllNotified(lProvEve.getProvvedimento().getIdEventoGenerato()));
      }
      setRequestAttribute("impugnazioni", impugnazioniProvvedimenti);
  	// fine Ricerca   

    if (lFasSigeEstMod.getFascicoloSige().getCodStatoFascicolo().equals(FASCICOLO_ARCHIVIATO))
        lmodificabile = "NO";
    else
    {
        // Si controlla la presenza dello scadenzario per definire se modificabili
        IScadenzarioSige lCtrlSc = SIGELookupRemote.getScadenzarioSigeRemote();
        if(lCtrlSc.ExScadutoScadenzarioSigeByIdFascicoloTipo(lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige(), SCADENZARIO_SIGE_IRREVOCABILITA))
          lmodificabile = "NO";
        else
          lmodificabile = "SI";
    }
    setRequestAttribute("modificabile", lmodificabile);
    return PG_ELENCOPROVVEDIMENTI;

  }
  
  private boolean isAllNotified (BigDecimal idEvento) throws F3BException{
	  boolean flag=true;
	  INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();
	  Vector <NotificaModel>lVect = lCtrl.ExRicercaEstesaNotificaByKeyEvento (idEvento);
	  Iterator <NotificaModel> it=lVect.iterator();
	  while (it.hasNext()) {
		  NotificaModel notifica=it.next();
		  if (isAutoritaGiudicante(notifica)) continue;
		  if (notifica.getDataAvvenutaNotifica() == null) {
			  flag=false;
			  break;
		  }
	  }
	  
	  return flag;
  }
  
  private boolean isAutoritaGiudicante (NotificaModel notifica) {
      boolean flag=false;
	  String ufficio=(notifica.getUfficio() != null?notifica.getUfficio().getCodTipoUfficio():"" );
	  if(ufficio.equalsIgnoreCase("GIP")  || ufficio.equalsIgnoreCase("GIPM") 
		 || ufficio.equalsIgnoreCase("GIPMI") || ufficio.equalsIgnoreCase("GIPP")
		 || ufficio.equalsIgnoreCase("GIPPSD") || ufficio.equalsIgnoreCase ("GUP")
		 || ufficio.equalsIgnoreCase("GUPM") || ufficio.equalsIgnoreCase("GUPMI")
		 || ufficio.equalsIgnoreCase("GUPP") || ufficio.equalsIgnoreCase("DIB")
		 || ufficio.equalsIgnoreCase("DIBM") || ufficio.equalsIgnoreCase("TDSM")
		 || ufficio.equalsIgnoreCase("CAP") || ufficio.equalsIgnoreCase("CAS")) {
		  flag=true;
     }
	  
	 return flag; 
  }
  
  
  
}