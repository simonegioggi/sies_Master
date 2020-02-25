package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * Action Load di inserimento del provvedimento di cessazione dell'Indultino L 207/2003
 *
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciCessazioneIndultino extends ActRevoca implements ICostantiMisuraAlternativa {

	/**
	 * Per questa action si passa due volte. Una proma volta in fase di inserimento del provvedimento della
	 * Sorveglianza + calcolo pena, e una seconda volta in fase di inserimento del provvedimento di esecuzione
	 * richiamata dalla ActInserisciCessazioneMAAffProv
	 */
	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getRevocaConCalcolo();
		if (!lRitorno.equals(""))
			return lRitorno;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		MisuraAlternativaModel lrevoca = null;
		if (!isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_SIUS)) {
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}

		/*
		 * ricerca misura alternativa sospesa deve assolutamente cercare l'ultima prima di entrare nella
		 * revoca. Inoltre se esiste la misura di revoca chiamata lrevoca devo cercare una misura precedente
		 * di tipo sospensione affidamento mentre se lrevoca non esiste vuol dire che è la prima volta che ci
		 * passo e quindi cerco la corrente misura.
		 */
		MisuraAlternativaModel lMisAlModSospesa = null;
		if (lrevoca != null && lrevoca.getIdMisuraAlternativa() != null) {
			UfficioModel lUffMod = new UfficioModel();
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lrevoca.getChiaveUfficioFascicoloSius());

			setRequestAttribute("UfficioEmittente", lUffMod);
			setRequestAttribute("misuraalternativa", lrevoca);

			// la seconda volta che ci passo la revoca esite!
			IMisuraAlternativaIndultino lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
			lMisAlModSospesa = lMisCtrl
					.ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		} else {
			// la prima volta che ci passo la revoca non esite!
			lMisAlModSospesa = lMisAltCtrl
					.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		}

		// istanzio un nuovo di tipo per concessione per sapere se
		// l'ultima misura in questione è una concessione
		MisuraAlternativaModel lMisAlModConcessa = null;
		if (lMisAlModSospesa != null)
			lMisAlModConcessa = new MisuraAlternativaModel(lMisAlModSospesa);

		if (lMisAlModSospesa != null && lMisAlModSospesa.getIdMisuraAlternativa() != null) {
			if (lMisAlModSospesa.getCodTipoDecisione() == null
					|| !lMisAlModSospesa.getCodTipoDecisione().equals("02")
					|| lMisAlModSospesa.getCodNaturaDecisione() == null
					|| !lMisAlModSospesa.getCodNaturaDecisione().equals("SP")
					|| lMisAlModSospesa.getCodTipoMisura() == null
					|| !lMisAlModSospesa.getCodTipoMisura().equals("2280")) {
				lMisAlModSospesa = null;
			}
		}
		setRequestAttribute("misurasospesa", lMisAlModSospesa);

		// Ricerco se presente l'Ordinanza di Concessione
		if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
			if (lMisAlModConcessa.getCodTipoDecisione() == null
					|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
					|| lMisAlModConcessa.getCodNaturaDecisione() == null
					|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
					|| lMisAlModConcessa.getCodTipoMisura() == null
					|| !lMisAlModConcessa.getCodTipoMisura().equals("2245")) {
				lMisAlModConcessa = null;
			}
		}

		setRequestAttribute("misuraconcessa", lMisAlModConcessa);

		// setto il campo codice motivo
		// Option lOption = new
		// Option(DecodificheManager.getInstance().getMotivoProvvedimentoCessazioneMAIndultino());
		// setRequestAttribute("motivoProvv", "" + lOption);
		Collection<DecodificheModel> lOggettoTDS = DecodificheManager.getInstance()
				.getMotivoProvvedimentoCessazioneMAIndultino();
		Collection<DecodificheModel> lOggettoMDS51bis = DecodificheManager.getInstance()
				.getMotivoProvvedimentoCessazioneMAIndultino();

		setRequestAttribute("oggettiMDS", lOggettoMDS51bis); // new!
		setRequestAttribute("oggettiTDS", lOggettoTDS);

		setRequestAttribute("tipoRevoca", "INDULTINO");

		// Sovrascrivo il tipo ufficio caricando solo il TDS
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		lOption.setFilter("TDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CESSAZIONE_AFF_PROV;
	}

}