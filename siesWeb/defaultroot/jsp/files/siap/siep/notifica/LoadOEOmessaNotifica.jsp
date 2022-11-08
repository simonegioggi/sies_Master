<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.verbale.model.VerbaleModel" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"  />
<jsp:useBean id="tipoAutoritaAltra" scope="request" class="java.lang.String"  />
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"  />
<jsp:useBean id="notifica" scope="request" class="siap.siep.notifica.model.NotificaModel"  />
<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"  />

<html>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<head>
<title>[S.I.E.S.] - Omessa Notifica</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">

	function Verify()
	{
		if(document.LoadOEOmessaNotifica.Notifica[0].checked)
		{
			if(document.LoadOEOmessaNotifica.Rinnovo[1].checked)
			{
	    	if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value.length==1)
				  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value;
			  if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value.length==1)
				  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value;

			  var data_to_verify = document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value;

	     if (!ControllaDataPassaVuota(data_to_verify) )
			  {
	       alert('Data Rinnovo non valida');
				 return false;
			  }
		}
		else
		{
	    if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value;
		  if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value;

		  var data_to_verify = document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value;

     if (!ControllaDataPassaVuota(data_to_verify) )
		  {
       alert('Data Rinnovo non valida');
			 return false;
		  }

  }
}else if(document.LoadOEOmessaNotifica.Notifica[1].checked)
 {
    if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value;
		  if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value;

		  var data_to_verify = document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>.value;

     if (!ControllaDataPassaVuota(data_to_verify) )
		  {
       alert('Data Rinnovo non valida');
			 return false;
		  }
 if(document.LoadOEOmessaNotifica.Ufficiali[1].checked)
 {
    if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value;
		  if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value;

		  var data_to_verify = document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value;

     if (!ControllaDataPassaVuota(data_to_verify) )
		  {
       alert('Data Rinnovo non valida');
			 return false;
		  }
 }else
   {
    if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value;
		  if (document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value.length==1)
			  document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value='0'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value;

		  var data_to_verify = document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value+'-'+document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value;

     if (!ControllaDataPassaVuota(data_to_verify) )
		  {
       alert('Data Rinnovo non valida');
			 return false;
		  }
   }
 }
  document.LoadOEOmessaNotifica.INSERISCI.disabled=true;
}

function radio()
{
  var nodeFor =document.getElementById('divForz');
  var nodeDel =document.getElementById('divdelegato');
  var nodeUff =document.getElementById('divUff');
  var nodeRi =document.getElementById('divRicerca');
  var nodeNo =document.getElementById('divNotifica');
  var nodebottone =document.getElementById('divBottone');

  var mese = <%=DateUtils.getSysDate("MM")%>;
  var giorno = <%=DateUtils.getSysDate("dd")%>;
  var anno = <%=DateUtils.getSysDate("yyyy")%>;

  if (mese<10)
    mese ='0'+mese;
  if (giorno<10)
    giorno='0'+giorno;

  var lverbale = <%=verbale.getIdVerbale()%>;


// if per gestire Autorità Polizia o Ufficiali giudiziari
  if(document.LoadOEOmessaNotifica.Notifica[0].checked)
    {

     // if per gestire autorità delegata
      if(document.LoadOEOmessaNotifica.Rinnovo[1].checked)
      {
        nodeDel.style.visibility='visible';

        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value = giorno;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value = mese;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value = anno;

        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value = "";
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value = "";
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value = "";


      }else
      {
        nodeDel.style.visibility='hidden';

        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value = giorno;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value = mese;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value = anno;

        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value = "";
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value = "";
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value = "";

      }


       nodeFor.style.visibility='visible';
       nodeUff.style.visibility='hidden';
       nodeRi.style.visibility='hidden'
       nodeNo.style.visibility='hidden'

       nodeDel.style.top='310px';
       nodebottone.style.top='390px';

     if(lverbale == null)
      {
       if (window.confirm("Non Esiste Nessun Verbale Vane Ricerche Associato all'OE con Sospensione.\n\t Vuoi andare al Verbale Vane Ricerche ?"))
        {
          var str = "/jsp/Main.jsp?Action=siap.siep.verbale.action.ActLoadInserisciVerbaleVaneRicerche&FlagOmesse=S";
          window.location.href=str;
        }
      }

    }else
      {

        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value = giorno;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value = mese;
        document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>.value = anno;

        // if per gestire la ricerca o notifica dell'Ufficile giudiziario
           if(document.LoadOEOmessaNotifica.Ufficiali[0].checked)
           {
             nodeNo.style.visibility='visible';
             nodeRi.style.visibility='hidden';

             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value = giorno;
             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value = mese;
             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value = anno;

             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value = "";
             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value = "";
             document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value = "";


           }else
           {
            nodeNo.style.visibility='hidden';
            nodeRi.style.visibility='visible';

            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value = giorno;
            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value = mese;
            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value = anno;

            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value = "";
            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value = "";
            document.LoadOEOmessaNotifica.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value = "";

           }

           nodeFor.style.visibility='hidden';
           nodeDel.style.visibility='hidden';
           nodeUff.style.visibility='visible';

           nodeNo.style.top='270px';
           nodeRi.style.top='270px';
           nodebottone.style.top='370px';
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
   if(document.LoadOEOmessaNotifica.Notifica[0].checked)
    {
      a_fieldname_1="FP";
    }else  if(document.LoadOEOmessaNotifica.Notifica[1].checked)
      {
        a_fieldname_1="UG";
      }
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notifica.action.ActLoadRicercaNotificaOmessa&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname_1="+a_fieldname_1, "RicercaNotificaOmessa","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=500");
  }


</script>
</head>

<%-- <%if(verbale == null) --%>
<!-- //   { -->
<!-- //     verbale = new VerbaleModel(); -->
<%--   }%> --%>


