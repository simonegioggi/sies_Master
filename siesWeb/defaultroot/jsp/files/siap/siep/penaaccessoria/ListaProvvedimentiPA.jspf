<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@page import="java.util.Iterator"%>
<%@page import="f3b.web.IWebConstants"%>
<%@page import="siap.web.ISIAPCostantiWeb"%>
<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
 <%@ page import="siap.sico.evento.model.EventoModel"%>
 <%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

  <head>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
			function chiama(idEvento)
			{
    			window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
			}
  	</script>
  </head>

<table>
    <tr>
      <td class=l width=2%> </td>
      <td class="int">N°</td>
      <td class="int">Provvedimento</td>
      <td class="int">Data Emissione</td>
      <td class="int" width=32%>Autorità Emittente</td>
      <td class="int">Documento<br>Validato</td>
      <td class="int" width=8%>Azioni</td>
    </tr>

<%
  Iterator itxLista = eventiPA.iterator();
  int i = 0;
  while ( itxLista.hasNext())
  {
    i++;
    EventoModel eventoPA = (EventoModel)itxLista.next();
    String lAction = new String("siap.siep.penaaccessoria.action.ActLoadDettaglioEsecuzionePA");
    if (eventoPA.getCodTipoEvento().compareTo("16")==0)
    	lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioEsecuzionePA";
    if (eventoPA.getCodTipoEvento().compareTo("17")==0)
    	lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE";
    if (eventoPA.getCodTipoEvento().compareTo("18")==0)
    	lAction = "siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione";
%>
    <tr>
      <td class="l"> </td>
      <td class="l"><%=i%></td>
      <td class="c"><font class="label"><%=eventoPA.getDescrTipoProvvedimento()%> </font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoPA.getDataEmissione(),"dd/MM/yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=eventoPA.getDescrUfficioEmittente()%> - <%=eventoPA.getDescrLuogoEmittente()%></font></td>
      <td class="c">&nbsp;
<%
      if (eventoPA.getFlagDocumentoRegistrato()!=null)
      {
        if (eventoPA.getFlagDocumentoRegistrato().compareTo("S")==0)
        {%>
          <img src="/images/TickRed.gif">
      <%}else if(eventoPA.getFlagDocumentoRegistrato().compareTo("A")==0)
        {%>
           <a class="cliccabile" href="javascript:chiama('<%=eventoPA.getIdEvento()%>');" title="ANNULLAMENTO">
           <font class="cRosso">ANNULLATO</font></a>
      <%}
      }
%>
      </td>
      <td class="c">
      <%-- Azioni per le Esecuzioni Pene Accessorie --%>
<%    if (eventoPA.getCodTipoEvento().equals("16") )
      {
%>
        	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadDettaglioEsecuzionePA&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>">
          	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        	</a>
<%
					if (eventoPA.getFlagDocumentoRegistrato() != null  &&
				   	 (eventoPA.getFlagDocumentoRegistrato().compareTo("S")==0 ||
				    	eventoPA.getFlagDocumentoRegistrato().compareTo("A")==0 ) )
        	{%>
       			<a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>');">
       				<img src="/images/print.gif" alt="Stampa Esecuzione P.A." width="12" height="12" border="0">
       			</a>
				<%}%>
<%
					if((eventoPA.getFlagDocumentoRegistrato() == null) ||
				  	 (eventoPA.getFlagDocumentoRegistrato() != null  &&
				  	 (eventoPA.getFlagDocumentoRegistrato().compareTo("A")!=0) ) )
        	{%>
	      		<a href="Javascript:confermaCancellazione('siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento', 'IdEvento', '<%=eventoPA.getIdEvento()%>','campo','<%=eventoPA.getFlagDocumentoRegistrato()%>', 'siap.siep.penaaccessoria.action.ActLoadInserisciEsecuzionePA');">
							<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      		</a>
				<%}%>
      <%}%>

      <%-- Azioni per le Richieste al GE --%>
<%    if (eventoPA.getCodTipoEvento().equals("17") )
      {
%>
        	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>">
          	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        	</a>
<%
					if (eventoPA.getFlagDocumentoRegistrato() != null  &&
				   	 (eventoPA.getFlagDocumentoRegistrato().compareTo("S")==0 ||
				    	eventoPA.getFlagDocumentoRegistrato().compareTo("A")==0 ) )
        	{%>
       			<a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>');">
       				<img src="/images/print.gif" alt="Stampa Richiesta al G.E." width="12" height="12" border="0">
       			</a>
				<%}%>
<%
					if((eventoPA.getFlagDocumentoRegistrato() == null) ||
				  	 (eventoPA.getFlagDocumentoRegistrato() != null  &&
				  	 (eventoPA.getFlagDocumentoRegistrato().compareTo("A")!=0) ) )
        	{%>
	      		<a href="Javascript:confermaCancellazione('siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento', 'IdEvento', '<%=eventoPA.getIdEvento()%>','campo','<%=eventoPA.getFlagDocumentoRegistrato()%>', 'siap.siep.penaaccessoria.action.ActLoadInserisciRichiestaGE');">
							<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      		</a>
				<%}%>
      <%}%>

      <%-- Azioni per le Comunicazioni --%>
<%    if (eventoPA.getCodTipoEvento().equals("18") )
      {
%>
        	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>">
          	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        	</a>
<%
					if (eventoPA.getFlagDocumentoRegistrato() != null  &&
				   	 (eventoPA.getFlagDocumentoRegistrato().compareTo("S")==0 ||
				    	eventoPA.getFlagDocumentoRegistrato().compareTo("A")==0 ) )
        	{%>
       			<a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=eventoPA.getIdEvento()%><%=retParam%>');">
       				<img src="/images/print.gif" alt="Stampa Comunicazione" width="12" height="12" border="0">
       			</a>
				<%}%>
<%
					if((eventoPA.getFlagDocumentoRegistrato() == null) ||
				  	 (eventoPA.getFlagDocumentoRegistrato() != null  &&
				  	 (eventoPA.getFlagDocumentoRegistrato().compareTo("A")!=0) ) )
        	{%>
	      		<a href="Javascript:confermaCancellazione('siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento', 'IdEvento', '<%=eventoPA.getIdEvento()%>','campo','<%=eventoPA.getFlagDocumentoRegistrato()%>', 'siap.siep.penaaccessoria.action.ActLoadInserisciComunicazione');">
							<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	      		</a>
				<%}%>
      <%}%>

	    </td>

    </tr>
<%}
%>
</table>