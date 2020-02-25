<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="dataInsFS"								scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario1"						scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario2"  					scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario3"						scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoRichiestaGE"			scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoRichiestaGE"		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataRichiestaGE_GG"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataRichiestaGE_MM"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataRichiestaGE_AA"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoPenaAccessoria"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoPenaAccessoria"	scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPeneAccessorie"		scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria"						scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.siep.penaaccessoria.action.ActRichiestaGE";
  String IdPenaAccessoria = request.getParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);

	// Parametri per i controlli della form.
  int LungDest1 = Destinatario1.length() ;
  int LungDest2 = Destinatario2.length() ;
  int LungDest3 = Destinatario3.length() ;
%>
	<script language="JavaScript">
    var desktop;
  	function ListaComuni(a_formname,a_fieldname)
  	{
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
    function ListaUfficiPerTipo(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

	</script>

<script language="JavaScript">
// La funzione attiva il campo di Descrizione Altre PA.
 function cambia()
 {
    //alert("cambia");
    var desTipoComunicazione;
    var desTipoPenaAccessoria;
    var cod;
    desTipoRichiestaGE = '<%=DescrTipoRichiestaGE%>';
    desTipoPenaAccessoria = '<%=DescrTipoPenaAccessoria%>';
    if (desTipoRichiestaGE.indexOf("ostituzione")>0 )
    {
    	document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].focus();
    	cod = document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO%>.value;
      document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_DESCR_ALTRE_PA%>.value="";
    }
   	return;
 }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Esecuzione Pena Accessoria </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario 1 è obbligatoria');
        return false;
      }

  		if ('<%=LungDest2%>'!='0')
			{
      	if (document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value != '-'
        	  && document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1].value == '')
      	{
        	alert('La Sede del destinatario 2 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest3%>'!=0)
			{
      	if (document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value != '-'
        	  && document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2].value == '')
      	{
        	alert('La Sede del destinatario 3 è obbligatoria');
        	return false;
      	}
			}

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value == '-' &&
				 (('<%=LungDest2%>'!=0 &&
							document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value == '-') ||
				  ('<%=LungDest2%>'==0)) &&
				 (('<%=LungDest3%>'!=0 &&
							document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value == '-') ||
				  ('<%=LungDest3%>'==0)) )
      {
        alert('Inserire almeno un Destinatario.');
        return false;
      }

      return true;
    }
  </script>
</head>

<body class="corpo" onload="javascript:cambia();" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Richiesta di <%=DescrTipoRichiestaGE%></font>
      </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Torna Indietro" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRichiestaGE">
    <table style="width: 95%;">

<%		if(IdPenaAccessoria == null)
			{%>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="l">
        		<select class="small" name="<%= ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA %>"  >
          		<%=TipoPenaAccessoria%>
        		</select>
      		</td>
				</tr>
    <%}else{%>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="campo"><%=DescrTipoPenaAccessoria%></td>
				</tr>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" value="<%=CodTipoPenaAccessoria%>" >
    <%}%>
      <tr>
        <td class="l">Tipo di Richiesta</td>
        <td class="campo"><%=DescrTipoRichiestaGE%></td>
      </tr>

      <tr>
        <td class="l">Data Richiesta </td>
        <td class="campo"><%=DataRichiestaGE_GG%>/<%=DataRichiestaGE_MM%>/<%=DataRichiestaGE_AA%>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE%>" value="<%=CodTipoRichiestaGE%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE%>" value="<%=DataRichiestaGE_AA%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE%>" value="<%=DataRichiestaGE_MM%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>" value="<%=DataRichiestaGE_GG%>" >
        </td>
      </tr>
<%------------%>
<%		if(DescrTipoRichiestaGE.indexOf("ostituzione")>0)
			{%>
    		<tr><td class="Titolo" colspan=4>Estremi Pena Accessoria in Sostituzione</td></tr>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="l">
        		<select class="small" name="<%= ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO %>"  onchange="javascript:cambia();" >
          		<%=TipoPenaAccessoria%>
        		</select>
      		</td>
				</tr>

    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_DESCR_ALTRE_PA%>" value="" >

    <%}else{%>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO%>" value="<%=CodTipoPenaAccessoria%>" >
    <%}%>
    </table>

		<%------------%>
    <table style="width: 95%;">
    <tr><td class="Titolo" colspan=4>Destinatari</td></tr>
      <!-- Primo destinatario + luogo -->
      <tr>
        <td class="l">Destinatario n°1</td>
        <td class="L" colspan=3>
         <table>
           <tr>
           <td class="l"> Ufficio </td>
           <td class="l">
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario1 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaUfficiPerTipo('LoadRichiestaGE','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0]', document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0][document.LoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>


        	</td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Secondo destinatario + luogo -->
<%  	if (Destinatario2.length()>2)
			{%>
       	<tr>
        	<td class="l">Destinatario n°2</td>
        	<td class="L" colspan=3>
         		<table>
           		<tr >
           			<td class="l"> Autorità </td>
           			<td class="l" >
             			<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               			<%= Destinatario2 %>
             			</select>
           			</td>
           		</tr>
           		<tr>
           			<td class="l">Sede <font class=ob>(*)</font></td>
           			<td class="l">
             			<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              			value="" type="text" maxlength="35" size="35" >
                 	<a href="Javascript:ListaComuni('LoadRichiestaGE','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1]');">
              			<img src="/images/filefolder.gif" border=0> </a>
        				</td>
        			</tr>
        			<tr>
          			<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          			<td class="L" colspan=3>
           				<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          			</td>
        			</tr>
        		</table>
     		</tr>
		<%}%>

    	<!-- Terzo destinatario + luogo -->
<%  	if (Destinatario3.length()>2)
			{%>
      	<tr>
        	<td class="l">Destinatario n°3</td>
        	<td class="L" colspan=3>
         	<table>
           	<tr >
           		<td class="l"> Autorità </td>
           		<td class="l" >
             		<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               		<%= Destinatario3 %>
             		</select>
           		</td>
           	</tr>
           	<tr>
           		<td class="l">Sede <font class=ob>(*)</font></td>
           		<td class="l">
             		<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              		value="" type="text" maxlength="35" size="35" >
              		<a href="Javascript:ListaComuni('LoadRichiestaGE','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2]');">
              		<img src="/images/filefolder.gif" border=0> </a>
        			</td>
        		</tr>
        		<tr>
          		<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          		<td class="L" colspan=3>
           			<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          		</td>
        		</tr>
        </table>
      </tr>
		<%}%>


      <!-- Campo Note + campo hidden -->
      <tr>
          <td class="l">Nota 1</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1 ></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>
      <tr>
          <td class="l">Nota 2</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=IdPenaAccessoria%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" value="<%=CodTipoPenaAccessoria%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA%>" value="<%=DescrTipoPenaAccessoria%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRichiestaGE");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>