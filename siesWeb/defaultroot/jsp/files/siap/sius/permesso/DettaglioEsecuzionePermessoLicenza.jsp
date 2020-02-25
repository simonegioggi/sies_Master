<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso"%>
<%@ page import="siap.sius.permesso.action.ICostantiEventoPermessoLicenza"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>

<jsp:useBean id="Modificabile"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel"/>

<%-- jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.PermessoDepositoDecretoModel"/--%>
<%
	String lDescrTipoLicenza = "",lCodTipoLicenza = "";
	lCodTipoLicenza = permessoDepDecr.getLicenza().getCodTipoLicenza();
	
	if( lCodTipoLicenza.equalsIgnoreCase( ICostantiLicenzaLibanticipata.LICENZA ))
	  lDescrTipoLicenza = "Licenza";
	else if( lCodTipoLicenza.equalsIgnoreCase( ICostantiLicenzaLibanticipata.LICENZA_INTERNATO ))
	  lDescrTipoLicenza = "Licenza per Internato";	
	else if( lCodTipoLicenza.equalsIgnoreCase( ICostantiLicenzaLibanticipata.PERMESSO_PREMIO ))
	  lDescrTipoLicenza = "Permesso";
	else if( lCodTipoLicenza.equalsIgnoreCase( ICostantiLicenzaLibanticipata.PERMESSO_INTERNATO ))
	  lDescrTipoLicenza = "Permesso per Internato";	
%>	
<html>
	<head>
		<title>[S.I.A.P.] - Dettaglio Esecuzione <%=lDescrTipoLicenza%> </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
	
  <body class="corpo">
    	<table>
      	<tr>
      		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        	<td class="LBG"><font class="label">Funzione : </font>
          	<font class="campo">Dettaglio Esecuzione <%=lDescrTipoLicenza%></font>&nbsp;
        	</td>
      	  <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    		</tr>
    	</table>
			
    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	<br>
    	<jsp:include page="<%=ICostantiPermesso.PG_SINTESI_DATI_PERMESSOLICENZA%>"/>
    	<br>

     	<table cellspacing=2 cellpadding=2 width="95%">
      	<tr>
        	<td class="Titolo" colspan=6>Esito <%=lDescrTipoLicenza%></td>
      	</tr>
<%
//        LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenzaLibAnticipata();
					LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenza();
%>
        <tr>
        	<td class="l">Esito <%=lDescrTipoLicenza%></td>
          <td class="l">
          	<font class="campo">
          		<%=StringUtils.toStringJSP(lLic.getDescrEsito(),"-")%>
          	</font>
          </td>
          <td class="c">
        		<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
        			<jsp:param name="Modificabile" value="<%=Modificabile%>" />
          		<jsp:param name="CampoIdEntita" value="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" />
          		<jsp:param name="ValoreIdEntita" value="<%=lLic.getIdLicenzaLibanticipata()%>" />
        		</jsp:include>
      		</td>
        </tr>          
        <tr>
         	<td class="l">Giorni e Ore Non Fruite</td>
          <td class="l">
          <% if(lLic.getNumeroGiorniNoFruiti() != null) {%> giorni 
          		<font class="campo"> 
          		<%=" " + StringUtils.toStringJSP(lLic.getNumeroGiorniNoFruiti(),"-") + " "%> 
          	</font> 
          <% } 
          	 if(lLic.getNumeroOreNoFruite() != null) {%> ore 
          	<font class="campo"> 
          		<%=" " + StringUtils.toStringJSP(lLic.getNumeroOreNoFruite(),"-")%> 
          	</font> 
          <% }%>
          </td>
        </tr>
        <tr>
        	<td class="l">Data annotazione esito</td>
          <td class="l">
          	<font class="campo">
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lLic.getDataAnnotazioneEsito(), "dd/MM/yyyy"))%>
          	</font>
          </td>
        </tr>
        <tr>
        	<td colspan=2>&nbsp;</td>
        </tr>          
      </table>
  		<jsp:include page="<%=ICostantiEventoPermessoLicenza.PG_ELENCOEVENTOPERMESSOLICENZA%>"/>  	  	
    <br>
	</body>	
</html>