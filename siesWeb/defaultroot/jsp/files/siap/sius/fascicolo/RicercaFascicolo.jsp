<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Collection" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>
<%@ page import="java.util.Collection"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="Nostato" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione" scope="request" class="java.lang.String"/>

<% 

String lAction = "siap.sige.sentenza.action.ActMultiAssegnazione";

Collection CodStato =(Collection) request.getAttribute("CodStato");

String lchecked = "";
//Controllo se il Titolo è stato passato nella request 
	String lTitolo = "Elenco Titoli Esecutivi";
	if (titolo != null && titolo.length() > 0)
	{
		lTitolo = titolo;
	}
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Titolo Esecutivo</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

<FORM name="RicercaFascicolo" >         

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo"><%=lTitolo%></font> </td>
    </tr>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio</td>
      <td class="int">Soggetto</td>
<%
	if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)){
%>
		<td class="int">Data di Nascita</td>	
<%		
	}
%>      
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Estremi Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <%-- 20170915: [SG] per SIGE si allo stato --%>
<% if (Nostato.length() < 1 && !codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)
		|| "SIGE".equals(Nostato) && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)) {
%>
      <td class="int">Stato</td>
<% }
%>
      <td class="int">Azioni</td>
      <td class="int">Sel.</td>
    </tr>

<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
      if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("021") )
        sUfficio = "Proc. Trib. "+ fascicolo.getDescrComuneUfficio();
      if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("007") )
        sUfficio = "Proc. Generale "+ fascicolo.getDescrComuneUfficio();
      // MEV10-s3: aggiunte casistiche per i minori
      if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("011") )
        sUfficio = "Proc. Trib. Minori "+ fascicolo.getDescrComuneUfficio();
      if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("012") )
          sUfficio = "Proc. Trib. Minori "+ fascicolo.getDescrComuneUfficio();
%>
      <tr>
        <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
        <td class="L"><font class="label"><%=sUfficio%></font></td>
<%        if (fascicolo.getSoggetto() != null)
        { %>
          <td class="L"><font class="label"><%=fascicolo.getSoggetto().getCognome() +" " +fascicolo.getSoggetto().getNome()%></font></td>
<%        }
        else
        { %>
          <td class="c">&nbsp;</td>
<%        } %>

<%
	if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)){
%>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%></font></td>
<%
	}
%>

        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font></td>

        <td class="L"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento() + " " + fascicolo.getSentenza().getDescrTipoAutoritaEmittente() + " " +fascicolo.getSentenza().getDescrLuogoEmittente() %></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita() ,"dd-MM-yyyy"), "-")%></font></td>
        <%-- 20170915: [SG] per SIGE si allo stato --%>
<% if (Nostato.length() < 1 && !codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)
		|| "SIGE".equals(Nostato) && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020012)) {
%>

       <td class="c"><font class="label"><%=StringUtils.toStringJSP(DecodificheUtils.getFiltrobyCode(CodStato, fascicolo.getCodStatoProcedimento()), "-")%></font></td>
<% }
%>

        <td class="c">
          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
             <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getIdFascicoloSiep()%>" />
          </jsp:include>
        </td>
        
  		<td width="10%" class="l">
			<input type="checkbox" name="tipoProcedimentoSiep" value=<%=fascicolo.getIdFascicoloSiep()%>>
		</td>
  	
      </tr>
<%
  }
%>

	 	<tr>
		    <td>
		      <input class="bottone" type="submit" value="Associa">
		    </td>
	  	</tr>
  
    </table>
    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    
  <br>
 </FORM>
 
  </body>
</html>