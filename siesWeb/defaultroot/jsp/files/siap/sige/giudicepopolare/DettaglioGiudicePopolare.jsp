<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>

<jsp:useBean id="giudicepopolare" scope="request" class="siap.sige.giudicepopolare.model.GiudicePopolareModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Giudice Popolare </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Giudice Popolare</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>" />
            <jsp:param name="ValoreIdEntita" value="<%=giudicepopolare.getIdGiudicePopolare()%>" />
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
      </table>
    </FORM>

    <table cellspacing="4" cellpadding="4">
    	<tr>
      	<td class="l">Cognome</td>
        <td class="l">
        	<font class="campo"><%=giudicepopolare.getCognome() %></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Nome</td>
        <td class="l">
        	<font class="campo"><%=giudicepopolare.getNome() %></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Codice Fiscale</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getCodiceFiscale(),"-") %></font>
        </td>
      </tr>

			<tr>
      	<td class="l">Sesso</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getCodSesso())%></font>
        </td>
      </tr>

     	<tr>
     		<td class="l">Data Nascita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(giudicepopolare.getDataNascita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Stato di Nascita</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getDescrStatoNascita())%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Comune di Nascita</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getDescrComuneNascita(),"-")%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Comune di Nascita Estero</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getComuneEsteroNascita(),"-")%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Indirizzo</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getIndirizzo(),"-")%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Ruolo</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getDescrRuolo())%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(giudicepopolare.getDescrSezione())%></font>
        </td>
      </tr>

     	<tr>
     		<td class="l">Data Inizio Validita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(giudicepopolare.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>
      
			<tr>
      	<td class="l">Data Fine Validita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(giudicepopolare.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>
    </table>
   </body>
</html>