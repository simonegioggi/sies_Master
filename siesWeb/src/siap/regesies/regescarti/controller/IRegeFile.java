package siap.regesies.regescarti.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.regesies.regescarti.model.RegeFileModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeFileController
 * </p>
 * <p>
 * Description: Classe Controller per file scartati REGE
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
public interface IRegeFile {

	public RegeFileModel ExInserisciRegeFile(RegeFileModel aRegeFile) throws F3BException;

	public Vector ExRicercaRegeFilePage(RegeFileModel aRegeFile, int aPage) throws F3BException;

	public RegeFileModel ExRicercaRegeFileByKey(String aKey) throws F3BException;

	public RegeFileModel ExModificaRegeFile(RegeFileModel aRegeFile) throws F3BException;

	public void ExCancellaRegeFile(RegeFileModel aRegeFile) throws F3BException;

	public BigDecimal ExgetCountFileRege(RegeFileModel aRegeFile) throws F3BException;

}