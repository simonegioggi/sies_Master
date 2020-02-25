package siap.siep.modulocumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadComunicazioniCancellerie extends ActionModuloCumulo implements ICostantiModuloCumulo {

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiFinaliCumulo = super.getDatiFinaliCumuloAggregato();

		if (lDatiFinaliCumulo.getProvvedimentoCumulo() == null
				|| lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Nessuna Provvedimento di cumulo associato all'istruttoria corrente. Impossibile inserire le comunicazioni");
		} else if ("A".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Provvedimento di Cumulo risulta annullato. Impossibile inserire le comunicazioni");
		} else if (!"S".equals(
				lDatiFinaliCumulo.getProvvedimentoCumulo().getEvento().getFlagDocumentoRegistrato())) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Provvedimento di cumulo non risulta validato. Impossibile inserire le comunicazioni");
		}

		// ==========================================================================
		// Recupero le comunicazioni già inviate per l'il provvedimento di cumulo/istruttoria
		// ==========================================================================
		String lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();

		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector<TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
				lIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);
		setRequestAttribute("ListaTitoli", lListaTitoli);

		return PG_GRIGLIA_COMUNICAZIONI_CANCELLERIE;
	}

}