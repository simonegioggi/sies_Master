package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * Action che gestisce il caricamente delle form di inserimento/modifica delle Revoche
 * Benefici Disposti in Sentenza (titolo CUMULATO)
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciRevocaBeneficioCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo
{

  
	public String processRequest() throws F3BException {
	    //==========================================================================
	    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
	    // di DettaglioTitoloCumulato.jsp
	    //==========================================================================
	    super.getDatiIstruttoria();
	    super.getDatiTitoloCumulato();
	    BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
	    
	    // TIPO_FORM_BENEFICIO 	= 01(TIPO_FORM_REVOCA_SOSPENSIONE) se inserisco o modifico Revoca si Sospensione o Non menzione
	    //					 	= 02(TIPO_FORM_REVOCA_INDULTO) se inserisco o modifico Revoca di Indulto o Amnistia
	    String lTipoForm = getRequestStringParameter(TIPO_FORM_BENEFICIO);
	    
	    // MODALITA_INSERIMENTO = 'I' per INSERIMENTO, 'M' per MODIFICA, 'C' per CANCELLAZIONE
	    String lModalita = MODALITA_INSERIMENTO;
	    
	    if (!isRequestParameterNullObj(MODALITA) && !"".equals(getRequestStringParameter(MODALITA)) )
	    	lModalita = getRequestStringParameter(MODALITA);
	
	    setRequestAttribute(MODALITA, lModalita);
	    
	    IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
	    BeneficioCumuloModel lBeneficioCumulo = null;
//=================================================================================
// Se siamo in MODIFICA, recuperiamo i dati del Beneficio (Revoca) da modificare,	    
//=================================================================================
	    if (lModalita.equals(MODALITA_MODIFICA))
	    {
	    	BigDecimal idBeneficioCumulo = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
	    	lBeneficioCumulo = (BeneficioCumuloModel)lCtrl.ExRicercaBeneficioCumuloByKey(idBeneficioCumulo);
	    	 
	    	setRequestAttribute("beneficioCumulo", lBeneficioCumulo);
	    	
	    }
	    
	    //=================================================================================================
	    // Caricamento combo da visualizzare nella form (Revoca Sospensione/Non Menzione o Revoca Indulto)
	    //=================================================================================================
	    	Option lOptionAuto  = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
	    	if(lBeneficioCumulo!=null && lBeneficioCumulo.getIdBeneficioCumulo()!=null)
	    		if(lBeneficioCumulo.getRifCodTipoAutoEmittente()!= null)
	    			lOptionAuto.setSelected(lBeneficioCumulo.getRifCodTipoAutoEmittente());

	    	setRequestAttribute("AutoritaEmittente", "" + lOptionAuto );
	    //---	
	    	Option lOptionProv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(), "01");

	    	if(lTipoForm.equals(TIPO_FORM_INDULTO))
		    	lOptionProv.setFilter( new String[] {"01", "02", "03"} );
		    else if(lTipoForm.equals(TIPO_FORM_SOSPENSIONE))
		    	lOptionProv.setFilter( new String[] {"01", "02"} );
	
	    	if(lBeneficioCumulo!=null && lBeneficioCumulo.getIdBeneficioCumulo()!=null)
	    		if(lBeneficioCumulo.getRifCodTipoProvvedimento()!=null)
	    			lOptionProv.setSelected(lBeneficioCumulo.getRifCodTipoProvvedimento());
	    		
	    	setRequestAttribute("TipoProvvedimento", "" + lOptionProv );   
	    //
	    //==================================================================================
	    // Caricamento combo da visualizzare in form SOLO per Revoca Indulto 
	    //==================================================================================
	    if (lTipoForm.equals(TIPO_FORM_INDULTO)) 
	    {	    	
	    	Option lOptionIndu  = new Option( DecodificheManager.getInstance().getTipoBeneficio(),"03");
	    	if(lBeneficioCumulo!=null && lBeneficioCumulo.getIdBeneficioCumulo()!=null)
	    		if(lBeneficioCumulo.getCodTipoBeneficio()!=null)
	    			lOptionIndu.setSelected(lBeneficioCumulo.getCodTipoBeneficio());
	    		  
	    	setRequestAttribute("tipoBeneficio", "" + lOptionIndu );
	    //----	
	    	Option lOptionDpr  = new Option( DecodificheManager.getInstance().getDPR(),"24");
	    	if(lBeneficioCumulo!=null && lBeneficioCumulo.getIdBeneficioCumulo()!=null)
	    		if(lBeneficioCumulo.getCodDpr()!=null)
	    			lOptionDpr.setSelected(lBeneficioCumulo.getCodDpr());
	    	  
	    	setRequestAttribute("listaDPR", "" + lOptionDpr );
	    }
	    
		 String lPage="";
		 setRequestAttribute(TIPO_FORM_BENEFICIO, lTipoForm);
		 
		 if(lModalita.compareTo(MODALITA_CANCELLA)==0)
		 {	
			 BigDecimal idBeneficioCumulo = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
			 BeneficioCumuloModel lBeneficioRevoca = (BeneficioCumuloModel)lCtrl.ExRicercaBeneficioCumuloByKey(idBeneficioCumulo);
			 // Cancellazione
			 lBeneficioRevoca.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
			 lBeneficioRevoca.setMotivoModifica( getRequestStringParameter( ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA) );
	
			 lBeneficioRevoca.setCodOperatoreAggiornamento ( getCodUtenteConnesso());
			 lBeneficioRevoca.setDataAggiornamento         ( DateUtils.getSysDate());
			 lBeneficioRevoca.setCodUfficioAggiornamento   ( getCodUfficioUtenteConnesso());
			 
			 lCtrl.ExCancellaBeneficioCumuloRevoca(lBeneficioRevoca);
			 
			 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaRevocheBeneficiCumulo&" + ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;
		 }	 
		 else
		 {	 // Inserimento/Modifica
			 if (lTipoForm.equals(TIPO_FORM_SOSPENSIONE))
				 lPage = PG_LOAD_INSERISCI_REVOCA_BENEFICIO_CUMULO;
			 else if(lTipoForm.equals(TIPO_FORM_INDULTO))
				 lPage = PG_LOAD_INSERISCI_REVOCA_BENEFICIO_INDULTO_CUMULO;
		 }	
	    
		 return lPage;
		 
	 }	// Chiude ProcessRequest()
  
} // Chiude ActLoadInserisciBeneficiCumulo()
