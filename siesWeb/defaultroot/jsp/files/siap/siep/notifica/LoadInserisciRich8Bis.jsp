<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"  />
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"  />
<jsp:useBean id="notifica" scope="request" class="java.util.Vector"  />
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"  />


<html>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<head>
<title>[S.I.E.S.] - Richiesta Informazioni Comma 8 Bis</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">

function Verify()
{
 if (document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadRichiestaInfo.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

     if (!ControllaDataPassaVuota(data_to_verify))
		  {
       alert('Data Decreto non valida');
			 return false;
		  }

if(document.LoadRichiestaInfo.Notifica[0].checked)
 {
    if (document.LoadRichiestaInfo.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value.length==1)
			  document.LoadRichiestaInfo.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value;
		  if (document.LoadRichiestaInfo.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value.length==1)
			  document.LoadRichiestaInfo.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value;

		  var data_to_verify = document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value;

     if (!ControllaDataPassaVuota(data_to_verify) )
		  {
       alert('Data Richiesta non valida');
			 return false;
		  }
      if(document.LoadRichiestaInfo.numAvv.value == 2)
      {
           if (document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value.length==1)
			         document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value;
		        if (document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value.length==1)
			         document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value;

		       var data_to_verify = document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value;

           if (!ControllaDataPassaVuota(data_to_verify) )
		        {
                alert('Data Richiesta non valida');
			           return false;
		         }
          }
   }
  else
  {
      if (document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value.length==1)
             document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value;
      if (document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value.length==1)
             document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value='0'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value;

      var data_to_verify = document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value+'-'+document.LoadRichiestaInfo.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value;

      if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data Richiesta non valida');
          return false;
        }
  }
}
function radio()
{
  var nodeDivUno =document.getElementById('divDifUno');
  var nodeDivDue =document.getElementById('divDifDue');
  var nodeAltri =document.getElementById('divAltri');
  var nodeBottone =document.getElementById('divBottone');

// if per gestire Autorità Polizia o Ufficiali giudiziari
  if(document.LoadRichiestaInfo.Notifica[0].checked)
    {
        nodeDivUno.style.visibility='visible';
        document.LoadRichiestaInfo.primoAvvocato.value="S";

        if(document.LoadRichiestaInfo.numAvv.value == 2)
        {
          nodeDivDue.style.visibility='visible';
          nodeDivDue.style.top='10px';
          nodeBottone.style.top='520px';
          document.LoadRichiestaInfo.secondoAvvocato.value="S";
          document.LoadRichiestaInfo.altri.value="N";
         }
         else
         {
          nodeDivDue.style.visibility='hidden';
          nodeBottone.style.top='400px';
         }
         nodeAltri.style.visibility='hidden';

      }else if(document.LoadRichiestaInfo.Notifica[1].checked)
      {
          nodeAltri.style.visibility='visible';
          nodeDivUno.style.visibility='hidden';
          nodeDivDue.style.visibility='hidden';
          nodeAltri.style.top='220px';
          nodeBottone.style.top='350px';
          document.LoadRichiestaInfo.altri.value="S";
          if(document.LoadRichiestaInfo.numAvv.value == 2)
          {
           document.LoadRichiestaInfo.secondoAvvocato.value="N";
          }
           document.LoadRichiestaInfo.primoAvvocato.value="N";

      }
  }


  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaNotifiche(a_formname,a_fieldname)
  {
   var a_fieldname_1 = null
   if(document.LoadRichiestaInfo.Notifica[0].checked)
    {
      a_fieldname_1="FP";
    }else  if(document.LoadRichiestaInfo.Notifica[1].checked)
      {
        a_fieldname_1="UG";
      }
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadRicercaNotificaOmessa&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname_1="+a_fieldname_1, "RicercaNotificaOmessa","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=500");
  }
</script>
</head>
<body class="corpo" onLoad="radio();">

  <form name="LoadRichiestaInfo" method="POST" action="/jsp/Main.jsp">

  <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richiesta Informazioni Comma 8 Bis</font>
      </td>
     </tr>
    <input type="hidden" name ="evento" value="<%=evento.getIdEvento()%>">

</table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<table width="100%">
     <tr>
      <td class="l"  width="40%">Decreto Di Sospensione Emesso in Data</td>
      <td class="l">
        <input title="Giorno Decreto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Mese Decreto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Anno Decreto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
		</tr>
</table >

<table width="100%">
   <tr>
       <td class="c">Richiesta Informazioni al Difensore &nbsp;<input type="radio" name="Notifica" value="FP" checked  onClick="radio();">
                      Richiesta Informazioni ad altri  &nbsp;<input type="radio" name="Notifica" value="UG"  onClick="radio();">
       </td>
    </tr>
</table>
<%

   List lAvvSiep = new ArrayList();
   Iterator iter = notifica.iterator();
   while (iter.hasNext())
   {
    NotificaModel lNotMod = (NotificaModel)iter.next();
    lAvvSiep.add(lNotMod.getAvvSiep());
   }

   AvvocatoSiepModel lAvv= (AvvocatoSiepModel) lAvvSiep.get(0);
   NotificaModel lNotModel = (NotificaModel)eventonotifica.getNotifiche()[0];
