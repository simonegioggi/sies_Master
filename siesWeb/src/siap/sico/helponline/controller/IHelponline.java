package siap.sico.helponline.controller;

/**
* <p>Title: HelponlineController</p>
* <p>Description: Classe Controller per Helponline</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.helponline.model.HelponlineModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IHelponline {

	public HelponlineModel ExInserisciHelponline(HelponlineModel aHelponline) throws F3BException;

	public Vector ExRicercaHelponline(HelponlineModel aHelponline) throws F3BException;

	public void ExModificaHelponline(HelponlineModel aHelponline) throws F3BException;

	public void ExCancellaHelponline(HelponlineModel aHelponline) throws F3BException;

	public BigDecimal ExGetCountHelponline(HelponlineModel aHelponline) throws F3BException;

	public HelponlineModel ExRicercaHelponlineById(BigDecimal aIdHelponline) throws F3BException;

	public Vector ExRicercaHelponlinePaged(HelponlineModel aHelponline, int aPage) throws F3BException;

}