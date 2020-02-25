<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<jsp:useBean id="posizione"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione" scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="decretoordinanza" scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoprovvedimento" scope="request" class="java.lang.String"/>



<jsp:useBean id="autoritaemittente" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contenutodecisione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="oggettodecisione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="tipologiadecisione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<jsp:useBean id="flagDecisioniSorveglianza" scope="request" class="java.lang.String"/>


<%@ page import="java.util.Collection"%>

<% 
Collection tiporegistroordinanza =(Collection) request.getAttribute("tiporegistroordinanza");
%>

<%
  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sospensione dell'esecuzione della pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      // ** GESTIONE COMBO BOX **
      // Matrice di tante righe quanti sono i tipi registro
      // e tante colonne quante sono le combo da gestire
      var lNumTipoReg=<%=tiporegistroordinanza.size()%>;
      var lNumCombo = 3;

      var lMatrice=new Array(lNumTipoReg)
      for (i=0; i<lNumTipoReg; i++)
        lMatrice[i]=new Array(lNumCombo);
<%
      //Autorita
      for(int i=0; i<tiporegistroordinanza.size(); i++)
      {
        List lAut = (List)autoritaemittente.get(i);

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

      //Contenuto
      for(int i=0; i<tiporegistroordinanza.size(); i++)
      {
        List lCont = (List)contenutodecisione.get(i);

        out.println("\n\n\tvar lContenuto"+i+"=new Array()\n");

        Iterator lIterContenuto = lCont.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterContenuto.hasNext())
        {
          lDec = (DecodificheModel) lIterContenuto.next();
          out.println("\tlContenuto"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getFiltro()+"\");");
          idx++;
    	  }
      }

      //Oggetto
      for(int i=0; i<tiporegistroordinanza.size(); i++)
      {
        List lOgg = (List)oggettodecisione.get(i);

        out.println("\n\n\tvar lOggetto"+i+"=new Array()\n");

        Iterator lIterOggetto = lOgg.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterOggetto.hasNext())
        {
          lDec = (DecodificheModel) lIterOggetto.next();
          out.println("\tlOggetto"+i+"["+idx+"]= new OggettoDecode(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\", \""+lDec.getFiltro()+"\");");
          idx++;
    	  }
      }

      //Tipologia
 // modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine della pena
 // delle decisioni della sorveglianza da qualle del GE
     // for(int i=0; i<(tiporegistroordinanza.size()-1); i++)
       for(int i=0; i<(tiporegistroordinanza.size()); i++)
      {
        List lTipo = (List)tipologiadecisione.get(i);

        out.println("\n\n\tvar lTipologia"+i+"=new Array()\n");

        Iterator lIterTipologia = lTipo.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterTipologia.hasNext())
        {
          lDec = (DecodificheModel) lIterTipologia.next();
          out.println("\tlTipologia"+i+"["+idx+"]= new OggettoDecode(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\", \""+lDec.getFiltro()+"\");");
          idx++;
    	  }
      }
