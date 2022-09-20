<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.competenza.action.ICostantiCompetenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>


<%@ page import="siap.sico.ufficio.util.UfficioAccorpatoUtils"%>



<jsp:useBean id="insertManuale"         scope="request" class="java.lang.String" />

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<% //Combo %>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"           scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPM"             scope="request" class="java.lang.String"/>

<jsp:useBean id="richiesta"             scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"               scope="request" class="java.lang.String"/>

<jsp:useBean id="sentenzaFascicoloDaRichiedere"   scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="fascicoloDaRichiedere"           scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>

<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />

<% // Criteri di ricerca da precaricare se fascicolo non trovato %>
<jsp:useBean id="annoRicerca"       scope="request" class="java.lang.String"/>
<jsp:useBean id="progrRicerca"      scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioRicerca"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="chiaveAccorpato"   scope="request" class="java.lang.String"/>



<%
//==============================================================================
// Form per l'inserimento richiesta trasmissione atti.
//
//==============================================================================
  //setto il flag per la verifica dell'inserimento manuale
  boolean insManuale = false;
  if (insertManuale.equalsIgnoreCase("1"))
    insManuale = true;  

     
%>

<html>
<head>
  <title>[S.I.E.S.] - Richiesta atti per competenza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">  

    var ufficiAccorpatiArray = new Array();
  
    <%
    // Costruzione Array Uffici Accorpati
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
    
    function ChoosePopup()
    {
      var selectTipoUfficio = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>;
      var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
      var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
        
      if (codTipoUfficio == 'PM'){
        ListaUffici('LoadInserisciRichiestaTrasmissioneCompetenza','<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>');
      } else if (codTipoUfficio == 'PGCAP'){
        ListaDistretti('LoadInserisciRichiestaTrasmissioneCompetenza','<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>');
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
    
    function loadUfficiAccorpati(codUfficio){
      var i=0;
      var ufficioAccorpatoSelect = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
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
    
  
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3){
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    function ListaComuni(a_formname,a_fieldname){
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function caricaDati(){
      <% if(insManuale) {%>
      loadUfficiAccorpati('<%=ufficioRicerca.getCodUfficio()%>');
      var ufficioAccorpatoSelect = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
      for (i=0; i<ufficioAccorpatoSelect.options.length; i++){
        if (ufficioAccorpatoSelect.options[i].value == '<%=chiaveAccorpato%>'){
          ufficioAccorpatoSelect.options[i].selected = 'selected';
          break;
        }
      }
      <% } %>
    }
    
    function resetSede(){
      document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>.value="";
      var ufficioAccorpatoSelect = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
      ufficioAccorpatoSelect.options.length = 0;
      ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
    }

    
    function Verifica() 
    {
      // Data Emissione
      if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                      +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                      +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) ){
        alert('Data Emissione non valida');
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }    
    
    
      // Data Trasmissione
      if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      var data_to_verify = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
                      +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
                      +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      if (!ControllaData(data_to_verify) ){
        alert('Data di Trasmissione non valida');
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
        return false;
      }
      
      <% if(insManuale) { %>
        // Tipo Provvedimento
        if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
          alert("E' obbligatorio selezionare la tipologia dell'atto");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
          return false;   
        }
        
        
        var annoTitolo = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_SENTENZA%>.value;
        var numTitolo  = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_NUMERO_SENTENZA%>.value;
        // Anno Provvedimento non obbligatorio
        if(annoTitolo!=''){
          if(annoTitolo<1900 || annoTitolo><%=DateUtils.getSysDate("yyyy")%> ){
            alert('Digitare correttamente il campo Anno Provvedimento. Maggiore di 1900, minore di '+<%=DateUtils.getSysDate("yyyy")%>);
            document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_SENTENZA%>.focus();
            return false;
          }
        }
        
        if(   (annoTitolo!='' && numTitolo=='')
           || (annoTitolo=='' && numTitolo!='')
          )
        {
          alert("Gli estremi del titolo (Anno/Numero Provvedimento), se specificati vanno indicati entrambi");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_SENTENZA%>.focus();
          return false;
        }
        
        
        // Data Provvedimento
        if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
  
        var data_to_verify = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value
                        +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value
                        +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
  
        if (!ControllaData(data_to_verify) ){
          alert('Data Provvedimento non valida');
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
          return false;
        }
        
        // Data Irrevocabilità
        if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
        if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
  
        var data_to_verify = document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
                        +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
                        +'-'+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
  
        if (!ControllaData(data_to_verify) ){
          alert('Data Irrevocabilità non valida');
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
          return false;
        }
        
        // Pronumciata da
        if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=='-'){
          alert("Selezionare l'Ufficio che ha pronunciato il provvedimento");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
          return false;
        }
        
        if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>.value.length==0){
          alert("Selezionare la sede dell'Ufficio che ha pronunciato il provvedimento");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>.focus();
          return false;
        }
        
        // Anno e Numero Procedimento Non obbligatori ma l'anno se indicato deve 
        // essere <= anno corrente, e in caso devono essere presenti entrambi
        // non uno solo
        if(   document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value.length==0
           && document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.value.length==0)
        {
          // OK dati non obbligatori
          // chiedere conferma se procedere
          var confirm = window.confirm("Non sono stati indicati gli estremi del procedimento di esecuzione (Anno/Numero). Si vuole procedere comunque?");
          if (!confirm) return false;
        } 
        else 
        {
      	  // Blocco eventuale settaggio a ZERO di Anno e Numero Fascicolo Richiesto
      	  if( document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value.length>0
           && document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.value.length>0)
          {
      		  if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value < 1 
      			&& document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.value < 1  )
      		  {
      			 alert("Se NON sono noti i dati del procedimento di esecuzione, lasciare a spazio Anno e Numero");
                 document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.focus();
                 return false;
      		  }
          }

          if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value.length==0){
            alert("Se noti, i dati del procedimento di esecuzione vanno specificati entrambi: Anno e Numero");
            document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.focus();
            return false;
          }
          else {
            // Controllo anno
            if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value><%=DateUtils.getSysDate("yyyy")%>){
              alert("L'Anno del procedimento di esecuzione non può essere superiore all'anno corrente");
              document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.focus();
              return false;
            } 
            // Blocco settaggio di ANNO a ZERO
            else if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value < 1)
            {
               alert("Se NON sono noti i dati del procedimento di esecuzione, lasciare a spazio Anno e Numero Procedimento");
               document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.focus();
               return false;
            }	
            else if (document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value<1980){
              var confirm = window.confirm("Confermi l'anno "+document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.value+" del procedimento di esecuzione?");
              if (!confirm) return false;
            }
          }
          
          if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.value.length==0)
          {
            alert("Se noti, i dati del procedimento di esecuzione vanno specificati entrambi: Anno e Numero");
            document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.focus();
            return false;
          }
          // Blocco eventuale settaggio a ZERO del NUMERO Procedimento
          else if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>.value < 1)
          {
         	 alert("Se NON sono noti i dati del procedimento di esecuzione, lasciare a spazio Anno e Numero Procedimento");
             document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>.focus();
             return false;
          }	 
        }
        
        // Ufficio del PM
        if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>.value=='-'){
          alert("Selezionare l'Ufficio del Pubblico Ministero ");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>.focus();
          return false;
        }
        if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>.value.length==0){
          alert("Selezionare la sede dell'Ufficio del Pubblico Ministero ");
          document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>.focus();
          return false;
        }
        
      <% } %>
      
      // Tipologia atti
      // Oggetto atto
      // Magistrato firmatario
      if(document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value.length==0){
        alert("Selezionare il Magistrato Firmatario");
        document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
        return false;
      }
      
      return true;
    }

  </script>
