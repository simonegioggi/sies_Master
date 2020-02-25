package siap.sius.titoloesecutivo.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RiferimentoFascicoloSiepController
 * </p>
 * <p>
 * Description: Classe Controller per RiferimentoFascicoloSiep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ITitoloEsecutivo {

	public FascicoloGPModel ExAssegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod) throws F3BException;

	public FascicoloGPModel ExAssegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod, FascicoloSiepModel lFasSiepOld) throws F3BException;

	public FascicoloGPModel ExDeassegnaTitoloEsecutivo(FascicoloGPModel aFascicoloGP,
			FascicoloSiepModel lFasSiepMod) throws F3BException;

	// TODO carmela da verificare

	public Vector ExRicercaTitoloEsecutivoByIdFascicoloSius(BigDecimal aKey) throws F3BException;

}