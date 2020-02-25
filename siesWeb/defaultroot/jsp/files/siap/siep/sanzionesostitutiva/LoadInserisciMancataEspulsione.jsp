<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.sico.util.CalendarUtil"%> 

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>


<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        		scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAnnotazione"       scope="request" class="java.lang.String"/>


<!-- Combo Autorità -->
<!-- n.b. dal 26/05/2008 non viene più prodotta una stampa, ma prevista la sola
          annotazione. Magistrato e destinatari vengono oscurati.
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="tipoAutorita"          scope="request" class="java.lang.String"/>
-->



<%
//==============================================================================
// Form per l'inserimento dell'annotazione mancata espulsione
//==============================================================================
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione       = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel         lAltraCausa      = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <!-- Load Inserisci Annotazione Espulsione  -->
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Verifica che i dati digitati in maschera siano coerenti prima di 
    // sottomettere la richiesta
    //==========================================================================
    function Verifica(){
      //===========================================
      // Verifico il fine pena manuale
      //===========================================
      try{
        if (     document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value!=""
              || document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").value!=""	
              || document.getElementById("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>").value!=""	
            )
        {
          if (document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value.length<2 && document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value.length!=0)
            document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value="0"+document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value;
          if (document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").length<2 && document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").length!=0)
            document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").value="0"+document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").value;
            
          var dataFinePena=document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").value +"/"+document.getElementById("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>").value+"/"+document.getElementById("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>").value;
          
          if (! ControllaData(dataFinePena))
          {
            alert('Data Fine Pena non valida');
            document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").focus();
            return false;
          }
        }      
        else 
        {
            alert('Data Fine Pena Obbligatoria');
            document.getElementById("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>").focus();
            return false;
        }   
      
      }
      catch(err){}
    
    
      //============================================
      // Verifica la correttezza della Data Nota
      //============================================
      if (     document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value!=""
            || document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").value!=""	
            || document.getElementById("<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>").value!=""	
          )
      {
        if (document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value.length<2 && document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value.length!=0)
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value="0"+document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value;
        if (document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").length<2 && document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").length!=0)
          document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").value="0"+document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").value;
          
        var dataVerbale=document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").value +"/"+document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>").value+"/"+document.getElementById("<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>").value;
        
        if (! ControllaData(dataVerbale))
        {
          alert('Data Nota non valida');
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").focus();
          return false;
        }
      }      
      else 
      {
          alert('Data Nota Obbligatoria');
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>").focus();
          return false;
      }   


      //==================================
      // Data Annotazione
      //==================================
      if (     document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value!=""
            || document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").value!=""	
            || document.getElementById("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>").value!=""	
          )
      {
        if (document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value.length<2 && document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value.length!=0)
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value="0"+document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value;
        if (document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").length<2 && document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").length!=0)
          document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").value="0"+document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").value;
          
        var dataAnnotazione=document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").value +"/"+document.getElementById("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>").value+"/"+document.getElementById("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>").value;
        
        if (! ControllaData(dataAnnotazione))
        {
          alert('Data Annotazione non valida');
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").focus();
          return false;
        }
      }      
      else 
      {
          alert('Data Annotazione Obbligatoria');
          document.getElementById("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>").focus();
          return false;
      }
      
      //===========================
      //
      //===========================
      if(document.getElementById("<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>").value == "")
      {
         alert("Autorità emittente obbligatoria");
         document.getElementById("<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>").focus();
         return false;
      }
      
      if(document.getElementById("<%=ICostantiEvento.CAMPO_COD_MOTIVO%>").value == "-")
      {
         alert("Annotazione mancata Espulsione obbligatoria");
         document.getElementById("<%=ICostantiEvento.CAMPO_COD_MOTIVO%>").focus();
         return false;
      }
      
      //===============================
      // Magistrato/Destinatari
      //===============================
      /*
      if(document.getElementById("<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>").value == "")
      {
         alert("Magistrato Obbligatorio");
         document.getElementById("<%=ICostantiMagistrato.CAMPO_COGNOME%>").focus();
         return false;
      }
      
      if(document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>").value == "-")
      {
         alert("Autorità di Polizia obbligatoria");
         document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>").focus();
         return false;
      }
        
      if(document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>").value == "")
      {
         alert("Sede Autorità di Polizia obbligatoria");
         document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>").focus();
         return false;
      }
    */
      
      return true;
    }
  
    //==========================================================================
    // 
    //==========================================================================
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    function ListaQuesture(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaQuesture&formname="+a_formname+"&fieldname="+a_fieldname, "Lista_Questure","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Mancata Espulsione</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMancataEspulsione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciMancataEspulsione">
  <input type="HIDDEN" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"  >
  <input type="HIDDEN" name="dataeditabile" value="<%=dataeditabile%>"  >

  <%
  //============================================================================
  // Sezione con il dettaglio della pena e posizione giuridica
  //============================================================================
  %>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
          DETENUTO PER ALTRA CAUSA
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
      </td>
    </tr>
  
    <%
    //================================================
    // Detenuto altra causa
    //================================================
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
    {
       if( lAltraCausa.getIstitutoDetenzione()!= null) 
       { %>
       <tr>
         <td class="l">Detenuto presso </td>
         <td class="L" colspan="5"><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
         </td>
       </tr>

         <% if (lAltraCausa.getAltroLuogo()!=null) { %>
         <tr>
           <td class="l">Altro Luogo </td>
           <td class="L" colspan="5">
             <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
           </td>
         </tr>
         <% } 
       } %>
    <% 
    }
    else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
    {
    %>
    <tr>
      <td class="l">Detenuto presso </td>
      <td class="L" colspan=5>
        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
      </td>
    </tr>
    <%
    }
    %>
    
    <% 
    //===============================================================
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    //===============================================================
    if(   lPosizione.getCodPosizioneGiuridica() != null 
       && (   lPosizione.getCodPosizioneGiuridica().equals("02") 
           || lPosizione.getCodPosizioneGiuridica().equals("04") 
          ) 
      )
    {
        if(lLuogoDetenzione.getAltroLuogo() != null) 
        { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
            </tr>
        <%
        }
    }
    %>
    
    
<tr>
<%
//==============================================================================
//              Sezione per la visualizzazione della PENA
// Se non ergastolo: viene visualizzata la pena
//==============================================================================
    if(   penaresidua.getIdPenaResidua() != null
       && (    penaresidua.getFlagErgastolo() == null
            || (   penaresidua.getFlagErgastolo() != null 
                && !penaresidua.getFlagErgastolo().equals("S") 
                && !penaresidua.getFlagErgastolo().equals("D")
               ) 
          ) 
      )
    {
        //=====================
        // RECLUSIONE se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(penaresidua.getQuantumReclusione())>0)
        {
        %>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
        %>
        </tr>
   
        <tr>
        <%
        //=====================
        // ARRESTI se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(penaresidua.getQuantumArresto())>0)
        {
        %>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
    }
    %>
    </tr>
    
    
    <%
    //============================================
    // Decorrenza/Scadenza
    //============================================
    %>
    <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
        <td class="l">Data Decorrenza Pena</td>
        <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if ( penaresidua.getFlagErgastolo() != null)
      {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        <%
        }
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        <%
        }
      }
      %>

      
      
      <%
      // Detenuta Altra Causa
      if(   !lPosizione.isLibero()
         || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
             && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
            ) 
        )
      { 
        if  (   (penaresidua.getFlagErgastolo() == null) 
             || (   penaresidua.getFlagErgastolo() != null 
                 && !penaresidua.getFlagErgastolo().equals("S") 
                 && !penaresidua.getFlagErgastolo().equals("D")
                )
              )
        {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
          { // Fine pena editabile: primo calcolo non validato
          %>
          <td class="l">Data Fine Pena</td>
          <td class="L" colspan=2>
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
          <%
          }
          else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="L" colspan=2>
                <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
              </td>
            <%
            }
            else
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="lRosso" colspan=2>
                <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
              </td>
<%          }
          }
        }
      }
