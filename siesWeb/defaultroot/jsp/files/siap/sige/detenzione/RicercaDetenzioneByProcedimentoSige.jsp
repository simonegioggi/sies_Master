<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.math.BigDecimal"%>


<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel" %>
<%@ page import="siap.sige.detenzione.model.FasSigeDetenzioneModel" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="luoghiDetenzione" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<% 
boolean modificabile = true;
if (isModificabile.length() > 0)
{
  if (isModificabile.equalsIgnoreCase("SI"))
  {
	  modificabile = true;
  }
  else
  {
	  modificabile = false;
  }
}%>

<%@page import="siap.sige.detenzione.action.ICostantiFasSigeDetenzione"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Luogo Detenzione per Procedimento SIGE </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Gestione Luogo Detenzione per Procedimento SIGE </font>
      </td>

<% if (modificabile) { %>
       <!-- BOTTONE DI INSERIMENTO LUOGO DETENZIONE -->
       <td class="LBG">
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.detenzione.action.ActLoadInserisciDetenzioneFasSige&IdFascicoloSige=<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>" >
           <img  align="middle" src="/images/new24.gif" alt="Assegna nuovo Luogo Detenzione" width="24" height="24" border="0">
         </a>
       </td>
<%} 
	if ( luoghiDetenzione.size()>0 ) { %>
  	<!-- BOTTONE DI CANCELLAZIONE ULTIMO LUOGO DETENZIONE -->
		<td class="LBG">
      <a href="Javascript:conferma('siap.sige.detenzione.action.ActCancellaDetenzioneFasSige','IdFascicoloSige','<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>','','');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella ultimo Luogo Detenzione" width="24" height="24" border="0">
  		</a>
		</td>
<%} 

%>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  <br>
<%
  BigDecimal lIdSoggetto = (FascicoloSigeEsteso.getSoggetto().getIdSoggetto() );
%>
  <table>
<%
  if ( luoghiDetenzione.size()==0 )
  {%>
    <tr>
      <td class="l">Nessun Luogo Detenzione assegnato</td>
		</tr>
<%}else{%>
  <tr>
    <td class=int>Data inizio</td>
    <td class=int>Data fine</td>
    <td class=int>Istituto Detenzione</td>
    <td class=int>Altro Luogo</td>
    <td class=int>Azione</td>

  </tr>
 <%
        Iterator itx = luoghiDetenzione.iterator();

        while ( itx.hasNext())
        {
        	FasSigeDetenzioneModel lDetSige = (FasSigeDetenzioneModel)itx.next();
        %>
        <tr>
	        <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDetSige.getLuogoDetenzione().getDataInizioDetenzione(),"dd-MM-yyyy"),"-")%></td>
	        <%-- 20170703: modifica per la data fine: se non esiste data fine pena prendo Data Fine Detenzione--%>
	        <%
	        if (FascicoloSigeEsteso.getFascicoloSige().getDataFinePena() != null) {
	        %>
	        <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy"),"-")%></td>
	        <%
	        } else {
	        %>
	        <td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDetSige.getLuogoDetenzione().getDataFineDetenzione(),"dd-MM-yyyy"),"-")%></td>
	        <%
	        }
	        %>
<%
			if(lDetSige.getLuogoDetenzione().getIstitutoDetenzione() != null){
%>	        
	        	<td class=l><%=StringUtils.toStringJSP(lDetSige.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto() +" di "+lDetSige.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione() ,"-")%></td>
<%
			} else {
%>	        
				<td class=l>&nbsp;</td>
<%
			}
%>	        
	        <td class=l><%=StringUtils.toStringJSP(lDetSige.getLuogoDetenzione().getAltroLuogo(),"-")%></td>
	
	        <td class="c">
	          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
	             <jsp:param name="CampoIdEntita" value="<%=ICostantiFasSigeDetenzione.CAMPO_ID_FAS_SIGE_DETENZIONE%>" />
	             <jsp:param name="ValoreIdEntita" value="<%=lDetSige.getIdFasSigeDetenzione()%>" />
	          </jsp:include>
	        </td>
        </tr>
      <%} }
%>
</table>

</body>
</html>