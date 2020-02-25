package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action per l'inserimento/modifica dei dati finali Cumulo. 
 * 
 * @author 
 */
public class ActLoadInserisciDatiFinaliCumulo extends ActionModuloCumulo implements ICostantiDatiFinaliCumulo
{
  /**
   * Recupera i dati da visualizzare in maschera
   */
	
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
		
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiFinaliAggregati = super.getDatiFinaliCumuloAggregato();
    
    BigDecimal lIdIstruttoria = getIdIstruttoria();

    
    //Caricamento combo
    Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
    //lOptionAutorita.setFilter(new String[] {"-", "CAP", "CAS", "CASAP", "GIP", "GUP", "TRIBSD", "DIB"});
    lOptionAutorita.setFilter(new String[] {"-", "CAP", "CAS", "CASAP", "GIP", "GUP", "DIB"});
    setRequestAttribute("comboAutorita",""+lOptionAutorita);
    
    // 30/04/2019 MEV70 Impostazione attributo per il caso di Titoli doppi in Istruttoria.
    IIstruttoriaCumulo lCtrlIC = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    Vector<TitoloCumulatoModel> lTitoli = lCtrlIC.titoloDoppioInIstruttoria(lIstruttoriaModel.getIdIstruttoriaCumulo() ) ;

    String strTitoloDoppio = "";
    ArrayList<String> lTitoliDoppi = new ArrayList<String>();
    if (lTitoli!=null)  {
    	for (int i=0; i<lTitoli.size(); i++) {
    		TitoloCumulatoModel lTitolo = lTitoli.get(i);
    		strTitoloDoppio = " - "+lTitolo.getDescrTipoProvvedimento()+" N. "+lTitolo.getNumeroSentenza()+"/"+lTitolo.getAnnoSentenza()+" del "+
    					  DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy") +" Emessa da "+ lTitolo.getDescrTipoAutoritaEmittente()+" di "+lTitolo.getDescrLuogoEmittente() ;
    		lTitoliDoppi.add(strTitoloDoppio);
    	}
    }
    setRequestAttribute("titoliDoppi",lTitoliDoppi);
    
    //==========================================================================
    // Affettua la ricerca se già presenti dati finali cumulo
    //==========================================================================
    if (!isRequestParameterNullObj(MODALITA) && MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))){
      // provengo del dettaglio e voglio andare in modifica
    	siesLogger.debug("sono in modifica");
      
      if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
        setRequestAttribute(MODALITA, MODALITA_MODIFICA);
        
        return PG_LOAD_INSERISCI_DATI_FINALI;
      }
      else {
        //Istruttoria Chiusa o Annullata, comunque non modificabile
        RedirectTo lRedirigi = new RedirectTo();

        lRedirigi.setPage(IWebConstants.PG_MAIN);
        setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'istruttoria non risulta Aperta. Impossibile Modificare i dati." );
        //lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
        lRedirigi.setAction( "siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo");
        
        setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
        
        lRedirigi.setParameter(ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO, "" + lDatiFinaliAggregati.getDatiFinaliCumulo().getIdDatiFinaliCumulo());
        lRedirigi.setParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO, "" + this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
        return IWebConstants.PG_MESSAGE;
        //throw new F3BException(F3BException.USER_MESSAGE,"L'istruttoria non risulta Aperta.");
      }
    }
    else {
      // Provengo dalla griglia Istruttoria
    	siesLogger.debug("Provengo dalla griglia Istruttoria");
      
      IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
      
      DatiFinaliCumuloModel lDatiFinaliCum = lCtrl.ExRicercaDatiFinaliCumuloByIdIstrutt(lIdIstruttoria);      
      
      if (lDatiFinaliCum!=null) {
        // carico il dettaglio
    	  siesLogger.debug("carico il dettaglio");
        String lPage = "";
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo" 
                + "&"+ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO +"="+lDatiFinaliCum.getIdDatiFinaliCumulo()
                + "&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

        return lPage;       
      }
      else{
        //carico la form di inserimento
    	  siesLogger.debug("Provengo dalla griglia ma dati non presenti, vado in insert");

        if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())){
          // Preseleziono il GE del Titolo
          FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute ("fascicolo");
          SentenzaModel lSentMod = lFascicolo.getSentenza();
          lOptionAutorita.setSelected (lSentMod.getCodTipoAutoritaEmittente());
          setRequestAttribute("comboAutorita",""+lOptionAutorita);
          
          setRequestAttribute("descrLuogoGE",""+lSentMod.getDescrLuogoEmittente());
          
          setRequestAttribute(MODALITA, MODALITA_INSERIMENTO);
          return PG_LOAD_INSERISCI_DATI_FINALI;
        }
        else {
          //Probabilmente Istruttoria annullata i dati finali non sono presenti
          RedirectTo lRedirigi = new RedirectTo();

          lRedirigi.setPage(IWebConstants.PG_MAIN);
          setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Dati Finali non presenti. L'istruttoria non risulta Aperta. Impossibile Inserire ulteriori dati." );
          lRedirigi.setAction( "siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo");
          
          lRedirigi.setParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO, "" + this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
          
          setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
          return IWebConstants.PG_MESSAGE;
          //throw new F3BException(F3BException.USER_MESSAGE,"L'istruttoria non risulta Aperta.");
        }
      }      
    }

    
  }
}
