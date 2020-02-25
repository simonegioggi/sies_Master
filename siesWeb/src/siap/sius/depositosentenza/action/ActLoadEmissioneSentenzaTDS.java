package siap.sius.depositosentenza.action;

import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActLoadEmissioneDecreto;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadEmissioneSentenzaTDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Sentenza UDS
 * </p>
 * Poichè l'azione deve implementare la stessa funzione implementata da ActLoadEmissioneDecreto, viene estesa questa in
 * modo di utilizzare il suo processRequest(). Si sfrutta l'override della funzione generaListaTipi() per differenziare
 * la jsp.
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadEmissioneSentenzaTDS extends ActLoadEmissioneDecreto {

	public String processRequest() throws Exception
  {
    String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
    String lPage = super.processRequest();
    
    if(this.isSessionAttributeNullObj("fascicoloSiusGP")){
      throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
    }

    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    // Controllo esistenza udienza solo in TDS !!
	// MEV10-s3: aggiunta or condition per gestire trib. sorv. minori
	if ("TDS".equals(strCodTipoUfficio) || "TDSM".equals(strCodTipoUfficio)) {
      if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
        throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile emettere una sentenza per il fascicolo " + lFasGPMod.getFascicoloSiusModel().getChiaveAnno() + "/" +  lFasGPMod.getFascicoloSiusModel().getChiaveProgr() + ". Non è stata fissata l'Udienza.");
    }
    
    // L'Emissione della Sentenza può essere emessa solo per i procedimenti con 
    // contenuto:
    // Riabilitazione Speciale per i Minorenni (C047);
    // Revoca Riabilitazione Speciale per i Minorenni (C048);
    // Correzione Errore Materiale (C018).
    if(lFasGPMod.getGeneraleProcedimentoModel() != null 
       && lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento() != null
       && !lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("") ){ 		
        if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("C047") 
    	   || lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("C048")
    	   || lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("C018") )
		{
		    // Contenuto corretto per emettere la Sentenza	
		} else {
			throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile emettere una sentenza per il fascicolo " + lFasGPMod.getFascicoloSiusModel().getChiaveAnno() + "/" +  lFasGPMod.getFascicoloSiusModel().getChiaveProgr() + ". Tipo di provvedimento non disponibile per il contenuto del procedimento.");
		}
    }
    
    setRequestAttribute("flagOrdinanza", "sentenza");

    return lPage;
  }

	// TODO necessaria questa lista ??????
	// Generazione della lista di tipi ordinanza
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List generaListaTipi() {
		List lTipoOrdinanza = null;
		setRequestAttribute("flagOrdinanza", "ordinanza");
		if (DecodificheManager.getInstance().getTipoOrdinanza() == null) {
			lTipoOrdinanza = new ArrayList();
		} else {
			lTipoOrdinanza = new ArrayList(DecodificheManager.getInstance().getTipoOrdinanza());
		}
		lTipoOrdinanza.add(new DecodificheModel("00", "Generazione Automatica", "-", "-", "-", "-", "-", "-", "-"));

		return lTipoOrdinanza;
	}
}