</head>

<body class="corpo" onload="caricaDati();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta atti per competenza</font></td>
    </tr>
  </table>

  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaTrasmissioneCompetenza">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaTrasmissioneTitolo">
  <input type="hidden" name="<%=ICostantiCompetenza.CAMPO_SENTENZA_SIEP_TROVATO%>" value="<%=StringUtils.toStringJSP(sentenzaFascicoloDaRichiedere.getIdSentenza())%>">
  <input type="hidden" name="<%=ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO%>" value="<%=StringUtils.toStringJSP(fascicoloDaRichiedere.getIdFascicoloSiep())%>">


  <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>


<% 
if(insManuale) 
{ 
//==============================================================================
//  Procedimento NON trovato, richiedo inserimento dati
//==============================================================================
%>
  <table width="90%">
    <tr>
     <td class="Titolo" colspan="4">Titolo Da Assorbire in Cumulo</td>
    </tr>
    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="L" colspan="1">
        <select  name="<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>" Title="Tipo Provvedimento" >
          <option value = "-"  />-
          <option value = "01"  />Sentenza
          <option value = "02"  />Decreto Penale
        </select>
      </td>
      <td class="l">Anno/Numero Provvedimento</td>
      <td class="L">
        <input type="text" Title="Anno Provvedimento"   maxlength="4" size="4"
               name="<%=ICostantiCompetenza.CAMPO_ANNO_SENTENZA%>"
               value=""
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
        <input type="text" Title="Numero Provvedimento" maxlength="6" size="6"
               value=""  
               name="<%=ICostantiCompetenza.CAMPO_NUMERO_SENTENZA%>"
               >
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Provvedimento <font class=ob>(*)</font></td>
      <td class="L" colspan="1">
        <input type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input type="text" size="4" maxlength="4" name="<%= ICostantiCompetenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Definitivo in data <font class=ob>(*)</font></td>
      <td class="L" colspan="1">
        <input type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input type="text" size="4" maxlength="4" name="<%= ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Pronunciata da <font class=ob>(*)</font></td>   
      <td class="L" colspan="3">
        <select  Title="Ufficio Emissione"  name="<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
        <%=autoritaEmi %>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo <font class=ob>(*)</font></td>    
      <td class="L" colspan="1">
        <input title="Sede Ufficio Emissione"  type="text" name="<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>"  maxlength="35" size="30">
        <a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaTrasmissioneCompetenza',
                                              '<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>',
                                               document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
           <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Sezione</td>
      <td class="L" colspan="1">
        <input title="Sezione Ufficio Emissione"  type="text" name="<%=ICostantiCompetenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>"  maxlength="35" size="20">
      </td> 
    </tr>
  
    <% //======================================================================= %>
    <tr>
      <td class="Titolo" colspan="8">Ufficio che ha in carico gli atti/Procedimento di esecuzione da assorbire</td>
    </tr>
    <tr>
      <td class="l" width="25%">Anno/Numero Procedimento</font></td>
      <td class="L" colspan="3">
        <input type="text" title="Anno" maxlength="4" size="4"
               name="<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>"  
               value="<%=StringUtils.toStringJSP(annoRicerca,"")%>"
               onkeypress="return TicTabNumField(this,event)"
               onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero SIEP" maxlength="20" size="20" 
               value="<%=StringUtils.toStringJSP(progrRicerca,"")%>"
               name="<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>" 
               onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr> 
    <tr>
      <td class="l">Ufficio del Pubblico Ministero <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <select title="Ufficio Pubblico Ministero"  name="<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>" onchange="resetSede()">
          <%=ufficioPM %>
        </select>
      </td>
    </tr>
    <tr>  
      <td class="l">Luogo <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <input type="text" title="Sede Ufficio" maxlength="35" size="30"
               name="<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>"  
               value="<%=StringUtils.toStringJSP(ufficioRicerca.getDescrComune(),"")%>" readonly="readonly">
        <a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaTrasmissioneCompetenza','<%=ICostantiCompetenza.CAMPO_SEDE_UFFICIO%>',document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>[document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_UFFICIO%>.options.selectedIndex].value);">
           <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>  
      <td class="l">Ufficio Accorpato</td>
      <td class="l" colspan="3">
        <select name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>">
          <option value="0" >-</option>
        </select>
      </td>      
    </tr>
  </table>
<% 
} 
else 
{ 
//==============================================================================
//  Procedimento trovato, visualizzo il dettaglio di sentenza e fascicolo
//==============================================================================
%>
<table width=90%>
  <tr>
   <td class="Titolo" colspan="4">Titolo Da Assorbire in Cumulo</td>
  </tr>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="L"><font class="campo"><%=sentenzaFascicoloDaRichiedere.getDescrTipoProvvedimento()%></font></td>
    <td class="L" align="right">Anno/Numero Provvedimento</td>
    <td class="L"><font class="campo"><%=sentenzaFascicoloDaRichiedere.getAnnoSentenza()%> / <%=sentenzaFascicoloDaRichiedere.getNumeroSentenza()%></font></td>
  </tr>
  <tr>    
    <td class="l" width="25%">Data Provvedimento</td>
    <td class="L" colspan="3"><font class="campo"><%=DateUtils.getDateToString(sentenzaFascicoloDaRichiedere.getDataProvvedimento(), "dd-MM-yyyy")%></font></td>
  </tr>
  <tr>
    <td class="l" width="25%">Pronunciata da</td>   
    <td class="L" colspan="3"><font class="campo"><%=sentenzaFascicoloDaRichiedere.getDescrTipoAutoritaEmittente()%></font></td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="L" colspan="1">
      <font class="campo"><%=sentenzaFascicoloDaRichiedere.getDescrLuogoEmittente()%>&nbsp;</font>
    </td>
    <td class="l">Sezione</td>
    <td class="L" colspan="1"><font class="campo">
      <%if(sentenzaFascicoloDaRichiedere.getNumSezioneAutoritaEmittente()!=null && (!sentenzaFascicoloDaRichiedere.getNumSezioneAutoritaEmittente().equalsIgnoreCase("null"))){%>
        <%=sentenzaFascicoloDaRichiedere.getNumSezioneAutoritaEmittente()%>
      <%}%>&nbsp;</font>
    </td>
  </tr>

  <% //======================================================================= %>
  <tr>
    <td class="Titolo" colspan="4">Procedimento di esecuzione da assorbire</td>
  </tr>
  <tr>
    <td class="l" width="25%">Anno/Numero Procedimento</td>
    <% 
    String lStrFascicolo = "<font class=\"campo\">"+fascicoloDaRichiedere.getChiaveAnno()+"/";
    if (fascicoloDaRichiedere.getChiaveProgrOrig()!=null) { 
      UfficioAccorpatoUtils lUffAccUtils = new UfficioAccorpatoUtils();
      
      //lUffAccUtils.getUfficioAccorpatoByCodAccorpanteProgr();
      
      BigDecimal lIncrement = (fascicoloDaRichiedere.getChiaveProgr()).subtract(fascicoloDaRichiedere.getChiaveProgrOrig());
      UfficioModel lUffOrigine = lUffAccUtils.getUfficioAccorpatoByCodAccorpanteIncrement (fascicoloDaRichiedere.getChiaveUfficio(), ""+lIncrement);
    
      lStrFascicolo+=fascicoloDaRichiedere.getChiaveProgrOrig()+"</font>";
      lStrFascicolo+=" <font class=\"cRosso\">(ex "+lUffOrigine.getDescrTipoUfficio()+" di "+lUffOrigine.getDescrComune()+") </font>";
    } 
    else {
      lStrFascicolo+=fascicoloDaRichiedere.getChiaveProgr();
    }
    %>
    
    <td class="L" colspan="3"><%=lStrFascicolo%></td>
  </tr> 
  <tr>
    <td class="l">Ufficio del Pubblico Ministero</td>
    <td class="L" colspan="3"><font class="campo"><%=fascicoloDaRichiedere.getDescrTipoUfficio() %></font></td>
  </tr>
  <tr>  
    <td class="l">Luogo </td>
    <td class="L" colspan="3"><font class="campo"><%=fascicoloDaRichiedere.getDescrComuneUfficio() %></font></td>
  </tr>
</table>

<% } %>


