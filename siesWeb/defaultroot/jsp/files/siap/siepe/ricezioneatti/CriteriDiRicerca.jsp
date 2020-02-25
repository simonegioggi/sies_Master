<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="FiltroRicerca" scope="request" class="siap.siepe.ricezioneatti.model.RicercaMessaggioModel"/>

<%
   // Determinazione dei criteri di ricerca adottati
   boolean casoDate = false;
   boolean casoSoggetto = false;
   boolean casoDataInvio = false;
   boolean casoFlagVisto = false;
   boolean esisteFiltroRicerca = false;
   boolean casoTipoAtto = false;
   boolean casoNumeroSius = false;
   boolean casoNumeroSiep = false;
   boolean casoTipoUfficio = false;

	// Controlli di Configurazione
 if ((FiltroRicerca.getDataIniziale() != null  && FiltroRicerca.getDataIniziale().after(DateUtils.getDate(1900,01,01)) ) || (FiltroRicerca.getDataFinale() != null  && (DateUtils.getDateToString(FiltroRicerca.getDataFinale(), "dd/mm/yyyy").compareTo(DateUtils.getDateToString(DateUtils.getSysDate(), "dd/mm/yyyy") )!=0 ) ) )
 {
    casoDate = true;
    esisteFiltroRicerca = true;
 }
 if ((FiltroRicerca.getCognomeSoggetto() != null && FiltroRicerca.getCognomeSoggetto().length() > 0 )   ||
     ( FiltroRicerca.getNomeSoggetto() != null && FiltroRicerca.getNomeSoggetto().length() > 0)         ||
     ( FiltroRicerca.getCodComuneNascita() != null && FiltroRicerca.getCodComuneNascita().length() > 1) ||
     ( FiltroRicerca.getCodStatoNascita() != null && FiltroRicerca.getCodStatoNascita().length() > 1)   ||
     ( FiltroRicerca.getDataNascita() != null && FiltroRicerca.getDataNascita().toString().length() > 2) )
 {
    casoSoggetto = true;
    esisteFiltroRicerca = true;
 }
 if (FiltroRicerca.getDataInvio() != null)
 {
    casoDataInvio = true;
    esisteFiltroRicerca = true;
 }
	if ((FiltroRicerca.getFlagVisto() != null && FiltroRicerca.getFlagVisto()!="-" ) )
	{
    casoFlagVisto = true;
    esisteFiltroRicerca = true;
	}

	if (FiltroRicerca.getDescrTipoOperazione() != null && FiltroRicerca.getDescrTipoOperazione().length() > 2 )
	{
    casoTipoAtto = true;
    esisteFiltroRicerca = true;
	}

	if (FiltroRicerca.getChiaveAnnoSius() != null && FiltroRicerca.getChiaveAnnoSius().toString().length() > 1 )
	{
    casoNumeroSius = true;
    esisteFiltroRicerca = true;
	}

	if (FiltroRicerca.getChiaveAnnoSiep() != null && FiltroRicerca.getChiaveAnnoSiep().toString().length() > 1 )
	{
    casoNumeroSiep = true;
    esisteFiltroRicerca = true;
	}
	if (FiltroRicerca.getCodUfficioMittente() != null && FiltroRicerca.getCodUfficioMittente().toString().length() > 1 )
	{
    casoTipoUfficio = true;
    esisteFiltroRicerca = true;
	}


	// Controlli di Visualizzazione
	if (esisteFiltroRicerca)
	{
%>
  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo"><font class="label"> Criteri di Ricerca:</font></td>
    </tr>
<%  // Periodo di date
 if (casoDate ) {%>
        <tr>
          <td class="lVerdeNB">Atti inviati <%if (FiltroRicerca.getDataIniziale() != null) {%> dal  <%=StringUtils.toStringJSP(DateUtils.getDateToString(FiltroRicerca.getDataIniziale(),"dd-MM-yyyy"))%> <% } %> <%if (FiltroRicerca.getDataFinale() != null) {%> fino al <%=StringUtils.toStringJSP(DateUtils.getDateToString(FiltroRicerca.getDataFinale(),"dd-MM-yyyy"))%> <% }%> </td>
        </tr>
<% }
  // Per il Soggetto
 if (casoSoggetto ){%>
        <tr>
          <td class="lVerdeNB"> Atti relativi al Soggetto:
<%				if (FiltroRicerca.getCognomeSoggetto() != null && FiltroRicerca.getCognomeSoggetto().length() > 0 )
					{%>
            <%=FiltroRicerca.getCognomeSoggetto()%>*
				<%}%>
<%				if (FiltroRicerca.getNomeSoggetto() != null && FiltroRicerca.getNomeSoggetto().length() > 0 )
					{%>
            <%=FiltroRicerca.getNomeSoggetto()%>*
				<%}%>
<%				if (FiltroRicerca.getDataNascita() != null && FiltroRicerca.getDataNascita().toString().length() > 0 )
					{%>
            Data di Nascita <%=FiltroRicerca.getDataNascita()%>
				<%}%>
<%				if (FiltroRicerca.getCodComuneNascita() != null && FiltroRicerca.getCodComuneNascita().length() > 1 )
					{%>
            Luogo di Nascita <%= FiltroRicerca.getDescrComuneNascita()%>
				<%}%>
          </td>
        </tr>
<% }
 // Data di Invio
 if (casoDataInvio) {%>
        <tr>
          <td class="lVerdeNB">Atti inviati in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(FiltroRicerca.getDataInvio(),"dd-MM-yyyy"))%></td>
        </tr>
 <%}

 // Presa Visione (FLAG_VISTO)
if ( casoFlagVisto  )
{
   String descStato = "ricevuti";
   if (FiltroRicerca.getFlagVisto().compareTo("S")==0 )
   {
        descStato = "presi in carico";
   }
    else if (FiltroRicerca.getFlagVisto().compareTo("V")==0 )
   {
        descStato = "presi in  visione";
   }
    else if (FiltroRicerca.getFlagVisto().compareTo("R")==0 )
   {
        descStato = "restituiti";
   }
    else if (FiltroRicerca.getFlagVisto().compareTo("N")==0 )
   {
        descStato = "non trattati";
   }
%>
        <tr>
          <td class="lVerdeNB"> Atti <%=descStato%></td>
        </tr>
<% }
 // Tipo Atto
 if (casoTipoAtto) {%>
        <tr>
          <td class="lVerdeNB">Tipo Atto richiesto : <%=FiltroRicerca.getDescrTipoOperazione()%></td>
        </tr>
 <%}
 // Numero SIUS
 if (casoNumeroSius) {%>
        <tr>
          <td class="lVerdeNB">Numero SIUS : <%=FiltroRicerca.getChiaveAnnoSius()%>/<%=FiltroRicerca.getChiaveProgrSius()%> </td>
        </tr>
 <%}
 // Numero SIEP
 if (casoNumeroSiep) {%>
        <tr>
          <td class="lVerdeNB">Numero SIEP : <%=FiltroRicerca.getChiaveAnnoSiep()%>/<%=FiltroRicerca.getChiaveProgrSiep()%> </td>
        </tr>
 <%}
 // Tipo Ufficio
 if (casoTipoUfficio) {%>
        <tr>
          <td class="lVerdeNB">Autorità Mittente : <%=FiltroRicerca.getDescrUfficioMittente()%> </td>
        </tr>
 <%}





%>




  </table>
<% } %>
  <br>