%>
      // La matrice contiene l'insieme delle opzioni selezionabili
      // strutturare in questo modo:
      // ci sono tante righe quante sono le opzioni della combo 'Registro'
      // e tante colonne quante sono le combo da relazionare
      lMatrice[0][0] = lAutorita0;  // Contiene un array di oggetti Option
      lMatrice[0][1] = lContenuto0; // Contiene un array di oggetti Option
      lMatrice[0][2] = lOggetto0;   // Contiene un array di oggetti OggettoDecode
      lMatrice[0][3] = lTipologia0; // Contiene un array di oggetti OggettoDecode

      lMatrice[1][0] = lAutorita1;  // Contiene un array di oggetti Option
      lMatrice[1][1] = lContenuto1; // Contiene un array di oggetti Option
      lMatrice[1][2] = lOggetto1;   // Contiene un array di oggetti OggettoDecode
      lMatrice[1][3] = lTipologia1; // Contiene un array di oggetti OggettoDecode

 // modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine della pena
 // delle decisioni della sorveglianza da qualle del GE
     /* lMatrice[2][0] = lAutorita2;  // Contiene un array di oggetti Option
      lMatrice[2][1] = lContenuto2; // Contiene un array di oggetti Option
      lMatrice[2][2] = lOggetto2;   // Contiene un array di oggetti OggettoDecode*/

      function initCombo()
      {
<%
        // Fare attenzione all'ordinamento all'interno della matrice
        // mi aspetto nell'ordine '0003', '0002', '0001'

        /*
         modifica 14-02-06 -- svolta da Dario -- commisionata da Viviana sul CAMPO -->'Registro'
         SE presente il 'flagDecisioniSorveglianza' uguale ad 'S' significa che
           PROVENIAMO dal menù '-->Decisioni Sorveglianza' e quindi il default è 'SIUS'(lIndex = 2)
         ALTRIMENTI PROVENIAMO dal menù '-->Decisioni GE' ed il il default è 'RPE'(lIndex = 1).
         Se presente decretoordinanza -->già era così<-- la combo viene carica di conseguenza!!!
        */

        int lIndex = 1;
 // modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine della pena
 // delle decisioni della sorveglianza da qualle del GE
       /* if("S".equals(flagDecisioniSorveglianza))
         {
           lIndex = 2;
         }*/

        if( decretoordinanza.getCodTipoRegistroOrdinanza() != null
          && decretoordinanza.getCodTipoRegistroOrdinanza().equals("0003") )
        {
          lIndex = 0;
        }
				/* STUB 14/12/2005
        else if(decretoordinanza.getCodTipoRegistroOrdinanza() != null
          && decretoordinanza.getCodTipoRegistroOrdinanza().equals("0002"))
        {
          lIndex = 1;
        } */
 // modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine della pena
 // delle decisioni della sorveglianza da qualle del GE
        /*else if(decretoordinanza.getCodTipoRegistroOrdinanza() != null
          && decretoordinanza.getCodTipoRegistroOrdinanza().equals("0001"))
        {
          lIndex = 2;
        }*/
%>
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options[<%=lIndex%>].selected=true;

        caricamento();
      }

      function caricamento()
      {
        var idxSel = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;

        loadCombo(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>, idxSel, 0);
        loadCombo(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>, idxSel, 1);

        caricamentoComboOggetto();

        //Gestione Div 'Tipologia Decisione'
        var idxSel = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;
        var nodeTitoloTipologia = document.getElementById('divtitolotipologia');
        var nodeTipologia = document.getElementById('divtipologia');
 // modifica del 27-06-06 -- dario -- viviana -- a seguito della divisione della maschera di sospensine della pena
 // delle decisioni della sorveglianza da qualle del GE
       /* if(idxSel == 2)
        {
          nodeTitoloTipologia.style.visibility='hidden';
          nodeTipologia.style.visibility='hidden';
        }
        else
        {*/
          nodeTitoloTipologia.style.visibility='visible';
          nodeTipologia.style.visibility='visible';
       // }

        caricamentoComboTipologia();
      }

      function caricamentoComboOggetto()
      {
        var idxRiga = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;

        loadComboOggettoDecisione(idxRiga, 2);

        caricamentoComboTipologia();
      }

      function caricamentoComboTipologia()
      {
        var idxRiga = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;

        loadComboTipologiaDecisione(idxRiga, 3);
      }

      function loadCombo(campo, riga, colonna)
      {
        for (m=campo.options.length-1;m>=0;m--)
          campo.options[m]=null;

        for (i=0;i<lMatrice[riga][colonna].length;i++)
        {
          campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value)
        }

        //campo.options[0].selected=true;
        selectCombo(campo);
      }

      // La funzione considera solo le opzioni in funzione della selezione della
      // combo 'Contenuto Decisione'
      function loadComboOggettoDecisione(riga, colonna)
      {
        var campo = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>;
        var campoRiferimento = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>;

        var j = 0;

        for (m=campo.options.length-1;m>=0;m--)
          campo.options[m]=null;

        for (i=0;i<lMatrice[riga][colonna].length;i++)
        {
          var valueSel = campoRiferimento[campoRiferimento.options.selectedIndex].value;

          if(valueSel == lMatrice[riga][colonna][i].CodiceAlternativo)
          {
            campo.options[j]=new Option(lMatrice[riga][colonna][i].OptionOggetto.text,lMatrice[riga][colonna][i].OptionOggetto.value)
            j++;
          }
        }

        selectCombo(campo);
      }

      // La funzione considera solo le opzioni in funzione della selezione della
      // combo 'Tipologia Decisione'
      function loadComboTipologiaDecisione(riga, colonna)
      {
        var campo = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>;
        var campoRiferimento = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>;

        var j = 0;

        for (m=campo.options.length-1;m>=0;m--)
          campo.options[m]=null;

        if(riga == 2)
          return;

        for (i=0;i<lMatrice[riga][colonna].length;i++)
        {
          var valueSel = campoRiferimento[campoRiferimento.options.selectedIndex].value;

          if(valueSel == lMatrice[riga][colonna][i].CodiceAlternativo)
          {
            campo.options[j]=new Option(lMatrice[riga][colonna][i].OptionOggetto.text,lMatrice[riga][colonna][i].OptionOggetto.value)
            j++;
          }
        }

        selectCombo(campo);
      }

      function selectCombo(campo, valore)
      {
<%
        if( flagdecretoordinanza.equals("S") )
        {
%>
          //alert("campo: "+campo)

          var valore = '';
          if(campo==document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>)
            valore = '<%=StringUtils.toStringJSP( decretoordinanza.getCodTipoAutoritaEmittente() )%>';
          if(campo==document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>)
            valore = '<%=StringUtils.toStringJSP( decretoordinanza.getCodOggettoDecisione() )%>';
          if(campo==document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>)
            valore = '<%=StringUtils.toStringJSP( decretoordinanza.getCodOggettoProcedimento() )%>';
          if(campo==document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>)
            valore = '<%=StringUtils.toStringJSP( decretoordinanza.getCodEsito() )%>';

          //alert("valore: "+valore)

          for (m=0;m<campo.options.length;m++)
          {
            if(campo.options[m].value == valore)
            {
              campo.options[m].selected=true;
              return;
            }
          }
<%
        }
        else
        {
%>
          campo.options[0].selected=true;
<%
        }
%>
      }

      // Oggetto Javascript che rappresenta i Dati della CG_REF_CODES
      // utili ala caricamento dinamico delle combo 'Oggetto Decisione' e 'Tipologia Decisione'
      function OggettoDecode( Descrizione, Codice, CodiceAlternativo)
      {
        this.OptionOggetto = new Option(Descrizione, Codice);
        this.CodiceAlternativo = CodiceAlternativo;
      }

      function Verify()
      {
        //DATA RICEZIONE PROVVEDIMENTO (non obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data emissione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

<%
      //Solo se Posizione != 46
      if(  posizione.getCodPosizioneGiuridica()!=null
        && !posizione.getCodPosizioneGiuridica().equals("46"))
      {
%>
        //DATA SOSPENSIONE ESECUZIONE (obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data sospensione esecuzione non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE %>.focus();

          return false;
        }
<%
      //Solo se Posizione != libero
      if( !posizione.isLibero() )
      {
%>
   var giorno_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"dd")%>;
   var mese_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"MM")%>;
   var anno_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"yyyy")%>;

    if(giorno_data_decorrenza_pena < '10')
   {
      giorno_data_decorrenza_pena ='0'+giorno_data_decorrenza_pena;
   }
    if(mese_data_decorrenza_pena < '10')
  {
      mese_data_decorrenza_pena ='0'+mese_data_decorrenza_pena;
   }

   var giorno_data_fine_pena = <%=DateUtils.getDateToString(penaresidua.getDataFine(),"dd")%>;
   var mese_data_fine_pena = <%=DateUtils.getDateToString(penaresidua.getDataFine(),"MM")%>;
   var anno_data_fine_pena = <%=DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy")%>;

    if(giorno_data_fine_pena < '10')
   {
      giorno_data_fine_pena ='0'+giorno_data_fine_pena;
   }
    if(mese_data_fine_pena < '10')
  {
      mese_data_fine_pena ='0'+mese_data_fine_pena;
  }



  var data_decorrenza_pena = giorno_data_decorrenza_pena+'/'+mese_data_decorrenza_pena+'/'+anno_data_decorrenza_pena

  var data_fine_pena = giorno_data_fine_pena+'/'+mese_data_fine_pena+'/'+anno_data_fine_pena

  if(!CompareDate(data_decorrenza_pena,data_to_verify))
  {
          alert("Data Sospensione deve essere superiore o uguale alla data di Inizio Pena");
          return false;

    }
  if(!CompareDate(data_to_verify,data_fine_pena))
  {
          alert("Data Sospensione deve essere minore o uguale alla data di Fine Pena");
          return false;

    }