<%
//==============================================================================
//
//==============================================================================
%>
<table   width=90%>
  <tr>
    <td class="Titolo" colspan='8'> Dati Atto </td>
  </tr>
  <tr>
    <td class="l" width="25%">Tipologia Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciRichiestaTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciRichiestaTrasmissioneCompetenza.<%= ICostantiEvento.CAMPO_COD_MOTIVO%>);" Title="Tipologia Atto" >
        <%//richiesta%>
        <option value="26" />Richiesta
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Oggetto Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
        <%=oggetto%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Contenuto</td>
    <td  class="L" colspan="3">
      <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ></textarea>
    </td>
  </tr>
 
  <tr>
    <td class="l" width="15%">Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L" colspan="7">
      <input type="HIDDEN" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" 
                           name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"   >
      
      <input type="text" title="Cognome Magistrato" maxlength="35" size="25"
             value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
             name="<%= ICostantiMagistrato.CAMPO_COGNOME %>"  readonly > 
      <input type="text" title= "Nome Magistrato"   maxlength="35" size="25"
             value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
             name="<%= ICostantiMagistrato.CAMPO_NOME %>"  readonly >
      <a href="Javascript:ListaMagistrati('LoadInserisciRichiestaTrasmissioneCompetenza','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>          
    </td>
  </tr>
  
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L" colspan="7">    
      <select Title="Altro Destinatario" class="small" name="AltroDestinatario" >
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede</td>   
    <td class="L" colspan="3">
      <input title="Sede Altro Destinatario" type="text" name="SedeAltroDestinatario" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciRichiestaTrasmissioneCompetenza','SedeAltroDestinatario');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>   
  </tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    </td>
  </tr>
</table>


</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaTrasmissioneCompetenza");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  


</script>
</body>
</html>