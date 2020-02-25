package siap.sius.udienza.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

public class ActElencoNuoveUdienze extends ActionSiap implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		GeneraleProcedimentoModel aGeneraleProcedimento = new GeneraleProcedimentoModel();
		aGeneraleProcedimento.setIdGeneraleProcedimento(lFasGPMod.getGeneraleProcedimentoModel()
				.getIdGeneraleProcedimento());
		aGeneraleProcedimento.setUdiIdUdienza(lFasGPMod.getGeneraleProcedimentoModel().getUdiIdUdienza());

		// Popola il model UdienzaModel con la data prelevata dalla
		// di sistema.
		UdienzaModel lUdienzaMod = new UdienzaModel();
		// lUdienzaMod.setDataUdienza(DateUtils.getSysDate());
		lUdienzaMod.setDataUdienza(lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio());

		// Chiama la RemoteInterfacce del controller udienza.
		IUdienza lCtrlUdienza = SIUSLookupRemote.getUdienzaRemote();

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// Invoca il metodo della ricerca udienze legate allo stesso colleggio dell'udienza corrente.
		// Come argomento al metdod del controllo si passa il numero di
		// occorrenze che si desidera visuliazzare, in questo caso le prime 30.

		Vector lUdienze = new Vector();
		lUdienze = lCtrlUdienza.ExRicercaNuoveUdienze(lUdienzaMod, aGeneraleProcedimento);

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("udienze", lUdienze);

		// Ritorna la View Jsp
		return PG_ELENCOUDIENZE;
	}

}