package siap.siep.modulocumulo.action;


/**
* <p>Title: ActLoadInserisciPenaAccessoriaCumulo</p>
* <p>Description: Classe Action per la load inserisci di PeneAccessorieCumulo</p>
*/

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

public class ActLoadInserisciPenaAccessoriaCumulo extends ActionModuloCumulo implements ICostantiPenaAccessoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    //==========================================================================
    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
// Vediamo se deve Inserire o Modificare : SE parametro "modalita" = NULL, allora  è INSERIMENTO
    String lInsMod = "";
    if(isRequestParameterNullObj("modalita") )
    	lInsMod = "I";
    else
		lInsMod = getRequestStringParameter("modalita");
    
    //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.debug("--XX-- ActLoadInserisciPenaAccessoriaCumulo - modalita = "+lInsMod);
    
	 // chiama il controller
	 IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
	 PenaAccessoriaCumuloModel lPenCumMod = null;
	 
	 if(lInsMod.compareTo("M") == 0 )
	 {
		 BigDecimal lIdPenaCum = getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA_CUMULO);
		 lPenCumMod = (PenaAccessoriaCumuloModel)lCtrl.ExRicercaPenaAccessoriaCumuloByKey(lIdPenaCum);
		 setRequestAttribute("penaAccessoriaCumulo", lPenCumMod);
	 }
	 
    //==========================================================================
    // Caricamento Dati delle Combo
    //==========================================================================
    Option lOption = new Option( DecodificheManager.getInstance().getTipoPeneAccessorie());
    if(lPenCumMod != null && lPenCumMod.getCodTipoPenaAccessoria() != null )
    {
    	//lOption.setSelected(lPenCumMod.getDescrTipoPenaAccessoria());
    	lOption.setSelected(lPenCumMod.getCodTipoPenaAccessoria());
    }
    setRequestAttribute("TipoPenaAccessoria", "" + lOption );

    lOption  = new Option( DecodificheManager.getInstance().getDurataPeneAccessorie() );
    if(lPenCumMod != null && lPenCumMod.getDurata() != null )
    {
    	//lOption.setSelected(lPenCumMod.getDescrDurata());
    	lOption.setSelected(lPenCumMod.getDurata());
    }
    setRequestAttribute("DurataPeneAccessorie", "" + lOption );
    
    // Imposta Modalità.
    setRequestAttribute("modalita", lInsMod);
    
	 BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
	 
	 String lPage="";
	 if(lInsMod.compareTo("C")==0)
	 {
		 BigDecimal lIdPenaCum = getRequestBigDecimalParameter(CAMPO_ID_PENA_ACCESSORIA_CUMULO);
		 lPenCumMod = (PenaAccessoriaCumuloModel)lCtrl.ExRicercaPenaAccessoriaCumuloByKey(lIdPenaCum);
		 // Cancellazione
		 lPenCumMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
		 lPenCumMod.setMotivoModifica( getRequestStringParameter( ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA) );

		 lPenCumMod.setCodOperatoreAggiornamento ( getCodUtenteConnesso());
		 lPenCumMod.setDataAggiornamento         ( DateUtils.getSysDate());
		 lPenCumMod.setCodUfficioAggiornamento   ( getCodUfficioUtenteConnesso());
		 
		 lCtrl.ExCancellaPenaAccessoriaCumulo(lPenCumMod);
		 
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaPeneAccessorieCumulo&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;

	 }	 
	 else
	 {	 
		 lPage = PG_LOAD_INSERISCI_PENEACCESSORIE_CUMULO;
	 }	

    // Restituisce la pagina di Inserimento dei Dati 
    return lPage; 
  }
}