<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date"%>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena"%>

<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>


<jsp:useBean id="lCalcoloPenaMod"  scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel" />
<jsp:useBean id="lPenComplMod"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="lUltimaPenResVal" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%
//==============================================================================
// Jsp per il calcolo della pena "virtuale" DL 92/2024  - MEV_2024-092
// La jsp visualizza:
//==============================================================================
%>

<% 
boolean isErgastolo = false;
if (   lPenComplMod!=null 
    && lPenComplMod.getCodTipoPenaDetentiva()!=null
    &&(   lPenComplMod.getCodTipoPenaDetentiva().equals("03") 
       || lPenComplMod.getCodTipoPenaDetentiva().equals("04")
      )
   )
{
  isErgastolo = true;
}

PenaResiduaModel lPenaDaEspiare = new PenaResiduaModel();
Date lDataInizioPena = null;
if (!isErgastolo)
{
  lDataInizioPena = (Date)request.getAttribute("lDataInizioPena");
  Date lDataDiSistema = null;
  lPenaDaEspiare = lCalcoloPenaMod.getPenaDaEspiare(lDataInizioPena,lDataDiSistema,null);
}

//==========================================================================
//    MISURE CAUTELARI COMPUTABILI
//==========================================================================
CalendarModel totMCComputabili = new CalendarModel();
totMCComputabili = lCalcoloPenaMod.getMisureCautelariReclusioneInSentenza();
%>

<html>
<head>
  <title>[S.I.E.S.] - Calcolo Pena DL92/2024 </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" >
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    function Verify()
    {
      // Verifica la correttezza della data di decorrenza
      if (document.CalcoloPenaDL92.PosizioneGiuridica[1].checked) {
        if (document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length<2 && document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length!=0)
        	document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value="0"+document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value;
        if (document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.length<2 && document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.length!=0)
        	document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value="0"+document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value;
          
        var dataDecorrenza = document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value 
                        +"/"+document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value
                        +"/"+document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value;
        
        if (dataDecorrenza=='//')
        {
          alert('Indicare la Data Decorrenza Pena');
          document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();
          return false;
        }                         

        if (! ControllaData(dataDecorrenza))
        {
          alert('Data Decorrenza Pena non valida');
          document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();
          return false;
        }          
      }
      
      return true;      
    }
    
    function pulisciMaschera(){
      document.CalcoloPenaDL92.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadCalcoloPenaDL92";
      document.CalcoloPenaDL92.submit();
    }
    
    function inizializza(){
        radio();
    }
    
    function radio(){
      if (document.CalcoloPenaDL92.PosizioneGiuridica[0].checked){
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value = "";
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value = "";
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value = "";
        
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.disabled = true;
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.disabled = true;
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.disabled = true;
      }
      else {
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.disabled = false;
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>.disabled = false;
        document.CalcoloPenaDL92.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.disabled = false;
      }
    }
  </script>
</head>

