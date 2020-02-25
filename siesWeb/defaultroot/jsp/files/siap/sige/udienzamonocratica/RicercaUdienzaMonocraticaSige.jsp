<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="udienzamonocraticasige" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_ricerca" scope="request" class="java.lang.String"/>

<html>
	<head>
  	<title> Elenco Post Ricerca Udienza Monocratica</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	</head>

<body class="corpo">
<%

String lRet = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.udienzamonocratica.action.ActLoadRicercaUdienzaMonocraticaSige";
%>
	<FORM method="POST" action="Main.jsp" name="RicercaUdienzaMonocraticaSige">
  	<table>
    	<tr>
      	<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>
      	<td class="LBG">
					<font class="label">Funzione :</font>&nbsp;&nbsp;
        	<font class="campo">Elenco Udienza Monocratica</font>
      	</td>
      	
      	 <!-- BOTTONE DI RITORNO -->
  			        <td class="LBG">
          <a href="<%=lRet%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
       
  		</td>	
    	</tr>
  	</table>
  <% 
  	 //=============================================== 
     // Include della jsp che gestisce la paginazione 
     //=============================================== 
  %>
  <%
  if (tipo_ricerca.equals("paginata")) { %>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <%}
  %>

<div>
	<br>
  <table align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Data Udienza</td>
      <td class="int">Giudice Udienza</td>
      <td class="int">Giudice Assegnatario</td>
      <td class="int">Orario Inizio</td>
      <td class="int">Orario Fine</td>
      <td class="int">Azioni</td>
    </tr>
    <%
      Iterator itx = udienzamonocraticasige.iterator();
      while ( itx.hasNext()) {
        UdienzaSigeModel lUdienzaMonocraticaSige = (UdienzaSigeModel)itx.next();
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMonocraticaSige.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
			</td>
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getDescrGiudice(),"&nbsp;")%>
			</td>
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getDescrMagistratoAss(),"--")%>
	  </td>
      <td class=c>&nbsp;
      	<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getOraInizio(),"--")%>:
      	<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getMinInizio(),"--")%>
			</td>
			<td class=c>&nbsp;
				<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getOraFine(),"--")%>:
				<%=StringUtils.toStringJSP(lUdienzaMonocraticaSige.getMinFine(),"--")%>
			</td>
      <td class=c>
      <%
       String modificabile = "SI";
       // inserire qui i criteri in base ai quali il campo è modificabile
      %>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=lUdienzaMonocraticaSige.getIdUdienzaSige()%>"/>
          
           <jsp:param name="CampoCodMagis" value="<%=ICostantiUdienzaSige.CAMPO_COD_MAG_ASS%>"/>
           <jsp:param name="ValoreCodMagis" value="<%=lUdienzaMonocraticaSige.getCodMagistratoAss()%>"/>
          
					<jsp:param name="TornaQui" value="0"/>
        </jsp:include>
      </td>
    </tr>
    <% } // end while su iterator %>
  </table>
</div>
</FORM>
</body>
</html>