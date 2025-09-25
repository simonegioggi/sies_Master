package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadModificaDesignazioneMagistratoRelatore extends ActionSius
		implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id evento dalla request
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoEventoMotivazioniModel ddemm = idd
				.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(idEvento);

		// Inserisce l'id evento nel model
		ddemm.getEvento().setIdEvento(idEvento);

		String codOggettiTenore = new String();
		String descrOggettiTenore = new String();
		String codDettagliOggetto = new String();
		String codOggettoProcedimento = new String();

		// Preleva i tenori, per il generale procedimento.
		TenoreModel[] tm = fgpm.getTenori();
		for (int i = 0; i < tm.length; i++) {
			if (tm[i] != null) {
				codOggettiTenore += tm[i].getCodOggettoTenore() + "|";
				descrOggettiTenore += tm[i].getDescrOggettoTenore() + "\n";
				if (fgpm.getTenori()[i].getCodDettaglioOggetto() != null
						&& fgpm.getTenori()[i].getCodDettaglioOggetto().length() > 1)
					codDettagliOggetto += fgpm.getTenori()[i].getCodOggettoTenore()
							+ fgpm.getTenori()[i].getCodDettaglioOggetto() + "|";
			}
		}

		// Imposta gli oggetti nella request
		setRequestAttribute("depositoDecretoMotivazioni", ddemm);
		setRequestAttribute("codOggetti", codOggettiTenore);
		setRequestAttribute("descOggetti", descrOggettiTenore);
		setRequestAttribute("codDettagli", codDettagliOggetto);

		// Preleva il cod Oggetto procedimento per poi passarlo come contenuto
		codOggettoProcedimento = fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		// Imposta Contenuto.
		setRequestAttribute("codContenuto", codOggettoProcedimento);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel mrm = imr
				.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", mrm);

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato ia = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = ia
				.ExRicercaAvvocatiByFascicoloNoError(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("avvocato", lAvvocato);

		/* Impostazione della data arrivo in cancelleria */
		String dataArrivoCancelleria;
		if (fgpm.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
			dataArrivoCancelleria = DateUtils.getDateToString(
					fgpm.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd/MM/yyyy");
		else if (fgpm.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
			dataArrivoCancelleria = DateUtils.getDateToString(
					fgpm.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
		else
			dataArrivoCancelleria = DateUtils
					.getDateToString(fgpm.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");
		setRequestAttribute("dataArrivoCancelleria", dataArrivoCancelleria);

		// Pagina di ritorno
		return PG_LOAD_MODIFICA_DESIGNAZIONE_MAGISTRATO_RELATORE;
	}

}