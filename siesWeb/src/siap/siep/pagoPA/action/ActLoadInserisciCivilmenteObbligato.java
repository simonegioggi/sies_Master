package siap.siep.pagoPA.action;

import java.util.Vector;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActLoadInserisciCivilmenteObbligato 
 * Description: Classe Action per la load del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadInserisciCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String lPage = PG_LOAD_INSERISCI_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		// gestioneRitorno();

		// Fascicolo siep in sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String idFascicoloSiep = fsm.getIdFascicoloSiep().toString();
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		// Chiama il controller.
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		if (Utils.isPresent(coms)) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction("siap.siep.pagoPA.action.ActDettaglioCivilmenteObbligato");
			rt.setParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO,
					coms.get(0).getIdCivilmenteObbligato().toString());
			rt.setParameter(CAMPO_ID_FASCICOLO_SIEP, fsm.getIdFascicoloSiep().toString());
			return rt.toString();
		} else {
            // Se fascicolo validato non posso consentire l'inserimento 
            if ("S".equals(fsm.getFlagValidato())) {
                RedirectTo lRedirigi = new RedirectTo();
                lRedirigi.setPage(IWebConstants.PG_MAIN);
                setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Impossibile procedere, il procedimento risulta già validato.");
                lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&"
                    + ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + fsm.getIdFascicoloSiep());
                setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
                return IWebConstants.PG_MESSAGE;                
            }
            else {
    			// aggiungo 2 model vuoti per insert
    			coms.add(new CivilmenteObbligatoModel());
    			// secondo tutore
    			coms.add(new CivilmenteObbligatoModel());
            }
		}

		Option o = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + o);
		setRequestAttribute("sesso_ST", "" + o);

		o = new Option(DecodificheManager.getInstance().getRagioneSocialeCO(), "-");
		setRequestAttribute("ragioneSociale", "" + o);

		o = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		setRequestAttribute("province", "" + o);

		o = new Option(
				DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
				"039");
		setRequestAttribute("nazioni", "" + o);
		setRequestAttribute("nazioniResidenza", "" + o);
		setRequestAttribute("nazioni_ST", "" + o);
		setRequestAttribute("nazioniResidenza_ST", "" + o);

		// TIPO_TUTORE
		o = new Option(DecodificheManager.getInstance().getTipoTutore(), "-");
		setRequestAttribute("tipoTutore", "" + o);

		// Imposta Modalità Inserimento.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("civilmenteObbligati", coms);
		setRequestAttribute("idFascicoloSiep", "" + idFascicoloSiep);

		// restituisce la jsp di VIEW
		return lPage;
	}

}