package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActLoadModificaCivilmenteObbligato 
 * Description: Classe Action per la insert del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadModificaCivilmenteObbligato extends ActionSiap
		implements ICostantiPagoPA, ICostantiEvento {

	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String lPage = PG_LOAD_MODIFICA_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		gestioneRitorno();

		// Identificativo della parte
		String idCivilmenteObbligato = getRequestStringParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO);

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();

		CivilmenteObbligatoModel com = ico
				.ExRicercaCivilmenteObbligatoByKey(new BigDecimal(idCivilmenteObbligato));

		setRequestAttribute("civilmenteObbligato", com);
		setRequestAttribute("modalita", "M");

		Option o = new Option(DecodificheManager.getInstance().getSesso(), com.getSesso());
		setRequestAttribute("sesso", "" + o);

		if (com.getRagSociale() != null && !com.getRagSociale().equals("")) {
			o = new Option(DecodificheManager.getInstance().getRagioneSociale(), com.getRagSociale());
		} else {
			o = new Option(DecodificheManager.getInstance().getRagioneSociale(), "-");
		}
		setRequestAttribute("ragioneSociale", "" + o);

		if (com.getCodProvincia() != null && !com.getCodProvincia().equals("")) {
			o = new Option(DecodificheManager.getInstance().getProvincie(), com.getCodProvincia());
		} else {
			o = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		}
		setRequestAttribute("province", "" + o);

		if (com.getCodStatoNascita() != null) {
			o = new Option(DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getNazioni(), "-"), com.getCodStatoNascita());
		} else {
			o = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		}
		setRequestAttribute("nazioni", "" + o);

		if (com.getResidenza() != null && com.getResidenza().getCodStato() != null) {
			o = new Option(DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getNazioni(), "-"), com.getResidenza().getCodStato());
		} else {
			o = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		}
		setRequestAttribute("nazioniResidenza", "" + o);

		// LISTA UFFICI per la Notifica all'Avvocato (solo ufficio UNEP)
		Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "22" };
		Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
		lOptionAvv.setFilter(lStringFilter);
		setRequestAttribute("tipiIstituto", "" + lOptionAvv);

		// LISTA UFFICI per la Notifica all'Avvocato (solo ufficio UNEP)
		String[] lStringFilterSNT = { "-", "22" };
		Option lOptionSNT = new Option(lTipoIstituto);
		lOptionSNT.setFilter(lStringFilterSNT);
		setRequestAttribute("tipiIstitutoSNT", "" + lOptionSNT);

		if (com != null && com.getFlagConvUdienza() != null && com.getFlagConvUdienza().equals("S")) {
			o = new Option(DecodificheManager.getInstance().getFlagSN(), "S");
		} else {
			o = new Option(DecodificheManager.getInstance().getFlagSN(), "N");
		}

		setRequestAttribute("convocazioneUdienza", "" + o);

		// restituisce la jsp di VIEW
		return lPage;
	}

}