<body class="corpo" onLoad="radio();">

  <form name="LoadOEOmessaNotifica" method="POST" action="/jsp/Main.jsp">

<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Rinnovo Ricerche Ordine Esecuzuine per Omesse Notifiche</font>
      </td>
     </tr>
</table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <table  width=100%>

   <tr>
       <td class="c">Omessa Notifica Forza di Polizia &nbsp;<input type="radio" name="Notifica" value="FP" checked  onClick="radio();">
                     Omessa Notifica Ufficiali Giudiziari  &nbsp; <input type="radio" name="Notifica" value="UG"  onClick="radio();">
       </td>
    </tr>


  </table>
    <table>
      <tr>
        <td class="LGB">
          <a href="Javascript:ListaNotifiche('LoadOEOmessaNotifica',<%=notifica.getIdNotifica()%>);">Elenco Notifiche Precedenti</a>
        </td>
      </tr>
    </table>

<div id="divForz" style="width: 100%; visibility:hidden; position:absolute; " >
<table width="100%">

     <tr>
      <td class="l" width="35%">Data pervenimento del verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%>&nbsp;</font></td>
		</tr>

    <tr>
       <td class="l" >Data verbale</td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</font> </td>
    </tr>

      <tr>
        <td class="l">Autorità che ha redatto il verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())%></font> di
         <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font>
        </td>
      </tr>
<table width=100%>
      <tr>
       <td class="l" >Rinnovo stessa autorità in data &nbsp;<input type="radio" name="Rinnovo" value="RS" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
       <td class="l">Rinnovo altra autorità in data &nbsp;<input type="radio" name="Rinnovo" value="RA" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
      </tr>
</table>
</table>
</div>

