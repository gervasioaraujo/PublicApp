package br.com.gsoft.publicapp.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDB extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    // Nome do banco
    public static final String NOME_BANCO = "projeto_precos.sqlite";
    private static final int VERSAO_BANCO = 1;

    public ProdutoDB(Context context) {
        // context, nome do banco, factory, versão
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a Tabela produto...");
        db.execSQL("create table if not exists produto (_id integer primary key autoincrement, nome text, preco real, categoria text, url_foto text);");
        Log.d(TAG, "Tabela produto criada com sucesso.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso mude a versão do banco de dados, podemos executar um SQL aqui
    }

    // Insere um novo produto, ou atualiza se já existe
    public long save(Produto produto) {
        long id = produto.id; // *********************
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nome", produto.nome);
            values.put("url_foto", produto.urlFoto);
            //values.put("preco", produto.getPreco());
            if (id != 0) {
                String _id = String.valueOf(produto.id);
                String[] whereArgs = new String[]{_id};
                // update carro set values = ... where _id=?
                int count = db.update("produto", values, "_id=?", whereArgs);
                return count;
            } else {
                // insert into carro values (...)
                id = db.insert("produto", "", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    // Deleta o carro
    public int delete(Produto produto) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // delete from carro where _id=?
            int count = db.delete("produto", "_id=?", new String[]{String.valueOf(produto.id)});
            Log.i(TAG, "Deletou [" + count + "] registro.");
            return count;
        } finally {
            db.close();
        }
    }

    // Deleta os carros do tipo fornecido
    public int deleteProdutos() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("produto", null, null);
            Log.i(TAG, "Deletou [" + count + "] registros");
            return count;
        } finally {
            db.close();
        }
    }

    // Consulta a lista com todos os produtos
    public List<Produto> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // select * from produto
            Cursor c = db.query("produto", null, null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    // Consulta o produto pelo tipo
//    public List<Produto> findAllByTipo(String tipo) {
//        SQLiteDatabase db = getWritableDatabase();
//        try {
//            // "select * from carro where tipo=?"
//            Cursor c = db.query("carro", null, "tipo = '" + tipo + "'", null, null, null, null);
//            return toList(c);
//        } finally {
//            db.close();
//        }
//    }

    // Lê o cursor e cria a lista de carros
    private List<Produto> toList(Cursor c) {
        List<Produto> produtos = new ArrayList<Produto>();
        if (c.moveToFirst()) {
            do {
                Produto produto = new Produto();
                produtos.add(produto);
                // recupera os atributos de produto
                produto.id = c.getLong(c.getColumnIndex("_id"));
                produto.nome = c.getString(c.getColumnIndex("nome"));
                //produto.setPreco(c.getFloat(c.getColumnIndex("preco")));
            } while (c.moveToNext());
        }
        return produtos;
    }

    // Executa um SQL
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    // Executa um SQL
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }

}
