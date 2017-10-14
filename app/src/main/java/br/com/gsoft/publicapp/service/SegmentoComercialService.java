package br.com.gsoft.publicapp.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.gsoft.publicapp.domain.Anunciante;
import br.com.gsoft.publicapp.domain.SegmentoComercial;
import livroandroid.lib.utils.HttpHelper;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by gervasio on 07/07/2017.
 */

public class SegmentoComercialService {

    private static final String URL = "http://192.168.1.10:8080/produtos/rest/segmentosComerciais";
//    private static final String URL = "http://publicapp.jelasticlw.com.br/rest/segmentosComerciais";

    public static List<String> getNomesSegmentosComerciais() throws IOException  {
        List<SegmentoComercial> segmentosComerciais = getSegmentosComerciais();
        List<String> nomes = new ArrayList<String>();
        for (SegmentoComercial s : segmentosComerciais) {
            nomes.add(s.nome);
        }
        return nomes;
    }

    public static List<SegmentoComercial> getSegmentosComerciais() throws IOException {
        List<SegmentoComercial> segmentosComerciais = getSegmentosComerciaisFromWebService();
        return segmentosComerciais;
    }

    private static List<SegmentoComercial> getSegmentosComerciaisFromWebService() throws IOException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(URL);
        List<SegmentoComercial> segmentosComerciais = parserJSON(json);
        return segmentosComerciais;
    }

    private static List<SegmentoComercial> parserJSON(String json) throws IOException {
        Type listType = new TypeToken<ArrayList<SegmentoComercial>>() {}.getType();
        List<SegmentoComercial> segmentosComerciais = new Gson().fromJson(json, listType);
        return segmentosComerciais;
    }



}
