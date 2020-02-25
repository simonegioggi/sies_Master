/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.DATIRINVIOUDIENZA;
import it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza.TENORETYPE;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.model.UdienzaModel;

/**
 * @author Gioggi
 */
public class RinvioUdienzaMapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per la mappatura dei dati dell'ordinanza
	 * 
	 * @param um
	 * @param fgpm
	 * @param udienzePrecedenti
	 * @param em
	 * 
	 * @return DATIRINVIOUDIENZA
	 */
	public static DATIRINVIOUDIENZA mapDatiRinvioUdienza(UdienzaModel um, FascicoloGPModel fgpm,
			String udienzePrecedenti, EventoModel em) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RinvioUdienzaMapper, metodo: mapDatiRinvioUdienza");

		// instanzio ed inizializzo un oggetto di tipo "DATIRINVIOUDIENZA"
		DATIRINVIOUDIENZA dru = new DATIRINVIOUDIENZA();
		// aggiungo elementi al tipo "DATIORDINANZA"
		dru.setDataEmissione(Mapper.creaDataTypeRinvioUdienza(em.getDataEmissione()));
		if (PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel()) && 
				PropertyUtil.isPresent(fgpm.getGeneraleProcedimentoModel().getUdiIdUdienza())) {
			dru.setDataUdienza(Mapper.creaDataTypeRinvioUdienza(um.getDataUdienza()));
		}		
		dru.setDateUdienzePrecedenti(udienzePrecedenti);
		dru.setLuogoSvolgimentoUdienza(um.getLuogoUdienza());

		// recupero dati tenori
		List<TENORETYPE> ltt = RinvioUdienzaMapper.mapTenori(fgpm.getTenori());
		if (ltt != null)
			dru.getElencoOggetti().addAll(ltt);

		// valore di ritorno
		return dru;
	}

	/**
	 * Metodo per la mappatura dei dati del procedimento
	 * 
	 * @param fgpm
	 * @param fspm
	 * @param mrm
	 * @return DATIRIEPILOGOPROCEDIMENTO
	 */
	public static DATIRIEPILOGOPROCEDIMENTO mapDatiRiepilogoProcedimento(FascicoloGPModel fgpm,
			FascicoloSiepModel fspm, MagistratoRelatoreModel mrm) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RinvioUdienzaMapper, metodo: mapDatiRiepilogoProcedimento");

		// instanzio ed inizializzo un oggetto di tipo "DATIRIEPILOGOPROCEDIMENTO"
		DATIRIEPILOGOPROCEDIMENTO drp = new DATIRIEPILOGOPROCEDIMENTO();
		// dati del soggetto
		SoggettoModel sm = fgpm.getFascicoloSiusModel().getSoggetto();
		// dati del fascicolo SIUS
		FascicoloSiusModel fssm = fgpm.getFascicoloSiusModel();
		// dati del magistrato
		MagistratoModel mm = mrm.getMagistrato();
		// dati del procedimento
		GeneraleProcedimentoModel gpm = fgpm.getGeneraleProcedimentoModel();

		if (fspm != null) {
			if (fspm.getChiaveAnno() != null)
				drp.setAnnoFascicoloSIEP(fspm.getChiaveAnno().toBigInteger());
			if (fspm.getChiaveProgr() != null)
				drp.setNumeroFascicoloSIEP(fspm.getChiaveProgr().toBigInteger());
			String descrTipoUfficio = fspm.getDescrTipoUfficio();
			String descrComuneUfficio = fspm.getDescrComuneUfficio();
			String ufficiofascicoloSIEP = descrTipoUfficio
					+ ((PropertyUtil.isPresent(descrComuneUfficio)) ? " di " + descrComuneUfficio : "");
			drp.setUfficioFascicoloSIEP(ufficiofascicoloSIEP);
			drp.setDataFascicoloSIEP(Mapper.creaDataTypeRinvioUdienza(fspm.getDataIscrizione()));
		} else {
			drp.setAnnoFascicoloSIEP(null);
			drp.setNumeroFascicoloSIEP(null);
			drp.setUfficioFascicoloSIEP(null);
			drp.setDataFascicoloSIEP(null);
		}

		if (fssm.getChiaveAnno() != null)
			drp.setAnnoProcedimentoSIUS(fssm.getChiaveAnno().toBigInteger());
		else
			drp.setAnnoProcedimentoSIUS(null);
		if (mm != null) {
			drp.setCognomeMagistratoRelatore(mm.getCognome());
			drp.setNomeMagistratoRelatore(mm.getNome());
		} else {
			drp.setCognomeMagistratoRelatore(null);
			drp.setNomeMagistratoRelatore(null);
		}
		drp.setDataUdienza(Mapper.creaDataTypeRinvioUdienza(gpm.getDataCameraConsiglio()));

		// SOGGETTO
		drp.setCodiceProvincia(sm.getCodProvinciaNascita());
		drp.setCognomeSoggetto(sm.getCognome());
		drp.setDataNascita(Mapper.creaDataTypeRinvioUdienza(sm.getDataNascita()));
		String luogoNascita = sm.getDescrComuneNascita();
		if (!PropertyUtil.isPresent(sm.getCodProvinciaNascita()) || "-".equals(sm.getCodProvinciaNascita()))
			luogoNascita = sm.getDescrStatoNascita();
		drp.setLuogoNascita(luogoNascita);
		drp.setNomeSoggetto(sm.getNome());
		drp.setSesso(sm.getSesso());
		if (sm.getEtaPresuntaAnni() != null)
			drp.setEtaPresuntaAnni(sm.getEtaPresuntaAnni().toBigInteger());
		else
			drp.setEtaPresuntaAnni(null);
		if (sm.getEtaPresuntaMesi() != null)
			drp.setEtaPresuntaMesi(sm.getEtaPresuntaMesi().toBigInteger());
		else
			drp.setEtaPresuntaMesi(null);
		if (fssm.getChiaveProgr() != null)
			drp.setNumeroProcedimentoSIUS(fssm.getChiaveProgr().toBigInteger());
		else
			drp.setNumeroProcedimentoSIUS(null);
		drp.setOggettoProcedimentoSIUS(gpm.getDescrOggettoProcedimento());
		drp.setCodiceStatoFascicolo(fssm.getCodStatoFascicolo());
        drp.setDescrStatoFascicolo(fssm.getDescrStatoFascicolo());        
        if(PropertyUtil.isPresent(fgpm.getUdiPro())){
        	drp.setFlagRinviata(fgpm.getUdiPro().getFlagRinviata());
        } 
		// valore di ritorno
		return drp;
	}

	/**
	 * Metodo per la mappatura dei dati dei tenori
	 * 
	 * @param tenori
	 * @return List<TENORETYPE>
	 */
	private static List<TENORETYPE> mapTenori(TenoreModel[] tenori) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: RinvioUdienzaMapper, metodo: mapTenori");

		// instanzio un oggetto di tipo "ArrayList"
		List<TENORETYPE> let = null;
		if (tenori != null) {
			// inizializzo l'oggetto di tipo "ArrayList"
			let = new ArrayList<TENORETYPE>(tenori.length);
			for (int i = 0; i < tenori.length; i++) {
				// instanzio ed valorizzo un oggetto di tipo "TenoreModel"
				TenoreModel tm = (TenoreModel) tenori[i];
				// instanzio ed inizializzo un oggetto di tipo "TENORETYPE"
				TENORETYPE tt = new TENORETYPE();
				tt.setDescrOggettoTenore(tm.getDescrOggettoTenore());
				tt.setNote(tm.getNote());
				// aggiungo alla lista di ritorno
				let.add(tt);
			}
		}

		// valore di ritorno
		return let;
	}

}