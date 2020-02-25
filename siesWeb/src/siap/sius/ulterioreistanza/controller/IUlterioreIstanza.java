package siap.sius.ulterioreistanza.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.tenore.model.TenoreModel;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UlterioreIstanza
 * </p>
 * <p>
 * Description: Classe Interfaccia per il Controller UlterioreIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IUlterioreIstanza {

	public UlterioreIstanzaModel ExInserisciUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza,
			TenoreModel[] aTenori) throws F3BException;

	public Vector ExRicercaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza) throws F3BException;

	public UlterioreIstanzaModel ExRicercaUlterioreIstanzaByKey(BigDecimal aKey) throws F3BException;

	public UlterioreIstanzaModel ExModificaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza)
			throws F3BException;

	public void ExCancellaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza) throws F3BException;

}