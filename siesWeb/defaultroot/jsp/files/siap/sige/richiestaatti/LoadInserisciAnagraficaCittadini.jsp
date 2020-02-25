<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>

<jsp:useBean id="datairrevocabilita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"           scope= "request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Accertamento Anagrafica</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

   function Verify()
   {


    if(!document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.disabled)
    {
      if (document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.value == '')
      {
         alert("Il Campo Sede è obbligatorio");
         return false;
      }
    } 
      
    if(!document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.AUTORITA%>.disabled)
    {
      if (document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.AUTORITA%>[document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.AUTORITA%>.selectedIndex].value == '-')
      {
         alert("Il Campo Autorita di polizia è obbligatorio");


         return false;
      }
    }  
     
  if (document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

  var data_to_verify = document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
  {
        alert('Data emissione non valida');
    return false;
  }
      
    if(CompareDate(data_to_verify,"<%=datairrevocabilita%>"))
    {
        alert("La Data Emissione del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
        document.LoadInserisciAnagraficaCittadini.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
    }


  }
  
  function radio()
  {
       var nodeanagrafe =document.getElementById('anagrafe');
       var nodeautorita =document.getElementById('autorita');
       
       if(document.LoadInserisciAnagraficaCittadini.tipo[0].checked || document.LoadInserisciAnagraficaCittadini.tipo[1].checked)
       {
         nodeanagrafe.style.display='block'; 
         nodeautorita.style.display='none'; 
                                 
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.disabled=false;
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.AUTORITA%>.disabled=true;
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA%>.disabled=true;
 
       }
       else
       {
         nodeanagrafe.style.display='none'; 
         nodeautorita.style.display='block'; 
                                 
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.disabled=true;
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.AUTORITA%>.disabled=false;
         document.LoadInserisciAnagraficaCittadini.<%=ICostantiIstruttoria.SEDE_AUTORITA%>.disabled=false;
 
       }
  
   }

  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }
  </script>

  </head>

 <body class="corpo" onload="radio();">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Accertamenti Anagrafici</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
  
<FORM method="POST" name="LoadInserisciAnagraficaCittadini" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActInserisciAccertamentiAnagrafici">
    <table width='100%'>

   <tr><td class="Titolo" colspan=3>Tipo Accertamento  </td></tr>
  <tr>
    <td class="c" colspan='3'>
     Rituale &nbsp;<input type="radio" name="tipo" value="R" checked onclick="radio();">
     &nbsp; Accertamento residenza &nbsp; <input type="radio" name="tipo" value="AR" onclick="radio();"> 
     &nbsp; Richiesta Informazioni Autorità di Polizia &nbsp;<input type="radio" name="tipo" value="RI" onclick="radio();"> 
    </td>
   </tr>
     <tr><td class="Titolo" colspan=3>Destinatario  </td></tr>   
    </table>

<div id="anagrafe" style="display:none; float:left; position:relative; width:100%;">
  <table width='100%'>
   <tr>
    <td class="l" colspan='3'>    
          Ufficio Anagrafe di : &nbsp;
          <input title="Sede Ufficio Anagrafe" value="" type="text" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciAnagraficaCittadini','<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>');">
          <img src="/images/filefolder.gif" border=0></a>       
       </td>
    </tr> 
  </table>          
</div>   

<div id="autorita" style="display:none; float:left; position:relative; width:100%;">
  <table width='100%'>
   <tr>
          <td class="l" width=30%>Autorità di Polizia <font class=ob>(*)</font></td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna" name="<%=ICostantiIstruttoria.AUTORITA%>">
             <%=autorita %>
             </select>
         </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiIstruttoria.SEDE_AUTORITA%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciAnagraficaCittadini','<%=ICostantiIstruttoria.SEDE_AUTORITA%>');">
             <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
      <tr>
        <td class="l">Indirizzo <font class="ob"></font></td>
        <td class="L">
           <input value="" type="text" size="35" maxlength="40" name="<%= ICostantiIstruttoria.TIPO_DOCUMENTO %>"  >
        </td>
      </tr>
       <td class="l">Note</td>
       <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiIstruttoria.CAMPO_NOTE%>"  cols=30 ></textarea>
        </td>      
    </tr>  
   </table>          
</div>      

  <table width='100%'>
   <tr><td class="Titolo" colspan=2>Oggetto  </td></tr>

   <tr>
        <td class="l">Data Emissione : </td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		  
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciAnagraficaCittadini','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
   </tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>


</table>
  </form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciAnagraficaCittadini");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");

 </script>

  </body>
</html>
  