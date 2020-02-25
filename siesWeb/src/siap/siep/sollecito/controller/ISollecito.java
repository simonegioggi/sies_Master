package siap.siep.sollecito.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISollecito {

	public Vector ExRicercaEventiPerFascicolo(BigDecimal aFascicoloId, int aNumPage) throws F3BException;

	public EventoNotificaModel ExStampaSollecito(EventoModel eveMod) throws F3BException;

	public int getEventiSize();

}