%>
    <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  </tr>
  
  <tr>
    <td class="l">Sanzione Sostitutiva Applicata</td>
    <td class="l">
      <font class="campo">ESPULSIONE</font>
    </td>
  </tr>
</table>
    
<%
//==============================================================================
//                         DATI DELLA COMUNICAZIONE
//==============================================================================
%>    
<table width="100%">
  <tr>
    <td class="Titolo" width="100%" colspan="100%"> Dati Comunicazione</td>
  </tr>
  <tr>
    <td class="l" width="30%">Numero Protocollo Nota</td>
    <td class="L" colspan="3">
      <input type="text"  maxlength="2000" size="40" value="" name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" >
    </td>
  </tr>
  
  <tr>
    <!-- Data Nota -->
    <td class="l">Data Nota<font class="ob">(*)</font></td>
    <td class="L" colspan="3">
      <input type="text" name="<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>"  maxlength="2" size="2" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>"  maxlength="2" size="2" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>"  maxlength="4" size="4" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
    </td>
  </tr>

  <tr>
    <!-- Data Annotazione -->
    <td class="l">Data Annotazione<font class="ob">(*)</font></td>
    <td class="L" colspan="3">
      <input type="text" name="<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>"  maxlength="2" size="2" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>"  maxlength="2" size="2" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
      /
      <input type="text" name="<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>"  maxlength="4" size="4" value="" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
    </td>
  </tr>

  <tr>
    <td class="l">Autorità Emittente<font class="ob">(*)</font></td>
    <td class="L">Questura di
      <input type="hidden" title="Autorità" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>" value="20"> <!-- 20 = Questura -->
      <input title="Luogo" value="" type="text" name="<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
      <a href="Javascript:ListaQuesture('LoadInserisciMancataEspulsione','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <%// Aggiunto il 11/06/2008 %>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiVerbale.CAMPO_NOTE %>"  cols="40"></textarea>
    </td>
  </tr>
  
  <tr>
    <td class="l">Annotazione<font class="ob">(*)</font></td>
    <td class="L" colspan="3">
      <select title="Autorità" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
        <%=tipoAnnotazione%>
      </select>
    </td>
  </tr>
  
  <%// Aggiunto il 26/05/2008 %>
  <tr>
    <td class="l">Note</td>
    <td class="L" colspan="3">
      <TEXTAREA title="Note" name="CampoNota"  cols="100" rows="3"></textarea>
    </td>
  </tr>
  
</table>    

<%
//==============================================================================
//                                  DESTINATARI
//==============================================================================
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<table width="100%">
  <tr>
    <td class="Titolo" colspan="100%" id="titolo_com_ann"> Destinatari </td>
  </tr>
  --%>
  <%--tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>

    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input title = "Giorno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr--%>
<%--
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciMancataEspulsione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="l" width="20%">Autorità di Polizia <font class=ob>(*)</font> </td>
    <td class="L" colspan="3">
      <select  Title="Autorita di Polizia" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
        <%=tipoAutorita%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede <font class=ob>(*)</font></td>
    <td class="L">
      <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciMancataEspulsione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>"  cols="30"></textarea>
    </td>
  </tr>
</table>
--%>

<table>
  <tr>
    <td>
      <INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
    </td>
  </tr>
</table>

 <script language="JavaScript" type="text/javascript">
   var frmvalidator  = new Validator("LoadInserisciMancataEspulsione");

   frmvalidator.setAddnlValidationFunction("Verifica");

 </script>

    
</form>

</body>