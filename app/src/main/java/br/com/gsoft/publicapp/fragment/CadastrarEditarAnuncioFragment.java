package br.com.gsoft.publicapp.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import br.com.gsoft.publicapp.R;
import br.com.gsoft.publicapp.activity.AnuncioActivity;
import br.com.gsoft.publicapp.domain.Anunciante;
import br.com.gsoft.publicapp.domain.Anuncio;
import br.com.gsoft.publicapp.rest.Response;
import br.com.gsoft.publicapp.rest.ResponseWithURL;
import br.com.gsoft.publicapp.service.AnuncioService;
import br.com.gsoft.publicapp.util.CameraUtil;

import static br.com.gsoft.publicapp.R.id.img;
import static br.com.gsoft.publicapp.R.id.nomeProduto;

public class CadastrarEditarAnuncioFragment extends BaseFragment implements AnuncioActivity.ClickHeaderListener {

    private AnuncioActivity activity; // ************************************************* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private CameraUtil camera = new CameraUtil(); // *************************************** !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    protected TextView tNomeProduto;
    protected TextView tPrecoProduto;
    private Anuncio anuncio;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lê os argumentos
        anuncio = Parcels.unwrap(getArguments().getParcelable("anuncio"));

        setHasOptionsMenu(true); // ********* Para que o botão de salvar na Action Bar apareça

        activity = (AnuncioActivity) getActivity();
        activity.setClickHeaderListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cadastrar_editar_anuncio, container, false);

        initViews(view);

        if (anuncio != null) {
            setAnuncio(anuncio);
        }

//        if (savedInstanceState != null) {
//            // Se girou a tela recupera o estado
//            camera.onCreate(savedInstanceState);
//        }

        return view;
    }

    protected void initViews(View view) {
        tNomeProduto = (TextView) view.findViewById(nomeProduto);
        tPrecoProduto = (TextView) view.findViewById(R.id.precoProduto);
    }

    private void setAnuncio(Anuncio a) {
        if (a != null) {
            tNomeProduto.setText(a.produto.nome);
            tPrecoProduto.setText(a.produto.preco.toString());
        }

//        // Imagem do Header na Toolbar
//        AnuncioActivity activity = (AnuncioActivity) getActivity();
//        activity.setAppBarInfo(a);
    }

    // Abrir a Câmera ao clicar na imagem do anúncio do produto
    @Override
    public void onHeaderClicked() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                // Recupera o Bitmap retornado pela câmera
                Bitmap bitmap = (Bitmap) bundle.get("data");
                // Atualiza a imagem na tela
                activity.setImage(bitmap);

                // Chame este método pra obter a URI da imagem
                Uri uri = getImageUri(bitmap);

                // Em seguida chame este método para obter o caminho do arquivo
                File file = new File(getRealPathFromURI(uri));

                activity.setFile(file);

            }
        }

    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes); // ************************************** PNG
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



    // *************** MÉTODO QUE ADICIONA O BOTÃO SALVAR NA TELA ***************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_save_anuncio, menu);
    }

    // *************** MÉTODO QUE TRATA DO CLIQUE NO BOTÃO DE SALVAR UM NOVO ANÚNCIO ****************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_salvar) {

            if (anuncio == null) {
                // Novo anúncio
                anuncio = new Anuncio();
                anuncio.dataCriacao = new Date();
                // ******************************************* !!!!!!!!!!!!!!!!!!!!!!
                anuncio.dataUltimaAtualizacao = new Date();
                Anunciante a = new Anunciante();
                a.id = 1L;
                anuncio.anunciante = a;
            }

            boolean formOk = validate(nomeProduto, R.id.precoProduto);

            if (formOk) {
                TextView nome = (TextView) getActivity().findViewById(nomeProduto);
                anuncio.produto.nome = nome.getText().toString();
                TextView preco = (TextView) getActivity().findViewById(R.id.precoProduto);
                anuncio.produto.preco = BigDecimal.valueOf(Double.parseDouble(preco.getText().toString()));
                startTask("salvar", taskSaveAnuncio());
            }

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    // ***************** MÉTODO PARA VALIDAR AS ENTRADAS DO USUÁRIO NO FORMULÁRIO DE CADASTRO ***************
    private boolean validate(int... textViewIds) {
        for (int id : textViewIds) {
            TextView t = (TextView) getActivity().findViewById(id);
            String s = t.getText().toString();
            if (s == null || s.trim().length() == 0) {
                t.setError(getString(R.string.msg_error_campo_obrigatorio));
                return false;
            }
        }
        return true;
    }


    private BaseTask taskSaveAnuncio() {
        return new BaseTask<Response>() {
            @Override
            public Response execute() throws Exception {
                // Faz upload da foto
                File file =  activity.getFile();
                if (file != null && file.exists()) {
                    ResponseWithURL response = AnuncioService.postFotoBase64(file);
                    if (response != null && response.isOk()) {
                        // Atualiza a foto do carro
                        anuncio.produto.urlFoto = response.getUrl();
                    }
                }
                // Salva o carro
                Response response = AnuncioService.saveAnuncio(anuncio);
                //Response response = Retrofit.getCarroREST().saveCarro(carro);
                return response;
            }

            @Override
            public void updateView(Response response) {
                super.updateView(response);
                if (response != null && "OK".equals(response.getStatus())) {
                    // Envia o evento para o bus
//                    PublicAppApplication.getInstance().getBus().post(new BusEvent.NovoAnuncioEvent());
                    // Fecha a tela
                    getActivity().finish();
                    toast("Anúncio salvo com sucesso!");
                    // ATUALIZAR A LISTA DOS ANÚNCIOS **********************************
                } else {
                    toast("Erro ao salvar o anúncio " + anuncio.produto.nome);
                }
            }
        };
    }

}
