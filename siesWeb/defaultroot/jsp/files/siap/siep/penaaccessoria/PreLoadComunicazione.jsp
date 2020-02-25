<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="dataInsFS"      						scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"              		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoComunicazione"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="IdPenaAccessoria" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoPenaAccessoria" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoPenaAccessoria"		scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria" 						scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>

<html>

<head>
<title>[S.I.E.S.] - Gestione Comunicazione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
  function init()
  {
    var data_fine_validità='<%=DateUtils.getDateToString(penaaccessoria.getDataFineValidita(), "dd/MM/yyyy")%>';
    if ( ControllaData(data_fine_validità) && data_fine_validità.length>2)
      alert('Attenzione! Pena Accessoria non più valida.');
    document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.focus();
  }

  var desktop;
  function Verify()
  {
    var descTipoPA="<%=DescrTipoPenaAccessoria%>";
		if(descTipoPA=='Altre Pene Accessorie')
    {
			if (document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value!='02' &&
			    document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value!='03'    )
			{
        alert('Tale tipo di Pena Accessoria può solo essere sostituito');
        return false;
			}
    }
    var data_fine_validità='<%=DateUtils.getDateToString(penaaccessoria.getDataFineValidita(), "dd/MM/yyyy")%>';
    if ( ControllaData(data_fine_validità) && data_fine_validità.length>2)
    {
      if( window.confirm('Attenzione! Pena Accessoria non più valida. Vuoi continuare?') );
      else
      	return false;
    }

		var flagCondonata='<%=penaaccessoria.getFlagCondonata()%>';

    if (document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value.length==1)
      document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value='0'+document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value;
    if (document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value.length==1)
      document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value='0'+document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value;

    var data_comunicazione=document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value+'/'+document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value+'/'+document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>.value;
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
    if (! ControllaData(data_comunicazione) && data_comunicazione.length>2)
    {
      alert('Data Comunicazione non valida');
      return false;
    }
    // Controllo della data Comunicazione <= data di sistema
    if (! CompareDate(data_comunicazione, data_sistema))
    {
      alert('Data Comunicazione > della data odierna');
      return false;
    }

    // Data inserimento Fascicolo Siep <= data comunicazione
    if ( !CompareDate( '<%=dataInsFS%>', data_comunicazione) )
    {
      alert('Data comunicazione minore della data di inserimento del fascicolo SIEP!');
      return false;
    }

    if (document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value == '-')
    {
      alert('Tipo Comunicazione obbligatorio');
      return false;
    }
    if (document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>.value == '-')
    {
      alert('Tipo Pena Accessoria obbligatorio');
      return false;
    }

    //alert("flagCondonata : "+flagCondonata);
    //alert("CAMPO_COD_TIPO_COMUNICAZIONE : "+document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value);
    if ((document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value !='05') &&
       ((flagCondonata=='C'   &&
         document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value !='01' ) ||
        (flagCondonata=='R'   &&
         document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value != '04') ||
        (flagCondonata=='S'   &&
         document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value != '02') ||
        (flagCondonata=='D'   &&
         document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value != '06') ||
        (flagCondonata=='T'   &&
         document.PreLoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>.value != '03')) )
    {
      alert("Tipo Comunicazione Non conforme al Tenore dell'ordinanza");
      return false;
    }

		return true;
  }
  </script>

</head>

  <body class="corpo" onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
			 PenaAccessoriaModel lPenaAccessoria = new PenaAccessoriaModel();
			 String lAzione = new String();
			 if( modalita.equals("I") )
			 {
			   lAzione = "siap.siep.penaaccessoria.action.ActLoadComunicazione";
%>
			   <font class="campo">Inserimento Comunicazione</font>
<%
       }
       else if( modalita.equals("M") )
       {
			   lAzione = "siap.siep.penaaccessoria.action.ActLoadModificaComunicazione";
%>
         <font class="campo">Modifica Comunicazione</font>
<%
       }
%>
      </td>
  		<!-- BOTTONE DI RITORNO -->
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="PreLoadComunicazione">
<%
		if (DescrTipoPenaAccessoria.length()>1)
    {%>
  		<table cellspacing=2 cellpadding=2>
				<tr>
      		<td class="label">Tipo Pena Accessoria</td>
      		<td class="campo"><%=DescrTipoPenaAccessoria%></td>
    		</tr>
			</table>
		<%}%>

  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Data Comunicazione <font class=ob>(*)</font></td>
      <td class="l">
        <input Title="Giorno Comunicazione" size=2 maxlength=2 type="text" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input Title="Mese Comunicazione" size=2 maxlength=2 type="text" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input Title="Anno Comunicazione" size=4 maxlength=4 type="text" name="<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
		</tr>
		<tr>
      <td class="l">Tipo Comunicazione<font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Tipo Comunicazione" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>">
            <%=tipoComunicazione%>
          </select>
        </td>
    </tr>
<%
		if (IdPenaAccessoria.length()<2)
    {%>
		<tr>
      <td class="l">Tipo di Pena Accessoria<font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Tipo Pena Accessoria" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>">
            <%=TipoPenaAccessoria%>
          </select>
        </td>
    </tr>
  <%}else{%>
  		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" value="<%=CodTipoPenaAccessoria%>" >
	<%}%>
		<br>
    <tr>
      <td>
        <input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=IdPenaAccessoria%>" >
  <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA%>" value="<%=DescrTipoPenaAccessoria%>" >

</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("PreLoadComunicazione");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE %>","req","Il campo Giorno Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE %>","req","Il campo Mese Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE %>","req","Il campo Anno Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>","lt=3000");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>