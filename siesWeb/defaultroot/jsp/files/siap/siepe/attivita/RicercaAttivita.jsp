<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siepe.attivita.action.ICostantiAttivita" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%@ page import="siap.siepe.attivita.model.AttivitaModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>

<jsp:useBean id="attivita" scope="request" class="java.util.Vector"/>

<%
	String lFunAnnullaValidazione = "siap.siepe.attivita.action.ActAnnullaValidazioneAttivita";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Attività </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class="label">Funzione :</font>
      <font class="campo"> Elenco Attività </font> </td>
    </tr>
  </table>

	<br>
		<jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
	<br>

  <table>
    <div align="center">
      <tr>
        <td class="int" width=35%>Tipo Attività</td>
        <td class="int" width=10%>Data Inizio Attività</td>
        <td class="int" width=10%>Data Chiusura Attività</td>
        <td class="int" width=5%>Attività Validata</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
    </div>
<%
  Iterator itx = attivita.iterator();
  while ( itx.hasNext())
  {
    AttivitaModel lAttivita = (AttivitaModel)itx.next();
%>
    <tr>
    	<td class="l"><%=lAttivita.getDescrTipoAttivita()%></td>
			<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAttivita.getDataInizio(),"dd-MM-yyyy"), "-")%></td>
			<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAttivita.getDataChiusura(),"dd-MM-yyyy"), "-")%></td>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      		<%--<td class="l"><%=StringUtils.toStringJSP(richiesta.getFlagDocumentoRegistrato())%></td>--%>
			<td class="c">
        <%
				if ( (lAttivita.getFlagDocumentoRegistrato()!=null) && (lAttivita.getFlagDocumentoRegistrato().compareTo("S")==0 ) )
        {
           // SVALIDAZIONE
           //if (richiesta.getNumAllValidati() < 1 && lFunAnnullaValidaProvvedimento.length() > 1 && modificabile)
           //{
        %>
           	<a href="Javascript:annulla('Vuoi annullare la validazione dell\'attività ? ','<%=lFunAnnullaValidazione%>' ,'<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>','<%=lAttivita.getIdAttivita()%>');">
          		<img src="/images/TickRed.gif" alt = "Annulla validazione attivita"  border="0">
          	</a>
        <% 	//}
						//else
          	//{ %>
          	<!--	<img src="/images/TickRed.gif"> -->
        <%	//}
				}
      	else
      	{
				%>
          -
        <%
        }
				%>
			</td>

			<td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lAttivita.getIdAttivita()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </form>
  <br>
  </body>
</html>