<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutoritaAltra" scope="request" class="java.lang.String"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="notifica" scope="request" class="siap.siep.notifica.model.NotificaModel"/>

<html>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<head>
<title>[S.I.E.S.] - Rinnovazione Notifica comma 8 bis</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">

function Verify()
{

    if (document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value.length==1)
			  document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value='0'+document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value;
    if (document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value.length==1)
			  document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value='0'+document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value;

		  var data_to_verify = document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value+'-'+document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value+'-'+document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value;

     <%-- Ticket#202106220110 - Aggiunti controlli di obbligatorietà sui campi --%>
     <%-- if (!ControllaDataPassaVuota(data_to_verify) ) --%>
     if (!ControllaData(data_to_verify) )
		  {
       alert('Data Rinnovo non valida');
			 return false;
		  }
      
      if (document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value=='-') {
        alert("Indicare l'autorità delegata alla notifica");
			  return false; 
      }
      
      if (document.LoadRinnovazioneNotifica.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.value.length==0) {
        alert("Indicare la sede dell'autorità delegata alla notifica");
			  return false; 
      }    
      <%-- Ticket#202106220110 - FINE --%>

      return true;

}


  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaNotifiche(a_formname,a_fieldname)
  {
   var a_fieldname_1 = null
   if(document.LoadRinnovazioneNotifica.Notifica[1].checked)
    {
      a_fieldname_1="FP";
    }else  if(document.LoadRinnovazioneNotifica.Notifica[0].checked)
      {
        a_fieldname_1="UG";
      }
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadRicercaRinnovazioneNotifica&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname_1="+a_fieldname_1, "LoadRinnovazioneNotifica","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=500");
  }


</script>
</head>


<body class="corpo">

  <form name="LoadRinnovazioneNotifica" method="POST" action="/jsp/Main.jsp">

<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Rinnovazione Notifica comma 8 bis</font>
      </td>
     </tr>
</table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <table>
      <tr>
        <td class="LGB">
          <a href="Javascript:ListaNotifiche('LoadRinnovazioneNotifica',<%=notifica.getIdNotifica()%>);">Elenco Notifiche Precedenti</a>
        </td>
      </tr>
    </table>

    <table  width=100%>
     <tr>
       <td class="c">A mezzo Ufficiali Giudiziari &nbsp;<input type="radio" name="Notifica" value="UG" checked>
                     A mezzo Autorità di Polizia  &nbsp; <input type="radio" name="Notifica" value="FP">
       </td>
    </tr>
   </table>

<table width="100%">
     <tr>
      <td class="l">Risposta Pervenuta in Data <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <input title="Giorno Pervenimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				-
        <input title="Mese Pervenimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Anno Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
		</tr>
     <tr>
       <td class="l">Autorità delegata alla notifica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
            <%=tipoAutoritaAltra%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo <font class="ob">(*)</font></td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadRinnovazioneNotifica','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
         <td class="l">Indirizzo</td>
         <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE%>" cols=30></textarea>
         </td>
     </tr>

      <tr>
         <td class="l">Luogo Nuova Notifica</td>
         <td class="L" colspan="3">
            <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA%>"  maxlength="35" size="35">
         </td>
      </tr>

     <tr>
      <td>
       <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActInserisciRinnovazioneNotifica">

       <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
      </td>
    </tr>
  </table>
    <input type="hidden" name="idnotifica" value="<%=notifica.getIdNotifica()%>">
    <input type="hidden" name="idevento"   value="<%=evento.getIdEvento()%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRinnovazioneNotifica");

   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>","numeric","Il campo Giorno rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>","numeric","Il campo Mese rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","numeric","Il campo Anno rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","gt=1900");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","lt=2099");


   frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>