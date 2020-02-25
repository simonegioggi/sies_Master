<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.lang.String" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="riferimenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Riferimenti altri titoli esecutivi </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Riferimenti altri titoli esecutivi&nbsp;</font></td>
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>

    <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      </tr>
    </table>
    <br>

    <table cellpadding=2 cellspacing=2>
    <tr>
      <td class="int">N. SIEP</td>
      <td class="int">Ufficio</td>
      <td class="int">Data Provvedimento</td>
      <td class="int">Tipo Provvedimento</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Sede Autorità</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Anno/Numero Provv.</td>
    </tr>

<%
    Iterator itx = riferimenti.iterator();
    while ( itx.hasNext())
    {
      RiferimentoFascicoloSiepModel lRFS = (RiferimentoFascicoloSiepModel)itx.next();
%>
      <tr>
        <td class="C"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.rifasiep.action.ActLoadDettaglioRifFascicoloSiep&<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>=<%=lRFS.getIdRiferimentoFascicoloSiep()%><%=retParam%>" >

<% 
	if( lRFS.getFlagMS() == null || lRFS.getFlagMS().equals("") || lRFS.getFlagMS().equals("N")) {
%>
            <%=lRFS.getAnnoFascicoloSiep()%>
            /
            <%=lRFS.getProgrFascicoloSiep()%>
<%
	} else {
		if (lRFS.getAnnoFascicoloSiep() == null && lRFS.getProgrFascicoloSiep() == null){
			if(lRFS.getFlagMS().equals("M")){
%>		
				Es. Mis. Sic.	
<%		
			} else {
%>
				Es. Pene Pec.
<%
			}
		} else {
			if(lRFS.getFlagMS().equals("M")){
%>
				<%=lRFS.getAnnoFascicoloSiep()%>
				/ 
				<%=lRFS.getProgrFascicoloSiep()%> MS

<%
			} else {
%>
				<%=lRFS.getAnnoFascicoloSiep()%>
				/ 
				<%=lRFS.getProgrFascicoloSiep()%> PP
<%				
			}
		}
	}
%>

          </a>

        </font></td>

        <td class="C"><%=lRFS.getDescrUffFascicoloSiep()%></td>
        <td class="C"><%=DateUtils.getDateToString(lRFS.getDataProvvedimento(),"dd/MM/yyyy") %></td>
        <td class="C"><%=lRFS.getDescrTipoProvvedimento()%></td>
        <td class="C"><%=lRFS.getDescrTipoAutoritaEmittente()%></td>
        <td class="C"><%=lRFS.getDescrLuogoEmittente()%></td>
<% if(lRFS.getDataIrrevocabilita() != null && !lRFS.getDataIrrevocabilita().equals("")){ %>
        <td class="C"><%=DateUtils.getDateToString(lRFS.getDataIrrevocabilita(),"dd/MM/yyyy") %></td>
<% } else { %>
		<td class="C"></td>
<% } %>
        <td class="C">
        
<% 
	if(lRFS.getAnnoProvvedimento() != null && lRFS.getNumeroProvvedimento() != null){
%>        
        <%=lRFS.getAnnoProvvedimento()%>/<%=lRFS.getNumeroProvvedimento()%>
      
<%
	} 
%>           
      	</td>
      </tr>
<%
    }
%>
    </table>
  </FORM>
</body>
</html>