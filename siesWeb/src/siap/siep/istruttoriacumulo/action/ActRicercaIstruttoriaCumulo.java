package siap.siep.istruttoriacumulo.action;


/**
* <p>Title: ActRicercaIstruttoriaCumulo</p>
* <p>Description: Classe Action per la ricerca di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActRicercaIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione che recupero l'elenco delle istruttoria per un certo fascicolo
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));
    
    // Recupero l'id dell'istruttoria corrente da passare alla finestra per il 
    // tasto torna indietro
    BigDecimal lIdIstruttoriaCorrente = new BigDecimal(0);
    if (   !isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
        && getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)!=null
       )
    {
      lIdIstruttoriaCorrente = getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lIdIstruttoriaCorrente = "+lIdIstruttoriaCorrente);
    }
    setRequestAttribute("lIdIstruttoriaCorrente", lIdIstruttoriaCorrente);
    
    //==============================================================
    // Imposto la condizione per idfascicolo
    //==============================================================
    IstruttoriaCumuloModel lIstMod = new IstruttoriaCumuloModel() ;
    lIstMod.setFasSieIdFascicoloSiep ( lFascMod.getIdFascicoloSiep() );

    //========================================================= 
    // Istanzio il controller ed effettuo la ricerca    
    //========================================================= 
    IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector <IstruttoriaCumuloModel> lVect = lCtrl.ExRicercaIstruttoriaCumulo (lIstMod);

    if (lVect.size() == 0) { 
      this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
      return IWebConstants.PG_MESSAGE;
    }

    //==========================================================================
    // Per le istruttorie chiuse recupero gli estremi del provvedimento di 
    // cumulo
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    for (int i=0; i<lVect.size(); i++ ) {
      IstruttoriaCumuloModel lIstruttoria = lVect.elementAt (i);
      
      EventoModel lEvento = lCtrlEvento.ExRicercaEventoByKey (lIstruttoria.getEveIdEventoProv());
      
      lIstruttoria.setProvvedimentoCumulo (lEvento);
    }
    
    
    setRequestAttribute("ListaIstruttorieCumulo", lVect);

    return PG_ELENCO_ISTRUTTORIE_CUMULO;

  }
}