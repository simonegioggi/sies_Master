package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.messaggio.model.MessaggioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.model.GenericModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IRicercaJMS {

	public MessaggioModel ExSpedisciRichiestaRicerca(GenericModel aModel) throws F3BException;

	public MessaggioModel ExRicercaFascicoloSiep(FascicoloSiepModel aModel) throws F3BException;

	public MessaggioModel ExRicercaFascicoloSiepPerTrasferimento(FascicoloSiepModel aModel)
			throws F3BException;

	public Vector ricercaEventoNotificaByFascicoloSiepPerTrasferimento(BigDecimal lKeyFascicolo)
			throws F3BException;

	public MessaggioModel ExRicercaFascicoloSiepPerRichiestaTrasferimento(FascicoloSiepModel aModel)
			throws F3BException;

	/**
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 * 
	 * @param aModel
	 * @param checkMinore
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiepPerTrasferimento(FascicoloSiepModel aModel, String checkMajor)
			throws F3BException;

	/**
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 * 
	 * @param aModel
	 * @param checkMajor
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiep(FascicoloSiepModel aModel, String checkMajor)
			throws F3BException;

}