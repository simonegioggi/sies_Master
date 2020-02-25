<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="autorita"   scope="request" class="java.lang.String"/>
<jsp:useBean id="IdIstruttoriaCumulo"     scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Estratto Sentenza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
    
  // Lista popup dei Comuni per Tipo Ufficio
    function ListaComuniperTipoUfficio(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
 
    function Verify()
    {      
      // Campo TipoAutorità OBBLIGATORIO
      if (document.LoadInserisciEstrattoSentenze.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>.value=='-'){
        alert("Selezionare la tipologia di Autorità Destinatario");
        document.LoadInserisciEstrattoSentenze.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>.focus();
        return false;
      }

      // Campo Sede OBBLIGATORIO
      if(document.LoadInserisciEstrattoSentenze.<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>.value=="" ||
         document.LoadInserisciEstrattoSentenze.<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>.value.length == 0)
      {
        alert('Il campo Luogo Destinazione è OBBLIGATORIO!');
        document.LoadInserisciEstrattoSentenze.<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>.focus();
        return false;
      }      
      
      
      // Data Emissione
      if (document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
          
      // Data Emissione deve essere <= SYSDATE
       var sysDate = new Date();

       var ggSysDate = sysDate.getDate();
       if(ggSysDate<10)
         ggSysDate = "0"+ggSysDate;

       var mmSysDate = (sysDate.getMonth()+1);
       if(mmSysDate<10)
         mmSysDate = "0"+mmSysDate;

       var yyyySysDate = sysDate.getYear()

       var strSysDate = ggSysDate + "/" + mmSysDate + "/" + yyyySysDate;

       if (!CompareDate(data_to_verify, strSysDate))
       {
          alert('Data di Emissione superiore alla data attuale');
          document.LoadInserisciEstrattoSentenze.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
       }
    }
    
    // Caricamento POPUP
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IdIstruttoriaCumulo;
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActCaricaElencoTitoliPerIstruttorieRichieste&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"    
                          , "Elenco_Titoli"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=800, height=500");
    } 
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
  </script>

  </head>

 <body class="corpo" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font> &nbsp;&nbsp;
        <font class="campo">Inserisci Richiesta Estratto Sentenza</font>
      </td>
      
      <td class="LBG">
        <a href="javascript:history.go(-1);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <FORM method="POST" name="LoadInserisciEstrattoSentenze" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciEstrattoSentenza">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>"  value="<%= IdIstruttoriaCumulo%>" >
 
  
  <table>
    <tr><td class="Titolo" colspan=6>Destinatario  </td></tr>
  
  <%  if(IdIstruttoriaCumulo!=null && !IdIstruttoriaCumulo.equals("")) 
      {%>
    <tr>
        <td class="l" colspan="1">
          <a href="Javascript:ElencoTitoliPopup('LoadInserisciEstrattoSentenze', '<%=ICostantiIstruttoria.FORM_TYPE_SENTENZA_INTEGRALE%>');">
            Seleziona Titolo Esecutivo <img src="/images/filefolder.gif" border=0></a>
        </td>
    </tr>
    
      <tr><td>&nbsp;</td></tr>
<%    } %>      
      <tr>
          <td class="l">Autorità Destinatario <font class="ob">(*)</font></td >
           <td class="L">
             <select Title="Autorita " name="<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>" >
               <%=autorita%>
             </select>
         </td>
    </tr>
    <tr><td class="l">Sede <font class="ob">(*)</font></td><td class="L">
       <input title="Sede Autorita Emittente" value="" type="text" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuniperTipoUfficio('LoadInserisciEstrattoSentenze','<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>', 
        document.LoadInserisciEstrattoSentenze.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>[document.LoadInserisciEstrattoSentenze.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.selectedIndex].value);">
       <img src="/images/filefolder.gif" border=0></a></td>
    </tr>

    <tr><td class="Titolo" colspan=6>Oggetto  </td></tr>
    <tr>
        <td class="l">Richiesta copie N. <font class="ob">(*)</font></td>
        <td class="L"> <input title="Numero Copie"  value="1" type="text" size="2" maxlength="2" name="<%= ICostantiIstruttoria.NUMERO_COPIE %>"  onkeypress="return TicTabNumField(this,event)">
          di
           <select Title="Tipo Documento" name="<%=ICostantiIstruttoria.TIPO_DOCUMENTO%>" >
             <option value="ESTRATTO"/>ESTRATTO
             <option value="COPIA INTEGRALE"/>COPIA INTEGRALE 
           </select>
           SENTENZA 
         </td> 
    </tr>

    <tr>
        <td class="l">Estremi Sentenza <font class="ob">(*)</font></td>
  <%  if(!IdIstruttoriaCumulo.equals(null) && !IdIstruttoriaCumulo.equals("")) 
      {%>        
        <td class="L">
      <input type="text" Title="Estremi sentenza" value="" name="<%= ICostantiIstruttoria.ESTREMI_SENTENZA %>" size="135" maxlength="135" >
        </td>
 <%   }
      else
      { %>
      <td class="L">
           <input value="" type="text" size="70" maxlength="70" name="<%= ICostantiIstruttoria.ESTREMI_SENTENZA%>" >  
        </td>
<%    } %>


    <% if(IdIstruttoriaCumulo!=null && !IdIstruttoriaCumulo.equals("")) { %>
    <tr id="idTrSoggetto" style="display:none">
      <td class="l">Estremi Soggetto</td>
      <td class="L">
        <input type="text" Title="Estremi Condannato" value="" name="<%=ICostantiIstruttoria.ESTREMI_SOGGETTO%>" size="135" maxlength="135" >
      </td>
    </tr>
    <% } %>

    <tr>
        <td class="l">Data Emissione </td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciEstrattoSentenze");

  frmvalidator.addValidation("<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>","req","La campo Sede Destinatario è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiIstruttoria.AUTORITA_DESTINATARIO%>","req","Il campo Destinatario è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiIstruttoria.NUMERO_COPIE %>","req","Il campo Numero Copie  è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiIstruttoria.NUMERO_COPIE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiIstruttoria.TIPO_DOCUMENTO%>","req","Il campo ESTRATTO/COPIA INTEGRALE obbligatorio");
  frmvalidator.addValidation("<%= ICostantiIstruttoria.ESTREMI_SENTENZA %>","req","Il campo Estremi Sentenza è obbligatorio");

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
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
 </script>

</table>
  </form>

  </body>
</html>