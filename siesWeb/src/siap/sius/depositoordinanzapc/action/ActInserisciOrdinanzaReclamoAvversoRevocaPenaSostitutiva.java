package siap.sius.depositoordinanzapc.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.util.SIUSLookupRemote;

/**
 * Inserimento dell'ordinanza di Reclamo Avverso Revoca Pena Sostitutiva n.b inserisce un nuovo record su
 * ESECUZIONE_SANZ_SOST con i dati inseriti in form
 *
 * @since MEV_2023-35
 * @author sgioggi
 */
public class ActInserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva extends ActInserisciOrdinanzaUDS {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		siesLogger.debug("ActInserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.processRequest()");
		return super.processRequest();
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		// info per il log
		siesLogger.debug("ActInserisciOrdinanzaReclamoAvversoRevocaPenaSostitutiva.inserimento()");
		// Dati relativi alla esecuzione sanz sost.
		String[] esitiTenore = this.getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);
		boolean isReclamo = false;
		for (int i = 0; i < esitiTenore.length; i++) {
			siesLogger.debug("lEsitoTenore = " + esitiTenore[i]);
			if ("3127".equals(esitiTenore[i])) {
				siesLogger.debug("esito 'Accoglie reclamo e converte in altra pena sostitutiva',"
						+ " procedo a registrare EsecuzioneSanzioneSostitutivaModel");
				isReclamo = true;
			}
		}

		EsecuzioneSanzioneSostitutivaModel essm = null;
		if (isReclamo) {
			siesLogger.debug("isReclamo");
			essm = new EsecuzioneSanzioneSostitutivaModel();
			essm.setAnnoS07(mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1());
			essm.setProgrS07(mFasGPMod.getGeneraleProcedimentoModel().getProgrS1());
			essm.setCodTipoSanzione(getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE));
			essm.setNumAnniSanzione(
					getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE));
			essm.setNumMesiSanzione(
					getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE));
			essm.setNumGiorniSanzione(
					getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE));
			essm.setGenPridGeneraleProcedimento(
					aOrdEveTenGP.getGeneraleProcedimento().getIdGeneraleProcedimento());
			essm.setDepOpidDepositoOrdinanzaPc(aOrdEveTenGP.getOrdinanza().getIdDepositoOrdinanzaPc());
			essm.setCodOperatoreInserimento(aOrdEveTenGP.getEvento().getCodOperatoreInserimento());
			essm.setCodUfficioInserimento(aOrdEveTenGP.getEvento().getCodUfficioInserimento());
			essm.setDataInserimento(aOrdEveTenGP.getEvento().getDataInserimento());
		}

		// inserimento
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriGProcModel oetgpm = idopc.ExInserisciOrdinanzaRevocaPS(aOrdEveTenGP, essm);

		if (oetgpm == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO.");

		// valore di ritorno
		return oetgpm;
	}

}