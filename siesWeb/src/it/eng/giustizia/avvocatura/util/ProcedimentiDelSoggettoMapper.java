/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti.DATIPROCEDIMENTOTYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * @author Gioggi
 */
public class ProcedimentiDelSoggettoMapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	@SuppressWarnings("rawtypes")
	public static List<DATIPROCEDIMENTOTYPE> mapProcedimentiDelSoggetto(Vector elencoProcedimenti) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: ProcedimentiDelSoggettoMapper, metodo: mapProcedimentiDelSoggetto");

		// instanzio un oggetto di tipo "ArrayList"
		List<DATIPROCEDIMENTOTYPE> lst = null;
		if (elencoProcedimenti != null) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lst = new ArrayList<DATIPROCEDIMENTOTYPE>(elencoProcedimenti.size());
			for (int i = 0; i < elencoProcedimenti.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "FascicoloGPModel"
				FascicoloGPModel fgpm = (FascicoloGPModel) elencoProcedimenti.get(i);
				// instanzio ed inizializzo un oggetto di tipo "DATIPROCEDIMENTOTYPE"
				DATIPROCEDIMENTOTYPE dpt = new DATIPROCEDIMENTOTYPE();
				// imposto i dati
				if (fgpm.getFascicoloSiusModel().getChiaveAnno() != null)
					dpt.setChiaveAnno(fgpm.getFascicoloSiusModel().getChiaveAnno().toBigInteger());
				else
					dpt.setChiaveAnno(null);
				if (fgpm.getFascicoloSiusModel().getChiaveProgr() != null)
					dpt.setChiaveProgr(fgpm.getFascicoloSiusModel().getChiaveProgr().toBigInteger());
				else
					dpt.setChiaveProgr(null);
				dpt.setCodStatoFascicolo(fgpm.getFascicoloSiusModel().getCodStatoFascicolo());
				dpt.setDataAggiornamento(Mapper.creaDataTypeElencoProcedimenti(fgpm
						.getGeneraleProcedimentoModel().getDataAggiornamento()));
				dpt.setDataCameraConsiglio(Mapper.creaDataTypeElencoProcedimenti(fgpm
						.getGeneraleProcedimentoModel().getDataCameraConsiglio()));
				dpt.setDataRichiesta(Mapper.creaDataTypeElencoProcedimenti(fgpm
						.getGeneraleProcedimentoModel().getDataRichiesta()));
				dpt.setDescrDefinizione(fgpm.getGeneraleProcedimentoModel().getDescrDefinizione());
				if ("01".compareToIgnoreCase(fgpm.getFascicoloSiusModel().getDescrStatoFascicolo()) == 0
						|| fgpm.getGeneraleProcedimentoModel().getDataRichiesta() == null)
					dpt.setDescrOggettoProcedimento(DecodificheUtils.getDescbyCode(DecodificheManager
							.getInstance().getStatoFascicolo(), fgpm.getFascicoloSiusModel()
							.getDescrStatoFascicolo()));
				else
					dpt.setDescrOggettoProcedimento(fgpm.getGeneraleProcedimentoModel()
							.getDescrOggettoProcedimento());
				dpt.setDescrPosGiuridica(fgpm.getGeneraleProcedimentoModel().getDescrPosGiuridica());
				dpt.setDescrStatoFascicolo(fgpm.getFascicoloSiusModel().getDescrStatoFascicolo());
				dpt.setDescrTipoAtto(fgpm.getGeneraleProcedimentoModel().getDescrTipoAtto());
				if (fgpm.getFascicoloSiusModel().getIdFascicoloSius() != null)
					dpt.setIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius().toBigInteger());
				else
					dpt.setIdFascicoloSius(null);
				dpt.setCodOggettoProcedimento(fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				dpt.setCodPosGiuridica(fgpm.getGeneraleProcedimentoModel().getCodPosGiuridica());
				dpt.setCodTipoAtto(fgpm.getGeneraleProcedimentoModel().getCodTipoAtto());
				dpt.setCodTipoUfficio(fgpm.getFascicoloSiusModel().getDescrTipoUfficio());

				// aggiungo alla lista di ritorno
				lst.add(dpt);
			}
		}

		// valore di ritorno
		return lst;
	}

}