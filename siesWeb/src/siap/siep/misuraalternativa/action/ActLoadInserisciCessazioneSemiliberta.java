package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciCessazioneSemiliberta extends ActRevoca implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getRevoca();
		if (!lRitorno.equals(""))
			return lRitorno;

		// setto il campo codice motivo
		// Option lOption = new
		// Option(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiL());

		Collection<DecodificheModel> lOggettoTDS = new Vector(
				DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiL());
		Collection<DecodificheModel> lOggettoTDS51BisSuReclamo = new Vector(
				DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiLTDS51bis());
		Collection<DecodificheModel> lOggettoMDS51bis = new Vector(
				DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMASemiLMDS51bis());

		lOggettoTDS.addAll(lOggettoTDS51BisSuReclamo);

		setRequestAttribute("oggettiMDS", lOggettoMDS51bis); // new!
		setRequestAttribute("oggettiTDS", lOggettoTDS);

		// Collection lOggetti = new Vector (lOggettoTDS);
		// lOggetti.addAll(lOggettoMDS51bis);
		// Option lOption = new Option(lOggetti);
		// setRequestAttribute("motivoProvv", "" + lOption);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		lOption.setFilter("TDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		setRequestAttribute("tipoRevoca", "SEMILIBERTA");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CESSAZIONE;
	}

}