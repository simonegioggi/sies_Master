<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ListIterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager" %>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenutoEsecuzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="collContenuto" scope="request" class="java.util.Vector"/>
<jsp:useBean id="oggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="posGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idLuogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloEsecuzione" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<%
Date dataFinePena = (Date)request.getAttribute("dataFinePena");
%>
<html>
  <FORM name="FormTestS22" >
<%
  // Prelevati i Contenuti UDS. Occorre caricare in oggetti HTML Hidden i LOW_VALUE
  // e gli HIGH_VALUE della collection per gestirli nella funzione JavaScript TestS22.
  ListIterator itx = collContenuto.listIterator();
  while ( itx.hasNext() )
  {
    DecodificheModel lContenuti = (DecodificheModel)itx.next();
%>
    <input type="HIDDEN" name='LowValue' value='<%=lContenuti.getCode()%>'>
    <input type="HIDDEN" name='HighValue' value='<%=lContenuti.getFiltro()%>'>
<%}%>
  </FORM>

  <head>
    <title>[S.I.E.S.] - Gestione Procedimenti SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
      var desktop;

      // Chiamata funzione lista Oggetti.
      function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
            aLink += "&formname="+a_formname;
            aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }

    </script>

    <script language="JavaScript">
      var resultS22;
      function TestS22(Code)
      {
        // Ripulisce i codici Oggetto, il TextBox con la descrizione degli oggetti e i campi S22.
        // STUB 21/04/2004 document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value='';
        // STUB 21/04/2004 document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>.value='';
        //document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.value='';
        //document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.value='';

        // Se Code = LowValue[i]!='U004' && HighValue[i]='S22' Occorre far visualizzare i Campi nascosti.
        if (typeof (Code) == "undefined")
          alert('selezionare il contenuto');
        else
        {
          // Il tag div S22 viene nascosto per default e visualizzato alla condizione HighValue = S22.
          S22_a.style.visibility='hidden';
          S22_aa.style.visibility='hidden'; // 25/07/2007
          S22_ems.style.visibility='hidden';
          S22_ab.style.visibility='hidden';
          S22_b.style.visibility='hidden';
          //alert('Code ='+Code);
          resultS22='';
          for (i = 0; i < document.FormTestS22.LowValue.length ; i++ )
          {
            if (Code == document.FormTestS22.LowValue[i].value)
            {
              // Imposta il Tipo Registro (da utilizzare in fase di inserimento).
              document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_REGISTRO%>.value=document.FormTestS22.HighValue[i].value;
              // STUB 30/03/2004 if(document.FormTestS22.LowValue[i].value != 'U004'   &&
                if (document.FormTestS22.HighValue[i].value =='S22')
              {
                //alert('Code ='+Code);
                //alert('LowValue[i].value ='+document.FormTestS22.LowValue[i].value);
                //alert('HighValue[i].value ='+document.FormTestS22.HighValue[i].value);
                // STUB 30/03/2004 resultS22='S22';
                if ( document.FormTestS22.LowValue[i].value == 'U004' )
                {
                  S22_ab.style.visibility='visible';
                  S22_ab.style.top='-20px';
                  S22_a.style.visibility='hidden';
                  S22_aa.style.visibility='hidden'; // 25/07/2007
                  S22_ems.style.visibility='hidden';
                  resultS22='U004';
                }
                else
                {
                  S22_a.style.visibility='visible';
                  S22_a.style.top='+4px';
                  S22_aa.style.visibility='hidden'; // 25/07/2007
                  S22_ems.style.visibility='hidden';
                  S22_ab.style.visibility='hidden';
                  resultS22='S22';
                }
                S22_b.style.visibility='visible';
                break;
              }
              // 25/07/2007 SANZIONI SOSTITUTIVE
              if (document.FormTestS22.HighValue[i].value =='S12')
              {
                if ( document.FormTestS22.LowValue[i].value == 'U019' )
                {
                  S22_ab.style.visibility='visible';
                  S22_ab.style.top='-20px';
                  S22_a.style.visibility='hidden';
                  S22_aa.style.visibility='hidden';
                  S22_ems.style.visibility='hidden';
                  resultS22='U019';
                }
                else
                {
                  S22_a.style.visibility='hidden';

                  S22_ab.style.visibility='hidden';
                  S22_aa.style.visibility='visible';
                  S22_ems.style.visibility='hidden';
                  resultS22='S12';
                }
                S22_b.style.visibility='visible';
                break;
              }
              // 29/04/2011 MISURE SICUREZZA
              if (document.FormTestS22.HighValue[i].value =='S09')
              {
                if ( document.FormTestS22.LowValue[i].value == 'U024' )
                {
                  S22_ab.style.visibility='visible';
                  S22_ab.style.top='-20px';
                  S22_a.style.visibility='hidden';
                  S22_aa.style.visibility='hidden';
                  S22_ems.style.visibility='hidden';
                  resultS22='U024';
                }
                else
                {
                  S22_a.style.visibility='hidden';

                  S22_ab.style.visibility='hidden';
                  S22_aa.style.visibility='hidden';
                  S22_ems.style.visibility='visible';
                  resultS22='S09';
                }
                S22_b.style.visibility='visible';
                break;
              }
              
            }
          }
        }
      }

      function Verify()
      {
        if (document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
        if (document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;

        if (document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value;
        if (document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value;

        // Controllo campo chiave anno.
        var anno_sistema='<%=DateUtils.getSysDate("yyyy")%>'
        var chiave_anno=document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.value;

        if(chiave_anno > anno_sistema)
        {
          alert("Il Campo Anno SIUS non può superare l'anno corrente");
          return false;
        }

        // STUB 01/12/2004 Controllo chiave anno minimo. STUB 16/04/2007 Anno minimo cambiato da 1995 a 1990.
        var anno_minimo='1990'
        if(chiave_anno < anno_minimo)
        {
          alert("Il Campo Anno SIUS non è valido");
          return false;
        }

        // Controllo obbligatorietà campi S22.
        //alert ('resultS22 = '+resultS22);
        var progrS22=document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.value;
        var annoS22=document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.value;
        //alert ('progrS22 = '+progrS22);
        //alert ('annoS22 = '+annoS22);

        if(resultS22=='S22' &&
            annoS22 =="" )
        {
          alert("Campo Anno del Procedimento di Esecuzione della misura alternativa Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }
        if(resultS22=='U004' &&
            annoS22 =="" )
        {
          alert("Campo Anno dell'ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }

        // 25/07/2007 Controlli Sanzioni Sostitutive
        if(resultS22=='S12' &&
            annoS22 =="" )
        {
          alert("Campo Anno del Procedimento di Esecuzione della Sanzione Sostitutiva Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }
        if(resultS22=='U019' &&
            annoS22 =="" )
        {
          alert("Campo Anno dell'Ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }
        
        // 29/04/2011 Controlli Misure Sicurezza
        if(resultS22=='S09' &&
            annoS22 =="" )
        {
          alert("Campo Anno del Procedimento di Esecuzione della Misura Sicurezza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }
        if(resultS22=='U024' &&
            annoS22 =="" )
        {
          alert("Campo Anno dell'Ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          return false;
        }

        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","numeric");

        if(resultS22=='S22' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo del Procedimento di Esecuzione della Misura Alternativa Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }
        if(resultS22=='U004' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }

        // 25/07/2007 Controlli Sanzioni Sostitutive
        if(resultS22=='S12' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo del Procedimento di Esecuzione della Sanzione Sostitutiva Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }
        if(resultS22=='U019' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }

        // 29/04/2011 Controlli Misure Sicurezza
        if(resultS22=='S09' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo del Procedimento di Esecuzione della Misura Sicurezza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }
        if(resultS22=='U024' &&
            progrS22 =="" )
        {
          alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          return false;
        }
        
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>","numeric");

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;

        if(tipoAtto =="-")
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if ( data_atto!='//')
        {
          if (! ControllaData(data_atto))
          {
            alert('Data atto non valida');
            return false;
          }
          // Controllo della data atto <= data di sistema
          if (! CompareDate(data_atto, data_sistema))
          {
            alert('Data atto > della data odierna');
            return false;
          }
        }

        // Controllo della data fine pena solo se valorizzata.
        var data_finepena=document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>.value;
        if ( data_finepena!='//')
        {
          if (! ControllaData(data_finepena))
          {
            alert('Data fine pena non valida');
            return false;
          }
          // Controllo della data fine pena => data di sistema
          // STUB 17/01/2005 aggiunta richiesta di proseguimento.
          if ( !CompareDate( data_sistema, data_finepena ))
          {
            if(! confirm("Data fine pena < Data odierna ! Si vuole continuare ?" ) )
              return false;
          }
        }

        // Controllo obbligatorietà contenuto.
        var contenuto=document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloUDSManuale.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;

        if(contenuto =="-")
        {
          alert("Il Campo Contenuto è obbligatorio");
          return false;
        }

        // Controllo della data arrivo solo se valorizzata.
        var data_arrivo=document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
        if ( data_arrivo!='//')
        {
          // Controllo di validità della data arrivo.
          if (! ControllaData(data_arrivo))
          {
            alert('Data di arrivo in cancelleria non valida');
            return false;
          }
          // Controllo della data arrivo <= data di sistema
          if (! CompareDate(data_arrivo, data_sistema))
          {
            alert('Data di arrivo > della data odierna');
            return false;
          }
          // Controllo della data atto <= data arrivo
          if ( data_atto!='//')
          {
            if (! CompareDate(data_atto, data_arrivo))
            {
              alert('Data atto > data arrivo in cancelleria');
              return false;
            }
          }
        }
      return true;
      }
    </script>

  </head>

  <body class="corpo" onload="Javascript:TestS22(document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value );">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        Date lDataIscrizione = new Date();

        String lAction = new String();

        FascicoloGPModel lFascicolo = new FascicoloGPModel();

        if( modalita.equals("IU") || modalita.equals("IE") )
        {
          lAction = "siap.sius.fascicolo.action.ActInserisciFascicoloUDSManuale";
%>
          <font class="campo">Iscrizione Procedimento manuale</font>
<%
        }
        // STUB 02/04/2004 La modalità IM corrisponde all'iscrizione delle EMA da soggetto e proviene da ActLoadInsFascicoloDaSoggettoUDS.
        else if( modalita.equals("IS") || modalita.equals("IM"))
        {
          lAction = "siap.sius.fascicolo.action.ActInsFascicoloDaSoggettoUDSManuale";
%>
          <font class="campo">Iscrizione Procedimento manuale da Soggetto</font>
<%
        }
%>
      </td>
    </tr>
  </table>

<%
  if( modalita.equals("IU") || modalita.equals("IE") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("IS") || modalita.equals("IM"))
  {
%>
    <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
<%
  }
%>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicoloUDSManuale" >

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Fine pena</font>
<%
        // La dataFinePena può essere valorizzata solo in alcuni casi con modalità = "IF"
        if( dataFinePena != null )
        {
%>
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly >
<%
        }
        else if( modalita.equals("IS") || modalita.equals("IM"))
        {
%>
          <input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%
        }
        else
        {
%>
          <input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly >
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly >
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly >
<%
        }
%>
        <font class="l">&nbsp;&nbsp;Pos. Giuridica </font>
<%
        if( modalita.equals("IS") || modalita.equals("IE"))
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>">
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=lFascicolo.getGeneraleProcedimentoModel().getCodPosGiuridica()%>" >
<%
        }
        // La Posizione Giuridica è prevalorizzata solo se modalità = "IF"
        else if( ! posGiuridica.equals("") )
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  disabled>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=posGiuridica%>"  >
<%
        }
        else
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  disabled>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="-">
<%
        }
%>
      </td>
    </tr>

<%
    // Gestione del luogo detenzione.
    if(!luogoDetenzione.equals("") )
    {
%>
      <tr>
        <td class="L">
          <font class="label">Detenuto in &nbsp;&nbsp;&nbsp;</font>
          <font class="campo"><%=luogoDetenzione%> &nbsp;&nbsp;&nbsp;&nbsp;</font>
          <input type=checkbox name="<%=ICostantiFascicoloSius.CAMPO_VALIDA_LUOGO_DET%>" value=1 title="Valida il Luogo Detenzione">
        </td>
      </tr>
<%
    }
%>

  </table>

  <br>

  <table cellspacing="2" cellpadding="2">

    <tr>
      <td class="lRosso">Anno/Progressivo SIUS <font class=ob>(*)</font></td>

      <td class="l">
        <input Title="Anno SIUS"  type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4">/
        <input Title="Numero SIUS" type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6">
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Atto <font class=ob>(*)</font></td>
      <td class="L">
        <select title="tipoAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>">
          <%= tipoAtto %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Data atto </td>
      <td class="L">
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Mittente </td>
      <td class="L">
        <select title="mittenteAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MITTENTE_ATTO%>">
          <%= mittenteAtto %>
        </select>
        &nbsp;&nbsp;
        <input Title="descrMittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50">
      </td>
    </tr>

    <tr>
      <td class="l">Sede Mittente </td>
      <td class="l">
        <input Title="Sede Mittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE%>"
        value="<%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrSedeMittente())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciFascicoloUDSManuale','<%= ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE %>');">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    <tr>
      <td class="l">Contenuto <font class=ob>(*)</font></td>
      <td class="L">
<%      if( modalita.equals("IE") || modalita.equals("IM"))
        {%>
        	<select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
          		<%= contenutoEsecuzione %>
          	</select>
      <%}else
        {%>
        	<select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>"
        			onchange="Javascript:TestS22(document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value );">
          		<%= contenuto %>
         	</select>
      <%}%>
      </td>
    </tr>

    <tr>
      <td class="l">Oggetto </td>
      <td class="l">
        <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly>
<%      //Nel caso di modifica devo ricaricare le variabili per la gestione degli oggetti(Tenore).

        String lCodOggetto="";
        String lCodDettagli="";
%>
        <%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()) %>
        </Textarea>
        <a href="Javascript:ListaOggetti('LoadInserisciFascicoloUDSManuale',document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
        <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
        &nbsp;
        <a href="Javascript:ListaOggetti('LoadInserisciFascicoloUDSManuale','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloUDSManuale.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
        <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
      </td>
    </tr>

    <%-- Tag div per gestione registro S22 La variabile VisS22 viene impostata a 'hidden'/'visible'--%>
    <tr>
      <td width=22%
<%      if( modalita.equals("IE") || modalita.equals("IM"))
          {%>class="lVerdeNB"<%}
        else{%>class="label"<%}
%>
      >
        <div id=S22_a style="visibility:visible; position:relative; " >
          Anno/Progressivo del Procedimento di Esecuzione della Misura Alternativa
          <font class=ob>(*)</font>
        </div>
	      <div id=S22_aa style="visibility:hidden; position:absolute; top:+256px;" >
  	      Anno/Progressivo del Procedimento di Esecuzione della Sanzione Sostitutiva
    	    <font class=ob>(*)</font>
      	</div>
	      <div id=S22_ems style="visibility:hidden; position:absolute; top:+256px;" >
  	      Anno/Progressivo del Procedimento di Esecuzione della Misura Sicurezza
    	    <font class=ob>(*)</font>
      	</div>
        <div id=S22_ab style="visibility:visible; position:relative; " >
          Anno/Numero Ordinanza
          <font class=ob>(*)</font>
        </div>
      </td>
      <td class="label">
        <div id=S22_b style="visibility='visible';">
          <input Title="Anno"  type="text" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22 %>" maxlength="4" size="4"
<%
            if( modalita.equals("IE") || modalita.equals("IM"))
            {%> value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveAnno() %>" READONLY
          <%}%>
          >/
          <input Title="Numero" type="text" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22 %>" maxlength="6" size="6"
<%
            if( modalita.equals("IE") || modalita.equals("IM"))
            {%> value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveProgr() %>" READONLY
          <%}%>
          >
        </div>
      </td>
    </tr>

    <tr>
      <td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
      <td class="L">
        <input  type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Magistrato </td>
      <td class="L" >
        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" >
          <%= magistrato %>
        </select>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="l">
        <Textarea Title="Note" name="<%= ICostantiFascicoloSius.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getAnnotazione()) %></textarea>
      </td>
    </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_SEDE_MITTENTE%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=lCodOggetto%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=lCodDettagli%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_REGISTRO%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.LUOGO_DETENZIONE%>" value="<%=luogoDetenzione%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicoloUDSManuale");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","req", "Il campo Anno Fascicolo è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>","req", "Il campo Progressivo Fascicolo è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>