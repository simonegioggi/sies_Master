<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siepe.ricezioneatti.model.CruscottoModel" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio" %>

<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack"/>
<jsp:useBean id="lista"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="OrdineDate"     scope="request" class="java.lang.String"/>

<%
// Preparazione del bottone di riordino date

  String lRet = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.ricezioneatti.action.ActRicercaCruscotto&OrdineDate=" + OrdineDate;

/*
  String lRet = "#";
  if (StackDiRitorno != null)
  {
    if (!StackDiRitorno.isEmpty())
    {
      lRet = (String) StackDiRitorno.peek();
      lRet  += "&" + IWebConstants.FLAG_RITORNO + "=1";
      lRet  += "&OrdineDate=" + OrdineDate;
    }
  }
*/
%>


<html>
<head>
  <title>[S.I.E.S.] - Cruscotto SIEPE</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Cruscotto Atti in ricezione &nbsp;</font>
      </td>
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
       </tr>
   </table>
<br>
<%
  if (lista.size() == 0)
  {
%>
  <table>
        <td class="LBG">
          <font class="label"> Non ci sono messaggi </font>
        </td>
  </table>
<% }else { %>
  <table >
    <tr valign="middle">
    	<td valign="top" class="int" width=18%>Data di ricezione
  			<a href="<%=lRet%>">
  				<img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="inverti ordine" border="0">
  			</a>&nbsp;&nbsp;
    	</td>
    	<td class="int" width=15%>Ricevuti</td>
    	<td class="int" width=15%>Presi in Visione</td>
    	<td class="int" width=15%>Non Trattati</td>
    	<td class="int" width=15%>Presi in Carico</td>
    	<td class="int" width=15%>Restituiti</td>
    </tr>
<%
    Iterator itx = lista.iterator();
    int i=0;
    while ( itx.hasNext())
    {
      CruscottoModel lCruscotto = (CruscottoModel)itx.next();
%>
    <tr>
      <td class="c">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy"),"-") %>
    	</td>
	      <td class="c" ><% if (lCruscotto.getNumAttiRicevuti().intValue() > 0)  {%>
  	       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRicercaAttiPerDataPerFlag&<%=ICostantiMessaggio.CAMPO_DATA_INVIO%>=<%=DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy")%>&TornaQui=<%=TornaQui%>">
    	    <%=StringUtils.toStringJSP(lCruscotto.getNumAttiRicevuti(),"-")%>
      	  </a>
        	<%} else { %> <%=StringUtils.toStringJSP(lCruscotto.getNumAttiRicevuti(),"-")%>
        	<%}%></td>
  	    <td class="c" ><% if (lCruscotto.getNumAttiPresiInVisione().intValue() > 0)  {%>
    	     <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRicercaAttiPerDataPerFlag&<%=ICostantiMessaggio.CAMPO_DATA_INVIO%>=<%=DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy")%>&<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>=V&TornaQui=<%=TornaQui%>">
      	  <%=StringUtils.toStringJSP(lCruscotto.getNumAttiPresiInVisione(),"-")%>
        	</a>
        	<%} else { %> <%=StringUtils.toStringJSP(lCruscotto.getNumAttiPresiInVisione(),"-")%>
	        <%}%></td>
	      <td class="c" ><% if (lCruscotto.getNumAttiNuovi().intValue() > 0)  {%>
  	       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRicercaAttiPerDataPerFlag&<%=ICostantiMessaggio.CAMPO_DATA_INVIO%>=<%=DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy")%>&<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>=N&TornaQui=<%=TornaQui%>">
    	    <%=StringUtils.toStringJSP(lCruscotto.getNumAttiNuovi(),"-")%>
      	  </a>
	        <%} else { %> <%=StringUtils.toStringJSP(lCruscotto.getNumAttiNuovi(),"-")%>
  	      <%}%></td>
    	  <td class="c" ><% if (lCruscotto.getNumAttiPresiInCarico().intValue() > 0)  {%>
      	   <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRicercaAttiPerDataPerFlag&<%=ICostantiMessaggio.CAMPO_DATA_INVIO%>=<%=DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy")%>&<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>=S&TornaQui=<%=TornaQui%>">
        	<%=StringUtils.toStringJSP(lCruscotto.getNumAttiPresiInCarico(),"-")%>
        	</a>
        	<%} else { %> <%=StringUtils.toStringJSP(lCruscotto.getNumAttiPresiInCarico(),"-")%>
       	 <%}%>
     	 	</td>
    	  <td class="c" ><% if (lCruscotto.getNumAttiRestituiti().intValue() > 0)  {%>
      	   <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRicercaAttiPerDataPerFlag&<%=ICostantiMessaggio.CAMPO_DATA_INVIO%>=<%=DateUtils.getDateToString(lCruscotto.getDataRicezione(),"dd-MM-yyyy")%>&<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>=R&TornaQui=<%=TornaQui%>">
        	<%=StringUtils.toStringJSP(lCruscotto.getNumAttiRestituiti(),"-")%>
        	</a>
        	<%} else { %> <%=StringUtils.toStringJSP(lCruscotto.getNumAttiRestituiti(),"-")%>
       	 <%}%>
     	 	</td>
    	</td>
  <%
     i++;
   } // endwhile
 } // endif
  %>
  </table>
  </body>
</html>