package siap.sius.motivazionedecreto.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MotivazioneDecretoController
 * </p>
 * <p>
 * Description: Classe Controller per MotivazioneDecreto
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
public interface IMotivazioneDecreto {

	public MotivazioneDecretoModel ExInserisciMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto)
			throws F3BException;

	public MotivazioneDecretoModel ExModificaMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto)
			throws F3BException;

	public void ExCancellaMotivazioneDecreto(MotivazioneDecretoModel aMotivazioneDecreto) throws F3BException;

	public void ExInserisciMotivazioniDecreto(MotivazioneDecretoModel[] aMotivazioniDecreto)
			throws F3BException;

	public void ExInserisciMotivazioniDecretoIncompetenza(MotivazioneDecretoModel[] aMotivazioniDecreto)
			throws F3BException;

	public Vector ExRicercaMotivazioniDecretoInammissibilitaByDepDecr(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaMotivazioniDecretoInammissibilitaByEve(BigDecimal aKey) throws F3BException;

	public EventoNotificaModel ExInserisciRichiestaParere(EventoNotificaModel aEvento,
			MotivazioneDecretoModel[] aMotivazioniDecreto) throws F3BException;

	public Vector ExRicercaUltimeMotivazioniDecretoByIdFasSius(BigDecimal aKey) throws F3BException;

}