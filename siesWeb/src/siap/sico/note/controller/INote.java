package siap.sico.note.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;

/**
 * <p>
 * Title: NoteController
 * </p>
 * <p>
 * Description: Classe Controller per Note
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface INote {

	public Vector ExRicercaNote(BigDecimal aIdFascicoloSius) throws F3BException;

	public Vector ExRicercaNoteFasSige(BigDecimal aIdFascicoloSige) throws F3BException;

}