<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>



<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="altracausaposizionegiuridica"       scope="request" class="siap.siep.altracausa.model.AltraCausaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>



<%
//==============================================================================
// Form per l'inserimento dell'Ordine di scrcerazione sia per detenuto questa 
// causa che per detenuto altra causa (futura memoria)
//
//
//
//
//==============================================================================
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

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
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      var desktop;

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

     // Chiamata lista Avvocati.
      function ListaAvvocati(a_formname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
      }

      //========================================================================
      //
      //========================================================================
      function Verify()
      {
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
  
        var data_to_verify = document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione non valida');
          return false;
        }

        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

        var data_to_verify = document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;


        var campo = document.LoadInserisciOrdineScarcerazione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
        {
          alert("Istituto di detenzione obbligatorio");
          return false;
        }

        //======================================================================
        // Se non Libero oppure 
        //======================================================================
        <%if(   (    !lPosizione.getCodPosizioneGiuridica().equals("07") 
                  && !lPosizione.getCodPosizioneGiuridica().equals("10")
                ) 
             || ( (     (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
                     && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) 
                  ) 
                )
            )
          {
            if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
            {
              if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
              {
              %>
              // Inserito controllo su esistenza nella form del campo DATA_FINE_PENA
              if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%> != undefined)
              {
                if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
                  document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
                if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
                  document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

                var data_to_verifica = document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

                if (!ControllaData(data_to_verifica) )
                {
                  alert('Data fine pena non valida');
                  return false;
                }
              }
              <%
              }
            }
          }%>


        //Controllo se Data Provvedimento della maschera è diversa da data di sistema e uguale o superiore a DataIrrevocabilità(TAB_Sentenza)
        if(   document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("dd")%> 
           || document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("MM")%> 
           || document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("yyyy")%>
          )
        {
          if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
          {
            alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
            return false;
          }
          else  if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
            if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
            {
              alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
              return false;
            }
            else if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
              if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value)
              {
                alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
                return false;
              }
        }
        
        //Controllo se data fine pena è minore di data emissione-->se sì fungibilità
        <%
        if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {%>
          // Inserito controllo su esistenza nella form del campo DATA_FINE_PENA
          if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%> != undefined)
          {
            if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value )
              document.LoadInserisciOrdineScarcerazione.fungibilita.value="S";
            else  if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value == document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value)
              if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value)
                document.LoadInserisciOrdineScarcerazione.fungibilita.value="S";
              else if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value == document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value)
                if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value)
                  document.LoadInserisciOrdineScarcerazione.fungibilita.value="S";
         }
        <%}%>


    }

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }


  </script>

</head>


<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
        <font class="campo">Emissione Ordine di Scarcerazione </font>
        <%}else{%>
        <font class="campo">Ordine di Scarcerazione </font>
        <%}%>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <FORM method="POST" name="LoadInserisciOrdineScarcerazione" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActInserisciOSFuturaMemoria">
    
    <%
    //==========================================================================
    // Posizione Giuridica e Luogo di detenzione
    //==========================================================================
    %>    
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
%>       DETENUTO PER ALTRA CAUSA  <%=altracausaposizionegiuridica.getDescrTipoPosGiuridica()%>
<%      }
        else
        {
          %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
       <%
        }
        %>
          </font>
        </td>
        
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
        <input type="HIDDEN" name="posizionegiuridica" value="<%=lPosizione.getDescrPosizioneGiuridica()%>">

      </tr>
      
      
      <%
        //======================================================================
        //
        //======================================================================
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
          if(lAltraCausa.getIstitutoDetenzione() != null)
          {
          %>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
           <%if (lAltraCausa.getAltroLuogo()!=null)
             { %>
            <tr>
             <td class="l">Altro Luogo </td >
             <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
             </td>
            </tr>
          <%  } %>
          <%}
        }

        if(lLuogoDetenzione.getIstitutoDetenzione() != null)
        {
          if(   !lLuogoDetenzione.getDescrTipoIstituto().equals("") 
             && lLuogoDetenzione.getDescrTipoIstituto()!= null
             && !lLuogoDetenzione.getDescrTipoIstituto().equals("-")
          )
          {%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%       }
       }
       else //Altro Luogo
       {
        if (lLuogoDetenzione.getAltroLuogo()!=null)
            {%>
           <tr>
             <td class="l">Detenuto presso Altro Luogo</td>
             <td class="L" colspan=5>
             <font class="campo"><%=lLuogoDetenzione.getAltroLuogo()%></font>
             </td>
          </Tr>
         <% }
       }
       
       // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
       if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
       {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {%>
            <tr>
              <td class="l">Indirizzo </td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
     <%    }
        }
%>
</table>


    <%
    //==========================================================================
    // Visualizzazione Pena da Eseguire (quantum decorrenza scadenza)
    //==========================================================================
    %>
    <table>
      <tr>
      <%
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if (   (penaresidua.getNumAnniReclusione() == null) 
            || (penaresidua.getNumMesiReclusione() == null)
            || (penaresidua.getNumGiorniReclusione() == null)
           )
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
    <%      if ((penaresidua.getNumAnniArresto() == null) || (penaresidua.getNumMesiArresto() == null)
      || (penaresidua.getNumGiorniArresto() == null))
     {%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
<%
      }
    }
%>

 <tr>
<%
 if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
 {
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
       }

      if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }
}

        if  ((penaresidua.getFlagErgastolo() == null) || ((penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("N"))))
        {
           if ( penaresidua.getDataFine() != null)
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
          }else
            {%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

 <%         }
        }
      }
%>
</tr>
<%
//==============================================================================
//
//==============================================================================
%>
<tr>
    <td class="l">Data Emissione</td>
    <td class="L" colspan=2 >
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
     <td class="l">Data Trasmissione</td>
    <td class="L" colspan=2>
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
</tr>
<tr>
  <input type="HIDDEN" title="Fungibilita" value="N" type="text" name="fungibilita">
  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloAssociato.getDataIrrevocabilita(), "dd") )%>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>">
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloAssociato.getDataIrrevocabilita(), "MM") )%>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>">
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloAssociato.getDataIrrevocabilita(), "yyyy") )%>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>">
</tr>

<%
  //============================================================================
  //                        Notifica agli Avvocati
  //============================================================================
  int lIdxAvv = 0;
  Iterator lItxAvv = avvocati.iterator();
  while(lItxAvv.hasNext())
  {
    AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>    </table>
        <table  width="60%">
          <tr>
            <td class="l" >Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>

<%
    lIdxAvv++;
  }
%>
<tr><td>&nbsp;</td></tr>
</table>

<%
//==============================================================================
//
//==============================================================================
%>
<table width="100%">
    <tr>
      <td class="Titolo" width="100%" colspan=6> Magistrato Firmatario </td>
    </tr>
    <tr>
      <td class="l"  width="30%">Magistrato Firmatario
      <td class="L" colspan=5>
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
           <a href="Javascript:ListaMagistrati('LoadInserisciOrdineScarcerazione');">
            <img src="/images/filefolder.gif" border=0>
           </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
    </tr>
    <tr>
      <td class="Titolo" width="100%" colspan=6> Destinatari</td>
    </tr>
    <tr>
      <td class="l" >Istituto di Detenzione <font class=ob>(*)</font></td>
      <%if(lLuogoDetenzione.getIstitutoDetenzione() == null) {%>
      <td class="l" colspan=5>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
      <%}else {%>
      <td class="l" colspan=5>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a></td>
      <%}%>
    </tr>
  </table>
  
  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOrdineScarcerazione");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

</script>
</body>
</html>