<body class="corpo" onLoad="inizializza();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="../../images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG" style="padding-right: 5px;">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Calcolo pena ipotetica con detrazioni DL. 92/2024</font>
      </td>
      <td class="LBG">
        <a href="Javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  
  <% if (session.getAttribute("fascicolo") != null) { %>
  <jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <% } %>
  
  <!-- ===================================================================== -->
  <!--                          SEZIONE DEI DATI                             -->  
  <!-- ===================================================================== -->
  <form method="POST" action="/jsp/Main.jsp" name="CalcoloPenaDL92">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActCalcoloPenaDL92">
    
    <table cellspacing="4" cellpadding="4">
      <tr>
        <td class="Titolo" colspan="7">Pena Da Espiare al netto della custodia cautelare (presofferto)</td>
      </tr>
      <tr>
        <td class="l" width="25%">Reclusione</td>
        <td class="l">
          <% if ( !lPenaDaEspiare.isQuantumReclusioneZero() ) {  %>
             Anni&nbsp;  <input Title="Anni Reclusione"   value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumAnniReclusione()  , "0") %>"  type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Mesi&nbsp;  <input Title="Mesi Reclusione"   value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumMesiReclusione()  , "0") %>"  type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Giorni&nbsp;<input Title="Giorni Reclusione" value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumGiorniReclusione()  , "0") %>"  type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% 
          } else {
          %>
             Anni&nbsp;  <input Title="Anni Reclusione"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Mesi&nbsp;  <input Title="Mesi Reclusione"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Giorni&nbsp;<input Title="Giorni Reclusione" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } %>
        </td>
        <td class="l">Multa</td>
        <td class="l" width="25%">
          <% if ( !lPenaDaEspiare.isMultaZero() ) {  %>
          <input type="text"  size="14" maxlength="14" style="text-align: right;" Title="Multa"
                 name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>" 
                 value="<%=StringUtils.getParteIntera (lPenaDaEspiare.getImportoMulta())%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          ,
          <input Title="Multa" size="2" maxlength="2" value="<%=StringUtils.getParteDecimale (lPenaDaEspiare.getImportoMulta())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } else { %>
          <input Title="Multa" size="14" maxlength="14" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          ,
          <input Title="Multa" size="2" maxlength="2" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } %>
        </td>
      </tr>
      <tr>
        <td class="l">Arresto</td>
        <td class="l">
          <% if ( !lPenaDaEspiare.isQuantumArrestoZero() ) { %>
             Anni&nbsp;  <input Title="Anni Arresto"   value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumAnniArresto(), "0") %>"type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Mesi&nbsp;  <input Title="Mesi Arresto"   value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumMesiArresto(), "0") %>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Giorni&nbsp;<input Title="Giorni Arresto" value="<%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumGiorniArresto(), "0") %>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } else { %>
             Anni&nbsp;  <input Title="Anni Arresto"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Mesi&nbsp;  <input Title="Mesi Arresto"   value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
             Giorni&nbsp;<input Title="Giorni Arresto" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } %>
        </td>
        <td class="l">Ammenda</td>
        <td class="l">
          <% if ( !lPenaDaEspiare.isAmmendaZero() ) { %>
          <input type="text" size="14" maxlength="14"  style="text-align: right;" Title="Ammenda"
                 name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>" 
                 value="<%=StringUtils.getParteIntera   (lPenaDaEspiare.getImportoAmmenda())%>"                   
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          ,
          <input Title="Ammenda" size="2" maxlength="2" value="<%=StringUtils.getParteDecimale (lPenaDaEspiare.getImportoAmmenda())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } else { %>
          <input Title="Ammenda" size="14" maxlength="14" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          ,
          <input Title="Ammenda" size="2" maxlength="2" value="" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } %>
        </td>
      </tr>
      <tr>
        <td colspan="4">&nbsp;</td>
      </tr>
      <tr>
        <td class="l">Presofferto</td>
        <td class="l">
          <% if ( !totMCComputabili.isQuantumZero() ) { %>
            Anni&nbsp;  <input Title="Anni Arresto"   value="<%= totMCComputabili.getNumAnni()  %>" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Arresto"   value="<%= totMCComputabili.getNumMesi()  %>" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Arresto" value="<%= totMCComputabili.getNumGiorni()%>" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } else { %>
            Anni&nbsp;  <input Title="Anni Arresto"   value="" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Mesi&nbsp;  <input Title="Mesi Arresto"   value="" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
            Giorni&nbsp;<input Title="Giorni Arresto" value="" type="text" name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <% } %>
        </td>
        <td class="l" colspan="2">&nbsp;</td>
      </tr>
     
       <tr>
        <td colspan="4">&nbsp;</td>
      </tr> 
      <tr>
        <td class="l">Posizione Giuridica:&nbsp;&nbsp;</td>
        <%
        String isLibero = "checked";
        String isDetenuto = "";
        if (lDataInizioPena!= null) {
        	isLibero = "";
        	isDetenuto = "checked";
        }
        %>
        <td class="l">
          <input type="radio" name="<%=ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA%>" value="<%=ICostantiCalcoloPena.POSIZIONE_GIURIDICA_LIBERO%>" <%=isLibero %> onclick="radio();">Libero &nbsp;
          <input type="radio" name="<%=ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA%>" value="<%=ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO%>" <%=isDetenuto %> onclick="radio();">Detenuto &nbsp;
        </td>
        <td class="l">
          <font class="label">Data Decorrenza Pena </font>
        </td>
        <td class="l">
          <input type="text" size="2" maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%> 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (lDataInizioPena, "dd")) %>" disabled
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
          <input type="text" size="2" maxlength="2" name=<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>   
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (lDataInizioPena, "MM")) %>" disabled
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
          <input type="text" size="4" maxlength="4" name=<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>   
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (lDataInizioPena, "yyyy")) %>" disabled
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td colspan="3">
          <input class="bottone" type="submit" name="INSERISCI" value="Conferma"
               onClick1="javascript:Verify_Dati();">
          &nbsp;
          <input class="bottone" type="button" name="PULISCI" value="Pulisci Dati" 
                 title="Pulisce i dati in maschera per un nuovo calcolo" 
                 onClick="javascript:pulisciMaschera();">&nbsp;&nbsp;
        </td>
      </tr>
    </table>
  </form>
  
  
<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("CalcoloPenaDL92");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>  
  </body>
</html>