<%
  }

}
%>
        // Non è possibile specificare solo il numero o solo l'anno per il registro
        if( (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length != 0)
          && (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length == 0) )
        {
          alert("Valorizzare Anno Registro");
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO %>.focus();

          return false;
        }

        if( (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length == 0)
        && (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length != 0) )
        {
          alert("Valorizzare Numero Registro");
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO %>.focus();

          return false;
        }

        // Non è possibile specificare solo il numero o solo l'anno per il provvedimento
        if( (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length != 0)
          && (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length == 0) )
        {
          alert("Valorizzare Anno Provvedimento");
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO %>.focus();

          return false;
        }

        if( (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length == 0)
        && (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length != 0) )
        {
          alert("Valorizzare Numero Provvedimento");
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO %>.focus();

          return false;
        }


    return true;
      }
    </script>
  </head>
  <body class="corpo" onLoad="initCombo();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Sospensione dell'esecuzione della pena</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensione">
    <input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="idPenaResidua" value="<%=penaresidua.getIdPenaResidua()%>">
    <input type="HIDDEN" name="codPosGiu" value="<%=posizione.getCodPosizioneGiuridica()%>">



<%
    String lIdDecretoOrdinanza = "";
    if(flagdecretoordinanza.equals("S"))
    {
      lIdDecretoOrdinanza = "" + decretoordinanza.getIdDecretoOrdinanzaSiep();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=lIdDecretoOrdinanza%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=8>
        <font class="campo">
          <%=StringUtils.toStringJSP(posizione.getDescrPosizioneGiuridica())%>
        </font>
      </td>
    </tr>
<%
  if( posizione.getCodPosizioneGiuridica()!=null
    && !posizione.getCodPosizioneGiuridica().equals("46"))
  {
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

    if( (!posizione.getCodPosizioneGiuridica().equals("07")
      && !posizione.getCodPosizioneGiuridica().equals("10") &&
       (!posizione.getCodPosizioneGiuridica().equals("16") && !posizione.getCodPosizioneGiuridica().equals("17")
        && !posizione.getCodPosizioneGiuridica().equals("46") && !posizione.getCodPosizioneGiuridica().equals("47"))) )
    {
%>
        <tr>
<%
          if(   flagergastolo.equals("N")
             && penaresidua.getDataInizio() != null )
          {
%>
            <td class="l">Data Decorrenza Pena</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
<%
          }

/*
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFinePresunta() != null)
        {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Data Fine Pena Automatica</td>
<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
--%>
<%
/*
       }
*/
         if (  flagergastolo.equals("N")
            && penaresidua.getDataFine()!=null
            )
         {
           String lClassTd="l";
           String lClassFont="campo";
           if(penaresidua.getDataFine() != null
             && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
           {
             lClassTd="lRosso";
             lClassFont="lRosso";
           }
%>
             <td class="l">Data Fine Pena</td>
             <td class="<%=lClassTd%>">
               <font class="<%=lClassFont%>">
                 <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%>&nbsp;
               </font>
             </td>
<%
         }
%>
          </tr>
<%
    }
  }
  else //POSIZIONE = 46 (Libero in sospensione)
  {
    if ( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaEspiata()) != 0 ||
         (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaEspiata()) != 0 ||
         (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaEspiata()) != 0
        )
    {
%>
      <tr>
        <td class="l">Pena Espiata</td>
        <td class="l" colspan=2>
          <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
        </td>
        <td class="l">Multa</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toEuroFormat(sospensione.getMultaEspiata())%>
          </font>&nbsp;
          <font class="l">Euro</font>
        </td>
        <td class="l">Ammenda</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toEuroFormat(sospensione.getAmmendaEspiata())%>
          </font>&nbsp;
          <font class="l">Euro</font>
        </td>
      </tr>
<%
    }

    if(flagergastolo.equals("N"))
    {
%>
      <tr>
        <td colspan=9 class="titolo">Pena Residua</td>
      </tr>
<%
      if ( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaResiduaReclus()) != 0 ||
           (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaResiduaReclus()) != 0 ||
           (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaResiduaReclus()) != 0
          )
      {
%>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2>
            <font class="campo">
              <%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%>
            </font>&nbsp;
            <font class="l">Euro</font>
          </td>
        </tr>
<%
      }

      if( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaResiduaArres()) != 0 ||
          (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaResiduaArres()) != 0 ||
          (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaResiduaArres()) != 0
         )
      {
%>
        <tr>
          <td class="l">Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2>
            <font class="campo">
              <%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%>
            </font>&nbsp;
            <font class="l">Euro</font>
          </td>
        </tr>
<%
      }
    }
