package siap.sige.impugnazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasmissioneImpugnazioneSige </p>
 * <p>Description: Trasferisce il provvedimento (compreso l'impugnazione) verso la Procura</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasmissioneImpugnazioneSige extends ActionSiap implements ICostantiImpugnazioneSige {
 
 @SuppressWarnings ("unchecked") 
 public String processRequest() throws Exception {

	BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);  
	setRequestAttribute("IdEvento", "" + lEveId);
	
	// Si prelevano dalla request i parametri del Ricorso appena iscritto/Letto.
    BigDecimal lImpId = getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE);
    String lPage = PG_LOAD_TRASMISSIONE_IMPUGNAZIONE;
    String lCodTipoImpugnazione = "";
    IImpugnazioneSige ictrl = SIGELookupRemote.getImpugnazioneSigeRemote();
    ImpugnazioneSigeModel impugnazione=ictrl.ExRicercaImpugnazioneByKey(lImpId);
    
    FascicoloSigeEstesoModel lFasEstMod = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
      
    //if(!isSessionAttributeNullObj("codTipoImpugnazione"))
    //  	lCodTipoImpugnazione = (String) getSessionAttribute("codTipoImpugnazione");
    if(impugnazione != null && impugnazione.getCodTipoImpugnazione() != null){
    	lCodTipoImpugnazione = impugnazione.getCodTipoImpugnazione();
    }

    // Recupero degli uffici Interessati all'Impugnazione.
   // Se l'impugnazione riguarda una Misura Alternativa o il Proc. SIUS è Unificante o esistono "RIFASIEP", possono esistere più uffici interessati all'impugnazione!
    IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
    //Vector lUfficiInteressati = lUctrl.ListaUfficiInteressatiProvvedimento(lFasEstMod.getFascicoloSige().getIdFascicoloSige(), lFasEstMod.getFascicoloSige().getNumeroFascicoliUnificati());
    // MEV_39 03/01/2018 modificata firma del metodo
    Vector <UfficiProvvedimentoModel>lUfficiInteressati = lUctrl.ListaUfficiInteressatiProvvedimento(lFasEstMod.getFascicoloSige().getIdFascicoloSige(), new BigDecimal(0), null);
    setRequestAttribute("ufficiInteressati", lUfficiInteressati );
    
    Option lOption = null;
    // Nel caso di una Opposizione convertita in Ricorso, il destinatario deve essere la
    // Corte Suprema di cassazione
    if(impugnazione != null && impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione") ){
	    lOption = new Option( DecodificheManager.getInstance().getTipoUfficio());
	    String[] lFilterCSS = {"CSS"};
	    lOption.setFilter( lFilterCSS );
    } else {
	    lOption = new Option(DecodificheManager.getInstance().getAutoritaCompetente());
	    lOption.setFilter(new String[]{"-","PGCAP","PM","PMM"});
	    lOption.setSelected("-");
    }
    
    super.setRequestAttribute("provvedimento", impugnazione.getProvvedimentoSige().getProvvedimento());
    setRequestAttribute("uffici", "" + lOption);
    setRequestAttribute ("impugnazione",impugnazione);
      
      // lPage = ICostantiProvvedimentoSige.PG_LOAD_TRASFERISCI_PROVVEDIMENTO;

    if (lCodTipoImpugnazione.compareTo(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE)==0)
      setRequestAttribute("postTitle","Trasferimento Opposizione Provvedimento");
    else
      setRequestAttribute("postTitle","Trasferimento Ricorso Provvedimento");
    
    setRequestAttribute("codTipoImpugnazione", lCodTipoImpugnazione);

    return lPage;
  }
}
