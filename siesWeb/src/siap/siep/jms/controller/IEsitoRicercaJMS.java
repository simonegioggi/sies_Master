package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.jms.messaggio.model.MessaggioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IEsitoRicercaJMS
 * </p>
 * <p>
 * Description: COntroller che gestisce i file in arrivo da OpenJMS per la ricerca
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IEsitoRicercaJMS {

	public MessaggioModel ExRicercaEsitoRicerca(MessaggioModel aMessaggio) throws F3BException;

	public Vector ExRicercaMessaggioEsitoRicercaPerUfficio(String aUfficio, String aTipoOperazione, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountEsitoRicercaPerUfficio(String aUfficio, String aTipoOperazione)
			throws F3BException;

	public BigDecimal ExGetCountEsitoRicercaFascAltreBDI(String lAnnoSiep, String lProgrSiep,
			String codUfficioMittente, String codUfficioDestinatario, String codUtente, String tipoEsito,
			String codTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException;

	public BigDecimal ExGetCountEsitoRicercaSoggAltreBDI(String codUfficioMittente, String codUtente,
			String codTipoOperazione, Date dataRicercaInizio, Date dataRicercaFine) throws F3BException;

}