%>
    <input type="hidden" name ="idPrimaNotifica" value="<%=lNotModel.getIdNotifica()%>">
    <input type="hidden" name="numAvv" value="<%=lAvvSiep.size()%>">


<div id="divDifUno" style="visibility:hidden; position:relative; width:100%;">
<table width="100%">

    <%if(lAvv != null && lAvv.getAvvocato() != null)
     {%>
     <tr>
      <td class="l" width=25%>Avvocato</td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="l" colspan=3>
         <font class="campo"><%=lAvv.getAvvocato().getCognome()%>&nbsp;<%=lAvv.getAvvocato().getNome()%></font>
      &nbsp; Foro di &nbsp;<font class="campo"><%=lAvv.getAvvocato().getForo()%></font>
     </td>
		</tr>
    <tr>
      <td class="l">Tipo Difensore</td>
      <td class="l" colspan=3>
         <font class="campo"><%=lAvv.getAvvocato().getDescrTipo()%></font>
    </td>
		</tr>
    <%}%>
    <input type="hidden" name ="primoAvvocato" value="">
    <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l" colspan=3>
          <input title="Giorno Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input title="Mese Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Richiesta" size=4 maxlength=4 value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
    </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadRichiestaInfo','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
        <td class="l">Indirizzo</td>
         <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE%>" cols=30></textarea>
         </td>
        </tr>
</table>
</div>
<div id="divDifDue" style="visibility:visible; position:relative; width:100%;">
<table width="100%">
<%if(lAvvSiep.size()>1)
{
  AvvocatoSiepModel lAvvSecondo= (AvvocatoSiepModel)lAvvSiep.get(1);
  NotificaModel lNotMod =(NotificaModel)eventonotifica.getNotifiche()[1];
%>
    <input type="hidden" name ="idSecondaNotifica" value="<%=lNotMod.getIdNotifica()%>">
    <input type="hidden" name ="secondoAvvocato" value="">
<%if(lAvvSecondo != null && lAvvSecondo.getAvvocato() != null)
 {%>
     <tr>
      <td class="l" width=25%>Avvocato</td>
      <td class="l" colspan="3">
          <font class="campo"><%=lAvvSecondo.getAvvocato().getCognome()%>&nbsp;<%=lAvvSecondo.getAvvocato().getNome()%> </font>
      &nbsp; Foro di &nbsp;<font class="campo"><%=lAvvSecondo.getAvvocato().getForo()%> </font>
     </td>
		</tr>
    <tr>
      <td class="l">Tipo Difensore</td>
      <td class="l" colspan="3">
         <font class="campo"><%=lAvvSecondo.getAvvocato().getDescrTipo()%></font>
    </td>
		</tr>
<%}%>
    <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l" colspan="3">
          <input title="Giorno Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>"  >
          -
          <input title="Mese Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>">
          -
          <input title="Anno Richiesta" size=4 maxlength=4 value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>"  >
       </td>
    </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_A%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadRichiestaInfo','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A %>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
         <td class="l">Indirizzo</td>
         <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE_A%>" cols=30></textarea>
         </td>
    </tr>

<%}%>
</table>
</div>
<div id="divAltri" style="visibility:hidden; position:absolute; width:100%;">
<table width="100%">
<%
if(eventonotifica.getNotifiche()[0].getCodTipoNotifica().equals("E"))
{
 NotificaModel lNotModAltri =(NotificaModel)eventonotifica.getNotifiche()[0];

%>

    <input type="hidden" name ="idAltraNotifica" value="<%=lNotModAltri.getIdNotifica()%>">

<%}%>
<tr>
    <input type="hidden" name ="altri" value="">

       <td class="l" >Data Richiesta</td>
       <td class="l" colspan="3">
          <input title="Giorno Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input title="Mese Richiesta" size=2 maxlength=2 value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Richiesta" size=4 maxlength=4 value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
    </tr>
    <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadRichiestaInfo','<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
         <td class="l">Indirizzo</td>
         <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE_AL%>" cols=30></textarea>
         </td>
      </tr>
</TABLE>
</DIV>

<div id="divBottone" style="visibility:visible; position:absolute; width:100%;">
  <table>
     <tr>
      <td>
       <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActInserisciRich8Bis">
       <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
      </td>
    </tr>
  </table>
</div>

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRichiestaInfo");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

if(document.LoadRichiestaInfo.Notifica[0].checked)
 {
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>","numeric","Il campo Giorno Richiesta è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>","numeric","Il campo Mese Richiesta è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","numeric","Il campo Anno Richiesta è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","lt=2099");
  if(document.LoadRichiestaInfo.numAvv.value == 2)
  {
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>","numeric","Il campo Giorno Richiesta è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>","numeric","Il campo Mese Richiesta è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","numeric","Il campo Anno Richiesta è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","gt=1900");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","lt=2099");
  }
 }
 else
 {
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>","numeric","Il campo Giorno verbale è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>","numeric","Il campo Mese verbale è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","numeric","Il campo Anno verbale è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","lt=2099");
 }
</SCRIPT>
</body>
</html>