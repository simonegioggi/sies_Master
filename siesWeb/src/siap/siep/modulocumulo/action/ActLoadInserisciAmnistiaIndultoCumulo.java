package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load del form di Inserimento/Modifica dei provvedimenti di annotazione Amnistia / Indulto
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciAmnistiaIndultoCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    StatoEsecTitoloCumulatoModel lStato = null;
    
    String lModalita = "I"; //default inserimento
    if (!isRequestParameterNullObj("modalita")) 
      lModalita = getRequestStringParameter("modalita");
    
    
    String lIdCompDaModificare = "";
    
    if ("I".equals(lModalita)){
      // Inserimento
      siesLogger.debug("Sto In Inserimento");
		} else if ("M".equals(lModalita) || "NP".equals(lModalita)) {
      // Modifica 
			BigDecimal lIdStat = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
      
      if ("M".equals(lModalita))
				lIdCompDaModificare = getRequestStringParameter(
						ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO);
      
			siesLogger.debug("Sto In modifica (" + lModalita + "), idStat = " + lIdStat + ", lIdComp = "
					+ lIdCompDaModificare);
      
      // Recupero i dati e li passo alla form
      IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
      
      lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull (lIdStat);  
      setRequestAttribute("aProvvedimento", lStato );
      setRequestAttribute("aIdComputo", ""+lIdCompDaModificare );
		} else {
      //Rilanciare Eccezione - Operazione non supportata
    }
    
    
    
    //==========================================================================
    // Caricamento Dati delle Combo
    //==========================================================================

  // To Do : Caricare la combo per i DPR!!!
  
    Option lOption = new Option (DecodificheManager.getInstance().getTipoUfficioPerCodice());
    String desLuogoUfficio = "";
		lOption.setFilter(new String[] { "CAP", "CAS", "CASAP", "CSS", "GIP", "GIPM", "GUPM", "GUP", "TRIBSD",
				"CAPSM", "DIB", "DIBM" });
    
    ComputiCumuloModel lComputo = null;
		if ("M".equals(lModalita)) {
      BigDecimal idComp = new BigDecimal (lIdCompDaModificare);
      
      Vector <ComputiCumuloModel> lListaComputi = lStato.getListaComputi();
      Iterator itxComputi = lListaComputi.iterator();
			while (itxComputi.hasNext()) {
        lComputo = (ComputiCumuloModel) itxComputi.next();
        if (lComputo.getIdComputiCumulo().compareTo(idComp)==0){
          break;
        }
      }
     
			if (lComputo.getCodUfficioEmittenteProvv() != null) {
				UfficioModel lUfficioEmittente = getUfficioByCodUfficio(
						lComputo.getCodUfficioEmittenteProvv());
        lOption.setSelected(lUfficioEmittente.getCodTipoUfficio());
        desLuogoUfficio=lUfficioEmittente.getDescrComune();
      }
    }    
    
    setRequestAttribute("tipoUfficioEmittente","" + lOption);
    setRequestAttribute("luogoUfficioEmittente","" + desLuogoUfficio);

    //==========================================================================
    // Caricamento combo specifiche della pagina di inserimento Amnistia/Indulto.
    // Posizionamento combo Tipo beneficio su Indulto.
    //==========================================================================
		Option lOptionTipoAnn = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
    if ( "M".equals(lModalita) && lComputo!=null)
      lOptionTipoAnn.setSelected (lComputo.getCodTipoAnnotazione());
    else 
      lOptionTipoAnn.setSelected ("002"); //Indulto
    setRequestAttribute("TipoAnnotazioneManuale", "" + lOptionTipoAnn );

    //==========================================================================
    // Caricamento Combo DPR
    //==========================================================================
    lOption  = new Option( DecodificheManager.getInstance().getDPR());
    // 27/10/2006 Aggiunto posizionamento combo DPR all'ultimo elemento.
    if ( "M".equals(lModalita) && lComputo!=null)
      lOption.setSelected (lComputo.getCodDpr());
    else {
      Vector lVect = (Vector)DecodificheManager.getInstance().getDPR();
      DecodificheModel lDecMod = (DecodificheModel)lVect.lastElement();
      lOption.setSelected(lDecMod.getCode());
    }
    setRequestAttribute("listaDPR", "" + lOption );

    //===============================================
    // Reati Cumulati
    //===============================================
    siesLogger.debug("Ricerca reati");
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
    ReatoCumuloModel lReaCumMod = new ReatoCumuloModel(); 
    lReaCumMod.setTitIdTitoloCumulato (lIdTitolo);
    IReatoCumulo lCtrlR = SIEPLookupRemote.getReatoCumuloRemote();
    Vector<ReatoCumuloModel> lReaVect = lCtrlR.ExRicercaReatoCumulo(lReaCumMod);
    
		for (int i = 0; i < lReaVect.size(); i++) {
      lReaCumMod = lReaVect.get(i);
      siesLogger.debug("Reato Cumulo Model = "+i+ " "+lReaCumMod);
    }
    setRequestAttribute("reati", lReaVect);
    
    // Imposta Modalità.
    setRequestAttribute("modalita", lModalita);

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_INSERISCI_AMNISTIA_INDULTO_CUMULO; 
  }
  
}