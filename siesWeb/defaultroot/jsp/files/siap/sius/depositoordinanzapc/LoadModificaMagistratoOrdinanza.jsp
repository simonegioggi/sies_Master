<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>

<jsp:useBean id="modalita"    			scope="request" class="java.lang.String"/>
<jsp:useBean id="acdest"      			scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"  			scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="utente"      			scope="request" class="siap.sico.utente.model.UtenteModel"/>

<html>
	<head>
    <title>[S.I.E.S.] - Gestione Magistrato Relatore</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript">
    	// Chiamata liste
    	function chiamaLista(a_formname) {
      	var desktop;
      	// Elenco magistrati.
  	  	desktop = 
  		  	window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    	}
    
    	function chiamaListaSingola(a_formname){
      	var desktop;
      	// Elenco magistrati.
      	desktop = 
    	  	window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
   	 }
  </script>

  <script language="JavaScript">
    // Funzione dei controlli formali della form
    function Verifica() {
      	if (document.LoadModificaMagistratoOrdinanza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value == "") {
          alert('Scegliere il Magistrato Relatore selezionando un magistrato!');
          return false;
      	}
      	if (document.LoadModificaMagistratoOrdinanza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != CodMag
          	&& document.LoadModificaMagistratoOrdinanza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != "") {
          document.LoadModificaMagistratoOrdinanza.magistratoMod.value='YES';
        }
      	if (document.LoadModificaMagistratoOrdinanza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value == CodMag){
          if (document.LoadModificaMagistratoOrdinanza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>.value != ""){
          	alert('Il Magistrato Relatore selezionato è uguale a quello già assegnato!');
            return false;
          }
      	}
      	return true;
    }
  </script >
  </head>

  <body class="corpo">  
	  <table>
	    <tr> 
	      <td class="LBG">
	      	<a href="Javascript:window.print();">
	      		<img align="middle" src="/images/quickprint24.gif" alt="Stampa questa videata" border=0>
	      	</a>
	     	</td>
	    	<td class="LBG">
	     		<font class="label">Funzione :</font>&nbsp;
	        <font class="campo">Modifica Magistrato Ordinanza </font>
	      </td>
	      <!-- BOTTONE DI RITORNO -->
	    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	   	</tr>
		</table>
    
    <% 
    	// preleva l'id evento daa riportare in avanti nella action chiamata.
    	String lIdEventoGenerato = (String)request.getParameter("IdEvento");
   		String lAzione = new String();
     	if( modalita.equals("M") ) {
       	lAzione = "siap.sius.depositoordinanzapc.action.ActModificaMagistratoOrdinanza";      
    %>
    <%
      }
    %>
  
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
  <FORM method="POST" name="LoadModificaMagistratoOrdinanza" action="<%= IWebConstants.PG_MAIN%>">
    <table cellspacing=2 cellpadding=2 width="100%">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Magistrato</td>
      <td class="L" >
        <input readonly  title="Cognome Magistrato" 
        	   	 value="<%=StringUtils.toStringJSP(magistrato.getCognome())%>" type="text" 
        	   	 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
        <input readonly  title="Nome Magistrato"    
							 value="<%=StringUtils.toStringJSP(magistrato.getNome())%>" 
							 type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"    
							 maxlength="35" size="25">
      </td>
    </tr>

<% if(utente.getUfficioUtente().getCodTipoUfficio().compareTo("TDS") == 0 ) 
   {%>
    <tr>
      <td class="L" >
        <a href="Javascript:chiamaLista('LoadModificaMagistratoOrdinanza');">
        	Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
<% }else{%>
    <tr>
      <td class="L" >
        <a href="Javascript:chiamaListaSingola('LoadModificaMagistratoOrdinanza');">
					Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
<% }%>

    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
      </td>
    </tr>
  </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"                 value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"   value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" >
    <input type="HIDDEN" name="magistratoMod"  																	value="NO">
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_EVENTO_GENERATO%>" value="<%=lIdEventoGenerato%>" >
    <input type="HIDDEN" name="acdest"         																	value="<%=acdest%>">
  </form>

  <script language="JavaScript">
    //Codice eseguito sempre
    //Memorizza il codice dell'attuale magistrato.
    var CodMag = "<%=StringUtils.toStringJSP(magistrato.getCodMagistrato())%>";
  </script>
 </body>
 </html>