<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita"             scope="request" class="java.lang.String"/>
<jsp:useBean id="procedimentoCumulato" scope="request" class="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"/>

<jsp:useBean id="autoritaCumulo" scope="request" class="java.lang.String"/>

<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />

<%
//===========================================================================
//
//===========================================================================
%>

<html>
<head>
  <title> Gestione Procedimento Cumulato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
  
    var ufficiAccorpatiArray = new Array();
  
    <%
    Iterator uaIter = ufficiAccorpati.iterator();
    int uaIndice = 0;
    while (uaIter.hasNext())
    {
      UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
      %>
      ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getDescrizioneNewUfficio()%>"); 
      <%
      uaIndice ++;
    }
    %>
  
    // Funzione richiamata dalla POPUP della selezione sede ufficio
    // codUfficio = codUfficio accorpante
    function loadUfficiAccorpati(codUfficio){
      var i=0;
      var ufficioAccorpatoSelect = document.LoadInserisciProcedimentoCumulato.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
      ufficioAccorpatoSelect.options.length = 0;
      ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
      while(i<ufficiAccorpatiArray.length){
        var ufficio = ufficiAccorpatiArray[i];
        if (ufficio[2]==codUfficio){
          ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
        }
        i++;
      }
    }  
  
  
    function ChoosePopup()
    {
      var selectTipoUfficio = document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>;
      var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
      var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;

      if (codTipoUfficio == '-'){
        alert("Selezionare prima l'autorità");
        document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.focus();      
      }
      else if (codTipoUfficio == 'PM'){
        ListaUffici('LoadInserisciProcedimentoCumulato','<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>');
      } 
      else if (codTipoUfficio == 'PGCAP'){
        ListaDistretti('LoadInserisciProcedimentoCumulato','<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>');
      }
      else {
        ListaUfficiPerTipo('LoadInserisciProcedimentoCumulato','<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO %>',document.LoadInserisciProcedimentoCumulato.<%= ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO %>.value);
      }
    } 
    
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  
    function ListaDistretti(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  
    function resetSede(){
      document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>.value="";
      var ufficioAccorpatoSelect = document.LoadInserisciProcedimentoCumulato.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
      ufficioAccorpatoSelect.options.length = 0;
      ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
    }
  
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    
    function selSedeUfficio()
    {
      if (document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>.value.length==0){
        ChoosePopup('LoadInserisciProcedimentoCumulato','<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>');
      }
    } 
    
    
    function inizializza(){
      <% if( modalita.equals("M") && "I".equals(procedimentoCumulato.getFlagStato()) ) { %>
      loadUfficiAccorpati('<%=procedimentoCumulato.getCodUfficioFasCumulato()%>');
      
      document.LoadInserisciProcedimentoCumulato.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>.value = <%=procedimentoCumulato.getIncrement()%>
      <% } %>
    }
    
    //==================
    function Verify() {
    
      if (document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.value=='-'){
        alert("Selezionare l'Autorità");
        document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.focus();
        return false;      
      }
      
      if (document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>.value==''){
        alert("Selezionare la Sede Autorità");
        document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>.focus();
        return false;      
      }      
    
      //=============================================================
      // controllo correttezza campo 'Data Richiesta Fascicolo'
      //=============================================================
      var data_to_verify =     document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO%>.value
                          +'/'+document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_MESE_DATA_RICHIESTA_FASCICOLO%>.value
                          +'/'+document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO%>.value;
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){
        alert('Data Richiesta Fascicolo non corretta');      
        document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO%>.focus();
        return false;
      }

      //=============================================================
      // controllo correttezza campo 'Data Pervenimento Fascicolo'
      //=============================================================
      var data_to_verify =       document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO%>.value
                           +'/'+ document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO%>.value
                           +'/'+ document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO%>.value;
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){
        alert('Data Pervenimento Fascicolo non corretta');      
        document.LoadInserisciProcedimentoCumulato.<%=ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO%>.focus();
        return false;
      }

      return true;
    }
  </script>
</head>

<body class="corpo" onLoad="inizializza();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione:</font>&nbsp;&nbsp;
        <%
        ProcedimentoCumulatoModel lProcedimentoCumulato = new ProcedimentoCumulatoModel();
        if( modalita.equals("I") ) {
        %>
        <font class="campo">Inserimento Estremi Procedimento Di Esecuzione</font>
        <% }
        else if( modalita.equals("M") ) {
          lProcedimentoCumulato = procedimentoCumulato;
        %>
        <font class="campo">Modifica Estremi Procedimento Di Esecuzione</font>
        <%}%>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>    
    </tr>
  </table>