%>
    <tr>
      <td class="l">Data Inizio</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
<%
  }

  if (  flagergastolo.equals("S") )
  {
%>
    <tr>
      <td class="l">Pena Complessiva</td>
      <td class="L">
        <font class="campo">ERGASTOLO&nbsp;</font>
      </td>
    </tr>
<%
    }else if(flagergastolo.equals("D"))
     {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
     }
%>
  </table>
  <br>
  <table style="width: 95%;">
    <tr>
      <td colspan=3 class="titolo">Dati del provvedimento di sospensione dell'esecuzione</td>
    </tr>
    <tr>
      <td class="l">
        Data ricezione provvedimento
        &nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;
        <input type="text" Title="Giorno ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Anno ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
    </tr>
    <tr>
      <td class="l">
        Anno/Numero registro
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="text" Title="Anno Registro" value="<%=StringUtils.toStringJSP( decretoordinanza.getAnnoRegistro() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>" size=4 maxlength=4>
        /
        <input type="text" Title="Numero Registro" value="<%=StringUtils.toStringJSP( decretoordinanza.getNumRegistro() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>" size=6 maxlength=6>
        &nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;
        Registro <font class="ob">(*)</font>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <select Title="Registro" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>" onChange="caricamento();">
<%
          Iterator lIter = tiporegistroordinanza.iterator();
          while(lIter.hasNext())
          {
            DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
            <option value="<%=lDecMod.getCodiceAlternativo()%>"/><%=lDecMod.getCode()%>
<%
          }
