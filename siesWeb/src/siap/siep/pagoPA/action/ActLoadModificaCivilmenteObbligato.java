package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
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
		String retPage = PG_LOAD_INSERISCI_CIVILMENTE_OBBLIGATO;

		// Gestione pulsante di ritorno
		gestioneRitorno();

		// Identificativo del fascicolo siep agganciato
		String idFascicolSiep = getRequestStringParameter(CAMPO_ID_FASCICOLO_SIEP);
		setRequestAttribute("idFascicolSiep", "" + idFascicolSiep);

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();

		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(new BigDecimal(idFascicolSiep));
		// gestione secondo tutore
		if (coms.size() == 1)
			coms.add(new CivilmenteObbligatoModel());

		setRequestAttribute("civilmenteObbligati", coms);
		setRequestAttribute("modalita", "M");

		Iterator<CivilmenteObbligatoModel> iter = coms.iterator();
		String st = "";
		int i = 0;
		while (iter.hasNext()) {
			CivilmenteObbligatoModel com = iter.next();
			if (i != 0)
				st = "_ST";
			Option o = new Option(DecodificheManager.getInstance().getSesso(),
					Utils.isPresent(com.getSesso()) ? com.getSesso() : "M");
			setRequestAttribute("sesso" + st, "" + o);

			if (i == 0) {
				if (Utils.isPresent(com.getRagSociale())) {
					o = new Option(DecodificheManager.getInstance().getRagioneSocialeCO(),
							com.getRagSociale());
				} else {
					o = new Option(DecodificheManager.getInstance().getRagioneSocialeCO(), "-");
				}
				setRequestAttribute("ragioneSociale", "" + o);

				if (Utils.isPresent(com.getCodProvincia())) {
					o = new Option(DecodificheManager.getInstance().getProvincie(), com.getCodProvincia());
				} else {
					o = new Option(DecodificheManager.getInstance().getProvincie(), "-");
				}
				setRequestAttribute("province", "" + o);
			}

			if (Utils.isPresent(com.getCodStatoNascita())) {
				o = new Option(DecodificheUtils.getDecodesWithoutCode(
						DecodificheManager.getInstance().getNazioni(), "-"), com.getCodStatoNascita());
			} else {
				o = new Option(DecodificheUtils
						.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
			}
			setRequestAttribute("nazioni" + st, "" + o);

			if (com.getResidenza() != null && Utils.isPresent(com.getResidenza().getCodStato())) {
				o = new Option(DecodificheUtils
						.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
						com.getResidenza().getCodStato());
			} else {
				o = new Option(DecodificheUtils
						.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
			}
			setRequestAttribute("nazioniResidenza" + st, "" + o);
			i++;
		}

		// TIPO_TUTORE
		String tipoTutore = coms.get(0).getCodTutore();
		Option o = new Option(DecodificheManager.getInstance().getTipoTutore(), tipoTutore);
		setRequestAttribute("tipoTutore", "" + o);

		// COD_PERSONA
		String codPersona = coms.get(0).getCodPersona();
		setRequestAttribute("codPersona", "" + codPersona);

		// restituisce la jsp di VIEW
		return retPage;
	}

}