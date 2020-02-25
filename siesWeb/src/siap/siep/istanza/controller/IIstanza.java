package siap.siep.istanza.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IstanzaController
 * </p>
 * <p>
 * Description: Classe Controller per Istanza
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
public interface IIstanza {

	public IstanzaModel ExInserisciIstanza(IstanzaModel aIstanza) throws F3BException;

	public IstanzaModel ExInserisciIstanzaSoggetto(IstanzaModel aIstanza, SoggettoModel aSoggetto)
			throws F3BException;

	public IstanzaModel ExInserisciIstanzaFascicoloSiep(IstanzaModel aIstanza,
			FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public Vector ExRicercaIstanza(IstanzaModel aIstanza) throws F3BException;

	public Vector ExRicercaIstanzaByFascicolo(BigDecimal aIstanza) throws F3BException;

	public IstanzaModel ExRicercaIstanzaByKey(BigDecimal aKey) throws F3BException;

	public IstanzaSoggettoEventoFascicoloSiepModel ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey(
			BigDecimal aKey) throws F3BException;

	public IstanzaModel ExModificaIstanza(IstanzaModel aIstanza) throws F3BException;

	public IstanzaModel ExModificaIstanzaFascicoloSiep(IstanzaModel aIstanza,
			FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public IstanzaModel ExAnnulamentoIstanzaInserisciCampoNota(IstanzaModel aIstMod,
			CampoNotaModel aCampoNota) throws F3BException;

	public Vector ExRicercaIstanzaSoggetto(SoggettoModel aSogMod) throws F3BException;

	public Vector ExRicercaIstanzaOggetto(IstanzaModel aIstMod) throws F3BException;

	public Vector ExRicercaIstanzaSoggettoPaged(SoggettoModel aSogMod, int aPage) throws F3BException;

	public BigDecimal ExGetCountIstanzaSoggettoPaged(SoggettoModel aSogMod) throws F3BException;

	public Vector ExRicercaIstanzaOggettoPaged(IstanzaModel aIstMod, int aPage) throws F3BException;

	public BigDecimal ExGetCountIstanzaOggettoPaged(IstanzaModel aIstMod) throws F3BException;

	public IstanzaSoggettoEventoFascicoloSiepModel ExRicercaIstanzaSoggettoEventoFascicoloSiepByIdEvento(
			BigDecimal aKey) throws F3BException;

}