<div id="divdelegato" style="width: 100%; visibility:hidden; position:absolute; " >
<table width="100%">
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
            <%=tipoAutoritaAltra%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadOEOmessaNotifica','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
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
<div id="divUff" style="width: 100%; visibility:hidden; position:absolute; " >
<table width=100%>
      <tr>
         <td class="l">Relata di notifica da Ufficiali Giudiziari di</td>
         <td class="L">
      <%if(notifica != null && notifica.getAutoritaEsterna() != null &&
              notifica.getAutoritaEsterna().getCodSede() != null &&  notifica.getAutoritaEsterna().getCodTipoAutorita() != null
              &&  notifica.getAutoritaEsterna().getCodTipoAutorita().equals("22"))
           {%>
            <input title="Luogo" type="text"  value="<%=notifica.getAutoritaEsterna().getDescrSede()%>" name="<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>"  maxlength="35" size="35">
         <%}else
            {%>
            <input title="Luogo" type="text" name="<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>"  maxlength="35" size="35">
          <%}%>
             <a href="Javascript:ListaComuni('LoadOEOmessaNotifica','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>');">
              <img src="/images/filefolder.gif" border=0>
             </a>
         </td>
       <td class="l" >in data
        <input value="" type="text" size="2" maxlength="2"  name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
        <input value="" type="text" size="2" maxlength="2"  name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
        <input value="" type="text" size="4" maxlength="4"  name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
      </tr>
</table>
<table width=100%>
      <tr>
       <td class="l" >Rinnovo notifica in data &nbsp;<input type="radio" name="Ufficiali" value="RN" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
       <td class="l">Attivazione ricerca in data &nbsp;<input type="radio" name="Ufficiali" value="AR" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       </td>
      </tr>
</table>
</div>
<div id="divNotifica" style="width: 100%; visibility:hidden; position:absolute; " >
<table width=100%>
      <tr>
         <td class="l">Ufficiali Giudiziari delegati in</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadOEOmessaNotifica','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
      </tr>
      <tr>
         <td class="l">Luogo Nuova Notifica</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA%>"  maxlength="35" size="35">
         </td>
      </tr>
</table>
</div>
<div id="divRicerca" style="width: 100%; visibility:hidden; position:absolute; " >
<table width=100%>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG%>">
            <%=tipoAutoritaAltra%>
          </select>
        </td>
      </tr>

      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadOEOmessaNotifica','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
      </tr>
      <tr>
         <td class="l">Luogo Nuova Notifica</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA_UG_AR%>"  maxlength="35" size="35">
         </td>
      </tr>
</table>
</div>
<div id="divBottone" style="width: 100%; visibility:visible; position:absolute; " >
  <table>
     <tr>
      <td>
       <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActInserisciOEOmessaNotifica">
       <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
      </td>
    </tr>
  </table>
</div>
    <input type="hidden" name="idnotifica" value="<%=notifica.getIdNotifica()%>">
    <input type="hidden" name="idevento"   value="<%=evento.getIdEvento()%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadOEOmessaNotifica");

if(document.LoadOEOmessaNotifica.Notifica[0].checked)
 {
 if(document.LoadOEOmessaNotifica.Rinnovo[1].checked)
 {
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>","numeric","Il campo Giorno rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>","numeric","Il campo Mese rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","numeric","Il campo Anno rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>","alphabetic","Il campo luogo rinnovo è alfabetico")
 }else
  {
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>","numeric","Il campo Giorno rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>","numeric","Il campo Mese rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","numeric","Il campo Anno rinnovo è numerico");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","gt=1900");
   frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>","lt=2099");
  }
}else if(document.LoadOEOmessaNotifica.Notifica[1].checked)
 {

  frmvalidator.addValidation("<%=  ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>","alphabetic","Il campo Ufficiali Giudiziario alfabetico")

  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>","numeric","Il campo Giorno relata è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>","numeric","Il campo Mese relata è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>","numeric","Il campo Anno relata è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>","lt=2099","Il campo luogo relata rinnovo è alfabetico");



 if(document.LoadOEOmessaNotifica.Ufficiali[1].checked)
 {
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>","numeric","Il campo Giorno rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>","numeric","Il campo Mese rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","numeric","Il campo Anno rinnovo è numerico");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>","lt=2099");

  frmvalidator.addValidation("<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>","alphabetic","Il campo luogo rinnovo rinnovo è alfabetico")
 }else
   {
    frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>","numeric","Il campo Giorno rinnovo è numerico");
    frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>","numeric","Il campo Mese rinnovo è numerico");
    frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","numeric","Il campo Anno rinnovo è numerico");
    frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>","lt=2099");

    frmvalidator.addValidation("<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>","alphabetic","Il campo luogo rinnovo rinnovo è alfabetico")
   }
 }

   frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>