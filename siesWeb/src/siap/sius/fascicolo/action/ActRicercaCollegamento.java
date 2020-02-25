package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaCollegamento
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca del Procedimento Collegato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
* @version 1.0
*/

public class ActRicercaCollegamento extends ActionSius implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

      // Si ricava il Fascicolo dalla sessione
      FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

      // Recupero del Codice Tipo Ufficio.
      String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO);
      String lDescrComuneUfficio = getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE);

      // Impostazione della provenienza.
      setRequestAttribute("provenienza", "R");

      // Ricerca Fascicolo da Collegare
      FascicoloGPModel lFasGPPadre = new FascicoloGPModel();
      String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio ) ;

      IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		try {
			lFasGPPadre = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), lCodUfficio);
        // Impostazione della modalità.
        setRequestAttribute("modalita", "M");
		} catch (SIUSException SIUSex) {
			if (SIUSex.getErrorCode() == SIUSException.USER_MESSAGE) {
          setRequestAttribute("provenienza", "FAttenzione. Fascicolo Inesistente!" );
          if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine()!= null) {
            setRequestAttribute("modalita", "I");
					lFasGPPadre = lCtrl.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel()
							.getIdFascicoloSiusOrigine());
          }
        }else{
          // Impostazione della modalità.
          setRequestAttribute("modalita", "I");
          throw SIUSex;
        }
      }
      // Si Imposta il Tipo Ufficio.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius(), lFasGPPadre
				.getFascicoloSiusModel().getCodTipoUfficio(), 46);
      // MEV63 AGGIUNGO GLUI UFFICI PER MINORENNI
      String[] lFilter = {"UDS","TDS", "UDSM", "TDSM"};
      lOption.setFilter(lFilter);
      setRequestAttribute("tipoUfficioSIUS", "" + lOption );
      setRequestAttribute("fascicoloPadre", lFasGPPadre);

      return PG_LOAD_MODIFICACOLLEGAMENTO;
    }

}