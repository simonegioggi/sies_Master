<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" %>


<jsp:useBean id="ListaOrdinanze" scope="request" class="java.util.Vector" />
<jsp:useBean id="Funzione" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>{S.I.E.S.] - Lista Ordinanze del soggetto nel distretto - </title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <script language="JavaScript">
      function insertIT(str,str1,str2,str3,str4,str5,competente)
      { 
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname1")%>.value=str1;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=str2;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname3")%>.value=str3;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname4")%>.value=str4;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname5")%>.value=str5;
         if (window.parent.opener.loadUfficiAccorpati) {
            window.parent.opener.loadUfficiAccorpati('elencoOrdinanzeNelDistretto',competente);
        }  
        window.parent.close();
      }
    </script>
  </head>

  <body onload="focus();">
   <table>
      <tr>
        <td class="LBG"><%=Funzione%></td>
      </tr>
   </table>
   
   <% if(ListaOrdinanze!=null && ListaOrdinanze.size()>0){ %>
   <table width="96%">
   <div align=center>
<%
      Iterator itx = ListaOrdinanze.iterator();

      while ( itx.hasNext())
      {
    	  ProvvedimentoSigeEventoModel provvSigeEvento = (ProvvedimentoSigeEventoModel)itx.next();
%>
 		<tr>
			<td class="int">Data Emissione</td>
			<td class="int">Anno/Numero Provvedimento</td>
			<td class="int">Tipologia</td> 
			<td class="int" >Azioni</td>
        </tr>
	</div>
        <tr>
        	<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSigeEvento.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"))%></td>
        	<td class="l"><%=StringUtils.toStringJSP(provvSigeEvento.getProvvedimento().getChiaveAnno(),"-")%>/<%=StringUtils.toStringJSP(provvSigeEvento.getProvvedimento().getChiaveProgr(),"-")%></td>
        	<td class="l"><%=StringUtils.toStringJSP(provvSigeEvento.getProvvedimento().getDescrTipoProvvedimentoSige(),"-")%></td>
        	
          	<td class="c"><a href="Javascript:insertIT('<%=StringUtils.toStringJSP(StringUtils.cStrForJS(provvSigeEvento.getEventoNotifica().getEvento().getDescrLuogoEmittente()) ,"-")%>'
          	                                          ,'<%if(provvSigeEvento.getProvvedimento().getChiaveAnno()!= null){%><%=StringUtils.cStrForJS(provvSigeEvento.getProvvedimento().getChiaveAnno().toString())%><%}%>'
          	                                          ,'<%if(provvSigeEvento.getProvvedimento().getChiaveProgr()!= null){%><%=StringUtils.cStrForJS(provvSigeEvento.getProvvedimento().getChiaveProgr().toString())%><%}%>'
          	                                          ,'<%=StringUtils.cStrForJS(DateUtils.getDateToString(provvSigeEvento.getProvvedimento().getDataEmissione(),"dd"))%>'
          	                                          ,'<%=StringUtils.cStrForJS(DateUtils.getDateToString(provvSigeEvento.getProvvedimento().getDataEmissione(),"MM"))%>'
          	                                          ,'<%=StringUtils.cStrForJS(DateUtils.getDateToString(provvSigeEvento.getProvvedimento().getDataEmissione(),"yyyy"))%>'
          	                                          ,'<%=StringUtils.toStringJSP(StringUtils.cStrForJS(provvSigeEvento.getEventoNotifica().getEvento().getCodTipoUfficioEmittente()) ,"-")%>')">
          	                      <img align="middle" src="/images/fileselected.gif" border=0>
          	              </a>
          	</td>
        </tr>
<%
    }// chiusura while
%>
  </table>
<%
    } // chiusura dell'if
    else{
%>
      <table width="96%">
        <tr>
        <td class="label">Non esistono ordinanze nel distretto per il soggetto presente nella sessione di lavoro.</td>
      </tr>
      </table>
  <%
    } // chiusura else
  %>
  </body>
  <script language=javascript>
    window.focus();
  </script>
</html>