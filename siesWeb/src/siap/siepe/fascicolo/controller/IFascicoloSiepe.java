package siap.siepe.fascicolo.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.fascicolo.model.FascicoloSiepeRicercaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FascicoloSiepeController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSiepe
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IFascicoloSiepe {

	public FascicoloSiepeModel ExInserisciFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe,
			MessaggioModel aMessaggio, AttivitaModel[] aListaAttivita) throws F3BException;

	public Vector ExRicercaFascicoloSiepe(FascicoloSiepeRicercaModel aFascicoloSiepe) throws F3BException;

	public FascicoloSiepeModel ExRicercaFascicoloSiepeByKey(BigDecimal aKey) throws F3BException;

	public FascicoloSiepeModel ExModificaFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe)
			throws F3BException;

	public void ExCancellaFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe) throws F3BException;

	public Vector ExRicercaFascicoloSiepePaginata(FascicoloSiepeRicercaModel aFasSiepeRicModel, int aPageNum)
			throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoloSiepe(FascicoloSiepeRicercaModel aFasSiepeRicModel)
			throws F3BException;

	public Vector ExRicercaFascSiepeBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lIncludeArchiviati, String lCodIncarico, Date dataDal,
			Date dataAl, int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lIncludeArchiviati, String lCodIncarico, Date dataDal,
			Date dataAl) throws F3BException;

	public Vector ExRicercaFascSiepeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) throws F3BException;

	public FascicoloSiepeModel ExRicercaFascicoloByAnnoProgrCodUfficio(FascicoloSiepeModel aFasMod)
			throws F3BException;

	public Vector ExRicercaFascicoliSiepePerIdFasSius(BigDecimal aIdFasSius) throws F3BException;

	public Vector ExRicercaFascicoliSiepePerIdFasSiep(BigDecimal aIdFasSius) throws F3BException;

	public void ExInserisciDefinizioneFascicoloSiepe(FascicoloSiepeModel aFasMod) throws F3BException;

}