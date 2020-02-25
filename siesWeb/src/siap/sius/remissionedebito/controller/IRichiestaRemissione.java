package siap.sius.remissionedebito.controller;

/**
* <p>Title: RichiestaRemissioneController</p>
* <p>Description: Classe Controller per RichiestaRemissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IRichiestaRemissione {

	public RichiestaRemissioneModel ExInserisciRichiestaRemissione(
			RichiestaRemissioneModel aRichiestaRemissione) throws F3BException;

	public Vector ExRicercaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public void ExModificaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public void ExCancellaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public BigDecimal ExGetCountRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public RichiestaRemissioneModel ExRicercaRichiestaRemissioneById(BigDecimal aIdRichiestaRemissione)
			throws F3BException;

	public Vector ExRicercaRichiestaRemissionePaged(RichiestaRemissioneModel aRichiestaRemissione, int aPage)
			throws F3BException;

	public RichiestaRemissioneModel ExInserisciRichiestaRemissionedaClasseI(SoggettoModel aSogMod,
			EventoModel aEvento, FascicoloSiepModel aFascicoloSiep,
			DettaglioFascicoloModel aDettaglioFascicolo, RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public RichiestaRemissioneModel ExRicercaRichiestaRemissioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public Vector ExRicercaRichiesteRemissioneDebito(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException;

	public RichiestaRemissioneModel ExInserisciRichiestaEvento(RichiestaRemissioneModel aRichiestaRemissione,
			EventoNotificaModel aEvento, PenaResiduaModel aPenaResidua, String StatoPro) throws F3BException;

	public String ExInserisciRichiesteRemissioniWithoutSequence(ArrayList aRichiesteRemissioni,
			Connection lConn) throws F3BException;

}