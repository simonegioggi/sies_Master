package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadInserisciInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		/*
		 * questa funzione serve a caricare la maschera per l'inserimento della data di inizio della
		 * esecuzione sanzione sostitutiva, per prima cosa occorre ricercare il * fascicolo e inserirlo come
		 * attributo "fascicoloSiusGP". Il fascicolo sarà ricercato con l'identificativo ereditato dalla
		 * maschera precedente prelevato tramite "getRequestBigDecimalParameter "CAMPO_ID_FASCICOLO_SIUS"
		 * Poichè nella maschera sarà necessario indicare l'autorità che ha inviato la comunicazione occorre
		 * caricare anche le autorita come attributo "tipoAutorita". Infine ricerchiamo eventuali precedenti
		 * comunicazione di data inizio sanzione sostitutiva per questo fascicolo. Queste sono necessarie per
		 * il controllo della giusta cronologia delle date (DATA_INIZIO_ESECUZIONE e DATA_SCADENZA) e per le
		 * comunicazioni successive alla prima il campo della maschera "Note" diventerà obbligatorio
		 *
		 */
		// ricerca del fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca delle autorita disponibili
		Option lOptionMess = new Option();
		lOptionMess = new Option(DecodificheManager.getInstance().getMotivoSanzioneSostitutiva(), 01);
		setRequestAttribute("MotivoESS", lOptionMess.toString());

		// ricerca delle precedenti comunicazioni di esecuzione sanzione sostitutiva
		// per il fascicolo
		IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		List lListaSanzioniSius = lCtrl1.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFasSius);
		setRequestAttribute("listaSanzioniSius", lListaSanzioniSius);

		// MEV_35: cambio nome etichetta in un caso particolare (U126)
		// MEV_35: recupero info sul fascicolo per oggetto procedimento
		String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";
		FascicoloGPModel fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
		if (!Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
			gpm = fgpm.getGeneraleProcedimentoModel();
		if (!Utils.isNullObj(gpm.getCodOggettoProcedimento()))
			codOggettoProcedimento = gpm.getCodOggettoProcedimento();
		if ("U126".equals(codOggettoProcedimento))
			tipoSostituzione = "Pena";

		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		if (lListaSanzioniSius != null && lListaSanzioniSius.size() > 0) {
			lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 1);
			// 01 Inizio
			// 02 Sospensione
			// 03 Ripresa
			// se esiste un periodo inizio/ripresa completo (ossia anche con la data scadenza)
			// non è possibile inserirne un altro. (occorre prima inserire un periodo di sospensione)
			if ((lPerMod.getFlagMotivo().equals("01") || lPerMod.getFlagMotivo().equals("02"))
					&& lPerMod.getDataScadenza() != null) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Ripresa non attivabile la " + tipoSostituzione + " non risulta sospesa!");
			}
			if (lPerMod.getFlagMotivo().equals("03") && lPerMod.getDataScadenza() == null) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Ripresa non attivabile la sospensione non è completa!");
			}
			if (lPerMod.getIstDetIdIstitutoDetenzione() != null
					&& !lPerMod.getIstDetIdIstitutoDetenzione().equals("-"))
				caricoIstitutiDetenzione(lPerMod);
		}

		// ricerca delle autorita disponibili
		Option lOptionAut = new Option();
		if (lPerMod != null && !lPerMod.getCodTipoAutorita().equals("-")) {
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lPerMod.getCodTipoAutorita());
		} else {
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita());
		}
		setRequestAttribute("tipoAutorita", lOptionAut.toString());

		// ricerca record esecuzione_sanzione_sost
		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
		if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE " + tipoSostituzione.toUpperCase() + " SOSTITUTIVA Assente!");
		setRequestAttribute("lESSModel", lESSModel);

		// pagina di ritorno
		String lRetPage = PG_LOADINSERISCIINIZIOSANZIONESOSTITUTIVA;

		// restituisce la jsp di VIEW
		return lRetPage;
	}

}