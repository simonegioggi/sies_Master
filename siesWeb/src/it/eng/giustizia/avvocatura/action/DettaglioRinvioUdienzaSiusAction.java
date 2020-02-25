/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.controller.IAvvisiSius;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.util.RinvioUdienzaMapper;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATIAVVISO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATIRINVIOUDIENZA;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.OUTPUTRINVIOUDIENZA;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
public class DettaglioRinvioUdienzaSiusAction extends ActionSius {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'elaborazione del dettaglio del rinvio udienza
	 * 
	 * @param codiceEsito
	 * @param codiTipoProvvedimento
	 * @param idEvento
	 * @param datiAvviso
	 * @return OUTPUTRINVIOUDIENZA
	 * @throws Exception
	 */
	public OUTPUTRINVIOUDIENZA dettaglioRinvioUdienza(String codiceEsito, String codiTipoProvvedimento,
			String idEvento, DATIAVVISO datiAvviso) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DettaglioRinvioUdienzaSiusAction, metodo: dettaglioRinvioUdienza");

		// recupero l'id dell'evento
		BigDecimal idEv = new BigDecimal(idEvento);

		// recupero i dati dell'evento
		EventoModel em = new EventoModel();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		em = iEvento.ExRicercaEventoByKey(idEv);
		FascicoloGPModel fgpm = null;
		FascicoloSiepModel fsm = null;

		try {
			// recupero i dati del fascicolo SIUS
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			fgpm = ifs.ExRicercaFascicoloByKey(em.getFasSiuIdFascicoloSius());

			// recupero i dati del fascicolo SIEP
			IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
			fsm = ifsp.ExRicercaFascicoloByKey(em.getFasSieIdFascicoloSiep());
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.info("Eccezione nella ricerca del fascicolo SIUS e SIEP: " + e.getMessage(), e);
		}

		// recupero i dati del magistrato
		IMagistrato iMagistrato = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = iMagistrato.ExRicercaMagistratoByEvento(idEv);
		MagistratoRelatoreModel mrm = new MagistratoRelatoreModel();
		mrm.setMagistrato(mm);

		String udienzePrecedenti = "";

		// recupero l'ID_UDIENZA
		BigDecimal idUdienza = fgpm.getGeneraleProcedimentoModel().getUdiIdUdienza();
		IUdienzaProcedimento iup = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		UdienzaProcedimentoModel upm = iup.ExRicercaUdienzaProcedimentoByEve(idEv);

		// aggiorno l'ID_UDIENZA se esiste udienza procedimento
		if (upm != null)
			idUdienza = upm.getUdiIdUdienza();

		UdienzaModel um = new UdienzaModel();

		if (idUdienza != null) {
			// recupero i dati dell'udienza
			IUdienza iUdienza = SIUSLookupRemote.getUdienzaRemote();
			um = iUdienza.ExRicercaUdienzaByKey(idUdienza);

			// imposto i dati del GeneraleProcedimentoModel
			BigDecimal idGeneraleProcedimento = fgpm.getGeneraleProcedimentoModel()
					.getIdGeneraleProcedimento();
			GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
			gpm.setIdGeneraleProcedimento(idGeneraleProcedimento);
			gpm.setUdiIdUdienza(idUdienza);
			gpm.setDataCameraConsiglio(fgpm.getGeneraleProcedimentoModel().getDataCameraConsiglio());

			// recupero i dati sulle udienze precedenti
			udienzePrecedenti = iUdienza.ExRicercaUdienzePrecedenti(um, gpm);
		}

		// se presente, aggiorno la tabella degli avvisi
		if (PropertyUtil.isPresent(datiAvviso)) {
			IAvvisiSius ias = SIUSLookupRemote.getAvvisiSiusRemote();
			ias.aggiornaAvvisiAvvocato(new BigDecimal(datiAvviso.getIdAvviso()));
		}

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: OUTPUTRINVIOUDIENZA");
		// copia dei dati dal model al type
		OUTPUTRINVIOUDIENZA oru = copyModelToType(um, fgpm, udienzePrecedenti, fsm, mrm, em);

		// valore di ritorno
		return oru;
	}

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param um
	 * @param fgpm
	 * @param udienzePrecedenti
	 * @param fsm
	 * @param mrm
	 * @param em
	 * @return OUTPUTRINVIOUDIENZA
	 * @throws Exception
	 */
	private OUTPUTRINVIOUDIENZA copyModelToType(UdienzaModel um, FascicoloGPModel fgpm,
			String udienzePrecedenti, FascicoloSiepModel fsm, MagistratoRelatoreModel mrm, EventoModel em)
			throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioRinvioUdienzaSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "OUTPUTRINVIOUDIENZA"
		OUTPUTRINVIOUDIENZA oru = new OUTPUTRINVIOUDIENZA();

		try {
			// recupero dati rinvio udienza
			DATIRINVIOUDIENZA dru = RinvioUdienzaMapper.mapDatiRinvioUdienza(um, fgpm, udienzePrecedenti, em);

			// imposto i dati rinvio udienza
			oru.setDATIRINVIOUDIENZA(dru);

			// recupero dati procedimento
			DATIRIEPILOGOPROCEDIMENTO drp = RinvioUdienzaMapper.mapDatiRiepilogoProcedimento(fgpm, fsm, mrm);
			oru.setDATIRIEPILOGOPROCEDIMENTO(drp);

			// imposto l'ERRORE
			oru.setERRORE(Mapper.mapErroreRinvioUdienza("000", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore nella mappatura dei dati del rinvio udienza: " + e.getMessage(), e);
			// imposto l'ERRORE
			oru.setERRORE(Mapper.mapErroreRinvioUdienza("025", e.getMessage()));
		}

		// valore di ritorno
		return oru;
	}

}