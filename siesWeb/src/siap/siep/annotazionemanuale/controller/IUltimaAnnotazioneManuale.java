package siap.siep.annotazionemanuale.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IUltimaAnnotazioneManuale {

	public Vector ExRicercaUltimeAnnotazioniManuali(BigDecimal aKeyFascicolo, EventoModel aEventoCorrente)
			throws F3BException;

}