%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">
      Data emissione provvedimento <font class="ob">(*)</font>&nbsp;
      <input type="text" Title="Giorno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "dd") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Mese emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "MM") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Anno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "yyyy") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      &nbsp;&nbsp;&nbsp;&nbsp;
      Anno/Numero provvedimento
      <input type="text" Title="Anno Provvedimento" value="<%=StringUtils.toStringJSP( decretoordinanza.getAnnoProvvedimento() )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO %>" size=4 maxlength=4>
      /
      <input type="text" Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP( decretoordinanza.getNumProvvedimento() )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO %>" size=6 maxlength=6>&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">
      Tipo provvedimento <font class="ob">(*)</font>
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;
      <select Title="Tipo Provvedimento" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
        <%=tipoprovvedimento%>
      </select>
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td class="l">
      Autorità emittente <font class="ob">(*)</font>
    </td>
    <td class="l">
      <select Title="Autorità emittente" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
      </select>
        Sede&nbsp;<font class="ob">(*)</font>
        <input title="Sede Autorita Esterna"  type="text" value="<%=StringUtils.toStringJSP( decretoordinanza.getDescrLuogoEmittente() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>
      Contenuto decisione <font class="ob">(*)</font>
    </td>
    <td class="l">
      <select Title="Contenuto Decisione" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>" onChange="caricamentoComboOggetto();">
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>
      Oggetto decisione <font class="ob">(*)</font>
    </td>
    <td class="l">
      <select Title="Oggetto Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>">
      </select>
    </td>
  </tr>
  <tr>
      <td class="l" nowrap>
        <div id="divtitolotipologia" style="visibility:hidden; position:relative;">
          Tipologia decisione <font class="ob">(*)</font>
        </div>
      </td>
      <td class="l">
        <div id="divtipologia" style="visibility:hidden; position:relative;">
          <select Title="Tiipologia Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>">
          </select>
        </div>
      </td>
  </tr>
  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l">
      <textarea cols="60" rows="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP( decretoordinanza.getMotivazioni() )%></textarea>
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
<%
 /*     if(  posizione.getCodPosizioneGiuridica()!=null
        && !posizione.getCodPosizioneGiuridica().equals("46") && !posizione.getCodPosizioneGiuridica().equals("47"))
      {*/
