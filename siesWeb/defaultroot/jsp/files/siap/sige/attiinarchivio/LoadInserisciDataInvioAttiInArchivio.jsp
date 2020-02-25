<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.attiinarchivio.action.ICostantiAttiInArchivio"%>
<%@page import="f3b.util.DateUtils"%>
<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.web.IWebConstants"%>
<%@page import="siap.sico.evento.model.EventoModel" %>
<%@page import="siap.sico.evento.action.ICostantiEvento"%>
<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<jsp:useBean id="tipoAtti" scope="request" class="java.lang.String" />
<jsp:useBean id="lEve" scope="request" class="siap.sico.evento.model.EventoModel" />

<%
String ulterioreDescrizione=(lEve.getDescrizioneInvioAtti()==null?"":lEve.getDescrizioneInvioAtti());
String gg=StringUtils.toStringJSP( DateUtils.getDateToString(lEve.getDataInvioAtti() ,"dd"));
String mm=StringUtils.toStringJSP( DateUtils.getDateToString(lEve.getDataInvioAtti() ,"MM"));
String aaaa=StringUtils.toStringJSP( DateUtils.getDateToString(lEve.getDataInvioAtti() ,"yyyy"));
String idEvento="";

if (lEve.getIdEvento() != null)
	idEvento=lEve.getIdEvento().toString();

%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Atti in Archivio </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  
  
  <script language="JavaScript">
  function calendario(a_formname,a_field_year,a_field_month,a_field_day)    {
      desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
  
  function Verify()  {
	  var dataEmissione=document.LoadInserisciDataAttiInArchivio.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
                        document.LoadInserisciDataAttiInArchivio.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
                        document.LoadInserisciDataAttiInArchivio.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
                        
      if (! ControllaData(dataEmissione)) {
          alert('Data invio atti in archivio non valida!');
          return false;
      }
      
      if (document.LoadInserisciDataAttiInArchivio.<%=ICostantiAttiInArchivio.CAMPO_COD_TIPO_INVIO_DATI_IN_ARCHIVIO%>.value == '-') {
    	  alert('Il campo Tipologia Invio atti in Archivio è Obbligatorio');
          return false;
      }
  }
  	
  </script>

</head>

<!-- body class="corpo" onload="Javascript:return ControlloAvvocato();"-->
<body class="corpo">

  <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
      <td class="LBG">
      	<font class="label"> Funzione :</font>&nbsp;

       	<font class="campo">Inserimento Data Atti in Archivio</font>
      </td>

  	<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <tr>
	  	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" 
				name="LoadInserisciDataAttiInArchivio">
				
	  <table cellspacing="2" cellpadding="2" width="100%">
  	  <tr>
      <td class="l" width="20%">Data Invio atti in Archivio (*)</td>
      <td class="L" width="80%">
        <input type="text" 
        	name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"
        	value="<%=gg %>"
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" 
        	value="<%=mm %>" 
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	value="<%=aaaa %>" 
        	size="4" maxlength="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
			
			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciDataAttiInArchivio','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
      </td>
      </tr>
      <tr>
      <td class="l" width="20%">Tipologia Invio atti in Archivio(*)</td>
      <td class="l" width="80%"><select title="Tipologia Invio atti in Archivio" name="<%=ICostantiAttiInArchivio.CAMPO_COD_TIPO_INVIO_DATI_IN_ARCHIVIO%>">
        <%=tipoAtti %>
      </select>
      </td>
    </tr>
    <tr>
    <td class="l" width="20%">Ulteriore Descrizione</td>
    <td class="l" width="80%">
        <TEXTAREA title="Ulteriore Descrizione" name="<%= ICostantiAttiInArchivio.CAMPO_ULTERIORE_DESCRIZIONE%>"  cols=80 rows=5 >  <%=ulterioreDescrizione%></textarea>
     </td>
    </tr>
    </tr>
    
    	
      </table>
	<br>


    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.attiinarchivio.action.ActInserisciDataInvioAttiInArchivio" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=idEvento %>" >  
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciDataAttiInArchivio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    
    
    
    frmvalidator.setAddnlValidationFunction("Verify");
</script>

  </body>
</html>