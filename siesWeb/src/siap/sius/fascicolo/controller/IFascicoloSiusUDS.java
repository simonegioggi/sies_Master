package siap.sius.fascicolo.controller;

/**
* <p>Title: IFascicoloSiusUDS</p>
* <p>Description: Classe di Interfaccia per Controller Fascicolo SIUS</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.produzioneatti.model.ParereModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IFascicoloSiusUDS {

	public FascicoloGPModel ExRicercaFascicoloByKey(BigDecimal aIdFascicoloSius) throws F3BException;

	// public FascicoloGPModel ExInserisciFascicoloSiusUDS (FascicoloGPModel aFascicoloGPModel)
	// throws F3BException;
	// public FascicoloGPModel ExInserisciFascicoloSiusUDS (FascicoloGPModel aFascicoloGPModel, String
	// aIdEventoInviato)
	// throws F3BException;
	public FascicoloGPModel ExInserisciFascicoloSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni)
			throws F3BException;

	// public FascicoloGPModel ExInserisciFascicoloSiusUDS (FascicoloGPModel aFascicoloGPModel, String
	// aIdEventoInviato, BigDecimal IdFascicoloPadreEsecuzione)
	// throws F3BException;

	public FascicoloGPModel ExInserisciFascicoloSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, BigDecimal IdFascicoloPadreEsecuzione, int aDurataEsitoAnni,
			int aDurataEsitoMesi, int aDurataEsitoGiorni) throws F3BException;

	public boolean ExistProcedimentoEsecuzione(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aCodContenuto, String aTipoRegistro, String ufficioUtenteConnesso, BigDecimal idSoggetto)
			throws F3BException;

	// public FascicoloGPModel ExInserisciFascicoloDaSiusUDS (FascicoloGPModel aFascicoloGPModel, String
	// aIdEventoInviato)
	// throws F3BException;

	public FascicoloGPModel ExInserisciFascicoloDaSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni)
			throws F3BException;

	public FascicoloGPModel ExInserisciFascicoloSiusUDSManuale(FascicoloGPModel aFascicoloGPModel)
			throws F3BException;

	public void ExInserisciDefinizioneFascicoloSius(FascicoloGPModel aFasGPMod) throws F3BException;

	public Vector ExRicercaPareriPaginata(ParereModel aParereIn, int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaPareri(ParereModel aParereIn) throws F3BException;

}