%>
      <td class="l">
        Data sospensione esecuzione <font class="ob">(*)</font>
        &nbsp;&nbsp;&nbsp;
        <input type="text" Title="Giorno sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Anno sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
        &nbsp;&nbsp;&nbsp;
<%
        String lCheckScarcerato = "";
        String lCheckDaScarcerare = "";
        if(  decretoordinanza.getFlagScarcerareScarcerato() != null
          && decretoordinanza.getFlagScarcerareScarcerato().equals("D") )
        {
          lCheckDaScarcerare = "checked";
        }
        else if(decretoordinanza.getFlagScarcerareScarcerato() != null
          && decretoordinanza.getFlagScarcerareScarcerato().equals("S") )
        {
          lCheckScarcerato = "checked";
        }
        else
        {
          lCheckDaScarcerare = "checked";
        }
%>
        <input type="radio" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_SCARCERARE_SCARCERATO%>" value="D" <%=lCheckDaScarcerare%>>Da scarcerare
        &nbsp;&nbsp;
        <input type="radio" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_SCARCERARE_SCARCERATO%>" value="S" <%=lCheckScarcerato%>>Già scarcerato
      </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- <% --%>
<!--      } -->
<!--       else -->
<!--       { -->
<%-- %> --%>
        <%--td class="l" width="19%">
          Data sospensione esecuzione
        </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
            &nbsp;
<%-- <% --%>
<!--         out.print("[Già scarcerato]"); -->
<%-- %> --%>
<%--
          <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>">
          <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>">
          <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>">
          <!-- In questo caso il CAMPO_FLAG_SCARCERARE_SCARCERATO è per default a 'Già Scarcerato' -->
          <input type="HIDDEN" value="S" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_SCARCERARE_SCARCERATO%>">
        </font>
      </td>
<%-- <% --%>
<!-- } -->
<%-- %> --%>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
	
	// Data ricezione provvedimento
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=31");
	
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=12");
	
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=3000");
	
	// Sede
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
	
	// Anno registro
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","maxlen=4","La lunghezza massima per l'anno registro è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","minlen=4","La lunghezza minima per l'anno registro è di 4 caratteri");
	// Numero registro
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>","numeric");
	
	// Anno provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno provvedimento è di 4 caratteri");
	
	// Numero provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>","numeric");
	
	// Data emissione provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Giorno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=31")
	
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Mese emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=12");
	
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Anno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

<%
    if(  posizione.getCodPosizioneGiuridica()!=null
      && !posizione.getCodPosizioneGiuridica().equals("46"))
    {
%>
		// Data sospensione Esecuzione
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Giorno sospensione esecuzione è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","lt=31");
		
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Mese sospensione esecuzione è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","lt=12");
		
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Anno sospensione esecuzione è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","maxlen=4","La lunghezza massima per l'anno sospensione esecuzione è di 4 caratteri");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","minlen=4","La lunghezza minima per l'anno sospensione esecuzione è di 4 caratteri");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1900");
		frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","lt=3000");
<%
    }
%>
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>