<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="provvedimento"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="oggettodefinzione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="autorita" scope="request" class="java.util.ArrayList"/>

<%
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
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Definizione Procedimento - Pena Espiata</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
        //DATA EMISSIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();

          return false;
        }
        
        //DATA RICEZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();

          return false;
        }
        
        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Provvedimento è obbligatorio");
          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.selectedIndex].value == '-')
        {
          alert("Il Campo Tipo Provvedimento è obbligatorio");
          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto definizione è obbligatorio");
          return false;
        }
        
        if(document.f.<%= ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.f.<%= ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        return true;
      }

      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      // ** GESTIONE COMBO BOX **
      // Matrice di tante righe quanti sono i tipi provvedimento
      // e tante colonne quante sono le combo da gestire
      var lNumProvvedimento=<%=provvedimento.size()%>;
      var lNumCombo = 2;

      var lMatrice=new Array(lNumProvvedimento)
      for (i=0; i<lNumProvvedimento; i++)
        lMatrice[i]=new Array(lNumCombo);

<%
      //Autorita
      for(int i=0; i<provvedimento.size(); i++)
      {
        List lAut = (List)autorita.get(i);

        out.println("\n\n\tvar lAutorita"+i+"=new Array()\n");

        Iterator lIterAutorita = lAut.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterAutorita.hasNext())
        {
          lDec = (DecodificheModel) lIterAutorita.next();
          out.println("\tlAutorita"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCodiceAlternativo()+"\");");
          idx++;
    	  }
      }

      //Oggetto
      for(int i=0; i<provvedimento.size(); i++)
      {
        List lCont = (List)oggettodefinzione.get(i);

        out.println("\n\n\tvar lOggetto"+i+"=new Array()\n");

        Iterator lIterOggetto = lCont.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterOggetto.hasNext())
        {
          lDec = (DecodificheModel) lIterOggetto.next();
          out.println("\tlOggetto"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\");");
          idx++;
    	  }
      }
%>

      // La matrice contiene l'insieme delle opzioni selezionabili
      // strutturare in questo modo:
      // ci sono tante righe quante sono le opzioni della combo 'Provvedimento'
      // e tante colonne quante sono le combo da relazionare
      lMatrice[0][0] = lAutorita1;  // Contiene un array di oggetti Option
      lMatrice[0][1] = lOggetto1;   // Contiene un array di oggetti Option

      lMatrice[1][0] = lAutorita0;  // Contiene un array di oggetti Option
      lMatrice[1][1] = lOggetto0;   // Contiene un array di oggetti Option

      lMatrice[2][0] = lAutorita2;  // Contiene un array di oggetti Option
      lMatrice[2][1] = lOggetto2;   // Contiene un array di oggetti Option

      function initCombo()
      {
<%
        int lIndex = 0;
%>
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>.options[<%=lIndex%>].selected=true;

        caricamento();
      }

      function caricamento()
      {
        var idxSel = document.f.<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>.options.selectedIndex;

        loadCombo(document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>, idxSel, 0);
        loadCombo(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>, idxSel, 1);
      }

      function loadCombo(campo, riga, colonna)
      {
        for (m=campo.options.length-1;m>=0;m--)
          campo.options[m]=null;

        for (i=0;i<lMatrice[riga][colonna].length;i++)
        {
          campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value)
        }

        campo.options[0].selected=true;

      }


    function ListaDocumenti(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.archiviazione.action.ActListaDocumentiArchiviazione&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>+", "Lista_Provvedimenti", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

  </script>
  </head>
  <body class="corpo" onLoad="initCombo();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Provvedimento Altra Autorità</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciProvvAltraAutorita">
    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }
%>

  <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }


       if ( penaresidua.getFlagErgastolo() != null)
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

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {

      if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                </td>
<%            }
        }
      }
}
%>
</tr>


  </table>
  <br>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumenti('f');">
          Seleziona provvedimenti dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>   
    <tr>
      <td class="l" width="20%">Provvedimento emesso da<font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <select Title="Provvedimento" name="<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>" onChange="caricamento();">
<%
          Iterator lIter = provvedimento.iterator();
          while(lIter.hasNext())
          {
            DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
            <option value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%
          }
%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Data ricezione provvedimento</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data emissione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Anno /Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento" value="" name="<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>" type="text" size="6" maxlength="6">
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
        <td class="l" colspan=3>
          <select Title="Tipo Provvedimento" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>">
            <%=tipoprovvedimento%>
          </select>
        </td>
    </tr>


    <tr>
      <td class="l" >Autorità Emittente <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Autorità Emittente" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
           </select>
      </td>

      <td class="l" colspan=2>Sede  &nbsp;
          <input title="Sede Autorita"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr>


    <tr>
      <td class="l">Data Definizione <font class=ob>(*)</font></td>
      <td class="l" colspan=3>
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Oggetto Definizione <font class=ob>(*)</font></td>
        <td class="l" colspan=3>
          <select Title="Oggetto Definzione" class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" >
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr>
       <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L" colspan=3>
          <textarea title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=4 ></textarea>
       </td>
     </tr>
</table>
<table>
    <tr>
      <td class="lNoBord">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>

</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>","lt=2050");

//data emissione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");

//data ricezione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","maxlen=4","La lunghezza massima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","minlen=4","La lunghezza minima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","numeric");

//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>