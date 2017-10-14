package br.com.gsoft.publicapp.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.gsoft.publicapp.domain.Anunciante;
import br.com.gsoft.publicapp.domain.Anuncio;
import livroandroid.lib.utils.HttpHelper;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by gervasio on 07/07/2017.
 */

public class AnuncianteService {

    private static final String URL = "http://192.168.1.10:8080/produtos/rest/anunciantes";
    private static final String URL_BY_NOME = "http://192.168.1.10:8080/produtos/rest/anunciantes/nome";
//    private static final String URL = "http://publicapp.jelasticlw.com.br/rest/anunciantes";

    // método chamado na classe ...
    public static List<Anunciante> getAnunciantes() throws IOException {
        List<Anunciante> anunciantesTodos = getAnunciantesFromWebService();
        return anunciantesTodos;
    }

    public static List<Anunciante> getAnunciantesFromWebService() throws IOException {
        HttpHelper http = new HttpHelper();
        String json = http.doGet(URL);
//        Log.d("JSON", json);
        List<Anunciante> anunciantes = parserJSON(json);
        return anunciantes;
    }

    public static List<Anunciante> getAnunciantesByNome(String query) throws IOException {

        String url = URL_BY_NOME + "/" + query;
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);
        List<Anunciante> anunciantes = parserJSON(json);
        return anunciantes;

    }

    private static List<Anunciante> parserJSON(String json) throws IOException {
        Type listType = new TypeToken<ArrayList<Anunciante>>() {}.getType();
        List<Anunciante> anunciantes = new Gson().fromJson(json, listType);
        return anunciantes;
    }

}