<br>

  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"  name="LoadInserisciProcedimentoCumulato">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciProcedimentoCumulato">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=StringUtils.toStringJSP(TitoloInCumulo.getIdTitoloCumulato()) %>">
  
  <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>" value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getIdProcedimentoCumulato()) %>">
  <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_FLAG_STATO%>"               value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getFlagStato()) %>">
  
  
  <input type="hidden" name="modalita" value="<%=modalita%>">

  <table cellspacing="2" cellpadding="2" width="95%">
    <tr><td class="Titolo" colspan="100%">Estremi del procedimento di esecuzione del titolo da cumulare</td></tr>
    
    <% if( modalita.equals("M") && !"I".equals(lProcedimentoCumulato.getFlagStato()) ) { %>
    <tr>
      <td class="l">Anno/Numero SIEP</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveAnnoFasCumulato()) %></font>
        /
        <% if (!"S".equals (lProcedimentoCumulato.getFlagAccorpato()) ) { %>
        <font class="campo"><%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveProgrFasCumulato()) %></font>
        <% } else {%>        
        <font class="campo"><%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveProgrOrigine()) %></font>
        <font class="cRosso">(Ex  <%=StringUtils.toStringJSP(lProcedimentoCumulato.getUfficioOrigine().getDescrTipoUfficio()) %> 
        di <%=StringUtils.toStringJSP(lProcedimentoCumulato.getUfficioOrigine().getDescrComune()) %>
         ) </font>
        <% } %>
      
      
      </td>
    </tr>
    <tr>
      <td class="l">Autorità</td>
      <td class="l"> 
        <font class="campo"><%=StringUtils.toStringJSP(lProcedimentoCumulato.getDescrTipoUfficioFasCumulato()) %>&nbsp;</font>   
      </td> 
    </tr>
    
    <tr>
      <td class="l">Luogo</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato()) %>&nbsp;</font>
      </td>
    </tr>
    
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>" value="<%=lProcedimentoCumulato.getChiaveAnnoFasCumulato()%>">
    
    <% if ("S".equals (lProcedimentoCumulato.getFlagAccorpato()) ) { %>
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>" value="<%=lProcedimentoCumulato.getChiaveProgrFasCumulato()%>">
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_ORIGINE%>"      value="<%=lProcedimentoCumulato.getChiaveProgrOrigine()%>">
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_UFFICIO_ORIGINE%>"    value="<%=lProcedimentoCumulato.getChiaveUfficioOrigine()%>">
    <% } else { %>
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>" value="<%=lProcedimentoCumulato.getChiaveProgrFasCumulato()%>">
    <% } %>    
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>" value="<%=lProcedimentoCumulato.getCodTipoUfficioFasCumulato()%>">
    <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>" value="<%=lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato()%>">


    <% } else {%>
    <tr>
      <td class="l">Anno/Numero SIEP <font class=ob>(*)</font></td>    
      <td class="L">
        <input type="text" title="Anno"  maxlength="4" size="4" 
               name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO %>"
               value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveAnnoFasCumulato()) %>"
               onkeypress="return TicTabNumField(this,event)"
               onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero SIEP"  maxlength="14" size="14"
               <% if ( "S".equals(lProcedimentoCumulato.getFlagAccorpato()) ) { %> 
               value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveProgrOrigine()) %>"
               <% } else { %>
               value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getChiaveProgrFasCumulato()) %>"
               <% } %>
               name="<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>    
    </tr>
    <tr>
      <td class="l">Autorità <font class=ob>(*)</font></td>
      <td class="l">
        <select name="<%= ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO %>" onchange="resetSede()">
          <option value="-">-</option>
          <%=autoritaCumulo%>
        </select>    
      </td>
    </tr>
  
    <tr>
      <td class="l">
        Luogo <font class=ob>(*)</font>
      </td>
      <td class="L">
        <input type="text" title="Sede Ufficio"  maxlength="35" size="35" readonly="readonly" onclick="javascript:selSedeUfficio();"
               name="<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO %>"
               value="<%=StringUtils.toStringJSP(lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato()) %>"  >
               
        <a href="Javascript:ChoosePopup('LoadInserisciProcedimentoCumulato','<%=ICostantiProcedimentoCumulato.CAMPO_DESCR_LUOGO_UFFICIO_FAS_CUMULATO%>');">
          <img src="/images/filefolder.gif" border=0></a>
          
      </td>
    </tr>
    
    <tr>
       <td class="L">Ufficio Accorpato</td>
       <td class="l">
        <select name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>"  >
          <option value="0" >-</option>
        </select>
       </td>
    </tr>    
    
    <% } %>

    <tr>
      <td class="l">Data Richiesta Fascicolo</td>
      <td class="l">
        <input type="text" size="2" maxlength="2"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataRichiestaFascicolo(),"dd")) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataRichiestaFascicolo(),"MM")) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_MESE_DATA_RICHIESTA_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataRichiestaFascicolo(),"yyyy")) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  
    <tr>
      <td class="l">Data Pervenimento Fascicolo</td>
      <td class="l">
        <input type="text" size="2" maxlength="2"             
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataPervenimentoFascicolo(),"dd")) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataPervenimentoFascicolo(),"MM")) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4"             
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProcedimentoCumulato.getDataPervenimentoFascicolo(),"yyyy"))%>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  
    <tr>
      <td class="l">Note</td>
      <td class="l">
        <textarea cols="100" rows="3"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_NOTE %>"
               ><%=StringUtils.toStringJSP(lProcedimentoCumulato.getNote()) %></textarea>
      </td>
    </tr>


    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <textarea cols="100" rows="3"
                  name="<%= ICostantiProcedimentoCumulato.CAMPO_MOTIVO_MODIFICA %>"
               ><%=StringUtils.toStringJSP(lProcedimentoCumulato.getMotivoModifica()) %></textarea>
      </td>
    </tr>
  
    <tr>
      <td>
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciProcedimentoCumulato");

  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO %>","req","Indicare l'anno del procedimento");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO %>","minlen=4","Indicare correttamente l'anno del procedimento");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO %>","gt=1900","Indicare correttamente l'anno del procedimento deve essere maggiore del 1900");

  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO %>","req","Indicare il numero del procedimento");



  //================================================================
  // Aggiungere le opportune chiamate al genvalidator
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");



  frmvalidator.setAddnlValidationFunction("Verify");

</script>