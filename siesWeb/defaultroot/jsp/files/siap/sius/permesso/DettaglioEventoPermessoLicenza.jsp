<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sius.permesso.action.ICostantiEventoPermessoLicenza"%>

<jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel"/>
<%-- jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.PermessoDepositoDecretoModel"/--%>
<jsp:useBean id="eventoPermesso"	scope="request" class="siap.sius.permesso.model.EventoPermessoLicenzaModel"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  String lDescrTipoLicenza = "",lCodTipoLicenza = "";
	lCodTipoLicenza = permessoDepDecr.getLicenza().getCodTipoLicenza();
	//lCodTipoLicenza = permessoDepDecr.getLicenzaLibAnticipata().getCodTipoLicenza();
	if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA) )
	  lDescrTipoLicenza = "Licenza";
	else if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO) )
	  lDescrTipoLicenza = "Permesso";  

%>
<html>
	<head>
		<title>[S.I.A.P.] - Dettaglio Evento <%=lDescrTipoLicenza%> </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
	
  <body class="corpo">
  	<!-- form name="dettaglio"-->
    	<table>
      	<tr>
      		<td class="LBG">
      			<a href="Javascript:window.print();">
      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      			</a>
      		</td>
        	<td class="LBG"><font class="label">Funzione : </font>
          	<font class="campo">Dettaglio Evento <%=lDescrTipoLicenza%></font>&nbsp;
        	</td>
      		<td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            	<jsp:param name="Modificabile" value="<%=Modificabile%>" />
        			<jsp:param name="CampoIdEntita" value="<%=ICostantiEventoPermessoLicenza.CAMPO_ID_EVENTO_PERMESSO_LICENZA%>" />
        			<jsp:param name="ValoreIdEntita" value="<%=eventoPermesso.getIdEventoPermessoLicenza()%>" />
            </jsp:include>
					</td>
      		<!-- Inserisce il pulsante di ritorno -->
					<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      	</tr>
    	</table>
			
    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	<br>
    	<jsp:include page="<%=ICostantiPermesso.PG_SINTESI_DATI_PERMESSOLICENZA%>"/>    	
    	<br>

     	<table cellspacing=2 cellpadding=2 width="95%">
      	<tr>
        	<td class="Titolo" colspan=6>Esito Permesso </td>
      	</tr>
<%
			//LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenzaLibAnticipata();
				LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenza();

%>
        <tr>
          <td class="l">Esito Permesso</td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lLic.getDescrEsito(),"-")%></font></td>
        </tr>

        <tr>
          <td class="l">Giorni e Ore Non Fruite</td>
          <td class="l"><% if(lLic.getNumeroGiorniNoFruiti() != null) {%> giorni <font class="campo"> <%=" " + StringUtils.toStringJSP(lLic.getNumeroGiorni(),"-") + " "%> </font> <% } if(lLic.getNumeroOre() != null) {%> ore <font class="campo"> <%=" " + StringUtils.toStringJSP(lLic.getNumeroOreNoFruite(),"-")%> </font> <%}%></td>
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

     	<table cellspacing=2 cellpadding=2 width="95%">
      	<tr>
        	<td class="Titolo" colspan=6>Evento <%=lDescrTipoLicenza%> </td>
      	</tr>

        <tr>
    			<td class="l">Tipo evento osservato durante la fruizione</td>
      		<td class="L">
      			<font class="campo">
      				<%=StringUtils.toStringJSP(eventoPermesso.getDescrTipoEvento(),"-")%>
      			</font>
      		</td>
    		</tr>			
				<tr>
      		<td class="l">Descrizione evento </td>
      		<td class="L">
      		 	<font class="campo">
      				<%=StringUtils.toStringJSP(eventoPermesso.getDescrEvento(),"")%>
      			</font>
      		</td>
      	</tr>
      	<tr>
      		<td class="l">Data segnalazione evento<font class="ob">(*)</font> (gg/mm/aa) </td>
      		<td class="L">
      			<font class="campo">
      				<%=DateUtils.getDateToString ( eventoPermesso.getDataSegnalazione(), "dd/MM/yyyy" )%>
      			</font>
      		</td>    		    		
      	</tr>
				<tr>
      		<td class="l">Mittente Segnalazione </td>
      		<td class="L">
      			<font class="campo">
      				<%=StringUtils.toStringJSP(eventoPermesso.getMittenteSegnalazione(),"")%>
      			</font>
      		</td>
    		</tr>

				<tr>
    			<td class="l">Conseguenze evento osservato durante la fruizione</td>
      		<td class="L">
						<font class="campo">
      				<%=StringUtils.toStringJSP(eventoPermesso.getDescrTipoConseguenza())%>
      			</font>
      		</td>

    		</tr>

				<tr>
      		<td class="l">Descrizione conseguenze </td>
      		<td class="L">
      			<font class="campo">
      				<%=StringUtils.toStringJSP(eventoPermesso.getDescrConseguenze())%>
      			</font>
      		</td>
    		</tr>
    		
        <tr>
          <td colspan=2>&nbsp;</td>
        </tr>          
          
      </table>
	</body>	
</html>