package siap.siep.sospensione.controller;

import java.math.BigDecimal;
import java.util.List;

import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IInterruzione {

	public List ExRicercaPeriodiInterruzione(BigDecimal aKeyFascicolo) throws F3BException;

}