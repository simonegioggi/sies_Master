<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="modalita"              		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRichiestaGE"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="IdPenaAccessoria" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoPenaAccessoria" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoPenaAccessoria"		scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria" 						scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>

<html>

<head>
<title>[S.I.E.S.] - Gestione Richiesta al GE </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
  function init()
  {
    var data_fine_validità='<%=DateUtils.getDateToString(penaaccessoria.getDataFineValidita(), "dd/MM/yyyy")%>';
    if ( ControllaData(data_fine_validità) && data_fine_validità.length>2)
      alert('Attenzione! Pena Accessoria non più valida.');
    document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>.focus();
  }

  var desktop;
  function Verify()
  {
    var descTipoPA="<%=DescrTipoPenaAccessoria%>";
		if(descTipoPA=='Altre Pene Accessorie')
    {
			if (document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE%>.value!='07' &&
			    document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE%>.value!='06'    )
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
    if (document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>.value.length==1)
      document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>.value='0'+document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>.value;
    if (document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE%>.value.length==1)
      document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE%>.value='0'+document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE%>.value;

    var data_richiestaGE=document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>.value+'/'+document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE%>.value+'/'+document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE%>.value;
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
    if (! ControllaData(data_richiestaGE) && data_richiestaGE.length>2)
    {
      alert('Data Richiesta al GE non valida');
      return false;
    }
    // Controllo della data Richiesta al GE <= data di sistema
    if (! CompareDate(data_richiestaGE, data_sistema))
    {
      alert('Data Richiesta al GE > della data odierna');
      return false;
    }

    if (document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE%>.value == '-')
    {
      alert('Tipo Richiesta al GE obbligatorio');
      return false;
    }
    if (document.PreLoadRichiestaGE.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>.value == '-')
    {
      alert('Tipo Pena Accessoria obbligatorio');
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
			   lAzione = "siap.siep.penaaccessoria.action.ActLoadRichiestaGE";
%>
			   <font class="campo">Inserimento Richiesta al GE</font>
<%
       }
       else if( modalita.equals("M") )
       {
			   lAzione = "siap.siep.penaaccessoria.action.ActLoadModificaRichiestaGE";
%>
         <font class="campo">Modifica Richiesta al GE</font>
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

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="PreLoadRichiestaGE">
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
      <td class="l">Data Richiesta <font class=ob>(*)</font></td>
      <td class="l">
        <input Title="Giorno Richiesta" size=2 maxlength=2 type="text" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input Title="Mese Richiesta" size=2 maxlength=2 type="text" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input Title="Anno Richiesta" size=4 maxlength=4 type="text" name="<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
		</tr>
		<tr>
      <td class="l">Tipo Richiesta<font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Tipo Richiesta" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_RICHIESTA_GE%>">
            <%=tipoRichiestaGE%>
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
  var frmvalidator  = new Validator("PreLoadRichiestaGE");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_RICHIESTAGE %>","req","Il campo Giorno Richiesta al GE è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_MESE_DATA_RICHIESTAGE %>","req","Il campo Mese Richiesta al GE è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE %>","req","Il campo Anno Richiesta al GE è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_RICHIESTAGE%>","lt=3000");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>