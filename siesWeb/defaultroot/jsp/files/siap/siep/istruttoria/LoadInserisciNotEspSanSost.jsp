<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IdIstruttoriaCumulo" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="datairrevocabilita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"   scope= "request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Notizie Espulsione Sanzione Sostitutiva</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    
    var desktop;
	function ListaQuesture(a_formname,a_fieldname)
	{
	     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaQuesture&formname="+a_formname+"&fieldname="+a_fieldname, "Lista_Questure","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}


   function Verify()
   {


    if(!document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.disabled)
    {
      if (document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>.value == '')
      {
         alert("Il Campo Sede è obbligatorio");
         return false;
      }
    } 

    if(!document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.disabled)
    {      
 	 if (document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.value.length==1)
			  document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.value='0'+document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.value;
     if (document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.value.length==1)
			  document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.value='0'+document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.value;

     var data_to_verify = document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.value+'/'+document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.value+'/'+document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_ANNO%>.value;


     if (!ControllaData(data_to_verify) )
	 {
        alert('Data Espulsione non valida');
		return false;
	 }
	}
     
	if (document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	var data_to_verify = document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
	{
        alert('Data richiesta non valida');
		return false;
	}
		  
    if(!CompareDate("<%=datairrevocabilita%>",data_to_verify))
    {
        alert("La Data richiesta del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
        document.LoadInserisciNotEspSanSost.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
    }


  }
  
  function radio()
  {
       var nodeDataEsp =document.getElementById('dataespulsione');
       
       if(document.LoadInserisciNotEspSanSost.tipo[0].checked)
       {
         nodeDataEsp.style.display='block'; 

                                 
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.disabled=false;
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.disabled=false;
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_ANNO%>.disabled=false;
 
       }
       else
       {
         nodeDataEsp.style.display='none'; 

                                 
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>.disabled=true;
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_MESE%>.disabled=true;
         document.LoadInserisciNotEspSanSost.<%=ICostantiIstruttoria.DATA_ESPULSIONE_ANNO%>.disabled=true;
 
       }
  
   }
  </script>

  </head>

 <body class="corpo" onload="radio();">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Notizie Espulsione Sanzione Sostitutiva</font>


      </td>
    </tr>
  </table>
<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>
  <FORM method="POST" name="LoadInserisciNotEspSanSost" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciNotEspSanSost">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>"	value="<%= IdIstruttoriaCumulo%>" >
    <table width='100%'>


   <tr>
    <td class="l" width='30%'> Destinatario </td>  
    <td class="l" colspan="4"> Questura - Ufficio Stranieri </td> 
   </tr> 
   <tr> 
    <td class="l" width='30%'>Luogo Destinazione</td>	
    <td class="l">
          <input title="Luogo Destinazione" value="" type="text" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>"  maxlength="35" size="35">
          <a href="Javascript:ListaQuesture('LoadInserisciNotEspSanSost','<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>');">
          <img src="/images/filefolder.gif" border=0></a>       
       </td>
            <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Indirizzo" name="<%= ICostantiIstruttoria.CAMPO_NOTE %>"  cols=30></textarea>
            </td>      
    </tr> 
    <tr>
        <td class="c" colspan='4'> 
          <input type="radio" name="tipo" checked onclick="radio();"> Richiesta documentazione di avvenuta espulsione        
          <input type="radio" name="tipo" onclick="radio();"> Richiesta verifica avvenuta espulsione
        </td>
    </tr>
    </table>

<div id="dataespulsione" style="display:none; float:left; position:relative; width:100%;">
  <table width='100%'>     
    <tr>
        <td class="l" width='30%'>Data Espulsione</td>
        <td class="L">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiIstruttoria.DATA_ESPULSIONE_GIORNO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiIstruttoria.DATA_ESPULSIONE_MESE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiIstruttoria.DATA_ESPULSIONE_ANNO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
    </tr> 
  </table>          
</div>  
  <table width='100%'>     
   <tr>
        <td class="l" width='30%'>Data richiesta</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
   </tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciNotEspSanSost");

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

</table>
	</form>

	</body>
</html>
	