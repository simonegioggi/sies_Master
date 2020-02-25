<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="prescrizioni" scope="request" class="java.util.Vector"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>


<html>
<head>
<title>[S.I.E.S.] - Dettaglio Prescrizione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Dettaglio Emissione Ordinanza e Prescrizioni</font>&nbsp;
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
     <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaOECondannatoLibero&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td> <%}%>


<%
        String lAction = new String();
        lAction = "siap.sius.depositoordinanzapc.action.ActStapaOrdinanzaPc";
     %>
      </td>
    </tr>

 <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr><tr>
      <td>&nbsp;</td>
     </tr>
</table>
	<!--	 <div align=left> -->
  <table cellspacing=4 cellpadding=4>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td class="int">Tipo Prescrizione</td>
    </tr>

<%
  Iterator itx = prescrizioni.iterator();
  while ( itx.hasNext())
  {
    PrescrizioneModel prescr = (PrescrizioneModel)itx.next();

%>
    <tr>
      <td class=l><%=prescr.getDescrTipoPrescrizione().compareTo("-")==0 ? prescr.getDescrAltraPrescrizione() : prescr.getDescrTipoPrescrizione()%></td>
  	</tr>
<%
  }
%>
  </table>
</body>
</html>